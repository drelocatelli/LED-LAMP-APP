package com.home.fragment.dmx03;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class DMX03ModeFragment_ViewBinding implements Unbinder {
    private DMX03ModeFragment target;

    public DMX03ModeFragment_ViewBinding(DMX03ModeFragment dMX03ModeFragment, View view) {
        this.target = dMX03ModeFragment;
        dMX03ModeFragment.wheelPicker = (WheelPicker) Utils.findRequiredViewAsType(view, R.id.wheelPicker, "field 'wheelPicker'", WheelPicker.class);
        dMX03ModeFragment.imageViewOnOff = (Button) Utils.findRequiredViewAsType(view, R.id.imageViewOnOff, "field 'imageViewOnOff'", Button.class);
        dMX03ModeFragment.seekBarMode = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarMode, "field 'seekBarMode'", SeekBar.class);
        dMX03ModeFragment.textViewMode = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewMode, "field 'textViewMode'", TextView.class);
        dMX03ModeFragment.seekBarSpeedBar = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSpeed, "field 'seekBarSpeedBar'", SeekBar.class);
        dMX03ModeFragment.textViewSpeed = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSpeed, "field 'textViewSpeed'", TextView.class);
        dMX03ModeFragment.seekBarBrightness = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBrightNess, "field 'seekBarBrightness'", SeekBar.class);
        dMX03ModeFragment.textViewBrightness = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightNess, "field 'textViewBrightness'", TextView.class);
        dMX03ModeFragment.llmode = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llmode, "field 'llmode'", LinearLayout.class);
        dMX03ModeFragment.rlModeTopDIYCAR01BLE = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlModeTopDIYCAR01BLE, "field 'rlModeTopDIYCAR01BLE'", RelativeLayout.class);
        dMX03ModeFragment.rlModeTopDIYCAR01DMX = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlModeTopDIYCAR01DMX, "field 'rlModeTopDIYCAR01DMX'", RelativeLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        DMX03ModeFragment dMX03ModeFragment = this.target;
        if (dMX03ModeFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        dMX03ModeFragment.wheelPicker = null;
        dMX03ModeFragment.imageViewOnOff = null;
        dMX03ModeFragment.seekBarMode = null;
        dMX03ModeFragment.textViewMode = null;
        dMX03ModeFragment.seekBarSpeedBar = null;
        dMX03ModeFragment.textViewSpeed = null;
        dMX03ModeFragment.seekBarBrightness = null;
        dMX03ModeFragment.textViewBrightness = null;
        dMX03ModeFragment.llmode = null;
        dMX03ModeFragment.rlModeTopDIYCAR01BLE = null;
        dMX03ModeFragment.rlModeTopDIYCAR01DMX = null;
    }
}
