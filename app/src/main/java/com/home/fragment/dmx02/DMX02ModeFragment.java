package com.home.fragment.dmx02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.listener.IListener;
import com.common.listener.ListenerManager;
import com.common.listener.ReceiveDataListener;
import com.common.listener.ReceiveDataListenerManager;
import com.common.net.NetResult;
import com.common.pictureselector.GlideEngine;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.common.view.SegmentedRadioGroup;
import com.common.view.toast.bamtoast.BamToast;
import com.home.activity.main.MainActivity_DMX02;
import com.home.activity.other.Dmx02EditColorActivity;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.home.view.ActionSheet;
import com.home.view.BlackWiteSelectView;
import com.home.view.ColorTextView;
import com.home.view.MyColorPicker;
import com.home.view.custom.LevelProgressBar;
import com.home.view.custom.StreamList.PullLeftToRefreshLayout;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.BuildConfig;
import com.ledlamp.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpStatus;
import pl.droidsonroids.gif.GifImageView;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class DMX02ModeFragment extends LedBleFragment implements ActionSheet.ActionSheetListener, ReceiveDataListener, IListener {
    private static final int COLOR_DEFALUT = 0;
    private static final int DMX02_INT_EDIT_COLOR = 1002;
    private static final String TAG = "DMX02ModeFragment";
    private ColorTextView actionView;
    private int animation;
    private ImageView backImage;
    @BindView(R.id.blackWiteSelectView)
    BlackWiteSelectView blackWiteSelectView;
    private BlackWiteSelectView blackWiteSelectView2;
    private int brightnessValue;
    private Button buttonSelectColorConfirm;
    private String cmdString;
    private ArrayList<MyColor> colors;
    private int currentSelecColorFromPicker;
    private String diyViewTag;
    @BindView(R.id.gridView)
    GridView gridView;
    private ImageAdapter imageAdapter;
    @BindView(R.id.imageViewPicker)
    ColorPickerView imageViewPicker;
    private ColorPickerView imageViewPicker2;
    private int index;
    @BindView(R.id.ivAddImage)
    ImageView ivAddImage;
    @BindView(R.id.ivAnimateDown)
    ImageView ivAnimateDown;
    @BindView(R.id.ivAnimateLeft)
    ImageView ivAnimateLeft;
    @BindView(R.id.ivAnimatePlay)
    ImageView ivAnimatePlay;
    @BindView(R.id.ivAnimateRight)
    ImageView ivAnimateRight;
    @BindView(R.id.ivAnimateUp)
    ImageView ivAnimateUp;
    @BindView(R.id.ivLeftMenu)
    ImageView ivLeftMenu;
    @BindView(R.id.ivTopGifImageView)
    GifImageView ivTopGifImageView;
    @BindView(R.id.ivTopImageView)
    ImageView ivTopImageView;
    @BindView(R.id.iv_switch)
    ImageView iv_switch;
    private ImageView iv_switch_select;
    @BindView(R.id.linearChouse)
    LinearLayout linearChouse;
    private LinearLayout linearChouse_select;
    @BindView(R.id.llAnimateColorchange)
    LinearLayout llAnimateColorchange;
    @BindView(R.id.llAnimateCycle)
    LinearLayout llAnimateCycle;
    @BindView(R.id.llAnimateDown)
    LinearLayout llAnimateDown;
    @BindView(R.id.llAnimateLeft)
    LinearLayout llAnimateLeft;
    @BindView(R.id.llAnimatePlay)
    LinearLayout llAnimatePlay;
    @BindView(R.id.llAnimateRight)
    LinearLayout llAnimateRight;
    @BindView(R.id.llAnimateUp)
    LinearLayout llAnimateUp;
    @BindView(R.id.llAnimation)
    LinearLayout llAnimation;
    private LinearLayout llCoverMode;
    @BindView(R.id.llDiyColor)
    LinearLayout llDiyColor;
    @BindView(R.id.llDiyColorCar01DMX)
    LinearLayout llDiyColorCar01DMX;
    private LinearLayout llRing;
    private MainActivity_DMX02 mActivity;
    private View mContentView;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureParameterStyle mPictureParameterStyle;
    private PopupWindow mPopupWindow;
    private PullAdapter mPullAdapter;
    private View menuView;
    @BindView(R.id.myColor)
    MyColorPicker myColor;
    private MyColorPicker myColor_select;
    @BindView(R.id.onOffButton)
    ToggleButton onOffButton;
    private Handler overtimeHandler;
    private Runnable overtimeRunnable;
    @BindView(R.id.plrl)
    PullLeftToRefreshLayout plrl;
    @BindView(R.id.progressBar)
    LevelProgressBar progressBar;
    private boolean receiveData;
    @BindView(R.id.relativeCustom)
    RelativeLayout relativeCustom;
    @BindView(R.id.relativeRGB)
    RelativeLayout relativeRGB;
    @BindView(R.id.rlProgressView)
    RelativeLayout rlProgressView;
    @BindView(R.id.seekBarBrightAnimation)
    SeekBar seekBarBrightAnimation;
    private SeekBar seekBarBrightBarSC;
    private SeekBar seekBarModeSC;
    @BindView(R.id.seekBarSpeedAnimation)
    SeekBar seekBarSpeedAnimation;
    private SeekBar seekBarSpeedBarSC;
    @BindView(R.id.seekBarSpeedCustom)
    SeekBar seekBarSpeedCustom;
    private SegmentedRadioGroup segmentedRadioGroup;
    private Handler sendCmdHandler;
    private Runnable sendCmdRunnable;
    private SharedPreferences sharedPreferences;
    private SegmentedRadioGroup srgCover;
    @BindView(R.id.rv)
    RecyclerView streamList;
    private String tempDataStr;
    @BindView(R.id.textGreen)
    TextView textGreen;
    private TextView textGreen_select;
    @BindView(R.id.textRed)
    TextView textRed;
    private TextView textRed_select;
    @BindView(R.id.textViewBrightAnimation)
    TextView textViewBrightAnimation;
    @BindView(R.id.textViewBrightCustom)
    TextView textViewBrightCustom;
    private TextView textViewBrightSC;
    @BindView(R.id.textViewConnectCount)
    TextView textViewConnectCount;
    private TextView textViewModeSC;
    private TextView textViewRingBrightSC;
    @BindView(R.id.textViewSpeedAnimation)
    TextView textViewSpeedAnimation;
    private TextView textViewSpeedSC;
    private int timeoutCount;
    @BindView(R.id.tvBlue)
    TextView tvBlue;
    private TextView tvBlue_select;
    @BindView(R.id.tvBrightness)
    TextView tvBrightness;
    @BindView(R.id.tvProgress)
    TextView tvProgress;
    @BindView(R.id.tvSend)
    TextView tvSend;
    private WheelPicker wheelPicker_tang;
    private List<String> listName = new ArrayList();
    private List<String> listNubmer = new ArrayList();
    private List<String> gifListName = new ArrayList();
    private int[] icon = {R.drawable.effect1, R.drawable.effect2, R.drawable.effect3, R.drawable.effect4, R.drawable.effect5, R.drawable.effect6, R.drawable.effect7, R.drawable.effect8, R.drawable.effect9, R.drawable.effect10, R.drawable.effect11, R.drawable.effect12};
    private ArrayList<String> cmdStringList = new ArrayList<>();
    private boolean isPlay = false;
    private ArrayList<Bitmap> bitmapsOfGif = new ArrayList<>();
    private ArrayList<Bitmap> bitmapsOfImg = new ArrayList<>();
    private int currentTab = 0;
    private int r = 255;
    private int g = 255;
    private int b = 255;
    private int select_r = 255;
    private int select_g = 255;
    private int select_b = 255;
    private List<LocalMedia> selectList = new ArrayList();

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
    }

    @Override // com.home.view.ActionSheet.ActionSheetListener
    public void onDismiss(ActionSheet actionSheet, boolean z) {
    }

    static /* synthetic */ int access$1208(DMX02ModeFragment dMX02ModeFragment) {
        int i = dMX02ModeFragment.index;
        dMX02ModeFragment.index = i + 1;
        return i;
    }

    static /* synthetic */ int access$2008(DMX02ModeFragment dMX02ModeFragment) {
        int i = dMX02ModeFragment.timeoutCount;
        dMX02ModeFragment.timeoutCount = i + 1;
        return i;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = layoutInflater.inflate(R.layout.dmx02_fragment_mode, viewGroup, false);
        this.menuView = layoutInflater.inflate(R.layout.activity_select_color, viewGroup, false);
        return this.mContentView;
    }

    public void setActive() {
        this.mActivity = MainActivity_DMX02.getMainActivity();
    }

    @Override // com.common.listener.IListener
    public void notifyAllActivity(String str) {
        if (str.equalsIgnoreCase(Constant.UpdateNewFindDevice)) {
            updateNewFindDevice();
        }
    }

    @Override // com.common.listener.ReceiveDataListener
    public void notifyReceiveData(String str, String str2) {
        if (str.equalsIgnoreCase(Constant.PasswordSet) || !str.equalsIgnoreCase("DMX02ModeFragment") || str2 == null) {
            return;
        }
        this.timeoutCount = 0;
        this.receiveData = true;
        Log.i("DMX02ModeFragment", "reveice data =" + str2 + ", index = " + this.index);
        int i = this.index;
        if (i == -1) {
            if (str2.contains("5A") || str2.contains("5a")) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 100L);
            }
        } else if (i >= 0) {
            String[] split = str2.split(" ");
            int parseInt = Integer.parseInt(split[split.length - 1] + split[split.length - 2] + split[split.length - 3] + split[split.length - 4], 16);
            Log.i("DMX02ModeFragment", "index =" + this.index + ", numberIndex = " + parseInt);
            if (this.index == parseInt) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 100L);
            }
        }
    }

    private void updateNewFindDevice() {
        if (LedBleApplication.getApp().getBleDevices() != null) {
            this.textViewConnectCount.setText(Integer.toString(LedBleApplication.getApp().getHashMapConnect().size()));
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        ReceiveDataListenerManager.getInstance().unRegisterListener(this);
        Handler handler = this.sendCmdHandler;
        if (handler != null) {
            handler.removeCallbacks(this.sendCmdRunnable);
            this.sendCmdHandler = null;
            this.sendCmdRunnable = null;
        }
        Handler handler2 = this.overtimeHandler;
        if (handler2 != null) {
            handler2.removeCallbacks(this.overtimeRunnable);
            this.overtimeHandler = null;
            this.sendCmdRunnable = null;
        }
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        ListenerManager.getInstance().registerListtener(this);
        this.mActivity = MainActivity_DMX02.getMainActivity();
        ReceiveDataListenerManager.getInstance().registerListtener(this);
        this.ivLeftMenu.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ListenerManager.getInstance().sendBroadCast(Constant.ShowLeftMenu);
            }
        });
        if (MainActivity_DMX02.getMainActivity() != null) {
            MainActivity_DMX02 mainActivity = MainActivity_DMX02.getMainActivity();
            this.mActivity = mainActivity;
            SegmentedRadioGroup segmentRgb = mainActivity.getSegmentRgb();
            this.segmentedRadioGroup = segmentRgb;
            segmentRgb.check(R.id.rbRgbRing);
        }
        if (MainActivity_DMX02.getMainActivity() != null) {
            if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01)) {
                getActivity().findViewById(R.id.rbRgbBright).setVisibility(8);
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
            } else if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-") || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-00-")) {
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
            } else if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
                getActivity().findViewById(R.id.rbRgbCT).setVisibility(8);
                getActivity().findViewById(R.id.rbRgbBright).setVisibility(8);
            }
        }
        if (this.mActivity != null) {
            this.sharedPreferences = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
        }
        SegmentedRadioGroup segmentedRadioGroup = this.segmentedRadioGroup;
        if (segmentedRadioGroup != null) {
            segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.2
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (R.id.rbRgbRing == i) {
                        DMX02ModeFragment.this.relativeRGB.setVisibility(0);
                        DMX02ModeFragment.this.llAnimation.setVisibility(8);
                        DMX02ModeFragment.this.relativeCustom.setVisibility(8);
                    } else if (R.id.rbAnimation == i) {
                        DMX02ModeFragment.this.llAnimation.setVisibility(0);
                        DMX02ModeFragment.this.relativeRGB.setVisibility(8);
                        DMX02ModeFragment.this.relativeCustom.setVisibility(8);
                    } else if (R.id.rbCustom == i) {
                        DMX02ModeFragment.this.relativeRGB.setVisibility(8);
                        DMX02ModeFragment.this.llAnimation.setVisibility(8);
                        DMX02ModeFragment.this.relativeCustom.setVisibility(0);
                    }
                }
            });
        }
        this.onOffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    DMX02ModeFragment.this.onOffButton.setBackgroundResource(R.drawable.off_btn);
                    DMX02ModeFragment.this.mActivity.bledmxclose();
                    return;
                }
                DMX02ModeFragment.this.onOffButton.setBackgroundResource(R.drawable.on_btn);
                DMX02ModeFragment.this.mActivity.bledmxopen();
            }
        });
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.r, this.g, this.b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.4
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                DMX02ModeFragment.this.r = Color.red(i);
                DMX02ModeFragment.this.g = Color.green(i);
                DMX02ModeFragment.this.b = Color.blue(i);
                DMX02ModeFragment.this.textRed.setBackgroundColor(Color.rgb(DMX02ModeFragment.this.r, 0, 0));
                DMX02ModeFragment.this.textGreen.setBackgroundColor(Color.rgb(0, DMX02ModeFragment.this.g, 0));
                DMX02ModeFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, DMX02ModeFragment.this.b));
                DMX02ModeFragment.this.textRed.setText(DMX02ModeFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX02ModeFragment.this.r)));
                DMX02ModeFragment.this.textGreen.setText(DMX02ModeFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX02ModeFragment.this.g)));
                DMX02ModeFragment.this.tvBlue.setText(DMX02ModeFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX02ModeFragment.this.b)));
                DMX02ModeFragment.this.updateRgbText(Tool.getRGB(i), true);
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(DMX02ModeFragment.this.r, DMX02ModeFragment.this.g, DMX02ModeFragment.this.b);
                colorPicker.show();
            }
        });
        this.imageViewPicker.setInitialColor(getResources().getColor(R.color.white));
        this.imageViewPicker.subscribe(new ColorObserver() { // from class: com.home.fragment.dmx02.DMX02ModeFragment$$ExternalSyntheticLambda0
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                DMX02ModeFragment.this.m22lambda$initData$0$comhomefragmentdmx02DMX02ModeFragment(i, z, z2);
            }
        });
        this.myColor.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.6
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                DMX02ModeFragment.this.blackWiteSelectView.setStartColor(i);
                DMX02ModeFragment.this.updateRgbText(rgb, true);
                DMX02ModeFragment.this.r = rgb[0];
                DMX02ModeFragment.this.g = rgb[1];
                DMX02ModeFragment.this.b = rgb[2];
                DMX02ModeFragment.this.textRed.setText(DMX02ModeFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX02ModeFragment.this.r)));
                DMX02ModeFragment.this.textRed.setBackgroundColor(Color.rgb(DMX02ModeFragment.this.r, 0, 0));
                DMX02ModeFragment.this.textGreen.setText(DMX02ModeFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX02ModeFragment.this.g)));
                DMX02ModeFragment.this.textGreen.setBackgroundColor(Color.rgb(0, DMX02ModeFragment.this.g, 0));
                DMX02ModeFragment.this.tvBlue.setText(DMX02ModeFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX02ModeFragment.this.b)));
                DMX02ModeFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, DMX02ModeFragment.this.b));
            }
        });
        this.iv_switch.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX02ModeFragment.this.imageViewPicker.getVisibility() == 0) {
                    DMX02ModeFragment.this.iv_switch.setImageResource(R.drawable.bg_collor_picker);
                    DMX02ModeFragment.this.imageViewPicker.setVisibility(8);
                    DMX02ModeFragment.this.myColor.setVisibility(0);
                    return;
                }
                DMX02ModeFragment.this.iv_switch.setImageResource(R.drawable.collor_picker);
                DMX02ModeFragment.this.myColor.setVisibility(8);
                DMX02ModeFragment.this.imageViewPicker.setVisibility(0);
            }
        });
        this.blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.8
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                if (i2 <= 0) {
                    i2 = 1;
                } else if (i2 >= 100) {
                    i2 = 100;
                }
                if (DMX02ModeFragment.this.tvBrightness != null) {
                    DMX02ModeFragment.this.tvBrightness.setText(DMX02ModeFragment.this.getContext().getResources().getString(R.string.brightness_set, String.valueOf(i2)));
                }
                if (DMX02ModeFragment.this.mActivity != null) {
                    DMX02ModeFragment.this.mActivity.setBrightNess(i2, false, false, false);
                }
                MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                if (MainActivity_DMX02.sceneBean != null) {
                    Context context = DMX02ModeFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02ModeFragment");
                    SharePersistent.saveInt(context, sb.toString(), i2);
                }
            }
        });
        this.blackWiteSelectView.setProgress(100);
        this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
        if (MainActivity_DMX02.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_DMX02.sceneBean + "DMX02ModeFragment");
            if (i > 0) {
                this.blackWiteSelectView.setProgress(i);
                this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            }
        }
        for (int i2 = 1; i2 <= 12; i2++) {
            if (i2 <= 9) {
                List<String> list = this.gifListName;
                list.add(getString(R.string.Tab_Effect) + NetResult.CODE_OK + i2);
            } else {
                List<String> list2 = this.gifListName;
                list2.add(getString(R.string.Tab_Effect) + "" + i2);
            }
        }
        ImageAdapter imageAdapter = new ImageAdapter(getContext());
        this.imageAdapter = imageAdapter;
        this.gridView.setAdapter((ListAdapter) imageAdapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.9
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j) {
                Log.e("DMX02ModeFragment", "ModeDmxA1  :  " + i3);
                DMX02ModeFragment.this.imageAdapter.clearSelection(i3);
                DMX02ModeFragment.this.imageAdapter.notifyDataSetChanged();
                NetConnectBle.getInstance().setDmx02AnimationMode(i3 + 1);
            }
        });
        this.llAnimatePlay.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.startAnimation(view);
                if (DMX02ModeFragment.this.isPlay) {
                    DMX02ModeFragment.this.ivAnimatePlay.setImageResource(R.drawable.dmx02_pause);
                    DMX02ModeFragment.this.isPlay = false;
                    NetConnectBle.getInstance().setDmx02AnimationPlay(1);
                    return;
                }
                DMX02ModeFragment.this.ivAnimatePlay.setImageResource(R.drawable.dmx02_play);
                DMX02ModeFragment.this.isPlay = true;
                NetConnectBle.getInstance().setDmx02AnimationPlay(0);
            }
        });
        this.llAnimateUp.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.startAnimation(view);
                NetConnectBle.getInstance().setDmx02AnimationDirection(0);
                DMX02ModeFragment.this.ivAnimateUp.setImageResource(R.drawable.dmxa2_moveup_check);
                DMX02ModeFragment.this.ivAnimateDown.setImageResource(R.drawable.dmxa2_movedown);
                DMX02ModeFragment.this.ivAnimateLeft.setImageResource(R.drawable.dmxa2_moveleft);
                DMX02ModeFragment.this.ivAnimateRight.setImageResource(R.drawable.dmxa2_moveright);
            }
        });
        this.llAnimateDown.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.startAnimation(view);
                NetConnectBle.getInstance().setDmx02AnimationDirection(1);
                DMX02ModeFragment.this.ivAnimateUp.setImageResource(R.drawable.dmxa2_moveup);
                DMX02ModeFragment.this.ivAnimateDown.setImageResource(R.drawable.dmxa2_movedown_check);
                DMX02ModeFragment.this.ivAnimateLeft.setImageResource(R.drawable.dmxa2_moveleft);
                DMX02ModeFragment.this.ivAnimateRight.setImageResource(R.drawable.dmxa2_moveright);
            }
        });
        this.llAnimateLeft.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.startAnimation(view);
                NetConnectBle.getInstance().setDmx02AnimationDirection(3);
                DMX02ModeFragment.this.ivAnimateUp.setImageResource(R.drawable.dmxa2_moveup);
                DMX02ModeFragment.this.ivAnimateDown.setImageResource(R.drawable.dmxa2_movedown);
                DMX02ModeFragment.this.ivAnimateLeft.setImageResource(R.drawable.dmxa2_moveleft_check);
                DMX02ModeFragment.this.ivAnimateRight.setImageResource(R.drawable.dmxa2_moveright);
            }
        });
        this.llAnimateRight.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.startAnimation(view);
                NetConnectBle.getInstance().setDmx02AnimationDirection(2);
                DMX02ModeFragment.this.ivAnimateUp.setImageResource(R.drawable.dmxa2_moveup);
                DMX02ModeFragment.this.ivAnimateDown.setImageResource(R.drawable.dmxa2_movedown);
                DMX02ModeFragment.this.ivAnimateLeft.setImageResource(R.drawable.dmxa2_moveleft);
                DMX02ModeFragment.this.ivAnimateRight.setImageResource(R.drawable.dmxa2_moveright_check);
            }
        });
        this.llAnimateCycle.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.15
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.startAnimation(view);
                NetConnectBle.getInstance().setDmx02AnimationCycle(0);
            }
        });
        this.llAnimateColorchange.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.startAnimation(view);
                DMX02ModeFragment.this.mActivity.startActivityForResult(new Intent(DMX02ModeFragment.this.mActivity, Dmx02EditColorActivity.class), 1002);
            }
        });
        this.seekBarSpeedAnimation.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.17
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX02ModeFragment.this.mActivity == null || DMX02ModeFragment.this.textViewSpeedAnimation.getTag() == null) {
                    return;
                }
                if (DMX02ModeFragment.this.textViewSpeedAnimation.getTag().equals(100)) {
                    NetConnectBle.getInstance().setDmx02Speed(100);
                } else if (DMX02ModeFragment.this.textViewSpeedAnimation.getTag().equals(1)) {
                    NetConnectBle.getInstance().setDmx02Speed(1);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    if (i3 == 0) {
                        DMX02ModeFragment.this.textViewSpeedAnimation.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, String.valueOf(1)));
                        if (DMX02ModeFragment.this.mActivity != null) {
                            NetConnectBle.getInstance().setDmx02Speed(1);
                        }
                        DMX02ModeFragment.this.textViewSpeedAnimation.setTag(1);
                        return;
                    }
                    NetConnectBle.getInstance().setDmx02Speed(i3);
                    DMX02ModeFragment.this.textViewSpeedAnimation.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i3)));
                    DMX02ModeFragment.this.textViewSpeedAnimation.setTag(Integer.valueOf(i3));
                    Context context2 = DMX02ModeFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02ModeFragment");
                    sb.append("speed");
                    SharePersistent.saveInt(context2, sb.toString(), i3);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context2 = getContext();
            int i3 = SharePersistent.getInt(context2, MainActivity_DMX02.sceneBean + "DMX02ModeFragmentspeed");
            if (i3 > 0) {
                this.seekBarSpeedAnimation.setProgress(i3);
                this.textViewSpeedAnimation.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i3)));
            } else {
                this.seekBarSpeedAnimation.setProgress(80);
                this.textViewSpeedAnimation.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
            }
        }
        this.seekBarBrightAnimation.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.18
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX02ModeFragment.this.textViewBrightAnimation == null || DMX02ModeFragment.this.textViewBrightAnimation.getTag() == null) {
                    return;
                }
                if (DMX02ModeFragment.this.textViewBrightAnimation.getTag().equals(100)) {
                    if (DMX02ModeFragment.this.mActivity != null) {
                        NetConnectBle.getInstance().setDmx02Bright(100);
                    }
                } else if (!DMX02ModeFragment.this.textViewBrightAnimation.getTag().equals(1) || DMX02ModeFragment.this.mActivity == null) {
                } else {
                    NetConnectBle.getInstance().setDmx02Bright(1);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i4, boolean z) {
                if (z) {
                    if (i4 == 0) {
                        DMX02ModeFragment.this.textViewBrightAnimation.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(1)));
                        DMX02ModeFragment.this.textViewBrightAnimation.setTag(1);
                        if (DMX02ModeFragment.this.mActivity != null) {
                            NetConnectBle.getInstance().setDmx02Bright(1);
                            return;
                        }
                        return;
                    }
                    DMX02ModeFragment.this.textViewBrightAnimation.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(i4)));
                    DMX02ModeFragment.this.textViewBrightAnimation.setTag(Integer.valueOf(i4));
                    if (DMX02ModeFragment.this.mActivity != null) {
                        NetConnectBle.getInstance().setDmx02Bright(i4);
                    }
                    MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                    if (MainActivity_DMX02.sceneBean != null) {
                        Context context3 = DMX02ModeFragment.this.getContext();
                        StringBuilder sb = new StringBuilder();
                        MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                        sb.append(MainActivity_DMX02.sceneBean);
                        sb.append("DMX02ModeFragment");
                        sb.append("bright");
                        SharePersistent.saveInt(context3, sb.toString(), i4);
                    }
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context3 = getContext();
            int i4 = SharePersistent.getInt(context3, MainActivity_DMX02.sceneBean + "DMX02ModeFragmentbright");
            if (i4 > 0) {
                this.seekBarBrightAnimation.setProgress(i4);
                this.textViewBrightAnimation.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i4)));
            } else {
                this.seekBarBrightAnimation.setProgress(100);
                this.textViewBrightAnimation.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
            }
        }
        this.ivAddImage.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02ModeFragment.this.getUserImage(PictureMimeType.ofImage());
            }
        });
        PullAdapter pullAdapter = new PullAdapter();
        this.mPullAdapter = pullAdapter;
        this.streamList.setAdapter(pullAdapter);
        this.streamList.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.plrl.setOnRefreshListener(new PullLeftToRefreshLayout.OnRefreshListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.20
            @Override // com.home.view.custom.StreamList.PullLeftToRefreshLayout.OnRefreshListener
            public void onRefresh() {
            }
        });
        final int[] iArr = {R.drawable.dmxa2_fixed, R.drawable.dmxa2_moveleft, R.drawable.dmxa2_moveright, R.drawable.dmxa2_moveup, R.drawable.dmxa2_movedown, R.drawable.dmxa2_flicker};
        final int[] iArr2 = {R.drawable.dmxa2_fixed_check, R.drawable.dmxa2_moveleft_check, R.drawable.dmxa2_moveright_check, R.drawable.dmxa2_moveup_check, R.drawable.dmxa2_movedown_check, R.drawable.dmxa2_flicker_check};
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(DMX02ModeFragment.this.getActivity(), R.anim.layout_scale));
                TextView textView = (TextView) view;
                DMX02ModeFragment.this.animation = Integer.parseInt(textView.getText().toString()) - 1;
                NetConnectBle.getInstance().setDmx02CustomanimationMode(DMX02ModeFragment.this.animation);
                for (int i5 = 1; i5 <= iArr.length; i5++) {
                    String charSequence = textView.getText().toString();
                    if (!charSequence.equals("" + i5)) {
                        View view2 = DMX02ModeFragment.this.mContentView;
                        ((TextView) view2.findViewWithTag(r4 + i5)).setBackgroundResource(iArr[i5 - 1]);
                    } else {
                        textView.setBackgroundResource(iArr2[i5 - 1]);
                    }
                }
            }
        };
        for (int i5 = 1; i5 <= 6; i5++) {
            View view = this.mContentView;
            TextView textView = (TextView) view.findViewWithTag("viewAnimation" + i5);
            textView.setOnClickListener(onClickListener);
            textView.setTag("viewAnimation" + i5);
        }
        this.seekBarSpeedCustom.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.22
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX02ModeFragment.this.mActivity == null || DMX02ModeFragment.this.textViewBrightCustom.getTag() == null) {
                    return;
                }
                if (DMX02ModeFragment.this.textViewBrightCustom.getTag().equals(100)) {
                    NetConnectBle.getInstance().setDmx02Speed(100);
                } else if (DMX02ModeFragment.this.textViewBrightCustom.getTag().equals(1)) {
                    NetConnectBle.getInstance().setDmx02Speed(1);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i6, boolean z) {
                if (z) {
                    if (i6 == 0) {
                        DMX02ModeFragment.this.textViewBrightCustom.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, String.valueOf(1)));
                        if (DMX02ModeFragment.this.mActivity != null) {
                            NetConnectBle.getInstance().setDmx02Speed(1);
                        }
                        DMX02ModeFragment.this.textViewBrightCustom.setTag(1);
                        return;
                    }
                    NetConnectBle.getInstance().setDmx02Speed(i6);
                    DMX02ModeFragment.this.textViewBrightCustom.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i6)));
                    DMX02ModeFragment.this.textViewBrightCustom.setTag(Integer.valueOf(i6));
                    Context context4 = DMX02ModeFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02ModeFragment");
                    sb.append("speed");
                    SharePersistent.saveInt(context4, sb.toString(), i6);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context4 = getContext();
            int i6 = SharePersistent.getInt(context4, MainActivity_DMX02.sceneBean + "DMX02ModeFragmentspeed");
            if (i6 > 0) {
                this.seekBarSpeedCustom.setProgress(i6);
                this.textViewBrightCustom.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i6)));
            } else {
                this.seekBarSpeedCustom.setProgress(80);
                this.textViewBrightCustom.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
            }
        }
        this.tvSend.setOnClickListener(new AnonymousClass23());
        this.myColor_select = (MyColorPicker) this.menuView.findViewById(R.id.myColor_select);
        this.linearChouse_select = (LinearLayout) this.menuView.findViewById(R.id.linearChouse_select);
        this.textRed_select = (TextView) this.menuView.findViewById(R.id.textRed_select);
        this.textGreen_select = (TextView) this.menuView.findViewById(R.id.textGreen_select);
        this.tvBlue_select = (TextView) this.menuView.findViewById(R.id.tvBlue_select);
        this.iv_switch_select = (ImageView) this.menuView.findViewById(R.id.iv_switch_select);
        SegmentedRadioGroup segmentedRadioGroup2 = (SegmentedRadioGroup) this.menuView.findViewById(R.id.srgCover);
        this.srgCover = segmentedRadioGroup2;
        segmentedRadioGroup2.check(R.id.rbRing);
        this.srgCover.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.24
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i7) {
                if (R.id.rbRing == i7) {
                    DMX02ModeFragment.this.currentTab = 0;
                    DMX02ModeFragment.this.llRing.setVisibility(0);
                    DMX02ModeFragment.this.llCoverMode.setVisibility(8);
                } else if (R.id.rbModle == i7) {
                    DMX02ModeFragment.this.currentTab = 1;
                    DMX02ModeFragment.this.llRing.setVisibility(8);
                    DMX02ModeFragment.this.llCoverMode.setVisibility(0);
                } else if (R.id.rbVoiceControl == i7) {
                    DMX02ModeFragment.this.currentTab = 2;
                    DMX02ModeFragment.this.llRing.setVisibility(8);
                    DMX02ModeFragment.this.llCoverMode.setVisibility(8);
                }
            }
        });
        ImageView imageView = (ImageView) this.menuView.findViewById(R.id.backImage);
        this.backImage = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.25
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DMX02ModeFragment.this.hideColorCover();
            }
        });
        initSingColorView();
        initDiyColorBlock();
        initDiyRingView();
        initDiyModeView();
        initConfirmButton();
        setActive();
        if (LedBleApplication.getApp().getBleGattMap().size() > 0) {
            updateNewFindDevice();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initData$0$com-home-fragment-dmx02-DMX02ModeFragment  reason: not valid java name */
    public /* synthetic */ void m22lambda$initData$0$comhomefragmentdmx02DMX02ModeFragment(int i, boolean z, boolean z2) {
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

    /* renamed from: com.home.fragment.dmx02.DMX02ModeFragment$23  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass23 implements View.OnClickListener {
        AnonymousClass23() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DMX02ModeFragment.this.startAnimation(view);
            SharePersistent.savePerference(DMX02ModeFragment.this.mActivity, Constant.Activity, "DMX02ModeFragment");
            if (DMX02ModeFragment.this.cmdString == null) {
                return;
            }
            Log.e("DMX02ModeFragment", "cmdStringList.length: " + DMX02ModeFragment.this.cmdStringList.size());
            DMX02ModeFragment.this.receiveData = false;
            DMX02ModeFragment.this.index = -2;
            if (DMX02ModeFragment.this.progressBar != null) {
                DMX02ModeFragment.this.progressBar.setLevels(DMX02ModeFragment.this.cmdStringList.size());
            }
            DMX02ModeFragment.this.rlProgressView.setVisibility(0);
            DMX02ModeFragment.this.tvSend.setEnabled(false);
            if (DMX02ModeFragment.this.sendCmdHandler == null) {
                DMX02ModeFragment.this.sendCmdHandler = new Handler();
            }
            if (DMX02ModeFragment.this.sendCmdRunnable == null) {
                DMX02ModeFragment.this.sendCmdRunnable = new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.23.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DMX02ModeFragment.access$1208(DMX02ModeFragment.this);
                        DMX02ModeFragment.this.receiveData = false;
                        if (DMX02ModeFragment.this.index == DMX02ModeFragment.this.cmdStringList.size()) {
                            DMX02ModeFragment.this.progressBar.setCurrentLevel(0);
                            DMX02ModeFragment.this.tvProgress.setText("0%");
                            DMX02ModeFragment.this.rlProgressView.setVisibility(8);
                            DMX02ModeFragment.this.tvSend.setEnabled(true);
                            DMX02ModeFragment.this.sendCmdHandler.removeCallbacks(DMX02ModeFragment.this.sendCmdRunnable);
                            DMX02ModeFragment.this.overtimeHandler.removeCallbacks(DMX02ModeFragment.this.overtimeRunnable);
                            DMX02ModeFragment.this.index = -2;
                            return;
                        }
                        if (DMX02ModeFragment.this.progressBar != null) {
                            DMX02ModeFragment.this.progressBar.setCurrentLevel(DMX02ModeFragment.this.index + 1);
                            DMX02ModeFragment.this.progressBar.setAnimMaxTime(300);
                        }
                        if (DMX02ModeFragment.this.cmdStringList.size() > 0 && DMX02ModeFragment.this.tvProgress != null) {
                            DMX02ModeFragment.this.tvProgress.setText((((DMX02ModeFragment.this.index + 1) * 100) / DMX02ModeFragment.this.cmdStringList.size()) + "%");
                        }
                        if (DMX02ModeFragment.this.index == -1) {
                            int[] numb = DMX02ModeFragment.this.getNumb(DMX02ModeFragment.this.cmdString.length() / 2);
                            NetConnectBle.getInstance().sendDmx02DataToFFE1WithCallback(new int[]{90, 74, 1, 128, 12, 0, 1, 0, HttpStatus.SC_NO_CONTENT, 0, numb[0], numb[1], numb[2], numb[3], 0, 0, 0, 0});
                        } else if (DMX02ModeFragment.this.index >= 0) {
                            NetConnectBle.getInstance().sendDmx02DataToFFE2WithCallback(DMX02ModeFragment.HexStringToIntArray((DMX02ModeFragment.this.to04Hex(DMX02ModeFragment.this.index).substring(2) + DMX02ModeFragment.this.to04Hex(DMX02ModeFragment.this.index).substring(0, 2)) + "0000" + ((String) DMX02ModeFragment.this.cmdStringList.get(DMX02ModeFragment.this.index))));
                        }
                        if (DMX02ModeFragment.this.overtimeHandler == null) {
                            DMX02ModeFragment.this.overtimeHandler = new Handler();
                        }
                        if (DMX02ModeFragment.this.overtimeRunnable == null) {
                            DMX02ModeFragment.this.overtimeRunnable = new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.23.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    DMX02ModeFragment.access$2008(DMX02ModeFragment.this);
                                    if (DMX02ModeFragment.this.timeoutCount <= 3) {
                                        if (DMX02ModeFragment.this.receiveData) {
                                            return;
                                        }
                                        DMX02ModeFragment.this.index--;
                                        if (DMX02ModeFragment.this.rlProgressView != null && DMX02ModeFragment.this.tvSend != null) {
                                            DMX02ModeFragment.this.rlProgressView.setVisibility(0);
                                        }
                                        DMX02ModeFragment.this.sendCmdHandler.postDelayed(DMX02ModeFragment.this.sendCmdRunnable, 70L);
                                        return;
                                    }
                                    if (DMX02ModeFragment.this.rlProgressView != null && DMX02ModeFragment.this.tvSend != null) {
                                        DMX02ModeFragment.this.rlProgressView.setVisibility(8);
                                        DMX02ModeFragment.this.tvSend.setEnabled(true);
                                    }
                                    DMX02ModeFragment.this.sendCmdHandler.removeCallbacks(DMX02ModeFragment.this.sendCmdRunnable);
                                    DMX02ModeFragment.this.overtimeHandler.removeCallbacks(DMX02ModeFragment.this.overtimeRunnable);
                                    DMX02ModeFragment.this.index = -2;
                                    DMX02ModeFragment.this.timeoutCount = 0;
                                }
                            };
                        }
                        DMX02ModeFragment.this.overtimeHandler.postDelayed(DMX02ModeFragment.this.overtimeRunnable, 100L);
                    }
                };
            }
            DMX02ModeFragment.this.sendCmdHandler.postDelayed(DMX02ModeFragment.this.sendCmdRunnable, 10L);
        }
    }

    @Override // com.home.view.ActionSheet.ActionSheetListener
    public void onOtherButtonClick(ActionSheet actionSheet, int i, String str) {
        if (i != 0) {
            return;
        }
        getUserImage(PictureMimeType.ofImage());
    }

    public void getUserImage(int i) {
        if (PermissionChecker.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE")) {
            PictureFileUtils.deleteCacheDirFile(getContext(), i);
        } else {
            PermissionChecker.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        PictureSelector.create(getActivity()).openGallery(i).loadImageEngine(GlideEngine.createGlideEngine()).theme(2131821094).setPictureStyle(this.mPictureParameterStyle).setPictureCropStyle(this.mCropParameterStyle).maxSelectNum(1).minSelectNum(1).imageSpanCount(4).cameraFileName("").selectionMode(2).isSingleDirectReturn(false).previewImage(true).previewVideo(true).enablePreviewAudio(true).isCamera(true).isZoomAnim(true).enableCrop(true).compress(true).compressQuality(70).synOrAsy(true).withAspectRatio(1, 1).isGif(true).openClickSound(false).selectionMedia(this.selectList).isDragFrame(false).minimumCompressSize(1).cropWH(getPixSetLong(), getPixSetWidth()).rotateEnabled(false).scaleEnabled(true).forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        Bitmap decodeFile;
        File file;
        Bitmap bitmap;
        super.onActivityResult(i, i2, intent);
        if (i != 188) {
            if (i == 1002 && intent != null) {
                ArrayList<MyColor> arrayList = (ArrayList) intent.getSerializableExtra("color");
                this.colors = arrayList;
                if (arrayList != null) {
                    this.mActivity.setChangeColor(false, false, false, arrayList);
                    return;
                }
                return;
            }
            return;
        }
        this.selectList = PictureSelector.obtainMultipleResult(intent);
        this.cmdStringList.clear();
        for (int i3 = 0; i3 < this.selectList.size(); i3++) {
            if (SdkVersionUtils.checkedAndroid_Q()) {
                decodeFile = BitmapFactory.decodeFile(this.selectList.get(i3).getAndroidQToPath());
                file = new File(this.selectList.get(i3).getAndroidQToPath());
            } else {
                decodeFile = BitmapFactory.decodeFile(this.selectList.get(i3).getPath());
                file = new File(this.selectList.get(i3).getPath());
            }
            if (file.length() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED <= 1) {
                if (this.selectList.get(i3).getCompressPath().contains("gif") || this.selectList.get(i3).getCompressPath().contains("GIF")) {
                    this.bitmapsOfImg.clear();
                    this.bitmapsOfGif.clear();
                    this.streamList.setVisibility(8);
                    this.ivTopImageView.setVisibility(8);
                    this.ivTopGifImageView.setVisibility(0);
                    Glide.with(getContext()).load(this.selectList.get(i3).getCompressPath()).into(this.ivTopGifImageView);
                    Glide.with(getContext()).asGif().load(this.selectList.get(i3).getCompressPath()).into((RequestBuilder<GifDrawable>) new SimpleTarget<GifDrawable>() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.26
                        @Override // com.bumptech.glide.request.target.Target
                        public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                            onResourceReady((GifDrawable) obj, (Transition<? super GifDrawable>) transition);
                        }

                        public void onResourceReady(GifDrawable gifDrawable, Transition<? super GifDrawable> transition) {
                            try {
                                Drawable.ConstantState constantState = gifDrawable.getConstantState();
                                Field declaredField = constantState.getClass().getDeclaredField("frameLoader");
                                declaredField.setAccessible(true);
                                Object obj = declaredField.get(constantState);
                                Field declaredField2 = obj.getClass().getDeclaredField("gifDecoder");
                                declaredField2.setAccessible(true);
                                StandardGifDecoder standardGifDecoder = (StandardGifDecoder) declaredField2.get(obj);
                                for (int i4 = 0; i4 < standardGifDecoder.getFrameCount(); i4++) {
                                    standardGifDecoder.advance();
                                    DMX02ModeFragment.this.bitmapsOfGif.add(DMX02ModeFragment.scale(standardGifDecoder.getNextFrame(), DMX02ModeFragment.this.getPixSetLong(), DMX02ModeFragment.this.getPixSetWidth()));
                                }
                                DMX02ModeFragment dMX02ModeFragment = DMX02ModeFragment.this;
                                dMX02ModeFragment.getCmdStringList(dMX02ModeFragment.bitmapsOfGif);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    this.streamList.setVisibility(0);
                    this.ivTopImageView.setVisibility(0);
                    this.ivTopGifImageView.setVisibility(8);
                    this.ivTopImageView.setImageBitmap(scale(decodeFile, getPixSetLong(), getPixSetWidth()));
                    this.bitmapsOfGif.clear();
                    if (this.ivTopImageView.getDrawable() instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) this.ivTopImageView.getDrawable()).getBitmap();
                    } else {
                        Drawable drawable = this.ivTopImageView.getDrawable();
                        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        drawable.draw(new Canvas(createBitmap));
                        bitmap = createBitmap;
                    }
                    if (bitmap != null) {
                        this.bitmapsOfImg.add(scale(bitmap, getPixSetLong(), getPixSetWidth()));
                        this.mPullAdapter.notifyDataSetChanged();
                    }
                    getCmdStringList(this.bitmapsOfImg);
                }
            } else {
                Toast.makeText(this.activity, (int) R.string.picture, 0).show();
            }
        }
        for (LocalMedia localMedia : this.selectList) {
            Log.i("DMX02ModeFragment", "压缩---->" + localMedia.getCompressPath());
            Log.i("DMX02ModeFragment", "原图---->" + localMedia.getPath());
            Log.i("DMX02ModeFragment", "裁剪---->" + localMedia.getCutPath());
            Log.i("DMX02ModeFragment", "Android Q 特有Path---->" + localMedia.getAndroidQToPath());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap scale(Bitmap bitmap, double d, double d2) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) d) / width, ((float) d2) / height);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCmdStringList(ArrayList<Bitmap> arrayList) {
        String substring;
        this.cmdString = "";
        this.cmdStringList.clear();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("5a4a494d");
        stringBuffer.append("0100");
        stringBuffer.append("0000");
        stringBuffer.append("6c65642d30310000000000000000000000000000000000000000000000000000");
        stringBuffer.append("01");
        stringBuffer.append("01");
        stringBuffer.append("00");
        stringBuffer.append("00");
        StringBuilder sb = new StringBuilder();
        sb.append(to04Hex(getPixSetLong()).substring(2));
        sb.append(to04Hex(getPixSetLong()).substring(0, 2));
        stringBuffer.append(sb.toString());
        stringBuffer.append(to04Hex(getPixSetWidth()).substring(2) + to04Hex(getPixSetWidth()).substring(0, 2));
        stringBuffer.append("0000");
        stringBuffer.append("0000");
        stringBuffer.append("6400");
        stringBuffer.append(to02Hex(arrayList.size()));
        stringBuffer.append("00");
        stringBuffer.append("000000ff");
        stringBuffer.append("00000000");
        this.cmdString = stringBuffer.toString();
        for (int i = 0; i < arrayList.size(); i++) {
            Bitmap bitmap = arrayList.get(i);
            StringBuffer stringBuffer2 = new StringBuffer();
            for (int i2 = 0; i2 < bitmap.getHeight(); i2++) {
                for (int i3 = 0; i3 < bitmap.getWidth(); i3++) {
                    int pixel = bitmap.getPixel(i3, i2);
                    Color.alpha(pixel);
                    stringBuffer2.append(get565RgbHex(Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
                }
            }
            stringBuffer.append(stringBuffer2);
            this.cmdString = stringBuffer.toString();
        }
        int length = ((this.cmdString.length() / 2) + 199) / 200;
        for (int i4 = 0; i4 < length; i4++) {
            if (i4 == length - 1) {
                substring = this.cmdString.substring(i4 * HttpStatus.SC_BAD_REQUEST);
            } else {
                substring = this.cmdString.substring(i4 * HttpStatus.SC_BAD_REQUEST, (i4 + 1) * HttpStatus.SC_BAD_REQUEST);
            }
            this.cmdStringList.add(substring);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPixSetLong() {
        Context context = getContext();
        String perference = SharePersistent.getPerference(context, LedBleApplication.getApp().getSceneBean() + "btnSetPixLong");
        if (perference == null || perference.length() <= 0) {
            SharePersistent.savePerference(getContext(), "DMX02-PIX-StringbtnSetPixLong", "16");
            perference = "16";
        }
        return Integer.parseInt(perference);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPixSetWidth() {
        Context context = getContext();
        String perference = SharePersistent.getPerference(context, LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth");
        if (perference == null || perference.length() <= 0) {
            SharePersistent.savePerference(getContext(), "DMX02-PIX-StringbtnSetPixWidth", "16");
            perference = "16";
        }
        return Integer.parseInt(perference);
    }

    private String to02Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 2 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String to04Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 4 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    private String get565RgbHex(int i, int i2, int i3) {
        String binaryString = Integer.toBinaryString(i);
        int length = binaryString.length();
        for (int i4 = 0; i4 < 8 - length; i4++) {
            binaryString = NetResult.CODE_OK + binaryString;
        }
        String substring = binaryString.substring(0, 5);
        String binaryString2 = Integer.toBinaryString(i2);
        int length2 = binaryString2.length();
        for (int i5 = 0; i5 < 8 - length2; i5++) {
            binaryString2 = NetResult.CODE_OK + binaryString2;
        }
        String substring2 = binaryString2.substring(0, 6);
        String binaryString3 = Integer.toBinaryString(i3);
        int length3 = binaryString3.length();
        for (int i6 = 0; i6 < 8 - length3; i6++) {
            binaryString3 = NetResult.CODE_OK + binaryString3;
        }
        String hexString = Integer.toHexString(Integer.parseInt(substring + substring2 + binaryString3.substring(0, 5), 2));
        int length4 = hexString.length();
        for (int i7 = 0; i7 < 4 - length4; i7++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString.substring(2) + hexString.substring(0, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getNumb(int i) {
        int[] iArr = new int[4];
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 8 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        String substring = hexString.substring(4);
        String substring2 = hexString.substring(0, 4);
        iArr[0] = Integer.parseInt(substring.substring(2), 16);
        iArr[1] = Integer.parseInt(substring.substring(0, 2), 16);
        iArr[2] = Integer.parseInt(substring2.substring(2), 16);
        iArr[3] = Integer.parseInt(substring2.substring(0, 2), 16);
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] HexStringToIntArray(String str) {
        int length = str.length() / 2;
        String[] strArr = new String[length];
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int i3 = i2 + 2;
            strArr[i] = str.substring(i2, i3);
            iArr[i] = Integer.parseInt(str.substring(i2, i3), 16);
        }
        return iArr;
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
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.27
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(DMX02ModeFragment.this.getActivity(), R.anim.layout_scale));
                int intValue = ((Integer) view.getTag()).intValue();
                DMX02ModeFragment.this.updateRgbText(Tool.getRGB(intValue), false);
                DMX02ModeFragment.this.blackWiteSelectView.setStartColor(intValue);
                DMX02ModeFragment.this.imageViewPicker.setInitialColor(intValue);
                int[] rgb = Tool.getRGB(intValue);
                DMX02ModeFragment.this.r = rgb[0];
                DMX02ModeFragment.this.g = rgb[1];
                DMX02ModeFragment.this.b = rgb[2];
                DMX02ModeFragment.this.textRed.setText(DMX02ModeFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX02ModeFragment.this.r)));
                DMX02ModeFragment.this.textRed.setBackgroundColor(Color.rgb(DMX02ModeFragment.this.r, 0, 0));
                DMX02ModeFragment.this.textGreen.setText(DMX02ModeFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX02ModeFragment.this.g)));
                DMX02ModeFragment.this.textGreen.setBackgroundColor(Color.rgb(0, DMX02ModeFragment.this.g, 0));
                DMX02ModeFragment.this.tvBlue.setText(DMX02ModeFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX02ModeFragment.this.b)));
                DMX02ModeFragment.this.tvBlue.setBackgroundColor(Color.rgb(0, 0, DMX02ModeFragment.this.b));
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
        int i;
        View findViewById = this.mContentView.findViewById(R.id.linarLayoutColorCile);
        for (int i2 = 1; i2 <= 6; i2++) {
            final ColorTextView colorTextView = (ColorTextView) findViewById.findViewWithTag("diyColor" + i2);
            String str = (String) colorTextView.getTag();
            SharedPreferences sharedPreferences = this.sharedPreferences;
            if (sharedPreferences != null && (i = sharedPreferences.getInt(str, 0)) != 0) {
                if (i < 128) {
                    float f = 10;
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                    shapeDrawable.getPaint().setColor(i);
                    shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                    colorTextView.setBackgroundDrawable(shapeDrawable);
                    colorTextView.setColor(i);
                } else {
                    Drawable image = getImage(String.valueOf(i));
                    if (image != null) {
                        colorTextView.setBackgroundDrawable(image);
                        colorTextView.setColor(i);
                    }
                }
                colorTextView.setText("");
            }
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.28
                /* JADX WARN: Removed duplicated region for block: B:26:0x01c5  */
                /* JADX WARN: Removed duplicated region for block: B:28:0x01db  */
                /* JADX WARN: Removed duplicated region for block: B:29:0x01ea  */
                /* JADX WARN: Removed duplicated region for block: B:50:0x037f  */
                /* JADX WARN: Removed duplicated region for block: B:68:0x04ff  */
                /* JADX WARN: Removed duplicated region for block: B:70:0x050a  */
                /* JADX WARN: Removed duplicated region for block: B:71:0x0518  */
                /* JADX WARN: Removed duplicated region for block: B:74:0x0529  */
                /* JADX WARN: Removed duplicated region for block: B:75:0x0537  */
                @Override // android.view.View.OnClickListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void onClick(View view) {
                    final int i3;
                    final int i4;
                    final int i5;
                    view.startAnimation(AnimationUtils.loadAnimation(DMX02ModeFragment.this.getActivity(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    DMX02ModeFragment.this.diyViewTag = (String) colorTextView.getTag();
                    if (color == 0) {
                        DMX02ModeFragment.this.showColorCover((ColorTextView) view, true);
                    } else if (color >= 128) {
                        if (DMX02ModeFragment.this.mActivity != null) {
                            MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                            if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                                MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                                if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                                    MainActivity_DMX02 unused3 = DMX02ModeFragment.this.mActivity;
                                    if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                        MainActivity_DMX02 unused4 = DMX02ModeFragment.this.mActivity;
                                        if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                            MainActivity_DMX02 unused5 = DMX02ModeFragment.this.mActivity;
                                            if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                                                i3 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi);
                                            }
                                        } else {
                                            i3 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE);
                                        }
                                    } else {
                                        i3 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART);
                                    }
                                } else {
                                    i3 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDDMX, DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-");
                                }
                            } else {
                                i3 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE);
                            }
                            if (DMX02ModeFragment.this.mActivity != null) {
                                MainActivity_DMX02 unused6 = DMX02ModeFragment.this.mActivity;
                                if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                                    MainActivity_DMX02 unused7 = DMX02ModeFragment.this.mActivity;
                                    if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                                        MainActivity_DMX02 unused8 = DMX02ModeFragment.this.mActivity;
                                        if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                            MainActivity_DMX02 unused9 = DMX02ModeFragment.this.mActivity;
                                            if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                                MainActivity_DMX02 unused10 = DMX02ModeFragment.this.mActivity;
                                                if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                                                    i4 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDWiFi);
                                                }
                                            } else {
                                                i4 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDSTAGE);
                                            }
                                        } else {
                                            i4 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDSMART, DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDSMART);
                                        }
                                    } else {
                                        i4 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDDMX, DMX02ModeFragment.this.diyViewTag + "speedLEDDMX-01-");
                                    }
                                } else {
                                    i4 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDBLE, DMX02ModeFragment.this.diyViewTag + "speed" + CommonConstant.LEDBLE);
                                }
                                if (DMX02ModeFragment.this.mActivity != null) {
                                    DMX02ModeFragment.this.mActivity.setRegModeNoInterval(color, false);
                                }
                                if (i3 == 0) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.28.3
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (DMX02ModeFragment.this.mActivity != null) {
                                                DMX02ModeFragment.this.mActivity.setBrightNessNoInterval(100, false, false);
                                            }
                                            handler.removeCallbacksAndMessages(null);
                                        }
                                    }, 100L);
                                } else {
                                    final Handler handler2 = new Handler();
                                    handler2.postDelayed(new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.28.4
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (DMX02ModeFragment.this.mActivity != null) {
                                                DMX02ModeFragment.this.mActivity.setBrightNessNoInterval(i3, false, false);
                                            }
                                            handler2.removeCallbacksAndMessages(null);
                                        }
                                    }, 100L);
                                }
                                if (i4 == 0) {
                                    final Handler handler3 = new Handler();
                                    handler3.postDelayed(new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.28.5
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (DMX02ModeFragment.this.mActivity != null) {
                                                DMX02ModeFragment.this.mActivity.setSpeedNoInterval(85);
                                            }
                                            handler3.removeCallbacksAndMessages(null);
                                        }
                                    }, 200L);
                                    return;
                                }
                                final Handler handler4 = new Handler();
                                handler4.postDelayed(new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.28.6
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (DMX02ModeFragment.this.mActivity != null) {
                                            DMX02ModeFragment.this.mActivity.setSpeedNoInterval(i4);
                                        }
                                        handler4.removeCallbacksAndMessages(null);
                                    }
                                }, 200L);
                                return;
                            }
                            i4 = 0;
                            if (DMX02ModeFragment.this.mActivity != null) {
                            }
                            if (i3 == 0) {
                            }
                            if (i4 == 0) {
                            }
                        }
                        i3 = 0;
                        if (DMX02ModeFragment.this.mActivity != null) {
                        }
                        i4 = 0;
                        if (DMX02ModeFragment.this.mActivity != null) {
                        }
                        if (i3 == 0) {
                        }
                        if (i4 == 0) {
                        }
                    } else {
                        int[] rgb = Tool.getRGB(color);
                        if (DMX02ModeFragment.this.mActivity != null) {
                            MainActivity_DMX02 unused11 = DMX02ModeFragment.this.mActivity;
                            if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                                MainActivity_DMX02 unused12 = DMX02ModeFragment.this.mActivity;
                                if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                                    MainActivity_DMX02 unused13 = DMX02ModeFragment.this.mActivity;
                                    if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                        MainActivity_DMX02 unused14 = DMX02ModeFragment.this.mActivity;
                                        if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                            MainActivity_DMX02 unused15 = DMX02ModeFragment.this.mActivity;
                                            if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                                                i5 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi);
                                            }
                                        } else {
                                            i5 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE);
                                        }
                                    } else {
                                        i5 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART);
                                    }
                                } else {
                                    i5 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDDMX, DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-");
                                }
                            } else {
                                i5 = SharePersistent.getBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE);
                            }
                            if (DMX02ModeFragment.this.mActivity != null) {
                                DMX02ModeFragment.this.mActivity.setRgb(rgb[0], rgb[1], rgb[2], false, false, false, false);
                            }
                            if (i5 != 0) {
                                final Handler handler5 = new Handler();
                                handler5.postDelayed(new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.28.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (DMX02ModeFragment.this.mActivity != null) {
                                            DMX02ModeFragment.this.mActivity.setBrightNess(100, false, false, false);
                                        }
                                        handler5.removeCallbacksAndMessages(null);
                                    }
                                }, 100L);
                                return;
                            }
                            final Handler handler6 = new Handler();
                            handler6.postDelayed(new Runnable() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.28.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DMX02ModeFragment.this.mActivity != null) {
                                        DMX02ModeFragment.this.mActivity.setBrightNess(i5, false, false, false);
                                    }
                                    handler6.removeCallbacksAndMessages(null);
                                }
                            }, 100L);
                            return;
                        }
                        i5 = 0;
                        if (DMX02ModeFragment.this.mActivity != null) {
                        }
                        if (i5 != 0) {
                        }
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.29
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    DMX02ModeFragment.this.sharedPreferences.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(DMX02ModeFragment.this.getActivity().getResources().getDrawable(R.drawable.block_shap_color));
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
        if (z) {
            if (MainActivity_DMX02.getSceneBean() != null) {
                if (MainActivity_DMX02.getSceneBean().contains(CommonConstant.LEDBLE) || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDCAR-01-") && MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDCAR-01-"))) {
                    this.srgCover.setVisibility(0);
                } else {
                    this.srgCover.setVisibility(8);
                }
            } else {
                this.srgCover.setVisibility(8);
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
        this.imageViewPicker2.subscribe(new ColorObserver() { // from class: com.home.fragment.dmx02.DMX02ModeFragment$$ExternalSyntheticLambda1
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                DMX02ModeFragment.this.m23x3b1e4912(i, z, z2);
            }
        });
        this.myColor_select.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.30
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                DMX02ModeFragment.this.blackWiteSelectView2.setStartColor(i);
                DMX02ModeFragment.this.currentSelecColorFromPicker = i;
                DMX02ModeFragment.this.updateRgbText(rgb, true);
                DMX02ModeFragment.this.select_r = rgb[0];
                DMX02ModeFragment.this.select_g = rgb[1];
                DMX02ModeFragment.this.select_b = rgb[2];
                DMX02ModeFragment.this.textRed_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX02ModeFragment.this.select_r)));
                DMX02ModeFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX02ModeFragment.this.select_r, 0, 0));
                DMX02ModeFragment.this.textGreen_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX02ModeFragment.this.select_g)));
                DMX02ModeFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX02ModeFragment.this.select_g, 0));
                DMX02ModeFragment.this.tvBlue_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX02ModeFragment.this.select_b)));
                DMX02ModeFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX02ModeFragment.this.select_b));
                if (DMX02ModeFragment.this.mActivity != null) {
                    MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                    if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                        MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                        if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                            MainActivity_DMX02 unused3 = DMX02ModeFragment.this.mActivity;
                            if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                MainActivity_DMX02 unused4 = DMX02ModeFragment.this.mActivity;
                                if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                    MainActivity_DMX02 unused5 = DMX02ModeFragment.this.mActivity;
                                    if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                                        SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.brightnessValue);
                                        return;
                                    }
                                    return;
                                }
                                SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.brightnessValue);
                                return;
                            }
                            SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, DMX02ModeFragment.this.brightnessValue);
                            return;
                        }
                        SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-", DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-", DMX02ModeFragment.this.brightnessValue);
                        return;
                    }
                    SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, DMX02ModeFragment.this.brightnessValue);
                }
            }
        });
        this.iv_switch_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX02ModeFragment.this.imageViewPicker2.getVisibility() == 0) {
                    DMX02ModeFragment.this.iv_switch_select.setImageResource(R.drawable.bg_collor_picker);
                    DMX02ModeFragment.this.imageViewPicker2.setVisibility(8);
                    DMX02ModeFragment.this.myColor_select.setVisibility(0);
                    return;
                }
                DMX02ModeFragment.this.iv_switch_select.setImageResource(R.drawable.collor_picker);
                DMX02ModeFragment.this.myColor_select.setVisibility(8);
                DMX02ModeFragment.this.imageViewPicker2.setVisibility(0);
            }
        });
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.select_r, this.select_g, this.select_b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.32
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                DMX02ModeFragment.this.blackWiteSelectView2.setStartColor(i);
                DMX02ModeFragment.this.currentSelecColorFromPicker = i;
                DMX02ModeFragment.this.select_r = Color.red(i);
                DMX02ModeFragment.this.select_g = Color.green(i);
                DMX02ModeFragment.this.select_b = Color.blue(i);
                DMX02ModeFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX02ModeFragment.this.select_r, 0, 0));
                DMX02ModeFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX02ModeFragment.this.select_g, 0));
                DMX02ModeFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX02ModeFragment.this.select_b));
                DMX02ModeFragment.this.textRed_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX02ModeFragment.this.select_r)));
                DMX02ModeFragment.this.textGreen_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX02ModeFragment.this.select_g)));
                DMX02ModeFragment.this.tvBlue_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX02ModeFragment.this.select_b)));
                DMX02ModeFragment.this.updateRgbText(Tool.getRGB(i), true);
                if (DMX02ModeFragment.this.mActivity != null) {
                    MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                    if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                        MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                        if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                            MainActivity_DMX02 unused3 = DMX02ModeFragment.this.mActivity;
                            if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                MainActivity_DMX02 unused4 = DMX02ModeFragment.this.mActivity;
                                if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                    MainActivity_DMX02 unused5 = DMX02ModeFragment.this.mActivity;
                                    if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                                        SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.brightnessValue);
                                        return;
                                    }
                                    return;
                                }
                                SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.brightnessValue);
                                return;
                            }
                            SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, DMX02ModeFragment.this.brightnessValue);
                            return;
                        }
                        SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-", DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-", DMX02ModeFragment.this.brightnessValue);
                        return;
                    }
                    SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, DMX02ModeFragment.this.brightnessValue);
                }
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.33
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(DMX02ModeFragment.this.select_r, DMX02ModeFragment.this.select_g, DMX02ModeFragment.this.select_b);
                colorPicker.show();
            }
        });
        this.textViewRingBrightSC = (TextView) this.menuView.findViewById(R.id.tvRingBrightnessSC);
        BlackWiteSelectView blackWiteSelectView = (BlackWiteSelectView) this.menuView.findViewById(R.id.blackWiteSelectView2);
        this.blackWiteSelectView2 = blackWiteSelectView;
        blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.34
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                int i3 = i2 <= 0 ? 1 : i2;
                if (i2 >= 100) {
                    i3 = 100;
                }
                DMX02ModeFragment.this.brightnessValue = i3;
                DMX02ModeFragment.this.textViewRingBrightSC.setText(DMX02ModeFragment.this.getResources().getString(R.string.brightness_set, Integer.valueOf(i3)));
                if (DMX02ModeFragment.this.mActivity != null) {
                    DMX02ModeFragment.this.mActivity.setBrightNess(i3, false, false, false);
                    MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                    if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                        MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                        if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                            MainActivity_DMX02 unused3 = DMX02ModeFragment.this.mActivity;
                            if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                                MainActivity_DMX02 unused4 = DMX02ModeFragment.this.mActivity;
                                if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                                    MainActivity_DMX02 unused5 = DMX02ModeFragment.this.mActivity;
                                    if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                                        SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDWiFi, i3);
                                    }
                                } else {
                                    SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, i3);
                                }
                            } else {
                                SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDSMART, i3);
                            }
                        } else {
                            SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-", DMX02ModeFragment.this.diyViewTag + "brightLEDDMX-01-", i3);
                        }
                    } else {
                        SharePersistent.saveBrightData(DMX02ModeFragment.this.getActivity(), DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, DMX02ModeFragment.this.diyViewTag + "bright" + CommonConstant.LEDBLE, i3);
                    }
                }
                MainActivity_DMX02 unused6 = DMX02ModeFragment.this.mActivity;
                if (MainActivity_DMX02.sceneBean != null) {
                    Context context = DMX02ModeFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused7 = DMX02ModeFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02ModeFragment");
                    sb.append("DIY");
                    SharePersistent.saveInt(context, sb.toString(), i3);
                }
            }
        });
        this.blackWiteSelectView2.setProgress(100);
        this.textViewRingBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
        if (MainActivity_DMX02.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_DMX02.sceneBean + "DMX02ModeFragmentDIY");
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
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.35
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(DMX02ModeFragment.this.getActivity(), R.anim.layout_scale));
                    int intValue = ((Integer) view.getTag()).intValue();
                    DMX02ModeFragment.this.currentSelecColorFromPicker = intValue;
                    DMX02ModeFragment.this.blackWiteSelectView2.setStartColor(intValue);
                    DMX02ModeFragment.this.imageViewPicker2.setInitialColor(intValue);
                    int[] rgb = Tool.getRGB(intValue);
                    DMX02ModeFragment.this.updateRgbText(rgb, false);
                    DMX02ModeFragment.this.select_r = rgb[0];
                    DMX02ModeFragment.this.select_g = rgb[1];
                    DMX02ModeFragment.this.select_b = rgb[2];
                    DMX02ModeFragment.this.textRed_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.red, Integer.valueOf(DMX02ModeFragment.this.select_r)));
                    DMX02ModeFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX02ModeFragment.this.select_r, 0, 0));
                    DMX02ModeFragment.this.textGreen_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.green, Integer.valueOf(DMX02ModeFragment.this.select_g)));
                    DMX02ModeFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX02ModeFragment.this.select_g, 0));
                    DMX02ModeFragment.this.tvBlue_select.setText(DMX02ModeFragment.this.getResources().getString(R.string.blue, Integer.valueOf(DMX02ModeFragment.this.select_b)));
                    DMX02ModeFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX02ModeFragment.this.select_b));
                    DMX02ModeFragment.this.updateRgbText(Tool.getRGB(intValue), false);
                }
            });
            arrayList.add(findViewWithTag);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initDiyRingView$1$com-home-fragment-dmx02-DMX02ModeFragment  reason: not valid java name */
    public /* synthetic */ void m23x3b1e4912(int i, boolean z, boolean z2) {
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
                if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                    SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright" + CommonConstant.LEDBLE, this.diyViewTag + "bright" + CommonConstant.LEDBLE, this.brightnessValue);
                } else if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                    SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "brightLEDDMX-01-", this.diyViewTag + "brightLEDDMX-01-", this.brightnessValue);
                } else if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
                    SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright" + CommonConstant.LEDSMART, this.diyViewTag + "bright" + CommonConstant.LEDSMART, this.brightnessValue);
                } else if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                    SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, this.diyViewTag + "bright" + CommonConstant.LEDSTAGE, this.brightnessValue);
                } else if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright" + CommonConstant.LEDWiFi, this.diyViewTag + "bright" + CommonConstant.LEDWiFi, this.brightnessValue);
                }
            }
        }
    }

    private void initDiyModeView() {
        this.llCoverMode = (LinearLayout) this.menuView.findViewById(R.id.llCoverMode);
        this.seekBarModeSC = (SeekBar) this.menuView.findViewById(R.id.seekBarModeSC);
        this.textViewModeSC = (TextView) this.menuView.findViewById(R.id.textViewModeSC);
        if (this.mActivity != null && (MainActivity_DMX02.getSceneBean().contains(CommonConstant.LEDBLE) || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDCAR-01-"))) {
            this.seekBarModeSC.setVisibility(8);
            this.textViewModeSC.setVisibility(8);
        }
        WheelPicker wheelPicker = (WheelPicker) this.menuView.findViewById(R.id.wheelPicker_tang);
        this.wheelPicker_tang = wheelPicker;
        if (this.mActivity != null) {
            wheelPicker.setData(bleModel());
        }
        this.wheelPicker_tang.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.36
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker2, Object obj, int i) {
                if (i >= 0) {
                    Log.e("DMX02ModeFragment", "onItemSelected: " + i);
                    DMX02ModeFragment dMX02ModeFragment = DMX02ModeFragment.this;
                    dMX02ModeFragment.currentSelecColorFromPicker = Integer.parseInt(((String) dMX02ModeFragment.listNubmer.get(i)).trim());
                    if (DMX02ModeFragment.this.mActivity != null) {
                        DMX02ModeFragment.this.mActivity.setRegMode(Integer.parseInt(((String) DMX02ModeFragment.this.listNubmer.get(i)).trim()), false);
                    }
                    if (DMX02ModeFragment.this.seekBarModeSC != null) {
                        DMX02ModeFragment.this.seekBarModeSC.setProgress(i);
                        SeekBar seekBar = DMX02ModeFragment.this.seekBarModeSC;
                        seekBar.setTag("" + DMX02ModeFragment.this.currentSelecColorFromPicker);
                    }
                }
            }
        });
        this.seekBarModeSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.37
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                List data = DMX02ModeFragment.this.wheelPicker_tang.getData();
                TextView textView = DMX02ModeFragment.this.textViewModeSC;
                textView.setText("" + data.get(i));
                if (DMX02ModeFragment.this.mActivity == null || i < 0 || i > 28) {
                    return;
                }
                DMX02ModeFragment.this.seekBarModeSC.setTag(((String) DMX02ModeFragment.this.listNubmer.get(i)).trim());
                DMX02ModeFragment dMX02ModeFragment = DMX02ModeFragment.this;
                dMX02ModeFragment.currentSelecColorFromPicker = Integer.parseInt(((String) dMX02ModeFragment.listNubmer.get(i)).trim());
                DMX02ModeFragment.this.wheelPicker_tang.setSelectedItemPosition(i);
                DMX02ModeFragment.this.mActivity.setRegMode(Integer.parseInt(((String) DMX02ModeFragment.this.listNubmer.get(i)).trim()), false);
            }
        });
        this.seekBarBrightBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarBrightNess);
        this.textViewBrightSC = (TextView) this.menuView.findViewById(R.id.textViewBrightNess);
        this.seekBarBrightBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.38
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i == 0) {
                    DMX02ModeFragment.this.textViewBrightSC.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, 1));
                    if (DMX02ModeFragment.this.mActivity != null) {
                        DMX02ModeFragment.this.mActivity.setBrightNess(1, false, false, false);
                        FragmentActivity activity = DMX02ModeFragment.this.getActivity();
                        StringBuilder sb = new StringBuilder();
                        sb.append(DMX02ModeFragment.this.diyViewTag);
                        sb.append("bright");
                        MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                        sb.append(MainActivity_DMX02.sceneBean);
                        String sb2 = sb.toString();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(DMX02ModeFragment.this.diyViewTag);
                        sb3.append("bright");
                        MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                        sb3.append(MainActivity_DMX02.sceneBean);
                        SharePersistent.saveBrightData(activity, sb2, sb3.toString(), 1);
                    }
                } else {
                    DMX02ModeFragment.this.textViewBrightSC.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, Integer.valueOf(i)));
                    if (DMX02ModeFragment.this.mActivity != null) {
                        DMX02ModeFragment.this.mActivity.setBrightNess(i, false, false, false);
                        FragmentActivity activity2 = DMX02ModeFragment.this.getActivity();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(DMX02ModeFragment.this.diyViewTag);
                        sb4.append("bright");
                        MainActivity_DMX02 unused3 = DMX02ModeFragment.this.mActivity;
                        sb4.append(MainActivity_DMX02.sceneBean);
                        String sb5 = sb4.toString();
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(DMX02ModeFragment.this.diyViewTag);
                        sb6.append("bright");
                        MainActivity_DMX02 unused4 = DMX02ModeFragment.this.mActivity;
                        sb6.append(MainActivity_DMX02.sceneBean);
                        SharePersistent.saveBrightData(activity2, sb5, sb6.toString(), i);
                    }
                }
                MainActivity_DMX02 unused5 = DMX02ModeFragment.this.mActivity;
                if (MainActivity_DMX02.sceneBean != null) {
                    Log.e("DMX02ModeFragment", "progress = " + i);
                    Context context = DMX02ModeFragment.this.getContext();
                    StringBuilder sb7 = new StringBuilder();
                    MainActivity_DMX02 unused6 = DMX02ModeFragment.this.mActivity;
                    sb7.append(MainActivity_DMX02.sceneBean);
                    sb7.append("DMX02ModeFragment");
                    sb7.append("DIYbright");
                    SharePersistent.saveInt(context, sb7.toString(), i);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_DMX02.sceneBean + "DMX02ModeFragmentDIYbright");
            if (i > 0) {
                this.seekBarBrightBarSC.setProgress(i);
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            } else {
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
            }
        }
        this.seekBarSpeedBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarSpeedSC);
        this.textViewSpeedSC = (TextView) this.menuView.findViewById(R.id.textViewSpeedSC);
        this.seekBarSpeedBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.39
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (i2 == 0) {
                    if (DMX02ModeFragment.this.mActivity != null) {
                        DMX02ModeFragment.this.mActivity.setSpeed(1, false, false);
                        FragmentActivity activity = DMX02ModeFragment.this.getActivity();
                        StringBuilder sb = new StringBuilder();
                        sb.append(DMX02ModeFragment.this.diyViewTag);
                        sb.append("speed");
                        MainActivity_DMX02 unused = DMX02ModeFragment.this.mActivity;
                        sb.append(MainActivity_DMX02.sceneBean);
                        String sb2 = sb.toString();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(DMX02ModeFragment.this.diyViewTag);
                        sb3.append("speed");
                        MainActivity_DMX02 unused2 = DMX02ModeFragment.this.mActivity;
                        sb3.append(MainActivity_DMX02.sceneBean);
                        SharePersistent.saveBrightData(activity, sb2, sb3.toString(), 1);
                    }
                } else {
                    DMX02ModeFragment.this.textViewSpeedSC.setText(DMX02ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i2)));
                    if (DMX02ModeFragment.this.mActivity != null) {
                        DMX02ModeFragment.this.mActivity.setSpeed(i2, false, false);
                        FragmentActivity activity2 = DMX02ModeFragment.this.getActivity();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(DMX02ModeFragment.this.diyViewTag);
                        sb4.append("speed");
                        MainActivity_DMX02 unused3 = DMX02ModeFragment.this.mActivity;
                        sb4.append(MainActivity_DMX02.sceneBean);
                        String sb5 = sb4.toString();
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(DMX02ModeFragment.this.diyViewTag);
                        sb6.append("speed");
                        MainActivity_DMX02 unused4 = DMX02ModeFragment.this.mActivity;
                        sb6.append(MainActivity_DMX02.sceneBean);
                        SharePersistent.saveBrightData(activity2, sb5, sb6.toString(), i2);
                    }
                }
                MainActivity_DMX02 unused5 = DMX02ModeFragment.this.mActivity;
                if (MainActivity_DMX02.sceneBean != null) {
                    Context context2 = DMX02ModeFragment.this.getContext();
                    StringBuilder sb7 = new StringBuilder();
                    MainActivity_DMX02 unused6 = DMX02ModeFragment.this.mActivity;
                    sb7.append(MainActivity_DMX02.sceneBean);
                    sb7.append("DMX02ModeFragment");
                    sb7.append("DIYspeed");
                    SharePersistent.saveInt(context2, sb7.toString(), i2);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, MainActivity_DMX02.sceneBean + "DMX02ModeFragmentDIYspeed");
            if (i2 > 0) {
                this.seekBarSpeedBarSC.setProgress(i2);
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i2)));
                return;
            }
            this.textViewSpeedSC.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
    }

    private void initConfirmButton() {
        Button button = (Button) this.menuView.findViewById(R.id.buttonSelectColorConfirm);
        this.buttonSelectColorConfirm = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.40
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Log.e("DMX02ModeFragment", "currentSelecColorFromPicker = " + DMX02ModeFragment.this.currentSelecColorFromPicker);
                if (DMX02ModeFragment.this.currentSelecColorFromPicker >= 0) {
                    if (DMX02ModeFragment.this.currentSelecColorFromPicker > 0 && DMX02ModeFragment.this.currentSelecColorFromPicker < 3000) {
                        DMX02ModeFragment.this.actionView.setColor(DMX02ModeFragment.this.currentSelecColorFromPicker);
                        String str = (String) DMX02ModeFragment.this.actionView.getTag();
                        if (DMX02ModeFragment.this.seekBarModeSC != null && DMX02ModeFragment.this.seekBarModeSC.getTag() != null) {
                            if (DMX02ModeFragment.this.seekBarModeSC.getTag().toString() != null) {
                                String obj = DMX02ModeFragment.this.seekBarModeSC.getTag().toString();
                                DMX02ModeFragment.this.sharedPreferences.edit().putInt(str, Integer.parseInt(obj.trim())).commit();
                                Drawable image = DMX02ModeFragment.this.getImage(obj);
                                if (image != null) {
                                    DMX02ModeFragment.this.actionView.setBackgroundDrawable(image);
                                }
                            }
                            DMX02ModeFragment.this.actionView.setText("");
                        }
                    }
                } else {
                    float f = 10;
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                    shapeDrawable.getPaint().setColor(DMX02ModeFragment.this.currentSelecColorFromPicker);
                    shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                    DMX02ModeFragment.this.sharedPreferences.edit().putInt((String) DMX02ModeFragment.this.actionView.getTag(), DMX02ModeFragment.this.currentSelecColorFromPicker).commit();
                    if (DMX02ModeFragment.this.currentSelecColorFromPicker != 0) {
                        DMX02ModeFragment.this.actionView.setColor(DMX02ModeFragment.this.currentSelecColorFromPicker);
                        DMX02ModeFragment.this.actionView.setBackgroundDrawable(shapeDrawable);
                        DMX02ModeFragment.this.actionView.setText("");
                    }
                }
                DMX02ModeFragment.this.hideColorCover();
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

    public void updateRgbText(int[] iArr, boolean z) {
        try {
            MainActivity_DMX02 mainActivity_DMX02 = this.mActivity;
            if (mainActivity_DMX02 != null) {
                mainActivity_DMX02.setRgb(iArr[0], iArr[1], iArr[2], z, false, false, false);
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

    /* loaded from: classes.dex */
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int selectedPosition = 0;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public ImageAdapter(Context context) {
            this.mContext = context;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return DMX02ModeFragment.this.icon.length;
        }

        public void clearSelection(int i) {
            this.selectedPosition = i;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.dmx02_animation_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.itemImg = (GifImageView) view.findViewById(R.id.gifImv);
                viewHolder.name = (TextView) view.findViewById(R.id.tv_name);
                TextView textView = viewHolder.name;
                textView.setTag("name" + (i + 1));
                viewHolder.llGifIVBoder = (LinearLayout) view.findViewById(R.id.llGifIVBoder);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Glide.with(this.mContext).load(Integer.valueOf(DMX02ModeFragment.this.icon[i])).into(viewHolder.itemImg);
            if (this.selectedPosition == i) {
                viewHolder.name.setTextColor(DMX02ModeFragment.this.getResources().getColor(R.color.dmx02_effect_color_check));
                viewHolder.llGifIVBoder.setVisibility(0);
            } else {
                viewHolder.name.setTextColor(DMX02ModeFragment.this.getResources().getColor(R.color.dmx02_effect_color_normal));
                viewHolder.llGifIVBoder.setVisibility(8);
            }
            TextView textView2 = viewHolder.name;
            textView2.setText("" + ((String) DMX02ModeFragment.this.gifListName.get(i)));
            return view;
        }

        /* loaded from: classes.dex */
        class ViewHolder {
            GifImageView itemImg;
            LinearLayout llGifIVBoder;
            TextView name;

            ViewHolder() {
            }
        }
    }

    /* loaded from: classes.dex */
    public class PullAdapter extends RecyclerView.Adapter<PullrHolder> {
        public PullAdapter() {
        }

        /* loaded from: classes.dex */
        public class PullrHolder extends RecyclerView.ViewHolder {
            ImageView ivDelete;
            ImageView ivItem;

            public PullrHolder(View view) {
                super(view);
                this.ivItem = (ImageView) view.findViewById(R.id.ivItem);
                this.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public PullrHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new PullrHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dmx02_item_pull_left, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(PullrHolder pullrHolder, final int i) {
            pullrHolder.ivItem.setImageBitmap((Bitmap) DMX02ModeFragment.this.bitmapsOfImg.get(i));
            pullrHolder.ivDelete.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02ModeFragment.PullAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Log.e("DMX02ModeFragment", "position: " + i);
                    DMX02ModeFragment.this.bitmapsOfImg.remove(i);
                    if (DMX02ModeFragment.this.bitmapsOfImg.size() > 0) {
                        DMX02ModeFragment.this.ivTopImageView.setImageBitmap((Bitmap) DMX02ModeFragment.this.bitmapsOfImg.get(DMX02ModeFragment.this.bitmapsOfImg.size() - 1));
                    } else {
                        DMX02ModeFragment.this.ivTopImageView.setImageBitmap(null);
                    }
                    DMX02ModeFragment.this.mPullAdapter.notifyDataSetChanged();
                    Log.e("DMX02ModeFragment", "bitmapsOfImg.size = " + DMX02ModeFragment.this.bitmapsOfImg.size());
                    DMX02ModeFragment.this.getCmdStringList(DMX02ModeFragment.this.bitmapsOfImg);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DMX02ModeFragment.this.bitmapsOfImg.size();
        }
    }
}
