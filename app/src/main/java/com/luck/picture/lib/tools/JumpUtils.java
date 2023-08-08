package com.luck.picture.lib.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.luck.picture.lib.PicturePreviewActivity;
import com.luck.picture.lib.PictureVideoPlayActivity;

/* loaded from: classes.dex */
public class JumpUtils {
    public static void startPictureVideoPlayActivity(Context context, Bundle bundle) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, PictureVideoPlayActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startPicturePreviewActivity(Context context, Bundle bundle, int i) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, PicturePreviewActivity.class);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, i);
        }
    }
}
