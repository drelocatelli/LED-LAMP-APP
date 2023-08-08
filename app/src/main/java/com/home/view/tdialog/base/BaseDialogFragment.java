package com.home.view.tdialog.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/* loaded from: classes.dex */
public abstract class BaseDialogFragment extends DialogFragment {
    private static final float DEFAULT_DIMAMOUNT = 0.2f;
    public static final String TAG = "TDialog";

    protected abstract void bindView(View view);

    protected int getDialogAnimationRes() {
        return 0;
    }

    public int getDialogHeight() {
        return -2;
    }

    protected abstract View getDialogView();

    public int getDialogWidth() {
        return -2;
    }

    public float getDimAmount() {
        return DEFAULT_DIMAMOUNT;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public int getGravity() {
        return 17;
    }

    protected abstract int getLayoutRes();

    protected DialogInterface.OnKeyListener getOnKeyListener() {
        return null;
    }

    protected boolean isCancelableOutside() {
        return true;
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        return super.onCreateDialog(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = getLayoutRes() > 0 ? layoutInflater.inflate(getLayoutRes(), viewGroup, false) : null;
        if (getDialogView() != null) {
            inflate = getDialogView();
        }
        bindView(inflate);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(1);
        dialog.setCanceledOnTouchOutside(isCancelableOutside());
        if (dialog.getWindow() != null && getDialogAnimationRes() > 0) {
            dialog.getWindow().setWindowAnimations(getDialogAnimationRes());
        }
        if (getOnKeyListener() != null) {
            dialog.setOnKeyListener(getOnKeyListener());
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (getDialogWidth() > 0) {
                attributes.width = getDialogWidth();
            } else {
                attributes.width = -2;
            }
            if (getDialogHeight() > 0) {
                attributes.height = getDialogHeight();
            } else {
                attributes.height = -2;
            }
            attributes.dimAmount = getDimAmount();
            attributes.gravity = getGravity();
            window.setAttributes(attributes);
        }
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getFragmentTag());
    }

    public static final int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static final int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
