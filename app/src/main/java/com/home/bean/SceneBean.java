package com.home.bean;

import com.common.bean.IBeanInterface;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SceneBean implements IBeanInterface, Serializable {
    private static final long serialVersionUID = 1;
    private byte[] img;
    private String name;
    private HashMap<String, LedDeviceBean> onlineDevices;
    private int sceneType;
    private String service;
    private TimerModelBean timer;

    public SceneBean() {
    }

    public SceneBean(String str, String str2, byte[] bArr, int i) {
        this.name = str;
        this.service = str2;
        this.img = bArr;
        this.sceneType = i;
    }

    public TimerModelBean getTimer() {
        return this.timer;
    }

    public void setTimer(TimerModelBean timerModelBean) {
        this.timer = timerModelBean;
    }

    public HashMap<String, LedDeviceBean> getOnlineDevices() {
        return this.onlineDevices;
    }

    public void setOnlineDevices(HashMap<String, LedDeviceBean> hashMap) {
        this.onlineDevices = hashMap;
    }

    public int getSceneType() {
        return this.sceneType;
    }

    public void setSceneType(int i) {
        this.sceneType = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setService(String str) {
        this.service = str;
    }

    public String getSerice() {
        return this.service;
    }

    public byte[] getImg() {
        return this.img;
    }

    public void setImg(byte[] bArr) {
        this.img = bArr;
    }

    public int hashCode() {
        String str = this.name;
        return 31 + (str == null ? 0 : str.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            SceneBean sceneBean = (SceneBean) obj;
            String str = this.name;
            if (str == null) {
                if (sceneBean.name != null) {
                    return false;
                }
            } else if (!str.equals(sceneBean.name)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
