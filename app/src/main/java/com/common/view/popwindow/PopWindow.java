package com.common.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class PopWindow extends PopupWindow {
    private static final int DEFAULT_BACKGROUND_RADIUS = 5;
    private static final int DEFAULT_DIVIDER_COLOR = -1694498817;
    private static final int DEFAULT_DIVIDER_WIDTH = 1;
    private static final int DEFAULT_NORMAL_BACKGROUND_COLOR = -872415232;
    private static final int DEFAULT_NORMAL_TEXT_COLOR = -1;
    private static final int DEFAULT_PRESSED_BACKGROUND_COLOR = -411601033;
    private static final int DEFAULT_PRESSED_TEXT_COLOR = -1;
    private static final int DEFAULT_TEXT_DRAWABLE_SIZE = 24;
    private static final int DEFAULT_TEXT_PADDING_BOTTOM = 6;
    private static final int DEFAULT_TEXT_PADDING_LEFT = 16;
    private static final int DEFAULT_TEXT_PADDING_RIGHT = 16;
    private static final int DEFAULT_TEXT_PADDING_TOP = 6;
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static PopWindow popupList;
    public Builder builder;
    private Context mContext;
    private GradientDrawable mCornerBackground;
    private StateListDrawable mCornerItemBackground;
    private StateListDrawable mLeftItemBackground;
    private int mRawX;
    private int mRawY;
    private StateListDrawable mRightItemBackground;
    private ColorStateList mTextColorStateList;

    /* loaded from: classes.dex */
    public interface OnPopuListItemClickListener {
        void onPopuListItemClick(View view, int i, int i2);
    }

    private PopWindow(Context context) {
        super(context);
        this.mContext = context;
        this.builder = new Builder();
    }

    public static synchronized PopWindow getInstance(Context context) {
        PopWindow popWindow;
        synchronized (PopWindow.class) {
            if (popupList == null) {
                popupList = new PopWindow(context);
            }
            popWindow = popupList;
        }
        return popWindow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Config {
        private int dividerColor;
        private int dividerWidth;
        private float indicatorViewHeight;
        private float indicatorViewWidth;
        private View mAnchorView;
        private int mIndicatorHeight;
        private View mIndicatorView;
        private int mIndicatorWidth;
        private OnPopuListItemClickListener mListener;
        private List<String> mPopupItemList;
        private PopupWindow mPopupWindow;
        private int mPopupWindowHeight;
        private int mPopupWindowWidth;
        private int normalBackgroundColor;
        private int normalTextColor;
        private int position;
        private int pressedBackgroundColor;
        private int pressedTextColor;
        private int radius;
        private List<Drawable> textDrawableList;
        private int textDrawableSize;
        private int textPaddingBottom;
        private int textPaddingLeft;
        private int textPaddingRight;
        private int textPaddingTop;
        private int textSize;
        private boolean visibility;

        private Config() {
            this.normalTextColor = -1;
            this.pressedTextColor = -1;
            this.textSize = 12;
            this.textPaddingLeft = PopWindow.this.dp2px(16);
            this.textPaddingTop = PopWindow.this.dp2px(6);
            this.textPaddingRight = PopWindow.this.dp2px(16);
            this.textPaddingBottom = PopWindow.this.dp2px(6);
            this.normalBackgroundColor = PopWindow.DEFAULT_NORMAL_BACKGROUND_COLOR;
            this.pressedBackgroundColor = PopWindow.DEFAULT_PRESSED_BACKGROUND_COLOR;
            this.radius = PopWindow.this.dp2px(5);
            this.dividerColor = PopWindow.DEFAULT_DIVIDER_COLOR;
            this.dividerWidth = PopWindow.this.dp2px(1);
            this.textDrawableSize = PopWindow.this.dp2px(24);
            this.indicatorViewWidth = PopWindow.this.dp2px(18);
            this.indicatorViewHeight = PopWindow.this.dp2px(9);
            this.mPopupWindow = null;
            this.visibility = true;
            this.mIndicatorView = PopWindow.this.getDefaultIndicatorView(PopWindow.this.mContext, this.normalBackgroundColor, this.indicatorViewWidth, this.indicatorViewHeight);
        }
    }

    /* loaded from: classes.dex */
    public class Builder {
        private Config config;

        private Builder() {
            this.config = new Config();
        }

        public Builder bindView(View view, int i) {
            this.config.position = i;
            this.config.mAnchorView = view;
            return PopWindow.this.builder;
        }

        public Builder setPopupItemList(String[] strArr) {
            if (strArr != null) {
                this.config.mPopupItemList = new ArrayList();
                this.config.mPopupItemList.clear();
                this.config.mPopupItemList.addAll(Arrays.asList(strArr));
            }
            return PopWindow.this.builder;
        }

        public Builder setPointers(int i, int i2) {
            PopWindow.this.mRawX = i;
            PopWindow.this.mRawY = i2;
            return PopWindow.this.builder;
        }

        public Builder setTextPadding(int i, int i2, int i3, int i4) {
            this.config.textPaddingLeft = i;
            this.config.textPaddingTop = i2;
            this.config.textPaddingRight = i3;
            this.config.textPaddingBottom = i4;
            return this;
        }

        public Builder setTextSize(int i) {
            this.config.textSize = i;
            return PopWindow.this.builder;
        }

        public Builder setRadius(int i) {
            this.config.radius = i;
            return PopWindow.this.builder;
        }

        public Builder setIndicatorViewSize(int i, int i2) {
            this.config.indicatorViewWidth = i;
            this.config.indicatorViewHeight = i2;
            Config config = this.config;
            PopWindow popWindow = PopWindow.this;
            config.mIndicatorView = popWindow.getDefaultIndicatorView(popWindow.mContext, this.config.normalBackgroundColor, this.config.indicatorViewWidth, this.config.indicatorViewHeight);
            return PopWindow.this.builder;
        }

        public Builder setPressedBackgroundColor(int i) {
            this.config.pressedBackgroundColor = i;
            return PopWindow.this.builder;
        }

        public Builder setNormalBackgroundColor(int i) {
            this.config.normalBackgroundColor = i;
            Config config = this.config;
            PopWindow popWindow = PopWindow.this;
            config.mIndicatorView = popWindow.getDefaultIndicatorView(popWindow.mContext, this.config.normalBackgroundColor, this.config.indicatorViewWidth, this.config.indicatorViewHeight);
            return PopWindow.this.builder;
        }

        public Builder setTextColor(int i) {
            this.config.normalTextColor = i;
            return PopWindow.this.builder;
        }

        public Builder setTextDrawableRes(Integer[] numArr) {
            if (numArr != null) {
                List asList = Arrays.asList(numArr);
                this.config.textDrawableList = new ArrayList();
                this.config.textDrawableList.clear();
                for (int i = 0; i < asList.size(); i++) {
                    this.config.textDrawableList.add(PopWindow.this.mContext.getResources().getDrawable(((Integer) asList.get(i)).intValue()));
                }
            }
            return PopWindow.this.builder;
        }

        public Builder setTextDrawableSize(int i) {
            this.config.textDrawableSize = i;
            return PopWindow.this.builder;
        }

        public Builder setOnPopuListItemClickListener(OnPopuListItemClickListener onPopuListItemClickListener) {
            this.config.mListener = onPopuListItemClickListener;
            return PopWindow.this.builder;
        }

        public Builder setDividerVisibility(boolean z) {
            this.config.visibility = z;
            return PopWindow.this.builder;
        }

        public void show() {
            checkAnchorView();
            if ((PopWindow.this.mContext instanceof Activity) && ((Activity) PopWindow.this.mContext).isFinishing()) {
                return;
            }
            if (this.config.mPopupWindow == null) {
                PopWindow.this.setPopupListBgAndRadius(this.config);
                PopWindow.this.setTextColorStateList(this.config);
                LinearLayout createContentView = PopWindow.this.createContentView();
                LinearLayout createContainerView = PopWindow.this.createContainerView();
                createContentView.addView(createContainerView);
                if (this.config.mIndicatorView != null) {
                    addIndicatorView(createContentView);
                }
                Objects.requireNonNull(this.config.mPopupItemList, "PopWindow item dataSources is null,please make sure (Builder)setPopupItemList() invoked");
                addPopuListItem(createContainerView);
                if (this.config.mPopupWindowWidth == 0) {
                    this.config.mPopupWindowWidth = PopWindow.this.getViewWidth(createContainerView);
                }
                if (this.config.mIndicatorView != null && this.config.mIndicatorWidth == 0) {
                    if (this.config.mIndicatorView.getLayoutParams().width > 0) {
                        Config config = this.config;
                        config.mIndicatorWidth = config.mIndicatorView.getLayoutParams().width;
                    } else {
                        Config config2 = this.config;
                        config2.mIndicatorWidth = PopWindow.this.getViewWidth(config2.mIndicatorView);
                    }
                }
                if (this.config.mIndicatorView != null && this.config.mIndicatorHeight == 0) {
                    if (this.config.mIndicatorView.getLayoutParams().height > 0) {
                        Config config3 = this.config;
                        config3.mIndicatorHeight = config3.mIndicatorView.getLayoutParams().height;
                    } else {
                        Config config4 = this.config;
                        config4.mIndicatorHeight = PopWindow.this.getViewHeight(config4.mIndicatorView);
                    }
                }
                if (this.config.mPopupWindowHeight == 0) {
                    this.config.mPopupWindowHeight = PopWindow.this.getViewHeight(createContainerView) + this.config.mIndicatorHeight;
                }
                this.config.mPopupWindow = new PopupWindow((View) createContentView, this.config.mPopupWindowWidth, this.config.mPopupWindowHeight, true);
                this.config.mPopupWindow.setTouchable(true);
                this.config.mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            }
            if (this.config.mIndicatorView != null) {
                setIndicatorLoaction();
            }
            if (!this.config.mPopupWindow.isShowing()) {
                PopupWindow popupWindow = this.config.mPopupWindow;
                View view = this.config.mAnchorView;
                int i = PopWindow.this.mRawX;
                PopWindow popWindow = PopWindow.this;
                int screenWidth = i - (popWindow.getScreenWidth(popWindow.mContext) / 2);
                int i2 = PopWindow.this.mRawY;
                PopWindow popWindow2 = PopWindow.this;
                popupWindow.showAtLocation(view, 17, screenWidth, (i2 - (popWindow2.getScreenHeight(popWindow2.mContext) / 2)) - this.config.mPopupWindowHeight);
            }
            this.config.mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.common.view.popwindow.PopWindow.Builder.1
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    PopWindow.this.release();
                }
            });
        }

        private void setIndicatorLoaction() {
            float f = PopWindow.this.mRawX;
            PopWindow popWindow = PopWindow.this;
            float screenWidth = popWindow.getScreenWidth(popWindow.mContext) - PopWindow.this.mRawX;
            if (f < this.config.mPopupWindowWidth / 2.0f) {
                if (f < (this.config.mIndicatorWidth / 2.0f) + this.config.radius) {
                    this.config.mIndicatorView.setTranslationX(((this.config.mIndicatorWidth / 2.0f) + this.config.radius) - (this.config.mPopupWindowWidth / 2.0f));
                } else {
                    this.config.mIndicatorView.setTranslationX(f - (this.config.mPopupWindowWidth / 2.0f));
                }
            } else if (screenWidth < this.config.mPopupWindowWidth / 2.0f) {
                if (screenWidth < (this.config.mIndicatorWidth / 2.0f) + this.config.radius) {
                    this.config.mIndicatorView.setTranslationX(((this.config.mPopupWindowWidth / 2.0f) - (this.config.mIndicatorWidth / 2.0f)) - this.config.radius);
                } else {
                    this.config.mIndicatorView.setTranslationX((this.config.mPopupWindowWidth / 2.0f) - screenWidth);
                }
            } else {
                this.config.mIndicatorView.setTranslationX(0.0f);
            }
        }

        private void addPopuListItem(LinearLayout linearLayout) {
            final int i = 0;
            while (i < this.config.mPopupItemList.size()) {
                TextView textView = new TextView(PopWindow.this.mContext);
                textView.setTextColor(PopWindow.this.mTextColorStateList);
                textView.setTextSize(2, this.config.textSize);
                textView.setPadding(this.config.textPaddingLeft, this.config.textPaddingTop, this.config.textPaddingRight, this.config.textPaddingBottom);
                textView.setClickable(true);
                textView.setGravity(17);
                textView.setText((CharSequence) this.config.mPopupItemList.get(i));
                if (this.config.textDrawableList != null && this.config.textDrawableList.size() > 0) {
                    Drawable drawable = this.config.textDrawableList.size() >= this.config.mPopupItemList.size() ? (Drawable) this.config.textDrawableList.get(i) : i < this.config.textDrawableList.size() ? (Drawable) this.config.textDrawableList.get(i) : (Drawable) this.config.textDrawableList.get(this.config.textDrawableList.size() - 1);
                    drawable.setBounds(0, 0, this.config.textDrawableSize, this.config.textDrawableSize);
                    textView.setCompoundDrawables(null, drawable, null, null);
                    textView.setCompoundDrawablePadding(2);
                }
                textView.setOnClickListener(new View.OnClickListener() { // from class: com.common.view.popwindow.PopWindow.Builder.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (Builder.this.config.mListener != null) {
                            Builder.this.config.mListener.onPopuListItemClick(Builder.this.config.mAnchorView, Builder.this.config.position, i);
                        }
                        PopWindow.this.hidePopupListWindow(Builder.this.config);
                    }
                });
                if (this.config.mPopupItemList.size() > 1 && i == 0) {
                    textView.setBackground(PopWindow.this.mLeftItemBackground);
                } else if (this.config.mPopupItemList.size() > 1 && i == this.config.mPopupItemList.size() - 1) {
                    textView.setBackground(PopWindow.this.mRightItemBackground);
                } else if (this.config.mPopupItemList.size() == 1) {
                    textView.setBackground(PopWindow.this.mCornerItemBackground);
                } else {
                    textView.setBackground(PopWindow.this.getCenterItemBackground(this.config));
                }
                linearLayout.addView(textView);
                if (this.config.visibility && this.config.mPopupItemList.size() > 1 && i != this.config.mPopupItemList.size() - 1) {
                    View view = new View(PopWindow.this.mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.config.dividerWidth, -1);
                    layoutParams.gravity = 17;
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(this.config.dividerColor);
                    linearLayout.addView(view);
                }
                i++;
            }
        }

        private void addIndicatorView(LinearLayout linearLayout) {
            LinearLayout.LayoutParams layoutParams;
            if (this.config.mIndicatorView.getLayoutParams() != null) {
                layoutParams = (LinearLayout.LayoutParams) this.config.mIndicatorView.getLayoutParams();
            } else {
                layoutParams = new LinearLayout.LayoutParams(-2, -2);
            }
            layoutParams.gravity = 17;
            this.config.mIndicatorView.setLayoutParams(layoutParams);
            ViewParent parent = this.config.mIndicatorView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.config.mIndicatorView);
            }
            linearLayout.addView(this.config.mIndicatorView);
        }

        private void checkAnchorView() {
            Objects.requireNonNull(this.config.mAnchorView, "PopWindow AnchorView is null,please make sure (Builder)bindView() invoked");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinearLayout createContainerView() {
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        linearLayout.setOrientation(0);
        linearLayout.setBackground(this.mCornerBackground);
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinearLayout createContentView() {
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        linearLayout.setOrientation(1);
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextColorStateList(Config config) {
        this.mTextColorStateList = new ColorStateList(new int[][]{new int[]{16842919}, new int[0]}, new int[]{config.pressedTextColor, config.normalTextColor});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPopupListBgAndRadius(Config config) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(config.pressedBackgroundColor);
        gradientDrawable.setCornerRadii(new float[]{config.radius, config.radius, 0.0f, 0.0f, 0.0f, 0.0f, config.radius, config.radius});
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setColor(0);
        gradientDrawable2.setCornerRadii(new float[]{config.radius, config.radius, 0.0f, 0.0f, 0.0f, 0.0f, config.radius, config.radius});
        StateListDrawable stateListDrawable = new StateListDrawable();
        this.mLeftItemBackground = stateListDrawable;
        stateListDrawable.addState(new int[]{16842919}, gradientDrawable);
        this.mLeftItemBackground.addState(new int[0], gradientDrawable2);
        GradientDrawable gradientDrawable3 = new GradientDrawable();
        gradientDrawable3.setColor(config.pressedBackgroundColor);
        gradientDrawable3.setCornerRadii(new float[]{0.0f, 0.0f, config.radius, config.radius, config.radius, config.radius, 0.0f, 0.0f});
        GradientDrawable gradientDrawable4 = new GradientDrawable();
        gradientDrawable4.setColor(0);
        gradientDrawable4.setCornerRadii(new float[]{0.0f, 0.0f, config.radius, config.radius, config.radius, config.radius, 0.0f, 0.0f});
        StateListDrawable stateListDrawable2 = new StateListDrawable();
        this.mRightItemBackground = stateListDrawable2;
        stateListDrawable2.addState(new int[]{16842919}, gradientDrawable3);
        this.mRightItemBackground.addState(new int[0], gradientDrawable4);
        GradientDrawable gradientDrawable5 = new GradientDrawable();
        gradientDrawable5.setColor(config.pressedBackgroundColor);
        gradientDrawable5.setCornerRadius(config.radius);
        GradientDrawable gradientDrawable6 = new GradientDrawable();
        gradientDrawable6.setColor(0);
        gradientDrawable6.setCornerRadius(config.radius);
        StateListDrawable stateListDrawable3 = new StateListDrawable();
        this.mCornerItemBackground = stateListDrawable3;
        stateListDrawable3.addState(new int[]{16842919}, gradientDrawable5);
        this.mCornerItemBackground.addState(new int[0], gradientDrawable6);
        GradientDrawable gradientDrawable7 = new GradientDrawable();
        this.mCornerBackground = gradientDrawable7;
        gradientDrawable7.setColor(config.normalBackgroundColor);
        this.mCornerBackground.setCornerRadius(config.radius);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getDefaultIndicatorView(Context context, final int i, final float f, final float f2) {
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(new Drawable() { // from class: com.common.view.popwindow.PopWindow.1
            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -3;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i2) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                Path path = new Path();
                Paint paint = new Paint();
                paint.setColor(i);
                paint.setStyle(Paint.Style.FILL);
                path.moveTo(0.0f, 0.0f);
                path.lineTo(f, 0.0f);
                path.lineTo(f / 2.0f, f2);
                path.close();
                canvas.drawPath(path, paint);
            }

            @Override // android.graphics.drawable.Drawable
            public int getIntrinsicWidth() {
                return (int) f;
            }

            @Override // android.graphics.drawable.Drawable
            public int getIntrinsicHeight() {
                return (int) f2;
            }
        });
        return imageView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hidePopupListWindow(Config config) {
        Context context = this.mContext;
        if (((context instanceof Activity) && ((Activity) context).isFinishing()) || config.mPopupWindow == null || !config.mPopupWindow.isShowing()) {
            return;
        }
        config.mPopupWindow.dismiss();
        config.mPopupWindow = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StateListDrawable getCenterItemBackground(Config config) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(config.pressedBackgroundColor);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setColor(0);
        stateListDrawable.addState(new int[]{16842919}, gradientDrawable);
        stateListDrawable.addState(new int[0], gradientDrawable2);
        return stateListDrawable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getViewWidth(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        return view.getMeasuredWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getViewHeight(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        return view.getMeasuredHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release() {
        if (this.mContext != null) {
            this.mContext = null;
        }
        if (popupList != null) {
            popupList = null;
        }
        if (this.builder != null) {
            this.builder = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int dp2px(int i) {
        return (int) TypedValue.applyDimension(1, i, this.mContext.getResources().getDisplayMetrics());
    }
}
