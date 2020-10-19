package com.yue.messagecounter.ui;

import com.yue.messagecounter.annotaion.Initialization;
import com.yue.messagecounter.global.utils.LangUtil;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

public abstract class CounterPanel extends JPanel {
    protected static ResourceBundle bundle;

    protected File[] files;

    protected JTextArea logArea;
    protected JScrollPane logScroll;

    protected JScrollPane fileScroll;
    protected List fileList;

    protected JButton chooseFile;
    protected JButton run;
    protected JButton clearLog;
    protected JButton clearFile;

    protected JLabel copyright;

    public JTextArea getNotice() {
        return logArea;
    }

    @Initialization
    protected static void init() {
        bundle = LangUtil.getInstance().getBundle();
    }

    protected CounterPanel() {
        setLayout(null);
        add(getLogScroll());
        add(getFileScroll());
        add(getChooseFileBtn());
        add(getRunBtn());
        add(getClearLogBtn());
        add(getClearFileBtn());
        add(getCopyright());
        service();
    }

    protected JScrollPane getLogScroll() {
        if (logScroll == null) {
            logScroll = new JScrollPane();
            logScroll.setBounds(new Rectangle(10, 40, 600, 200));
            logScroll.setViewportView(getLogArea());
        }

        return logScroll;
    }

    protected JTextArea getLogArea() {
        if (logArea == null) {
            logArea = new JTextArea();
            logArea.setFont(new Font("Dialog", Font.PLAIN, 20));
            DefaultCaret caret = (DefaultCaret) logArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            logArea.setEditable(false);
        }

        return logArea;
    }

    protected JScrollPane getFileScroll() {
        if (fileScroll == null) {
            fileScroll = new JScrollPane();
            fileScroll.setBounds(new Rectangle(10, 250, 300, 300));
            fileScroll.setViewportView(getFileList());
        }

        return fileScroll;
    }

    protected List getFileList() {
        if (fileList == null) {
            fileList = new List();
            fileList.setFont(new Font("Dialog", Font.PLAIN, 20));
        }

        return fileList;
    }

    protected JButton getChooseFileBtn() {
        if (chooseFile == null) {
            chooseFile = new JButton();
            chooseFile.setBounds(new Rectangle(350, 410, 225, 50));
            chooseFile.setFont(new Font("Dialog", Font.PLAIN, 20));
        }

        return chooseFile;
    }

    protected JButton getRunBtn() {
        if (run == null) {
            run = new JButton();
            run.setBounds(new Rectangle(350, 490, 225, 50));
            run.setFont(new Font("Dialog", Font.PLAIN, 20));
            run.setText(bundle.getString("btn.run"));
        }

        return run;
    }

    protected JButton getClearLogBtn() {
        if (clearLog == null) {
            clearLog = new JButton();
            clearLog.setBounds(new Rectangle(350, 250, 225, 50));
            clearLog.setFont(new Font("Dialog", Font.PLAIN, 20));
            clearLog.setText(bundle.getString("btn.clearLog"));

            clearLog.addActionListener((event) -> getLogArea().setText(""));
        }

        return clearLog;
    }

    protected JButton getClearFileBtn() {
        if (clearFile == null) {
            clearFile = new JButton();
            clearFile.setBounds(new Rectangle(350, 330, 225, 50));
            clearFile.setFont(new Font("Dialog", Font.PLAIN, 20));

            clearFile.addActionListener((event) -> resetFiles());
        }

        return clearFile;
    }

    protected JLabel getCopyright() {
        if (copyright == null) {
            copyright = new JLabel();
            copyright.setBounds(new Rectangle(5, 1, 600, 32));
            copyright.setHorizontalAlignment(SwingConstants.CENTER);
            copyright.setFont(new Font("Dialog", Font.PLAIN, 21));
        }

        return copyright;
    }

    protected void resetFiles() {
        getFileList().removeAll();
        files = null;
    }

    protected abstract void service();
}
