package com.forum.login;

import com.common.net.NetResult;
import java.io.PrintStream;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/* loaded from: classes.dex */
public class PasswordUtil {
    private static final String DES = "DES";
    private static final String PASSWORD_CRYPT_KEY = "com.xpy_@_123_456";

    public byte[] encrypt(byte[] bArr, byte[] bArr2) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        SecretKey generateSecret = SecretKeyFactory.getInstance(DES).generateSecret(new DESKeySpec(bArr2));
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(1, generateSecret, secureRandom);
        return cipher.doFinal(bArr);
    }

    public byte[] decrypt(byte[] bArr, byte[] bArr2) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        SecretKey generateSecret = SecretKeyFactory.getInstance(DES).generateSecret(new DESKeySpec(bArr2));
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(2, generateSecret, secureRandom);
        return cipher.doFinal(bArr);
    }

    public String byte2hex(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            str = hexString.length() == 1 ? str + NetResult.CODE_OK + hexString : str + hexString;
        }
        return str.toUpperCase();
    }

    public byte[] hex2byte(byte[] bArr) {
        if (bArr.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] bArr2 = new byte[bArr.length / 2];
        for (int i = 0; i < bArr.length; i += 2) {
            bArr2[i / 2] = (byte) Integer.parseInt(new String(bArr, i, 2), 16);
        }
        return bArr2;
    }

    public static String ISO2GBK(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        boolean z = false;
        int i = 0;
        while (true) {
            if (i < str.length()) {
                char charAt = str.charAt(i);
                if (charAt < 255 && charAt > 127) {
                    z = true;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (z) {
            try {
                return new String(str.getBytes("ISO-8859-1"), "GBK");
            } catch (Exception unused) {
                return str;
            }
        }
        return str;
    }

    public String conversion(String str) {
        return str != null ? str.replaceAll("@", "%") : str;
    }

    public final synchronized String deEncrypt(String str) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new String(decrypt(hex2byte(str.getBytes()), PASSWORD_CRYPT_KEY.getBytes()));
    }

    public final synchronized String encrypt(String str) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return byte2hex(encrypt(str.getBytes(), PASSWORD_CRYPT_KEY.getBytes()));
    }

    public static void main(String[] strArr) {
        String encrypt = new PasswordUtil().encrypt("123456");
        PrintStream printStream = System.out;
        printStream.println("原值：123456");
        PrintStream printStream2 = System.out;
        printStream2.println("加密后的密码：" + encrypt);
        PrintStream printStream3 = System.out;
        printStream3.println("加密后的密码长度：" + encrypt.length() + "位");
        String deEncrypt = new PasswordUtil().deEncrypt(encrypt);
        PrintStream printStream4 = System.out;
        printStream4.println("解密后的密码：" + deEncrypt);
    }
}
