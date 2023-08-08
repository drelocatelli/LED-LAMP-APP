package com.home.activity.set.timer.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.home.constant.CommonConstant;

/* loaded from: classes.dex */
public class MySqliteHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "led";
    public static final int VERSER = 1;
    private String[] tables;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public MySqliteHelper(Context context) {
        super(context, DBNAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.tables = new String[]{"LEDBLE00", "LEDBLE01", "LEDDMX00", "LEDDMX01", "LEDDMX02", "LEDDMX03", "LEDCAR00", "LEDCAR01", CommonConstant.LEDSMART, CommonConstant.LEDSTAGE, CommonConstant.LEDLIGHT, CommonConstant.LEDSUN, CommonConstant.LEDLIKE};
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        for (int i = 0; i < this.tables.length; i++) {
            sQLiteDatabase.execSQL("create table if not exists " + this.tables[i] + "(_id integer primary key autoincrement,HOUR integer,MINUTE integer,MODE nvarchar,WEEK nvarchar,SWITCH integer)");
        }
    }
}
