package com.home.fragment.dmx03;

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
import butterknife.BindView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.common.view.SegmentedRadioGroup;
import com.common.view.toast.bamtoast.BamToast;
import com.home.activity.main.MainActivity_DMX03;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
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
public class DMX03RgbFragment extends LedBleFragment {
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
    private LinearLayout llVoicecontrol;
    private LinearLayout ll_breathe;
    private LinearLayout ll_flash;
    private LinearLayout ll_gradient;
    private LinearLayout ll_jump;
    private MainActivity_DMX03 mActivity;
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
    private int currentTab = 0;
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

    @Override // com.home.base.LedBleFragment
    public void initData() {
        if (MainActivity_DMX03.getMainActivity() != null) {
            MainActivity_DMX03 mainActivity = MainActivity_DMX03.getMainActivity();
            this.mActivity = mainActivity;
            SegmentedRadioGroup segmentRgb = mainActivity.getSegmentRgb();
            this.segmentedRadioGroup = segmentRgb;
            segmentRgb.check(R.id.rbRgbRing);
        }
        if (MainActivity_DMX03.getMainActivity() != null) {
            Log.e(TAG, "MainActivity_BLE.getSceneBean() =" + MainActivity_DMX03.getSceneBean());
            if (MainActivity_DMX03.getSceneBean().equalsIgnoreCase("LEDDMX-03-") || MainActivity_DMX03.getSceneBean().equalsIgnoreCase("LEDDMX-00-")) {
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
            }
        }
        if (this.mActivity != null) {
            this.sharedPreferences = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
        }
        SegmentedRadioGroup segmentedRadioGroup = this.segmentedRadioGroup;
        if (segmentedRadioGroup != null) {
            segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.1
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (R.id.rbRgbRing == i) {
                        DMX03RgbFragment.this.relativeTab1.setVisibility(0);
                        DMX03RgbFragment.this.relativeTab2.setVisibility(8);
                        DMX03RgbFragment.this.relativeTab3.setVisibility(8);
                        DMX03RgbFragment.this.relativeTabBN.setVisibility(8);
                    } else if (R.id.rbRgbCT == i) {
                        DMX03RgbFragment.this.relativeTab2.setVisibility(0);
                        DMX03RgbFragment.this.relativeTab1.setVisibility(8);
                        DMX03RgbFragment.this.relativeTab3.setVisibility(8);
                        DMX03RgbFragment.this.relativeTabBN.setVisibility(8);
                    } else if (R.id.rbRgbBright == i) {
                        DMX03RgbFragment.this.relativeTab3.setVisibility(8);
                        DMX03RgbFragment.this.relativeTab1.setVisibility(8);
                        DMX03RgbFragment.this.relativeTab2.setVisibility(8);
                        DMX03RgbFragment.this.relativeTabBN.setVisibility(0);
                    } else if (R.id.rbRgbDIM == i) {
                        if (DMX03RgbFragment.this.relativeTab3 != null) {
                            DMX03RgbFragment.this.relativeTab3.setVisibility(0);
                        }
                        DMX03RgbFragment.this.relativeTab1.setVisibility(8);
                        DMX03RgbFragment.this.relativeTab2.setVisibility(8);
                        DMX03RgbFragment.this.relativeTabBN.setVisibility(8);
                    }
                }
            });
        }
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.r, this.g, this.b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.2
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                DMX03RgbFragment.this.r = Color.red(i);
                DMX03RgbFragment.this.g = Color.green(i);
                DMX03RgbFragment.this.b = Color.blue(i);
                DMX03RgbFragment.this.textRed.setBackgroundColor(Color.rgb(DMX03RgbFragment.this.r, 0, 0));
                DMX03RgbFragment.this.textGreen.setBackgroundColor(Color.rgb(0, DMX03RgbFragment.this.g, 0));
                DMX03RgbFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, DMX03RgbFragment.this.b));
                DMX03RgbFragment.this.textRed.setText(DMX03RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX03RgbFragment.this.r)));
                DMX03RgbFragment.this.textGreen.setText(DMX03RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX03RgbFragment.this.g)));
                DMX03RgbFragment.this.tvBlue.setText(DMX03RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX03RgbFragment.this.b)));
                DMX03RgbFragment.this.updateRgbText(Tool.getRGB(i), true);
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(DMX03RgbFragment.this.r, DMX03RgbFragment.this.g, DMX03RgbFragment.this.b);
                colorPicker.show();
            }
        });
        this.imageViewPicker.setInitialColor(getResources().getColor(R.color.white));
        this.imageViewPicker.subscribe(new ColorObserver() { // from class: com.home.fragment.dmx03.DMX03RgbFragment$$ExternalSyntheticLambda0
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                DMX03RgbFragment.this.m28lambda$initData$0$comhomefragmentdmx03DMX03RgbFragment(i, z, z2);
            }
        });
        this.myColor.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.4
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                DMX03RgbFragment.this.blackWiteSelectView.setStartColor(i);
                DMX03RgbFragment.this.updateRgbText(rgb, true);
                DMX03RgbFragment.this.r = rgb[0];
                DMX03RgbFragment.this.g = rgb[1];
                DMX03RgbFragment.this.b = rgb[2];
                DMX03RgbFragment.this.textRed.setText(DMX03RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX03RgbFragment.this.r)));
                DMX03RgbFragment.this.textRed.setBackgroundColor(Color.rgb(DMX03RgbFragment.this.r, 0, 0));
                DMX03RgbFragment.this.textGreen.setText(DMX03RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX03RgbFragment.this.g)));
                DMX03RgbFragment.this.textGreen.setBackgroundColor(Color.rgb(0, DMX03RgbFragment.this.g, 0));
                DMX03RgbFragment.this.tvBlue.setText(DMX03RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX03RgbFragment.this.b)));
                DMX03RgbFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, DMX03RgbFragment.this.b));
            }
        });
        this.iv_switch.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX03RgbFragment.this.imageViewPicker.getVisibility() == 0) {
                    DMX03RgbFragment.this.iv_switch.setImageResource(R.drawable.bg_collor_picker);
                    DMX03RgbFragment.this.imageViewPicker.setVisibility(8);
                    DMX03RgbFragment.this.myColor.setVisibility(0);
                    return;
                }
                DMX03RgbFragment.this.iv_switch.setImageResource(R.drawable.collor_picker);
                DMX03RgbFragment.this.myColor.setVisibility(8);
                DMX03RgbFragment.this.imageViewPicker.setVisibility(0);
            }
        });
        this.blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.6
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                if (i2 <= 0) {
                    i2 = 1;
                } else if (i2 >= 100) {
                    i2 = 100;
                }
                if (DMX03RgbFragment.this.tvBrightness != null) {
                    DMX03RgbFragment.this.tvBrightness.setText(DMX03RgbFragment.this.getContext().getResources().getString(R.string.brightness_set, String.valueOf(i2)));
                }
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setBrightNess(i2, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                }
                if (DMX03RgbFragment.this.mActivity != null) {
                    Context context = DMX03RgbFragment.this.getContext();
                    SharePersistent.saveInt(context, LedBleApplication.getApp().getSceneBean() + DMX03RgbFragment.TAG, i2);
                }
            }
        });
        this.blackWiteSelectView.setProgress(100);
        this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
        if (this.mActivity != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, LedBleApplication.getApp().getSceneBean() + TAG);
            if (i > 0) {
                this.blackWiteSelectView.setProgress(i);
                this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            }
        }
        this.pikerImageView.setInnerCircle(0.459f);
        this.pikerImageView.setOnTouchPixListener(new MyColorPickerImageView.OnTouchPixListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.7
            @Override // com.home.view.MyColorPickerImageView.OnTouchPixListener
            public void onColorSelect(int i2, float f) {
                int i3 = (int) ((f / 360.0f) * 100.0f);
                int i4 = 100 - i3;
                DMX03RgbFragment.this.textViewWarmCool.setText(DMX03RgbFragment.this.getActivity().getString(R.string.cool_warm, new Object[]{String.valueOf(i3), String.valueOf(i4)}));
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setCT(i4, 100 - i4);
                }
            }
        });
        this.seekBarBrightNessCT.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.8
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (i2 == 0) {
                    DMX03RgbFragment.this.textViewBrightNessCT.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(1)));
                    seekBar.setProgress(1);
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setBrightNess(1, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                        return;
                    }
                    return;
                }
                DMX03RgbFragment.this.textViewBrightNessCT.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(i2)));
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setBrightNess(i2, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                }
            }
        });
        this.seekBarRedBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.9
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    Log.e(DMX03RgbFragment.TAG, "fromUser=" + z);
                    DMX03RgbFragment.this.redBrightNessValue = i2;
                    if (i2 < 0 || i2 > 100) {
                        return;
                    }
                    DMX03RgbFragment.this.tvBrightness1.setText(Integer.toString(i2));
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setSmartBrightness(1, i2);
                    }
                    Context context2 = DMX03RgbFragment.this.getContext();
                    SharePersistent.saveInt(context2, LedBleApplication.getApp().getSceneBean() + DMX03RgbFragment.TAG + "red-aisle", i2);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX03RgbFragment.this.redBrightNessValue < 0 || DMX03RgbFragment.this.redBrightNessValue > 100) {
                    return;
                }
                DMX03RgbFragment.this.tvBrightness1.setText(Integer.toString(DMX03RgbFragment.this.redBrightNessValue));
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setSmartBrightnessNoInterval(1, DMX03RgbFragment.this.redBrightNessValue);
                }
            }
        });
        if (MainActivity_DMX03.sceneBean != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, LedBleApplication.getApp().getSceneBean() + TAG + "red-aisle");
            if (i2 >= 0) {
                this.seekBarRedBrightNess.setProgress(i2);
                this.tvBrightness1.setText(String.valueOf(i2));
            } else {
                this.seekBarRedBrightNess.setProgress(50);
                this.tvBrightness1.setText(String.valueOf(50));
            }
        }
        this.seekBarGreenBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.10
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    DMX03RgbFragment.this.greenBrightNessValue = i3;
                    if (i3 < 0 || i3 > 100) {
                        return;
                    }
                    DMX03RgbFragment.this.tvBrightness2.setText(Integer.toString(i3));
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setSmartBrightness(2, i3);
                    }
                    Context context3 = DMX03RgbFragment.this.getContext();
                    SharePersistent.saveInt(context3, LedBleApplication.getApp().getSceneBean() + DMX03RgbFragment.TAG + "green-aisle", i3);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX03RgbFragment.this.greenBrightNessValue < 0 || DMX03RgbFragment.this.greenBrightNessValue > 100) {
                    return;
                }
                DMX03RgbFragment.this.tvBrightness2.setText(Integer.toString(DMX03RgbFragment.this.greenBrightNessValue));
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setSmartBrightnessNoInterval(2, DMX03RgbFragment.this.greenBrightNessValue);
                }
            }
        });
        if (MainActivity_DMX03.sceneBean != null) {
            Context context3 = getContext();
            int i3 = SharePersistent.getInt(context3, LedBleApplication.getApp().getSceneBean() + TAG + "green-aisle");
            if (i3 >= 0) {
                this.seekBarGreenBrightNess.setProgress(i3);
                this.tvBrightness2.setText(String.valueOf(i3));
            } else {
                this.seekBarGreenBrightNess.setProgress(50);
                this.tvBrightness2.setText(String.valueOf(50));
            }
        }
        this.seekBarBlueBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.11
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i4, boolean z) {
                if (z) {
                    DMX03RgbFragment.this.blueBrightNessValue = i4;
                    if (i4 < 0 || i4 > 100) {
                        return;
                    }
                    DMX03RgbFragment.this.tvBrightness3.setText(Integer.toString(i4));
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setSmartBrightness(3, i4);
                    }
                    Context context4 = DMX03RgbFragment.this.getContext();
                    SharePersistent.saveInt(context4, LedBleApplication.getApp().getSceneBean() + DMX03RgbFragment.TAG + "blue-aisle", i4);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX03RgbFragment.this.blueBrightNessValue < 0 || DMX03RgbFragment.this.blueBrightNessValue > 100) {
                    return;
                }
                DMX03RgbFragment.this.tvBrightness3.setText(Integer.toString(DMX03RgbFragment.this.blueBrightNessValue));
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setSmartBrightnessNoInterval(3, DMX03RgbFragment.this.blueBrightNessValue);
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context4 = getContext();
            int i4 = SharePersistent.getInt(context4, LedBleApplication.getApp().getSceneBean() + TAG + "blue-aisle");
            if (i4 >= 0) {
                this.seekBarBlueBrightNess.setProgress(i4);
                this.tvBrightness3.setText(String.valueOf(i4));
            } else {
                this.seekBarBlueBrightNess.setProgress(50);
                this.tvBrightness3.setText(String.valueOf(50));
            }
        }
        this.pikerImageViewDim.setInnerCircle(0.25f);
        this.pikerImageViewDim.setOnTouchPixListener(new MyColorPickerImageView.OnTouchPixListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.12
            @Override // com.home.view.MyColorPickerImageView.OnTouchPixListener
            public void onColorSelect(int i5, float f) {
                int i6 = (int) ((f / 360.0f) * 100.0f);
                TextView textView = DMX03RgbFragment.this.textViewBrightNessDim;
                textView.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.brightness) + ":" + (i6 + "%"));
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setDim(i6);
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
        this.srgCover.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.13
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i5) {
                if (R.id.rbRing == i5) {
                    DMX03RgbFragment.this.currentTab = 0;
                    DMX03RgbFragment.this.llRing.setVisibility(0);
                    DMX03RgbFragment.this.llCoverMode.setVisibility(8);
                    DMX03RgbFragment.this.llVoicecontrol.setVisibility(8);
                } else if (R.id.rbModle == i5) {
                    DMX03RgbFragment.this.currentTab = 1;
                    DMX03RgbFragment.this.llRing.setVisibility(8);
                    DMX03RgbFragment.this.llCoverMode.setVisibility(0);
                    DMX03RgbFragment.this.llVoicecontrol.setVisibility(8);
                } else if (R.id.rbVoiceControl == i5) {
                    DMX03RgbFragment.this.currentTab = 2;
                    DMX03RgbFragment.this.llRing.setVisibility(8);
                    DMX03RgbFragment.this.llCoverMode.setVisibility(8);
                    DMX03RgbFragment.this.llVoicecontrol.setVisibility(0);
                }
            }
        });
        ImageView imageView = (ImageView) this.menuView.findViewById(R.id.backImage);
        this.backImage = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX03RgbFragment.this.hideColorCover();
            }
        });
        initSingColorView();
        initDiyColorBlock();
        initDiyColorCar01DMXBlock();
        initDiyRingView();
        initDiyModeView();
        initDiyVoiceControlView();
        initConfirmButton();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initData$0$com-home-fragment-dmx03-DMX03RgbFragment  reason: not valid java name */
    public /* synthetic */ void m28lambda$initData$0$comhomefragmentdmx03DMX03RgbFragment(int i, boolean z, boolean z2) {
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
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.15
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(DMX03RgbFragment.this.getActivity(), R.anim.layout_scale));
                int intValue = ((Integer) view.getTag()).intValue();
                DMX03RgbFragment.this.updateRgbText(Tool.getRGB(intValue), false);
                DMX03RgbFragment.this.blackWiteSelectView.setStartColor(intValue);
                DMX03RgbFragment.this.imageViewPicker.setInitialColor(intValue);
                int[] rgb = Tool.getRGB(intValue);
                DMX03RgbFragment.this.r = rgb[0];
                DMX03RgbFragment.this.g = rgb[1];
                DMX03RgbFragment.this.b = rgb[2];
                DMX03RgbFragment.this.textRed.setText(DMX03RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX03RgbFragment.this.r)));
                DMX03RgbFragment.this.textRed.setBackgroundColor(Color.rgb(DMX03RgbFragment.this.r, 0, 0));
                DMX03RgbFragment.this.textGreen.setText(DMX03RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX03RgbFragment.this.g)));
                DMX03RgbFragment.this.textGreen.setBackgroundColor(Color.rgb(0, DMX03RgbFragment.this.g, 0));
                DMX03RgbFragment.this.tvBlue.setText(DMX03RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX03RgbFragment.this.b)));
                DMX03RgbFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, DMX03RgbFragment.this.b));
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
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(DMX03RgbFragment.this.getActivity(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    DMX03RgbFragment.this.diyViewTag = (String) colorTextView.getTag();
                    if (color == 0) {
                        DMX03RgbFragment.this.showColorCover((ColorTextView) view, true);
                    } else if (color < 128) {
                        int[] rgb = Tool.getRGB(color);
                        final int brightData = SharePersistent.getBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean());
                        if (DMX03RgbFragment.this.mActivity != null) {
                            DMX03RgbFragment.this.mActivity.setRgb(rgb[0], rgb[1], rgb[2], false, DMX03RgbFragment.this.isCAR01DMX.booleanValue(), DMX03RgbFragment.this.isCAR01LED.booleanValue(), false);
                        }
                        if (brightData == 0) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setBrightNess(100, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                            return;
                        }
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (DMX03RgbFragment.this.mActivity != null) {
                                    DMX03RgbFragment.this.mActivity.setBrightNess(brightData, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                }
                                handler2.removeCallbacksAndMessages(null);
                            }
                        }, 100L);
                    } else if (color >= 3000) {
                        if (DMX03RgbFragment.this.mActivity != null) {
                            if (DMX03RgbFragment.this.mActivity != null) {
                                DMX03RgbFragment.this.mActivity.setMode(true, false, false, color - DMX03RgbFragment.this.VCTAG);
                            }
                            final Handler handler3 = new Handler();
                            handler3.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        NetConnectBle.getInstance().setSensitivity(93, false, false, LedBleApplication.getApp().getSceneBean());
                                    }
                                    handler3.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                    } else {
                        final int brightData2 = SharePersistent.getBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean());
                        final int brightData3 = SharePersistent.getBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean());
                        if (DMX03RgbFragment.this.mActivity != null) {
                            DMX03RgbFragment.this.mActivity.setRegModeNoInterval(color, false);
                        }
                        if (brightData2 == 0) {
                            final Handler handler4 = new Handler();
                            handler4.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setBrightNessNoInterval(100, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler4.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        } else {
                            final Handler handler5 = new Handler();
                            handler5.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setBrightNessNoInterval(brightData2, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler5.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                        if (brightData3 == 0) {
                            final Handler handler6 = new Handler();
                            handler6.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16.7
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setSpeedNoInterval(85);
                                    }
                                    handler6.removeCallbacksAndMessages(null);
                                }
                            }, 200L);
                            return;
                        }
                        final Handler handler7 = new Handler();
                        handler7.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.16.8
                            @Override // java.lang.Runnable
                            public void run() {
                                if (DMX03RgbFragment.this.mActivity != null) {
                                    DMX03RgbFragment.this.mActivity.setSpeedNoInterval(brightData3);
                                }
                                handler7.removeCallbacksAndMessages(null);
                            }
                        }, 200L);
                    }
                }

                /* renamed from: com.home.fragment.dmx03.DMX03RgbFragment$16$4  reason: invalid class name */
                /* loaded from: classes.dex */
                class AnonymousClass4 implements Runnable {
                    final /* synthetic */ Handler val$handler;
                    final /* synthetic */ int val$sensitivity2;

                    AnonymousClass4(int i, Handler handler) {
                        this.val$sensitivity2 = i;
                        this.val$handler = handler;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (DMX03RgbFragment.this.mActivity != null) {
                            NetConnectBle.getInstance().setSensitivity(this.val$sensitivity2, false, false, LedBleApplication.getApp().getSceneBean());
                        }
                        this.val$handler.removeCallbacksAndMessages(null);
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.17
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    DMX03RgbFragment.this.sharedPreferences.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(DMX03RgbFragment.this.getActivity().getResources().getDrawable(R.drawable.block_shap_color));
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
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    final int i3;
                    view.startAnimation(AnimationUtils.loadAnimation(DMX03RgbFragment.this.getActivity(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    DMX03RgbFragment.this.diyViewTag = (String) colorTextView.getTag();
                    if (color == 0) {
                        DMX03RgbFragment.this.showColorCover((ColorTextView) view, true);
                    } else if (color < 128) {
                        int[] rgb = Tool.getRGB(color);
                        final int brightData = SharePersistent.getBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean());
                        if (DMX03RgbFragment.this.mActivity != null) {
                            DMX03RgbFragment.this.mActivity.setRgb(rgb[0], rgb[1], rgb[2], false, DMX03RgbFragment.this.isCAR01DMX.booleanValue(), DMX03RgbFragment.this.isCAR01LED.booleanValue(), false);
                        }
                        if (brightData == 0) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setBrightNess(100, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                            return;
                        }
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (DMX03RgbFragment.this.mActivity != null) {
                                    DMX03RgbFragment.this.mActivity.setBrightNess(brightData, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                }
                                handler2.removeCallbacksAndMessages(null);
                            }
                        }, 100L);
                    } else if (color >= 3000) {
                        if (DMX03RgbFragment.this.mActivity != null) {
                            if (DMX03RgbFragment.this.mActivity != null) {
                                DMX03RgbFragment.this.mActivity.setMode(true, false, false, color - DMX03RgbFragment.this.VCTAG);
                            }
                            final Handler handler3 = new Handler();
                            handler3.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        NetConnectBle.getInstance().setSensitivity(93, false, false, LedBleApplication.getApp().getSceneBean());
                                    }
                                    handler3.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                    } else {
                        final int brightData2 = SharePersistent.getBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean());
                        if (DMX03RgbFragment.this.mActivity != null) {
                            i3 = SharePersistent.getBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean());
                        } else {
                            i3 = 0;
                        }
                        if (DMX03RgbFragment.this.mActivity != null) {
                            DMX03RgbFragment.this.mActivity.setRegModeNoInterval(color, false);
                        }
                        if (brightData2 == 0) {
                            final Handler handler4 = new Handler();
                            handler4.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setBrightNessNoInterval(100, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler4.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        } else {
                            final Handler handler5 = new Handler();
                            handler5.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setBrightNessNoInterval(brightData2, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                                    }
                                    handler5.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                        }
                        if (i3 == 0) {
                            final Handler handler6 = new Handler();
                            handler6.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18.7
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX03RgbFragment.this.mActivity != null) {
                                        DMX03RgbFragment.this.mActivity.setSpeedNoInterval(85);
                                    }
                                    handler6.removeCallbacksAndMessages(null);
                                }
                            }, 200L);
                            return;
                        }
                        final Handler handler7 = new Handler();
                        handler7.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.18.8
                            @Override // java.lang.Runnable
                            public void run() {
                                if (DMX03RgbFragment.this.mActivity != null) {
                                    DMX03RgbFragment.this.mActivity.setSpeedNoInterval(i3);
                                }
                                handler7.removeCallbacksAndMessages(null);
                            }
                        }, 200L);
                    }
                }

                /* renamed from: com.home.fragment.dmx03.DMX03RgbFragment$18$4  reason: invalid class name */
                /* loaded from: classes.dex */
                class AnonymousClass4 implements Runnable {
                    final /* synthetic */ Handler val$handler;
                    final /* synthetic */ int val$sensitivity2;

                    AnonymousClass4(int i, Handler handler) {
                        this.val$sensitivity2 = i;
                        this.val$handler = handler;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (DMX03RgbFragment.this.mActivity != null) {
                            NetConnectBle.getInstance().setSensitivity(this.val$sensitivity2, false, false, LedBleApplication.getApp().getSceneBean());
                        }
                        this.val$handler.removeCallbacksAndMessages(null);
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.19
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    DMX03RgbFragment.this.sharedPreferences.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(DMX03RgbFragment.this.getActivity().getResources().getDrawable(R.drawable.block_shap_color));
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
        this.srgCover.setVisibility(8);
        if (z) {
            this.llRing.setVisibility(0);
            this.llCoverMode.setVisibility(8);
            this.blackWiteSelectView2.setVisibility(0);
        } else {
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
        this.imageViewPicker2.subscribe(new ColorObserver() { // from class: com.home.fragment.dmx03.DMX03RgbFragment$$ExternalSyntheticLambda1
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                DMX03RgbFragment.this.m29xce5051dc(i, z, z2);
            }
        });
        this.myColor_select.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.20
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                DMX03RgbFragment.this.blackWiteSelectView2.setStartColor(i);
                DMX03RgbFragment.this.currentSelecColorFromPicker = i;
                DMX03RgbFragment.this.updateRgbText(rgb, true);
                DMX03RgbFragment.this.select_r = rgb[0];
                DMX03RgbFragment.this.select_g = rgb[1];
                DMX03RgbFragment.this.select_b = rgb[2];
                DMX03RgbFragment.this.textRed_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX03RgbFragment.this.select_r)));
                DMX03RgbFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX03RgbFragment.this.select_r, 0, 0));
                DMX03RgbFragment.this.textGreen_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX03RgbFragment.this.select_g)));
                DMX03RgbFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX03RgbFragment.this.select_g, 0));
                DMX03RgbFragment.this.tvBlue_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX03RgbFragment.this.select_b)));
                DMX03RgbFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX03RgbFragment.this.select_b));
                if (DMX03RgbFragment.this.mActivity != null) {
                    SharePersistent.saveBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.brightnessValue);
                }
            }
        });
        this.iv_switch_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX03RgbFragment.this.imageViewPicker2.getVisibility() == 0) {
                    DMX03RgbFragment.this.iv_switch_select.setImageResource(R.drawable.bg_collor_picker);
                    DMX03RgbFragment.this.imageViewPicker2.setVisibility(8);
                    DMX03RgbFragment.this.myColor_select.setVisibility(0);
                    return;
                }
                DMX03RgbFragment.this.iv_switch_select.setImageResource(R.drawable.collor_picker);
                DMX03RgbFragment.this.myColor_select.setVisibility(8);
                DMX03RgbFragment.this.imageViewPicker2.setVisibility(0);
            }
        });
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.select_r, this.select_g, this.select_b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.22
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                DMX03RgbFragment.this.blackWiteSelectView2.setStartColor(i);
                DMX03RgbFragment.this.currentSelecColorFromPicker = i;
                DMX03RgbFragment.this.select_r = Color.red(i);
                DMX03RgbFragment.this.select_g = Color.green(i);
                DMX03RgbFragment.this.select_b = Color.blue(i);
                DMX03RgbFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX03RgbFragment.this.select_r, 0, 0));
                DMX03RgbFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX03RgbFragment.this.select_g, 0));
                DMX03RgbFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX03RgbFragment.this.select_b));
                DMX03RgbFragment.this.textRed_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX03RgbFragment.this.select_r)));
                DMX03RgbFragment.this.textGreen_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX03RgbFragment.this.select_g)));
                DMX03RgbFragment.this.tvBlue_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX03RgbFragment.this.select_b)));
                DMX03RgbFragment.this.updateRgbText(Tool.getRGB(i), true);
                if (DMX03RgbFragment.this.mActivity != null) {
                    SharePersistent.saveBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.brightnessValue);
                }
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.23
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(DMX03RgbFragment.this.select_r, DMX03RgbFragment.this.select_g, DMX03RgbFragment.this.select_b);
                colorPicker.show();
            }
        });
        this.textViewRingBrightSC = (TextView) this.menuView.findViewById(R.id.tvRingBrightnessSC);
        BlackWiteSelectView blackWiteSelectView = (BlackWiteSelectView) this.menuView.findViewById(R.id.blackWiteSelectView2);
        this.blackWiteSelectView2 = blackWiteSelectView;
        blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.24
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                int i3 = i2 <= 0 ? 1 : i2;
                if (i2 >= 100) {
                    i3 = 100;
                }
                DMX03RgbFragment.this.brightnessValue = i3;
                DMX03RgbFragment.this.textViewRingBrightSC.setText(DMX03RgbFragment.this.getResources().getString(R.string.brightness_set, Integer.valueOf(i3)));
                if (DMX03RgbFragment.this.mActivity != null) {
                    DMX03RgbFragment.this.mActivity.setBrightNess(i3, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                    SharePersistent.saveBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.brightnessValue);
                }
                SharePersistent.saveInt(DMX03RgbFragment.this.getContext(), LedBleApplication.getApp().getSceneBean() + DMX03RgbFragment.TAG + "DIY", i3);
            }
        });
        this.blackWiteSelectView2.setProgress(100);
        this.textViewRingBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, LedBleApplication.getApp().getSceneBean() + TAG + "DIY");
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
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.25
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(DMX03RgbFragment.this.getActivity(), R.anim.layout_scale));
                    int intValue = ((Integer) view.getTag()).intValue();
                    DMX03RgbFragment.this.currentSelecColorFromPicker = intValue;
                    DMX03RgbFragment.this.blackWiteSelectView2.setStartColor(intValue);
                    DMX03RgbFragment.this.imageViewPicker2.setInitialColor(intValue);
                    int[] rgb = Tool.getRGB(intValue);
                    DMX03RgbFragment.this.updateRgbText(rgb, false);
                    DMX03RgbFragment.this.select_r = rgb[0];
                    DMX03RgbFragment.this.select_g = rgb[1];
                    DMX03RgbFragment.this.select_b = rgb[2];
                    DMX03RgbFragment.this.textRed_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX03RgbFragment.this.select_r)));
                    DMX03RgbFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX03RgbFragment.this.select_r, 0, 0));
                    DMX03RgbFragment.this.textGreen_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX03RgbFragment.this.select_g)));
                    DMX03RgbFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX03RgbFragment.this.select_g, 0));
                    DMX03RgbFragment.this.tvBlue_select.setText(DMX03RgbFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX03RgbFragment.this.select_b)));
                    DMX03RgbFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX03RgbFragment.this.select_b));
                    DMX03RgbFragment.this.updateRgbText(Tool.getRGB(intValue), false);
                }
            });
            arrayList.add(findViewWithTag);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initDiyRingView$1$com-home-fragment-dmx03-DMX03RgbFragment  reason: not valid java name */
    public /* synthetic */ void m29xce5051dc(int i, boolean z, boolean z2) {
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
                SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), this.brightnessValue);
            }
        }
    }

    private void initDiyModeView() {
        this.llCoverMode = (LinearLayout) this.menuView.findViewById(R.id.llCoverMode);
        this.seekBarModeSC = (SeekBar) this.menuView.findViewById(R.id.seekBarModeSC);
        this.textViewModeSC = (TextView) this.menuView.findViewById(R.id.textViewModeSC);
        WheelPicker wheelPicker = (WheelPicker) this.menuView.findViewById(R.id.wheelPicker_tang);
        this.wheelPicker_tang = wheelPicker;
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.26
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker2, Object obj, int i) {
                if (i >= 0) {
                    Log.e(DMX03RgbFragment.TAG, "onItemSelected: " + i);
                    DMX03RgbFragment dMX03RgbFragment = DMX03RgbFragment.this;
                    dMX03RgbFragment.currentSelecColorFromPicker = Integer.parseInt(((String) dMX03RgbFragment.listNubmer.get(i)).trim());
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setRegMode(Integer.parseInt(((String) DMX03RgbFragment.this.listNubmer.get(i)).trim()), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                    }
                    if (DMX03RgbFragment.this.seekBarModeSC != null) {
                        DMX03RgbFragment.this.seekBarModeSC.setProgress(i);
                        SeekBar seekBar = DMX03RgbFragment.this.seekBarModeSC;
                        seekBar.setTag("" + DMX03RgbFragment.this.currentSelecColorFromPicker);
                    }
                }
            }
        });
        this.seekBarModeSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.27
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                List data = DMX03RgbFragment.this.wheelPicker_tang.getData();
                TextView textView = DMX03RgbFragment.this.textViewModeSC;
                textView.setText("" + data.get(i));
                if (DMX03RgbFragment.this.mActivity == null || i < 0 || i > 28) {
                    return;
                }
                DMX03RgbFragment.this.seekBarModeSC.setTag(((String) DMX03RgbFragment.this.listNubmer.get(i)).trim());
                DMX03RgbFragment dMX03RgbFragment = DMX03RgbFragment.this;
                dMX03RgbFragment.currentSelecColorFromPicker = Integer.parseInt(((String) dMX03RgbFragment.listNubmer.get(i)).trim());
                DMX03RgbFragment.this.wheelPicker_tang.setSelectedItemPosition(i);
                DMX03RgbFragment.this.mActivity.setRegMode(Integer.parseInt(((String) DMX03RgbFragment.this.listNubmer.get(i)).trim()), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
            }
        });
        this.seekBarBrightBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarBrightNess);
        this.textViewBrightSC = (TextView) this.menuView.findViewById(R.id.textViewBrightNess);
        this.seekBarBrightBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.28
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i == 0) {
                    DMX03RgbFragment.this.textViewBrightSC.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, 1));
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setBrightNess(1, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), 1);
                    }
                } else {
                    DMX03RgbFragment.this.textViewBrightSC.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.brightness_set, Integer.valueOf(i)));
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setBrightNess(i, false, DMX03RgbFragment.this.isCAR01LED.booleanValue(), DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "bright" + LedBleApplication.getApp().getSceneBean(), i);
                    }
                }
                SharePersistent.saveInt(DMX03RgbFragment.this.getContext(), LedBleApplication.getApp().getSceneBean() + DMX03RgbFragment.TAG + "DIYbright", i);
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, LedBleApplication.getApp().getSceneBean() + TAG + "DIYbright");
            if (i > 0) {
                this.seekBarBrightBarSC.setProgress(i);
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            } else {
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
            }
        }
        this.seekBarSpeedBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarSpeedSC);
        this.textViewSpeedSC = (TextView) this.menuView.findViewById(R.id.textViewSpeedSC);
        this.seekBarSpeedBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.29
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (i2 == 0) {
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setSpeed(1, false, DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean(), 1);
                    }
                } else {
                    DMX03RgbFragment.this.textViewSpeedSC.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i2)));
                    if (DMX03RgbFragment.this.mActivity != null) {
                        DMX03RgbFragment.this.mActivity.setSpeed(i2, false, DMX03RgbFragment.this.isCAR01DMX.booleanValue());
                        SharePersistent.saveBrightData(DMX03RgbFragment.this.getActivity(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean(), DMX03RgbFragment.this.diyViewTag + "speed" + LedBleApplication.getApp().getSceneBean(), i2);
                    }
                }
                SharePersistent.saveInt(DMX03RgbFragment.this.getContext(), LedBleApplication.getApp().getSceneBean() + "DIYspeed", i2);
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, LedBleApplication.getApp().getSceneBean() + TAG + "DIYspeed");
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
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.30
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX03RgbFragment.this.startAnimation(view);
                DMX03RgbFragment.this.tvCurrentMode.setText(DMX03RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, DMX03RgbFragment.this.getString(R.string.jump)));
                DMX03RgbFragment.this.tvCurrentMode.setTag("3000");
                DMX03RgbFragment.this.currentSelecColorFromPicker = 3000;
                DMX03RgbFragment.this.mActivity.setMode(true, false, false, 0);
                MainActivity_DMX03 mainActivity_DMX03 = DMX03RgbFragment.this.mActivity;
                DMX03RgbFragment.showToast(mainActivity_DMX03, "" + DMX03RgbFragment.this.getString(R.string.jump));
            }
        });
        LinearLayout linearLayout2 = (LinearLayout) this.menuView.findViewById(R.id.ll_breathe);
        this.ll_breathe = linearLayout2;
        linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX03RgbFragment.this.startAnimation(view);
                DMX03RgbFragment.this.tvCurrentMode.setText(DMX03RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, DMX03RgbFragment.this.getString(R.string.breathe)));
                DMX03RgbFragment.this.tvCurrentMode.setTag("3001");
                DMX03RgbFragment.this.currentSelecColorFromPicker = 3001;
                DMX03RgbFragment.this.mActivity.setMode(true, false, false, 1);
                MainActivity_DMX03 mainActivity_DMX03 = DMX03RgbFragment.this.mActivity;
                DMX03RgbFragment.showToast(mainActivity_DMX03, "" + DMX03RgbFragment.this.getString(R.string.breathe));
            }
        });
        LinearLayout linearLayout3 = (LinearLayout) this.menuView.findViewById(R.id.ll_flash);
        this.ll_flash = linearLayout3;
        linearLayout3.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.32
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX03RgbFragment.this.startAnimation(view);
                DMX03RgbFragment.this.tvCurrentMode.setText(DMX03RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, DMX03RgbFragment.this.getString(R.string.flash)));
                DMX03RgbFragment.this.tvCurrentMode.setTag("3002");
                DMX03RgbFragment.this.currentSelecColorFromPicker = 3002;
                DMX03RgbFragment.this.mActivity.setMode(true, false, false, 2);
                MainActivity_DMX03 mainActivity_DMX03 = DMX03RgbFragment.this.mActivity;
                DMX03RgbFragment.showToast(mainActivity_DMX03, "" + DMX03RgbFragment.this.getString(R.string.flash));
            }
        });
        LinearLayout linearLayout4 = (LinearLayout) this.menuView.findViewById(R.id.ll_gradient);
        this.ll_gradient = linearLayout4;
        linearLayout4.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.33
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX03RgbFragment.this.startAnimation(view);
                DMX03RgbFragment.this.tvCurrentMode.setText(DMX03RgbFragment.this.mActivity.getResources().getString(R.string.current_mode_format, DMX03RgbFragment.this.getString(R.string.gradient)));
                DMX03RgbFragment.this.tvCurrentMode.setTag("3003");
                DMX03RgbFragment.this.currentSelecColorFromPicker = 3003;
                DMX03RgbFragment.this.mActivity.setMode(true, false, false, 3);
                MainActivity_DMX03 mainActivity_DMX03 = DMX03RgbFragment.this.mActivity;
                DMX03RgbFragment.showToast(mainActivity_DMX03, "" + DMX03RgbFragment.this.getString(R.string.gradient));
            }
        });
        this.seekBarSensitivitySC = (SeekBar) this.menuView.findViewById(R.id.seekBarSensitivitySC);
        this.textViewSensitivitySC = (TextView) this.menuView.findViewById(R.id.textViewSensitivitySC);
        this.seekBarSensitivitySC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.34
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
                        DMX03RgbFragment.this.textViewSensitivitySC.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, String.valueOf(1)));
                        NetConnectBle.getInstance().setSensitivity(1, false, false, LedBleApplication.getApp().getSceneBean());
                    } else {
                        DMX03RgbFragment.this.textViewSensitivitySC.setText(DMX03RgbFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, String.valueOf(i)));
                        NetConnectBle.getInstance().setSensitivity(i, false, false, LedBleApplication.getApp().getSceneBean());
                    }
                    Context context = DMX03RgbFragment.this.getContext();
                    SharePersistent.saveInt(context, LedBleApplication.getApp().getSceneBean() + DMX03RgbFragment.TAG + "DIYsensitivity", i);
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, LedBleApplication.getApp().getSceneBean() + TAG + "DIYsensitivity");
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
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03RgbFragment.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Log.e(DMX03RgbFragment.TAG, "currentSelecColorFromPicker = " + DMX03RgbFragment.this.currentSelecColorFromPicker);
                if (DMX03RgbFragment.this.currentSelecColorFromPicker >= 0) {
                    if (DMX03RgbFragment.this.currentSelecColorFromPicker <= 0 || DMX03RgbFragment.this.currentSelecColorFromPicker >= 3000) {
                        if (DMX03RgbFragment.this.currentSelecColorFromPicker >= 3000) {
                            DMX03RgbFragment.this.actionView.setColor(DMX03RgbFragment.this.currentSelecColorFromPicker);
                            String str = (String) DMX03RgbFragment.this.actionView.getTag();
                            if (DMX03RgbFragment.this.tvCurrentMode != null && DMX03RgbFragment.this.tvCurrentMode.getTag() != null) {
                                if (DMX03RgbFragment.this.tvCurrentMode.getTag().toString() != null) {
                                    String obj = DMX03RgbFragment.this.tvCurrentMode.getTag().toString();
                                    DMX03RgbFragment.this.sharedPreferences.edit().putInt(str, Integer.parseInt(obj.trim())).commit();
                                    Drawable vcModeImage = DMX03RgbFragment.this.getVcModeImage(obj);
                                    if (vcModeImage != null) {
                                        DMX03RgbFragment.this.actionView.setBackgroundDrawable(vcModeImage);
                                    }
                                }
                                DMX03RgbFragment.this.actionView.setText("");
                            }
                        }
                    } else {
                        DMX03RgbFragment.this.actionView.setColor(DMX03RgbFragment.this.currentSelecColorFromPicker);
                        String str2 = (String) DMX03RgbFragment.this.actionView.getTag();
                        if (DMX03RgbFragment.this.seekBarModeSC != null && DMX03RgbFragment.this.seekBarModeSC.getTag() != null) {
                            if (DMX03RgbFragment.this.seekBarModeSC.getTag().toString() != null) {
                                String obj2 = DMX03RgbFragment.this.seekBarModeSC.getTag().toString();
                                DMX03RgbFragment.this.sharedPreferences.edit().putInt(str2, Integer.parseInt(obj2.trim())).commit();
                                Drawable image = DMX03RgbFragment.this.getImage(obj2);
                                if (image != null) {
                                    DMX03RgbFragment.this.actionView.setBackgroundDrawable(image);
                                }
                            }
                            DMX03RgbFragment.this.actionView.setText("");
                        }
                    }
                } else {
                    float f = 10;
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                    shapeDrawable.getPaint().setColor(DMX03RgbFragment.this.currentSelecColorFromPicker);
                    shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                    DMX03RgbFragment.this.sharedPreferences.edit().putInt((String) DMX03RgbFragment.this.actionView.getTag(), DMX03RgbFragment.this.currentSelecColorFromPicker).commit();
                    if (DMX03RgbFragment.this.currentSelecColorFromPicker != 0) {
                        DMX03RgbFragment.this.actionView.setColor(DMX03RgbFragment.this.currentSelecColorFromPicker);
                        DMX03RgbFragment.this.actionView.setBackgroundDrawable(shapeDrawable);
                        DMX03RgbFragment.this.actionView.setText("");
                    }
                }
                DMX03RgbFragment.this.hideColorCover();
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
            MainActivity_DMX03 mainActivity_DMX03 = this.mActivity;
            if (mainActivity_DMX03 != null) {
                mainActivity_DMX03.setRgb(iArr[0], iArr[1], iArr[2], z, this.isCAR01DMX.booleanValue(), this.isCAR01LED.booleanValue(), false);
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
