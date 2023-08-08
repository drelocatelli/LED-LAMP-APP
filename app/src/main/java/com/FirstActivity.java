package com;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.OnClick;
import com.common.uitl.Tool;
import com.forum.HomePageFragment;
import com.forum.ReleseCharacterActivity;
import com.forum.login.LogInActivity;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.other.HelpActivity;
import com.home.activity.service.AddSceneActivity;
import com.home.activity.set.OprationManualActivity;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.fragment.service.ServicesFragment;
import com.home.utils.ManageFragment;
import com.home.view.TabButton;
import com.home.view.TabLayout;
import com.ledlamp.R;
import com.me.MyPersonalFragment;
import com.me.SettingActivity;
import com.video.ReleseVideoActivity;
import com.video.VideoFragment;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FirstActivity extends LedBleActivity implements View.OnClickListener, MyPersonalFragment.ExitCallBack {
    public static final int ADD_SCENE_REQUEST_CODE = 1;
    public static final int RESULT111 = 111;
    public static final int RESULT222 = 222;
    public static final int RESULT333 = 333;
    public static final int RESULT444 = 444;
    private static final String TAG = "CommentActivity";
    public static HomePageFragment homeFragment;
    private static LinearLayout ll_tab_item;
    public static VideoFragment videoFragment;
    private FragmentManager fragmentManager;
    private ImageView imageViewAdd;
    private ImageView ivBack;
    private TextView ivTutorial;
    long mExitTime;
    private MyPersonalFragment myPersonalFragment;
    TabButton tabForum;
    TabButton tabHome;
    private TabLayout tabLayout;
    TabButton tabPersonal;
    TabButton tabVideo;
    private TextView tvTitle;
    private List<Fragment> fragmentList = new ArrayList();
    private int[] buttonId = {R.id.tabHome, R.id.tabForum, R.id.tabVideo, R.id.tabPersonal};
    private int currentIndex = 0;
    boolean bl = true;
    Handler handler1 = new Handler();
    Runnable runnable1 = new Runnable() { // from class: com.FirstActivity.1
        @Override // java.lang.Runnable
        public void run() {
            FirstActivity.this.myPersonalFragment.initData();
        }
    };
    Handler handler2 = new Handler();
    Runnable runnable2 = new Runnable() { // from class: com.FirstActivity.2
        @Override // java.lang.Runnable
        public void run() {
            FirstActivity.homeFragment.getContent();
        }
    };
    Handler handler3 = new Handler();
    Runnable runnable3 = new Runnable() { // from class: com.FirstActivity.3
        @Override // java.lang.Runnable
        public void run() {
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(1);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.APP_FIRST_OPEN, 0);
        if (Boolean.valueOf(sharedPreferences.getBoolean("FIRST", true)).booleanValue()) {
            startActivity(new Intent(getApplicationContext(), OprationManualActivity.class));
            sharedPreferences.edit().putBoolean("FIRST", false).commit();
        }
        setContentView(R.layout.activity_comment);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        getSwipeBackLayout().setEnableGesture(false);
        ImageView imageView = (ImageView) findViewById(R.id.ivBack);
        this.ivBack = imageView;
        imageView.setVisibility(8);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView textView = (TextView) findViewById(R.id.ivTutorial);
        this.ivTutorial = textView;
        textView.setOnClickListener(this);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageViewAdd);
        this.imageViewAdd = imageView2;
        imageView2.setOnClickListener(this);
        this.tabHome = (TabButton) findViewById(R.id.tabHome);
        this.tabForum = (TabButton) findViewById(R.id.tabForum);
        this.tabVideo = (TabButton) findViewById(R.id.tabVideo);
        this.tabPersonal = (TabButton) findViewById(R.id.tabPersonal);
        ll_tab_item = (LinearLayout) findViewById(R.id.ll_tab_item);
        this.tabLayout = new TabLayout(R.color.gray, R.color.gray).addBtn(this.tabHome, this.tabForum, this.tabVideo, this.tabPersonal);
        this.tabHome.init(R.drawable.tab_home_nor, R.drawable.tab_home, getString(R.string.home), false, this);
        this.tabForum.init(R.drawable.tab_forum_nor, R.drawable.tab_forum, getString(R.string.forum), false, this);
        this.tabVideo.init(R.drawable.tab_video_nor, R.drawable.tab_video, getString(R.string.video), false, this);
        this.tabPersonal.init(R.drawable.tab_mine_nor, R.drawable.tab_mine, getString(R.string.my_personal), false, this);
        this.tabHome.getTvTabName().setTextColor(getResources().getColor(R.color.tab_title_normal_color));
        this.tabForum.getTvTabName().setTextColor(getResources().getColor(R.color.tab_title_normal_color));
        this.tabVideo.getTvTabName().setTextColor(getResources().getColor(R.color.tab_title_normal_color));
        this.tabPersonal.getTvTabName().setTextColor(getResources().getColor(R.color.tab_title_normal_color));
        this.tabLayout.selectBtn(this.buttonId[this.currentIndex]);
        this.tabHome.setOnClickListener(this);
        this.tabForum.setOnClickListener(this);
        this.tabVideo.setOnClickListener(this);
        this.tabPersonal.setOnClickListener(this);
        initFragment();
    }

    public void initFragment() {
        ServicesFragment servicesFragment = new ServicesFragment();
        homeFragment = new HomePageFragment();
        videoFragment = new VideoFragment();
        this.myPersonalFragment = new MyPersonalFragment();
        this.fragmentList.add(servicesFragment);
        this.fragmentList.add(homeFragment);
        this.fragmentList.add(videoFragment);
        this.fragmentList.add(this.myPersonalFragment);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fragmentManager = supportFragmentManager;
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        for (int i = 0; i < this.fragmentList.size(); i++) {
            beginTransaction.add(R.id.flContent, this.fragmentList.get(i), this.fragmentList.get(i).getClass().getSimpleName());
        }
        beginTransaction.commitAllowingStateLoss();
        if (this.currentIndex == 0) {
            this.ivTutorial.setVisibility(0);
        }
        ManageFragment.showFragment(this.fragmentManager, this.fragmentList, this.currentIndex);
        this.myPersonalFragment.setExitCallBack(this);
    }

    @Override // android.view.View.OnClickListener
    @OnClick({R.id.tvOther, R.id.tvConnect, R.id.tvAdvisory, R.id.tvSuggest, R.id.tvHot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewAdd /* 2131296671 */:
                int i = this.currentIndex;
                if (i == 0) {
                    startActivityForResult(new Intent(getApplicationContext(), AddSceneActivity.class), 1);
                    return;
                } else if (i == 1) {
                    if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
                        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                        return;
                    } else {
                        startActivityForResult(new Intent(getApplicationContext(), ReleseCharacterActivity.class), 111);
                        return;
                    }
                } else if (i != 2) {
                    if (i == 3) {
                        startActivityForResult(new Intent(getApplicationContext(), SettingActivity.class), RESULT333);
                        return;
                    }
                    return;
                } else if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                    return;
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), ReleseVideoActivity.class), RESULT333);
                    return;
                }
            case R.id.ivTutorial /* 2131296785 */:
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                return;
            case R.id.tabForum /* 2131297534 */:
                this.currentIndex = 1;
                this.tvTitle.setVisibility(8);
                this.ivTutorial.setVisibility(8);
                ll_tab_item.setVisibility(0);
                showFragment();
                return;
            case R.id.tabHome /* 2131297535 */:
                this.currentIndex = 0;
                this.tvTitle.setVisibility(0);
                this.tvTitle.setText(getString(R.string.main_tab_text_service));
                this.ivTutorial.setVisibility(0);
                ll_tab_item.setVisibility(8);
                showFragment();
                return;
            case R.id.tabPersonal /* 2131297537 */:
                this.currentIndex = 3;
                this.tvTitle.setVisibility(0);
                this.tvTitle.setText(R.string.my_personal);
                this.ivTutorial.setVisibility(8);
                ll_tab_item.setVisibility(8);
                showFragment();
                return;
            case R.id.tabVideo /* 2131297538 */:
                this.currentIndex = 2;
                this.ivTutorial.setVisibility(0);
                if (this.bl) {
                    videoFragment.getVideoList(true);
                    this.bl = false;
                }
                this.tvTitle.setVisibility(0);
                this.tvTitle.setText(R.string.video);
                ll_tab_item.setVisibility(8);
                showFragment();
                return;
            default:
                return;
        }
    }

    private void showFragment() {
        if (this.currentIndex == 3) {
            this.imageViewAdd.setImageDrawable(getResources().getDrawable(R.drawable.tab_setting));
        } else {
            this.imageViewAdd.setImageDrawable(getResources().getDrawable(R.drawable.abcd));
        }
        if (this.currentIndex == 0) {
            this.ivTutorial.setText("");
        } else {
            this.ivTutorial.setText(getString(R.string.Tutorial));
        }
        ManageFragment.showFragment(this.fragmentManager, this.fragmentList, this.currentIndex);
        this.tabLayout.selectBtn(this.buttonId[this.currentIndex]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 111) {
            startActivityForResult(new Intent(this, LogInActivity.class), RESULT444);
        } else if (i2 == 222) {
            homeFragment.getContent();
        } else if (i2 == 333) {
            videoFragment.getVideoList(true);
        } else if (i2 != 444) {
        } else {
            this.bl = false;
            this.handler1.postDelayed(this.runnable1, 0L);
            this.handler2.postDelayed(this.runnable2, 500L);
            this.handler3.postDelayed(this.runnable3, 1000L);
        }
    }

    @Override // com.me.MyPersonalFragment.ExitCallBack
    public void exitLog() {
        this.bl = false;
        this.handler1.postDelayed(this.runnable1, 0L);
        this.handler2.postDelayed(this.runnable2, 500L);
        this.handler3.postDelayed(this.runnable3, 1000L);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (System.currentTimeMillis() - this.mExitTime > 2000) {
                Tool.ToastShow(this, getResources().getString(R.string.text_exit));
                this.mExitTime = System.currentTimeMillis();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public static LinearLayout getForumTopTabItem() {
        return ll_tab_item;
    }
}
