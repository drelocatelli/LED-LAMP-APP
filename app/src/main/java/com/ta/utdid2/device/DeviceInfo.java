package com.ta.utdid2.device;

import android.content.Context;
import com.ta.utdid2.android.utils.PhoneInfoUtils;
import com.ta.utdid2.android.utils.StringUtils;
import java.util.zip.Adler32;

/* loaded from: classes.dex */
public class DeviceInfo {
    static final Object CREATE_DEVICE_METADATA_LOCK = new Object();
    static String HMAC_KEY = "d6fc3a4a06adbde89223bvefedc24fecde188aaa9161";
    static final byte UTDID_VERSION_CODE = 1;
    private static Device mDevice;

    static long getMetadataCheckSum(Device device) {
        if (device != null) {
            String format = String.format("%s%s%s%s%s", device.getUtdid(), device.getDeviceId(), Long.valueOf(device.getCreateTimestamp()), device.getImsi(), device.getImei());
            if (StringUtils.isEmpty(format)) {
                return 0L;
            }
            Adler32 adler32 = new Adler32();
            adler32.reset();
            adler32.update(format.getBytes());
            return adler32.getValue();
        }
        return 0L;
    }

    private static Device _initDeviceMetadata(Context context) {
        if (context != null) {
            new Device();
            synchronized (CREATE_DEVICE_METADATA_LOCK) {
                String value = UTUtdid.instance(context).getValue();
                if (StringUtils.isEmpty(value)) {
                    return null;
                }
                if (value.endsWith("\n")) {
                    value = value.substring(0, value.length() - 1);
                }
                Device device = new Device();
                long currentTimeMillis = System.currentTimeMillis();
                String imei = PhoneInfoUtils.getImei(context);
                String imsi = PhoneInfoUtils.getImsi(context);
                device.setDeviceId(imei);
                device.setImei(imei);
                device.setCreateTimestamp(currentTimeMillis);
                device.setImsi(imsi);
                device.setUtdid(value);
                device.setCheckSum(getMetadataCheckSum(device));
                return device;
            }
        }
        return null;
    }

    public static synchronized Device getDevice(Context context) {
        synchronized (DeviceInfo.class) {
            Device device = mDevice;
            if (device != null) {
                return device;
            }
            if (context != null) {
                Device _initDeviceMetadata = _initDeviceMetadata(context);
                mDevice = _initDeviceMetadata;
                return _initDeviceMetadata;
            }
            return null;
        }
    }
}
