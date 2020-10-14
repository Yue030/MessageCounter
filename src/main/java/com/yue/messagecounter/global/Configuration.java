package com.yue.messagecounter.global;

import com.yue.messagecounter.utils.FileUtil;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Configuration {
    public static final String CONFIG_PATH = "./config.properties";

    private static final Properties config = new Properties();

    private static final FileUtil fileUtil = FileUtil.getInstance(CONFIG_PATH);

    static {
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
