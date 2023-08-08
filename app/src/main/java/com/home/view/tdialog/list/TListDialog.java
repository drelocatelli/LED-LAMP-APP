package com.home.view.tdialog.list;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.home.view.tdialog.TDialog;
import com.home.view.tdialog.base.BaseDialogFragment;
import com.home.view.tdialog.base.TBaseAdapter;
import com.home.view.tdialog.base.TController;
import com.home.view.tdialog.listener.OnBindViewListener;
import com.home.view.tdialog.listener.OnViewClickListener;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class TListDialog extends TDialog {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.view.tdialog.TDialog, com.home.view.tdialog.base.BaseDialogFragment
    public void bindView(View view) {
        super.bindView(view);
        if (this.tController.getAdapter() != null) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            if (recyclerView == null) {
                throw new IllegalArgumentException("自定义列表xml布局,请设置RecyclerView的控件id为recycler_view");
            }
            this.tController.getAdapter().setTDialog(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), this.tController.getOrientation(), false));
            recyclerView.setAdapter(this.tController.getAdapter());
            this.tController.getAdapter().notifyDataSetChanged();
            if (this.tController.getAdapterItemClickListener() != null) {
                this.tController.getAdapter().setOnAdapterItemClickListener(this.tController.getAdapterItemClickListener());
                return;
            }
            return;
        }
        Log.d(BaseDialogFragment.TAG, "列表弹窗需要先调用setAdapter()方法!");
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

        public Builder setListLayoutRes(int i, int i2) {
            this.params.listLayoutRes = i;
            this.params.orientation = i2;
            return this;
        }

        public Builder setScreenWidthAspect(Activity activity, float f) {
            this.params.mWidth = (int) (BaseDialogFragment.getScreenWidth(activity) * f);
            return this;
        }

        public Builder setWidth(int i) {
            this.params.mWidth = i;
            return this;
        }

        public Builder setScreenHeightAspect(Activity activity, float f) {
            this.params.mHeight = (int) (BaseDialogFragment.getScreenHeight(activity) * f);
            return this;
        }

        public Builder setHeight(int i) {
            this.params.mHeight = i;
            return this;
        }

        public Builder setGravity(int i) {
            this.params.mGravity = i;
            return this;
        }

        public Builder setCancelOutside(boolean z) {
            this.params.mIsCancelableOutside = z;
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

        public <A extends TBaseAdapter> Builder setAdapter(A a) {
            this.params.adapter = a;
            return this;
        }

        public Builder setOnAdapterItemClickListener(TBaseAdapter.OnAdapterItemClickListener onAdapterItemClickListener) {
            this.params.adapterItemClickListener = onAdapterItemClickListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.params.mOnDismissListener = onDismissListener;
            return this;
        }

        public TListDialog create() {
            TListDialog tListDialog = new TListDialog();
            this.params.apply(tListDialog.tController);
            return tListDialog;
        }
    }
}
