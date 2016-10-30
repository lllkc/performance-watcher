package com.github.watcher.ll.interceptor;

import com.github.watcher.ll.anotation.WatchDefinition;
import com.github.watcher.ll.support.Config;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author: linlei
 * @date: 2016/10/23.
 */
public class MonitorInterceptor implements MethodInterceptor {
    private Logger LOGGER = LoggerFactory.getLogger(MonitorInterceptor.class);

    private WatchDefinition defaultDef;

    private boolean useDynamicLogger;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        Class cl = invocation.getThis().getClass();
        Method m = invocation.getMethod();

        String cacheKey = Config.getCacheKey(cl);
        WatchDefinition wd = Config.watchConfig.get(cacheKey);

        cacheKey = Config.getCacheKey(cl, m);
        if (Config.watchConfig.get(cacheKey) != null) {
            wd = Config.watchConfig.get(cacheKey);
        }

        if (null == wd) {
            return invocation.proceed();
        }

        if (useDynamicLogger) {
            LOGGER = LoggerFactory.getLogger(cl);
        }

        long start = System.currentTimeMillis();
        try {
            result = invocation.proceed();
        } finally {
            long cost = System.currentTimeMillis() - start;
            if (cost >= wd.getThreshold()) {
                LOGGER.info("[{}][{}]{}", cl.getName(), m.getName(), arguments(invocation));
                LOGGER.info("[{}][{}] cost [{}] ms.", cl.getName(), m.getName(), cost);
                LOGGER.info("[{}][{}] return value: {}", cl.getName(), m.getName(), result);
            } else {
                LOGGER.debug("[{}][{}]{}", cl.getName(), m.getName(), arguments(invocation));
                LOGGER.debug("[{}][{}] cost [{}] ms.", cl.getName(), m.getName(), cost);
                LOGGER.debug("[{}][{}] return value: {}", cl.getName(), m.getName(), result);
            }
        }
        return result;
    }

    private String arguments(MethodInvocation invocation) {
        Object[] objects = invocation.getArguments();
        StringBuilder sb = new StringBuilder();
        sb.append(" Argument:");
        for (Object obj : objects) {
            sb.append("[");
            sb.append(obj);
            sb.append("]");
        }
        return sb.toString();

    }

    public WatchDefinition getDefaultDef() {
        return defaultDef;
    }

    public void setDefaultDef(WatchDefinition defaultDef) {
        this.defaultDef = defaultDef;
    }

    public boolean isUseDynamicLogger() {
        return useDynamicLogger;
    }

    public void setUseDynamicLogger(boolean useDynamicLogger) {
        this.useDynamicLogger = useDynamicLogger;
    }
}
