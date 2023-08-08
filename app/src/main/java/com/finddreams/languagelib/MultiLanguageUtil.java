package com.finddreams.languagelib;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes.dex */
public class MultiLanguageUtil {
    public static final String SAVE_LANGUAGE = "save_language";
    private static final String TAG = "MultiLanguageUtil";
    private static MultiLanguageUtil instance;
    private Context mContext;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (MultiLanguageUtil.class) {
                if (instance == null) {
                    instance = new MultiLanguageUtil(context);
                }
            }
        }
    }

    public static MultiLanguageUtil getInstance() {
        MultiLanguageUtil multiLanguageUtil = instance;
        if (multiLanguageUtil != null) {
            return multiLanguageUtil;
        }
        throw new IllegalStateException("You must be init MultiLanguageUtil first");
    }

    private MultiLanguageUtil(Context context) {
        this.mContext = context;
    }

    public void setConfiguration() {
        Locale languageLocale = getLanguageLocale();
        Configuration configuration = this.mContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(languageLocale);
        } else {
            configuration.locale = languageLocale;
        }
        Resources resources = this.mContext.getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private Locale getLanguageLocale() {
        int i = CommSharedUtil.getInstance(this.mContext).getInt(SAVE_LANGUAGE, 0);
        if (i == 0) {
            return getSysLocale();
        }
        if (i == 1) {
            return Locale.ENGLISH;
        }
        if (i == 2) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        if (i == 3) {
            return Locale.TRADITIONAL_CHINESE;
        }
        getSystemLanguage(getSysLocale());
        Log.e(TAG, "getLanguageLocale" + i + i);
        return Locale.SIMPLIFIED_CHINESE;
    }

    private String getSystemLanguage(Locale locale) {
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    public Locale getSysLocale() {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleList.getDefault().get(0);
        }
        return Locale.getDefault();
    }

    public void updateLanguage(int i) {
        CommSharedUtil.getInstance(this.mContext).putInt(SAVE_LANGUAGE, i);
        getInstance().setConfiguration();
        EventBus.getDefault().post(new OnChangeLanguageEvent(i));
    }

    public String getLanguageName(Context context) {
        int i = CommSharedUtil.getInstance(context).getInt(SAVE_LANGUAGE, 0);
        if (i == 1) {
            return this.mContext.getString(R.string.setting_language_english);
        }
        if (i == 2) {
            return this.mContext.getString(R.string.setting_simplified_chinese);
        }
        if (i == 3) {
            return this.mContext.getString(R.string.setting_traditional_chinese);
        }
        return this.mContext.getString(R.string.setting_language_auto);
    }

    public int getLanguageType() {
        int i = CommSharedUtil.getInstance(this.mContext).getInt(SAVE_LANGUAGE, 0);
        if (i == 2) {
            return 2;
        }
        if (i == 3) {
            return 3;
        }
        if (i == 0) {
            return 0;
        }
        Log.e(TAG, "getLanguageType" + i);
        return i;
    }

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return createConfigurationResources(context);
        }
        getInstance().setConfiguration();
        return context;
    }

    private static Context createConfigurationResources(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(getInstance().getLanguageLocale());
        return context.createConfigurationContext(configuration);
    }
}
