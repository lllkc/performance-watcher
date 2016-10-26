package com.github.watcher.ll.support;

/**
 * @author: linlei
 * @date: 2016/10/24.
 */
public interface Parser {
    boolean isEligibleBean(Object bean);

    void parseAnnotation(Object bean);
}
