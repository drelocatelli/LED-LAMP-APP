package com.common.net;

import java.io.Serializable;

/* loaded from: classes.dex */
public class NetResult implements Serializable {
    public static final String CODE_ERROR = "10000";
    public static final String CODE_OK = "0";
    private String code;
    private Object[] data;
    private boolean isSuccess;
    private String message;
    private Object tag;

    public Object getTag() {
        return this.tag;
    }

    public void setTag(Object obj) {
        this.tag = obj;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public Object[] getData() {
        return this.data;
    }

    public void setData(Object[] objArr) {
        this.data = objArr;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean z) {
        this.isSuccess = z;
    }
}
