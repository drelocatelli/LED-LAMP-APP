package com.home.fragment.dmx02;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
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
import com.home.utils.font.StringUtil;
import com.home.view.custom.LevelProgressBar;
import com.ledlamp.R;
import com.xian.freetype.word.WordManager;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
public class Dmx02TxFragment extends LedBleFragment implements ReceiveDataListener {
    private static final String TAG = "DMX02TextFragment";
    private int alignment;
    private int background;
    private String cmdString;
    @BindView(R.id.etContent)
    EditText etContent;
    private int fontSize;
    private int index;
    @BindView(R.id.llTextBackground)
    LinearLayout llTextBackground;
    @BindView(R.id.llTextSize)
    LinearLayout llTextSize;
    @BindView(R.id.llTop)
    LinearLayout llTop;
    private MainActivity_DMX02 mActivity;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private View menuView;
    private Handler overtimeHandler;
    private Runnable overtimeRunnable;
    @BindView(R.id.progressBar)
    LevelProgressBar progressBar;
    private boolean receiveData;
    @BindView(R.id.rlProgressView)
    RelativeLayout rlProgressView;
    @BindView(R.id.sbColor)
    SeekBar sbColor;
    @BindView(R.id.sbSpeed)
    SeekBar sbSpeed;
    private Handler sendCmdHandler;
    private Runnable sendCmdRunnable;
    private SharedPreferences sp;
    private String tempDataStr;
    private int timeoutCount;
    @BindView(R.id.tvBackgroundValue)
    TextView tvBackgroundValue;
    @BindView(R.id.tvColor)
    TextView tvColor;
    @BindView(R.id.tvFontSizeValue)
    TextView tvFontSizeValue;
    @BindView(R.id.tvProgress)
    TextView tvProgress;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    private TextView tvTextpramTitle;
    @BindView(R.id.tvTop)
    EditText tvTop;
    private LinearLayout viewBackground;
    private LinearLayout viewColors;
    private LinearLayout viewSizes;
    private int leftAlignIndex = 1;
    private int rightAlignIndex = 4;
    private int animation = 5;
    private int[] rgbColors = {255, 0, 0};
    private ArrayList<String> cmdStringList = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

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

    static /* synthetic */ int access$1008(Dmx02TxFragment dmx02TxFragment) {
        int i = dmx02TxFragment.index;
        dmx02TxFragment.index = i + 1;
        return i;
    }

    static /* synthetic */ int access$1908(Dmx02TxFragment dmx02TxFragment) {
        int i = dmx02TxFragment.timeoutCount;
        dmx02TxFragment.timeoutCount = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
    }

