package com.yue.messagecounter.mode.facebook.parser.task;

import com.yue.messagecounter.annotaion.Task;
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
import java.util.concurrent.RecursiveTask;

@Task
public class MessengerParserTask extends RecursiveTask<Integer> {
    private static final int DATA_MAX_LENGTH = 3;
    private final ResourceBundle bundle;

    private final int startIndex, endIndex;

    private final File[] data;

    public MessengerParserTask(File[] data, int startIndex, int endIndex) {
        this.data = data;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        bundle = LangUtil.getInstance().getBundle();
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        if (endIndex - startIndex <= DATA_MAX_LENGTH) {
            for (int i = startIndex; i < endIndex; i++) {
                StringBuilder builder = new StringBuilder();
                Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading"),
                        new Object[]{data[i].getName().replaceAll(".json", "")}));
                if (!(data[i].toString().endsWith(".json"))) {
                    Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading.failed.IllegalFile"),
                            new Object[]{data[i].getName()}));
                    continue;
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(data[i]))) {
                    while (reader.ready()) {
                        builder.append(reader.readLine());
                    }

                    JSONObject jsonObject = new JSONObject(builder.toString());
                    JSONArray items = jsonObject.getJSONArray("messages");

                    sum += items.length();
                } catch (IOException | JSONException e) {
                    Notice.note(LangUtil.getInstance().format(bundle.getString("log.loading.failed.JsonException"),
                            new Object[]{data[i].getName()}));
                    e.printStackTrace();
                }
            }
            return sum;
        } else {
            MessengerParserTask c1 = new MessengerParserTask(data, startIndex + DATA_MAX_LENGTH, endIndex);
            c1.fork();

            MessengerParserTask c2 = new MessengerParserTask(data, startIndex, Math.min(endIndex, startIndex + DATA_MAX_LENGTH));

            return c2.compute() + c1.join();
        }
    }
}
