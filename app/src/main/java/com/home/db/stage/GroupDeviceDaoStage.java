package com.home.db.stage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class GroupDeviceDaoStage {
    private SQLiteHelperStage sqLiteHelper;

    public GroupDeviceDaoStage(Context context) {
        this.sqLiteHelper = new SQLiteHelperStage(context);
    }

    public ArrayList<GroupStage> getAllgroup() {
        ArrayList<GroupStage> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_ss", new String[0]);
        while (rawQuery.moveToNext()) {
            GroupStage groupStage = new GroupStage();
            groupStage.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupStage.GROUP_NAME_STAGE)));
            groupStage.setIsOn(rawQuery.getString(rawQuery.getColumnIndex(GroupStage.GROUP_ISON_STAGE)));
            arrayList.add(groupStage);
        }
        writableDatabase.close();
        return arrayList;
    }

    public void updateGroupStatus(ArrayList<GroupStage> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            GroupStage groupStage = arrayList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(GroupStage.GROUP_ISON_STAGE, groupStage.getIsOn());
            writableDatabase.update(GroupStage.GROUP_TAB_STAGE, contentValues, "group_names =?", new String[]{groupStage.getGroupName()});
        }
        writableDatabase.close();
    }

    public ArrayList<GroupDeviceStage> getAllGroupDevices() {
        ArrayList<GroupDeviceStage> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_content_name_stage", new String[0]);
        while (rawQuery.moveToNext()) {
            GroupDeviceStage groupDeviceStage = new GroupDeviceStage();
            groupDeviceStage.setAddress(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceStage.ADDRESSNUM_STAGE)));
            groupDeviceStage.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceStage.GROUPNUM_STAGE)));
            arrayList.add(groupDeviceStage);
        }
        writableDatabase.close();
        return arrayList;
    }

    public void delteByGroup(String str) {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        writableDatabase.execSQL("DELETE FROM group_content_name_stage WHERE groupName_stage = ? ", new Object[]{str});
        writableDatabase.close();
    }

    public ArrayList<GroupDeviceStage> getDevicesByGroup(String str) {
        ArrayList<GroupDeviceStage> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_content_name_stage where groupName_stage =?", new String[]{str});
        while (rawQuery.moveToNext()) {
            GroupDeviceStage groupDeviceStage = new GroupDeviceStage();
            groupDeviceStage.setAddress(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceStage.ADDRESSNUM_STAGE)));
            groupDeviceStage.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceStage.GROUPNUM_STAGE)));
            arrayList.add(groupDeviceStage);
        }
        writableDatabase.close();
        return arrayList;
    }

    public void addGroup(String str) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupStage.GROUP_NAME_STAGE, str);
        writableDatabase.insert(GroupStage.GROUP_TAB_STAGE, null, contentValues);
        writableDatabase.close();
    }

    public void save2Group(ArrayList<GroupDeviceStage> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        Iterator<GroupDeviceStage> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupDeviceStage next = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put(GroupDeviceStage.ADDRESSNUM_STAGE, next.getAddress());
            contentValues.put(GroupDeviceStage.GROUPNUM_STAGE, next.getGroupName());
            writableDatabase.insert(GroupDeviceStage.GROUP_CONTENT_TAB_STAGE, null, contentValues);
        }
        writableDatabase.close();
    }

    public void deleteGroup(String str) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        writableDatabase.execSQL("DELETE FROM group_ss WHERE group_names = ?", new Object[]{str});
        writableDatabase.close();
    }

    public void deleteGroupDevice(ArrayList<GroupDeviceStage> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        Iterator<GroupDeviceStage> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupDeviceStage next = it.next();
            writableDatabase.execSQL("DELETE FROM group_content_name_stage WHERE address_stage = ? & groupName_stage = ? ", new Object[]{next.getAddress(), next.getGroupName()});
        }
        writableDatabase.close();
    }
}
