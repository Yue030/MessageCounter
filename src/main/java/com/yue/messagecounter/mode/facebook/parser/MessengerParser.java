package com.yue.messagecounter.mode.facebook.parser;

import com.yue.messagecounter.global.Parser;
import com.yue.messagecounter.mode.facebook.parser.task.MessengerParserTask;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class MessengerParser implements Parser<Integer> {
    private final File[] files;

    public static MessengerParser getInstance(File[] files) {
        return new MessengerParser(files);
    }

    private MessengerParser(File[] files) {
        this.files = files;
    }

    @Override
    public Integer parse() {
        ForkJoinPool joinPool = new ForkJoinPool();
        if (files != null) {
            return joinPool.invoke(new MessengerParserTask(files, 0, files.length));
        }

        return -1;
    }
}
