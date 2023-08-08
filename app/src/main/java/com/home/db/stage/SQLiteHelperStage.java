package com.home.db.stage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class SQLiteHelperStage extends SQLiteOpenHelper {
    private static final String dbname_stage = "groupstage.db";
    private static final int version_stage = 1;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public SQLiteHelperStage(Context context) {
        super(context, dbname_stage, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS group_ss ( group_names varchar primary key,is_on_stage varchar ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS group_content_name_stage ( address_stage varchar,groupName_stage varchar ) ");
    }
}
