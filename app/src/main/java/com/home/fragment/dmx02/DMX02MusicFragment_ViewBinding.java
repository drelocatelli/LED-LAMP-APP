package com.home.fragment.dmx02;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.view.ScrollForeverTextView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class DMX02MusicFragment_ViewBinding implements Unbinder {
    private DMX02MusicFragment target;

    public DMX02MusicFragment_ViewBinding(DMX02MusicFragment dMX02MusicFragment, View view) {
        this.target = dMX02MusicFragment;
        dMX02MusicFragment.imageViewPre = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPre, "field 'imageViewPre'", ImageView.class);
        dMX02MusicFragment.imageViewPlay = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlay, "field 'imageViewPlay'", ImageView.class);
        dMX02MusicFragment.imageViewNext = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewNext, "field 'imageViewNext'", ImageView.class);
        dMX02MusicFragment.textViewAutoAjust = (ScrollForeverTextView) Utils.findRequiredViewAsType(view, R.id.textViewAutoAjust, "field 'textViewAutoAjust'", ScrollForeverTextView.class);
        dMX02MusicFragment.tvCurrentTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvCurrentTime, "field 'tvCurrentTime'", TextView.class);
        dMX02MusicFragment.tvTotalTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTotalTime, "field 'tvTotalTime'", TextView.class);
        dMX02MusicFragment.seekBarMusic = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarMusic, "field 'seekBarMusic'", SeekBar.class);
        dMX02MusicFragment.imageViewRotate = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewRotate, "field 'imageViewRotate'", ImageView.class);
        dMX02MusicFragment.imageViewPlayType = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlayType, "field 'imageViewPlayType'", ImageView.class);
        dMX02MusicFragment.buttonMusicLib = (Button) Utils.findRequiredViewAsType(view, R.id.buttonMusicLib, "field 'buttonMusicLib'", Button.class);
        dMX02MusicFragment.seekBarRhythm = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarRhythm, "field 'seekBarRhythm'", SeekBar.class);
        dMX02MusicFragment.tvRhythm = (TextView) Utils.findRequiredViewAsType(view, R.id.tvRhythm, "field 'tvRhythm'", TextView.class);
        dMX02MusicFragment.tvrhythmValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvrhythmValue, "field 'tvrhythmValue'", TextView.class);
        dMX02MusicFragment.rlDMX02VoiceCtl = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlDMX02VoiceCtl, "field 'rlDMX02VoiceCtl'", RelativeLayout.class);
        dMX02MusicFragment.seekBarSensitivityDMX02 = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSensitivityDMX02, "field 'seekBarSensitivityDMX02'", SeekBar.class);
        dMX02MusicFragment.textViewSensitivityDMX02 = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSensitivityDMX02, "field 'textViewSensitivityDMX02'", TextView.class);
        dMX02MusicFragment.llMusic = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llMusic, "field 'llMusic'", LinearLayout.class);
        dMX02MusicFragment.ivEditColor = (Button) Utils.findRequiredViewAsType(view, R.id.ivEditColor, "field 'ivEditColor'", Button.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        DMX02MusicFragment dMX02MusicFragment = this.target;
        if (dMX02MusicFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        dMX02MusicFragment.imageViewPre = null;
        dMX02MusicFragment.imageViewPlay = null;
        dMX02MusicFragment.imageViewNext = null;
        dMX02MusicFragment.textViewAutoAjust = null;
        dMX02MusicFragment.tvCurrentTime = null;
        dMX02MusicFragment.tvTotalTime = null;
        dMX02MusicFragment.seekBarMusic = null;
        dMX02MusicFragment.imageViewRotate = null;
        dMX02MusicFragment.imageViewPlayType = null;
        dMX02MusicFragment.buttonMusicLib = null;
        dMX02MusicFragment.seekBarRhythm = null;
        dMX02MusicFragment.tvRhythm = null;
        dMX02MusicFragment.tvrhythmValue = null;
        dMX02MusicFragment.rlDMX02VoiceCtl = null;
        dMX02MusicFragment.seekBarSensitivityDMX02 = null;
        dMX02MusicFragment.textViewSensitivityDMX02 = null;
        dMX02MusicFragment.llMusic = null;
        dMX02MusicFragment.ivEditColor = null;
    }
}
