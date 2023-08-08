package com.forum.im.db;

/* loaded from: classes.dex */
public class ChatMessageBean {
    private String UserContent;
    private String UserHeadIcon;
    private String UserId;
    private String UserName;
    private String UserVoicePath;
    private float UserVoiceTime;
    private String UserVoiceUrl;
    private Long id;
    private String imageIconUrl;
    private String imageLocal;
    private String imageUrl;
    private int messagetype;
    private int sendState;
    private String time;
    private int type;

    public ChatMessageBean(Long l, String str, String str2, String str3, String str4, String str5, int i, int i2, float f, String str6, String str7, int i3, String str8, String str9, String str10) {
        this.id = l;
        this.UserId = str;
        this.UserName = str2;
        this.UserHeadIcon = str3;
        this.UserContent = str4;
        this.time = str5;
        this.type = i;
        this.messagetype = i2;
        this.UserVoiceTime = f;
        this.UserVoicePath = str6;
        this.UserVoiceUrl = str7;
        this.sendState = i3;
        this.imageUrl = str8;
        this.imageIconUrl = str9;
        this.imageLocal = str10;
    }

    public ChatMessageBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String str) {
        this.UserId = str;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String str) {
        this.UserName = str;
    }

    public String getUserHeadIcon() {
        return this.UserHeadIcon;
    }

    public void setUserHeadIcon(String str) {
        this.UserHeadIcon = str;
    }

    public String getUserContent() {
        return this.UserContent;
    }

    public void setUserContent(String str) {
        this.UserContent = str;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getMessagetype() {
        return this.messagetype;
    }

    public void setMessagetype(int i) {
        this.messagetype = i;
    }

    public float getUserVoiceTime() {
        return this.UserVoiceTime;
    }

    public void setUserVoiceTime(float f) {
        this.UserVoiceTime = f;
    }

    public String getUserVoicePath() {
        return this.UserVoicePath;
    }

    public void setUserVoicePath(String str) {
        this.UserVoicePath = str;
    }

    public String getUserVoiceUrl() {
        return this.UserVoiceUrl;
    }

    public void setUserVoiceUrl(String str) {
        this.UserVoiceUrl = str;
    }

    public int getSendState() {
        return this.sendState;
    }

    public void setSendState(int i) {
        this.sendState = i;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    public String getImageIconUrl() {
        return this.imageIconUrl;
    }

    public void setImageIconUrl(String str) {
        this.imageIconUrl = str;
    }

    public String getImageLocal() {
        return this.imageLocal;
    }

    public void setImageLocal(String str) {
        this.imageLocal = str;
    }
}
