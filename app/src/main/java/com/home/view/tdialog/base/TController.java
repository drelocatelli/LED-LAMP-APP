package com.home.view.tdialog.base;

import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import com.home.view.tdialog.base.TBaseAdapter;
import com.home.view.tdialog.listener.OnBindViewListener;
import com.home.view.tdialog.listener.OnViewClickListener;
import com.ledlamp.R;
import java.io.Serializable;

/* loaded from: classes.dex */
public class TController<A extends TBaseAdapter> implements Parcelable, Serializable {
    public static final Parcelable.Creator<TController> CREATOR = new Parcelable.Creator<TController>() { // from class: com.home.view.tdialog.base.TController.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TController createFromParcel(Parcel parcel) {
            return new TController(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TController[] newArray(int i) {
            return new TController[i];
        }
    };
    private A adapter;
    private TBaseAdapter.OnAdapterItemClickListener adapterItemClickListener;
    private int dialogAnimationRes;
    private View dialogView;
    private float dimAmount;
    private FragmentManager fragmentManager;
    private int gravity;
    private int height;
    private int[] ids;
    private boolean isCancelableOutside;
    private int layoutRes;
    private OnBindViewListener onBindViewListener;
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnKeyListener onKeyListener;
    private OnViewClickListener onViewClickListener;
    private int orientation;
    private String tag;
    private int width;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public TController() {
    }

    protected TController(Parcel parcel) {
        this.layoutRes = parcel.readInt();
        this.height = parcel.readInt();
        this.width = parcel.readInt();
        this.dimAmount = parcel.readFloat();
        this.gravity = parcel.readInt();
        this.tag = parcel.readString();
        this.ids = parcel.createIntArray();
        this.isCancelableOutside = parcel.readByte() != 0;
        this.orientation = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.layoutRes);
        parcel.writeInt(this.height);
        parcel.writeInt(this.width);
        parcel.writeFloat(this.dimAmount);
        parcel.writeInt(this.gravity);
        parcel.writeString(this.tag);
        parcel.writeIntArray(this.ids);
        parcel.writeByte(this.isCancelableOutside ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.orientation);
    }

    public FragmentManager getFragmentManager() {
        return this.fragmentManager;
    }

    public int getLayoutRes() {
        return this.layoutRes;
    }

    public void setLayoutRes(int i) {
        this.layoutRes = i;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public float getDimAmount() {
        return this.dimAmount;
    }

    public int getGravity() {
        return this.gravity;
    }

    public String getTag() {
        return this.tag;
    }

    public int[] getIds() {
        return this.ids;
    }

    public boolean isCancelableOutside() {
        return this.isCancelableOutside;
    }

    public OnViewClickListener getOnViewClickListener() {
        return this.onViewClickListener;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public OnBindViewListener getOnBindViewListener() {
        return this.onBindViewListener;
    }

    public DialogInterface.OnDismissListener getOnDismissListener() {
        return this.onDismissListener;
    }

    public DialogInterface.OnKeyListener getOnKeyListener() {
        return this.onKeyListener;
    }

    public View getDialogView() {
        return this.dialogView;
    }

    public A getAdapter() {
        return this.adapter;
    }

    public void setAdapter(A a) {
        this.adapter = a;
    }

    public TBaseAdapter.OnAdapterItemClickListener getAdapterItemClickListener() {
        return this.adapterItemClickListener;
    }

    public void setAdapterItemClickListener(TBaseAdapter.OnAdapterItemClickListener onAdapterItemClickListener) {
        this.adapterItemClickListener = onAdapterItemClickListener;
    }

    public int getDialogAnimationRes() {
        return this.dialogAnimationRes;
    }

    /* loaded from: classes.dex */
    public static class TParams<A extends TBaseAdapter> {
        public A adapter;
        public TBaseAdapter.OnAdapterItemClickListener adapterItemClickListener;
        public OnBindViewListener bindViewListener;
        public int[] ids;
        public int listLayoutRes;
        public View mDialogView;
        public FragmentManager mFragmentManager;
        public int mHeight;
        public DialogInterface.OnKeyListener mKeyListener;
        public int mLayoutRes;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public OnViewClickListener mOnViewClickListener;
        public int mWidth;
        public float mDimAmount = 0.2f;
        public int mGravity = 17;
        public String mTag = BaseDialogFragment.TAG;
        public boolean mIsCancelableOutside = true;
        public int mDialogAnimationRes = 0;
        public int orientation = 1;

        public void apply(TController tController) {
            tController.fragmentManager = this.mFragmentManager;
            int i = this.mLayoutRes;
            if (i > 0) {
                tController.layoutRes = i;
            }
            View view = this.mDialogView;
            if (view != null) {
                tController.dialogView = view;
            }
            int i2 = this.mWidth;
            if (i2 > 0) {
                tController.width = i2;
            }
            int i3 = this.mHeight;
            if (i3 > 0) {
                tController.height = i3;
            }
            tController.dimAmount = this.mDimAmount;
            tController.gravity = this.mGravity;
            tController.tag = this.mTag;
            int[] iArr = this.ids;
            if (iArr != null) {
                tController.ids = iArr;
            }
            tController.isCancelableOutside = this.mIsCancelableOutside;
            tController.onViewClickListener = this.mOnViewClickListener;
            tController.onBindViewListener = this.bindViewListener;
            tController.onDismissListener = this.mOnDismissListener;
            tController.dialogAnimationRes = this.mDialogAnimationRes;
            tController.onKeyListener = this.mKeyListener;
            A a = this.adapter;
            if (a != null) {
                tController.setAdapter(a);
                int i4 = this.listLayoutRes;
                if (i4 <= 0) {
                    tController.setLayoutRes(R.layout.dialog_recycler);
                } else {
                    tController.setLayoutRes(i4);
                }
                tController.orientation = this.orientation;
            } else if (tController.getLayoutRes() <= 0 && tController.getDialogView() == null) {
                throw new IllegalArgumentException("请先调用setLayoutRes()方法设置弹窗所需的xml布局!");
            }
            TBaseAdapter.OnAdapterItemClickListener onAdapterItemClickListener = this.adapterItemClickListener;
            if (onAdapterItemClickListener != null) {
                tController.setAdapterItemClickListener(onAdapterItemClickListener);
            }
            if (tController.width > 0 || tController.height > 0) {
                return;
            }
            tController.width = 600;
        }
    }
}
