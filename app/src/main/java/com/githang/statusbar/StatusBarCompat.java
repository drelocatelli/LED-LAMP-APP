package com.githang.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/* loaded from: classes.dex */
public class StatusBarCompat {
    static final IStatusBar IMPL;

    static {
        if (Build.VERSION.SDK_INT >= 23) {
            IMPL = new StatusBarMImpl();
        } else if (Build.VERSION.SDK_INT >= 21 && !isEMUI()) {
            IMPL = new StatusBarLollipopImpl();
        } else if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new StatusBarKitkatImpl();
        } else {
            IMPL = new IStatusBar() { // from class: com.githang.statusbar.StatusBarCompat.1
                @Override // com.githang.statusbar.IStatusBar
                public void setStatusBarColor(Window window, int i) {
                }
            };
        }
    }

    private static boolean isEMUI() {
        File file = new File(Environment.getRootDirectory(), "build.prop");
        if (file.exists()) {
            Properties properties = new Properties();
            FileInputStream fileInputStream = null;
            try {
                try {
                    try {
                        FileInputStream fileInputStream2 = new FileInputStream(file);
                        try {
                            properties.load(fileInputStream2);
                            fileInputStream2.close();
                        } catch (Exception e) {
                            e = e;
                            fileInputStream = fileInputStream2;
                            e.printStackTrace();
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            return properties.containsKey("ro.build.hw_emui_api_level");
                        } catch (Throwable th) {
                            th = th;
                            fileInputStream = fileInputStream2;
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Exception e3) {
                    e = e3;
                }
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            return properties.containsKey("ro.build.hw_emui_api_level");
        }
        return false;
    }

    public static void setStatusBarColor(Activity activity, int i) {
        setStatusBarColor(activity, i, toGrey(i) > 225);
    }

    public static int toGrey(int i) {
        int blue = Color.blue(i);
        return (((Color.red(i) * 38) + (Color.green(i) * 75)) + (blue * 15)) >> 7;
    }

    public static void setStatusBarColor(Activity activity, int i, boolean z) {
        setStatusBarColor(activity.getWindow(), i, z);
    }

    public static void setStatusBarColor(Window window, int i, boolean z) {
        if ((window.getAttributes().flags & 1024) > 0 || StatusBarExclude.exclude) {
            return;
        }
        IMPL.setStatusBarColor(window, i);
        LightStatusBarCompat.setLightStatusBar(window, z);
    }

    public static void setFitsSystemWindows(Window window, boolean z) {
        if (Build.VERSION.SDK_INT >= 14) {
            internalSetFitsSystemWindows(window, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void internalSetFitsSystemWindows(Window window, boolean z) {
        View childAt = ((ViewGroup) window.findViewById(16908290)).getChildAt(0);
        if (childAt != null) {
            childAt.setFitsSystemWindows(z);
        }
    }

    public static void resetActionBarContainerTopMargin(Window window) {
        ViewGroup viewGroup = (ViewGroup) window.findViewById(16908290).getParent();
        if (viewGroup.getChildCount() > 1) {
            internalResetActionBarContainer(viewGroup.getChildAt(1));
        }
    }

    public static void resetActionBarContainerTopMargin(Window window, int i) {
        internalResetActionBarContainer(window.findViewById(i));
    }

    private static void internalResetActionBarContainer(View view) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = 0;
                view.setLayoutParams(layoutParams);
            }
        }
    }

    public static void setLightStatusBar(Window window, boolean z) {
        LightStatusBarCompat.setLightStatusBar(window, z);
    }

    public static void setTranslucent(Window window, boolean z) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (z) {
                window.addFlags(67108864);
                internalSetFitsSystemWindows(window, false);
                return;
            }
            window.clearFlags(67108864);
        }
    }
}
