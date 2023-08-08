package com.common.view.videoplayer.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ledlamp.R;
import java.util.Formatter;
import java.util.Locale;

/* loaded from: classes.dex */
public class SimplerPlayerControllerLayout extends FrameLayout {
    private static final int LAYOUT_FULL = 1;
    private static final int LAYOUT_SMALL = 0;
    private static final int sDefaultTimeout = 4000;
    private boolean enableFullScreen;
    private boolean isMultiPointEvent;
    private boolean isScrollX;
    private boolean isScrollY;
    private View.OnClickListener itemClickListener;
    private View mBottomControlLay;
    private ImageView mCenterStateIv;
    private View mCenterStateLay;
    private TextView mCenterTimeTv;
    private int mCurrentLayout;
    private boolean mDragging;
    private TextView mEndTimeTv;
    private Runnable mFadeOut;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private ImageView mFullScreenIv;
    private GestureDetector mGestureDetector;
    private boolean mIsShowing;
    private ProgressBar mLoadingPb;
    private long mNewSeekPro;
    private MediaController.MediaPlayerControl mPlayer;
    private SeekBar mProgressSeekBar;
    private Runnable mShowProgress;
    private ImageView mStartPauseIv;
    private TextView mStartTimeTv;
    private int mTouchSeekPro;
    private IMediaController mediaController;
    private boolean scrubbing;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener;

    private void gestureBrightness(float f) {
    }

    private void gestureVolume(float f) {
    }

