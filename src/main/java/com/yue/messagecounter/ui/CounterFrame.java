package com.yue.messagecounter.ui;

import com.yue.messagecounter.annotaion.Frame;
import com.yue.messagecounter.global.Mode;
import com.yue.messagecounter.global.utils.LangUtil;
import com.yue.messagecounter.mode.ModeController;
import com.yue.messagecounter.mode.facebook.FacebookPane;
import com.yue.messagecounter.mode.instagram.InstagramPane;

import javax.swing.*;
import java.awt.*;

@Frame
public class CounterFrame extends JFrame {
    public static final long serialVersionUID = 1L;

    private static CounterFrame counter;

    private String resultMode;

    private JPanel contentPane;

    private CounterFrame() {
        super();
        Mode mode = ModeController.getMode();
        if (mode == Mode.FACEBOOK) {
            contentPane = FacebookPane.getInstance();
            resultMode = "Messenger";
        } else if (mode == Mode.INSTAGRAM) {
            contentPane = InstagramPane.getInstance();
            resultMode = "Instagram";
        }

        initFrame();
    }

    public static CounterFrame getInstance() {
      if (counter == null) {
            counter = new CounterFrame();
        }
        return counter;
    }

    private void initFrame() {
        LangUtil util = LangUtil.getInstance();
        setTitle(util.format(util.getBundle().getString("title"), new Object[]{resultMode}));
        setSize(620, 600);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setVisible(true);
    }
}
