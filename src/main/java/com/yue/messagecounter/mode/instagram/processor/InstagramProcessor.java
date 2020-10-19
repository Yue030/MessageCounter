package com.yue.messagecounter.mode.instagram.processor;

import com.yue.messagecounter.global.utils.LangUtil;
import com.yue.messagecounter.mode.AbstractJSONProcessor;
import com.yue.messagecounter.mode.instagram.comparator.InstagramPositiveOrder;
import com.yue.messagecounter.mode.instagram.comparator.InstagramReverseOrder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InstagramProcessor extends AbstractJSONProcessor {
    private static final Map<File, InstagramProcessor> flyweight = new Hashtable<>();
    private final int index;
    private final Map<Integer, List<String>> participant;

    public static InstagramProcessor getInstance(File file, Map<Integer, List<String>> map , int index) {
        if (!(flyweight.containsKey(index))) {
            flyweight.put(file, new InstagramProcessor(file, index, map));
        }
        return flyweight.get(file);
    }

    private InstagramProcessor(File file, int index, Map<Integer, List<String>> map) {
        super(file);
        this.index = index;
        this.participant = map;
    }

    @Override
    public void process() {
        StringBuilder builder = new StringBuilder();
        LinkedList<String> stringList = new LinkedList<>();
        Map<String, Integer> members = new HashMap<>();
        AtomicInteger msgAmount = new AtomicInteger();

        List<JSONObject> jsonObjects = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(files[0]))) {
            while (reader.ready()) {
                builder.append(reader.readLine());
            }

            JSONArray jsonFile = new JSONArray(builder.toString());
            JSONObject jsonObject = jsonFile.getJSONObject(index);
            JSONArray message = jsonObject.getJSONArray("conversation");

            participant.get(index).forEach(obj -> members.put(obj, 0));

            message.forEach(obj -> {
                JSONObject jsonObject1 = (JSONObject) obj;
                String sender = jsonObject1.getString("sender");

                int amount = Optional.ofNullable(members.get(sender)).orElse(0);
                members.put(sender, ++amount);

                jsonObjects.add(jsonObject1);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        Comparator<JSONObject> comparator;
        String setting = config.getProperty("Order");

        if (setting.equals("-1"))
            comparator = new InstagramReverseOrder();
        else
            comparator = new InstagramPositiveOrder();

        jsonObjects.stream()
                .sorted(comparator)
                .forEach(obj -> {
                    String value = obj.getString("created_at");
                    String[] values = value.split("T");
                    String date = LocalDate.parse(values[0]).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                    String time = LocalTime.parse(values[1].substring(0, 8)).toString();

                    String sender = obj.getString("sender");
                    String content = LangUtil.getInstance().getBundle().getString("file.message");

                    if (obj.has("text") && !(obj.isNull("text"))) {
                        content = obj.getString("text");
                    }
                    msgAmount.getAndIncrement();
                    stringList.add(date + " " + time + "\t" + sender + " -> " + content);
                });

        stringList.addFirst("====================\n");

        members.forEach((k, v) -> stringList.addFirst(k + ": " + v));

        stringList.addFirst("====================");
        stringList.addFirst(LangUtil.getInstance().getBundle().getString("log.total") + msgAmount);

        try {
            fileUtil.write(stringList.toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
