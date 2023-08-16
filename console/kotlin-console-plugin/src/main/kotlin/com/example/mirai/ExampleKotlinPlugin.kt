package com.example.mirai

import com.example.mirai.command.ExampleCompositeCommand
import com.example.mirai.command.ExampleSimpleCommand
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.*
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl

@PublishedApi
internal object ExampleKotlinPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "com.example.mirai.kotlin-console-plugin",
        name = "kotlin-console-plugin",
        version = "0.0.0"
    ) {
        author("cssxsh")
    }
) {
    override fun PluginComponentStorage.onLoad() {
        logger.info("load, 用于资源加载和注册服务等操作，一般插件不会用到，比如公共服务插件注册服务")
    }

    override fun onEnable() {
        logger.info("enable, 用于指令注册，数据载入等操作")

        ExampleAutoLoadPluginConfig.reload()

        ExampleSimpleCommand.register()
        ExampleCompositeCommand.unregister()

        ExampleSimpleListenerHost.registerTo(globalEventChannel())

        // 监听消息
        globalEventChannel().subscribeMessages {
            // 收到 ???
            "???" {
                logger.info("有人 ???")
            }

            // 收到 114514 就回复 1919810
            "114514" reply {
                "1919810"
            }

            // 收到 的消息里有图片
            has<Image> { image ->
                logger.info(image.queryUrl())
            }

            // 正则匹配
            """\d+_\d+_\d+""".toRegex() matching { input ->
                logger.info(input)
            }

            // 正则匹配 并回复
            """(\d+)\s(\d+)\s(\d+)""".toRegex() matchingReply { match ->
                val (a, b, c) = match.destructured
                "a=${a}, b=${b}, c=${c}"
            }

            // 管理员发了消息
            sentByAdministrator() reply {
                "狗管理"
            }

            // 群主发了消息
            sentByOwner() reply {
                "狗群主"
            }
        }

        // 监听群消息
        globalEventChannel().subscribeGroupMessages {
            // ...
        }

        // 监听好友消息
        globalEventChannel().subscribeFriendMessages {
            // ...
        }
    }

    override fun onDisable() {
        logger.info("disable, 用于指令注销等操作")
        ExampleSimpleCommand.unregister()
        ExampleCompositeCommand.unregister()

        ExampleSimpleListenerHost.cancel()

        // 结束上面的 globalEventChannel().subscribe...
        coroutineContext.cancelChildren()
    }
}