package cn.jzvd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
public class JZVideoPlayerStandard extends JZVideoPlayer {
    protected static Timer DISMISS_CONTROL_VIEW_TIMER = null;
    public static int LAST_GET_BATTERYLEVEL_PERCENT = 70;
    public static long LAST_GET_BATTERYLEVEL_TIME;
    public ImageView backButton;
    private BroadcastReceiver battertReceiver;
    public ImageView batteryLevel;
    public LinearLayout batteryTimeLayout;
    public ProgressBar bottomProgressBar;
    public TextView clarity;
    public PopupWindow clarityPopWindow;
    public ProgressBar loadingProgressBar;
    protected Dialog mBrightnessDialog;
    protected ProgressBar mDialogBrightnessProgressBar;
    protected TextView mDialogBrightnessTextView;
    protected ImageView mDialogIcon;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogVolumeImageView;
    protected ProgressBar mDialogVolumeProgressBar;
    protected TextView mDialogVolumeTextView;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected Dialog mProgressDialog;
    public TextView mRetryBtn;
    public LinearLayout mRetryLayout;
    protected Dialog mVolumeDialog;
    public TextView replayTextView;
    public ImageView thumbImageView;
    public ImageView tinyBackImageView;
    public TextView titleTextView;
    public TextView videoCurrentTime;

