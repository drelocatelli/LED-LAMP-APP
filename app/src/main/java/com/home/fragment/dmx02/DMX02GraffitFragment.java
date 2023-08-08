package com.home.fragment.dmx02;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import butterknife.BindView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.listener.ReceiveDataListener;
import com.common.listener.ReceiveDataListenerManager;
import com.common.net.NetResult;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.home.activity.main.MainActivity_DMX02;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.home.view.custom.LevelProgressBar;
import com.home.view.dmx02.DoodleView;
import com.ledlamp.R;
import java.util.ArrayList;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
public class DMX02GraffitFragment extends LedBleFragment implements ReceiveDataListener {
    private static final String TAG = "DMX02GraffitFragment";
    private String cmdString;
    @BindView(R.id.graffitView)
    DoodleView graffitView;
    private int index;
    @BindView(R.id.llTop)
    LinearLayout llTop;
    private MainActivity_DMX02 mActivity;
    private View mContentView;
    private Handler overtimeHandler;
    private Runnable overtimeRunnable;
    private String panColorString;
    @BindView(R.id.progressBar)
    LevelProgressBar progressBar;
    private boolean receiveData;
    @BindView(R.id.rlProgressView)
    RelativeLayout rlProgressView;
    @BindView(R.id.sbBrightness)
    SeekBar sbBrightness;
    @BindView(R.id.sbColor)
    SeekBar sbColor;
    private Handler sendCmdHandler;
    private Runnable sendCmdRunnable;
    private SharedPreferences sp;
    private String tempDataStr;
    private int timeoutCount;
    @BindView(R.id.tvBrightness)
    TextView tvBrightness;
    @BindView(R.id.tvColor)
    TextView tvColor;
    @BindView(R.id.tvProgress)
    TextView tvProgress;
    @BindView(R.id.tvSend)
    TextView tvSend;
    private StringBuffer sb = new StringBuffer();
    private StringBuffer sbRgb = new StringBuffer();
    private StringBuffer sbLoc = new StringBuffer();
    private ArrayList<String> cmdStringList = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private int mode = 3;
    private int red = 255;
    private int green = 0;
    private int blue = 0;

    private static int MAX(int i, int i2) {
        return i > i2 ? i : i2;
    }

