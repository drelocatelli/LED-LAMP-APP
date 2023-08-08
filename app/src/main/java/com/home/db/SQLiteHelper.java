package com.home.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String dbname = "group.db";
    private static final int version = 1;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public SQLiteHelper(Context context) {
        super(context, dbname, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS group_s ( group_name varchar primary key,is_on varchar ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS group_content_name ( address varchar,groupName varchar ) ");
    }
}
