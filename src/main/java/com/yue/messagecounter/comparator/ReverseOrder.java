package com.yue.messagecounter.comparator;

import org.json.JSONObject;

import java.util.Comparator;

public class ReverseOrder implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        return Long.compare(o2.getLong("timestamp_ms"), o1.getLong("timestamp_ms"));
    }
}
