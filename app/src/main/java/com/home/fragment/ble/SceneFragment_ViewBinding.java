package com.home.fragment.ble;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SceneFragment_ViewBinding implements Unbinder {
    private SceneFragment target;

    public SceneFragment_ViewBinding(SceneFragment sceneFragment, View view) {
        this.target = sceneFragment;
        sceneFragment.relativeTab2 = Utils.findRequiredView(view, R.id.relativeTab2, "field 'relativeTab2'");
        sceneFragment.ll_SceneSunrise = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneSunrise, "field 'll_SceneSunrise'", LinearLayout.class);
        sceneFragment.ll_SceneSunset = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneSunset, "field 'll_SceneSunset'", LinearLayout.class);
        sceneFragment.ll_SceneColorful = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneColorful, "field 'll_SceneColorful'", LinearLayout.class);
        sceneFragment.ll_SceneAfternoontea = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneAfternoontea, "field 'll_SceneAfternoontea'", LinearLayout.class);
        sceneFragment.ll_SceneDrivemidge = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneDrivemidge, "field 'll_SceneDrivemidge'", LinearLayout.class);
        sceneFragment.ll_SceneYoga = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneYoga, "field 'll_SceneYoga'", LinearLayout.class);
        sceneFragment.ll_SceneParty = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneParty, "field 'll_SceneParty'", LinearLayout.class);
        sceneFragment.ll_SceneTropical = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneTropical, "field 'll_SceneTropical'", LinearLayout.class);
        sceneFragment.ll_SceneSea = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneSea, "field 'll_SceneSea'", LinearLayout.class);
        sceneFragment.ll_SceneReading = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneReading, "field 'll_SceneReading'", LinearLayout.class);
        sceneFragment.ll_SceneCandlelightdinner = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_SceneCandlelightdinner, "field 'll_SceneCandlelightdinner'", LinearLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        SceneFragment sceneFragment = this.target;
        if (sceneFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        sceneFragment.relativeTab2 = null;
        sceneFragment.ll_SceneSunrise = null;
        sceneFragment.ll_SceneSunset = null;
        sceneFragment.ll_SceneColorful = null;
        sceneFragment.ll_SceneAfternoontea = null;
        sceneFragment.ll_SceneDrivemidge = null;
        sceneFragment.ll_SceneYoga = null;
        sceneFragment.ll_SceneParty = null;
        sceneFragment.ll_SceneTropical = null;
        sceneFragment.ll_SceneSea = null;
        sceneFragment.ll_SceneReading = null;
        sceneFragment.ll_SceneCandlelightdinner = null;
    }
}
