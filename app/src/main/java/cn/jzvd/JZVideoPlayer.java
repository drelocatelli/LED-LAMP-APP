package cn.jzvd;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.luck.picture.lib.config.PictureMimeType;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
public abstract class JZVideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    public static boolean ACTION_BAR_EXIST = true;
    public static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6;
    public static final int CURRENT_STATE_ERROR = 7;
    public static final int CURRENT_STATE_NORMAL = 0;
    public static final int CURRENT_STATE_PAUSE = 5;
    public static final int CURRENT_STATE_PLAYING = 3;
    public static final int CURRENT_STATE_PREPARING = 1;
    public static final int CURRENT_STATE_PREPARING_CHANGING_URL = 2;
    public static int FULLSCREEN_ORIENTATION = 4;
    public static final int FULL_SCREEN_NORMAL_DELAY = 300;
    protected static JZUserAction JZ_USER_EVENT = null;
    public static int NORMAL_ORIENTATION = 1;
    public static boolean SAVE_PROGRESS = true;
    public static final int SCREEN_WINDOW_FULLSCREEN = 2;
    public static final int SCREEN_WINDOW_LIST = 1;
    public static final int SCREEN_WINDOW_NORMAL = 0;
    public static final int SCREEN_WINDOW_TINY = 3;
    public static final String TAG = "JiaoZiVideoPlayer";
    public static final int THRESHOLD = 80;
    public static boolean TOOL_BAR_EXIST = true;
    protected static Timer UPDATE_PROGRESS_TIMER = null;
    public static final String URL_KEY_DEFAULT = "URL_KEY_DEFAULT";
    public static int VIDEO_IMAGE_DISPLAY_TYPE = 0;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER = 0;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT = 1;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP = 2;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL = 3;
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    public static long lastAutoFullscreenTime;
    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() { // from class: cn.jzvd.JZVideoPlayer.1
        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(int i) {
            if (i != -2) {
                if (i != -1) {
                    return;
                }
                JZVideoPlayer.releaseAllVideos();
                Log.d("JiaoZiVideoPlayer", "AUDIOFOCUS_LOSS [" + hashCode() + "]");
                return;
            }
            try {
                if (JZVideoPlayerManager.getCurrentJzvd().currentState == 3) {
                    JZVideoPlayerManager.getCurrentJzvd().startButton.performClick();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            Log.d("JiaoZiVideoPlayer", "AUDIOFOCUS_LOSS_TRANSIENT [" + hashCode() + "]");
        }
    };
    public ViewGroup bottomContainer;
    public int currentScreen;
    public int currentState;
    public TextView currentTimeTextView;
    public int currentUrlMapIndex;
    public Object[] dataSourceObjects;
    public ImageView fullscreenButton;
    public int heightRatio;
    protected AudioManager mAudioManager;
    protected boolean mChangeBrightness;
    protected boolean mChangePosition;
    protected boolean mChangeVolume;
    protected float mDownX;
    protected float mDownY;
    protected float mGestureDownBrightness;
    protected long mGestureDownPosition;
    protected int mGestureDownVolume;
    protected ProgressTimerTask mProgressTimerTask;
    protected int mScreenHeight;
    protected int mScreenWidth;
    protected long mSeekTimePosition;
    protected boolean mTouchingProgressBar;
    public Object[] objects;
    public int positionInList;
    public SeekBar progressBar;
    public long seekToInAdvance;
    public ImageView startButton;
    public ViewGroup textureViewContainer;
    boolean tmp_test_back;
    public ViewGroup topContainer;
    public TextView totalTimeTextView;
    public int videoRotation;
    public int widthRatio;

    public void dismissBrightnessDialog() {
    }

    public void dismissProgressDialog() {
    }

    public void dismissVolumeDialog() {
    }

    public abstract int getLayoutId();

    public void onSeekComplete() {
    }

    public void showBrightnessDialog(int i) {
    }

    public void showProgressDialog(float f, String str, long j, String str2, long j2) {
    }

    public void showVolumeDialog(float f, int i) {
    }

    public void showWifiDialog() {
    }

    public JZVideoPlayer(Context context) {
        super(context);
        this.currentState = -1;
        this.currentScreen = -1;
        this.objects = null;
        this.seekToInAdvance = 0L;
        this.widthRatio = 0;
        this.heightRatio = 0;
        this.currentUrlMapIndex = 0;
        this.positionInList = -1;
        this.videoRotation = 0;
        this.tmp_test_back = false;
        init(context);
    }

    public JZVideoPlayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentState = -1;
        this.currentScreen = -1;
        this.objects = null;
        this.seekToInAdvance = 0L;
        this.widthRatio = 0;
        this.heightRatio = 0;
        this.currentUrlMapIndex = 0;
        this.positionInList = -1;
        this.videoRotation = 0;
        this.tmp_test_back = false;
        init(context);
    }

    public static void releaseAllVideos() {
        if (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME > 300) {
            Log.d("JiaoZiVideoPlayer", "releaseAllVideos");
            JZVideoPlayerManager.completeAll();
            JZMediaManager.instance().positionInList = -1;
            JZMediaManager.instance().releaseMediaPlayer();
        }
    }

    public static void startFullscreen(Context context, Class cls, String str, Object... objArr) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(URL_KEY_DEFAULT, str);
        startFullscreen(context, cls, new Object[]{linkedHashMap}, 0, objArr);
    }

    public static void startFullscreen(Context context, Class cls, Object[] objArr, int i, Object... objArr2) {
        hideSupportActionBar(context);
        JZUtils.setRequestedOrientation(context, FULLSCREEN_ORIENTATION);
        ViewGroup viewGroup = (ViewGroup) JZUtils.scanForActivity(context).findViewById(16908290);
        View findViewById = viewGroup.findViewById(R.id.jz_fullscreen_id);
        if (findViewById != null) {
            viewGroup.removeView(findViewById);
        }
        try {
            JZVideoPlayer jZVideoPlayer = (JZVideoPlayer) cls.getConstructor(Context.class).newInstance(context);
            jZVideoPlayer.setId(R.id.jz_fullscreen_id);
            viewGroup.addView(jZVideoPlayer, new FrameLayout.LayoutParams(-1, -1));
            jZVideoPlayer.setUp(objArr, i, 2, objArr2);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            jZVideoPlayer.startButton.performClick();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean backPress() {
        Log.i("JiaoZiVideoPlayer", "backPress");
        if (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME < 300) {
            return false;
        }
        if (JZVideoPlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            if (JZUtils.dataSourceObjectsContainsUri(JZVideoPlayerManager.getFirstFloor().dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                JZVideoPlayer secondFloor = JZVideoPlayerManager.getSecondFloor();
                secondFloor.onEvent(secondFloor.currentScreen == 2 ? 8 : 10);
                JZVideoPlayerManager.getFirstFloor().playOnThisJzvd();
            } else {
                quitFullscreenOrTinyWindow();
            }
            return true;
        } else if (JZVideoPlayerManager.getFirstFloor() == null || !(JZVideoPlayerManager.getFirstFloor().currentScreen == 2 || JZVideoPlayerManager.getFirstFloor().currentScreen == 3)) {
            return false;
        } else {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            quitFullscreenOrTinyWindow();
            return true;
        }
    }

    public static void quitFullscreenOrTinyWindow() {
        JZVideoPlayerManager.getFirstFloor().clearFloatScreen();
        JZMediaManager.instance().releaseMediaPlayer();
        JZVideoPlayerManager.completeAll();
    }

    public static void showSupportActionBar(Context context) {
        ActionBar supportActionBar;
        if (ACTION_BAR_EXIST && JZUtils.getAppCompActivity(context) != null && (supportActionBar = JZUtils.getAppCompActivity(context).getSupportActionBar()) != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
            supportActionBar.show();
        }
        if (TOOL_BAR_EXIST) {
            JZUtils.getWindow(context).clearFlags(1024);
        }
    }

    public static void hideSupportActionBar(Context context) {
        ActionBar supportActionBar;
        if (ACTION_BAR_EXIST && JZUtils.getAppCompActivity(context) != null && (supportActionBar = JZUtils.getAppCompActivity(context).getSupportActionBar()) != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
            supportActionBar.hide();
        }
        if (TOOL_BAR_EXIST) {
            JZUtils.getWindow(context).setFlags(1024, 1024);
        }
    }

    public static void clearSavedProgress(Context context, String str) {
        JZUtils.clearSavedProgress(context, str);
    }

    public static void setJzUserAction(JZUserAction jZUserAction) {
        JZ_USER_EVENT = jZUserAction;
    }

    public static void goOnPlayOnResume() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer currentJzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (currentJzvd.currentState == 5) {
                currentJzvd.onStatePlaying();
                JZMediaManager.start();
            }
        }
    }

    public static void goOnPlayOnPause() {
        JZVideoPlayer currentJzvd;
        int i;
        if (JZVideoPlayerManager.getCurrentJzvd() == null || (i = (currentJzvd = JZVideoPlayerManager.getCurrentJzvd()).currentState) == 6 || i == 0 || i == 7) {
            return;
        }
        currentJzvd.onStatePause();
        JZMediaManager.pause();
    }

    public static void onScrollAutoTiny(AbsListView absListView, int i, int i2, int i3) {
        int i4 = i2 + i;
        int i5 = JZMediaManager.instance().positionInList;
        if (i5 >= 0) {
            if (i5 < i || i5 > i4 - 1) {
                if (JZVideoPlayerManager.getCurrentJzvd() == null || JZVideoPlayerManager.getCurrentJzvd().currentScreen == 3 || JZVideoPlayerManager.getCurrentJzvd().currentScreen == 2) {
                    return;
                }
                if (JZVideoPlayerManager.getCurrentJzvd().currentState == 5) {
                    releaseAllVideos();
                    return;
                }
                Log.e("JiaoZiVideoPlayer", "onScroll: out screen");
                JZVideoPlayerManager.getCurrentJzvd().startWindowTiny();
            } else if (JZVideoPlayerManager.getCurrentJzvd() == null || JZVideoPlayerManager.getCurrentJzvd().currentScreen != 3) {
            } else {
                Log.e("JiaoZiVideoPlayer", "onScroll: into screen");
                backPress();
            }
        }
    }

    public static void onScrollReleaseAllVideos(AbsListView absListView, int i, int i2, int i3) {
        int i4 = i2 + i;
        int i5 = JZMediaManager.instance().positionInList;
        Log.e("JiaoZiVideoPlayer", "onScrollReleaseAllVideos: " + i5 + " " + i + " " + i5 + " " + i4);
        if (i5 >= 0) {
            if ((i5 < i || i5 > i4 - 1) && JZVideoPlayerManager.getCurrentJzvd().currentScreen != 2) {
                releaseAllVideos();
            }
        }
    }

    public static void onChildViewAttachedToWindow(View view, int i) {
        JZVideoPlayer jZVideoPlayer;
        if (JZVideoPlayerManager.getCurrentJzvd() == null || JZVideoPlayerManager.getCurrentJzvd().currentScreen != 3 || (jZVideoPlayer = (JZVideoPlayer) view.findViewById(i)) == null || !JZUtils.getCurrentFromDataSource(jZVideoPlayer.dataSourceObjects, jZVideoPlayer.currentUrlMapIndex).equals(JZMediaManager.getCurrentDataSource())) {
            return;
        }
        backPress();
    }

    public static void onChildViewDetachedFromWindow(View view) {
        if (JZVideoPlayerManager.getCurrentJzvd() == null || JZVideoPlayerManager.getCurrentJzvd().currentScreen == 3) {
            return;
        }
        JZVideoPlayer currentJzvd = JZVideoPlayerManager.getCurrentJzvd();
        if (((ViewGroup) view).indexOfChild(currentJzvd) != -1) {
            if (currentJzvd.currentState == 5) {
                releaseAllVideos();
            } else {
                currentJzvd.startWindowTiny();
            }
        }
    }

    public static void setTextureViewRotation(int i) {
        if (JZMediaManager.textureView != null) {
            JZMediaManager.textureView.setRotation(i);
        }
    }

    public static void setVideoImageDisplayType(int i) {
        VIDEO_IMAGE_DISPLAY_TYPE = i;
        if (JZMediaManager.textureView != null) {
            JZMediaManager.textureView.requestLayout();
        }
    }

    public Object getCurrentUrl() {
        return JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex);
    }

    public void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        this.startButton = (ImageView) findViewById(R.id.start);
        this.fullscreenButton = (ImageView) findViewById(R.id.fullscreen);
        this.progressBar = (SeekBar) findViewById(R.id.bottom_seek_progress);
        this.currentTimeTextView = (TextView) findViewById(R.id.current);
        this.totalTimeTextView = (TextView) findViewById(R.id.total);
        this.bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        this.textureViewContainer = (ViewGroup) findViewById(R.id.surface_container);
        this.topContainer = (ViewGroup) findViewById(R.id.layout_top);
        this.startButton.setOnClickListener(this);
        this.fullscreenButton.setOnClickListener(this);
        this.progressBar.setOnSeekBarChangeListener(this);
        this.bottomContainer.setOnClickListener(this);
        this.textureViewContainer.setOnClickListener(this);
        this.textureViewContainer.setOnTouchListener(this);
        this.mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        this.mAudioManager = (AudioManager) getContext().getSystemService(PictureMimeType.MIME_TYPE_PREFIX_AUDIO);
        try {
            if (isCurrentPlay()) {
                NORMAL_ORIENTATION = ((AppCompatActivity) context).getRequestedOrientation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUp(String str, int i, Object... objArr) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(URL_KEY_DEFAULT, str);
        setUp(new Object[]{linkedHashMap}, 0, i, objArr);
    }

    public void setUp(Object[] objArr, int i, int i2, Object... objArr2) {
        long j;
        if (this.dataSourceObjects == null || JZUtils.getCurrentFromDataSource(objArr, this.currentUrlMapIndex) == null || !JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).equals(JZUtils.getCurrentFromDataSource(objArr, this.currentUrlMapIndex))) {
            if (isCurrentJZVD() && JZUtils.dataSourceObjectsContainsUri(objArr, JZMediaManager.getCurrentDataSource())) {
                try {
                    j = JZMediaManager.getCurrentPosition();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    j = 0;
                }
                if (j != 0) {
                    JZUtils.saveProgress(getContext(), JZMediaManager.getCurrentDataSource(), j);
                }
                JZMediaManager.instance().releaseMediaPlayer();
            } else if (isCurrentJZVD() && !JZUtils.dataSourceObjectsContainsUri(objArr, JZMediaManager.getCurrentDataSource())) {
                startWindowTiny();
            } else if (!isCurrentJZVD() && JZUtils.dataSourceObjectsContainsUri(objArr, JZMediaManager.getCurrentDataSource())) {
                if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentScreen == 3) {
                    this.tmp_test_back = true;
                }
            } else if (!isCurrentJZVD()) {
                JZUtils.dataSourceObjectsContainsUri(objArr, JZMediaManager.getCurrentDataSource());
            }
            this.dataSourceObjects = objArr;
            this.currentUrlMapIndex = i;
            this.currentScreen = i2;
            this.objects = objArr2;
            onStateNormal();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start) {
            Log.i("JiaoZiVideoPlayer", "onClick start [" + hashCode() + "] ");
            Object[] objArr = this.dataSourceObjects;
            if (objArr == null || JZUtils.getCurrentFromDataSource(objArr, this.currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), 0).show();
                return;
            }
            int i = this.currentState;
            if (i == 0) {
                if (!JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("file") && !JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("/") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startVideo();
                onEvent(0);
            } else if (i == 3) {
                onEvent(3);
                Log.d("JiaoZiVideoPlayer", "pauseVideo [" + hashCode() + "] ");
                JZMediaManager.pause();
                onStatePause();
            } else if (i == 5) {
                onEvent(4);
                JZMediaManager.start();
                onStatePlaying();
            } else if (i == 6) {
                onEvent(2);
                startVideo();
            }
        } else if (id == R.id.fullscreen) {
            Log.i("JiaoZiVideoPlayer", "onClick fullscreen [" + hashCode() + "] ");
            if (this.currentState == 6) {
                return;
            }
            if (this.currentScreen == 2) {
                backPress();
                return;
            }
            Log.d("JiaoZiVideoPlayer", "toFullscreenActivity [" + hashCode() + "] ");
            onEvent(7);
            startWindowFullscreen();
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int streamMaxVolume;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (view.getId() == R.id.surface_container) {
            int action = motionEvent.getAction();
            if (action == 0) {
                Log.i("JiaoZiVideoPlayer", "onTouch surfaceContainer actionDown [" + hashCode() + "] ");
                this.mTouchingProgressBar = true;
                this.mDownX = x;
                this.mDownY = y;
                this.mChangeVolume = false;
                this.mChangePosition = false;
                this.mChangeBrightness = false;
            } else if (action == 1) {
                Log.i("JiaoZiVideoPlayer", "onTouch surfaceContainer actionUp [" + hashCode() + "] ");
                this.mTouchingProgressBar = false;
                dismissProgressDialog();
                dismissVolumeDialog();
                dismissBrightnessDialog();
                if (this.mChangePosition) {
                    onEvent(12);
                    JZMediaManager.seekTo(this.mSeekTimePosition);
                    long duration = getDuration();
                    long j = this.mSeekTimePosition * 100;
                    if (duration == 0) {
                        duration = 1;
                    }
                    this.progressBar.setProgress((int) (j / duration));
                }
                if (this.mChangeVolume) {
                    onEvent(11);
                }
                startProgressTimer();
            } else if (action == 2) {
                Log.i("JiaoZiVideoPlayer", "onTouch surfaceContainer actionMove [" + hashCode() + "] ");
                float f = x - this.mDownX;
                float f2 = y - this.mDownY;
                float abs = Math.abs(f);
                float abs2 = Math.abs(f2);
                if (this.currentScreen == 2 && !this.mChangePosition && !this.mChangeVolume && !this.mChangeBrightness && (abs > 80.0f || abs2 > 80.0f)) {
                    cancelProgressTimer();
                    if (abs >= 80.0f) {
                        if (this.currentState != 7) {
                            this.mChangePosition = true;
                            this.mGestureDownPosition = getCurrentPositionWhenPlaying();
                        }
                    } else if (this.mDownX < this.mScreenWidth * 0.5f) {
                        this.mChangeBrightness = true;
                        WindowManager.LayoutParams attributes = JZUtils.getWindow(getContext()).getAttributes();
                        if (attributes.screenBrightness < 0.0f) {
                            try {
                                this.mGestureDownBrightness = Settings.System.getInt(getContext().getContentResolver(), "screen_brightness");
                                Log.i("JiaoZiVideoPlayer", "current system brightness: " + this.mGestureDownBrightness);
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            this.mGestureDownBrightness = attributes.screenBrightness * 255.0f;
                            Log.i("JiaoZiVideoPlayer", "current activity brightness: " + this.mGestureDownBrightness);
                        }
                    } else {
                        this.mChangeVolume = true;
                        this.mGestureDownVolume = this.mAudioManager.getStreamVolume(3);
                    }
                }
                if (this.mChangePosition) {
                    long duration2 = getDuration();
                    long j2 = (int) (((float) this.mGestureDownPosition) + ((((float) duration2) * f) / this.mScreenWidth));
                    this.mSeekTimePosition = j2;
                    if (j2 > duration2) {
                        this.mSeekTimePosition = duration2;
                    }
                    showProgressDialog(f, JZUtils.stringForTime(this.mSeekTimePosition), this.mSeekTimePosition, JZUtils.stringForTime(duration2), duration2);
                }
                if (this.mChangeVolume) {
                    f2 = -f2;
                    this.mAudioManager.setStreamVolume(3, this.mGestureDownVolume + ((int) (((this.mAudioManager.getStreamMaxVolume(3) * f2) * 3.0f) / this.mScreenHeight)), 0);
                    showVolumeDialog(-f2, (int) (((this.mGestureDownVolume * 100) / streamMaxVolume) + (((f2 * 3.0f) * 100.0f) / this.mScreenHeight)));
                }
                if (this.mChangeBrightness) {
                    float f3 = -f2;
                    WindowManager.LayoutParams attributes2 = JZUtils.getWindow(getContext()).getAttributes();
                    float f4 = this.mGestureDownBrightness;
                    float f5 = (int) (((f3 * 255.0f) * 3.0f) / this.mScreenHeight);
                    if ((f4 + f5) / 255.0f >= 1.0f) {
                        attributes2.screenBrightness = 1.0f;
                    } else if ((f4 + f5) / 255.0f <= 0.0f) {
                        attributes2.screenBrightness = 0.01f;
                    } else {
                        attributes2.screenBrightness = (f4 + f5) / 255.0f;
                    }
                    JZUtils.getWindow(getContext()).setAttributes(attributes2);
                    showBrightnessDialog((int) (((this.mGestureDownBrightness * 100.0f) / 255.0f) + (((f3 * 3.0f) * 100.0f) / this.mScreenHeight)));
                }
            }
        }
        return false;
    }

    public void startVideo() {
        JZVideoPlayerManager.completeAll();
        Log.d("JiaoZiVideoPlayer", "startVideo [" + hashCode() + "] ");
        initTextureView();
        addTextureView();
        ((AudioManager) getContext().getSystemService(PictureMimeType.MIME_TYPE_PREFIX_AUDIO)).requestAudioFocus(onAudioFocusChangeListener, 3, 2);
        JZUtils.scanForActivity(getContext()).getWindow().addFlags(128);
        JZMediaManager.setDataSource(this.dataSourceObjects);
        JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        JZMediaManager.instance().positionInList = this.positionInList;
        onStatePreparing();
        JZVideoPlayerManager.setFirstFloor(this);
    }

    public void onPrepared() {
        Log.i("JiaoZiVideoPlayer", "onPrepared  [" + hashCode() + "] ");
        onStatePrepared();
        onStatePlaying();
    }

    public void setState(int i) {
        setState(i, 0, 0);
    }

    public void setState(int i, int i2, int i3) {
        if (i == 0) {
            onStateNormal();
        } else if (i == 1) {
            onStatePreparing();
        } else if (i == 2) {
            onStatePreparingChangingUrl(i2, i3);
        } else if (i == 3) {
            onStatePlaying();
        } else if (i == 5) {
            onStatePause();
        } else if (i == 6) {
            onStateAutoComplete();
        } else if (i != 7) {
        } else {
            onStateError();
        }
    }

    public void onStateNormal() {
        Log.i("JiaoZiVideoPlayer", "onStateNormal  [" + hashCode() + "] ");
        this.currentState = 0;
        cancelProgressTimer();
    }

    public void onStatePreparing() {
        Log.i("JiaoZiVideoPlayer", "onStatePreparing  [" + hashCode() + "] ");
        this.currentState = 1;
        resetProgressAndTime();
    }

    public void onStatePreparingChangingUrl(int i, long j) {
        this.currentState = 2;
        this.currentUrlMapIndex = i;
        this.seekToInAdvance = j;
        JZMediaManager.setDataSource(this.dataSourceObjects);
        JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        JZMediaManager.instance().prepare();
    }

    public void onStatePrepared() {
        long j = this.seekToInAdvance;
        if (j != 0) {
            JZMediaManager.seekTo(j);
            this.seekToInAdvance = 0L;
            return;
        }
        long savedProgress = JZUtils.getSavedProgress(getContext(), JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        if (savedProgress != 0) {
            JZMediaManager.seekTo(savedProgress);
        }
    }

    public void onStatePlaying() {
        Log.i("JiaoZiVideoPlayer", "onStatePlaying  [" + hashCode() + "] ");
        this.currentState = 3;
        startProgressTimer();
    }

    public void onStatePause() {
        Log.i("JiaoZiVideoPlayer", "onStatePause  [" + hashCode() + "] ");
        this.currentState = 5;
        startProgressTimer();
    }

    public void onStateError() {
        Log.i("JiaoZiVideoPlayer", "onStateError  [" + hashCode() + "] ");
        this.currentState = 7;
        cancelProgressTimer();
    }

    public void onStateAutoComplete() {
        Log.i("JiaoZiVideoPlayer", "onStateAutoComplete  [" + hashCode() + "] ");
        this.currentState = 6;
        cancelProgressTimer();
        this.progressBar.setProgress(100);
        this.currentTimeTextView.setText(this.totalTimeTextView.getText());
    }

    public void onInfo(int i, int i2) {
        Log.d("JiaoZiVideoPlayer", "onInfo what - " + i + " extra - " + i2);
    }

    public void onError(int i, int i2) {
        Log.e("JiaoZiVideoPlayer", "onError " + i + " - " + i2 + " [" + hashCode() + "] ");
        if (i == 38 || i2 == -38 || i == -38 || i2 == 38 || i2 == -19) {
            return;
        }
        onStateError();
        if (isCurrentPlay()) {
            JZMediaManager.instance().releaseMediaPlayer();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3 = this.currentScreen;
        if (i3 == 2 || i3 == 3) {
            super.onMeasure(i, i2);
        } else if (this.widthRatio != 0 && this.heightRatio != 0) {
            int size = View.MeasureSpec.getSize(i);
            int i4 = (int) ((size * this.heightRatio) / this.widthRatio);
            setMeasuredDimension(size, i4);
            getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
        } else {
            super.onMeasure(i, i2);
        }
    }

    public void onAutoCompletion() {
        Runtime.getRuntime().gc();
        Log.i("JiaoZiVideoPlayer", "onAutoCompletion  [" + hashCode() + "] ");
        onEvent(6);
        dismissVolumeDialog();
        dismissProgressDialog();
        dismissBrightnessDialog();
        onStateAutoComplete();
        int i = this.currentScreen;
        if (i == 2 || i == 3) {
            backPress();
        }
        JZMediaManager.instance().releaseMediaPlayer();
        JZUtils.saveProgress(getContext(), JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex), 0L);
    }

    public void onCompletion() {
        Log.i("JiaoZiVideoPlayer", "onCompletion  [" + hashCode() + "] ");
        int i = this.currentState;
        if (i == 3 || i == 5) {
            JZUtils.saveProgress(getContext(), JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex), getCurrentPositionWhenPlaying());
        }
        cancelProgressTimer();
        dismissBrightnessDialog();
        dismissProgressDialog();
        dismissVolumeDialog();
        onStateNormal();
        this.textureViewContainer.removeView(JZMediaManager.textureView);
        JZMediaManager.instance().currentVideoWidth = 0;
        JZMediaManager.instance().currentVideoHeight = 0;
        ((AudioManager) getContext().getSystemService(PictureMimeType.MIME_TYPE_PREFIX_AUDIO)).abandonAudioFocus(onAudioFocusChangeListener);
        JZUtils.scanForActivity(getContext()).getWindow().clearFlags(128);
        clearFullscreenLayout();
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        if (JZMediaManager.surface != null) {
            JZMediaManager.surface.release();
        }
        if (JZMediaManager.savedSurfaceTexture != null) {
            JZMediaManager.savedSurfaceTexture.release();
        }
        JZMediaManager.textureView = null;
        JZMediaManager.savedSurfaceTexture = null;
    }

    public void release() {
        if (!JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).equals(JZMediaManager.getCurrentDataSource()) || System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME <= 300) {
            return;
        }
        if (JZVideoPlayerManager.getSecondFloor() == null || JZVideoPlayerManager.getSecondFloor().currentScreen != 2) {
            if (JZVideoPlayerManager.getSecondFloor() == null && JZVideoPlayerManager.getFirstFloor() != null && JZVideoPlayerManager.getFirstFloor().currentScreen == 2) {
                return;
            }
            Log.d("JiaoZiVideoPlayer", "releaseMediaPlayer [" + hashCode() + "]");
            releaseAllVideos();
        }
    }

    public void initTextureView() {
        removeTextureView();
        JZMediaManager.textureView = new JZResizeTextureView(getContext());
        JZMediaManager.textureView.setSurfaceTextureListener(JZMediaManager.instance());
    }

    public void addTextureView() {
        Log.d("JiaoZiVideoPlayer", "addTextureView [" + hashCode() + "] ");
        this.textureViewContainer.addView(JZMediaManager.textureView, new FrameLayout.LayoutParams(-1, -1, 17));
    }

    public void removeTextureView() {
        JZMediaManager.savedSurfaceTexture = null;
        if (JZMediaManager.textureView == null || JZMediaManager.textureView.getParent() == null) {
            return;
        }
        ((ViewGroup) JZMediaManager.textureView.getParent()).removeView(JZMediaManager.textureView);
    }

    public void clearFullscreenLayout() {
        ViewGroup viewGroup = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
        View findViewById = viewGroup.findViewById(R.id.jz_fullscreen_id);
        View findViewById2 = viewGroup.findViewById(R.id.jz_tiny_id);
        if (findViewById != null) {
            viewGroup.removeView(findViewById);
        }
        if (findViewById2 != null) {
            viewGroup.removeView(findViewById2);
        }
        showSupportActionBar(getContext());
    }

    public void clearFloatScreen() {
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        showSupportActionBar(getContext());
        ViewGroup viewGroup = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
        JZVideoPlayer jZVideoPlayer = (JZVideoPlayer) viewGroup.findViewById(R.id.jz_fullscreen_id);
        JZVideoPlayer jZVideoPlayer2 = (JZVideoPlayer) viewGroup.findViewById(R.id.jz_tiny_id);
        if (jZVideoPlayer != null) {
            viewGroup.removeView(jZVideoPlayer);
            ViewGroup viewGroup2 = jZVideoPlayer.textureViewContainer;
            if (viewGroup2 != null) {
                viewGroup2.removeView(JZMediaManager.textureView);
            }
        }
        if (jZVideoPlayer2 != null) {
            viewGroup.removeView(jZVideoPlayer2);
            ViewGroup viewGroup3 = jZVideoPlayer2.textureViewContainer;
            if (viewGroup3 != null) {
                viewGroup3.removeView(JZMediaManager.textureView);
            }
        }
        JZVideoPlayerManager.setSecondFloor(null);
    }

    public void onVideoSizeChanged() {
        Log.i("JiaoZiVideoPlayer", "onVideoSizeChanged  [" + hashCode() + "] ");
        if (JZMediaManager.textureView != null) {
            if (this.videoRotation != 0) {
                JZMediaManager.textureView.setRotation(this.videoRotation);
            }
            JZMediaManager.textureView.setVideoSize(JZMediaManager.instance().currentVideoWidth, JZMediaManager.instance().currentVideoHeight);
        }
    }

    public void startProgressTimer() {
        Log.i("JiaoZiVideoPlayer", "startProgressTimer:  [" + hashCode() + "] ");
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        ProgressTimerTask progressTimerTask = new ProgressTimerTask();
        this.mProgressTimerTask = progressTimerTask;
        UPDATE_PROGRESS_TIMER.schedule(progressTimerTask, 0L, 300L);
    }

    public void cancelProgressTimer() {
        Timer timer = UPDATE_PROGRESS_TIMER;
        if (timer != null) {
            timer.cancel();
        }
        ProgressTimerTask progressTimerTask = this.mProgressTimerTask;
        if (progressTimerTask != null) {
            progressTimerTask.cancel();
        }
    }

    public void setProgressAndText(int i, long j, long j2) {
        if (!this.mTouchingProgressBar && i != 0) {
            this.progressBar.setProgress(i);
        }
        if (j != 0) {
            this.currentTimeTextView.setText(JZUtils.stringForTime(j));
        }
        this.totalTimeTextView.setText(JZUtils.stringForTime(j2));
    }

    public void setBufferProgress(int i) {
        if (i != 0) {
            this.progressBar.setSecondaryProgress(i);
        }
    }

    public void resetProgressAndTime() {
        this.progressBar.setProgress(0);
        this.progressBar.setSecondaryProgress(0);
        this.currentTimeTextView.setText(JZUtils.stringForTime(0L));
        this.totalTimeTextView.setText(JZUtils.stringForTime(0L));
    }

    public long getCurrentPositionWhenPlaying() {
        int i = this.currentState;
        if (i == 3 || i == 5) {
            try {
                return JZMediaManager.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return 0L;
            }
        }
        return 0L;
    }

    public long getDuration() {
        try {
            return JZMediaManager.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i("JiaoZiVideoPlayer", "bottomProgress onStartTrackingTouch [" + hashCode() + "] ");
        cancelProgressTimer();
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i("JiaoZiVideoPlayer", "bottomProgress onStopTrackingTouch [" + hashCode() + "] ");
        onEvent(5);
        startProgressTimer();
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
        int i = this.currentState;
        if (i == 3 || i == 5) {
            long progress = (seekBar.getProgress() * getDuration()) / 100;
            JZMediaManager.seekTo(progress);
            Log.i("JiaoZiVideoPlayer", "seekTo " + progress + " [" + hashCode() + "] ");
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            this.currentTimeTextView.setText(JZUtils.stringForTime((i * getDuration()) / 100));
        }
    }

    public void startWindowFullscreen() {
        Log.i("JiaoZiVideoPlayer", "startWindowFullscreen  [" + hashCode() + "] ");
        hideSupportActionBar(getContext());
        ViewGroup viewGroup = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
        View findViewById = viewGroup.findViewById(R.id.jz_fullscreen_id);
        if (findViewById != null) {
            viewGroup.removeView(findViewById);
        }
        this.textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            JZVideoPlayer jZVideoPlayer = (JZVideoPlayer) getClass().getConstructor(Context.class).newInstance(getContext());
            jZVideoPlayer.setId(R.id.jz_fullscreen_id);
            viewGroup.addView(jZVideoPlayer, new FrameLayout.LayoutParams(-1, -1));
            jZVideoPlayer.setSystemUiVisibility(4102);
            jZVideoPlayer.setUp(this.dataSourceObjects, this.currentUrlMapIndex, 2, this.objects);
            jZVideoPlayer.setState(this.currentState);
            jZVideoPlayer.addTextureView();
            JZVideoPlayerManager.setSecondFloor(jZVideoPlayer);
            JZUtils.setRequestedOrientation(getContext(), FULLSCREEN_ORIENTATION);
            onStateNormal();
            jZVideoPlayer.progressBar.setSecondaryProgress(this.progressBar.getSecondaryProgress());
            jZVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startWindowTiny() {
        Log.i("JiaoZiVideoPlayer", "startWindowTiny  [" + hashCode() + "] ");
        onEvent(9);
        int i = this.currentState;
        if (i == 0 || i == 7 || i == 6) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
        View findViewById = viewGroup.findViewById(R.id.jz_tiny_id);
        if (findViewById != null) {
            viewGroup.removeView(findViewById);
        }
        this.textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            JZVideoPlayer jZVideoPlayer = (JZVideoPlayer) getClass().getConstructor(Context.class).newInstance(getContext());
            jZVideoPlayer.setId(R.id.jz_tiny_id);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) HttpStatus.SC_BAD_REQUEST, (int) HttpStatus.SC_BAD_REQUEST);
            layoutParams.gravity = 85;
            viewGroup.addView(jZVideoPlayer, layoutParams);
            jZVideoPlayer.setUp(this.dataSourceObjects, this.currentUrlMapIndex, 3, this.objects);
            jZVideoPlayer.setState(this.currentState);
            jZVideoPlayer.addTextureView();
            JZVideoPlayerManager.setSecondFloor(jZVideoPlayer);
            onStateNormal();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public boolean isCurrentPlay() {
        return isCurrentJZVD() && JZUtils.dataSourceObjectsContainsUri(this.dataSourceObjects, JZMediaManager.getCurrentDataSource());
    }

    public boolean isCurrentJZVD() {
        return JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd() == this;
    }

    public void playOnThisJzvd() {
        Log.i("JiaoZiVideoPlayer", "playOnThisJzvd  [" + hashCode() + "] ");
        this.currentState = JZVideoPlayerManager.getSecondFloor().currentState;
        this.currentUrlMapIndex = JZVideoPlayerManager.getSecondFloor().currentUrlMapIndex;
        clearFloatScreen();
        setState(this.currentState);
        addTextureView();
    }

    public void autoFullscreen(float f) {
        int i;
        if (!isCurrentPlay() || this.currentState != 3 || (i = this.currentScreen) == 2 || i == 3) {
            return;
        }
        if (f > 0.0f) {
            JZUtils.setRequestedOrientation(getContext(), 0);
        } else {
            JZUtils.setRequestedOrientation(getContext(), 8);
        }
        onEvent(7);
        startWindowFullscreen();
    }

    public void autoQuitFullscreen() {
        if (System.currentTimeMillis() - lastAutoFullscreenTime > 2000 && isCurrentPlay() && this.currentState == 3 && this.currentScreen == 2) {
            lastAutoFullscreenTime = System.currentTimeMillis();
            backPress();
        }
    }

    public void onEvent(int i) {
        Object[] objArr;
        if (JZ_USER_EVENT == null || !isCurrentPlay() || (objArr = this.dataSourceObjects) == null) {
            return;
        }
        JZ_USER_EVENT.onEvent(i, JZUtils.getCurrentFromDataSource(objArr, this.currentUrlMapIndex), this.currentScreen, this.objects);
    }

    public static void setMediaInterface(JZMediaInterface jZMediaInterface) {
        JZMediaManager.instance().jzMediaInterface = jZMediaInterface;
    }

    /* loaded from: classes.dex */
    public static class JZAutoFullscreenListener implements SensorEventListener {
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            if ((f < -12.0f || f > 12.0f) && System.currentTimeMillis() - JZVideoPlayer.lastAutoFullscreenTime > 2000) {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().autoFullscreen(f);
                }
                JZVideoPlayer.lastAutoFullscreenTime = System.currentTimeMillis();
            }
        }
    }

    /* loaded from: classes.dex */
    public class ProgressTimerTask extends TimerTask {
        public ProgressTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (JZVideoPlayer.this.currentState == 3 || JZVideoPlayer.this.currentState == 5) {
                JZVideoPlayer.this.post(new Runnable() { // from class: cn.jzvd.JZVideoPlayer.ProgressTimerTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        long currentPositionWhenPlaying = JZVideoPlayer.this.getCurrentPositionWhenPlaying();
                        long duration = JZVideoPlayer.this.getDuration();
                        JZVideoPlayer.this.setProgressAndText((int) ((100 * currentPositionWhenPlaying) / (duration == 0 ? 1L : duration)), currentPositionWhenPlaying, duration);
                    }
                });
            }
        }
    }
}
