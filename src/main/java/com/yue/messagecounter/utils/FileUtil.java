package com.yue.messagecounter.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class FileUtil {
    private static final Map<String, FileUtil> hashtable = new Hashtable<>();

    private final File file;

    public static FileUtil getInstance(String path)  {
        if (!(hashtable.containsKey(path))) {
            hashtable.put(path, new FileUtil(path));
        }
        return hashtable.get(path);
    }

    private FileUtil(String path) {
        this.file = new File(path);
    }

    public void createFile()  {
        if (file.exists())
            file.delete();

        if (!(file.exists())) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String[] str) throws IOException {
        try (FileWriter writer = new FileWriter(file)){
            for (String s : str) {
                writer.write(s + "\n");
            }
            writer.flush();
        }
    }

    public void delete() {
        if (file.exists())
            file.delete();
    }
}
