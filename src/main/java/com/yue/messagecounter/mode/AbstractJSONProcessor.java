package com.yue.messagecounter.mode;

import com.yue.messagecounter.annotaion.Initialization;
import com.yue.messagecounter.global.Configuration;
import com.yue.messagecounter.global.JSONProcessor;
import com.yue.messagecounter.global.utils.FileUtil;
import com.yue.messagecounter.interpreter.MessageInterpreter;
import com.yue.messagecounter.interpreter.impl.MessagerInterpreter;

import java.io.File;
import java.util.Properties;

public abstract class AbstractJSONProcessor implements JSONProcessor {
    protected static Properties config;
    protected static FileUtil fileUtil;

    protected static MessageInterpreter interpreter;

    protected File[] files;

    protected AbstractJSONProcessor(File file) {
        this.files = new File[]{file};
    }

    protected AbstractJSONProcessor(File[] files) {
        this.files = files;
    }

    @Initialization
    protected static void init() {
        config = Configuration.getConfig();
        fileUtil = FileUtil.getInstance(config.getProperty("MessageFilePath"));
        String mode = config.getProperty("Mode").toLowerCase();
        if (mode.equals("fb")) {
            interpreter = MessagerInterpreter.getInstance();
        }
    }
}
