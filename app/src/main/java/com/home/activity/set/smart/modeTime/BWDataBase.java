package com.home.activity.set.smart.modeTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class BWDataBase {
    public static BWDataBase bwDataBase;
    private static MySqliteModeHelper mySqliteModeHelper;
    private SQLiteDatabase db;

    private BWDataBase(Context context) {
        MySqliteModeHelper mySqliteModeHelper2 = new MySqliteModeHelper(context);
        mySqliteModeHelper = mySqliteModeHelper2;
        this.db = mySqliteModeHelper2.getWritableDatabase();
    }

    public static BWDataBase getInstance(Context context) {
        if (bwDataBase == null) {
            bwDataBase = new BWDataBase(context);
        }
        return bwDataBase;
    }

    public void update(String str, int i, int i2, int i3, int i4, int i5, int i6, String str2, String[] strArr) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.BW)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            contentValues.put("WHITE", Integer.valueOf(i6));
        }
        this.db.update(MySqliteModeHelper.BW, contentValues, str2, strArr);
    }

    public void insert(String str, int i, int i2, int i3, int i4, int i5, int i6) {
        ContentValues contentValues = new ContentValues();
        if (str.equals(MySqliteModeHelper.BW)) {
            contentValues.put("HOUR", Integer.valueOf(i));
            contentValues.put("MINUTE", Integer.valueOf(i2));
            contentValues.put("RED", Integer.valueOf(i3));
            contentValues.put("GREEN", Integer.valueOf(i4));
            contentValues.put("BLUDE", Integer.valueOf(i5));
            contentValues.put("WHITE", Integer.valueOf(i6));
        }
        this.db.insert(MySqliteModeHelper.BW, null, contentValues);
    }

    public List<TimeModle> query(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.db.query(str, null, null, null, null, null, "HOUR,MINUTE");
        while (query.moveToNext()) {
            if (str.equals(MySqliteModeHelper.BW)) {
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

    public boolean tabbleIsExist(String str) {
        if (str == null) {
            return false;
        }
        try {
            SQLiteDatabase sQLiteDatabase = this.db;
            Cursor rawQuery = sQLiteDatabase.rawQuery("select count(*) as c from Sqlite_master  where type ='table' and name ='" + str.trim() + "' ", null);
            if (rawQuery.moveToNext()) {
                return rawQuery.getInt(0) > 0;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public void create(String str) {
        this.db.execSQL("create table if not exists bw(_id integer primary key autoincrement,HOUR integer,MINUTE integer,RED integer,GREEN integer,BLUDE integer,WHITE integer)");
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
