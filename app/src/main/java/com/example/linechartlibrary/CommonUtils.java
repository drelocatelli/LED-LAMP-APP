package com.example.linechartlibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.common.uitl.Constant;
import com.google.android.material.tabs.TabLayout;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
public class CommonUtils {
    private static InputMethodManager imm;

    private static boolean isEmojiCharacter(char c) {
        return c == 0 || c == '\t' || c == '\n' || c == '\r' || (c >= ' ' && c <= 55295) || ((c >= 57344 && c <= 65533) || (c >= 0 && c <= 65535));
    }

    public static void clearUserData(Context context) {
        SPManager.setAuthCode(context, "");
    }

    public static void clearFocus(EditText editText) {
        editText.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
    }

    public static boolean isSingleActivity(Context context) {
        List<ActivityManager.RunningTaskInfo> list;
        try {
            list = ((ActivityManager) context.getSystemService("activity_message")).getRunningTasks(1);
        } catch (SecurityException e) {
            e.printStackTrace();
            list = null;
        }
        return list != null && list.size() >= 1 && list.get(0).numRunning == 1;
    }

    public static void setUNDERLINE(Context context, TextView textView, int i) {
        textView.getPaint().setFlags(8);
        textView.setTextColor(context.getResources().getColor(i));
    }

    public static void insertContact(Context context, String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        long parseId = ContentUris.parseId(context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues));
        if (!TextUtils.isEmpty(str2)) {
            contentValues.clear();
            contentValues.put("raw_contact_id", Long.valueOf(parseId));
            contentValues.put("mimetype", "vnd.android.cursor.item/name");
            contentValues.put("data2", str);
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);
        }
        if (!TextUtils.isEmpty(str2)) {
            contentValues.clear();
            contentValues.put("raw_contact_id", Long.valueOf(parseId));
            contentValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
            contentValues.put("data1", str2);
            contentValues.put("data2", (Integer) 2);
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);
        }
        if (TextUtils.isEmpty(str3)) {
            return;
        }
        contentValues.clear();
        contentValues.put("raw_contact_id", Long.valueOf(parseId));
        contentValues.put("mimetype", "vnd.android.cursor.item/email_v2");
        contentValues.put("data1", str3);
        contentValues.put("data2", (Integer) 2);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);
    }

    public static void sendMessage(Context context, String str) {
        context.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str)));
    }

    public static void showSoft(EditText editText, Context context) {
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService("input_method");
        }
        editText.requestFocus();
        imm.showSoftInput(editText, 0);
    }

    public static void openKeyBoard(Context context) {
        openKeyBoard(context, 50);
    }

    public static void openKeyBoard(Context context, int i) {
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService("input_method");
        }
        new Timer().schedule(new TimerTask() { // from class: com.example.linechartlibrary.CommonUtils.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                CommonUtils.imm.toggleSoftInput(0, 2);
            }
        }, i);
    }

    public static void closeKeyBoard(Activity activity) {
        if (imm == null) {
            imm = (InputMethodManager) activity.getSystemService("input_method");
        }
        if (activity.getCurrentFocus() == null || activity.getCurrentFocus().getWindowToken() == null) {
            return;
        }
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 2);
    }

    public static void hideSoft(EditText editText, Context context) {
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService("input_method");
        }
        editText.clearFocus();
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return byteToHexString(messageDigest.digest());
        } catch (Exception unused) {
            return str;
        }
    }

    public static String byteToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            sb.append(hexString.toUpperCase());
        }
        return sb.toString();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int i = 0;
        for (int i2 = 0; i2 < adapter.getCount(); i2++) {
            View view = adapter.getView(i2, null, listView);
            view.measure(0, 0);
            i += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = i + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
    }

    public static String formatDateSimple(long j) {
        return new SimpleDateFormat(Constant.STRING_DAY_FORMAT2).format(Long.valueOf(j));
    }

    public static void AppExit(Context context) {
        try {
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception unused) {
        }
    }

    public static boolean isMobileNO(String str) {
        return Pattern.compile("^13[0-9]{1}[0-9]{8}$|15[0-9]{1}[0-9]{8}$|18[0-9]{1}[0-9]{8}$|17[0768]{1}[0-9]{8}$|14[57]{1}[0-9]{8}$").matcher(str).matches();
    }

    public static boolean isMobileNOSimple(String str) {
        return str.length() == 11;
    }

    public static boolean isEmail(String str) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(str).matches();
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static String getFormatMoney(double d) {
        double d2 = (int) d;
        Double.isNaN(d2);
        if (d - d2 != 0.0d) {
            return new DecimalFormat("##0.00").format(d).replaceAll("0+?$", "");
        }
        return new DecimalFormat("##0").format(d);
    }

    public static boolean isNetWorkConnected(Context context) {
        NetworkInfo activeNetworkInfo;
        return (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable()) ? false : true;
    }

    public static boolean isExitsSdcard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static int getCurrentYear(Calendar calendar) {
        return calendar.get(1);
    }

    public static int getCurrentMonth(Calendar calendar) {
        return calendar.get(2) + 1;
    }

    public static int getCurrrentDay(Calendar calendar) {
        return calendar.get(5);
    }

    public static Point getScreenHeightAndWidth(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point;
    }

    public static Date StrToDate(String str) {
        try {
            return new SimpleDateFormat(Constant.STRING_DAY_FORMAT2).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long dateToStamp(String str, String str2) throws ParseException {
        return new SimpleDateFormat(str2).parse(str).getTime();
    }

    public static boolean verifyChinese(String str) {
        return Pattern.compile("^[一-龥]+$").matcher(str).matches();
    }

    public static boolean verifyPassport(String str) {
        return Pattern.compile("^([a-zA-Z]{1}[0-9]{8})$").matcher(str).matches();
    }

    public static boolean verifydentityICard(String str) {
        return str.length() == 15 || str.length() == 18;
    }

    public static boolean verifyCharacters(String str) {
        return Pattern.compile("^[a-zA-Z]+$").matcher(str).matches();
    }

    public static boolean isIllegalCharacter(String str) {
        return Pattern.compile("^[a-zA-Z一-龥]+$").matcher(str).matches();
    }

    public static boolean containsEmoji(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBackground(Context context) {
        Iterator<ActivityManager.RunningAppProcessInfo> it = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ActivityManager.RunningAppProcessInfo next = it.next();
            if (next.processName.equals(context.getPackageName())) {
                Log.i("info", "此appimportace =" + next.importance + ",context.getClass().getName()=" + context.getClass().getName());
                if (next.importance != 100) {
                    return true;
                }
            }
        }
        return false;
    }

    public static float getViewWidth(View view, boolean z) {
        int measuredHeight;
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        if (z) {
            measuredHeight = view.getMeasuredWidth();
        } else {
            measuredHeight = view.getMeasuredHeight();
        }
        return measuredHeight;
    }

    public static ArrayList<Calendar> getCurrentMonthCalendar(Calendar calendar, int i) {
        ArrayList<Calendar> arrayList = new ArrayList<>();
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.add(2, i);
        Calendar calendar3 = (Calendar) calendar.clone();
        calendar3.add(2, i);
        calendar3.set(5, 1);
        int firstDayOfWeek = calendar3.getFirstDayOfWeek() - calendar3.get(7);
        if (firstDayOfWeek > 0) {
            firstDayOfWeek -= 7;
        }
        calendar3.add(5, firstDayOfWeek);
        while (true) {
            if ((calendar3.get(2) < calendar2.get(2) + 1 || calendar3.get(1) < calendar2.get(1)) && calendar3.get(1) <= calendar2.get(1)) {
                for (int i2 = 0; i2 < 7; i2++) {
                    arrayList.add((Calendar) calendar3.clone());
                    calendar3.add(5, 1);
                    Log.i("aaa", calendar3.get(2) + "--" + calendar3.get(5));
                }
            }
        }
        return arrayList;
    }

    public static int differentDays(Date date, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        int i = calendar.get(6);
        int i2 = calendar2.get(6);
        int i3 = calendar.get(1);
        int i4 = calendar2.get(1);
        if (i3 != i4) {
            int i5 = 0;
            while (i3 < i4) {
                i5 = ((i3 % 4 != 0 || i3 % 100 == 0) && i3 % HttpStatus.SC_BAD_REQUEST != 0) ? i5 + 365 : i5 + 366;
                i3++;
            }
            return i5 + (i2 - i);
        }
        return i2 - i;
    }

    public static SpannableString setTextStyle(int i, int i2, int i3, int i4, boolean z, boolean z2, String str, Context context) {
        SpannableString spannableString = new SpannableString(str);
        if (z) {
            spannableString.setSpan(new AbsoluteSizeSpan(DisplayUtil.dp2px(context, i3)), i, i2, 17);
        }
        if (z2) {
            spannableString.setSpan(new ForegroundColorSpan(i4), i, i2, 17);
        }
        return spannableString;
    }

    public static String encryptCode(String str, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < str.length(); i3++) {
            if (i3 >= i && i3 < i2) {
                sb.append("*");
            } else {
                sb.append(str.charAt(i3));
            }
        }
        return sb.toString();
    }

    public static boolean isAvilible(Context context, String str) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        if (installedPackages != null) {
            for (int i = 0; i < installedPackages.size(); i++) {
                if (installedPackages.get(i).packageName.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setViewSize(View view, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (i > 0) {
            layoutParams.width = i;
        }
        if (i2 > 0) {
            layoutParams.height = i2;
        }
        view.setLayoutParams(layoutParams);
    }

    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static String getCurrentTime(String str) {
        return new SimpleDateFormat(str).format(new Date());
    }

    public static void setIndicator(TabLayout tabLayout, int i, int i2) {
        Field field;
        LinearLayout linearLayout = null;
        try {
            field = tabLayout.getClass().getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            field = null;
        }
        field.setAccessible(true);
        try {
            linearLayout = (LinearLayout) field.get(tabLayout);
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        int applyDimension = (int) TypedValue.applyDimension(1, i, Resources.getSystem().getDisplayMetrics());
        int applyDimension2 = (int) TypedValue.applyDimension(1, i2, Resources.getSystem().getDisplayMetrics());
        for (int i3 = 0; i3 < linearLayout.getChildCount(); i3++) {
            View childAt = linearLayout.getChildAt(i3);
            childAt.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -1, 1.0f);
            layoutParams.leftMargin = applyDimension;
            layoutParams.rightMargin = applyDimension2;
            childAt.setLayoutParams(layoutParams);
            childAt.invalidate();
        }
    }

    public static void solveScrollConflict(View view, final ScrollView scrollView) {
        view.setOnTouchListener(new View.OnTouchListener() { // from class: com.example.linechartlibrary.CommonUtils.2
            float ratio = 1.2f;
            float x0 = 0.0f;
            float y0 = 0.0f;

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.x0 = motionEvent.getX();
                    this.y0 = motionEvent.getY();
                } else if (action == 2) {
                    float abs = Math.abs(motionEvent.getX() - this.x0);
                    float abs2 = Math.abs(motionEvent.getY() - this.y0);
                    this.x0 = motionEvent.getX();
                    this.y0 = motionEvent.getY();
                    scrollView.requestDisallowInterceptTouchEvent(abs * this.ratio > abs2);
                }
                return false;
            }
        });
    }
}
