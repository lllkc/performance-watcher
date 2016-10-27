package com.github.watcher.ll.support;

import com.github.watcher.ll.anotation.DefaultWatchDefinition;
import com.github.watcher.ll.anotation.Watch;
import com.github.watcher.ll.anotation.WatchDefinition;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author: linlei
 * @date: 2016/10/24.
 */
public class WatchAnnotationParser implements Parser {

    private WatchDefinition defaultDef;

    private String watchPackages;

    @Override
    public boolean isEligibleBean(Object bean) {
        if (bean.getClass().isAnnotationPresent(Watch.class)) {
            return true;
        }

        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method m : methods) {
            if (m.isAnnotationPresent(Watch.class)) {
                return true;
            }
        }

        return withinPackage(bean);
    }

    private boolean withinPackage(Object bean) {
        String className = bean.getClass().getName();
        String[] packages = watchPackages.split(",");
        for (String p : packages) {
            if (className.equals(p) || (className.startsWith(p) && className.charAt(p.length()) == '.')) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void parseAnnotation(Object bean) {
        String cacheKey = Config.getCacheKey(bean.getClass());
        if(withinPackage(bean)) {
            Config.watchConfig.put(cacheKey, defaultDef);
        }

        if (bean.getClass().isAnnotationPresent(Watch.class)) {
            if (!Config.watchConfig.containsKey(cacheKey)) {
                Watch w = bean.getClass().getAnnotation(Watch.class);
                DefaultWatchDefinition def = new DefaultWatchDefinition();

                if (w.threshold() != WatchDefinition.DEFAULT_THRESHOLD) {
                    def.setThreshold(w.threshold());
                } else {
                    def.setThreshold(defaultDef.getThreshold());
                }
                Config.watchConfig.put(cacheKey, def);
            }
        }

        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method m : methods) {
            cacheKey = Config.getCacheKey(bean.getClass(), m);
            if (m.isAnnotationPresent(Watch.class)) {
                if (!Config.watchConfig.containsKey(cacheKey)) {
                    Watch w = m.getAnnotation(Watch.class);
                    DefaultWatchDefinition def = new DefaultWatchDefinition();

                    if (w.threshold() != WatchDefinition.DEFAULT_THRESHOLD) {
                        def.setThreshold(w.threshold());
                    } else {
                        def.setThreshold(defaultDef.getThreshold());
                    }
                    Config.watchConfig.put(cacheKey, def);
                }
            }
        }

    }

    public WatchDefinition getDefaultDef() {
        return defaultDef;
    }

    public void setDefaultDef(WatchDefinition defaultDef) {
        this.defaultDef = defaultDef;
    }

    public String getWatchPackages() {
        return watchPackages;
    }

    public void setWatchPackages(String watchPackages) {
        this.watchPackages = watchPackages;
    }

}
