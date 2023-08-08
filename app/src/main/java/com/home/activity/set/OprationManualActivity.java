package com.home.activity.set;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.common.BaseActivity;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OprationManualActivity extends BaseActivity {
    private ViewPagerAdapter adapter;
    private List<View> dots;
    private List<ImageView> images;
    private LinearLayout ll_dot;
    TextView mTvVideoBean;
    private List<View> mViewList;
    private ViewPager mViewPaper;
    private TextView okBtn;
    private int oldPosition = 0;
    private int[] imageIds = {R.drawable.instruction_image_1, R.drawable.instruction_image_2};

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.common.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(1);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
            getWindow().setStatusBarColor(0);
        }
        initData();
        initView();
    }

    @Override // com.common.BaseActivity
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_opration_manual);
        this.mViewPaper = (ViewPager) findViewById(R.id.vp);
        this.mTvVideoBean = new TextView(getApplicationContext());
        inintent();
    }

    private void inintent() {
        this.images = new ArrayList();
        for (int i = 0; i < this.imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(this.imageIds[i]);
            this.images.add(imageView);
        }
        this.ll_dot = (LinearLayout) findViewById(R.id.ll_dot);
        ArrayList arrayList = new ArrayList();
        this.dots = arrayList;
        arrayList.add(findViewById(R.id.dot_0));
        this.dots.add(findViewById(R.id.dot_1));
        TextView textView = (TextView) findViewById(R.id.tv_ok);
        this.okBtn = textView;
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.OprationManualActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OprationManualActivity.this.finish();
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        this.adapter = viewPagerAdapter;
        this.mViewPaper.setAdapter(viewPagerAdapter);
        this.mViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.home.activity.set.OprationManualActivity.2
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                ((View) OprationManualActivity.this.dots.get(i2)).setBackgroundResource(R.drawable.dot_focused);
                ((View) OprationManualActivity.this.dots.get(OprationManualActivity.this.oldPosition)).setBackgroundResource(R.drawable.dot_normal);
                OprationManualActivity.this.oldPosition = i2;
                if (i2 == 1) {
                    OprationManualActivity.this.okBtn.setVisibility(0);
                    OprationManualActivity.this.ll_dot.setVisibility(8);
                    return;
                }
                OprationManualActivity.this.okBtn.setVisibility(8);
                OprationManualActivity.this.ll_dot.setVisibility(0);
            }
        });
    }

    private void initData() {
        this.mViewList = new ArrayList();
        getLayoutInflater();
        LayoutInflater from = LayoutInflater.from(this);
        View inflate = from.inflate(R.layout.we_indicator1, (ViewGroup) null);
        View inflate2 = from.inflate(R.layout.we_indicator2, (ViewGroup) null);
        this.mViewList.add(inflate);
        this.mViewList.add(inflate2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ViewPagerAdapter extends PagerAdapter {
        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        private ViewPagerAdapter() {
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return OprationManualActivity.this.mViewList.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) OprationManualActivity.this.mViewList.get(i));
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            viewGroup.addView((View) OprationManualActivity.this.mViewList.get(i));
            return OprationManualActivity.this.mViewList.get(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
    }
}
