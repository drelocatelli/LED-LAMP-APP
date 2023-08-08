package com.home.base;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.linechartlibrary.MyApp;
import com.finddreams.languagelib.MultiLanguageUtil;
import com.forum.im.db.base.BaseManager;
import com.forum.login.UserBean;
import com.home.adapter.BluetoothDataModel;
import com.home.bean.Mp3;
import com.home.constant.Constant;
import com.home.utils.font.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/* loaded from: classes.dex */
public class LedBleApplication extends Application {
    public static LedBleApplication app = null;
    private static Context applicationContext = null;
    private static boolean auto = false;
    private static boolean canConnect = false;
    private static SharedPreferences sf = null;
    public static final String tag = "ble";
    private ArrayList<BluetoothDevice> bleDevices;
    private HashMap<String, Boolean> hashMapConnect;
    private HashMap<String, BluetoothGatt> hashMapGatt;
    private ArrayList<BluetoothDataModel> manmualBleDevices;
    private ArrayList<Mp3> mp3s;
    private int safetyLevel;
    private String sceneBean;
    private Set<BluetoothDevice> tempDevices;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        BaseManager.initOpenHelper(this);
        Context applicationContext2 = getApplicationContext();
        applicationContext = applicationContext2;
        MyApp.setInstance(applicationContext2);
        this.bleDevices = new ArrayList<>();
        this.hashMapConnect = new HashMap<>();
        this.hashMapGatt = new HashMap<>();
        this.manmualBleDevices = new ArrayList<>();
        this.mp3s = new ArrayList<>();
        sf = getSharedPreferences(Constant.USER_INFO, 0);
        app = this;
        MultiLanguageUtil.init(this);
    }

    public static Context getInstance() {
        return applicationContext;
    }

    public boolean isAuto() {
        return auto;
    }

    public boolean isCanConnect() {
        return canConnect;
    }

    public void setAuto(boolean z) {
        auto = z;
    }

    public void setCanConnect(boolean z) {
        canConnect = z;
    }

    public static LedBleApplication getApp() {
        return app;
    }

    public void setSceneBean(String str) {
        this.sceneBean = str;
    }

    public String getSceneBean() {
        return this.sceneBean;
    }

    public ArrayList<BluetoothDevice> getBleDevices() {
        return this.bleDevices;
    }

    public HashMap<String, Boolean> getHashMapConnect() {
        return this.hashMapConnect;
    }

    public HashMap<String, BluetoothGatt> getBleGattMap() {
        return this.hashMapGatt;
    }

    public ArrayList<BluetoothDataModel> getManmualBleDevices() {
        return this.manmualBleDevices;
    }

    public ArrayList<Mp3> getMp3s() {
        return this.mp3s;
    }

    public void setMp3s(ArrayList<Mp3> arrayList) {
        this.mp3s = arrayList;
    }

    public void clearBleGatMap(String str) {
        BluetoothGatt bluetoothGatt;
        if (this.hashMapGatt.size() <= 0 || str == null || (bluetoothGatt = this.hashMapGatt.get(str)) == null) {
            return;
        }
        bluetoothGatt.disconnect();
    }

    public Set<BluetoothDevice> getTempDevices() {
        return this.tempDevices;
    }

    public void setTempDevices(Set<BluetoothDevice> set) {
        this.tempDevices = set;
    }

    public String getUserToken() {
        return sf.getString(Constant.USER_TOKEN, "");
    }

    public void setUserBean(UserBean userBean) {
        SharedPreferences.Editor edit = sf.edit();
        edit.putString(Constant.USER_NAME, userBean != null ? userBean.getUserName() : "");
        edit.putString(Constant.USER_CHECK_MODE, userBean != null ? userBean.getCheckMode() : "");
        edit.putString(Constant.USER_TOKEN, userBean != null ? userBean.getToken() : "");
        edit.putString(Constant.USER_HEADIMAGE, userBean != null ? userBean.getHeadImage() : "");
        edit.commit();
    }

    public String getUserName() {
        return sf.getString(Constant.USER_NAME, "");
    }

    public String getCheckMode() {
        return sf.getString(Constant.USER_CHECK_MODE, "");
    }

    public String getUserHeadImage() {
        return sf.getString(Constant.USER_HEADIMAGE, "");
    }

    public void setSafetyLevel(int i) {
        this.safetyLevel = i;
    }

    public int getSafetyLevel() {
        return this.safetyLevel;
    }
}
