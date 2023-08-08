package com.home.fragment.dmx02;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.home.view.custom.LevelProgressBar;
import com.home.view.dmx02.DoodleView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class DMX02GraffitFragment_ViewBinding implements Unbinder {
    private DMX02GraffitFragment target;

    public DMX02GraffitFragment_ViewBinding(DMX02GraffitFragment dMX02GraffitFragment, View view) {
        this.target = dMX02GraffitFragment;
        dMX02GraffitFragment.llTop = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llTop, "field 'llTop'", LinearLayout.class);
        dMX02GraffitFragment.graffitView = (DoodleView) Utils.findRequiredViewAsType(view, R.id.graffitView, "field 'graffitView'", DoodleView.class);
        dMX02GraffitFragment.sbColor = (SeekBar) Utils.findRequiredViewAsType(view, R.id.sbColor, "field 'sbColor'", SeekBar.class);
        dMX02GraffitFragment.tvColor = (TextView) Utils.findRequiredViewAsType(view, R.id.tvColor, "field 'tvColor'", TextView.class);
        dMX02GraffitFragment.sbBrightness = (SeekBar) Utils.findRequiredViewAsType(view, R.id.sbBrightness, "field 'sbBrightness'", SeekBar.class);
        dMX02GraffitFragment.tvBrightness = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness, "field 'tvBrightness'", TextView.class);
        dMX02GraffitFragment.tvSend = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSend, "field 'tvSend'", TextView.class);
        dMX02GraffitFragment.rlProgressView = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlProgressView, "field 'rlProgressView'", RelativeLayout.class);
        dMX02GraffitFragment.tvProgress = (TextView) Utils.findRequiredViewAsType(view, R.id.tvProgress, "field 'tvProgress'", TextView.class);
        dMX02GraffitFragment.progressBar = (LevelProgressBar) Utils.findRequiredViewAsType(view, R.id.progressBar, "field 'progressBar'", LevelProgressBar.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        DMX02GraffitFragment dMX02GraffitFragment = this.target;
        if (dMX02GraffitFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        dMX02GraffitFragment.llTop = null;
        dMX02GraffitFragment.graffitView = null;
        dMX02GraffitFragment.sbColor = null;
        dMX02GraffitFragment.tvColor = null;
        dMX02GraffitFragment.sbBrightness = null;
        dMX02GraffitFragment.tvBrightness = null;
        dMX02GraffitFragment.tvSend = null;
        dMX02GraffitFragment.rlProgressView = null;
        dMX02GraffitFragment.tvProgress = null;
        dMX02GraffitFragment.progressBar = null;
    }
}
