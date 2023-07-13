package com.example.mirai.command

import com.example.mirai.ExampleKotlinPlugin
import net.mamoe.mirai.console.command.CompositeCommand

/**
 * 一个复合指令
 */
public object ExampleCompositeCommand : CompositeCommand(
    owner = ExampleKotlinPlugin,
    "composite",
    description = "这是一个简单的指令"
) {

    /**
     * 指定 子指令名为 start, ready
     *
     * `/composite start`
     *
     * `/composite ready`
     */
    @SubCommand("start", "ready")
    public fun init() {
        ExampleKotlinPlugin.logger.info("你用了一个指令")
    }

    /**
     * 不指定指令名时， 取方法名 stop 作为指令
     *
     * `/composite stop`
     *
     * `@Description` 为 子指令指定一个简介
     */
    @SubCommand
    @Description("这是一段简介")
    public fun stop() {
        ExampleKotlinPlugin.logger.info("你用了一个指令")
    }
}