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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InstagramParticipantParser implements Parser<Map<Integer, List<String>>> {
    private final File file;

    private static final ResourceBundle bundle = LangUtil.getInstance().getBundle();

    public static InstagramParticipantParser getInstance(File file) {
        return new InstagramParticipantParser(file);
    }

    private InstagramParticipantParser(File file) {
        this.file = file;
    }

    @Override
    public Map<Integer, List<String>> parse() {
        StringBuilder builder = new StringBuilder();
        Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading"),
                new Object[]{file.getName().replaceAll(".json", "")}));
        if (!(file.toString().endsWith(".json"))) {
            Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading.failed.IllegalFile"),
                    new Object[]{file.getName()}));
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                builder.append(reader.readLine());
            }

            JSONArray jsonFile = new JSONArray(builder.toString());
            Map<Integer, List<String>> map = new HashMap<>();

            AtomicInteger atomicInteger = new AtomicInteger(0);
            jsonFile.forEach(obj -> {
                JSONArray array = ((JSONObject)obj).getJSONArray("participants");
                List<String> list = new ArrayList<>();

                array.toList().forEach((v) -> list.add((String) v));
                map.put(atomicInteger.getAndIncrement(), list);
            });

            return map;
        } catch (IOException | JSONException e) {
            Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading.failed.JsonException"),
                    new Object[]{file.getName()}));
            e.printStackTrace();
        }

        return null;
    }
}
