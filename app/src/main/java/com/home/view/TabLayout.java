package com.home.view;

import android.util.SparseArray;

/* loaded from: classes.dex */
public class TabLayout {
    public int backgroundResId;
    private int selectedBackgroundResId;
    private TabButton selectedButton;
    private SparseArray<TabButton> tabButtons = new SparseArray<>();

    /* loaded from: classes.dex */
    public interface OnSelectedListener {
        void onSelected();
    }

    public TabLayout(int i, int i2) {
        this.selectedBackgroundResId = i2;
        this.backgroundResId = i;
    }

    public TabLayout addBtn(TabButton... tabButtonArr) {
        for (TabButton tabButton : tabButtonArr) {
            tabButton.setBackgroundResId(this.backgroundResId);
            tabButton.setSelectedBackgroundResId(this.selectedBackgroundResId);
            this.tabButtons.put(tabButton.getId(), tabButton);
        }
        return this;
    }

    public TabLayout selectBtn(int i) {
        selectBtn(i, null);
        return this;
    }

    public void selectBtn(int i, OnSelectedListener onSelectedListener) {
        TabButton tabButton = this.tabButtons.get(i);
        this.selectedButton = tabButton;
        tabButton.setSelect(true);
        for (int i2 = 0; i2 < this.tabButtons.size(); i2++) {
            int keyAt = this.tabButtons.keyAt(i2);
            if (keyAt != i) {
                this.tabButtons.get(keyAt).setSelect(false);
            } else {
                this.tabButtons.get(keyAt).setSelect(true);
            }
        }
        if (onSelectedListener != null) {
            onSelectedListener.onSelected();
        }
    }

    public void selectBtnOnScrolled(int i, int i2, float f) {
        if (i == 0) {
            i2 = 1;
        } else if (i == 3) {
            i2 = 2;
        } else if (i2 == i) {
            i2 = i + 1;
        }
        TabButton valueAt = this.tabButtons.valueAt(i);
        TabButton valueAt2 = this.tabButtons.valueAt(i2);
        valueAt.getLlBelow().setAlpha(f);
        float f2 = 1.0f - f;
        valueAt.getLlAbove().setAlpha(f2);
        valueAt2.getLlBelow().setAlpha(f2);
        valueAt2.getLlAbove().setAlpha(f);
        valueAt.postInvalidate();
    }

    public TabButton getSelectedButton() {
        return this.selectedButton;
    }
}
