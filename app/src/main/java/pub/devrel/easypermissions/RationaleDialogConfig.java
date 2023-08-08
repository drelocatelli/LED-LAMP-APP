package pub.devrel.easypermissions;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

/* loaded from: classes.dex */
class RationaleDialogConfig {
    private static final String KEY_NEGATIVE_BUTTON = "negativeButton";
    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_POSITIVE_BUTTON = "positiveButton";
    private static final String KEY_RATIONALE_MESSAGE = "rationaleMsg";
    private static final String KEY_REQUEST_CODE = "requestCode";
    int negativeButton;
    String[] permissions;
    int positiveButton;
    String rationaleMsg;
    int requestCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RationaleDialogConfig(int i, int i2, String str, int i3, String[] strArr) {
        this.positiveButton = i;
        this.negativeButton = i2;
        this.rationaleMsg = str;
        this.requestCode = i3;
        this.permissions = strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RationaleDialogConfig(Bundle bundle) {
        this.positiveButton = bundle.getInt(KEY_POSITIVE_BUTTON);
        this.negativeButton = bundle.getInt(KEY_NEGATIVE_BUTTON);
        this.rationaleMsg = bundle.getString(KEY_RATIONALE_MESSAGE);
        this.requestCode = bundle.getInt(KEY_REQUEST_CODE);
        this.permissions = bundle.getStringArray(KEY_PERMISSIONS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITIVE_BUTTON, this.positiveButton);
        bundle.putInt(KEY_NEGATIVE_BUTTON, this.negativeButton);
        bundle.putString(KEY_RATIONALE_MESSAGE, this.rationaleMsg);
        bundle.putInt(KEY_REQUEST_CODE, this.requestCode);
        bundle.putStringArray(KEY_PERMISSIONS, this.permissions);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlertDialog createDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setCancelable(false).setPositiveButton(this.positiveButton, onClickListener).setNegativeButton(this.negativeButton, onClickListener).setMessage(this.rationaleMsg).create();
    }
}
