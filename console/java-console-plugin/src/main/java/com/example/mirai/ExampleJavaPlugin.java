package com.example.mirai;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

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

        // 延时任务
        getScheduler().delayed(30_000, () -> {
            getLogger().info("...");
        });

        // 定时任务
        getScheduler().repeating(60_000, () -> {
            getLogger().info("...");
            getLogger().info("...");
            getLogger().info("...");
        });
    }

    @Override
    public void onDisable() {
        // ...
    }
}
