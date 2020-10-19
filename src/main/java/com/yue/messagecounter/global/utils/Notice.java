package com.yue.messagecounter.global.utils;

import com.yue.messagecounter.global.Mode;
import com.yue.messagecounter.mode.ModeController;
import com.yue.messagecounter.mode.facebook.FacebookPane;
import com.yue.messagecounter.mode.instagram.InstagramPane;

public class Notice {
    private Notice() {}

    public static void note(String str) {
        Mode mode = ModeController.getMode();

        if (mode == Mode.FACEBOOK) {
            FacebookPane.getInstance().getNotice().append(str + "\n");
        } else if (mode == Mode.INSTAGRAM) {
            InstagramPane.getInstance().getNotice().append(str + "\n");
        }
    }
}
