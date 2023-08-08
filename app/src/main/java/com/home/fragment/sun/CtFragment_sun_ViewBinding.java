package com.home.fragment.sun;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.view.TempControlView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class CtFragment_sun_ViewBinding implements Unbinder {
    private CtFragment_sun target;

    public CtFragment_sun_ViewBinding(CtFragment_sun ctFragment_sun, View view) {
        this.target = ctFragment_sun;
        ctFragment_sun.tempControl = (TempControlView) Utils.findRequiredViewAsType(view, R.id.temp_control, "field 'tempControl'", TempControlView.class);
        ctFragment_sun.seekBarBrightNess_sun = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBrightNess_sun, "field 'seekBarBrightNess_sun'", SeekBar.class);
        ctFragment_sun.textViewBrightNess_sun = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightNess_sun, "field 'textViewBrightNess_sun'", TextView.class);
        ctFragment_sun.ll_whitelight = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_whitelight, "field 'll_whitelight'", LinearLayout.class);
        ctFragment_sun.ll_naturallight = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_naturallight, "field 'll_naturallight'", LinearLayout.class);
        ctFragment_sun.ll_warmlight = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_warmlight, "field 'll_warmlight'", LinearLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        CtFragment_sun ctFragment_sun = this.target;
        if (ctFragment_sun == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        ctFragment_sun.tempControl = null;
        ctFragment_sun.seekBarBrightNess_sun = null;
        ctFragment_sun.textViewBrightNess_sun = null;
        ctFragment_sun.ll_whitelight = null;
        ctFragment_sun.ll_naturallight = null;
        ctFragment_sun.ll_warmlight = null;
    }
}
