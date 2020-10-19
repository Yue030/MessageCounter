package com.yue.messagecounter.global.utils;

import com.yue.messagecounter.annotaion.Utils;

import java.awt.*;
import java.io.File;

@Utils(
        name = "FileChooser",
        type = "File"
)

public final class FileChooser {
    private FileChooser() { }

    public static String chooseFile() {
        FileDialog dialog = new FileDialog((Frame)null, LangUtil.getInstance().getBundle().getString("fileChoose.title"), FileDialog.LOAD);

        dialog.setFilenameFilter((file, name) -> name.endsWith(".json"));
        dialog.setMultipleMode(true);
        dialog.setVisible(true);

        return dialog.getDirectory() + dialog.getFile();
    }

    public static File[] chooseFiles() {
        FileDialog dialog = new FileDialog((Frame)null, LangUtil.getInstance().getBundle().getString("fileChoose.title"), FileDialog.LOAD);

        dialog.setFilenameFilter((file, name) -> name.endsWith(".json"));
        dialog.setMultipleMode(true);
        dialog.setVisible(true);

        return dialog.getFiles();
    }
}