    @Override // com.common.listener.ReceiveDataListener
    public void notifyReceiveData(String str, String str2) {
        if (str.equalsIgnoreCase(Constant.PasswordSet) || !str.equalsIgnoreCase("DMX02TextFragment") || str2 == null) {
            return;
        }
        this.timeoutCount = 0;
        this.receiveData = true;
        int i = this.index;
        if (i == -1) {
            if (str2.contains("5A") || str2.contains("5a")) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 100L);
            }
        } else if (i >= 0) {
            String[] split = str2.split(" ");
            if (this.index == Integer.parseInt(split[split.length - 1] + split[split.length - 2] + split[split.length - 3] + split[split.length - 4], 16)) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 100L);
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
        this.mContentView = layoutInflater.inflate(R.layout.dmx02_fragment_text, viewGroup, false);
        this.menuView = layoutInflater.inflate(R.layout.activity_select_textpram, viewGroup, false);
        return this.mContentView;
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        ReceiveDataListenerManager.getInstance().registerListtener(this);
        MainActivity_DMX02 mainActivity = MainActivity_DMX02.getMainActivity();
        this.mActivity = mainActivity;
        if (mainActivity != null) {
            this.mActivity = MainActivity_DMX02.getMainActivity();
        }
        this.sp = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
        this.etContent.addTextChangedListener(new TextWatcher() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.i("DMX02TextFragment", "开始输入:" + Dmx02TxFragment.this.getInputText());
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.i("DMX02TextFragment", "文字变化:" + Dmx02TxFragment.this.getInputText());
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                Log.i("DMX02TextFragment", "输入结束:" + Dmx02TxFragment.this.getInputText());
                if (MainActivity_DMX02.getMainActivity().isRequestPermissionOk) {
                    Dmx02TxFragment.this.tvTop.setText(Dmx02TxFragment.this.getInputText());
                    Dmx02TxFragment.this.getCmdStringList();
                    return;
                }
                Toast.makeText(Dmx02TxFragment.this.getContext(), Dmx02TxFragment.this.getResources().getString(R.string.Read_Write_Required), 0).show();
            }
        });
        this.sbColor.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.2
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (Dmx02TxFragment.this.mActivity == null || Dmx02TxFragment.this.tvColor.getTag() == null) {
                    return;
                }
                if (Dmx02TxFragment.this.tvColor.getTag().equals(100)) {
                    NetConnectBle.getInstance().setDmx02Speed(100);
                } else if (Dmx02TxFragment.this.tvColor.getTag().equals(1)) {
                    NetConnectBle.getInstance().setDmx02Speed(1);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int rgb;
                if (z) {
                    int i2 = 0;
                    if (i < 0 || i >= 255) {
                        if (i >= 255 && i < 510) {
                            int i3 = 255 - (510 - i);
                            rgb = Color.rgb(255, i3, 0);
                            Dmx02TxFragment.this.tvColor.setText(Dmx02TxFragment.this.getActivity().getResources().getString(R.string.dmx02_r_g_b, 255, Integer.valueOf(i3), 0));
                        } else if (i >= 510 && i < 765) {
                            int i4 = 765 - i;
                            rgb = Color.rgb(i4, 255, 0);
                            Dmx02TxFragment.this.tvColor.setText(Dmx02TxFragment.this.getActivity().getResources().getString(R.string.dmx02_r_g_b, Integer.valueOf(i4), 255, 0));
                        } else if (i >= 765 && i < 1020) {
                            int i5 = 255 - (1020 - i);
                            rgb = Color.rgb(0, 255, i5);
                            Dmx02TxFragment.this.tvColor.setText(Dmx02TxFragment.this.getActivity().getResources().getString(R.string.dmx02_r_g_b, 0, 255, Integer.valueOf(i5)));
                        } else if (i >= 1020 && i < 1275) {
                            int i6 = 1275 - i;
                            rgb = Color.rgb(0, i6, 255);
                            Dmx02TxFragment.this.tvColor.setText(Dmx02TxFragment.this.getActivity().getResources().getString(R.string.dmx02_r_g_b, 0, Integer.valueOf(i6), 255));
                        } else if (i >= 1275 && i <= 1530) {
                            int i7 = 255 - (1530 - i);
                            rgb = Color.rgb(i7, 0, 255);
                            Dmx02TxFragment.this.tvColor.setText(Dmx02TxFragment.this.getActivity().getResources().getString(R.string.dmx02_r_g_b, Integer.valueOf(i7), 0, 255));
                        }
                        i2 = rgb;
                    } else {
                        int i8 = 255 - i;
                        Dmx02TxFragment.this.tvColor.setText(Dmx02TxFragment.this.getActivity().getResources().getString(R.string.dmx02_r_g_b, 255, Integer.valueOf(i8), Integer.valueOf(i8)));
                        i2 = Color.rgb(255, i8, i8);
                    }
                    Dmx02TxFragment.this.rgbColors = Tool.getRGB(i2);
                    Dmx02TxFragment.this.tvTop.setTextColor(i2);
                    NetConnectBle.getInstanceByGroup("").setDmx02TextAlignmentColorBackgroundAnimation(Dmx02TxFragment.this.animation, Dmx02TxFragment.this.rgbColors, Dmx02TxFragment.this.background, Dmx02TxFragment.this.alignment);
                    Context context = Dmx02TxFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused = Dmx02TxFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02TextFragment");
                    sb.append("RGB");
                    SharePersistent.savePerference(context, sb.toString(), Dmx02TxFragment.this.tvColor.getText().toString());
                    Context context2 = Dmx02TxFragment.this.getContext();
                    StringBuilder sb2 = new StringBuilder();
                    MainActivity_DMX02 unused2 = Dmx02TxFragment.this.mActivity;
                    sb2.append(MainActivity_DMX02.sceneBean);
                    sb2.append("DMX02TextFragment");
                    sb2.append("RGBValue");
                    SharePersistent.saveInt(context2, sb2.toString(), i);
                    Context context3 = Dmx02TxFragment.this.getContext();
                    StringBuilder sb3 = new StringBuilder();
                    MainActivity_DMX02 unused3 = Dmx02TxFragment.this.mActivity;
                    sb3.append(MainActivity_DMX02.sceneBean);
                    sb3.append("DMX02TextFragment");
                    sb3.append("Color");
                    SharePersistent.saveInt(context3, sb3.toString(), i2);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context = getContext();
            String perference = SharePersistent.getPerference(context, MainActivity_DMX02.sceneBean + "DMX02TextFragmentRGB");
            Context context2 = getContext();
            int i = SharePersistent.getInt(context2, MainActivity_DMX02.sceneBean + "DMX02TextFragmentRGBValue");
            Context context3 = getContext();
            int i2 = SharePersistent.getInt(context3, MainActivity_DMX02.sceneBean + "DMX02TextFragmentColor");
            if (!perference.isEmpty()) {
                this.sbColor.setProgress(i);
                this.tvColor.setText(perference);
                this.rgbColors = Tool.getRGB(i2);
                this.tvTop.setTextColor(i2);
            }
        }
        this.sbSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.3
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (Dmx02TxFragment.this.mActivity == null || Dmx02TxFragment.this.tvSpeed.getTag() == null) {
                    return;
                }
                if (Dmx02TxFragment.this.tvSpeed.getTag().equals(100)) {
                    NetConnectBle.getInstance().setDmx02Speed(100);
                } else if (Dmx02TxFragment.this.tvSpeed.getTag().equals(1)) {
                    NetConnectBle.getInstance().setDmx02Speed(1);
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    if (i3 == 0) {
                        Dmx02TxFragment.this.tvSpeed.setText(String.valueOf(1));
                        if (Dmx02TxFragment.this.mActivity != null) {
                            NetConnectBle.getInstance().setDmx02Speed(1);
                        }
                        Dmx02TxFragment.this.tvSpeed.setTag(1);
                        return;
                    }
                    NetConnectBle.getInstance().setDmx02Speed(i3);
                    TextView textView = Dmx02TxFragment.this.tvSpeed;
                    textView.setText("" + i3);
                    Dmx02TxFragment.this.tvSpeed.setTag(Integer.valueOf(i3));
                    Context context4 = Dmx02TxFragment.this.getContext();
                    StringBuilder sb = new StringBuilder();
                    MainActivity_DMX02 unused = Dmx02TxFragment.this.mActivity;
                    sb.append(MainActivity_DMX02.sceneBean);
                    sb.append("DMX02TextFragment");
                    sb.append("speed_set");
                    SharePersistent.saveInt(context4, sb.toString(), i3);
                }
            }
        });
        if (MainActivity_DMX02.sceneBean != null) {
            Context context4 = getContext();
            int i3 = SharePersistent.getInt(context4, MainActivity_DMX02.sceneBean + "DMX02TextFragmentspeed");
            if (i3 > 0) {
                this.sbSpeed.setProgress(i3);
                this.tvSpeed.setText(String.valueOf(i3));
            } else {
                this.sbSpeed.setProgress(80);
                this.tvSpeed.setText(String.valueOf(80));
            }
        }
        this.tvSend.setOnClickListener(new AnonymousClass4());
        this.tvTextpramTitle = (TextView) this.menuView.findViewById(R.id.tvTextpramTitle);
        this.viewColors = (LinearLayout) this.menuView.findViewById(R.id.viewColors);
        this.viewSizes = (LinearLayout) this.menuView.findViewById(R.id.viewSizes);
        this.viewBackground = (LinearLayout) this.menuView.findViewById(R.id.viewBackground);
        this.llTextSize.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02TxFragment.this.tvTextpramTitle.setText(Dmx02TxFragment.this.getString(R.string.Font_Size));
                Dmx02TxFragment.this.viewColors.setVisibility(8);
                Dmx02TxFragment.this.viewSizes.setVisibility(0);
                Dmx02TxFragment.this.viewBackground.setVisibility(8);
                Dmx02TxFragment.this.showColorCover();
            }
        });
        this.llTextBackground.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02TxFragment.this.tvTextpramTitle.setText(Dmx02TxFragment.this.getString(R.string.Background));
                Dmx02TxFragment.this.viewColors.setVisibility(8);
                Dmx02TxFragment.this.viewSizes.setVisibility(8);
                Dmx02TxFragment.this.viewBackground.setVisibility(0);
                Dmx02TxFragment.this.showColorCover();
            }
        });
        ((ImageView) this.menuView.findViewById(R.id.ivConfirm)).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Dmx02TxFragment.this.mPopupWindow != null) {
                    Dmx02TxFragment.this.mPopupWindow.dismiss();
                }
            }
        });
        ((ImageView) this.menuView.findViewById(R.id.backImage)).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Dmx02TxFragment.this.mPopupWindow != null) {
                    Dmx02TxFragment.this.mPopupWindow.dismiss();
                }
            }
        });
        initSingColorView();
    }

    /* renamed from: com.home.fragment.dmx02.Dmx02TxFragment$4  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Dmx02TxFragment.this.startAnimation(view);
            SharePersistent.savePerference(Dmx02TxFragment.this.mActivity, Constant.Activity, "DMX02TextFragment");
            Dmx02TxFragment.this.etContent.setText(Dmx02TxFragment.this.getInputText());
            if (Dmx02TxFragment.this.cmdString == null) {
                return;
            }
            Dmx02TxFragment.this.receiveData = false;
            Dmx02TxFragment.this.index = -2;
            Dmx02TxFragment.this.progressBar.setLevels(Dmx02TxFragment.this.cmdStringList.size());
            Dmx02TxFragment.this.rlProgressView.setVisibility(0);
            Dmx02TxFragment.this.tvSend.setEnabled(false);
            if (Dmx02TxFragment.this.sendCmdHandler == null) {
                Dmx02TxFragment.this.sendCmdHandler = new Handler();
            }
            if (Dmx02TxFragment.this.sendCmdRunnable == null) {
                Dmx02TxFragment.this.sendCmdRunnable = new Runnable() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Dmx02TxFragment.access$1008(Dmx02TxFragment.this);
                        Dmx02TxFragment.this.receiveData = false;
                        if (Dmx02TxFragment.this.index == Dmx02TxFragment.this.cmdStringList.size()) {
                            Dmx02TxFragment.this.progressBar.setCurrentLevel(0);
                            Dmx02TxFragment.this.tvProgress.setText("0%");
                            Dmx02TxFragment.this.rlProgressView.setVisibility(8);
                            Dmx02TxFragment.this.tvSend.setEnabled(true);
                            Dmx02TxFragment.this.sendCmdHandler.removeCallbacks(this);
                            Dmx02TxFragment.this.index = -2;
                        }
                        if (Dmx02TxFragment.this.progressBar != null) {
                            Dmx02TxFragment.this.progressBar.setCurrentLevel(Dmx02TxFragment.this.index + 1);
                            Dmx02TxFragment.this.progressBar.setAnimMaxTime(300);
                        }
                        if (Dmx02TxFragment.this.cmdStringList.size() > 0 && Dmx02TxFragment.this.tvProgress != null) {
                            Dmx02TxFragment.this.tvProgress.setText((((Dmx02TxFragment.this.index + 1) * 100) / Dmx02TxFragment.this.cmdStringList.size()) + "%");
                        }
                        if (Dmx02TxFragment.this.index == -1) {
                            int[] numb = Dmx02TxFragment.this.getNumb(Dmx02TxFragment.this.cmdString.length() / 2);
                            NetConnectBle.getInstance().sendDmx02DataToFFE1WithCallback(new int[]{90, 74, 1, 128, 12, 0, 1, 0, HttpStatus.SC_NO_CONTENT, 0, numb[0], numb[1], numb[2], numb[3], 0, 0, 0, 0});
                        } else if (Dmx02TxFragment.this.index >= 0) {
                            NetConnectBle.getInstance().sendDmx02DataToFFE2WithCallback(Dmx02TxFragment.HexStringToIntArray((Dmx02TxFragment.this.to04Hex(Dmx02TxFragment.this.index).substring(2) + Dmx02TxFragment.this.to04Hex(Dmx02TxFragment.this.index).substring(0, 2)) + "0000" + ((String) Dmx02TxFragment.this.cmdStringList.get(Dmx02TxFragment.this.index))));
                        }
                        if (Dmx02TxFragment.this.overtimeHandler == null) {
                            Dmx02TxFragment.this.overtimeHandler = new Handler();
                        }
                        if (Dmx02TxFragment.this.overtimeRunnable == null) {
                            Dmx02TxFragment.this.overtimeRunnable = new Runnable() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.4.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Dmx02TxFragment.access$1908(Dmx02TxFragment.this);
                                    if (Dmx02TxFragment.this.timeoutCount <= 3) {
                                        if (Dmx02TxFragment.this.receiveData) {
                                            return;
                                        }
                                        Dmx02TxFragment.this.index--;
                                        Dmx02TxFragment.this.sendCmdHandler.postDelayed(Dmx02TxFragment.this.sendCmdRunnable, 100L);
                                        return;
                                    }
                                    if (Dmx02TxFragment.this.rlProgressView != null) {
                                        Dmx02TxFragment.this.rlProgressView.setVisibility(8);
                                    }
                                    if (Dmx02TxFragment.this.tvSend != null) {
                                        Dmx02TxFragment.this.tvSend.setEnabled(true);
                                    }
                                    Dmx02TxFragment.this.sendCmdHandler.removeCallbacks(this);
                                    Dmx02TxFragment.this.overtimeHandler.removeCallbacks(this);
                                    Dmx02TxFragment.this.index = -2;
                                    Dmx02TxFragment.this.timeoutCount = 0;
                                }
                            };
                        }
                        Dmx02TxFragment.this.overtimeHandler.postDelayed(Dmx02TxFragment.this.overtimeRunnable, 1000L);
                    }
                };
            }
            Dmx02TxFragment.this.sendCmdHandler.postDelayed(Dmx02TxFragment.this.sendCmdRunnable, 10L);
        }
    }

    private boolean containZh(String str) {
        return str.matches("[一-龥]+");
    }

    private boolean containEn(String str) {
        return str.matches("[a-zA-Z0-9 /]+");
    }

    private boolean containSymbol(String str) {
        String[] strArr = {",", ".", "?", "!", ";", "?", "(", ")", "，", "。", "？", "！", "；", "@", "#", "$", "￥", "%", "&", "*", "（", "）"};
        for (int i = 0; i < 22; i++) {
            if (str.contains(strArr[i])) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getInputText() {
        String replaceAll = this.etContent.getText().toString().replaceAll(" ", "");
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < replaceAll.length()) {
            int i2 = i + 1;
            String substring = replaceAll.substring(i, i2);
            if (containZh(substring) || containEn(substring) || containSymbol(substring)) {
                stringBuffer.append(substring);
            }
            i = i2;
        }
        return StringUtil.isEmpty(replaceAll) ? "" : stringBuffer.toString();
    }

    public void showColorCover() {
        View view = this.menuView;
        if (view != null && view.getParent() != null) {
            ((ViewGroup) this.menuView.getParent()).removeAllViews();
        }
        PopupWindow popupWindow = new PopupWindow(this.menuView, -1, -1, true);
        this.mPopupWindow = popupWindow;
        popupWindow.showAtLocation(this.mContentView, 80, 0, 0);
    }

    private void initSingColorView() {
        HashMap hashMap = new HashMap();
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, -1, InputDeviceCompat.SOURCE_ANY, -65281};
        hashMap.put(Integer.valueOf(iArr[0]), Double.valueOf(0.0d));
        hashMap.put(Integer.valueOf(iArr[1]), Double.valueOf(1.0471975511965976d));
        hashMap.put(Integer.valueOf(iArr[2]), Double.valueOf(2.0943951023931953d));
        hashMap.put(Integer.valueOf(iArr[3]), Double.valueOf(3.141592653589793d));
        hashMap.put(Integer.valueOf(iArr[4]), Double.valueOf(4.1887902047863905d));
        hashMap.put(Integer.valueOf(iArr[5]), Double.valueOf(5.235987755982989d));
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(Dmx02TxFragment.this.getActivity(), R.anim.layout_scale));
                Dmx02TxFragment.this.fontSize = Integer.parseInt(((TextView) view).getText().toString());
                Dmx02TxFragment.this.tvTop.setTextSize(Dmx02TxFragment.this.fontSize);
                TextView textView = Dmx02TxFragment.this.tvFontSizeValue;
                textView.setText("" + Dmx02TxFragment.this.fontSize);
                NetConnectBle.getInstance().setDmx02TextFoneSize(Dmx02TxFragment.this.fontSize);
                Context context = Dmx02TxFragment.this.getContext();
                SharePersistent.saveInt(context, LedBleApplication.getApp().getSceneBean() + "DMX02TextFragmentfontSize", Dmx02TxFragment.this.fontSize);
                if (Dmx02TxFragment.this.mPopupWindow != null) {
                    Dmx02TxFragment.this.mPopupWindow.dismiss();
                }
            }
        };
        for (int i = 1; i <= 10; i++) {
            View view = this.menuView;
            TextView textView = (TextView) view.findViewWithTag("viewSize" + i);
            textView.setOnClickListener(onClickListener);
            textView.setTag("viewSize" + i);
        }
        Context context = getContext();
        int i2 = SharePersistent.getInt(context, LedBleApplication.getApp().getSceneBean() + "DMX02TextFragmentfontSize");
        this.fontSize = i2;
        if (i2 > 0) {
            TextView textView2 = this.tvFontSizeValue;
            textView2.setText("" + this.fontSize);
        } else {
            this.tvFontSizeValue.setText("16");
            this.fontSize = 16;
        }
        getResources().getStringArray(R.array.Dmx02TextColorArray);
        View.OnClickListener onClickListener2 = new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                view2.startAnimation(AnimationUtils.loadAnimation(Dmx02TxFragment.this.getActivity(), R.anim.layout_scale));
                int intValue = ((Integer) view2.getTag()).intValue();
                Dmx02TxFragment.this.rgbColors = Tool.getRGB(intValue);
                Integer.parseInt(((TextView) view2).getText().toString());
                Dmx02TxFragment.this.tvTop.setTextColor(intValue);
                NetConnectBle.getInstanceByGroup("").setDmx02TextAlignmentColorBackgroundAnimation(Dmx02TxFragment.this.animation, Dmx02TxFragment.this.rgbColors, Dmx02TxFragment.this.background, Dmx02TxFragment.this.alignment);
                if (Dmx02TxFragment.this.mPopupWindow != null) {
                    Dmx02TxFragment.this.mPopupWindow.dismiss();
                }
            }
        };
        for (int i3 = 1; i3 <= 6; i3++) {
            View view2 = this.menuView;
            TextView textView3 = (TextView) view2.findViewWithTag("viewColor" + i3);
            textView3.setOnClickListener(onClickListener2);
            textView3.setTag(Integer.valueOf(iArr[i3 + (-1)]));
        }
        View.OnClickListener onClickListener3 = new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                view3.startAnimation(AnimationUtils.loadAnimation(Dmx02TxFragment.this.getActivity(), R.anim.layout_scale));
                int parseInt = Integer.parseInt(((TextView) view3).getText().toString());
                if (parseInt == 1) {
                    Dmx02TxFragment.this.tvBackgroundValue.setText(Dmx02TxFragment.this.getString(R.string.No_Style));
                } else {
                    TextView textView4 = Dmx02TxFragment.this.tvBackgroundValue;
                    textView4.setText(Dmx02TxFragment.this.getString(R.string.Style) + parseInt);
                }
                Dmx02TxFragment.this.background = parseInt;
                NetConnectBle.getInstanceByGroup("").setDmx02TextAlignmentColorBackgroundAnimation(Dmx02TxFragment.this.animation, Dmx02TxFragment.this.rgbColors, Dmx02TxFragment.this.background, Dmx02TxFragment.this.alignment);
                if (Dmx02TxFragment.this.mPopupWindow != null) {
                    Dmx02TxFragment.this.mPopupWindow.dismiss();
                }
            }
        };
        for (int i4 = 1; i4 <= 8; i4++) {
            View view3 = this.menuView;
            TextView textView4 = (TextView) view3.findViewWithTag("viewBackground" + i4);
            textView4.setOnClickListener(onClickListener3);
            textView4.setTag("viewBackground" + i4);
        }
        final int[] iArr2 = {R.drawable.dmx02_align_left, R.drawable.dmx02_centered_horizontal, R.drawable.dmx02_align_right, R.drawable.dmx02_align_top, R.drawable.dmx02_center_vertical, R.drawable.dmx02_align_down};
        final int[] iArr3 = {R.drawable.dmx02_align_left_check, R.drawable.dmx02_centered_horizontal_check, R.drawable.dmx02_align_right_check, R.drawable.dmx02_align_top_check, R.drawable.dmx02_center_vertical_check, R.drawable.dmx02_align_down_check};
        View.OnClickListener onClickListener4 = new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view4) {
                view4.startAnimation(AnimationUtils.loadAnimation(Dmx02TxFragment.this.getActivity(), R.anim.layout_scale));
                TextView textView5 = (TextView) view4;
                if (Integer.parseInt(textView5.getText().toString()) >= 0 && Integer.parseInt(textView5.getText().toString()) <= 3) {
                    Dmx02TxFragment.this.leftAlignIndex = Integer.parseInt(textView5.getText().toString());
                    for (int i5 = 1; i5 <= 3; i5++) {
                        String charSequence = textView5.getText().toString();
                        if (!charSequence.equals("" + i5)) {
                            View view5 = Dmx02TxFragment.this.mContentView;
                            ((TextView) view5.findViewWithTag(r3 + i5)).setBackgroundResource(iArr2[i5 - 1]);
                        } else {
                            textView5.setBackgroundResource(iArr3[i5 - 1]);
                        }
                    }
                } else {
                    Dmx02TxFragment.this.rightAlignIndex = Integer.parseInt(textView5.getText().toString());
                    for (int i6 = 4; i6 <= iArr2.length; i6++) {
                        String charSequence2 = textView5.getText().toString();
                        if (!charSequence2.equals("" + i6)) {
                            View view6 = Dmx02TxFragment.this.mContentView;
                            ((TextView) view6.findViewWithTag(r3 + i6)).setBackgroundResource(iArr2[i6 - 1]);
                        } else {
                            textView5.setBackgroundResource(iArr3[i6 - 1]);
                        }
                    }
                }
                if (Dmx02TxFragment.this.leftAlignIndex != 1) {
                    if (Dmx02TxFragment.this.leftAlignIndex != 2) {
                        if (Dmx02TxFragment.this.leftAlignIndex == 3) {
                            Dmx02TxFragment dmx02TxFragment = Dmx02TxFragment.this;
                            dmx02TxFragment.alignment = dmx02TxFragment.rightAlignIndex + 2;
                            NetConnectBle.getInstanceByGroup("").setDmx02TextAlignmentColorBackgroundAnimation(Dmx02TxFragment.this.animation, Dmx02TxFragment.this.rgbColors, Dmx02TxFragment.this.background, Dmx02TxFragment.this.alignment);
                            return;
                        }
                        return;
                    }
                    Dmx02TxFragment dmx02TxFragment2 = Dmx02TxFragment.this;
                    dmx02TxFragment2.alignment = dmx02TxFragment2.rightAlignIndex - 1;
                    NetConnectBle.getInstanceByGroup("").setDmx02TextAlignmentColorBackgroundAnimation(Dmx02TxFragment.this.animation, Dmx02TxFragment.this.rgbColors, Dmx02TxFragment.this.background, Dmx02TxFragment.this.alignment);
                    return;
                }
                Dmx02TxFragment dmx02TxFragment3 = Dmx02TxFragment.this;
                dmx02TxFragment3.alignment = dmx02TxFragment3.rightAlignIndex - 4;
                NetConnectBle.getInstanceByGroup("").setDmx02TextAlignmentColorBackgroundAnimation(Dmx02TxFragment.this.animation, Dmx02TxFragment.this.rgbColors, Dmx02TxFragment.this.background, Dmx02TxFragment.this.alignment);
            }
        };
        for (int i5 = 1; i5 <= 6; i5++) {
            View view4 = this.mContentView;
            TextView textView5 = (TextView) view4.findViewWithTag("viewAlignment" + i5);
            textView5.setOnClickListener(onClickListener4);
            textView5.setTag("viewAlignment" + i5);
        }
        final int[] iArr4 = {R.drawable.dmxa2_fixed, R.drawable.dmxa2_moveleft, R.drawable.dmxa2_moveright, R.drawable.dmxa2_moveup, R.drawable.dmxa2_movedown, R.drawable.dmxa2_flicker};
        final int[] iArr5 = {R.drawable.dmxa2_fixed_check, R.drawable.dmxa2_moveleft_check, R.drawable.dmxa2_moveright_check, R.drawable.dmxa2_moveup_check, R.drawable.dmxa2_movedown_check, R.drawable.dmxa2_flicker_check};
        View.OnClickListener onClickListener5 = new View.OnClickListener() { // from class: com.home.fragment.dmx02.Dmx02TxFragment.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view5) {
                view5.startAnimation(AnimationUtils.loadAnimation(Dmx02TxFragment.this.getActivity(), R.anim.layout_scale));
                TextView textView6 = (TextView) view5;
                Dmx02TxFragment.this.animation = Integer.parseInt(textView6.getText().toString()) - 1;
                NetConnectBle.getInstanceByGroup("").setDmx02TextAlignmentColorBackgroundAnimation(Dmx02TxFragment.this.animation, Dmx02TxFragment.this.rgbColors, Dmx02TxFragment.this.background, Dmx02TxFragment.this.alignment);
                for (int i6 = 1; i6 <= iArr4.length; i6++) {
                    String charSequence = textView6.getText().toString();
                    if (!charSequence.equals("" + i6)) {
                        View view6 = Dmx02TxFragment.this.mContentView;
                        ((TextView) view6.findViewWithTag(r4 + i6)).setBackgroundResource(iArr4[i6 - 1]);
                    } else {
                        textView6.setBackgroundResource(iArr5[i6 - 1]);
                    }
                }
            }
        };
        for (int i6 = 1; i6 <= 6; i6++) {
            View view5 = this.mContentView;
            TextView textView6 = (TextView) view5.findViewWithTag("viewAnimation" + i6);
            textView6.setOnClickListener(onClickListener5);
            textView6.setTag("viewAnimation" + i6);
        }
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
    public void getCmdStringList() {
        String substring;
        int length = 4 - ((getInputText().getBytes().length + 1) & 3);
        if (length == 4) {
            length = 0;
        }
        int length2 = getInputText().getBytes().length + 1 + length;
        String str = "";
        for (int i = 0; i < length + 1; i++) {
            str = str + "00";
        }
        String str2 = this.rgbColors != null ? to02Hex(this.rgbColors[0]) + to02Hex(this.rgbColors[1]) + to02Hex(this.rgbColors[2]) + "FF" : "FF000000FF";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("5a4a5458");
        stringBuffer.append("0000");
        stringBuffer.append("0000");
        stringBuffer.append("6c65642d30310000000000000000000000000000000000000000000000000000");
        stringBuffer.append(to08Hex(length2).substring(6) + to08Hex(length2).substring(0, 6));
        stringBuffer.append(byte2hex(getInputText().getBytes()));
        stringBuffer.append(str);
        stringBuffer.append("10000000");
        stringBuffer.append("00");
        stringBuffer.append("00");
        stringBuffer.append("6400");
        stringBuffer.append("0000");
        stringBuffer.append("0000");
        stringBuffer.append(str2);
        stringBuffer.append("00000000");
        stringBuffer.append(to02Hex(this.fontSize));
        stringBuffer.append("00");
        stringBuffer.append("00");
        stringBuffer.append("00");
        stringBuffer.append("00");
        stringBuffer.append("00");
        stringBuffer.append(to04Hex(getInputText().length()).substring(2) + to04Hex(getInputText().length()).substring(0, 2));
        stringBuffer.append("0000");
        stringBuffer.append("0000");
        stringBuffer.append("0000000000000000");
        this.cmdString = stringBuffer.toString();
        int i2 = 0;
        int i3 = 0;
        while (i2 < getInputText().length()) {
            int i4 = 36;
            int i5 = 32;
            if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 12) {
                if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                    i5 = 12;
                    i4 = 12;
                    String str3 = to04Hex(getInputText().charAt(i2));
                    String str4 = to08Hex(i3);
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append(str3.substring(2) + str3.substring(0, 2));
                    stringBuffer2.append("00");
                    stringBuffer2.append("00");
                    stringBuffer2.append(str4.substring(6) + str4.substring(4, 6) + str4.substring(2, 4) + str4.substring(0, 2));
                    stringBuffer2.append(to02Hex(i4));
                    stringBuffer2.append(to02Hex(i5));
                    stringBuffer2.append(to02Hex(0));
                    stringBuffer2.append(to02Hex(0));
                    int i6 = i2 + 1;
                    i3 += WordManager.getInstance().getBitmapData(getInputText().substring(i2, i6), Integer.parseInt(this.tvFontSizeValue.getText().toString()), 0).length() / 2;
                    stringBuffer.append(stringBuffer2);
                    this.cmdString = stringBuffer.toString();
                    i2 = i6;
                } else {
                    i5 = 12;
                    i4 = 6;
                    String str32 = to04Hex(getInputText().charAt(i2));
                    String str42 = to08Hex(i3);
                    StringBuffer stringBuffer22 = new StringBuffer();
                    stringBuffer22.append(str32.substring(2) + str32.substring(0, 2));
                    stringBuffer22.append("00");
                    stringBuffer22.append("00");
                    stringBuffer22.append(str42.substring(6) + str42.substring(4, 6) + str42.substring(2, 4) + str42.substring(0, 2));
                    stringBuffer22.append(to02Hex(i4));
                    stringBuffer22.append(to02Hex(i5));
                    stringBuffer22.append(to02Hex(0));
                    stringBuffer22.append(to02Hex(0));
                    int i62 = i2 + 1;
                    i3 += WordManager.getInstance().getBitmapData(getInputText().substring(i2, i62), Integer.parseInt(this.tvFontSizeValue.getText().toString()), 0).length() / 2;
                    stringBuffer.append(stringBuffer22);
                    this.cmdString = stringBuffer.toString();
                    i2 = i62;
                }
            } else {
                if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 16) {
                    if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                        i5 = 16;
                        i4 = 16;
                    } else {
                        i4 = 8;
                        i5 = 16;
                    }
                } else if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 20) {
                    if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                        i5 = 20;
                        i4 = 20;
                    } else {
                        i4 = 10;
                        i5 = 20;
                    }
                } else {
                    if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 24) {
                        if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                            i5 = 24;
                        } else {
                            i5 = 24;
                            i4 = 12;
                        }
                    } else if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 28) {
                        if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                            i5 = 28;
                            i4 = 28;
                        } else {
                            i5 = 26;
                            i4 = 14;
                        }
                    } else if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 32) {
                        if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                            i4 = 32;
                        }
                        i4 = 16;
                    } else if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 36) {
                        if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                            i5 = 36;
                        } else {
                            i5 = 36;
                            i4 = 18;
                        }
                    } else if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 40) {
                        if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                            i5 = 40;
                            i4 = 40;
                        } else {
                            i5 = 40;
                            i4 = 20;
                        }
                    } else if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 44) {
                        if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                            i5 = 44;
                            i4 = 44;
                        } else {
                            i4 = 22;
                            i5 = 44;
                        }
                    } else if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 48) {
                        if (isChinese(this.etContent.getText().toString().substring(i2, i2 + 1))) {
                            i5 = 48;
                            i4 = 48;
                        } else {
                            i5 = 48;
                        }
                    } else {
                        i5 = 52;
                        if (Integer.parseInt(this.tvFontSizeValue.getText().toString()) == 52) {
                            i4 = isChinese(this.etContent.getText().toString().substring(i2, i2 + 1)) ? 52 : 26;
                        } else {
                            i5 = 0;
                            i4 = 0;
                        }
                    }
                    i4 = 24;
                }
                String str322 = to04Hex(getInputText().charAt(i2));
                String str422 = to08Hex(i3);
                StringBuffer stringBuffer222 = new StringBuffer();
                stringBuffer222.append(str322.substring(2) + str322.substring(0, 2));
                stringBuffer222.append("00");
                stringBuffer222.append("00");
                stringBuffer222.append(str422.substring(6) + str422.substring(4, 6) + str422.substring(2, 4) + str422.substring(0, 2));
                stringBuffer222.append(to02Hex(i4));
                stringBuffer222.append(to02Hex(i5));
                stringBuffer222.append(to02Hex(0));
                stringBuffer222.append(to02Hex(0));
                int i622 = i2 + 1;
                i3 += WordManager.getInstance().getBitmapData(getInputText().substring(i2, i622), Integer.parseInt(this.tvFontSizeValue.getText().toString()), 0).length() / 2;
                stringBuffer.append(stringBuffer222);
                this.cmdString = stringBuffer.toString();
                i2 = i622;
            }
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        String inputText = getInputText();
        int i7 = 0;
        while (i7 < inputText.length()) {
            int i8 = i7 + 1;
            stringBuffer3.append(WordManager.getInstance().getBitmapData(inputText.substring(i7, i8), Integer.parseInt(this.tvFontSizeValue.getText().toString()), 0));
            i7 = i8;
        }
        stringBuffer.append(stringBuffer3);
        String stringBuffer4 = stringBuffer.toString();
        this.cmdString = stringBuffer4;
        int length3 = ((stringBuffer4.length() / 2) + 199) / 200;
        this.cmdStringList.clear();
        for (int i9 = 0; i9 < length3; i9++) {
            if (i9 == length3 - 1) {
                substring = this.cmdString.substring(i9 * HttpStatus.SC_BAD_REQUEST);
            } else {
                substring = this.cmdString.substring(i9 * HttpStatus.SC_BAD_REQUEST, (i9 + 1) * HttpStatus.SC_BAD_REQUEST);
            }
            this.cmdStringList.add(substring);
        }
    }

    private boolean isChinese(String str) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            Log.e("DMX02TextFragment", "n = " + ((int) charAt));
            if (charAt <= 'z') {
                return false;
            }
        }
        return true;
    }

    private int getPixSetLong() {
        Context context = getContext();
        String perference = SharePersistent.getPerference(context, LedBleApplication.getApp().getSceneBean() + "btnSetPixLong");
        if (perference == null || perference.length() <= 0) {
            Context context2 = getContext();
            SharePersistent.savePerference(context2, LedBleApplication.getApp().getSceneBean() + "btnSetPixLong", "20");
            perference = "16";
        }
        return Integer.parseInt(perference);
    }

    private int getPixSetWidth() {
        Context context = getContext();
        String perference = SharePersistent.getPerference(context, LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth");
        if (perference == null || perference.length() <= 0) {
            Context context2 = getContext();
            SharePersistent.savePerference(context2, LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth", "20");
            perference = "16";
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
}
