package com.home.fragment.dmx03;

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
import com.ledlamp.R;

/* loaded from: classes.dex */
public class DMX03MusicFragment_ViewBinding implements Unbinder {
    private DMX03MusicFragment target;

    public DMX03MusicFragment_ViewBinding(DMX03MusicFragment dMX03MusicFragment, View view) {
        this.target = dMX03MusicFragment;
        dMX03MusicFragment.changeButton_micro = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.changeButton_micro, "field 'changeButton_micro'", SegmentedRadioGroup.class);
        dMX03MusicFragment.imageViewPre = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPre, "field 'imageViewPre'", ImageView.class);
        dMX03MusicFragment.imageViewPlay = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlay, "field 'imageViewPlay'", ImageView.class);
        dMX03MusicFragment.imageViewNext = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewNext, "field 'imageViewNext'", ImageView.class);
        dMX03MusicFragment.textViewAutoAjust = (ScrollForeverTextView) Utils.findRequiredViewAsType(view, R.id.textViewAutoAjust, "field 'textViewAutoAjust'", ScrollForeverTextView.class);
        dMX03MusicFragment.tvCurrentTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvCurrentTime, "field 'tvCurrentTime'", TextView.class);
        dMX03MusicFragment.tvTotalTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTotalTime, "field 'tvTotalTime'", TextView.class);
        dMX03MusicFragment.seekBarMusic = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarMusic, "field 'seekBarMusic'", SeekBar.class);
        dMX03MusicFragment.imageViewRotate = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewRotate, "field 'imageViewRotate'", ImageView.class);
        dMX03MusicFragment.imageViewPlayType = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlayType, "field 'imageViewPlayType'", ImageView.class);
        dMX03MusicFragment.buttonMusicLib = (Button) Utils.findRequiredViewAsType(view, R.id.buttonMusicLib, "field 'buttonMusicLib'", Button.class);
        dMX03MusicFragment.seekBarRhythm = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarRhythm, "field 'seekBarRhythm'", SeekBar.class);
        dMX03MusicFragment.tvRhythm = (TextView) Utils.findRequiredViewAsType(view, R.id.tvRhythm, "field 'tvRhythm'", TextView.class);
        dMX03MusicFragment.tvrhythmValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvrhythmValue, "field 'tvrhythmValue'", TextView.class);
        dMX03MusicFragment.changeButton_One = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_One, "field 'changeButton_One'", RadioButton.class);
        dMX03MusicFragment.changeButton_Two = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_Two, "field 'changeButton_Two'", RadioButton.class);
        dMX03MusicFragment.changeButton_Three = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_Three, "field 'changeButton_Three'", RadioButton.class);
        dMX03MusicFragment.changeButton_Four = (RadioButton) Utils.findRequiredViewAsType(view, R.id.changeButton_Four, "field 'changeButton_Four'", RadioButton.class);
        dMX03MusicFragment.rlDMXVoiceCtl = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlDMXVoiceCtl, "field 'rlDMXVoiceCtl'", RelativeLayout.class);
        dMX03MusicFragment.seekBarSensitivityDMX = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivityDMX, "field 'seekBarSensitivityDMX'", SeekBar.class);
        dMX03MusicFragment.textViewSensitivityDMX = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSensitivityDMX, "field 'textViewSensitivityDMX'", TextView.class);
        dMX03MusicFragment.llMusic = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llMusic, "field 'llMusic'", LinearLayout.class);
        dMX03MusicFragment.llMicro = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llMicro, "field 'llMicro'", LinearLayout.class);
        dMX03MusicFragment.volumCircleBar = (VolumCircleBar) Utils.findRequiredViewAsType(view, R.id.volumCircleBar, "field 'volumCircleBar'", VolumCircleBar.class);
        dMX03MusicFragment.ll_seekBarDecibel = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_seekBarDecibel, "field 'll_seekBarDecibel'", LinearLayout.class);
        dMX03MusicFragment.seekBarDecibel = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarDecibel, "field 'seekBarDecibel'", SeekBar.class);
        dMX03MusicFragment.tvDecibelValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvDecibelValue, "field 'tvDecibelValue'", TextView.class);
        dMX03MusicFragment.ll_seekBarSensitivity = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_seekBarSensitivity, "field 'll_seekBarSensitivity'", LinearLayout.class);
        dMX03MusicFragment.seekBarSensitivity = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivity, "field 'seekBarSensitivity'", SeekBar.class);
        dMX03MusicFragment.tvSensitivityValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSensitivityValue, "field 'tvSensitivityValue'", TextView.class);
        dMX03MusicFragment.rbNeiMai = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbNeiMai, "field 'rbNeiMai'", RadioButton.class);
        dMX03MusicFragment.rbWaiMai = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbWaiMai, "field 'rbWaiMai'", RadioButton.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        DMX03MusicFragment dMX03MusicFragment = this.target;
        if (dMX03MusicFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        dMX03MusicFragment.changeButton_micro = null;
        dMX03MusicFragment.imageViewPre = null;
        dMX03MusicFragment.imageViewPlay = null;
        dMX03MusicFragment.imageViewNext = null;
        dMX03MusicFragment.textViewAutoAjust = null;
        dMX03MusicFragment.tvCurrentTime = null;
        dMX03MusicFragment.tvTotalTime = null;
        dMX03MusicFragment.seekBarMusic = null;
        dMX03MusicFragment.imageViewRotate = null;
        dMX03MusicFragment.imageViewPlayType = null;
        dMX03MusicFragment.buttonMusicLib = null;
        dMX03MusicFragment.seekBarRhythm = null;
        dMX03MusicFragment.tvRhythm = null;
        dMX03MusicFragment.tvrhythmValue = null;
        dMX03MusicFragment.changeButton_One = null;
        dMX03MusicFragment.changeButton_Two = null;
        dMX03MusicFragment.changeButton_Three = null;
        dMX03MusicFragment.changeButton_Four = null;
        dMX03MusicFragment.rlDMXVoiceCtl = null;
        dMX03MusicFragment.seekBarSensitivityDMX = null;
        dMX03MusicFragment.textViewSensitivityDMX = null;
        dMX03MusicFragment.llMusic = null;
        dMX03MusicFragment.llMicro = null;
        dMX03MusicFragment.volumCircleBar = null;
        dMX03MusicFragment.ll_seekBarDecibel = null;
        dMX03MusicFragment.seekBarDecibel = null;
        dMX03MusicFragment.tvDecibelValue = null;
        dMX03MusicFragment.ll_seekBarSensitivity = null;
        dMX03MusicFragment.seekBarSensitivity = null;
        dMX03MusicFragment.tvSensitivityValue = null;
        dMX03MusicFragment.rbNeiMai = null;
        dMX03MusicFragment.rbWaiMai = null;
    }
}
