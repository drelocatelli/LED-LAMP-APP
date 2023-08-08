package com.itheima.wheelpicker.model;

import java.io.Serializable;
import java.util.List;

/* loaded from: classes.dex */
public class City implements Serializable {
    public List<String> area;
    public String name;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<String> getArea() {
        return this.area;
    }

    public void setArea(List<String> list) {
        this.area = list;
    }
}
