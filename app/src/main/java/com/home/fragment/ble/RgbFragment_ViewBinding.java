package com.home.fragment.ble;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.home.view.BlackWiteSelectView;
import com.home.view.MyColorPicker;
import com.home.view.MyColorPickerImageView;
import com.ledlamp.R;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class RgbFragment_ViewBinding implements Unbinder {
    private RgbFragment target;

    public RgbFragment_ViewBinding(RgbFragment rgbFragment, View view) {
        this.target = rgbFragment;
        rgbFragment.relativeTab1 = Utils.findRequiredView(view, R.id.relativeTab1, "field 'relativeTab1'");
        rgbFragment.relativeTab2 = Utils.findRequiredView(view, R.id.relativeTab2, "field 'relativeTab2'");
        rgbFragment.relativeTab3 = Utils.findRequiredView(view, R.id.relativeTab3, "field 'relativeTab3'");
        rgbFragment.relativeTabBN = Utils.findRequiredView(view, R.id.relativeTabBN, "field 'relativeTabBN'");
        rgbFragment.imageViewPicker = (ColorPickerView) Utils.findRequiredViewAsType(view, R.id.imageViewPicker, "field 'imageViewPicker'", ColorPickerView.class);
        rgbFragment.blackWiteSelectView = (BlackWiteSelectView) Utils.findRequiredViewAsType(view, R.id.blackWiteSelectView, "field 'blackWiteSelectView'", BlackWiteSelectView.class);
        rgbFragment.tvBrightness = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness, "field 'tvBrightness'", TextView.class);
        rgbFragment.linearChouse = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearChouse, "field 'linearChouse'", LinearLayout.class);
        rgbFragment.textRed = (TextView) Utils.findRequiredViewAsType(view, R.id.textRed, "field 'textRed'", TextView.class);
        rgbFragment.textGreen = (TextView) Utils.findRequiredViewAsType(view, R.id.textGreen, "field 'textGreen'", TextView.class);
        rgbFragment.tvBlue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBlue, "field 'tvBlue'", TextView.class);
        rgbFragment.iv_switch = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_switch, "field 'iv_switch'", ImageView.class);
        rgbFragment.myColor = (MyColorPicker) Utils.findRequiredViewAsType(view, R.id.myColor, "field 'myColor'", MyColorPicker.class);
        rgbFragment.llDiyColor = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDiyColor, "field 'llDiyColor'", LinearLayout.class);
        rgbFragment.llDiyColorCar01DMX = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDiyColorCar01DMX, "field 'llDiyColorCar01DMX'", LinearLayout.class);
        rgbFragment.textViewWarmCool = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewWarmCool, "field 'textViewWarmCool'", TextView.class);
        rgbFragment.pikerImageView = (MyColorPickerImageView) Utils.findRequiredViewAsType(view, R.id.pikerImageView, "field 'pikerImageView'", MyColorPickerImageView.class);
        rgbFragment.seekBarBrightNessCT = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBrightNessCT, "field 'seekBarBrightNessCT'", SeekBar.class);
        rgbFragment.textViewBrightNessCT = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightNessCT, "field 'textViewBrightNessCT'", TextView.class);
        rgbFragment.seekBarRedBrightNess = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarRedBrightNess, "field 'seekBarRedBrightNess'", SeekBar.class);
        rgbFragment.tvBrightness1 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness1, "field 'tvBrightness1'", TextView.class);
        rgbFragment.seekBarGreenBrightNess = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarGreenBrightNess, "field 'seekBarGreenBrightNess'", SeekBar.class);
        rgbFragment.tvBrightness2 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness2, "field 'tvBrightness2'", TextView.class);
        rgbFragment.seekBarBlueBrightNess = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBlueBrightNess, "field 'seekBarBlueBrightNess'", SeekBar.class);
        rgbFragment.tvBrightness3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness3, "field 'tvBrightness3'", TextView.class);
        rgbFragment.textViewBrightNessDim = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightNessDim, "field 'textViewBrightNessDim'", TextView.class);
        rgbFragment.pikerImageViewDim = (MyColorPickerImageView) Utils.findRequiredViewAsType(view, R.id.pikerImageViewDim, "field 'pikerImageViewDim'", MyColorPickerImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        RgbFragment rgbFragment = this.target;
        if (rgbFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        rgbFragment.relativeTab1 = null;
        rgbFragment.relativeTab2 = null;
        rgbFragment.relativeTab3 = null;
        rgbFragment.relativeTabBN = null;
        rgbFragment.imageViewPicker = null;
        rgbFragment.blackWiteSelectView = null;
        rgbFragment.tvBrightness = null;
        rgbFragment.linearChouse = null;
        rgbFragment.textRed = null;
        rgbFragment.textGreen = null;
        rgbFragment.tvBlue = null;
        rgbFragment.iv_switch = null;
        rgbFragment.myColor = null;
        rgbFragment.llDiyColor = null;
        rgbFragment.llDiyColorCar01DMX = null;
        rgbFragment.textViewWarmCool = null;
        rgbFragment.pikerImageView = null;
        rgbFragment.seekBarBrightNessCT = null;
        rgbFragment.textViewBrightNessCT = null;
        rgbFragment.seekBarRedBrightNess = null;
        rgbFragment.tvBrightness1 = null;
        rgbFragment.seekBarGreenBrightNess = null;
        rgbFragment.tvBrightness2 = null;
        rgbFragment.seekBarBlueBrightNess = null;
        rgbFragment.tvBrightness3 = null;
        rgbFragment.textViewBrightNessDim = null;
        rgbFragment.pikerImageViewDim = null;
    }
}
