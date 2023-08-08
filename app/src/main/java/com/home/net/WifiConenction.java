package com.home.net;

import android.content.Context;
import android.os.Handler;
import android.text.format.Time;
import com.alibaba.fastjson.JSON;
import com.common.net.NetResult;
import com.common.uitl.Constant;
import com.common.uitl.LogUtil;
import com.common.uitl.NumberHelper;
import com.common.uitl.Tool;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.weigan.loopview.MessageHandler;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class WifiConenction extends BaseConnection {
    public static final String LOCALHOST = "192.168.2.3";
    public static final int PORT = 5000;
    private static WifiConenction netConnect = null;
    public static String tag = "WifiConenction";
    Handler handlerrgb;
    private InputStream inputStream;
    private ArrayList<String> ips;
    private boolean isUDP;
    private int k;
    private OutputStream outStream;
    private DataInputStream reader;
    private Socket socket;
    private int value;
    boolean aa = true;
    private boolean isMusic = false;

    @Override // com.home.net.BaseConnection
    public void sendSearchOnlineDevices() throws Exception {
    }

    static /* synthetic */ int access$008(WifiConenction wifiConenction) {
        int i = wifiConenction.k;
        wifiConenction.k = i + 1;
        return i;
    }

    public WifiConenction(Context context) {
    }

    public WifiConenction() {
    }

    public static WifiConenction getInstance() {
        if (netConnect == null) {
            netConnect = new WifiConenction();
        }
        return netConnect;
    }

    public WifiConenction(Context context, boolean z, ArrayList<String> arrayList) {
        this.isUDP = z;
        this.ips = arrayList;
    }

    @Override // com.home.net.BaseConnection
    public void open() throws Exception {
        write(new int[]{126, 255, 4, 1, 255, 255, 255, 255, 239});
        sendTuyaCommand("101", "7eff0401ffffffffef");
    }

    @Override // com.home.net.BaseConnection
    public void close() throws Exception {
        write(new int[]{126, 255, 4, 0, 255, 255, 255, 255, 239});
        sendTuyaCommand("101", "7eff0400ffffffffef");
    }

    @Override // com.home.net.BaseConnection
    public void setRgb(int i, int i2, int i3, String str) throws Exception {
        String hexString;
        String hexString2;
        String hexString3;
        write(new int[]{126, 255, 5, 3, i, i2, i3, 255, 239});
        if (str.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
            if (this.handlerrgb == null) {
                this.handlerrgb = new Handler();
            }
            if (this.aa) {
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
                sendTuyaCommand("101", "7eff0503" + hexString + hexString2 + hexString3 + "ffef");
                sendTuyaCommand("101", "7b0407" + hexString + hexString2 + hexString3 + "ffffbf");
                this.aa = false;
                this.handlerrgb.postDelayed(new Runnable() { // from class: com.home.net.WifiConenction.1
                    @Override // java.lang.Runnable
                    public void run() {
                        WifiConenction.this.aa = true;
                    }
                }, 50L);
            }
        }
    }

    @Override // com.home.net.BaseConnection
    public void setRgbMode(int i) throws Exception {
        String hexString;
        write(new int[]{126, 255, 3, i, 3, 255, 255, 255, 239});
        if (Integer.toHexString(i).length() <= 1) {
            hexString = NetResult.CODE_OK + Integer.toHexString(i);
        } else {
            hexString = Integer.toHexString(i);
        }
        sendTuyaCommand("101", "7eff03" + hexString + "03ffffffef");
    }

    @Override // com.home.net.BaseConnection
    public void setSpeed(int i) throws Exception {
        String hexString;
        write(new int[]{126, 255, 2, i, 255, 255, 255, 255, 239});
        if (Integer.toHexString(i).length() <= 1) {
            hexString = NetResult.CODE_OK + Integer.toHexString(i);
        } else {
            hexString = Integer.toHexString(i);
        }
        sendTuyaCommand("101", "7eff02" + hexString + "ffffffffef");
    }

    @Override // com.home.net.BaseConnection
    public void setBrightness(int i) throws Exception {
        String hexString;
        String hexString2;
        write(new int[]{126, 255, 1, i, 255, 255, 255, 255, 239});
        int i2 = (i * 32) / 100;
        if (Integer.toHexString(i2).length() <= 1) {
            hexString = NetResult.CODE_OK + Integer.toHexString(i2);
        } else {
            hexString = Integer.toHexString(i2);
        }
        if (Integer.toHexString(i).length() <= 1) {
            hexString2 = NetResult.CODE_OK + Integer.toHexString(i);
        } else {
            hexString2 = Integer.toHexString(i);
        }
        sendTuyaCommand("101", "7b0401" + hexString + hexString2 + "ffffffbf");
    }

    public void sendTimeLightandStage(int i, int i2, int i3, int i4, int i5, int i6) throws Exception {
        Thread.sleep(200L);
        write(new int[]{126, i, 8, i2, i3, i4, i5, i6, 239});
    }

    public void setDiy(final ArrayList<MyColor> arrayList, final int i) {
        try {
            this.k = 0;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: com.home.net.WifiConenction.2
                @Override // java.lang.Runnable
                public void run() {
                    String hexString;
                    String hexString2;
                    String hexString3;
                    String hexString4;
                    String hexString5;
                    if (WifiConenction.this.k != arrayList.size()) {
                        int i2 = ((MyColor) arrayList.get(WifiConenction.this.k)).r;
                        int i3 = ((MyColor) arrayList.get(WifiConenction.this.k)).g;
                        int i4 = ((MyColor) arrayList.get(WifiConenction.this.k)).b;
                        int[] iArr = {126, 255, 6, i, i2, i3, i4, arrayList.size(), 239};
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
                        if (Integer.toHexString(arrayList.size()).length() <= 1) {
                            hexString5 = NetResult.CODE_OK + Integer.toHexString(arrayList.size());
                        } else {
                            hexString5 = Integer.toHexString(arrayList.size());
                        }
                        WifiConenction.this.sendTuyaCommand("101", "7eff06" + hexString + hexString2 + hexString3 + hexString4 + hexString5 + "ef");
                        WifiConenction.access$008(WifiConenction.this);
                        try {
                            WifiConenction.this.write(iArr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.postDelayed(this, 200L);
                        return;
                    }
                    handler.removeCallbacks(this);
                    WifiConenction.this.k = 0;
                }
            }, 100L);
        } catch (Exception unused) {
        }
    }

    @Override // com.home.net.BaseConnection
    public void setColorWarmModel(int i) throws Exception {
        write(new int[]{126, 255, 3, i, 2, 255, 255, 255, 239});
    }

    @Override // com.home.net.BaseConnection
    public void setDimModel(int i) throws Exception {
        write(new int[]{126, 255, 3, i, 1, 255, 255, 255, 239});
    }

    @Override // com.home.net.BaseConnection
    public void setDim(int i) throws Exception {
        String hexString;
        write(new int[]{126, 255, 5, 1, i, 255, 255, 255, 239});
        if (Integer.toHexString(i).length() <= 1) {
            hexString = NetResult.CODE_OK + Integer.toHexString(i);
        } else {
            hexString = Integer.toHexString(i);
        }
        sendTuyaCommand("101", "7eff0501" + hexString + "ffffffef");
    }

    @Override // com.home.net.BaseConnection
    public void setMusic(int i, int i2, int i3, int i4) throws Exception {
        write(new int[]{126, 7, 6, i, 0, 0, 0, 0, 239});
    }

    @Override // com.home.net.BaseConnection
    public void setColorWarm(int i, int i2) throws Exception {
        String hexString;
        String hexString2;
        write(new int[]{126, 255, 5, 2, i, i2, 255, 255, 239});
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
    }

    public void setModeCycle(int i, int i2, int i3, int i4, int i5, int i6) throws Exception {
        write(new int[]{126, i, 15, 1, i2, i3, i4, i5, 239});
    }

    @Override // com.home.net.BaseConnection
    public void setSPIBrightness(int i) throws Exception {
        LogUtil.i(tag, "setSPIBrightness");
        write(new int[]{123, 4, 1, i, 255, 255, 255, 0, 191});
    }

    @Override // com.home.net.BaseConnection
    public void setSPISpeed(int i) throws Exception {
        LogUtil.i(tag, "setSPISpeed");
        write(new int[]{123, 4, 2, i, 255, 255, 255, 0, 191});
    }

    public void setSPIModel(int i) throws Exception {
        LogUtil.i(tag, "setSPIModel");
        write(new int[]{123, 5, 3, i, 3, 255, 255, 0, 191});
    }

    @Override // com.home.net.BaseConnection
    public void turnOnSPI(int i) throws Exception {
        LogUtil.i(tag, "turnOnSPI");
        write(new int[]{123, 4, 4, i, 255, 255, 255, 0, 191});
    }

    @Override // com.home.net.BaseConnection
    public void configSPI(int i, byte b, byte b2, int i2) throws Exception {
        LogUtil.i(tag, "configSPI");
        write(new int[]{123, 4, 5, i, b, b2, i2, 0, 191});
    }

    @Override // com.home.net.BaseConnection
    public void pauseSPI(int i) throws Exception {
        LogUtil.i(tag, "pauseSPI");
        write(new int[]{123, 4, 6, i, 255, 255, 255, 0, 191});
    }

    public void sendOffline() throws Exception {
        LogUtil.i(tag, "ssid");
        write(new int[]{126, 7, 11, 255, 255, 255, 255, 255, 239});
    }

    @Override // com.home.net.BaseConnection
    public void sendRouteSSID() throws Exception {
        LogUtil.i(tag, "ssid");
        write(new int[]{126, 7, 7, 255, 255, 255, 255, 255, 239});
    }

    @Override // com.home.net.BaseConnection
    public void sendRoutePassword() throws Exception {
        LogUtil.i(tag, "password");
        write(new int[]{126, 7, 8, 255, 255, 255, 255, 255, 239});
    }

    @Override // com.home.net.BaseConnection
    public void sendSSID(String str) throws Exception {
        writeByte((str + "\n").getBytes("utf-8"));
    }

    @Override // com.home.net.BaseConnection
    public void sendPassword(String str) throws Exception {
        writeByte((str + "\n").getBytes("utf-8"));
    }

    public void setSmartBrightness(int i, int i2) throws Exception {
        write(new int[]{126, 255, 7, i, i2, 255, 255, 255, 239});
    }

    public void code_check(int i, int i2) throws Exception {
        write(new int[]{126, 255, 10, i, i2, 255, 255, 255, 191});
    }

    public void SetCHN(int i, int i2, int i3, int i4, int i5, int i6) throws Exception {
        write(new int[]{126, i, 11, i2, i3, i4, i5, i6, 239});
    }

    public void setSmartCheck(int i) throws Exception {
        write(new int[]{126, 255, 9, i, 255, 255, 255, 255, 239});
    }

    public ArrayList<String> setTBCheck(int i) throws Exception {
        write(new int[]{126, 255, 9, i, 255, 255, 255, 255, 239});
        return null;
    }

    public ArrayList<String> TBCheckRecv() throws Exception {
        try {
            Socket socket = this.socket;
            if (socket == null || !socket.isConnected()) {
                return null;
            }
            String str = new String();
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                this.inputStream.available();
                int read = this.inputStream.read();
                if (read > 0 && (i == 3 || i == 4)) {
                    str = str + Integer.toString(read);
                    arrayList.add(Integer.toString(read));
                }
            }
            return arrayList;
        } catch (Exception unused) {
            return null;
        }
    }

    private int SocketRecv(byte[] bArr) {
        try {
            InputStream inputStream = this.inputStream;
            if (inputStream != null) {
                return inputStream.read(bArr);
            }
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String receiveDataReturned(String str) throws Exception {
        InetAddress byName = InetAddress.getByName(LOCALHOST);
        byte[] bytes = ("xcmd_req::cmd=encrypt,key=1,data=" + str + "\r\n").getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, byName, 25000);
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.send(datagramPacket);
        byte[] bArr = new byte[1024];
        DatagramPacket datagramPacket2 = new DatagramPacket(bArr, 1024);
        datagramSocket.setSoTimeout(MessageHandler.WHAT_SMOOTH_SCROLL);
        datagramSocket.receive(datagramPacket2);
        String str2 = new String(bArr, 0, datagramPacket2.getLength());
        datagramSocket.close();
        return str2;
    }

    @Override // com.home.net.BaseConnection
    public void sendRouteCommand(String str, String str2) throws Exception {
        sendRouteSSID();
        Thread.sleep(300L);
        sendSSID(str);
        Thread.sleep(300L);
        sendRoutePassword();
        Thread.sleep(300L);
        sendPassword(str2);
    }

    private static int computeTime(int i, int i2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.STRING_DAY_FORMAT5);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(Constant.STRING_DAY_FORMAT2);
        Date time = Calendar.getInstance().getTime();
        String format = simpleDateFormat2.format(time);
        int time2 = (int) ((simpleDateFormat.parse(format + " " + NumberHelper.LeftPad_Tow_Zero(i) + ":" + NumberHelper.LeftPad_Tow_Zero(i2)).getTime() - time.getTime()) / 1000);
        return time2 < 0 ? time2 + 86400 : time2;
    }

    public void turnOnOffTimerOn(int i, int i2, int i3, int i4) {
        try {
            Time time = new Time();
            time.setToNow();
            write(new int[]{126, i, 8, time.hour, time.minute, i2, i3, i4, 239});
        } catch (Exception unused) {
        }
    }

    public void turnOnOrOffTimerOff(int i, int i2, int i3, int i4) {
        try {
            Time time = new Time();
            time.setToNow();
            write(new int[]{126, i, 8, time.hour, time.minute, i2, i3, i4, 239});
        } catch (Exception unused) {
        }
    }

    public void timerOn(int i, int i2, int i3) {
        try {
            int computeTime = computeTime(i, i2);
            int i4 = computeTime / 60;
            write(new int[]{126, 1, 13, (byte) (i4 >> 8), (byte) i4, 1, i3, computeTime % 60, 239});
        } catch (Exception unused) {
        }
    }

    public void timerOff(int i, int i2) {
        try {
            int computeTime = computeTime(i, i2);
            int i3 = computeTime / 60;
            write(new int[]{126, 1, 13, (byte) (i3 >> 8), (byte) i3, 0, 255, computeTime % 60, 239});
        } catch (Exception unused) {
        }
    }

    @Override // com.home.net.BaseConnection
    public boolean isOnLine() {
        if (this.isUDP) {
            return true;
        }
        Socket socket = this.socket;
        return (socket == null || socket.isClosed()) ? false : true;
    }

    public void isMusic(boolean z) {
        this.isMusic = z;
    }

    @Override // com.home.net.BaseConnection
    public void write(final int[] iArr) throws Exception {
        if (this.isUDP) {
            ArrayList<String> arrayList = this.ips;
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            if (this.isMusic) {
                Iterator<String> it = this.ips.iterator();
                while (it.hasNext()) {
                    sendBroadcast(iArr, it.next());
                }
                return;
            }
            new Thread(new Runnable() { // from class: com.home.net.WifiConenction.3
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it2 = WifiConenction.this.ips.iterator();
                    while (it2.hasNext()) {
                        WifiConenction.this.sendBroadcast(iArr, (String) it2.next());
                    }
                }
            }).start();
            return;
        }
        new Thread(new Runnable() { // from class: com.home.net.WifiConenction.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int i = 0;
                    while (true) {
                        int[] iArr2 = iArr;
                        if (i >= iArr2.length) {
                            break;
                        }
                        byteArrayOutputStream.write(Tool.int2bytearray(iArr2[i]));
                        i++;
                    }
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    if (byteArray == null || WifiConenction.this.outStream == null) {
                        return;
                    }
                    WifiConenction.this.outStream.write(byteArray);
                    WifiConenction.this.outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override // com.home.net.BaseConnection
    public void writeByte(byte[] bArr) throws Exception {
        this.outStream.write(bArr);
        this.outStream.flush();
    }

    @Override // com.home.net.BaseConnection
    public void closeSocket() {
        if (this.isUDP) {
            return;
        }
        try {
            Socket socket = this.socket;
            if (socket != null) {
                socket.close();
                this.socket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.home.net.BaseConnection
    public boolean connect() {
        if (this.isUDP) {
            return true;
        }
        closeSocket();
        try {
            Socket socket = new Socket(InetAddress.getByName(LOCALHOST), 5000);
            this.socket = socket;
            this.inputStream = socket.getInputStream();
            this.outStream = this.socket.getOutputStream();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendBroadcast(int[] iArr, String str) {
        DatagramSocket datagramSocket = null;
        try {
            try {
                DatagramSocket datagramSocket2 = new DatagramSocket();
                try {
                    InetAddress byName = InetAddress.getByName(str);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    for (int i : iArr) {
                        byteArrayOutputStream.write(Tool.int2bytearray(i));
                    }
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    datagramSocket2.send(new DatagramPacket(byteArray, byteArray.length, byName, 5000));
                    datagramSocket2.close();
                } catch (Exception e) {
                    e = e;
                    datagramSocket = datagramSocket2;
                    e.printStackTrace();
                    if (datagramSocket != null) {
                        datagramSocket.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    datagramSocket = datagramSocket2;
                    if (datagramSocket != null) {
                        datagramSocket.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTuyaCommand(String str, Object obj) {
        HashMap hashMap = new HashMap();
        hashMap.put(str, obj);
        JSON.toJSONString(hashMap);
    }
}
