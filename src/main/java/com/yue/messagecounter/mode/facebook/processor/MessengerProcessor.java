package com.yue.messagecounter.mode.facebook.processor;

import com.yue.messagecounter.global.utils.LangUtil;
import com.yue.messagecounter.mode.AbstractJSONProcessor;
import com.yue.messagecounter.mode.facebook.comparator.FacebookPositiveOrder;
import com.yue.messagecounter.mode.facebook.comparator.FacebookReverseOrder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MessengerProcessor extends AbstractJSONProcessor {
    private static final Map<File[], MessengerProcessor> flyweight = new Hashtable<>();

    public static MessengerProcessor getInstance(File[] files) {
        if (!(flyweight.containsKey(files))) {
            flyweight.put(files, new MessengerProcessor(files));
        }
        return flyweight.get(files);
    }

    private MessengerProcessor(File[] files) {
        super(files);
    }

    @Override
    public void process() {
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

                member.forEach(obj -> {
                    String name = ((JSONObject) obj).getString("name");
                    members.put(name, members.get(name));
                });

                items.forEach(obj -> {
                    JSONObject jsonObject1 = (JSONObject) obj;
                    String sender = jsonObject1.getString("sender_name");

                    int amount = Optional.ofNullable(members.get(sender)).orElse(0);
                    members.put(sender, ++amount);

                    jsonObjects.add(jsonObject1);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Comparator<JSONObject> comparator;
        String setting = config.getProperty("Order");

        if (setting.equals("-1"))
            comparator = new FacebookReverseOrder();
        else
            comparator = new FacebookPositiveOrder();

        jsonObjects.stream()
                .sorted(comparator)
                .forEach(obj -> {
                    Date date = new Date(obj.getLong("timestamp_ms"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String sender = obj.getString("sender_name");
                    String content = LangUtil.getInstance().getBundle().getString("file.message");

                    if (obj.has("content")) {
                        content = obj.getString("content");
                    }
                    stringList.add(interpreter.decode(dateFormat.format(date) + "\t" + sender + " -> " + content));
        });

        stringList.addFirst("====================\n");

        members.forEach((k, v) -> stringList.addFirst(interpreter.decode(k) + ": " + v));

        stringList.addFirst("====================");
        stringList.addFirst(LangUtil.getInstance().getBundle().getString("log.total") + msgAmount);

        try {
            fileUtil.write(stringList.toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
