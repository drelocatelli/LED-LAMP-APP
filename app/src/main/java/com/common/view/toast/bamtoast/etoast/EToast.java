package com.common.view.toast.bamtoast.etoast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.common.view.toast.bamtoast.btoast.BToast;
import com.weigan.loopview.MessageHandler;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
public class EToast {
    public static final int ICONTYPE_ERROR = 2;
    public static final int ICONTYPE_NONE = 0;
    public static final int ICONTYPE_SUCCEED = 1;
    private static View contentView;
    private static Handler handler;
    private static Toast oldToast;
    private static Timer timer;
    private int mTime;
    private WindowManager manger;
    private WindowManager.LayoutParams params;
    private BToast toast;

    public EToast(Context context, CharSequence charSequence, int i, int i2) {
        int i3 = MessageHandler.WHAT_SMOOTH_SCROLL;
        this.mTime = MessageHandler.WHAT_SMOOTH_SCROLL;
        this.manger = (WindowManager) context.getSystemService("window");
        BToast toast = BToast.getToast(context, charSequence, i, i2);
        this.toast = toast;
        this.mTime = i != 0 ? 3500 : i3;
        contentView = toast.getView();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.params = layoutParams;
        layoutParams.height = -2;
        this.params.width = -2;
        this.params.format = -3;
        this.params.windowAnimations = -1;
        this.params.setTitle("EToast");
        this.params.flags = 152;
        this.params.gravity = 17;
        this.params.y = 200;
        if (handler == null) {
            handler = new Handler() { // from class: com.common.view.toast.bamtoast.etoast.EToast.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    EToast.this.cancel();
                }
            };
        }
    }

    public static EToast makeText(Context context, int i) {
        return new EToast(context, context.getString(i), 0, 0);
    }

    public static EToast makeText(Context context, CharSequence charSequence) {
        return new EToast(context, charSequence, 0, 0);
    }

    public static EToast makeText(Context context, int i, boolean z) {
        return new EToast(context, context.getString(i), 0, z ? 1 : 2);
    }

    public static EToast makeText(Context context, CharSequence charSequence, boolean z) {
        return new EToast(context, charSequence, 0, z ? 1 : 2);
    }

    public static EToast makeText(Context context, int i, int i2) {
        return new EToast(context, context.getString(i), i2, 0);
    }

    public static EToast makeText(Context context, CharSequence charSequence, int i) {
        return new EToast(context, charSequence, i, 0);
    }

    public static EToast makeText(Context context, int i, int i2, boolean z) {
        return new EToast(context, context.getString(i), i2, z ? 1 : 2);
    }

    public static EToast makeText(Context context, CharSequence charSequence, int i, boolean z) {
        return new EToast(context, charSequence, i, z ? 1 : 2);
    }

    public void show() {
        oldToast = this.toast;
        this.manger.addView(contentView, this.params);
        if (handler == null) {
            handler = new Handler() { // from class: com.common.view.toast.bamtoast.etoast.EToast.2
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    EToast.this.cancel();
                }
            };
        }
        Timer timer2 = new Timer();
        timer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.common.view.toast.bamtoast.etoast.EToast.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (EToast.handler != null) {
                    EToast.handler.sendEmptyMessage(1);
                }
            }
        }, this.mTime);
    }

    public void cancel() {
        try {
            WindowManager windowManager = this.manger;
            if (windowManager != null) {
                windowManager.removeView(contentView);
            }
        } catch (IllegalArgumentException unused) {
        }
        Timer timer2 = timer;
        if (timer2 != null) {
            timer2.cancel();
        }
        Toast toast = oldToast;
        if (toast != null) {
            toast.cancel();
        }
        timer = null;
        this.toast = null;
        oldToast = null;
        contentView = null;
        handler = null;
    }

    public void setText(CharSequence charSequence) {
        this.toast.setText(charSequence);
    }
}
