package com.yue.messagecounter.mode;

import com.yue.messagecounter.annotaion.Initialization;
import com.yue.messagecounter.global.Configuration;
import com.yue.messagecounter.global.Mode;

import java.util.Properties;

public class ModeController {
    private static Mode mode;

    private ModeController() {}

    @Initialization(priority = 2)
    public static void initMode() {
        Properties prop = Configuration.getConfig();
        String value = prop.getProperty("Mode").toLowerCase();

        if (value.equals("fb")) {
            mode = Mode.FACEBOOK;
        } else if (value.equals("ig")) {
            mode = Mode.INSTAGRAM;
        } else {
            System.exit(0);
        }
    }

    public static Mode getMode() {
        return mode;
    }
}
