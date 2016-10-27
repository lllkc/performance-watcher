package com.github.watcher.ll.support;

import com.github.watcher.ll.anotation.WatchDefinition;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: linlei
 * @date: 2016/10/24.
 */
public class Config {
    private Config() {
    }

    public static final Map<String, WatchDefinition> watchConfig = new HashMap<>();

    public static String getCacheKey(Class<?> aClass, Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append(aClass.getName());
        sb.append("_");
        sb.append(m.getName());
        Class[] cl = m.getParameterTypes();
        for (Class c : cl) {
            sb.append("_");
            sb.append(c.getName());
        }
        return sb.toString();
    }

    public static String getCacheKey(Class<?> aClass) {
        return aClass.getName();
    }
}
