package com.home.activity.set.timer.sql;

import java.io.Serializable;

/* loaded from: classes.dex */
public class RecordMode implements Serializable, Comparable<RecordMode> {
    private int hour;
    private int id;
    private int minute;
    private String mode;
    private int type;
    private String week;

    public String getMode() {
        return this.mode;
    }

    public void setMode(String str) {
        this.mode = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String str) {
        this.week = str;
    }

    public int getHour() {
        return this.hour;
    }

    public void setHour(int i) {
        this.hour = i;
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

    public void setMinute(int i) {
        this.minute = i;
    }

    @Override // java.lang.Comparable
    public int compareTo(RecordMode recordMode) {
        int hour = hour() - recordMode.hour();
        return hour == 0 ? this.minute - recordMode.minute() : hour;
    }

    private int minute() {
        return this.minute;
    }

    private int hour() {
        return this.hour;
    }
}
