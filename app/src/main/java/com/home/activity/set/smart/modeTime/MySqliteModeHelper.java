package com.home.activity.set.smart.modeTime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class MySqliteModeHelper extends SQLiteOpenHelper {
    public static final String BW = "bw";
    public static final String BWONE = "bwone";
    private static final String DBNAME = "time";
    public static final String RGB = "rgb";
    public static final String RGBONE = "rgbone";
    public static final String RGBW = "rgbw";
    public static final String RGBWCP = "rgbwcp";
    public static final String RGBWCPONE = "rgbwcpone";
    public static final String RGBWONE = "rgbwone";
    public static final int VERSER = 1;
    public static final String W = "w";
    public static final String WONE = "wone";
    public String[] tables;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public MySqliteModeHelper(Context context) {
        super(context, DBNAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.tables = new String[]{W, BW, RGB, RGBW, RGBWCP, WONE, BWONE, RGBONE, RGBWONE, RGBWCPONE};
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        for (int i = 0; i < this.tables.length; i++) {
            sQLiteDatabase.execSQL("create table if not exists " + this.tables[i] + "(_id integer primary key autoincrement,HOUR integer,MINUTE integer,RED integer,GREEN integer,BLUDE integer,WHITE integer,CYAN integer,VIOLET integer)");
        }
    }
}