    private static int MIN(int i, int i2) {
        return i > i2 ? i2 : i;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    static /* synthetic */ int access$1308(DMX02GraffitFragment dMX02GraffitFragment) {
        int i = dMX02GraffitFragment.index;
        dMX02GraffitFragment.index = i + 1;
        return i;
    }

    static /* synthetic */ int access$2408(DMX02GraffitFragment dMX02GraffitFragment) {
        int i = dMX02GraffitFragment.timeoutCount;
        dMX02GraffitFragment.timeoutCount = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
    }

    @Override // com.common.listener.ReceiveDataListener
    public void notifyReceiveData(String str, String str2) {
        if (str.equalsIgnoreCase(Constant.PasswordSet) || !str.equalsIgnoreCase("DMX02GraffitFragment") || str2 == null) {
            return;
        }
        this.timeoutCount = 0;
        this.receiveData = true;
        int i = this.index;
        if (i == -1) {
            if (str2.contains("5A") || str2.contains("5a")) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 15L);
            }
        } else if (i >= 0) {
            String[] split = str2.split(" ");
            if (this.index == Integer.parseInt(split[split.length - 1] + split[split.length - 2] + split[split.length - 3] + split[split.length - 4], 16)) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 15L);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        ReceiveDataListenerManager.getInstance().unRegisterListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dmx02_fragment_graffit, viewGroup, false);
        this.mContentView = inflate;
        return inflate;
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        ReceiveDataListenerManager.getInstance().registerListtener(this);
        this.panColorString = "#ff0000";
        this.graffitView.setSize(dip2px(8.0f));
        this.graffitView.setColor(this.panColorString);
        this.graffitView.setOnGetBitmapListener(new DoodleView.OnGetBitmapListener() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.1
            @Override // com.home.view.dmx02.DoodleView.OnGetBitmapListener
            public void onGetBitmap(Bitmap bitmap) {
                DMX02GraffitFragment.this.bitmaps.clear();
                DMX02GraffitFragment.this.bitmaps.add(DMX02GraffitFragment.scale(bitmap, DMX02GraffitFragment.this.getPixSetLong(), DMX02GraffitFragment.this.getPixSetWidth()));
                DMX02GraffitFragment dMX02GraffitFragment = DMX02GraffitFragment.this;
                dMX02GraffitFragment.getCmdStringList(dMX02GraffitFragment.bitmaps);
            }
        });
        MainActivity_DMX02 mainActivity = MainActivity_DMX02.getMainActivity();
        this.mActivity = mainActivity;
        if (mainActivity != null) {
            this.mActivity = MainActivity_DMX02.getMainActivity();
        }
        this.sp = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(DMX02GraffitFragment.this.getActivity(), R.anim.layout_scale));
                int parseInt = Integer.parseInt(((TextView) view).getText().toString());
                if (parseInt == 1) {
                    DMX02GraffitFragment.this.mode = 0;
                    DMX02GraffitFragment.this.graffitView.back();
                } else if (parseInt == 2) {
                    DMX02GraffitFragment.this.mode = 1;
                    DMX02GraffitFragment.this.graffitView.setColor("#000000");
                } else if (parseInt == 3) {
                    DMX02GraffitFragment.this.mode = 2;
                    DMX02GraffitFragment.this.graffitView.reset();
                } else if (parseInt == 4) {
                    DMX02GraffitFragment.this.mode = 3;
                    DMX02GraffitFragment.this.graffitView.setColor(DMX02GraffitFragment.this.panColorString);
                } else if (parseInt == 5) {
                    DMX02GraffitFragment.this.mode = 4;
                }
                NetConnectBle.getInstanceByGroup("").setDmx02GraffitColor(DMX02GraffitFragment.this.mode, DMX02GraffitFragment.this.red, DMX02GraffitFragment.this.green, DMX02GraffitFragment.this.blue);
            }
        };
        for (int i = 1; i <= 6; i++) {
            View view = this.mContentView;
            ((TextView) view.findViewWithTag("viewAction" + i)).setOnClickListener(onClickListener);
        }
        final String[] strArr = {"#FF0000", "#00FF00", "#0000FF", "#FFFFFF", "#FFFF00", "#FF00FF"};
        final int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, -1, InputDeviceCompat.SOURCE_ANY, -65281};
        View.OnClickListener onClickListener2 = new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                view2.startAnimation(AnimationUtils.loadAnimation(DMX02GraffitFragment.this.getActivity(), R.anim.layout_scale));
                int parseInt = Integer.parseInt(((TextView) view2).getText().toString());
                DMX02GraffitFragment.this.graffitView.setColor(strArr[parseInt]);
                DMX02GraffitFragment.this.panColorString = strArr[parseInt];
                int[] rgb = Tool.getRGB(iArr[parseInt]);
                DMX02GraffitFragment.this.red = rgb[0];
                DMX02GraffitFragment.this.green = rgb[1];
                DMX02GraffitFragment.this.blue = rgb[2];
                DMX02GraffitFragment.this.mode = 3;
                NetConnectBle.getInstanceByGroup("").setDmx02GraffitColor(DMX02GraffitFragment.this.mode, DMX02GraffitFragment.this.red, DMX02GraffitFragment.this.green, DMX02GraffitFragment.this.blue);
            }
        };
        for (int i2 = 1; i2 <= 6; i2++) {
            View view2 = this.mContentView;
            ((TextView) view2.findViewWithTag("viewColor" + i2)).setOnClickListener(onClickListener2);
        }
        this.sbColor.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.4
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX02GraffitFragment.this.mActivity == null || DMX02GraffitFragment.this.tvColor.getTag() == null) {
                    return;
                }
                if (DMX02GraffitFragment.this.tvColor.getTag().equals(100)) {
                    NetConnectBle.getInstance().setDmx02Speed(100);
                } else if (DMX02GraffitFragment.this.tvColor.getTag().equals(1)) {
                    NetConnectBle.getInstance().setDmx02Speed(1);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    DMX02GraffitFragment.this.red = 0;
                    DMX02GraffitFragment.this.green = 0;
                    DMX02GraffitFragment.this.blue = 0;
                    if (i3 >= 0 && i3 < 255) {
                        DMX02GraffitFragment.this.red = 255;
                        int i4 = 255 - i3;
                        DMX02GraffitFragment.this.green = i4;
                        DMX02GraffitFragment.this.blue = i4;
                    } else if (i3 >= 255 && i3 < 510) {
                        DMX02GraffitFragment.this.red = 255;
                        DMX02GraffitFragment.this.green = 255 - (510 - i3);
                        DMX02GraffitFragment.this.blue = 0;
                    } else if (i3 >= 510 && i3 < 765) {
                        DMX02GraffitFragment.this.red = 765 - i3;
                        DMX02GraffitFragment.this.green = 255;
                        DMX02GraffitFragment.this.blue = 0;
                    } else if (i3 >= 765 && i3 < 1020) {
                        DMX02GraffitFragment.this.red = 0;
                        DMX02GraffitFragment.this.green = 255;
                        DMX02GraffitFragment.this.blue = 255 - (1020 - i3);
                    } else if (i3 >= 1020 && i3 < 1275) {
                        DMX02GraffitFragment.this.red = 0;
                        DMX02GraffitFragment.this.green = 1275 - i3;
                        DMX02GraffitFragment.this.blue = 255;
                    } else if (i3 >= 1275 && i3 <= 1530) {
                        DMX02GraffitFragment.this.red = 255 - (1530 - i3);
                        DMX02GraffitFragment.this.green = 0;
                        DMX02GraffitFragment.this.blue = 255;
                    }
                    DMX02GraffitFragment.this.tvColor.setText(DMX02GraffitFragment.this.getActivity().getResources().getString(R.string.r_g_b, Integer.valueOf(DMX02GraffitFragment.this.red), Integer.valueOf(DMX02GraffitFragment.this.green), Integer.valueOf(DMX02GraffitFragment.this.blue)));
                    DMX02GraffitFragment dMX02GraffitFragment = DMX02GraffitFragment.this;
                    dMX02GraffitFragment.panColorString = String.format("#%02x%02x%02x", Integer.valueOf(dMX02GraffitFragment.red), Integer.valueOf(DMX02GraffitFragment.this.green), Integer.valueOf(DMX02GraffitFragment.this.blue));
                    DMX02GraffitFragment.this.graffitView.setColor(DMX02GraffitFragment.this.panColorString);
                    DMX02GraffitFragment.this.mode = 3;
                    NetConnectBle.getInstanceByGroup("").setDmx02GraffitColor(DMX02GraffitFragment.this.mode, DMX02GraffitFragment.this.red, DMX02GraffitFragment.this.green, DMX02GraffitFragment.this.blue);
                    Context context = DMX02GraffitFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused = DMX02GraffitFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02GraffitFragment");
                    sb.append("RGB");
                    SharePersistent.savePerference(context, sb.toString(), DMX02GraffitFragment.this.tvColor.getText().toString());
                    Context context2 = DMX02GraffitFragment.this.getContext();
                    StringBuilder sb2 = new StringBuilder();
                    MainActivity_DMX02 unused2 = DMX02GraffitFragment.this.mActivity;
                    sb2.append(MainActivity_DMX02.sceneBean);
                    sb2.append("DMX02GraffitFragment");
                    sb2.append("RGBValue");
                    SharePersistent.saveInt(context2, sb2.toString(), i3);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context = getContext();
            String perference = SharePersistent.getPerference(context, MainActivity_DMX02.sceneBean + "DMX02GraffitFragmentRGB");
            Context context2 = getContext();
            int i3 = SharePersistent.getInt(context2, MainActivity_DMX02.sceneBean + "DMX02GraffitFragmentRGBValue");
            if (!perference.isEmpty()) {
                this.sbColor.setProgress(i3);
                this.tvColor.setText(perference);
            }
        }
        this.sbBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.5
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX02GraffitFragment.this.mActivity == null || DMX02GraffitFragment.this.tvBrightness.getTag() == null) {
                    return;
                }
                if (DMX02GraffitFragment.this.tvBrightness.getTag().equals(100)) {
                    NetConnectBle.getInstance().setDmx02Bright(100);
                } else if (DMX02GraffitFragment.this.tvBrightness.getTag().equals(1)) {
                    NetConnectBle.getInstance().setDmx02Bright(1);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i4, boolean z) {
                if (z) {
                    if (i4 == 0) {
                        DMX02GraffitFragment.this.tvBrightness.setText(DMX02GraffitFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(1)));
                        if (DMX02GraffitFragment.this.mActivity != null) {
                            NetConnectBle.getInstance().setDmx02Bright(1);
                        }
                        DMX02GraffitFragment.this.tvBrightness.setTag(1);
                        return;
                    }
                    NetConnectBle.getInstance().setDmx02Bright(i4);
                    DMX02GraffitFragment.this.tvBrightness.setText(DMX02GraffitFragment.this.getActivity().getResources().getString(R.string.brightness_set, Integer.valueOf(i4)));
                    DMX02GraffitFragment.this.tvBrightness.setTag(Integer.valueOf(i4));
                    Context context3 = DMX02GraffitFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused = DMX02GraffitFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02GraffitFragment");
                    sb.append("bright");
                    SharePersistent.saveInt(context3, sb.toString(), i4);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context3 = getContext();
            int i4 = SharePersistent.getInt(context3, MainActivity_DMX02.sceneBean + "DMX02GraffitFragmentbright");
            if (i4 > 0) {
                this.sbBrightness.setProgress(i4);
                this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i4)));
            } else {
                this.sbBrightness.setProgress(100);
                this.tvBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
            }
        }
        this.tvSend.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                DMX02GraffitFragment.this.startAnimation(view3);
                DMX02GraffitFragment.this.sendData();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendData() {
        SharePersistent.savePerference(this.mActivity, Constant.Activity, "DMX02GraffitFragment");
        if (this.cmdString == null) {
            return;
        }
        this.receiveData = false;
        this.index = -2;
        LevelProgressBar levelProgressBar = this.progressBar;
        if (levelProgressBar != null) {
            levelProgressBar.setLevels(this.cmdStringList.size());
        }
        this.rlProgressView.setVisibility(0);
        this.tvSend.setEnabled(false);
        if (this.sendCmdHandler == null) {
            this.sendCmdHandler = new Handler();
        }
        if (this.sendCmdRunnable == null) {
            this.sendCmdRunnable = new Runnable() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.7
                @Override // java.lang.Runnable
                public void run() {
                    DMX02GraffitFragment.access$1308(DMX02GraffitFragment.this);
                    DMX02GraffitFragment.this.receiveData = false;
                    if (DMX02GraffitFragment.this.index == DMX02GraffitFragment.this.cmdStringList.size()) {
                        DMX02GraffitFragment.this.progressBar.setCurrentLevel(0);
                        DMX02GraffitFragment.this.tvProgress.setText("0%");
                        DMX02GraffitFragment.this.rlProgressView.setVisibility(8);
                        DMX02GraffitFragment.this.tvSend.setEnabled(true);
                        DMX02GraffitFragment.this.sendCmdHandler.removeCallbacks(DMX02GraffitFragment.this.sendCmdRunnable);
                        DMX02GraffitFragment.this.overtimeHandler.removeCallbacks(DMX02GraffitFragment.this.overtimeRunnable);
                        DMX02GraffitFragment.this.index = -2;
                        return;
                    }
                    if (DMX02GraffitFragment.this.progressBar != null) {
                        DMX02GraffitFragment.this.progressBar.setCurrentLevel(DMX02GraffitFragment.this.index + 1);
                        DMX02GraffitFragment.this.progressBar.setAnimMaxTime(300);
                    }
                    if (DMX02GraffitFragment.this.cmdStringList.size() > 0 && DMX02GraffitFragment.this.tvProgress != null) {
                        TextView textView = DMX02GraffitFragment.this.tvProgress;
                        textView.setText((((DMX02GraffitFragment.this.index + 1) * 100) / DMX02GraffitFragment.this.cmdStringList.size()) + "%");
                    }
                    if (DMX02GraffitFragment.this.index != -1) {
                        if (DMX02GraffitFragment.this.index >= 0) {
                            StringBuilder sb = new StringBuilder();
                            DMX02GraffitFragment dMX02GraffitFragment = DMX02GraffitFragment.this;
                            sb.append(dMX02GraffitFragment.to04Hex(dMX02GraffitFragment.index).substring(2));
                            DMX02GraffitFragment dMX02GraffitFragment2 = DMX02GraffitFragment.this;
                            sb.append(dMX02GraffitFragment2.to04Hex(dMX02GraffitFragment2.index).substring(0, 2));
                            String sb2 = sb.toString();
                            NetConnectBle.getInstance().sendDmx02DataToFFE2WithCallback(DMX02GraffitFragment.HexStringToIntArray(sb2 + "0000" + ((String) DMX02GraffitFragment.this.cmdStringList.get(DMX02GraffitFragment.this.index))));
                        }
                    } else {
                        DMX02GraffitFragment dMX02GraffitFragment3 = DMX02GraffitFragment.this;
                        int[] numb = dMX02GraffitFragment3.getNumb(dMX02GraffitFragment3.cmdString.length() / 2);
                        NetConnectBle.getInstance().sendDmx02DataToFFE1WithCallback(new int[]{90, 74, 1, 128, 12, 0, 1, 0, HttpStatus.SC_NO_CONTENT, 0, numb[0], numb[1], numb[2], numb[3], 0, 0, 0, 0});
                    }
                    if (DMX02GraffitFragment.this.overtimeHandler == null) {
                        DMX02GraffitFragment.this.overtimeHandler = new Handler();
                    }
                    if (DMX02GraffitFragment.this.overtimeRunnable == null) {
                        DMX02GraffitFragment.this.overtimeRunnable = new Runnable() { // from class: com.home.fragment.dmx02.DMX02GraffitFragment.7.1
                            @Override // java.lang.Runnable
                            public void run() {
                                DMX02GraffitFragment.access$2408(DMX02GraffitFragment.this);
                                if (DMX02GraffitFragment.this.timeoutCount <= 3) {
                                    if (DMX02GraffitFragment.this.receiveData) {
                                        return;
                                    }
                                    DMX02GraffitFragment.this.index--;
                                    if (DMX02GraffitFragment.this.rlProgressView != null && DMX02GraffitFragment.this.tvSend != null) {
                                        DMX02GraffitFragment.this.rlProgressView.setVisibility(0);
                                    }
                                    DMX02GraffitFragment.this.sendCmdHandler.postDelayed(DMX02GraffitFragment.this.sendCmdRunnable, 15L);
                                    return;
                                }
                                if (DMX02GraffitFragment.this.rlProgressView != null && DMX02GraffitFragment.this.tvSend != null) {
                                    DMX02GraffitFragment.this.rlProgressView.setVisibility(8);
                                    DMX02GraffitFragment.this.tvSend.setEnabled(true);
                                }
                                DMX02GraffitFragment.this.sendCmdHandler.removeCallbacks(DMX02GraffitFragment.this.sendCmdRunnable);
                                DMX02GraffitFragment.this.overtimeHandler.removeCallbacks(DMX02GraffitFragment.this.overtimeRunnable);
                                DMX02GraffitFragment.this.index = -2;
                                DMX02GraffitFragment.this.timeoutCount = 0;
                            }
                        };
                    }
                    DMX02GraffitFragment.this.overtimeHandler.postDelayed(DMX02GraffitFragment.this.overtimeRunnable, 80L);
                }
            };
        }
        this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 10L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap scale(Bitmap bitmap, double d, double d2) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) d) / width, ((float) d2) / height);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    private int dip2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static Bitmap generateBitmap(String str, int i, int i2) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(i);
        textPaint.setColor(i2);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        Bitmap createBitmap = Bitmap.createBitmap((int) Math.ceil(textPaint.measureText(str)), (int) Math.ceil(Math.abs(fontMetrics.bottom) + Math.abs(fontMetrics.top)), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawText(str, 0.0f, Math.abs(fontMetrics.ascent), textPaint);
        return createBitmap;
    }

    private Bitmap getTransparentBitmapCopy(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] iArr = new int[width * height];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public String byte2hex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = NetResult.CODE_OK + hexString;
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCmdStringList(ArrayList<Bitmap> arrayList) {
        String substring;
        this.cmdString = "";
        this.cmdStringList.clear();
        StringBuffer stringBuffer = this.sb;
        if (stringBuffer == null) {
            this.sb = new StringBuffer();
        } else {
            stringBuffer.setLength(0);
        }
        StringBuffer stringBuffer2 = this.sb;
        stringBuffer2.append("5a4a4752");
        stringBuffer2.append("0100");
        stringBuffer2.append("0000");
        stringBuffer2.append("6c65642d30310000000000000000000000000000000000000000000000000000");
        stringBuffer2.append("01");
        stringBuffer2.append("01");
        stringBuffer2.append("00");
        stringBuffer2.append("00");
        stringBuffer2.append(to04Hex(getPixSetLong()).substring(2) + to04Hex(getPixSetLong()).substring(0, 2));
        stringBuffer2.append(to04Hex(getPixSetWidth()).substring(2) + to04Hex(getPixSetWidth()).substring(0, 2));
        stringBuffer2.append("0000");
        stringBuffer2.append("0000");
        stringBuffer2.append("6400");
        stringBuffer2.append(to02Hex(arrayList.size()));
        stringBuffer2.append("00");
        stringBuffer2.append("000000ff");
        stringBuffer2.append("00000000");
        this.cmdString = stringBuffer2.toString();
        for (int i = 0; i < arrayList.size(); i++) {
            Bitmap bitmap = arrayList.get(i);
            StringBuffer stringBuffer3 = this.sbLoc;
            if (stringBuffer3 == null) {
                this.sbLoc = new StringBuffer();
            } else {
                stringBuffer3.setLength(0);
            }
            for (int i2 = 0; i2 < bitmap.getHeight(); i2++) {
                for (int i3 = 0; i3 < bitmap.getWidth(); i3++) {
                    int pixel = bitmap.getPixel(i3, i2);
                    int red = Color.red(pixel);
                    int green = Color.green(pixel);
                    int blue = Color.blue(pixel);
                    if (red > 0 || green > 0 || blue > 0) {
                        StringBuffer stringBuffer4 = this.sbLoc;
                        stringBuffer4.append(to02Hex(i3) + to02Hex(i2));
                    }
                }
            }
            StringBuffer stringBuffer5 = this.sb;
            stringBuffer5.append(this.sbLoc);
            this.cmdString = stringBuffer5.toString();
        }
        int length = ((this.cmdString.length() / 2) + 199) / 200;
        for (int i4 = 0; i4 < length; i4++) {
            if (i4 == length - 1) {
                substring = this.cmdString.substring(i4 * HttpStatus.SC_BAD_REQUEST);
            } else {
                substring = this.cmdString.substring(i4 * HttpStatus.SC_BAD_REQUEST, (i4 + 1) * HttpStatus.SC_BAD_REQUEST);
            }
            this.cmdStringList.add(substring);
        }
        sendData();
    }

    private String get565RgbHex(int i, int i2, int i3) {
        String binaryString = Integer.toBinaryString(i);
        int length = binaryString.length();
        for (int i4 = 0; i4 < 8 - length; i4++) {
            binaryString = NetResult.CODE_OK + binaryString;
        }
        String substring = binaryString.substring(0, 5);
        String binaryString2 = Integer.toBinaryString(i2);
        int length2 = binaryString2.length();
        for (int i5 = 0; i5 < 8 - length2; i5++) {
            binaryString2 = NetResult.CODE_OK + binaryString2;
        }
        String substring2 = binaryString2.substring(0, 6);
        String binaryString3 = Integer.toBinaryString(i3);
        int length3 = binaryString3.length();
        for (int i6 = 0; i6 < 8 - length3; i6++) {
            binaryString3 = NetResult.CODE_OK + binaryString3;
        }
        String hexString = Integer.toHexString(Integer.parseInt(substring + substring2 + binaryString3.substring(0, 5), 2));
        int length4 = hexString.length();
        for (int i7 = 0; i7 < 4 - length4; i7++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString.substring(2) + hexString.substring(0, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPixSetLong() {
        Context context = getContext();
        String perference = SharePersistent.getPerference(context, LedBleApplication.getApp().getSceneBean() + "btnSetPixLong");
        if (perference == null || perference.length() <= 0) {
            perference = "16";
            Context context2 = getContext();
            SharePersistent.savePerference(context2, LedBleApplication.getApp().getSceneBean() + "btnSetPixLong", "16");
        }
        return Integer.parseInt(perference);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPixSetWidth() {
        Context context = getContext();
        String perference = SharePersistent.getPerference(context, LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth");
        if (perference == null || perference.length() <= 0) {
            perference = "16";
            Context context2 = getContext();
            SharePersistent.savePerference(context2, LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth", "16");
        }
        return Integer.parseInt(perference);
    }

    private String to02Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 2 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String to04Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 4 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    private String to08Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 8 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getNumb(int i) {
        int[] iArr = new int[4];
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 8 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        String substring = hexString.substring(4);
        String substring2 = hexString.substring(0, 4);
        iArr[0] = Integer.parseInt(substring.substring(2), 16);
        iArr[1] = Integer.parseInt(substring.substring(0, 2), 16);
        iArr[2] = Integer.parseInt(substring2.substring(2), 16);
        iArr[3] = Integer.parseInt(substring2.substring(0, 2), 16);
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] HexStringToIntArray(String str) {
        int length = str.length() / 2;
        String[] strArr = new String[length];
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int i3 = i2 + 2;
            strArr[i] = str.substring(i2, i3);
            iArr[i] = Integer.parseInt(str.substring(i2, i3), 16);
        }
        return iArr;
    }

    public static Bitmap compressBitmap(Bitmap bitmap, double d, double d2) {
        float f;
        float f2;
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width > height) {
            f = ((float) d) / width;
            f2 = ((float) d2) / height;
        } else {
            f = ((float) d) / height;
            f2 = ((float) d2) / width;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(f, f2);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }
}
