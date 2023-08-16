package com.example.mirai

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.console.util.*
import java.io.IOException
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent

public object ExampleAutoLoadPluginConfig : ReadOnlyPluginConfig(saveName = "example_auto_load") {

    @ValueName("name")
    public val name: String by value("")

    @OptIn(ConsoleExperimentalApi::class)
    override fun onInit(owner: PluginDataHolder, storage: PluginDataStorage) {
        // 判断是在插件中加载
        val plugin = owner as? JvmPlugin ?: return
        // 判断是第一次加载
        if (plugin.isEnabled) return

        // 生产流
        val shared = MutableSharedFlow<Pair<Path, WatchEvent.Kind<*>>>()

        // 处理内容变更
        plugin.launch(CoroutineName(name = "Reload(config=${saveName})")) {
            // 取样 5000ms, 避免过度触发 load
            @OptIn(FlowPreview::class)
            shared.sample(periodMillis = 5_000).collect { (path, kind) ->
                plugin.logger.debug("[${kind.name()}] ${path.toUri()}")
                launch {
                    // 等待 3000ms, 避免过早触发 load
                    delay(timeMillis = 3_000)
                    with(plugin) {
                        logger.info("[${kind.name()}] auto load config $saveName")
                        loader.configStorage.load(holder = owner, instance = this@ExampleAutoLoadPluginConfig)
                    }
                }
            }
        }

        // 监视内容变更
        plugin.launch(CoroutineName(name = "Watch(config=${saveName})")) {
            @Suppress("USELESS_IS_CHECK")
            val folder = when (this@ExampleAutoLoadPluginConfig) {
                is ReadOnlyPluginConfig -> plugin.configFolderPath
                is ReadOnlyPluginData -> plugin.dataFolderPath
                else -> return@launch
            }
            // 创建监听器
            val watcher = try {
                runInterruptible(Dispatchers.IO) {
                    folder.fileSystem.newWatchService()
                }
            } catch (cause: IOException) {
                plugin.logger.warning("正则词库文件监视器创建失败", cause)
                return@launch
            }
            // 注册监听器
            runInterruptible(Dispatchers.IO) {
                folder.register(
                    watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE
                )
            }
            plugin.logger.info("watch config $saveName")

            // 循环获取变更
            while (isActive) {
                val key = runInterruptible(Dispatchers.IO, watcher::take)
                for (event in key.pollEvents()) {
                    val path = event.context() as? Path ?: continue
                    if (path.toString() != "${saveName}.${saveType.extension}") continue
                    val file = folder.resolve(path)

                    shared.emit(value = file to event.kind())
                }
                key.reset()
            }
        }
    }
}