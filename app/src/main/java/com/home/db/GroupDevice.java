package com.home.db;

import com.common.bean.IBeanInterface;

/* loaded from: classes.dex */
public class GroupDevice implements IBeanInterface {
    public static final String ADDRESSNUM = "address";
    public static final String GROUPNUM = "groupName";
    public static final String GROUP_CONTENT_TAB = "group_content_name";
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
