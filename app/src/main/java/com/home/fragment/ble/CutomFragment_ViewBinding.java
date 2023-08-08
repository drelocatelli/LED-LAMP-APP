package com.home.fragment.ble;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.view.SegmentedRadioGroup;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class CutomFragment_ViewBinding implements Unbinder {
    private CutomFragment target;

    public CutomFragment_ViewBinding(CutomFragment cutomFragment, View view) {
        this.target = cutomFragment;
        cutomFragment.llCustomCAR01BLEKeys = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llCustomCAR01BLEKeys, "field 'llCustomCAR01BLEKeys'", LinearLayout.class);
        cutomFragment.llCustomCAR01DMXKeys = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llCustomCAR01DMXKeys, "field 'llCustomCAR01DMXKeys'", LinearLayout.class);
        cutomFragment.llViewBlocks = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llViewBlocks, "field 'llViewBlocks'", LinearLayout.class);
        cutomFragment.llViewBlocksCar01DMX = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llViewBlocksCar01DMX, "field 'llViewBlocksCar01DMX'", LinearLayout.class);
        cutomFragment.segmentedRadioGroupOne = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentedRadioGroupOne, "field 'segmentedRadioGroupOne'", SegmentedRadioGroup.class);
        cutomFragment.segmentedRadioGroupTwo = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentedRadioGroupTwo, "field 'segmentedRadioGroupTwo'", SegmentedRadioGroup.class);
        cutomFragment.llSegmentedRadioGroupTwo = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llSegmentedRadioGroupTwo, "field 'llSegmentedRadioGroupTwo'", LinearLayout.class);
        cutomFragment.seekBarSpeedCustom = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSpeedCustom, "field 'seekBarSpeedCustom'", SeekBar.class);
        cutomFragment.textViewSpeedCustom = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSpeedCustom, "field 'textViewSpeedCustom'", TextView.class);
        cutomFragment.seekBarBrightCustom = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBrightCustom, "field 'seekBarBrightCustom'", SeekBar.class);
        cutomFragment.textViewBrightCustom = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightCustom, "field 'textViewBrightCustom'", TextView.class);
        cutomFragment.changeButtonOne = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonOne, "field 'changeButtonOne'", RadioButton.class);
        cutomFragment.changeButtonTwo = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonTwo, "field 'changeButtonTwo'", RadioButton.class);
        cutomFragment.changeButtonThree = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonThree, "field 'changeButtonThree'", RadioButton.class);
        cutomFragment.changeButtonFour = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonFour, "field 'changeButtonFour'", RadioButton.class);
        cutomFragment.changeButtonFive = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonFive, "field 'changeButtonFive'", RadioButton.class);
        cutomFragment.changeButtonSix = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonSix, "field 'changeButtonSix'", RadioButton.class);
        cutomFragment.changeButtonSeven = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonSeven, "field 'changeButtonSeven'", RadioButton.class);
        cutomFragment.changeButtonEight = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButtonEight, "field 'changeButtonEight'", RadioButton.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        CutomFragment cutomFragment = this.target;
        if (cutomFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        cutomFragment.llCustomCAR01BLEKeys = null;
        cutomFragment.llCustomCAR01DMXKeys = null;
        cutomFragment.llViewBlocks = null;
        cutomFragment.llViewBlocksCar01DMX = null;
        cutomFragment.segmentedRadioGroupOne = null;
        cutomFragment.segmentedRadioGroupTwo = null;
        cutomFragment.llSegmentedRadioGroupTwo = null;
        cutomFragment.seekBarSpeedCustom = null;
        cutomFragment.textViewSpeedCustom = null;
        cutomFragment.seekBarBrightCustom = null;
        cutomFragment.textViewBrightCustom = null;
        cutomFragment.changeButtonOne = null;
        cutomFragment.changeButtonTwo = null;
        cutomFragment.changeButtonThree = null;
        cutomFragment.changeButtonFour = null;
        cutomFragment.changeButtonFive = null;
        cutomFragment.changeButtonSix = null;
        cutomFragment.changeButtonSeven = null;
        cutomFragment.changeButtonEight = null;
    }
}
