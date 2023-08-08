package com.home.db.stage;

/* loaded from: classes.dex */
public class GroupStage {
    public static final String GROUP_ISON_STAGE = "is_on_stage";
    public static final String GROUP_NAME_STAGE = "group_names";
    public static final String GROUP_TAB_STAGE = "group_ss";
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
