package com.home.activity.set.smart.modeTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RGBDataBase {
    public static RGBDataBase mrgb;
    private static MySqliteModeHelper mySqliteModeHelper;
    private SQLiteDatabase db;

    private RGBDataBase(Context context) {
        MySqliteModeHelper mySqliteModeHelper2 = new MySqliteModeHelper(context);
        mySqliteModeHelper = mySqliteModeHelper2;
        this.db = mySqliteModeHelper2.getWritableDatabase();
    }

    public static RGBDataBase getInstance(Context context) {
        if (mrgb == null) {
            mrgb = new RGBDataBase(context);
        }
        return mrgb;
    }

    public void update(String str, int i, int i2, int i3, int i4, int i5, String str2, String[] strArr) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.RGB)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            this.db.update(MySqliteModeHelper.RGB, contentValues, str2, strArr);
        }
    }

    public void insert(String str, int i, int i2, int i3, int i4, int i5) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.RGB)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            this.db.insert(MySqliteModeHelper.RGB, null, contentValues);
        }
    }

    public List<TimeModle> query(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.db.query(str, null, null, null, null, null, "HOUR,MINUTE");
        while (query.moveToNext()) {
            if (str.equals(MySqliteModeHelper.RGB)) {
                TimeModle timeModle = new TimeModle();
                timeModle.setId(query.getInt(0));
                timeModle.setHour(query.getInt(1));
                timeModle.setMinute(query.getInt(2));
                timeModle.setRed(query.getInt(3));
                timeModle.setGreen(query.getInt(4));
                timeModle.setBlude(query.getInt(5));
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

    public void deleteDatebase(String str) {
        SQLiteDatabase sQLiteDatabase = this.db;
        sQLiteDatabase.execSQL("DELETE FROM " + str);
    }
}
