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
        // ...
    }

    @Override
    public void onDisable() {
        // ...
    }
}
