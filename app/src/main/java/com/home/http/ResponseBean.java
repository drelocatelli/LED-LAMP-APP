package com.home.http;

/* loaded from: classes.dex */
public class ResponseBean<T> {
    private T content;
    private String returnCode;
    private String returnDesc;

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String str) {
        this.returnCode = str;
    }

    public String getReturnDesc() {
        return this.returnDesc;
    }

    public void setReturnDesc(String str) {
        this.returnDesc = str;
    }

    public T getContent() {
        return this.content;
    }

    public void setContent(T t) {
        this.content = t;
    }
}
