package com.common.uitl;

import java.util.UUID;

/* loaded from: classes.dex */
public class UUIDGenerator {
    public static String getUUID(int i) {
        try {
            String uuid = UUID.randomUUID().toString();
            String str = uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
            return str.length() > i ? str.substring(0, i) : str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
