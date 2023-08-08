package com.common.view.videoplayer.view;

import android.widget.MediaController;

/* loaded from: classes.dex */
public interface IMediaController {
    void hide();

    boolean isShowing();

    void onComplete();

    void onError();

    void onPrepared();

    void onPreparing();

    void setEnabled(boolean z);

    void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl);

    void show();

    void show(int i);
}
