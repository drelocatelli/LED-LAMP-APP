package com.home.fragment.dmx02;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
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
import com.home.activity.main.MainActivity_DMX02;
import com.home.activity.other.Dmx02EditColorActivity;
import com.home.activity.other.Dmx02MusicLibActivity;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.bean.Mp3;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.ledlamp.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/* loaded from: classes.dex */
public class DMX02MusicFragment extends LedBleFragment {
    private static final int DMX02_MUSIC_INT_EDIT_COLOR = 1003;
    private static final int INT_GO_SELECT_MUSIC = 100;
    private static final int INT_UPDATE_PROGRESS = 101;
    private static final int INT_UPDATE_RECORD = 103;
    private static int SAMPLE_RATE_IN_HZ = 8000;
    private static final String TAG = "DMX02MusicFragment";
    private Animation animationRotate;
    private Animation animationScale;
    private AudioRecord audioRecord;
    private int bs;
    @BindView(R.id.buttonMusicLib)
    Button buttonMusicLib;
    private volatile int chnnelValue;
    private ArrayList<MyColor> colors;
    private Mp3 currentMp3;
    @BindView(R.id.imageViewNext)
    ImageView imageViewNext;
    @BindView(R.id.imageViewPlay)
    ImageView imageViewPlay;
    @BindView(R.id.imageViewPlayType)
    ImageView imageViewPlayType;
    @BindView(R.id.imageViewPre)
    ImageView imageViewPre;
    @BindView(R.id.imageViewRotate)
    ImageView imageViewRotate;
    @BindView(R.id.ivEditColor)
    Button ivEditColor;
    @BindView(R.id.llMusic)
    LinearLayout llMusic;
    private MainActivity_DMX02 mActivity;
    private ObjectAnimator mCircleAnimator;
    private View mContentView;
    private Visualizer mVisualizer;
    private VisualizerView mVisualizerView;
    private MediaPlayer mediaPlayer;
    private volatile float microRecordValue;
    private Handler mstartMicroHandler;
    private Runnable mstartMicroRunnable;
    private RadioButton rbMusicOne;
    private RadioButton rbMusicTwo;
    @BindView(R.id.rlDMX02VoiceCtl)
    RelativeLayout rlDMX02VoiceCtl;
    @BindView(R.id.seekBarMusic)
    SeekBar seekBarMusic;
    @BindView(R.id.seekBarRhythm)
    SeekBar seekBarRhythm;
    @BindView(R.id.seekBarSensitivityDMX02)
    SeekBar seekBarSensitivityDMX02;
    private SegmentedRadioGroup segmentedRadioGroup;
    private Thread sendMusicThread;
    @BindView(R.id.textViewAutoAjust)
    ScrollForeverTextView textViewAutoAjust;
    @BindView(R.id.textViewSensitivityDMX02)
    TextView textViewSensitivityDMX02;
    @BindView(R.id.tvCurrentTime)
    TextView tvCurrentTime;
    @BindView(R.id.tvRhythm)
    TextView tvRhythm;
    @BindView(R.id.tvTotalTime)
    TextView tvTotalTime;
    @BindView(R.id.tvrhythmValue)
    TextView tvrhythmValue;
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
    private Handler mhanHandler = new Handler() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.15
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
                DMX02MusicFragment.this.microRecordValue = pow / 100.0f;
                DMX02MusicFragment dMX02MusicFragment = DMX02MusicFragment.this;
                dMX02MusicFragment.sendVolumValue(dMX02MusicFragment.getRandColor(), pow);
                return;
            }
            DMX02MusicFragment dMX02MusicFragment2 = DMX02MusicFragment.this;
            dMX02MusicFragment2.updatePlayProgress(dMX02MusicFragment2.currentMp3);
            if (DMX02MusicFragment.this.mediaPlayer != null) {
                int currentPosition = DMX02MusicFragment.this.mediaPlayer.getCurrentPosition() / 1000;
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
                if (DMX02MusicFragment.this.tvCurrentTime == null || str2 == null) {
                    return;
                }
                DMX02MusicFragment.this.tvCurrentTime.setText(str2);
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
        View inflate = layoutInflater.inflate(R.layout.dmx02_fragment_music, viewGroup, false);
        this.mContentView = inflate;
        return inflate;
    }

    public boolean isCheckSegmentedRadioGroupIndexTwo() {
        return this.segmentedRadioGroup.getCheckedRadioButtonId() == R.id.rbMusicTwo;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        MainActivity_DMX02 mainActivity_DMX02 = (MainActivity_DMX02) getActivity();
        this.mActivity = mainActivity_DMX02;
        SegmentedRadioGroup segmentMusic = mainActivity_DMX02.getSegmentMusic();
        this.segmentedRadioGroup = segmentMusic;
        segmentMusic.check(R.id.rbMusicOne);
        this.rbMusicOne = (RadioButton) this.segmentedRadioGroup.findViewById(R.id.rbMusicOne);
        this.rbMusicTwo = (RadioButton) this.segmentedRadioGroup.findViewById(R.id.rbMusicTwo);
        if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-02-")) {
            this.rbMusicOne.setText(getString(R.string.Voicecontrol));
            this.rbMusicTwo.setText(getString(R.string.tab_music));
        }
        this.ivEditColor.setVisibility(8);
        this.segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.rbMusicOne == i) {
                    MainActivity_DMX02 unused = DMX02MusicFragment.this.mActivity;
                    if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-02-")) {
                        DMX02MusicFragment.this.rlDMX02VoiceCtl.setVisibility(0);
                        DMX02MusicFragment.this.ivEditColor.setVisibility(8);
                        DMX02MusicFragment.this.llMusic.setVisibility(8);
                        DMX02MusicFragment.this.pauseMusic();
                        return;
                    }
                    DMX02MusicFragment.this.llMusic.setVisibility(0);
                    DMX02MusicFragment.this.tvRhythm.setText(DMX02MusicFragment.this.getString(R.string.speed));
                    if (DMX02MusicFragment.this.mediaPlayer != null && DMX02MusicFragment.this.mediaPlayer.isPlaying()) {
                        DMX02MusicFragment.this.sendMusicMode();
                    }
                    DMX02MusicFragment.this.isClickMicro = false;
                    return;
                }
                MainActivity_DMX02 unused2 = DMX02MusicFragment.this.mActivity;
                if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-02-")) {
                    DMX02MusicFragment.this.rlDMX02VoiceCtl.setVisibility(8);
                    DMX02MusicFragment.this.ivEditColor.setVisibility(0);
                    DMX02MusicFragment.this.llMusic.setVisibility(0);
                    DMX02MusicFragment.this.tvRhythm.setText(DMX02MusicFragment.this.getString(R.string.sensitivity));
                    DMX02MusicFragment.this.rbMusicOne.setText(DMX02MusicFragment.this.getString(R.string.Voicecontrol));
                    DMX02MusicFragment.this.rbMusicTwo.setText(DMX02MusicFragment.this.getString(R.string.tab_music));
                }
            }
        });
        if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-02-") && MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-02-")) {
            this.seekBarSensitivityDMX02.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.2
                @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar seekBar) {
                    super.onStopTrackingTouch(seekBar);
                    if (DMX02MusicFragment.this.textViewSensitivityDMX02.getTag() != null) {
                        if (DMX02MusicFragment.this.textViewSensitivityDMX02.getTag().equals(100)) {
                            DMX02MusicFragment.this.mActivity.setSensitivity(100, false, false);
                        } else if (DMX02MusicFragment.this.textViewSensitivityDMX02.getTag().equals(1)) {
                            DMX02MusicFragment.this.mActivity.setSensitivity(1, false, false);
                        }
                    }
                }

                @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    if (z) {
                        if (i == 0) {
                            if (DMX02MusicFragment.this.mActivity != null) {
                                DMX02MusicFragment.this.mActivity.setSensitivity(1, false, false);
                            }
                            DMX02MusicFragment.this.textViewSensitivityDMX02.setText(DMX02MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, 1));
                            DMX02MusicFragment.this.textViewSensitivityDMX02.setTag(1);
                            MainActivity_DMX02 unused = DMX02MusicFragment.this.mActivity;
                            if (MainActivity_DMX02.sceneBean != null) {
                                Context context = DMX02MusicFragment.this.getContext();
                                StringBuilder sb = new StringBuilder();
                                MainActivity_DMX02 unused2 = DMX02MusicFragment.this.mActivity;
                                sb.append(MainActivity_DMX02.sceneBean);
                                sb.append(DMX02MusicFragment.TAG);
                                sb.append("sensitivity");
                                SharePersistent.saveInt(context, sb.toString(), 1);
                                return;
                            }
                            return;
                        }
                        if (DMX02MusicFragment.this.mActivity != null) {
                            DMX02MusicFragment.this.mActivity.setSensitivity(i, false, false);
                        }
                        DMX02MusicFragment.this.textViewSensitivityDMX02.setText(DMX02MusicFragment.this.getActivity().getResources().getString(R.string.sensitivity_set, Integer.valueOf(i)));
                        DMX02MusicFragment.this.textViewSensitivityDMX02.setTag(Integer.valueOf(i));
                        MainActivity_DMX02 unused3 = DMX02MusicFragment.this.mActivity;
                        if (MainActivity_DMX02.sceneBean != null) {
                            Context context2 = DMX02MusicFragment.this.getContext();
                            StringBuilder sb2 = new StringBuilder();
                            MainActivity_DMX02 unused4 = DMX02MusicFragment.this.mActivity;
                            sb2.append(MainActivity_DMX02.sceneBean);
                            sb2.append(DMX02MusicFragment.TAG);
                            sb2.append("sensitivity");
                            SharePersistent.saveInt(context2, sb2.toString(), i);
                        }
                    }
                }
            });
            if (MainActivity_DMX02.sceneBean != null) {
                String str = MainActivity_DMX02.sceneBean;
                Context context = getContext();
                int i = SharePersistent.getInt(context, MainActivity_DMX02.sceneBean + TAG + "sensitivity");
                if (i > 0) {
                    this.seekBarSensitivityDMX02.setProgress(i);
                    this.textViewSensitivityDMX02.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i)));
                } else {
                    this.seekBarSensitivityDMX02.setProgress(90);
                    this.textViewSensitivityDMX02.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
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
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.RECORD_AUDIO") == 0) {
            this.bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, 1, 2);
            this.audioRecord = new AudioRecord(1, SAMPLE_RATE_IN_HZ, 1, 2, this.bs);
        }
        initCoiceCtlViewBlicks();
    }

    private void initCoiceCtlViewBlicks() {
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(DMX02MusicFragment.this.getActivity(), R.anim.layout_scale));
                DMX02MusicFragment.this.mActivity.setVoiceCtlAndMusicMode(true, false, false, Integer.parseInt(((TextView) view).getText().toString()));
            }
        };
        for (int i = 1; i <= 4; i++) {
            RelativeLayout relativeLayout = this.rlDMX02VoiceCtl;
            View findViewWithTag = relativeLayout.findViewWithTag("ivMicroMode" + i);
            findViewWithTag.setOnClickListener(onClickListener);
            findViewWithTag.setTag("ivMicroMode" + i);
        }
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
        this.imageViewPre.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02MusicFragment.this.playAnimationPress(view);
                DMX02MusicFragment.this.playPre();
            }
        });
        this.imageViewPlay.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02MusicFragment.this.playAnimationPress(view);
                DMX02MusicFragment.this.playPause();
            }
        });
        this.imageViewNext.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX02MusicFragment.this.playAnimationPress(view);
                DMX02MusicFragment.this.palayNext();
            }
        });
        this.animationScale = AnimationUtils.loadAnimation(this.activity, R.anim.layout_scale);
        Animation loadAnimation = AnimationUtils.loadAnimation(this.activity, R.anim.rotate_frover);
        this.animationRotate = loadAnimation;
        loadAnimation.setInterpolator(new LinearInterpolator());
        this.animationRotate.setRepeatCount(-1);
        this.animationRotate.setAnimationListener(new AnimationListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.7
        });
        this.imageViewRotate.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int i = DMX02MusicFragment.this.musicMode;
                if (i == 0) {
                    DMX02MusicFragment.this.imageViewRotate.setBackground(DMX02MusicFragment.this.getResources().getDrawable(R.drawable.music_gradualchange));
                    DMX02MusicFragment.this.musicMode = 1;
                } else if (i == 1) {
                    DMX02MusicFragment.this.imageViewRotate.setBackground(DMX02MusicFragment.this.getResources().getDrawable(R.drawable.music_trailing));
                    DMX02MusicFragment.this.musicMode = 2;
                } else if (i == 2) {
                    DMX02MusicFragment.this.imageViewRotate.setBackground(DMX02MusicFragment.this.getResources().getDrawable(R.drawable.music_jump));
                    DMX02MusicFragment.this.musicMode = 3;
                } else if (i == 3) {
                    DMX02MusicFragment.this.imageViewRotate.setBackground(DMX02MusicFragment.this.getResources().getDrawable(R.drawable.music_stroboflash));
                    DMX02MusicFragment.this.musicMode = 4;
                } else if (i == 4) {
                    DMX02MusicFragment.this.imageViewRotate.setBackground(DMX02MusicFragment.this.getResources().getDrawable(R.drawable.music_nooutput));
                    DMX02MusicFragment.this.musicMode = 0;
                }
                DMX02MusicFragment.this.sendMusicMode();
            }
        });
        this.imageViewPlayType.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!DMX02MusicFragment.this.isLoopAll) {
                    if (!DMX02MusicFragment.this.isLoopOne) {
                        if (DMX02MusicFragment.this.isRandom) {
                            DMX02MusicFragment.this.imageViewPlayType.setImageResource(R.drawable.playtype_random);
                            Toast.makeText(DMX02MusicFragment.this.mActivity, (int) R.string.RandomPlay, 0).show();
                            DMX02MusicFragment.this.isRandom = false;
                            DMX02MusicFragment.this.isLoopAll = true;
                            return;
                        }
                        return;
                    }
                    DMX02MusicFragment.this.imageViewPlayType.setImageResource(R.drawable.playtype_loopone);
                    Toast.makeText(DMX02MusicFragment.this.mActivity, (int) R.string.SinglePlay, 0).show();
                    DMX02MusicFragment.this.isLoopOne = false;
                    DMX02MusicFragment.this.isRandom = true;
                    return;
                }
                DMX02MusicFragment.this.imageViewPlayType.setImageResource(R.drawable.playtype_loopall);
                Toast.makeText(DMX02MusicFragment.this.mActivity, (int) R.string.SequentialPlay, 0).show();
                DMX02MusicFragment.this.isLoopAll = false;
                DMX02MusicFragment.this.isLoopOne = true;
            }
        });
        this.buttonMusicLib.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(DMX02MusicFragment.this.getContext(), "android.permission.RECORD_AUDIO") != 0) {
                    DMX02MusicFragment.this.mActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 200);
                    return;
                }
                Intent intent = new Intent(DMX02MusicFragment.this.mActivity, Dmx02MusicLibActivity.class);
                intent.putExtra("sun", 200);
                DMX02MusicFragment.this.startActivityForResult(intent, 100);
            }
        });
        this.ivEditColor.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX02MusicFragment.this.mediaPlayer != null && DMX02MusicFragment.this.mediaPlayer.isPlaying()) {
                    DMX02MusicFragment.this.playPause();
                }
                DMX02MusicFragment.this.pauseVolum(true);
                DMX02MusicFragment.this.mActivity.startActivityForResult(new Intent(DMX02MusicFragment.this.mActivity, Dmx02EditColorActivity.class), 1003);
            }
        });
        this.seekBarRhythm.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.12
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    DMX02MusicFragment.this.tvrhythmValue.setText(String.valueOf(i));
                    MainActivity_DMX02 unused = DMX02MusicFragment.this.mActivity;
                    if (MainActivity_DMX02.sceneBean != null) {
                        MainActivity_DMX02 unused2 = DMX02MusicFragment.this.mActivity;
                        if (MainActivity_DMX02.sceneBean.equalsIgnoreCase("LEDDMX-02-")) {
                            DMX02MusicFragment.this.mActivity.setSensitivity(i, true, true);
                        } else {
                            DMX02MusicFragment.this.mActivity.setSpeed(i, false, false);
                        }
                        Context context = DMX02MusicFragment.this.getContext();
                        StringBuilder sb = new StringBuilder();
                        MainActivity_DMX02 unused3 = DMX02MusicFragment.this.mActivity;
                        sb.append(MainActivity_DMX02.sceneBean);
                        sb.append("rhythm");
                        SharePersistent.saveInt(context, sb.toString(), i);
                    }
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            FragmentActivity activity = getActivity();
            int i = SharePersistent.getInt(activity, MainActivity_DMX02.sceneBean + "rhythm");
            if (i > 0) {
                this.seekBarRhythm.setProgress(i);
                this.tvrhythmValue.setText(String.valueOf(i));
            } else {
                this.tvrhythmValue.setText(String.valueOf(80));
            }
        }
        this.seekBarMusic.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.13
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (DMX02MusicFragment.this.currentMp3 == null) {
                    Tool.ToastShow(DMX02MusicFragment.this.mActivity, (int) R.string.chose_list);
                    seekBar.setProgress(0);
                } else if (ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
                } else {
                    int duration = DMX02MusicFragment.this.currentMp3.getDuration();
                    DMX02MusicFragment.this.mediaPlayer.seekTo((seekBar.getProgress() * duration) / 100);
                }
            }
        });
    }

    private void sendMusicData() {
        if (this.sendMusicThread == null) {
            Thread thread = new Thread(new Runnable() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.14
                @Override // java.lang.Runnable
                public void run() {
                    while (DMX02MusicFragment.this.isStartTimer) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (DMX02MusicFragment.this.mediaPlayer != null && DMX02MusicFragment.this.chnnelValue > 0 && DMX02MusicFragment.this.canSendCmd && DMX02MusicFragment.this.mediaPlayer.isPlaying()) {
                            DMX02MusicFragment dMX02MusicFragment = DMX02MusicFragment.this;
                            dMX02MusicFragment.sendMusicValue(dMX02MusicFragment.getRandColor(), DMX02MusicFragment.this.chnnelValue);
                        }
                        if (DMX02MusicFragment.this.mActivity.isFinishing()) {
                            return;
                        }
                    }
                }
            });
            this.sendMusicThread = thread;
            thread.start();
        }
    }

    private void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
    }

    public void updateRgbText(int[] iArr) {
        if (MainActivity_DMX02.getMainActivity() != null) {
            MainActivity_DMX02.getMainActivity().setRgb(iArr[0], iArr[1], iArr[2], false, false, false, false);
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
        AudioRecord audioRecord;
        this.imageViewPlay.setImageResource(R.drawable.bg_play);
        if (this.imageViewRotate.getVisibility() == 0 || (audioRecord = this.audioRecord) == null) {
            return;
        }
        this.isClickMicro = true;
        audioRecord.startRecording();
    }

    public void startMicro() {
        if (this.isClickMicro) {
            this.canSendCmd = true;
        }
    }

    public void pauseVolum(boolean z) {
        this.canSendCmd = false;
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
            if (MainActivity_DMX02.getMainActivity() == null || !MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
                return;
            }
            this.mActivity.setMusicBrightness(1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.16
                @Override // java.lang.Runnable
                public void run() {
                    DMX02MusicFragment.this.mActivity.setMusicBrightness(1);
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
                if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE) || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
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
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.17
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer2) {
                try {
                    ArrayList<Mp3> mp3s = LedBleApplication.getApp().getMp3s();
                    int findCurrentIndex = DMX02MusicFragment.this.findCurrentIndex(mp3);
                    if (DMX02MusicFragment.this.isLoopOne || !DMX02MusicFragment.this.isRandom) {
                        if (!DMX02MusicFragment.this.isRandom && DMX02MusicFragment.this.isLoopAll) {
                            DMX02MusicFragment.this.playMp3(mp3s.get(DMX02MusicFragment.this.getRandIntPlayIndex(mp3s.size())));
                            return;
                        } else if (-1 == findCurrentIndex || ListUtiles.isEmpty(mp3s)) {
                            return;
                        } else {
                            if (findCurrentIndex == mp3s.size() - 1) {
                                DMX02MusicFragment.this.playMp3(mp3s.get(0));
                                return;
                            } else if (findCurrentIndex <= mp3s.size() - 2) {
                                DMX02MusicFragment.this.playMp3(mp3s.get(findCurrentIndex + 1));
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                    DMX02MusicFragment.this.mediaPlayer.start();
                    DMX02MusicFragment.this.mediaPlayer.setLooping(true);
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
        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment$$ExternalSyntheticLambda0
            @Override // android.media.MediaPlayer.OnPreparedListener
            public final void onPrepared(MediaPlayer mediaPlayer2) {
                DMX02MusicFragment.this.m24lambda$playMp3$0$comhomefragmentdmx02DMX02MusicFragment(mediaPlayer2);
            }
        });
        startRotate();
        if (this.mActivity != null && (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE) || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-"))) {
            sendMusicMode();
        }
        this.mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.18
            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onWaveFormDataCapture(Visualizer visualizer2, byte[] bArr, int i) {
                if (DMX02MusicFragment.this.mVisualizerView != null) {
                    DMX02MusicFragment.this.mVisualizerView.updateVisualizer(bArr);
                }
            }

            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onFftDataCapture(Visualizer visualizer2, byte[] bArr, int i) {
                if (DMX02MusicFragment.this.mVisualizerView != null) {
                    DMX02MusicFragment.this.mVisualizerView.updateVisualizer(bArr);
                }
            }
        }, Visualizer.getMaxCaptureRate() / 2, false, true);
        Visualizer visualizer2 = this.mVisualizer;
        if (visualizer2 != null) {
            visualizer2.setEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$playMp3$0$com-home-fragment-dmx02-DMX02MusicFragment  reason: not valid java name */
    public /* synthetic */ void m24lambda$playMp3$0$comhomefragmentdmx02DMX02MusicFragment(MediaPlayer mediaPlayer) {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.start();
        }
    }

    public void sendMusicMode() {
        if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-02-")) {
            int i = this.musicMode;
            if (i == 0) {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, 4);
            } else {
                this.mActivity.setVoiceCtlAndMusicMode(false, true, false, i - 1);
            }
        }
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
        if (!MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE)) {
            MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-");
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
        if (i == 1003 && i2 == -1 && intent != null) {
            this.colors = (ArrayList) intent.getSerializableExtra("color");
            Log.e(TAG, "colors ======== " + this.colors);
            if (this.colors == null) {
                return;
            }
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
            Runnable runnable = new Runnable() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.19
                @Override // java.lang.Runnable
                public void run() {
                    DMX02MusicFragment.this.canSendCmd = true;
                    handler.removeCallbacksAndMessages(null);
                }
            };
            if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01) || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
                handler.postDelayed(runnable, getSelectColor().size() * 300);
            } else {
                handler.postDelayed(runnable, getSelectColor().size() * 300);
            }
            if (MainActivity_DMX02.getSceneBean().equalsIgnoreCase(CommonConstant.LEDBLE_01) || MainActivity_DMX02.getSceneBean().equalsIgnoreCase("LEDDMX-01-")) {
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

    public void sendMu() {
        new Thread(new Runnable() { // from class: com.home.fragment.dmx02.DMX02MusicFragment.20
            @Override // java.lang.Runnable
            public void run() {
                do {
                    try {
                        if (!DMX02MusicFragment.this.isMusic) {
                            return;
                        }
                        if (DMX02MusicFragment.this.canSendCmd && DMX02MusicFragment.this.mediaPlayer != null && DMX02MusicFragment.this.mediaPlayer.isPlaying()) {
                            DMX02MusicFragment.this.mhanHandler.sendEmptyMessage(101);
                        }
                        Thread.sleep(300L);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } while (!DMX02MusicFragment.this.activity.isFinishing());
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
            DMX02MusicFragment dMX02MusicFragment;
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
                DMX02MusicFragment.this.chnnelValue = (int) (1.0f - fArr2[i5]);
                DMX02MusicFragment.this.chnnelValue = (int) Math.pow(dMX02MusicFragment.chnnelValue, 1.6d);
                if (DMX02MusicFragment.this.chnnelValue <= 1) {
                    DMX02MusicFragment.this.chnnelValue = 1;
                }
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
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
