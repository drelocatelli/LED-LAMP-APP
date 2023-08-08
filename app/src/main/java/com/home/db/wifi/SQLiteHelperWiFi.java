package com.home.db.wifi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class SQLiteHelperWiFi extends SQLiteOpenHelper {
    private static final String dbname_wifi = "groupwifi.db";
    private static final int version_wifi = 1;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public SQLiteHelperWiFi(Context context) {
        super(context, dbname_wifi, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS group_sw ( group_namew varchar primary key,is_on_wifi varchar ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS group_content_name ( address_wifi varchar,groupName_wifi varchar ) ");
    }
}
