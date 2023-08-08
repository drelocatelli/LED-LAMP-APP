package com.home.fragment.ble;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.AudioRecord;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import butterknife.BindView;
import com.common.adapter.AnimationListenerAdapter;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.net.NetResult;
import com.common.uitl.DrawTool;
import com.common.uitl.ListUtiles;
import com.common.uitl.SharePersistent;
import com.common.uitl.StringUtils;
import com.common.uitl.Tool;
import com.common.view.ScrollForeverTextView;
import com.common.view.SegmentedRadioGroup;
import com.home.activity.main.MainActivity_BLE;
import com.home.activity.other.EditColorActivity;
import com.home.activity.other.MusicLibActivity;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.bean.Mp3;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.home.view.BlackWiteSelectView;
import com.home.view.ColorTextView;
import com.home.view.VolumCircleBar;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class MusicFragment extends LedBleFragment {
    private static final int COLOR_DEFALUT = 0;
    private static final int INT_EDIT_COLOR = 102;
    private static final int INT_GO_SELECT_MUSIC = 100;
    private static final int INT_UPDATE_PROGRESS = 101;
    private static final int INT_UPDATE_RECORD = 103;
    private static int SAMPLE_RATE_IN_HZ = 8000;
    private static final String TAG = "MusicFragment";
    private ColorTextView actionView;
    private Animation animationRotate;
    private Animation animationScale;
    private AudioRecord audioRecord;
    private BlackWiteSelectView blackWiteSelectView;
    private int bs;
    private Button btnChangeColor;
    private Button btnModeCycle;
    private Button buttonConfirButton;
    @BindView(R.id.buttonMusicLib)
    Button buttonMusicLib;
    private RadioButton changeButtonFour;
    private RadioButton changeButtonOne;
    private RadioButton changeButtonThree;
    private RadioButton changeButtonTwo;
    @BindView(R.id.changeButton_Four)
    RadioButton changeButton_Four;
    @BindView(R.id.changeButton_One)
    RadioButton changeButton_One;
    @BindView(R.id.changeButton_Three)
    RadioButton changeButton_Three;
    @BindView(R.id.changeButton_Two)
    RadioButton changeButton_Two;
    @BindView(R.id.changeButton_micro)
    SegmentedRadioGroup changeButton_micro;
    private volatile int chnnelValue;
    private ArrayList<ColorTextView> colorTextViews;
    private ArrayList<MyColor> colors;
    private Mp3 currentMp3;
    private int currentSelecColorFromPicker;
    private SegmentedRadioGroup dynamicChangeGroup;
    @BindView(R.id.imageViewNext)
    ImageView imageViewNext;
    private ColorPickerView imageViewPicker2;
    @BindView(R.id.imageViewPlay)
    ImageView imageViewPlay;
    @BindView(R.id.imageViewPlayType)
    ImageView imageViewPlayType;
    @BindView(R.id.imageViewPre)
    ImageView imageViewPre;
    @BindView(R.id.imageViewRotate)
    ImageView imageViewRotate;
    private Button ivEditColor;
    @BindView(R.id.llMicro)
    LinearLayout llMicro;
    @BindView(R.id.llMusic)
    LinearLayout llMusic;
    @BindView(R.id.ll_breathe)
    LinearLayout ll_breathe;
    @BindView(R.id.ll_flash)
    LinearLayout ll_flash;
    @BindView(R.id.ll_gradient)
    LinearLayout ll_gradient;
    @BindView(R.id.ll_jump)
    LinearLayout ll_jump;
    @BindView(R.id.ll_seekBarDecibel)
    LinearLayout ll_seekBarDecibel;
    @BindView(R.id.ll_seekBarSensitivity)
    LinearLayout ll_seekBarSensitivity;
    private MainActivity_BLE mActivity;
    private ObjectAnimator mCircleAnimator;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private Visualizer mVisualizer;
    private VisualizerView mVisualizerView;
    private MediaPlayer mediaPlayer;
    private View menuView;
    private volatile float microRecordValue;
    private Handler mstartMicroHandler;
    private Runnable mstartMicroRunnable;
    private RadioButton rbMusicOne;
    private RadioButton rbMusicTwo;
    @BindView(R.id.rbNeiMai)
    RadioButton rbNeiMai;
    @BindView(R.id.rbWaiMai)
    RadioButton rbWaiMai;
    @BindView(R.id.rlBLEVoiceCtl)
    RelativeLayout rlBLEVoiceCtl;
    @BindView(R.id.rlCar01VoiceCtl)
    RelativeLayout rlCar01VoiceCtl;
    private RelativeLayout rlCustomtopInfo;
    @BindView(R.id.rlDMXVoiceCtl)
    RelativeLayout rlDMXVoiceCtl;
    private SeekBar seekBarBrightNess2;
    @BindView(R.id.seekBarDecibel)
    SeekBar seekBarDecibel;
    @BindView(R.id.seekBarMusic)
    SeekBar seekBarMusic;
    @BindView(R.id.seekBarRhythm)
    SeekBar seekBarRhythm;
    @BindView(R.id.seekBarSensitivity)
    SeekBar seekBarSensitivity;
    @BindView(R.id.seekBarSensitivityCAR01Bottom)
    SeekBar seekBarSensitivityCAR01Bottom;
    @BindView(R.id.seekBarSensitivityCAR01Top)
    SeekBar seekBarSensitivityCAR01Top;
    @BindView(R.id.seekBarSensitivityDMX)
    SeekBar seekBarSensitivityDMX;
    private SegmentedRadioGroup segmentedRadioGroup;
    private Thread sendMusicThread;
    private SharedPreferences sp;
    private TextView textRGB;
    @BindView(R.id.textViewAutoAjust)
    ScrollForeverTextView textViewAutoAjust;
    private TextView textViewBrightNess2;
    private TextView textViewRingBrightSC;
    @BindView(R.id.textViewSensitivityCAR01Bottom)
    TextView textViewSensitivityCAR01Bottom;
    @BindView(R.id.textViewSensitivityCAR01Top)
    TextView textViewSensitivityCAR01Top;
    @BindView(R.id.textViewSensitivityDMX)
    TextView textViewSensitivityDMX;
    @BindView(R.id.tvCurrentTime)
    TextView tvCurrentTime;
    @BindView(R.id.tvDecibelValue)
    TextView tvDecibelValue;
    @BindView(R.id.tvRhythm)
    TextView tvRhythm;
    @BindView(R.id.tvSensitivityValue)
    TextView tvSensitivityValue;
    @BindView(R.id.tvTotalTime)
    TextView tvTotalTime;
    @BindView(R.id.tvrhythmValue)
    TextView tvrhythmValue;
    @BindView(R.id.volumCircleBar)
    VolumCircleBar volumCircleBar;
    @BindView(R.id.wheelPickerCAR01)
    WheelPicker wheelPickerCAR01;
    @BindView(R.id.wheelPickerDMX)
    WheelPicker wheelPickerDMX;
    private int style = 0;
    private final int RECORD_AUDIO = 200;
    private boolean canSendCmd = true;
    private boolean isLoopAll = false;
    private boolean isLoopOne = true;
    private boolean isRandom = false;
    private int musicMode = 1;
    private int microMode = 0;
    private boolean isMusic = true;
    private boolean isClickMicro = false;
    private boolean isStartTimer = true;
    private Random random = new Random();
    private boolean openMac = true;
    private Handler mhanHandler = new Handler() { // from class: com.home.fragment.ble.MusicFragment.45
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String str;
            String str2;
            int i = message.what;
            if (i != 101) {
                if (i != 103) {
                    return;
                }
                int pow = ((int) Math.pow(((Double) message.obj).doubleValue(), 0.24d)) - 10;
                MusicFragment.this.microRecordValue = pow / 100.0f;
                MusicFragment musicFragment = MusicFragment.this;
                musicFragment.sendVolumValue(musicFragment.getRandColor(), pow);
                return;
            }
            MusicFragment musicFragment2 = MusicFragment.this;
            musicFragment2.updatePlayProgress(musicFragment2.currentMp3);
            if (MusicFragment.this.mediaPlayer != null) {
                int currentPosition = MusicFragment.this.mediaPlayer.getCurrentPosition() / 1000;
                if (currentPosition >= 60) {
                    int i2 = currentPosition % 60;
                    if (i2 < 10) {
                        str = NetResult.CODE_OK + i2;
                    } else {
                        str = "" + i2;
                    }
                    str2 = (currentPosition / 60) + ":" + str;
                } else if (currentPosition < 10) {
                    str2 = "0:0" + currentPosition;
                } else {
                    str2 = "0:" + currentPosition;
                }
                if (MusicFragment.this.tvCurrentTime == null || str2 == null) {
                    return;
                }
                MusicFragment.this.tvCurrentTime.setText(str2);
            }
        }
    };
    boolean isSettingBoolean = false;
    private int idx = 0;
    Random rand = new Random();

    @Override // com.home.base.LedBleFragment
    public void initView() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = layoutInflater.inflate(R.layout.fragment_music, viewGroup, false);
        this.menuView = layoutInflater.inflate(R.layout.activity_car00vtl_select_color, viewGroup, false);
        return this.mContentView;
    }

    public boolean isCheckSegmentedRadioGroupIndexTwo() {
        return this.segmentedRadioGroup.getCheckedRadioButtonId() == R.id.rbMusicTwo;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        MainActivity_BLE mainActivity_BLE = (MainActivity_BLE) getActivity();
        this.mActivity = mainActivity_BLE;
        SegmentedRadioGroup segmentMusic = mainActivity_BLE.getSegmentMusic();
        this.segmentedRadioGroup = segmentMusic;
        segmentMusic.check(R.id.rbMusicOne);
        this.rbMusicOne = (RadioButton) this.segmentedRadioGroup.findViewById(R.id.rbMusicOne);
        this.rbMusicTwo = (RadioButton) this.segmentedRadioGroup.findViewById(R.id.rbMusicTwo);
        if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
            this.rbMusicOne.setText(getString(R.string.Voicecontrol));
            this.rbMusicTwo.setText(getString(R.string.tab_music));
            this.btnChangeColor = this.mActivity.getBtnChangeColor();
            this.btnModeCycle = this.mActivity.getBtnModeCycle();
            this.ivEditColor = this.mActivity.getIvEditColor();
            this.btnChangeColor.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(MusicFragment.this.getActivity(), R.anim.layout_scale));
                    MusicFragment.this.mActivity.setChangeColor(false, true, false, MusicFragment.this.getVoiceCtlSelectColor());
                }
            });
            this.btnModeCycle.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(MusicFragment.this.getActivity(), R.anim.layout_scale));
                    MainActivity_BLE.getMainActivity().setVoiceCtlAndMusicMode(true, false, false, 255);
                }
            });
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-")) {
                this.ivEditColor.setVisibility(8);
                this.rlDMXVoiceCtl.setVisibility(0);
                this.wheelPickerDMX.setData(buildModel());
                this.wheelPickerDMX.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.fragment.ble.MusicFragment.3
                    @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
                    public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                        if (i >= 0) {
                            MainActivity_BLE.getMainActivity().setVoiceCtlAndMusicMode(true, false, false, Integer.parseInt(((String) MusicFragment.this.buildModel().get(i)).split(" ")[1].trim()));
                        }
                    }
                });
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
                this.rlCustomtopInfo = this.mActivity.getRlCustomtopInfo();
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                this.rlDMXVoiceCtl.setVisibility(8);
                this.btnChangeColor.setVisibility(8);
                this.rlCar01VoiceCtl.setVisibility(0);
                this.wheelPickerCAR01.setData(buildModel());
                this.wheelPickerCAR01.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.fragment.ble.MusicFragment.4
                    @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
                    public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                        if (i >= 0) {
                            Log.e(MusicFragment.TAG, "onItemSelected: " + i);
                            if (MusicFragment.this.mActivity != null) {
                                MusicFragment.this.mActivity.setVoiceCtlAndMusicMode(true, false, true, i + 1);
                            }
                            if (MainActivity_BLE.sceneBean != null) {
                                Context context = MusicFragment.this.getContext();
                                SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + MusicFragment.TAG + "mode-mode", i);
                            }
                        }
                    }
                });
            }
        } else {
            this.llMusic.setVisibility(0);
            this.rbMusicOne.setText(getString(R.string.tab_music));
            this.rbMusicTwo.setText(getString(R.string.tab_record));
        }
        this.llMicro.setVisibility(8);
        this.segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.MusicFragment.5
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.rbMusicOne == i) {
                    if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                        MusicFragment.this.ivEditColor.setVisibility(8);
                        MusicFragment.this.btnModeCycle.setVisibility(8);
                        MusicFragment.this.btnChangeColor.setVisibility(8);
                        if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-")) {
                            MusicFragment.this.rlDMXVoiceCtl.setVisibility(0);
                            MusicFragment.this.btnModeCycle.setVisibility(0);
                            MusicFragment.this.rlCar01VoiceCtl.setVisibility(8);
                        } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
                            MusicFragment.this.rlCustomtopInfo.setVisibility(0);
                            MusicFragment.this.rlBLEVoiceCtl.setVisibility(0);
                            MusicFragment.this.btnChangeColor.setVisibility(0);
                        } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                            MusicFragment.this.rlCar01VoiceCtl.setVisibility(0);
                            MusicFragment.this.rlDMXVoiceCtl.setVisibility(8);
                            MusicFragment.this.btnChangeColor.setVisibility(8);
                        }
                        MusicFragment.this.llMusic.setVisibility(8);
                        MusicFragment.this.llMicro.setVisibility(8);
                        MusicFragment.this.pauseMusic();
                        return;
                    }
                    MusicFragment.this.llMusic.setVisibility(0);
                    MusicFragment.this.tvRhythm.setText(MusicFragment.this.getString(R.string.speed));
                    MusicFragment.this.rlDMXVoiceCtl.setVisibility(8);
                    MusicFragment.this.llMicro.setVisibility(8);
                    if (MusicFragment.this.audioRecord != null) {
                        MusicFragment.this.pauseVolum(true);
                    }
                    if (MusicFragment.this.mediaPlayer != null && MusicFragment.this.mediaPlayer.isPlaying()) {
                        MusicFragment.this.sendMusicMode();
                    }
                    MusicFragment.this.isClickMicro = false;
                    return;
                }
                MusicFragment.this.rlDMXVoiceCtl.setVisibility(8);
                MusicFragment.this.rlCar01VoiceCtl.setVisibility(8);
                if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                    MusicFragment.this.btnModeCycle.setVisibility(8);
                    MusicFragment.this.btnChangeColor.setVisibility(8);
                    MusicFragment.this.ivEditColor.setVisibility(0);
                    MusicFragment.this.llMusic.setVisibility(0);
                    MusicFragment.this.tvRhythm.setText(MusicFragment.this.getString(R.string.sensitivity));
                    MusicFragment.this.llMicro.setVisibility(8);
                    MusicFragment.this.rbMusicOne.setText(MusicFragment.this.getString(R.string.Voicecontrol));
                    MusicFragment.this.rbMusicTwo.setText(MusicFragment.this.getString(R.string.tab_music));
                    if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
                        MusicFragment.this.rlCustomtopInfo.setVisibility(8);
                        MusicFragment.this.rlBLEVoiceCtl.setVisibility(8);
                        MusicFragment.this.tvRhythm.setText(MusicFragment.this.getString(R.string.speed));
                        return;
                    }
                    return;
                }
                MusicFragment.this.llMusic.setVisibility(8);
                MusicFragment.this.llMicro.setVisibility(0);
                MusicFragment.this.rbMusicOne.setText(MusicFragment.this.getString(R.string.tab_music));
                MusicFragment.this.rbMusicTwo.setText(MusicFragment.this.getString(R.string.tab_record));
                MusicFragment.this.isClickMicro = true;
                MusicFragment.this.canSendCmd = true;
                if (MusicFragment.this.openMac) {
                    if (ContextCompat.checkSelfPermission(MusicFragment.this.getContext(), "android.permission.RECORD_AUDIO") != 0) {
                        MusicFragment.this.mActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 200);
                    } else if (MusicFragment.this.audioRecord != null) {
                        MusicFragment.this.pauseVolum(false);
                    } else {
                        MusicFragment.this.requestMicroPermissionsSucess(true);
                    }
                }
            }
        });
        if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-")) {
                this.seekBarSensitivityDMX.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.MusicFragment.6
                    @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        super.onStopTrackingTouch(seekBar);
                        if (MusicFragment.this.textViewSensitivityDMX.getTag() != null) {
                            if (MusicFragment.this.textViewSensitivityDMX.getTag().equals(100)) {
                                MusicFragment.this.mActivity.setSensitivity(100, false, false);
                            } else if (MusicFragment.this.textViewSensitivityDMX.getTag().equals(1)) {
                                MusicFragment.this.mActivity.setSensitivity(1, false, false);
                            }
                        }
                    }

                    @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                        if (z) {
                            if (i == 0) {
                                if (MusicFragment.this.mActivity != null) {
                                    MusicFragment.this.mActivity.setSensitivity(1, false, false);
                                }
                                MusicFragment.this.textViewSensitivityDMX.setText(MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, 1));
                                MusicFragment.this.textViewSensitivityDMX.setTag(1);
                                if (MainActivity_BLE.sceneBean != null) {
                                    Context context = MusicFragment.this.getContext();
                                    SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + MusicFragment.TAG + "sensitivity", 1);
                                    return;
                                }
                                return;
                            }
                            if (MusicFragment.this.mActivity != null) {
                                MusicFragment.this.mActivity.setSensitivity(i, false, false);
                            }
                            MusicFragment.this.textViewSensitivityDMX.setText(MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, Integer.valueOf(i)));
                            MusicFragment.this.textViewSensitivityDMX.setTag(Integer.valueOf(i));
                            if (MainActivity_BLE.sceneBean != null) {
                                Context context2 = MusicFragment.this.getContext();
                                SharePersistent.saveInt(context2, MainActivity_BLE.sceneBean + MusicFragment.TAG + "sensitivity", i);
                            }
                        }
                    }
                });
                if (MainActivity_BLE.sceneBean != null) {
                    String str = MainActivity_BLE.sceneBean;
                    Context context = getContext();
                    int i = SharePersistent.getInt(context, MainActivity_BLE.sceneBean + TAG + "sensitivity");
                    if (i > 0) {
                        this.seekBarSensitivityDMX.setProgress(i);
                        this.textViewSensitivityDMX.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i)));
                    } else {
                        this.seekBarSensitivityDMX.setProgress(90);
                        this.textViewSensitivityDMX.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
                    }
                }
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
                this.sp = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
                this.rlBLEVoiceCtl.setVisibility(0);
                this.textViewBrightNess2 = (TextView) this.mContentView.findViewById(R.id.textViewBrightNess2);
                SeekBar seekBar = (SeekBar) this.mContentView.findViewById(R.id.seekBarBrightNess2);
                this.seekBarBrightNess2 = seekBar;
                seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.MusicFragment.7
                    @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar2, int i2, boolean z) {
                        if (z) {
                            if (i2 == 0) {
                                seekBar2.setProgress(1);
                                NetConnectBle.getInstance().setSensitivity(1, true, false, MainActivity_BLE.sceneBean);
                                MusicFragment.this.textViewBrightNess2.setText(MusicFragment.this.getResources().getString(R.string.sensitivity_set, 1));
                                if (MainActivity_BLE.getSceneBean() != null) {
                                    Context context2 = MusicFragment.this.getContext();
                                    SharePersistent.saveInt(context2, MainActivity_BLE.getSceneBean() + MusicFragment.TAG + "bright", 1);
                                    return;
                                }
                                return;
                            }
                            NetConnectBle.getInstance().setSensitivity(i2, true, false, MainActivity_BLE.sceneBean);
                            MusicFragment.this.textViewBrightNess2.setText(MusicFragment.this.getResources().getString(R.string.sensitivity_set, Integer.valueOf(i2)));
                            if (MainActivity_BLE.getSceneBean() != null) {
                                Context context3 = MusicFragment.this.getContext();
                                SharePersistent.saveInt(context3, MainActivity_BLE.getSceneBean() + MusicFragment.TAG + "bright", i2);
                            }
                        }
                    }
                });
                if (MainActivity_BLE.getSceneBean() != null) {
                    FragmentActivity activity = getActivity();
                    int i2 = SharePersistent.getInt(activity, MainActivity_BLE.getSceneBean() + TAG + "bright");
                    if (i2 > 0) {
                        this.seekBarBrightNess2.setProgress(i2);
                        this.textViewBrightNess2.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i2)));
                    } else {
                        this.textViewBrightNess2.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
                    }
                }
                SegmentedRadioGroup segmentedRadioGroup = (SegmentedRadioGroup) this.mContentView.findViewById(R.id.dynamicChangeButton);
                this.dynamicChangeGroup = segmentedRadioGroup;
                segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.MusicFragment.8
                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i3) {
                        if (R.id.dynamicChangeButtonOne == i3) {
                            MusicFragment.this.style = 0;
                        } else if (R.id.dynamicChangeButtonTwo == i3) {
                            MusicFragment.this.style = 1;
                        } else if (R.id.dynamicChangeButtonThree == i3) {
                            MusicFragment.this.style = 2;
                        } else if (R.id.dynamicChangeButtonFour == i3) {
                            MusicFragment.this.style = 3;
                        }
                    }
                });
                RadioButton radioButton = (RadioButton) this.mContentView.findViewById(R.id.dynamicChangeButtonOne);
                this.changeButtonOne = radioButton;
                radioButton.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.9
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment musicFragment = MusicFragment.this;
                        musicFragment.putDataBack(musicFragment.style);
                    }
                });
                RadioButton radioButton2 = (RadioButton) this.mContentView.findViewById(R.id.dynamicChangeButtonTwo);
                this.changeButtonTwo = radioButton2;
                radioButton2.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment musicFragment = MusicFragment.this;
                        musicFragment.putDataBack(musicFragment.style);
                    }
                });
                RadioButton radioButton3 = (RadioButton) this.mContentView.findViewById(R.id.dynamicChangeButtonThree);
                this.changeButtonThree = radioButton3;
                radioButton3.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.11
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment musicFragment = MusicFragment.this;
                        musicFragment.putDataBack(musicFragment.style);
                    }
                });
                RadioButton radioButton4 = (RadioButton) this.mContentView.findViewById(R.id.dynamicChangeButtonFour);
                this.changeButtonFour = radioButton4;
                radioButton4.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.12
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment musicFragment = MusicFragment.this;
                        musicFragment.putDataBack(musicFragment.style);
                    }
                });
                initColorBlock();
                initColorSelecterView();
            } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
                this.ll_jump.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.13
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment.this.setCar01Mode(0, false, view);
                    }
                });
                this.ll_breathe.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.14
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment.this.setCar01Mode(1, false, view);
                    }
                });
                this.ll_flash.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.15
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment.this.setCar01Mode(2, false, view);
                    }
                });
                this.ll_gradient.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.16
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MusicFragment.this.setCar01Mode(3, false, view);
                    }
                });
                this.seekBarSensitivityCAR01Top.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.MusicFragment.17
                    @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStopTrackingTouch(SeekBar seekBar2) {
                        super.onStopTrackingTouch(seekBar2);
                        if (MusicFragment.this.textViewSensitivityCAR01Top.getTag() != null) {
                            if (MusicFragment.this.textViewSensitivityCAR01Top.getTag().equals(100)) {
                                MusicFragment.this.mActivity.setSensitivity(100, false, false);
                            } else if (MusicFragment.this.textViewSensitivityCAR01Top.getTag().equals(1)) {
                                MusicFragment.this.mActivity.setSensitivity(1, false, false);
                            }
                        }
                    }

                    @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar2, int i3, boolean z) {
                        if (z) {
                            if (i3 == 0) {
                                if (MusicFragment.this.mActivity != null) {
                                    MusicFragment.this.mActivity.setSensitivity(1, false, false);
                                }
                                MusicFragment.this.textViewSensitivityCAR01Top.setText(MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, 1));
                                MusicFragment.this.textViewSensitivityCAR01Top.setTag(1);
                                if (MainActivity_BLE.sceneBean != null) {
                                    Context context2 = MusicFragment.this.getContext();
                                    SharePersistent.saveInt(context2, MainActivity_BLE.sceneBean + MusicFragment.TAG + "sensitivityTop", 1);
                                    return;
                                }
                                return;
                            }
                            if (MusicFragment.this.mActivity != null) {
                                MusicFragment.this.mActivity.setSensitivity(i3, false, false);
                            }
                            MusicFragment.this.textViewSensitivityCAR01Top.setText(MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, Integer.valueOf(i3)));
                            MusicFragment.this.textViewSensitivityCAR01Top.setTag(Integer.valueOf(i3));
                            if (MainActivity_BLE.sceneBean != null) {
                                Context context3 = MusicFragment.this.getContext();
                                SharePersistent.saveInt(context3, MainActivity_BLE.sceneBean + MusicFragment.TAG + "sensitivityTop", i3);
                            }
                        }
                    }
                });
                if (MainActivity_BLE.sceneBean != null) {
                    Context context2 = getContext();
                    int i3 = SharePersistent.getInt(context2, MainActivity_BLE.sceneBean + TAG + "sensitivityTop");
                    if (i3 > 0) {
                        this.seekBarSensitivityCAR01Top.setProgress(i3);
                        this.textViewSensitivityCAR01Top.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i3)));
                    } else {
                        this.textViewSensitivityCAR01Top.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
                    }
                }
                this.seekBarSensitivityCAR01Bottom.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.MusicFragment.18
                    @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStopTrackingTouch(SeekBar seekBar2) {
                        super.onStopTrackingTouch(seekBar2);
                        if (MusicFragment.this.textViewSensitivityCAR01Top.getTag() != null) {
                            if (MusicFragment.this.textViewSensitivityCAR01Bottom.getTag().equals(100)) {
                                MusicFragment.this.mActivity.setSensitivity(100, false, true);
                            } else if (MusicFragment.this.textViewSensitivityCAR01Bottom.getTag().equals(1)) {
                                MusicFragment.this.mActivity.setSensitivity(1, false, true);
                            }
                        }
                    }

                    @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar2, int i4, boolean z) {
                        if (z) {
                            if (i4 == 0) {
                                if (MusicFragment.this.mActivity != null) {
                                    MusicFragment.this.mActivity.setSensitivity(1, false, true);
                                }
                                MusicFragment.this.textViewSensitivityCAR01Bottom.setText(MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, 1));
                                MusicFragment.this.textViewSensitivityCAR01Bottom.setTag(1);
                                if (MainActivity_BLE.sceneBean != null) {
                                    Context context3 = MusicFragment.this.getContext();
                                    SharePersistent.saveInt(context3, MainActivity_BLE.sceneBean + MusicFragment.TAG + "sensitivityBottom", 1);
                                    return;
                                }
                                return;
                            }
                            if (MusicFragment.this.mActivity != null) {
                                MusicFragment.this.mActivity.setSensitivity(i4, false, true);
                            }
                            MusicFragment.this.textViewSensitivityCAR01Bottom.setText(MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, Integer.valueOf(i4)));
                            MusicFragment.this.textViewSensitivityCAR01Bottom.setTag(Integer.valueOf(i4));
                            if (MainActivity_BLE.sceneBean != null) {
                                Context context4 = MusicFragment.this.getContext();
                                SharePersistent.saveInt(context4, MainActivity_BLE.sceneBean + MusicFragment.TAG + "sensitivityBottom", i4);
                            }
                        }
                    }
                });
                if (MainActivity_BLE.sceneBean != null) {
                    Context context3 = getContext();
                    int i4 = SharePersistent.getInt(context3, MainActivity_BLE.sceneBean + TAG + "sensitivityBottom");
                    if (i4 > 0) {
                        this.seekBarSensitivityCAR01Bottom.setProgress(i4);
                        this.textViewSensitivityCAR01Bottom.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i4)));
                    } else {
                        this.textViewSensitivityCAR01Bottom.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
                    }
                }
            }
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.imageViewRotate, "rotation", 0.0f, 360.0f);
        this.mCircleAnimator = ofFloat;
        ofFloat.setDuration(8000L);
        this.mCircleAnimator.setInterpolator(new LinearInterpolator());
        this.mCircleAnimator.setRepeatCount(-1);
        this.mCircleAnimator.setRepeatMode(1);
        this.mVisualizerView = new VisualizerView(this.mActivity);
    }

    public void requestMicroPermissionsSucess(boolean z) {
        if (z) {
            if (this.audioRecord == null) {
                this.bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, 1, 2);
                this.audioRecord = new AudioRecord(1, SAMPLE_RATE_IN_HZ, 1, 2, this.bs);
            }
            pauseMusic();
            this.microMode = 0;
            this.volumCircleBar.setBackgroundResource(R.drawable.neimai_icon);
            this.volumCircleBar.toggleRecord(0);
            AudioRecord audioRecord = this.audioRecord;
            if (audioRecord != null) {
                audioRecord.startRecording();
            }
            if (this.audioRecord != null && this.isClickMicro) {
                sendMicro();
            }
            startRecord();
            return;
        }
        this.openMac = false;
        this.segmentedRadioGroup.check(R.id.rbMusicOne);
        this.openMac = true;
    }

    public void request() {
        if (this.isClickMicro) {
            return;
        }
        this.bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, 1, 2);
        this.audioRecord = new AudioRecord(1, SAMPLE_RATE_IN_HZ, 1, 2, this.bs);
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
        this.imageViewPre.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MusicFragment.this.playAnimationPress(view);
                MusicFragment.this.playPre();
            }
        });
        this.imageViewPlay.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.20
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MusicFragment.this.playAnimationPress(view);
                MusicFragment.this.playPause();
            }
        });
        this.imageViewNext.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MusicFragment.this.playAnimationPress(view);
                MusicFragment.this.palayNext();
            }
        });
        this.animationScale = AnimationUtils.loadAnimation(this.activity, R.anim.layout_scale);
        Animation loadAnimation = AnimationUtils.loadAnimation(this.activity, R.anim.rotate_frover);
        this.animationRotate = loadAnimation;
        loadAnimation.setInterpolator(new LinearInterpolator());
        this.animationRotate.setRepeatCount(-1);
        this.animationRotate.setAnimationListener(new AnimationListenerAdapter() { // from class: com.home.fragment.ble.MusicFragment.22
        });
        this.imageViewRotate.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.23
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int i = MusicFragment.this.musicMode;
                if (i == 0) {
                    MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_gradualchange));
                    MusicFragment.this.musicMode = 1;
                } else if (i == 1) {
                    if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDDMX)) {
                        MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_trailing));
                    } else {
                        MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_jump));
                    }
                    MusicFragment.this.musicMode = 2;
                } else if (i == 2) {
                    if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDDMX)) {
                        MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_jump));
                    } else {
                        MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_stroboflash));
                    }
                    MusicFragment.this.musicMode = 3;
                } else if (i == 3) {
                    if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDDMX)) {
                        MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_stroboflash));
                        MusicFragment.this.musicMode = 4;
                    } else {
                        MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_breathe));
                        MusicFragment.this.musicMode = 0;
                    }
                } else if (i == 4 && MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDDMX)) {
                    MusicFragment.this.imageViewRotate.setBackground(MusicFragment.this.getResources().getDrawable(R.drawable.music_nooutput));
                    MusicFragment.this.musicMode = 0;
                }
                MusicFragment.this.sendMusicMode();
            }
        });
        this.imageViewPlayType.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.24
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!MusicFragment.this.isLoopAll) {
                    if (!MusicFragment.this.isLoopOne) {
                        if (MusicFragment.this.isRandom) {
                            MusicFragment.this.imageViewPlayType.setImageResource(R.drawable.playtype_random);
                            Toast.makeText(MusicFragment.this.mActivity, (int) R.string.RandomPlay, 0).show();
                            MusicFragment.this.isRandom = false;
                            MusicFragment.this.isLoopAll = true;
                            return;
                        }
                        return;
                    }
                    MusicFragment.this.imageViewPlayType.setImageResource(R.drawable.playtype_loopone);
                    Toast.makeText(MusicFragment.this.mActivity, (int) R.string.SinglePlay, 0).show();
                    MusicFragment.this.isLoopOne = false;
                    MusicFragment.this.isRandom = true;
                    return;
                }
                MusicFragment.this.imageViewPlayType.setImageResource(R.drawable.playtype_loopall);
                Toast.makeText(MusicFragment.this.mActivity, (int) R.string.SequentialPlay, 0).show();
                MusicFragment.this.isLoopAll = false;
                MusicFragment.this.isLoopOne = true;
            }
        });
        this.buttonMusicLib.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.25
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MusicFragment.this.getContext(), "android.permission.RECORD_AUDIO") != 0) {
                    MusicFragment.this.mActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 200);
                    return;
                }
                Intent intent = new Intent(MusicFragment.this.mActivity, MusicLibActivity.class);
                intent.putExtra("sun", 200);
                MusicFragment.this.startActivityForResult(intent, 100);
            }
        });
        this.mActivity.getIvEditColor().setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.26
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MusicFragment.this.mediaPlayer != null && MusicFragment.this.mediaPlayer.isPlaying()) {
                    MusicFragment.this.playPause();
                }
                MusicFragment.this.pauseVolum(true);
                MusicFragment.this.mActivity.startActivityForResult(new Intent(MusicFragment.this.mActivity, EditColorActivity.class), 102);
            }
        });
        this.seekBarRhythm.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.MusicFragment.27
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    MusicFragment.this.tvrhythmValue.setText(String.valueOf(i));
                    if (MainActivity_BLE.sceneBean != null) {
                        if (MainActivity_BLE.sceneBean.equalsIgnoreCase("LEDDMX-00-") || MainActivity_BLE.sceneBean.equalsIgnoreCase("LEDCAR-01-")) {
                            MusicFragment.this.mActivity.setSensitivity(i, true, true);
                        } else {
                            MusicFragment.this.mActivity.setSpeed(i, true, false);
                        }
                        Context context = MusicFragment.this.getContext();
                        SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + "rhythm", i);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            FragmentActivity activity = getActivity();
            int i = SharePersistent.getInt(activity, MainActivity_BLE.sceneBean + "rhythm");
            if (i > 0) {
                this.seekBarRhythm.setProgress(i);
                this.tvrhythmValue.setText(String.valueOf(i));
            } else {
                this.tvrhythmValue.setText(String.valueOf(80));
            }
        }
        this.changeButton_micro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.MusicFragment.28
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i2) {
                if (R.id.changeButton_One == i2) {
                    MusicFragment.this.microMode = 0;
                    MusicFragment.this.volumCircleBar.toggleRecord(0);
                } else if (R.id.changeButton_Two == i2) {
                    MusicFragment.this.microMode = 1;
                    MusicFragment.this.volumCircleBar.toggleRecord(1);
                } else if (R.id.changeButton_Three == i2) {
                    MusicFragment.this.microMode = 2;
                    MusicFragment.this.volumCircleBar.toggleRecord(2);
                } else if (R.id.changeButton_Four == i2) {
                    MusicFragment.this.microMode = 3;
                    MusicFragment.this.volumCircleBar.toggleRecord(3);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null && MainActivity_BLE.sceneBean.contains(CommonConstant.LEDBLE)) {
            this.changeButton_One.setText(getResources().getString(R.string.jump));
            this.changeButton_Two.setText(getResources().getString(R.string.breathe));
            this.changeButton_Three.setText(getResources().getString(R.string.flash));
            this.changeButton_Four.setText(getResources().getString(R.string.gradient));
        }
        this.changeButton_One.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.29
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MusicFragment.this.isClickMicro) {
                    MusicFragment.this.sendMicroMode();
                }
            }
        });
        this.changeButton_Two.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.30
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MusicFragment.this.isClickMicro) {
                    MusicFragment.this.sendMicroMode();
                }
            }
        });
        this.changeButton_Three.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MusicFragment.this.isClickMicro) {
                    MusicFragment.this.sendMicroMode();
                }
            }
        });
        this.changeButton_Four.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.32
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MusicFragment.this.isClickMicro) {
                    MusicFragment.this.sendMicroMode();
                }
            }
        });
        this.seekBarMusic.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.MusicFragment.33
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (MusicFragment.this.currentMp3 == null) {
                    Tool.ToastShow(MusicFragment.this.mActivity, (int) R.string.chose_list);
                    seekBar.setProgress(0);
                } else if (ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
                } else {
                    int duration = MusicFragment.this.currentMp3.getDuration();
                    MusicFragment.this.mediaPlayer.seekTo((seekBar.getProgress() * duration) / 100);
                }
            }
        });
        this.rbNeiMai.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.34
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MusicFragment.this.rbNeiMai.isChecked()) {
                    MusicFragment.this.rbWaiMai.setChecked(false);
                    MusicFragment.this.ll_seekBarDecibel.setVisibility(0);
                    MusicFragment.this.ll_seekBarSensitivity.setVisibility(8);
                    MusicFragment.this.volumCircleBar.setBackgroundResource(R.drawable.neimai_icon);
                    MusicFragment.this.canSendCmd = true;
                    MusicFragment.this.requestMicroPermissionsSucess(true);
                }
            }
        });
        this.rbWaiMai.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MusicFragment.this.rbWaiMai.isChecked()) {
                    MusicFragment.this.rbNeiMai.setChecked(false);
                    MusicFragment.this.ll_seekBarDecibel.setVisibility(8);
                    MusicFragment.this.ll_seekBarSensitivity.setVisibility(0);
                    MusicFragment.this.volumCircleBar.setBackgroundResource(R.drawable.waimai_iocn);
                    MusicFragment.this.pauseVolum(true);
                    MusicFragment.this.changeButton_micro.check(R.id.changeButton_One);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null && !MainActivity_BLE.sceneBean.contains(CommonConstant.LEDBLE)) {
            this.rbNeiMai.setVisibility(8);
            this.rbWaiMai.setVisibility(8);
        }
        this.seekBarDecibel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.ble.MusicFragment.36
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicFragment.this.canSendCmd = true;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                MusicFragment.this.canSendCmd = false;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                MusicFragment.this.mActivity.setSpeed(i2, true, false);
                MusicFragment.this.tvDecibelValue.setText(String.valueOf(i2));
                if (MainActivity_BLE.sceneBean != null) {
                    Context context = MusicFragment.this.getContext();
                    SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + "decibel", i2);
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            FragmentActivity activity2 = getActivity();
            int i2 = SharePersistent.getInt(activity2, MainActivity_BLE.sceneBean + "decibel");
            if (i2 > 0) {
                this.seekBarDecibel.setProgress(i2);
                this.tvDecibelValue.setText(String.valueOf(i2));
            } else {
                this.seekBarDecibel.setProgress(80);
                this.tvDecibelValue.setText(String.valueOf(80));
            }
        }
        this.seekBarSensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.ble.MusicFragment.37
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicFragment.this.canSendCmd = true;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                MusicFragment.this.canSendCmd = false;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    NetConnectBle.getInstance().setSensitivity(i3, true, false, MainActivity_BLE.sceneBean);
                    MusicFragment.this.tvSensitivityValue.setText(String.valueOf(i3));
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context = MusicFragment.this.getContext();
                        SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + "sensitivity", i3);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            FragmentActivity activity3 = getActivity();
            int i3 = SharePersistent.getInt(activity3, MainActivity_BLE.sceneBean + "sensitivity");
            if (i3 > 0) {
                this.seekBarSensitivity.setProgress(i3);
                this.tvSensitivityValue.setText(String.valueOf(i3));
                return;
            }
            this.seekBarSensitivity.setProgress(90);
            this.tvSensitivityValue.setText(String.valueOf(90));
        }
    }

    public void startRecord() {
        if (this.mstartMicroHandler == null) {
            this.mstartMicroHandler = new Handler();
        }
        if (this.mstartMicroRunnable == null) {
            this.mstartMicroRunnable = new Runnable() { // from class: com.home.fragment.ble.MusicFragment.38
                @Override // java.lang.Runnable
                public void run() {
                    MusicFragment.this.mstartMicroHandler.postDelayed(MusicFragment.this.mstartMicroRunnable, 50L);
                    if (MusicFragment.this.volumCircleBar != null) {
                        MusicFragment.this.volumCircleBar.updateVolumRate(MusicFragment.this.microRecordValue);
                    }
                }
            };
        }
        this.mstartMicroHandler.postDelayed(this.mstartMicroRunnable, 500L);
    }

    private void sendMusicData() {
        if (this.sendMusicThread == null) {
            Thread thread = new Thread(new Runnable() { // from class: com.home.fragment.ble.MusicFragment.39
                @Override // java.lang.Runnable
                public void run() {
                    while (MusicFragment.this.isStartTimer) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (MusicFragment.this.mediaPlayer != null && MusicFragment.this.chnnelValue > 0 && MusicFragment.this.canSendCmd && MusicFragment.this.mediaPlayer.isPlaying()) {
                            MusicFragment musicFragment = MusicFragment.this;
                            musicFragment.sendMusicValue(musicFragment.getRandColor(), MusicFragment.this.chnnelValue);
                        }
                        if (MusicFragment.this.mActivity.isFinishing()) {
                            return;
                        }
                    }
                }
            });
            this.sendMusicThread = thread;
            thread.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCar01Mode(int i, boolean z, View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
        this.mActivity.setMode(true, false, z, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putDataBack(int i) {
        this.mActivity.setMode(true, false, false, i);
    }

    private ArrayList<MyColor> getCAR00SelectColor() {
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

    private void initColorBlock() {
        boolean z;
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, InputDeviceCompat.SOURCE_ANY, -16711681, -65281, -1};
        View findViewById = this.mContentView.findViewById(R.id.linearLayoutViewBlocks);
        this.colorTextViews = new ArrayList<>();
        int i = 1;
        while (true) {
            if (i > 16) {
                z = false;
                break;
            }
            if (this.sp.getInt((String) ((ColorTextView) findViewById.findViewWithTag("vtlColor" + i)).getTag(), 0) != 0) {
                z = true;
                break;
            }
            i++;
        }
        for (int i2 = 1; i2 <= 16; i2++) {
            final ColorTextView colorTextView = (ColorTextView) findViewById.findViewWithTag("vtlColor" + i2);
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
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.40
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(MusicFragment.this.getContext(), R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    if (color == 0) {
                        MusicFragment.this.showColorCover((ColorTextView) view);
                    } else {
                        MusicFragment.this.updateRgbText(Tool.getRGB(color));
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.fragment.ble.MusicFragment.41
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    MusicFragment.this.sp.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(MusicFragment.this.getResources().getDrawable(R.drawable.block_shap_color));
                    colorTextView2.setText("+");
                    return true;
                }
            });
            this.colorTextViews.add(colorTextView);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showColorCover(ColorTextView colorTextView) {
        this.actionView = colorTextView;
        this.currentSelecColorFromPicker = 0;
        this.textRGB.setText(getResources().getString(R.string.r_g_b, 0, 0, 0));
        PopupWindow popupWindow = new PopupWindow(this.menuView, -1, -1, true);
        this.mPopupWindow = popupWindow;
        popupWindow.showAtLocation(this.mContentView, 80, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideColorCover() {
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private void initColorSelecterView() {
        this.textRGB = (TextView) this.menuView.findViewById(R.id.tvRGB);
        this.textViewRingBrightSC = (TextView) this.menuView.findViewById(R.id.tvRingBrightnessSC);
        this.imageViewPicker2 = (ColorPickerView) this.menuView.findViewById(R.id.imageViewPicker2);
        this.blackWiteSelectView = (BlackWiteSelectView) this.menuView.findViewById(R.id.blackWiteSelectView);
        ColorPickerView colorPickerView = (ColorPickerView) this.menuView.findViewById(R.id.imageViewPicker2);
        this.imageViewPicker2 = colorPickerView;
        colorPickerView.setInitialColor(getResources().getColor(R.color.white));
        this.imageViewPicker2.subscribe(new ColorObserver() { // from class: com.home.fragment.ble.MusicFragment$$ExternalSyntheticLambda1
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                MusicFragment.this.m18x5812ee34(i, z, z2);
            }
        });
        this.blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.ble.MusicFragment.42
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                MusicFragment.this.currentSelecColorFromPicker = i;
                int i3 = i2 <= 0 ? 1 : i2;
                if (i2 >= 100) {
                    i3 = 100;
                }
                MusicFragment.this.textViewRingBrightSC.setText(MusicFragment.this.getResources().getString(R.string.brightness_set, Integer.valueOf(i3)));
                MainActivity_BLE.getMainActivity().setBrightNess(i3, false, false, false);
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
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.43
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    MusicFragment.this.startAnimation(view);
                    int intValue = ((Integer) view.getTag()).intValue();
                    MusicFragment.this.currentSelecColorFromPicker = intValue;
                    MusicFragment.this.blackWiteSelectView.setStartColor(intValue);
                    MusicFragment.this.imageViewPicker2.setInitialColor(intValue);
                    int[] rgb = Tool.getRGB(intValue);
                    MusicFragment.this.textRGB.setText(MusicFragment.this.getResources().getString(R.string.r_g_b, Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
                    MainActivity_BLE.getMainActivity().setRgb(rgb[0], rgb[1], rgb[2], false, false, false, true);
                }
            });
            arrayList.add(findViewWithTag);
        }
        Button button = (Button) this.menuView.findViewById(R.id.buttonConfirm);
        this.buttonConfirButton = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.MusicFragment.44
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MusicFragment.this.hideColorCover();
                if (MusicFragment.this.currentSelecColorFromPicker == 0) {
                    return;
                }
                float f = 10;
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                shapeDrawable.getPaint().setColor(MusicFragment.this.currentSelecColorFromPicker);
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                MusicFragment.this.actionView.setColor(MusicFragment.this.currentSelecColorFromPicker);
                MusicFragment.this.actionView.setBackgroundDrawable(shapeDrawable);
                MusicFragment.this.sp.edit().putInt((String) MusicFragment.this.actionView.getTag(), MusicFragment.this.currentSelecColorFromPicker).commit();
                MusicFragment.this.actionView.setText("");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initColorSelecterView$0$com-home-fragment-ble-MusicFragment  reason: not valid java name */
    public /* synthetic */ void m18x5812ee34(int i, boolean z, boolean z2) {
        if (z) {
            BlackWiteSelectView blackWiteSelectView = this.blackWiteSelectView;
            if (blackWiteSelectView != null) {
                blackWiteSelectView.setStartColor(i);
            }
            this.currentSelecColorFromPicker = i;
            int[] rgb = Tool.getRGB(i);
            this.textRGB.setText(getResources().getString(R.string.r_g_b, Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
            MainActivity_BLE.getMainActivity().setRgb(rgb[0], rgb[1], rgb[2], true, false, false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
    }

    public void updateRgbText(int[] iArr) {
        if (MainActivity_BLE.getMainActivity() != null) {
            MainActivity_BLE.getMainActivity().setRgb(iArr[0], iArr[1], iArr[2], false, false, false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MyColor getRandColor() {
        ArrayList<MyColor> arrayList = this.colors;
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        return this.colors.get(this.random.nextInt(this.colors.size()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playAnimationPress(View view) {
        view.startAnimation(this.animationScale);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlayProgress(Mp3 mp3) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        int duration = mp3.getDuration();
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        if (duration == 0 || currentPosition >= duration) {
            return;
        }
        this.seekBarMusic.setProgress((currentPosition * 100) / duration);
    }

    public void pauseMusic() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        this.isMusic = false;
        if (this.isStartTimer) {
            this.isStartTimer = false;
            this.sendMusicThread = null;
        }
        this.mediaPlayer.pause();
        this.imageViewPlay.setImageResource(R.drawable.bg_play);
        this.mActivity.setMusicBrightness(1);
    }

    public void startMusice() {
        this.imageViewPlay.setImageResource(R.drawable.bg_play);
        if (this.imageViewRotate.getVisibility() == 0 || this.audioRecord == null) {
            return;
        }
        this.isClickMicro = true;
        this.volumCircleBar.toggleRecord(0);
        this.audioRecord.startRecording();
        if (this.isClickMicro) {
            sendMicro();
        }
        startRecord();
        sendMicroMode();
    }

    public void startMicro() {
        if (this.isClickMicro) {
            this.canSendCmd = true;
        }
    }

    public void pauseVolum(boolean z) {
        if (z) {
            this.canSendCmd = false;
        } else {
            this.canSendCmd = true;
        }
    }

    public void playPause() {
        if (ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
            return;
        }
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            startPlay(0);
        } else if (mediaPlayer.isPlaying()) {
            this.canSendCmd = false;
            this.isMusic = false;
            this.mediaPlayer.pause();
            stopRotate();
            this.imageViewPlay.setImageResource(R.drawable.bg_play);
            if (MainActivity_BLE.getMainActivity() == null || !MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                return;
            }
            this.mActivity.setMusicBrightness(1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: com.home.fragment.ble.MusicFragment.46
                @Override // java.lang.Runnable
                public void run() {
                    MusicFragment.this.mActivity.setMusicBrightness(1);
                    handler.removeCallbacks(this);
                }
            }, 200L);
        } else if (this.currentMp3 != null) {
            this.canSendCmd = true;
            this.isStartTimer = true;
            this.isMusic = true;
            this.sendMusicThread = null;
            this.imageViewPlay.setImageResource(R.drawable.bg_play_pause);
            this.mediaPlayer.start();
            sendMusicData();
            resumeRotate();
            sendMu();
            if (this.mActivity != null) {
                if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                    sendMusicMode();
                }
            }
        } else if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            startPlay(0);
        } else {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
        }
    }

    public void palayNext() {
        if (this.currentMp3 == null) {
            if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
                startPlay(0);
            } else {
                Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
            }
        } else if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            int findCurrentIndex = findCurrentIndex(this.currentMp3);
            if (findCurrentIndex != -1) {
                if (findCurrentIndex < LedBleApplication.getApp().getMp3s().size() - 1) {
                    startPlay(findCurrentIndex + 1);
                    return;
                } else if (findCurrentIndex == LedBleApplication.getApp().getMp3s().size() - 1) {
                    startPlay(0);
                    return;
                } else {
                    return;
                }
            }
            startPlay(0);
        } else {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
        }
    }

    public void playPre() {
        if (this.currentMp3 == null) {
            if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
                startPlay(0);
            } else {
                Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
            }
        } else if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            int findCurrentIndex = findCurrentIndex(this.currentMp3);
            if (findCurrentIndex == -1) {
                startPlay(0);
            } else if (findCurrentIndex > 0) {
                startPlay(findCurrentIndex - 1);
            } else if (findCurrentIndex == 0) {
                startPlay(LedBleApplication.getApp().getMp3s().size() - 1);
            }
        } else {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
        }
    }

    public void playMp3(final Mp3 mp3) {
        this.currentMp3 = mp3;
        setTitles(mp3);
        setAbulmImage(this.imageViewRotate, mp3);
        if (this.mediaPlayer == null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mediaPlayer = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
        }
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer.reset();
        try {
            this.mediaPlayer.setDataSource(mp3.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!this.isSettingBoolean) {
            this.isSettingBoolean = true;
            Visualizer visualizer = new Visualizer(this.mediaPlayer.getAudioSessionId());
            this.mVisualizer = visualizer;
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        }
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.home.fragment.ble.MusicFragment.47
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer2) {
                try {
                    ArrayList<Mp3> mp3s = LedBleApplication.getApp().getMp3s();
                    int findCurrentIndex = MusicFragment.this.findCurrentIndex(mp3);
                    if (MusicFragment.this.isLoopOne || !MusicFragment.this.isRandom) {
                        if (!MusicFragment.this.isRandom && MusicFragment.this.isLoopAll) {
                            MusicFragment.this.playMp3(mp3s.get(MusicFragment.this.getRandIntPlayIndex(mp3s.size())));
                            return;
                        } else if (-1 == findCurrentIndex || ListUtiles.isEmpty(mp3s)) {
                            return;
                        } else {
                            if (findCurrentIndex == mp3s.size() - 1) {
                                MusicFragment.this.playMp3(mp3s.get(0));
                                return;
                            } else if (findCurrentIndex <= mp3s.size() - 2) {
                                MusicFragment.this.playMp3(mp3s.get(findCurrentIndex + 1));
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                    MusicFragment.this.mediaPlayer.start();
                    MusicFragment.this.mediaPlayer.setLooping(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        try {
            this.mediaPlayer.prepare();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.home.fragment.ble.MusicFragment$$ExternalSyntheticLambda0
            @Override // android.media.MediaPlayer.OnPreparedListener
            public final void onPrepared(MediaPlayer mediaPlayer2) {
                MusicFragment.this.m19lambda$playMp3$1$comhomefragmentbleMusicFragment(mediaPlayer2);
            }
        });
        startRotate();
        if (this.mActivity != null && (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-01-"))) {
            sendMusicMode();
        }
        this.mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() { // from class: com.home.fragment.ble.MusicFragment.48
            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onWaveFormDataCapture(Visualizer visualizer2, byte[] bArr, int i) {
                if (MusicFragment.this.mVisualizerView != null) {
                    MusicFragment.this.mVisualizerView.updateVisualizer(bArr);
                }
            }

            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onFftDataCapture(Visualizer visualizer2, byte[] bArr, int i) {
                if (MusicFragment.this.mVisualizerView != null) {
                    MusicFragment.this.mVisualizerView.updateVisualizer(bArr);
                }
            }
        }, Visualizer.getMaxCaptureRate() / 2, false, true);
        Visualizer visualizer2 = this.mVisualizer;
        if (visualizer2 != null) {
            visualizer2.setEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$playMp3$1$com-home-fragment-ble-MusicFragment  reason: not valid java name */
    public /* synthetic */ void m19lambda$playMp3$1$comhomefragmentbleMusicFragment(MediaPlayer mediaPlayer) {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.start();
        }
    }

    public void sendMusicMode() {
        if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-00-") || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-02-")) {
            int i = this.musicMode;
            if (i == 0) {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, 4);
            } else {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, i - 1);
            }
        } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-01-")) {
            int i2 = this.musicMode;
            if (i2 == 0) {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, true, 3);
            } else {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, true, i2 - 1);
            }
        } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_00) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDCAR-00-")) {
            int i3 = this.musicMode;
            if (i3 == 0) {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, 2);
            } else if (i3 == 1) {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, 1);
            } else if (i3 == 2) {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, 3);
            } else if (i3 != 3) {
            } else {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, 0);
            }
        } else {
            this.canSendCmd = false;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: com.home.fragment.ble.MusicFragment.49
                @Override // java.lang.Runnable
                public void run() {
                    MusicFragment.this.canSendCmd = true;
                    handler.removeCallbacks(this);
                }
            }, getSelectColor().size() * ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
            if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDDMX)) {
                if (this.musicMode == 0) {
                    this.mActivity.setDiy(getSelectColor(), 4);
                    return;
                } else {
                    this.mActivity.setDiy(getSelectColor(), this.musicMode - 1);
                    return;
                }
            }
            int i4 = this.musicMode;
            if (i4 == 0) {
                this.mActivity.setDiy(getSelectColor(), 1);
            } else if (i4 == 1) {
                this.mActivity.setDiy(getSelectColor(), 3);
            } else if (i4 == 2) {
                this.mActivity.setDiy(getSelectColor(), 0);
            } else if (i4 != 3) {
            } else {
                this.mActivity.setDiy(getSelectColor(), 2);
            }
        }
    }

    public void sendMicroMode() {
        this.canSendCmd = false;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() { // from class: com.home.fragment.ble.MusicFragment.50
            @Override // java.lang.Runnable
            public void run() {
                MusicFragment.this.canSendCmd = true;
                handler.removeCallbacks(this);
            }
        }, getSelectColor().size() * ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        int i = this.microMode;
        if (i == 0) {
            if (this.rbNeiMai.isChecked()) {
                this.mActivity.setDiy(getSelectColor(), 0);
                this.mActivity.setSpeed(80, true, false);
                return;
            }
            NetConnectBle.getInstance().setDynamicDiy(getSelectColor(), 0);
            NetConnectBle.getInstance().setSensitivity(90, false, false, MainActivity_BLE.sceneBean);
        } else if (i == 1) {
            if (this.rbNeiMai.isChecked()) {
                this.mActivity.setDiy(getSelectColor(), 1);
                this.mActivity.setSpeed(60, true, false);
                return;
            }
            NetConnectBle.getInstance().setDynamicDiy(getSelectColor(), 1);
            NetConnectBle.getInstance().setSensitivity(90, false, false, MainActivity_BLE.sceneBean);
        } else if (i == 2) {
            if (this.rbNeiMai.isChecked()) {
                this.mActivity.setDiy(getSelectColor(), 2);
                this.mActivity.setSpeed(98, true, false);
                return;
            }
            NetConnectBle.getInstance().setDynamicDiy(getSelectColor(), 2);
            NetConnectBle.getInstance().setSensitivity(90, false, false, MainActivity_BLE.sceneBean);
        } else if (i != 3) {
        } else {
            if (this.rbNeiMai.isChecked()) {
                this.mActivity.setDiy(getSelectColor(), 3);
                this.mActivity.setSpeed(100, true, false);
                return;
            }
            NetConnectBle.getInstance().setDynamicDiy(getSelectColor(), 3);
            NetConnectBle.getInstance().setSensitivity(90, false, false, MainActivity_BLE.sceneBean);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<MyColor> getVoiceCtlSelectColor() {
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

    private ArrayList<MyColor> getSelectColor() {
        ArrayList<MyColor> arrayList = new ArrayList<>();
        ArrayList<MyColor> arrayList2 = this.colors;
        int i = 0;
        if (arrayList2 == null || arrayList2.size() == 0) {
            while (i < 7) {
                arrayList.add(new MyColor(getRandIntColor(), getRandIntColor(), getRandIntColor()));
                i++;
            }
        } else {
            while (i < this.colors.size()) {
                arrayList.add(this.colors.get(i));
                i++;
            }
        }
        return arrayList;
    }

    public void startRotate() {
        ObjectAnimator objectAnimator = this.mCircleAnimator;
        if (objectAnimator != null) {
            objectAnimator.start();
        }
    }

    public void resumeRotate() {
        ObjectAnimator objectAnimator = this.mCircleAnimator;
        if (objectAnimator != null) {
            objectAnimator.resume();
        }
    }

    public void stopRotate() {
        ObjectAnimator objectAnimator = this.mCircleAnimator;
        if (objectAnimator != null) {
            objectAnimator.pause();
        }
    }

    private void setTitles(Mp3 mp3) {
        if (StringUtils.isEmpty(mp3.getArtist())) {
            ScrollForeverTextView scrollForeverTextView = this.textViewAutoAjust;
            if (scrollForeverTextView != null) {
                scrollForeverTextView.setText(mp3.getTitle());
                return;
            }
            return;
        }
        Resources resources = getActivity().getResources();
        String string = resources.getString(R.string.ablum_title, "<<" + mp3.getTitle() + ">>", mp3.getArtist(), mp3.getAlbum());
        ScrollForeverTextView scrollForeverTextView2 = this.textViewAutoAjust;
        if (scrollForeverTextView2 != null) {
            scrollForeverTextView2.setText(string);
        }
    }

    private void setAbulmImage(ImageView imageView, Mp3 mp3) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mp3.getUrl());
            byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
            if (embeddedPicture != null) {
                DrawTool.toRoundBitmap(BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMusicValue(MyColor myColor, int i) {
        MediaPlayer mediaPlayer;
        if (!MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
            MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-01-");
        }
        Log.e(TAG, "chnnel ======== " + i);
        if (this.canSendCmd && (mediaPlayer = this.mediaPlayer) != null && mediaPlayer.isPlaying() && this.canSendCmd) {
            Log.e(TAG, "chnnel ======== " + i);
            this.mActivity.setMusicBrightness(this.chnnelValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendVolumValue(MyColor myColor, int i) {
        Log.e(TAG, "canSendCmd ======== " + this.canSendCmd);
        boolean z = this.canSendCmd;
        if (z && z && this.isClickMicro) {
            Log.e(TAG, "volumValue ======== " + i);
            this.mActivity.setMusicBrightness(i);
        }
    }

    private int getRandIntColor() {
        return this.rand.nextInt(255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRandIntPlayIndex(int i) {
        return this.rand.nextInt(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findCurrentIndex(Mp3 mp3) {
        ArrayList<Mp3> mp3s = LedBleApplication.getApp().getMp3s();
        if (ListUtiles.isEmpty(mp3s)) {
            return -1;
        }
        int size = mp3s.size();
        for (int i = 0; i < size; i++) {
            if (mp3.equals(mp3s.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private void startPlay(int i) {
        int duration;
        String str;
        if (ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s()) || LedBleApplication.getApp().getMp3s().size() <= i) {
            return;
        }
        this.canSendCmd = true;
        if (this.currentMp3 != null) {
            this.isStartTimer = true;
            this.canSendCmd = true;
            this.isMusic = true;
            this.sendMusicThread = null;
            this.imageViewPlay.setImageResource(R.drawable.bg_play_pause);
            this.mediaPlayer.start();
            sendMusicData();
            resumeRotate();
            sendMu();
        }
        sendMusicData();
        sendMu();
        playMp3(LedBleApplication.getApp().getMp3s().get(i));
        this.imageViewPlay.setImageResource(R.drawable.bg_play_pause);
        int duration2 = (this.mediaPlayer.getDuration() / 1000) % 60;
        if (duration2 < 10) {
            str = NetResult.CODE_OK + duration2;
        } else {
            str = "" + duration2;
        }
        this.tvCurrentTime.setText("0:00");
        this.tvTotalTime.setText((duration / 60) + ":" + str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 100 && i2 == -1) {
            this.isMusic = true;
            this.isStartTimer = true;
            sendMusicData();
            startPlay(0);
            sendMu();
        }
        if (i == 102 && i2 == -1 && intent != null) {
            this.colors = (ArrayList) intent.getSerializableExtra("color");
            this.canSendCmd = false;
            if (!this.isClickMicro && this.currentMp3 != null) {
                this.isStartTimer = true;
                this.isMusic = true;
                this.sendMusicThread = null;
                this.imageViewPlay.setImageResource(R.drawable.bg_play_pause);
                this.mediaPlayer.start();
                sendMusicData();
                resumeRotate();
                sendMu();
            }
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() { // from class: com.home.fragment.ble.MusicFragment.51
                @Override // java.lang.Runnable
                public void run() {
                    MusicFragment.this.canSendCmd = true;
                    handler.removeCallbacksAndMessages(null);
                }
            };
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                handler.postDelayed(runnable, getSelectColor().size() * 300);
            } else {
                handler.postDelayed(runnable, getSelectColor().size() * 300);
            }
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01) || MainActivity_BLE.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                if (this.musicMode == 0) {
                    this.mActivity.setDiy(getSelectColor(), 3);
                    return;
                } else {
                    this.mActivity.setDiy(getSelectColor(), this.musicMode - 1);
                    return;
                }
            }
            changeColor(true, false, true, getSelectColor());
        }
    }

    private void changeColor(boolean z, boolean z2, boolean z3, ArrayList<MyColor> arrayList) {
        this.mActivity.setChangeColor(z, z2, z3, arrayList);
    }

    public void sendMicro() {
        new Thread(new Runnable() { // from class: com.home.fragment.ble.MusicFragment.52
            @Override // java.lang.Runnable
            public void run() {
                int i = MusicFragment.this.bs;
                short[] sArr = new short[i];
                if (MusicFragment.this.rbNeiMai != null) {
                    while (MusicFragment.this.rbNeiMai.isChecked()) {
                        if (MusicFragment.this.audioRecord != null && MusicFragment.this.volumCircleBar != null && MusicFragment.this.volumCircleBar.recordMode() != 4) {
                            int read = MusicFragment.this.audioRecord.read(sArr, 0, MusicFragment.this.bs);
                            long j = 0;
                            for (int i2 = 0; i2 < i; i2++) {
                                j += sArr[i2] * sArr[i2];
                            }
                            double d = j;
                            double d2 = read;
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            Message message = new Message();
                            message.what = 103;
                            message.obj = Double.valueOf(d / d2);
                            MusicFragment.this.mhanHandler.sendMessage(message);
                        }
                        if (MusicFragment.this.activity.isFinishing()) {
                            return;
                        }
                    }
                }
            }
        }).start();
    }

    public void sendMu() {
        new Thread(new Runnable() { // from class: com.home.fragment.ble.MusicFragment.53
            @Override // java.lang.Runnable
            public void run() {
                do {
                    try {
                        if (!MusicFragment.this.isMusic) {
                            return;
                        }
                        if (MusicFragment.this.canSendCmd && MusicFragment.this.mediaPlayer != null && MusicFragment.this.mediaPlayer.isPlaying()) {
                            MusicFragment.this.mhanHandler.sendEmptyMessage(101);
                        }
                        Thread.sleep(300L);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } while (!MusicFragment.this.activity.isFinishing());
            }
        }).start();
    }

    /* loaded from: classes.dex */
    class VisualizerView extends View {
        private byte[] mBytes;
        private boolean mFirst;
        private float[] mPoints;
        private Rect mRect;
        private int mSpectrumNum;

        public VisualizerView(Context context) {
            super(context);
            this.mRect = new Rect();
            this.mSpectrumNum = 20;
            this.mFirst = true;
        }

        public void updateVisualizer(byte[] bArr) {
            MusicFragment musicFragment;
            byte[] bArr2 = new byte[(bArr.length / 2) + 1];
            bArr2[0] = (byte) Math.abs((int) bArr[0]);
            int i = 2;
            for (int i2 = 1; i2 < this.mSpectrumNum; i2++) {
                bArr2[i2] = (byte) Math.hypot(bArr[i], bArr[i + 1]);
                i += 2;
            }
            this.mBytes = bArr2;
            float[] fArr = this.mPoints;
            if (fArr == null || fArr.length < bArr2.length * 4) {
                this.mPoints = new float[bArr2.length * 4];
            }
            this.mRect.set(0, 0, getWidth(), getHeight());
            int width = this.mRect.width() / this.mSpectrumNum;
            int height = this.mRect.height();
            for (int i3 = 0; i3 < this.mSpectrumNum; i3++) {
                byte[] bArr3 = this.mBytes;
                if (bArr3[i3] < 0) {
                    bArr3[i3] = Byte.MAX_VALUE;
                }
                float[] fArr2 = this.mPoints;
                int i4 = i3 * 4;
                float f = (width * i3) + (width / 2);
                fArr2[i4] = f;
                fArr2[i4 + 1] = height;
                fArr2[i4 + 2] = f;
                int i5 = i4 + 3;
                fArr2[i5] = height - bArr3[i3];
                MusicFragment.this.chnnelValue = (int) (1.0f - fArr2[i5]);
                MusicFragment.this.chnnelValue = (int) Math.pow(musicFragment.chnnelValue, 1.6d);
                if (MusicFragment.this.chnnelValue <= 1) {
                    MusicFragment.this.chnnelValue = 1;
                }
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 200 && this.openMac) {
            requestMicroPermissionsSucess(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> buildModel() {
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= 255; i++) {
            arrayList.add("MODE " + i);
        }
        return arrayList;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            audioRecord.stop();
            this.audioRecord.release();
            this.audioRecord = null;
        }
        Handler handler = this.mstartMicroHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mstartMicroRunnable);
            this.mstartMicroRunnable = null;
            this.mstartMicroHandler = null;
        }
        Visualizer visualizer = this.mVisualizer;
        if (visualizer != null) {
            visualizer.setEnabled(false);
            this.mVisualizer = null;
        }
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        this.mediaPlayer.stop();
        this.mediaPlayer = null;
    }
}
