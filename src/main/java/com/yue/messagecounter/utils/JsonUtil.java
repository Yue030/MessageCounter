package com.yue.messagecounter.utils;

import com.yue.messagecounter.comparator.PositiveOrder;
import com.yue.messagecounter.comparator.ReverseOrder;
import com.yue.messagecounter.global.Configuration;
import com.yue.messagecounter.interpreter.MessageInterpreter;
import com.yue.messagecounter.interpreter.impl.MessagerInterpreter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonUtil {
    private JsonUtil() {}

    private static final Properties config = Configuration.getConfig();
    private static final FileUtil fileUtil = FileUtil.getInstance(config.getProperty("MessageFilePath"));

    private static final MessageInterpreter interpreter = MessagerInterpreter.getInstance();

    public static void getJson(File[] files) {
        StringBuilder builder = new StringBuilder();
        List<String> stringList = new ArrayList<>();

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while (reader.ready()) {
                    builder.append(reader.readLine());
                }

                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONArray items = jsonObject.getJSONArray("messages");
                JSONArray member = jsonObject.getJSONArray("participants");

                List<JSONObject> jsonObjects = new ArrayList<>();

                Map<String, Integer> members = new HashMap<>();

                for (int i =0; i < member.length(); i++) {
                    String name = member.getJSONObject(i).getString("name");
                    members.put(name, 0);
                }

                for (int i = 0; i < items.length(); i++) {
                    JSONObject jsonObject1 = items.getJSONObject(i);
                    String sender = jsonObject1.getString("sender_name");

                    int count = members.get(sender);
                    members.put(sender, ++count);
                    jsonObjects.add(jsonObject1);
                }

                stringList.add(LangUtil.getInstance().getBundle().getString("log.total") + items.length());

                members.forEach((k, v) -> stringList.add(interpreter.decode(k) + ": " + v));

                stringList.add("\n");

                Comparator<JSONObject> comparator;
                String setting = config.getProperty("Order");

                if (setting.equals("-1"))
                    comparator = new ReverseOrder();
                else
                    comparator = new PositiveOrder();

                jsonObjects.stream()
                        .sorted(comparator)
                        .forEach((obj)-> {
                    Date date = new Date(obj.getLong("timestamp_ms"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String sender = obj.getString("sender_name");
                    String content = LangUtil.getInstance().getBundle().getString("file.message");

                    if (obj.has("content")) {
                        content = obj.getString("content");
                    }

                    stringList.add(interpreter.decode(dateFormat.format(date) + "\t" + sender + " -> " + content));
                });

                fileUtil.write(stringList.toArray(new String[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
