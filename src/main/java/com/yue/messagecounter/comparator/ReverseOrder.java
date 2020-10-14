package com.yue.messagecounter.comparator;

import com.yue.messagecounter.annotaion.Order;
import org.json.JSONObject;

import java.util.Comparator;

@Order
public class ReverseOrder implements Comparator<JSONObject> {
    private Order order = getClass().getAnnotation(Order.class);

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        return Long.compare(o2.getLong(order.value()), o1.getLong(order.value()));
    }
}
