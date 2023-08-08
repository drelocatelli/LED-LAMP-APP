package com.home.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.common.uitl.LogUtil;
import com.home.base.LedBleApplication;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class GroupDeviceDao {
    private SQLiteHelper sqLiteHelper;

    public GroupDeviceDao(Context context) {
        this.sqLiteHelper = new SQLiteHelper(context);
    }

    public ArrayList<Group> getAllgroup() {
        ArrayList<Group> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_s", new String[0]);
        while (rawQuery.moveToNext()) {
            Group group = new Group();
            group.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(Group.GROUP_NAME)));
            group.setIsOn(rawQuery.getString(rawQuery.getColumnIndex(Group.GROUP_ISON)));
            arrayList.add(group);
            LogUtil.i(LedBleApplication.tag, "group:" + group.getGroupName() + " is on:" + group.getIsOn());
        }
        writableDatabase.close();
        return arrayList;
    }

    public void updateGroupStatus(ArrayList<Group> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Group group = arrayList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Group.GROUP_ISON, group.getIsOn());
            writableDatabase.update(Group.GROUP_TAB, contentValues, "group_name =?", new String[]{group.getGroupName()});
        }
        writableDatabase.close();
    }

    public ArrayList<GroupDevice> getAllGroupDevices() {
        ArrayList<GroupDevice> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_content_name", new String[0]);
        while (rawQuery.moveToNext()) {
            GroupDevice groupDevice = new GroupDevice();
            groupDevice.setAddress(rawQuery.getString(rawQuery.getColumnIndex(GroupDevice.ADDRESSNUM)));
            groupDevice.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupDevice.GROUPNUM)));
            arrayList.add(groupDevice);
        }
        writableDatabase.close();
        return arrayList;
    }

    public void delteByGroup(String str) {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        writableDatabase.execSQL("DELETE FROM group_content_name WHERE groupName = ? ", new Object[]{str});
        writableDatabase.close();
    }

    public ArrayList<GroupDevice> getDevicesByGroup(String str) {
        ArrayList<GroupDevice> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_content_name where groupName =?", new String[]{str});
        while (rawQuery.moveToNext()) {
            GroupDevice groupDevice = new GroupDevice();
            groupDevice.setAddress(rawQuery.getString(rawQuery.getColumnIndex(GroupDevice.ADDRESSNUM)));
            groupDevice.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupDevice.GROUPNUM)));
            arrayList.add(groupDevice);
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
        contentValues.put(Group.GROUP_NAME, str);
        writableDatabase.insert(Group.GROUP_TAB, null, contentValues);
        writableDatabase.close();
    }

    public void save2Group(ArrayList<GroupDevice> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        Iterator<GroupDevice> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupDevice next = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put(GroupDevice.ADDRESSNUM, next.getAddress());
            contentValues.put(GroupDevice.GROUPNUM, next.getGroupName());
            writableDatabase.insert("group_content_name", null, contentValues);
        }
        writableDatabase.close();
    }

    public void deleteGroup(String str) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        writableDatabase.execSQL("DELETE FROM group_s WHERE group_name = ?", new Object[]{str});
        writableDatabase.close();
    }

    public void deleteGroupDevice(ArrayList<GroupDevice> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        Iterator<GroupDevice> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupDevice next = it.next();
            writableDatabase.execSQL("DELETE FROM group_content_name WHERE address = ? & groupName = ? ", new Object[]{next.getAddress(), next.getGroupName()});
        }
        writableDatabase.close();
    }
}
