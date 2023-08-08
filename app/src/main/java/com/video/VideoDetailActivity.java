package com.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.common.view.videoplayer.view.SimpleVideoView;
import com.common.view.videoplayer.view.SimplerPlayerControllerLayout;
import com.githang.statusbar.StatusBarCompat;
import com.ledlamp.R;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class VideoDetailActivity extends AppCompatActivity {
    private SimplerPlayerControllerLayout controllerLayout;
    private boolean isBackClicked;
    private ImageView ivBack;
    private String title;
    private TextView titleTv;
    private String videoTitle;
    private String videoUrl;
    private SimpleVideoView videoView;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.black), true);
        requestWindowFeature(1);
        setContentView(R.layout.activity_videodetail);
        getDataFromIntent();
        this.videoView = (SimpleVideoView) findViewById(R.id.ijk_player);
        this.controllerLayout = (SimplerPlayerControllerLayout) findViewById(R.id.ijk_player_controller);
        this.titleTv = (TextView) findViewById(R.id.title_tv);
        this.videoView.setMediaController(this.controllerLayout.getMediaController());
        ImageView imageView = (ImageView) findViewById(R.id.ivBack);
        this.ivBack = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoDetailActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoDetailActivity.this.finish();
            }
        });
        init();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        this.videoUrl = intent.getStringExtra("videoUrl");
        this.videoTitle = intent.getStringExtra("videoTitle");
        this.title = intent.getStringExtra(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
        this.videoUrl = (String) getIntent().getSerializableExtra("videoPath");
        this.videoTitle = this.title;
    }

    private void init() {
        if (!TextUtils.isEmpty(this.videoTitle)) {
            this.titleTv.setText(this.videoTitle);
        }
        this.videoView.setVideoPath(this.videoUrl);
        this.videoView.start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        getWindow().clearFlags(128);
        if (this.isBackClicked) {
            return;
        }
        this.controllerLayout.pause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        getWindow().addFlags(128);
        this.controllerLayout.resume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        if (this.isBackClicked) {
            this.videoView.stopPlayback();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.controllerLayout.isFullScreen()) {
            setRequestedOrientation(1);
            return;
        }
        this.isBackClicked = true;
        finish();
    }
}
