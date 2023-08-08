package com.home.fragment.ble;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.uitl.SharePersistent;
import com.common.view.SegmentedRadioGroup;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleFragment;
import com.home.view.custom.IndicatorSeekBar;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class AisleFragment extends LedBleFragment {
    private static final String TAG = "AisleFragment";
    int bValue;
    int gValue;
    @BindView(R.id.indicator_seek_bar)
    IndicatorSeekBar indicatorSeekBar;
    @BindView(R.id.linearLayoutTab)
    LinearLayout linearLayoutTab;
    @BindView(R.id.linearLayoutTabB)
    LinearLayout linearLayoutTabB;
    @BindView(R.id.linearLayoutTabC)
    LinearLayout linearLayoutTabC;
    @BindView(R.id.ll_aisle_a)
    LinearLayout ll_aisle_a;
    @BindView(R.id.ll_aisle_b)
    LinearLayout ll_aisle_b;
    @BindView(R.id.ll_aisle_c)
    LinearLayout ll_aisle_c;
    private MainActivity_BLE mActivity;
    private View mContentView;
    int pValue;
    int rValue;
    @BindView(R.id.seekBarBlueBrightNess)
    SeekBar seekBarBlueBrightNess;
    @BindView(R.id.seekBarBlueBrightNessB)
    SeekBar seekBarBlueBrightNessB;
    @BindView(R.id.seekBarBlueBrightNessC)
    SeekBar seekBarBlueBrightNessC;
    @BindView(R.id.seekBarCH1BrightNess)
    SeekBar seekBarCH1BrightNess;
    @BindView(R.id.seekBarCH1BrightNessB)
    SeekBar seekBarCH1BrightNessB;
    @BindView(R.id.seekBarCH1BrightNessC)
    SeekBar seekBarCH1BrightNessC;
    @BindView(R.id.seekBarCH2BrightNess)
    SeekBar seekBarCH2BrightNess;
    @BindView(R.id.seekBarCH2BrightNessB)
    SeekBar seekBarCH2BrightNessB;
    @BindView(R.id.seekBarCH2BrightNessC)
    SeekBar seekBarCH2BrightNessC;
    @BindView(R.id.seekBarCH3BrightNess)
    SeekBar seekBarCH3BrightNess;
    @BindView(R.id.seekBarCH3BrightNessB)
    SeekBar seekBarCH3BrightNessB;
    @BindView(R.id.seekBarCH3BrightNessC)
    SeekBar seekBarCH3BrightNessC;
    @BindView(R.id.seekBarCrystalBrightNess)
    SeekBar seekBarCrystalBrightNess;
    @BindView(R.id.seekBarCrystalBrightNessB)
    SeekBar seekBarCrystalBrightNessB;
    @BindView(R.id.seekBarCrystalBrightNessC)
    SeekBar seekBarCrystalBrightNessC;
    @BindView(R.id.seekBarGreenBrightNess)
    SeekBar seekBarGreenBrightNess;
    @BindView(R.id.seekBarGreenBrightNessB)
    SeekBar seekBarGreenBrightNessB;
    @BindView(R.id.seekBarGreenBrightNessC)
    SeekBar seekBarGreenBrightNessC;
    @BindView(R.id.seekBarPinkBrightNess)
    SeekBar seekBarPinkBrightNess;
    @BindView(R.id.seekBarPinkBrightNessB)
    SeekBar seekBarPinkBrightNessB;
    @BindView(R.id.seekBarPinkBrightNessC)
    SeekBar seekBarPinkBrightNessC;
    @BindView(R.id.seekBarRedBrightNess)
    SeekBar seekBarRedBrightNess;
    @BindView(R.id.seekBarRedBrightNessB)
    SeekBar seekBarRedBrightNessB;
    @BindView(R.id.seekBarRedBrightNessC)
    SeekBar seekBarRedBrightNessC;
    @BindView(R.id.seekBarWhiteBrightNess)
    SeekBar seekBarWhiteBrightNess;
    @BindView(R.id.seekBarWhiteBrightNessB)
    SeekBar seekBarWhiteBrightNessB;
    @BindView(R.id.seekBarWhiteBrightNessC)
    SeekBar seekBarWhiteBrightNessC;
    @BindView(R.id.seekBarYellowBrightNess)
    SeekBar seekBarYellowBrightNess;
    @BindView(R.id.seekBarYellowBrightNessB)
    SeekBar seekBarYellowBrightNessB;
    @BindView(R.id.seekBarYellowBrightNessC)
    SeekBar seekBarYellowBrightNessC;
    private SegmentedRadioGroup segmentedRadioGroup;
    @BindView(R.id.tvBrightness1)
    TextView tvBrightness1;
    @BindView(R.id.tvBrightness10)
    TextView tvBrightness10;
    @BindView(R.id.tvBrightness10B)
    TextView tvBrightness10B;
    @BindView(R.id.tvBrightness10C)
    TextView tvBrightness10C;
    @BindView(R.id.tvBrightness1B)
    TextView tvBrightness1B;
    @BindView(R.id.tvBrightness1C)
    TextView tvBrightness1C;
    @BindView(R.id.tvBrightness2)
    TextView tvBrightness2;
    @BindView(R.id.tvBrightness2B)
    TextView tvBrightness2B;
    @BindView(R.id.tvBrightness2C)
    TextView tvBrightness2C;
    @BindView(R.id.tvBrightness3)
    TextView tvBrightness3;
    @BindView(R.id.tvBrightness3B)
    TextView tvBrightness3B;
    @BindView(R.id.tvBrightness3C)
    TextView tvBrightness3C;
    @BindView(R.id.tvBrightness4)
    TextView tvBrightness4;
    @BindView(R.id.tvBrightness4B)
    TextView tvBrightness4B;
    @BindView(R.id.tvBrightness4C)
    TextView tvBrightness4C;
    @BindView(R.id.tvBrightness5)
    TextView tvBrightness5;
    @BindView(R.id.tvBrightness5B)
    TextView tvBrightness5B;
    @BindView(R.id.tvBrightness5C)
    TextView tvBrightness5C;
    @BindView(R.id.tvBrightness6)
    TextView tvBrightness6;
    @BindView(R.id.tvBrightness6B)
    TextView tvBrightness6B;
    @BindView(R.id.tvBrightness6C)
    TextView tvBrightness6C;
    @BindView(R.id.tvBrightness7)
    TextView tvBrightness7;
    @BindView(R.id.tvBrightness7B)
    TextView tvBrightness7B;
    @BindView(R.id.tvBrightness7C)
    TextView tvBrightness7C;
    @BindView(R.id.tvBrightness8)
    TextView tvBrightness8;
    @BindView(R.id.tvBrightness8B)
    TextView tvBrightness8B;
    @BindView(R.id.tvBrightness8C)
    TextView tvBrightness8C;
    @BindView(R.id.tvBrightness9)
    TextView tvBrightness9;
    @BindView(R.id.tvBrightness9B)
    TextView tvBrightness9B;
    @BindView(R.id.tvBrightness9C)
    TextView tvBrightness9C;
    int wValue;
    int yValue;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_brightness_wifi, viewGroup, false);
        this.mContentView = inflate;
        return inflate;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        if (MainActivity_BLE.getMainActivity() != null) {
            MainActivity_BLE mainActivity = MainActivity_BLE.getMainActivity();
            this.mActivity = mainActivity;
            this.segmentedRadioGroup = mainActivity.getSegmentAisle();
        }
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        SegmentedRadioGroup segmentedRadioGroup = this.segmentedRadioGroup;
        if (segmentedRadioGroup != null) {
            segmentedRadioGroup.check(R.id.aisleOne);
            this.segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.ble.AisleFragment.1
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (R.id.aisleOne == i) {
                        AisleFragment.this.ll_aisle_a.setVisibility(0);
                        AisleFragment.this.ll_aisle_b.setVisibility(8);
                        AisleFragment.this.ll_aisle_c.setVisibility(8);
                    } else if (R.id.aisleTwo == i) {
                        AisleFragment.this.ll_aisle_a.setVisibility(8);
                        AisleFragment.this.ll_aisle_b.setVisibility(0);
                        AisleFragment.this.ll_aisle_c.setVisibility(8);
                    } else if (R.id.aisleThree == i) {
                        AisleFragment.this.ll_aisle_a.setVisibility(8);
                        AisleFragment.this.ll_aisle_b.setVisibility(8);
                        AisleFragment.this.ll_aisle_c.setVisibility(0);
                    }
                }
            });
        }
        this.indicatorSeekBar.setOnSeekBarChangeListener(new IndicatorSeekBar.OnIndicatorSeekBarChangeListener() { // from class: com.home.fragment.ble.AisleFragment.2
            @Override // com.home.view.custom.IndicatorSeekBar.OnIndicatorSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, float f) {
            }

            @Override // com.home.view.custom.IndicatorSeekBar.OnIndicatorSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // com.home.view.custom.IndicatorSeekBar.OnIndicatorSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.seekBarRedBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.3
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness1.setText(Integer.toString(i));
                    AisleFragment.this.setSmartBrightness(i, 1);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarRedBrightNess", i);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, MainActivity_BLE.sceneBean + TAG + "seekBarRedBrightNess");
            if (i >= 0) {
                this.seekBarRedBrightNess.setProgress(i);
                this.tvBrightness1.setText(String.valueOf(i));
            }
        }
        this.seekBarGreenBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.4
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness2.setText(Integer.toString(i2));
                    AisleFragment.this.setSmartBrightness(i2, 2);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context2 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context2, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarGreenBrightNess", i2);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, MainActivity_BLE.sceneBean + TAG + "seekBarGreenBrightNess");
            if (i2 >= 0) {
                this.seekBarGreenBrightNess.setProgress(i2);
                this.tvBrightness2.setText(String.valueOf(i2));
            }
        }
        this.seekBarBlueBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.5
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness3.setText(Integer.toString(i3));
                    AisleFragment.this.setSmartBrightness(i3, 3);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context3 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context3, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarBlueBrightNess", i3);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context3 = getContext();
            int i3 = SharePersistent.getInt(context3, MainActivity_BLE.sceneBean + TAG + "seekBarBlueBrightNess");
            if (i3 >= 0) {
                this.seekBarBlueBrightNess.setProgress(i3);
                this.tvBrightness3.setText(String.valueOf(i3));
            }
        }
        this.seekBarWhiteBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.6
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i4, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness4.setText(Integer.toString(i4));
                    AisleFragment.this.setSmartBrightness(i4, 4);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context4 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context4, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarWhiteBrightNess", i4);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context4 = getContext();
            int i4 = SharePersistent.getInt(context4, MainActivity_BLE.sceneBean + TAG + "seekBarWhiteBrightNess");
            if (i4 >= 0) {
                this.seekBarWhiteBrightNess.setProgress(i4);
                this.tvBrightness4.setText(String.valueOf(i4));
            }
        }
        this.seekBarYellowBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.7
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i5, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness5.setText(Integer.toString(i5));
                    AisleFragment.this.setSmartBrightness(i5, 5);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context5 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context5, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarYellowBrightNess", i5);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context5 = getContext();
            int i5 = SharePersistent.getInt(context5, MainActivity_BLE.sceneBean + TAG + "seekBarYellowBrightNess");
            if (i5 >= 0) {
                this.seekBarYellowBrightNess.setProgress(i5);
                this.tvBrightness5.setText(String.valueOf(i5));
            }
        }
        this.seekBarPinkBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.8
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i6, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness6.setText(Integer.toString(i6));
                    AisleFragment.this.setSmartBrightness(i6, 6);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context6 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context6, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarPinkBrightNess", i6);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context6 = getContext();
            int i6 = SharePersistent.getInt(context6, MainActivity_BLE.sceneBean + TAG + "seekBarPinkBrightNess");
            if (i6 >= 0) {
                this.seekBarPinkBrightNess.setProgress(i6);
                this.tvBrightness6.setText(String.valueOf(i6));
            }
        }
        this.seekBarCrystalBrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.9
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i7, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness7.setText(Integer.toString(i7));
                    AisleFragment.this.setSmartBrightness(i7, 7);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context7 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context7, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCrystalBrightNess", i7);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context7 = getContext();
            int i7 = SharePersistent.getInt(context7, MainActivity_BLE.sceneBean + TAG + "seekBarCrystalBrightNess");
            if (i7 >= 0) {
                this.seekBarCrystalBrightNess.setProgress(i7);
                this.tvBrightness7.setText(String.valueOf(i7));
            }
        }
        this.seekBarCH1BrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.10
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i8, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness8.setText(Integer.toString(i8));
                    AisleFragment.this.setSmartBrightness(i8, 8);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context8 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context8, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH1BrightNess", i8);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context8 = getContext();
            int i8 = SharePersistent.getInt(context8, MainActivity_BLE.sceneBean + TAG + "seekBarCH1BrightNess");
            if (i8 >= 0) {
                this.seekBarCH1BrightNess.setProgress(i8);
                this.tvBrightness8.setText(String.valueOf(i8));
            }
        }
        this.seekBarCH2BrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.11
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i9, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness9.setText(Integer.toString(i9));
                    AisleFragment.this.setSmartBrightness(i9, 9);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context9 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context9, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH2BrightNess", i9);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context9 = getContext();
            int i9 = SharePersistent.getInt(context9, MainActivity_BLE.sceneBean + TAG + "seekBarCH2BrightNess");
            if (i9 >= 0) {
                this.seekBarCH2BrightNess.setProgress(i9);
                this.tvBrightness9.setText(String.valueOf(i9));
            }
        }
        this.seekBarCH3BrightNess.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.12
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i10, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness10.setText(Integer.toString(i10));
                    AisleFragment.this.setSmartBrightness(i10, 10);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context10 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context10, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH3BrightNess", i10);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context10 = getContext();
            int i10 = SharePersistent.getInt(context10, MainActivity_BLE.sceneBean + TAG + "seekBarCH3BrightNess");
            if (i10 >= 0) {
                this.seekBarCH3BrightNess.setProgress(i10);
                this.tvBrightness10.setText(String.valueOf(i10));
            }
        }
        this.seekBarRedBrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.13
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i11, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness1B.setText(Integer.toString(i11));
                    AisleFragment.this.setSmartBrightness(i11, 11);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context11 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context11, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarRedBrightNessB", i11);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context11 = getContext();
            int i11 = SharePersistent.getInt(context11, MainActivity_BLE.sceneBean + TAG + "seekBarRedBrightNessB");
            if (i11 >= 0) {
                this.seekBarRedBrightNessB.setProgress(i11);
                this.tvBrightness1B.setText(String.valueOf(i11));
            }
        }
        this.seekBarGreenBrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.14
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i12, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness2B.setText(Integer.toString(i12));
                    AisleFragment.this.setSmartBrightness(i12, 12);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context12 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context12, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarGreenBrightNessB", i12);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context12 = getContext();
            int i12 = SharePersistent.getInt(context12, MainActivity_BLE.sceneBean + TAG + "seekBarGreenBrightNessB");
            if (i12 >= 0) {
                this.seekBarGreenBrightNessB.setProgress(i12);
                this.tvBrightness2B.setText(String.valueOf(i12));
            }
        }
        this.seekBarBlueBrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.15
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i13, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness3B.setText(Integer.toString(i13));
                    AisleFragment.this.setSmartBrightness(i13, 13);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context13 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context13, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarBlueBrightNessB", i13);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context13 = getContext();
            int i13 = SharePersistent.getInt(context13, MainActivity_BLE.sceneBean + TAG + "seekBarBlueBrightNessB");
            if (i13 >= 0) {
                this.seekBarBlueBrightNessB.setProgress(i13);
                this.tvBrightness3B.setText(String.valueOf(i13));
            }
        }
        this.seekBarWhiteBrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.16
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i14, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness4B.setText(Integer.toString(i14));
                    AisleFragment.this.setSmartBrightness(i14, 14);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context14 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context14, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarWhiteBrightNessB", i14);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context14 = getContext();
            int i14 = SharePersistent.getInt(context14, MainActivity_BLE.sceneBean + TAG + "seekBarWhiteBrightNessB");
            if (i14 >= 0) {
                this.seekBarWhiteBrightNessB.setProgress(i14);
                this.tvBrightness4B.setText(String.valueOf(i14));
            }
        }
        this.seekBarYellowBrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.17
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i15, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness5B.setText(Integer.toString(i15));
                    AisleFragment.this.setSmartBrightness(i15, 15);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context15 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context15, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarYellowBrightNessB", i15);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context15 = getContext();
            int i15 = SharePersistent.getInt(context15, MainActivity_BLE.sceneBean + TAG + "seekBarYellowBrightNessB");
            if (i15 >= 0) {
                this.seekBarYellowBrightNessB.setProgress(i15);
                this.tvBrightness5B.setText(String.valueOf(i15));
            }
        }
        this.seekBarPinkBrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.18
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i16, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness6B.setText(Integer.toString(i16));
                    AisleFragment.this.setSmartBrightness(i16, 16);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context16 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context16, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarPinkBrightNessB", i16);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context16 = getContext();
            int i16 = SharePersistent.getInt(context16, MainActivity_BLE.sceneBean + TAG + "seekBarPinkBrightNessB");
            if (i16 >= 0) {
                this.seekBarPinkBrightNessB.setProgress(i16);
                this.tvBrightness6B.setText(String.valueOf(i16));
            }
        }
        this.seekBarCrystalBrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.19
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i17, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness7B.setText(Integer.toString(i17));
                    AisleFragment.this.setSmartBrightness(i17, 17);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context17 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context17, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCrystalBrightNessB", i17);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context17 = getContext();
            int i17 = SharePersistent.getInt(context17, MainActivity_BLE.sceneBean + TAG + "seekBarCrystalBrightNessB");
            if (i17 >= 0) {
                this.seekBarCrystalBrightNessB.setProgress(i17);
                this.tvBrightness7B.setText(String.valueOf(i17));
            }
        }
        this.seekBarCH1BrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.20
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i18, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness8B.setText(Integer.toString(i18));
                    AisleFragment.this.setSmartBrightness(i18, 18);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context18 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context18, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH1BrightNessB", i18);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context18 = getContext();
            int i18 = SharePersistent.getInt(context18, MainActivity_BLE.sceneBean + TAG + "seekBarCH1BrightNessB");
            if (i18 >= 0) {
                this.seekBarCH1BrightNessB.setProgress(i18);
                this.tvBrightness8B.setText(String.valueOf(i18));
            }
        }
        this.seekBarCH2BrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.21
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i19, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness9B.setText(Integer.toString(i19));
                    AisleFragment.this.setSmartBrightness(i19, 19);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context19 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context19, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH2BrightNessB", i19);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context19 = getContext();
            int i19 = SharePersistent.getInt(context19, MainActivity_BLE.sceneBean + TAG + "seekBarCH2BrightNessB");
            if (i19 >= 0) {
                this.seekBarCH2BrightNessB.setProgress(i19);
                this.tvBrightness9B.setText(String.valueOf(i19));
            }
        }
        this.seekBarCH3BrightNessB.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.22
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i20, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness10B.setText(Integer.toString(i20));
                    AisleFragment.this.setSmartBrightness(i20, 20);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context20 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context20, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH3BrightNessB", i20);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context20 = getContext();
            int i20 = SharePersistent.getInt(context20, MainActivity_BLE.sceneBean + TAG + "seekBarCH3BrightNessB");
            if (i20 >= 0) {
                this.seekBarCH3BrightNessB.setProgress(i20);
                this.tvBrightness10B.setText(String.valueOf(i20));
            }
        }
        this.seekBarRedBrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.23
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i21, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness1C.setText(Integer.toString(i21));
                    AisleFragment.this.setSmartBrightness(i21, 21);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context21 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context21, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarRedBrightNessC", i21);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context21 = getContext();
            int i21 = SharePersistent.getInt(context21, MainActivity_BLE.sceneBean + TAG + "seekBarRedBrightNessC");
            if (i21 >= 0) {
                this.seekBarRedBrightNessC.setProgress(i21);
                this.tvBrightness1C.setText(String.valueOf(i21));
            }
        }
        this.seekBarGreenBrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.24
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i22, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness2C.setText(Integer.toString(i22));
                    AisleFragment.this.setSmartBrightness(i22, 22);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context22 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context22, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarGreenBrightNessC", i22);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context22 = getContext();
            int i22 = SharePersistent.getInt(context22, MainActivity_BLE.sceneBean + TAG + "seekBarGreenBrightNessC");
            if (i22 >= 0) {
                this.seekBarGreenBrightNessC.setProgress(i22);
                this.tvBrightness2C.setText(String.valueOf(i22));
            }
        }
        this.seekBarBlueBrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.25
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i23, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness3C.setText(Integer.toString(i23));
                    AisleFragment.this.setSmartBrightness(i23, 23);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context23 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context23, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarBlueBrightNessC", i23);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context23 = getContext();
            int i23 = SharePersistent.getInt(context23, MainActivity_BLE.sceneBean + TAG + "seekBarBlueBrightNessC");
            if (i23 >= 0) {
                this.seekBarBlueBrightNessC.setProgress(i23);
                this.tvBrightness3C.setText(String.valueOf(i23));
            }
        }
        this.seekBarWhiteBrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.26
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i24, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness4C.setText(Integer.toString(i24));
                    AisleFragment.this.setSmartBrightness(i24, 24);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context24 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context24, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarWhiteBrightNessC", i24);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context24 = getContext();
            int i24 = SharePersistent.getInt(context24, MainActivity_BLE.sceneBean + TAG + "seekBarWhiteBrightNessC");
            if (i24 >= 0) {
                this.seekBarWhiteBrightNessC.setProgress(i24);
                this.tvBrightness4C.setText(String.valueOf(i24));
            }
        }
        this.seekBarYellowBrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.27
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i25, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness5C.setText(Integer.toString(i25));
                    AisleFragment.this.setSmartBrightness(i25, 25);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context25 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context25, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarYellowBrightNessC", i25);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context25 = getContext();
            int i25 = SharePersistent.getInt(context25, MainActivity_BLE.sceneBean + TAG + "seekBarYellowBrightNessC");
            if (i25 >= 0) {
                this.seekBarYellowBrightNessC.setProgress(i25);
                this.tvBrightness5C.setText(String.valueOf(i25));
            }
        }
        this.seekBarPinkBrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.28
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i26, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness6C.setText(Integer.toString(i26));
                    AisleFragment.this.setSmartBrightness(i26, 26);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context26 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context26, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarPinkBrightNessC", i26);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context26 = getContext();
            int i26 = SharePersistent.getInt(context26, MainActivity_BLE.sceneBean + TAG + "seekBarPinkBrightNessC");
            if (i26 >= 0) {
                this.seekBarPinkBrightNessC.setProgress(i26);
                this.tvBrightness6C.setText(String.valueOf(i26));
            }
        }
        this.seekBarCrystalBrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.29
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i27, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness7C.setText(Integer.toString(i27));
                    AisleFragment.this.setSmartBrightness(i27, 27);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context27 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context27, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCrystalBrightNessC", i27);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context27 = getContext();
            int i27 = SharePersistent.getInt(context27, MainActivity_BLE.sceneBean + TAG + "seekBarCrystalBrightNessC");
            if (i27 >= 0) {
                this.seekBarCrystalBrightNessC.setProgress(i27);
                this.tvBrightness7C.setText(String.valueOf(i27));
            }
        }
        this.seekBarCH1BrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.30
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i28, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness8C.setText(Integer.toString(i28));
                    AisleFragment.this.setSmartBrightness(i28, 28);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context28 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context28, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH1BrightNessC", i28);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context28 = getContext();
            int i28 = SharePersistent.getInt(context28, MainActivity_BLE.sceneBean + TAG + "seekBarCH1BrightNessC");
            if (i28 >= 0) {
                this.seekBarCH1BrightNessC.setProgress(i28);
                this.tvBrightness8C.setText(String.valueOf(i28));
            }
        }
        this.seekBarCH2BrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.31
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i29, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness9C.setText(Integer.toString(i29));
                    AisleFragment.this.setSmartBrightness(i29, 29);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context29 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context29, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH2BrightNessC", i29);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context29 = getContext();
            int i29 = SharePersistent.getInt(context29, MainActivity_BLE.sceneBean + TAG + "seekBarCH2BrightNessC");
            if (i29 >= 0) {
                this.seekBarCH2BrightNessC.setProgress(i29);
                this.tvBrightness9C.setText(String.valueOf(i29));
            }
        }
        this.seekBarCH3BrightNessC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.ble.AisleFragment.32
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i30, boolean z) {
                if (z) {
                    AisleFragment.this.tvBrightness10C.setText(Integer.toString(i30));
                    AisleFragment.this.setSmartBrightness(i30, 30);
                    if (MainActivity_BLE.sceneBean != null) {
                        Context context30 = AisleFragment.this.getContext();
                        SharePersistent.saveInt(context30, MainActivity_BLE.sceneBean + AisleFragment.TAG + "seekBarCH3BrightNessC", i30);
                    }
                }
            }
        });
        if (MainActivity_BLE.sceneBean != null) {
            Context context30 = getContext();
            int i30 = SharePersistent.getInt(context30, MainActivity_BLE.sceneBean + TAG + "seekBarCH3BrightNessC");
            if (i30 >= 0) {
                this.seekBarCH3BrightNessC.setProgress(i30);
                this.tvBrightness10C.setText(String.valueOf(i30));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSmartBrightness(int i, int i2) {
        if (MainActivity_BLE.getMainActivity() != null) {
            this.mActivity.setSmartBrightness(i, i2);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (MainActivity_BLE.getMainActivity() != null) {
            this.rValue = SharePersistent.getInt(getActivity(), "CH_R_STAGE");
            this.gValue = SharePersistent.getInt(getActivity(), "CH_G_STAGE");
            this.bValue = SharePersistent.getInt(getActivity(), "CH_B_STAGE");
            this.wValue = SharePersistent.getInt(getActivity(), "CH_W_STAGE");
            this.yValue = SharePersistent.getInt(getActivity(), "CH_Y_STAGE");
            this.pValue = SharePersistent.getInt(getActivity(), "CH_P_STAGE");
        }
        for (int i = 1; i <= 30; i++) {
            SeekBar seekBar = null;
            if (i >= 21) {
                LinearLayout linearLayout = this.linearLayoutTabC;
                seekBar = (SeekBar) linearLayout.findViewWithTag("SeekBar" + i);
            } else if (i >= 11 && i <= 20) {
                LinearLayout linearLayout2 = this.linearLayoutTabB;
                seekBar = (SeekBar) linearLayout2.findViewWithTag("SeekBar" + i);
            } else if (i <= 10) {
                LinearLayout linearLayout3 = this.linearLayoutTab;
                seekBar = (SeekBar) linearLayout3.findViewWithTag("SeekBar" + i);
            }
            if (seekBar != null) {
                seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_bringh_ch1_ch6));
                if (this.rValue == i) {
                    seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_red));
                }
                if (this.gValue == i) {
                    seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_green));
                }
                if (this.bValue == i) {
                    seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_blue));
                }
                if (this.wValue == i) {
                    seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_writ));
                }
                if (this.yValue == i) {
                    seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_yellow));
                }
                if (this.pValue == i) {
                    seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_p));
                }
            }
        }
    }
}
