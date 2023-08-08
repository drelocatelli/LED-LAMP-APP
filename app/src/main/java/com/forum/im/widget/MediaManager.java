package com.forum.im.widget;

import android.media.MediaPlayer;
import java.io.IOException;

/* loaded from: classes.dex */
public class MediaManager {
    private static boolean isPause;
    private static MediaPlayer mPlayer;

    public static void playSound(String str, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mediaPlayer = mPlayer;
        if (mediaPlayer == null) {
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            mPlayer = mediaPlayer2;
            mediaPlayer2.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.forum.im.widget.MediaManager.1
                @Override // android.media.MediaPlayer.OnErrorListener
                public boolean onError(MediaPlayer mediaPlayer3, int i, int i2) {
                    MediaManager.mPlayer.reset();
                    return false;
                }
            });
        } else {
            mediaPlayer.reset();
        }
        try {
            mPlayer.setAudioStreamType(3);
            mPlayer.setOnCompletionListener(onCompletionListener);
            mPlayer.setDataSource(str);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalStateException e3) {
            e3.printStackTrace();
        } catch (SecurityException e4) {
            e4.printStackTrace();
        }
    }

    public static void pause() {
        MediaPlayer mediaPlayer = mPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        mPlayer.pause();
        isPause = true;
    }

    public static void resume() {
        MediaPlayer mediaPlayer = mPlayer;
        if (mediaPlayer == null || !isPause) {
            return;
        }
        mediaPlayer.start();
        isPause = false;
    }

    public static void release() {
        MediaPlayer mediaPlayer = mPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mPlayer = null;
        }
    }
}
