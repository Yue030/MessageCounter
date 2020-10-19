package com.yue.messagecounter.mode.facebook.comparator;

import com.yue.messagecounter.annotaion.Order;
import com.yue.messagecounter.global.SortedOrder;
import org.json.JSONObject;

@Order
public class FacebookPositiveOrder implements SortedOrder {
    private Order order = getClass().getAnnotation(Order.class);

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        return Long.compare(o1.getLong(order.facebook()), o2.getLong(order.facebook()));
    }
}
