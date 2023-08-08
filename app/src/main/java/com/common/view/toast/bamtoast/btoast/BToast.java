package com.common.view.toast.bamtoast.btoast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class BToast extends Toast {
    public static final int ICONTYPE_ERROR = 2;
    public static final int ICONTYPE_NONE = 0;
    public static final int ICONTYPE_SUCCEED = 1;
    private static LayoutInflater inflater;
    private static View layout;
    private static BToast toast;
    private static ImageView toast_img;
    private static TextView toast_text;

    public BToast(Context context) {
        super(context);
    }

    public static BToast makeText(Context context, int i) {
        return getToast(context, context.getString(i), 0, 0);
    }

    public static BToast makeText(Context context, CharSequence charSequence) {
        return getToast(context, charSequence, 0, 0);
    }

    public static BToast makeText(Context context, int i, boolean z) {
        return getToast(context, context.getString(i), 0, z ? 1 : 2);
    }

    public static BToast makeText(Context context, CharSequence charSequence, boolean z) {
        return getToast(context, charSequence, 0, z ? 1 : 2);
    }

    public static BToast makeText(Context context, int i, int i2) {
        return getToast(context, context.getString(i), i2, 0);
    }

    public static BToast makeText(Context context, CharSequence charSequence, int i) {
        return getToast(context, charSequence, i, 0);
    }

    public static BToast makeText(Context context, int i, int i2, boolean z) {
        return getToast(context, context.getString(i), i2, z ? 1 : 2);
    }

    public static BToast makeText(Context context, CharSequence charSequence, int i, boolean z) {
        return getToast(context, charSequence, i, z ? 1 : 2);
    }

    public static BToast getToast(Context context, CharSequence charSequence, int i, int i2) {
        initToast(context, charSequence);
        if (i2 == 0) {
            toast_img.setVisibility(8);
        }
        if (i == 1) {
            toast.setDuration(1);
        } else {
            toast.setDuration(0);
        }
        return toast;
    }

    private static void initToast(Context context, CharSequence charSequence) {
        try {
            cancelToast();
            toast = new BToast(context);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            inflater = layoutInflater;
            View inflate = layoutInflater.inflate(R.layout.toast_layout, (ViewGroup) null);
            layout = inflate;
            toast_img = (ImageView) inflate.findViewById(R.id.toast_img);
            TextView textView = (TextView) layout.findViewById(R.id.toast_text);
            toast_text = textView;
            textView.setText(charSequence);
            toast.setView(layout);
            toast.setGravity(17, 0, 70);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelToast() {
        BToast bToast = toast;
        if (bToast != null) {
            bToast.cancel();
        }
    }

    @Override // android.widget.Toast
    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.widget.Toast
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
