package com.home.service;

import java.util.HashMap;

/* loaded from: classes.dex */
public class SampleGattAttributes {
    public static String CLIENT_CHARACTERISTIC_CONFIG;
    public static String HEART_RATE_MEASUREMENT;
    private static HashMap<String, String> attributes;

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        attributes = hashMap;
        HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
        CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
        hashMap.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String str, String str2) {
        String str3 = attributes.get(str);
        return str3 == null ? str2 : str3;
    }
}
