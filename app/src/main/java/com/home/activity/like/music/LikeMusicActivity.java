package com.home.activity.like.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.net.NetResult;
import com.common.task.BaseTask;
import com.common.task.NetCallBack;
import com.common.uitl.DrawTool;
import com.common.uitl.ListUtiles;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.main.MainActivity_LIKE;
import com.home.activity.other.MusicLibActivity;
import com.home.adapter.Mp3Adapter_LIKE;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.bean.Mp3;
import com.home.bean.SceneBean;
import com.home.constant.CommonConstant;
import com.ledlamp.R;
import com.luck.picture.lib.tools.PictureFileUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class LikeMusicActivity extends LedBleActivity {
    private static final int INT_GO_SELECT_MUSIC = 100;
    private static final int INT_UPDATE_PROGRESS = 101;
    private static final String TAG = "LikeMusicActivity";
    private static ArrayList<Mp3> mp3s = new ArrayList<>();
    public static SceneBean sceneBeanFragment;
    private BaseTask baseTask;
    @BindView(R.id.buttonBreathe)
    Button buttonBreathe;
    @BindView(R.id.buttonCancell)
    ImageView buttonCancell;
    @BindView(R.id.buttonFlash)
    Button buttonFlash;
    @BindView(R.id.buttonMusicLib)
    Button buttonMusicLib;
    @BindView(R.id.buttonStrobe)
    Button buttonStrobe;
    private volatile int chnnelValue;
    private Mp3 currentMp3;
    @BindView(R.id.imageViewNext)
    ImageView imageViewNext;
    @BindView(R.id.imageViewPlay)
    ImageView imageViewPlay;
    @BindView(R.id.imageViewPlayType)
    ImageView imageViewPlayType;
    @BindView(R.id.imageViewPre)
    ImageView imageViewPre;
    @BindView(R.id.listViewMuiscsList)
    ListView listViewMuiscsList;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    private MainActivity_LIKE mActivity;
    private Visualizer mVisualizer;
    private VisualizerView mVisualizerView;
    private MediaPlayer mediaPlayer;
    private Mp3Adapter_LIKE mp3Adapter;
    @BindView(R.id.seekBarMusic)
    SeekBar seekBarMusic;
    private Thread sendMusicThread;
    private Handler startHandler;
    private Runnable startRunnable;
    @BindView(R.id.tvCurrentTime)
    TextView tvCurrentTime;
    @BindView(R.id.tvTotalTime)
    TextView tvTotalTime;
    private final int LOCATION_CODE = 110;
    private final int RECORD_AUDIO = 200;
    private boolean isSending = true;
    private boolean isLoopAll = false;
    private boolean isLoopOne = true;
    private boolean isRandom = false;
    private boolean isMusic = true;
    private boolean isStartTimer = true;
    private Random random = new Random();
    private Handler mhanHandler = new Handler() { // from class: com.home.activity.like.music.LikeMusicActivity.11
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String str;
            String str2;
            if (message.what != 101) {
                return;
            }
            LikeMusicActivity likeMusicActivity = LikeMusicActivity.this;
            likeMusicActivity.updatePlayProgress(likeMusicActivity.currentMp3);
            if (LikeMusicActivity.this.mediaPlayer != null) {
                int currentPosition = LikeMusicActivity.this.mediaPlayer.getCurrentPosition() / 1000;
                if (currentPosition >= 60) {
                    int i = currentPosition % 60;
                    if (i < 10) {
                        str = NetResult.CODE_OK + i;
                    } else {
                        str = "" + i;
                    }
                    str2 = (currentPosition / 60) + ":" + str;
                } else if (currentPosition < 10) {
                    str2 = "0:0" + currentPosition;
                } else {
                    str2 = "0:" + currentPosition;
                }
                if (LikeMusicActivity.this.tvCurrentTime == null || str2 == null) {
                    return;
                }
                LikeMusicActivity.this.tvCurrentTime.setText(str2);
            }
        }
    };
    boolean isSettingBoolean = false;
    Random rand = new Random();

    private void sendMusicData() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        super.onCreate(bundle);
        setContentView(R.layout.activity_music_like);
        initData();
        initEvent();
        initMp3();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        this.mediaPlayer.stop();
    }

    public void initData() {
        MainActivity_LIKE mainActivity = MainActivity_LIKE.getMainActivity();
        this.mActivity = mainActivity;
        sceneBeanFragment = (SceneBean) SharePersistent.getObjectValue(mainActivity, CommonConstant.APP_MODE);
        this.mVisualizerView = new VisualizerView(this.mActivity);
    }

    private void initMp3() {
        if (ListUtiles.isEmpty(mp3s)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") != 0) {
                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 110);
                    refresh();
                } else {
                    refresh();
                }
            } else {
                refresh();
            }
        } else {
            buildmp3Adapter(mp3s, true);
        }
        LedBleApplication.getApp().setMp3s(mp3s);
    }

    public void refresh() {
        if (this.startHandler == null) {
            this.startHandler = new Handler();
        }
        if (this.startRunnable == null) {
            this.startRunnable = new Runnable() { // from class: com.home.activity.like.music.LikeMusicActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    LikeMusicActivity.this.startHandler.postDelayed(LikeMusicActivity.this.startRunnable, 100L);
                    if (ContextCompat.checkSelfPermission(LikeMusicActivity.this.getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
                        if (ListUtiles.isEmpty(LikeMusicActivity.mp3s)) {
                            LikeMusicActivity.this.scanMp3();
                        } else {
                            LikeMusicActivity.this.buildmp3Adapter(LikeMusicActivity.mp3s, true);
                        }
                        LikeMusicActivity.this.startHandler.removeCallbacks(LikeMusicActivity.this.startRunnable);
                    }
                }
            };
        }
        this.startHandler.postDelayed(this.startRunnable, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Mp3Adapter_LIKE buildmp3Adapter(ArrayList<Mp3> arrayList, boolean z) {
        if (ListUtiles.isEmpty(arrayList)) {
            return null;
        }
        if (!z) {
            mp3s.clear();
            mp3s.addAll(arrayList);
        }
        this.mp3Adapter = new Mp3Adapter_LIKE(this, mp3s);
        if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            this.mp3Adapter.setSelectSet(new HashSet<>(LedBleApplication.getApp().getMp3s()));
        }
        this.mp3Adapter.setOnSelectListener(new Mp3Adapter_LIKE.OnSelectListener() { // from class: com.home.activity.like.music.LikeMusicActivity.2
            @Override // com.home.adapter.Mp3Adapter_LIKE.OnSelectListener
            public void onSelect(int i, Mp3 mp3, HashSet<Mp3> hashSet, boolean z2, BaseAdapter baseAdapter) {
                if (hashSet.contains(mp3)) {
                    hashSet.remove(mp3);
                } else {
                    hashSet.add(mp3);
                }
                baseAdapter.notifyDataSetChanged();
            }
        });
        Mp3Adapter_LIKE mp3Adapter_LIKE = this.mp3Adapter;
        if (mp3Adapter_LIKE != null) {
            this.listViewMuiscsList.setAdapter((ListAdapter) mp3Adapter_LIKE);
        }
        this.listViewMuiscsList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.like.music.LikeMusicActivity.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                try {
                    LikeMusicActivity.this.mp3Adapter.setCurrentItem(i);
                    LikeMusicActivity.this.mp3Adapter.notifyDataSetChanged();
                    LikeMusicActivity.this.startPlay(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return this.mp3Adapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanMp3() {
        BaseTask baseTask = this.baseTask;
        if (baseTask != null && !baseTask.cancel(true)) {
            this.baseTask.cancel(true);
        }
        BaseTask baseTask2 = new BaseTask(new NetCallBack() { // from class: com.home.activity.like.music.LikeMusicActivity.4
            @Override // com.common.task.NetCallBack
            public void onPreCall() {
            }

            @Override // com.common.task.NetCallBack
            public void onFinish(NetResult netResult) {
                if (netResult != null) {
                    LikeMusicActivity.this.buildmp3Adapter((ArrayList) netResult.getTag(), false);
                }
            }

            @Override // com.common.task.NetCallBack
            public NetResult onDoInBack(HashMap<String, String> hashMap) {
                NetResult netResult;
                Exception e;
                ArrayList<Mp3> mp3Files;
                try {
                    mp3Files = LikeMusicActivity.this.getMp3Files();
                    netResult = new NetResult();
                } catch (Exception e2) {
                    netResult = null;
                    e = e2;
                }
                try {
                    netResult.setTag(mp3Files);
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                    return netResult;
                }
                return netResult;
            }
        });
        this.baseTask = baseTask2;
        baseTask2.execute(new HashMap());
    }

    public ArrayList<Mp3> getMp3Files() {
        ArrayList<Mp3> arrayList = new ArrayList<>();
        Cursor query = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "title_key");
        while (query.moveToNext()) {
            Mp3 mp3 = new Mp3();
            mp3.setId(query.getInt(query.getColumnIndexOrThrow("_id")));
            mp3.setTitle(query.getString(query.getColumnIndexOrThrow(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE)));
            mp3.setAlbum(query.getString(query.getColumnIndexOrThrow(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM)));
            mp3.setArtist(query.getString(query.getColumnIndexOrThrow(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST)));
            String string = query.getString(query.getColumnIndexOrThrow("_data"));
            mp3.setUrl(string);
            mp3.setDuration(query.getInt(query.getColumnIndexOrThrow(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)));
            Long valueOf = Long.valueOf(query.getLong(query.getColumnIndexOrThrow("_size")));
            mp3.setSize(valueOf.longValue());
            if (valueOf.longValue() > 819200 && string.endsWith(PictureFileUtils.POST_AUDIO)) {
                arrayList.add(mp3);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        if (getApplicationContext() != null) {
            view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.layout_scale));
        }
    }

    public void initEvent() {
        this.imageViewPre.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.music.LikeMusicActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LikeMusicActivity.this.startAnimation(view);
                LikeMusicActivity.this.playPre();
            }
        });
        this.imageViewPlay.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.music.LikeMusicActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LikeMusicActivity.this.startAnimation(view);
                LikeMusicActivity.this.playPause();
            }
        });
        this.imageViewNext.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.music.LikeMusicActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LikeMusicActivity.this.startAnimation(view);
                LikeMusicActivity.this.palayNext();
            }
        });
        this.imageViewPlayType.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.music.LikeMusicActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!LikeMusicActivity.this.isLoopAll) {
                    if (!LikeMusicActivity.this.isLoopOne) {
                        if (LikeMusicActivity.this.isRandom) {
                            LikeMusicActivity.this.imageViewPlayType.setImageResource(R.drawable.like_music_random);
                            Toast.makeText(LikeMusicActivity.this.mActivity, (int) R.string.RandomPlay, 0).show();
                            LikeMusicActivity.this.isRandom = false;
                            LikeMusicActivity.this.isLoopAll = true;
                            return;
                        }
                        return;
                    }
                    LikeMusicActivity.this.imageViewPlayType.setImageResource(R.drawable.like_music_loopone);
                    Toast.makeText(LikeMusicActivity.this.mActivity, (int) R.string.SinglePlay, 0).show();
                    LikeMusicActivity.this.isLoopOne = false;
                    LikeMusicActivity.this.isRandom = true;
                    return;
                }
                LikeMusicActivity.this.imageViewPlayType.setImageResource(R.drawable.like_music_loopall);
                Toast.makeText(LikeMusicActivity.this.mActivity, (int) R.string.SequentialPlay, 0).show();
                LikeMusicActivity.this.isLoopAll = false;
                LikeMusicActivity.this.isLoopOne = true;
            }
        });
        this.buttonMusicLib.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.music.LikeMusicActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(LikeMusicActivity.this.mActivity, MusicLibActivity.class);
                intent.putExtra("sun", 200);
                LikeMusicActivity.this.startActivityForResult(intent, 100);
            }
        });
        this.seekBarMusic.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.activity.like.music.LikeMusicActivity.10
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (LikeMusicActivity.this.currentMp3 == null) {
                    Tool.ToastShow(LikeMusicActivity.this.mActivity, (int) R.string.chose_list);
                    seekBar.setProgress(0);
                } else if (ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
                } else {
                    int duration = LikeMusicActivity.this.currentMp3.getDuration();
                    LikeMusicActivity.this.mediaPlayer.seekTo((seekBar.getProgress() * duration) / 100);
                }
            }
        });
        this.buttonCancell.setOnClickListener(new MyOnClickListener());
        this.buttonBreathe.setOnClickListener(new MyOnClickListener());
        this.buttonFlash.setOnClickListener(new MyOnClickListener());
        this.buttonStrobe.setOnClickListener(new MyOnClickListener());
    }

    /* loaded from: classes.dex */
    public class MyOnClickListener implements View.OnClickListener {
        public MyOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LikeMusicActivity.this.startAnimation(view);
            switch (view.getId()) {
                case R.id.buttonBreathe /* 2131296428 */:
                    LikeMusicActivity.this.sendMusicMode(0);
                    return;
                case R.id.buttonCancell /* 2131296429 */:
                    LikeMusicActivity.this.finish();
                    return;
                case R.id.buttonFlash /* 2131296432 */:
                    LikeMusicActivity.this.sendMusicMode(1);
                    return;
                case R.id.buttonStrobe /* 2131296438 */:
                    LikeMusicActivity.this.sendMusicMode(2);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlayProgress(Mp3 mp3) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        int duration = mp3.getDuration();
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        if (duration == 0 || currentPosition >= duration) {
            return;
        }
        this.seekBarMusic.setProgress((currentPosition * 100) / duration);
    }

    public void palayNext() {
        if (this.currentMp3 == null) {
            if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
                startPlay(0);
            } else {
                Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
            }
        } else if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            int findCurrentIndex = findCurrentIndex(this.currentMp3);
            if (findCurrentIndex != -1) {
                if (findCurrentIndex < LedBleApplication.getApp().getMp3s().size() - 1) {
                    startPlay(findCurrentIndex + 1);
                    return;
                } else if (findCurrentIndex == LedBleApplication.getApp().getMp3s().size() - 1) {
                    startPlay(0);
                    return;
                } else {
                    return;
                }
            }
            startPlay(0);
        } else {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
        }
    }

    public void playPre() {
        if (this.currentMp3 == null) {
            if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
                startPlay(0);
            } else {
                Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
            }
        } else if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            int findCurrentIndex = findCurrentIndex(this.currentMp3);
            if (findCurrentIndex == -1) {
                startPlay(0);
            } else if (findCurrentIndex > 0) {
                startPlay(findCurrentIndex - 1);
            } else if (findCurrentIndex == 0) {
                startPlay(LedBleApplication.getApp().getMp3s().size() - 1);
            }
        } else {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
        }
    }

    public void pauseMusic() {
        MediaPlayer mediaPlayer;
        if (this.isSending && (mediaPlayer = this.mediaPlayer) != null && mediaPlayer.isPlaying()) {
            this.isMusic = false;
            if (this.isStartTimer) {
                this.isStartTimer = false;
                this.sendMusicThread = null;
            }
            this.mediaPlayer.pause();
        }
    }

    public void playPause() {
        if (ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
            return;
        }
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            startPlay(0);
        } else if (mediaPlayer.isPlaying()) {
            this.isSending = false;
            this.isMusic = false;
            this.mediaPlayer.pause();
            this.imageViewPlay.setImageResource(R.drawable.like_music_play);
        } else if (this.currentMp3 != null) {
            this.isStartTimer = true;
            this.isSending = true;
            this.isMusic = true;
            this.sendMusicThread = null;
            this.imageViewPlay.setImageResource(R.drawable.like_music_stop);
            this.mediaPlayer.start();
            sendMusicData();
            sendMusicMode(0);
            sendMu();
        } else if (!ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s())) {
            startPlay(0);
        } else {
            Tool.ToastShow(this.mActivity, (int) R.string.chose_list);
        }
    }

    public void playMp3(final Mp3 mp3) {
        this.currentMp3 = mp3;
        if (this.mediaPlayer == null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mediaPlayer = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
        }
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer.reset();
        try {
            this.mediaPlayer.setDataSource(mp3.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!this.isSettingBoolean) {
            this.isSettingBoolean = true;
            Visualizer visualizer = new Visualizer(this.mediaPlayer.getAudioSessionId());
            this.mVisualizer = visualizer;
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        }
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.home.activity.like.music.LikeMusicActivity.12
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer2) {
                try {
                    ArrayList<Mp3> mp3s2 = LedBleApplication.getApp().getMp3s();
                    int findCurrentIndex = LikeMusicActivity.this.findCurrentIndex(mp3);
                    if (LikeMusicActivity.this.isLoopOne || !LikeMusicActivity.this.isRandom) {
                        if (!LikeMusicActivity.this.isRandom && LikeMusicActivity.this.isLoopAll) {
                            LikeMusicActivity.this.palayNext();
                            return;
                        } else if (-1 == findCurrentIndex || ListUtiles.isEmpty(mp3s2)) {
                            return;
                        } else {
                            if (findCurrentIndex == mp3s2.size() - 1) {
                                LikeMusicActivity.this.playMp3(mp3s2.get(0));
                                return;
                            } else if (findCurrentIndex <= mp3s2.size() - 2) {
                                LikeMusicActivity.this.playMp3(mp3s2.get(findCurrentIndex + 1));
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                    LikeMusicActivity.this.mediaPlayer.start();
                    LikeMusicActivity.this.mediaPlayer.setLooping(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        try {
            this.mediaPlayer.prepare();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.home.activity.like.music.LikeMusicActivity$$ExternalSyntheticLambda0
            @Override // android.media.MediaPlayer.OnPreparedListener
            public final void onPrepared(MediaPlayer mediaPlayer2) {
                LikeMusicActivity.this.m13lambda$playMp3$0$comhomeactivitylikemusicLikeMusicActivity(mediaPlayer2);
            }
        });
        sendMusicMode(0);
        this.mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() { // from class: com.home.activity.like.music.LikeMusicActivity.13
            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onWaveFormDataCapture(Visualizer visualizer2, byte[] bArr, int i) {
                LikeMusicActivity.this.mVisualizerView.updateVisualizer(bArr);
            }

            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onFftDataCapture(Visualizer visualizer2, byte[] bArr, int i) {
                LikeMusicActivity.this.mVisualizerView.updateVisualizer(bArr);
            }
        }, Visualizer.getMaxCaptureRate() / 2, false, true);
        Visualizer visualizer2 = this.mVisualizer;
        if (visualizer2 != null) {
            visualizer2.setEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$playMp3$0$com-home-activity-like-music-LikeMusicActivity  reason: not valid java name */
    public /* synthetic */ void m13lambda$playMp3$0$comhomeactivitylikemusicLikeMusicActivity(MediaPlayer mediaPlayer) {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.start();
        }
    }

    public void sendMusicMode(int i) {
        this.mActivity.setMusicModel(i);
    }

    private void setAbulmImage(ImageView imageView, Mp3 mp3) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mp3.getUrl());
            byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
            if (embeddedPicture != null) {
                DrawTool.toRoundBitmap(BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMusicValue(int i) {
        MediaPlayer mediaPlayer;
        if (this.isSending && (mediaPlayer = this.mediaPlayer) != null && mediaPlayer.isPlaying()) {
            Log.e(TAG, "chnnel ======== " + i);
            this.mActivity.setLikeBrightness(i, false, true, true);
        }
    }

    private int getRandIntPlayIndex(int i) {
        return this.rand.nextInt(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findCurrentIndex(Mp3 mp3) {
        ArrayList<Mp3> mp3s2 = LedBleApplication.getApp().getMp3s();
        if (ListUtiles.isEmpty(mp3s2)) {
            return -1;
        }
        int size = mp3s2.size();
        for (int i = 0; i < size; i++) {
            if (mp3.equals(mp3s2.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPlay(int i) {
        int duration;
        String str;
        if (ListUtiles.isEmpty(LedBleApplication.getApp().getMp3s()) || LedBleApplication.getApp().getMp3s().size() <= i) {
            return;
        }
        try {
            sendMu();
            sendMusicData();
            playMp3(LedBleApplication.getApp().getMp3s().get(i));
            playMp3(LedBleApplication.getApp().getMp3s().get(i));
            this.mp3Adapter.setCurrentItem(i);
            this.mp3Adapter.notifyDataSetChanged();
            this.imageViewPlay.setImageResource(R.drawable.like_music_stop);
            if ((this.mediaPlayer.getDuration() / 1000) % 60 < 10) {
                str = NetResult.CODE_OK + (duration % 60);
            } else {
                str = "" + (duration % 60);
            }
            this.tvCurrentTime.setText("0:00");
            this.tvTotalTime.setText((duration / 60) + ":" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 100 && i2 == -1) {
            this.isMusic = true;
            this.isStartTimer = true;
            sendMusicMode(0);
            sendMusicData();
            startPlay(0);
            sendMu();
        }
    }

    public void sendMu() {
        new Thread(new Runnable() { // from class: com.home.activity.like.music.LikeMusicActivity.14
            @Override // java.lang.Runnable
            public void run() {
                do {
                    try {
                        if (!LikeMusicActivity.this.isMusic) {
                            return;
                        }
                        if (LikeMusicActivity.this.isSending && LikeMusicActivity.this.mediaPlayer != null && LikeMusicActivity.this.mediaPlayer.isPlaying()) {
                            LikeMusicActivity.this.mhanHandler.sendEmptyMessage(101);
                        }
                        Thread.sleep(300L);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } while (!LikeMusicActivity.this.mActivity.isFinishing());
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class VisualizerView extends View {
        private byte[] mBytes;
        private boolean mFirst;
        private float[] mPoints;
        private Rect mRect;
        private int mSpectrumNum;

        public VisualizerView(Context context) {
            super(context);
            this.mRect = new Rect();
            this.mSpectrumNum = 20;
            this.mFirst = true;
        }

        public void updateVisualizer(byte[] bArr) {
            byte[] bArr2 = new byte[(bArr.length / 2) + 1];
            bArr2[0] = (byte) Math.abs((int) bArr[0]);
            int i = 2;
            for (int i2 = 1; i2 < this.mSpectrumNum; i2++) {
                bArr2[i2] = (byte) Math.hypot(bArr[i], bArr[i + 1]);
                i += 2;
            }
            this.mBytes = bArr2;
            float[] fArr = this.mPoints;
            if (fArr == null || fArr.length < bArr2.length * 4) {
                this.mPoints = new float[bArr2.length * 4];
            }
            this.mRect.set(0, 0, getWidth(), getHeight());
            int width = this.mRect.width() / this.mSpectrumNum;
            int height = this.mRect.height();
            for (int i3 = 0; i3 < this.mSpectrumNum; i3++) {
                byte[] bArr3 = this.mBytes;
                if (bArr3[i3] < 0) {
                    bArr3[i3] = Byte.MAX_VALUE;
                }
                float[] fArr2 = this.mPoints;
                int i4 = i3 * 4;
                float f = (width * i3) + (width / 2);
                fArr2[i4] = f;
                fArr2[i4 + 1] = height;
                fArr2[i4 + 2] = f;
                int i5 = i4 + 3;
                fArr2[i5] = height - bArr3[i3];
                LikeMusicActivity.this.chnnelValue = (int) (1.0f - fArr2[i5]);
                LikeMusicActivity.this.chnnelValue *= LikeMusicActivity.this.chnnelValue;
                Log.e(LikeMusicActivity.TAG, "chnnelValue ======== " + LikeMusicActivity.this.chnnelValue);
                if (LikeMusicActivity.this.chnnelValue <= 1) {
                    LikeMusicActivity.this.chnnelValue = 1;
                }
                if (LikeMusicActivity.this.chnnelValue > 0 && LikeMusicActivity.this.isSending && LikeMusicActivity.this.mediaPlayer.isPlaying()) {
                    LikeMusicActivity likeMusicActivity = LikeMusicActivity.this;
                    likeMusicActivity.sendMusicValue(likeMusicActivity.chnnelValue);
                }
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        Visualizer visualizer = this.mVisualizer;
        if (visualizer != null) {
            visualizer.setEnabled(false);
            this.mVisualizer = null;
        }
    }
}
