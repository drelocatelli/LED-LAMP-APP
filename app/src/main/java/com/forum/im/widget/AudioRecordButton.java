package com.forum.im.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.forum.im.utils.AudioManager;
import com.forum.im.utils.FileSaveUtil;
import com.ledlamp.R;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/* loaded from: classes.dex */
public class AudioRecordButton extends Button implements AudioManager.AudioStageListener {
    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int MSG_AUDIO_PREPARED = 272;
    private static final int MSG_DIALOG_DIMISS = 274;
    private static final int MSG_OVERTIME_SEND = 275;
    private static final int MSG_VOICE_CHANGE = 273;
    private static final int OVERTIME = 60;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;
    private boolean isRecording;
    private boolean isTouch;
    private AudioManager mAudioManager;
    private int mCurrentState;
    private DialogManager mDialogManager;
    private Runnable mGetVoiceLevelRunnable;
    private AudioFinishRecorderListener mListener;
    private boolean mReady;
    private float mTime;
    private Handler mhandler;
    private Handler mp3handler;
    private String saveDir;

    /* loaded from: classes.dex */
    public interface AudioFinishRecorderListener {
        void onFinished(float f, String str);

        void onStart();
    }

    @Override // android.widget.TextView, android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        return false;
    }

    static /* synthetic */ float access$716(AudioRecordButton audioRecordButton, float f) {
        float f2 = audioRecordButton.mTime + f;
        audioRecordButton.mTime = f2;
        return f2;
    }

    public AudioRecordButton(Context context) {
        this(context, null);
    }

    public AudioRecordButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentState = 1;
        this.isRecording = false;
        this.mTime = 0.0f;
        this.saveDir = FileSaveUtil.voice_dir;
        this.mp3handler = new Handler() { // from class: com.forum.im.widget.AudioRecordButton.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != -4) {
                    return;
                }
                Toast.makeText(AudioRecordButton.this.getContext(), "录音权限被屏蔽或者录音设备损坏！\n请在设置中检查是否开启权限！", 0).show();
                AudioRecordButton.this.mDialogManager.dimissDialog();
                AudioRecordButton.this.mAudioManager.cancel();
                AudioRecordButton.this.reset();
            }
        };
        this.mGetVoiceLevelRunnable = new Runnable() { // from class: com.forum.im.widget.AudioRecordButton.3
            @Override // java.lang.Runnable
            public void run() {
                while (AudioRecordButton.this.isRecording) {
                    try {
                        Thread.sleep(100L);
                        AudioRecordButton.access$716(AudioRecordButton.this, 0.1f);
                        AudioRecordButton.this.mhandler.sendEmptyMessage(273);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (AudioRecordButton.this.mTime >= 60.0f) {
                        AudioRecordButton.this.mTime = 60.0f;
                        AudioRecordButton.this.mhandler.sendEmptyMessage(AudioRecordButton.MSG_OVERTIME_SEND);
                        AudioRecordButton.this.isRecording = false;
                        return;
                    }
                    continue;
                }
            }
        };
        this.mhandler = new Handler() { // from class: com.forum.im.widget.AudioRecordButton.4
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case AudioRecordButton.MSG_AUDIO_PREPARED /* 272 */:
                        if (AudioRecordButton.this.isTouch) {
                            AudioRecordButton.this.mTime = 0.0f;
                            AudioRecordButton.this.mDialogManager.showRecordingDialog();
                            AudioRecordButton.this.isRecording = true;
                            new Thread(AudioRecordButton.this.mGetVoiceLevelRunnable).start();
                            return;
                        }
                        return;
                    case 273:
                        AudioRecordButton.this.mDialogManager.updateVoiceLevel(AudioRecordButton.this.mAudioManager.getVoiceLevel(3));
                        return;
                    case AudioRecordButton.MSG_DIALOG_DIMISS /* 274 */:
                        AudioRecordButton.this.isRecording = false;
                        AudioRecordButton.this.mDialogManager.dimissDialog();
                        return;
                    case AudioRecordButton.MSG_OVERTIME_SEND /* 275 */:
                        AudioRecordButton.this.mDialogManager.tooLong();
                        AudioRecordButton.this.mhandler.sendEmptyMessageDelayed(AudioRecordButton.MSG_DIALOG_DIMISS, 1300L);
                        if (AudioRecordButton.this.mListener != null) {
                            if (!FileSaveUtil.isFileExists(new File(AudioRecordButton.this.mAudioManager.getCurrentFilePath()))) {
                                AudioRecordButton.this.mp3handler.sendEmptyMessage(-4);
                            } else {
                                AudioRecordButton.this.mListener.onFinished(AudioRecordButton.this.mTime, AudioRecordButton.this.mAudioManager.getCurrentFilePath());
                            }
                        }
                        AudioRecordButton.this.isRecording = false;
                        AudioRecordButton.this.reset();
                        return;
                    default:
                        return;
                }
            }
        };
        this.isTouch = false;
        this.mDialogManager = new DialogManager(getContext());
        try {
            FileSaveUtil.createSDDirectory(FileSaveUtil.voice_dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioManager audioManager = AudioManager.getInstance(FileSaveUtil.voice_dir);
        this.mAudioManager = audioManager;
        audioManager.setOnAudioStageListener(this);
        this.mAudioManager.setHandle(this.mp3handler);
        setOnLongClickListener(new View.OnLongClickListener() { // from class: com.forum.im.widget.AudioRecordButton.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                try {
                    FileSaveUtil.createSDDirectory(AudioRecordButton.this.saveDir);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                AudioRecordButton.this.mAudioManager.setVocDir(AudioRecordButton.this.saveDir);
                AudioRecordButton.this.mListener.onStart();
                AudioRecordButton.this.mReady = true;
                AudioRecordButton.this.mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    public void setSaveDir(String str) {
        this.saveDir = str + str;
    }

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener audioFinishRecorderListener) {
        this.mListener = audioFinishRecorderListener;
    }

    @Override // com.forum.im.utils.AudioManager.AudioStageListener
    public void wellPrepared() {
        this.mhandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (action == 0) {
            this.isTouch = true;
            changeState(2);
        } else if (action == 1) {
            this.isTouch = false;
            if (!this.mReady) {
                reset();
                return super.onTouchEvent(motionEvent);
            }
            if (!this.isRecording || this.mTime < 0.6f) {
                this.mDialogManager.tooShort();
                this.mAudioManager.cancel();
                this.mhandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300L);
            } else {
                int i = this.mCurrentState;
                if (i == 2) {
                    this.mDialogManager.dimissDialog();
                    this.mAudioManager.release();
                    if (this.mListener != null) {
                        float floatValue = new BigDecimal(this.mTime).setScale(1, 4).floatValue();
                        if (FileSaveUtil.isFileExists(new File(this.mAudioManager.getCurrentFilePath()))) {
                            this.mListener.onFinished(floatValue, this.mAudioManager.getCurrentFilePath());
                        } else {
                            this.mp3handler.sendEmptyMessage(-4);
                        }
                    }
                } else if (i == 3) {
                    this.mAudioManager.cancel();
                    this.mDialogManager.dimissDialog();
                }
            }
            this.isRecording = false;
            reset();
        } else if (action != 2) {
            if (action == 3) {
                this.isTouch = false;
                reset();
            }
        } else if (this.isRecording) {
            if (wantToCancel(x, y)) {
                changeState(3);
            } else {
                changeState(2);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        this.isRecording = false;
        changeState(1);
        this.mReady = false;
        this.mTime = 0.0f;
    }

    private boolean wantToCancel(int i, int i2) {
        return i < 0 || i > getWidth() || i2 < -50 || i2 > getHeight() + 50;
    }

    private void changeState(int i) {
        if (this.mCurrentState != i) {
            this.mCurrentState = i;
            if (i == 1) {
                setBackgroundResource(R.drawable.button_recordnormal);
                setText(R.string.normal);
            } else if (i != 2) {
                if (i != 3) {
                    return;
                }
                setBackgroundResource(R.drawable.button_recording);
                setText(R.string.want_to_cancle);
                this.mDialogManager.wantToCancel();
            } else {
                setBackgroundResource(R.drawable.button_recording);
                setText(R.string.recording);
                if (this.isRecording) {
                    this.mDialogManager.recording();
                }
            }
        }
    }
}
