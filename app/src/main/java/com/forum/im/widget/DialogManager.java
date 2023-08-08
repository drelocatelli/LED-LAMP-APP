package com.forum.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.forum.im.utils.ScreenUtil;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class DialogManager {
    private Context mContext;
    private Dialog mDialog;
    private ImageView mIcon;
    private TextView mLable;
    private ImageView mVoice;

    public DialogManager(Context context) {
        this.mContext = context;
    }

    public void showRecordingDialog() {
        Dialog dialog = new Dialog(this.mContext, R.style.Theme_audioDialog);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        this.mDialog.setContentView(LayoutInflater.from(this.mContext).inflate(R.layout.layout_voice_dialog_manager, (ViewGroup) null));
        this.mIcon = (ImageView) this.mDialog.findViewById(R.id.dialog_icon);
        this.mVoice = (ImageView) this.mDialog.findViewById(R.id.dialog_voice);
        this.mLable = (TextView) this.mDialog.findViewById(R.id.recorder_dialogtext);
        Window window = this.mDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        int screenWidth = ScreenUtil.getScreenWidth(this.mContext) / 2;
        attributes.width = screenWidth;
        attributes.height = screenWidth;
        window.setAttributes(attributes);
        this.mDialog.setCancelable(false);
        this.mDialog.show();
    }

    public void recording() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mIcon.setVisibility(8);
        this.mVoice.setVisibility(0);
        this.mLable.setVisibility(0);
        this.mLable.setText(R.string.shouzhishanghua);
    }

    public void wantToCancel() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mIcon.setVisibility(0);
        this.mVoice.setVisibility(8);
        this.mLable.setVisibility(0);
        this.mIcon.setImageResource(R.drawable.cancel);
        this.mLable.setText(R.string.want_to_cancle);
    }

    public void tooShort() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mIcon.setVisibility(0);
        this.mVoice.setVisibility(8);
        this.mLable.setVisibility(0);
        this.mIcon.setImageResource(R.drawable.voice_to_short);
        this.mLable.setText(R.string.tooshort);
    }

    public void tooLong() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mIcon.setVisibility(0);
        this.mVoice.setVisibility(8);
        this.mLable.setVisibility(0);
        this.mIcon.setImageResource(R.drawable.voice_to_short);
        this.mLable.setText(R.string.toolong);
    }

    public void dimissDialog() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
        this.mDialog = null;
    }

    public void updateVoiceLevel(int i) {
        int identifier;
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        if (i >= 1 && i < 2) {
            identifier = this.mContext.getResources().getIdentifier("tb_voice1", "mipmap", this.mContext.getPackageName());
        } else if (i >= 2 && i < 3) {
            identifier = this.mContext.getResources().getIdentifier("tb_voice2", "mipmap", this.mContext.getPackageName());
        } else {
            identifier = this.mContext.getResources().getIdentifier("tb_voice3", "mipmap", this.mContext.getPackageName());
        }
        this.mVoice.setImageResource(identifier);
    }
}
