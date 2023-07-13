# Example Kotlin Console Plugin

mirai jvm 插件的生命周期

onLoad -> onEnable -> onDisable

* onLoad 用于资源加载和注册服务等操作，一般插件不会用到，比如公共服务插件注册服务
* onEnable 用于指令注册，数据载入等操作
* onDisable 用于指令注销等操作

## 测试启动

`gradlew runConsole`

## 整合包

`gradlew copyConsoleRuntime`