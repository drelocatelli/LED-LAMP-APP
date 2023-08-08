package cn.jzvd;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

/* loaded from: classes.dex */
public class JZMediaManager implements TextureView.SurfaceTextureListener {
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;
    public static final String TAG = "JiaoZiVideoPlayer";
    public static JZMediaManager jzMediaManager;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;
    public static JZResizeTextureView textureView;
    public JZMediaInterface jzMediaInterface;
    public MediaHandler mMediaHandler;
    public HandlerThread mMediaHandlerThread;
    public Handler mainThreadHandler;
    public int positionInList = -1;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public JZMediaManager() {
        HandlerThread handlerThread = new HandlerThread("JiaoZiVideoPlayer");
        this.mMediaHandlerThread = handlerThread;
        handlerThread.start();
        this.mMediaHandler = new MediaHandler(this.mMediaHandlerThread.getLooper());
        this.mainThreadHandler = new Handler();
        if (this.jzMediaInterface == null) {
            this.jzMediaInterface = new JZMediaSystem();
        }
    }

    public static JZMediaManager instance() {
        if (jzMediaManager == null) {
            jzMediaManager = new JZMediaManager();
        }
        return jzMediaManager;
    }

    public static Object[] getDataSource() {
        return instance().jzMediaInterface.dataSourceObjects;
    }

    public static void setDataSource(Object[] objArr) {
        instance().jzMediaInterface.dataSourceObjects = objArr;
    }

    public static Object getCurrentDataSource() {
        return instance().jzMediaInterface.currentDataSource;
    }

    public static void setCurrentDataSource(Object obj) {
        instance().jzMediaInterface.currentDataSource = obj;
    }

    public static long getCurrentPosition() {
        return instance().jzMediaInterface.getCurrentPosition();
    }

    public static long getDuration() {
        return instance().jzMediaInterface.getDuration();
    }

    public static void seekTo(long j) {
        instance().jzMediaInterface.seekTo(j);
    }

    public static void pause() {
        instance().jzMediaInterface.pause();
    }

    public static void start() {
        instance().jzMediaInterface.start();
    }

    public static boolean isPlaying() {
        return instance().jzMediaInterface.isPlaying();
    }

    public void releaseMediaPlayer() {
        this.mMediaHandler.removeCallbacksAndMessages(null);
        Message message = new Message();
        message.what = 2;
        this.mMediaHandler.sendMessage(message);
    }

    public void prepare() {
        releaseMediaPlayer();
        Message message = new Message();
        message.what = 0;
        this.mMediaHandler.sendMessage(message);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        Log.i("JiaoZiVideoPlayer", "onSurfaceTextureAvailable [" + JZVideoPlayerManager.getCurrentJzvd().hashCode() + "] ");
        SurfaceTexture surfaceTexture2 = savedSurfaceTexture;
        if (surfaceTexture2 == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
            return;
        }
        textureView.setSurfaceTexture(surfaceTexture2);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    /* loaded from: classes.dex */
    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i != 0) {
                if (i != 2) {
                    return;
                }
                JZMediaManager.this.jzMediaInterface.release();
                return;
            }
            JZMediaManager.this.currentVideoWidth = 0;
            JZMediaManager.this.currentVideoHeight = 0;
            JZMediaManager.this.jzMediaInterface.prepare();
            if (JZMediaManager.savedSurfaceTexture != null) {
                if (JZMediaManager.surface != null) {
                    JZMediaManager.surface.release();
                }
                JZMediaManager.surface = new Surface(JZMediaManager.savedSurfaceTexture);
                JZMediaManager.this.jzMediaInterface.setSurface(JZMediaManager.surface);
            }
        }
    }
}