    public SimplerPlayerControllerLayout(Context context) {
        super(context);
        this.mCurrentLayout = 0;
        this.enableFullScreen = true;
        this.itemClickListener = new View.OnClickListener() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SimplerPlayerControllerLayout.this.checkPlayerIsNull()) {
                    return;
                }
                int id = view.getId();
                if (id != R.id.center_state_iv) {
                    if (id == R.id.full_screen_iv) {
                        if (SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                            SimplerPlayerControllerLayout.this.mediaController.hide();
                        }
                        Context context2 = SimplerPlayerControllerLayout.this.getContext();
                        if (context2 instanceof Activity) {
                            Activity activity = (Activity) context2;
                            if (SimplerPlayerControllerLayout.this.mCurrentLayout != 0) {
                                if (SimplerPlayerControllerLayout.this.mCurrentLayout == 1) {
                                    activity.setRequestedOrientation(1);
                                    SimplerPlayerControllerLayout.this.mCurrentLayout = 0;
                                    return;
                                }
                                return;
                            }
                            activity.setRequestedOrientation(0);
                            SimplerPlayerControllerLayout.this.mCurrentLayout = 1;
                            return;
                        }
                        return;
                    } else if (id != R.id.start_pause_iv) {
                        return;
                    }
                }
                if (SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                    SimplerPlayerControllerLayout.this.mPlayer.pause();
                } else {
                    SimplerPlayerControllerLayout.this.mPlayer.start();
                }
                SimplerPlayerControllerLayout.this.mediaController.show(SimplerPlayerControllerLayout.sDefaultTimeout);
            }
        };
        this.seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (!SimplerPlayerControllerLayout.this.checkPlayerIsNull() && z) {
                    long duration = (SimplerPlayerControllerLayout.this.mPlayer.getDuration() * i) / 1000;
                    if (SimplerPlayerControllerLayout.this.mStartTimeTv != null) {
                        SimplerPlayerControllerLayout.this.mStartTimeTv.setText(SimplerPlayerControllerLayout.this.stringForTime((int) duration));
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                SimplerPlayerControllerLayout.this.mediaController.show(3600000);
                SimplerPlayerControllerLayout.this.mDragging = true;
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mShowProgress);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SimplerPlayerControllerLayout.this.checkPlayerIsNull()) {
                    return;
                }
                int duration = (int) ((SimplerPlayerControllerLayout.this.mPlayer.getDuration() * seekBar.getProgress()) / 1000);
                SimplerPlayerControllerLayout.this.mPlayer.seekTo(duration);
                if (SimplerPlayerControllerLayout.this.mStartTimeTv != null) {
                    SimplerPlayerControllerLayout.this.mStartTimeTv.setText(SimplerPlayerControllerLayout.this.stringForTime(duration));
                }
                SimplerPlayerControllerLayout.this.mDragging = false;
                SimplerPlayerControllerLayout.this.updatePausePlay();
                SimplerPlayerControllerLayout.this.mediaController.show(SimplerPlayerControllerLayout.sDefaultTimeout);
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.post(simplerPlayerControllerLayout.mShowProgress);
            }
        };
        this.mediaController = new IMediaController() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.3
            @Override // com.common.view.videoplayer.view.IMediaController
            public void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl) {
                SimplerPlayerControllerLayout.this.mPlayer = mediaPlayerControl;
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void setEnabled(boolean z) {
                if (SimplerPlayerControllerLayout.this.mStartPauseIv != null) {
                    SimplerPlayerControllerLayout.this.mStartPauseIv.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateIv != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mProgressSeekBar != null) {
                    SimplerPlayerControllerLayout.this.mProgressSeekBar.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mFullScreenIv != null) {
                    SimplerPlayerControllerLayout.this.mFullScreenIv.setEnabled(z);
                }
                SimplerPlayerControllerLayout.super.setEnabled(z);
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onPreparing() {
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(0);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onPrepared() {
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(8);
                }
                show();
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onComplete() {
                SimplerPlayerControllerLayout.this.updateProgress();
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(0);
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setImageResource(R.drawable.refresh_icon);
                }
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onError() {
                Toast.makeText(SimplerPlayerControllerLayout.this.getContext(), "播放出错!", 1).show();
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(0);
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setImageResource(R.drawable.refresh_icon);
                }
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void show(int i) {
                if (!SimplerPlayerControllerLayout.this.mIsShowing) {
                    if (SimplerPlayerControllerLayout.this.mBottomControlLay != null) {
                        SimplerPlayerControllerLayout.this.mBottomControlLay.setVisibility(0);
                    }
                    SimplerPlayerControllerLayout.this.mIsShowing = true;
                }
                SimplerPlayerControllerLayout.this.updatePausePlay();
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mShowProgress);
                SimplerPlayerControllerLayout simplerPlayerControllerLayout2 = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout2.post(simplerPlayerControllerLayout2.mShowProgress);
                if (i != 0) {
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout3 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout3.removeCallbacks(simplerPlayerControllerLayout3.mFadeOut);
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout4 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout4.postDelayed(simplerPlayerControllerLayout4.mFadeOut, i);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void show() {
                show(SimplerPlayerControllerLayout.sDefaultTimeout);
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void hide() {
                if (SimplerPlayerControllerLayout.this.mIsShowing) {
                    SimplerPlayerControllerLayout.this.mIsShowing = false;
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mFadeOut);
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout2 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout2.removeCallbacks(simplerPlayerControllerLayout2.mShowProgress);
                    if (SimplerPlayerControllerLayout.this.mBottomControlLay != null) {
                        SimplerPlayerControllerLayout.this.mBottomControlLay.setVisibility(8);
                    }
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public boolean isShowing() {
                return SimplerPlayerControllerLayout.this.mIsShowing;
            }
        };
        this.mFadeOut = new Runnable() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.4
            @Override // java.lang.Runnable
            public void run() {
                SimplerPlayerControllerLayout.this.mediaController.hide();
            }
        };
        this.mShowProgress = new Runnable() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.5
            @Override // java.lang.Runnable
            public void run() {
                int updateProgress = SimplerPlayerControllerLayout.this.updateProgress();
                if (SimplerPlayerControllerLayout.this.mPlayer == null || SimplerPlayerControllerLayout.this.mDragging || !SimplerPlayerControllerLayout.this.mIsShowing || !SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                    return;
                }
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.postDelayed(simplerPlayerControllerLayout.mShowProgress, 1000 - (updateProgress % 1000));
            }
        };
        init(context);
    }

    public SimplerPlayerControllerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentLayout = 0;
        this.enableFullScreen = true;
        this.itemClickListener = new View.OnClickListener() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SimplerPlayerControllerLayout.this.checkPlayerIsNull()) {
                    return;
                }
                int id = view.getId();
                if (id != R.id.center_state_iv) {
                    if (id == R.id.full_screen_iv) {
                        if (SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                            SimplerPlayerControllerLayout.this.mediaController.hide();
                        }
                        Context context2 = SimplerPlayerControllerLayout.this.getContext();
                        if (context2 instanceof Activity) {
                            Activity activity = (Activity) context2;
                            if (SimplerPlayerControllerLayout.this.mCurrentLayout != 0) {
                                if (SimplerPlayerControllerLayout.this.mCurrentLayout == 1) {
                                    activity.setRequestedOrientation(1);
                                    SimplerPlayerControllerLayout.this.mCurrentLayout = 0;
                                    return;
                                }
                                return;
                            }
                            activity.setRequestedOrientation(0);
                            SimplerPlayerControllerLayout.this.mCurrentLayout = 1;
                            return;
                        }
                        return;
                    } else if (id != R.id.start_pause_iv) {
                        return;
                    }
                }
                if (SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                    SimplerPlayerControllerLayout.this.mPlayer.pause();
                } else {
                    SimplerPlayerControllerLayout.this.mPlayer.start();
                }
                SimplerPlayerControllerLayout.this.mediaController.show(SimplerPlayerControllerLayout.sDefaultTimeout);
            }
        };
        this.seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (!SimplerPlayerControllerLayout.this.checkPlayerIsNull() && z) {
                    long duration = (SimplerPlayerControllerLayout.this.mPlayer.getDuration() * i) / 1000;
                    if (SimplerPlayerControllerLayout.this.mStartTimeTv != null) {
                        SimplerPlayerControllerLayout.this.mStartTimeTv.setText(SimplerPlayerControllerLayout.this.stringForTime((int) duration));
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                SimplerPlayerControllerLayout.this.mediaController.show(3600000);
                SimplerPlayerControllerLayout.this.mDragging = true;
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mShowProgress);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SimplerPlayerControllerLayout.this.checkPlayerIsNull()) {
                    return;
                }
                int duration = (int) ((SimplerPlayerControllerLayout.this.mPlayer.getDuration() * seekBar.getProgress()) / 1000);
                SimplerPlayerControllerLayout.this.mPlayer.seekTo(duration);
                if (SimplerPlayerControllerLayout.this.mStartTimeTv != null) {
                    SimplerPlayerControllerLayout.this.mStartTimeTv.setText(SimplerPlayerControllerLayout.this.stringForTime(duration));
                }
                SimplerPlayerControllerLayout.this.mDragging = false;
                SimplerPlayerControllerLayout.this.updatePausePlay();
                SimplerPlayerControllerLayout.this.mediaController.show(SimplerPlayerControllerLayout.sDefaultTimeout);
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.post(simplerPlayerControllerLayout.mShowProgress);
            }
        };
        this.mediaController = new IMediaController() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.3
            @Override // com.common.view.videoplayer.view.IMediaController
            public void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl) {
                SimplerPlayerControllerLayout.this.mPlayer = mediaPlayerControl;
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void setEnabled(boolean z) {
                if (SimplerPlayerControllerLayout.this.mStartPauseIv != null) {
                    SimplerPlayerControllerLayout.this.mStartPauseIv.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateIv != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mProgressSeekBar != null) {
                    SimplerPlayerControllerLayout.this.mProgressSeekBar.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mFullScreenIv != null) {
                    SimplerPlayerControllerLayout.this.mFullScreenIv.setEnabled(z);
                }
                SimplerPlayerControllerLayout.super.setEnabled(z);
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onPreparing() {
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(0);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onPrepared() {
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(8);
                }
                show();
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onComplete() {
                SimplerPlayerControllerLayout.this.updateProgress();
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(0);
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setImageResource(R.drawable.refresh_icon);
                }
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onError() {
                Toast.makeText(SimplerPlayerControllerLayout.this.getContext(), "播放出错!", 1).show();
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(0);
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setImageResource(R.drawable.refresh_icon);
                }
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void show(int i) {
                if (!SimplerPlayerControllerLayout.this.mIsShowing) {
                    if (SimplerPlayerControllerLayout.this.mBottomControlLay != null) {
                        SimplerPlayerControllerLayout.this.mBottomControlLay.setVisibility(0);
                    }
                    SimplerPlayerControllerLayout.this.mIsShowing = true;
                }
                SimplerPlayerControllerLayout.this.updatePausePlay();
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mShowProgress);
                SimplerPlayerControllerLayout simplerPlayerControllerLayout2 = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout2.post(simplerPlayerControllerLayout2.mShowProgress);
                if (i != 0) {
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout3 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout3.removeCallbacks(simplerPlayerControllerLayout3.mFadeOut);
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout4 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout4.postDelayed(simplerPlayerControllerLayout4.mFadeOut, i);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void show() {
                show(SimplerPlayerControllerLayout.sDefaultTimeout);
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void hide() {
                if (SimplerPlayerControllerLayout.this.mIsShowing) {
                    SimplerPlayerControllerLayout.this.mIsShowing = false;
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mFadeOut);
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout2 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout2.removeCallbacks(simplerPlayerControllerLayout2.mShowProgress);
                    if (SimplerPlayerControllerLayout.this.mBottomControlLay != null) {
                        SimplerPlayerControllerLayout.this.mBottomControlLay.setVisibility(8);
                    }
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public boolean isShowing() {
                return SimplerPlayerControllerLayout.this.mIsShowing;
            }
        };
        this.mFadeOut = new Runnable() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.4
            @Override // java.lang.Runnable
            public void run() {
                SimplerPlayerControllerLayout.this.mediaController.hide();
            }
        };
        this.mShowProgress = new Runnable() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.5
            @Override // java.lang.Runnable
            public void run() {
                int updateProgress = SimplerPlayerControllerLayout.this.updateProgress();
                if (SimplerPlayerControllerLayout.this.mPlayer == null || SimplerPlayerControllerLayout.this.mDragging || !SimplerPlayerControllerLayout.this.mIsShowing || !SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                    return;
                }
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.postDelayed(simplerPlayerControllerLayout.mShowProgress, 1000 - (updateProgress % 1000));
            }
        };
        init(context);
    }

    public SimplerPlayerControllerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentLayout = 0;
        this.enableFullScreen = true;
        this.itemClickListener = new View.OnClickListener() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SimplerPlayerControllerLayout.this.checkPlayerIsNull()) {
                    return;
                }
                int id = view.getId();
                if (id != R.id.center_state_iv) {
                    if (id == R.id.full_screen_iv) {
                        if (SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                            SimplerPlayerControllerLayout.this.mediaController.hide();
                        }
                        Context context2 = SimplerPlayerControllerLayout.this.getContext();
                        if (context2 instanceof Activity) {
                            Activity activity = (Activity) context2;
                            if (SimplerPlayerControllerLayout.this.mCurrentLayout != 0) {
                                if (SimplerPlayerControllerLayout.this.mCurrentLayout == 1) {
                                    activity.setRequestedOrientation(1);
                                    SimplerPlayerControllerLayout.this.mCurrentLayout = 0;
                                    return;
                                }
                                return;
                            }
                            activity.setRequestedOrientation(0);
                            SimplerPlayerControllerLayout.this.mCurrentLayout = 1;
                            return;
                        }
                        return;
                    } else if (id != R.id.start_pause_iv) {
                        return;
                    }
                }
                if (SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                    SimplerPlayerControllerLayout.this.mPlayer.pause();
                } else {
                    SimplerPlayerControllerLayout.this.mPlayer.start();
                }
                SimplerPlayerControllerLayout.this.mediaController.show(SimplerPlayerControllerLayout.sDefaultTimeout);
            }
        };
        this.seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (!SimplerPlayerControllerLayout.this.checkPlayerIsNull() && z) {
                    long duration = (SimplerPlayerControllerLayout.this.mPlayer.getDuration() * i2) / 1000;
                    if (SimplerPlayerControllerLayout.this.mStartTimeTv != null) {
                        SimplerPlayerControllerLayout.this.mStartTimeTv.setText(SimplerPlayerControllerLayout.this.stringForTime((int) duration));
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                SimplerPlayerControllerLayout.this.mediaController.show(3600000);
                SimplerPlayerControllerLayout.this.mDragging = true;
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mShowProgress);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SimplerPlayerControllerLayout.this.checkPlayerIsNull()) {
                    return;
                }
                int duration = (int) ((SimplerPlayerControllerLayout.this.mPlayer.getDuration() * seekBar.getProgress()) / 1000);
                SimplerPlayerControllerLayout.this.mPlayer.seekTo(duration);
                if (SimplerPlayerControllerLayout.this.mStartTimeTv != null) {
                    SimplerPlayerControllerLayout.this.mStartTimeTv.setText(SimplerPlayerControllerLayout.this.stringForTime(duration));
                }
                SimplerPlayerControllerLayout.this.mDragging = false;
                SimplerPlayerControllerLayout.this.updatePausePlay();
                SimplerPlayerControllerLayout.this.mediaController.show(SimplerPlayerControllerLayout.sDefaultTimeout);
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.post(simplerPlayerControllerLayout.mShowProgress);
            }
        };
        this.mediaController = new IMediaController() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.3
            @Override // com.common.view.videoplayer.view.IMediaController
            public void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl) {
                SimplerPlayerControllerLayout.this.mPlayer = mediaPlayerControl;
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void setEnabled(boolean z) {
                if (SimplerPlayerControllerLayout.this.mStartPauseIv != null) {
                    SimplerPlayerControllerLayout.this.mStartPauseIv.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateIv != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mProgressSeekBar != null) {
                    SimplerPlayerControllerLayout.this.mProgressSeekBar.setEnabled(z);
                }
                if (SimplerPlayerControllerLayout.this.mFullScreenIv != null) {
                    SimplerPlayerControllerLayout.this.mFullScreenIv.setEnabled(z);
                }
                SimplerPlayerControllerLayout.super.setEnabled(z);
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onPreparing() {
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(0);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onPrepared() {
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(8);
                }
                show();
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onComplete() {
                SimplerPlayerControllerLayout.this.updateProgress();
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(0);
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setImageResource(R.drawable.refresh_icon);
                }
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void onError() {
                Toast.makeText(SimplerPlayerControllerLayout.this.getContext(), "播放出错!", 1).show();
                if (SimplerPlayerControllerLayout.this.mCenterStateLay != null) {
                    SimplerPlayerControllerLayout.this.mCenterStateLay.setVisibility(0);
                    SimplerPlayerControllerLayout.this.mCenterStateIv.setImageResource(R.drawable.refresh_icon);
                }
                if (SimplerPlayerControllerLayout.this.mLoadingPb != null) {
                    SimplerPlayerControllerLayout.this.mLoadingPb.setVisibility(8);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void show(int i2) {
                if (!SimplerPlayerControllerLayout.this.mIsShowing) {
                    if (SimplerPlayerControllerLayout.this.mBottomControlLay != null) {
                        SimplerPlayerControllerLayout.this.mBottomControlLay.setVisibility(0);
                    }
                    SimplerPlayerControllerLayout.this.mIsShowing = true;
                }
                SimplerPlayerControllerLayout.this.updatePausePlay();
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mShowProgress);
                SimplerPlayerControllerLayout simplerPlayerControllerLayout2 = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout2.post(simplerPlayerControllerLayout2.mShowProgress);
                if (i2 != 0) {
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout3 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout3.removeCallbacks(simplerPlayerControllerLayout3.mFadeOut);
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout4 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout4.postDelayed(simplerPlayerControllerLayout4.mFadeOut, i2);
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void show() {
                show(SimplerPlayerControllerLayout.sDefaultTimeout);
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public void hide() {
                if (SimplerPlayerControllerLayout.this.mIsShowing) {
                    SimplerPlayerControllerLayout.this.mIsShowing = false;
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout.removeCallbacks(simplerPlayerControllerLayout.mFadeOut);
                    SimplerPlayerControllerLayout simplerPlayerControllerLayout2 = SimplerPlayerControllerLayout.this;
                    simplerPlayerControllerLayout2.removeCallbacks(simplerPlayerControllerLayout2.mShowProgress);
                    if (SimplerPlayerControllerLayout.this.mBottomControlLay != null) {
                        SimplerPlayerControllerLayout.this.mBottomControlLay.setVisibility(8);
                    }
                }
            }

            @Override // com.common.view.videoplayer.view.IMediaController
            public boolean isShowing() {
                return SimplerPlayerControllerLayout.this.mIsShowing;
            }
        };
        this.mFadeOut = new Runnable() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.4
            @Override // java.lang.Runnable
            public void run() {
                SimplerPlayerControllerLayout.this.mediaController.hide();
            }
        };
        this.mShowProgress = new Runnable() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.5
            @Override // java.lang.Runnable
            public void run() {
                int updateProgress = SimplerPlayerControllerLayout.this.updateProgress();
                if (SimplerPlayerControllerLayout.this.mPlayer == null || SimplerPlayerControllerLayout.this.mDragging || !SimplerPlayerControllerLayout.this.mIsShowing || !SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                    return;
                }
                SimplerPlayerControllerLayout simplerPlayerControllerLayout = SimplerPlayerControllerLayout.this;
                simplerPlayerControllerLayout.postDelayed(simplerPlayerControllerLayout.mShowProgress, 1000 - (updateProgress % 1000));
            }
        };
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.player_controller_layout, this);
        this.mCenterStateLay = findViewById(R.id.center_state_fl);
        this.mBottomControlLay = findViewById(R.id.bottom_control_ll);
        this.mLoadingPb = (ProgressBar) findViewById(R.id.loading_pb);
        ImageView imageView = (ImageView) findViewById(R.id.center_state_iv);
        this.mCenterStateIv = imageView;
        imageView.setOnClickListener(this.itemClickListener);
        ImageView imageView2 = (ImageView) findViewById(R.id.start_pause_iv);
        this.mStartPauseIv = imageView2;
        imageView2.setOnClickListener(this.itemClickListener);
        ImageView imageView3 = (ImageView) findViewById(R.id.full_screen_iv);
        this.mFullScreenIv = imageView3;
        imageView3.setOnClickListener(this.itemClickListener);
        this.mFullScreenIv.setVisibility(this.enableFullScreen ? 0 : 8);
        SeekBar seekBar = (SeekBar) findViewById(R.id.progress_seek_bar);
        this.mProgressSeekBar = seekBar;
        seekBar.setOnSeekBarChangeListener(this.seekBarChangeListener);
        this.mStartTimeTv = (TextView) findViewById(R.id.start_time_tv);
        this.mEndTimeTv = (TextView) findViewById(R.id.end_time_tv);
        this.mCenterTimeTv = (TextView) findViewById(R.id.center_time_tv);
        initFormatter();
        initGesture(context);
    }

    public IMediaController getMediaController() {
        return this.mediaController;
    }

    private void initFormatter() {
        if (this.mFormatter == null) {
            this.mFormatBuilder = new StringBuilder();
            this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        }
    }

    private void initGesture(Context context) {
        this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: com.common.view.videoplayer.view.SimplerPlayerControllerLayout.6
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (SimplerPlayerControllerLayout.this.isMultiPointEvent) {
                    return true;
                }
                float abs = Math.abs(f);
                float abs2 = Math.abs(f2);
                if (SimplerPlayerControllerLayout.this.isScrollX) {
                    SimplerPlayerControllerLayout.this.gestureSeekTime(-f, false);
                }
                if (!SimplerPlayerControllerLayout.this.isScrollX && !SimplerPlayerControllerLayout.this.isScrollY) {
                    if (abs > abs2) {
                        SimplerPlayerControllerLayout.this.isScrollX = true;
                        SimplerPlayerControllerLayout.this.isScrollY = false;
                    } else {
                        SimplerPlayerControllerLayout.this.isScrollX = false;
                        SimplerPlayerControllerLayout.this.isScrollY = true;
                    }
                }
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                SimplerPlayerControllerLayout.this.isScrollX = false;
                SimplerPlayerControllerLayout.this.isScrollY = false;
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                if (SimplerPlayerControllerLayout.this.checkPlayerIsNull()) {
                    return true;
                }
                if (SimplerPlayerControllerLayout.this.mPlayer.isPlaying()) {
                    SimplerPlayerControllerLayout.this.mPlayer.pause();
                } else {
                    SimplerPlayerControllerLayout.this.mPlayer.start();
                }
                SimplerPlayerControllerLayout.this.updatePausePlay();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                if (!SimplerPlayerControllerLayout.this.mediaController.isShowing()) {
                    SimplerPlayerControllerLayout.this.mediaController.show();
                    return true;
                }
                SimplerPlayerControllerLayout.this.mediaController.hide();
                return true;
            }
        });
    }

    private int[] getDownScreenPos(MotionEvent motionEvent) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return new int[]{0, 0};
        }
        return new int[]{motionEvent.getX() > ((float) (width >> 1)) ? 1 : -1, motionEvent.getY() > ((float) (height >> 1)) ? 1 : -1};
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.isMultiPointEvent = false;
            this.scrubbing = false;
            this.mTouchSeekPro = 0;
            this.mNewSeekPro = 0L;
        } else if (action == 5) {
            this.isMultiPointEvent = true;
            return true;
        } else if ((action == 1 || action == 3) && this.scrubbing) {
            gestureSeekTime(0.0f, true);
        }
        this.mGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePausePlay() {
        if (checkPlayerIsNull()) {
            return;
        }
        if (this.mPlayer.isPlaying()) {
            this.mStartPauseIv.setImageResource(R.drawable.pause_icon);
            this.mCenterStateLay.setVisibility(8);
            return;
        }
        this.mStartPauseIv.setImageResource(R.drawable.play_icon);
        this.mCenterStateLay.setVisibility(0);
        this.mCenterStateIv.setImageResource(R.drawable.play_p_icon);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int updateProgress() {
        if (checkPlayerIsNull() || this.mDragging) {
            return 0;
        }
        int currentPosition = this.mPlayer.getCurrentPosition();
        int duration = this.mPlayer.getDuration();
        SeekBar seekBar = this.mProgressSeekBar;
        if (seekBar != null) {
            if (duration > 0) {
                seekBar.setProgress((int) ((currentPosition * 1000) / duration));
            }
            this.mProgressSeekBar.setSecondaryProgress(this.mPlayer.getBufferPercentage() * 10);
        }
        TextView textView = this.mEndTimeTv;
        if (textView != null) {
            textView.setText(stringForTime(duration));
        }
        TextView textView2 = this.mStartTimeTv;
        if (textView2 != null) {
            textView2.setText(stringForTime(currentPosition));
        }
        Log.i("PlayerControllerLayout", "=position=" + currentPosition + "==duration==" + duration);
        return currentPosition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gestureSeekTime(float f, boolean z) {
        if (checkPlayerIsNull() || this.mCenterTimeTv == null) {
            return;
        }
        if (z) {
            this.mTouchSeekPro = 0;
            if (Math.abs(this.mPlayer.getCurrentPosition() - this.mNewSeekPro) > 1000) {
                this.mPlayer.seekTo((int) this.mNewSeekPro);
            }
            this.mCenterTimeTv.setVisibility(8);
            return;
        }
        int width = isFullScreen() ? this.mProgressSeekBar.getWidth() * 4 : this.mProgressSeekBar.getWidth() * 2;
        long duration = this.mPlayer.getDuration();
        if (width <= 0 || duration <= 0) {
            return;
        }
        if (this.mTouchSeekPro == 0) {
            this.mTouchSeekPro = (int) (((this.mPlayer.getCurrentPosition() * 1.0f) / ((float) duration)) * width);
        }
        int i = (int) (this.mTouchSeekPro + f);
        this.mTouchSeekPro = i;
        long j = ((i * 1.0f) / width) * ((float) duration);
        this.mNewSeekPro = j;
        if (j <= 0) {
            this.mNewSeekPro = 0L;
        }
        if (this.mNewSeekPro >= duration) {
            this.mNewSeekPro = duration;
        }
        this.mCenterTimeTv.setText(stringForTime((int) this.mNewSeekPro));
        this.mCenterTimeTv.setVisibility(0);
        this.scrubbing = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String stringForTime(int i) {
        initFormatter();
        int i2 = i / 1000;
        int i3 = i2 % 60;
        int i4 = (i2 / 60) % 60;
        int i5 = i2 / 3600;
        this.mFormatBuilder.setLength(0);
        return i5 > 0 ? this.mFormatter.format("%d:%02d:%02d", Integer.valueOf(i5), Integer.valueOf(i4), Integer.valueOf(i3)).toString() : this.mFormatter.format("%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3)).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkPlayerIsNull() {
        return this.mPlayer == null || this.mStartPauseIv == null;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        Window window;
        super.onConfigurationChanged(configuration);
        if (configuration != null) {
            boolean z = configuration.orientation == 2;
            updateFullScreen(z);
            Context context = getContext();
            if (!(context instanceof Activity) || (window = ((Activity) context).getWindow()) == null) {
                return;
            }
            if (z) {
                this.mCurrentLayout = 1;
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= 1024;
                window.setAttributes(attributes);
                window.addFlags(512);
                return;
            }
            this.mCurrentLayout = 0;
            WindowManager.LayoutParams attributes2 = window.getAttributes();
            attributes2.flags &= -1025;
            window.setAttributes(attributes2);
            window.clearFlags(512);
        }
    }

    public void setEnableFullScreen(boolean z) {
        this.enableFullScreen = z;
        ImageView imageView = this.mFullScreenIv;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
        }
    }

    public boolean isFullScreen() {
        return this.mCurrentLayout == 1;
    }

    public void updateFullScreen(boolean z) {
        ImageView imageView = this.mFullScreenIv;
        if (imageView != null) {
            imageView.setImageResource(z ? R.drawable.shrink_screen_icon : R.drawable.full_screen_icon);
        }
    }

    public void pause() {
        if (!checkPlayerIsNull() && this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
            this.mStartPauseIv.setImageResource(R.drawable.play_icon);
            this.mCenterStateLay.setVisibility(0);
            this.mCenterStateIv.setImageResource(R.drawable.play_p_icon);
        }
    }

    public void resume() {
        if (checkPlayerIsNull() || this.mPlayer.isPlaying()) {
            return;
        }
        this.mPlayer.start();
        this.mStartPauseIv.setImageResource(R.drawable.pause_icon);
        this.mCenterStateLay.setVisibility(8);
    }
}
