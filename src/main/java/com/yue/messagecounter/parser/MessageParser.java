package com.yue.messagecounter.parser;

import com.yue.messagecounter.parser.task.CounterTask;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class MessageParser {
    private File file = null;
    private File[] files = null;

    public static MessageParser getInstance(File file) {
        return new MessageParser(file);
    }

    public static MessageParser getInstance(File[] files) {
        return new MessageParser(files);
    }

    public MessageParser(File file) {
        this.file = file;
    }

    public MessageParser(File[] files) {
        this.files = files;
    }


    public int parse() {
        ForkJoinPool joinPool = new ForkJoinPool();
        if (files != null) {
            return joinPool.invoke(new CounterTask(files, 0, files.length));
        } else if (file != null) {
            return joinPool.invoke(new CounterTask(new File[]{file}, 0, files.length));
        }

        return -1;
    }
}
