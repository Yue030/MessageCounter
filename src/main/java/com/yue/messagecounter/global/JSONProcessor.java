package com.yue.messagecounter.global;

import java.io.FileNotFoundException;

@FunctionalInterface
public interface JSONProcessor {
    void process() throws FileNotFoundException;
}
