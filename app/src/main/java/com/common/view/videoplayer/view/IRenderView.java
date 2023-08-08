package com.common.view.videoplayer.view;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

/* loaded from: classes.dex */
public interface IRenderView {
    public static final int AR_16_9_FIT_PARENT = 4;
    public static final int AR_4_3_FIT_PARENT = 5;
    public static final int AR_ASPECT_FILL_PARENT = 1;
    public static final int AR_ASPECT_FIT_PARENT = 0;
    public static final int AR_ASPECT_WRAP_CONTENT = 2;
    public static final int AR_MATCH_PARENT = 3;

    /* loaded from: classes.dex */
    public interface IRenderCallback {
        void onSurfaceChanged(ISurfaceHolder iSurfaceHolder, int i, int i2, int i3);

        void onSurfaceCreated(ISurfaceHolder iSurfaceHolder, int i, int i2);

        void onSurfaceDestroyed(ISurfaceHolder iSurfaceHolder);
    }

    /* loaded from: classes.dex */
    public interface ISurfaceHolder {
        void bindToMediaPlayer(MediaPlayer mediaPlayer);

        IRenderView getRenderView();

        SurfaceHolder getSurfaceHolder();

        SurfaceTexture getSurfaceTexture();

        Surface openSurface();
    }

    void addRenderCallback(IRenderCallback iRenderCallback);

    View getView();

    void removeRenderCallback(IRenderCallback iRenderCallback);

    void setAspectRatio(int i);

    void setVideoSize(int i, int i2);

    boolean shouldWaitForResize();
}
