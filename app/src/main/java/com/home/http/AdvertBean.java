package com.home.http;

import java.io.Serializable;

/* loaded from: classes.dex */
public class AdvertBean implements Serializable {
    private static final long serialVersionUID = 1;
    private int advertId;
    private String advertUrl;
    private int appId;
    private String describe;
    private String imageUrl;
    private String imageVisitUrl;
    private String uploadTime;

    public AdvertBean() {
    }

    public AdvertBean(int i, String str, String str2, String str3, String str4, String str5, int i2) {
        this.advertId = i;
        this.describe = str;
        this.advertUrl = str2;
        this.imageUrl = str3;
        this.imageVisitUrl = str4;
        this.uploadTime = str5;
        this.appId = i2;
    }

    public int getAdvertId() {
        return this.advertId;
    }

    public void setAdvertId(int i) {
        this.advertId = i;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String str) {
        this.describe = str;
    }

    public String getAdvertUrl() {
        return this.advertUrl;
    }

    public void setAdvertUrl(String str) {
        this.advertUrl = str;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    public String getImageVisitUrl() {
        return this.imageVisitUrl;
    }

    public void setImageVisitUrl(String str) {
        this.imageVisitUrl = str;
    }

    public String getUploadTime() {
        return this.uploadTime;
    }

    public void setUploadTime(String str) {
        this.uploadTime = str;
    }

    public int getAppId() {
        return this.appId;
    }

    public void setAppId(int i) {
        this.appId = i;
    }
}
