package com.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.common.uitl.ScreenUtil;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SelectMultiCheckGroup extends LinearLayout {
    private List<CheckBox> checkBoxList;
    private int column;
    private boolean isSingleSelected;
    private int itemHorizontalPadding;
    private int itemHorizontalSpace;
    private int itemTextSize;
    private int itemVerticalPadding;
    private int itemVerticalSpace;
    private OnItemSelectedListener listener;
    private List<String> mData;
    private int mSelected;
    private List<Integer> mSelectedList;
    private List<RadioButton> radioButtonList;
    private int row;

    /* loaded from: classes.dex */
    public interface OnItemSelectedListener {
        void checked(View view, int i, boolean z);
    }

    public SelectMultiCheckGroup(Context context) {
        this(context, null);
    }

    public SelectMultiCheckGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SelectMultiCheckGroup(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
        initView();
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SelectMultiCheckGroup);
            this.isSingleSelected = obtainStyledAttributes.getBoolean(1, false);
            this.column = obtainStyledAttributes.getInteger(0, 1);
            this.row = obtainStyledAttributes.getInteger(7, 1);
            this.itemHorizontalSpace = obtainStyledAttributes.getDimensionPixelSize(3, getResources().getDimensionPixelSize(R.dimen.dp_5));
            this.itemVerticalSpace = obtainStyledAttributes.getDimensionPixelSize(6, getResources().getDimensionPixelSize(R.dimen.dp_5));
            this.itemHorizontalPadding = obtainStyledAttributes.getDimensionPixelSize(2, getResources().getDimensionPixelSize(R.dimen.dp_4));
            this.itemVerticalPadding = obtainStyledAttributes.getDimensionPixelSize(5, getResources().getDimensionPixelSize(R.dimen.dp_4));
            this.itemTextSize = obtainStyledAttributes.getDimensionPixelSize(4, 12);
            obtainStyledAttributes.recycle();
        } else {
            this.column = 1;
            this.row = 1;
            this.itemHorizontalSpace = ScreenUtil.dip2px(getContext(), 14.0f);
            this.itemVerticalSpace = ScreenUtil.dip2px(getContext(), 6.0f);
            this.itemHorizontalPadding = ScreenUtil.dip2px(getContext(), 8.0f);
            this.itemVerticalPadding = ScreenUtil.dip2px(getContext(), 6.0f);
            this.itemTextSize = 12;
        }
        if (this.isSingleSelected) {
            this.radioButtonList = new ArrayList();
            return;
        }
        this.mSelectedList = new ArrayList();
        this.checkBoxList = new ArrayList();
    }

    private void initView() {
        LinearLayout linearLayout;
        setOrientation(1);
        for (final int i = 0; i < this.row; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(0, this.itemVerticalSpace, this.itemHorizontalSpace, 0);
            RadioGroup radioGroup = null;
            if (this.isSingleSelected) {
                RadioGroup radioGroup2 = new RadioGroup(getContext());
                radioGroup2.setLayoutParams(layoutParams);
                radioGroup2.setOrientation(0);
                radioGroup = radioGroup2;
                linearLayout = null;
            } else {
                linearLayout = new LinearLayout(getContext());
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(0);
            }
            for (final int i2 = 0; i2 < this.column; i2++) {
                if (this.isSingleSelected) {
                    RadioGroup.LayoutParams layoutParams2 = new RadioGroup.LayoutParams(0, -2, 1.0f);
                    layoutParams2.leftMargin = this.itemHorizontalSpace;
                    layoutParams2.bottomMargin = 5;
                    RadioButton radioButton = new RadioButton(getContext());
                    radioButton.setLayoutParams(layoutParams2);
                    radioButton.setBackgroundResource(R.drawable.select_cb_selector);
                    radioButton.setTextColor(getResources().getColorStateList(R.color.cb_textcolor_selector));
                    radioButton.setButtonDrawable(17170445);
                    radioButton.setGravity(17);
                    int i3 = this.itemHorizontalPadding;
                    int i4 = this.itemVerticalPadding;
                    radioButton.setPadding(i3, i4, i3, i4);
                    radioButton.setTextSize(2, this.itemTextSize);
                    radioButton.setSingleLine(true);
                    radioButton.setEllipsize(TextUtils.TruncateAt.END);
                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.home.view.SelectMultiCheckGroup.1
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                            if (z) {
                                SelectMultiCheckGroup selectMultiCheckGroup = SelectMultiCheckGroup.this;
                                selectMultiCheckGroup.mSelected = (i * selectMultiCheckGroup.column) + i2;
                                for (int i5 = 0; i5 < SelectMultiCheckGroup.this.row; i5++) {
                                    if (((RadioButton) SelectMultiCheckGroup.this.radioButtonList.get(SelectMultiCheckGroup.this.mSelected)).getParent() != SelectMultiCheckGroup.this.getChildAt(i5)) {
                                        ((RadioGroup) SelectMultiCheckGroup.this.getChildAt(i5)).clearCheck();
                                    }
                                }
                                if (SelectMultiCheckGroup.this.listener != null) {
                                    SelectMultiCheckGroup.this.listener.checked(compoundButton, SelectMultiCheckGroup.this.mSelected, true);
                                }
                            }
                        }
                    });
                    if (radioGroup != null) {
                        radioGroup.addView(radioButton);
                        this.radioButtonList.add(radioButton);
                    }
                } else {
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, -2, 1.0f);
                    layoutParams3.leftMargin = this.itemHorizontalSpace;
                    layoutParams3.bottomMargin = 5;
                    CheckBox checkBox = new CheckBox(getContext());
                    checkBox.setLayoutParams(layoutParams3);
                    checkBox.setBackgroundResource(R.drawable.select_cb_selector);
                    checkBox.setTextColor(getResources().getColorStateList(R.color.cb_textcolor_selector));
                    checkBox.setButtonDrawable(17170445);
                    checkBox.setGravity(17);
                    int i5 = this.itemHorizontalPadding;
                    int i6 = this.itemVerticalPadding;
                    checkBox.setPadding(i5, i6, i5, i6);
                    checkBox.setTextSize(2, this.itemTextSize);
                    checkBox.setSingleLine(true);
                    checkBox.setEllipsize(TextUtils.TruncateAt.END);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.home.view.SelectMultiCheckGroup.2
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                            SelectMultiCheckGroup selectMultiCheckGroup = SelectMultiCheckGroup.this;
                            selectMultiCheckGroup.mSelected = (i * selectMultiCheckGroup.column) + i2;
                            if (z) {
                                if (!SelectMultiCheckGroup.this.mSelectedList.contains(Integer.valueOf(SelectMultiCheckGroup.this.mSelected))) {
                                    SelectMultiCheckGroup.this.mSelectedList.add(Integer.valueOf(SelectMultiCheckGroup.this.mSelected));
                                }
                            } else if (SelectMultiCheckGroup.this.mSelectedList.contains(Integer.valueOf(SelectMultiCheckGroup.this.mSelected))) {
                                SelectMultiCheckGroup.this.mSelectedList.remove(SelectMultiCheckGroup.this.mSelectedList.indexOf(Integer.valueOf(SelectMultiCheckGroup.this.mSelected)));
                            }
                            if (SelectMultiCheckGroup.this.listener != null) {
                                SelectMultiCheckGroup.this.listener.checked(compoundButton, SelectMultiCheckGroup.this.mSelected, z);
                            }
                        }
                    });
                    if (linearLayout != null) {
                        linearLayout.addView(checkBox);
                        this.checkBoxList.add(checkBox);
                    }
                }
            }
            if (this.isSingleSelected) {
                addView(radioGroup);
            } else {
                addView(linearLayout);
            }
        }
    }

    public void initData(List<String> list) {
        if (list != null && !list.isEmpty()) {
            if (list.size() <= (this.isSingleSelected ? this.radioButtonList : this.checkBoxList).size()) {
                ArrayList arrayList = new ArrayList();
                this.mData = arrayList;
                arrayList.addAll(list);
                setData();
                return;
            }
        }
        throw new IllegalArgumentException("setData() 参数不合法");
    }

    private void setData() {
        int i = 0;
        if (this.isSingleSelected) {
            if (this.mData.size() < this.radioButtonList.size()) {
                int size = this.mData.size() - 1;
                for (int size2 = this.radioButtonList.size() - 1; size2 > size; size2--) {
                    this.radioButtonList.get(size2).setVisibility(4);
                    this.radioButtonList.remove(size2);
                }
            }
            while (i < this.mData.size()) {
                this.radioButtonList.get(i).setText(this.mData.get(i));
                i++;
            }
            return;
        }
        if (this.mData.size() < this.checkBoxList.size()) {
            int size3 = this.mData.size() - 1;
            for (int size4 = this.checkBoxList.size() - 1; size4 > size3; size4--) {
                this.checkBoxList.get(size4).setVisibility(4);
                this.checkBoxList.remove(size4);
            }
        }
        while (i < this.mData.size()) {
            this.checkBoxList.get(i).setText(this.mData.get(i));
            i++;
        }
    }

    public int getSelectedOne() {
        if (this.isSingleSelected) {
            return this.mSelected;
        }
        throw new IllegalStateException("针对单选使用，app:isSingleSelected=\"true\"");
    }

    public List<Integer> getSelectedAll() {
        if (this.isSingleSelected) {
            throw new IllegalStateException("针对多选使用，app:isSingleSelected=\"false\"");
        }
        return this.mSelectedList;
    }

    public List<CheckBox> getSelectedAllView() {
        return this.checkBoxList;
    }

    public int getDataSize() {
        List<String> list = this.mData;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setSeleted(int i) {
        if (i < 0 || i >= this.mData.size()) {
            return;
        }
        this.mSelected = i;
        if (this.isSingleSelected) {
            this.radioButtonList.get(i).setChecked(true);
        } else {
            this.checkBoxList.get(i).setChecked(true);
        }
    }

    public void resetSelect() {
        if (!this.isSingleSelected) {
            for (int i = 1; i < this.checkBoxList.size(); i++) {
                this.checkBoxList.get(i).setChecked(false);
            }
        }
        setSeleted(0);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.listener = onItemSelectedListener;
    }
}
