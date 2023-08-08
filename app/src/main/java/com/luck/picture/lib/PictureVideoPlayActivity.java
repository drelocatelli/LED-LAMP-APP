package com.luck.picture.lib;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.core.view.ViewCompat;
import com.luck.picture.lib.config.PictureMimeType;

/* loaded from: classes.dex */
public class PictureVideoPlayActivity extends PictureBaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private ImageView iv_play;
    private MediaController mMediaController;
    private VideoView mVideoView;
    private ImageView picture_left_back;
    private String video_path = "";
    private int mPositionWhenPaused = -1;

    @Override // com.luck.picture.lib.PictureBaseActivity, android.app.Activity
    public boolean isImmersive() {
        return false;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        getWindow().addFlags(67108864);
        super.onCreate(bundle);
        setContentView(R.layout.picture_activity_video_play);
        this.video_path = getIntent().getStringExtra("video_path");
        this.picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        this.mVideoView = videoView;
        videoView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.iv_play = (ImageView) findViewById(R.id.iv_play);
        this.mMediaController = new MediaController(this);
        this.mVideoView.setOnCompletionListener(this);
        this.mVideoView.setOnPreparedListener(this);
        this.mVideoView.setMediaController(this.mMediaController);
        this.picture_left_back.setOnClickListener(this);
        this.iv_play.setOnClickListener(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        this.mVideoView.setVideoPath(this.video_path);
        this.mVideoView.start();
        super.onStart();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mPositionWhenPaused = this.mVideoView.getCurrentPosition();
        this.mVideoView.stopPlayback();
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.mMediaController = null;
        this.mVideoView = null;
        this.iv_play = null;
        super.onDestroy();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        int i = this.mPositionWhenPaused;
        if (i >= 0) {
            this.mVideoView.seekTo(i);
            this.mPositionWhenPaused = -1;
        }
        super.onResume();
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        ImageView imageView = this.iv_play;
        if (imageView != null) {
            imageView.setVisibility(0);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.picture_left_back) {
            finish();
        } else if (id == R.id.iv_play) {
            this.mVideoView.start();
            this.iv_play.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context context) {
        super.attachBaseContext(new ContextWrapper(context) { // from class: com.luck.picture.lib.PictureVideoPlayActivity.1
            @Override // android.content.ContextWrapper, android.content.Context
            public Object getSystemService(String str) {
                if (PictureMimeType.MIME_TYPE_PREFIX_AUDIO.equals(str)) {
                    return getApplicationContext().getSystemService(str);
                }
                return super.getSystemService(str);
            }
        });
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() { // from class: com.luck.picture.lib.PictureVideoPlayActivity$$ExternalSyntheticLambda0
            @Override // android.media.MediaPlayer.OnInfoListener
            public final boolean onInfo(MediaPlayer mediaPlayer2, int i, int i2) {
                return PictureVideoPlayActivity.this.m44x49f10168(mediaPlayer2, i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onPrepared$0$com-luck-picture-lib-PictureVideoPlayActivity  reason: not valid java name */
    public /* synthetic */ boolean m44x49f10168(MediaPlayer mediaPlayer, int i, int i2) {
        if (i == 3) {
            this.mVideoView.setBackgroundColor(0);
            return true;
        }
        return false;
    }
}
