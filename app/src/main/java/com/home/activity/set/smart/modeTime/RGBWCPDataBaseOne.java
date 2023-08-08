package com.home.activity.set.smart.modeTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RGBWCPDataBaseOne {
    public static RGBWCPDataBaseOne mrgbwcp;
    private static MySqliteModeHelper mySqliteModeHelper;
    private SQLiteDatabase db;

    private RGBWCPDataBaseOne(Context context) {
        MySqliteModeHelper mySqliteModeHelper2 = new MySqliteModeHelper(context);
        mySqliteModeHelper = mySqliteModeHelper2;
        this.db = mySqliteModeHelper2.getWritableDatabase();
    }

    public static RGBWCPDataBaseOne getInstance(Context context) {
        if (mrgbwcp == null) {
            mrgbwcp = new RGBWCPDataBaseOne(context);
        }
        return mrgbwcp;
    }

    public void deleteDatebase(String str) {
        SQLiteDatabase sQLiteDatabase = this.db;
        sQLiteDatabase.execSQL("DELETE FROM " + str);
    }

    public void update(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, String str2, String[] strArr) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.RGBWCPONE)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            contentValues.put("WHITE", Integer.valueOf(i6));
            contentValues.put("CYAN", Integer.valueOf(i7));
            contentValues.put("VIOLET", Integer.valueOf(i8));
        }
        this.db.update(MySqliteModeHelper.RGBWCPONE, contentValues, str2, strArr);
    }

    public void insert(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.RGBWCPONE)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            contentValues.put("WHITE", Integer.valueOf(i6));
            contentValues.put("CYAN", Integer.valueOf(i7));
            contentValues.put("VIOLET", Integer.valueOf(i8));
        }
        this.db.insert(MySqliteModeHelper.RGBWCPONE, null, contentValues);
    }

    public List<TimeModle> query(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.db.query(str, null, null, null, null, null, "HOUR,MINUTE");
        while (query.moveToNext()) {
            if (str.equals(MySqliteModeHelper.RGBWCPONE)) {
                TimeModle timeModle = new TimeModle();
                timeModle.setId(query.getInt(0));
                timeModle.setHour(query.getInt(1));
                timeModle.setMinute(query.getInt(2));
                timeModle.setRed(query.getInt(3));
                timeModle.setGreen(query.getInt(4));
                timeModle.setBlude(query.getInt(5));
                timeModle.setWhite(query.getInt(6));
                timeModle.setCyan(query.getInt(7));
                timeModle.setViolet(query.getInt(8));
                arrayList.add(timeModle);
            }
        }
        query.close();
        return arrayList;
    }

    public boolean delete(String str, String str2, String[] strArr) {
        SQLiteDatabase sQLiteDatabase = this.db;
        sQLiteDatabase.delete(str, str2 + "=?", strArr);
        return false;
    }
}
