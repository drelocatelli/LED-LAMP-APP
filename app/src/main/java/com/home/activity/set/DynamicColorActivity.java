package com.home.activity.set;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import com.common.adapter.AnimationListenerAdapter;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.uitl.ListUtiles;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.common.view.SegmentedRadioGroup;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleActivity;
import com.home.bean.MyColor;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.home.view.BlackWiteSelectView;
import com.home.view.ColorTextView;
import com.home.view.MyColorPickerImageView4RGB;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class DynamicColorActivity extends LedBleActivity {
    private static final int COLOR_DEFALUT = 0;
    private static final String TAG = "DynamicColorActivity";
    private ColorTextView actionView;
    private BlackWiteSelectView blackWiteSelectView;
    private Button buttonBackButton;
    private Button buttonConfirButton;
    private RadioButton changeButtonFour;
    private RadioButton changeButtonOne;
    private RadioButton changeButtonThree;
    private RadioButton changeButtonTwo;
    private ArrayList<ColorTextView> colorTextViews;
    private int currentSelecColorFromPicker;
    private SegmentedRadioGroup dynamicChangeGroup;
    private MyColorPickerImageView4RGB imageViewPicker;
    private View relativeTabColorCover;
    private SeekBar seekBarBrightNess2;
    private SharedPreferences sp;
    private int style = 0;
    private TextView textRGB;
    private TextView textViewBrightNess2;
    private TextView textViewRingBrightSC;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_dynamic_color);
        this.sp = getSharedPreferences(SharePersistent.getPerference(this, Constant.CUSTOM_DIY_APPKEY), 0);
        this.textRGB = (TextView) findViewById(R.id.tvRGB);
        this.textViewRingBrightSC = (TextView) findViewById(R.id.tvRingBrightnessSC);
        this.textViewBrightNess2 = (TextView) findViewById(R.id.textViewBrightNess2);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarBrightNess2);
        this.seekBarBrightNess2 = seekBar;
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.activity.set.DynamicColorActivity.1
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                if (z) {
                    if (i == 0) {
                        seekBar2.setProgress(1);
                        NetConnectBle.getInstance().setSensitivity(1, false, false, MainActivity_BLE.sceneBean);
                        DynamicColorActivity.this.textViewBrightNess2.setText(DynamicColorActivity.this.getResources().getString(R.string.sensitivity_set, 1));
                    } else {
                        NetConnectBle.getInstance().setSensitivity(i, false, false, MainActivity_BLE.sceneBean);
                        DynamicColorActivity.this.textViewBrightNess2.setText(DynamicColorActivity.this.getResources().getString(R.string.sensitivity_set, Integer.valueOf(i)));
                    }
                    if (MainActivity_BLE.getSceneBean() != null) {
                        Context applicationContext = DynamicColorActivity.this.getApplicationContext();
                        SharePersistent.saveInt(applicationContext, MainActivity_BLE.getSceneBean() + DynamicColorActivity.TAG + "bright", i);
                    }
                }
            }
        });
        if (MainActivity_BLE.getSceneBean() != null) {
            Context applicationContext = getApplicationContext();
            int i = SharePersistent.getInt(applicationContext, MainActivity_BLE.getSceneBean() + TAG + "bright");
            if (i > 0) {
                this.seekBarBrightNess2.setProgress(i);
                this.textViewBrightNess2.setText(getApplicationContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i)));
            } else {
                this.textViewBrightNess2.setText(getApplicationContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
            }
        }
        SegmentedRadioGroup segmentedRadioGroup = (SegmentedRadioGroup) findViewById(R.id.dynamicChangeButton);
        this.dynamicChangeGroup = segmentedRadioGroup;
        segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.activity.set.DynamicColorActivity.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i2) {
                if (R.id.dynamicChangeButtonOne == i2) {
                    DynamicColorActivity.this.style = 0;
                } else if (R.id.dynamicChangeButtonTwo == i2) {
                    DynamicColorActivity.this.style = 1;
                } else if (R.id.dynamicChangeButtonThree == i2) {
                    DynamicColorActivity.this.style = 2;
                } else if (R.id.dynamicChangeButtonFour == i2) {
                    DynamicColorActivity.this.style = 3;
                }
            }
        });
        RadioButton radioButton = (RadioButton) findViewById(R.id.dynamicChangeButtonOne);
        this.changeButtonOne = radioButton;
        radioButton.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DynamicColorActivity dynamicColorActivity = DynamicColorActivity.this;
                dynamicColorActivity.putDataBack(dynamicColorActivity.getSelectColor());
            }
        });
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.dynamicChangeButtonTwo);
        this.changeButtonTwo = radioButton2;
        radioButton2.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DynamicColorActivity dynamicColorActivity = DynamicColorActivity.this;
                dynamicColorActivity.putDataBack(dynamicColorActivity.getSelectColor());
            }
        });
        RadioButton radioButton3 = (RadioButton) findViewById(R.id.dynamicChangeButtonThree);
        this.changeButtonThree = radioButton3;
        radioButton3.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DynamicColorActivity dynamicColorActivity = DynamicColorActivity.this;
                dynamicColorActivity.putDataBack(dynamicColorActivity.getSelectColor());
            }
        });
        RadioButton radioButton4 = (RadioButton) findViewById(R.id.dynamicChangeButtonFour);
        this.changeButtonFour = radioButton4;
        radioButton4.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DynamicColorActivity dynamicColorActivity = DynamicColorActivity.this;
                dynamicColorActivity.putDataBack(dynamicColorActivity.getSelectColor());
            }
        });
        this.relativeTabColorCover = findViewById(R.id.relativeTabColorCover);
        Button button = (Button) findViewById(R.id.buttonBack);
        this.buttonBackButton = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DynamicColorActivity.this.finish();
            }
        });
        initColorBlock();
        initColorSelecterView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putDataBack(ArrayList<MyColor> arrayList) {
        NetConnectBle.getInstance().setDynamicDiy(arrayList, this.style);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<MyColor> getSelectColor() {
        ArrayList<MyColor> arrayList = new ArrayList<>();
        if (!ListUtiles.isEmpty(this.colorTextViews)) {
            Iterator<ColorTextView> it = this.colorTextViews.iterator();
            while (it.hasNext()) {
                ColorTextView next = it.next();
                if (next.getColor() != 0) {
                    int[] rgb = Tool.getRGB(next.getColor());
                    arrayList.add(new MyColor(rgb[0], rgb[1], rgb[2]));
                }
            }
        }
        return arrayList;
    }

    private void initColorBlock() {
        boolean z;
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, InputDeviceCompat.SOURCE_ANY, -16711681, -65281, -1};
        View findViewById = findViewById(R.id.linearLayoutViewBlocks);
        this.colorTextViews = new ArrayList<>();
        int i = 1;
        while (true) {
            if (i > 16) {
                z = false;
                break;
            }
            if (this.sp.getInt((String) ((ColorTextView) findViewById.findViewWithTag("editColor" + i)).getTag(), 0) != 0) {
                z = true;
                break;
            }
            i++;
        }
        for (int i2 = 1; i2 <= 16; i2++) {
            final ColorTextView colorTextView = (ColorTextView) findViewById.findViewWithTag("editColor" + i2);
            int i3 = this.sp.getInt((String) colorTextView.getTag(), 0);
            float f = (float) 10;
            ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            if (i3 != 0) {
                shapeDrawable.getPaint().setColor(i3);
                colorTextView.setBackgroundDrawable(shapeDrawable);
                colorTextView.setColor(i3);
                colorTextView.setText("");
            } else if (!z && i2 <= 7) {
                int i4 = i2 - 1;
                shapeDrawable.getPaint().setColor(iArr[i4]);
                colorTextView.setBackgroundDrawable(shapeDrawable);
                colorTextView.setColor(iArr[i4]);
                colorTextView.setText("");
                this.sp.edit().putInt((String) colorTextView.getTag(), iArr[i4]).commit();
            }
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.8
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(DynamicColorActivity.this, R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    if (color == 0) {
                        DynamicColorActivity.this.showColorCover((ColorTextView) view);
                    } else {
                        DynamicColorActivity.this.updateRgbText(Tool.getRGB(color));
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.activity.set.DynamicColorActivity.9
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    DynamicColorActivity.this.sp.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(DynamicColorActivity.this.getResources().getDrawable(R.drawable.block_shap_color));
                    colorTextView2.setText("+");
                    return true;
                }
            });
            this.colorTextViews.add(colorTextView);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showColorCover(ColorTextView colorTextView) {
        this.actionView = colorTextView;
        this.currentSelecColorFromPicker = 0;
        this.textRGB.setText(getResources().getString(R.string.r_g_b, 0, 0, 0));
        this.blackWiteSelectView.setVisibility(4);
        this.textViewRingBrightSC.setVisibility(4);
        this.relativeTabColorCover.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideColorCover() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        loadAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.home.activity.set.DynamicColorActivity.10
            @Override // com.common.adapter.AnimationListenerAdapter, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                DynamicColorActivity.this.relativeTabColorCover.setVisibility(8);
            }
        });
        this.relativeTabColorCover.startAnimation(loadAnimation);
    }

    private void initColorSelecterView() {
        this.imageViewPicker = (MyColorPickerImageView4RGB) findViewById(R.id.imageViewPicker);
        this.blackWiteSelectView = (BlackWiteSelectView) findViewById(R.id.blackWiteSelectView);
        this.imageViewPicker.setOnTouchPixListener(new MyColorPickerImageView4RGB.OnTouchPixListener() { // from class: com.home.activity.set.DynamicColorActivity.11
            @Override // com.home.view.MyColorPickerImageView4RGB.OnTouchPixListener
            public void onColorSelect(int i, float f) {
                DynamicColorActivity.this.blackWiteSelectView.setStartColor(i);
                DynamicColorActivity.this.currentSelecColorFromPicker = i;
                int[] rgb = Tool.getRGB(i);
                DynamicColorActivity.this.textRGB.setText(DynamicColorActivity.this.getResources().getString(R.string.r_g_b, Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
                DynamicColorActivity.this.updateRgbText(rgb);
            }
        });
        this.blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.activity.set.DynamicColorActivity.12
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                DynamicColorActivity.this.currentSelecColorFromPicker = i;
                int i3 = i2 <= 0 ? 1 : i2;
                if (i2 >= 100) {
                    i3 = 100;
                }
                DynamicColorActivity.this.textViewRingBrightSC.setText(DynamicColorActivity.this.getResources().getString(R.string.brightness_set, Integer.valueOf(i3)));
                MainActivity_BLE.getMainActivity().setBrightNess(i3, false, false, false);
            }
        });
        View findViewById = findViewById(R.id.viewColors);
        ArrayList arrayList = new ArrayList();
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, -1, InputDeviceCompat.SOURCE_ANY, -65281};
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(iArr[0]), Double.valueOf(0.0d));
        hashMap.put(Integer.valueOf(iArr[1]), Double.valueOf(1.0471975511965976d));
        hashMap.put(Integer.valueOf(iArr[2]), Double.valueOf(2.0943951023931953d));
        hashMap.put(Integer.valueOf(iArr[3]), Double.valueOf(3.141592653589793d));
        hashMap.put(Integer.valueOf(iArr[4]), Double.valueOf(4.1887902047863905d));
        hashMap.put(Integer.valueOf(iArr[5]), Double.valueOf(5.235987755982989d));
        for (int i = 1; i <= 6; i++) {
            View findViewWithTag = findViewById.findViewWithTag("viewColor" + i);
            findViewWithTag.setTag(Integer.valueOf(iArr[i + (-1)]));
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.13
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    DynamicColorActivity.this.startAnimation(view);
                    int intValue = ((Integer) view.getTag()).intValue();
                    DynamicColorActivity.this.currentSelecColorFromPicker = intValue;
                    DynamicColorActivity.this.blackWiteSelectView.setStartColor(intValue);
                    int[] rgb = Tool.getRGB(intValue);
                    DynamicColorActivity.this.textRGB.setText(DynamicColorActivity.this.getResources().getString(R.string.r_g_b, Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
                    DynamicColorActivity.this.updateRgbText(rgb);
                }
            });
            arrayList.add(findViewWithTag);
        }
        Button button = (Button) findViewById(R.id.buttonConfirm);
        this.buttonConfirButton = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.DynamicColorActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DynamicColorActivity.this.hideColorCover();
                if (DynamicColorActivity.this.currentSelecColorFromPicker == 0) {
                    return;
                }
                float f = 10;
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                shapeDrawable.getPaint().setColor(DynamicColorActivity.this.currentSelecColorFromPicker);
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                DynamicColorActivity.this.actionView.setColor(DynamicColorActivity.this.currentSelecColorFromPicker);
                DynamicColorActivity.this.actionView.setBackgroundDrawable(shapeDrawable);
                DynamicColorActivity.this.sp.edit().putInt((String) DynamicColorActivity.this.actionView.getTag(), DynamicColorActivity.this.currentSelecColorFromPicker).commit();
                DynamicColorActivity.this.actionView.setText("");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_scale));
    }

    public void updateRgbText(int[] iArr) {
        if (MainActivity_BLE.getMainActivity() != null) {
            MainActivity_BLE.getMainActivity().setRgb(iArr[0], iArr[1], iArr[2], true, false, false, false);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.relativeTabColorCover.getVisibility() == 0) {
            hideColorCover();
        } else {
            super.onBackPressed();
        }
    }
}
