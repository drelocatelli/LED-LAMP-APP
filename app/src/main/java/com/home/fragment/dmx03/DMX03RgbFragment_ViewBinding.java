package com.home.fragment.dmx03;

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
public class DMX03RgbFragment_ViewBinding implements Unbinder {
    private DMX03RgbFragment target;

    public DMX03RgbFragment_ViewBinding(DMX03RgbFragment dMX03RgbFragment, View view) {
        this.target = dMX03RgbFragment;
        dMX03RgbFragment.relativeTab1 = Utils.findRequiredView(view, R.id.relativeTab1, "field 'relativeTab1'");
        dMX03RgbFragment.relativeTab2 = Utils.findRequiredView(view, R.id.relativeTab2, "field 'relativeTab2'");
        dMX03RgbFragment.relativeTab3 = Utils.findRequiredView(view, R.id.relativeTab3, "field 'relativeTab3'");
        dMX03RgbFragment.relativeTabBN = Utils.findRequiredView(view, R.id.relativeTabBN, "field 'relativeTabBN'");
        dMX03RgbFragment.imageViewPicker = (ColorPickerView) Utils.findRequiredViewAsType(view, R.id.imageViewPicker, "field 'imageViewPicker'", ColorPickerView.class);
        dMX03RgbFragment.blackWiteSelectView = (BlackWiteSelectView) Utils.findRequiredViewAsType(view, R.id.blackWiteSelectView, "field 'blackWiteSelectView'", BlackWiteSelectView.class);
        dMX03RgbFragment.tvBrightness = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness, "field 'tvBrightness'", TextView.class);
        dMX03RgbFragment.linearChouse = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearChouse, "field 'linearChouse'", LinearLayout.class);
        dMX03RgbFragment.textRed = (TextView) Utils.findRequiredViewAsType(view, R.id.textRed, "field 'textRed'", TextView.class);
        dMX03RgbFragment.textGreen = (TextView) Utils.findRequiredViewAsType(view, R.id.textGreen, "field 'textGreen'", TextView.class);
        dMX03RgbFragment.tvBlue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBlue, "field 'tvBlue'", TextView.class);
        dMX03RgbFragment.iv_switch = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_switch, "field 'iv_switch'", ImageView.class);
        dMX03RgbFragment.myColor = (MyColorPicker) Utils.findRequiredViewAsType(view, R.id.myColor, "field 'myColor'", MyColorPicker.class);
        dMX03RgbFragment.llDiyColor = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDiyColor, "field 'llDiyColor'", LinearLayout.class);
        dMX03RgbFragment.llDiyColorCar01DMX = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDiyColorCar01DMX, "field 'llDiyColorCar01DMX'", LinearLayout.class);
        dMX03RgbFragment.textViewWarmCool = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewWarmCool, "field 'textViewWarmCool'", TextView.class);
        dMX03RgbFragment.pikerImageView = (MyColorPickerImageView) Utils.findRequiredViewAsType(view, R.id.pikerImageView, "field 'pikerImageView'", MyColorPickerImageView.class);
        dMX03RgbFragment.seekBarBrightNessCT = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBrightNessCT, "field 'seekBarBrightNessCT'", SeekBar.class);
        dMX03RgbFragment.textViewBrightNessCT = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightNessCT, "field 'textViewBrightNessCT'", TextView.class);
        dMX03RgbFragment.seekBarRedBrightNess = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarRedBrightNess, "field 'seekBarRedBrightNess'", SeekBar.class);
        dMX03RgbFragment.tvBrightness1 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness1, "field 'tvBrightness1'", TextView.class);
        dMX03RgbFragment.seekBarGreenBrightNess = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarGreenBrightNess, "field 'seekBarGreenBrightNess'", SeekBar.class);
        dMX03RgbFragment.tvBrightness2 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness2, "field 'tvBrightness2'", TextView.class);
        dMX03RgbFragment.seekBarBlueBrightNess = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBlueBrightNess, "field 'seekBarBlueBrightNess'", SeekBar.class);
        dMX03RgbFragment.tvBrightness3 = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness3, "field 'tvBrightness3'", TextView.class);
        dMX03RgbFragment.textViewBrightNessDim = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightNessDim, "field 'textViewBrightNessDim'", TextView.class);
        dMX03RgbFragment.pikerImageViewDim = (MyColorPickerImageView) Utils.findRequiredViewAsType(view, R.id.pikerImageViewDim, "field 'pikerImageViewDim'", MyColorPickerImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        DMX03RgbFragment dMX03RgbFragment = this.target;
        if (dMX03RgbFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        dMX03RgbFragment.relativeTab1 = null;
        dMX03RgbFragment.relativeTab2 = null;
        dMX03RgbFragment.relativeTab3 = null;
        dMX03RgbFragment.relativeTabBN = null;
        dMX03RgbFragment.imageViewPicker = null;
        dMX03RgbFragment.blackWiteSelectView = null;
        dMX03RgbFragment.tvBrightness = null;
        dMX03RgbFragment.linearChouse = null;
        dMX03RgbFragment.textRed = null;
        dMX03RgbFragment.textGreen = null;
        dMX03RgbFragment.tvBlue = null;
        dMX03RgbFragment.iv_switch = null;
        dMX03RgbFragment.myColor = null;
        dMX03RgbFragment.llDiyColor = null;
        dMX03RgbFragment.llDiyColorCar01DMX = null;
        dMX03RgbFragment.textViewWarmCool = null;
        dMX03RgbFragment.pikerImageView = null;
        dMX03RgbFragment.seekBarBrightNessCT = null;
        dMX03RgbFragment.textViewBrightNessCT = null;
        dMX03RgbFragment.seekBarRedBrightNess = null;
        dMX03RgbFragment.tvBrightness1 = null;
        dMX03RgbFragment.seekBarGreenBrightNess = null;
        dMX03RgbFragment.tvBrightness2 = null;
        dMX03RgbFragment.seekBarBlueBrightNess = null;
        dMX03RgbFragment.tvBrightness3 = null;
        dMX03RgbFragment.textViewBrightNessDim = null;
        dMX03RgbFragment.pikerImageViewDim = null;
    }
}
