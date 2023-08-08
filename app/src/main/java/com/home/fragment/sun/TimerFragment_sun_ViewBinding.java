package com.home.fragment.sun;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class TimerFragment_sun_ViewBinding implements Unbinder {
    private TimerFragment_sun target;

    public TimerFragment_sun_ViewBinding(TimerFragment_sun timerFragment_sun, View view) {
        this.target = timerFragment_sun;
        timerFragment_sun.gv_sun = (GridView) Utils.findRequiredViewAsType(view, R.id.gv_sun, "field 'gv_sun'", GridView.class);
        timerFragment_sun.sure_sun = (Button) Utils.findRequiredViewAsType(view, R.id.sure_sun, "field 'sure_sun'", Button.class);
        timerFragment_sun.closeTime_sun = (Button) Utils.findRequiredViewAsType(view, R.id.closeTime_sun, "field 'closeTime_sun'", Button.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        TimerFragment_sun timerFragment_sun = this.target;
        if (timerFragment_sun == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        timerFragment_sun.gv_sun = null;
        timerFragment_sun.sure_sun = null;
        timerFragment_sun.closeTime_sun = null;
    }
}
