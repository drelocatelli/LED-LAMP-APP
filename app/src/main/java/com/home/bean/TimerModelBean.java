package com.home.bean;

import com.common.bean.IBeanInterface;

/* loaded from: classes.dex */
public class TimerModelBean implements IBeanInterface {
    public static final String groupNameDefaut = "tagTotal";
    private static final long serialVersionUID = 1;
    private boolean CloseTurnOn;
    private boolean OpenTurnOn;
    private String groupName;
    private int model;
    private String modelText;
    private int timerOffHour;
    private int timerOffMinute;
    private int timerOnHour;
    private int timerOnMinute;

    public int getModel() {
        return this.model;
    }

    public void setModel(int i) {
        this.model = i;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getModelText() {
        return this.modelText;
    }

    public void setModelText(String str) {
        this.modelText = str;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public boolean isOpenTurnOn() {
        return this.OpenTurnOn;
    }

    public void setOpenTurnOn(boolean z) {
        this.OpenTurnOn = z;
    }

    public boolean isCloseTurnOn() {
        return this.CloseTurnOn;
    }

    public void setCloseTurnOn(boolean z) {
        this.CloseTurnOn = z;
    }

    public int getTimerOnHour() {
        return this.timerOnHour;
    }

    public void setTimerOnHour(int i) {
        this.timerOnHour = i;
    }

    public int getTimerOnMinute() {
        return this.timerOnMinute;
    }

    public void setTimerOnMinute(int i) {
        this.timerOnMinute = i;
    }

    public int getTimerOffHour() {
        return this.timerOffHour;
    }

    public void setTimerOffHour(int i) {
        this.timerOffHour = i;
    }

    public int getTimerOffMinute() {
        return this.timerOffMinute;
    }

    public void setTimerOffMinute(int i) {
        this.timerOffMinute = i;
    }

    public String toString() {
        return "" + this.groupName + ":" + this.timerOnHour + " :" + this.timerOnMinute + " " + this.modelText + " |" + this.timerOffHour + " " + this.timerOnMinute;
    }
}
