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
public class MainActivity_BLE_ViewBinding implements Unbinder {
    private MainActivity_BLE target;

    public MainActivity_BLE_ViewBinding(MainActivity_BLE mainActivity_BLE) {
        this(mainActivity_BLE, mainActivity_BLE.getWindow().getDecorView());
    }

    public MainActivity_BLE_ViewBinding(MainActivity_BLE mainActivity_BLE, View view) {
        this.target = mainActivity_BLE;
        mainActivity_BLE.segmentDm = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentDm, "field 'segmentDm'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentCt = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentCt, "field 'segmentCt'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentRgb = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentRgb, "field 'segmentRgb'", SegmentedRadioGroup.class);
        mainActivity_BLE.rbRgbBright = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbRgbBright, "field 'rbRgbBright'", RadioButton.class);
        mainActivity_BLE.segmentCAR01RgbTop = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentCAR01RgbTop, "field 'segmentCAR01RgbTop'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentCAR01ModeTop = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentCAR01ModeTop, "field 'segmentCAR01ModeTop'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentCAR01CustomTop = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentCAR01CustomTop, "field 'segmentCAR01CustomTop'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentCustomDMX00Top = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentCustomDMX00Top, "field 'segmentCustomDMX00Top'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentCustomDMX01Top = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentCustomDMX01Top, "field 'segmentCustomDMX01Top'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentCustomCAR00 = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentCustomCAR00, "field 'segmentCustomCAR00'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentMusic = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentMusic, "field 'segmentMusic'", SegmentedRadioGroup.class);
        mainActivity_BLE.segmentAisle = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentAisle, "field 'segmentAisle'", SegmentedRadioGroup.class);
        mainActivity_BLE.rlCustomtopInfo = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlCustomtopInfo, "field 'rlCustomtopInfo'", RelativeLayout.class);
        mainActivity_BLE.rlModeTopDMX01 = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlModeTopDMX01, "field 'rlModeTopDMX01'", RelativeLayout.class);
        mainActivity_BLE.rlModeTopDMX00 = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlModeTopDMX00, "field 'rlModeTopDMX00'", RelativeLayout.class);
        mainActivity_BLE.rlModeTopCAR00 = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlModeTopCAR00, "field 'rlModeTopCAR00'", RelativeLayout.class);
        mainActivity_BLE.ivType = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivType, "field 'ivType'", ImageView.class);
        mainActivity_BLE.rgBottom = (RadioGroup) Utils.findRequiredViewAsType(view, R.id.rgBottom, "field 'rgBottom'", RadioGroup.class);
        mainActivity_BLE.rgBottom_car = (RadioGroup) Utils.findRequiredViewAsType(view, R.id.rgBottom_car, "field 'rgBottom_car'", RadioGroup.class);
        mainActivity_BLE.rgBottom_sun = (RadioGroup) Utils.findRequiredViewAsType(view, R.id.rgBottom_sun, "field 'rgBottom_sun'", RadioGroup.class);
        mainActivity_BLE.rgBottom_light = (RadioGroup) Utils.findRequiredViewAsType(view, R.id.rgBottom_light, "field 'rgBottom_light'", RadioGroup.class);
        mainActivity_BLE.rbRGB = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbRGB, "field 'rbRGB'", RadioButton.class);
        mainActivity_BLE.rbScene = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbScene, "field 'rbScene'", RadioButton.class);
        mainActivity_BLE.rbBrightness = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbBrightness, "field 'rbBrightness'", RadioButton.class);
        mainActivity_BLE.rbMode = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbMode, "field 'rbMode'", RadioButton.class);
        mainActivity_BLE.rbCustom = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbCustom, "field 'rbCustom'", RadioButton.class);
        mainActivity_BLE.rbMusic = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbMusic, "field 'rbMusic'", RadioButton.class);
        mainActivity_BLE.rbTimer = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbTimer, "field 'rbTimer'", RadioButton.class);
        mainActivity_BLE.rbAisle = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbAisle, "field 'rbAisle'", RadioButton.class);
        mainActivity_BLE.rbCustomOne = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbCustomOne, "field 'rbCustomOne'", RadioButton.class);
        mainActivity_BLE.rbCustomTwo = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rbCustomTwo, "field 'rbCustomTwo'", RadioButton.class);
        mainActivity_BLE.backTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.backTextView, "field 'backTextView'", TextView.class);
        mainActivity_BLE.onOffButton = (Button) Utils.findRequiredViewAsType(view, R.id.onOffButton, "field 'onOffButton'", Button.class);
        mainActivity_BLE.ivEditColor = (Button) Utils.findRequiredViewAsType(view, R.id.ivEditColor, "field 'ivEditColor'", Button.class);
        mainActivity_BLE.btnChangeColor = (Button) Utils.findRequiredViewAsType(view, R.id.btnChangeColor, "field 'btnChangeColor'", Button.class);
        mainActivity_BLE.btnModeCycle = (Button) Utils.findRequiredViewAsType(view, R.id.btnModeCycle, "field 'btnModeCycle'", Button.class);
        mainActivity_BLE.avtivity_main = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llMenu, "field 'avtivity_main'", LinearLayout.class);
        mainActivity_BLE.left_menu = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.left_menu_content_layout, "field 'left_menu'", LinearLayout.class);
        mainActivity_BLE.right_menu = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.right_menu_frame, "field 'right_menu'", LinearLayout.class);
        mainActivity_BLE.TopItem = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearLayoutTopItem, "field 'TopItem'", LinearLayout.class);
        mainActivity_BLE.shakeView = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.shakeView, "field 'shakeView'", RelativeLayout.class);
        mainActivity_BLE.linearLayoutBottom = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearLayoutBottom, "field 'linearLayoutBottom'", LinearLayout.class);
        mainActivity_BLE.segmentModeSun = (SegmentedRadioGroup) Utils.findRequiredViewAsType(view, R.id.segmentModeSun, "field 'segmentModeSun'", SegmentedRadioGroup.class);
        mainActivity_BLE.textViewCustomTitle_sun = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewCustomTitle_sun, "field 'textViewCustomTitle_sun'", TextView.class);
        mainActivity_BLE.btnTimerAdd = (Button) Utils.findRequiredViewAsType(view, R.id.btnTimerAdd, "field 'btnTimerAdd'", Button.class);
        mainActivity_BLE.ivLeftMenu = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivLeftMenu, "field 'ivLeftMenu'", ImageView.class);
        mainActivity_BLE.textViewConnectCount = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewConnectCount, "field 'textViewConnectCount'", TextView.class);
        mainActivity_BLE.textViewAllDeviceIndicater = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewAllDeviceIndicater, "field 'textViewAllDeviceIndicater'", TextView.class);
        mainActivity_BLE.ivRightMenu = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivRightMenu, "field 'ivRightMenu'", ImageView.class);
        mainActivity_BLE.rlSetting = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlSetting, "field 'rlSetting'", RelativeLayout.class);
        mainActivity_BLE.llCommentRight = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llCommentRight, "field 'llCommentRight'", LinearLayout.class);
        mainActivity_BLE.llBLE00Right = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llBLE00Right, "field 'llBLE00Right'", LinearLayout.class);
        mainActivity_BLE.tvTimerBLE00 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTimerBLE00, "field 'tvTimerBLE00'", TextView.class);
        mainActivity_BLE.tvBtnBLE00_1 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBtnBLE00_1, "field 'tvBtnBLE00_1'", TextView.class);
        mainActivity_BLE.tvBtnBLE00_2 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBtnBLE00_2, "field 'tvBtnBLE00_2'", TextView.class);
        mainActivity_BLE.tvBtnBLE00_3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBtnBLE00_3, "field 'tvBtnBLE00_3'", TextView.class);
        mainActivity_BLE.llCAR00Right = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llCAR00Right, "field 'llCAR00Right'", LinearLayout.class);
        mainActivity_BLE.tv_car00_btn1 = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_car00_btn1, "field 'tv_car00_btn1'", TextView.class);
        mainActivity_BLE.tv_car00_btn2 = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_car00_btn2, "field 'tv_car00_btn2'", TextView.class);
        mainActivity_BLE.tv_car00_btn3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_car00_btn3, "field 'tv_car00_btn3'", TextView.class);
        mainActivity_BLE.tv_car00_btn4 = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_car00_btn4, "field 'tv_car00_btn4'", TextView.class);
        mainActivity_BLE.llCAR01Right = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llCAR01Right, "field 'llCAR01Right'", LinearLayout.class);
        mainActivity_BLE.llDoor = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDoor, "field 'llDoor'", LinearLayout.class);
        mainActivity_BLE.llDirection = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDirection, "field 'llDirection'", LinearLayout.class);
        mainActivity_BLE.llPixNum1 = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llPixNum1, "field 'llPixNum1'", LinearLayout.class);
        mainActivity_BLE.btnPixSet1 = (Button) Utils.findRequiredViewAsType(view, R.id.btnPixSet1, "field 'btnPixSet1'", Button.class);
        mainActivity_BLE.tvPixNum = (TextView) Utils.findRequiredViewAsType(view, R.id.tvPixNum, "field 'tvPixNum'", TextView.class);
        mainActivity_BLE.llPixNum2 = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llPixNum2, "field 'llPixNum2'", LinearLayout.class);
        mainActivity_BLE.btnPixSet2 = (Button) Utils.findRequiredViewAsType(view, R.id.btnPixSet2, "field 'btnPixSet2'", Button.class);
        mainActivity_BLE.tvPixLong = (TextView) Utils.findRequiredViewAsType(view, R.id.tvPixLong, "field 'tvPixLong'", TextView.class);
        mainActivity_BLE.tvPixWidth = (TextView) Utils.findRequiredViewAsType(view, R.id.tvPixWidth, "field 'tvPixWidth'", TextView.class);
        mainActivity_BLE.tvPixHigh = (TextView) Utils.findRequiredViewAsType(view, R.id.tvPixHigh, "field 'tvPixHigh'", TextView.class);
        mainActivity_BLE.btnChipselectCar01OK = (Button) Utils.findRequiredViewAsType(view, R.id.btnChipselectCar01OK, "field 'btnChipselectCar01OK'", Button.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MainActivity_BLE mainActivity_BLE = this.target;
        if (mainActivity_BLE == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        mainActivity_BLE.segmentDm = null;
        mainActivity_BLE.segmentCt = null;
        mainActivity_BLE.segmentRgb = null;
        mainActivity_BLE.rbRgbBright = null;
        mainActivity_BLE.segmentCAR01RgbTop = null;
        mainActivity_BLE.segmentCAR01ModeTop = null;
        mainActivity_BLE.segmentCAR01CustomTop = null;
        mainActivity_BLE.segmentCustomDMX00Top = null;
        mainActivity_BLE.segmentCustomDMX01Top = null;
        mainActivity_BLE.segmentCustomCAR00 = null;
        mainActivity_BLE.segmentMusic = null;
        mainActivity_BLE.segmentAisle = null;
        mainActivity_BLE.rlCustomtopInfo = null;
        mainActivity_BLE.rlModeTopDMX01 = null;
        mainActivity_BLE.rlModeTopDMX00 = null;
        mainActivity_BLE.rlModeTopCAR00 = null;
        mainActivity_BLE.ivType = null;
        mainActivity_BLE.rgBottom = null;
        mainActivity_BLE.rgBottom_car = null;
        mainActivity_BLE.rgBottom_sun = null;
        mainActivity_BLE.rgBottom_light = null;
        mainActivity_BLE.rbRGB = null;
        mainActivity_BLE.rbScene = null;
        mainActivity_BLE.rbBrightness = null;
        mainActivity_BLE.rbMode = null;
        mainActivity_BLE.rbCustom = null;
        mainActivity_BLE.rbMusic = null;
        mainActivity_BLE.rbTimer = null;
        mainActivity_BLE.rbAisle = null;
        mainActivity_BLE.rbCustomOne = null;
        mainActivity_BLE.rbCustomTwo = null;
        mainActivity_BLE.backTextView = null;
        mainActivity_BLE.onOffButton = null;
        mainActivity_BLE.ivEditColor = null;
        mainActivity_BLE.btnChangeColor = null;
        mainActivity_BLE.btnModeCycle = null;
        mainActivity_BLE.avtivity_main = null;
        mainActivity_BLE.left_menu = null;
        mainActivity_BLE.right_menu = null;
        mainActivity_BLE.TopItem = null;
        mainActivity_BLE.shakeView = null;
        mainActivity_BLE.linearLayoutBottom = null;
        mainActivity_BLE.segmentModeSun = null;
        mainActivity_BLE.textViewCustomTitle_sun = null;
        mainActivity_BLE.btnTimerAdd = null;
        mainActivity_BLE.ivLeftMenu = null;
        mainActivity_BLE.textViewConnectCount = null;
        mainActivity_BLE.textViewAllDeviceIndicater = null;
        mainActivity_BLE.ivRightMenu = null;
        mainActivity_BLE.rlSetting = null;
        mainActivity_BLE.llCommentRight = null;
        mainActivity_BLE.llBLE00Right = null;
        mainActivity_BLE.tvTimerBLE00 = null;
        mainActivity_BLE.tvBtnBLE00_1 = null;
        mainActivity_BLE.tvBtnBLE00_2 = null;
        mainActivity_BLE.tvBtnBLE00_3 = null;
        mainActivity_BLE.llCAR00Right = null;
        mainActivity_BLE.tv_car00_btn1 = null;
        mainActivity_BLE.tv_car00_btn2 = null;
        mainActivity_BLE.tv_car00_btn3 = null;
        mainActivity_BLE.tv_car00_btn4 = null;
        mainActivity_BLE.llCAR01Right = null;
        mainActivity_BLE.llDoor = null;
        mainActivity_BLE.llDirection = null;
        mainActivity_BLE.llPixNum1 = null;
        mainActivity_BLE.btnPixSet1 = null;
        mainActivity_BLE.tvPixNum = null;
        mainActivity_BLE.llPixNum2 = null;
        mainActivity_BLE.btnPixSet2 = null;
        mainActivity_BLE.tvPixLong = null;
        mainActivity_BLE.tvPixWidth = null;
        mainActivity_BLE.tvPixHigh = null;
        mainActivity_BLE.btnChipselectCar01OK = null;
    }
}
