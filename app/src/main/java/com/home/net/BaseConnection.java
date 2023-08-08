package com.home.net;

/* loaded from: classes.dex */
public abstract class BaseConnection {
    public abstract void close() throws Exception;

    public abstract void closeSocket();

    public abstract void configSPI(int i, byte b, byte b2, int i2) throws Exception;

    public abstract boolean connect();

    public abstract boolean isOnLine();

    public abstract void open() throws Exception;

    public abstract void pauseSPI(int i) throws Exception;

    public abstract void sendPassword(String str) throws Exception;

    public abstract void sendRouteCommand(String str, String str2) throws Exception;

    public abstract void sendRoutePassword() throws Exception;

    public abstract void sendRouteSSID() throws Exception;

    public abstract void sendSSID(String str) throws Exception;

    public abstract void sendSearchOnlineDevices() throws Exception;

    public abstract void setBrightness(int i) throws Exception;

    public abstract void setColorWarm(int i, int i2) throws Exception;

    public abstract void setColorWarmModel(int i) throws Exception;

    public abstract void setDim(int i) throws Exception;

    public abstract void setDimModel(int i) throws Exception;

    public abstract void setMusic(int i, int i2, int i3, int i4) throws Exception;

    public abstract void setRgb(int i, int i2, int i3, String str) throws Exception;

    public abstract void setRgbMode(int i) throws Exception;

    public abstract void setSPIBrightness(int i) throws Exception;

    public abstract void setSPISpeed(int i) throws Exception;

    public abstract void setSpeed(int i) throws Exception;

    public abstract void turnOnSPI(int i) throws Exception;

    public abstract void write(int[] iArr) throws Exception;

    public abstract void writeByte(byte[] bArr) throws Exception;
}
