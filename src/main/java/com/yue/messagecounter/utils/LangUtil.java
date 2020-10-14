package com.yue.messagecounter.utils;

import com.yue.messagecounter.global.Configuration;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class LangUtil {
    public static final Locale LOCALE_TW = new Locale("zh", "TW");
    public static final Locale LOCALE_US = new Locale("en", "US");

    private static final Properties config = Configuration.getConfig();

    private static LangUtil langUtil;

    private Locale locale;

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
