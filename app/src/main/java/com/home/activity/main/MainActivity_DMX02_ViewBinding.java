package com.home.activity.main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.view.SegmentedRadioGroup;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MainActivity_DMX02_ViewBinding implements Unbinder {
    private MainActivity_DMX02 target;

    public MainActivity_DMX02_ViewBinding(MainActivity_DMX02 mainActivity_DMX02) {
        this(mainActivity_DMX02, mainActivity_DMX02.getWindow().getDecorView());
    }

    public MainActivity_DMX02_ViewBinding(MainActivity_DMX02 mainActivity_DMX02, View view) {
        this.target = mainActivity_DMX02;
        mainActivity_DMX02.segmentRgb = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentRgbDmx02, "field 'segmentRgb'", SegmentedRadioGroup.class);
        mainActivity_DMX02.segmentMusic = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentMusicDmx02, "field 'segmentMusic'", SegmentedRadioGroup.class);
        mainActivity_DMX02.ivRightMenu = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivRightMenu, "field 'ivRightMenu'", ImageView.class);
        mainActivity_DMX02.rgBottom = (RadioGroup) Utils.findRequiredViewAsType(view, R.id.rgBottom, "field 'rgBottom'", RadioGroup.class);
        mainActivity_DMX02.rbMode = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbMode, "field 'rbMode'", RadioButton.class);
        mainActivity_DMX02.rbText = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbText, "field 'rbText'", RadioButton.class);
        mainActivity_DMX02.rbGraffiti = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbGraffiti, "field 'rbGraffiti'", RadioButton.class);
        mainActivity_DMX02.rbMusic = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbMusic, "field 'rbMusic'", RadioButton.class);
        mainActivity_DMX02.backTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.backTextView, "field 'backTextView'", TextView.class);
        mainActivity_DMX02.onOffButton = (Button) Utils.findRequiredViewAsType(view, R.id.onOffButton, "field 'onOffButton'", Button.class);
        mainActivity_DMX02.btnChangeColor = (Button) Utils.findRequiredViewAsType(view, R.id.btnChangeColor, "field 'btnChangeColor'", Button.class);
        mainActivity_DMX02.avtivity_main = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llMenu, "field 'avtivity_main'", LinearLayout.class);
        mainActivity_DMX02.left_menu = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.left_menu_content_layout, "field 'left_menu'", LinearLayout.class);
        mainActivity_DMX02.right_menu = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.right_menu_frame, "field 'right_menu'", LinearLayout.class);
        mainActivity_DMX02.TopItem = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearLayoutTopItem, "field 'TopItem'", LinearLayout.class);
        mainActivity_DMX02.linearLayoutBottom = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearLayoutBottom, "field 'linearLayoutBottom'", LinearLayout.class);
        mainActivity_DMX02.rlSetting = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlSetting, "field 'rlSetting'", RelativeLayout.class);
        mainActivity_DMX02.llCommentRight = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llCommentRight, "field 'llCommentRight'", LinearLayout.class);
        mainActivity_DMX02.tvMateriallibrary = (TextView) Utils.findRequiredViewAsType(view, R.id.tvMateriallibrary, "field 'tvMateriallibrary'", TextView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MainActivity_DMX02 mainActivity_DMX02 = this.target;
        if (mainActivity_DMX02 == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        mainActivity_DMX02.segmentRgb = null;
        mainActivity_DMX02.segmentMusic = null;
        mainActivity_DMX02.ivRightMenu = null;
        mainActivity_DMX02.rgBottom = null;
        mainActivity_DMX02.rbMode = null;
        mainActivity_DMX02.rbText = null;
        mainActivity_DMX02.rbGraffiti = null;
        mainActivity_DMX02.rbMusic = null;
        mainActivity_DMX02.backTextView = null;
        mainActivity_DMX02.onOffButton = null;
        mainActivity_DMX02.btnChangeColor = null;
        mainActivity_DMX02.avtivity_main = null;
        mainActivity_DMX02.left_menu = null;
        mainActivity_DMX02.right_menu = null;
        mainActivity_DMX02.TopItem = null;
        mainActivity_DMX02.linearLayoutBottom = null;
        mainActivity_DMX02.rlSetting = null;
        mainActivity_DMX02.llCommentRight = null;
        mainActivity_DMX02.tvMateriallibrary = null;
    }
}
