package com.huy.topica.mail.data;

import java.util.HashMap;

public class TestData {
    
    private TestData() {

    }
    private static HashMap<Integer, Integer> dateData = new HashMap<Integer, Integer>();

    static {
        dateData.put(1, 31);
        dateData.put(2, 29);
        dateData.put(3, 31);
        dateData.put(4, 30);
        dateData.put(5, 31);
        dateData.put(6, 30);
        dateData.put(7, 31);
        dateData.put(8, 31);
        dateData.put(9, 30);
        dateData.put(10, 31);
        dateData.put(11, 30);
        dateData.put(12, 31);
    }

    public static boolean check(int month, int date) {
        return dateData.get(month) == date;
    }
}
