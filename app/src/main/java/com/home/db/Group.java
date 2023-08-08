package com.home.db;

/* loaded from: classes.dex */
public class Group {
    public static final String GROUP_ISON = "is_on";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_TAB = "group_s";
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
