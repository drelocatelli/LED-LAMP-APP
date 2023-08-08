package com.forum.bean;

import java.util.List;

/* loaded from: classes.dex */
public class PageBean<T> {
    private String count;
    private String isLast;
    private List<T> list;

    public PageBean() {
    }

    public PageBean(String str, List<T> list) {
        this.isLast = str;
        this.list = list;
    }

    public String getIsLast() {
        return this.isLast;
    }

    public void setIsLast(String str) {
        this.isLast = str;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String str) {
        this.count = str;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
