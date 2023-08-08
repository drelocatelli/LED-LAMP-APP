package com.forum.im.utils;

import android.media.MediaRecorder;
import android.os.Handler;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

/* loaded from: classes.dex */
public class AudioManager {
    public static final int MSG_ERROR_AUDIO_RECORD = -4;
    private static AudioManager mInstance;
    private Handler handler;
    private boolean isPrepared;
    private String mCurrentFilePathString;
    private String mDirString;
    public AudioStageListener mListener;
    private MediaRecorder mRecorder;
    private int[] vocAuthority = new int[10];
    private int vocNum = 0;
    private boolean check = true;

    /* loaded from: classes.dex */
    public interface AudioStageListener {
        void wellPrepared();
    }

    private AudioManager(String str) {
        this.mDirString = str;
    }

    public static AudioManager getInstance(String str) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager(str);
                }
            }
        }
        return mInstance;
    }

    public void setHandle(Handler handler) {
        this.handler = handler;
    }

    public void setOnAudioStageListener(AudioStageListener audioStageListener) {
        this.mListener = audioStageListener;
    }

    public void setVocDir(String str) {
        this.mDirString = str;
    }

    public void prepareAudio() {
        try {
            this.isPrepared = false;
            File file = new File(this.mDirString);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, generalFileName());
            this.mCurrentFilePathString = file2.getAbsolutePath();
            MediaRecorder mediaRecorder = new MediaRecorder();
            this.mRecorder = mediaRecorder;
            mediaRecorder.setOutputFile(file2.getAbsolutePath());
            this.mRecorder.setAudioSource(1);
            this.mRecorder.setOutputFormat(3);
            this.mRecorder.setAudioEncoder(1);
            this.mRecorder.prepare();
            this.mRecorder.start();
            AudioStageListener audioStageListener = this.mListener;
            if (audioStageListener != null) {
                audioStageListener.wellPrepared();
            }
            this.isPrepared = true;
        } catch (IOException e) {
            e.printStackTrace();
            Handler handler = this.handler;
            if (handler != null) {
                handler.sendEmptyMessage(-4);
            }
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
            Handler handler2 = this.handler;
            if (handler2 != null) {
                handler2.sendEmptyMessage(-4);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            Handler handler3 = this.handler;
            if (handler3 != null) {
                handler3.sendEmptyMessage(-4);
            }
        }
    }

    private String generalFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int i) {
        if (this.isPrepared) {
            try {
                int maxAmplitude = this.mRecorder.getMaxAmplitude();
                if (this.check) {
                    int i2 = this.vocNum;
                    if (i2 >= 10) {
                        HashSet hashSet = new HashSet();
                        for (int i3 = 0; i3 < this.vocNum; i3++) {
                            hashSet.add(Integer.valueOf(this.vocAuthority[i3]));
                        }
                        if (hashSet.size() == 1) {
                            Handler handler = this.handler;
                            if (handler != null) {
                                handler.sendEmptyMessage(-4);
                            }
                            this.vocNum = 0;
                            this.vocAuthority = new int[10];
                        } else {
                            this.check = false;
                        }
                    } else {
                        this.vocAuthority[i2] = maxAmplitude;
                        this.vocNum = i2 + 1;
                    }
                }
                return ((i * maxAmplitude) / 32768) + 1;
            } catch (Exception unused) {
                Handler handler2 = this.handler;
                if (handler2 != null) {
                    handler2.sendEmptyMessage(-4);
                }
            }
        }
        return 1;
    }

    public void release() {
        MediaRecorder mediaRecorder = this.mRecorder;
        if (mediaRecorder != null) {
            this.isPrepared = false;
            try {
                mediaRecorder.stop();
                this.mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (this.mCurrentFilePathString != null) {
            new File(this.mCurrentFilePathString).delete();
            this.mCurrentFilePathString = null;
        }
    }

    public String getCurrentFilePath() {
        return this.mCurrentFilePathString;
    }
}
