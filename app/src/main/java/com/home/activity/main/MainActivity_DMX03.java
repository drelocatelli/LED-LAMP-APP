package com.home.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import com.FirstActivity;
import com.common.gps.GPSPresenter;
import com.common.gps.GPS_Interface;
import com.common.listener.IListener;
import com.common.listener.ListenerManager;
import com.common.uitl.ListUtiles;
import com.common.uitl.LogUtil;
import com.common.uitl.MyReceiver;
import com.common.uitl.SharePersistent;
import com.common.uitl.StringUtils;
import com.common.uitl.Tool;
import com.common.view.SegmentedRadioGroup;
import com.common.view.toast.bamtoast.BamToast;
import com.githang.statusbar.StatusBarCompat;
import com.google.android.material.snackbar.Snackbar;
import com.home.activity.other.DeviceListActivity;
import com.home.activity.set.AuxiliaryActivity;
import com.home.activity.set.ChipSelectActivity;
import com.home.activity.set.CodeActivity;
import com.home.activity.set.VoiceCtlActivity;
import com.home.activity.set.smart.TimerSettingActivity_Smart;
import com.home.adapter.BluetoothDataModel;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.db.Group;
import com.home.db.GroupDevice;
import com.home.db.GroupDeviceDao;
import com.home.fragment.ble.LineFragment;
import com.home.fragment.dmx03.DMX03ModeFragment;
import com.home.fragment.dmx03.DMX03MusicFragment;
import com.home.fragment.dmx03.DMX03RgbFragment;
import com.home.fragment.dmx03.DMX03TimerFragment;
import com.home.fragment.service.ServicesFragment;
import com.home.net.NetConnectBle;
import com.home.net.NetExceptionInterface;
import com.home.utils.ManageFragment;
import com.home.utils.Utils;
import com.home.view.ActionSheet;
import com.home.view.GroupView;
import com.home.view.SlideSwitch;
import com.home.widget.effects.Effectstype;
import com.home.widget.effects.NiftyDialogBuilder;
import com.ledlamp.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.weigan.loopview.MessageHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class MainActivity_DMX03 extends LedBleActivity implements NetExceptionInterface, SensorEventListener, ActionSheet.ActionSheetListener, MyReceiver.MyListener, IListener, GPS_Interface {
    private static final int REQUEST_BLUETOOTH_CONNECT = 225;
    private static final int REQUEST_BLUETOOTH_SCAN = 224;
    private static final int REQUEST_CODE_OPEN_GPS = 222;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 223;
    private static final String TAG = "MainActivity_BLE";
    public static MainActivity_DMX03 mActivity = null;
    public static String sceneBean = null;
    private static SharedPreferences sharedPreferences = null;
    public static String table = "one";
    @BindView(R.id.linearLayoutTopItem)
    LinearLayout TopItem;
    private ArrayList<GroupView> arrayListGroupViews;
    @BindView(R.id.backTextView)
    TextView backTextView;
    private BluetoothManager bluetoothManager;
    private Bitmap bm;
    @BindView(R.id.btnChangeColor)
    Button btnChangeColor;
    @BindView(R.id.btnModePlay)
    Button btnModePlay;
    @BindView(R.id.btnTimerAdd)
    Button btnTimerAdd;
    private Button buttonAddGroup;
    private Button buttonAllOff;
    private Button buttonAllOn;
    private Button bv_sure;
    private TextView changePicTV;
    private TextView codeTV;
    private int currentIndex;
    LinearLayout devices_connect;
    private Dialog dialogConnect;
    private Dialog dialogDisconnect;
    private SharedPreferences.Editor editor;
    private Effectstype effect;
    private FragmentManager fragmentManager;
    private GPSPresenter gps_presenter;
    private Handler handler;
    @BindView(R.id.ivEditColor)
    Button ivEditColor;
    @BindView(R.id.ivLeftMenu)
    ImageView ivLeftMenu;
    @BindView(R.id.ivRightMenu)
    ImageView ivRightMenu;
    private ImageView iv_all11;
    private ImageView iv_all22;
    private Dialog lDialog;
    private RelativeLayout layout;
    @BindView(R.id.left_menu_content_layout)
    LinearLayout left_menu;
    private LinearLayout linearGroups;
    @BindView(R.id.linearLayoutBottom)
    LinearLayout linearLayoutBottom;
    @BindView(R.id.llCommentRight)
    LinearLayout llCommentRight;
    private ListView lv_alldevices;
    private DeviceAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private DrawerLayout mDrawerLayout;
    private DMX03MusicFragment musicFragment;
    @BindView(R.id.onOffButton)
    Button onOffButton;
    private ImageView refreshView;
    private TextView resetTV;
    @BindView(R.id.rgBottomDmx03)
    RadioGroup rgBottomDmx03;
    private DMX03RgbFragment rgbFragment;
    @BindView(R.id.right_menu_frame)
    LinearLayout right_menu;
    @BindView(R.id.rlModeTopDMX00)
    RelativeLayout rlModeTopDMX00;
    @BindView(R.id.rlSetting)
    RelativeLayout rlSetting;
    private RelativeLayout rl_alldevices;
    private RelativeLayout rl_item_dmx;
    private Runnable runnable;
    @BindView(R.id.segmentMusic)
    SegmentedRadioGroup segmentMusic;
    @BindView(R.id.segmentRgb)
    SegmentedRadioGroup segmentRgb;
    private SensorManager sensorManager;
    private TextView setTV;
    private ImageView shakeColorIV;
    private ImageView shakeModelIV;
    private ImageView shakeNoneIV;
    private TextView shakeTV;
    @BindView(R.id.shakeView)
    RelativeLayout shakeView;
    private int soundID;
    private SoundPool soundPool;
    private SharedPreferences sp;
    private TextView textViewAllDeviceIndicater;
    private TextView textViewConnectCount;
    private Intent turnOnBluetoothIntent;
    private TextView tvAuxiliaryDMX;
    private TextView tvTimerDMX;
    @BindView(R.id.tvTopTitle)
    TextView tvTopTitle;
    private TextView tvVoicecontrolDMX;
    private TextView tv_dmx_btn1;
    private TextView tv_dmx_btn2;
    private TextView tv_dmx_btn3;
    private TextView tv_dmx_btn4;
    private boolean canSend = true;
    private List<Fragment> fragmentList = new ArrayList();
    public boolean isLightOpen = true;
    private String groupName = "";
    private int OPEN_BLE = FirstActivity.RESULT333;
    private Random random = new Random();
    private int shakeStyle = 1;
    private int STORAGE_CODE = 112;
    private ImageView imageView = null;
    private final int TAKE_PICTURE = 0;
    private final int CHOOSE_PICTURE = 1;
    final int INT_GO_LIST = 111;
    private boolean isInitGroup = false;
    private boolean isAllOn = true;
    private Map<String, SlideSwitch> map = new HashMap();
    private final int LOCATION_CODE = 110;
    boolean sun = true;
    boolean mon = true;
    boolean tue = true;
    boolean wed = true;
    boolean thu = true;
    boolean fri = true;
    boolean sta = true;
    ArrayList<BluetoothDevice> listDevices = new ArrayList<>();
    int isTime = 1;

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // com.home.view.ActionSheet.ActionSheetListener
    public void onDismiss(ActionSheet actionSheet, boolean z) {
    }

    @Override // com.home.net.NetExceptionInterface
    public void onException(Exception exc) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.transparent), true);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
            getWindow().setStatusBarColor(0);
        }
        setRequestedOrientation(1);
        setContentView(R.layout.dmx03_activity_main);
        getSwipeBackLayout().setEnableGesture(false);
        mActivity = this;
        ListenerManager.getInstance().registerListtener(this);
        sceneBean = (String) getIntent().getSerializableExtra("scene");
        initFragment();
        initSlidingMenu();
        initView();
        if (getImagePath() != "") {
            showImage(getImagePath());
        }
    }

    @Override // com.common.listener.IListener
    public void notifyAllActivity(String str) {
        if (str.equalsIgnoreCase(Constant.UpdateNewFindDevice)) {
            updateNewFindDevice();
        } else if (!str.equalsIgnoreCase(Constant.PasswordSet) || mActivity == null) {
        } else {
            getMainActivity().setPassword();
        }
    }

    public static MainActivity_DMX03 getMainActivity() {
        return mActivity;
    }

    public static String getSceneBean() {
        return sceneBean;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 4) {
            back();
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    @Override // com.common.uitl.MyReceiver.MyListener
    public void onListener(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.text_warming);
        builder.setMessage(R.string.timelow);
        builder.setNegativeButton(R.string.bind_end, (DialogInterface.OnClickListener) null).show();
    }

    private SpannableStringBuilder getBuilder() {
        Locale locale;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.access_request_description));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.home.activity.main.MainActivity_DMX03.1
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://source.android.google.cn/devices/bluetooth/ble"));
                MainActivity_DMX03.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (Build.VERSION.SDK_INT >= 24) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        if (locale.getLanguage().contains("zh")) {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.access_request_description1).length(), getResources().getString(R.string.access_request_description1).length() + getResources().getString(R.string.access_request_description2).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.access_request_description1).length(), getResources().getString(R.string.access_request_description1).length() + getResources().getString(R.string.access_request_description2).length() + 1, 34);
        }
        return spannableStringBuilder;
    }

    @Override // com.common.gps.GPS_Interface
    public void gpsSwitchState(boolean z) {
        if (z) {
            this.layout.setVisibility(8);
        } else {
            this.layout.setVisibility(0);
        }
    }

    private void initSlidingMenu() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerLayout = drawerLayout;
        drawerLayout.setScrimColor(0);
    }

    public void initFragment() {
        DMX03RgbFragment dMX03RgbFragment = new DMX03RgbFragment();
        this.rgbFragment = dMX03RgbFragment;
        this.fragmentList.add(dMX03RgbFragment);
        this.fragmentList.add(new DMX03ModeFragment());
        this.fragmentList.add(new DMX03TimerFragment());
        DMX03MusicFragment dMX03MusicFragment = new DMX03MusicFragment();
        this.musicFragment = dMX03MusicFragment;
        this.fragmentList.add(dMX03MusicFragment);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fragmentManager = supportFragmentManager;
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        for (int i = 0; i < this.fragmentList.size(); i++) {
            beginTransaction.add(R.id.flContent, this.fragmentList.get(i), this.fragmentList.get(i).getClass().getSimpleName());
        }
        beginTransaction.commitAllowingStateLoss();
        ManageFragment.showFragment(this.fragmentManager, this.fragmentList, this.currentIndex);
    }

    private void initView() {
        if (!mActivity.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(mActivity, (int) R.string.ble_not_supported, 0).show();
            Tool.exitApp();
        }
        BluetoothManager bluetoothManager = (BluetoothManager) mActivity.getSystemService("bluetooth");
        this.bluetoothManager = bluetoothManager;
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        this.mBluetoothAdapter = adapter;
        if (adapter == null) {
            Tool.ToastShow(mActivity, (int) R.string.ble_not_supported);
            Tool.exitApp();
        }
        this.textViewAllDeviceIndicater = (TextView) mActivity.findViewById(R.id.textViewAllDeviceIndicater);
        this.textViewConnectCount = (TextView) mActivity.findViewById(R.id.textViewConnectCount);
        this.arrayListGroupViews = new ArrayList<>();
        if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
            this.mDrawerLayout.setDrawerLockMode(1);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            this.gps_presenter = new GPSPresenter(this, this);
            this.layout = (RelativeLayout) findViewById(R.id.location_layout);
            ((TextView) findViewById(R.id.btn_location_enable)).setOnClickListener(new MyOnClickListener());
            ((TextView) findViewById(R.id.btn_location_more)).setOnClickListener(new MyOnClickListener());
        }
        this.imageView = (ImageView) findViewById(R.id.activity_main_imageview);
        if (sceneBean.equalsIgnoreCase("LEDCAR-00-") || sceneBean.equalsIgnoreCase("LEDCAR-01-")) {
            this.imageView.setImageDrawable(getResources().getDrawable(R.drawable.car_bg_all));
        }
        this.linearGroups = (LinearLayout) mActivity.findViewById(R.id.linearLayoutDefineGroups);
        this.TopItem.setOnClickListener(new MyOnClickListener());
        Button button = (Button) mActivity.findViewById(R.id.buttonAllOff);
        this.buttonAllOff = button;
        button.setOnClickListener(new MyOnClickListener());
        Button button2 = (Button) mActivity.findViewById(R.id.buttonAllOn);
        this.buttonAllOn = button2;
        button2.setOnClickListener(new MyOnClickListener());
        Button button3 = (Button) mActivity.findViewById(R.id.buttonAddGroup);
        this.buttonAddGroup = button3;
        button3.setOnClickListener(new MyOnClickListener());
        ImageView imageView = (ImageView) mActivity.findViewById(R.id.ivRefresh);
        this.refreshView = imageView;
        imageView.setOnClickListener(new MyOnClickListener());
        this.llCommentRight.setVisibility(0);
        this.rl_item_dmx = (RelativeLayout) mActivity.findViewById(R.id.rl_item_dmx);
        TextView textView = (TextView) mActivity.findViewById(R.id.tvTimerDMX);
        this.tvTimerDMX = textView;
        textView.setOnClickListener(new MyOnClickListener());
        TextView textView2 = (TextView) mActivity.findViewById(R.id.tvVoicecontrolDMX);
        this.tvVoicecontrolDMX = textView2;
        textView2.setOnClickListener(new MyOnClickListener());
        TextView textView3 = (TextView) mActivity.findViewById(R.id.code_tv);
        this.codeTV = textView3;
        textView3.setOnClickListener(new MyOnClickListener());
        TextView textView4 = (TextView) mActivity.findViewById(R.id.tv_auxiliary);
        this.tvAuxiliaryDMX = textView4;
        textView4.setOnClickListener(new MyOnClickListener());
        TextView textView5 = (TextView) mActivity.findViewById(R.id.tv_dmx_btn1);
        this.tv_dmx_btn1 = textView5;
        textView5.setOnClickListener(new MyOnClickListener());
        TextView textView6 = (TextView) mActivity.findViewById(R.id.tv_dmx_btn2);
        this.tv_dmx_btn2 = textView6;
        textView6.setOnClickListener(new MyOnClickListener());
        TextView textView7 = (TextView) mActivity.findViewById(R.id.tv_dmx_btn3);
        this.tv_dmx_btn3 = textView7;
        textView7.setOnClickListener(new MyOnClickListener());
        TextView textView8 = (TextView) mActivity.findViewById(R.id.tv_dmx_btn4);
        this.tv_dmx_btn4 = textView8;
        textView8.setOnClickListener(new MyOnClickListener());
        if (sceneBean.equalsIgnoreCase("LEDDMX-00-")) {
            this.tvVoicecontrolDMX.setVisibility(8);
            this.codeTV.setVisibility(8);
        }
        this.backTextView.setOnClickListener(new MyOnClickListener());
        this.onOffButton.setOnClickListener(new MyOnClickListener());
        TextView textView9 = (TextView) mActivity.findViewById(R.id.change_under_pic_tv);
        this.changePicTV = textView9;
        textView9.setOnClickListener(new MyOnClickListener());
        TextView textView10 = (TextView) mActivity.findViewById(R.id.reset_tv);
        this.resetTV = textView10;
        textView10.setOnClickListener(new MyOnClickListener());
        TextView textView11 = (TextView) mActivity.findViewById(R.id.shake_tv);
        this.shakeTV = textView11;
        textView11.setOnClickListener(new MyOnClickListener());
        TextView textView12 = (TextView) mActivity.findViewById(R.id.set_tv);
        this.setTV = textView12;
        textView12.setOnClickListener(new MyOnClickListener());
        ImageView imageView2 = (ImageView) mActivity.findViewById(R.id.shake_one_iv);
        this.shakeColorIV = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.setBackgroundResource(R.drawable.bg_in_press);
                MainActivity_DMX03.this.shakeNoneIV.setBackgroundResource(R.drawable.bg_shake_green);
                MainActivity_DMX03.this.shakeModelIV.setBackgroundResource(R.drawable.bg_shake_green);
                MainActivity_DMX03.this.shakeView.setBackgroundResource(R.drawable.bg_shake_green);
                MainActivity_DMX03.this.shakeStyle = 0;
            }
        });
        ImageView imageView3 = (ImageView) mActivity.findViewById(R.id.shake_two_iv);
        this.shakeNoneIV = imageView3;
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.setBackgroundResource(R.drawable.bg_in_press);
                MainActivity_DMX03.this.shakeColorIV.setBackgroundResource(R.drawable.bg_shake_lightgray);
                MainActivity_DMX03.this.shakeModelIV.setBackgroundResource(R.drawable.bg_shake_lightgray);
                MainActivity_DMX03.this.shakeView.setBackgroundResource(R.drawable.bg_shake_lightgray);
                MainActivity_DMX03.this.shakeStyle = 1;
            }
        });
        ImageView imageView4 = (ImageView) mActivity.findViewById(R.id.shake_three_iv);
        this.shakeModelIV = imageView4;
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.setBackgroundResource(R.drawable.bg_in_press);
                MainActivity_DMX03.this.shakeColorIV.setBackgroundResource(R.drawable.bg_shake_orange);
                MainActivity_DMX03.this.shakeNoneIV.setBackgroundResource(R.drawable.bg_shake_orange);
                MainActivity_DMX03.this.shakeView.setBackgroundResource(R.drawable.bg_shake_orange);
                MainActivity_DMX03.this.shakeStyle = 2;
            }
        });
        this.shakeNoneIV.performClick();
        this.sensorManager = (SensorManager) getSystemService("sensor");
        if (Build.VERSION.SDK_INT >= 21) {
            this.soundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(4).build()).build();
        } else {
            this.soundPool = new SoundPool(10, 3, 1);
        }
        this.soundID = this.soundPool.load(this, R.raw.dang, 1);
        SharedPreferences sharedPreferences2 = getSharedPreferences(Constant.MODLE_TYPE, 0);
        this.sp = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
        this.currentIndex = 0;
        this.ivLeftMenu.setOnClickListener(new MyOnClickListener());
        this.ivRightMenu.setOnClickListener(new MyOnClickListener());
        if (sceneBean.equalsIgnoreCase("LEDDMX-03-")) {
            this.rgBottomDmx03.setVisibility(0);
            this.rgBottomDmx03.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.activity.main.MainActivity_DMX03.5
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    MainActivity_DMX03.this.ivLeftMenu.setVisibility(8);
                    MainActivity_DMX03.this.textViewConnectCount.setVisibility(8);
                    switch (i) {
                        case R.id.rbModeDmx03 /* 2131297177 */:
                            MainActivity_DMX03.this.isTime = 1;
                            MainActivity_DMX03.this.currentIndex = 1;
                            MainActivity_DMX03.this.segmentMusic.setVisibility(8);
                            MainActivity_DMX03.this.onOffButton.setVisibility(8);
                            MainActivity_DMX03.this.btnModePlay.setVisibility(0);
                            MainActivity_DMX03.this.btnTimerAdd.setVisibility(8);
                            MainActivity_DMX03.this.btnChangeColor.setVisibility(8);
                            MainActivity_DMX03.this.ivEditColor.setVisibility(8);
                            MainActivity_DMX03.this.segmentRgb.setVisibility(8);
                            MainActivity_DMX03.this.editor.putInt(Constant.MODLE_VALUE, MainActivity_DMX03.this.currentIndex);
                            MainActivity_DMX03.this.editor.commit();
                            MainActivity_DMX03.this.onOffButton.setBackgroundResource(R.drawable.on_btn);
                            MainActivity_DMX03.this.tvTopTitle.setVisibility(0);
                            MainActivity_DMX03.this.tvTopTitle.setText(MainActivity_DMX03.this.getString(R.string.tab_mode));
                            MainActivity_DMX03.this.pauseMusicAndVolum(true);
                            break;
                        case R.id.rbMusicDmx03 /* 2131297184 */:
                            MainActivity_DMX03.this.isTime = 1;
                            MainActivity_DMX03.this.currentIndex = 3;
                            MainActivity_DMX03.this.segmentMusic.setVisibility(0);
                            MainActivity_DMX03.this.onOffButton.setVisibility(8);
                            MainActivity_DMX03.this.btnModePlay.setVisibility(8);
                            MainActivity_DMX03.this.btnTimerAdd.setVisibility(8);
                            MainActivity_DMX03.this.segmentRgb.setVisibility(8);
                            if (MainActivity_DMX03.this.musicFragment.isCheckSegmentedRadioGroupIndexTwo()) {
                                MainActivity_DMX03.this.ivEditColor.setVisibility(0);
                                MainActivity_DMX03.this.btnChangeColor.setVisibility(8);
                            } else {
                                MainActivity_DMX03.this.ivEditColor.setVisibility(8);
                                MainActivity_DMX03.this.btnChangeColor.setVisibility(8);
                            }
                            MainActivity_DMX03.this.onOffButton.setBackgroundResource(R.drawable.on_btn);
                            MainActivity_DMX03.this.tvTopTitle.setVisibility(8);
                            MainActivity_DMX03.this.musicFragment.startMusice();
                            break;
                        case R.id.rbRGBDmx03 /* 2131297191 */:
                            MainActivity_DMX03.this.currentIndex = 0;
                            MainActivity_DMX03.this.ivLeftMenu.setVisibility(0);
                            MainActivity_DMX03.this.textViewConnectCount.setVisibility(0);
                            MainActivity_DMX03.this.segmentMusic.setVisibility(8);
                            MainActivity_DMX03.this.onOffButton.setVisibility(0);
                            MainActivity_DMX03.this.btnModePlay.setVisibility(8);
                            MainActivity_DMX03.this.btnTimerAdd.setVisibility(8);
                            MainActivity_DMX03.this.btnChangeColor.setVisibility(8);
                            MainActivity_DMX03.this.ivEditColor.setVisibility(8);
                            MainActivity_DMX03.this.editor.putInt(Constant.MODLE_VALUE, MainActivity_DMX03.this.currentIndex);
                            MainActivity_DMX03.this.segmentRgb.setVisibility(8);
                            MainActivity_DMX03.this.editor.commit();
                            MainActivity_DMX03.this.onOffButton.setBackgroundResource(R.drawable.on_btn);
                            MainActivity_DMX03.this.tvTopTitle.setVisibility(0);
                            MainActivity_DMX03.this.tvTopTitle.setText(MainActivity_DMX03.this.getString(R.string.tab_reg));
                            MainActivity_DMX03.this.pauseMusicAndVolum(true);
                            break;
                        case R.id.rbTimerDmx03 /* 2131297204 */:
                            MainActivity_DMX03.this.isTime = 1;
                            MainActivity_DMX03.this.currentIndex = 2;
                            MainActivity_DMX03.this.segmentMusic.setVisibility(8);
                            MainActivity_DMX03.this.onOffButton.setVisibility(8);
                            MainActivity_DMX03.this.btnModePlay.setVisibility(8);
                            MainActivity_DMX03.this.btnTimerAdd.setVisibility(8);
                            MainActivity_DMX03.this.btnChangeColor.setVisibility(8);
                            MainActivity_DMX03.this.ivEditColor.setVisibility(8);
                            MainActivity_DMX03.this.segmentRgb.setVisibility(8);
                            MainActivity_DMX03.this.editor.putInt(Constant.MODLE_VALUE, MainActivity_DMX03.this.currentIndex);
                            MainActivity_DMX03.this.editor.commit();
                            MainActivity_DMX03.this.onOffButton.setBackgroundResource(R.drawable.on_btn);
                            MainActivity_DMX03.this.tvTopTitle.setVisibility(0);
                            MainActivity_DMX03.this.tvTopTitle.setText(MainActivity_DMX03.this.getString(R.string.tab_timer));
                            MainActivity_DMX03.this.pauseMusicAndVolum(true);
                            break;
                    }
                    MainActivity_DMX03.this.editor.putInt(Constant.MODLE_VALUE, MainActivity_DMX03.this.currentIndex);
                    MainActivity_DMX03.this.editor.commit();
                    ManageFragment.showFragment(MainActivity_DMX03.this.fragmentManager, MainActivity_DMX03.this.fragmentList, MainActivity_DMX03.this.currentIndex);
                }
            });
            this.rgBottomDmx03.check(R.id.rbRGBDmx03);
        }
        this.iv_all11 = (ImageView) findViewById(R.id.iv_all11);
        this.iv_all22 = (ImageView) findViewById(R.id.iv_all22);
        this.iv_all11.setOnClickListener(new MyOnClickListener());
        this.iv_all22.setOnClickListener(new MyOnClickListener());
        this.bv_sure = (Button) findViewById(R.id.bv_sure);
        this.lv_alldevices = (ListView) findViewById(R.id.lv_alldevices);
        DeviceAdapter deviceAdapter = new DeviceAdapter();
        this.mAdapter = deviceAdapter;
        this.lv_alldevices.setAdapter((ListAdapter) deviceAdapter);
        this.lv_alldevices.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.6
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                BluetoothDataModel bluetoothDataModel = LedBleApplication.getApp().getManmualBleDevices().get(i);
                bluetoothDataModel.setSeleted(!bluetoothDataModel.isSeleted());
                MainActivity_DMX03.this.mAdapter.notifyDataSetChanged();
            }
        });
        this.rl_alldevices = (RelativeLayout) findViewById(R.id.rl_alldevices);
        this.devices_connect = (LinearLayout) findViewById(R.id.devices_connect);
        this.bv_sure.setOnClickListener(new MyOnClickListener());
        if (SharePersistent.getPerference(getMainActivity(), Constant.AUTO_OR_MANUAL).equalsIgnoreCase("MANUAL")) {
            LedBleApplication.getApp().setAuto(false);
            LedBleApplication.getApp().setCanConnect(false);
            this.rl_alldevices.setVisibility(0);
            this.iv_all22.setVisibility(0);
            this.devices_connect.setVisibility(8);
            this.iv_all11.setVisibility(8);
            this.linearLayoutBottom.setVisibility(8);
        } else {
            LedBleApplication.getApp().setAuto(true);
            LedBleApplication.getApp().setCanConnect(true);
            this.iv_all11.setVisibility(0);
            this.iv_all22.setVisibility(8);
        }
        if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
            return;
        }
        initBleScanList(this.isAllOn);
    }

    public void pauseMusicAndVolum(boolean z) {
        DMX03MusicFragment dMX03MusicFragment = this.musicFragment;
        if (dMX03MusicFragment != null) {
            dMX03MusicFragment.pauseMusic();
            this.musicFragment.pauseVolum(true);
        }
    }

    public void back() {
        if (getMainActivity() != null) {
            SharePersistent.savePerference(getMainActivity(), Constant.CUSTOM_DIY_APPKEY, (String) null);
            LedBleApplication.getApp().setAuto(false);
        }
        finish();
    }

    /* loaded from: classes.dex */
    public class MyOnClickListener implements View.OnClickListener {
        public MyOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MainActivity_DMX03.this.pauseMusicAndVolum(true);
            switch (view.getId()) {
                case R.id.backTextView /* 2131296338 */:
                    MainActivity_DMX03.this.back();
                    return;
                case R.id.btn_location_enable /* 2131296412 */:
                    MainActivity_DMX03.this.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 222);
                    return;
                case R.id.btn_location_more /* 2131296413 */:
                    if (MainActivity_DMX03.mActivity == null || MainActivity_DMX03.mActivity.isFinishing()) {
                        return;
                    }
                    new AlertDialog.Builder(MainActivity_DMX03.mActivity).setTitle(MainActivity_DMX03.this.getResources().getString(R.string.position_service)).setMessage(MainActivity_DMX03.this.getResources().getString(R.string.position_service_info)).setPositiveButton(MainActivity_DMX03.this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.MyOnClickListener.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                    return;
                case R.id.buttonAddGroup /* 2131296424 */:
                    MainActivity_DMX03.this.addGroupMessage();
                    MainActivity_DMX03.this.startAnimation(view);
                    return;
                case R.id.buttonAllOff /* 2131296425 */:
                    MainActivity_DMX03.this.allOff();
                    MainActivity_DMX03.this.startAnimation(view);
                    return;
                case R.id.buttonAllOn /* 2131296426 */:
                    MainActivity_DMX03.this.allOn();
                    MainActivity_DMX03.this.startAnimation(view);
                    return;
                case R.id.bv_sure /* 2131296449 */:
                    MainActivity_DMX03.this.showConnect();
                    LedBleApplication.getApp().setCanConnect(true);
                    new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.MyOnClickListener.4
                        @Override // java.lang.Runnable
                        public void run() {
                            if (MainActivity_DMX03.getMainActivity() == null || MainActivity_DMX03.getMainActivity().isFinishing() || MainActivity_DMX03.this.dialogConnect == null) {
                                return;
                            }
                            MainActivity_DMX03.this.dialogConnect.dismiss();
                            MainActivity_DMX03.this.dialogConnect = null;
                        }
                    }, 4000L);
                    if (LedBleApplication.getApp().getHashMapConnect().size() > 0) {
                        for (final String str : LedBleApplication.getApp().getHashMapConnect().keySet()) {
                            new Thread(new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.MyOnClickListener.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    LedBleApplication.getApp().clearBleGatMap(str);
                                }
                            }).start();
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        LedBleApplication.getApp().getBleGattMap().clear();
                        new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.MyOnClickListener.6
                            @Override // java.lang.Runnable
                            public void run() {
                                LedBleApplication.getApp().getBleDevices().clear();
                                MainActivity_DMX03.this.listDevices.clear();
                                Iterator<BluetoothDataModel> it = LedBleApplication.getApp().getManmualBleDevices().iterator();
                                while (it.hasNext()) {
                                    BluetoothDataModel next = it.next();
                                    if (next.isSeleted()) {
                                        MainActivity_DMX03.this.listDevices.add(next.getDevice());
                                    }
                                }
                                LedBleApplication.getApp().getBleDevices().addAll(MainActivity_DMX03.this.listDevices);
                                ListenerManager.getInstance().sendBroadCast(Constant.ManualModeConnectDevice);
                            }
                        }, 2000L);
                        return;
                    }
                    LedBleApplication.getApp().getBleDevices().clear();
                    MainActivity_DMX03.this.listDevices.clear();
                    Iterator<BluetoothDataModel> it = LedBleApplication.getApp().getManmualBleDevices().iterator();
                    while (it.hasNext()) {
                        BluetoothDataModel next = it.next();
                        if (next.isSeleted()) {
                            MainActivity_DMX03.this.listDevices.add(next.getDevice());
                        }
                    }
                    LedBleApplication.getApp().getBleDevices().addAll(MainActivity_DMX03.this.listDevices);
                    ListenerManager.getInstance().sendBroadCast(Constant.ManualModeConnectDevice);
                    return;
                case R.id.change_under_pic_tv /* 2131296502 */:
                    MainActivity_DMX03.this.showPicturePicker();
                    return;
                case R.id.code_tv /* 2131296531 */:
                    MainActivity_DMX03.this.startActivity(new Intent(MainActivity_DMX03.mActivity, CodeActivity.class));
                    return;
                case R.id.ivLeftMenu /* 2131296768 */:
                    MainActivity_DMX03.this.mDrawerLayout.openDrawer(MainActivity_DMX03.this.left_menu);
                    return;
                case R.id.ivRefresh /* 2131296775 */:
                    if (LedBleApplication.getApp().isAuto()) {
                        LedBleApplication.getApp().setCanConnect(true);
                    } else {
                        LedBleApplication.getApp().setCanConnect(false);
                        for (int i = 0; i < LedBleApplication.getApp().getManmualBleDevices().size(); i++) {
                            if (!LedBleApplication.getApp().getManmualBleDevices().get(i).isSeleted()) {
                                LedBleApplication.getApp().getManmualBleDevices().remove(i);
                            }
                        }
                        if (MainActivity_DMX03.this.mAdapter != null) {
                            MainActivity_DMX03.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                    MainActivity_DMX03.this.refreshDevices(true);
                    MainActivity_DMX03.this.startAnimation(view);
                    return;
                case R.id.ivRightMenu /* 2131296778 */:
                    MainActivity_DMX03.this.mDrawerLayout.openDrawer(MainActivity_DMX03.this.right_menu);
                    return;
                case R.id.iv_all11 /* 2131296795 */:
                    MainActivity_DMX03.this.backTextView.setVisibility(4);
                    LedBleApplication.getApp().setAuto(false);
                    MainActivity_DMX03.this.rl_alldevices.setVisibility(0);
                    MainActivity_DMX03.this.iv_all22.setVisibility(0);
                    MainActivity_DMX03.this.devices_connect.setVisibility(8);
                    MainActivity_DMX03.this.iv_all11.setVisibility(8);
                    MainActivity_DMX03.this.linearLayoutBottom.setVisibility(8);
                    MainActivity_DMX03.this.clearList();
                    new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.MyOnClickListener.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (MainActivity_DMX03.getMainActivity() == null || MainActivity_DMX03.getMainActivity().isFinishing()) {
                                return;
                            }
                            if (MainActivity_DMX03.this.dialogDisconnect != null) {
                                MainActivity_DMX03.this.dialogDisconnect.dismiss();
                                MainActivity_DMX03.this.dialogDisconnect = null;
                            }
                            LedBleApplication.getApp().setCanConnect(false);
                            MainActivity_DMX03.this.refreshDevices(true);
                            MainActivity_DMX03.this.backTextView.setVisibility(0);
                        }
                    }, 2000L);
                    SharePersistent.savePerference(MainActivity_DMX03.getMainActivity(), Constant.AUTO_OR_MANUAL, "MANUAL");
                    return;
                case R.id.iv_all22 /* 2131296796 */:
                    MainActivity_DMX03.this.backTextView.setVisibility(4);
                    LedBleApplication.getApp().setAuto(true);
                    MainActivity_DMX03.this.devices_connect.setVisibility(0);
                    MainActivity_DMX03.this.iv_all11.setVisibility(0);
                    MainActivity_DMX03.this.rl_alldevices.setVisibility(8);
                    MainActivity_DMX03.this.iv_all22.setVisibility(8);
                    MainActivity_DMX03.this.linearLayoutBottom.setVisibility(0);
                    MainActivity_DMX03.this.clearList();
                    new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.MyOnClickListener.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (MainActivity_DMX03.this.dialogDisconnect != null) {
                                MainActivity_DMX03.this.dialogDisconnect.dismiss();
                                MainActivity_DMX03.this.dialogDisconnect = null;
                            }
                            LedBleApplication.getApp().setCanConnect(true);
                            MainActivity_DMX03.this.refreshDevices(true);
                            MainActivity_DMX03.this.backTextView.setVisibility(0);
                        }
                    }, 2000L);
                    SharePersistent.savePerference(MainActivity_DMX03.getMainActivity(), Constant.AUTO_OR_MANUAL, "AUTO");
                    return;
                case R.id.linearLayoutTopItem /* 2131296911 */:
                    MainActivity_DMX03.this.showActionSheet("");
                    return;
                case R.id.onOffButton /* 2131297111 */:
                    if (MainActivity_DMX03.this.isTime == 2) {
                        if (LineFragment.listDate_line.size() < 24) {
                            Intent intent = new Intent(MainActivity_DMX03.mActivity, TimerSettingActivity_Smart.class);
                            intent.putExtra("tag", LineFragment.listDate_line.size() + 1);
                            MainActivity_DMX03.this.startActivityForResult(intent, 113);
                            return;
                        }
                        Toast.makeText(MainActivity_DMX03.mActivity, (int) R.string.timeSize, 1).show();
                        return;
                    } else if (MainActivity_DMX03.this.isLightOpen) {
                        MainActivity_DMX03.this.onOffButton.setBackgroundResource(R.drawable.off_btn);
                        if (MainActivity_DMX03.sceneBean.contains(CommonConstant.LEDBLE) || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDDMX-01-") || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDDMX-00-") || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDCAR-00-") || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDCAR-01-")) {
                            if (!MainActivity_DMX03.this.rgbFragment.checkRelativeTab3() || MainActivity_DMX03.this.currentIndex != 0) {
                                if (MainActivity_DMX03.this.rgbFragment.checkRelativeTabDMXAisle()) {
                                    MainActivity_DMX03 mainActivity_DMX03 = MainActivity_DMX03.this;
                                    mainActivity_DMX03.close(true, mainActivity_DMX03.rgbFragment.isCAR01DMX(), MainActivity_DMX03.this.rgbFragment.isCAR01LED());
                                } else {
                                    MainActivity_DMX03 mainActivity_DMX032 = MainActivity_DMX03.this;
                                    mainActivity_DMX032.close(false, mainActivity_DMX032.rgbFragment.isCAR01DMX(), MainActivity_DMX03.this.rgbFragment.isCAR01LED());
                                }
                            } else {
                                MainActivity_DMX03.this.bledmxclose();
                            }
                        } else if (MainActivity_DMX03.this.rgbFragment != null) {
                            MainActivity_DMX03 mainActivity_DMX033 = MainActivity_DMX03.this;
                            mainActivity_DMX033.close(false, mainActivity_DMX033.rgbFragment.isCAR01DMX(), MainActivity_DMX03.this.rgbFragment.isCAR01LED());
                        } else {
                            MainActivity_DMX03.this.close(false, false, false);
                        }
                        MainActivity_DMX03.this.isLightOpen = false;
                        return;
                    } else {
                        MainActivity_DMX03.this.onOffButton.setBackgroundResource(R.drawable.on_btn);
                        if (MainActivity_DMX03.sceneBean.contains(CommonConstant.LEDBLE) || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDDMX-00-") || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDDMX-01-") || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDCAR-00-") || MainActivity_DMX03.sceneBean.equalsIgnoreCase("LEDCAR-01-")) {
                            if (!MainActivity_DMX03.this.rgbFragment.checkRelativeTab3() || MainActivity_DMX03.this.currentIndex != 0) {
                                if (MainActivity_DMX03.this.rgbFragment.checkRelativeTabDMXAisle()) {
                                    MainActivity_DMX03 mainActivity_DMX034 = MainActivity_DMX03.this;
                                    mainActivity_DMX034.open(true, mainActivity_DMX034.rgbFragment.isCAR01DMX(), MainActivity_DMX03.this.rgbFragment.isCAR01LED());
                                } else {
                                    MainActivity_DMX03 mainActivity_DMX035 = MainActivity_DMX03.this;
                                    mainActivity_DMX035.open(false, mainActivity_DMX035.rgbFragment.isCAR01DMX(), MainActivity_DMX03.this.rgbFragment.isCAR01LED());
                                }
                            } else {
                                MainActivity_DMX03.this.bledmxopen();
                            }
                        } else if (MainActivity_DMX03.this.rgbFragment != null) {
                            MainActivity_DMX03 mainActivity_DMX036 = MainActivity_DMX03.this;
                            mainActivity_DMX036.open(false, mainActivity_DMX036.rgbFragment.isCAR01DMX(), MainActivity_DMX03.this.rgbFragment.isCAR01LED());
                        } else {
                            MainActivity_DMX03.this.open(false, false, false);
                        }
                        MainActivity_DMX03.this.isLightOpen = true;
                        return;
                    }
                case R.id.reset_tv /* 2131297262 */:
                    MainActivity_DMX03.this.imageView.setImageDrawable(MainActivity_DMX03.this.getResources().getDrawable(R.drawable.bg_all));
                    MainActivity_DMX03.this.getImagePath();
                    MainActivity_DMX03.this.saveImagePathToSharedPreferences("");
                    return;
                case R.id.set_tv /* 2131297473 */:
                    MainActivity_DMX03.this.startActivity(new Intent(MainActivity_DMX03.mActivity, ChipSelectActivity.class));
                    return;
                case R.id.tvVoicecontrolDMX /* 2131297826 */:
                    MainActivity_DMX03.this.startActivity(new Intent(MainActivity_DMX03.mActivity, VoiceCtlActivity.class));
                    return;
                case R.id.tv_auxiliary /* 2131297842 */:
                    MainActivity_DMX03.this.startActivity(new Intent(MainActivity_DMX03.mActivity, AuxiliaryActivity.class));
                    return;
                case R.id.tv_dmx_btn1 /* 2131297882 */:
                    MainActivity_DMX03.this.setAuxiliary(0, view);
                    return;
                case R.id.tv_dmx_btn2 /* 2131297883 */:
                    MainActivity_DMX03.this.setAuxiliary(1, view);
                    return;
                case R.id.tv_dmx_btn3 /* 2131297884 */:
                    MainActivity_DMX03.this.setAuxiliary(2, view);
                    return;
                case R.id.tv_dmx_btn4 /* 2131297885 */:
                    MainActivity_DMX03.this.setAuxiliary(3, view);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        if (getApplicationContext() != null) {
            view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.layout_scale));
        }
    }

    public static void showToast(Context context, String str) {
        BamToast.showText(context, str);
    }

    public void clearList() {
        if (LedBleApplication.getApp().getBleGattMap().size() > 0) {
            showDisconnect();
            for (final String str : LedBleApplication.getApp().getBleGattMap().keySet()) {
                new Thread(new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.7
                    @Override // java.lang.Runnable
                    public void run() {
                        LedBleApplication.getApp().clearBleGatMap(str);
                    }
                }).start();
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LedBleApplication.getApp().getBleGattMap().clear();
        }
        LedBleApplication.getApp().getBleDevices().clear();
        LedBleApplication.getApp().getHashMapConnect().clear();
        LedBleApplication.getApp().getManmualBleDevices().clear();
        this.mAdapter.notifyDataSetChanged();
        updateNewFindDevice();
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        private ImageView imageView;
        private TextView textView;

        ViewHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DeviceAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        private DeviceAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return LedBleApplication.getApp().getManmualBleDevices().size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            BluetoothDataModel bluetoothDataModel = LedBleApplication.getApp().getManmualBleDevices().get(i);
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = LayoutInflater.from(MainActivity_DMX03.this).inflate(R.layout.listlayout, (ViewGroup) null);
                viewHolder.imageView = (ImageView) view2.findViewById(R.id.iv_sure);
                viewHolder.textView = (TextView) view2.findViewById(R.id.tv);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (bluetoothDataModel.isSeleted()) {
                viewHolder.imageView.setVisibility(0);
            } else {
                viewHolder.imageView.setVisibility(8);
            }
            viewHolder.textView.setText(bluetoothDataModel.getDevice().getName());
            return view2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v25 */
    /* JADX WARN: Type inference failed for: r0v26 */
    /* JADX WARN: Type inference failed for: r0v5, types: [int] */
    public int getweek() {
        boolean z = this.mon;
        boolean z2 = z;
        if (this.tue) {
            z2 = (z ? 1 : 0) | true;
        }
        boolean z3 = z2;
        if (this.wed) {
            z3 = (z2 ? 1 : 0) | true;
        }
        boolean z4 = z3;
        if (this.thu) {
            z4 = (z3 ? 1 : 0) | true;
        }
        boolean z5 = z4;
        if (this.fri) {
            z5 = (z4 ? 1 : 0) | true;
        }
        ?? r0 = z5;
        if (this.sta) {
            r0 = (z5 ? 1 : 0) | true;
        }
        return this.sun ? r0 | 64 : r0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showConnect() {
        MainActivity_DMX03 mainActivity_DMX03 = mActivity;
        if (mainActivity_DMX03 == null || mainActivity_DMX03.isFinishing() || this.dialogConnect != null) {
            return;
        }
        Dialog dialog = new Dialog(mActivity, 16973840);
        this.dialogConnect = dialog;
        dialog.requestWindowFeature(1024);
        this.dialogConnect.setContentView(R.layout.dialogview_connect);
        ((TextView) this.dialogConnect.findViewById(R.id.dialodTv)).setText(getString(R.string.connecting));
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(2000L);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(1);
        rotateAnimation.setStartTime(-1L);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        ((ImageView) this.dialogConnect.findViewById(R.id.imageViewWait)).startAnimation(rotateAnimation);
        Dialog dialog2 = this.dialogConnect;
        if (dialog2 != null) {
            dialog2.show();
        }
    }

    private void showDisconnect() {
        MainActivity_DMX03 mainActivity_DMX03 = mActivity;
        if (mainActivity_DMX03 == null || mainActivity_DMX03.isFinishing() || this.dialogDisconnect != null) {
            return;
        }
        Dialog dialog = new Dialog(mActivity, 16973840);
        this.dialogDisconnect = dialog;
        dialog.requestWindowFeature(1024);
        this.dialogDisconnect.setContentView(R.layout.dialogview_scan);
        ((TextView) this.dialogDisconnect.findViewById(R.id.dialodTv)).setText(getString(R.string.disconnect));
        ((ImageView) this.dialogDisconnect.findViewById(R.id.imageViewWait)).setVisibility(8);
        Dialog dialog2 = this.dialogDisconnect;
        if (dialog2 != null) {
            dialog2.show();
        }
    }

    private void showCustomMessage() {
        MainActivity_DMX03 mainActivity_DMX03 = mActivity;
        if (mainActivity_DMX03 == null || mainActivity_DMX03.isFinishing() || this.lDialog != null) {
            return;
        }
        Dialog dialog = new Dialog(this, 16973840);
        this.lDialog = dialog;
        dialog.requestWindowFeature(1024);
        this.lDialog.setContentView(R.layout.dialogview_scan);
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(2000L);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(1);
        rotateAnimation.setStartTime(-1L);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        ((ImageView) this.lDialog.findViewById(R.id.imageViewWait)).startAnimation(rotateAnimation);
        this.lDialog.show();
    }

    private void showSure(View view) {
        if (mActivity != null) {
            startAnimation(view);
        }
    }

    private boolean isAndroid12() {
        return Build.VERSION.SDK_INT >= 31;
    }

    public void turnOnBluetooth() {
        if (isAndroid12()) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_SCAN") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_CONNECT") != 0) {
                requestPermissions(new String[]{"android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT"}, REQUEST_BLUETOOTH_SCAN);
            } else if (this.turnOnBluetoothIntent == null) {
                Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.turnOnBluetoothIntent = intent;
                startActivityForResult(intent, this.OPEN_BLE);
            }
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_PERMISSION_LOCATION);
        } else if (this.turnOnBluetoothIntent == null) {
            Intent intent2 = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            this.turnOnBluetoothIntent = intent2;
            startActivityForResult(intent2, this.OPEN_BLE);
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION")) {
            MainActivity_DMX03 mainActivity_DMX03 = mActivity;
            if (mainActivity_DMX03 == null || mainActivity_DMX03.isFinishing()) {
                return;
            }
            final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(mActivity, R.style.dialog_user_agreement_and_privacy_policy);
            this.effect = Effectstype.SlideBottom;
            niftyDialogBuilder.setCancelable(false);
            niftyDialogBuilder.withTitle(getResources().getString(R.string.access_request)).withTitleColor("#000000").withDividerColor("#11000000").isCancelableOnTouchOutside(false).withDuration(200).withEffect(this.effect).withButton1Text(getResources().getString(R.string.cancel)).withButton2Text(getResources().getString(R.string.agree));
            niftyDialogBuilder.setCustomView(R.layout.activity_spannable, getApplicationContext());
            TextView textView = (TextView) niftyDialogBuilder.getContentView().findViewById(R.id.span_builder);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setHighlightColor(getResources().getColor(17170445));
            textView.setText(getBuilder());
            niftyDialogBuilder.setButton1Click(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.9
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (niftyDialogBuilder.isShowing()) {
                        niftyDialogBuilder.dismiss();
                    }
                }
            }).setButton2Click(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.8
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (niftyDialogBuilder.isShowing()) {
                        niftyDialogBuilder.dismiss();
                    }
                    ActivityCompat.requestPermissions(MainActivity_DMX03.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, MainActivity_DMX03.REQUEST_CODE_PERMISSION_LOCATION);
                }
            }).show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_PERMISSION_LOCATION);
    }

    public void initBleScanList(boolean z) {
        if (!this.isInitGroup) {
            initGroup(z);
            this.isInitGroup = true;
        }
        if (LedBleApplication.getApp().getBleGattMap().size() > 0) {
            updateNewFindDevice();
        } else {
            refreshDevices(true);
        }
    }

    protected void refreshDevices(final boolean z) {
        if (ServicesFragment.getBluetoothAdapter() != null && !ServicesFragment.getBluetoothAdapter().isEnabled()) {
            turnOnBluetooth();
        } else if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            requestLocationPermission();
        } else {
            if (z) {
                showCustomMessage();
            }
            startLeScan(z);
            new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.10
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_DMX03.this.stopLeScan(z);
                }
            }, 6000L);
        }
    }

    protected void startLeScan(boolean z) {
        ListenerManager.getInstance().sendBroadCast(Constant.StartLeScan);
        if (z) {
            showCustomMessage();
        }
    }

    protected void stopLeScan(boolean z) {
        ListenerManager.getInstance().sendBroadCast(Constant.StopLeScan);
        if (z) {
            try {
                Dialog dialog = this.lDialog;
                if (dialog != null && dialog.isShowing()) {
                    this.lDialog.dismiss();
                }
            } catch (IllegalArgumentException | Exception unused) {
            } catch (Throwable th) {
                this.lDialog = null;
                throw th;
            }
            this.lDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addGroupMessage() {
        if (mActivity != null) {
            final EditText editText = new EditText(mActivity);
            new AlertDialog.Builder(mActivity).setTitle(R.string.please_input).setIcon(17301659).setView(editText).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.12
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String obj = editText.getText().toString();
                    Iterator<Group> it = new GroupDeviceDao(MainActivity_DMX03.mActivity).getAllgroup().iterator();
                    while (it.hasNext()) {
                        if (it.next().getGroupName().equalsIgnoreCase(obj)) {
                            Tool.ToastShow(MainActivity_DMX03.mActivity, (int) R.string.groupname_cannot_same);
                            return;
                        }
                    }
                    if (StringUtils.isEmpty(obj)) {
                        return;
                    }
                    MainActivity_DMX03.this.addGroupByName(obj);
                }
            }).setNegativeButton(R.string.cancell_dialog, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.11
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addGroupByName(String str) {
        try {
            new GroupDeviceDao(mActivity).addGroup(str);
            addGroupView(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addGroupView(final String str) {
        final GroupView groupView = new GroupView(mActivity, str, this.isAllOn);
        final SlideSwitch slideSwitch = groupView.getSlideSwitch();
        this.linearGroups.addView(groupView.getGroupView());
        this.map.put(str, slideSwitch);
        slideSwitch.setStateNoListener(false);
        slideSwitch.setSlideListener(new SlideSwitch.SlideListener() { // from class: com.home.activity.main.MainActivity_DMX03.13
            @Override // com.home.view.SlideSwitch.SlideListener
            public void open() {
                MainActivity_DMX03.this.changeStatus(str);
            }

            @Override // com.home.view.SlideSwitch.SlideListener
            public void close() {
                slideSwitch.setStateNoListener(true);
            }
        });
        groupView.getGroupView().setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (groupView.isTurnOn()) {
                    if (groupView.getConnect() > 0) {
                        return;
                    }
                    Tool.ToastShow(MainActivity_DMX03.mActivity, (int) R.string.edit_group_please);
                    MainActivity_DMX03.this.showActionSheet(str);
                    return;
                }
                MainActivity_DMX03.this.showActionSheet(str);
            }
        });
        groupView.getGroupView().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.15
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                MainActivity_DMX03.this.showDeleteDialog(str);
                return true;
            }
        });
        this.arrayListGroupViews.add(groupView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeStatus(String str) {
        setGroupName(str);
        for (String str2 : this.map.keySet()) {
            if (!str.equals(str2)) {
                this.map.get(str2).setStateNoListener(false);
            }
        }
    }

    private void gotoEdit(String str) {
        Intent intent = new Intent(mActivity, DeviceListActivity.class);
        intent.putExtra("group", str);
        ArrayList<GroupDevice> devicesByGroup = new GroupDeviceDao(mActivity).getDevicesByGroup(str);
        if (!ListUtiles.isEmpty(devicesByGroup)) {
            intent.putExtra("devices", devicesByGroup);
        }
        startActivityForResult(intent, 111);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveImagePathToSharedPreferences(String str) {
        SharedPreferences.Editor edit = getSharedPreferences(Constant.IMAGE_VALUE, 0).edit();
        edit.putString(Constant.IMAGE_VALUE, str);
        edit.commit();
    }

    public void showPicturePicker() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") != 0) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, this.STORAGE_CODE);
                return;
            }
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 1);
                return;
            }
            return;
        }
        Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
        intent2.setType("image/*");
        if (intent2.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent2, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getImagePath() {
        SharedPreferences sharedPreferences2 = getSharedPreferences(Constant.IMAGE_VALUE, 0);
        this.sp = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
        return this.sp.getString(Constant.IMAGE_VALUE, "");
    }

    private void showImage(String str) {
        Bitmap bitmap = this.bm;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.bm.recycle();
            this.bm = null;
            System.gc();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inTempStorage = new byte[5120];
        this.imageView.setImageBitmap(BitmapFactory.decodeFile(str, options));
    }

    public static String getRealPathFromUri(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else {
                if (isMediaDocument(uri)) {
                    String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                    String str = split2[0];
                    if (PictureMimeType.MIME_TYPE_PREFIX_IMAGE.equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (PictureMimeType.MIME_TYPE_PREFIX_VIDEO.equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if (PictureMimeType.MIME_TYPE_PREFIX_AUDIO.equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                }
            }
        } else if (Utils.RESPONSE_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(query.getColumnIndexOrThrow("_data"));
                        if (query != null) {
                            query.close();
                        }
                        return string;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void updateNewFindDevice() {
        if (LedBleApplication.getApp().getBleDevices() != null) {
            this.textViewAllDeviceIndicater.setText(getResources().getString(R.string.conenct_device, String.valueOf(LedBleApplication.getApp().getHashMapConnect().size()), String.valueOf(LedBleApplication.getApp().getHashMapConnect().size())));
            if (this.groupName.equalsIgnoreCase("")) {
                this.textViewConnectCount.setText(Integer.toString(LedBleApplication.getApp().getHashMapConnect().size()));
            }
        }
        if (LedBleApplication.getApp().isAuto()) {
            return;
        }
        this.mAdapter.notifyDataSetChanged();
    }

    private void initGroup(boolean z) {
        Iterator<Group> it = new GroupDeviceDao(mActivity).getAllgroup().iterator();
        while (it.hasNext()) {
            Group next = it.next();
            addGroupViewFromInit(next.getGroupName(), next.getIsOn(), z);
        }
    }

    private void addGroupViewFromInit(final String str, String str2, boolean z) {
        final GroupView groupView = new GroupView(mActivity, str, z);
        ArrayList<GroupDevice> devicesByGroup = new GroupDeviceDao(mActivity).getDevicesByGroup(str);
        if (!ListUtiles.isEmpty(devicesByGroup)) {
            groupView.setGroupDevices(devicesByGroup);
        }
        final SlideSwitch slideSwitch = groupView.getSlideSwitch();
        this.map.put(str, slideSwitch);
        slideSwitch.setStateNoListener(false);
        slideSwitch.setSlideListener(new SlideSwitch.SlideListener() { // from class: com.home.activity.main.MainActivity_DMX03.16
            @Override // com.home.view.SlideSwitch.SlideListener
            public void open() {
                MainActivity_DMX03.this.changeStatus(str);
            }

            @Override // com.home.view.SlideSwitch.SlideListener
            public void close() {
                slideSwitch.setStateNoListener(true);
            }
        });
        groupView.getGroupView().setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (groupView.isTurnOn()) {
                    if (groupView.getConnect() > 0) {
                        return;
                    }
                    Tool.ToastShow(MainActivity_DMX03.mActivity, (int) R.string.edit_group_please);
                    MainActivity_DMX03.this.showActionSheet(str);
                    return;
                }
                MainActivity_DMX03.this.showActionSheet(str);
            }
        });
        this.linearGroups.addView(groupView.getGroupView());
        groupView.getGroupView().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.18
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                MainActivity_DMX03.this.showDeleteDialog(str);
                return true;
            }
        });
        this.arrayListGroupViews.add(groupView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeleteDialog(final String str) {
        new AlertDialog.Builder(mActivity).setTitle(getResources().getString(R.string.tips)).setMessage(getResources().getString(R.string.delete_group, str)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.20
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                try {
                    GroupDeviceDao groupDeviceDao = new GroupDeviceDao(MainActivity_DMX03.mActivity);
                    groupDeviceDao.deleteGroup(str);
                    groupDeviceDao.delteByGroup(str);
                    MainActivity_DMX03.this.linearGroups.removeView(MainActivity_DMX03.this.linearGroups.findViewWithTag(str));
                    MainActivity_DMX03.this.map.remove(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(R.string.cancell_dialog, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.19
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private void save2GroupByGroupName(String str, Set<BluetoothDevice> set) throws Exception {
        GroupDeviceDao groupDeviceDao = new GroupDeviceDao(mActivity);
        groupDeviceDao.delteByGroup(str);
        ArrayList<GroupDevice> arrayList = new ArrayList<>();
        for (BluetoothDevice bluetoothDevice : set) {
            GroupDevice groupDevice = new GroupDevice();
            groupDevice.setAddress(bluetoothDevice.getAddress());
            groupDevice.setGroupName(str);
            arrayList.add(groupDevice);
        }
        groupDeviceDao.save2Group(arrayList);
    }

    private int findConnectCount(ArrayList<GroupDevice> arrayList) {
        int i = 0;
        if (ListUtiles.isEmpty(arrayList)) {
            return 0;
        }
        Iterator<GroupDevice> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupDevice next = it.next();
            if (LedBleApplication.getApp().getHashMapConnect().containsKey(next.getAddress()) && LedBleApplication.getApp().getHashMapConnect().get(next.getAddress()).booleanValue()) {
                i++;
            }
        }
        return i;
    }

    public void showActionSheet(String str) {
        NetConnectBle.getInstanceByGroup(str);
        if (str.equalsIgnoreCase("")) {
            ActionSheet.createBuilder(this, getFragmentManager()).setCancelItem(new ActionSheet.Item(R.color.white, R.color.white, 0, 0, R.color.colorPrimary, R.color.white, getResources().getString(R.string.text_cancel), 1.0f)).setmOtherItems(new ActionSheet.Item(R.color.white, R.color.white, R.drawable.tab_ct, R.drawable.tab_ct, R.color.colorPrimary, R.color.white, getResources().getString(R.string.control), 1.0f)).setGroupName(str).setCancelableOnTouchOutside(true).setListener(this).show();
            return;
        }
        ActionSheet.createBuilder(this, getFragmentManager()).setCancelItem(new ActionSheet.Item(R.color.white, R.color.white, 0, 0, R.color.colorPrimary, R.color.white, getResources().getString(R.string.text_cancel), 1.0f)).setmOtherItems(new ActionSheet.Item(R.color.white, R.color.white, R.drawable.tab_ct, R.drawable.tab_ct, R.color.colorPrimary, R.color.white, getResources().getString(R.string.control), 1.0f), new ActionSheet.Item(R.color.white, R.color.white, R.drawable.tab_ct, R.drawable.tab_ct, R.color.colorPrimary, R.color.white, getResources().getString(R.string.add_device), 1.0f)).setGroupName(str).setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override // com.home.view.ActionSheet.ActionSheetListener
    public void onOtherButtonClick(ActionSheet actionSheet, int i, String str) {
        if (i != 0) {
            if (i != 1) {
                return;
            }
            gotoEdit(str);
        } else if (LedBleApplication.getApp().getHashMapConnect().size() > 0) {
            this.groupName = str;
            if (str.equalsIgnoreCase("")) {
                this.textViewConnectCount.setText(Integer.toString(LedBleApplication.getApp().getHashMapConnect().size()));
            } else {
                this.textViewConnectCount.setText(Integer.toString(NetConnectBle.getInstance().getDevicesByGroup().size()));
            }
        } else {
            Toast.makeText(getApplicationContext(), (int) R.string.no_device_found, 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 110) {
            if (iArr.length <= 0 || iArr[0] != 0 || Build.VERSION.SDK_INT < 23) {
                return;
            }
            if (LedBleApplication.getApp().isAuto()) {
                LedBleApplication.getApp().setCanConnect(true);
            } else {
                LedBleApplication.getApp().setCanConnect(false);
            }
            refreshDevices(true);
        } else if (i == 111) {
            if (iArr.length > 0 && iArr[0] == 0) {
                Toast.makeText(mActivity, getString(R.string.micro_authorized), 0).show();
                this.musicFragment.requestMicroPermissionsSucess(true);
                return;
            }
            this.musicFragment.requestMicroPermissionsSucess(false);
        } else if (i == 200) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                return;
            }
            this.musicFragment.request();
        } else {
            switch (i) {
                case REQUEST_CODE_PERMISSION_LOCATION /* 223 */:
                    if (iArr.length > 0 && iArr[0] == 0) {
                        refreshDevices(true);
                        return;
                    } else {
                        Snackbar.make(findViewById(R.id.root), getResources().getString(R.string.scan_failed_with_missing_permissions), -2).setAction("settings", new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_DMX03.21
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.addFlags(268435456);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", MainActivity_DMX03.this.getPackageName(), null));
                                MainActivity_DMX03.this.startActivity(intent);
                            }
                        }).setDuration(MessageHandler.WHAT_SMOOTH_SCROLL).show();
                        return;
                    }
                case REQUEST_BLUETOOTH_SCAN /* 224 */:
                    if (iArr.length <= 0 || iArr[0] != 0) {
                        return;
                    }
                    turnOnBluetooth();
                    return;
                case REQUEST_BLUETOOTH_CONNECT /* 225 */:
                    if (iArr.length <= 0 || iArr[0] != 0) {
                        return;
                    }
                    turnOnBluetooth();
                    return;
                default:
                    return;
            }
        }
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        MainActivity_DMX03 mainActivity_DMX03;
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 111) {
            if (intent.getStringExtra("group") != null) {
                String stringExtra = intent.getStringExtra("group");
                try {
                    save2GroupByGroupName(stringExtra, LedBleApplication.getApp().getTempDevices());
                    ArrayList<GroupDevice> devicesByGroup = new GroupDeviceDao(mActivity).getDevicesByGroup(stringExtra);
                    Iterator<GroupView> it = this.arrayListGroupViews.iterator();
                    while (it.hasNext()) {
                        GroupView next = it.next();
                        if (stringExtra.equals(next.getGroupName())) {
                            next.setGroupDevices(devicesByGroup);
                            int findConnectCount = findConnectCount(devicesByGroup);
                            LogUtil.i(LedBleApplication.tag, "count:" + findConnectCount);
                            next.setConnectCount(findConnectCount);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Tool.ToastShow(mActivity, "保存失败！");
                }
            }
        } else if (i == this.OPEN_BLE) {
            if (i2 == -1) {
                refreshDevices(true);
            }
        } else if (i2 == -1 && i == 1) {
            ContentResolver contentResolver = getContentResolver();
            if (intent == null || (data = intent.getData()) == null) {
                return;
            }
            try {
                Bitmap bitmap = this.bm;
                if (bitmap != null && !bitmap.isRecycled()) {
                    this.bm.recycle();
                    this.bm = null;
                    System.gc();
                }
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(contentResolver, data);
                this.bm = bitmap2;
                if (bitmap2 == null || (mainActivity_DMX03 = mActivity) == null) {
                    return;
                }
                String realPathFromUri = getRealPathFromUri(mainActivity_DMX03, data);
                showImage(realPathFromUri);
                saveImagePathToSharedPreferences(realPathFromUri);
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    @Override // com.home.base.LedBleActivity, android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        float[] fArr = sensorEvent.values;
        if (type == 1) {
            if (Math.abs(fArr[0]) > 19.0f || Math.abs(fArr[1]) > 19.0f || Math.abs(fArr[2]) > 19.0f) {
                int i = this.shakeStyle;
                if (i == 0) {
                    this.soundPool.play(this.soundID, 1.0f, 1.0f, 0, 0, 1.0f);
                    setRgb(this.random.nextInt(255) + 1, this.random.nextInt(255) + 1, this.random.nextInt(255) + 1, false, false, false, false);
                } else if (i != 2) {
                } else {
                    this.soundPool.play(this.soundID, 1.0f, 1.0f, 0, 0, 1.0f);
                    if (sceneBean.equalsIgnoreCase(CommonConstant.LEDSMART)) {
                        setRegMode(this.random.nextInt(11) + 1, false);
                    } else {
                        setRegMode(this.random.nextInt(22) + 135, false);
                    }
                }
            }
        }
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public boolean open(boolean z, boolean z2, boolean z3) {
        NetConnectBle.getInstanceByGroup(this.groupName).turnOn(sceneBean, z, z2, z3);
        return false;
    }

    public boolean close(boolean z, boolean z2, boolean z3) {
        NetConnectBle.getInstanceByGroup(this.groupName).turnOff(sceneBean, z, z2, z3);
        return false;
    }

    public void bledmxopen() {
        NetConnectBle.getInstanceByGroup(this.groupName).bledmxturnOn(sceneBean);
    }

    public void bledmxclose() {
        NetConnectBle.getInstanceByGroup(this.groupName).bledmxturnOff(sceneBean);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allOn() {
        NetConnectBle.getInstanceByGroup("").turnOn(sceneBean, false, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allOff() {
        NetConnectBle.getInstanceByGroup("").turnOff(sceneBean, false, false, false);
    }

    public void settingTime(int i, int i2, int i3, int i4) {
        NetConnectBle.getInstanceByGroup(this.groupName).sendsetting(i, i2, i3, i4);
    }

    public void sendTime(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        NetConnectBle.getInstanceByGroup(this.groupName).sendTime(i, i2, i3, i4, i5, i6, i7, sceneBean);
    }

    public void sendStageTime(int i, int i2, int i3, int i4, int i5, int i6) {
        NetConnectBle.getInstanceByGroup(this.groupName).sendtimestage(i, i2, i3, i4, i5, i6);
    }

    public void endTime(int i, int i2, int i3, int i4) {
        NetConnectBle.getInstanceByGroup(this.groupName).endTime(i, i2, i3, i4, sceneBean);
    }

    public void closeTime() {
        NetConnectBle.getInstanceByGroup(this.groupName).closeTime(sceneBean);
    }

    public void setRgb(int i, int i2, int i3, boolean z, boolean z2, boolean z3, boolean z4) {
        if (z) {
            if (this.handler == null) {
                this.handler = new Handler();
            }
            if (this.canSend) {
                NetConnectBle.getInstanceByGroup(this.groupName).setRgb(i, i2, i3, sceneBean, z2, z3, z4);
                this.canSend = false;
                this.runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.22
                    @Override // java.lang.Runnable
                    public void run() {
                        MainActivity_DMX03.this.canSend = true;
                    }
                };
                if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    this.handler.postDelayed(this.runnable, 200L);
                    return;
                } else {
                    this.handler.postDelayed(this.runnable, 200L);
                    return;
                }
            }
            return;
        }
        NetConnectBle.getInstanceByGroup(this.groupName).setRgb(i, i2, i3, sceneBean, z2, z3, z4);
    }

    public void setRegMode(int i, boolean z) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setRgbMode(i, sceneBean, z);
            this.canSend = false;
            this.runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.23
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_DMX03.this.canSend = true;
                }
            };
            if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                this.handler.postDelayed(this.runnable, 200L);
            } else {
                this.handler.postDelayed(this.runnable, 200L);
            }
        }
    }

    public void setRegModeNoInterval(int i, boolean z) {
        NetConnectBle.getInstanceByGroup(this.groupName).setRgbMode(i, sceneBean, z);
    }

    public void setDim(int i) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setDim(i, sceneBean);
            this.canSend = false;
            this.runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.24
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_DMX03.this.canSend = true;
                }
            };
            if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                this.handler.postDelayed(this.runnable, 200L);
            } else {
                this.handler.postDelayed(this.runnable, 200L);
            }
        }
    }

    public void setSPIModel(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setSPIModel(i, sceneBean);
    }

    public void setSpeed(int i, boolean z, boolean z2) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setSpeed(i, sceneBean, z, z2);
            this.canSend = false;
            this.runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.25
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_DMX03.this.canSend = true;
                }
            };
            if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                this.handler.postDelayed(this.runnable, 200L);
            } else {
                this.handler.postDelayed(this.runnable, 200L);
            }
        }
    }

    public void setSpeedNoInterval(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setSpeed(i, sceneBean, false, false);
    }

    public void setBrightNess(int i, boolean z, boolean z2, boolean z3) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setBrightness(i, sceneBean, z, z2, z3);
            this.canSend = false;
            this.runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.26
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_DMX03.this.canSend = true;
                }
            };
            if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                this.handler.postDelayed(this.runnable, 200L);
            } else {
                this.handler.postDelayed(this.runnable, 200L);
            }
        }
    }

    public void setBrightNessNoInterval(int i, boolean z, boolean z2) {
        NetConnectBle.getInstanceByGroup(this.groupName).setBrightness(i, sceneBean, false, z, z2);
    }

    public void setDirection(int i, boolean z) {
        NetConnectBle.getInstanceByGroup(this.groupName).setDirection(i, sceneBean, z);
    }

    public void setTime(int i, int i2, int i3, int i4, int i5, int i6) {
        NetConnectBle.getInstanceByGroup(this.groupName).setrgbTime(i, i2, i3, i4, i5, i6);
    }

    public void setCirculation() {
        NetConnectBle.getInstanceByGroup(this.groupName).setCirculation();
    }

    public void setDiy(ArrayList<MyColor> arrayList, int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setDiy(arrayList, i, sceneBean);
    }

    public void setMusicBrightness(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setMusicBrightness(i, sceneBean);
    }

    public void setCT(int i, int i2) {
        NetConnectBle.getInstanceByGroup(this.groupName).setColorWarm(i, i2, sceneBean);
    }

    public void setConfigSPI(int i, byte b, byte b2, int i2) {
        NetConnectBle.getInstance().setConfigSPI(i, b, b2, i2, sceneBean);
    }

    public void setDynamicModel(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setDynamicModel(i);
    }

    public void setSPIPause(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).pauseSPI(i, sceneBean);
    }

    public void setSmartBrightnessNoInterval(int i, int i2) {
        NetConnectBle.getInstanceByGroup(this.groupName).setSmartBrightness(i, i2, sceneBean);
    }

    public void setSmartBrightness(int i, int i2) {
        Log.e(TAG, "brightness: " + i2 + ",   mode: " + i);
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setSmartBrightness(i, i2, sceneBean);
            this.canSend = false;
            this.runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.27
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_DMX03.this.canSend = true;
                }
            };
            if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                this.handler.postDelayed(this.runnable, 200L);
            } else {
                this.handler.postDelayed(this.runnable, 300L);
            }
        }
    }

    public void setRgbSort(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).SetRgbSort(i);
    }

    public void setPairCode(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).SetPairCode(i);
    }

    public void setTimeStart(int i, int i2, int i3, int i4, int i5, int i6) {
        NetConnectBle.getInstanceByGroup(this.groupName).setTime(i, i2, i3, i4, i5, i6);
    }

    public void setTimerSecData(int[] iArr) {
        NetConnectBle.getInstanceByGroup(this.groupName).setTimerSecData(iArr);
    }

    public void setSmartBubbleCheck(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setSmartBubbleCheck(i);
    }

    public void sendSun(int i, int i2, int i3, int i4, int i5) {
        NetConnectBle.getInstanceByGroup(this.groupName).sendSun(i, i2, i3, i4, i5);
    }

    public void timeSun(int i, int i2, int i3, int i4) {
        NetConnectBle.getInstanceByGroup(this.groupName).timeSun(i, i2, i3, i4);
    }

    public void setAuxiliary(int i, View view) {
        NetConnectBle.getInstanceByGroup(this.groupName).setAuxiliary(i, sceneBean);
        showSure(view);
    }

    public void setModeCycle(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
        NetConnectBle.getInstanceByGroup(this.groupName).setModeCycle(i, i2, i3, i4, i5, i6, sceneBean, z);
    }

    public void setCustomCycle(boolean z) {
        NetConnectBle.getInstanceByGroup(this.groupName).setCustomCycle(sceneBean, z);
    }

    public void setChangeColor(boolean z, boolean z2, boolean z3, ArrayList<MyColor> arrayList) {
        NetConnectBle.getInstanceByGroup(this.groupName).setChangeColor(z, z2, z3, arrayList, sceneBean);
    }

    public void setMode(boolean z, boolean z2, boolean z3, int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setMode(z, z2, z3, i, sceneBean);
    }

    public void setVoiceCtlAndMusicMode(boolean z, boolean z2, boolean z3, int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setVoiceCtlAndMusicMode(z, z2, z3, i, sceneBean);
    }

    public void setSensitivity(int i, boolean z, boolean z2) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstance().setSensitivity(i, z, z2, sceneBean);
            this.canSend = false;
            this.runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_DMX03.28
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_DMX03.this.canSend = true;
                }
            };
            if (sceneBean.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                this.handler.postDelayed(this.runnable, 200L);
            } else {
                this.handler.postDelayed(this.runnable, 300L);
            }
        }
    }

    public void SetCHN(int i, int i2, int i3, int i4, int i5, int i6) {
        NetConnectBle.getInstanceByGroup(this.groupName).SetCHN(i, i2, i3, i4, i5, i6);
    }

    public void setPassword() {
        Arrays.asList(getResources().getStringArray(R.array.week));
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("ss");
        int parseInt = Integer.parseInt(simpleDateFormat.format(date).trim());
        int parseInt2 = Integer.parseInt(simpleDateFormat2.format(date).trim());
        Integer.parseInt(simpleDateFormat3.format(date).trim());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int i = 7;
        String valueOf = String.valueOf(calendar.get(7));
        if (!"1".equals(valueOf)) {
            if (ExifInterface.GPS_MEASUREMENT_2D.equals(valueOf)) {
                i = 1;
            } else if (ExifInterface.GPS_MEASUREMENT_3D.equals(valueOf)) {
                i = 2;
            } else if ("4".equals(valueOf)) {
                i = 3;
            } else if ("5".equals(valueOf)) {
                i = 4;
            } else if ("6".equals(valueOf)) {
                i = 5;
            } else {
                i = "7".equals(valueOf) ? 6 : 0;
            }
        }
        if (mActivity.getweek() == 0) {
            Toast.makeText(mActivity, (int) R.string.choose, 0).show();
            return;
        }
        int i2 = (i << 5) | parseInt;
        String perference = SharePersistent.getPerference(this, Constant.PasswordSet);
        if (perference != null && perference.length() == 8) {
            NetConnectBle.getInstanceByGroup(this.groupName).setPassword(Integer.parseInt(perference.substring(0, 2), 16), Integer.parseInt(perference.substring(2, 4), 16), Integer.parseInt(perference.substring(4, 6), 16), Integer.parseInt(perference.substring(6, 8), 16), i2, parseInt2);
        } else {
            NetConnectBle.getInstanceByGroup(this.groupName).setPassword(0, 0, 0, 0, i2, parseInt2);
        }
    }

    private static SharedPreferences getSp(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("SpUtil", 0);
        }
        return sharedPreferences;
    }

    public static void putString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = getSp(context).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static String getString(Context context, String str, String str2) {
        return getSp(context).getString(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        GPSPresenter gPSPresenter;
        super.onResume();
        SensorManager sensorManager = this.sensorManager;
        if (sensorManager != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(1), 3);
        }
        if (Build.VERSION.SDK_INT < 23 || (gPSPresenter = this.gps_presenter) == null || gPSPresenter.gpsIsOpen(this)) {
            return;
        }
        this.layout.setVisibility(0);
    }

    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        GPSPresenter gPSPresenter = this.gps_presenter;
        if (gPSPresenter != null) {
            gPSPresenter.onDestroy();
            this.gps_presenter = null;
        }
        if (sceneBean.equalsIgnoreCase("LEDDMX-03-")) {
            if (this.iv_all22.getVisibility() == 0) {
                putString(mActivity, "LEDDMX-03-", "two");
            } else {
                putString(mActivity, "LEDDMX-03-", "one");
            }
        }
        mActivity = null;
    }

    public SegmentedRadioGroup getSegmentRgb() {
        return this.segmentRgb;
    }

    public SegmentedRadioGroup getSegmentMusic() {
        return this.segmentMusic;
    }

    public Button getBtnChangeColor() {
        return this.btnChangeColor;
    }

    public RelativeLayout getRlModeTopDMX00() {
        return this.rlModeTopDMX00;
    }

    public Button getBtnModePlay() {
        return this.btnModePlay;
    }

    public Button getIvEditColor() {
        return this.ivEditColor;
    }

    public Button getBtnTimerAdd() {
        return this.btnTimerAdd;
    }
}
