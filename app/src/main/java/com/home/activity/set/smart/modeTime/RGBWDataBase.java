package com.home.activity.set.smart.modeTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RGBWDataBase {
    public static RGBWDataBase mrgbw;
    private static MySqliteModeHelper mySqliteModeHelper;
    private SQLiteDatabase db;

    private RGBWDataBase(Context context) {
        MySqliteModeHelper mySqliteModeHelper2 = new MySqliteModeHelper(context);
        mySqliteModeHelper = mySqliteModeHelper2;
        this.db = mySqliteModeHelper2.getWritableDatabase();
    }

    public static RGBWDataBase getInstance(Context context) {
        if (mrgbw == null) {
            mrgbw = new RGBWDataBase(context);
        }
        return mrgbw;
    }

    public void deleteDatebase(String str) {
        SQLiteDatabase sQLiteDatabase = this.db;
        sQLiteDatabase.execSQL("DELETE FROM " + str);
    }

    public void update(String str, int i, int i2, int i3, int i4, int i5, int i6, String str2, String[] strArr) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.RGBW)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            contentValues.put("WHITE", Integer.valueOf(i6));
        }
        this.db.update(MySqliteModeHelper.RGBW, contentValues, str2, strArr);
    }

    public void insert(String str, int i, int i2, int i3, int i4, int i5, int i6) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.RGBW)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            contentValues.put("WHITE", Integer.valueOf(i6));
        }
        this.db.insert(MySqliteModeHelper.RGBW, null, contentValues);
    }

    public List<TimeModle> query(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.db.query(str, null, null, null, null, null, "HOUR,MINUTE");
        while (query.moveToNext()) {
            if (str.equals(MySqliteModeHelper.RGBW)) {
                TimeModle timeModle = new TimeModle();
                timeModle.setId(query.getInt(0));
                timeModle.setHour(query.getInt(1));
                timeModle.setMinute(query.getInt(2));
                timeModle.setRed(query.getInt(3));
                timeModle.setGreen(query.getInt(4));
                timeModle.setBlude(query.getInt(5));
                timeModle.setWhite(query.getInt(6));
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
