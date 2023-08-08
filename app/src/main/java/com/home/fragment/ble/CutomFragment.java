package com.home.fragment.ble;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import butterknife.BindView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.uitl.ListUtiles;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.common.view.SegmentedRadioGroup;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleFragment;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.view.BlackWiteSelectView;
import com.home.view.ColorTextView;
import com.home.view.MyColorPicker;
import com.ledlamp.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class CutomFragment extends LedBleFragment {
    private static final int COLOR_DEFALUT = 0;
    private static final String TAG = "CutomFragment";
    private ColorTextView actionView;
    private ImageView backImage;
    private BlackWiteSelectView blackWiteSelectView2;
    private Button buttonSelectColorConfirm;
    @BindView(R.id.changeButtonEight)
    RadioButton changeButtonEight;
    @BindView(R.id.changeButtonFive)
    RadioButton changeButtonFive;
    @BindView(R.id.changeButtonFour)
    RadioButton changeButtonFour;
    @BindView(R.id.changeButtonOne)
    RadioButton changeButtonOne;
    @BindView(R.id.changeButtonSeven)
    RadioButton changeButtonSeven;
    @BindView(R.id.changeButtonSix)
    RadioButton changeButtonSix;
    @BindView(R.id.changeButtonThree)
    RadioButton changeButtonThree;
    @BindView(R.id.changeButtonTwo)
    RadioButton changeButtonTwo;
    private ArrayList<ColorTextView> colorTextViews;
    private int currentSelecColorFromPicker;
    private String diyViewTag;
    private ColorPickerView imageViewPicker2;
    private ImageView iv_switch_select;
    private LinearLayout linearChouse_select;
    private LinearLayout llCoverMode;
    @BindView(R.id.llCustomCAR01BLEKeys)
    LinearLayout llCustomCAR01BLEKeys;
    @BindView(R.id.llCustomCAR01DMXKeys)
    LinearLayout llCustomCAR01DMXKeys;
    private LinearLayout llRing;
    @BindView(R.id.llSegmentedRadioGroupTwo)
    LinearLayout llSegmentedRadioGroupTwo;
    @BindView(R.id.llViewBlocks)
    LinearLayout llViewBlocks;
    @BindView(R.id.llViewBlocksCar01DMX)
    LinearLayout llViewBlocksCar01DMX;
    private MainActivity_BLE mActivity;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private View menuView;
    private MyColorPicker myColor_select;
    private RadioButton rbCAR01CustomBLE;
    private RadioButton rbCAR01CustomDMX;
    private RadioButton rbCustomCAR00ChangeColor;
    private RadioButton rbCustomCAR00Cycle;
    private RadioButton rbCustomDMX01Cycle;
    private RadioButton rbCustomDMX01Forward;
    private RadioButton rbCustomDMX01Reverse;
    @BindView(R.id.seekBarBrightCustom)
    SeekBar seekBarBrightCustom;
    @BindView(R.id.seekBarSpeedCustom)
    SeekBar seekBarSpeedCustom;
    private SegmentedRadioGroup segmentCAR01Top;
    private SegmentedRadioGroup segmentCustomCAR00;
    private SegmentedRadioGroup segmentCustomDMX00Top;
    private SegmentedRadioGroup segmentCustomDMX01Top;
    @BindView(R.id.segmentedRadioGroupOne)
    SegmentedRadioGroup segmentedRadioGroupOne;
    @BindView(R.id.segmentedRadioGroupTwo)
    SegmentedRadioGroup segmentedRadioGroupTwo;
    private SharedPreferences sp;
    private SegmentedRadioGroup srgCover;
    private TextView textGreen_select;
    private TextView textRed_select;
    @BindView(R.id.textViewBrightCustom)
    TextView textViewBrightCustom;
    private TextView textViewRingBrightSC;
    @BindView(R.id.textViewSpeedCustom)
    TextView textViewSpeedCustom;
    private TextView tvBlue_select;
    private Boolean isCAR01DMX = false;
    private int styleOne = -1;
    private int styleTwo = -1;
    private int currentTab = 1;
    private int select_r = 255;
    private int select_g = 255;
    private int select_b = 255;

    @Override // com.home.base.LedBleFragment
    public void initData() {
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = layoutInflater.inflate(R.layout.fragment_custom, viewGroup, false);
        this.menuView = layoutInflater.inflate(R.layout.activity_select_color, viewGroup, false);
        return this.mContentView;
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        if (MainActivity_BLE.getMainActivity() != null) {
            this.mActivity = MainActivity_BLE.getMainActivity();
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-")) {
                SegmentedRadioGroup segmentCustomDMX00Top = MainActivity_BLE.getMainActivity().getSegmentCustomDMX00Top();
                this.segmentCustomDMX00Top = segmentCustomDMX00Top;
                segmentCustomDMX00Top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.CutomFragment.1
                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    }
                });
                this.segmentCustomDMX00Top.findViewById(R.id.rbCustomOne).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.mActivity.setDirection(0, CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                });
                this.segmentCustomDMX00Top.findViewById(R.id.rbCustomTwo).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.mActivity.setDirection(1, CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                });
                this.segmentCustomDMX00Top.findViewById(R.id.rbCustomThree).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.mActivity.setChangeColor(false, false, CutomFragment.this.isCAR01DMX.booleanValue(), CutomFragment.this.getSelectColor());
                    }
                });
                this.segmentCustomDMX00Top.findViewById(R.id.rbCustomFour).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.5
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.mActivity.setCustomCycle(false);
                    }
                });
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                SegmentedRadioGroup segmentCustomDMX01Top = MainActivity_BLE.getMainActivity().getSegmentCustomDMX01Top();
                this.segmentCustomDMX01Top = segmentCustomDMX01Top;
                this.rbCustomDMX01Forward = (RadioButton) segmentCustomDMX01Top.findViewById(R.id.rbCustomDMX01Forward);
                this.rbCustomDMX01Reverse = (RadioButton) this.segmentCustomDMX01Top.findViewById(R.id.rbCustomDMX01Reverse);
                this.rbCustomDMX01Cycle = (RadioButton) this.segmentCustomDMX01Top.findViewById(R.id.rbCustomDMX01Cycle);
                this.segmentCustomDMX01Top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.CutomFragment.6
                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    }
                });
                this.rbCustomDMX01Forward.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.7
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.setDirection(0);
                    }
                });
                this.rbCustomDMX01Reverse.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.8
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.setDirection(1);
                    }
                });
                this.rbCustomDMX01Cycle.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.9
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (CutomFragment.this.mActivity != null) {
                            CutomFragment.this.mActivity.setCirculation();
                        }
                    }
                });
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDLIGHT) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
                SegmentedRadioGroup segmentCustomCAR00 = MainActivity_BLE.getMainActivity().getSegmentCustomCAR00();
                this.segmentCustomCAR00 = segmentCustomCAR00;
                this.rbCustomCAR00ChangeColor = (RadioButton) segmentCustomCAR00.findViewById(R.id.rbCustomCAR00ChangeColor);
                this.rbCustomCAR00Cycle = (RadioButton) this.segmentCustomCAR00.findViewById(R.id.rbCustomCAR00Cycle);
                this.segmentCustomCAR00.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.CutomFragment.10
                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    }
                });
                this.rbCustomCAR00ChangeColor.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.11
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.mActivity.setChangeColor(false, false, CutomFragment.this.isCAR01DMX.booleanValue(), CutomFragment.this.getSelectColor());
                    }
                });
                this.rbCustomCAR00Cycle.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.12
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.mActivity.setCustomCycle(false);
                    }
                });
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                SegmentedRadioGroup segmentCAR01CustomTop = MainActivity_BLE.getMainActivity().getSegmentCAR01CustomTop();
                this.segmentCAR01Top = segmentCAR01CustomTop;
                this.rbCAR01CustomBLE = (RadioButton) segmentCAR01CustomTop.findViewById(R.id.rbCAR01CustomBLE);
                this.rbCAR01CustomDMX = (RadioButton) this.segmentCAR01Top.findViewById(R.id.rbCAR01CustomDMX);
                this.segmentCAR01Top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.CutomFragment.13
                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    }
                });
                this.llCustomCAR01BLEKeys.setVisibility(0);
                this.rbCAR01CustomBLE.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.14
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.isCAR01DMX = false;
                        CutomFragment.this.llViewBlocks.setVisibility(0);
                        CutomFragment.this.llViewBlocksCar01DMX.setVisibility(8);
                        CutomFragment.this.llCustomCAR01BLEKeys.setVisibility(0);
                        CutomFragment.this.llCustomCAR01DMXKeys.setVisibility(8);
                        CutomFragment.this.llSegmentedRadioGroupTwo.setVisibility(8);
                        CutomFragment.this.changeButtonOne.setText(CutomFragment.this.getString(R.string.jump));
                        CutomFragment.this.changeButtonTwo.setText(CutomFragment.this.getString(R.string.breathe));
                        CutomFragment.this.changeButtonThree.setText(CutomFragment.this.getString(R.string.flash));
                        CutomFragment.this.changeButtonFour.setText(CutomFragment.this.getString(R.string.gradient));
                        CutomFragment.this.initColorBlock();
                    }
                });
                this.rbCAR01CustomDMX.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.15
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CutomFragment.this.isCAR01DMX = true;
                        CutomFragment.this.llViewBlocks.setVisibility(8);
                        CutomFragment.this.llViewBlocksCar01DMX.setVisibility(0);
                        CutomFragment.this.llCustomCAR01BLEKeys.setVisibility(8);
                        CutomFragment.this.llCustomCAR01DMXKeys.setVisibility(0);
                        CutomFragment.this.llSegmentedRadioGroupTwo.setVisibility(0);
                        CutomFragment.this.changeButtonOne.setText(CutomFragment.this.getString(R.string.GD));
                        CutomFragment.this.changeButtonTwo.setText(CutomFragment.this.getString(R.string.FD));
                        CutomFragment.this.changeButtonThree.setText(CutomFragment.this.getString(R.string.FW));
                        CutomFragment.this.changeButtonFour.setText(CutomFragment.this.getString(R.string.FS));
                        CutomFragment.this.initColorBlockCar01DMX();
                    }
                });
                initButtonCar01BLEBlock();
                initButtonCar01DMXBlock();
            }
        }
        this.sp = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
        if (MainActivity_BLE.getMainActivity() != null) {
            if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDDMX) || ((MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-") && this.isCAR01DMX.booleanValue()) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi))) {
                this.changeButtonOne.setText(getString(R.string.GD));
                this.changeButtonTwo.setText(getString(R.string.FD));
                this.changeButtonThree.setText(getString(R.string.FW));
                this.changeButtonFour.setText(getString(R.string.FS));
                this.changeButtonEight.setVisibility(0);
                this.changeButtonSeven.setVisibility(0);
                this.changeButtonSix.setVisibility(0);
                this.changeButtonFive.setVisibility(0);
            } else if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDBLE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || ((MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-") && !this.isCAR01DMX.booleanValue()) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDLIGHT))) {
                this.llSegmentedRadioGroupTwo.setVisibility(8);
            }
        }
        this.segmentedRadioGroupOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.CutomFragment.16
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.changeButtonOne == i) {
                    CutomFragment.this.styleOne = 0;
                } else if (R.id.changeButtonTwo == i) {
                    CutomFragment.this.styleOne = 1;
                } else if (R.id.changeButtonThree == i) {
                    CutomFragment.this.styleOne = 2;
                } else if (R.id.changeButtonFour == i) {
                    CutomFragment.this.styleOne = 3;
                }
            }
        });
        this.changeButtonOne.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleOne, true);
            }
        });
        this.changeButtonTwo.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleOne, true);
            }
        });
        this.changeButtonThree.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleOne, true);
            }
        });
        this.changeButtonFour.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.20
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleOne, true);
            }
        });
        this.segmentedRadioGroupTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.CutomFragment.21
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.changeButtonFive == i) {
                    CutomFragment.this.styleTwo = 4;
                } else if (R.id.changeButtonSix == i) {
                    CutomFragment.this.styleTwo = 5;
                } else if (R.id.changeButtonSeven == i) {
                    CutomFragment.this.styleTwo = 6;
                } else if (R.id.changeButtonEight == i) {
                    CutomFragment.this.styleTwo = 7;
                }
            }
        });
        this.changeButtonFive.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.22
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleTwo, false);
            }
        });
        this.changeButtonSix.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.23
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleTwo, false);
            }
        });
        this.changeButtonSeven.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.24
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleTwo, false);
            }
        });
        this.changeButtonEight.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.25
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment cutomFragment = CutomFragment.this;
                cutomFragment.SendCMD(cutomFragment.styleTwo, false);
            }
        });
        this.seekBarSpeedCustom.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.CutomFragment.26
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (CutomFragment.this.textViewSpeedCustom == null || CutomFragment.this.textViewSpeedCustom.getTag() == null) {
                    return;
                }
                if (CutomFragment.this.textViewSpeedCustom.getTag().equals(100)) {
                    if (CutomFragment.this.mActivity != null) {
                        CutomFragment.this.mActivity.setSpeed(100, false, CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                } else if (!CutomFragment.this.textViewSpeedCustom.getTag().equals(1) || CutomFragment.this.mActivity == null) {
                } else {
                    CutomFragment.this.mActivity.setSpeed(1, false, CutomFragment.this.isCAR01DMX.booleanValue());
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i == 0) {
                    if (CutomFragment.this.mActivity != null) {
                        CutomFragment.this.mActivity.setSpeed(1, false, CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                    CutomFragment.this.textViewSpeedCustom.setText(CutomFragment.this.getActivity().getResources().getString(R.string.speed_set, 1));
                    CutomFragment.this.textViewSpeedCustom.setTag(1);
                } else {
                    if (CutomFragment.this.mActivity != null) {
                        CutomFragment.this.mActivity.setSpeed(i, false, CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                    CutomFragment.this.textViewSpeedCustom.setText(CutomFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i)));
                    CutomFragment.this.textViewSpeedCustom.setTag(Integer.valueOf(i));
                }
                if (MainActivity_BLE.sceneBean != null) {
                    Context context = CutomFragment.this.getContext();
                    SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + CutomFragment.TAG + "speed", i);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_BLE.sceneBean + TAG + "speed");
            if (i > 0) {
                this.seekBarSpeedCustom.setProgress(i);
                this.textViewSpeedCustom.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i)));
            } else {
                this.textViewSpeedCustom.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
            }
        }
        this.seekBarBrightCustom.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.CutomFragment.27
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (CutomFragment.this.textViewBrightCustom == null || CutomFragment.this.textViewBrightCustom.getTag() == null) {
                    return;
                }
                if (CutomFragment.this.textViewBrightCustom.getTag().equals(100)) {
                    if (CutomFragment.this.mActivity != null) {
                        CutomFragment.this.mActivity.setBrightNess(100, false, false, CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                } else if (!CutomFragment.this.textViewBrightCustom.getTag().equals(1) || CutomFragment.this.mActivity == null) {
                } else {
                    CutomFragment.this.mActivity.setBrightNess(1, false, false, CutomFragment.this.isCAR01DMX.booleanValue());
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    if (i2 == 0) {
                        if (CutomFragment.this.mActivity != null) {
                            CutomFragment.this.mActivity.setBrightNess(1, false, false, CutomFragment.this.isCAR01DMX.booleanValue());
                        }
                        CutomFragment.this.textViewBrightCustom.setText(CutomFragment.this.getActivity().getResources().getString(R.string.brightness_set, 1));
                        CutomFragment.this.textViewBrightCustom.setTag(1);
                    } else {
                        if (CutomFragment.this.mActivity != null) {
                            CutomFragment.this.mActivity.setBrightNess(i2, false, false, CutomFragment.this.isCAR01DMX.booleanValue());
                        }
                        CutomFragment.this.textViewBrightCustom.setText(CutomFragment.this.getActivity().getResources().getString(R.string.brightness_set, Integer.valueOf(i2)));
                        CutomFragment.this.textViewBrightCustom.setTag(Integer.valueOf(i2));
                    }
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context2 = CutomFragment.this.getContext();
                        SharePersistent.saveInt(context2, MainActivity_BLE.sceneBean + CutomFragment.TAG + "bright", i2);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, MainActivity_BLE.sceneBean + TAG + "bright");
            if (i2 > 0) {
                this.seekBarBrightCustom.setProgress(i2);
                this.textViewBrightCustom.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i2)));
            } else {
                this.textViewBrightCustom.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
            }
        }
        this.buttonSelectColorConfirm = (Button) this.menuView.findViewById(R.id.buttonSelectColorConfirm);
        this.srgCover = (SegmentedRadioGroup) this.menuView.findViewById(R.id.srgCover);
        this.llRing = (LinearLayout) this.menuView.findViewById(R.id.llRing);
        this.llCoverMode = (LinearLayout) this.menuView.findViewById(R.id.llCoverMode);
        this.imageViewPicker2 = (ColorPickerView) this.menuView.findViewById(R.id.imageViewPicker2);
        this.blackWiteSelectView2 = (BlackWiteSelectView) this.menuView.findViewById(R.id.blackWiteSelectView2);
        this.textViewRingBrightSC = (TextView) this.menuView.findViewById(R.id.tvRingBrightnessSC);
        initColorBlock();
        initColorSelecterView();
        ImageView imageView = (ImageView) this.menuView.findViewById(R.id.backImage);
        this.backImage = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.28
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CutomFragment.this.hideColorCover();
            }
        });
    }

    private void initButtonCar01BLEBlock() {
        for (int i = 1; i <= 2; i++) {
            View view = this.mContentView;
            final ColorTextView colorTextView = (ColorTextView) view.findViewWithTag("labelColorCar01BLE" + i);
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.29
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    view2.startAnimation(AnimationUtils.loadAnimation(CutomFragment.this.getActivity(), R.anim.layout_scale));
                    View view3 = CutomFragment.this.mContentView;
                    ColorTextView colorTextView2 = (ColorTextView) view3.findViewWithTag("labelColorCar01BLE1");
                    if (((String) colorTextView2.getTag()).equalsIgnoreCase((String) colorTextView.getTag())) {
                        colorTextView2.setBackground(CutomFragment.this.getResources().getDrawable(R.drawable.color_block_shap_car01_selected));
                    } else {
                        colorTextView2.setBackground(CutomFragment.this.getResources().getDrawable(R.drawable.color_block_shap_car01_normal));
                    }
                    if (((String) colorTextView.getTag()).equalsIgnoreCase("labelColorCar01BLE1")) {
                        CutomFragment.this.mActivity.setChangeColor(false, false, CutomFragment.this.isCAR01DMX.booleanValue(), CutomFragment.this.getSelectColor());
                    } else {
                        CutomFragment.this.mActivity.setCustomCycle(CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                }
            });
        }
    }

    private void initButtonCar01DMXBlock() {
        for (int i = 1; i <= 4; i++) {
            View view = this.mContentView;
            final ColorTextView colorTextView = (ColorTextView) view.findViewWithTag("labelColorCar01DMX" + i);
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.30
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    view2.startAnimation(AnimationUtils.loadAnimation(CutomFragment.this.getActivity(), R.anim.layout_scale));
                    View view3 = CutomFragment.this.mContentView;
                    ColorTextView colorTextView2 = (ColorTextView) view3.findViewWithTag("labelColorCar01DMX1");
                    if (((String) colorTextView2.getTag()).equalsIgnoreCase((String) colorTextView.getTag())) {
                        colorTextView2.setBackground(CutomFragment.this.getResources().getDrawable(R.drawable.color_block_shap_car01_selected));
                    } else {
                        colorTextView2.setBackground(CutomFragment.this.getResources().getDrawable(R.drawable.color_block_shap_car01_normal));
                    }
                    if (((String) colorTextView.getTag()).equalsIgnoreCase("labelColorCar01DMX1")) {
                        CutomFragment.this.mActivity.setDirection(0, CutomFragment.this.isCAR01DMX.booleanValue());
                    } else if (((String) colorTextView.getTag()).equalsIgnoreCase("labelColorCar01DMX2")) {
                        CutomFragment.this.mActivity.setDirection(1, CutomFragment.this.isCAR01DMX.booleanValue());
                    } else if (((String) colorTextView.getTag()).equalsIgnoreCase("labelColorCar01DMX3")) {
                        CutomFragment.this.mActivity.setChangeColor(false, false, CutomFragment.this.isCAR01DMX.booleanValue(), CutomFragment.this.getSelectColor());
                    } else if (((String) colorTextView.getTag()).equalsIgnoreCase("labelColorCar01DMX4")) {
                        CutomFragment.this.mActivity.setCustomCycle(CutomFragment.this.isCAR01DMX.booleanValue());
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDirection(int i) {
        MainActivity_BLE mainActivity_BLE = this.mActivity;
        if (mainActivity_BLE != null) {
            mainActivity_BLE.setDirection(i, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void SendCMD(int i, boolean z) {
        if (getSelectColor().size() > 0 && this.mActivity != null) {
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDLIGHT) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                this.mActivity.setMode(false, false, this.isCAR01DMX.booleanValue(), i);
            } else {
                this.mActivity.setDiy(getSelectColor(), i);
            }
        }
        if (z) {
            if (this.styleTwo >= 4) {
                this.segmentedRadioGroupTwo.clearCheck();
                return;
            }
            return;
        }
        int i2 = this.styleOne;
        if (i2 < 0 || i2 > 3) {
            return;
        }
        this.segmentedRadioGroupOne.clearCheck();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initColorBlock() {
        boolean z;
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, InputDeviceCompat.SOURCE_ANY, -16711681, -65281, -1};
        LinearLayout linearLayout = this.llViewBlocks;
        this.colorTextViews = new ArrayList<>();
        int i = 1;
        while (true) {
            if (i > 16) {
                z = false;
                break;
            }
            if (this.sp.getInt((String) ((ColorTextView) linearLayout.findViewWithTag("labelColor" + i)).getTag(), 0) != 0) {
                z = true;
                break;
            }
            i++;
        }
        for (int i2 = 1; i2 <= 16; i2++) {
            final ColorTextView colorTextView = (ColorTextView) linearLayout.findViewWithTag("labelColor" + i2);
            int i3 = this.sp.getInt((String) colorTextView.getTag(), 0);
            float f = (float) 10;
            ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            if (i3 != 0) {
                shapeDrawable.getPaint().setColor(i3);
                colorTextView.setBackgroundDrawable(shapeDrawable);
                colorTextView.setColor(i3);
                colorTextView.setText("");
            } else if (!z && i2 <= 7) {
                int i4 = i2 - 1;
                shapeDrawable.getPaint().setColor(iArr[i4]);
                colorTextView.setBackgroundDrawable(shapeDrawable);
                colorTextView.setColor(iArr[i4]);
                colorTextView.setText("");
                this.sp.edit().putInt((String) colorTextView.getTag(), iArr[i4]).commit();
            }
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.31
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(CutomFragment.this.getActivity(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    if (color == 0) {
                        CutomFragment.this.showColorCover((ColorTextView) view, false);
                    } else {
                        CutomFragment.this.updateRgbText(Tool.getRGB(color), true);
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.ble.CutomFragment.32
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    CutomFragment.this.sp.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(CutomFragment.this.getActivity().getResources().getDrawable(R.drawable.block_shap_color));
                    colorTextView2.setText("+");
                    return true;
                }
            });
            this.colorTextViews.add(colorTextView);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initColorBlockCar01DMX() {
        boolean z;
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, InputDeviceCompat.SOURCE_ANY, -16711681, -65281, -1};
        LinearLayout linearLayout = this.llViewBlocksCar01DMX;
        this.colorTextViews = new ArrayList<>();
        int i = 1;
        while (true) {
            if (i > 16) {
                z = false;
                break;
            }
            if (this.sp.getInt((String) ((ColorTextView) linearLayout.findViewWithTag("labelColorCar01DMX" + i)).getTag(), 0) != 0) {
                z = true;
                break;
            }
            i++;
        }
        for (int i2 = 1; i2 <= 16; i2++) {
            final ColorTextView colorTextView = (ColorTextView) linearLayout.findViewWithTag("labelColorCar01DMX" + i2);
            int i3 = this.sp.getInt((String) colorTextView.getTag(), 0);
            float f = (float) 10;
            ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            if (i3 != 0) {
                shapeDrawable.getPaint().setColor(i3);
                colorTextView.setBackgroundDrawable(shapeDrawable);
                colorTextView.setColor(i3);
                colorTextView.setText("");
            } else if (!z && i2 <= 7) {
                int i4 = i2 - 1;
                shapeDrawable.getPaint().setColor(iArr[i4]);
                colorTextView.setBackgroundDrawable(shapeDrawable);
                colorTextView.setColor(iArr[i4]);
                colorTextView.setText("");
                this.sp.edit().putInt((String) colorTextView.getTag(), iArr[i4]).commit();
            }
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.33
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(CutomFragment.this.getActivity(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    if (color == 0) {
                        CutomFragment.this.showColorCover((ColorTextView) view, false);
                    } else {
                        CutomFragment.this.updateRgbText(Tool.getRGB(color), true);
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.ble.CutomFragment.34
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    CutomFragment.this.sp.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(CutomFragment.this.getActivity().getResources().getDrawable(R.drawable.block_shap_color));
                    colorTextView2.setText("+");
                    return true;
                }
            });
            this.colorTextViews.add(colorTextView);
        }
    }

    public Drawable getImage(String str) {
        Resources resources = getActivity().getResources();
        return getActivity().getResources().getDrawable(resources.getIdentifier("img_" + str, "drawable", "com.Home.ledble"));
    }

    public void showColorCover(ColorTextView colorTextView, boolean z) {
        this.actionView = colorTextView;
        this.currentSelecColorFromPicker = 0;
        this.srgCover.check(R.id.rbRing);
        if (z) {
            this.srgCover.setVisibility(4);
            this.llRing.setVisibility(0);
            this.llCoverMode.setVisibility(8);
            this.blackWiteSelectView2.setVisibility(0);
        } else {
            this.srgCover.setVisibility(4);
            this.llRing.setVisibility(0);
            this.llCoverMode.setVisibility(8);
            this.blackWiteSelectView2.setVisibility(8);
            this.textViewRingBrightSC.setVisibility(8);
        }
        View view = this.menuView;
        if (view != null && view.getParent() != null) {
            ((ViewGroup) this.menuView.getParent()).removeAllViews();
        }
        PopupWindow popupWindow = new PopupWindow(this.menuView, -1, -1, true);
        this.mPopupWindow = popupWindow;
        popupWindow.showAtLocation(this.mContentView, 80, 0, 0);
    }

    private void initColorSelecterView() {
        this.myColor_select = (MyColorPicker) this.menuView.findViewById(R.id.myColor_select);
        this.linearChouse_select = (LinearLayout) this.menuView.findViewById(R.id.linearChouse_select);
        this.textRed_select = (TextView) this.menuView.findViewById(R.id.textRed_select);
        this.textGreen_select = (TextView) this.menuView.findViewById(R.id.textGreen_select);
        this.tvBlue_select = (TextView) this.menuView.findViewById(R.id.tvBlue_select);
        this.iv_switch_select = (ImageView) this.menuView.findViewById(R.id.iv_switch_select);
        this.myColor_select.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.ble.CutomFragment.35
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                CutomFragment.this.blackWiteSelectView2.setStartColor(i);
                CutomFragment.this.currentSelecColorFromPicker = i;
                CutomFragment.this.updateRgbText(rgb, false);
                CutomFragment.this.select_r = rgb[0];
                CutomFragment.this.select_g = rgb[1];
                CutomFragment.this.select_b = rgb[2];
                CutomFragment.this.textRed_select.setText(CutomFragment.this.getActivity().getString(R.string.red, new Object[]{Integer.valueOf(CutomFragment.this.select_r)}));
                CutomFragment.this.textRed_select.setBackgroundColor(Color.rgb(CutomFragment.this.select_r, 0, 0));
                CutomFragment.this.textGreen_select.setText(CutomFragment.this.getActivity().getString(R.string.green, new Object[]{Integer.valueOf(CutomFragment.this.select_g)}));
                CutomFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, CutomFragment.this.select_g, 0));
                CutomFragment.this.tvBlue_select.setText(CutomFragment.this.getActivity().getString(R.string.blue, new Object[]{Integer.valueOf(CutomFragment.this.select_b)}));
                CutomFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, CutomFragment.this.select_b));
                SharePersistent.saveBrightData(CutomFragment.this.getActivity(), CutomFragment.this.diyViewTag + "bright", CutomFragment.this.diyViewTag + "bright", 32);
            }
        });
        this.iv_switch_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.36
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CutomFragment.this.imageViewPicker2.getVisibility() == 0) {
                    CutomFragment.this.iv_switch_select.setImageResource(R.drawable.bg_collor_picker);
                    CutomFragment.this.imageViewPicker2.setVisibility(8);
                    CutomFragment.this.myColor_select.setVisibility(0);
                    return;
                }
                CutomFragment.this.iv_switch_select.setImageResource(R.drawable.collor_picker);
                CutomFragment.this.myColor_select.setVisibility(8);
                CutomFragment.this.imageViewPicker2.setVisibility(0);
            }
        });
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.select_r, this.select_g, this.select_b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.ble.CutomFragment.37
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                CutomFragment.this.currentSelecColorFromPicker = i;
                CutomFragment.this.blackWiteSelectView2.setStartColor(i);
                CutomFragment.this.updateRgbText(Tool.getRGB(i), false);
                CutomFragment.this.select_r = Color.red(i);
                CutomFragment.this.select_g = Color.green(i);
                CutomFragment.this.select_b = Color.blue(i);
                CutomFragment.this.textRed_select.setBackgroundColor(Color.rgb(CutomFragment.this.select_r, 0, 0));
                CutomFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, CutomFragment.this.select_g, 0));
                CutomFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, CutomFragment.this.select_b));
                CutomFragment.this.textRed_select.setText(CutomFragment.this.getActivity().getString(R.string.red, new Object[]{Integer.valueOf(CutomFragment.this.select_r)}));
                CutomFragment.this.textGreen_select.setText(CutomFragment.this.getActivity().getString(R.string.green, new Object[]{Integer.valueOf(CutomFragment.this.select_g)}));
                CutomFragment.this.tvBlue_select.setText(CutomFragment.this.getActivity().getString(R.string.blue, new Object[]{Integer.valueOf(CutomFragment.this.select_b)}));
                SharePersistent.saveBrightData(CutomFragment.this.getActivity(), CutomFragment.this.diyViewTag + "bright", CutomFragment.this.diyViewTag + "bright", 32);
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.38
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(CutomFragment.this.select_r, CutomFragment.this.select_g, CutomFragment.this.select_b);
                colorPicker.show();
            }
        });
        this.imageViewPicker2.setInitialColor(getResources().getColor(R.color.white));
        this.imageViewPicker2.subscribe(new ColorObserver() { // from class: com.home.fragment.ble.CutomFragment$$ExternalSyntheticLambda0
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                CutomFragment.this.m16x203e5caf(i, z, z2);
            }
        });
        this.blackWiteSelectView2.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.ble.CutomFragment.39
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                if (i2 <= 0) {
                    i2 = -3;
                } else if (i2 >= 100) {
                    i2 = 100;
                }
                if (i2 > 95) {
                    return;
                }
                int i3 = (i2 + 3) / 3;
                CutomFragment.this.textViewRingBrightSC.setText(CutomFragment.this.getActivity().getResources().getString(R.string.brightness_set, Integer.valueOf(i3)));
                SharePersistent.saveBrightData(CutomFragment.this.getActivity(), CutomFragment.this.diyViewTag + "bright", CutomFragment.this.diyViewTag + "bright", i3);
                if (CutomFragment.this.mActivity != null) {
                    CutomFragment.this.mActivity.setBrightNess(i3, false, false, CutomFragment.this.isCAR01DMX.booleanValue());
                }
            }
        });
        View findViewById = this.menuView.findViewById(R.id.viewColors);
        ArrayList arrayList = new ArrayList();
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, -1, InputDeviceCompat.SOURCE_ANY, -65281};
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(iArr[0]), Double.valueOf(0.0d));
        hashMap.put(Integer.valueOf(iArr[1]), Double.valueOf(1.0471975511965976d));
        hashMap.put(Integer.valueOf(iArr[2]), Double.valueOf(2.0943951023931953d));
        hashMap.put(Integer.valueOf(iArr[3]), Double.valueOf(3.141592653589793d));
        hashMap.put(Integer.valueOf(iArr[4]), Double.valueOf(4.1887902047863905d));
        hashMap.put(Integer.valueOf(iArr[5]), Double.valueOf(5.235987755982989d));
        for (int i = 1; i <= 6; i++) {
            View findViewWithTag = findViewById.findViewWithTag("viewColor" + i);
            findViewWithTag.setTag(Integer.valueOf(iArr[i + (-1)]));
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.40
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(CutomFragment.this.getActivity(), R.anim.layout_scale));
                    int intValue = ((Integer) view.getTag()).intValue();
                    CutomFragment.this.currentSelecColorFromPicker = intValue;
                    CutomFragment.this.blackWiteSelectView2.setStartColor(intValue);
                    CutomFragment.this.imageViewPicker2.setInitialColor(intValue);
                    CutomFragment.this.updateRgbText(Tool.getRGB(intValue), true);
                    int[] rgb = Tool.getRGB(intValue);
                    CutomFragment.this.select_r = rgb[0];
                    CutomFragment.this.select_g = rgb[1];
                    CutomFragment.this.select_b = rgb[2];
                    CutomFragment.this.textRed_select.setText(CutomFragment.this.getActivity().getString(R.string.red, new Object[]{Integer.valueOf(CutomFragment.this.select_r)}));
                    CutomFragment.this.textRed_select.setBackgroundColor(Color.rgb(CutomFragment.this.select_r, 0, 0));
                    CutomFragment.this.textGreen_select.setText(CutomFragment.this.getActivity().getString(R.string.green, new Object[]{Integer.valueOf(CutomFragment.this.select_g)}));
                    CutomFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, CutomFragment.this.select_g, 0));
                    CutomFragment.this.tvBlue_select.setText(CutomFragment.this.getActivity().getString(R.string.blue, new Object[]{Integer.valueOf(CutomFragment.this.select_b)}));
                    CutomFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, CutomFragment.this.select_b));
                }
            });
            arrayList.add(findViewWithTag);
        }
        this.buttonSelectColorConfirm.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.CutomFragment.41
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CutomFragment.this.currentSelecColorFromPicker != 0 && CutomFragment.this.currentTab == 1) {
                    float f = 10;
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                    shapeDrawable.getPaint().setColor(CutomFragment.this.currentSelecColorFromPicker);
                    shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                    CutomFragment.this.actionView.setColor(CutomFragment.this.currentSelecColorFromPicker);
                    CutomFragment.this.sp.edit().putInt((String) CutomFragment.this.actionView.getTag(), CutomFragment.this.currentSelecColorFromPicker).commit();
                    CutomFragment.this.actionView.setBackgroundDrawable(shapeDrawable);
                    CutomFragment.this.actionView.setText("");
                    Log.e(CutomFragment.TAG, "currentSelecColorFromPicker=" + CutomFragment.this.currentSelecColorFromPicker);
                }
                CutomFragment.this.hideColorCover();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initColorSelecterView$0$com-home-fragment-ble-CutomFragment  reason: not valid java name */
    public /* synthetic */ void m16x203e5caf(int i, boolean z, boolean z2) {
        if (z) {
            this.blackWiteSelectView2.setStartColor(i);
            this.currentSelecColorFromPicker = i;
            int[] rgb = Tool.getRGB(i);
            updateRgbText(rgb, false);
            this.select_r = rgb[0];
            this.select_g = rgb[1];
            this.select_b = rgb[2];
            this.textRed_select.setText(getActivity().getString(R.string.red, new Object[]{Integer.valueOf(this.select_r)}));
            this.textRed_select.setBackgroundColor(Color.rgb(this.select_r, 0, 0));
            this.textGreen_select.setText(getActivity().getString(R.string.green, new Object[]{Integer.valueOf(this.select_g)}));
            this.textGreen_select.setBackgroundColor(Color.rgb(0, this.select_g, 0));
            this.tvBlue_select.setText(getActivity().getString(R.string.blue, new Object[]{Integer.valueOf(this.select_b)}));
            this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, this.select_b));
            SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright", this.diyViewTag + "bright", 32);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideColorCover() {
        this.mPopupWindow.dismiss();
    }

    public void updateRgbText(int[] iArr, boolean z) {
        MainActivity_BLE mainActivity_BLE = this.mActivity;
        if (mainActivity_BLE != null) {
            mainActivity_BLE.setRgb(iArr[0], iArr[1], iArr[2], !z, this.isCAR01DMX.booleanValue(), false, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<MyColor> getSelectColor() {
        ArrayList<MyColor> arrayList = new ArrayList<>();
        if (!ListUtiles.isEmpty(this.colorTextViews)) {
            Iterator<ColorTextView> it = this.colorTextViews.iterator();
            while (it.hasNext()) {
                ColorTextView next = it.next();
                if (next.getColor() != 0) {
                    int[] rgb = Tool.getRGB(next.getColor());
                    arrayList.add(new MyColor(rgb[0], rgb[1], rgb[2]));
                }
            }
        }
        return arrayList;
    }
}
