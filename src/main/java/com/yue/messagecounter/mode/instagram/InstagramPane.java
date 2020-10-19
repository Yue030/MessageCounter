package com.yue.messagecounter.mode.instagram;

import com.yue.messagecounter.global.utils.FileChooser;
import com.yue.messagecounter.global.utils.LangUtil;
import com.yue.messagecounter.global.utils.Notice;
import com.yue.messagecounter.mode.instagram.parser.InstagramAmountParser;
import com.yue.messagecounter.mode.instagram.parser.InstagramParticipantParser;
import com.yue.messagecounter.mode.instagram.processor.InstagramProcessor;
import com.yue.messagecounter.ui.CounterPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class InstagramPane extends CounterPanel {
    private static InstagramPane pane;

    public static InstagramPane getInstance() {
        if (pane == null) {
            pane = new InstagramPane();
        }

        return pane;
    }

    private InstagramPane() {
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
                File file = new File(FileChooser.chooseFile());
                files = new File[]{file};

                InstagramParticipantParser participantParser = InstagramParticipantParser.getInstance(file);
                Map<Integer, List<String>> map = participantParser.parse();

                map.keySet().forEach((key) -> getFileList().add(key + ":" + map.get(key)));
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
                int index = getFileList().getSelectedIndex();
                System.out.println(index);

                int total = InstagramAmountParser.getInstance(files[0], index).parse();
                Notice.note(bundle.getString("log.total") + total);
                InstagramParticipantParser participantParser = InstagramParticipantParser.getInstance(files[0]);

                InstagramProcessor processor = InstagramProcessor.getInstance(files[0], participantParser.parse(), index);
                processor.process();

                JOptionPane.showMessageDialog(null, bundle.getString("dialog.run"));
            });
        }

        return run;
    }

    @Override
    protected void service() {
        clearFile.setText(bundle.getString("instagram.btn.clearParticipant"));
        copyright.setText(LangUtil.getInstance().format(bundle.getString("copyRight"), new Object[]{"Instagram"}));
    }
}
