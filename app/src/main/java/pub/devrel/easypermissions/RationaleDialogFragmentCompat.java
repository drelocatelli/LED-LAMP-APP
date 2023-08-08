package pub.devrel.easypermissions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import pub.devrel.easypermissions.EasyPermissions;

/* loaded from: classes.dex */
public class RationaleDialogFragmentCompat extends AppCompatDialogFragment {
    private RationaleDialogClickListener clickListener;
    private RationaleDialogConfig config;
    private EasyPermissions.PermissionCallbacks permissionCallbacks;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RationaleDialogFragmentCompat newInstance(int i, int i2, String str, int i3, String[] strArr) {
        RationaleDialogFragmentCompat rationaleDialogFragmentCompat = new RationaleDialogFragmentCompat();
        rationaleDialogFragmentCompat.setArguments(new RationaleDialogConfig(i, i2, str, i3, strArr).toBundle());
        return rationaleDialogFragmentCompat;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && (getParentFragment() instanceof EasyPermissions.PermissionCallbacks)) {
            this.permissionCallbacks = (EasyPermissions.PermissionCallbacks) getParentFragment();
        } else if (context instanceof EasyPermissions.PermissionCallbacks) {
            this.permissionCallbacks = (EasyPermissions.PermissionCallbacks) context;
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.permissionCallbacks = null;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        setCancelable(false);
        RationaleDialogConfig rationaleDialogConfig = new RationaleDialogConfig(getArguments());
        this.config = rationaleDialogConfig;
        this.clickListener = new RationaleDialogClickListener(this, rationaleDialogConfig, this.permissionCallbacks);
        return this.config.createDialog(getContext(), this.clickListener);
    }
}
