package com.home.fragment.ble;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class BrightFragment_ViewBinding implements Unbinder {
    private BrightFragment target;

    public BrightFragment_ViewBinding(BrightFragment brightFragment, View view) {
        this.target = brightFragment;
        brightFragment.one = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.one, "field 'one'", LinearLayout.class);
        brightFragment.two = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.two, "field 'two'", LinearLayout.class);
        brightFragment.three = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.three, "field 'three'", LinearLayout.class);
        brightFragment.four = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.four, "field 'four'", LinearLayout.class);
        brightFragment.five = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.five, "field 'five'", LinearLayout.class);
        brightFragment.six = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.six, "field 'six'", LinearLayout.class);
        brightFragment.seekBarRedBrightNess3 = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarRedBrightNess3, "field 'seekBarRedBrightNess3'", SeekBar.class);
        brightFragment.tvRedBrightNess3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvRedBrightNess3, "field 'tvRedBrightNess3'", TextView.class);
        brightFragment.seekBarGreenBrightNess3 = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarGreenBrightNess3, "field 'seekBarGreenBrightNess3'", SeekBar.class);
        brightFragment.tvGreenBrightNess3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvGreenBrightNess3, "field 'tvGreenBrightNess3'", TextView.class);
        brightFragment.seekBarBlueBrightNess3 = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBlueBrightNess3, "field 'seekBarBlueBrightNess3'", SeekBar.class);
        brightFragment.tvBlueBrightNess3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBlueBrightNess3, "field 'tvBlueBrightNess3'", TextView.class);
        brightFragment.seekBarWhiteBrightNess3 = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarWhiteBrightNess3, "field 'seekBarWhiteBrightNess3'", SeekBar.class);
        brightFragment.tvWhiteBrightNess3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvWhiteBrightNess3, "field 'tvWhiteBrightNess3'", TextView.class);
        brightFragment.seekBarCrystalBrightNess3 = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarCrystalBrightNess3, "field 'seekBarCrystalBrightNess3'", SeekBar.class);
        brightFragment.tvCrystalBrightNess3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvCrystalBrightNess3, "field 'tvCrystalBrightNess3'", TextView.class);
        brightFragment.seekBarPinkBrightNess3 = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarPinkBrightNess3, "field 'seekBarPinkBrightNess3'", SeekBar.class);
        brightFragment.tvPinkBrightNess3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvPinkBrightNess3, "field 'tvPinkBrightNess3'", TextView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        BrightFragment brightFragment = this.target;
        if (brightFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        brightFragment.one = null;
        brightFragment.two = null;
        brightFragment.three = null;
        brightFragment.four = null;
        brightFragment.five = null;
        brightFragment.six = null;
        brightFragment.seekBarRedBrightNess3 = null;
        brightFragment.tvRedBrightNess3 = null;
        brightFragment.seekBarGreenBrightNess3 = null;
        brightFragment.tvGreenBrightNess3 = null;
        brightFragment.seekBarBlueBrightNess3 = null;
        brightFragment.tvBlueBrightNess3 = null;
        brightFragment.seekBarWhiteBrightNess3 = null;
        brightFragment.tvWhiteBrightNess3 = null;
        brightFragment.seekBarCrystalBrightNess3 = null;
        brightFragment.tvCrystalBrightNess3 = null;
        brightFragment.seekBarPinkBrightNess3 = null;
        brightFragment.tvPinkBrightNess3 = null;
    }
}
