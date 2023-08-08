package com.common.uitl;

import androidx.exifinterface.media.ExifInterface;
import com.common.net.NetResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class IdcardValidator {
    private String[][] codeAndCity = {new String[]{"11", "北京"}, new String[]{"12", "天津"}, new String[]{"13", "河北"}, new String[]{"14", "山西"}, new String[]{"15", "内蒙古"}, new String[]{"21", "辽宁"}, new String[]{"22", "吉林"}, new String[]{"23", "黑龙江"}, new String[]{"31", "上海"}, new String[]{"32", "江苏"}, new String[]{"33", "浙江"}, new String[]{"34", "安徽"}, new String[]{"35", "福建"}, new String[]{"36", "江西"}, new String[]{"37", "山东"}, new String[]{"41", "河南"}, new String[]{"42", "湖北"}, new String[]{"43", "湖南"}, new String[]{"44", "广东"}, new String[]{"45", "广西"}, new String[]{"46", "海南"}, new String[]{"50", "重庆"}, new String[]{"51", "四川"}, new String[]{"52", "贵州"}, new String[]{"53", "云南"}, new String[]{"54", "西藏"}, new String[]{"61", "陕西"}, new String[]{"62", "甘肃"}, new String[]{"63", "青海"}, new String[]{"64", "宁夏"}, new String[]{"65", "新疆"}, new String[]{"71", "台湾"}, new String[]{"81", "香港"}, new String[]{"82", "澳门"}, new String[]{"91", "国外"}};
    private String[] cityCode = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};
    private int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private String[] verifyCode = {"1", NetResult.CODE_OK, "X", "9", "8", "7", "6", "5", "4", ExifInterface.GPS_MEASUREMENT_3D, ExifInterface.GPS_MEASUREMENT_2D};

    public boolean isValidatedAllIdcard(String str) {
        if (str.length() == 15) {
            str = convertIdcarBy15bit(str);
        }
        return isValidate18Idcard(str);
    }

    private boolean isValidate18Idcard(String str) {
        String checkCodeBySum;
        if (str.length() != 18) {
            return false;
        }
        String substring = str.substring(0, 17);
        String substring2 = str.substring(17, 18);
        if (isDigital(substring)) {
            char[] charArray = substring.toCharArray();
            if (charArray != null) {
                int[] iArr = new int[substring.length()];
                int powerSum = getPowerSum(converCharToInt(charArray));
                return (powerSum == 0 || (checkCodeBySum = getCheckCodeBySum(powerSum)) == null || !substring2.equalsIgnoreCase(checkCodeBySum)) ? false : true;
            }
            return true;
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ac, code lost:
        if (r13 <= 30) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00ae, code lost:
        r13 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00cc, code lost:
        if (r13 <= 31) goto L38;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isValidate15Idcard(String str) {
        boolean z;
        boolean z2;
        if (str.length() == 15 && isDigital(str)) {
            String substring = str.substring(0, 2);
            String substring2 = str.substring(6, 12);
            int parseInt = Integer.parseInt(str.substring(6, 8).trim());
            int parseInt2 = Integer.parseInt(str.substring(8, 10).trim());
            int parseInt3 = Integer.parseInt(str.substring(10, 12).trim());
            String[] strArr = this.cityCode;
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = false;
                    break;
                } else if (strArr[i].equals(substring)) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if (!z) {
                return false;
            }
            Date date = null;
            try {
                date = new SimpleDateFormat("yyMMdd").parse(substring2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null && !new Date().before(date)) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                int parseInt4 = Integer.parseInt(String.valueOf(gregorianCalendar.get(1)).substring(2).trim());
                if ((parseInt >= 50 || parseInt <= parseInt4) && parseInt2 >= 1 && parseInt2 <= 12) {
                    gregorianCalendar.setTime(date);
                    switch (parseInt2) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            if (parseInt3 >= 1) {
                                break;
                            }
                            z2 = false;
                            break;
                        case 2:
                            if (!gregorianCalendar.isLeapYear(gregorianCalendar.get(1))) {
                                z2 = false;
                                break;
                            } else {
                                z2 = false;
                            }
                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            if (parseInt3 >= 1) {
                                break;
                            }
                            z2 = false;
                            break;
                        default:
                            z2 = false;
                            break;
                    }
                    return z2;
                }
            }
        }
        return false;
    }

    private String convertIdcarBy15bit(String str) {
        Date date;
        if (str.length() == 15 && isDigital(str)) {
            try {
                date = new SimpleDateFormat("yyMMdd").parse(str.substring(6, 12));
            } catch (ParseException e) {
                e.printStackTrace();
                date = null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String str2 = str.substring(0, 6) + String.valueOf(calendar.get(1)) + str.substring(8);
            char[] charArray = str2.toCharArray();
            if (charArray != null) {
                int[] iArr = new int[str2.length()];
                String checkCodeBySum = getCheckCodeBySum(getPowerSum(converCharToInt(charArray)));
                if (checkCodeBySum == null) {
                    return null;
                }
                return str2 + checkCodeBySum;
            }
            return str2;
        }
        return null;
    }

    private boolean isIdcard(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return Pattern.matches("(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)", str);
    }

    public boolean is15Idcard(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return Pattern.matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$", str);
    }

    public boolean is18Idcard(String str) {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", str);
    }

    private boolean isDigital(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return str.matches("^[0-9]*$");
    }

    private int getPowerSum(int[] iArr) {
        if (this.power.length != iArr.length) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = 0;
            while (true) {
                int[] iArr2 = this.power;
                if (i3 < iArr2.length) {
                    if (i2 == i3) {
                        i += iArr[i2] * iArr2[i3];
                    }
                    i3++;
                }
            }
        }
        return i;
    }

    private String getCheckCodeBySum(int i) {
        switch (i % 11) {
            case 0:
                return "1";
            case 1:
                return NetResult.CODE_OK;
            case 2:
                return "x";
            case 3:
                return "9";
            case 4:
                return "8";
            case 5:
                return "7";
            case 6:
                return "6";
            case 7:
                return "5";
            case 8:
                return "4";
            case 9:
                return ExifInterface.GPS_MEASUREMENT_3D;
            case 10:
                return ExifInterface.GPS_MEASUREMENT_2D;
            default:
                return null;
        }
    }

    private int[] converCharToInt(char[] cArr) throws NumberFormatException {
        int[] iArr = new int[cArr.length];
        int length = cArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            iArr[i2] = Integer.parseInt(String.valueOf(cArr[i]).trim());
            i++;
            i2++;
        }
        return iArr;
    }
}
