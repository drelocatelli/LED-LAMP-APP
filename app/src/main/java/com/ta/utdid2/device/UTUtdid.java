package com.ta.utdid2.device;

import android.content.Context;
import android.provider.Settings;
import androidx.exifinterface.media.ExifInterface;
import com.ta.utdid2.android.utils.Base64;
import com.ta.utdid2.android.utils.IntUtils;
import com.ta.utdid2.android.utils.PhoneInfoUtils;
import com.ta.utdid2.android.utils.StringUtils;
import com.ta.utdid2.core.persistent.PersistentConfiguration;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class UTUtdid {
    private static final String HMAC_KEY = "d6fc3a4a06adbde89223bvefedc24fecde188aaa9161";
    private static final String S_GLOBAL_PERSISTENT_CONFIG_KEY = "Alvin2";
    private static final String S_LOCAL_STORAGE_KEY = "ContextData";
    private static final String S_LOCAL_STORAGE_NAME = ".DataStorage";
    static final String UM_SETTINGS_STORAGE = "dxCRMxhQkdGePGnp";
    static final String UM_SETTINGS_STORAGE_NEW = "mqBRboGZkQPcAkyk";
    private static UTUtdid s_umutdid;
    private String mCBDomain;
    private String mCBKey;
    private Context mContext;
    private PersistentConfiguration mPC;
    private PersistentConfiguration mTaoPC;
    private UTUtdidHelper mUtdidHelper;
    private static final Object CREATE_LOCK = new Object();
    private static final String S_GLOBAL_PERSISTENT_CONFIG_DIR = ".UTSystemConfig" + File.separator + "Global";
    private String mUtdid = null;
    private Pattern mPattern = Pattern.compile("[^0-9a-zA-Z=/+]+");

    public UTUtdid(Context context) {
        this.mContext = null;
        this.mUtdidHelper = null;
        this.mCBKey = "xx_utdid_key";
        this.mCBDomain = "xx_utdid_domain";
        this.mPC = null;
        this.mTaoPC = null;
        this.mContext = context;
        this.mTaoPC = new PersistentConfiguration(context, S_GLOBAL_PERSISTENT_CONFIG_DIR, S_GLOBAL_PERSISTENT_CONFIG_KEY, false, true);
        this.mPC = new PersistentConfiguration(context, S_LOCAL_STORAGE_NAME, S_LOCAL_STORAGE_KEY, false, true);
        this.mUtdidHelper = new UTUtdidHelper();
        this.mCBKey = String.format("K_%d", Integer.valueOf(StringUtils.hashCode(this.mCBKey)));
        this.mCBDomain = String.format("D_%d", Integer.valueOf(StringUtils.hashCode(this.mCBDomain)));
    }

    public static UTUtdid instance(Context context) {
        if (context != null && s_umutdid == null) {
            synchronized (CREATE_LOCK) {
                if (s_umutdid == null) {
                    s_umutdid = new UTUtdid(context);
                }
            }
        }
        return s_umutdid;
    }

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

    private void saveUtdidToTaoPPC(String str) {
        PersistentConfiguration persistentConfiguration;
        if (isValidUTDID(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.length() != 24 || (persistentConfiguration = this.mTaoPC) == null) {
                return;
            }
            String string = persistentConfiguration.getString("UTDID");
            String string2 = this.mTaoPC.getString("EI");
            if (StringUtils.isEmpty(string2)) {
                string2 = PhoneInfoUtils.getImei(this.mContext);
            }
            String string3 = this.mTaoPC.getString("SI");
            if (StringUtils.isEmpty(string3)) {
                string3 = PhoneInfoUtils.getImsi(this.mContext);
            }
            String string4 = this.mTaoPC.getString("DID");
            if (StringUtils.isEmpty(string4)) {
                string4 = string2;
            }
            if (string == null || !string.equals(str)) {
                Device device = new Device();
                device.setImei(string2);
                device.setImsi(string3);
                device.setUtdid(str);
                device.setDeviceId(string4);
                device.setCreateTimestamp(System.currentTimeMillis());
                this.mTaoPC.putString("UTDID", str);
                this.mTaoPC.putString("EI", device.getImei());
                this.mTaoPC.putString("SI", device.getImsi());
                this.mTaoPC.putString("DID", device.getDeviceId());
                this.mTaoPC.putLong("timestamp", device.getCreateTimestamp());
                this.mTaoPC.putLong(ExifInterface.LATITUDE_SOUTH, getMetadataCheckSum(device));
                this.mTaoPC.commit();
            }
        }
    }

    private void saveUtdidToLocalStorage(String str) {
        PersistentConfiguration persistentConfiguration;
        if (str == null || (persistentConfiguration = this.mPC) == null || str.equals(persistentConfiguration.getString(this.mCBKey))) {
            return;
        }
        this.mPC.putString(this.mCBKey, str);
        this.mPC.commit();
    }

    private void saveUtdidToNewSettings(String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && isValidUTDID(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (24 != str.length() || isValidUTDID(Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW))) {
                return;
            }
            Settings.System.putString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW, str);
        }
    }

    private void syncUTDIDToSettings(String str) {
        if (str.equals(Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE))) {
            return;
        }
        Settings.System.putString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE, str);
    }

    private void saveUtdidToSettings(String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") != 0 || str == null) {
            return;
        }
        syncUTDIDToSettings(str);
    }

    private String getUtdidFromTaoPPC() {
        PersistentConfiguration persistentConfiguration = this.mTaoPC;
        if (persistentConfiguration != null) {
            String string = persistentConfiguration.getString("UTDID");
            if (StringUtils.isEmpty(string) || this.mUtdidHelper.packUtdidStr(string) == null) {
                return null;
            }
            return string;
        }
        return null;
    }

    private boolean isValidUTDID(String str) {
        if (str != null) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (24 == str.length() && !this.mPattern.matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    public synchronized String getValue() {
        String str = this.mUtdid;
        if (str != null) {
            return str;
        }
        String string = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW);
        if (isValidUTDID(string)) {
            return string;
        }
        UTUtdidHelper2 uTUtdidHelper2 = new UTUtdidHelper2();
        boolean z = false;
        String string2 = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
        if (StringUtils.isEmpty(string2)) {
            z = true;
        } else {
            String dePackWithBase64 = uTUtdidHelper2.dePackWithBase64(string2);
            if (isValidUTDID(dePackWithBase64)) {
                saveUtdidToNewSettings(dePackWithBase64);
                return dePackWithBase64;
            }
            String dePack = uTUtdidHelper2.dePack(string2);
            if (isValidUTDID(dePack)) {
                String packUtdidStr = this.mUtdidHelper.packUtdidStr(dePack);
                if (!StringUtils.isEmpty(packUtdidStr)) {
                    saveUtdidToSettings(packUtdidStr);
                    string2 = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
                }
            }
            String dePack2 = this.mUtdidHelper.dePack(string2);
            if (isValidUTDID(dePack2)) {
                this.mUtdid = dePack2;
                saveUtdidToTaoPPC(dePack2);
                saveUtdidToLocalStorage(string2);
                saveUtdidToNewSettings(this.mUtdid);
                return this.mUtdid;
            }
        }
        String utdidFromTaoPPC = getUtdidFromTaoPPC();
        if (isValidUTDID(utdidFromTaoPPC)) {
            String packUtdidStr2 = this.mUtdidHelper.packUtdidStr(utdidFromTaoPPC);
            if (z) {
                saveUtdidToSettings(packUtdidStr2);
            }
            saveUtdidToNewSettings(utdidFromTaoPPC);
            saveUtdidToLocalStorage(packUtdidStr2);
            this.mUtdid = utdidFromTaoPPC;
            return utdidFromTaoPPC;
        }
        String string3 = this.mPC.getString(this.mCBKey);
        if (!StringUtils.isEmpty(string3)) {
            String dePack3 = uTUtdidHelper2.dePack(string3);
            if (!isValidUTDID(dePack3)) {
                dePack3 = this.mUtdidHelper.dePack(string3);
            }
            if (isValidUTDID(dePack3)) {
                String packUtdidStr3 = this.mUtdidHelper.packUtdidStr(dePack3);
                if (!StringUtils.isEmpty(dePack3)) {
                    this.mUtdid = dePack3;
                    if (z) {
                        saveUtdidToSettings(packUtdidStr3);
                    }
                    saveUtdidToTaoPPC(this.mUtdid);
                    return this.mUtdid;
                }
            }
        }
        try {
            byte[] _generateUtdid = _generateUtdid();
            if (_generateUtdid != null) {
                String encodeToString = Base64.encodeToString(_generateUtdid, 2);
                this.mUtdid = encodeToString;
                saveUtdidToTaoPPC(encodeToString);
                String pack = this.mUtdidHelper.pack(_generateUtdid);
                if (pack != null) {
                    if (z) {
                        saveUtdidToSettings(pack);
                    }
                    saveUtdidToLocalStorage(pack);
                }
                return this.mUtdid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private final byte[] _generateUtdid() throws Exception {
        String str;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        int nextInt = new Random().nextInt();
        byte[] bytes = IntUtils.getBytes(currentTimeMillis);
        byte[] bytes2 = IntUtils.getBytes(nextInt);
        byteArrayOutputStream.write(bytes, 0, 4);
        byteArrayOutputStream.write(bytes2, 0, 4);
        byteArrayOutputStream.write(3);
        byteArrayOutputStream.write(0);
        try {
            str = PhoneInfoUtils.getImei(this.mContext);
        } catch (Exception unused) {
            str = "" + new Random().nextInt();
        }
        byteArrayOutputStream.write(IntUtils.getBytes(StringUtils.hashCode(str)), 0, 4);
        byteArrayOutputStream.write(IntUtils.getBytes(StringUtils.hashCode(_calcHmac(byteArrayOutputStream.toByteArray()))));
        return byteArrayOutputStream.toByteArray();
    }

    private static String _calcHmac(byte[] bArr) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(HMAC_KEY.getBytes(), mac.getAlgorithm()));
        return Base64.encodeToString(mac.doFinal(bArr), 2);
    }
}
