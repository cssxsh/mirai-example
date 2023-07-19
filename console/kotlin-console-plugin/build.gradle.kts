plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"

    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "com.example.mirai"
version = "0.1.0"

repositories {
    // 镜像库，加快依赖下载速度
    maven("https://repo.huaweicloud.com/repository/maven")
    // Maven Central
    mavenCentral()
    // 快照，如果你需要使用开发版 mirai, 那么就要添加这个
    maven("https://repo.mirai.mamoe.net/snapshots")
}

dependencies {
    testImplementation(kotlin("test"))
    //
    implementation(platform("net.mamoe:mirai-bom:2.15.0"))
    compileOnly("net.mamoe:mirai-console-compiler-common")
    testImplementation("net.mamoe:mirai-core-utils")
    testImplementation("net.mamoe:mirai-core-mock")
    testImplementation("net.mamoe:mirai-logging-slf4j")
    testImplementation("net.mamoe:mirai-console-compiler-common")
    //
    implementation(platform("org.slf4j:slf4j-parent:2.0.7"))
    testImplementation("org.slf4j:slf4j-simple")
}

kotlin {
    explicitApi()
}

mirai {
    // 指定 runConsole 时的 mirai 版本, 可以用来测试兼容性
    coreVersion = System.getenv("MIRAI_VERSION") ?: "2.15.0"
    consoleVersion = System.getenv("MIRAI_VERSION") ?: "2.15.0"

    // 指定编译的 java 版本，如果你需要更新的版本的特性
    jvmTarget = JavaVersion.VERSION_1_8
}

tasks {
    test {
        useJUnitPlatform()
    }
    // 制作整合包, 保存路径 run/console
    register("copyConsoleRuntime") {
        group = "mirai"

        val folder = file("run/console")
        val libs = folder.resolve("libs")
        libs.mkdirs()
        val plugins = folder.resolve("plugins")
        plugins.mkdirs()

        doLast {
            configurations.getByName("testConsoleRuntime").resolve().forEach { lib ->
                val dest = libs.resolve(lib.name)
                lib.copyTo(dest, true)
            }
            buildDir.resolve("mirai/${name}-${version}.mirai2.jar").let { plugin ->
                val dest = plugins.resolve(plugin.name)
                plugin.copyTo(dest, true)
            }
            """
                @echo off
                setlocal
                set JAVA_BINARY="java"
                if exist "java" set JAVA_BINARY=".\java\bin\java.exe"
                if exist "java-home" set JAVA_BINARY=".\java-home\bin\java.exe"
                if exist "jdk-17.0.7+7" set JAVA_BINARY=".\jdk-17.0.7+7\bin\java.exe"
                if exist "jdk-17.0.7+7-jre" set JAVA_BINARY=".\jdk-17.0.7+7-jre\bin\java.exe"
                
                %JAVA_BINARY% -version
                %JAVA_BINARY% -D"file.encoding=gbk" -cp "./libs/*" "net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader"
                
                set EL=%ERRORLEVEL%
                if %EL% NEQ 0 (
                    echo Process exited with %EL%
                    pause
                )
            """.trimIndent().let {
                folder.resolve("start.cmd").writeText(it)
            }
            """
                java -D"file.encoding=utf-8" -cp "./libs/*" net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
            """.trimIndent().let {
                folder.resolve("start.sh").writeText(it)
            }
            """
                这是独立的开发版本整合包，不能替换 mcl 的文件来使用
                
                如果出现 'java' 不是内部或外部命令，也不是可运行的程序
                说明你没有安装好 Java，你可以选择本地补充安装
                
                本地补充安装（Windows）：
                
                自行二选一下载，然后解压全部文件 到 start.cmd 同目录
                
                windows x64 版本
                https://mirrors.tuna.tsinghua.edu.cn/Adoptium/17/jre/x64/windows/OpenJDK17U-jre_x64_windows_hotspot_17.0.7_7.zip
                
                windows x86 版本
                https://mirrors.tuna.tsinghua.edu.cn/Adoptium/17/jre/x32/windows/OpenJDK17U-jre_x86-32_windows_hotspot_17.0.7_7.zip
                
                目录结构如下
                ├───java-home (本地java目录，注意要解压全部文件，这里仅列出部分目录结构)
                │   └───bin
                │       └───java.exe
                ├───start.cmd (WIN启动脚本)
                ├───start.sh (Linux/MACOS启动脚本)
                ├───libs (库目录，里面有mirai本体)
                ├───logs (日志目录，里面有日志文件)
                └───plugins (插件目录)
            """.trimIndent().let {
                folder.resolve("README.txt").writeText(it)
            }
        }
    }
}