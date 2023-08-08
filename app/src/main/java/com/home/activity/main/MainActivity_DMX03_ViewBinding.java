package com.home.activity.main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.view.SegmentedRadioGroup;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MainActivity_DMX03_ViewBinding implements Unbinder {
    private MainActivity_DMX03 target;

    public MainActivity_DMX03_ViewBinding(MainActivity_DMX03 mainActivity_DMX03) {
        this(mainActivity_DMX03, mainActivity_DMX03.getWindow().getDecorView());
    }

    public MainActivity_DMX03_ViewBinding(MainActivity_DMX03 mainActivity_DMX03, View view) {
        this.target = mainActivity_DMX03;
        mainActivity_DMX03.segmentRgb = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentRgb, "field 'segmentRgb'", SegmentedRadioGroup.class);
        mainActivity_DMX03.segmentMusic = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentMusic, "field 'segmentMusic'", SegmentedRadioGroup.class);
        mainActivity_DMX03.rlModeTopDMX00 = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlModeTopDMX00, "field 'rlModeTopDMX00'", RelativeLayout.class);
        mainActivity_DMX03.ivLeftMenu = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivLeftMenu, "field 'ivLeftMenu'", ImageView.class);
        mainActivity_DMX03.ivRightMenu = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivRightMenu, "field 'ivRightMenu'", ImageView.class);
        mainActivity_DMX03.rgBottomDmx03 = (RadioGroup) Utils.findRequiredViewAsType(view, R.id.rgBottomDmx03, "field 'rgBottomDmx03'", RadioGroup.class);
        mainActivity_DMX03.backTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.backTextView, "field 'backTextView'", TextView.class);
        mainActivity_DMX03.tvTopTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTopTitle, "field 'tvTopTitle'", TextView.class);
        mainActivity_DMX03.onOffButton = (Button) Utils.findRequiredViewAsType(view, R.id.onOffButton, "field 'onOffButton'", Button.class);
        mainActivity_DMX03.btnModePlay = (Button) Utils.findRequiredViewAsType(view, R.id.btnModePlay, "field 'btnModePlay'", Button.class);
        mainActivity_DMX03.btnTimerAdd = (Button) Utils.findRequiredViewAsType(view, R.id.btnTimerAdd, "field 'btnTimerAdd'", Button.class);
        mainActivity_DMX03.ivEditColor = (Button) Utils.findRequiredViewAsType(view, R.id.ivEditColor, "field 'ivEditColor'", Button.class);
        mainActivity_DMX03.btnChangeColor = (Button) Utils.findRequiredViewAsType(view, R.id.btnChangeColor, "field 'btnChangeColor'", Button.class);
        mainActivity_DMX03.left_menu = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.left_menu_content_layout, "field 'left_menu'", LinearLayout.class);
        mainActivity_DMX03.right_menu = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.right_menu_frame, "field 'right_menu'", LinearLayout.class);
        mainActivity_DMX03.TopItem = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearLayoutTopItem, "field 'TopItem'", LinearLayout.class);
        mainActivity_DMX03.shakeView = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.shakeView, "field 'shakeView'", RelativeLayout.class);
        mainActivity_DMX03.linearLayoutBottom = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearLayoutBottom, "field 'linearLayoutBottom'", LinearLayout.class);
        mainActivity_DMX03.rlSetting = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlSetting, "field 'rlSetting'", RelativeLayout.class);
        mainActivity_DMX03.llCommentRight = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llCommentRight, "field 'llCommentRight'", LinearLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MainActivity_DMX03 mainActivity_DMX03 = this.target;
        if (mainActivity_DMX03 == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        mainActivity_DMX03.segmentRgb = null;
        mainActivity_DMX03.segmentMusic = null;
        mainActivity_DMX03.rlModeTopDMX00 = null;
        mainActivity_DMX03.ivLeftMenu = null;
        mainActivity_DMX03.ivRightMenu = null;
        mainActivity_DMX03.rgBottomDmx03 = null;
        mainActivity_DMX03.backTextView = null;
        mainActivity_DMX03.tvTopTitle = null;
        mainActivity_DMX03.onOffButton = null;
        mainActivity_DMX03.btnModePlay = null;
        mainActivity_DMX03.btnTimerAdd = null;
        mainActivity_DMX03.ivEditColor = null;
        mainActivity_DMX03.btnChangeColor = null;
        mainActivity_DMX03.left_menu = null;
        mainActivity_DMX03.right_menu = null;
        mainActivity_DMX03.TopItem = null;
        mainActivity_DMX03.shakeView = null;
        mainActivity_DMX03.linearLayoutBottom = null;
        mainActivity_DMX03.rlSetting = null;
        mainActivity_DMX03.llCommentRight = null;
    }
}
