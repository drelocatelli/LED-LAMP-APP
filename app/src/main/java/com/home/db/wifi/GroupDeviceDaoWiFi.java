package com.home.db.wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class GroupDeviceDaoWiFi {
    private SQLiteHelperWiFi sqLiteHelper;

    public GroupDeviceDaoWiFi(Context context) {
        this.sqLiteHelper = new SQLiteHelperWiFi(context);
    }

    public ArrayList<GroupWiFi> getAllgroup() {
        ArrayList<GroupWiFi> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_sw", new String[0]);
        while (rawQuery.moveToNext()) {
            GroupWiFi groupWiFi = new GroupWiFi();
            groupWiFi.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupWiFi.GROUP_NAME_WIFI)));
            groupWiFi.setIsOn(rawQuery.getString(rawQuery.getColumnIndex(GroupWiFi.GROUP_ISON_WIFI)));
            arrayList.add(groupWiFi);
        }
        writableDatabase.close();
        return arrayList;
    }

    public void updateGroupStatus(ArrayList<GroupWiFi> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            GroupWiFi groupWiFi = arrayList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(GroupWiFi.GROUP_ISON_WIFI, groupWiFi.getIsOn());
            writableDatabase.update(GroupWiFi.GROUP_TAB_WIFI, contentValues, "group_namew =?", new String[]{groupWiFi.getGroupName()});
        }
        writableDatabase.close();
    }

    public ArrayList<GroupDeviceWiFi> getAllGroupDevices() {
        ArrayList<GroupDeviceWiFi> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_content_name", new String[0]);
        while (rawQuery.moveToNext()) {
            GroupDeviceWiFi groupDeviceWiFi = new GroupDeviceWiFi();
            groupDeviceWiFi.setAddress(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceWiFi.ADDRESSNUM_WIFI)));
            groupDeviceWiFi.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceWiFi.GROUPNUM_WIFI)));
            arrayList.add(groupDeviceWiFi);
        }
        writableDatabase.close();
        return arrayList;
    }

    public void delteByGroup(String str) {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        writableDatabase.execSQL("DELETE FROM group_content_name WHERE groupName_wifi = ? ", new Object[]{str});
        writableDatabase.close();
    }

    public ArrayList<GroupDeviceWiFi> getDevicesByGroup(String str) {
        ArrayList<GroupDeviceWiFi> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from group_content_name where groupName_wifi =?", new String[]{str});
        while (rawQuery.moveToNext()) {
            GroupDeviceWiFi groupDeviceWiFi = new GroupDeviceWiFi();
            groupDeviceWiFi.setAddress(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceWiFi.ADDRESSNUM_WIFI)));
            groupDeviceWiFi.setGroupName(rawQuery.getString(rawQuery.getColumnIndex(GroupDeviceWiFi.GROUPNUM_WIFI)));
            arrayList.add(groupDeviceWiFi);
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
        contentValues.put(GroupWiFi.GROUP_NAME_WIFI, str);
        writableDatabase.insert(GroupWiFi.GROUP_TAB_WIFI, null, contentValues);
        writableDatabase.close();
    }

    public void save2Group(ArrayList<GroupDeviceWiFi> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        Iterator<GroupDeviceWiFi> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupDeviceWiFi next = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put(GroupDeviceWiFi.ADDRESSNUM_WIFI, next.getAddress());
            contentValues.put(GroupDeviceWiFi.GROUPNUM_WIFI, next.getGroupName());
            writableDatabase.insert("group_content_name", null, contentValues);
        }
        writableDatabase.close();
    }

    public void deleteGroup(String str) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        writableDatabase.execSQL("DELETE FROM group_sw WHERE group_namew = ?", new Object[]{str});
        writableDatabase.close();
    }

    public void deleteGroupDevice(ArrayList<GroupDeviceWiFi> arrayList) throws Exception {
        SQLiteDatabase writableDatabase = this.sqLiteHelper.getWritableDatabase();
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            return;
        }
        Iterator<GroupDeviceWiFi> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupDeviceWiFi next = it.next();
            writableDatabase.execSQL("DELETE FROM group_content_name WHERE address_wifi = ? & groupName_wifi = ? ", new Object[]{next.getAddress(), next.getGroupName()});
        }
        writableDatabase.close();
    }
}
