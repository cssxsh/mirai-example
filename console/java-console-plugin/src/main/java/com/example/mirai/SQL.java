package com.example.mirai;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * <a href="https://github.com/cssxsh/mirai-example/issues/1">加载 SQL 驱动</a>
 */
public class SQL {
    static void load() {
        Thread current = Thread.currentThread();
        ClassLoader context = current.getContextClassLoader();
        ClassLoader plugin = SQL.class.getClassLoader();
        try {
            current.setContextClassLoader(plugin);
            ServiceLoader.load(java.sql.Driver.class).reload();

            // region Database init, 下面的代码自行换成你自己的初始化代码

            DriverManager.getDriver("jdbc:mysql://localhost:3306/mydb");

            // endregion
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            current.setContextClassLoader(context);
        }
    }
}