package cn.jzvd;

import android.media.MediaPlayer;
import android.view.Surface;
import java.lang.reflect.Method;
import java.util.Map;

/* loaded from: classes.dex */
public class JZMediaSystem extends JZMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    public MediaPlayer mediaPlayer;

    @Override // cn.jzvd.JZMediaInterface
    public void start() {
        this.mediaPlayer.start();
    }

    @Override // cn.jzvd.JZMediaInterface
    public void prepare() {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mediaPlayer = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
            if (this.dataSourceObjects.length > 1) {
                this.mediaPlayer.setLooping(((Boolean) this.dataSourceObjects[1]).booleanValue());
            }
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnBufferingUpdateListener(this);
            this.mediaPlayer.setScreenOnWhilePlaying(true);
            this.mediaPlayer.setOnSeekCompleteListener(this);
            this.mediaPlayer.setOnErrorListener(this);
            this.mediaPlayer.setOnInfoListener(this);
            this.mediaPlayer.setOnVideoSizeChangedListener(this);
            Method declaredMethod = MediaPlayer.class.getDeclaredMethod("setDataSource", String.class, Map.class);
            if (this.dataSourceObjects.length > 2) {
                declaredMethod.invoke(this.mediaPlayer, this.currentDataSource.toString(), this.dataSourceObjects[2]);
            } else {
                declaredMethod.invoke(this.mediaPlayer, this.currentDataSource.toString(), null);
            }
            this.mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.jzvd.JZMediaInterface
    public void pause() {
        this.mediaPlayer.pause();
    }

    @Override // cn.jzvd.JZMediaInterface
    public boolean isPlaying() {
        return this.mediaPlayer.isPlaying();
    }

    @Override // cn.jzvd.JZMediaInterface
    public void seekTo(long j) {
        try {
            this.mediaPlayer.seekTo((int) j);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override // cn.jzvd.JZMediaInterface
    public void release() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override // cn.jzvd.JZMediaInterface
    public long getCurrentPosition() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0L;
    }

    @Override // cn.jzvd.JZMediaInterface
    public long getDuration() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0L;
    }

    @Override // cn.jzvd.JZMediaInterface
    public void setSurface(Surface surface) {
        this.mediaPlayer.setSurface(surface);
    }

    @Override // cn.jzvd.JZMediaInterface
    public void setVolume(float f, float f2) {
        this.mediaPlayer.setVolume(f, f2);
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        if (this.currentDataSource.toString().toLowerCase().contains("mp3")) {
            JZMediaManager.instance().mainThreadHandler.post(new Runnable() { // from class: cn.jzvd.JZMediaSystem.1
                @Override // java.lang.Runnable
                public void run() {
                    if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                        JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() { // from class: cn.jzvd.JZMediaSystem.2
            @Override // java.lang.Runnable
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    @Override // android.media.MediaPlayer.OnBufferingUpdateListener
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int i) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() { // from class: cn.jzvd.JZMediaSystem.3
            @Override // java.lang.Runnable
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().setBufferProgress(i);
                }
            }
        });
    }

    @Override // android.media.MediaPlayer.OnSeekCompleteListener
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() { // from class: cn.jzvd.JZMediaSystem.4
            @Override // java.lang.Runnable
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, final int i, final int i2) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() { // from class: cn.jzvd.JZMediaSystem.5
            @Override // java.lang.Runnable
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onError(i, i2);
                }
            }
        });
        return true;
    }

    @Override // android.media.MediaPlayer.OnInfoListener
    public boolean onInfo(MediaPlayer mediaPlayer, final int i, final int i2) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() { // from class: cn.jzvd.JZMediaSystem.6
            @Override // java.lang.Runnable
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    if (i == 3) {
                        if (JZVideoPlayerManager.getCurrentJzvd().currentState == 1 || JZVideoPlayerManager.getCurrentJzvd().currentState == 2) {
                            JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                            return;
                        }
                        return;
                    }
                    JZVideoPlayerManager.getCurrentJzvd().onInfo(i, i2);
                }
            }
        });
        return false;
    }

    @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        JZMediaManager.instance().currentVideoWidth = i;
        JZMediaManager.instance().currentVideoHeight = i2;
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() { // from class: cn.jzvd.JZMediaSystem.7
            @Override // java.lang.Runnable
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }
}
