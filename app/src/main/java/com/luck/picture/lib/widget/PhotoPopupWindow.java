package com.luck.picture.lib.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.luck.picture.lib.R;

/* loaded from: classes.dex */
public class PhotoPopupWindow extends PopupWindow implements View.OnClickListener {
    private Animation animationIn;
    private Animation animationOut;
    private FrameLayout fl_content;
    private boolean isDismiss;
    private LinearLayout ll_root;
    private OnItemClickListener onItemClickListener;
    private TextView picture_tv_cancel;
    private TextView picture_tv_photo;
    private TextView picture_tv_video;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public PhotoPopupWindow(Context context) {
        super(context);
        this.isDismiss = false;
        View inflate = LayoutInflater.from(context).inflate(R.layout.picture_camera_pop_layout, (ViewGroup) null);
        setWidth(-1);
        setHeight(-1);
        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        update();
        setBackgroundDrawable(new ColorDrawable());
        setContentView(inflate);
        this.animationIn = AnimationUtils.loadAnimation(context, R.anim.picture_anim_up_in);
        this.animationOut = AnimationUtils.loadAnimation(context, R.anim.picture_anim_down_out);
        this.ll_root = (LinearLayout) inflate.findViewById(R.id.ll_root);
        this.fl_content = (FrameLayout) inflate.findViewById(R.id.fl_content);
        this.picture_tv_photo = (TextView) inflate.findViewById(R.id.picture_tv_photo);
        this.picture_tv_cancel = (TextView) inflate.findViewById(R.id.picture_tv_cancel);
        TextView textView = (TextView) inflate.findViewById(R.id.picture_tv_video);
        this.picture_tv_video = textView;
        textView.setOnClickListener(this);
        this.picture_tv_cancel.setOnClickListener(this);
        this.picture_tv_photo.setOnClickListener(this);
        this.fl_content.setOnClickListener(this);
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view) {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                int[] iArr = new int[2];
                view.getLocationOnScreen(iArr);
                showAtLocation(view, 80, iArr[0], iArr[1] + view.getHeight());
            } else {
                showAtLocation(view, 80, 0, 0);
            }
            this.isDismiss = false;
            this.ll_root.startAnimation(this.animationIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        if (this.isDismiss) {
            return;
        }
        this.isDismiss = true;
        this.ll_root.startAnimation(this.animationOut);
        dismiss();
        this.animationOut.setAnimationListener(new Animation.AnimationListener() { // from class: com.luck.picture.lib.widget.PhotoPopupWindow.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                PhotoPopupWindow.this.isDismiss = false;
                if (Build.VERSION.SDK_INT <= 16) {
                    PhotoPopupWindow.this.dismiss4Pop();
                } else {
                    PhotoPopupWindow.super.dismiss();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismiss4Pop() {
        new Handler().post(new Runnable() { // from class: com.luck.picture.lib.widget.PhotoPopupWindow$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PhotoPopupWindow.this.m56xb18d9bfa();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$dismiss4Pop$0$com-luck-picture-lib-widget-PhotoPopupWindow  reason: not valid java name */
    public /* synthetic */ void m56xb18d9bfa() {
        super.dismiss();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        OnItemClickListener onItemClickListener;
        OnItemClickListener onItemClickListener2;
        int id = view.getId();
        if (id == R.id.picture_tv_photo && (onItemClickListener2 = this.onItemClickListener) != null) {
            onItemClickListener2.onItemClick(0);
            super.dismiss();
        }
        if (id == R.id.picture_tv_video && (onItemClickListener = this.onItemClickListener) != null) {
            onItemClickListener.onItemClick(1);
            super.dismiss();
        }
        dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
