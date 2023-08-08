package com.home.net;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.common.net.NetResult;
import com.common.uitl.ListUtiles;
import com.common.uitl.LogUtil;
import com.common.uitl.StringUtils;
import com.common.uitl.Tool;
import com.home.activity.main.MainActivity_BLE;
import com.home.activity.main.MainActivity_DMX02;
import com.home.base.LedBleApplication;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.home.db.GroupDevice;
import com.home.db.GroupDeviceDao;
import com.home.fragment.service.ServicesFragment;
import com.home.utils.Utils;
import com.ledlamp.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/* loaded from: classes.dex */
public class NetConnectBle {
    public static final String FFE0 = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String FFE1 = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String FFE2 = "0000ffe2-0000-1000-8000-00805f9b34fb";
    private static final String TAG = "NetConnectBle";
    private static NetConnectBle netConnect;
    public static Set<String> setAddress;
    private NetExceptionInterface exceptionCallBack;
    private ArrayList<GroupDevice> groupDevices;
    private String groupName;
    Handler handler;
    Handler handlerrgb;
    private int k;
    boolean aa = true;
    int idx = 0;

    static /* synthetic */ int access$008(NetConnectBle netConnectBle) {
        int i = netConnectBle.k;
        netConnectBle.k = i + 1;
        return i;
    }

    public NetConnectBle() {
        setAddress = new HashSet();
    }

    public static NetConnectBle getInstance() {
        if (netConnect == null) {
            netConnect = new NetConnectBle();
        }
        return netConnect;
    }

    public static NetConnectBle getInstanceByGroup(String str) {
        if (netConnect == null) {
            netConnect = new NetConnectBle();
        }
        netConnect.setGroupName(str);
        return netConnect;
    }

