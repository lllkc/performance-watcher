package com.github.watcher.ll.autoproxy;

import com.github.watcher.ll.interceptor.MonitorInterceptor;
import com.github.watcher.ll.support.Parser;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;

/**
 * @author: linlei
 * @date: 2016/10/23.
 */

public class MonitorProxyCreator extends AbstractAutoProxyCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorInterceptor.class);

    private Parser parser;

    private MethodInterceptor[] interceptors;

    private boolean switchOn;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!switchOn) {
            return bean;
        }
        Object ori = bean;
        if (AopUtils.isAopProxy(bean)) {
            Object proxy = null;
            //找到最里层的未被代理的bean
            while (AopUtils.isAopProxy(bean)) {
                try {
                    proxy = bean;
                    bean = ((Advised) bean).getTargetSource().getTarget();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    return bean;
                }
            }
            if (parser.isEligibleBean(bean)) {
                parser.parseAnnotation(bean);
                for (MethodInterceptor mi : interceptors)
                    ((Advised) proxy).addAdvice(mi);
            }
        } else {
            if (parser.isEligibleBean(bean)) {
                parser.parseAnnotation(bean);
                //创建代理
                return super.postProcessAfterInitialization(bean, beanName);
            }
        }
        return ori;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return interceptors;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public MethodInterceptor[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(MethodInterceptor[] interceptors) {
        this.interceptors = interceptors;
    }

    public boolean isSwitchOn() {
        return switchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        this.switchOn = switchOn;
    }

}
