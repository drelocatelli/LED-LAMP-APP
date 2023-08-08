package com.home.utils;

import android.content.Context;
import android.view.View;
import com.home.widget.effects.Effectstype;
import com.home.widget.effects.NiftyDialogBuilder;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class WarmingDialog {
    private Effectstype effect;
    private Context mContext;
    private OnDialogButtonClickListener mDialogButtonClickListener;

    /* loaded from: classes.dex */
    public interface OnDialogButtonClickListener {
        void onCancelClick();

        void onConfirmClick();
    }

    public WarmingDialog(Context context) {
        this.mContext = context;
    }

    public WarmingDialog warmingDialog(String str, String str2, String str3) {
        if (str2 == null) {
            str2 = getStringById(R.string.text_ok);
        }
        if (str3 == null) {
            str3 = getStringById(R.string.text_cancel);
        }
        final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(this.mContext, R.style.dialog_untran);
        this.effect = Effectstype.Shake;
        niftyDialogBuilder.withTitle(getStringById(R.string.text_warming)).withTitleColor("#FFFFFF").withDividerColor("#11000000").withMessage(str).withMessageColor("#FFFFFF").withIcon(this.mContext.getResources().getDrawable(R.drawable.icon)).isCancelableOnTouchOutside(false).withDuration(200).withEffect(this.effect).withButton1Text(str2).withButton2Text(str3).setButton1Click(new View.OnClickListener() { // from class: com.home.utils.WarmingDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NiftyDialogBuilder niftyDialogBuilder2 = niftyDialogBuilder;
                if (niftyDialogBuilder2 != null) {
                    niftyDialogBuilder2.dismiss();
                }
                WarmingDialog.this.mDialogButtonClickListener.onConfirmClick();
            }
        }).setButton2Click(new View.OnClickListener() { // from class: com.home.utils.WarmingDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NiftyDialogBuilder niftyDialogBuilder2 = niftyDialogBuilder;
                if (niftyDialogBuilder2 != null) {
                    niftyDialogBuilder2.dismiss();
                }
                WarmingDialog.this.mDialogButtonClickListener.onCancelClick();
            }
        }).show();
        return this;
    }

    public View fillDialog(int i, String str) {
        final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(this.mContext, R.style.dialog_untran);
        this.effect = Effectstype.Slidetop;
        niftyDialogBuilder.withTitle(str).withTitleColor("#FFFFFF").withDividerColor("#11000000").withMessage((CharSequence) null).withMessageColor("#FFFFFF").withIcon(this.mContext.getResources().getDrawable(R.drawable.icon)).isCancelableOnTouchOutside(false).withDuration(200).withEffect(this.effect).withButton1Text(getStringById(R.string.text_ok)).withButton2Text(getStringById(R.string.text_cancel)).setCustomView(i, this.mContext).setButton1Click(new View.OnClickListener() { // from class: com.home.utils.WarmingDialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NiftyDialogBuilder niftyDialogBuilder2 = niftyDialogBuilder;
                if (niftyDialogBuilder2 != null) {
                    niftyDialogBuilder2.dismiss();
                }
                WarmingDialog.this.mDialogButtonClickListener.onConfirmClick();
            }
        }).setButton2Click(new View.OnClickListener() { // from class: com.home.utils.WarmingDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NiftyDialogBuilder niftyDialogBuilder2 = niftyDialogBuilder;
                if (niftyDialogBuilder2 != null) {
                    niftyDialogBuilder2.dismiss();
                }
                WarmingDialog.this.mDialogButtonClickListener.onCancelClick();
            }
        }).show();
        return niftyDialogBuilder.getContentView();
    }

    private String getStringById(int i) {
        return this.mContext.getResources().getString(i);
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        this.mDialogButtonClickListener = onDialogButtonClickListener;
    }
}
