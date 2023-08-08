package com.home.fragment.sun;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class ModeFragment_sun_ViewBinding implements Unbinder {
    private ModeFragment_sun target;

    public ModeFragment_sun_ViewBinding(ModeFragment_sun modeFragment_sun, View view) {
        this.target = modeFragment_sun;
        modeFragment_sun.linearLayoutTab1 = Utils.findRequiredView(view, R.id.linearLayoutTab1, "field 'linearLayoutTab1'");
        modeFragment_sun.ll_rread = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_rread, "field 'll_rread'", LinearLayout.class);
        modeFragment_sun.ll_learn = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_learn, "field 'll_learn'", LinearLayout.class);
        modeFragment_sun.ll_together = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_together, "field 'll_together'", LinearLayout.class);
        modeFragment_sun.ll_leisure = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_leisure, "field 'll_leisure'", LinearLayout.class);
        modeFragment_sun.ll_romantic = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_romantic, "field 'll_romantic'", LinearLayout.class);
        modeFragment_sun.ll_soft = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_soft, "field 'll_soft'", LinearLayout.class);
        modeFragment_sun.ll_sleep = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_sleep, "field 'll_sleep'", LinearLayout.class);
        modeFragment_sun.ll_natural = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_natural, "field 'll_natural'", LinearLayout.class);
        modeFragment_sun.seekBarBrightNess_sun = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBrightNess_sun, "field 'seekBarBrightNess_sun'", SeekBar.class);
        modeFragment_sun.textViewBrightNess_sun = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightNess_sun, "field 'textViewBrightNess_sun'", TextView.class);
        modeFragment_sun.seekBarSpeed_sun = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSpeed_sun, "field 'seekBarSpeed_sun'", SeekBar.class);
        modeFragment_sun.textViewSpeed_sun = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSpeed_sun, "field 'textViewSpeed_sun'", TextView.class);
        modeFragment_sun.linearLayoutTab2 = Utils.findRequiredView(view, R.id.linearLayoutTab2, "field 'linearLayoutTab2'");
        modeFragment_sun.ll_jump = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_jump, "field 'll_jump'", LinearLayout.class);
        modeFragment_sun.ll_flash = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_flash, "field 'll_flash'", LinearLayout.class);
        modeFragment_sun.ll_breath = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_breath, "field 'll_breath'", LinearLayout.class);
        modeFragment_sun.seekBarSensitivity_sun = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivity_sun, "field 'seekBarSensitivity_sun'", SeekBar.class);
        modeFragment_sun.textViewSensitivity_sun = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSensitivity_sun, "field 'textViewSensitivity_sun'", TextView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        ModeFragment_sun modeFragment_sun = this.target;
        if (modeFragment_sun == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        modeFragment_sun.linearLayoutTab1 = null;
        modeFragment_sun.ll_rread = null;
        modeFragment_sun.ll_learn = null;
        modeFragment_sun.ll_together = null;
        modeFragment_sun.ll_leisure = null;
        modeFragment_sun.ll_romantic = null;
        modeFragment_sun.ll_soft = null;
        modeFragment_sun.ll_sleep = null;
        modeFragment_sun.ll_natural = null;
        modeFragment_sun.seekBarBrightNess_sun = null;
        modeFragment_sun.textViewBrightNess_sun = null;
        modeFragment_sun.seekBarSpeed_sun = null;
        modeFragment_sun.textViewSpeed_sun = null;
        modeFragment_sun.linearLayoutTab2 = null;
        modeFragment_sun.ll_jump = null;
        modeFragment_sun.ll_flash = null;
        modeFragment_sun.ll_breath = null;
        modeFragment_sun.seekBarSensitivity_sun = null;
        modeFragment_sun.textViewSensitivity_sun = null;
    }
}
