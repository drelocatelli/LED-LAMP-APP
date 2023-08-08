package com.common.uitl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/* loaded from: classes.dex */
public class NumberHelper {
    public static String keepDecimal2(String str) {
        try {
            return new DecimalFormat("0.00").format(Double.parseDouble(str));
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String div10000Str(String str) {
        try {
            double parseDouble = Double.parseDouble(str);
            if (parseDouble < 10000.0d) {
                return str;
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(decimalFormat.format(parseDouble / 10000.0d));
            stringBuffer.append("万");
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String numberToCnMoney(String str) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            long parseDouble = (long) Double.parseDouble(str);
            if (str.length() > 5 && str.length() < 9) {
                stringBuffer.append(parseDouble / 10000);
                stringBuffer.append("万");
            } else if (str.length() < 9) {
                return str;
            } else {
                if (parseDouble / 100000000 != 0) {
                    stringBuffer.append(parseDouble / 100000000);
                    stringBuffer.append("亿");
                }
                stringBuffer.append((parseDouble % 100000000) / 10000);
                stringBuffer.append("万");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String floatToPercent(String str) {
        double parseDouble = Double.parseDouble(str);
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(2);
        return percentInstance.format(parseDouble);
    }

    public static String floatToCurrency(String str) {
        float parseFloat = Float.parseFloat(str);
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.CHINA);
        currencyInstance.setMinimumFractionDigits(3);
        return currencyInstance.format(parseFloat);
    }

    public static String floatToCurrency2(String str) {
        float parseFloat = Float.parseFloat(str);
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.CHINA);
        currencyInstance.setMinimumFractionDigits(2);
        return currencyInstance.format(parseFloat);
    }

    public static String LeftPad_Tow_Zero(int i) {
        return new DecimalFormat("00").format(i);
    }

    public static String formatNumber(double d) {
        return new DecimalFormat("0.00").format(d);
    }
}
