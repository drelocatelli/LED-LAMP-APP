package com.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class TabButton extends LinearLayout {
    private int backgroundResId;
    private int imageResId;
    private boolean isSelect;
    ImageView ivTabImage;
    ImageView ivTabImage_above;
    LinearLayout llAbove;
    LinearLayout llBelow;
    private int selImageResId;
    private int selectedBackgroundResId;
    private String tabName;
    TextView tvCount;
    TextView tvTabName;
    TextView tvTabName_above;
    View view;

    public TabButton(Context context) {
        super(context);
    }

    public TabButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            return;
        }
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_tabbtn, this);
        this.view = inflate;
        this.ivTabImage = (ImageView) inflate.findViewById(R.id.ivTabImage);
        this.tvTabName = (TextView) this.view.findViewById(R.id.tvTabName);
        this.ivTabImage_above = (ImageView) this.view.findViewById(R.id.ivTabImage_above);
        this.tvTabName_above = (TextView) this.view.findViewById(R.id.tvTabName_above);
        this.llBelow = (LinearLayout) this.view.findViewById(R.id.llBelow);
        this.llAbove = (LinearLayout) this.view.findViewById(R.id.llAbove);
        this.tvCount = (TextView) this.view.findViewById(R.id.tvCount);
        ButterKnife.bind(this, this.view);
    }

    public TabButton init(int i, int i2, String str, boolean z, Context context) {
        this.imageResId = i;
        this.selImageResId = i2;
        this.tabName = str;
        this.isSelect = z;
        this.ivTabImage.setImageResource(i);
        this.tvTabName.setText(str);
        this.ivTabImage_above.setImageResource(i2);
        this.tvTabName_above.setText(str);
        this.tvTabName.setTextColor(context.getResources().getColor(R.color.tab_title_normal_color));
        this.tvTabName_above.setTextColor(context.getResources().getColor(R.color.tab_title_selected_color));
        if (z) {
            this.llAbove.setAlpha(1.0f);
            this.llBelow.setAlpha(0.0f);
        } else {
            this.llAbove.setAlpha(0.0f);
            this.llBelow.setAlpha(1.0f);
        }
        postInvalidate();
        return this;
    }

    public int getImageResId() {
        return this.imageResId;
    }

    public TabButton setImageResId(int i) {
        this.imageResId = i;
        return this;
    }

    public String getTabName() {
        return this.tabName;
    }

    public TabButton setTabName(String str) {
        this.tabName = str;
        return this;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
        if (z) {
            this.llAbove.setAlpha(1.0f);
            this.llBelow.setAlpha(0.0f);
        } else {
            this.llAbove.setAlpha(0.0f);
            this.llBelow.setAlpha(1.0f);
        }
        postInvalidate();
    }

    public int getBackgroundResId() {
        return this.backgroundResId;
    }

    public void setBackgroundResId(int i) {
        this.backgroundResId = i;
    }

    public int getSelectedBackgroundResId() {
        return this.selectedBackgroundResId;
    }

    public void setSelectedBackgroundResId(int i) {
        this.selectedBackgroundResId = i;
    }

    public int getSelImageResId() {
        return this.selImageResId;
    }

    public void setSelImageResId(int i) {
        this.selImageResId = i;
    }

    public ImageView getIvTabImage() {
        return this.ivTabImage;
    }

    public void setIvTabImage(ImageView imageView) {
        this.ivTabImage = imageView;
    }

    public TextView getTvTabName() {
        return this.tvTabName;
    }

    public void setTvTabName(TextView textView) {
        this.tvTabName = textView;
    }

    public LinearLayout getLlBelow() {
        return this.llBelow;
    }

    public void setLlBelow(LinearLayout linearLayout) {
        this.llBelow = linearLayout;
    }

    public LinearLayout getLlAbove() {
        return this.llAbove;
    }

    public void setLlAbove(LinearLayout linearLayout) {
        this.llAbove = linearLayout;
    }

    public TextView getTvCount() {
        return this.tvCount;
    }

    public void setTvCount(TextView textView) {
        this.tvCount = textView;
    }
}
