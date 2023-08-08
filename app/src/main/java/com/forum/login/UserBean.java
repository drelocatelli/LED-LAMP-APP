package com.forum.login;

/* loaded from: classes.dex */
public class UserBean {
    private String checkMode;
    private String headImage;
    private int id;
    private String token;
    private String userName;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public String getCheckMode() {
        return this.checkMode;
    }

    public void setCheckMode(String str) {
        this.checkMode = str;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public void setHeadImage(String str) {
        this.headImage = str;
    }

    public String getHeadImage() {
        return this.headImage;
    }
}
