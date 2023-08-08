package com.home.db.stage;

import com.common.bean.IBeanInterface;

/* loaded from: classes.dex */
public class GroupDeviceStage implements IBeanInterface {
    public static final String ADDRESSNUM_STAGE = "address_stage";
    public static final String GROUPNUM_STAGE = "groupName_stage";
    public static final String GROUP_CONTENT_TAB_STAGE = "group_content_name_stage";
    private static final long serialVersionUID = 1;
    private String address;
    private String groupName;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }
}
