package com.yue.messagecounter.utils;

import com.yue.messagecounter.annotaion.Initialization;
import com.yue.messagecounter.annotaion.Utils;
import com.yue.messagecounter.global.Configuration;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

@Utils(type = "Language")
public class LangUtil {
    public static Locale LOCALE_TW;
    public static Locale LOCALE_US;

    private static Properties config;

    private static LangUtil langUtil;

    private Locale locale;

    @Initialization
    private static void init() {
        LOCALE_TW = new Locale("zh", "TW");
        LOCALE_US = new Locale("en", "US");
        config = Configuration.getConfig();
        System.out.println(LangUtil.class);
    }

    private LangUtil() {
        String lang = config.getProperty("Lang");
        if (lang == null) {
            lang = "en_US";
        }

        if (lang.equals("zh_TW")) {
            locale = LOCALE_TW;
        } else {
            locale = LOCALE_US;
        }
    }

    public static LangUtil getInstance() {
        if (langUtil == null) {
            langUtil = new LangUtil();
        }
        return langUtil;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("i18n.message", locale);
    }

    public String format(String str, Object[] objects) {
        MessageFormat formatter = new MessageFormat(str);
        return formatter.format(objects);
    }
}
