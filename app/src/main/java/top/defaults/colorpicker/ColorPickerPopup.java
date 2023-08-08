package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.Locale;

/* loaded from: classes.dex */
public class ColorPickerPopup {
    private String cancelTitle;
    private Context context;
    private boolean enableAlpha;
    private boolean enableBrightness;
    private int initialColor;
    private String okTitle;
    private boolean onlyUpdateOnTouchEventUp;
    private PopupWindow popupWindow;
    private boolean showIndicator;
    private boolean showValue;

    /* loaded from: classes.dex */
    public static abstract class ColorPickerObserver implements ColorObserver {
        @Override // top.defaults.colorpicker.ColorObserver
        public final void onColor(int i, boolean z, boolean z2) {
        }

        public abstract void onColorPicked(int i);
    }

    private ColorPickerPopup(Builder builder) {
        this.context = builder.context;
        this.initialColor = builder.initialColor;
        this.enableBrightness = builder.enableBrightness;
        this.enableAlpha = builder.enableAlpha;
        this.okTitle = builder.okTitle;
        this.cancelTitle = builder.cancelTitle;
        this.showIndicator = builder.showIndicator;
        this.showValue = builder.showValue;
        this.onlyUpdateOnTouchEventUp = builder.onlyUpdateOnTouchEventUp;
    }

    public void show(ColorPickerObserver colorPickerObserver) {
        show(null, colorPickerObserver);
    }

    public void show(View view, final ColorPickerObserver colorPickerObserver) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        if (layoutInflater == null) {
            return;
        }
        View inflate = layoutInflater.inflate(R.layout.top_defaults_view_color_picker_popup, (ViewGroup) null);
        final ColorPickerView colorPickerView = (ColorPickerView) inflate.findViewById(R.id.colorPickerView);
        PopupWindow popupWindow = new PopupWindow(inflate, -2, -2);
        this.popupWindow = popupWindow;
        popupWindow.setBackgroundDrawable(new ColorDrawable(-1));
        this.popupWindow.setOutsideTouchable(true);
        colorPickerView.setInitialColor(this.initialColor);
        colorPickerView.setEnabledBrightness(this.enableBrightness);
        colorPickerView.setEnabledAlpha(this.enableAlpha);
        colorPickerView.setOnlyUpdateOnTouchEventUp(this.onlyUpdateOnTouchEventUp);
        colorPickerView.subscribe(colorPickerObserver);
        TextView textView = (TextView) inflate.findViewById(R.id.cancel);
        textView.setText(this.cancelTitle);
        textView.setOnClickListener(new View.OnClickListener() { // from class: top.defaults.colorpicker.ColorPickerPopup.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ColorPickerPopup.this.popupWindow.dismiss();
            }
        });
        TextView textView2 = (TextView) inflate.findViewById(R.id.ok);
        textView2.setText(this.okTitle);
        textView2.setOnClickListener(new View.OnClickListener() { // from class: top.defaults.colorpicker.ColorPickerPopup.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ColorPickerPopup.this.popupWindow.dismiss();
                ColorPickerObserver colorPickerObserver2 = colorPickerObserver;
                if (colorPickerObserver2 != null) {
                    colorPickerObserver2.onColorPicked(colorPickerView.getColor());
                }
            }
        });
        final View findViewById = inflate.findViewById(R.id.colorIndicator);
        final TextView textView3 = (TextView) inflate.findViewById(R.id.colorHex);
        findViewById.setVisibility(this.showIndicator ? 0 : 8);
        textView3.setVisibility(this.showValue ? 0 : 8);
        if (this.showIndicator) {
            findViewById.setBackgroundColor(this.initialColor);
        }
        if (this.showValue) {
            textView3.setText(colorHex(this.initialColor));
        }
        colorPickerView.subscribe(new ColorObserver() { // from class: top.defaults.colorpicker.ColorPickerPopup.3
            @Override // top.defaults.colorpicker.ColorObserver
            public void onColor(int i, boolean z, boolean z2) {
                if (ColorPickerPopup.this.showIndicator) {
                    findViewById.setBackgroundColor(i);
                }
                if (ColorPickerPopup.this.showValue) {
                    textView3.setText(ColorPickerPopup.this.colorHex(i));
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            this.popupWindow.setElevation(10.0f);
        }
        this.popupWindow.setAnimationStyle(R.style.TopDefaultsViewColorPickerPopupAnimation);
        if (view == null) {
            view = inflate;
        }
        this.popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void dismiss() {
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private Context context;
        private int initialColor = -65281;
        private boolean enableBrightness = true;
        private boolean enableAlpha = false;
        private String okTitle = "OK";
        private String cancelTitle = "Cancel";
        private boolean showIndicator = true;
        private boolean showValue = true;
        private boolean onlyUpdateOnTouchEventUp = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder initialColor(int i) {
            this.initialColor = i;
            return this;
        }

        public Builder enableBrightness(boolean z) {
            this.enableBrightness = z;
            return this;
        }

        public Builder enableAlpha(boolean z) {
            this.enableAlpha = z;
            return this;
        }

        public Builder okTitle(String str) {
            this.okTitle = str;
            return this;
        }

        public Builder cancelTitle(String str) {
            this.cancelTitle = str;
            return this;
        }

        public Builder showIndicator(boolean z) {
            this.showIndicator = z;
            return this;
        }

        public Builder showValue(boolean z) {
            this.showValue = z;
            return this;
        }

        public Builder onlyUpdateOnTouchEventUp(boolean z) {
            this.onlyUpdateOnTouchEventUp = z;
            return this;
        }

        public ColorPickerPopup build() {
            return new ColorPickerPopup(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String colorHex(int i) {
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", Integer.valueOf(Color.alpha(i)), Integer.valueOf(Color.red(i)), Integer.valueOf(Color.green(i)), Integer.valueOf(Color.blue(i)));
    }
}
