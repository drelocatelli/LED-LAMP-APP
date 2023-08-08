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
import android.os.Handler;
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
import androidx.exifinterface.media.ExifInterface;
import butterknife.BindView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.common.view.SegmentedRadioGroup;
import com.common.view.toast.bamtoast.BamToast;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleFragment;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.home.view.BlackWiteSelectView;
import com.home.view.ColorTextView;
import com.home.view.MyColorPicker;
import com.home.view.MyColorPickerImageView;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.BuildConfig;
import com.ledlamp.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class RgbFragment extends LedBleFragment {
    private static final int COLOR_DEFALUT = 0;
    private static final String TAG = "RgbFragment";
    private ColorTextView actionView;
    private ImageView backImage;
    @BindView(R.id.blackWiteSelectView)
    BlackWiteSelectView blackWiteSelectView;
    private BlackWiteSelectView blackWiteSelectView2;
    private int blueBrightNessValue;
    private int brightnessValue;
    private Button buttonSelectColorConfirm;
    private int currentSelecColorFromPicker;
    private String diyViewTag;
    private int greenBrightNessValue;
    @BindView(R.id.imageViewPicker)
    ColorPickerView imageViewPicker;
    private ColorPickerView imageViewPicker2;
    @BindView(R.id.iv_switch)
    ImageView iv_switch;
    private ImageView iv_switch_select;
    @BindView(R.id.linearChouse)
    LinearLayout linearChouse;
    private LinearLayout linearChouse_select;
    private LinearLayout llCoverMode;
    @BindView(R.id.llDiyColor)
    LinearLayout llDiyColor;
    @BindView(R.id.llDiyColorCar01DMX)
    LinearLayout llDiyColorCar01DMX;
    private LinearLayout llRing;
    private LinearLayout llSeekBarModeSC;
    private LinearLayout llVoicecontrol;
    private LinearLayout ll_breathe;
    private LinearLayout ll_flash;
    private LinearLayout ll_gradient;
    private LinearLayout ll_jump;
    private MainActivity_BLE mActivity;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private View menuView;
    @BindView(R.id.myColor)
    MyColorPicker myColor;
    private MyColorPicker myColor_select;
    @BindView(R.id.pikerImageView)
    MyColorPickerImageView pikerImageView;
    @BindView(R.id.pikerImageViewDim)
    MyColorPickerImageView pikerImageViewDim;
    private RadioButton rbCAR01RgbBLE;
    private RadioButton rbCAR01RgbDMX;
    private RadioButton rbCAR01RgbLED;
    private int redBrightNessValue;
    @BindView(R.id.relativeTab1)
    View relativeTab1;
    @BindView(R.id.relativeTab2)
    View relativeTab2;
    @BindView(R.id.relativeTab3)
    View relativeTab3;
    @BindView(R.id.relativeTabBN)
    View relativeTabBN;
    @BindView(R.id.seekBarBlueBrightNess)
    SeekBar seekBarBlueBrightNess;
    private SeekBar seekBarBrightBarSC;
    @BindView(R.id.seekBarBrightNessCT)
    SeekBar seekBarBrightNessCT;
    @BindView(R.id.seekBarGreenBrightNess)
    SeekBar seekBarGreenBrightNess;
    private SeekBar seekBarModeSC;
    @BindView(R.id.seekBarRedBrightNess)
    SeekBar seekBarRedBrightNess;
    private SeekBar seekBarSensitivitySC;
    private SeekBar seekBarSpeedBarSC;
    private SegmentedRadioGroup segmentCAR01Top;
    private SegmentedRadioGroup segmentedRadioGroup;
    private SharedPreferences sharedPreferences;
    private SegmentedRadioGroup srgCover;
    private SegmentedRadioGroup srgCoverStage;
    @BindView(R.id.textGreen)
    TextView textGreen;
    private TextView textGreen_select;
    @BindView(R.id.textRed)
    TextView textRed;
    private TextView textRed_select;
    @BindView(R.id.textViewBrightNessCT)
    TextView textViewBrightNessCT;
    @BindView(R.id.textViewBrightNessDim)
    TextView textViewBrightNessDim;
    private TextView textViewBrightSC;
    private TextView textViewModeSC;
    private TextView textViewRingBrightSC;
    private TextView textViewSensitivitySC;
    private TextView textViewSpeedSC;
    @BindView(R.id.textViewWarmCool)
    TextView textViewWarmCool;
    @BindView(R.id.tvBlue)
    TextView tvBlue;
    private TextView tvBlue_select;
    @BindView(R.id.tvBrightness)
    TextView tvBrightness;
    @BindView(R.id.tvBrightness1)
    TextView tvBrightness1;
    @BindView(R.id.tvBrightness2)
    TextView tvBrightness2;
    @BindView(R.id.tvBrightness3)
    TextView tvBrightness3;
    private TextView tvCurrentMode;
    private WheelPicker wheelPicker_tang;
    private List<String> listName = new ArrayList();
    private List<String> listNubmer = new ArrayList();
    private Boolean isCAR01DMX = false;
    private Boolean isCAR01LED = true;
    private int VCTAG = 3000;
    private int r = 255;
    private int g = 255;
    private int b = 255;
    private int select_r = 255;
    private int select_g = 255;
    private int select_b = 255;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = layoutInflater.inflate(R.layout.fragment_rgb, viewGroup, false);
        this.menuView = layoutInflater.inflate(R.layout.activity_select_color, viewGroup, false);
        return this.mContentView;
    }

    public void setActive(String str) {
        if (MainActivity_BLE.getMainActivity() != null && MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART) && isAdded()) {
            if (str.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST) || str.equalsIgnoreCase("BW")) {
                this.segmentedRadioGroup.setVisibility(8);
                this.relativeTab1.setVisibility(8);
                this.relativeTab2.setVisibility(8);
                this.relativeTab3.setVisibility(0);
                this.relativeTabBN.setVisibility(8);
                return;
            }
            this.segmentedRadioGroup.setVisibility(0);
            this.relativeTab2.setVisibility(8);
            this.relativeTabBN.setVisibility(8);
            if (this.relativeTab3.getVisibility() == 0) {
                this.relativeTab1.setVisibility(8);
                this.relativeTab3.setVisibility(0);
                this.segmentedRadioGroup.check(R.id.rbRgbDIM);
                return;
            }
            this.relativeTab1.setVisibility(0);
            this.relativeTab3.setVisibility(8);
            this.segmentedRadioGroup.check(R.id.rbRgbRing);
        }
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        if (MainActivity_BLE.getMainActivity() != null) {
            MainActivity_BLE mainActivity = MainActivity_BLE.getMainActivity();
            this.mActivity = mainActivity;
            SegmentedRadioGroup segmentRgb = mainActivity.getSegmentRgb();
            this.segmentedRadioGroup = segmentRgb;
            segmentRgb.check(R.id.rbRgbRing);
        }
        if (MainActivity_BLE.getMainActivity() != null) {
            Log.e(TAG, "MainActivity_BLE.getSceneBean() =" + MainActivity_BLE.getSceneBean());
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                getActivity().findViewById(R.id.rbRgbBright).setVisibility(8);
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-01-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-")) {
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
                getActivity().findViewById(R.id.rbRgbBright).setVisibility(8);
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                SegmentedRadioGroup segmentCAR01RgbTop = MainActivity_BLE.getMainActivity().getSegmentCAR01RgbTop();
                this.segmentCAR01Top = segmentCAR01RgbTop;
                this.rbCAR01RgbBLE = (RadioButton) segmentCAR01RgbTop.findViewById(R.id.rbCAR01RgbBLE);
                this.rbCAR01RgbLED = (RadioButton) this.segmentCAR01Top.findViewById(R.id.rbCAR01RgbLED);
                this.rbCAR01RgbDMX = (RadioButton) this.segmentCAR01Top.findViewById(R.id.rbCAR01RgbDMX);
                this.rbCAR01RgbLED.setChecked(true);
                this.rbCAR01RgbBLE.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        RgbFragment.this.isCAR01DMX = false;
                        RgbFragment.this.isCAR01LED = false;
                        RgbFragment.this.llDiyColor.setVisibility(0);
                        RgbFragment.this.llDiyColorCar01DMX.setVisibility(8);
                    }
                });
                this.rbCAR01RgbLED.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.2
                    @Override // android.view.View.OnClickListenerF
                    public void onClick(View view) {
                        RgbFragment.this.isCAR01DMX = false;
                        RgbFragment.this.isCAR01LED = true;
                        RgbFragment.this.llDiyColor.setVisibility(8);
                        RgbFragment.this.llDiyColorCar01DMX.setVisibility(0);
                    }
                });
                this.rbCAR01RgbDMX.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        RgbFragment.this.isCAR01DMX = true;
                        RgbFragment.this.isCAR01LED = false;
                        RgbFragment.this.llDiyColor.setVisibility(8);
                        RgbFragment.this.llDiyColorCar01DMX.setVisibility(0);
                    }
                });
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
                getActivity().findViewById(R.id.rbRgbBright).setVisibility(8);
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDLIGHT)) {
                getActivity().findViewById(R.id.rbRgbBright).setVisibility(8);
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
                getActivity().findViewById(R.id.rbRgbBright).setVisibility(8);
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
            }
        }
        if (this.mActivity != null) {
            this.sharedPreferences = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
        }
        SegmentedRadioGroup segmentedRadioGroup = this.segmentedRadioGroup;
        if (segmentedRadioGroup != null) {
            segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.RgbFragment.4
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (R.id.rbRgbRing == i) {
                        RgbFragment.this.relativeTab1.setVisibility(0);
                        RgbFragment.this.relativeTab2.setVisibility(8);
                        RgbFragment.this.relativeTab3.setVisibility(8);
                        RgbFragment.this.relativeTabBN.setVisibility(8);
                    } else if (R.id.rbRgbCT == i) {
                        RgbFragment.this.relativeTab2.setVisibility(0);
                        RgbFragment.this.relativeTab1.setVisibility(8);
                        RgbFragment.this.relativeTab3.setVisibility(8);
                        RgbFragment.this.relativeTabBN.setVisibility(8);
                    } else if (R.id.rbRgbBright == i) {
                        RgbFragment.this.relativeTab3.setVisibility(8);
                        RgbFragment.this.relativeTab1.setVisibility(8);
                        RgbFragment.this.relativeTab2.setVisibility(8);
                        RgbFragment.this.relativeTabBN.setVisibility(0);
                    } else if (R.id.rbRgbDIM == i) {
                        if (RgbFragment.this.relativeTab3 != null) {
                            RgbFragment.this.relativeTab3.setVisibility(0);
                        }
                        RgbFragment.this.relativeTab1.setVisibility(8);
                        RgbFragment.this.relativeTab2.setVisibility(8);
                        RgbFragment.this.relativeTabBN.setVisibility(8);
                    }
                }
            });
        }
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.r, this.g, this.b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.ble.RgbFragment.5
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                RgbFragment.this.r = Color.red(i);
                RgbFragment.this.g = Color.green(i);
                RgbFragment.this.b = Color.blue(i);
                RgbFragment.this.textRed.setBackgroundColor(Color.rgb(RgbFragment.this.r, 0, 0));
                RgbFragment.this.textGreen.setBackgroundColor(Color.rgb(0, RgbFragment.this.g, 0));
                RgbFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, RgbFragment.this.b));
                RgbFragment.this.textRed.setText(RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(RgbFragment.this.r)));
                RgbFragment.this.textGreen.setText(RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(RgbFragment.this.g)));
                RgbFragment.this.tvBlue.setText(RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(RgbFragment.this.b)));
                RgbFragment.this.updateRgbText(Tool.getRGB(i), true);
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(RgbFragment.this.r, RgbFragment.this.g, RgbFragment.this.b);
                colorPicker.show();
            }
        });
        this.imageViewPicker.setInitialColor(getResources().getColor(R.color.white));
        this.imageViewPicker.subscribe(new ColorObserver() { // from class: com.home.fragment.ble.RgbFragment$$ExternalSyntheticLambda0
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                RgbFragment.this.m20lambda$initData$0$comhomefragmentbleRgbFragment(i, z, z2);
            }
        });
        this.myColor.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.ble.RgbFragment.7
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                RgbFragment.this.blackWiteSelectView.setStartColor(i);
                RgbFragment.this.updateRgbText(rgb, true);
                RgbFragment.this.r = rgb[0];
                RgbFragment.this.g = rgb[1];
                RgbFragment.this.b = rgb[2];
                RgbFragment.this.textRed.setText(RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(RgbFragment.this.r)));
                RgbFragment.this.textRed.setBackgroundColor(Color.rgb(RgbFragment.this.r, 0, 0));
                RgbFragment.this.textGreen.setText(RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(RgbFragment.this.g)));
                RgbFragment.this.textGreen.setBackgroundColor(Color.rgb(0, RgbFragment.this.g, 0));
                RgbFragment.this.tvBlue.setText(RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(RgbFragment.this.b)));
                RgbFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, RgbFragment.this.b));
            }
        });
        this.iv_switch.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (RgbFragment.this.imageViewPicker.getVisibility() == 0) {
                    RgbFragment.this.iv_switch.setImageResource(R.drawable.bg_collor_picker);
                    RgbFragment.this.imageViewPicker.setVisibility(8);
                    RgbFragment.this.myColor.setVisibility(0);
                    return;
                }
                RgbFragment.this.iv_switch.setImageResource(R.drawable.collor_picker);
                RgbFragment.this.myColor.setVisibility(8);
                RgbFragment.this.imageViewPicker.setVisibility(0);
            }
        });
        this.blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.ble.RgbFragment.9
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                if (i2 <= 0) {
                    i2 = 1;
                } else if (i2 >= 100) {
                    i2 = 100;
                }
                if (RgbFragment.this.tvBrightness != null) {
                    RgbFragment.this.tvBrightness.setText(RgbFragment.this.getContext().getResources().getString(R.string.brightness_set, String.valueOf(i2)));
                }
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setBrightNess(i2, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                }
                if (MainActivity_BLE.sceneBean != null) {
                    Context context = RgbFragment.this.getContext();
                    SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + RgbFragment.TAG, i2);
                }
            }
        });
        this.blackWiteSelectView.setProgress(100);
        this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
        if (MainActivity_BLE.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_BLE.sceneBean + TAG);
            if (i > 0) {
                this.blackWiteSelectView.setProgress(i);
                this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            }
        }
        this.pikerImageView.setInnerCircle(0.459f);
        this.pikerImageView.setOnTouchPixListener(new MyColorPickerImageView.OnTouchPixListener() { // from class: com.home.fragment.ble.RgbFragment.10
            @Override // com.home.view.MyColorPickerImageView.OnTouchPixListener
            public void onColorSelect(int i2, float f) {
                int i3 = (int) ((f / 360.0f) * 100.0f);
                int i4 = 100 - i3;
                RgbFragment.this.textViewWarmCool.setText(RgbFragment.this.getActivity().getString(R.string.cool_warm, new Object[]{String.valueOf(i3), String.valueOf(i4)}));
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setCT(i4, 100 - i4);
                }
            }
        });
        this.seekBarBrightNessCT.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.RgbFragment.11
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (i2 == 0) {
                    RgbFragment.this.textViewBrightNessCT.setText(RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(1)));
                    seekBar.setProgress(1);
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setBrightNess(1, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                        return;
                    }
                    return;
                }
                RgbFragment.this.textViewBrightNessCT.setText(RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(i2)));
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setBrightNess(i2, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                }
            }
        });
        this.seekBarRedBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.RgbFragment.12
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    Log.e(RgbFragment.TAG, "fromUser=" + z);
                    RgbFragment.this.redBrightNessValue = i2;
                    if (i2 < 0 || i2 > 100) {
                        return;
                    }
                    RgbFragment.this.tvBrightness1.setText(Integer.toString(i2));
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setSmartBrightness(1, i2);
                    }
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context2 = RgbFragment.this.getContext();
                        SharePersistent.saveInt(context2, MainActivity_BLE.sceneBean + RgbFragment.TAG + "red-aisle", i2);
                    }
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (RgbFragment.this.redBrightNessValue < 0 || RgbFragment.this.redBrightNessValue > 100) {
                    return;
                }
                RgbFragment.this.tvBrightness1.setText(Integer.toString(RgbFragment.this.redBrightNessValue));
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setSmartBrightnessNoInterval(1, RgbFragment.this.redBrightNessValue);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, MainActivity_BLE.sceneBean + TAG + "red-aisle");
            if (i2 >= 0) {
                this.seekBarRedBrightNess.setProgress(i2);
                this.tvBrightness1.setText(String.valueOf(i2));
            } else {
                this.seekBarRedBrightNess.setProgress(50);
                this.tvBrightness1.setText(String.valueOf(50));
            }
        }
        this.seekBarGreenBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.RgbFragment.13
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    RgbFragment.this.greenBrightNessValue = i3;
                    if (i3 < 0 || i3 > 100) {
                        return;
                    }
                    RgbFragment.this.tvBrightness2.setText(Integer.toString(i3));
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setSmartBrightness(2, i3);
                    }
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context3 = RgbFragment.this.getContext();
                        SharePersistent.saveInt(context3, MainActivity_BLE.sceneBean + RgbFragment.TAG + "green-aisle", i3);
                    }
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (RgbFragment.this.greenBrightNessValue < 0 || RgbFragment.this.greenBrightNessValue > 100) {
                    return;
                }
                RgbFragment.this.tvBrightness2.setText(Integer.toString(RgbFragment.this.greenBrightNessValue));
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setSmartBrightnessNoInterval(2, RgbFragment.this.greenBrightNessValue);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context3 = getContext();
            int i3 = SharePersistent.getInt(context3, MainActivity_BLE.sceneBean + TAG + "green-aisle");
            if (i3 >= 0) {
                this.seekBarGreenBrightNess.setProgress(i3);
                this.tvBrightness2.setText(String.valueOf(i3));
            } else {
                this.seekBarGreenBrightNess.setProgress(50);
                this.tvBrightness2.setText(String.valueOf(50));
            }
        }
        this.seekBarBlueBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.RgbFragment.14
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i4, boolean z) {
                if (z) {
                    RgbFragment.this.blueBrightNessValue = i4;
                    if (i4 < 0 || i4 > 100) {
                        return;
                    }
                    RgbFragment.this.tvBrightness3.setText(Integer.toString(i4));
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setSmartBrightness(3, i4);
                    }
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context4 = RgbFragment.this.getContext();
                        SharePersistent.saveInt(context4, MainActivity_BLE.sceneBean + RgbFragment.TAG + "blue-aisle", i4);
                    }
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (RgbFragment.this.blueBrightNessValue < 0 || RgbFragment.this.blueBrightNessValue > 100) {
                    return;
                }
                RgbFragment.this.tvBrightness3.setText(Integer.toString(RgbFragment.this.blueBrightNessValue));
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setSmartBrightnessNoInterval(3, RgbFragment.this.blueBrightNessValue);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context4 = getContext();
            int i4 = SharePersistent.getInt(context4, MainActivity_BLE.sceneBean + TAG + "blue-aisle");
            if (i4 >= 0) {
                this.seekBarBlueBrightNess.setProgress(i4);
                this.tvBrightness3.setText(String.valueOf(i4));
            } else {
                this.seekBarBlueBrightNess.setProgress(50);
                this.tvBrightness3.setText(String.valueOf(50));
            }
        }
        this.pikerImageViewDim.setInnerCircle(0.25f);
        this.pikerImageViewDim.setOnTouchPixListener(new MyColorPickerImageView.OnTouchPixListener() { // from class: com.home.fragment.ble.RgbFragment.15
            @Override // com.home.view.MyColorPickerImageView.OnTouchPixListener
            public void onColorSelect(int i5, float f) {
                int i6 = (int) ((f / 360.0f) * 100.0f);
                TextView textView = RgbFragment.this.textViewBrightNessDim;
                textView.setText(RgbFragment.this.getActivity().getResources().getString(R.string.brightness) + ":" + (i6 + "%"));
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setDim(i6);
                }
            }
        });
        this.myColor_select = (MyColorPicker) this.menuView.findViewById(R.id.myColor_select);
        this.linearChouse_select = (LinearLayout) this.menuView.findViewById(R.id.linearChouse_select);
        this.textRed_select = (TextView) this.menuView.findViewById(R.id.textRed_select);
        this.textGreen_select = (TextView) this.menuView.findViewById(R.id.textGreen_select);
        this.tvBlue_select = (TextView) this.menuView.findViewById(R.id.tvBlue_select);
        this.iv_switch_select = (ImageView) this.menuView.findViewById(R.id.iv_switch_select);
        SegmentedRadioGroup segmentedRadioGroup2 = (SegmentedRadioGroup) this.menuView.findViewById(R.id.srgCover);
        this.srgCover = segmentedRadioGroup2;
        segmentedRadioGroup2.check(R.id.rbRing);
        this.srgCover.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.RgbFragment.16
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i5) {
                if (R.id.rbRing == i5) {
                    RgbFragment.this.llRing.setVisibility(0);
                    RgbFragment.this.llCoverMode.setVisibility(8);
                    RgbFragment.this.llVoicecontrol.setVisibility(8);
                } else if (R.id.rbModle == i5) {
                    RgbFragment.this.llRing.setVisibility(8);
                    RgbFragment.this.llCoverMode.setVisibility(0);
                    RgbFragment.this.llVoicecontrol.setVisibility(8);
                } else if (R.id.rbVoiceControl == i5) {
                    RgbFragment.this.llRing.setVisibility(8);
                    RgbFragment.this.llCoverMode.setVisibility(8);
                    RgbFragment.this.llVoicecontrol.setVisibility(0);
                }
            }
        });
        SegmentedRadioGroup segmentedRadioGroup3 = (SegmentedRadioGroup) this.menuView.findViewById(R.id.srgCoverStage);
        this.srgCoverStage = segmentedRadioGroup3;
        segmentedRadioGroup3.check(R.id.rbRingStage);
        this.srgCoverStage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.RgbFragment.17
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i5) {
                if (R.id.rbRingStage == i5) {
                    RgbFragment.this.llRing.setVisibility(0);
                    RgbFragment.this.llCoverMode.setVisibility(8);
                    RgbFragment.this.llVoicecontrol.setVisibility(8);
                } else if (R.id.rbModleStage == i5) {
                    RgbFragment.this.llRing.setVisibility(8);
                    RgbFragment.this.llCoverMode.setVisibility(0);
                    RgbFragment.this.llVoicecontrol.setVisibility(8);
                }
            }
        });
        ImageView imageView = (ImageView) this.menuView.findViewById(R.id.backImage);
        this.backImage = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RgbFragment.this.hideColorCover();
            }
        });
        initSingColorView();
        initDiyColorBlock();
        initDiyColorCar01DMXBlock();
        initDiyRingView();
        initDiyModeView();
        initDiyVoiceControlView();
        initConfirmButton();
        setActive(SharePersistent.getSmartModeString(this.mActivity, CommonConstant.SELECT_MODE_SMART_STRING));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initData$0$com-home-fragment-ble-RgbFragment  reason: not valid java name */
    public /* synthetic */ void m20lambda$initData$0$comhomefragmentbleRgbFragment(int i, boolean z, boolean z2) {
        int[] rgb = Tool.getRGB(i);
        this.blackWiteSelectView.setStartColor(i);
        this.r = rgb[0];
        this.g = rgb[1];
        this.b = rgb[2];
        this.textRed.setText(getResources().getString(R.string.red, Integer.valueOf(this.r)));
        this.textRed.setBackgroundColor(Color.rgb(this.r, 0, 0));
        this.textGreen.setText(getResources().getString(R.string.green, Integer.valueOf(this.g)));
        this.textGreen.setBackgroundColor(Color.rgb(0, this.g, 0));
        this.tvBlue.setText(getResources().getString(R.string.blue, Integer.valueOf(this.b)));
        this.tvBlue.setBackgroundColor(Color.rgb(0, 0, this.b));
        if (z) {
            updateRgbText(Tool.getRGB(i), true);
        }
    }

    public boolean checkRelativeTab3() {
        return this.relativeTab3.getVisibility() == 0;
    }

    public boolean checkRelativeTabDMXAisle() {
        return this.relativeTabBN.getVisibility() == 0;
    }

    public boolean isCAR01DMX() {
        return this.isCAR01DMX.booleanValue();
    }

    public boolean isCAR01LED() {
        return this.isCAR01LED.booleanValue();
    }

    private void initSingColorView() {
        HashMap hashMap = new HashMap();
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, -1, InputDeviceCompat.SOURCE_ANY, -65281};
        hashMap.put(Integer.valueOf(iArr[0]), Double.valueOf(0.0d));
        hashMap.put(Integer.valueOf(iArr[1]), Double.valueOf(1.0471975511965976d));
        hashMap.put(Integer.valueOf(iArr[2]), Double.valueOf(2.0943951023931953d));
        hashMap.put(Integer.valueOf(iArr[3]), Double.valueOf(3.141592653589793d));
        hashMap.put(Integer.valueOf(iArr[4]), Double.valueOf(4.1887902047863905d));
        hashMap.put(Integer.valueOf(iArr[5]), Double.valueOf(5.235987755982989d));
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(RgbFragment.this.getActivity(), R.anim.layout_scale));
                int intValue = ((Integer) view.getTag()).intValue();
                RgbFragment.this.updateRgbText(Tool.getRGB(intValue), false);
                RgbFragment.this.blackWiteSelectView.setStartColor(intValue);
                RgbFragment.this.imageViewPicker.setInitialColor(intValue);
                int[] rgb = Tool.getRGB(intValue);
                RgbFragment.this.r = rgb[0];
                RgbFragment.this.g = rgb[1];
                RgbFragment.this.b = rgb[2];
                RgbFragment.this.textRed.setText(RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(RgbFragment.this.r)));
                RgbFragment.this.textRed.setBackgroundColor(Color.rgb(RgbFragment.this.r, 0, 0));
                RgbFragment.this.textGreen.setText(RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(RgbFragment.this.g)));
                RgbFragment.this.textGreen.setBackgroundColor(Color.rgb(0, RgbFragment.this.g, 0));
                RgbFragment.this.tvBlue.setText(RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(RgbFragment.this.b)));
                RgbFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, RgbFragment.this.b));
            }
        };
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= 6; i++) {
            View view = this.mContentView;
            View findViewWithTag = view.findViewWithTag("viewColor" + i);
            findViewWithTag.setOnClickListener(onClickListener);
            findViewWithTag.setTag(Integer.valueOf(iArr[i + (-1)]));
            arrayList.add(findViewWithTag);
        }
    }

    private void initDiyColorBlock() {
        View findViewById = this.mContentView.findViewById(R.id.linarLayoutColorCile);
        for (int i = 1; i <= 6; i++) {
            final ColorTextView colorTextView = (ColorTextView) findViewById.findViewWithTag("diyColor" + i);
            String str = (String) colorTextView.getTag();
            SharedPreferences sharedPreferences = this.sharedPreferences;
            if (sharedPreferences != null) {
                int i2 = sharedPreferences.getInt(str, 0);
                if (i2 > this.VCTAG) {
                    Drawable vcModeImage = getVcModeImage(String.valueOf(i2));
                    if (vcModeImage != null) {
                        colorTextView.setBackgroundDrawable(vcModeImage);
                        colorTextView.setColor(i2);
                    }
                    colorTextView.setText("");
                } else if (i2 != 0) {
                    if (i2 < 128) {
                        float f = 10;
                        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                        shapeDrawable.getPaint().setColor(i2);
                        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                        colorTextView.setBackgroundDrawable(shapeDrawable);
                        colorTextView.setColor(i2);
                    } else {
                        Drawable image = getImage(String.valueOf(i2));
                        if (image != null) {
                            colorTextView.setBackgroundDrawable(image);
                            colorTextView.setColor(i2);
                        }
                    }
                    colorTextView.setText("");
                }
            }
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.20
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    final int i3;
                    final int i4;
                    final int i5;
                    view.startAnimation(AnimationUtils.loadAnimation(RgbFragment.this.getActivity(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    RgbFragment.this.diyViewTag = (String) colorTextView.getTag();
                    if (color == 0) {
                        RgbFragment.this.showColorCover((ColorTextView) view, true);
                    } else if (color < 128) {
                        int[] rgb = Tool.getRGB(color);
                        if (RgbFragment.this.mActivity != null) {
                            i5 = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean);
                        } else {
                            i5 = 0;
                        }
                        if (RgbFragment.this.mActivity != null) {
                            RgbFragment.this.mActivity.setRgb(rgb[0], rgb[1], rgb[2], false, RgbFragment.this.isCAR01DMX.booleanValue(), RgbFragment.this.isCAR01LED.booleanValue(), false);
                        }
                        if (i5 == 0) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setBrightNess(100, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                            return;
                        }
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (RgbFragment.this.mActivity != null) {
                                    RgbFragment.this.mActivity.setBrightNess(i5, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                }
                                handler2.removeCallbacksAndMessages(null);
                            }
                        }, 100L);
                    } else if (color >= 3000) {
                        if (RgbFragment.this.mActivity != null) {
                            final int brightData = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean);
                            if (RgbFragment.this.mActivity != null) {
                                RgbFragment.this.mActivity.setMode(true, false, false, color - RgbFragment.this.VCTAG);
                            }
                            if (brightData == 0) {
                                final Handler handler3 = new Handler();
                                handler3.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (RgbFragment.this.mActivity != null) {
                                            NetConnectBle.getInstance().setSensitivity(93, false, false, MainActivity_BLE.sceneBean);
                                        }
                                        handler3.removeCallbacksAndMessages(null);
                                    }
                                }, 100L);
                                return;
                            }
                            final Handler handler4 = new Handler();
                            handler4.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        NetConnectBle.getInstance().setSensitivity(brightData, false, false, MainActivity_BLE.sceneBean);
                                    }
                                    handler4.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                    } else {
                        if (RgbFragment.this.mActivity != null) {
                            i3 = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean);
                        } else {
                            i3 = 0;
                        }
                        if (RgbFragment.this.mActivity != null) {
                            i4 = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean);
                        } else {
                            i4 = 0;
                        }
                        if (RgbFragment.this.mActivity != null) {
                            RgbFragment.this.mActivity.setRegModeNoInterval(color, false);
                        }
                        if (i3 == 0) {
                            final Handler handler5 = new Handler();
                            handler5.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setBrightNessNoInterval(100, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler5.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        } else {
                            final Handler handler6 = new Handler();
                            handler6.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setBrightNessNoInterval(i3, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler6.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                        if (i4 == 0) {
                            final Handler handler7 = new Handler();
                            handler7.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.7
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setSpeedNoInterval(85);
                                    }
                                    handler7.removeCallbacksAndMessages(null);
                                }
                            }, 200L);
                            return;
                        }
                        final Handler handler8 = new Handler();
                        handler8.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.20.8
                            @Override // java.lang.Runnable
                            public void run() {
                                if (RgbFragment.this.mActivity != null) {
                                    RgbFragment.this.mActivity.setSpeedNoInterval(i4);
                                }
                                handler8.removeCallbacksAndMessages(null);
                            }
                        }, 200L);
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.ble.RgbFragment.21
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    RgbFragment.this.sharedPreferences.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(RgbFragment.this.getActivity().getResources().getDrawable(R.drawable.block_shap_color));
                    colorTextView2.setText("+");
                    return true;
                }
            });
        }
    }

    private void initDiyColorCar01DMXBlock() {
        View findViewById = this.mContentView.findViewById(R.id.linarLayoutColorCile);
        for (int i = 1; i <= 6; i++) {
            final ColorTextView colorTextView = (ColorTextView) findViewById.findViewWithTag("diyColorCar01DMX" + i);
            String str = (String) colorTextView.getTag();
            SharedPreferences sharedPreferences = this.sharedPreferences;
            if (sharedPreferences != null) {
                int i2 = sharedPreferences.getInt(str, 0);
                if (i2 > this.VCTAG) {
                    Drawable vcModeImage = getVcModeImage(String.valueOf(i2));
                    if (vcModeImage != null) {
                        colorTextView.setBackgroundDrawable(vcModeImage);
                        colorTextView.setColor(i2);
                    }
                    colorTextView.setText("");
                } else if (i2 != 0) {
                    if (i2 < 128) {
                        float f = 10;
                        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                        shapeDrawable.getPaint().setColor(i2);
                        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                        colorTextView.setBackgroundDrawable(shapeDrawable);
                        colorTextView.setColor(i2);
                    } else {
                        Drawable image = getImage(String.valueOf(i2));
                        if (image != null) {
                            colorTextView.setBackgroundDrawable(image);
                            colorTextView.setColor(i2);
                        }
                    }
                    colorTextView.setText("");
                }
            }
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.22
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    final int i3;
                    final int i4;
                    final int i5;
                    view.startAnimation(AnimationUtils.loadAnimation(RgbFragment.this.getActivity(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    RgbFragment.this.diyViewTag = (String) colorTextView.getTag();
                    if (color == 0) {
                        RgbFragment.this.showColorCover((ColorTextView) view, true);
                    } else if (color < 128) {
                        int[] rgb = Tool.getRGB(color);
                        if (RgbFragment.this.mActivity != null) {
                            i5 = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean);
                        } else {
                            i5 = 0;
                        }
                        if (RgbFragment.this.mActivity != null) {
                            RgbFragment.this.mActivity.setRgb(rgb[0], rgb[1], rgb[2], false, RgbFragment.this.isCAR01DMX.booleanValue(), RgbFragment.this.isCAR01LED.booleanValue(), false);
                        }
                        if (i5 == 0) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setBrightNess(100, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                            return;
                        }
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (RgbFragment.this.mActivity != null) {
                                    RgbFragment.this.mActivity.setBrightNess(i5, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                }
                                handler2.removeCallbacksAndMessages(null);
                            }
                        }, 100L);
                    } else if (color >= 3000) {
                        if (RgbFragment.this.mActivity != null) {
                            final int brightData = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean);
                            if (RgbFragment.this.mActivity != null) {
                                RgbFragment.this.mActivity.setMode(true, false, false, color - RgbFragment.this.VCTAG);
                            }
                            if (brightData == 0) {
                                final Handler handler3 = new Handler();
                                handler3.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (RgbFragment.this.mActivity != null) {
                                            NetConnectBle.getInstance().setSensitivity(93, false, false, MainActivity_BLE.sceneBean);
                                        }
                                        handler3.removeCallbacksAndMessages(null);
                                    }
                                }, 100L);
                                return;
                            }
                            final Handler handler4 = new Handler();
                            handler4.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        NetConnectBle.getInstance().setSensitivity(brightData, false, false, MainActivity_BLE.sceneBean);
                                    }
                                    handler4.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                    } else {
                        if (RgbFragment.this.mActivity != null) {
                            i3 = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean);
                        } else {
                            i3 = 0;
                        }
                        if (RgbFragment.this.mActivity != null) {
                            i4 = SharePersistent.getBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean);
                        } else {
                            i4 = 0;
                        }
                        if (RgbFragment.this.mActivity != null) {
                            RgbFragment.this.mActivity.setRegModeNoInterval(color, false);
                        }
                        if (i3 == 0) {
                            final Handler handler5 = new Handler();
                            handler5.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setBrightNessNoInterval(100, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler5.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        } else {
                            final Handler handler6 = new Handler();
                            handler6.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setBrightNessNoInterval(i3, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler6.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                        if (i4 == 0) {
                            final Handler handler7 = new Handler();
                            handler7.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.7
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (RgbFragment.this.mActivity != null) {
                                        RgbFragment.this.mActivity.setSpeedNoInterval(85);
                                    }
                                    handler7.removeCallbacksAndMessages(null);
                                }
                            }, 200L);
                            return;
                        }
                        final Handler handler8 = new Handler();
                        handler8.postDelayed(new Runnable() { // from class: com.home.fragment.ble.RgbFragment.22.8
                            @Override // java.lang.Runnable
                            public void run() {
                                if (RgbFragment.this.mActivity != null) {
                                    RgbFragment.this.mActivity.setSpeedNoInterval(i4);
                                }
                                handler8.removeCallbacksAndMessages(null);
                            }
                        }, 200L);
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.ble.RgbFragment.23
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    RgbFragment.this.sharedPreferences.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(RgbFragment.this.getActivity().getResources().getDrawable(R.drawable.block_shap_color));
                    colorTextView2.setText("+");
                    return true;
                }
            });
        }
    }

    public void showColorCover(ColorTextView colorTextView, boolean z) {
        this.actionView = colorTextView;
        this.currentSelecColorFromPicker = 0;
        this.srgCover.check(R.id.rbRing);
        this.srgCoverStage.check(R.id.rbRingStage);
        if (z) {
            if (this.mActivity != null) {
                if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDBLE) || MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDLIGHT) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-") && !this.isCAR01DMX.booleanValue() && MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-") && !this.isCAR01LED.booleanValue())) {
                    if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDLIGHT)) {
                        this.srgCover.setVisibility(8);
                        this.srgCoverStage.setVisibility(0);
                    } else {
                        this.srgCover.setVisibility(0);
                        this.srgCoverStage.setVisibility(8);
                    }
                } else {
                    this.srgCover.setVisibility(8);
                }
            }
            this.llRing.setVisibility(0);
            this.llCoverMode.setVisibility(8);
            this.blackWiteSelectView2.setVisibility(0);
        } else {
            this.srgCover.setVisibility(8);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void hideColorCover() {
        this.mPopupWindow.dismiss();
    }

    private void initDiyRingView() {
        this.llRing = (LinearLayout) this.menuView.findViewById(R.id.llRing);
        ColorPickerView colorPickerView = (ColorPickerView) this.menuView.findViewById(R.id.imageViewPicker2);
        this.imageViewPicker2 = colorPickerView;
        colorPickerView.setInitialColor(getResources().getColor(R.color.white));
        this.imageViewPicker2.subscribe(new ColorObserver() { // from class: com.home.fragment.ble.RgbFragment$$ExternalSyntheticLambda1
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                RgbFragment.this.m21lambda$initDiyRingView$1$comhomefragmentbleRgbFragment(i, z, z2);
            }
        });
        this.myColor_select.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.ble.RgbFragment.24
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                RgbFragment.this.blackWiteSelectView2.setStartColor(i);
                RgbFragment.this.currentSelecColorFromPicker = i;
                RgbFragment.this.updateRgbText(rgb, true);
                RgbFragment.this.select_r = rgb[0];
                RgbFragment.this.select_g = rgb[1];
                RgbFragment.this.select_b = rgb[2];
                RgbFragment.this.textRed_select.setText(RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(RgbFragment.this.select_r)));
                RgbFragment.this.textRed_select.setBackgroundColor(Color.rgb(RgbFragment.this.select_r, 0, 0));
                RgbFragment.this.textGreen_select.setText(RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(RgbFragment.this.select_g)));
                RgbFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, RgbFragment.this.select_g, 0));
                RgbFragment.this.tvBlue_select.setText(RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(RgbFragment.this.select_b)));
                RgbFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, RgbFragment.this.select_b));
                if (RgbFragment.this.mActivity != null) {
                    SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, RgbFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, RgbFragment.this.brightnessValue);
                }
            }
        });
        this.iv_switch_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.25
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (RgbFragment.this.imageViewPicker2.getVisibility() == 0) {
                    RgbFragment.this.iv_switch_select.setImageResource(R.drawable.bg_collor_picker);
                    RgbFragment.this.imageViewPicker2.setVisibility(8);
                    RgbFragment.this.myColor_select.setVisibility(0);
                    return;
                }
                RgbFragment.this.iv_switch_select.setImageResource(R.drawable.collor_picker);
                RgbFragment.this.myColor_select.setVisibility(8);
                RgbFragment.this.imageViewPicker2.setVisibility(0);
            }
        });
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.select_r, this.select_g, this.select_b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.ble.RgbFragment.26
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                RgbFragment.this.blackWiteSelectView2.setStartColor(i);
                RgbFragment.this.currentSelecColorFromPicker = i;
                RgbFragment.this.select_r = Color.red(i);
                RgbFragment.this.select_g = Color.green(i);
                RgbFragment.this.select_b = Color.blue(i);
                RgbFragment.this.textRed_select.setBackgroundColor(Color.rgb(RgbFragment.this.select_r, 0, 0));
                RgbFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, RgbFragment.this.select_g, 0));
                RgbFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, RgbFragment.this.select_b));
                RgbFragment.this.textRed_select.setText(RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(RgbFragment.this.select_r)));
                RgbFragment.this.textGreen_select.setText(RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(RgbFragment.this.select_g)));
                RgbFragment.this.tvBlue_select.setText(RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(RgbFragment.this.select_b)));
                RgbFragment.this.updateRgbText(Tool.getRGB(i), true);
                if (RgbFragment.this.mActivity != null) {
                    SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, RgbFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, RgbFragment.this.brightnessValue);
                }
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.27
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(RgbFragment.this.select_r, RgbFragment.this.select_g, RgbFragment.this.select_b);
                colorPicker.show();
            }
        });
        this.textViewRingBrightSC = (TextView) this.menuView.findViewById(R.id.tvRingBrightnessSC);
        BlackWiteSelectView blackWiteSelectView = (BlackWiteSelectView) this.menuView.findViewById(R.id.blackWiteSelectView2);
        this.blackWiteSelectView2 = blackWiteSelectView;
        blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.ble.RgbFragment.28
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                int i3 = i2 <= 0 ? 1 : i2;
                if (i2 >= 100) {
                    i3 = 100;
                }
                RgbFragment.this.brightnessValue = i3;
                RgbFragment.this.textViewRingBrightSC.setText(RgbFragment.this.getResources().getString(R.string.brightness_set, Integer.valueOf(i3)));
                if (RgbFragment.this.mActivity != null) {
                    RgbFragment.this.mActivity.setBrightNess(i3, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                    SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, RgbFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, i3);
                }
                if (MainActivity_BLE.sceneBean != null) {
                    SharePersistent.saveInt(RgbFragment.this.getContext(), MainActivity_BLE.sceneBean + RgbFragment.TAG + "DIY", i3);
                }
            }
        });
        this.blackWiteSelectView2.setProgress(100);
        this.textViewRingBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
        if (MainActivity_BLE.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_BLE.sceneBean + TAG + "DIY");
            if (i > 0) {
                this.blackWiteSelectView2.setProgress(i);
                this.textViewRingBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            }
        }
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
        for (int i2 = 1; i2 <= 6; i2++) {
            View findViewWithTag = findViewById.findViewWithTag("viewColor" + i2);
            findViewWithTag.setTag(Integer.valueOf(iArr[i2 + (-1)]));
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.29
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(RgbFragment.this.getActivity(), R.anim.layout_scale));
                    int intValue = ((Integer) view.getTag()).intValue();
                    RgbFragment.this.currentSelecColorFromPicker = intValue;
                    RgbFragment.this.blackWiteSelectView2.setStartColor(intValue);
                    RgbFragment.this.imageViewPicker2.setInitialColor(intValue);
                    int[] rgb = Tool.getRGB(intValue);
                    RgbFragment.this.updateRgbText(rgb, false);
                    RgbFragment.this.select_r = rgb[0];
                    RgbFragment.this.select_g = rgb[1];
                    RgbFragment.this.select_b = rgb[2];
                    RgbFragment.this.textRed_select.setText(RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(RgbFragment.this.select_r)));
                    RgbFragment.this.textRed_select.setBackgroundColor(Color.rgb(RgbFragment.this.select_r, 0, 0));
                    RgbFragment.this.textGreen_select.setText(RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(RgbFragment.this.select_g)));
                    RgbFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, RgbFragment.this.select_g, 0));
                    RgbFragment.this.tvBlue_select.setText(RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(RgbFragment.this.select_b)));
                    RgbFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, RgbFragment.this.select_b));
                    RgbFragment.this.updateRgbText(Tool.getRGB(intValue), false);
                }
            });
            arrayList.add(findViewWithTag);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initDiyRingView$1$com-home-fragment-ble-RgbFragment  reason: not valid java name */
    public /* synthetic */ void m21lambda$initDiyRingView$1$comhomefragmentbleRgbFragment(int i, boolean z, boolean z2) {
        if (z) {
            BlackWiteSelectView blackWiteSelectView = this.blackWiteSelectView2;
            if (blackWiteSelectView != null) {
                blackWiteSelectView.setStartColor(i);
            }
            this.currentSelecColorFromPicker = i;
            int[] rgb = Tool.getRGB(i);
            updateRgbText(rgb, false);
            this.select_r = rgb[0];
            this.select_g = rgb[1];
            this.select_b = rgb[2];
            this.textRed_select.setText(getResources().getString(R.string.red, Integer.valueOf(this.select_r)));
            this.textRed_select.setBackgroundColor(Color.rgb(this.select_r, 0, 0));
            this.textGreen_select.setText(getResources().getString(R.string.green, Integer.valueOf(this.select_g)));
            this.textGreen_select.setBackgroundColor(Color.rgb(0, this.select_g, 0));
            this.tvBlue_select.setText(getResources().getString(R.string.blue, Integer.valueOf(this.select_b)));
            this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, this.select_b));
            if (this.mActivity != null) {
                SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright" + CommonConstant.LEDBLE, this.diyViewTag + "bright" + CommonConstant.LEDBLE, this.brightnessValue);
            }
        }
    }

    private void initDiyModeView() {
        this.llCoverMode = (LinearLayout) this.menuView.findViewById(R.id.llCoverMode);
        this.llSeekBarModeSC = (LinearLayout) this.menuView.findViewById(R.id.llSeekBarModeSC);
        this.seekBarModeSC = (SeekBar) this.menuView.findViewById(R.id.seekBarModeSC);
        this.textViewModeSC = (TextView) this.menuView.findViewById(R.id.textViewModeSC);
        if (this.mActivity != null && (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDBLE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || ((MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-") && !this.isCAR01DMX.booleanValue()) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDLIGHT)))) {
            this.llSeekBarModeSC.setVisibility(8);
        }
        this.wheelPicker_tang = (WheelPicker) this.menuView.findViewById(R.id.wheelPicker_tang);
        if (this.mActivity != null) {
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-") && !this.isCAR01DMX.booleanValue())) {
                this.wheelPicker_tang.setData(carModel());
            } else {
                this.wheelPicker_tang.setData(bleModel());
                this.seekBarModeSC.setMax(bleModel().size() - 1);
            }
        }
        this.wheelPicker_tang.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.fragment.ble.RgbFragment.30
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                if (i >= 0) {
                    Log.e(RgbFragment.TAG, "onItemSelected: " + i);
                    RgbFragment rgbFragment = RgbFragment.this;
                    rgbFragment.currentSelecColorFromPicker = Integer.parseInt(((String) rgbFragment.listNubmer.get(i)).trim());
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setRegMode(Integer.parseInt(((String) RgbFragment.this.listNubmer.get(i)).trim()), RgbFragment.this.isCAR01DMX.booleanValue());
                    }
                    if (RgbFragment.this.seekBarModeSC != null) {
                        RgbFragment.this.seekBarModeSC.setProgress(i);
                        SeekBar seekBar = RgbFragment.this.seekBarModeSC;
                        seekBar.setTag("" + RgbFragment.this.currentSelecColorFromPicker);
                    }
                }
            }
        });
        this.seekBarModeSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.RgbFragment.31
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                List data = RgbFragment.this.wheelPicker_tang.getData();
                TextView textView = RgbFragment.this.textViewModeSC;
                textView.setText("" + data.get(i));
                RgbFragment.this.seekBarModeSC.setTag(((String) RgbFragment.this.listNubmer.get(i)).trim());
                RgbFragment rgbFragment = RgbFragment.this;
                rgbFragment.currentSelecColorFromPicker = Integer.parseInt(((String) rgbFragment.listNubmer.get(i)).trim());
                RgbFragment.this.wheelPicker_tang.setSelectedItemPosition(i);
                if (RgbFragment.this.mActivity == null || i < 0 || i > 28) {
                    return;
                }
                RgbFragment.this.mActivity.setRegMode(Integer.parseInt(((String) RgbFragment.this.listNubmer.get(i)).trim()), RgbFragment.this.isCAR01DMX.booleanValue());
            }
        });
        this.seekBarBrightBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarBrightNess);
        this.textViewBrightSC = (TextView) this.menuView.findViewById(R.id.textViewBrightNess);
        this.seekBarBrightBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.RgbFragment.32
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i == 0) {
                    RgbFragment.this.textViewBrightSC.setText(RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, 1));
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setBrightNess(1, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, 1);
                    }
                } else {
                    RgbFragment.this.textViewBrightSC.setText(RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, Integer.valueOf(i)));
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setBrightNess(i, false, RgbFragment.this.isCAR01LED.booleanValue(), RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "bright" + MainActivity_BLE.sceneBean, i);
                    }
                }
                if (MainActivity_BLE.sceneBean != null) {
                    Log.e(RgbFragment.TAG, "progress = " + i);
                    SharePersistent.saveInt(RgbFragment.this.getContext(), MainActivity_BLE.sceneBean + RgbFragment.TAG + "DIYbright", i);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_BLE.sceneBean + TAG + "DIYbright");
            if (i > 0) {
                this.seekBarBrightBarSC.setProgress(i);
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            } else {
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
            }
        }
        this.seekBarSpeedBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarSpeedSC);
        this.textViewSpeedSC = (TextView) this.menuView.findViewById(R.id.textViewSpeedSC);
        this.seekBarSpeedBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.RgbFragment.33
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (i2 == 0) {
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setSpeed(1, false, RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean, 1);
                    }
                } else {
                    RgbFragment.this.textViewSpeedSC.setText(RgbFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i2)));
                    if (RgbFragment.this.mActivity != null) {
                        RgbFragment.this.mActivity.setSpeed(i2, false, RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "speed" + MainActivity_BLE.sceneBean, i2);
                    }
                }
                if (MainActivity_BLE.sceneBean != null) {
                    SharePersistent.saveInt(RgbFragment.this.getContext(), MainActivity_BLE.sceneBean + RgbFragment.TAG + "DIYspeed", i2);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, MainActivity_BLE.sceneBean + TAG + "DIYspeed");
            if (i2 > 0) {
                this.seekBarSpeedBarSC.setProgress(i2);
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i2)));
                return;
            }
            this.textViewSpeedSC.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
        }
    }

    private void initDiyVoiceControlView() {
        this.llVoicecontrol = (LinearLayout) this.menuView.findViewById(R.id.llVoicecontrol);
        this.tvCurrentMode = (TextView) this.menuView.findViewById(R.id.tvCurrentMode);
        LinearLayout linearLayout = (LinearLayout) this.menuView.findViewById(R.id.ll_jump);
        this.ll_jump = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.34
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RgbFragment.this.startAnimation(view);
                RgbFragment.this.tvCurrentMode.setText(RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, RgbFragment.this.getString(R.string.jump)));
                RgbFragment.this.tvCurrentMode.setTag("3000");
                RgbFragment.this.currentSelecColorFromPicker = 3000;
                RgbFragment.this.mActivity.setMode(true, false, false, 0);
                MainActivity_BLE mainActivity_BLE = RgbFragment.this.mActivity;
                RgbFragment.showToast(mainActivity_BLE, "" + RgbFragment.this.getString(R.string.jump));
            }
        });
        LinearLayout linearLayout2 = (LinearLayout) this.menuView.findViewById(R.id.ll_breathe);
        this.ll_breathe = linearLayout2;
        linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RgbFragment.this.startAnimation(view);
                RgbFragment.this.tvCurrentMode.setText(RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, RgbFragment.this.getString(R.string.breathe)));
                RgbFragment.this.tvCurrentMode.setTag("3001");
                RgbFragment.this.currentSelecColorFromPicker = 3001;
                RgbFragment.this.mActivity.setMode(true, false, false, 1);
                MainActivity_BLE mainActivity_BLE = RgbFragment.this.mActivity;
                RgbFragment.showToast(mainActivity_BLE, "" + RgbFragment.this.getString(R.string.breathe));
            }
        });
        LinearLayout linearLayout3 = (LinearLayout) this.menuView.findViewById(R.id.ll_flash);
        this.ll_flash = linearLayout3;
        linearLayout3.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.36
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RgbFragment.this.startAnimation(view);
                RgbFragment.this.tvCurrentMode.setText(RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, RgbFragment.this.getString(R.string.flash)));
                RgbFragment.this.tvCurrentMode.setTag("3002");
                RgbFragment.this.currentSelecColorFromPicker = 3002;
                RgbFragment.this.mActivity.setMode(true, false, false, 2);
                MainActivity_BLE mainActivity_BLE = RgbFragment.this.mActivity;
                RgbFragment.showToast(mainActivity_BLE, "" + RgbFragment.this.getString(R.string.flash));
            }
        });
        LinearLayout linearLayout4 = (LinearLayout) this.menuView.findViewById(R.id.ll_gradient);
        this.ll_gradient = linearLayout4;
        linearLayout4.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.37
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RgbFragment.this.startAnimation(view);
                RgbFragment.this.tvCurrentMode.setText(RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, RgbFragment.this.getString(R.string.gradient)));
                RgbFragment.this.tvCurrentMode.setTag("3003");
                RgbFragment.this.currentSelecColorFromPicker = 3003;
                RgbFragment.this.mActivity.setMode(true, false, false, 3);
                MainActivity_BLE mainActivity_BLE = RgbFragment.this.mActivity;
                RgbFragment.showToast(mainActivity_BLE, "" + RgbFragment.this.getString(R.string.gradient));
            }
        });
        this.seekBarSensitivitySC = (SeekBar) this.menuView.findViewById(R.id.seekBarSensitivitySC);
        this.textViewSensitivitySC = (TextView) this.menuView.findViewById(R.id.textViewSensitivitySC);
        this.seekBarSensitivitySC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.ble.RgbFragment.38
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    if (i == 0) {
                        RgbFragment.this.textViewSensitivitySC.setText(RgbFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, String.valueOf(1)));
                        NetConnectBle.getInstance().setSensitivity(1, false, false, MainActivity_BLE.sceneBean);
                        SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean, 1);
                    } else {
                        RgbFragment.this.textViewSensitivitySC.setText(RgbFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, String.valueOf(i)));
                        NetConnectBle.getInstance().setSensitivity(i, false, false, MainActivity_BLE.sceneBean);
                        SharePersistent.saveBrightData(RgbFragment.this.getActivity(), RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean, RgbFragment.this.diyViewTag + "sensitivity" + MainActivity_BLE.sceneBean, 1);
                    }
                    if (MainActivity_BLE.sceneBean != null) {
                        SharePersistent.saveInt(RgbFragment.this.getContext(), MainActivity_BLE.sceneBean + RgbFragment.TAG + "DIYsensitivity", i);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_BLE.sceneBean + TAG + "DIYsensitivity");
            if (i > 0) {
                this.seekBarSensitivitySC.setProgress(i);
                this.textViewSensitivitySC.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i)));
                return;
            }
            this.textViewSensitivitySC.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
    }

    private void initConfirmButton() {
        Button button = (Button) this.menuView.findViewById(R.id.buttonSelectColorConfirm);
        this.buttonSelectColorConfirm = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.RgbFragment.39
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Log.e(RgbFragment.TAG, "currentSelecColorFromPicker = " + RgbFragment.this.currentSelecColorFromPicker);
                if (RgbFragment.this.currentSelecColorFromPicker >= 0) {
                    if (RgbFragment.this.currentSelecColorFromPicker <= 0 || RgbFragment.this.currentSelecColorFromPicker >= 3000) {
                        if (RgbFragment.this.currentSelecColorFromPicker >= 3000) {
                            RgbFragment.this.actionView.setColor(RgbFragment.this.currentSelecColorFromPicker);
                            String str = (String) RgbFragment.this.actionView.getTag();
                            if (RgbFragment.this.tvCurrentMode != null && RgbFragment.this.tvCurrentMode.getTag() != null) {
                                if (RgbFragment.this.tvCurrentMode.getTag().toString() != null) {
                                    String obj = RgbFragment.this.tvCurrentMode.getTag().toString();
                                    RgbFragment.this.sharedPreferences.edit().putInt(str, Integer.parseInt(obj.trim())).commit();
                                    Drawable vcModeImage = RgbFragment.this.getVcModeImage(obj);
                                    if (vcModeImage != null) {
                                        RgbFragment.this.actionView.setBackgroundDrawable(vcModeImage);
                                    }
                                }
                                RgbFragment.this.actionView.setText("");
                            }
                        }
                    } else {
                        RgbFragment.this.actionView.setColor(RgbFragment.this.currentSelecColorFromPicker);
                        String str2 = (String) RgbFragment.this.actionView.getTag();
                        if (RgbFragment.this.seekBarModeSC != null && RgbFragment.this.seekBarModeSC.getTag() != null) {
                            if (RgbFragment.this.seekBarModeSC.getTag().toString() != null) {
                                String obj2 = RgbFragment.this.seekBarModeSC.getTag().toString();
                                RgbFragment.this.sharedPreferences.edit().putInt(str2, Integer.parseInt(obj2.trim())).commit();
                                Drawable image = RgbFragment.this.getImage(obj2);
                                if (image != null) {
                                    RgbFragment.this.actionView.setBackgroundDrawable(image);
                                }
                            }
                            RgbFragment.this.actionView.setText("");
                        }
                    }
                } else {
                    float f = 10;
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                    shapeDrawable.getPaint().setColor(RgbFragment.this.currentSelecColorFromPicker);
                    shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                    RgbFragment.this.sharedPreferences.edit().putInt((String) RgbFragment.this.actionView.getTag(), RgbFragment.this.currentSelecColorFromPicker).commit();
                    if (RgbFragment.this.currentSelecColorFromPicker != 0) {
                        RgbFragment.this.actionView.setColor(RgbFragment.this.currentSelecColorFromPicker);
                        RgbFragment.this.actionView.setBackgroundDrawable(shapeDrawable);
                        RgbFragment.this.actionView.setText("");
                    }
                }
                RgbFragment.this.hideColorCover();
            }
        });
    }

    public static void showToast(Context context, String str) {
        BamToast.showText(context, str);
    }

    public Drawable getImage(String str) {
        Resources resources = getResources();
        int identifier = resources.getIdentifier("img_" + str, "drawable", BuildConfig.APPLICATION_ID);
        Log.e("====", "getImage: " + identifier + "===" + str);
        if (identifier > 0) {
            return getActivity().getResources().getDrawable(identifier);
        }
        return null;
    }

    public Drawable getVcModeImage(String str) {
        Resources resources = getResources();
        int identifier = resources.getIdentifier("ledble_diy_" + str, "drawable", BuildConfig.APPLICATION_ID);
        Log.e("====", "getImage: " + identifier + "===" + str);
        if (identifier > 0) {
            return getActivity().getResources().getDrawable(identifier);
        }
        return null;
    }

    public void updateRgbText(int[] iArr, boolean z) {
        try {
            MainActivity_BLE mainActivity_BLE = this.mActivity;
            if (mainActivity_BLE != null) {
                mainActivity_BLE.setRgb(iArr[0], iArr[1], iArr[2], z, this.isCAR01DMX.booleanValue(), this.isCAR01LED.booleanValue(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> bleModel() {
        String[] stringArray;
        this.listName.clear();
        this.listNubmer.clear();
        for (String str : getActivity().getResources().getStringArray(R.array.ble_mode)) {
            this.listName.add(str.substring(0, str.lastIndexOf(",")));
            this.listNubmer.add(str.substring(str.lastIndexOf(",") + 1).trim());
        }
        return this.listName;
    }

    private List<String> carModel() {
        String[] stringArray;
        this.listName.clear();
        this.listNubmer.clear();
        for (String str : getActivity().getResources().getStringArray(R.array.car_mode)) {
            this.listName.add(str.substring(0, str.lastIndexOf(",")));
            this.listNubmer.add(str.substring(str.lastIndexOf(",") + 1).trim());
        }
        return this.listName;
    }
}
