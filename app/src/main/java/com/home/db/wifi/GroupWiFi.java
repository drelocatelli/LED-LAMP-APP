package com.home.db.wifi;

/* loaded from: classes.dex */
public class GroupWiFi {
    public static final String GROUP_ISON_WIFI = "is_on_wifi";
    public static final String GROUP_NAME_WIFI = "group_namew";
    public static final String GROUP_TAB_WIFI = "group_sw";
    private String groupName;
    private String isOn;

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public String getIsOn() {
        return this.isOn;
    }

    public void setIsOn(String str) {
        this.isOn = str;
    }
}
