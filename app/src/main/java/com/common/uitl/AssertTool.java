package com.common.uitl;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

/* loaded from: classes.dex */
public class AssertTool {
    public static String DATA_PATH = "data/";
    private static String tag = "AssertTool";

    public static String getStrFromAssert(Context context, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStreamFromAssertFile(context, str)));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine.trim());
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static HashSet<String> listAssertFiles(Context context) {
        HashSet<String> hashSet = null;
        try {
            String[] list = context.getAssets().list(DATA_PATH);
            if (list == null || list.length <= 0) {
                return null;
            }
            HashSet<String> hashSet2 = new HashSet<>();
            try {
                for (String str : list) {
                    hashSet2.add(str);
                }
                return hashSet2;
            } catch (IOException e) {
                e = e;
                hashSet = hashSet2;
                e.printStackTrace();
                LogUtil.e(tag, GlobalTool.getLogPrffix() + ":" + e.getLocalizedMessage());
                return hashSet;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    public static InputStream getInputStreamFromAssertFile(Context context, String str) {
        try {
            return context.getAssets().open(str);
        } catch (IOException e) {
            e.printStackTrace();
            String str2 = tag;
            LogUtil.e(str2, GlobalTool.getLogPrffix() + ":" + e.getLocalizedMessage());
            return null;
        }
    }

    public static ArrayList<String> readLinesFromAssertsFiles(Context context, String str) {
        InputStream inputStreamFromAssertFile = getInputStreamFromAssertFile(context, str);
        if (inputStreamFromAssertFile == null) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamFromAssertFile));
        ArrayList<String> arrayList = new ArrayList<>();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    arrayList.add(readLine.trim());
                } else {
                    inputStreamFromAssertFile.close();
                    return arrayList;
                }
            } catch (IOException e) {
                e.printStackTrace();
                String str2 = tag;
                LogUtil.e(str2, GlobalTool.getLogPrffix() + " message:" + e.getLocalizedMessage());
                return arrayList;
            }
        }
    }
}