    public JZVideoPlayerStandard(Context context) {
        super(context);
        this.battertReceiver = new BroadcastReceiver() { // from class: cn.jzvd.JZVideoPlayerStandard.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                    JZVideoPlayerStandard.LAST_GET_BATTERYLEVEL_PERCENT = (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
                    JZVideoPlayerStandard.this.setBatteryLevel();
                    JZVideoPlayerStandard.this.getContext().unregisterReceiver(JZVideoPlayerStandard.this.battertReceiver);
                }
            }
        };
    }

    public JZVideoPlayerStandard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.battertReceiver = new BroadcastReceiver() { // from class: cn.jzvd.JZVideoPlayerStandard.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                    JZVideoPlayerStandard.LAST_GET_BATTERYLEVEL_PERCENT = (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
                    JZVideoPlayerStandard.this.setBatteryLevel();
                    JZVideoPlayerStandard.this.getContext().unregisterReceiver(JZVideoPlayerStandard.this.battertReceiver);
                }
            }
        };
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void init(Context context) {
        super.init(context);
        this.batteryTimeLayout = (LinearLayout) findViewById(R.id.battery_time_layout);
        this.bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progress);
        this.titleTextView = (TextView) findViewById(R.id.title);
        this.backButton = (ImageView) findViewById(R.id.back);
        this.thumbImageView = (ImageView) findViewById(R.id.thumb);
        this.loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        this.tinyBackImageView = (ImageView) findViewById(R.id.back_tiny);
        this.batteryLevel = (ImageView) findViewById(R.id.battery_level);
        this.videoCurrentTime = (TextView) findViewById(R.id.video_current_time);
        this.replayTextView = (TextView) findViewById(R.id.replay_text);
        this.clarity = (TextView) findViewById(R.id.clarity);
        this.mRetryBtn = (TextView) findViewById(R.id.retry_btn);
        this.mRetryLayout = (LinearLayout) findViewById(R.id.retry_layout);
        this.thumbImageView.setOnClickListener(this);
        this.backButton.setOnClickListener(this);
        this.tinyBackImageView.setOnClickListener(this);
        this.clarity.setOnClickListener(this);
        this.mRetryBtn.setOnClickListener(this);
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void setUp(Object[] objArr, int i, int i2, Object... objArr2) {
        super.setUp(objArr, i, i2, objArr2);
        if (objArr2.length != 0) {
            this.titleTextView.setText(objArr2[0].toString());
        }
        if (this.currentScreen == 2) {
            this.fullscreenButton.setImageResource(R.drawable.jz_shrink);
            this.backButton.setVisibility(0);
            this.tinyBackImageView.setVisibility(4);
            this.batteryTimeLayout.setVisibility(0);
            if (((LinkedHashMap) objArr[0]).size() == 1) {
                this.clarity.setVisibility(8);
            } else {
                this.clarity.setText(JZUtils.getKeyFromDataSource(objArr, this.currentUrlMapIndex));
                this.clarity.setVisibility(0);
            }
            changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_fullscreen));
        } else if (this.currentScreen == 0 || this.currentScreen == 1) {
            this.fullscreenButton.setImageResource(R.drawable.jz_enlarge);
            this.backButton.setVisibility(8);
            this.tinyBackImageView.setVisibility(4);
            changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_normal));
            this.batteryTimeLayout.setVisibility(8);
            this.clarity.setVisibility(8);
        } else if (this.currentScreen == 3) {
            this.tinyBackImageView.setVisibility(0);
            setAllControlsVisiblity(4, 4, 4, 4, 4, 4, 4);
            this.batteryTimeLayout.setVisibility(8);
            this.clarity.setVisibility(8);
        }
        setSystemTimeAndBattery();
        if (this.tmp_test_back) {
            this.tmp_test_back = false;
            JZVideoPlayerManager.setFirstFloor(this);
            backPress();
        }
    }

    public void changeStartButtonSize(int i) {
        ViewGroup.LayoutParams layoutParams = this.startButton.getLayoutParams();
        layoutParams.height = i;
        layoutParams.width = i;
        ViewGroup.LayoutParams layoutParams2 = this.loadingProgressBar.getLayoutParams();
        layoutParams2.height = i;
        layoutParams2.width = i;
    }

    @Override // cn.jzvd.JZVideoPlayer
    public int getLayoutId() {
        return R.layout.jz_layout_standard;
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onStatePreparingChangingUrl(int i, long j) {
        super.onStatePreparingChangingUrl(i, j);
        this.loadingProgressBar.setVisibility(0);
        this.startButton.setVisibility(4);
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
        this.bottomProgressBar.setProgress(100);
    }

    @Override // cn.jzvd.JZVideoPlayer, android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        if (id == R.id.surface_container) {
            if (motionEvent.getAction() == 1) {
                startDismissControlViewTimer();
                if (this.mChangePosition) {
                    long duration = getDuration();
                    long j = this.mSeekTimePosition * 100;
                    if (duration == 0) {
                        duration = 1;
                    }
                    this.bottomProgressBar.setProgress((int) (j / duration));
                }
                if (!this.mChangePosition && !this.mChangeVolume) {
                    onEvent(102);
                    onClickUiToggle();
                }
            }
        } else if (id == R.id.bottom_seek_progress) {
            int action = motionEvent.getAction();
            if (action == 0) {
                cancelDismissControlViewTimer();
            } else if (action == 1) {
                startDismissControlViewTimer();
            }
        }
        return super.onTouch(view, motionEvent);
    }

    @Override // cn.jzvd.JZVideoPlayer, android.view.View.OnClickListener
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.thumb) {
            if (this.dataSourceObjects == null || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), 0).show();
            } else if (this.currentState == 0) {
                if (!JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("file") && !JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("/") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                onEvent(101);
                startVideo();
            } else if (this.currentState == 6) {
                onClickUiToggle();
            }
        } else if (id == R.id.surface_container) {
            startDismissControlViewTimer();
        } else if (id == R.id.back) {
            backPress();
        } else if (id == R.id.back_tiny) {
            if (JZVideoPlayerManager.getFirstFloor().currentScreen == 1) {
                quitFullscreenOrTinyWindow();
            } else {
                backPress();
            }
        } else if (id == R.id.clarity) {
            final LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.jz_layout_clarity, (ViewGroup) null);
            View.OnClickListener onClickListener = new View.OnClickListener() { // from class: cn.jzvd.JZVideoPlayerStandard.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    int intValue = ((Integer) view2.getTag()).intValue();
                    JZVideoPlayerStandard jZVideoPlayerStandard = JZVideoPlayerStandard.this;
                    jZVideoPlayerStandard.onStatePreparingChangingUrl(intValue, jZVideoPlayerStandard.getCurrentPositionWhenPlaying());
                    JZVideoPlayerStandard.this.clarity.setText(JZUtils.getKeyFromDataSource(JZVideoPlayerStandard.this.dataSourceObjects, JZVideoPlayerStandard.this.currentUrlMapIndex));
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        if (i == JZVideoPlayerStandard.this.currentUrlMapIndex) {
                            ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#fff85959"));
                        } else {
                            ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    if (JZVideoPlayerStandard.this.clarityPopWindow != null) {
                        JZVideoPlayerStandard.this.clarityPopWindow.dismiss();
                    }
                }
            };
            for (int i = 0; i < ((LinkedHashMap) this.dataSourceObjects[0]).size(); i++) {
                String keyFromDataSource = JZUtils.getKeyFromDataSource(this.dataSourceObjects, i);
                TextView textView = (TextView) View.inflate(getContext(), R.layout.jz_layout_clarity_item, null);
                textView.setText(keyFromDataSource);
                textView.setTag(Integer.valueOf(i));
                linearLayout.addView(textView, i);
                textView.setOnClickListener(onClickListener);
                if (i == this.currentUrlMapIndex) {
                    textView.setTextColor(Color.parseColor("#fff85959"));
                }
            }
            PopupWindow popupWindow = new PopupWindow((View) linearLayout, -2, -2, true);
            this.clarityPopWindow = popupWindow;
            popupWindow.setContentView(linearLayout);
            this.clarityPopWindow.showAsDropDown(this.clarity);
            linearLayout.measure(0, 0);
            this.clarityPopWindow.update(this.clarity, -(this.clarity.getMeasuredWidth() / 3), -(this.clarity.getMeasuredHeight() / 3), Math.round(linearLayout.getMeasuredWidth() * 2), linearLayout.getMeasuredHeight());
        } else if (id == R.id.retry_btn) {
            if (this.dataSourceObjects == null || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), 0).show();
            } else if (!JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("file") && !JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("/") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                showWifiDialog();
            } else {
                initTextureView();
                addTextureView();
                JZMediaManager.setDataSource(this.dataSourceObjects);
                JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
                onStatePreparing();
                onEvent(1);
            }
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() { // from class: cn.jzvd.JZVideoPlayerStandard.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                JZVideoPlayerStandard.this.onEvent(103);
                JZVideoPlayerStandard.this.startVideo();
                JZVideoPlayer.WIFI_TIP_DIALOG_SHOWED = true;
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() { // from class: cn.jzvd.JZVideoPlayerStandard.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                JZVideoPlayerStandard.this.clearFloatScreen();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: cn.jzvd.JZVideoPlayerStandard.5
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override // cn.jzvd.JZVideoPlayer, android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override // cn.jzvd.JZVideoPlayer, android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        if (this.currentState == 3) {
            dissmissControlView();
        } else {
            startDismissControlViewTimer();
        }
    }

    public void onClickUiToggle() {
        if (this.bottomContainer.getVisibility() != 0) {
            setSystemTimeAndBattery();
            this.clarity.setText(JZUtils.getKeyFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        }
        if (this.currentState == 1) {
            changeUiToPreparing();
            if (this.bottomContainer.getVisibility() == 0) {
                return;
            }
            setSystemTimeAndBattery();
        } else if (this.currentState == 3) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (this.currentState == 5) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    public void setSystemTimeAndBattery() {
        this.videoCurrentTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        if (System.currentTimeMillis() - LAST_GET_BATTERYLEVEL_TIME > 30000) {
            LAST_GET_BATTERYLEVEL_TIME = System.currentTimeMillis();
            getContext().registerReceiver(this.battertReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            return;
        }
        setBatteryLevel();
    }

    public void setBatteryLevel() {
        int i = LAST_GET_BATTERYLEVEL_PERCENT;
        if (i < 15) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_10);
        } else if (i >= 15 && i < 40) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_30);
        } else if (i >= 40 && i < 60) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_50);
        } else if (i >= 60 && i < 80) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_70);
        } else if (i >= 80 && i < 95) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_90);
        } else if (i < 95 || i > 100) {
        } else {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_100);
        }
    }

    public void onCLickUiToggleToClear() {
        if (this.currentState == 1) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPreparing();
            }
        } else if (this.currentState == 3) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPlayingClear();
            }
        } else if (this.currentState == 5) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            }
        } else if (this.currentState == 6 && this.bottomContainer.getVisibility() == 0) {
            changeUiToComplete();
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void setProgressAndText(int i, long j, long j2) {
        super.setProgressAndText(i, j, j2);
        if (i != 0) {
            this.bottomProgressBar.setProgress(i);
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void setBufferProgress(int i) {
        super.setBufferProgress(i);
        if (i != 0) {
            this.bottomProgressBar.setSecondaryProgress(i);
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        this.bottomProgressBar.setProgress(0);
        this.bottomProgressBar.setSecondaryProgress(0);
    }

    public void changeUiToNormal() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
            updateStartImage();
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToPreparing() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 4, 4, 0, 0, 4, 4);
            updateStartImage();
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(4, 4, 4, 0, 0, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToPlayingShow() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
            updateStartImage();
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToPlayingClear() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
        }
    }

    public void changeUiToPauseShow() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
            updateStartImage();
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToPauseClear() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
        }
    }

    public void changeUiToComplete() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
            updateStartImage();
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToError() {
        int i = this.currentScreen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 4, 0, 4, 4, 4, 0);
            updateStartImage();
        } else if (i != 2) {
        } else {
            setAllControlsVisiblity(0, 4, 0, 4, 4, 4, 0);
            updateStartImage();
        }
    }

    public void setAllControlsVisiblity(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.topContainer.setVisibility(i);
        this.bottomContainer.setVisibility(i2);
        this.startButton.setVisibility(i3);
        this.loadingProgressBar.setVisibility(i4);
        this.thumbImageView.setVisibility(i5);
        this.bottomProgressBar.setVisibility(i6);
        this.mRetryLayout.setVisibility(i7);
    }

    public void updateStartImage() {
        if (this.currentState == 3) {
            this.startButton.setVisibility(0);
            this.startButton.setImageResource(R.drawable.jz_click_pause_selector);
            this.replayTextView.setVisibility(4);
        } else if (this.currentState == 7) {
            this.startButton.setVisibility(4);
            this.replayTextView.setVisibility(4);
        } else if (this.currentState == 6) {
            this.startButton.setVisibility(0);
            this.startButton.setImageResource(R.drawable.jz_click_replay_selector);
            this.replayTextView.setVisibility(0);
        } else {
            this.startButton.setImageResource(R.drawable.jz_click_play_selector);
            this.replayTextView.setVisibility(4);
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void showProgressDialog(float f, String str, long j, String str2, long j2) {
        super.showProgressDialog(f, str, j, str2, j2);
        if (this.mProgressDialog == null) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.jz_dialog_progress, (ViewGroup) null);
            this.mDialogProgressBar = (ProgressBar) inflate.findViewById(R.id.duration_progressbar);
            this.mDialogSeekTime = (TextView) inflate.findViewById(R.id.tv_current);
            this.mDialogTotalTime = (TextView) inflate.findViewById(R.id.tv_duration);
            this.mDialogIcon = (ImageView) inflate.findViewById(R.id.duration_image_tip);
            this.mProgressDialog = createDialogWithView(inflate);
        }
        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }
        this.mDialogSeekTime.setText(str);
        TextView textView = this.mDialogTotalTime;
        textView.setText(" / " + str2);
        this.mDialogProgressBar.setProgress(j2 <= 0 ? 0 : (int) ((j * 100) / j2));
        if (f > 0.0f) {
            this.mDialogIcon.setBackgroundResource(R.drawable.jz_forward_icon);
        } else {
            this.mDialogIcon.setBackgroundResource(R.drawable.jz_backward_icon);
        }
        onCLickUiToggleToClear();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        Dialog dialog = this.mProgressDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void showVolumeDialog(float f, int i) {
        super.showVolumeDialog(f, i);
        if (this.mVolumeDialog == null) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.jz_dialog_volume, (ViewGroup) null);
            this.mDialogVolumeImageView = (ImageView) inflate.findViewById(R.id.volume_image_tip);
            this.mDialogVolumeTextView = (TextView) inflate.findViewById(R.id.tv_volume);
            this.mDialogVolumeProgressBar = (ProgressBar) inflate.findViewById(R.id.volume_progressbar);
            this.mVolumeDialog = createDialogWithView(inflate);
        }
        if (!this.mVolumeDialog.isShowing()) {
            this.mVolumeDialog.show();
        }
        if (i <= 0) {
            this.mDialogVolumeImageView.setBackgroundResource(R.drawable.jz_close_volume);
        } else {
            this.mDialogVolumeImageView.setBackgroundResource(R.drawable.jz_add_volume);
        }
        if (i > 100) {
            i = 100;
        } else if (i < 0) {
            i = 0;
        }
        TextView textView = this.mDialogVolumeTextView;
        textView.setText(i + "%");
        this.mDialogVolumeProgressBar.setProgress(i);
        onCLickUiToggleToClear();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        Dialog dialog = this.mVolumeDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void showBrightnessDialog(int i) {
        super.showBrightnessDialog(i);
        if (this.mBrightnessDialog == null) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.jz_dialog_brightness, (ViewGroup) null);
            this.mDialogBrightnessTextView = (TextView) inflate.findViewById(R.id.tv_brightness);
            this.mDialogBrightnessProgressBar = (ProgressBar) inflate.findViewById(R.id.brightness_progressbar);
            this.mBrightnessDialog = createDialogWithView(inflate);
        }
        if (!this.mBrightnessDialog.isShowing()) {
            this.mBrightnessDialog.show();
        }
        if (i > 100) {
            i = 100;
        } else if (i < 0) {
            i = 0;
        }
        TextView textView = this.mDialogBrightnessTextView;
        textView.setText(i + "%");
        this.mDialogBrightnessProgressBar.setProgress(i);
        onCLickUiToggleToClear();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        Dialog dialog = this.mBrightnessDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public Dialog createDialogWithView(View view) {
        Dialog dialog = new Dialog(getContext(), R.style.jz_style_dialog_progress);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 17;
        window.setAttributes(attributes);
        return dialog;
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        DismissControlViewTimerTask dismissControlViewTimerTask = new DismissControlViewTimerTask();
        this.mDismissControlViewTimerTask = dismissControlViewTimerTask;
        DISMISS_CONTROL_VIEW_TIMER.schedule(dismissControlViewTimerTask, 2500L);
    }

    public void cancelDismissControlViewTimer() {
        Timer timer = DISMISS_CONTROL_VIEW_TIMER;
        if (timer != null) {
            timer.cancel();
        }
        DismissControlViewTimerTask dismissControlViewTimerTask = this.mDismissControlViewTimerTask;
        if (dismissControlViewTimerTask != null) {
            dismissControlViewTimerTask.cancel();
        }
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onAutoCompletion() {
        super.onAutoCompletion();
        cancelDismissControlViewTimer();
    }

    @Override // cn.jzvd.JZVideoPlayer
    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
        PopupWindow popupWindow = this.clarityPopWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    public void dissmissControlView() {
        if (this.currentState == 0 || this.currentState == 7 || this.currentState == 6) {
            return;
        }
        post(new Runnable() { // from class: cn.jzvd.JZVideoPlayerStandard.6
            @Override // java.lang.Runnable
            public void run() {
                JZVideoPlayerStandard.this.bottomContainer.setVisibility(4);
                JZVideoPlayerStandard.this.topContainer.setVisibility(4);
                JZVideoPlayerStandard.this.startButton.setVisibility(4);
                if (JZVideoPlayerStandard.this.clarityPopWindow != null) {
                    JZVideoPlayerStandard.this.clarityPopWindow.dismiss();
                }
                if (JZVideoPlayerStandard.this.currentScreen != 3) {
                    JZVideoPlayerStandard.this.bottomProgressBar.setVisibility(0);
                }
            }
        });
    }

    /* loaded from: classes.dex */
    public class DismissControlViewTimerTask extends TimerTask {
        public DismissControlViewTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            JZVideoPlayerStandard.this.dissmissControlView();
        }
    }
}
