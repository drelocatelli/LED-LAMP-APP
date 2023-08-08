package com.githang.statusbar;

import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/* loaded from: classes.dex */
class LightStatusBarCompat {
    private static final ILightStatusBar IMPL;

    /* loaded from: classes.dex */
    interface ILightStatusBar {
        void setLightStatusBar(Window window, boolean z);
    }

    LightStatusBarCompat() {
    }

    static {
        if (MIUILightStatusBarImpl.isMe()) {
            if (Build.VERSION.SDK_INT >= 23) {
                IMPL = new MLightStatusBarImpl() { // from class: com.githang.statusbar.LightStatusBarCompat.1
                    private final ILightStatusBar DELEGATE = new MIUILightStatusBarImpl();

                    @Override // com.githang.statusbar.LightStatusBarCompat.MLightStatusBarImpl, com.githang.statusbar.LightStatusBarCompat.ILightStatusBar
                    public void setLightStatusBar(Window window, boolean z) {
                        super.setLightStatusBar(window, z);
                        this.DELEGATE.setLightStatusBar(window, z);
                    }
                };
            } else {
                IMPL = new MIUILightStatusBarImpl();
            }
        } else if (Build.VERSION.SDK_INT >= 23) {
            IMPL = new MLightStatusBarImpl();
        } else if (MeizuLightStatusBarImpl.isMe()) {
            IMPL = new MeizuLightStatusBarImpl();
        } else {
            IMPL = new ILightStatusBar() { // from class: com.githang.statusbar.LightStatusBarCompat.2
                @Override // com.githang.statusbar.LightStatusBarCompat.ILightStatusBar
                public void setLightStatusBar(Window window, boolean z) {
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setLightStatusBar(Window window, boolean z) {
        IMPL.setLightStatusBar(window, z);
    }

    /* loaded from: classes.dex */
    private static class MLightStatusBarImpl implements ILightStatusBar {
        private MLightStatusBarImpl() {
        }

        @Override // com.githang.statusbar.LightStatusBarCompat.ILightStatusBar
        public void setLightStatusBar(Window window, boolean z) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            decorView.setSystemUiVisibility(z ? systemUiVisibility | 8192 : systemUiVisibility & (-8193));
        }
    }

    /* loaded from: classes.dex */
    private static class MIUILightStatusBarImpl implements ILightStatusBar {
        private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
        private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";

        private MIUILightStatusBarImpl() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0030, code lost:
            if (r1.getProperty(com.githang.statusbar.LightStatusBarCompat.MIUILightStatusBarImpl.KEY_MIUI_INTERNAL_STORAGE) != null) goto L18;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        static boolean isMe() {
            FileInputStream fileInputStream;
            boolean z = false;
            FileInputStream fileInputStream2 = null;
            try {
                fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            } catch (IOException unused) {
            } catch (Throwable th) {
                th = th;
            }
            try {
                Properties properties = new Properties();
                properties.load(fileInputStream);
                if (properties.getProperty(KEY_MIUI_VERSION_CODE) == null && properties.getProperty(KEY_MIUI_VERSION_NAME) == null) {
                }
                z = true;
                try {
                    fileInputStream.close();
                } catch (IOException unused2) {
                }
                return z;
            } catch (IOException unused3) {
                fileInputStream2 = fileInputStream;
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException unused4) {
                    }
                }
                return false;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream2 = fileInputStream;
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException unused5) {
                    }
                }
                throw th;
            }
        }

        @Override // com.githang.statusbar.LightStatusBarCompat.ILightStatusBar
        public void setLightStatusBar(Window window, boolean z) {
            Class<?> cls = window.getClass();
            try {
                Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
                Method method = cls.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(z ? i : 0);
                objArr[1] = Integer.valueOf(i);
                method.invoke(window, objArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* loaded from: classes.dex */
    private static class MeizuLightStatusBarImpl implements ILightStatusBar {
        private MeizuLightStatusBarImpl() {
        }

        static boolean isMe() {
            return Build.DISPLAY.startsWith("Flyme");
        }

        @Override // com.githang.statusbar.LightStatusBarCompat.ILightStatusBar
        public void setLightStatusBar(Window window, boolean z) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            try {
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (i ^ (-1)) & i2);
                window.setAttributes(attributes);
                declaredField.setAccessible(false);
                declaredField2.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
