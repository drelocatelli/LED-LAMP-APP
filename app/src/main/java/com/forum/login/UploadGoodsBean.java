package com.forum.login;

import java.io.Serializable;

/* loaded from: classes.dex */
public class UploadGoodsBean implements Serializable {
    private Boolean isNet;
    private String url;

    public UploadGoodsBean(String str) {
        this.url = str;
    }

    public UploadGoodsBean(String str, Boolean bool) {
        this.url = str;
        this.isNet = bool;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public Boolean getIsNet() {
        return this.isNet;
    }

    public void setIsNet(Boolean bool) {
        this.isNet = bool;
    }
}
