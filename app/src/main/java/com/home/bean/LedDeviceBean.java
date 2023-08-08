package com.home.bean;

import com.common.bean.IBeanInterface;

/* loaded from: classes.dex */
public class LedDeviceBean implements IBeanInterface {
    private static final long serialVersionUID = 1;
    private String ip;
    private boolean isOnline;
    private String mac;
    private String name;

    public LedDeviceBean(String str, String str2, String str3, boolean z) {
        this.ip = str3;
        this.isOnline = z;
        this.name = str;
        this.mac = str2;
    }

    public LedDeviceBean() {
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String str) {
        this.ip = str;
    }

    public boolean isOnline() {
        return this.isOnline;
    }

    public void setOnline(boolean z) {
        this.isOnline = z;
    }

    public LedDeviceBean(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
