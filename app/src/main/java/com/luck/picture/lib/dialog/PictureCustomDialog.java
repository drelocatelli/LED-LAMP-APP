package com.luck.picture.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/* loaded from: classes.dex */
public class PictureCustomDialog extends Dialog {
    public PictureCustomDialog(Context context, int i, int i2, int i3, int i4) {
        super(context, i4);
        setContentView(i3);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = i;
        attributes.height = i2;
        attributes.gravity = 17;
        window.setAttributes(attributes);
    }
}
