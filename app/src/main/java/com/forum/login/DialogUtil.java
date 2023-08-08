package com.forum.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class DialogUtil {
    private static Dialog dialog;

    public static MyProgressDialog showWithProgress(Context context, String str, String str2, boolean z) {
        return showProgress(context, str, str2, z, null);
    }

    public static MyProgressDialog showCancelableProgress(Context context, String str, String str2, DialogInterface.OnCancelListener onCancelListener) {
        return showProgress(context, str, str2, true, onCancelListener);
    }

    public static MyProgressDialog showNoCancelProgress(Context context, String str, String str2) {
        return showProgress(context, str, str2, false, null);
    }

    public static Dialog showProgress(Context context) {
        Dialog dialog2 = new Dialog(context, R.style.dialog_styles);
        dialog2.setContentView(View.inflate(context, R.layout.layout_progress_dialog, null));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.show();
        dialog = dialog2;
        return dialog2;
    }

    public static MyProgressDialog showProgress(Context context, String str, String str2, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        MyProgressDialog myProgressDialog = new MyProgressDialog(context, str, str2);
        myProgressDialog.setCancelable(z);
        myProgressDialog.setCanceledOnTouchOutside(false);
        if (z && onCancelListener != null) {
            myProgressDialog.setOnCancelListener(onCancelListener);
        }
        myProgressDialog.show();
        dialog = myProgressDialog;
        return myProgressDialog;
    }

    public static AlertDialog getCustomDialog(Context context, View view, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder builder = getBuilder(context);
        builder.setTitle(str);
        builder.setView(view);
        builder.setPositiveButton(str2, onClickListener);
        builder.setNegativeButton(str3, onClickListener2);
        return builder.show();
    }

    public static void closeWithProgress() {
        Dialog dialog2 = dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
            dialog = null;
        }
    }

    public static AlertDialog.Builder getBuilder(Context context) {
        return new AlertDialog.Builder(context);
    }
}
