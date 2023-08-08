package com.common.uitl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import com.home.base.LedBleApplication;
import com.home.service.BluetoothLeServiceSingle;
import com.ledlamp.R;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class StringUtils {
    public static String getReturnCodeString(Context context, String str) {
        if (str.equalsIgnoreCase(com.home.constant.Constant.NODATA_CODE)) {
            return context.getResources().getString(R.string.no_data);
        }
        if (str.equalsIgnoreCase("000002")) {
            return context.getResources().getString(R.string.wrong_user_name_or_password);
        }
        if (str.equalsIgnoreCase("000003")) {
            return context.getResources().getString(R.string.wrong_old_password);
        }
        if (str.equalsIgnoreCase("000004")) {
            return context.getResources().getString(R.string.invalid_verification_code);
        }
        if (str.equalsIgnoreCase("000005")) {
            return context.getResources().getString(R.string.username_already_exists);
        }
        if (str.equalsIgnoreCase("000006")) {
            return context.getResources().getString(R.string.please_use_the_verification_code_sent);
        }
        if (str.equalsIgnoreCase("000007")) {
            return context.getResources().getString(R.string.the_verification_code_is_sent_abnormally_please_try_again);
        }
        if (str.equalsIgnoreCase("E00000")) {
            return context.getResources().getString(R.string.the_parameter_is_empty);
        }
        if (str.equalsIgnoreCase("E00001")) {
            return context.getResources().getString(R.string.parameter_error);
        }
        if (str.equalsIgnoreCase("E00002")) {
            return context.getResources().getString(R.string.system_error);
        }
        if (str.equalsIgnoreCase("E00003")) {
            return context.getResources().getString(R.string.token_error);
        }
        if (str.equalsIgnoreCase("E00004")) {
            return context.getResources().getString(R.string.database_error);
        }
        if (str.equalsIgnoreCase("E00005")) {
            return context.getResources().getString(R.string.file_size_exceeds_limit);
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static int computStrlen(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        return str.length();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isDajiePasswd(String str) {
        int length;
        return !isEmpty(str) && (length = str.length()) >= 4 && length <= 20;
    }

    public static boolean isEmptyNull(String str) {
        return str == null || "".equals(str.trim()) || "null".equals(str);
    }

    public static String convertByteArrayToString(byte[] bArr, String str) throws UnsupportedEncodingException {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        return new String(bArr, str);
    }

    public static String nvl(int i) {
        return String.valueOf(i);
    }

    public static String nvl(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    public static int getCharCount(String str, char c) {
        if (isEmpty(str)) {
            return 0;
        }
        int i = 0;
        for (char c2 : str.toCharArray()) {
            if (c2 == c) {
                i++;
            }
        }
        return i;
    }

    public static ArrayList<String> getDeviceNameCompairResult(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator<BluetoothDevice> it = LedBleApplication.getApp().getBleDevices().iterator();
        while (it.hasNext()) {
            BluetoothDevice next = it.next();
            if (next.getName() != null && next.getName().startsWith(BluetoothLeServiceSingle.NAME_START_LED)) {
                if (next.getName().contains("-")) {
                    arrayList.add(next.getName().split("-")[0].replaceAll("\\d+", ""));
                } else if (next.getName().contains("_")) {
                    arrayList.add(next.getName().split("_")[0].replaceAll("\\d+", ""));
                }
            }
        }
        String replaceAll = str.replaceAll(" ", "");
        if (arrayList.size() <= 0 || arrayList.contains(BluetoothLeServiceSingle.NAME_START_LED) || arrayList.contains(replaceAll)) {
            return null;
        }
        return arrayList;
    }

    public static String convertByteArrayToString(byte[] bArr) throws UnsupportedEncodingException {
        return convertByteArrayToString(bArr, "utf-8");
    }
}
