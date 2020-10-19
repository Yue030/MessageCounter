package com.yue.messagecounter.mode.instagram.comparator;

import com.yue.messagecounter.annotaion.Order;
import com.yue.messagecounter.global.SortedOrder;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;

@Order
public class InstagramReverseOrder implements SortedOrder {
    private final Order order = getClass().getAnnotation(Order.class);

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        String o1String = o1.getString(order.instagram());
        String o2String = o2.getString(order.instagram());

        String[] o1Values = o1String.split("T");
        String[] o2Values = o2String.split("T");

        LocalDate o1Date = LocalDate.parse(o1Values[0]);
        LocalDate o2Date = LocalDate.parse(o2Values[0]);

        if (o2Date.compareTo(o1Date) == 0) {
            LocalTime o1Time = LocalTime.parse(o1Values[1].substring(0, 8));
            LocalTime o2Time = LocalTime.parse(o2Values[1].substring(0, 8));

            return o2Time.compareTo(o1Time);
        }
        return o2Date.compareTo(o1Date);
    }
}
