package com.home.db.wifi;

import com.common.bean.IBeanInterface;

/* loaded from: classes.dex */
public class GroupDeviceWiFi implements IBeanInterface {
    public static final String ADDRESSNUM_WIFI = "address_wifi";
    public static final String GROUPNUM_WIFI = "groupName_wifi";
    public static final String GROUP_CONTENT_TAB_WIFI = "group_content_name";
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
