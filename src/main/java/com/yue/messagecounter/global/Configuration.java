package com.yue.messagecounter.global;

import com.yue.messagecounter.annotaion.Initialization;
import com.yue.messagecounter.global.utils.FileUtil;

import javax.swing.*;
import java.io.*;
import java.util.*;

@com.yue.messagecounter.annotaion.Configuration
public class Configuration {
    public static String CONFIG_PATH;

    private static Properties config;

    private static FileUtil fileUtil;

    @Initialization(priority = 3)
    private static void init() {
        config = new Properties();
        CONFIG_PATH = "./config.properties";
        fileUtil = FileUtil.getInstance(CONFIG_PATH);

        try {
            config.load(new FileInputStream(CONFIG_PATH));
        } catch (FileNotFoundException e) {
            createConfig();
            JOptionPane.showMessageDialog(null, "Can not found configuration file (config.properties)");
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unknown Error");
            System.exit(0);
        }
    }
    public static void createConfig() {
        try (InputStream in = Configuration.class.getClassLoader().getResourceAsStream("config.properties")) {
            fileUtil.createFile();

            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            String str = new String(bytes);

            fileUtil.write(new String[]{str});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getConfig() {
        return config;
    }
}
