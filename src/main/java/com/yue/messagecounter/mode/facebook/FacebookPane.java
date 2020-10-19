package com.yue.messagecounter.mode.facebook;

import com.yue.messagecounter.global.utils.FileChooser;
import com.yue.messagecounter.global.utils.FileUtil;
import com.yue.messagecounter.global.utils.LangUtil;
import com.yue.messagecounter.global.utils.Notice;
import com.yue.messagecounter.mode.facebook.parser.MessengerParser;
import com.yue.messagecounter.mode.facebook.processor.MessengerProcessor;
import com.yue.messagecounter.ui.CounterPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class FacebookPane extends CounterPanel {
    private static FacebookPane pane;

    public static FacebookPane getInstance() {
        if (pane == null) {
            pane = new FacebookPane();
        }

        return pane;
    }

    private FacebookPane() {
        super();
    }

    @Override
    protected JButton getChooseFileBtn() {
        if (chooseFile == null) {
            chooseFile = new JButton();
            chooseFile.setBounds(new Rectangle(350, 410, 225, 50));
            chooseFile.setFont(new Font("Dialog", Font.PLAIN, 20));
            chooseFile.setText(bundle.getString("btn.chooseFile"));

            chooseFile.addActionListener((event) -> {
                resetFiles();
                files = FileChooser.chooseFiles();

                for (File file : files) {
                    getFileList().add(file.getAbsolutePath());
                }
            });
        }
        return chooseFile;
    }

    @Override
    protected JButton getRunBtn() {
        if (run == null) {
            run = new JButton();
            run.setBounds(new Rectangle(350, 490, 225, 50));
            run.setFont(new Font("Dialog", Font.PLAIN, 20));
            run.setText(bundle.getString("btn.run"));

            run.addActionListener((event) -> {
                int total = MessengerParser.getInstance(files).parse();
                Notice.note(bundle.getString("log.total") + total);
                MessengerProcessor.getInstance(files).process();

                JOptionPane.showMessageDialog(null, bundle.getString("dialog.run"));
            });
        }

        return run;
    }

    @Override
    protected void service() {
        clearFile.setText(bundle.getString("facebook.btn.clearFile"));
        copyright.setText(LangUtil.getInstance().format(bundle.getString("copyRight"), new Object[]{"Messenger"}));
    }
}
