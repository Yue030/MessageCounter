package com.yue.messagecounter.global;

@FunctionalInterface
public interface Parser<T> {
    T parse();
}
