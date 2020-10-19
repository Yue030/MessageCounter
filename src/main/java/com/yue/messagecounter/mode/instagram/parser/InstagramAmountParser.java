package com.yue.messagecounter.mode.instagram.parser;

import com.yue.messagecounter.global.Parser;
import com.yue.messagecounter.global.utils.LangUtil;
import com.yue.messagecounter.global.utils.Notice;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;

public class InstagramAmountParser implements Parser<Integer> {
    private final File file;
    private final int index;

    private static final ResourceBundle bundle = LangUtil.getInstance().getBundle();

    public static InstagramAmountParser getInstance(File file, int index) {
        return new InstagramAmountParser(file, index);
    }

    private InstagramAmountParser(File file, int index) {
        this.file = file;
        this.index = index;
    }

    @Override
    public Integer parse() {
        StringBuilder builder = new StringBuilder();
        Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading"),
                new Object[]{file.getName().replaceAll(".json", "")}));
        if (!(file.toString().endsWith(".json"))) {
            Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading.failed.IllegalFile"),
                    new Object[]{file.getName()}));
            return -1;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                builder.append(reader.readLine());
            }

            JSONArray jsonFile = new JSONArray(builder.toString());
            JSONObject jsonObject = jsonFile.getJSONObject(index);

            return jsonObject.getJSONArray("conversation").length();
        } catch (IOException | JSONException e) {
            Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading.failed.JsonException"),
                    new Object[]{file.getName()}));
            e.printStackTrace();
        }

        return -1;
    }
}
