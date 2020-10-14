package com.yue.messagecounter.utils;

import com.yue.messagecounter.annotaion.Initialization;
import com.yue.messagecounter.annotaion.Utils;
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
import java.util.concurrent.atomic.AtomicInteger;

@Utils(type = "Json")
public class JsonUtil {
    private JsonUtil() {}

    private static Properties config;
    private static FileUtil fileUtil;

    private static MessageInterpreter interpreter;

    @Initialization
    private static void init() {
        config = Configuration.getConfig();
        fileUtil = FileUtil.getInstance(config.getProperty("MessageFilePath"));
        interpreter = MessagerInterpreter.getInstance();
        System.out.println(JsonUtil.class);
    }

    public static void getJson(File[] files) {
        StringBuilder builder = new StringBuilder();
        LinkedList<String> stringList = new LinkedList<>();
        Map<String, Integer> members = new HashMap<>();
        AtomicInteger msgAmount = new AtomicInteger();
        List<JSONObject> jsonObjects = new ArrayList<>();

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                builder.delete(0, builder.length());
                while (reader.ready()) {
                    builder.append(reader.readLine());
                }

                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONArray items = jsonObject.getJSONArray("messages");
                JSONArray member = jsonObject.getJSONArray("participants");

                msgAmount.addAndGet(items.length());

                for (int i = 0; i < member.length(); i++) {
                    String name = member.getJSONObject(i).getString("name");
                    members.put(name, members.get(name));
                }

                for (int i = 0; i < items.length(); i++) {
                    JSONObject jsonObject1 = items.getJSONObject(i);
                    String sender = jsonObject1.getString("sender_name");

                    int amount = Optional.ofNullable((members.get(sender))).orElse(0);
                    members.put(sender, ++amount);

                    jsonObjects.add(jsonObject1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        stringList.addFirst("\n");

        members.forEach((k, v) -> stringList.addFirst(interpreter.decode(k) + ": " + v));

        stringList.addFirst(LangUtil.getInstance().getBundle().getString("log.total") + msgAmount);

        try {
            fileUtil.write(stringList.toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
