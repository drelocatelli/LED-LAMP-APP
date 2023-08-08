package com.forum.im.adapter;

import android.view.View;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.List;

/* loaded from: classes.dex */
public class ExpressionPagerAdapter extends PagerAdapter {
    private List<View> views;

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public ExpressionPagerAdapter(List<View> list) {
        this.views = list;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.views.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(View view, int i) {
        ((ViewPager) view).addView(this.views.get(i));
        return this.views.get(i);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(View view, int i, Object obj) {
        ((ViewPager) view).removeView(this.views.get(i));
    }
}
