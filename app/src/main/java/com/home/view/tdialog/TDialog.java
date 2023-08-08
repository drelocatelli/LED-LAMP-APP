package com.home.view.tdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.home.view.tdialog.base.BaseDialogFragment;
import com.home.view.tdialog.base.BindViewHolder;
import com.home.view.tdialog.base.TController;
import com.home.view.tdialog.listener.OnBindViewListener;
import com.home.view.tdialog.listener.OnViewClickListener;

/* loaded from: classes.dex */
public class TDialog extends BaseDialogFragment {
    private static final String KEY_TCONTROLLER = "TController";
    protected TController tController = new TController();

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.tController = (TController) bundle.getSerializable(KEY_TCONTROLLER);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(KEY_TCONTROLLER, this.tController);
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DialogInterface.OnDismissListener onDismissListener = this.tController.getOnDismissListener();
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    protected int getLayoutRes() {
        return this.tController.getLayoutRes();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    protected View getDialogView() {
        return this.tController.getDialogView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.view.tdialog.base.BaseDialogFragment
    public void bindView(View view) {
        BindViewHolder bindViewHolder = new BindViewHolder(view, this);
        if (this.tController.getIds() != null && this.tController.getIds().length > 0) {
            for (int i : this.tController.getIds()) {
                bindViewHolder.addOnClickListener(i);
            }
        }
        if (this.tController.getOnBindViewListener() != null) {
            this.tController.getOnBindViewListener().bindView(bindViewHolder);
        }
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    public int getGravity() {
        return this.tController.getGravity();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    public float getDimAmount() {
        return this.tController.getDimAmount();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    public int getDialogHeight() {
        return this.tController.getHeight();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    public int getDialogWidth() {
        return this.tController.getWidth();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    public String getFragmentTag() {
        return this.tController.getTag();
    }

    public OnViewClickListener getOnViewClickListener() {
        return this.tController.getOnViewClickListener();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    protected boolean isCancelableOutside() {
        return this.tController.isCancelableOutside();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    protected int getDialogAnimationRes() {
        return this.tController.getDialogAnimationRes();
    }

    @Override // com.home.view.tdialog.base.BaseDialogFragment
    protected DialogInterface.OnKeyListener getOnKeyListener() {
        return this.tController.getOnKeyListener();
    }

    public TDialog show() {
        Log.d(BaseDialogFragment.TAG, "show");
        try {
            FragmentTransaction beginTransaction = this.tController.getFragmentManager().beginTransaction();
            beginTransaction.add(this, this.tController.getTag());
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e(BaseDialogFragment.TAG, e.toString());
        }
        return this;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        TController.TParams params;

        public Builder(FragmentManager fragmentManager) {
            TController.TParams tParams = new TController.TParams();
            this.params = tParams;
            tParams.mFragmentManager = fragmentManager;
        }

        public Builder setLayoutRes(int i) {
            this.params.mLayoutRes = i;
            return this;
        }

        public Builder setDialogView(View view) {
            this.params.mDialogView = view;
            return this;
        }

        public Builder setWidth(int i) {
            this.params.mWidth = i;
            return this;
        }

        public Builder setHeight(int i) {
            this.params.mHeight = i;
            return this;
        }

        public Builder setScreenWidthAspect(Context context, float f) {
            this.params.mWidth = (int) (BaseDialogFragment.getScreenWidth(context) * f);
            return this;
        }

        public Builder setScreenHeightAspect(Context context, float f) {
            this.params.mHeight = (int) (BaseDialogFragment.getScreenHeight(context) * f);
            return this;
        }

        public Builder setGravity(int i) {
            this.params.mGravity = i;
            return this;
        }

        public Builder setCancelableOutside(boolean z) {
            this.params.mIsCancelableOutside = z;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.params.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setDimAmount(float f) {
            this.params.mDimAmount = f;
            return this;
        }

        public Builder setTag(String str) {
            this.params.mTag = str;
            return this;
        }

        public Builder setOnBindViewListener(OnBindViewListener onBindViewListener) {
            this.params.bindViewListener = onBindViewListener;
            return this;
        }

        public Builder addOnClickListener(int... iArr) {
            this.params.ids = iArr;
            return this;
        }

        public Builder setOnViewClickListener(OnViewClickListener onViewClickListener) {
            this.params.mOnViewClickListener = onViewClickListener;
            return this;
        }

        public Builder setDialogAnimationRes(int i) {
            this.params.mDialogAnimationRes = i;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            this.params.mKeyListener = onKeyListener;
            return this;
        }

        public TDialog create() {
            TDialog tDialog = new TDialog();
            Log.d(BaseDialogFragment.TAG, "create");
            this.params.apply(tDialog.tController);
            return tDialog;
        }
    }
}
