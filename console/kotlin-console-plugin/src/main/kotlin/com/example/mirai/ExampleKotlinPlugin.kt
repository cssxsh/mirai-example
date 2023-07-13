package com.example.mirai

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

@PublishedApi
internal object ExampleKotlinPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "com.example.mirai.kotlin-console-plugin",
        name = "fix-protocol-version",
        version = "1.9.6"
    ) {
        author("cssxsh")
    }
) {
    override fun PluginComponentStorage.onLoad() {
        logger.info("load, 用于资源加载和注册服务等操作，一般插件不会用到，比如公共服务插件注册服务")
    }

    override fun onEnable() {
        logger.info("enable, 用于指令注册，数据载入等操作")
    }

    override fun onDisable() {
        logger.info("disable, 用于指令注销等操作")
    }
}