    public void sendCharacteristic(byte[] bArr) {
        Log.i(TAG, "发送数据aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        HashMap<String, BluetoothGatt> bleGattMap = LedBleApplication.getApp().getBleGattMap();
        if (bleGattMap == null || bleGattMap.isEmpty()) {
            return;
        }
        if (!ListUtiles.isEmpty(this.groupDevices) || StringUtils.isEmpty(this.groupName)) {
            for (Map.Entry<String, BluetoothGatt> entry : bleGattMap.entrySet()) {
                try {
                    BluetoothGatt value = entry.getValue();
                    BluetoothGattService service = value.getService(UUID.fromString(FFE0));
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(FFE1));
                    Log.i(TAG, "发送数据bbbbbbbbbbbbbbbbbbbbbbbb");
                    if (service != null && setAddress.contains(value.getDevice().getAddress()) && characteristic != null) {
                        characteristic.setValue(bArr);
                        value.writeCharacteristic(characteristic);
                        Log.i(TAG, "发送数据ZZZZZZZZZZZZZZZZZZZZZZZZZ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getGroupName() {
        return this.groupName;
    }

    public ArrayList<GroupDevice> getDevicesByGroup() {
        return this.groupDevices;
    }

    public void setGroupName(String str) {
        ArrayList<BluetoothDevice> bleDevices;
        this.groupName = str;
        setAddress.clear();
        GroupDeviceDao groupDeviceDao = new GroupDeviceDao(LedBleApplication.getApp());
        int i = 0;
        if (StringUtils.isEmpty(str)) {
            if (ServicesFragment.sceneBeanFragment == null || ServicesFragment.sceneBeanFragment == null) {
                return;
            }
            if (ServicesFragment.sceneBeanFragment.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                bleDevices = LedBleApplication.getApp().getBleDevices();
            } else {
                bleDevices = LedBleApplication.getApp().getBleDevices();
            }
            if (bleDevices != null) {
                int size = bleDevices.size();
                while (i < size) {
                    setAddress.add(bleDevices.get(i).getAddress());
                    i++;
                }
                return;
            }
            return;
        }
        if (StringUtils.isEmpty(str) || "null".equalsIgnoreCase(str)) {
            this.groupDevices = groupDeviceDao.getAllGroupDevices();
        } else {
            this.groupDevices = groupDeviceDao.getDevicesByGroup(str);
        }
        while (ListUtiles.getListSize(this.groupDevices) > 0 && i < this.groupDevices.size()) {
            setAddress.add(this.groupDevices.get(i).getAddress());
            i++;
        }
    }

    public NetConnectBle(NetExceptionInterface netExceptionInterface) {
        this.exceptionCallBack = netExceptionInterface;
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x017c, code lost:
        if (r24.equalsIgnoreCase("LEDDMX-00-") != false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0182, code lost:
        if (r24.equalsIgnoreCase("LEDDMX-03-") == false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0185, code lost:
        sendTuyaCommand("101", "7b040405ffffffffbf");
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x018c, code lost:
        sendData(new int[]{123, 255, 4, 5, 255, 255, 255, 255, 191});
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01d5, code lost:
        if (r24.equalsIgnoreCase("LEDDMX-01-") == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01d7, code lost:
        sendData(new int[]{123, 4, 4, 5, 255, 255, 255, 255, 191});
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01ef, code lost:
        sendTuyaCommand("101", "7b040405ffffffffbf");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void turnOn(String str, boolean z, boolean z2, boolean z3) {
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                sendData(new int[]{126, 255, 4, 1, 255, 255, 255, 255, 239});
                sendTuyaCommand("101", "7eff0401ffffffffef");
            } else {
                if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                    if (!str.equalsIgnoreCase("LEDCAR-01-")) {
                        if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                            if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                                if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                    sendData(new int[]{125, 1, 1, 1, 255, 255, 255, 255, 223});
                                } else {
                                    if (!str.equalsIgnoreCase(CommonConstant.LEDLIGHT) && !str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                        if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                                            sendData(new int[]{122, 1, 1, 255, 255, 255, 255, 255, 175});
                                        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                                            sendData(new int[]{112, 1, 1, 255, 255, 255, 255, 255, 15});
                                        }
                                    }
                                    sendData(new int[]{126, 255, 4, 1, 255, 255, 255, 255, 239});
                                }
                            }
                            if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                                sendTuyaCommand("101", "7b040401ffffffffbf");
                            }
                            sendData(new int[]{123, 255, 4, 1, 255, 255, 255, 255, 191});
                        }
                        if (str.contains("LEDDMX-01-")) {
                            sendData(new int[]{123, 4, 4, 1, 255, 255, 255, 255, 191});
                        } else {
                            sendTuyaCommand("101", "7b040401ffffffffbf");
                        }
                    } else if (!z2 && !z3) {
                        sendData(new int[]{126, -1, 4, 1, -1, -1, -1, -1, -17});
                    } else if (z3) {
                        sendData(new int[]{123, 255, 4, 7, 255, 255, 255, 255, 191});
                    } else if (z2) {
                        sendData(new int[]{123, 255, 4, 1, 255, 255, 255, 255, 191});
                    }
                }
                sendData(new int[]{126, -1, 4, 1, -1, -1, -1, -1, -17});
            }
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void bledmxturnOn(String str) {
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                sendData(new int[]{126, 255, 4, 3, 255, 255, 255, 255, 239});
            } else {
                if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                    if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                        if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                            if (str.equalsIgnoreCase("LEDDMX-02-")) {
                                sendData(new int[]{123, 255, 4, 1, 255, 255, 255, 255, 191});
                            }
                        }
                        sendData(new int[]{123, 255, 4, 3, 255, 255, 255, 255, 191});
                    }
                    if (str.equalsIgnoreCase("LEDDMX-01-")) {
                        sendData(new int[]{123, 4, 4, 3, 255, 255, 255, 255, 191});
                    } else {
                        sendTuyaCommand("101", "7b040403ffffffffbf");
                    }
                }
                sendData(new int[]{126, 255, 4, 3, 255, 255, 255, 255, 239});
            }
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x0173, code lost:
        sendData(new int[]{123, 255, 4, 4, 255, 255, 255, 255, 191});
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x01aa, code lost:
        if (r23.equalsIgnoreCase("LEDDMX-01-") == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x01ac, code lost:
        sendData(new int[]{123, 4, 4, 4, 255, 255, 255, 255, 191});
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01c4, code lost:
        sendTuyaCommand("101", "7b040404ffffffffbf");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void turnOff(String str, boolean z, boolean z2, boolean z3) {
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                sendData(new int[]{126, 255, 4, 0, 255, 255, 255, 255, 239});
                sendTuyaCommand("101", "7eff0400ffffffffef");
            } else {
                if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                    if (!str.equalsIgnoreCase("LEDCAR-01-")) {
                        if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                            if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                                if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                    sendData(new int[]{125, 1, 1, 0, 255, 255, 255, 255, 223});
                                } else {
                                    if (!str.equalsIgnoreCase(CommonConstant.LEDLIGHT) && !str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                        if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                                            sendData(new int[]{122, 1, 0, 255, 255, 255, 255, 255, 175});
                                        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                                            sendData(new int[]{112, 1, 0, 255, 255, 255, 255, 255, 15});
                                        }
                                    }
                                    sendData(new int[]{126, 255, 4, 0, 255, 255, 255, 255, 239});
                                }
                            }
                            sendData(new int[]{123, 255, 4, 0, 255, 255, 255, 255, 191});
                        }
                        if (str.equalsIgnoreCase("LEDDMX-01-")) {
                            sendData(new int[]{123, 4, 4, 0, 255, 255, 255, 255, 191});
                        } else {
                            sendTuyaCommand("101", "7b040400ffffffffbf");
                        }
                    } else if (!z2 && !z3) {
                        sendData(new int[]{126, -1, 4, 0, -1, -1, -1, -1, -17});
                    } else if (z3) {
                        sendData(new int[]{123, 255, 4, 6, 255, 255, 255, 255, 191});
                    } else if (z2) {
                        sendData(new int[]{123, 255, 4, 0, 255, 255, 255, 255, 191});
                    }
                }
                sendData(new int[]{126, -1, 4, 0, -1, -1, -1, -1, -17});
            }
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void bledmxturnOff(String str) {
        int[] iArr = null;
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                iArr = new int[]{126, 255, 4, 2, 255, 255, 255, 255, 239};
                sendData(iArr);
            } else {
                if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                    if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                        if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                            if (str.equalsIgnoreCase("LEDDMX-02-")) {
                                iArr = new int[]{123, 255, 4, 0, 255, 255, 255, 255, 191};
                                sendData(iArr);
                            }
                        }
                        iArr = new int[]{123, -1, 4, 2, -1, -1, -1, -1, -65};
                        sendData(iArr);
                    }
                    if (str.equalsIgnoreCase("LEDDMX-01-")) {
                        iArr = new int[]{123, 4, 4, 2, -1, -1, -1, -1, -65};
                        sendData(iArr);
                    } else {
                        sendTuyaCommand("101", "7b040402ffffffffbf");
                    }
                }
                iArr = new int[]{126, 255, 4, 2, 255, 255, 255, 255, 239};
                sendData(iArr);
            }
            sendData(iArr);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void sendsetting(int i, int i2, int i3, int i4) {
        try {
            Thread.sleep(50L);
            sendData(new int[]{125, 1, 3, i, i2, i3, i4, 255, 223});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void sendTime(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) {
        String hexString;
        String hexString2;
        String hexString3;
        String hexString4;
        String hexString5;
        String hexString6;
        String hexString7;
        String hexString8;
        String hexString9;
        String hexString10;
        String hexString11;
        String hexString12;
        String hexString13;
        String hexString14;
        try {
            if (!str.contains(CommonConstant.LEDBLE_00) && !str.contains(CommonConstant.LEDBLE_01)) {
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-02-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                            sendData(new int[]{112, 5, i, i2, i3, i4, i5, 255, 15});
                            return;
                        }
                        return;
                    }
                    sendData(new int[]{139, i, i2, i3, i4, i5, i6, i7, 191});
                    return;
                }
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    sendData(new int[]{139, i, i2, i3, i4, i5, i6, i7, 191});
                    return;
                }
                if (Integer.toHexString(i).length() <= 1) {
                    hexString8 = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString8 = Integer.toHexString(i);
                }
                if (Integer.toHexString(i2).length() <= 1) {
                    hexString9 = NetResult.CODE_OK + Integer.toHexString(i2);
                } else {
                    hexString9 = Integer.toHexString(i2);
                }
                if (Integer.toHexString(i3).length() <= 1) {
                    hexString10 = NetResult.CODE_OK + Integer.toHexString(i3);
                } else {
                    hexString10 = Integer.toHexString(i3);
                }
                if (Integer.toHexString(i4).length() <= 1) {
                    hexString11 = NetResult.CODE_OK + Integer.toHexString(i4);
                } else {
                    hexString11 = Integer.toHexString(i4);
                }
                if (Integer.toHexString(i5).length() <= 1) {
                    hexString12 = NetResult.CODE_OK + Integer.toHexString(i5);
                } else {
                    hexString12 = Integer.toHexString(i5);
                }
                if (Integer.toHexString(i6).length() <= 1) {
                    hexString13 = NetResult.CODE_OK + Integer.toHexString(i6);
                } else {
                    hexString13 = Integer.toHexString(i6);
                }
                if (Integer.toHexString(i7).length() <= 1) {
                    hexString14 = NetResult.CODE_OK + Integer.toHexString(i7);
                } else {
                    hexString14 = Integer.toHexString(i7);
                }
                sendTuyaCommand("101", "8b" + hexString8 + hexString9 + hexString10 + hexString11 + hexString12 + hexString13 + hexString14 + "bf");
                return;
            }
            sendData(new int[]{142, i, i2, i3, i4, i5, i6, i7, 239});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            if (Integer.toHexString(i2).length() <= 1) {
                hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
            } else {
                hexString2 = Integer.toHexString(i2);
            }
            if (Integer.toHexString(i3).length() <= 1) {
                hexString3 = NetResult.CODE_OK + Integer.toHexString(i3);
            } else {
                hexString3 = Integer.toHexString(i3);
            }
            if (Integer.toHexString(i4).length() <= 1) {
                hexString4 = NetResult.CODE_OK + Integer.toHexString(i4);
            } else {
                hexString4 = Integer.toHexString(i4);
            }
            if (Integer.toHexString(i5).length() <= 1) {
                hexString5 = NetResult.CODE_OK + Integer.toHexString(i5);
            } else {
                hexString5 = Integer.toHexString(i5);
            }
            if (Integer.toHexString(i6).length() <= 1) {
                hexString6 = NetResult.CODE_OK + Integer.toHexString(i6);
            } else {
                hexString6 = Integer.toHexString(i6);
            }
            if (Integer.toHexString(i7).length() <= 1) {
                hexString7 = NetResult.CODE_OK + Integer.toHexString(i7);
            } else {
                hexString7 = Integer.toHexString(i7);
            }
            sendTuyaCommand("101", "8e" + hexString + hexString2 + hexString3 + hexString4 + hexString5 + hexString6 + hexString7 + "ef");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void endTime(int i, int i2, int i3, int i4, String str) {
        String hexString;
        String hexString2;
        String hexString3;
        String hexString4;
        String hexString5;
        String hexString6;
        String hexString7;
        String hexString8;
        try {
            if (str.contains(CommonConstant.LEDBLE)) {
                sendData(new int[]{126, 255, 17, i, i2, 255, i3, i4, 239});
                if (Integer.toHexString(i).length() <= 1) {
                    hexString5 = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString5 = Integer.toHexString(i);
                }
                if (Integer.toHexString(i2).length() <= 1) {
                    hexString6 = NetResult.CODE_OK + Integer.toHexString(i2);
                } else {
                    hexString6 = Integer.toHexString(i2);
                }
                if (Integer.toHexString(i3).length() <= 1) {
                    hexString7 = NetResult.CODE_OK + Integer.toHexString(i3);
                } else {
                    hexString7 = Integer.toHexString(i3);
                }
                if (Integer.toHexString(i4).length() <= 1) {
                    hexString8 = NetResult.CODE_OK + Integer.toHexString(i4);
                } else {
                    hexString8 = Integer.toHexString(i4);
                }
                sendTuyaCommand("101", "7eff11" + hexString5 + hexString6 + "ff" + hexString7 + hexString8 + "ef");
                return;
            }
            if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-02-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                    if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                        sendData(new int[]{112, 6, i2, i, i3, i4, 255, 255, 15});
                        return;
                    }
                    return;
                }
                sendData(new int[]{123, 255, 16, i, i2, 255, i3, i4, 191});
                return;
            }
            if (str.equalsIgnoreCase("LEDDMX-01-")) {
                sendData(new int[]{123, 4, 16, i, i2, 255, i3, i4, 191});
                return;
            }
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            if (Integer.toHexString(i2).length() <= 1) {
                hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
            } else {
                hexString2 = Integer.toHexString(i2);
            }
            if (Integer.toHexString(i3).length() <= 1) {
                hexString3 = NetResult.CODE_OK + Integer.toHexString(i3);
            } else {
                hexString3 = Integer.toHexString(i3);
            }
            if (Integer.toHexString(i4).length() <= 1) {
                hexString4 = NetResult.CODE_OK + Integer.toHexString(i4);
            } else {
                hexString4 = Integer.toHexString(i4);
            }
            sendTuyaCommand("101", "7b0410" + hexString + hexString2 + "ff" + hexString3 + hexString4 + "bf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setrgbTime(int i, int i2, int i3, int i4, int i5, int i6) {
        try {
            sendData(new int[]{125, 4, i, i2, i3, i4, i5, i6, 223});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void closeTime(String str) {
        try {
            if (str.contains(CommonConstant.LEDBLE)) {
                sendData(new int[]{142, 255, 255, 255, 255, 255, 255, 255, 239});
                sendTuyaCommand("101", "8effffffffffffef");
            } else {
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-02-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                            sendData(new int[]{122, 11, 255, 255, 255, 255, 255, 255, 175});
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                            sendData(new int[]{112, 7, 255, 255, 255, 255, 255, 255, 15});
                        }
                    }
                    sendData(new int[]{139, 255, 255, 255, 255, 255, 255, 255, 191});
                }
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    sendData(new int[]{139, 255, 255, 255, 255, 255, 255, 255, 191});
                } else {
                    sendTuyaCommand("101", "8bffffffffffffbf");
                }
            }
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setRgb(int i, int i2, int i3, String str, boolean z, boolean z2, boolean z3) {
        int[] iArr;
        try {
            int i4 = 0;
            if (!str.contains(CommonConstant.LEDBLE) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase("LEDDMX-02-")) {
                            sendData(new int[]{123, 255, 7, i, i2, i3, 255, 255, 191});
                            return;
                        } else if (str.equalsIgnoreCase("LEDCAR-01-")) {
                            if (z || z2) {
                                iArr = new int[9];
                                iArr[0] = 123;
                                if (!z2) {
                                    if (z3) {
                                    }
                                    iArr[1] = i4;
                                    iArr[2] = 7;
                                    iArr[3] = i;
                                    iArr[4] = i2;
                                    iArr[5] = i3;
                                    iArr[6] = 255;
                                    iArr[7] = 255;
                                    iArr[8] = 191;
                                }
                                i4 = 1;
                                iArr[1] = i4;
                                iArr[2] = 7;
                                iArr[3] = i;
                                iArr[4] = i2;
                                iArr[5] = i3;
                                iArr[6] = 255;
                                iArr[7] = 255;
                                iArr[8] = 191;
                            } else {
                                iArr = new int[]{126, 255, 5, 3, i, i2, i3, 255, 239};
                            }
                            sendData(iArr);
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                            sendData(new int[]{125, 2, 1, 255, i, i2, i3, 255, 223});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                            sendData(new int[]{126, 255, 5, 3, i, i2, i3, 255, 239});
                            return;
                        } else {
                            return;
                        }
                    }
                    sendData(new int[]{123, 255, 7, i, i2, i3, 255, 255, 191});
                    return;
                }
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    sendData(new int[]{123, 4, 7, i, i2, i3, 255, 255, 191});
                    return;
                }
                return;
            }
            if (this.handlerrgb == null) {
                this.handlerrgb = new Handler();
            }
            if (this.aa) {
                sendData(new int[]{126, 255, 5, 3, i, i2, i3, 255, 239});
                Log.i(TAG, "setRgb,   scenebean = " + str);
                this.aa = false;
                this.handlerrgb.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.1
                    @Override // java.lang.Runnable
                    public void run() {
                        NetConnectBle.this.aa = true;
                    }
                }, 50L);
                return;
            }
            Log.i(TAG, "setRgb: AAAAAAAAA");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setRgbMode(int i, String str, boolean z) {
        int[] iArr;
        String hexString;
        int[] iArr2;
        String hexString2;
        try {
            if (!str.contains(CommonConstant.LEDBLE) && !str.equalsIgnoreCase("LEDCAR-00-") && (!str.equalsIgnoreCase("LEDCAR-01-") || z)) {
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase("LEDCAR-01-") && z) {
                            iArr = new int[]{123, 255, 3, i, 255, 255, 255, 255, 191};
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                            iArr = new int[]{125, 2, 5, i, 255, 255, 255, 255, 223};
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                            iArr = new int[]{126, 255, 3, i, 3, 255, 255, 255, 239};
                        } else if (!str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                            if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                                iArr = new int[]{112, 4, i, 255, 255, 255, 255, 255, 15};
                            }
                            iArr2 = null;
                            sendData(iArr2);
                        } else {
                            iArr = new int[]{122, 6, i, 255, 255, 255, 255, 255, 175};
                        }
                        iArr2 = iArr;
                        sendData(iArr2);
                    }
                    iArr = new int[]{123, 255, 3, i, 255, 255, 255, 255, 191};
                    iArr2 = iArr;
                    sendData(iArr2);
                }
                if (!str.equalsIgnoreCase("LEDDMX-01-")) {
                    if (Integer.toHexString(i).length() <= 1) {
                        hexString2 = NetResult.CODE_OK + Integer.toHexString(i);
                    } else {
                        hexString2 = Integer.toHexString(i);
                    }
                    sendTuyaCommand("101", "7b0403" + hexString2 + "03ffffffbf");
                    iArr2 = null;
                    sendData(iArr2);
                }
                iArr = new int[]{123, 4, 3, i, 255, 255, 255, 255, 191};
                iArr2 = iArr;
                sendData(iArr2);
            }
            iArr = new int[]{126, 255, 3, i, 3, 255, 255, 255, 239};
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7eff03" + hexString + "03ffffffef");
            iArr2 = iArr;
            sendData(iArr2);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setLikeMode(int i, boolean z) {
        try {
            sendData(z ? new int[]{112, 4, i, 1, 255, 255, 255, 255, 15} : new int[]{112, 4, i, 0, 255, 255, 255, 255, 15});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDirection(int i, String str, boolean z) {
        String hexString;
        try {
            if ((!str.equalsIgnoreCase("LEDCAR-01-") || !z) && !str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                sendData(new int[]{123, 4, 13, i, 255, 255, 255, 255, 191});
                if (Integer.toHexString(i).length() <= 1) {
                    hexString = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString = Integer.toHexString(i);
                }
                sendTuyaCommand("101", "7b040d" + hexString + "ffffffffbf");
                return;
            }
            sendData(new int[]{123, 255, 13, i, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setMusicSpeed(int i) {
        String hexString;
        try {
            sendData(new int[]{123, 4, 12, i, 255, 255, 255, 255, 191});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7b040c" + hexString + "ffffffffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setMusicModel(int i, String str) {
        String hexString;
        try {
            if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                    if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                        sendData(new int[]{112, 4, i, 255, 255, 255, 255, 255, 15});
                        return;
                    }
                    return;
                }
                sendData(new int[]{123, 255, 11, i, 255, 255, 255, 255, 191});
                return;
            }
            if (str.equalsIgnoreCase("LEDDMX-01-")) {
                sendData(new int[]{123, 4, 11, i, 255, 255, 255, 255, 191});
                return;
            }
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7b040b" + hexString + "ffffffffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setCirculation() {
        try {
            sendData(new int[]{123, 4, 15, 1, 255, 255, 255, 255, 191});
            sendTuyaCommand("101", "7b040f01ffffffffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSpeed(int i, String str, boolean z, boolean z2) {
        String hexString;
        String hexString2;
        try {
            if (!str.contains(CommonConstant.LEDBLE) && !str.equalsIgnoreCase("LEDCAR-00-") && (!str.equalsIgnoreCase("LEDCAR-01-") || z2)) {
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDCAR-01-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                            sendData(new int[]{125, 2, 4, i, 255, 255, 255, 255, 223});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                            sendData(new int[]{126, 255, 2, i, 255, 255, 255, 255, 239});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                            sendData(new int[]{122, 3, i, 255, 255, 255, 255, 255, 175});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                            sendData(new int[]{112, 3, i, 255, 255, 255, 255, 255, 15});
                            return;
                        } else {
                            return;
                        }
                    }
                    int[] iArr = new int[9];
                    iArr[0] = 123;
                    iArr[1] = 255;
                    iArr[2] = 2;
                    iArr[3] = i;
                    iArr[4] = z ? 1 : 0;
                    iArr[5] = 255;
                    iArr[6] = 255;
                    iArr[7] = 255;
                    iArr[8] = 191;
                    sendData(iArr);
                    return;
                }
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    int[] iArr2 = new int[9];
                    iArr2[0] = 123;
                    iArr2[1] = 4;
                    iArr2[2] = 2;
                    iArr2[3] = i;
                    iArr2[4] = 255;
                    iArr2[5] = z ? 1 : 0;
                    iArr2[6] = 255;
                    iArr2[7] = 255;
                    iArr2[8] = 191;
                    sendData(iArr2);
                    return;
                }
                if (Integer.toHexString(i).length() <= 1) {
                    hexString2 = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString2 = Integer.toHexString(i);
                }
                sendTuyaCommand("101", "7b0402" + hexString2 + "ffffffffbf");
                return;
            }
            Log.e(TAG, "setSpeed: " + i);
            int[] iArr3 = new int[9];
            iArr3[0] = 126;
            iArr3[1] = 255;
            iArr3[2] = 2;
            iArr3[3] = i;
            iArr3[4] = z ? 1 : 0;
            iArr3[5] = 255;
            iArr3[6] = 255;
            iArr3[7] = 255;
            iArr3[8] = 239;
            sendData(iArr3);
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7eff02" + hexString + "ffffffffef");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDiy(final ArrayList<MyColor> arrayList, final int i, final String str) {
        String hexString;
        if (str.contains(CommonConstant.LEDBLE) || str.equalsIgnoreCase("LEDCAR-00-")) {
            try {
                sendData(new int[]{126, 255, 14, i, 3, 255, 255, 255, 239});
                if (Integer.toHexString(i).length() <= 1) {
                    hexString = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString = Integer.toHexString(i);
                }
                final String str2 = hexString;
                sendTuyaCommand("101", "7eff0e" + str2 + "03ffffffef");
                this.k = 0;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.2
                    @Override // java.lang.Runnable
                    public void run() {
                        String hexString2;
                        String hexString3;
                        String hexString4;
                        String hexString5;
                        if (NetConnectBle.this.k != arrayList.size()) {
                            int i2 = ((MyColor) arrayList.get(NetConnectBle.this.k)).r;
                            int i3 = ((MyColor) arrayList.get(NetConnectBle.this.k)).g;
                            int i4 = ((MyColor) arrayList.get(NetConnectBle.this.k)).b;
                            int[] iArr = {126, 255, 16, i, i2, i3, i4, arrayList.size(), 239};
                            NetConnectBle.access$008(NetConnectBle.this);
                            try {
                                NetConnectBle.this.sendData(iArr);
                                if (Integer.toHexString(i2).length() <= 1) {
                                    hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
                                } else {
                                    hexString2 = Integer.toHexString(i2);
                                }
                                if (Integer.toHexString(i3).length() <= 1) {
                                    hexString3 = NetResult.CODE_OK + Integer.toHexString(i3);
                                } else {
                                    hexString3 = Integer.toHexString(i3);
                                }
                                if (Integer.toHexString(i4).length() <= 1) {
                                    hexString4 = NetResult.CODE_OK + Integer.toHexString(i4);
                                } else {
                                    hexString4 = Integer.toHexString(i4);
                                }
                                if (Integer.toHexString(arrayList.size()).length() <= 1) {
                                    hexString5 = NetResult.CODE_OK + Integer.toHexString(arrayList.size());
                                } else {
                                    hexString5 = Integer.toHexString(arrayList.size());
                                }
                                NetConnectBle.this.sendTuyaCommand("101", "7eff10" + str2 + hexString2 + hexString3 + hexString4 + hexString5 + "ffef");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            handler.postDelayed(this, 200L);
                            return;
                        }
                        handler.removeCallbacks(this);
                        try {
                            NetConnectBle.this.sendData(new int[]{126, 255, 15, i, 3, 255, 255, 255, 239});
                            NetConnectBle.this.sendTuyaCommand("101", "7eff0f" + str2 + "03ffffffef");
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        NetConnectBle.this.k = 0;
                    }
                }, 150L);
                return;
            } catch (Exception e) {
                NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
                if (netExceptionInterface != null) {
                    netExceptionInterface.onException(e);
                    return;
                }
                return;
            }
        }
        try {
            this.k = 0;
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.3
                @Override // java.lang.Runnable
                public void run() {
                    String hexString2;
                    String hexString3;
                    String hexString4;
                    String hexString5;
                    String hexString6;
                    if (NetConnectBle.this.k != arrayList.size()) {
                        int i2 = ((MyColor) arrayList.get(NetConnectBle.this.k)).r;
                        int i3 = ((MyColor) arrayList.get(NetConnectBle.this.k)).g;
                        int i4 = ((MyColor) arrayList.get(NetConnectBle.this.k)).b;
                        if (str.equalsIgnoreCase(CommonConstant.LEDBLE)) {
                            try {
                                NetConnectBle.this.sendData(new int[]{126, 255, 16, i, i2, i3, i4, arrayList.size(), 239});
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        } else if (str.equalsIgnoreCase("LEDDMX-01-") || str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                            if (str.equalsIgnoreCase("LEDDMX-01-")) {
                                try {
                                    NetConnectBle.this.sendData(new int[]{123, 4, 14, i, i2, i3, i4, arrayList.size(), 191});
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            } else {
                                if (Integer.toHexString(i).length() <= 1) {
                                    hexString2 = NetResult.CODE_OK + Integer.toHexString(i);
                                } else {
                                    hexString2 = Integer.toHexString(i);
                                }
                                if (Integer.toHexString(i2).length() <= 1) {
                                    hexString3 = NetResult.CODE_OK + Integer.toHexString(i2);
                                } else {
                                    hexString3 = Integer.toHexString(i2);
                                }
                                if (Integer.toHexString(i3).length() <= 1) {
                                    hexString4 = NetResult.CODE_OK + Integer.toHexString(i3);
                                } else {
                                    hexString4 = Integer.toHexString(i3);
                                }
                                if (Integer.toHexString(i4).length() <= 1) {
                                    hexString5 = NetResult.CODE_OK + Integer.toHexString(i4);
                                } else {
                                    hexString5 = Integer.toHexString(i4);
                                }
                                if (Integer.toHexString(arrayList.size()).length() <= 1) {
                                    hexString6 = NetResult.CODE_OK + Integer.toHexString(arrayList.size());
                                } else {
                                    hexString6 = Integer.toHexString(arrayList.size());
                                }
                                NetConnectBle.this.sendTuyaCommand("101", "7b040e" + hexString2 + hexString3 + hexString4 + hexString5 + hexString6 + "bf");
                            }
                        } else if (str.equalsIgnoreCase("LEDDMX-00-") || str.equalsIgnoreCase("LEDDMX-02-") || str.equalsIgnoreCase("LEDDMX-03-")) {
                            try {
                                NetConnectBle.this.sendData(new int[]{123, 255, 14, i, i2, i3, i4, arrayList.size(), 191});
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                            try {
                                NetConnectBle.this.sendData(new int[]{125, 2, 3, i, i2, i3, i4, arrayList.size(), 223});
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                            try {
                                NetConnectBle.this.sendData(new int[]{126, 255, 6, i, i2, i3, i4, arrayList.size(), 239});
                            } catch (IOException e6) {
                                e6.printStackTrace();
                            }
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                            try {
                                NetConnectBle.this.sendData(new int[]{126, 255, 6, i, i2, i3, i4, arrayList.size(), 239});
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                        }
                        NetConnectBle.access$008(NetConnectBle.this);
                        handler2.postDelayed(this, 200L);
                        return;
                    }
                    handler2.removeCallbacks(this);
                    NetConnectBle.this.k = 0;
                }
            }, 150L);
        } catch (Exception e2) {
            NetExceptionInterface netExceptionInterface2 = this.exceptionCallBack;
            if (netExceptionInterface2 != null) {
                netExceptionInterface2.onException(e2);
            }
        }
    }

    public void setMusicBrightness(int i, String str) {
        String hexString;
        String hexString2;
        String hexString3;
        int i2 = i;
        if (i2 > 100) {
            i2 = 100;
        } else if (i2 <= 0) {
            i2 = 0;
        }
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                sendData(new int[]{126, 255, 1, i2, 1, 255, 255, 255, 239});
                if (Integer.toHexString(i2).length() <= 1) {
                    hexString3 = NetResult.CODE_OK + Integer.toHexString(i2);
                } else {
                    hexString3 = Integer.toHexString(i2);
                }
                sendTuyaCommand("101", "7eff01" + hexString3 + "ffffffffef");
                return;
            }
            if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                if (str.equalsIgnoreCase("LEDCAR-01-")) {
                    sendData(new int[]{123, 255, 1, (i2 * 32) / 100, i2, 1, 255, 255, 191});
                    return;
                }
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-02-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                            sendData(new int[]{122, 9, i2, 255, 255, 255, 255, 0, 175});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                            sendData(new int[]{112, 2, i2, 255, 255, 255, 255, 255, 15});
                            return;
                        } else {
                            return;
                        }
                    }
                    sendData(new int[]{123, 255, 1, (i2 * 32) / 100, i2, 1, 255, 255, 191});
                    return;
                }
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    sendData(new int[]{123, 4, 1, (i2 * 32) / 100, i2, 1, 255, 255, 191});
                    return;
                }
                int i3 = i2 * 32;
                if (Integer.toHexString(i3 / 100).length() <= 1) {
                    hexString = NetResult.CODE_OK + Integer.toHexString(i3 / 100);
                } else {
                    hexString = Integer.toHexString(i3 / 100);
                }
                if (Integer.toHexString(i2).length() <= 1) {
                    hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
                } else {
                    hexString2 = Integer.toHexString(i2);
                }
                sendTuyaCommand("101", "7b0401" + hexString + hexString2 + "01ffffbf");
                return;
            }
            sendData(new int[]{126, 255, 1, i2, 1, 255, 255, 255, 239});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setBrightness(int i, String str, boolean z, boolean z2, boolean z3) {
        String hexString;
        String hexString2;
        String hexString3;
        int[] iArr;
        int i2 = i;
        int i3 = 0;
        if (i2 > 100) {
            i2 = 100;
        } else if (i2 <= 0) {
            i2 = 0;
        }
        try {
            int i4 = 1;
            if (!str.contains(CommonConstant.LEDBLE) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-02-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase("LEDCAR-01-")) {
                            if (z2 || z3) {
                                iArr = new int[9];
                                iArr[0] = 123;
                                iArr[1] = 255;
                                iArr[2] = 1;
                                iArr[3] = (i2 * 32) / 100;
                                iArr[4] = i2;
                                if (z) {
                                    i3 = 1;
                                } else if (z2) {
                                    i3 = 2;
                                }
                                iArr[5] = i3;
                                iArr[6] = 255;
                                iArr[7] = 255;
                                iArr[8] = 191;
                            } else {
                                iArr = new int[]{126, 255, 1, i2, 0, 255, 255, 255, 239};
                            }
                            sendData(iArr);
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                            sendData(new int[]{125, 2, 2, i2, 255, 255, 255, 255, 223});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                            sendData(new int[]{126, 255, 1, i2, 255, 255, 255, 255, 239});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                            sendData(new int[]{122, 2, i2, 255, 255, 255, 255, 255, 175});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
                            sendData(new int[]{112, 2, i2, 255, 255, 255, 255, 255, 15});
                            return;
                        } else {
                            return;
                        }
                    }
                    if (this.handler == null) {
                        this.handler = new Handler();
                    }
                    if (this.aa) {
                        int[] iArr2 = new int[9];
                        iArr2[0] = 123;
                        iArr2[1] = 255;
                        iArr2[2] = 1;
                        iArr2[3] = (i2 * 32) / 100;
                        iArr2[4] = i2;
                        if (!z) {
                            i4 = 0;
                        }
                        iArr2[5] = i4;
                        iArr2[6] = 255;
                        iArr2[7] = 255;
                        iArr2[8] = 191;
                        sendData(iArr2);
                        this.aa = false;
                        this.handler.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.6
                            @Override // java.lang.Runnable
                            public void run() {
                                NetConnectBle.this.aa = true;
                            }
                        }, 30L);
                        return;
                    }
                    Log.i(TAG, "setRgb: AAAAAAAAA");
                    return;
                }
                if (this.handler == null) {
                    this.handler = new Handler();
                }
                if (this.aa) {
                    if (str.equalsIgnoreCase("LEDDMX-01-")) {
                        int[] iArr3 = new int[9];
                        iArr3[0] = 123;
                        iArr3[1] = 4;
                        iArr3[2] = 1;
                        iArr3[3] = (i2 * 32) / 100;
                        iArr3[4] = i2;
                        if (!z) {
                            i4 = 0;
                        }
                        iArr3[5] = i4;
                        iArr3[6] = 255;
                        iArr3[7] = 255;
                        iArr3[8] = 191;
                        sendData(iArr3);
                    } else {
                        int i5 = i2 * 32;
                        if (Integer.toHexString(i5 / 100).length() <= 1) {
                            hexString2 = NetResult.CODE_OK + Integer.toHexString(i5 / 100);
                        } else {
                            hexString2 = Integer.toHexString(i5 / 100);
                        }
                        if (Integer.toHexString(i2).length() <= 1) {
                            hexString3 = NetResult.CODE_OK + Integer.toHexString(i2);
                        } else {
                            hexString3 = Integer.toHexString(i2);
                        }
                        sendTuyaCommand("101", "7b0401" + hexString2 + hexString3 + (z ? "01" : "00") + "ffffbf");
                    }
                    Log.e(TAG, "setBrightness: " + i2);
                    this.aa = false;
                    this.handler.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.5
                        @Override // java.lang.Runnable
                        public void run() {
                            NetConnectBle.this.aa = true;
                        }
                    }, 30L);
                    return;
                }
                Log.i(TAG, "setRgb: AAAAAAAAA");
                return;
            }
            if (this.handler == null) {
                this.handler = new Handler();
            }
            if (this.aa) {
                Log.e(TAG, "setBrightness: " + i2);
                int[] iArr4 = new int[9];
                iArr4[0] = 126;
                iArr4[1] = 255;
                iArr4[2] = 1;
                iArr4[3] = i2;
                iArr4[4] = z ? 1 : 0;
                iArr4[5] = 255;
                iArr4[6] = 255;
                iArr4[7] = 255;
                iArr4[8] = 239;
                sendData(iArr4);
                if (Integer.toHexString(i2).length() <= 1) {
                    hexString = NetResult.CODE_OK + Integer.toHexString(i2);
                } else {
                    hexString = Integer.toHexString(i2);
                }
                sendTuyaCommand("101", "7eff01" + hexString + "ffffffffef");
                this.aa = false;
                this.handler.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.4
                    @Override // java.lang.Runnable
                    public void run() {
                        NetConnectBle.this.aa = true;
                    }
                }, 30L);
                return;
            }
            Log.i(TAG, "setRgb: AAAAAAAAA");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setLikeBrightness(int i, String str, boolean z, boolean z2) {
        int i2 = i;
        if (i2 > 100) {
            i2 = 100;
        } else if (i2 <= 0) {
            i2 = 0;
        }
        try {
            sendData(z ? new int[]{112, 2, i2, 1, 255, 255, 255, 255, 15} : z2 ? new int[]{112, 2, i2, 2, 255, 255, 255, 255, 15} : new int[]{112, 2, i2, 0, 255, 255, 255, 255, 15});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setColorWarmModel(int i) {
        try {
            sendData(new int[]{126, 255, 3, i, 2, 255, 255, 255, 239});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDimModel(int i) {
        try {
            sendData(new int[]{126, 255, 3, i, 1, 255, 255, 255, 239});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDim(int i, String str) {
        String hexString;
        String hexString2;
        String hexString3;
        try {
            if (!str.contains(CommonConstant.LEDBLE) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                        if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                            sendData(new int[]{125, 2, 7, i, 255, 255, 255, 255, 223});
                            return;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                            sendData(new int[]{126, 255, 5, 1, i, 255, 255, 255, 239});
                            return;
                        } else {
                            return;
                        }
                    }
                    sendData(new int[]{123, 255, 9, (i * 32) / 100, i, 255, 255, 255, 191});
                    return;
                }
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    sendData(new int[]{123, 4, 9, (i * 32) / 100, i, 255, 255, 255, 191});
                    return;
                }
                int i2 = i * 32;
                if (i2 / 100 < 10) {
                    hexString2 = NetResult.CODE_OK + (i2 / 100);
                } else {
                    hexString2 = Integer.toHexString(i2 / 100);
                }
                if (i < 10) {
                    hexString3 = NetResult.CODE_OK + i;
                } else {
                    hexString3 = Integer.toHexString(i);
                }
                sendTuyaCommand("101", "7b0409" + hexString2 + hexString3 + "ffffffbf");
                return;
            }
            sendData(new int[]{126, 255, 5, 1, i, 255, 255, 255, 239});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7eff0501" + hexString + "ffffffef");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setColorWarm(int i, int i2, String str) {
        String hexString;
        String hexString2;
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDBLE)) {
                sendData(new int[]{126, 255, 5, 2, i, i2, 255, 255, 239});
                if (Integer.toHexString(i).length() <= 1) {
                    hexString = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString = Integer.toHexString(i);
                }
                if (Integer.toHexString(i2).length() <= 1) {
                    hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
                } else {
                    hexString2 = Integer.toHexString(i2);
                }
                sendTuyaCommand("101", "7eff0502" + hexString + hexString2 + "ffffef");
            } else if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                sendData(new int[]{126, 255, 5, 2, i, i2, 255, 255, 239});
            } else if (str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
                sendData(new int[]{122, 5, i, 255, 255, 255, 255, 255, 175});
            }
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDynamicModel(int i) {
        String hexString;
        try {
            sendData(new int[]{126, 255, 3, i, 4, 255, 255, 255, 239});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7eff03" + hexString + "04ffffffef");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSensitivity(int i, boolean z, boolean z2, String str) {
        String hexString;
        int[] iArr = null;
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                iArr = new int[]{126, 255, 7, i, 255, 255, 255, 255, 239};
                if (Integer.toHexString(i).length() <= 1) {
                    hexString = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString = Integer.toHexString(i);
                }
                sendTuyaCommand("101", "7eff07" + hexString + "ffffffffef");
            } else if (str.equalsIgnoreCase("LEDDMX-01-")) {
                iArr = new int[9];
                iArr[0] = 123;
                iArr[1] = 4;
                iArr[2] = 12;
                iArr[3] = i;
                iArr[4] = z ? 1 : 0;
                iArr[5] = 255;
                iArr[6] = 255;
                iArr[7] = 255;
                iArr[8] = 191;
            } else {
                if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-02-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                    if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                        if (str.equalsIgnoreCase("LEDCAR-01-")) {
                            if (z2) {
                                iArr = new int[9];
                                iArr[0] = 123;
                                iArr[1] = 255;
                                iArr[2] = 12;
                                iArr[3] = i;
                                iArr[4] = z ? 1 : 0;
                                iArr[5] = 255;
                                iArr[6] = 255;
                                iArr[7] = 255;
                                iArr[8] = 191;
                            } else {
                                iArr = new int[]{126, 255, 7, i, 255, 255, 255, 255, 239};
                            }
                        }
                    }
                    iArr = new int[]{126, 255, 7, i, 255, 255, 255, 255, 239};
                }
                iArr = new int[9];
                iArr[0] = 123;
                iArr[1] = 255;
                iArr[2] = 12;
                iArr[3] = i;
                iArr[4] = z ? 1 : 0;
                iArr[5] = 255;
                iArr[6] = 255;
                iArr[7] = 255;
                iArr[8] = 191;
            }
            sendData(iArr);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setLikeSensitivity(int i) {
        try {
            sendData(new int[]{112, 8, i, 255, 255, 255, 255, 255, 15});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDynamicDiy(final ArrayList<MyColor> arrayList, final int i) {
        String hexString;
        try {
            sendData(new int[]{126, 255, 10, i, 3, 255, 255, 255, 239});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + i;
            } else {
                hexString = Integer.toHexString(i);
            }
            final String str = hexString;
            sendTuyaCommand("101", "7eff0a" + str + "03ffffffef");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.7
                @Override // java.lang.Runnable
                public void run() {
                    String hexString2;
                    String hexString3;
                    String hexString4;
                    String hexString5;
                    if (NetConnectBle.this.idx >= arrayList.size()) {
                        NetConnectBle.this.idx = 0;
                        try {
                            NetConnectBle.this.sendData(new int[]{126, 255, 12, i, 3, 255, 255, 255, 239});
                            NetConnectBle.this.sendTuyaCommand("101", "7eff0c" + str + "03ffffffef");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler.removeCallbacks(this);
                        return;
                    }
                    int i2 = ((MyColor) arrayList.get(NetConnectBle.this.idx)).r;
                    int i3 = ((MyColor) arrayList.get(NetConnectBle.this.idx)).g;
                    int i4 = ((MyColor) arrayList.get(NetConnectBle.this.idx)).b;
                    LogUtil.i(LedBleApplication.tag, "r:" + i2 + " g:" + i3 + " b:" + i4);
                    try {
                        NetConnectBle.this.sendData(new int[]{126, 255, 11, i, i2, i3, i4, arrayList.size(), 239});
                        if (Integer.toHexString(i2).length() <= 1) {
                            hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
                        } else {
                            hexString2 = Integer.toHexString(i2);
                        }
                        if (Integer.toHexString(i3).length() <= 1) {
                            hexString3 = NetResult.CODE_OK + Integer.toHexString(i3);
                        } else {
                            hexString3 = Integer.toHexString(i3);
                        }
                        if (Integer.toHexString(i4).length() <= 1) {
                            hexString4 = NetResult.CODE_OK + Integer.toHexString(i4);
                        } else {
                            hexString4 = Integer.toHexString(i4);
                        }
                        if (Integer.toHexString(arrayList.size()).length() <= 1) {
                            hexString5 = NetResult.CODE_OK + Integer.toHexString(arrayList.size());
                        } else {
                            hexString5 = Integer.toHexString(arrayList.size());
                        }
                        NetConnectBle.this.sendTuyaCommand("101", "7eff0b" + str + hexString2 + hexString3 + hexString4 + hexString5 + "ef");
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    NetConnectBle.this.idx++;
                    handler.postDelayed(this, 300L);
                }
            }, 300L);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void sendtimestage(int i, int i2, int i3, int i4, int i5, int i6) {
        try {
            Thread.sleep(200L);
            sendData(new int[]{126, i, 8, i2, i3, i4, i5, i6, 239});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void sendSun(int i, int i2, int i3, int i4, int i5) {
        try {
            sendData(new int[]{122, 10, i, i2, i3, i4, i5, 255, 175});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void timeSun(int i, int i2, int i3, int i4) {
        try {
            sendData(new int[]{122, 12, i, i2, i3, i4, 255, 255, 175});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0083 A[Catch: Exception -> 0x00b9, TryCatch #0 {Exception -> 0x00b9, blocks: (B:3:0x0005, B:5:0x001f, B:8:0x0028, B:10:0x0030, B:15:0x0076, B:17:0x0083, B:19:0x009d, B:18:0x0099, B:11:0x0045, B:13:0x004d, B:14:0x0062), top: B:26:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0099 A[Catch: Exception -> 0x00b9, TryCatch #0 {Exception -> 0x00b9, blocks: (B:3:0x0005, B:5:0x001f, B:8:0x0028, B:10:0x0030, B:15:0x0076, B:17:0x0083, B:19:0x009d, B:18:0x0099, B:11:0x0045, B:13:0x004d, B:14:0x0062), top: B:26:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSPIModel(int i, String str) {
        String hexString;
        int[] iArr = null;
        try {
            if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    iArr = new int[]{123, 4, 3, i, 255, 255, 255, 255, 191};
                } else if (str.equalsIgnoreCase("LEDCAR-01-")) {
                    iArr = new int[]{123, 255, 3, i, 255, 255, 255, 255, 191};
                }
                sendData(iArr);
                if (Integer.toHexString(i).length() > 1) {
                    hexString = NetResult.CODE_OK + Integer.toHexString(i);
                } else {
                    hexString = Integer.toHexString(i);
                }
                sendTuyaCommand("101", "7b0403" + hexString + "ffffffffbf");
            }
            iArr = new int[]{123, 255, 3, i, 255, 255, 255, 255, 191};
            sendData(iArr);
            if (Integer.toHexString(i).length() > 1) {
            }
            sendTuyaCommand("101", "7b0403" + hexString + "ffffffffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setConfigSPI(int i, byte b, byte b2, int i2, String str) {
        int[] iArr;
        String hexString;
        String hexString2;
        String hexString3;
        String hexString4;
        try {
            if (str.equalsIgnoreCase("LEDCAR-01-")) {
                iArr = new int[]{123, 255, 5, 4, b, b2, i2, 255, 191};
            } else {
                if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                    iArr = new int[]{123, 4, 5, i, b, b2, i2, 255, 191};
                }
                iArr = new int[]{123, 255, 5, i, b, b2, i2, 255, 191};
            }
            sendData(iArr);
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            if (Integer.toHexString(b).length() <= 1) {
                hexString2 = NetResult.CODE_OK + Integer.toHexString(b);
            } else {
                hexString2 = Integer.toHexString(b);
            }
            if (Integer.toHexString(b2).length() <= 1) {
                hexString3 = NetResult.CODE_OK + Integer.toHexString(b2);
            } else {
                hexString3 = Integer.toHexString(b2);
            }
            if (Integer.toHexString(i2).length() <= 1) {
                hexString4 = NetResult.CODE_OK + Integer.toHexString(i2);
            } else {
                hexString4 = Integer.toHexString(i2);
            }
            sendTuyaCommand("101", "7b0405" + hexString + hexString2 + hexString3 + hexString4 + "ffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setConfigCAR01(int i, int i2, int i3, int i4, String str) {
        try {
            sendData(str.equalsIgnoreCase("LEDCAR-01-") ? new int[]{123, i, 5, 5, i2, i3, i4, 255, 191} : null);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void pauseSPI(int i, String str) {
        String hexString;
        try {
            sendData(str.equalsIgnoreCase("LEDDMX-01-") ? new int[]{123, 4, 6, i, 255, 255, 255, 255, 191} : new int[]{123, 255, 6, i, 255, 255, 255, 255, 191});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7b0406" + hexString + "ffffffffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void SetRgbSort(int i) {
        String hexString;
        try {
            sendData(new int[]{126, 255, 8, i, 255, 255, 255, 255, 239});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7eff08" + hexString + "ffffffffef");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void SetPairCode(int i) {
        String hexString;
        try {
            sendData(new int[]{126, 255, 9, i, 255, 255, 255, 255, 239});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7eff09" + hexString + "ffffffffef");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void SetCHN(int i, int i2, int i3, int i4, int i5, int i6) {
        try {
            sendData(new int[]{126, i, 11, i2, i3, i4, i5, i6, 239});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setTimerFirData(int i) {
        try {
            Calendar calendar = Calendar.getInstance();
            sendData(new int[]{125, 3, 1, i, calendar.get(11), calendar.get(12), calendar.get(13), 255, 223});
        } catch (IOException e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setTime(int i, int i2, int i3, int i4, int i5, int i6) {
        try {
            sendData(new int[]{125, 3, i, i2, i3, i4, i5, i6, 223});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTimerSecData(int[] iArr) {
        try {
            Thread.sleep(200L);
            sendData(new int[]{124, iArr[0], iArr[1], iArr[2], iArr[3], iArr[4], iArr[5], iArr[6], iArr[7]});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void configCode(int i, int i2, int i3) {
        String hexString;
        String hexString2;
        String hexString3;
        try {
            sendData(new int[]{123, 4, 10, i, i2, i3, 255, 255, 191});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            if (Integer.toHexString(i2).length() <= 1) {
                hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
            } else {
                hexString2 = Integer.toHexString(i2);
            }
            if (Integer.toHexString(i3).length() <= 1) {
                hexString3 = NetResult.CODE_OK + Integer.toHexString(i3);
            } else {
                hexString3 = Integer.toHexString(i3);
            }
            sendTuyaCommand("101", "7b040a" + hexString + hexString2 + hexString3 + "ffffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setAuxiliary(int i, String str) {
        String hexString;
        try {
            if (!str.contains(CommonConstant.LEDBLE) && !str.equalsIgnoreCase("LEDCAR-00-") && !str.equalsIgnoreCase("LEDCAR-01-")) {
                if (str.contains(CommonConstant.LEDDMX)) {
                    sendData(new int[]{123, 255, 17, i, 255, 255, 255, 255, 191});
                    return;
                }
                return;
            }
            sendData(new int[]{126, 255, 18, i, 255, 255, 255, 255, 239});
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            sendTuyaCommand("101", "7eff12" + hexString + "ffffffffef");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setModeCycle(int i, int i2, int i3, int i4, int i5, int i6, String str, boolean z) {
        try {
            if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase(CommonConstant.LEDSTAGE) && !str.equalsIgnoreCase(CommonConstant.LEDLIGHT) && !str.equalsIgnoreCase("LEDCAR-00-") && (!str.equalsIgnoreCase("LEDCAR-01-") || z)) {
                    if (str.equalsIgnoreCase("LEDCAR-01-") && z) {
                        sendData(new int[]{123, i, 18, i2, i3, i4, i5, i6, 191});
                    }
                }
                sendData(new int[]{126, i, 15, 1, i2, i3, i4, i5, 239});
            }
            sendData(new int[]{123, i, 18, i2, i3, i4, i5, i6, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setCustomCycle(String str, boolean z) {
        try {
            if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase(CommonConstant.LEDBLE_01) && !str.equalsIgnoreCase(CommonConstant.LEDSTAGE) && !str.equalsIgnoreCase(CommonConstant.LEDLIGHT) && !str.equalsIgnoreCase("LEDCAR-00-") && (!str.equalsIgnoreCase("LEDCAR-01-") || z)) {
                    if (str.equalsIgnoreCase("LEDCAR-01-") && z) {
                        sendData(new int[]{123, 255, 15, 1, 255, 255, 255, 255, 191});
                    }
                }
                sendData(new int[]{126, 255, 15, 0, 255, 255, 255, 255, 239});
            }
            sendData(new int[]{123, 255, 15, 1, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setChangeColor(final boolean z, final boolean z2, final boolean z3, final ArrayList<MyColor> arrayList, final String str) {
        try {
            this.k = 0;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.8
                @Override // java.lang.Runnable
                public void run() {
                    int[] iArr;
                    if (NetConnectBle.this.k != arrayList.size()) {
                        int i = ((MyColor) arrayList.get(NetConnectBle.this.k)).r;
                        int i2 = ((MyColor) arrayList.get(NetConnectBle.this.k)).g;
                        int i3 = ((MyColor) arrayList.get(NetConnectBle.this.k)).b;
                        if (str.equalsIgnoreCase("LEDDMX-00-") || str.equalsIgnoreCase("LEDDMX-02-") || str.equalsIgnoreCase("LEDDMX-03-")) {
                            iArr = new int[9];
                            iArr[0] = 123;
                            iArr[1] = NetConnectBle.this.k + 1;
                            iArr[2] = 14;
                            iArr[3] = z ? 254 : 253;
                            iArr[4] = i;
                            iArr[5] = i2;
                            iArr[6] = i3;
                            iArr[7] = arrayList.size();
                            iArr[8] = 191;
                        } else if (str.equalsIgnoreCase(CommonConstant.LEDBLE_00) || str.equalsIgnoreCase(CommonConstant.LEDBLE_01) || str.equalsIgnoreCase(CommonConstant.LEDSTAGE) || str.equalsIgnoreCase(CommonConstant.LEDLIGHT) || str.equalsIgnoreCase("LEDCAR-00-") || (str.equalsIgnoreCase("LEDCAR-01-") && !z3)) {
                            iArr = new int[9];
                            iArr[0] = 126;
                            iArr[1] = NetConnectBle.this.k + 1;
                            iArr[2] = 13;
                            iArr[3] = z ? 254 : z2 ? 252 : 253;
                            iArr[4] = i;
                            iArr[5] = i2;
                            iArr[6] = i3;
                            iArr[7] = arrayList.size();
                            iArr[8] = 239;
                        } else if (str.equalsIgnoreCase("LEDCAR-01-") && z3) {
                            iArr = new int[9];
                            iArr[0] = 123;
                            iArr[1] = NetConnectBle.this.k + 1;
                            iArr[2] = 14;
                            iArr[3] = z ? 254 : z2 ? 252 : 253;
                            iArr[4] = i;
                            iArr[5] = i2;
                            iArr[6] = i3;
                            iArr[7] = arrayList.size();
                            iArr[8] = 191;
                        } else {
                            iArr = null;
                        }
                        try {
                            NetConnectBle.this.sendData(iArr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        NetConnectBle.access$008(NetConnectBle.this);
                        handler.postDelayed(this, 100L);
                        return;
                    }
                    handler.removeCallbacks(this);
                    NetConnectBle.this.k = 0;
                }
            }, 100L);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setMode(boolean z, boolean z2, boolean z3, int i, String str) {
        int[] iArr = null;
        try {
            int i2 = 1;
            int i3 = 0;
            if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase(CommonConstant.LEDBLE_01) && !str.equalsIgnoreCase(CommonConstant.LEDSTAGE) && !str.equalsIgnoreCase(CommonConstant.LEDLIGHT) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                if (str.equalsIgnoreCase("LEDCAR-01-")) {
                    if (z3) {
                        iArr = new int[9];
                        iArr[0] = 123;
                        iArr[1] = 255;
                        iArr[2] = z2 ? 11 : 19;
                        iArr[3] = i;
                        if (z) {
                            i2 = 0;
                        } else if (!z2) {
                            i2 = 255;
                        }
                        iArr[4] = i2;
                        iArr[5] = 255;
                        iArr[6] = 255;
                        iArr[7] = 255;
                        iArr[8] = 191;
                    } else {
                        iArr = new int[9];
                        iArr[0] = 126;
                        if (!z) {
                            i3 = 1;
                        }
                        iArr[1] = i3;
                        iArr[2] = 14;
                        iArr[3] = i;
                        iArr[4] = 255;
                        iArr[5] = 255;
                        iArr[6] = 255;
                        iArr[7] = 255;
                        iArr[8] = 239;
                    }
                } else if (str.equalsIgnoreCase("LEDDMX-00-") || str.equalsIgnoreCase("LEDDMX-02-") || str.equalsIgnoreCase("LEDDMX-03-")) {
                    iArr = new int[]{123, 255, 19, i, 255, 255, 255, 255, 191};
                }
                sendData(iArr);
            }
            iArr = new int[9];
            iArr[0] = 126;
            if (z2) {
                i3 = 2;
            } else if (!z) {
                i3 = 1;
            }
            iArr[1] = i3;
            iArr[2] = 14;
            iArr[3] = i;
            iArr[4] = 255;
            iArr[5] = 255;
            iArr[6] = 255;
            iArr[7] = 255;
            iArr[8] = 239;
            sendData(iArr);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setVoiceCtlAndMusicMode(boolean z, boolean z2, boolean z3, int i, String str) {
        int[] iArr = null;
        try {
            int i2 = 11;
            if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-02-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                if (str.equalsIgnoreCase("LEDDMX-01-")) {
                    iArr = new int[9];
                    iArr[0] = 123;
                    iArr[1] = 4;
                    iArr[2] = 11;
                    iArr[3] = i;
                    iArr[4] = z2 ? 1 : 0;
                    iArr[5] = 255;
                    iArr[6] = 255;
                    iArr[7] = 255;
                    iArr[8] = 191;
                } else if (str.equalsIgnoreCase("LEDDMX-02-")) {
                    iArr = new int[9];
                    iArr[0] = 123;
                    iArr[1] = 255;
                    iArr[2] = 11;
                    iArr[3] = i;
                    iArr[4] = z2 ? 1 : 0;
                    iArr[5] = 255;
                    iArr[6] = 255;
                    iArr[7] = 255;
                    iArr[8] = 191;
                } else {
                    if (!str.equalsIgnoreCase(CommonConstant.LEDBLE_00) && !str.equalsIgnoreCase(CommonConstant.LEDBLE_01) && !str.equalsIgnoreCase("LEDCAR-00-")) {
                        if (str.equalsIgnoreCase("LEDCAR-01-")) {
                            iArr = new int[9];
                            iArr[0] = 123;
                            iArr[1] = 255;
                            if (!z3) {
                                i2 = 1;
                            }
                            iArr[2] = i2;
                            iArr[3] = i;
                            iArr[4] = z2 ? 1 : 0;
                            iArr[5] = 255;
                            iArr[6] = 255;
                            iArr[7] = 255;
                            iArr[8] = 191;
                        }
                    }
                    iArr = new int[9];
                    iArr[0] = 126;
                    if (z2) {
                        r16 = 2;
                    } else if (!z) {
                        r16 = 1;
                    }
                    iArr[1] = r16;
                    iArr[2] = 14;
                    iArr[3] = i;
                    iArr[4] = 255;
                    iArr[5] = 255;
                    iArr[6] = 255;
                    iArr[7] = 255;
                    iArr[8] = 239;
                }
                sendData(iArr);
            }
            iArr = new int[9];
            iArr[0] = 123;
            iArr[1] = 255;
            if (!z2 && !z) {
                i2 = 19;
            }
            iArr[2] = i2;
            iArr[3] = i;
            iArr[4] = z2 ? 1 : 0;
            iArr[5] = 255;
            iArr[6] = 255;
            iArr[7] = 255;
            iArr[8] = 191;
            sendData(iArr);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSmartBrightness(int i, int i2, String str) {
        String hexString;
        String hexString2;
        String hexString3;
        try {
            if (str.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                Log.e(TAG, "setSmartBrightness: " + i2 + "===" + i);
                sendData(new int[]{126, 255, 7, i, i2, 255, 255, 255, 239});
                return;
            }
            if (!str.equalsIgnoreCase("LEDDMX-01-") && !str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                if (!str.equalsIgnoreCase("LEDDMX-00-") && !str.equalsIgnoreCase("LEDDMX-03-")) {
                    if (str.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                        sendData(new int[]{125, 1, 2, i2, i, 255, 255, 255, 223});
                        return;
                    }
                    return;
                }
                sendData(new int[]{123, 255, 8, i, (i2 * 32) / 100, i2, 255, 255, 191});
                return;
            }
            int i3 = (i2 * 32) / 100;
            if (str.equalsIgnoreCase("LEDDMX-01-")) {
                sendData(new int[]{123, 4, 8, i, i3, i2, 255, 255, 191});
                return;
            }
            if (Integer.toHexString(i).length() <= 1) {
                hexString = NetResult.CODE_OK + Integer.toHexString(i);
            } else {
                hexString = Integer.toHexString(i);
            }
            if (Integer.toHexString(i2).length() <= 1) {
                hexString2 = NetResult.CODE_OK + Integer.toHexString(i2);
            } else {
                hexString2 = Integer.toHexString(i3);
            }
            if (Integer.toHexString(i3).length() <= 1) {
                hexString3 = NetResult.CODE_OK + Integer.toHexString(i3);
            } else {
                hexString3 = Integer.toHexString(i3);
            }
            sendTuyaCommand("101", "7b0408" + hexString + hexString3 + hexString2 + "ffffbf");
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSmartTimeSet(int i, int i2, int i3, int i4) {
        try {
            sendData(new int[]{125, 1, 3, i, i2, i3, i4, 255, 223});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSmartFanSet(int i) {
        try {
            sendData(new int[]{125, 1, 5, i, 255, 255, 255, 255, 223});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSmartCheck(int i) {
        try {
            sendSmartDataToFFE1WithCallback(new int[]{125, 1, 5, i, 255, 255, 255, 255, 223});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSmartBubbleCheck(int i) {
        try {
            sendData(new int[]{125, 1, 6, i, 255, 255, 255, 255, 223});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void sendSmartDataToFFE1WithCallback(int[] iArr) {
        BluetoothGatt value;
        List<BluetoothGattDescriptor> descriptors;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i : iArr) {
                byteArrayOutputStream.write(Tool.int2bytearray(i));
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            HashMap<String, BluetoothGatt> bleGattMap = LedBleApplication.getApp().getBleGattMap();
            if (bleGattMap == null || bleGattMap.isEmpty()) {
                return;
            }
            if (!ListUtiles.isEmpty(this.groupDevices) || StringUtils.isEmpty(this.groupName)) {
                for (Map.Entry<String, BluetoothGatt> entry : bleGattMap.entrySet()) {
                    try {
                        value = entry.getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (value != null) {
                        BluetoothGattCharacteristic characteristic = value.getService(UUID.fromString(FFE0)).getCharacteristic(UUID.fromString(FFE1));
                        if (characteristic != null) {
                            characteristic.setValue(byteArray);
                            value.writeCharacteristic(characteristic);
                            if (!value.setCharacteristicNotification(characteristic, true) || (descriptors = characteristic.getDescriptors()) == null || descriptors.size() <= 0) {
                                return;
                            }
                            for (BluetoothGattDescriptor bluetoothGattDescriptor : descriptors) {
                                bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                value.writeDescriptor(bluetoothGattDescriptor);
                            }
                            return;
                        }
                        return;
                    }
                    continue;
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void setSunVcMode(int i) {
        try {
            sendData(new int[]{122, 7, i + 128, 255, 255, 255, 255, 255, 175});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setSunSensitivity(int i) {
        try {
            sendData(new int[]{122, 8, i, 255, 255, 255, 255, 255, 175});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setPassword(int i, int i2, int i3, int i4, int i5, int i6) {
        try {
            sendPasswordDataWithCallback(new int[]{26, 1, i, i2, i3, i4, i5, i6, 175});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setPasswordFeedback() {
        try {
            sendPasswordDataWithCallback(new int[]{26, 5, 255, 255, 255, 255, 255, 255, 175});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02Speed(int i) {
        try {
            sendData(new int[]{123, 255, 2, i, 0, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02Bright(int i) {
        try {
            sendData(new int[]{123, 255, 1, (i * 32) / 100, i, 0, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02AnimationMode(int i) {
        try {
            sendData(new int[]{123, 255, 23, i, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02AnimationPlay(int i) {
        try {
            sendData(new int[]{123, 255, 6, i, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02AnimationDirection(int i) {
        try {
            sendData(new int[]{123, 255, 13, i, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02AnimationCycle(int i) {
        try {
            sendData(new int[]{123, 255, 15, 1, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02ChangeColor(final ArrayList<MyColor> arrayList) {
        try {
            this.k = 0;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: com.home.net.NetConnectBle.9
                @Override // java.lang.Runnable
                public void run() {
                    if (NetConnectBle.this.k == arrayList.size()) {
                        handler.removeCallbacks(this);
                        NetConnectBle.this.k = 0;
                        return;
                    }
                    try {
                        NetConnectBle.this.sendData(new int[]{123, NetConnectBle.this.k + 1, 14, 253, ((MyColor) arrayList.get(NetConnectBle.this.k)).r, ((MyColor) arrayList.get(NetConnectBle.this.k)).g, ((MyColor) arrayList.get(NetConnectBle.this.k)).b, arrayList.size(), 191});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    NetConnectBle.access$008(NetConnectBle.this);
                    handler.postDelayed(this, 100L);
                }
            }, 100L);
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void sendDmx02DataToFFE1WithCallback(int[] iArr) {
        List<BluetoothGattDescriptor> descriptors;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i : iArr) {
                byteArrayOutputStream.write(Tool.int2bytearray(i));
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            HashMap<String, BluetoothGatt> bleGattMap = LedBleApplication.getApp().getBleGattMap();
            if (bleGattMap == null || bleGattMap.isEmpty()) {
                return;
            }
            if (!ListUtiles.isEmpty(this.groupDevices) || StringUtils.isEmpty(this.groupName)) {
                for (Map.Entry<String, BluetoothGatt> entry : bleGattMap.entrySet()) {
                    try {
                        BluetoothGatt value = entry.getValue();
                        BluetoothGattCharacteristic characteristic = value.getService(UUID.fromString(FFE0)).getCharacteristic(UUID.fromString(FFE1));
                        if (characteristic != null) {
                            characteristic.setValue(byteArray);
                            value.writeCharacteristic(characteristic);
                            if (value.setCharacteristicNotification(characteristic, true) && (descriptors = characteristic.getDescriptors()) != null && descriptors.size() > 0) {
                                for (BluetoothGattDescriptor bluetoothGattDescriptor : descriptors) {
                                    bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    value.writeDescriptor(bluetoothGattDescriptor);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void sendDmx02DataToFFE2WithCallback(int[] iArr) {
        List<BluetoothGattDescriptor> descriptors;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i : iArr) {
                byteArrayOutputStream.write(Tool.int2bytearray(i));
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            HashMap<String, BluetoothGatt> bleGattMap = LedBleApplication.getApp().getBleGattMap();
            if (bleGattMap == null || bleGattMap.isEmpty()) {
                return;
            }
            if (!ListUtiles.isEmpty(this.groupDevices) || StringUtils.isEmpty(this.groupName)) {
                for (Map.Entry<String, BluetoothGatt> entry : bleGattMap.entrySet()) {
                    try {
                        BluetoothGatt value = entry.getValue();
                        BluetoothGattCharacteristic characteristic = value.getService(UUID.fromString(FFE0)).getCharacteristic(UUID.fromString(FFE2));
                        if (characteristic != null) {
                            characteristic.setValue(byteArray);
                            value.writeCharacteristic(characteristic);
                            if (value.setCharacteristicNotification(characteristic, true) && (descriptors = characteristic.getDescriptors()) != null && descriptors.size() > 0) {
                                for (BluetoothGattDescriptor bluetoothGattDescriptor : descriptors) {
                                    bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    value.writeDescriptor(bluetoothGattDescriptor);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void setDmx02CustomanimationMode(int i) {
        try {
            sendData(new int[]{123, 255, 21, i, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02TextFoneSize(int i) {
        try {
            sendData(new int[]{123, i, 24, 255, 255, 255, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02TextColor(int[] iArr) {
        try {
            sendData(new int[]{123, 1, 22, iArr[0], iArr[1], iArr[2], 0, 0, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02TextAlignmentColorBackgroundAnimation(int i, int[] iArr, int i2, int i3) {
        try {
            sendData(new int[]{123, i, 22, iArr[0], iArr[1], iArr[2], i2, i3, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setDmx02GraffitColor(int i, int i2, int i3, int i4) {
        try {
            sendData(new int[]{123, i, 25, i2, i3, i4, 255, 255, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    public void setConfigDmx02(int i, int i2, byte b, byte b2, byte b3, byte b4) {
        try {
            sendData(new int[]{123, i, 20, i2, b, b2, b3, b4, 191});
        } catch (Exception e) {
            NetExceptionInterface netExceptionInterface = this.exceptionCallBack;
            if (netExceptionInterface != null) {
                netExceptionInterface.onException(e);
            }
        }
    }

    private String to04Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 4 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    private int[] getNumb(int i) {
        int[] iArr = new int[4];
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 8 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        String substring = hexString.substring(4);
        String substring2 = hexString.substring(0, 4);
        iArr[0] = Integer.parseInt(substring.substring(2), 16);
        iArr[1] = Integer.parseInt(substring.substring(0, 2), 16);
        iArr[2] = Integer.parseInt(substring2.substring(2), 16);
        iArr[3] = Integer.parseInt(substring2.substring(0, 2), 16);
        return iArr;
    }

    private static int[] HexStringToIntArray(String str) {
        int length = str.length() / 2;
        String[] strArr = new String[length];
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int i3 = i2 + 2;
            strArr[i] = str.substring(i2, i3);
            iArr[i] = Integer.parseInt(str.substring(i2, i3), 16);
        }
        return iArr;
    }

    public void sendPasswordDataWithCallback(int[] iArr) {
        List<BluetoothGattDescriptor> descriptors;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i : iArr) {
                byteArrayOutputStream.write(Tool.int2bytearray(i));
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            HashMap<String, BluetoothGatt> bleGattMap = LedBleApplication.getApp().getBleGattMap();
            if (bleGattMap == null || bleGattMap.isEmpty()) {
                return;
            }
            if (!ListUtiles.isEmpty(this.groupDevices) || StringUtils.isEmpty(this.groupName)) {
                for (Map.Entry<String, BluetoothGatt> entry : bleGattMap.entrySet()) {
                    try {
                        BluetoothGatt value = entry.getValue();
                        BluetoothGattCharacteristic characteristic = value.getService(UUID.fromString(FFE0)).getCharacteristic(UUID.fromString(FFE1));
                        if (characteristic != null) {
                            characteristic.setValue(byteArray);
                            value.writeCharacteristic(characteristic);
                            if (value.setCharacteristicNotification(characteristic, true) && (descriptors = characteristic.getDescriptors()) != null && descriptors.size() > 0) {
                                for (BluetoothGattDescriptor bluetoothGattDescriptor : descriptors) {
                                    bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    value.writeDescriptor(bluetoothGattDescriptor);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void sendData(int[] iArr) throws IOException {
        if (MainActivity_BLE.getMainActivity() != null) {
            Utils.getInstance();
            Utils.versionIdentificationTips(MainActivity_BLE.getMainActivity(), MainActivity_BLE.getSceneBean());
        }
        if (MainActivity_DMX02.getMainActivity() != null) {
            Utils.getInstance();
            Utils.versionIdentificationTips(MainActivity_DMX02.getMainActivity(), MainActivity_DMX02.getSceneBean());
            if (LedBleApplication.getApp().getSafetyLevel() == 1) {
                Toast.makeText(MainActivity_DMX02.getMainActivity(), "" + MainActivity_DMX02.getMainActivity().getResources().getString(R.string.password_error), 0).show();
            } else if (LedBleApplication.getApp().getSafetyLevel() == 2) {
                Toast.makeText(MainActivity_DMX02.getMainActivity(), "" + MainActivity_DMX02.getMainActivity().getResources().getString(R.string.The_password_cannot_be_the_ame_number_or_letter), 0).show();
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i : iArr) {
            byteArrayOutputStream.write(Tool.int2bytearray(i));
        }
        sendCharacteristic(byteArrayOutputStream.toByteArray());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTuyaCommand(String str, Object obj) {
        JSON.toJSONString(new HashMap());
    }
}
