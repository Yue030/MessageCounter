package com.yue.messagecounter.utils;

import com.yue.messagecounter.annotaion.Utils;

import java.awt.*;
import java.io.File;

@Utils(
        name = "FileChooser",
        type = "File"
)

public final class FileChooser {
    private FileChooser() { }

    public static File[] chooseFile() {
        FileDialog dialog = new FileDialog((Frame)null, LangUtil.getInstance().getBundle().getString("fileChoose.title"), FileDialog.LOAD);

        dialog.setFilenameFilter((file, name) -> name.endsWith(".json"));
        dialog.setMultipleMode(true);
        dialog.setVisible(true);

        return dialog.getFiles();
    }
}
