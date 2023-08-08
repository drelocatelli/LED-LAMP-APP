package pub.devrel.easypermissions;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import pub.devrel.easypermissions.EasyPermissions;

/* loaded from: classes.dex */
public class RationaleDialogFragment extends DialogFragment {
    private RationaleDialogClickListener clickListener;
    private RationaleDialogConfig config;
    private EasyPermissions.PermissionCallbacks permissionCallbacks;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RationaleDialogFragment newInstance(int i, int i2, String str, int i3, String[] strArr) {
        RationaleDialogFragment rationaleDialogFragment = new RationaleDialogFragment();
        rationaleDialogFragment.setArguments(new RationaleDialogConfig(i, i2, str, i3, strArr).toBundle());
        return rationaleDialogFragment;
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if ((Build.VERSION.SDK_INT >= 17) && getParentFragment() != null && (getParentFragment() instanceof EasyPermissions.PermissionCallbacks)) {
            this.permissionCallbacks = (EasyPermissions.PermissionCallbacks) getParentFragment();
        } else if (context instanceof EasyPermissions.PermissionCallbacks) {
            this.permissionCallbacks = (EasyPermissions.PermissionCallbacks) context;
        }
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.permissionCallbacks = null;
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        setCancelable(false);
        RationaleDialogConfig rationaleDialogConfig = new RationaleDialogConfig(getArguments());
        this.config = rationaleDialogConfig;
        this.clickListener = new RationaleDialogClickListener(this, rationaleDialogConfig, this.permissionCallbacks);
        return this.config.createDialog(getActivity(), this.clickListener);
    }
}
