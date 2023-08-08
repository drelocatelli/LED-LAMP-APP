package com.home.activity.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import com.common.adapter.AnimationListenerAdapter;
import com.common.uitl.ListUtiles;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.main.MainActivity_DMX02;
import com.home.base.LedBleActivity;
import com.home.bean.MyColor;
import com.home.bean.SceneBean;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.view.BlackWiteSelectView;
import com.home.view.ColorTextView;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class Dmx02EditColorActivity extends LedBleActivity {
    private static final int COLOR_DEFALUT = 0;
    private static final String TAG = "EditColorActivity";
    public static SceneBean sceneBeanFragment;
    private ColorTextView actionView;
    private BlackWiteSelectView blackWiteSelectView;
    private ImageView buttonBackButton;
    private Button buttonConfirButton;
    private Button buttonRunButton;
    private ArrayList<ColorTextView> colorTextViews;
    private int currentSelecColorFromPicker;
    private ColorPickerView imageViewPicker;
    private MainActivity_DMX02 mainActivity_Dmx02;
    private View relativeTabColorCover;
    private SharedPreferences sp;
    private TextView textRGB;
    private TextView textViewRingBrightSC;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.black), true);
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.dmx02_activity_edit_color);
        sceneBeanFragment = (SceneBean) SharePersistent.getObjectValue(this, CommonConstant.APP_MODE);
        this.mainActivity_Dmx02 = MainActivity_DMX02.getMainActivity();
        this.sp = getSharedPreferences(SharePersistent.getPerference(this, Constant.CUSTOM_DIY_APPKEY), 0);
        this.textRGB = (TextView) findViewById(R.id.tvRGB);
        this.textViewRingBrightSC = (TextView) findViewById(R.id.tvRingBrightnessSC);
        Button button = (Button) findViewById(R.id.buttonRun);
        this.buttonRunButton = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.Dmx02EditColorActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02EditColorActivity dmx02EditColorActivity = Dmx02EditColorActivity.this;
                dmx02EditColorActivity.putDataBack(dmx02EditColorActivity.getSelectColor());
            }
        });
        this.relativeTabColorCover = findViewById(R.id.relativeTabColorCover);
        ImageView imageView = (ImageView) findViewById(R.id.backedit);
        this.buttonBackButton = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.Dmx02EditColorActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02EditColorActivity.this.putDataBack(null);
            }
        });
        initColorBlock();
        initColorSelecterView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putDataBack(ArrayList<MyColor> arrayList) {
        Intent intent = new Intent();
        intent.putExtra("color", arrayList);
        setResult(-1, intent);
        finish();
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
            String str = (String) ((ColorTextView) findViewById.findViewWithTag("editColor" + i)).getTag();
            SharedPreferences sharedPreferences = this.sp;
            if (sharedPreferences != null && sharedPreferences.getInt(str, 0) != 0) {
                z = true;
                break;
            }
            i++;
        }
        for (int i2 = 1; i2 <= 16; i2++) {
            final ColorTextView colorTextView = (ColorTextView) findViewById.findViewWithTag("editColor" + i2);
            String str2 = (String) colorTextView.getTag();
            SharedPreferences sharedPreferences2 = this.sp;
            if (sharedPreferences2 != null) {
                int i3 = sharedPreferences2.getInt(str2, 0);
                float f = 10;
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
            }
            colorTextView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.Dmx02EditColorActivity.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(Dmx02EditColorActivity.this, R.anim.layout_scale));
                    int color = colorTextView.getColor();
                    if (color == 0) {
                        Dmx02EditColorActivity.this.showColorCover((ColorTextView) view);
                    } else {
                        Dmx02EditColorActivity.this.updateRgbText(Tool.getRGB(color));
                    }
                }
            });
            colorTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.activity.other.Dmx02EditColorActivity.4
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    ColorTextView colorTextView2 = (ColorTextView) view;
                    colorTextView2.setColor(0);
                    Dmx02EditColorActivity.this.sp.edit().putInt((String) colorTextView.getTag(), 0).commit();
                    colorTextView2.setBackgroundDrawable(Dmx02EditColorActivity.this.getResources().getDrawable(R.drawable.block_shap_color));
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
        this.relativeTabColorCover.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideColorCover() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        loadAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.home.activity.other.Dmx02EditColorActivity.5
            @Override // com.common.adapter.AnimationListenerAdapter, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                Dmx02EditColorActivity.this.relativeTabColorCover.setVisibility(8);
            }
        });
        this.relativeTabColorCover.startAnimation(loadAnimation);
    }

    private void initColorSelecterView() {
        this.imageViewPicker = (ColorPickerView) findViewById(R.id.imageViewPicker);
        this.blackWiteSelectView = (BlackWiteSelectView) findViewById(R.id.blackWiteSelectView);
        this.imageViewPicker.setInitialColor(getResources().getColor(R.color.white));
        this.imageViewPicker.subscribe(new ColorObserver() { // from class: com.home.activity.other.Dmx02EditColorActivity$$ExternalSyntheticLambda0
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                Dmx02EditColorActivity.this.m14xb6509124(i, z, z2);
            }
        });
        this.blackWiteSelectView.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.activity.other.Dmx02EditColorActivity.6
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                Dmx02EditColorActivity.this.currentSelecColorFromPicker = i;
                int i3 = i2 <= 0 ? 1 : i2;
                if (i2 >= 100) {
                    i3 = 100;
                }
                Dmx02EditColorActivity.this.textViewRingBrightSC.setText(Dmx02EditColorActivity.this.getResources().getString(R.string.brightness_set, Integer.valueOf(i3)));
                MainActivity_DMX02.getMainActivity().setBrightNess(i3, false, false, false);
                if (Dmx02EditColorActivity.sceneBeanFragment != null) {
                    Dmx02EditColorActivity dmx02EditColorActivity = Dmx02EditColorActivity.this;
                    SharePersistent.saveInt(dmx02EditColorActivity, Dmx02EditColorActivity.sceneBeanFragment.getName() + Dmx02EditColorActivity.TAG, i3);
                }
            }
        });
        this.blackWiteSelectView.setProgress(100);
        this.textViewRingBrightSC.setText(getResources().getString(R.string.brightness_set, String.valueOf(100)));
        if (sceneBeanFragment != null) {
            int i = SharePersistent.getInt(this, sceneBeanFragment.getName() + TAG);
            if (i > 0) {
                this.blackWiteSelectView.setProgress(i);
                this.textViewRingBrightSC.setText(getResources().getString(R.string.brightness_set, String.valueOf(i)));
            }
        }
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
        for (int i2 = 1; i2 <= 6; i2++) {
            View findViewWithTag = findViewById.findViewWithTag("viewColor" + i2);
            findViewWithTag.setTag(Integer.valueOf(iArr[i2 + (-1)]));
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.Dmx02EditColorActivity.7
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation(Dmx02EditColorActivity.this.getApplicationContext(), R.anim.layout_scale));
                    int intValue = ((Integer) view.getTag()).intValue();
                    Dmx02EditColorActivity.this.currentSelecColorFromPicker = intValue;
                    Dmx02EditColorActivity.this.blackWiteSelectView.setStartColor(intValue);
                    Dmx02EditColorActivity.this.imageViewPicker.setInitialColor(intValue);
                    int[] rgb = Tool.getRGB(intValue);
                    Dmx02EditColorActivity.this.textRGB.setText(Dmx02EditColorActivity.this.getResources().getString(R.string.r_g_b, Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
                    Dmx02EditColorActivity.this.updateRgbText(Tool.getRGB(intValue));
                }
            });
            arrayList.add(findViewWithTag);
        }
        Button button = (Button) findViewById(R.id.buttonConfirm);
        this.buttonConfirButton = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.Dmx02EditColorActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02EditColorActivity.this.hideColorCover();
                if (Dmx02EditColorActivity.this.currentSelecColorFromPicker == 0) {
                    return;
                }
                float f = 10;
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
                shapeDrawable.getPaint().setColor(Dmx02EditColorActivity.this.currentSelecColorFromPicker);
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                Dmx02EditColorActivity.this.actionView.setColor(Dmx02EditColorActivity.this.currentSelecColorFromPicker);
                Dmx02EditColorActivity.this.actionView.setBackgroundDrawable(shapeDrawable);
                Dmx02EditColorActivity.this.sp.edit().putInt((String) Dmx02EditColorActivity.this.actionView.getTag(), Dmx02EditColorActivity.this.currentSelecColorFromPicker).commit();
                Dmx02EditColorActivity.this.actionView.setText("");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initColorSelecterView$0$com-home-activity-other-Dmx02EditColorActivity  reason: not valid java name */
    public /* synthetic */ void m14xb6509124(int i, boolean z, boolean z2) {
        this.blackWiteSelectView.setStartColor(i);
        this.currentSelecColorFromPicker = i;
        int[] rgb = Tool.getRGB(i);
        this.textRGB.setText(getResources().getString(R.string.r_g_b, Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
        updateRgbText(Tool.getRGB(i));
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.relativeTabColorCover.getVisibility() == 0) {
            hideColorCover();
        } else {
            super.onBackPressed();
        }
    }

    public void updateRgbText(int[] iArr) {
        if (MainActivity_DMX02.getMainActivity() != null) {
            MainActivity_DMX02.getMainActivity().setRgb(iArr[0], iArr[1], iArr[2], false, false, false, false);
        }
    }
}
