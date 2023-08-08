package com.home.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class ActionSheet extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private static final int ALPHA_DURATION = 300;
    private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
    private static final String ARG_CANCEL_ITEM = "cancel_item";
    private static final String ARG_CANCEL_MARGIN_TOP = "cancel_margintop";
    private static final String ARG_ICON_MARGIN_LEFT = "item_icon_margin_left";
    private static final String ARG_OTHER_ITEM = "other_item";
    private static final String ARG_OTHER_ITEM_SPACING = "other_item_spacing";
    private static final String ARG_TEXT_SIZE = "100";
    private static final int BG_VIEW_ID = 10;
    private static final int CANCEL_BUTTON_ID = 100;
    private static final int TRANSLATE_DURATION = 200;
    private static String groupName;
    private View mBg;
    private ViewGroup mGroup;
    private ActionSheetListener mListener;
    private LinearLayout mPanel;
    private View mView;
    private boolean mDismissed = true;
    private boolean isCancel = true;

    /* loaded from: classes.dex */
    public interface ActionSheetListener {
        void onDismiss(ActionSheet actionSheet, boolean z);

        void onOtherButtonClick(ActionSheet actionSheet, int i, String str);
    }

    public void show(FragmentManager fragmentManager, String str) {
        if (this.mDismissed) {
            this.mDismissed = false;
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(this, str);
            beginTransaction.addToBackStack(null);
            beginTransaction.commit();
        }
    }

    public void dismiss() {
        if (this.mDismissed) {
            return;
        }
        this.mDismissed = true;
        getFragmentManager().popBackStack();
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.remove(this);
        beginTransaction.commit();
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View currentFocus;
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService("input_method");
        if (inputMethodManager.isActive() && (currentFocus = getActivity().getCurrentFocus()) != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        this.mView = createView();
        this.mGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        createItems();
        this.mGroup.addView(this.mView);
        this.mBg.startAnimation(createAlphaInAnimation());
        this.mPanel.startAnimation(createTranslationInAnimation());
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    private Animation createTranslationInAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        translateAnimation.setDuration(200L);
        return translateAnimation;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300L);
        return alphaAnimation;
    }

    private Animation createTranslationOutAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        translateAnimation.setDuration(200L);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300L);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    private View createView() {
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        View view = new View(getActivity());
        this.mBg = view;
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
        this.mBg.setId(10);
        this.mBg.setOnClickListener(this);
        this.mPanel = new LinearLayout(getActivity());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        layoutParams.gravity = 80;
        this.mPanel.setLayoutParams(layoutParams);
        this.mPanel.setOrientation(1);
        frameLayout.addView(this.mBg);
        frameLayout.addView(this.mPanel);
        return frameLayout;
    }

    private void createItems() {
        Item[] otherItem = getOtherItem();
        Resources resources = getActivity().getResources();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        if (otherItem != null) {
            for (int i2 = 0; i2 < otherItem.length; i2++) {
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setId(i2 + 100 + 1);
                linearLayout.setOnClickListener(this);
                linearLayout.setOnTouchListener(this);
                linearLayout.setBackground(getOtherButtonBg(otherItem, i2));
                ((GradientDrawable) linearLayout.getBackground()).setColor(resources.getColor(otherItem[i2].getItemBackgroundNormal()));
                linearLayout.setOrientation(0);
                linearLayout.setAlpha(otherItem[i2].getItemBackgroundAlpha());
                linearLayout.setAlpha(otherItem[i2].getItemBackgroundAlpha());
                TextView textView = new TextView(getActivity());
                textView.setText(otherItem[i2].getItemText());
                textView.setTextSize(0, 50.0f);
                textView.setTextColor(resources.getColor(otherItem[i2].getItemTextColorNormal()));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
                layoutParams.setMargins(0, 40, 0, 40);
                textView.setLayoutParams(layoutParams);
                textView.setGravity(17);
                linearLayout.addView(textView);
                if (i2 > 0) {
                    LinearLayout.LayoutParams createButtonLayoutParams = createButtonLayoutParams();
                    createButtonLayoutParams.topMargin = getOtherItemSpacing();
                    this.mPanel.addView(linearLayout, createButtonLayoutParams);
                } else {
                    this.mPanel.addView(linearLayout);
                }
            }
        }
        Button button = new Button(getActivity());
        button.setTextSize(0, 50.0f);
        button.setId(100);
        button.setBackground(resources.getDrawable(R.drawable.actionsheet_cancel_border));
        ((GradientDrawable) button.getBackground()).setColor(resources.getColor(getCancelItem().getItemBackgroundNormal()));
        button.setAlpha(getCancelItem().getItemBackgroundAlpha());
        button.setText(getCancelItem().getItemText());
        button.setTextColor(resources.getColor(getCancelItem().getItemTextColorNormal()));
        button.setOnClickListener(this);
        button.setOnTouchListener(this);
        LinearLayout.LayoutParams createButtonLayoutParams2 = createButtonLayoutParams();
        createButtonLayoutParams2.topMargin = getCancelButtonMarginTop();
        this.mPanel.addView(button, createButtonLayoutParams2);
        this.mPanel.setBackgroundColor(0);
        this.mPanel.setPadding(20, 20, 20, 20);
    }

    public LinearLayout.LayoutParams createButtonLayoutParams() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    private Drawable getOtherButtonBg(Item[] itemArr, int i) {
        Resources resources = getActivity().getResources();
        if (itemArr.length == 1) {
            return resources.getDrawable(R.drawable.actionsheet_single_border);
        }
        if (itemArr.length == 2) {
            if (i == 0) {
                return resources.getDrawable(R.drawable.actionsheet_top_border);
            }
            if (i == 1) {
                return resources.getDrawable(R.drawable.actionsheet_bottom_border);
            }
        }
        if (itemArr.length > 2) {
            if (i == 0) {
                return resources.getDrawable(R.drawable.actionsheet_top_border);
            }
            if (i == itemArr.length - 1) {
                return resources.getDrawable(R.drawable.actionsheet_bottom_border);
            }
            return resources.getDrawable(R.drawable.actionsheet_middle_border);
        }
        return null;
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        this.mPanel.startAnimation(createTranslationOutAnimation());
        this.mBg.startAnimation(createAlphaOutAnimation());
        this.mView.postDelayed(new Runnable() { // from class: com.home.view.ActionSheet.1
            @Override // java.lang.Runnable
            public void run() {
                ActionSheet.this.mGroup.removeView(ActionSheet.this.mView);
            }
        }, 300L);
        ActionSheetListener actionSheetListener = this.mListener;
        if (actionSheetListener != null) {
            actionSheetListener.onDismiss(this, this.isCancel);
        }
        super.onDestroyView();
    }

    private Item getCancelItem() {
        return (Item) getArguments().getParcelable(ARG_CANCEL_ITEM);
    }

    private Item[] getOtherItem() {
        return (Item[]) getArguments().getParcelableArray(ARG_OTHER_ITEM);
    }

    private int getTextSize() {
        return getArguments().getInt(ARG_TEXT_SIZE);
    }

    private int getCancelButtonMarginTop() {
        return getArguments().getInt(ARG_CANCEL_MARGIN_TOP);
    }

    private int getOtherItemSpacing() {
        return getArguments().getInt(ARG_OTHER_ITEM_SPACING);
    }

    private int getIconMarginLeft() {
        return getArguments().getInt(ARG_ICON_MARGIN_LEFT);
    }

    private boolean getCancelableOnTouchOutside() {
        return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
    }

    public void setActionSheetListener(ActionSheetListener actionSheetListener) {
        this.mListener = actionSheetListener;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != 10 || getCancelableOnTouchOutside()) {
            dismiss();
            if (view.getId() == 100 || view.getId() == 10) {
                return;
            }
            ActionSheetListener actionSheetListener = this.mListener;
            if (actionSheetListener != null) {
                actionSheetListener.onOtherButtonClick(this, (view.getId() - 100) - 1, groupName);
            }
            this.isCancel = false;
        }
    }

    public static Builder createBuilder(Context context, FragmentManager fragmentManager) {
        return new Builder(context, fragmentManager);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private Item mCancelItem;
        private boolean mCancelableOnTouchOutside;
        private Context mContext;
        private FragmentManager mFragmentManager;
        private ActionSheetListener mListener;
        private Item[] mOtherItems;
        private String mTag = "actionSheet";
        private int mTextSize = 30;
        private int mIconMarginLeft = 3;
        private int mCancelButtonMarginTop = 20;
        private int mOtherItemSpacing = 5;

        public Builder(Context context, FragmentManager fragmentManager) {
            this.mContext = context;
            this.mFragmentManager = fragmentManager;
        }

        public Builder setCancelItem(Item item) {
            this.mCancelItem = item;
            return this;
        }

        public Builder setmOtherItems(Item... itemArr) {
            this.mOtherItems = itemArr;
            return this;
        }

        public Builder setGroupName(String str) {
            String unused = ActionSheet.groupName = str;
            return this;
        }

        public Builder setmIconMarginLeft(int i) {
            this.mIconMarginLeft = i;
            return this;
        }

        public Builder setmTextSize(int i) {
            this.mTextSize = (int) TypedValue.applyDimension(1, i, this.mContext.getResources().getDisplayMetrics());
            return this;
        }

        public Builder setmCancelButtonMarginTop(int i) {
            this.mCancelButtonMarginTop = i;
            return this;
        }

        public Builder setmOtherItemSpacing(int i) {
            this.mOtherItemSpacing = i;
            return this;
        }

        public Builder setTag(String str) {
            this.mTag = str;
            return this;
        }

        public Builder setListener(ActionSheetListener actionSheetListener) {
            this.mListener = actionSheetListener;
            return this;
        }

        public Builder setCancelableOnTouchOutside(boolean z) {
            this.mCancelableOnTouchOutside = z;
            return this;
        }

        public Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ActionSheet.ARG_CANCEL_ITEM, this.mCancelItem);
            bundle.putParcelableArray(ActionSheet.ARG_OTHER_ITEM, this.mOtherItems);
            bundle.putBoolean(ActionSheet.ARG_CANCELABLE_ONTOUCHOUTSIDE, this.mCancelableOnTouchOutside);
            bundle.putInt(ActionSheet.ARG_TEXT_SIZE, this.mTextSize);
            bundle.putInt(ActionSheet.ARG_CANCEL_MARGIN_TOP, this.mCancelButtonMarginTop);
            bundle.putInt(ActionSheet.ARG_OTHER_ITEM_SPACING, this.mOtherItemSpacing);
            bundle.putInt(ActionSheet.ARG_ICON_MARGIN_LEFT, this.mIconMarginLeft);
            return bundle;
        }

        public ActionSheet show() {
            ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(this.mContext, ActionSheet.class.getName(), prepareArguments());
            actionSheet.setActionSheetListener(this.mListener);
            actionSheet.show(this.mFragmentManager, this.mTag);
            return actionSheet;
        }
    }

    /* loaded from: classes.dex */
    public static class Item implements Parcelable {
        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() { // from class: com.home.view.ActionSheet.Item.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Item createFromParcel(Parcel parcel) {
                return new Item(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Item[] newArray(int i) {
                return new Item[i];
            }
        };
        private float itemBackgroundAlpha;
        private int itemBackgroundNormal;
        private int itemBackgroundPressed;
        private String itemText;
        private int itemTextColorNormal;
        private int itemTextColorPressed;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        protected Item(Parcel parcel) {
            this.itemBackgroundAlpha = 0.7f;
            this.itemBackgroundNormal = parcel.readInt();
            this.itemBackgroundPressed = parcel.readInt();
            this.itemTextColorNormal = parcel.readInt();
            this.itemTextColorPressed = parcel.readInt();
            this.itemBackgroundAlpha = parcel.readFloat();
            this.itemText = parcel.readString();
        }

        public float getItemBackgroundAlpha() {
            return this.itemBackgroundAlpha;
        }

        public void setItemBackgroundAlpha(float f) {
            this.itemBackgroundAlpha = f;
        }

        public int getItemBackgroundNormal() {
            return this.itemBackgroundNormal;
        }

        public int getItemBackgroundPressed() {
            return this.itemBackgroundPressed;
        }

        public int getItemTextColorNormal() {
            return this.itemTextColorNormal;
        }

        public int getItemTextColorPressed() {
            return this.itemTextColorPressed;
        }

        public String getItemText() {
            return this.itemText;
        }

        public Item() {
            this.itemBackgroundAlpha = 0.7f;
        }

        public Item(int i, int i2, int i3, int i4, int i5, int i6, String str) {
            this.itemBackgroundAlpha = 0.7f;
            this.itemText = str;
            this.itemTextColorNormal = i5;
            this.itemTextColorPressed = i6;
            this.itemBackgroundNormal = i;
            this.itemBackgroundPressed = i2;
        }

        public Item(int i, int i2, int i3, int i4, int i5, int i6, String str, float f) {
            this.itemText = str;
            this.itemTextColorNormal = i5;
            this.itemTextColorPressed = i6;
            this.itemBackgroundNormal = i;
            this.itemBackgroundPressed = i2;
            this.itemBackgroundAlpha = f;
        }

        public void setItemBackgroundNormal(int i) {
            this.itemBackgroundNormal = i;
        }

        public void setItemBackgroundPressed(int i) {
            this.itemBackgroundPressed = i;
        }

        public void setItemTextColorNormal(int i) {
            this.itemTextColorNormal = i;
        }

        public void setItemTextColorPressed(int i) {
            this.itemTextColorPressed = i;
        }

        public void setItemText(String str) {
            this.itemText = str;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.itemBackgroundNormal);
            parcel.writeInt(this.itemBackgroundPressed);
            parcel.writeInt(this.itemTextColorNormal);
            parcel.writeInt(this.itemTextColorPressed);
            parcel.writeString(this.itemText);
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        View childAt;
        View childAt2;
        if (getActivity() != null) {
            if (view.getId() != 100) {
                Resources resources = getActivity().getResources();
                LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(view.getId());
                TextView textView = null;
                if (linearLayout.getChildCount() == 1) {
                    childAt = linearLayout.getChildAt(0);
                    childAt2 = null;
                } else {
                    childAt = linearLayout.getChildAt(0);
                    childAt2 = linearLayout.getChildAt(1);
                }
                if (childAt != null) {
                    if (childAt instanceof ImageView) {
                        ImageView imageView = (ImageView) childAt;
                    } else {
                        textView = (TextView) childAt;
                    }
                }
                if (childAt2 != null) {
                    if (childAt2 instanceof TextView) {
                        textView = (TextView) childAt2;
                    } else {
                        ImageView imageView2 = (ImageView) childAt2;
                    }
                }
                Item[] otherItem = getOtherItem();
                int id = (view.getId() - 100) - 1;
                int action = motionEvent.getAction();
                if (action == 0) {
                    if (textView != null && otherItem[id].getItemTextColorPressed() != 0) {
                        textView.setTextColor(getResources().getColor(otherItem[id].getItemTextColorPressed()));
                    }
                    if (otherItem[id].getItemBackgroundPressed() != 0) {
                        linearLayout.setBackground(getOtherButtonBg(otherItem, id));
                        ((GradientDrawable) linearLayout.getBackground()).setColor(resources.getColor(otherItem[id].getItemBackgroundPressed()));
                    }
                } else if (action == 1) {
                    if (textView != null && otherItem[id].getItemTextColorNormal() != 0) {
                        textView.setTextColor(getResources().getColor(otherItem[id].getItemTextColorNormal()));
                    }
                    if (otherItem[id].getItemBackgroundNormal() != 0) {
                        linearLayout.setBackground(getOtherButtonBg(otherItem, id));
                        ((GradientDrawable) linearLayout.getBackground()).setColor(resources.getColor(otherItem[id].getItemBackgroundNormal()));
                    }
                } else if (action == 2 && (motionEvent.getY() < 0.0f || motionEvent.getY() > linearLayout.getHeight() || motionEvent.getX() < 0.0f || motionEvent.getX() > linearLayout.getWidth())) {
                    if (textView != null && otherItem[id].getItemTextColorNormal() != 0) {
                        textView.setTextColor(getResources().getColor(otherItem[id].getItemTextColorNormal()));
                    }
                    if (otherItem[id].getItemBackgroundNormal() != 0) {
                        linearLayout.setBackground(getOtherButtonBg(otherItem, id));
                        ((GradientDrawable) linearLayout.getBackground()).setColor(resources.getColor(otherItem[id].getItemBackgroundNormal()));
                    }
                }
            } else {
                Button button = (Button) getActivity().findViewById(100);
                Item cancelItem = getCancelItem();
                Resources resources2 = getActivity().getResources();
                int action2 = motionEvent.getAction();
                if (action2 == 0) {
                    if (cancelItem.getItemBackgroundPressed() != 0) {
                        button.setBackground(resources2.getDrawable(R.drawable.actionsheet_cancel_border));
                        ((GradientDrawable) button.getBackground()).setColor(resources2.getColor(getCancelItem().getItemBackgroundPressed()));
                    }
                    if (cancelItem.getItemTextColorPressed() != 0) {
                        button.setTextColor(resources2.getColor(cancelItem.getItemTextColorPressed()));
                    }
                } else if (action2 == 1) {
                    if (cancelItem.getItemBackgroundNormal() != 0) {
                        button.setBackground(resources2.getDrawable(R.drawable.actionsheet_cancel_border));
                        ((GradientDrawable) button.getBackground()).setColor(resources2.getColor(getCancelItem().getItemBackgroundNormal()));
                    }
                    if (cancelItem.getItemTextColorNormal() != 0) {
                        button.setTextColor(resources2.getColor(cancelItem.getItemTextColorNormal()));
                    }
                } else if (action2 == 2 && (motionEvent.getY() < 0.0f || motionEvent.getY() > button.getHeight() || motionEvent.getX() < 0.0f || motionEvent.getX() > button.getWidth())) {
                    if (cancelItem.getItemBackgroundNormal() != 0) {
                        button.setBackground(resources2.getDrawable(R.drawable.actionsheet_cancel_border));
                        ((GradientDrawable) button.getBackground()).setColor(resources2.getColor(getCancelItem().getItemBackgroundNormal()));
                    }
                    if (cancelItem.getItemTextColorNormal() != 0) {
                        button.setTextColor(resources2.getColor(cancelItem.getItemTextColorNormal()));
                    }
                }
            }
        }
        return false;
    }
}
