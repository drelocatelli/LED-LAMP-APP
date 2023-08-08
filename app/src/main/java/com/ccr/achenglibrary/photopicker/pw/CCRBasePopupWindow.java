package com.ccr.achenglibrary.photopicker.pw;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupWindow;

/* loaded from: classes.dex */
public abstract class CCRBasePopupWindow extends PopupWindow implements View.OnClickListener {
    protected Activity mActivity;
    protected View mAnchorView;
    protected View mWindowRootView;

    protected abstract void initView();

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    protected abstract void processLogic();

    protected abstract void setListener();

    public abstract void show();

    public CCRBasePopupWindow(Activity activity, int i, View view, int i2, int i3) {
        super(View.inflate(activity, i, null), i2, i3, true);
        init(activity, view);
        initView();
        setListener();
        processLogic();
    }

    private void init(Activity activity, View view) {
        getContentView().setOnKeyListener(new View.OnKeyListener() { // from class: com.ccr.achenglibrary.photopicker.pw.CCRBasePopupWindow.1
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view2, int i, KeyEvent keyEvent) {
                if (i == 4) {
                    CCRBasePopupWindow.this.dismiss();
                    return true;
                }
                return false;
            }
        });
        setBackgroundDrawable(new ColorDrawable(0));
        this.mAnchorView = view;
        this.mActivity = activity;
        this.mWindowRootView = activity.getWindow().peekDecorView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <VT extends View> VT getViewById(int i) {
        return (VT) getContentView().findViewById(i);
    }
}
