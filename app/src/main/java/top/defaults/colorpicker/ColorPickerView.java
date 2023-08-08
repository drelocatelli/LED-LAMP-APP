package top.defaults.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ColorPickerView extends LinearLayout implements ColorObservable {
    private AlphaSliderView alphaSliderView;
    private BrightnessSliderView brightnessSliderView;
    private ColorWheelView colorWheelView;
    private int initialColor;
    private ColorObservable observableOnDuty;
    List<ColorObserver> observers;
    private boolean onlyUpdateOnTouchEventUp;
    private int sliderHeight;
    private int sliderMargin;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.initialColor = ViewCompat.MEASURED_STATE_MASK;
        this.observers = new ArrayList();
        setOrientation(1);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ColorPickerView);
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.ColorPickerView_enableAlpha, false);
        boolean z2 = obtainStyledAttributes.getBoolean(R.styleable.ColorPickerView_enableBrightness, true);
        this.onlyUpdateOnTouchEventUp = obtainStyledAttributes.getBoolean(R.styleable.ColorPickerView_onlyUpdateOnTouchEventUp, false);
        obtainStyledAttributes.recycle();
        this.colorWheelView = new ColorWheelView(context);
        float f = getResources().getDisplayMetrics().density;
        int i2 = (int) (8.0f * f);
        this.sliderMargin = i2 * 2;
        this.sliderHeight = (int) (f * 24.0f);
        addView(this.colorWheelView, new LinearLayout.LayoutParams(-2, -2));
        setEnabledBrightness(z2);
        setEnabledAlpha(z);
        setPadding(i2, i2, i2, i2);
    }

    public void setOnlyUpdateOnTouchEventUp(boolean z) {
        this.onlyUpdateOnTouchEventUp = z;
        updateObservableOnDuty();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = (View.MeasureSpec.getSize(i2) - (getPaddingTop() + getPaddingBottom())) + getPaddingLeft() + getPaddingRight();
        if (this.brightnessSliderView != null) {
            size2 -= this.sliderMargin + this.sliderHeight;
        }
        if (this.alphaSliderView != null) {
            size2 -= this.sliderMargin + this.sliderHeight;
        }
        int min = Math.min(size, size2);
        int paddingLeft = (min - (getPaddingLeft() + getPaddingRight())) + getPaddingTop() + getPaddingBottom();
        if (this.brightnessSliderView != null) {
            paddingLeft += this.sliderMargin + this.sliderHeight;
        }
        if (this.alphaSliderView != null) {
            paddingLeft += this.sliderMargin + this.sliderHeight;
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(min, View.MeasureSpec.getMode(i)), View.MeasureSpec.makeMeasureSpec(paddingLeft, View.MeasureSpec.getMode(i2)));
    }

    public void setInitialColor(int i) {
        this.initialColor = i;
        this.colorWheelView.setColor(i, true);
    }

    public void setEnabledBrightness(boolean z) {
        if (z) {
            if (this.brightnessSliderView == null) {
                this.brightnessSliderView = new BrightnessSliderView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, this.sliderHeight);
                layoutParams.topMargin = this.sliderMargin;
                addView(this.brightnessSliderView, 1, layoutParams);
            }
            this.brightnessSliderView.bind(this.colorWheelView);
            updateObservableOnDuty();
        } else {
            BrightnessSliderView brightnessSliderView = this.brightnessSliderView;
            if (brightnessSliderView != null) {
                brightnessSliderView.unbind();
                removeView(this.brightnessSliderView);
                this.brightnessSliderView = null;
            }
            updateObservableOnDuty();
        }
        if (this.alphaSliderView != null) {
            setEnabledAlpha(true);
        }
    }

    public void setEnabledAlpha(boolean z) {
        if (z) {
            if (this.alphaSliderView == null) {
                this.alphaSliderView = new AlphaSliderView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, this.sliderHeight);
                layoutParams.topMargin = this.sliderMargin;
                addView(this.alphaSliderView, layoutParams);
            }
            ColorObservable colorObservable = this.brightnessSliderView;
            if (colorObservable == null) {
                colorObservable = this.colorWheelView;
            }
            this.alphaSliderView.bind(colorObservable);
            updateObservableOnDuty();
            return;
        }
        AlphaSliderView alphaSliderView = this.alphaSliderView;
        if (alphaSliderView != null) {
            alphaSliderView.unbind();
            removeView(this.alphaSliderView);
            this.alphaSliderView = null;
        }
        updateObservableOnDuty();
    }

    private void updateObservableOnDuty() {
        if (this.observableOnDuty != null) {
            for (ColorObserver colorObserver : this.observers) {
                this.observableOnDuty.unsubscribe(colorObserver);
            }
        }
        this.colorWheelView.setOnlyUpdateOnTouchEventUp(false);
        BrightnessSliderView brightnessSliderView = this.brightnessSliderView;
        if (brightnessSliderView != null) {
            brightnessSliderView.setOnlyUpdateOnTouchEventUp(false);
        }
        AlphaSliderView alphaSliderView = this.alphaSliderView;
        if (alphaSliderView != null) {
            alphaSliderView.setOnlyUpdateOnTouchEventUp(false);
        }
        BrightnessSliderView brightnessSliderView2 = this.brightnessSliderView;
        if (brightnessSliderView2 == null && this.alphaSliderView == null) {
            ColorWheelView colorWheelView = this.colorWheelView;
            this.observableOnDuty = colorWheelView;
            colorWheelView.setOnlyUpdateOnTouchEventUp(this.onlyUpdateOnTouchEventUp);
        } else {
            AlphaSliderView alphaSliderView2 = this.alphaSliderView;
            if (alphaSliderView2 != null) {
                this.observableOnDuty = alphaSliderView2;
                alphaSliderView2.setOnlyUpdateOnTouchEventUp(this.onlyUpdateOnTouchEventUp);
            } else {
                this.observableOnDuty = brightnessSliderView2;
                brightnessSliderView2.setOnlyUpdateOnTouchEventUp(this.onlyUpdateOnTouchEventUp);
            }
        }
        List<ColorObserver> list = this.observers;
        if (list != null) {
            for (ColorObserver colorObserver2 : list) {
                this.observableOnDuty.subscribe(colorObserver2);
                colorObserver2.onColor(this.observableOnDuty.getColor(), false, true);
            }
        }
    }

    public void reset() {
        this.colorWheelView.setColor(this.initialColor, true);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public void subscribe(ColorObserver colorObserver) {
        this.observableOnDuty.subscribe(colorObserver);
        this.observers.add(colorObserver);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public void unsubscribe(ColorObserver colorObserver) {
        this.observableOnDuty.unsubscribe(colorObserver);
        this.observers.remove(colorObserver);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public int getColor() {
        return this.observableOnDuty.getColor();
    }
}
