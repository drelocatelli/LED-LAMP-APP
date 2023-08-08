package com.common.view.videoplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import androidx.core.view.ViewCompat;
import com.common.view.videoplayer.view.IRenderView;
import com.ledlamp.R;
import com.luck.picture.lib.config.PictureMimeType;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class SimpleVideoView extends FrameLayout implements MediaController.MediaPlayerControl {
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int[] s_allAspectRatio = {0, 1, 2, 4, 5};
    private String TAG;
    private Context mAppContext;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private int mCurrentAspectRatio;
    private int mCurrentBufferPercentage;
    private int mCurrentRender;
    private int mCurrentState;
    private MediaPlayer.OnErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private MediaPlayer.OnInfoListener mInfoListener;
    private IMediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnInfoListener mOnInfoListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    MediaPlayer.OnPreparedListener mPreparedListener;
    private IRenderView mRenderView;
    IRenderView.IRenderCallback mSHCallback;
    private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener;
    private int mSeekWhenPrepared;
    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private int mSurfaceHeight;
    private IRenderView.ISurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoWidth;

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getAudioSessionId() {
        return 0;
    }

    public SimpleVideoView(Context context) {
        super(context);
        this.TAG = "SimpleVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mCurrentRender = 1;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
                SimpleVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                SimpleVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                if (SimpleVideoView.this.mVideoWidth == 0 || SimpleVideoView.this.mVideoHeight == 0) {
                    return;
                }
                if (SimpleVideoView.this.mRenderView != null) {
                    SimpleVideoView.this.mRenderView.setVideoSize(SimpleVideoView.this.mVideoWidth, SimpleVideoView.this.mVideoHeight);
                }
                SimpleVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                SimpleVideoView.this.mCurrentState = 2;
                if (SimpleVideoView.this.mOnPreparedListener != null) {
                    SimpleVideoView.this.mOnPreparedListener.onPrepared(SimpleVideoView.this.mMediaPlayer);
                }
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.setEnabled(true);
                    SimpleVideoView.this.mMediaController.onPrepared();
                }
                SimpleVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                SimpleVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                int i = SimpleVideoView.this.mSeekWhenPrepared;
                if (i != 0) {
                    SimpleVideoView.this.seekTo(i);
                }
                if (SimpleVideoView.this.mVideoWidth == 0 || SimpleVideoView.this.mVideoHeight == 0) {
                    if (SimpleVideoView.this.mTargetState == 3) {
                        SimpleVideoView.this.start();
                        if (SimpleVideoView.this.mMediaController != null) {
                            SimpleVideoView.this.mMediaController.show();
                        }
                    }
                } else if (SimpleVideoView.this.mRenderView != null) {
                    SimpleVideoView.this.mRenderView.setVideoSize(SimpleVideoView.this.mVideoWidth, SimpleVideoView.this.mVideoHeight);
                    if (!SimpleVideoView.this.mRenderView.shouldWaitForResize() || (SimpleVideoView.this.mSurfaceWidth == SimpleVideoView.this.mVideoWidth && SimpleVideoView.this.mSurfaceHeight == SimpleVideoView.this.mVideoHeight)) {
                        if (SimpleVideoView.this.mTargetState == 3) {
                            SimpleVideoView.this.start();
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.show();
                            }
                        } else if (SimpleVideoView.this.isPlaying()) {
                        } else {
                            if ((i != 0 || SimpleVideoView.this.getCurrentPosition() > 0) && SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.show();
                            }
                        }
                    } else if (SimpleVideoView.this.mTargetState == 3) {
                        SimpleVideoView.this.start();
                        if (SimpleVideoView.this.mMediaController != null) {
                            SimpleVideoView.this.mMediaController.show();
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                SimpleVideoView.this.mCurrentState = 5;
                SimpleVideoView.this.mTargetState = 5;
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.show(3600000);
                    SimpleVideoView.this.mMediaController.onComplete();
                }
                if (SimpleVideoView.this.mOnCompletionListener != null) {
                    SimpleVideoView.this.mOnCompletionListener.onCompletion(SimpleVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new MediaPlayer.OnInfoListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.4
            @Override // android.media.MediaPlayer.OnInfoListener
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                if (SimpleVideoView.this.mOnInfoListener != null) {
                    SimpleVideoView.this.mOnInfoListener.onInfo(mediaPlayer, i, i2);
                }
                if (i == 3) {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                } else if (i == 901) {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                } else if (i != 902) {
                    switch (i) {
                        case 700:
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            return true;
                        case 701:
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.onPreparing();
                            }
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                            return true;
                        case 702:
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.onPrepared();
                                return true;
                            }
                            return true;
                        default:
                            switch (i) {
                                case 800:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                    return true;
                                case 801:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                    return true;
                                case 802:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                    return true;
                                default:
                                    return true;
                            }
                    }
                } else {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
            }
        };
        this.mErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.5
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                String str = SimpleVideoView.this.TAG;
                Log.d(str, "Error: " + i + "," + i2);
                SimpleVideoView.this.mCurrentState = -1;
                SimpleVideoView.this.mTargetState = -1;
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.show(3600000);
                    SimpleVideoView.this.mMediaController.onError();
                }
                if (SimpleVideoView.this.mOnErrorListener != null) {
                    SimpleVideoView.this.mOnErrorListener.onError(SimpleVideoView.this.mMediaPlayer, i, i2);
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.6
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                SimpleVideoView.this.mCurrentBufferPercentage = i;
            }
        };
        this.mSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.7
            @Override // android.media.MediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(MediaPlayer mediaPlayer) {
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: com.common.view.videoplayer.view.SimpleVideoView.8
            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceChanged(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2, int i3) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceWidth = i2;
                SimpleVideoView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = SimpleVideoView.this.mTargetState == 3;
                if (SimpleVideoView.this.mRenderView.shouldWaitForResize() && (SimpleVideoView.this.mVideoWidth != i2 || SimpleVideoView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (SimpleVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (SimpleVideoView.this.mSeekWhenPrepared != 0) {
                        SimpleVideoView simpleVideoView = SimpleVideoView.this;
                        simpleVideoView.seekTo(simpleVideoView.mSeekWhenPrepared);
                    }
                    SimpleVideoView.this.start();
                }
            }

            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceCreated(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceHolder = iSurfaceHolder;
                if (SimpleVideoView.this.mMediaPlayer == null) {
                    SimpleVideoView.this.openVideo();
                    return;
                }
                SimpleVideoView simpleVideoView = SimpleVideoView.this;
                simpleVideoView.bindSurfaceHolder(simpleVideoView.mMediaPlayer, iSurfaceHolder);
            }

            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceHolder = null;
                SimpleVideoView.this.releaseWithoutStop();
            }
        };
        initVideoView(context);
    }

    public SimpleVideoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = "SimpleVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mCurrentRender = 1;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
                SimpleVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                SimpleVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                if (SimpleVideoView.this.mVideoWidth == 0 || SimpleVideoView.this.mVideoHeight == 0) {
                    return;
                }
                if (SimpleVideoView.this.mRenderView != null) {
                    SimpleVideoView.this.mRenderView.setVideoSize(SimpleVideoView.this.mVideoWidth, SimpleVideoView.this.mVideoHeight);
                }
                SimpleVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                SimpleVideoView.this.mCurrentState = 2;
                if (SimpleVideoView.this.mOnPreparedListener != null) {
                    SimpleVideoView.this.mOnPreparedListener.onPrepared(SimpleVideoView.this.mMediaPlayer);
                }
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.setEnabled(true);
                    SimpleVideoView.this.mMediaController.onPrepared();
                }
                SimpleVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                SimpleVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                int i = SimpleVideoView.this.mSeekWhenPrepared;
                if (i != 0) {
                    SimpleVideoView.this.seekTo(i);
                }
                if (SimpleVideoView.this.mVideoWidth == 0 || SimpleVideoView.this.mVideoHeight == 0) {
                    if (SimpleVideoView.this.mTargetState == 3) {
                        SimpleVideoView.this.start();
                        if (SimpleVideoView.this.mMediaController != null) {
                            SimpleVideoView.this.mMediaController.show();
                        }
                    }
                } else if (SimpleVideoView.this.mRenderView != null) {
                    SimpleVideoView.this.mRenderView.setVideoSize(SimpleVideoView.this.mVideoWidth, SimpleVideoView.this.mVideoHeight);
                    if (!SimpleVideoView.this.mRenderView.shouldWaitForResize() || (SimpleVideoView.this.mSurfaceWidth == SimpleVideoView.this.mVideoWidth && SimpleVideoView.this.mSurfaceHeight == SimpleVideoView.this.mVideoHeight)) {
                        if (SimpleVideoView.this.mTargetState == 3) {
                            SimpleVideoView.this.start();
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.show();
                            }
                        } else if (SimpleVideoView.this.isPlaying()) {
                        } else {
                            if ((i != 0 || SimpleVideoView.this.getCurrentPosition() > 0) && SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.show();
                            }
                        }
                    } else if (SimpleVideoView.this.mTargetState == 3) {
                        SimpleVideoView.this.start();
                        if (SimpleVideoView.this.mMediaController != null) {
                            SimpleVideoView.this.mMediaController.show();
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                SimpleVideoView.this.mCurrentState = 5;
                SimpleVideoView.this.mTargetState = 5;
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.show(3600000);
                    SimpleVideoView.this.mMediaController.onComplete();
                }
                if (SimpleVideoView.this.mOnCompletionListener != null) {
                    SimpleVideoView.this.mOnCompletionListener.onCompletion(SimpleVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new MediaPlayer.OnInfoListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.4
            @Override // android.media.MediaPlayer.OnInfoListener
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                if (SimpleVideoView.this.mOnInfoListener != null) {
                    SimpleVideoView.this.mOnInfoListener.onInfo(mediaPlayer, i, i2);
                }
                if (i == 3) {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                } else if (i == 901) {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                } else if (i != 902) {
                    switch (i) {
                        case 700:
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            return true;
                        case 701:
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.onPreparing();
                            }
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                            return true;
                        case 702:
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.onPrepared();
                                return true;
                            }
                            return true;
                        default:
                            switch (i) {
                                case 800:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                    return true;
                                case 801:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                    return true;
                                case 802:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                    return true;
                                default:
                                    return true;
                            }
                    }
                } else {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
            }
        };
        this.mErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.5
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                String str = SimpleVideoView.this.TAG;
                Log.d(str, "Error: " + i + "," + i2);
                SimpleVideoView.this.mCurrentState = -1;
                SimpleVideoView.this.mTargetState = -1;
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.show(3600000);
                    SimpleVideoView.this.mMediaController.onError();
                }
                if (SimpleVideoView.this.mOnErrorListener != null) {
                    SimpleVideoView.this.mOnErrorListener.onError(SimpleVideoView.this.mMediaPlayer, i, i2);
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.6
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                SimpleVideoView.this.mCurrentBufferPercentage = i;
            }
        };
        this.mSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.7
            @Override // android.media.MediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(MediaPlayer mediaPlayer) {
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: com.common.view.videoplayer.view.SimpleVideoView.8
            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceChanged(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2, int i3) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceWidth = i2;
                SimpleVideoView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = SimpleVideoView.this.mTargetState == 3;
                if (SimpleVideoView.this.mRenderView.shouldWaitForResize() && (SimpleVideoView.this.mVideoWidth != i2 || SimpleVideoView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (SimpleVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (SimpleVideoView.this.mSeekWhenPrepared != 0) {
                        SimpleVideoView simpleVideoView = SimpleVideoView.this;
                        simpleVideoView.seekTo(simpleVideoView.mSeekWhenPrepared);
                    }
                    SimpleVideoView.this.start();
                }
            }

            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceCreated(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceHolder = iSurfaceHolder;
                if (SimpleVideoView.this.mMediaPlayer == null) {
                    SimpleVideoView.this.openVideo();
                    return;
                }
                SimpleVideoView simpleVideoView = SimpleVideoView.this;
                simpleVideoView.bindSurfaceHolder(simpleVideoView.mMediaPlayer, iSurfaceHolder);
            }

            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceHolder = null;
                SimpleVideoView.this.releaseWithoutStop();
            }
        };
        readStyleParameters(context, attributeSet);
        initVideoView(context);
    }

    public SimpleVideoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.TAG = "SimpleVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mCurrentRender = 1;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i2, int i22) {
                SimpleVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                SimpleVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                if (SimpleVideoView.this.mVideoWidth == 0 || SimpleVideoView.this.mVideoHeight == 0) {
                    return;
                }
                if (SimpleVideoView.this.mRenderView != null) {
                    SimpleVideoView.this.mRenderView.setVideoSize(SimpleVideoView.this.mVideoWidth, SimpleVideoView.this.mVideoHeight);
                }
                SimpleVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                SimpleVideoView.this.mCurrentState = 2;
                if (SimpleVideoView.this.mOnPreparedListener != null) {
                    SimpleVideoView.this.mOnPreparedListener.onPrepared(SimpleVideoView.this.mMediaPlayer);
                }
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.setEnabled(true);
                    SimpleVideoView.this.mMediaController.onPrepared();
                }
                SimpleVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                SimpleVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                int i2 = SimpleVideoView.this.mSeekWhenPrepared;
                if (i2 != 0) {
                    SimpleVideoView.this.seekTo(i2);
                }
                if (SimpleVideoView.this.mVideoWidth == 0 || SimpleVideoView.this.mVideoHeight == 0) {
                    if (SimpleVideoView.this.mTargetState == 3) {
                        SimpleVideoView.this.start();
                        if (SimpleVideoView.this.mMediaController != null) {
                            SimpleVideoView.this.mMediaController.show();
                        }
                    }
                } else if (SimpleVideoView.this.mRenderView != null) {
                    SimpleVideoView.this.mRenderView.setVideoSize(SimpleVideoView.this.mVideoWidth, SimpleVideoView.this.mVideoHeight);
                    if (!SimpleVideoView.this.mRenderView.shouldWaitForResize() || (SimpleVideoView.this.mSurfaceWidth == SimpleVideoView.this.mVideoWidth && SimpleVideoView.this.mSurfaceHeight == SimpleVideoView.this.mVideoHeight)) {
                        if (SimpleVideoView.this.mTargetState == 3) {
                            SimpleVideoView.this.start();
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.show();
                            }
                        } else if (SimpleVideoView.this.isPlaying()) {
                        } else {
                            if ((i2 != 0 || SimpleVideoView.this.getCurrentPosition() > 0) && SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.show();
                            }
                        }
                    } else if (SimpleVideoView.this.mTargetState == 3) {
                        SimpleVideoView.this.start();
                        if (SimpleVideoView.this.mMediaController != null) {
                            SimpleVideoView.this.mMediaController.show();
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                SimpleVideoView.this.mCurrentState = 5;
                SimpleVideoView.this.mTargetState = 5;
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.show(3600000);
                    SimpleVideoView.this.mMediaController.onComplete();
                }
                if (SimpleVideoView.this.mOnCompletionListener != null) {
                    SimpleVideoView.this.mOnCompletionListener.onCompletion(SimpleVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new MediaPlayer.OnInfoListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.4
            @Override // android.media.MediaPlayer.OnInfoListener
            public boolean onInfo(MediaPlayer mediaPlayer, int i2, int i22) {
                if (SimpleVideoView.this.mOnInfoListener != null) {
                    SimpleVideoView.this.mOnInfoListener.onInfo(mediaPlayer, i2, i22);
                }
                if (i2 == 3) {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                } else if (i2 == 901) {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                } else if (i2 != 902) {
                    switch (i2) {
                        case 700:
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            return true;
                        case 701:
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.onPreparing();
                            }
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                            return true;
                        case 702:
                            Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                            if (SimpleVideoView.this.mMediaController != null) {
                                SimpleVideoView.this.mMediaController.onPrepared();
                                return true;
                            }
                            return true;
                        default:
                            switch (i2) {
                                case 800:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                    return true;
                                case 801:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                    return true;
                                case 802:
                                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                    return true;
                                default:
                                    return true;
                            }
                    }
                } else {
                    Log.d(SimpleVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
            }
        };
        this.mErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.5
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i2, int i22) {
                String str = SimpleVideoView.this.TAG;
                Log.d(str, "Error: " + i2 + "," + i22);
                SimpleVideoView.this.mCurrentState = -1;
                SimpleVideoView.this.mTargetState = -1;
                if (SimpleVideoView.this.mMediaController != null) {
                    SimpleVideoView.this.mMediaController.show(3600000);
                    SimpleVideoView.this.mMediaController.onError();
                }
                if (SimpleVideoView.this.mOnErrorListener != null) {
                    SimpleVideoView.this.mOnErrorListener.onError(SimpleVideoView.this.mMediaPlayer, i2, i22);
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.6
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i2) {
                SimpleVideoView.this.mCurrentBufferPercentage = i2;
            }
        };
        this.mSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() { // from class: com.common.view.videoplayer.view.SimpleVideoView.7
            @Override // android.media.MediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(MediaPlayer mediaPlayer) {
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: com.common.view.videoplayer.view.SimpleVideoView.8
            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceChanged(IRenderView.ISurfaceHolder iSurfaceHolder, int i2, int i22, int i3) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceWidth = i22;
                SimpleVideoView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = SimpleVideoView.this.mTargetState == 3;
                if (SimpleVideoView.this.mRenderView.shouldWaitForResize() && (SimpleVideoView.this.mVideoWidth != i22 || SimpleVideoView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (SimpleVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (SimpleVideoView.this.mSeekWhenPrepared != 0) {
                        SimpleVideoView simpleVideoView = SimpleVideoView.this;
                        simpleVideoView.seekTo(simpleVideoView.mSeekWhenPrepared);
                    }
                    SimpleVideoView.this.start();
                }
            }

            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceCreated(IRenderView.ISurfaceHolder iSurfaceHolder, int i2, int i22) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceHolder = iSurfaceHolder;
                if (SimpleVideoView.this.mMediaPlayer == null) {
                    SimpleVideoView.this.openVideo();
                    return;
                }
                SimpleVideoView simpleVideoView = SimpleVideoView.this;
                simpleVideoView.bindSurfaceHolder(simpleVideoView.mMediaPlayer, iSurfaceHolder);
            }

            @Override // com.common.view.videoplayer.view.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != SimpleVideoView.this.mRenderView) {
                    Log.e(SimpleVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                SimpleVideoView.this.mSurfaceHolder = null;
                SimpleVideoView.this.releaseWithoutStop();
            }
        };
        readStyleParameters(context, attributeSet);
        initVideoView(context);
    }

    private void readStyleParameters(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SimpleVideoView);
        try {
            int integer = obtainStyledAttributes.getInteger(1, 1);
            if (integer == 1) {
                this.mCurrentRender = 1;
            } else if (integer == 2) {
                this.mCurrentRender = 2;
            }
            int integer2 = obtainStyledAttributes.getInteger(0, 1);
            if (integer2 == 1) {
                this.mCurrentAspectRatio = s_allAspectRatio[0];
            } else if (integer2 == 2) {
                this.mCurrentAspectRatio = s_allAspectRatio[1];
            } else if (integer2 == 3) {
                this.mCurrentAspectRatio = s_allAspectRatio[2];
            } else if (integer2 == 4) {
                this.mCurrentAspectRatio = s_allAspectRatio[3];
            } else if (integer2 == 5) {
                this.mCurrentAspectRatio = s_allAspectRatio[4];
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    private void initRenders() {
        setRender(this.mCurrentRender);
    }

    public void setRender(int i) {
        if (i == 0) {
            setRenderView(null);
        } else if (i == 1) {
            setRenderView(new SurfaceRenderView(getContext()));
        } else if (i == 2) {
            TextureRenderView textureRenderView = new TextureRenderView(getContext());
            if (this.mMediaPlayer != null) {
                textureRenderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                textureRenderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
                textureRenderView.setAspectRatio(this.mCurrentAspectRatio);
            }
            setRenderView(textureRenderView);
        } else {
            Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", Integer.valueOf(i)));
        }
    }

    public void setRenderView(IRenderView iRenderView) {
        int i;
        if (this.mRenderView != null) {
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null) {
                mediaPlayer.setDisplay(null);
            }
            View view = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(view);
        }
        if (iRenderView == null) {
            return;
        }
        this.mRenderView = iRenderView;
        iRenderView.setAspectRatio(this.mCurrentAspectRatio);
        int i2 = this.mVideoWidth;
        if (i2 > 0 && (i = this.mVideoHeight) > 0) {
            iRenderView.setVideoSize(i2, i);
        }
        View view2 = this.mRenderView.getView();
        view2.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
        addView(view2);
        this.mRenderView.addRenderCallback(this.mSHCallback);
    }

    public void setVideoPath(String str) {
        setVideoURI(Uri.parse(str));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    public void setVideoURI(Uri uri, Map<String, String> map) {
        this.mUri = uri;
        this.mHeaders = map;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openVideo() {
        if (this.mUri == null || this.mSurfaceHolder == null) {
            return;
        }
        release(false);
        AudioManager audioManager = (AudioManager) this.mAppContext.getSystemService(PictureMimeType.MIME_TYPE_PREFIX_AUDIO);
        if (audioManager != null) {
            audioManager.requestAudioFocus(null, 3, 1);
        }
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mMediaPlayer = mediaPlayer;
            mediaPlayer.setOnPreparedListener(this.mPreparedListener);
            this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
            this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
            this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
            this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
            this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
            this.mCurrentBufferPercentage = 0;
            this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
            bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.setScreenOnWhilePlaying(true);
            this.mMediaPlayer.prepareAsync();
            this.mCurrentState = 1;
            attachMediaController();
        } catch (Exception e) {
            String str = this.TAG;
            Log.w(str, "Unable to open content: " + this.mUri, e);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
        }
    }

    public void setMediaController(IMediaController iMediaController) {
        IMediaController iMediaController2 = this.mMediaController;
        if (iMediaController2 != null) {
            iMediaController2.hide();
        }
        this.mMediaController = iMediaController;
        attachMediaController();
    }

    private void attachMediaController() {
        IMediaController iMediaController;
        if (this.mMediaPlayer == null || (iMediaController = this.mMediaController) == null) {
            return;
        }
        iMediaController.setMediaPlayer(this);
        this.mMediaController.setEnabled(isInPlaybackState());
        this.mMediaController.show(3600000);
        this.mMediaController.onPreparing();
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindSurfaceHolder(MediaPlayer mediaPlayer, IRenderView.ISurfaceHolder iSurfaceHolder) {
        if (mediaPlayer == null) {
            return;
        }
        if (iSurfaceHolder == null) {
            mediaPlayer.setDisplay(null);
        } else {
            iSurfaceHolder.bindToMediaPlayer(mediaPlayer);
        }
    }

    public void releaseWithoutStop() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(null);
        }
    }

    public void release(boolean z) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (z) {
                this.mTargetState = 0;
            }
            AudioManager audioManager = (AudioManager) this.mAppContext.getSystemService(PictureMimeType.MIME_TYPE_PREFIX_AUDIO);
            if (audioManager != null) {
                audioManager.abandonAudioFocus(null);
            }
        }
    }

    public void stopPlayback() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
            AudioManager audioManager = (AudioManager) this.mAppContext.getSystemService(PictureMimeType.MIME_TYPE_PREFIX_AUDIO);
            if (audioManager != null) {
                audioManager.abandonAudioFocus(null);
            }
        }
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (!isInPlaybackState() || this.mMediaController == null) {
            return false;
        }
        toggleMediaControlsVisibility();
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z = (i == 4 || i == 24 || i == 25 || i == 164 || i == 82 || i == 5 || i == 6) ? false : true;
        if (isInPlaybackState() && z && this.mMediaController != null) {
            if (i == 79 || i == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                } else {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            } else if (i == 126) {
                if (!this.mMediaPlayer.isPlaying()) {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            } else if (i == 86 || i == 127) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                }
                return true;
            } else {
                toggleMediaControlsVisibility();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void toggleMediaControlsVisibility() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        } else if (this.mCurrentState == -1) {
            resume();
        }
        this.mTargetState = 3;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getDuration() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getCurrentPosition() {
        if (isCompleted()) {
            return this.mMediaPlayer.getDuration();
        }
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void seekTo(int i) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(i);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = i;
    }

    private boolean isCompleted() {
        return isInPlaybackState() && this.mCurrentState == 5;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean isPlaying() {
        return isInPlaybackState() && this.mCurrentState == 3;
    }

    public boolean isOnPause() {
        return isInPlaybackState() && this.mCurrentState == 4;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        int i;
        return (this.mMediaPlayer == null || (i = this.mCurrentState) == -1 || i == 0 || i == 1) ? false : true;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canPause() {
        return this.mCanPause;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }
}
