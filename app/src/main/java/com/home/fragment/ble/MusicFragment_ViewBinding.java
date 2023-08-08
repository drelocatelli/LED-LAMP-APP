package com.home.fragment.ble;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.view.ScrollForeverTextView;
import com.common.view.SegmentedRadioGroup;
import com.home.view.VolumCircleBar;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MusicFragment_ViewBinding implements Unbinder {
    private MusicFragment target;

    public MusicFragment_ViewBinding(MusicFragment musicFragment, View view) {
        this.target = musicFragment;
        musicFragment.changeButton_micro = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.changeButton_micro, "field 'changeButton_micro'", SegmentedRadioGroup.class);
        musicFragment.imageViewPre = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPre, "field 'imageViewPre'", ImageView.class);
        musicFragment.imageViewPlay = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlay, "field 'imageViewPlay'", ImageView.class);
        musicFragment.imageViewNext = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewNext, "field 'imageViewNext'", ImageView.class);
        musicFragment.textViewAutoAjust = (ScrollForeverTextView) Utils.findRequiredViewAsType(view, R.id.textViewAutoAjust, "field 'textViewAutoAjust'", ScrollForeverTextView.class);
        musicFragment.tvCurrentTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvCurrentTime, "field 'tvCurrentTime'", TextView.class);
        musicFragment.tvTotalTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTotalTime, "field 'tvTotalTime'", TextView.class);
        musicFragment.seekBarMusic = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarMusic, "field 'seekBarMusic'", SeekBar.class);
        musicFragment.imageViewRotate = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewRotate, "field 'imageViewRotate'", ImageView.class);
        musicFragment.imageViewPlayType = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlayType, "field 'imageViewPlayType'", ImageView.class);
        musicFragment.buttonMusicLib = (Button) Utils.findRequiredViewAsType(view, R.id.buttonMusicLib, "field 'buttonMusicLib'", Button.class);
        musicFragment.seekBarRhythm = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarRhythm, "field 'seekBarRhythm'", SeekBar.class);
        musicFragment.tvRhythm = (TextView) Utils.findRequiredViewAsType(view, R.id.tvRhythm, "field 'tvRhythm'", TextView.class);
        musicFragment.tvrhythmValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvrhythmValue, "field 'tvrhythmValue'", TextView.class);
        musicFragment.changeButton_One = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_One, "field 'changeButton_One'", RadioButton.class);
        musicFragment.changeButton_Two = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_Two, "field 'changeButton_Two'", RadioButton.class);
        musicFragment.changeButton_Three = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_Three, "field 'changeButton_Three'", RadioButton.class);
        musicFragment.changeButton_Four = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_Four, "field 'changeButton_Four'", RadioButton.class);
        musicFragment.rlBLEVoiceCtl = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlBLEVoiceCtl, "field 'rlBLEVoiceCtl'", RelativeLayout.class);
        musicFragment.rlDMXVoiceCtl = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlDMXVoiceCtl, "field 'rlDMXVoiceCtl'", RelativeLayout.class);
        musicFragment.wheelPickerDMX = (WheelPicker) Utils.findRequiredViewAsType(view, R.id.wheelPickerDMX, "field 'wheelPickerDMX'", WheelPicker.class);
        musicFragment.seekBarSensitivityDMX = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivityDMX, "field 'seekBarSensitivityDMX'", SeekBar.class);
        musicFragment.textViewSensitivityDMX = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSensitivityDMX, "field 'textViewSensitivityDMX'", TextView.class);
        musicFragment.rlCar01VoiceCtl = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlCar01VoiceCtl, "field 'rlCar01VoiceCtl'", RelativeLayout.class);
        musicFragment.ll_jump = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_jump, "field 'll_jump'", LinearLayout.class);
        musicFragment.ll_breathe = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_breathe, "field 'll_breathe'", LinearLayout.class);
        musicFragment.ll_flash = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_flash, "field 'll_flash'", LinearLayout.class);
        musicFragment.ll_gradient = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_gradient, "field 'll_gradient'", LinearLayout.class);
        musicFragment.seekBarSensitivityCAR01Top = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivityCAR01Top, "field 'seekBarSensitivityCAR01Top'", SeekBar.class);
        musicFragment.textViewSensitivityCAR01Top = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSensitivityCAR01Top, "field 'textViewSensitivityCAR01Top'", TextView.class);
        musicFragment.wheelPickerCAR01 = (WheelPicker) Utils.findRequiredViewAsType(view, R.id.wheelPickerCAR01, "field 'wheelPickerCAR01'", WheelPicker.class);
        musicFragment.seekBarSensitivityCAR01Bottom = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivityCAR01Bottom, "field 'seekBarSensitivityCAR01Bottom'", SeekBar.class);
        musicFragment.textViewSensitivityCAR01Bottom = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSensitivityCAR01Bottom, "field 'textViewSensitivityCAR01Bottom'", TextView.class);
        musicFragment.llMusic = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llMusic, "field 'llMusic'", LinearLayout.class);
        musicFragment.llMicro = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llMicro, "field 'llMicro'", LinearLayout.class);
        musicFragment.volumCircleBar = (VolumCircleBar) Utils.findRequiredViewAsType(view, R.id.volumCircleBar, "field 'volumCircleBar'", VolumCircleBar.class);
        musicFragment.ll_seekBarDecibel = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_seekBarDecibel, "field 'll_seekBarDecibel'", LinearLayout.class);
        musicFragment.seekBarDecibel = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarDecibel, "field 'seekBarDecibel'", SeekBar.class);
        musicFragment.tvDecibelValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvDecibelValue, "field 'tvDecibelValue'", TextView.class);
        musicFragment.ll_seekBarSensitivity = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_seekBarSensitivity, "field 'll_seekBarSensitivity'", LinearLayout.class);
        musicFragment.seekBarSensitivity = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivity, "field 'seekBarSensitivity'", SeekBar.class);
        musicFragment.tvSensitivityValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSensitivityValue, "field 'tvSensitivityValue'", TextView.class);
        musicFragment.rbNeiMai = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbNeiMai, "field 'rbNeiMai'", RadioButton.class);
        musicFragment.rbWaiMai = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbWaiMai, "field 'rbWaiMai'", RadioButton.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MusicFragment musicFragment = this.target;
        if (musicFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        musicFragment.changeButton_micro = null;
        musicFragment.imageViewPre = null;
        musicFragment.imageViewPlay = null;
        musicFragment.imageViewNext = null;
        musicFragment.textViewAutoAjust = null;
        musicFragment.tvCurrentTime = null;
        musicFragment.tvTotalTime = null;
        musicFragment.seekBarMusic = null;
        musicFragment.imageViewRotate = null;
        musicFragment.imageViewPlayType = null;
        musicFragment.buttonMusicLib = null;
        musicFragment.seekBarRhythm = null;
        musicFragment.tvRhythm = null;
        musicFragment.tvrhythmValue = null;
        musicFragment.changeButton_One = null;
        musicFragment.changeButton_Two = null;
        musicFragment.changeButton_Three = null;
        musicFragment.changeButton_Four = null;
        musicFragment.rlBLEVoiceCtl = null;
        musicFragment.rlDMXVoiceCtl = null;
        musicFragment.wheelPickerDMX = null;
        musicFragment.seekBarSensitivityDMX = null;
        musicFragment.textViewSensitivityDMX = null;
        musicFragment.rlCar01VoiceCtl = null;
        musicFragment.ll_jump = null;
        musicFragment.ll_breathe = null;
        musicFragment.ll_flash = null;
        musicFragment.ll_gradient = null;
        musicFragment.seekBarSensitivityCAR01Top = null;
        musicFragment.textViewSensitivityCAR01Top = null;
        musicFragment.wheelPickerCAR01 = null;
        musicFragment.seekBarSensitivityCAR01Bottom = null;
        musicFragment.textViewSensitivityCAR01Bottom = null;
        musicFragment.llMusic = null;
        musicFragment.llMicro = null;
        musicFragment.volumCircleBar = null;
        musicFragment.ll_seekBarDecibel = null;
        musicFragment.seekBarDecibel = null;
        musicFragment.tvDecibelValue = null;
        musicFragment.ll_seekBarSensitivity = null;
        musicFragment.seekBarSensitivity = null;
        musicFragment.tvSensitivityValue = null;
        musicFragment.rbNeiMai = null;
        musicFragment.rbWaiMai = null;
    }
}
