package com.home.activity.set.smart.modeTime;

import java.io.Serializable;

/* loaded from: classes.dex */
public class TimeModle implements Serializable {
    private int blude;
    private int cyan;
    private int green;
    private int hour;
    private int id;
    private int minute;
    private int red;
    private int violet;
    private int white;

    public TimeModle() {
    }

    public TimeModle(int i, int i2, int i3, int i4, int i5) {
        this.hour = i;
        this.minute = i2;
        this.red = i3;
        this.green = i4;
        this.blude = i5;
    }

    public TimeModle(int i, int i2, int i3, int i4, int i5, int i6) {
        this.hour = i;
        this.minute = i2;
        this.red = i3;
        this.green = i4;
        this.blude = i5;
        this.white = i6;
    }

    public TimeModle(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.hour = i;
        this.minute = i2;
        this.red = i3;
        this.green = i4;
        this.blude = i5;
        this.white = i6;
        this.cyan = i7;
        this.violet = i8;
    }

    public void setMinute(int i) {
        this.minute = i;
    }

    public void setBlude(int i) {
        this.blude = i;
    }

    public void setCyan(int i) {
        this.cyan = i;
    }

    public void setGreen(int i) {
        this.green = i;
    }

    public void setHour(int i) {
        this.hour = i;
    }

    public void setRed(int i) {
        this.red = i;
    }

    public void setWhite(int i) {
        this.white = i;
    }

    public void setViolet(int i) {
        this.violet = i;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getBlude() {
        return this.blude;
    }

    public int getCyan() {
        return this.cyan;
    }

    public int getGreen() {
        return this.green;
    }

    public int getHour() {
        return this.hour;
    }

    public int getRed() {
        return this.red;
    }

    public int getViolet() {
        return this.violet;
    }

    public int getWhite() {
        return this.white;
    }
}
