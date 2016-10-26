package com.github.watcher.ll.anotation;

/**
 * @author: linlei
 * @date: 2016/10/24.
 */
public class DefaultWatchDefinition implements WatchDefinition {
    private int threshold;

    @Override
    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

}
