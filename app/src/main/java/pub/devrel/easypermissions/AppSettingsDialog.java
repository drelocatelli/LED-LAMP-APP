package pub.devrel.easypermissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;

/* loaded from: classes.dex */
public class AppSettingsDialog {
    public static final int DEFAULT_SETTINGS_REQ_CODE = 16061;
    private AlertDialog mAlertDialog;

    private AppSettingsDialog(final Object obj, final Context context, String str, String str2, String str3, String str4, DialogInterface.OnClickListener onClickListener, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(str);
        builder.setTitle(str2);
        String string = TextUtils.isEmpty(str3) ? context.getString(17039370) : str3;
        str4 = TextUtils.isEmpty(str3) ? context.getString(17039360) : str4;
        i = i <= 0 ? DEFAULT_SETTINGS_REQ_CODE : i;
        builder.setPositiveButton(string, new DialogInterface.OnClickListener() { // from class: pub.devrel.easypermissions.AppSettingsDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                AppSettingsDialog.this.startForResult(obj, intent, i);
            }
        });
        builder.setNegativeButton(str4, onClickListener);
        this.mAlertDialog = builder.create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startForResult(Object obj, Intent intent, int i) {
        if (obj instanceof Activity) {
            ((Activity) obj).startActivityForResult(intent, i);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(intent, i);
        } else if (obj instanceof android.app.Fragment) {
            ((android.app.Fragment) obj).startActivityForResult(intent, i);
        }
    }

    public void show() {
        this.mAlertDialog.show();
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private Object mActivityOrFragment;
        private Context mContext;
        private String mNegativeButton;
        private DialogInterface.OnClickListener mNegativeListener;
        private String mPositiveButton;
        private String mRationale;
        private int mRequestCode = -1;
        private String mTitle;

        public Builder(Activity activity, String str) {
            this.mActivityOrFragment = activity;
            this.mContext = activity;
            this.mRationale = str;
        }

        public Builder(Fragment fragment, String str) {
            this.mActivityOrFragment = fragment;
            this.mContext = fragment.getContext();
            this.mRationale = str;
        }

        public Builder(android.app.Fragment fragment, String str) {
            this.mActivityOrFragment = fragment;
            this.mContext = fragment.getActivity();
            this.mRationale = str;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder setPositiveButton(String str) {
            this.mPositiveButton = str;
            return this;
        }

        public Builder setNegativeButton(String str, DialogInterface.OnClickListener onClickListener) {
            this.mNegativeButton = str;
            this.mNegativeListener = onClickListener;
            return this;
        }

        public Builder setRequestCode(int i) {
            this.mRequestCode = i;
            return this;
        }

        public AppSettingsDialog build() {
            return new AppSettingsDialog(this.mActivityOrFragment, this.mContext, this.mRationale, this.mTitle, this.mPositiveButton, this.mNegativeButton, this.mNegativeListener, this.mRequestCode);
        }
    }
}
