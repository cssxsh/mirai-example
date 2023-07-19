package com.example.mirai;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExampleJavaPlugin extends JavaPlugin {
    public ExampleJavaPlugin() {
        super(new JvmPluginDescriptionBuilder("com.example.mirai.example-java-plugin", "0.0.0")
                .name("example-java-plugin")
                .build());
    }

    @Override
    public void onEnable() {
        // 异步任务
        getScheduler().async(() -> {
            getLogger().info("...");
        });

        // 异步任务
        Future<Long> async = getScheduler().async(() -> System.currentTimeMillis() / 1000);

        try {
            getLogger().info(async.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 延时任务
        getScheduler().delayed(30_000, () -> {
            getLogger().info("...");
        });

        Future<Bot> delayed = getScheduler().delayed(30_000, () -> Bot.getInstance(12345));

        try {
            getLogger().info(delayed.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 定时任务
        Future<Void> repeating = getScheduler().repeating(60_000, () -> {
            getLogger().info("...");
            getLogger().info("...");
            getLogger().info("...");
        });
        // 结束定时任务
        repeating.cancel(true);
    }

    @Override
    public void onDisable() {
        // ...
    }
}
