package com.yue.messagecounter.frame;

import com.yue.messagecounter.annotaion.Frame;
import com.yue.messagecounter.parser.MessageParser;
import com.yue.messagecounter.utils.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

@Frame
public class CounterFrame extends JFrame {
    public static final long serialVersionUID = 1L;

    private static CounterFrame counter;

    private File[] files;

    private JPanel panel;

    private JTextArea logArea;

    private JScrollPane logScroll;
    private JScrollPane fileScroll;

    private List fileList;

    private JButton chooseFile;
    private JButton run;
    private JButton clearLog;
    private JButton clearFile;

    private JLabel copyright;

    private ResourceBundle bundle;

    private CounterFrame() {
        super();
        initialization();
    }

    public static CounterFrame getInstance() {
      if (counter == null) {
            counter = new CounterFrame();
        }
        return counter;
    }

    public void notice(String text) {
        logArea.append(text + "\n");
    }

    private void initialization() {
        resetFiles();
        initBundle();
        initComponent();
        initFrame();
    }

    private void initBundle() {
        bundle = LangUtil.getInstance().getBundle();
    }

    private void initFrame() {
        setTitle(bundle.getString("title"));
        setSize(620, 600);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(getPanel());
        setVisible(true);
    }

    private void initComponent() {
        panel = null;
        logArea = null;
        logScroll = null;
        fileList = null;
        fileScroll = null;
        chooseFile = null;
        run = null;
        clearLog = null;
        clearFile = null;
        copyright = null;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(null);
            panel.add(getLogScroll());
            panel.add(getFileScroll());
            panel.add(getChooseFileBtn());
            panel.add(getRunBtn());
            panel.add(getClearLogBtn());
            panel.add(getClearFileBtn());
            panel.add(getCopyright());
        }

        return panel;
    }

    private JScrollPane getLogScroll() {
        if (logScroll == null) {
            logScroll = new JScrollPane();
            logScroll.setBounds(new Rectangle(10, 40, 600, 200));
            logScroll.setViewportView(getLogArea());
        }

        return logScroll;
    }

    private JTextArea getLogArea() {
        if (logArea == null) {
            logArea = new JTextArea();
            logArea.setFont(new Font("Dialog", Font.PLAIN, 20));
            DefaultCaret caret = (DefaultCaret) logArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            logArea.setEditable(false);
        }

        return logArea;
    }

    private JScrollPane getFileScroll() {
        if (fileScroll == null) {
            fileScroll = new JScrollPane();
            fileScroll.setBounds(new Rectangle(10, 250, 300, 300));
            fileScroll.setViewportView(getFileList());
        }

        return fileScroll;
    }

    private List getFileList() {
        if (fileList == null) {
            fileList = new List();
            fileList.setFont(new Font("Dialog", Font.PLAIN, 20));
        }

        return fileList;
    }

    private JButton getChooseFileBtn() {
        if (chooseFile == null) {
            chooseFile = new JButton();
            chooseFile.setBounds(new Rectangle(350, 410, 225, 50));
            chooseFile.setFont(new Font("Dialog", Font.PLAIN, 20));
            chooseFile.setText(bundle.getString("btn.chooseFile"));

            chooseFile.addActionListener((event) -> {
                resetFiles();
                files = FileChooser.chooseFile();

                for (File file : files) {
                    getFileList().add(file.getAbsolutePath());
                }
            });
        }

        return chooseFile;
    }

    private JButton getRunBtn() {
        if (run == null) {
            run = new JButton();
            run.setBounds(new Rectangle(350, 490, 225, 50));
            run.setFont(new Font("Dialog", Font.PLAIN, 20));
            run.setText(bundle.getString("btn.run"));

            run.addActionListener((event) -> {
                FileUtil utils = FileUtil.getInstance("./messages.txt");
                utils.createFile();

                int total = MessageParser.getInstance(files).parse();
                notice(bundle.getString("log.total") + total);
                JsonUtil.getJson(files);

                JOptionPane.showMessageDialog(null, bundle.getString("dialog.run"));
            });
        }

        return run;
    }

    private JButton getClearLogBtn() {
        if (clearLog == null) {
            clearLog = new JButton();
            clearLog.setBounds(new Rectangle(350, 250, 225, 50));
            clearLog.setFont(new Font("Dialog", Font.PLAIN, 20));
            clearLog.setText(bundle.getString("btn.clearLog"));

            clearLog.addActionListener((event) -> getLogArea().setText(""));
        }

        return clearLog;
    }

    private JButton getClearFileBtn() {
        if (clearFile == null) {
            clearFile = new JButton();
            clearFile.setBounds(new Rectangle(350, 330, 225, 50));
            clearFile.setFont(new Font("Dialog", Font.PLAIN, 20));
            clearFile.setText(bundle.getString("btn.clearFile"));

            clearFile.addActionListener((event) -> resetFiles());
        }

        return clearFile;
    }

    private JLabel getCopyright() {
        if (copyright == null) {
            copyright = new JLabel();
            copyright.setBounds(new Rectangle(5, 1, 600, 32));
            copyright.setHorizontalAlignment(SwingConstants.CENTER);
            copyright.setFont(new Font("Dialog", Font.PLAIN, 21));
            copyright.setText(bundle.getString("copyRight"));
        }

        return copyright;
    }

    private void resetFiles() {
        getFileList().removeAll();
        files = null;
    }
}
