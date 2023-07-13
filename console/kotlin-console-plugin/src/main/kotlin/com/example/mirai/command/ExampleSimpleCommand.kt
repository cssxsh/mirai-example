package com.example.mirai.command

import com.example.mirai.ExampleKotlinPlugin
import net.mamoe.mirai.console.command.SimpleCommand

/**
 * 一个简单指令
 */
public object ExampleSimpleCommand : SimpleCommand(
    owner = ExampleKotlinPlugin,
    "simple",
    description = "这是一个简单的指令"
) {
    @Handler
    public fun handle() {
        ExampleKotlinPlugin.logger.info("你用了一个指令")
    }
}