package com.example.mirai;

import java.util.ServiceLoader;

/**
 * <a href="https://github.com/cssxsh/mirai-example/issues/1">加载 SQL 驱动</a>
 */
public class SQL {
    void load() {
        Thread current = Thread.currentThread();
        ClassLoader context = current.getContextClassLoader();
        ClassLoader plugin = this.getClass().getClassLoader();
        try {
            current.setContextClassLoader(plugin);
            ServiceLoader.load(java.sql.Driver.class).reload();

            // database load ...
        } finally {
            current.setContextClassLoader(context);
        }
    }
}