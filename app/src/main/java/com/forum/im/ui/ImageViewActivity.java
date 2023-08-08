package com.forum.im.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.forum.im.fragment.ImageViewFragment;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ImageViewActivity extends FragmentActivity {
    private int currentPage;
    private TextView currentTv;
    private List<Fragment> fragList;
    private ArrayList<String> imageList;
    private ViewPager imageVp;
    private ImageView ivBack;
    private TextView totalTv;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("images")) {
                this.imageList = extras.getStringArrayList("images");
            }
            if (extras.containsKey("clickedIndex")) {
                this.currentPage = extras.getInt("clickedIndex");
            }
        }
        setContentView(R.layout.activity_images_view);
        findView();
        init();
    }

    private void init() {
        TextView textView = this.totalTv;
        textView.setText("/" + this.imageList.size());
        this.fragList = new ArrayList();
        for (int i = 0; i < this.imageList.size(); i++) {
            ImageViewFragment imageViewFragment = new ImageViewFragment();
            imageViewFragment.setImageUrl(this.imageList.get(i));
            this.fragList.add(imageViewFragment);
        }
        this.imageVp.setOffscreenPageLimit(this.imageList.size());
        this.imageVp.setAdapter(new ImageViewFPAdapter(getSupportFragmentManager()));
        this.imageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.forum.im.ui.ImageViewActivity.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                ImageViewActivity.this.currentPage = i2;
                TextView textView2 = ImageViewActivity.this.currentTv;
                textView2.setText((i2 + 1) + "");
                ((Fragment) ImageViewActivity.this.fragList.get(ImageViewActivity.this.currentPage)).onPause();
                if (((Fragment) ImageViewActivity.this.fragList.get(i2)).isAdded()) {
                    ((Fragment) ImageViewActivity.this.fragList.get(i2)).onResume();
                }
            }
        });
        this.imageVp.setCurrentItem(this.currentPage);
        TextView textView2 = this.currentTv;
        textView2.setText((this.currentPage + 1) + "");
    }

    protected void findView() {
        this.imageVp = (ViewPager) findViewById(R.id.images_vp);
        this.currentTv = (TextView) findViewById(R.id.imageView_current_tv);
        this.totalTv = (TextView) findViewById(R.id.imageView_total_tv);
        ImageView imageView = (ImageView) findViewById(R.id.ivBack);
        this.ivBack = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.ui.ImageViewActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ImageViewActivity.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ImageViewFPAdapter extends FragmentPagerAdapter {
        protected FragmentManager fm;

        public ImageViewFPAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.fm = fragmentManager;
        }

        @Override // androidx.fragment.app.FragmentPagerAdapter
        public Fragment getItem(int i) {
            return (Fragment) ImageViewActivity.this.fragList.get(i);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return ImageViewActivity.this.fragList.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        if (this.fragList.size() > 0) {
            for (Fragment fragment : this.fragList) {
                fragment.onDestroy();
            }
        }
        super.onDestroy();
    }
}
