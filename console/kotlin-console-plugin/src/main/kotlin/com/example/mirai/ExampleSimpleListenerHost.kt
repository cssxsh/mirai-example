package com.example.mirai

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import net.mamoe.mirai.console.events.StartupEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ExceptionInEventHandlerException
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.findIsInstance
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.warning
import java.time.Instant
import java.time.ZoneId
import kotlin.coroutines.CoroutineContext

public object ExampleSimpleListenerHost : SimpleListenerHost() {

    private val logger: MiraiLogger = MiraiLogger.Factory.create(this::class)

    /**
     * 异常处理
     */
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        when (exception) {
            is CancellationException -> {
                // ignore
            }
            // 事件处理中断异常
            is ExceptionInEventHandlerException -> {
                logger.warning({ "exception in ${exception.event}" }, exception.cause)
            }
            else -> {
                logger.warning({ "exception in ${context[CoroutineName]}" }, exception)
            }
        }
    }

    /**
     * 监听消息事件
     */
    @EventHandler
    public fun MessageEvent.handle() {
        // 找一下有没有图片
        val image = message.findIsInstance<Image>() ?: throw NoSuchElementException("image")


        // 找一下有没有文本
        val text = message.findIsInstance<PlainText>() ?: return
    }

    /**
     * 监听Console 启动完成事件
     */
    @EventHandler
    public fun StartupEvent.handle() {
        val time = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault())
        logger.info("Startup at $time")
    }
}