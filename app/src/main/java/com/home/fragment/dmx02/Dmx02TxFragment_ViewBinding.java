package com.home.fragment.dmx02;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.home.view.custom.LevelProgressBar;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class Dmx02TxFragment_ViewBinding implements Unbinder {
    private Dmx02TxFragment target;

    public Dmx02TxFragment_ViewBinding(Dmx02TxFragment dmx02TxFragment, View view) {
        this.target = dmx02TxFragment;
        dmx02TxFragment.llTop = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llTop, "field 'llTop'", LinearLayout.class);
        dmx02TxFragment.tvTop = (EditText) Utils.findRequiredViewAsType(view, R.id.tvTop, "field 'tvTop'", EditText.class);
        dmx02TxFragment.etContent = (EditText) Utils.findRequiredViewAsType(view, R.id.etContent, "field 'etContent'", EditText.class);
        dmx02TxFragment.tvFontSizeValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvFontSizeValue, "field 'tvFontSizeValue'", TextView.class);
        dmx02TxFragment.llTextSize = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llTextSize, "field 'llTextSize'", LinearLayout.class);
        dmx02TxFragment.llTextBackground = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llTextBackground, "field 'llTextBackground'", LinearLayout.class);
        dmx02TxFragment.tvBackgroundValue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBackgroundValue, "field 'tvBackgroundValue'", TextView.class);
        dmx02TxFragment.sbColor = (SeekBar) Utils.findRequiredViewAsType(view, R.id.sbColor, "field 'sbColor'", SeekBar.class);
        dmx02TxFragment.tvColor = (TextView) Utils.findRequiredViewAsType(view, R.id.tvColor, "field 'tvColor'", TextView.class);
        dmx02TxFragment.sbSpeed = (SeekBar) Utils.findRequiredViewAsType(view, R.id.sbSpeed, "field 'sbSpeed'", SeekBar.class);
        dmx02TxFragment.tvSpeed = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSpeed, "field 'tvSpeed'", TextView.class);
        dmx02TxFragment.tvSend = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSend, "field 'tvSend'", TextView.class);
        dmx02TxFragment.rlProgressView = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlProgressView, "field 'rlProgressView'", RelativeLayout.class);
        dmx02TxFragment.tvProgress = (TextView) Utils.findRequiredViewAsType(view, R.id.tvProgress, "field 'tvProgress'", TextView.class);
        dmx02TxFragment.progressBar = (LevelProgressBar) Utils.findRequiredViewAsType(view, R.id.progressBar, "field 'progressBar'", LevelProgressBar.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        Dmx02TxFragment dmx02TxFragment = this.target;
        if (dmx02TxFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        dmx02TxFragment.llTop = null;
        dmx02TxFragment.tvTop = null;
        dmx02TxFragment.etContent = null;
        dmx02TxFragment.tvFontSizeValue = null;
        dmx02TxFragment.llTextSize = null;
        dmx02TxFragment.llTextBackground = null;
        dmx02TxFragment.tvBackgroundValue = null;
        dmx02TxFragment.sbColor = null;
        dmx02TxFragment.tvColor = null;
        dmx02TxFragment.sbSpeed = null;
        dmx02TxFragment.tvSpeed = null;
        dmx02TxFragment.tvSend = null;
        dmx02TxFragment.rlProgressView = null;
        dmx02TxFragment.tvProgress = null;
        dmx02TxFragment.progressBar = null;
    }
}
