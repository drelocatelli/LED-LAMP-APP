package com.home.fragment.dmx02;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.home.view.BlackWiteSelectView;
import com.home.view.MyColorPicker;
import com.home.view.custom.LevelProgressBar;
import com.home.view.custom.StreamList.PullLeftToRefreshLayout;
import com.ledlamp.R;
import pl.droidsonroids.gif.GifImageView;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class DMX02ModeFragment_ViewBinding implements Unbinder {
    private DMX02ModeFragment target;

    public DMX02ModeFragment_ViewBinding(DMX02ModeFragment dMX02ModeFragment, View view) {
        this.target = dMX02ModeFragment;
        dMX02ModeFragment.ivLeftMenu = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivLeftMenu, "field 'ivLeftMenu'", ImageView.class);
        dMX02ModeFragment.textViewConnectCount = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewConnectCount, "field 'textViewConnectCount'", TextView.class);
        dMX02ModeFragment.onOffButton = (ToggleButton) Utils.findRequiredViewAsType(view, R.id.onOffButton, "field 'onOffButton'", ToggleButton.class);
        dMX02ModeFragment.relativeRGB = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.relativeRGB, "field 'relativeRGB'", RelativeLayout.class);
        dMX02ModeFragment.imageViewPicker = (ColorPickerView) Utils.findRequiredViewAsType(view, R.id.imageViewPicker, "field 'imageViewPicker'", ColorPickerView.class);
        dMX02ModeFragment.blackWiteSelectView = (BlackWiteSelectView) Utils.findRequiredViewAsType(view, R.id.blackWiteSelectView, "field 'blackWiteSelectView'", BlackWiteSelectView.class);
        dMX02ModeFragment.tvBrightness = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBrightness, "field 'tvBrightness'", TextView.class);
        dMX02ModeFragment.linearChouse = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.linearChouse, "field 'linearChouse'", LinearLayout.class);
        dMX02ModeFragment.textRed = (TextView) Utils.findRequiredViewAsType(view, R.id.textRed, "field 'textRed'", TextView.class);
        dMX02ModeFragment.textGreen = (TextView) Utils.findRequiredViewAsType(view, R.id.textGreen, "field 'textGreen'", TextView.class);
        dMX02ModeFragment.tvBlue = (TextView) Utils.findRequiredViewAsType(view, R.id.tvBlue, "field 'tvBlue'", TextView.class);
        dMX02ModeFragment.iv_switch = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_switch, "field 'iv_switch'", ImageView.class);
        dMX02ModeFragment.myColor = (MyColorPicker) Utils.findRequiredViewAsType(view, R.id.myColor, "field 'myColor'", MyColorPicker.class);
        dMX02ModeFragment.llDiyColor = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDiyColor, "field 'llDiyColor'", LinearLayout.class);
        dMX02ModeFragment.llDiyColorCar01DMX = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llDiyColorCar01DMX, "field 'llDiyColorCar01DMX'", LinearLayout.class);
        dMX02ModeFragment.llAnimation = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimation, "field 'llAnimation'", LinearLayout.class);
        dMX02ModeFragment.gridView = (GridView) Utils.findRequiredViewAsType(view, R.id.gridView, "field 'gridView'", GridView.class);
        dMX02ModeFragment.llAnimatePlay = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimatePlay, "field 'llAnimatePlay'", LinearLayout.class);
        dMX02ModeFragment.ivAnimatePlay = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivAnimatePlay, "field 'ivAnimatePlay'", ImageView.class);
        dMX02ModeFragment.llAnimateUp = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimateUp, "field 'llAnimateUp'", LinearLayout.class);
        dMX02ModeFragment.llAnimateDown = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimateDown, "field 'llAnimateDown'", LinearLayout.class);
        dMX02ModeFragment.llAnimateLeft = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimateLeft, "field 'llAnimateLeft'", LinearLayout.class);
        dMX02ModeFragment.llAnimateRight = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimateRight, "field 'llAnimateRight'", LinearLayout.class);
        dMX02ModeFragment.ivAnimateUp = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivAnimateUp, "field 'ivAnimateUp'", ImageView.class);
        dMX02ModeFragment.ivAnimateDown = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivAnimateDown, "field 'ivAnimateDown'", ImageView.class);
        dMX02ModeFragment.ivAnimateLeft = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivAnimateLeft, "field 'ivAnimateLeft'", ImageView.class);
        dMX02ModeFragment.ivAnimateRight = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivAnimateRight, "field 'ivAnimateRight'", ImageView.class);
        dMX02ModeFragment.llAnimateCycle = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimateCycle, "field 'llAnimateCycle'", LinearLayout.class);
        dMX02ModeFragment.llAnimateColorchange = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llAnimateColorchange, "field 'llAnimateColorchange'", LinearLayout.class);
        dMX02ModeFragment.seekBarSpeedAnimation = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSpeedAnimation, "field 'seekBarSpeedAnimation'", SeekBar.class);
        dMX02ModeFragment.textViewSpeedAnimation = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewSpeedAnimation, "field 'textViewSpeedAnimation'", TextView.class);
        dMX02ModeFragment.seekBarBrightAnimation = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarBrightAnimation, "field 'seekBarBrightAnimation'", SeekBar.class);
        dMX02ModeFragment.textViewBrightAnimation = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightAnimation, "field 'textViewBrightAnimation'", TextView.class);
        dMX02ModeFragment.relativeCustom = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.relativeCustom, "field 'relativeCustom'", RelativeLayout.class);
        dMX02ModeFragment.ivTopImageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivTopImageView, "field 'ivTopImageView'", ImageView.class);
        dMX02ModeFragment.ivTopGifImageView = (GifImageView) Utils.findRequiredViewAsType(view, R.id.ivTopGifImageView, "field 'ivTopGifImageView'", GifImageView.class);
        dMX02ModeFragment.ivAddImage = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivAddImage, "field 'ivAddImage'", ImageView.class);
        dMX02ModeFragment.streamList = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.rv, "field 'streamList'", RecyclerView.class);
        dMX02ModeFragment.plrl = (PullLeftToRefreshLayout) Utils.findRequiredViewAsType(view, R.id.plrl, "field 'plrl'", PullLeftToRefreshLayout.class);
        dMX02ModeFragment.rlProgressView = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rlProgressView, "field 'rlProgressView'", RelativeLayout.class);
        dMX02ModeFragment.tvProgress = (TextView) Utils.findRequiredViewAsType(view, R.id.tvProgress, "field 'tvProgress'", TextView.class);
        dMX02ModeFragment.progressBar = (LevelProgressBar) Utils.findRequiredViewAsType(view, R.id.progressBar, "field 'progressBar'", LevelProgressBar.class);
        dMX02ModeFragment.seekBarSpeedCustom = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarSpeedCustom, "field 'seekBarSpeedCustom'", SeekBar.class);
        dMX02ModeFragment.textViewBrightCustom = (TextView) Utils.findRequiredViewAsType(view, R.id.textViewBrightCustom, "field 'textViewBrightCustom'", TextView.class);
        dMX02ModeFragment.tvSend = (TextView) Utils.findRequiredViewAsType(view, R.id.tvSend, "field 'tvSend'", TextView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        DMX02ModeFragment dMX02ModeFragment = this.target;
        if (dMX02ModeFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        dMX02ModeFragment.ivLeftMenu = null;
        dMX02ModeFragment.textViewConnectCount = null;
        dMX02ModeFragment.onOffButton = null;
        dMX02ModeFragment.relativeRGB = null;
        dMX02ModeFragment.imageViewPicker = null;
        dMX02ModeFragment.blackWiteSelectView = null;
        dMX02ModeFragment.tvBrightness = null;
        dMX02ModeFragment.linearChouse = null;
        dMX02ModeFragment.textRed = null;
        dMX02ModeFragment.textGreen = null;
        dMX02ModeFragment.tvBlue = null;
        dMX02ModeFragment.iv_switch = null;
        dMX02ModeFragment.myColor = null;
        dMX02ModeFragment.llDiyColor = null;
        dMX02ModeFragment.llDiyColorCar01DMX = null;
        dMX02ModeFragment.llAnimation = null;
        dMX02ModeFragment.gridView = null;
        dMX02ModeFragment.llAnimatePlay = null;
        dMX02ModeFragment.ivAnimatePlay = null;
        dMX02ModeFragment.llAnimateUp = null;
        dMX02ModeFragment.llAnimateDown = null;
        dMX02ModeFragment.llAnimateLeft = null;
        dMX02ModeFragment.llAnimateRight = null;
        dMX02ModeFragment.ivAnimateUp = null;
        dMX02ModeFragment.ivAnimateDown = null;
        dMX02ModeFragment.ivAnimateLeft = null;
        dMX02ModeFragment.ivAnimateRight = null;
        dMX02ModeFragment.llAnimateCycle = null;
        dMX02ModeFragment.llAnimateColorchange = null;
        dMX02ModeFragment.seekBarSpeedAnimation = null;
        dMX02ModeFragment.textViewSpeedAnimation = null;
        dMX02ModeFragment.seekBarBrightAnimation = null;
        dMX02ModeFragment.textViewBrightAnimation = null;
        dMX02ModeFragment.relativeCustom = null;
        dMX02ModeFragment.ivTopImageView = null;
        dMX02ModeFragment.ivTopGifImageView = null;
        dMX02ModeFragment.ivAddImage = null;
        dMX02ModeFragment.streamList = null;
        dMX02ModeFragment.plrl = null;
        dMX02ModeFragment.rlProgressView = null;
        dMX02ModeFragment.tvProgress = null;
        dMX02ModeFragment.progressBar = null;
        dMX02ModeFragment.seekBarSpeedCustom = null;
        dMX02ModeFragment.textViewBrightCustom = null;
        dMX02ModeFragment.tvSend = null;
    }
}
