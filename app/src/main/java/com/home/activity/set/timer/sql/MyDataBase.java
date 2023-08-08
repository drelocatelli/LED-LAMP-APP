package com.home.activity.set.timer.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.home.base.LedBleApplication;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MyDataBase {
    static Context mContext;
    private static MyDataBase myDataBase;
    private static MySqliteHelper sqliteHelper;
    private SQLiteDatabase db;

    private MyDataBase(Context context) {
        MySqliteHelper mySqliteHelper = new MySqliteHelper(context);
        sqliteHelper = mySqliteHelper;
        this.db = mySqliteHelper.getWritableDatabase();
    }

    public static MyDataBase getInstance(Context context) {
        if (myDataBase == null) {
            myDataBase = new MyDataBase(context);
        }
        return myDataBase;
    }

    public void update(String str, int i, int i2, String str2, String str3, int i3, String str4, String[] strArr) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(LedBleApplication.getApp().getSceneBean())) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("MODE", str2);
            contentValues.put("WEEK", str3);
            contentValues.put("SWITCH", Integer.valueOf(i3));
        }
        this.db.update(LedBleApplication.getApp().getSceneBean(), contentValues, str4, strArr);
    }

    public void insert(String str, int i, int i2, String str2, String str3, int i3) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(LedBleApplication.getApp().getSceneBean())) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("MODE", str2);
            contentValues.put("WEEK", str3);
            contentValues.put("SWITCH", Integer.valueOf(i3));
        }
        this.db.insert(LedBleApplication.getApp().getSceneBean(), null, contentValues);
    }

    public List<RecordMode> query(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.db.query(str, null, null, null, null, null, null);
        while (query.moveToNext()) {
            if (str.equals(LedBleApplication.getApp().getSceneBean())) {
                RecordMode recordMode = new RecordMode();
                recordMode.setId(query.getInt(0));
                recordMode.setHour(query.getInt(1));
                recordMode.setMinute(query.getInt(2));
                recordMode.setMode(query.getString(3));
                recordMode.setWeek(query.getString(4));
                recordMode.setType(query.getInt(5));
                arrayList.add(recordMode);
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
