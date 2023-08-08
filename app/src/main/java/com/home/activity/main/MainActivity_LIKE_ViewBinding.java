package com.home.activity.main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MainActivity_LIKE_ViewBinding implements Unbinder {
    private MainActivity_LIKE target;

    public MainActivity_LIKE_ViewBinding(MainActivity_LIKE mainActivity_LIKE) {
        this(mainActivity_LIKE, mainActivity_LIKE.getWindow().getDecorView());
    }

    public MainActivity_LIKE_ViewBinding(MainActivity_LIKE mainActivity_LIKE, View view) {
        this.target = mainActivity_LIKE;
        mainActivity_LIKE.backTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.backTextView, "field 'backTextView'", TextView.class);
        mainActivity_LIKE.onOffButton = (Button) Utils.findRequiredViewAsType(view, R.id.onOffButton, "field 'onOffButton'", Button.class);
        mainActivity_LIKE.tvModeBrightness0 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvModeBrightness0, "field 'tvModeBrightness0'", TextView.class);
        mainActivity_LIKE.tvModeBrightness20 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvModeBrightness20, "field 'tvModeBrightness20'", TextView.class);
        mainActivity_LIKE.tvModeBrightness40 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvModeBrightness40, "field 'tvModeBrightness40'", TextView.class);
        mainActivity_LIKE.tvModeBrightness60 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvModeBrightness60, "field 'tvModeBrightness60'", TextView.class);
        mainActivity_LIKE.tvModeBrightness80 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvModeBrightness80, "field 'tvModeBrightness80'", TextView.class);
        mainActivity_LIKE.tvModeBrightness100 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvModeBrightness100, "field 'tvModeBrightness100'", TextView.class);
        mainActivity_LIKE.ll_mode = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_mode, "field 'll_mode'", LinearLayout.class);
        mainActivity_LIKE.ll_voicecontrol = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_voicecontrol, "field 'll_voicecontrol'", LinearLayout.class);
        mainActivity_LIKE.tvMode = (TextView) Utils.findRequiredViewAsType(view, R.id.tvMode, "field 'tvMode'", TextView.class);
        mainActivity_LIKE.tvVoicecontrol = (TextView) Utils.findRequiredViewAsType(view, R.id.tvVoicecontrol, "field 'tvVoicecontrol'", TextView.class);
        mainActivity_LIKE.tvModeLine = (TextView) Utils.findRequiredViewAsType(view, R.id.tvModeLine, "field 'tvModeLine'", TextView.class);
        mainActivity_LIKE.tvVoicecontrolLine = (TextView) Utils.findRequiredViewAsType(view, R.id.tvVoicecontrolLine, "field 'tvVoicecontrolLine'", TextView.class);
        mainActivity_LIKE.tvBrightnessValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightnessValue, "field 'tvBrightnessValue'", TextView.class);
        mainActivity_LIKE.sb_brightness = (SeekBar) Utils.findRequiredViewAsType(view, R.id.sb_brightness, "field 'sb_brightness'", SeekBar.class);
        mainActivity_LIKE.tvSpeedValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSpeedValue, "field 'tvSpeedValue'", TextView.class);
        mainActivity_LIKE.sb_speed = (SeekBar) Utils.findRequiredViewAsType(view, R.id.sb_speed, "field 'sb_speed'", SeekBar.class);
        mainActivity_LIKE.tvTimer = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTimer, "field 'tvTimer'", TextView.class);
        mainActivity_LIKE.tvMusic = (TextView) Utils.findRequiredViewAsType(view, R.id.tvMusic, "field 'tvMusic'", TextView.class);
        mainActivity_LIKE.buttonBreathe = (Button) Utils.findRequiredViewAsType(view, R.id.buttonBreathe, "field 'buttonBreathe'", Button.class);
        mainActivity_LIKE.buttonFlash = (Button) Utils.findRequiredViewAsType(view, R.id.buttonFlash, "field 'buttonFlash'", Button.class);
        mainActivity_LIKE.buttonStrobe = (Button) Utils.findRequiredViewAsType(view, R.id.buttonStrobe, "field 'buttonStrobe'", Button.class);
        mainActivity_LIKE.buttonVoiceCtlBreathe = (Button) Utils.findRequiredViewAsType(view, R.id.buttonVoiceCtlBreathe, "field 'buttonVoiceCtlBreathe'", Button.class);
        mainActivity_LIKE.buttonVoiceCtlFlash = (Button) Utils.findRequiredViewAsType(view, R.id.buttonVoiceCtlFlash, "field 'buttonVoiceCtlFlash'", Button.class);
        mainActivity_LIKE.buttonVoiceCtlStrobe = (Button) Utils.findRequiredViewAsType(view, R.id.buttonVoiceCtlStrobe, "field 'buttonVoiceCtlStrobe'", Button.class);
        mainActivity_LIKE.tvSensitivityValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSensitivityValue, "field 'tvSensitivityValue'", TextView.class);
        mainActivity_LIKE.sbSensitivity = (SeekBar) Utils.findRequiredViewAsType(view, R.id.sbSensitivity, "field 'sbSensitivity'", SeekBar.class);
        mainActivity_LIKE.ivLeftMenu = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivLeftMenu, "field 'ivLeftMenu'", ImageView.class);
        mainActivity_LIKE.left_menu = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.menu_content_layout, "field 'left_menu'", LinearLayout.class);
        mainActivity_LIKE.linearLayoutBottom = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearLayoutBottom, "field 'linearLayoutBottom'", LinearLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MainActivity_LIKE mainActivity_LIKE = this.target;
        if (mainActivity_LIKE == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        mainActivity_LIKE.backTextView = null;
        mainActivity_LIKE.onOffButton = null;
        mainActivity_LIKE.tvModeBrightness0 = null;
        mainActivity_LIKE.tvModeBrightness20 = null;
        mainActivity_LIKE.tvModeBrightness40 = null;
        mainActivity_LIKE.tvModeBrightness60 = null;
        mainActivity_LIKE.tvModeBrightness80 = null;
        mainActivity_LIKE.tvModeBrightness100 = null;
        mainActivity_LIKE.ll_mode = null;
        mainActivity_LIKE.ll_voicecontrol = null;
        mainActivity_LIKE.tvMode = null;
        mainActivity_LIKE.tvVoicecontrol = null;
        mainActivity_LIKE.tvModeLine = null;
        mainActivity_LIKE.tvVoicecontrolLine = null;
        mainActivity_LIKE.tvBrightnessValue = null;
        mainActivity_LIKE.sb_brightness = null;
        mainActivity_LIKE.tvSpeedValue = null;
        mainActivity_LIKE.sb_speed = null;
        mainActivity_LIKE.tvTimer = null;
        mainActivity_LIKE.tvMusic = null;
        mainActivity_LIKE.buttonBreathe = null;
        mainActivity_LIKE.buttonFlash = null;
        mainActivity_LIKE.buttonStrobe = null;
        mainActivity_LIKE.buttonVoiceCtlBreathe = null;
        mainActivity_LIKE.buttonVoiceCtlFlash = null;
        mainActivity_LIKE.buttonVoiceCtlStrobe = null;
        mainActivity_LIKE.tvSensitivityValue = null;
        mainActivity_LIKE.sbSensitivity = null;
        mainActivity_LIKE.ivLeftMenu = null;
        mainActivity_LIKE.left_menu = null;
        mainActivity_LIKE.linearLayoutBottom = null;
    }
}
