package com.home.activity.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
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
import android.hardware.SensorEventListener;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.exifinterface.media.ExifInterface;
import butterknife.BindView;
import com.FirstActivity;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
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
import com.common.view.toast.bamtoast.BamToast;
import com.google.android.material.snackbar.Snackbar;
import com.home.activity.like.music.LikeMusicActivity;
import com.home.activity.like.timer.LikeTimerActivity;
import com.home.activity.other.DeviceListActivity;
import com.home.adapter.BluetoothDataModel;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.bean.MyColor;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.db.Group;
import com.home.db.GroupDevice;
import com.home.db.GroupDeviceDao;
import com.home.fragment.service.ServicesFragment;
import com.home.net.NetConnectBle;
import com.home.net.NetExceptionInterface;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class MainActivity_LIKE extends LedBleActivity implements NetExceptionInterface, SensorEventListener, ActionSheet.ActionSheetListener, MyReceiver.MyListener, IListener, GPS_Interface {
    private static final int REQUEST_BLUETOOTH_CONNECT = 225;
    private static final int REQUEST_BLUETOOTH_SCAN = 224;
    private static final int REQUEST_CODE_OPEN_GPS = 222;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 223;
    private static final String TAG = "MainActivity_LIKE";
    public static MainActivity_LIKE mActivity = null;
    public static String sceneBean = null;
    private static SharedPreferences sharedPreferences = null;
    public static String table = "one";
    private ArrayList<GroupView> arrayListGroupViews;
    @BindView(R.id.backTextView)
    TextView backTextView;
    private Bitmap bm;
    private Button buttonAddGroup;
    private Button buttonAllOff;
    private Button buttonAllOn;
    @BindView(R.id.buttonBreathe)
    Button buttonBreathe;
    @BindView(R.id.buttonFlash)
    Button buttonFlash;
    @BindView(R.id.buttonStrobe)
    Button buttonStrobe;
    @BindView(R.id.buttonVoiceCtlBreathe)
    Button buttonVoiceCtlBreathe;
    @BindView(R.id.buttonVoiceCtlFlash)
    Button buttonVoiceCtlFlash;
    @BindView(R.id.buttonVoiceCtlStrobe)
    Button buttonVoiceCtlStrobe;
    private Button bv_sure;
    LinearLayout devices_connect;
    private Dialog dialogDisconnect;
    private Dialog dialogSure;
    private SharedPreferences.Editor editor;
    private Effectstype effect;
    private GPSPresenter gps_presenter;
    String grop;
    private Handler handler;
    @BindView(R.id.ivLeftMenu)
    ImageView ivLeftMenu;
    private ImageView iv_all11;
    private ImageView iv_all22;
    private Dialog lDialog;
    private RelativeLayout layout;
    @BindView(R.id.menu_content_layout)
    LinearLayout left_menu;
    private LinearLayout linearGroups;
    @BindView(R.id.linearLayoutBottom)
    LinearLayout linearLayoutBottom;
    @BindView(R.id.ll_mode)
    LinearLayout ll_mode;
    @BindView(R.id.ll_voicecontrol)
    LinearLayout ll_voicecontrol;
    private ListView lv_alldevices;
    private DeviceAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    @BindView(R.id.onOffButton)
    Button onOffButton;
    private ImageView refreshView;
    private RelativeLayout rl_alldevices;
    private Runnable runnable;
    @BindView(R.id.sbSensitivity)
    SeekBar sbSensitivity;
    @BindView(R.id.sb_brightness)
    SeekBar sb_brightness;
    @BindView(R.id.sb_speed)
    SeekBar sb_speed;
    private SharedPreferences sp;
    private TextView textViewAllDeviceIndicater;
    private TextView textViewConnectCount;
    private Intent turnOnBluetoothIntent;
    @BindView(R.id.tvBrightnessValue)
    TextView tvBrightnessValue;
    @BindView(R.id.tvMode)
    TextView tvMode;
    @BindView(R.id.tvModeBrightness0)
    TextView tvModeBrightness0;
    @BindView(R.id.tvModeBrightness100)
    TextView tvModeBrightness100;
    @BindView(R.id.tvModeBrightness20)
    TextView tvModeBrightness20;
    @BindView(R.id.tvModeBrightness40)
    TextView tvModeBrightness40;
    @BindView(R.id.tvModeBrightness60)
    TextView tvModeBrightness60;
    @BindView(R.id.tvModeBrightness80)
    TextView tvModeBrightness80;
    @BindView(R.id.tvModeLine)
    TextView tvModeLine;
    @BindView(R.id.tvMusic)
    TextView tvMusic;
    @BindView(R.id.tvSensitivityValue)
    TextView tvSensitivityValue;
    @BindView(R.id.tvSpeedValue)
    TextView tvSpeedValue;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.tvVoicecontrol)
    TextView tvVoicecontrol;
    @BindView(R.id.tvVoicecontrolLine)
    TextView tvVoicecontrolLine;
    private boolean canSend = true;
    private int STORAGE_CODE = 112;
    private final int RECORD_AUDIO = 200;
    public boolean isLightOpen = false;
    public int speed = 1;
    private int brightness = 1;
    private String groupName = "";
    private ImageView imageView = null;
    private final int TAKE_PICTURE = 0;
    private final int CHOOSE_PICTURE = 1;
    private final int REQUEST_ENABLE_BT = 1;
    private final int INT_GO_LIST = 111;
    private boolean isInitGroup = false;
    private boolean isAllOn = true;
    private Map<String, SlideSwitch> map = new HashMap();
    private final int LOCATION_CODE = 110;
    private int OPEN_BLE = FirstActivity.RESULT333;
    private int INT_GO_COLORMODE = 112;
    private final int GPS_REQUEST_CODE = 113;
    ArrayList<BluetoothDevice> listDevices = new ArrayList<>();
    int handover = 999;
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

    public void setDirection(int i) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
            getWindow().setStatusBarColor(0);
        }
        setRequestedOrientation(1);
        setContentView(R.layout.activity_main_like);
        getSwipeBackLayout().setEnableGesture(false);
        mActivity = this;
        ListenerManager.getInstance().registerListtener(this);
        sceneBean = (String) getIntent().getSerializableExtra("scene");
        initSlidingMenu();
        initView();
    }

    public static MainActivity_LIKE getMainActivity() {
        return mActivity;
    }

    public static String getSceneBean() {
        return sceneBean;
    }

    @Override // com.common.listener.IListener
    public void notifyAllActivity(String str) {
        String str2;
        if (str.equalsIgnoreCase(Constant.UpdateNewFindDevice)) {
            updateNewFindDevice();
        } else if (!str.equalsIgnoreCase(Constant.SmartTimeNowSet) || (str2 = sceneBean) == null || str2 == null || !str2.equalsIgnoreCase(CommonConstant.LEDSMART) || mActivity == null) {
        } else {
            getMainActivity().setSmartTimeNowSet();
        }
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
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.home.activity.main.MainActivity_LIKE.1
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://source.android.google.cn/devices/bluetooth/ble"));
                MainActivity_LIKE.this.startActivity(intent);
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

    private void initView() {
        this.backTextView.setOnClickListener(new MyOnClickListener());
        this.ivLeftMenu.setOnClickListener(new MyOnClickListener());
        this.onOffButton.setOnClickListener(new MyOnClickListener());
        this.sb_brightness.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.activity.main.MainActivity_LIKE.2
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (MainActivity_LIKE.getMainActivity() != null) {
                    MainActivity_LIKE.getMainActivity().setLikeBrightness(MainActivity_LIKE.this.brightness, true, false, false);
                }
                TextView textView = MainActivity_LIKE.this.tvBrightnessValue;
                textView.setText(MainActivity_LIKE.this.brightness + "%");
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    MainActivity_LIKE.this.brightness = i;
                    if (i == 0) {
                        MainActivity_LIKE.this.brightness = 1;
                        TextView textView = MainActivity_LIKE.this.tvBrightnessValue;
                        textView.setText(String.valueOf(1) + "%");
                        if (MainActivity_LIKE.mActivity != null) {
                            MainActivity_LIKE.mActivity.setLikeBrightness(1, true, false, true);
                        }
                    } else {
                        MainActivity_LIKE.this.brightness = i;
                        if (MainActivity_LIKE.getMainActivity() != null) {
                            MainActivity_LIKE.getMainActivity().setLikeBrightness(i, true, false, true);
                        }
                        TextView textView2 = MainActivity_LIKE.this.tvBrightnessValue;
                        textView2.setText(i + "%");
                    }
                    SharePersistent.saveInt(MainActivity_LIKE.mActivity, "MainActivity_LIKEbrightness", i);
                }
            }
        });
        int i = SharePersistent.getInt(mActivity, "MainActivity_LIKEbrightness");
        if (i > 0) {
            this.sb_brightness.setProgress(i);
            TextView textView = this.tvBrightnessValue;
            textView.setText(i + "%");
        } else {
            this.tvBrightnessValue.setText("100%");
            this.sb_brightness.setProgress(100);
        }
        this.sb_speed.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.activity.main.MainActivity_LIKE.3
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (MainActivity_LIKE.getMainActivity() != null) {
                    MainActivity_LIKE.getMainActivity().setSpeed(MainActivity_LIKE.this.speed);
                }
                TextView textView2 = MainActivity_LIKE.this.tvSpeedValue;
                textView2.setText(MainActivity_LIKE.this.speed + "%");
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    if (i2 == 0) {
                        MainActivity_LIKE.this.speed = 1;
                        MainActivity_LIKE.this.tvSpeedValue.setText("1%");
                        if (MainActivity_LIKE.mActivity != null) {
                            MainActivity_LIKE.mActivity.setSpeed(1);
                        }
                    } else {
                        MainActivity_LIKE.this.speed = i2;
                        if (MainActivity_LIKE.getMainActivity() != null) {
                            MainActivity_LIKE.getMainActivity().setSpeed(i2);
                        }
                        TextView textView2 = MainActivity_LIKE.this.tvSpeedValue;
                        textView2.setText(i2 + "%");
                    }
                    SharePersistent.saveInt(MainActivity_LIKE.mActivity, "MainActivity_LIKEspeed", i2);
                }
            }
        });
        int i2 = SharePersistent.getInt(mActivity, "MainActivity_LIKEspeed");
        if (i2 > 0) {
            this.sb_speed.setProgress(i2);
            TextView textView2 = this.tvSpeedValue;
            textView2.setText(i2 + "%");
        } else {
            this.tvSpeedValue.setText("80%");
            this.sb_speed.setProgress(80);
        }
        this.sbSensitivity.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.activity.main.MainActivity_LIKE.4
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    if (i3 == 0) {
                        MainActivity_LIKE.this.tvSensitivityValue.setText("1%");
                        if (MainActivity_LIKE.mActivity != null) {
                            MainActivity_LIKE.mActivity.setLikeSensitivity(1);
                        }
                    } else {
                        if (MainActivity_LIKE.getMainActivity() != null) {
                            MainActivity_LIKE.getMainActivity().setLikeSensitivity(i3);
                        }
                        TextView textView3 = MainActivity_LIKE.this.tvSensitivityValue;
                        textView3.setText(i3 + "%");
                    }
                    SharePersistent.saveInt(MainActivity_LIKE.mActivity, "MainActivity_LIKElike_sensitivity", i3);
                }
            }
        });
        int i3 = SharePersistent.getInt(mActivity, "MainActivity_LIKElike_sensitivity");
        if (i3 > 0) {
            this.sbSensitivity.setProgress(i3);
            TextView textView3 = this.tvSensitivityValue;
            textView3.setText(i3 + "%");
        } else {
            this.tvSensitivityValue.setText("90%");
            this.sbSensitivity.setProgress(90);
        }
        this.tvMode.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity_LIKE.this.ll_mode.setVisibility(0);
                MainActivity_LIKE.this.ll_voicecontrol.setVisibility(8);
                MainActivity_LIKE.this.tvMode.setTextColor(MainActivity_LIKE.this.getResources().getColor(R.color.blue));
                MainActivity_LIKE.this.tvModeLine.setBackgroundResource(R.color.blue);
                MainActivity_LIKE.this.tvVoicecontrol.setTextColor(MainActivity_LIKE.this.getResources().getColor(R.color.black));
                MainActivity_LIKE.this.tvVoicecontrolLine.setBackgroundResource(R.color.transparent);
            }
        });
        this.tvVoicecontrol.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity_LIKE.this.ll_mode.setVisibility(8);
                MainActivity_LIKE.this.ll_voicecontrol.setVisibility(0);
                MainActivity_LIKE.this.tvMode.setTextColor(MainActivity_LIKE.this.getResources().getColor(R.color.black));
                MainActivity_LIKE.this.tvModeLine.setBackgroundResource(R.color.transparent);
                MainActivity_LIKE.this.tvVoicecontrol.setTextColor(MainActivity_LIKE.this.getResources().getColor(R.color.blue));
                MainActivity_LIKE.this.tvVoicecontrolLine.setBackgroundResource(R.color.blue);
            }
        });
        this.tvTimer.setOnClickListener(new MyOnClickListener());
        this.tvMusic.setOnClickListener(new MyOnClickListener());
        this.buttonBreathe.setOnClickListener(new MyOnClickListener());
        this.buttonFlash.setOnClickListener(new MyOnClickListener());
        this.buttonStrobe.setOnClickListener(new MyOnClickListener());
        this.buttonVoiceCtlBreathe.setOnClickListener(new MyOnClickListener());
        this.buttonVoiceCtlFlash.setOnClickListener(new MyOnClickListener());
        this.buttonVoiceCtlStrobe.setOnClickListener(new MyOnClickListener());
        this.tvModeBrightness0.setOnClickListener(new MyOnClickListener());
        this.tvModeBrightness20.setOnClickListener(new MyOnClickListener());
        this.tvModeBrightness40.setOnClickListener(new MyOnClickListener());
        this.tvModeBrightness60.setOnClickListener(new MyOnClickListener());
        this.tvModeBrightness80.setOnClickListener(new MyOnClickListener());
        this.tvModeBrightness100.setOnClickListener(new MyOnClickListener());
        this.textViewAllDeviceIndicater = (TextView) mActivity.findViewById(R.id.textViewAllDeviceIndicater);
        this.textViewConnectCount = (TextView) mActivity.findViewById(R.id.textViewConnectCount);
        this.linearGroups = (LinearLayout) mActivity.findViewById(R.id.linearLayoutDefineGroups);
        this.arrayListGroupViews = new ArrayList<>();
        ImageView imageView = (ImageView) mActivity.findViewById(R.id.ivRefresh);
        this.refreshView = imageView;
        imageView.setOnClickListener(new MyOnClickListener());
        this.lv_alldevices = (ListView) findViewById(R.id.lv_alldevices);
        DeviceAdapter deviceAdapter = new DeviceAdapter();
        this.mAdapter = deviceAdapter;
        this.lv_alldevices.setAdapter((ListAdapter) deviceAdapter);
        this.lv_alldevices.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.7
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i4, long j) {
                BluetoothDataModel bluetoothDataModel = LedBleApplication.getApp().getManmualBleDevices().get(i4);
                bluetoothDataModel.setSeleted(!bluetoothDataModel.isSeleted());
                MainActivity_LIKE.this.mAdapter.notifyDataSetChanged();
            }
        });
        Button button = (Button) mActivity.findViewById(R.id.buttonAllOff);
        this.buttonAllOff = button;
        button.setOnClickListener(new MyOnClickListener());
        Button button2 = (Button) mActivity.findViewById(R.id.buttonAllOn);
        this.buttonAllOn = button2;
        button2.setOnClickListener(new MyOnClickListener());
        Button button3 = (Button) mActivity.findViewById(R.id.buttonAddGroup);
        this.buttonAddGroup = button3;
        button3.setOnClickListener(new MyOnClickListener());
        this.iv_all11 = (ImageView) findViewById(R.id.iv_all11);
        this.iv_all22 = (ImageView) findViewById(R.id.iv_all22);
        this.iv_all11.setOnClickListener(new MyOnClickListener());
        this.iv_all22.setOnClickListener(new MyOnClickListener());
        this.bv_sure = (Button) findViewById(R.id.bv_sure);
        this.lv_alldevices = (ListView) findViewById(R.id.lv_alldevices);
        DeviceAdapter deviceAdapter2 = new DeviceAdapter();
        this.mAdapter = deviceAdapter2;
        this.lv_alldevices.setAdapter((ListAdapter) deviceAdapter2);
        this.lv_alldevices.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.8
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i4, long j) {
                BluetoothDataModel bluetoothDataModel = LedBleApplication.getApp().getManmualBleDevices().get(i4);
                bluetoothDataModel.setSeleted(!bluetoothDataModel.isSeleted());
                MainActivity_LIKE.this.mAdapter.notifyDataSetChanged();
            }
        });
        this.rl_alldevices = (RelativeLayout) findViewById(R.id.rl_alldevices);
        this.devices_connect = (LinearLayout) findViewById(R.id.devices_connect);
        this.bv_sure.setOnClickListener(new MyOnClickListener());
        if ((sceneBean.equalsIgnoreCase(CommonConstant.LEDLIKE) ? getString(mActivity, TAG, "onCreate") : null).equals("onCreate")) {
            this.devices_connect.setVisibility(0);
            this.rl_alldevices.setVisibility(8);
            this.iv_all22.setVisibility(8);
            this.linearLayoutBottom.setVisibility(0);
            LedBleApplication.getApp().setAuto(true);
            LedBleApplication.getApp().setCanConnect(true);
        }
        initBleScanList(this.isAllOn);
    }

    public void back() {
        SharePersistent.savePerference(getMainActivity(), Constant.CUSTOM_DIY_APPKEY, (String) null);
        LedBleApplication.getApp().setAuto(false);
        finish();
        finish();
    }

    /* loaded from: classes.dex */
    public class MyOnClickListener implements View.OnClickListener {
        public MyOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() != R.id.tvTimer && view.getId() != R.id.tvMusic) {
                MainActivity_LIKE.this.startAnimation(view);
            }
            switch (view.getId()) {
                case R.id.backTextView /* 2131296338 */:
                    MainActivity_LIKE.this.back();
                    return;
                case R.id.btn_location_enable /* 2131296412 */:
                    MainActivity_LIKE.this.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 222);
                    return;
                case R.id.btn_location_more /* 2131296413 */:
                    if (MainActivity_LIKE.mActivity == null || MainActivity_LIKE.mActivity.isFinishing()) {
                        return;
                    }
                    new AlertDialog.Builder(MainActivity_LIKE.mActivity).setTitle(MainActivity_LIKE.this.getResources().getString(R.string.position_service)).setMessage(MainActivity_LIKE.this.getResources().getString(R.string.position_service_info)).setPositiveButton(MainActivity_LIKE.this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.MyOnClickListener.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                    return;
                case R.id.buttonAddGroup /* 2131296424 */:
                    MainActivity_LIKE.this.addGroupMessage();
                    MainActivity_LIKE.this.startAnimation(view);
                    return;
                case R.id.buttonAllOff /* 2131296425 */:
                    MainActivity_LIKE.this.allOff();
                    MainActivity_LIKE.this.startAnimation(view);
                    return;
                case R.id.buttonAllOn /* 2131296426 */:
                    MainActivity_LIKE.this.allOn();
                    MainActivity_LIKE.this.startAnimation(view);
                    return;
                case R.id.buttonBreathe /* 2131296428 */:
                    MainActivity_LIKE.mActivity.setLikeMode(0, false);
                    return;
                case R.id.buttonFlash /* 2131296432 */:
                    MainActivity_LIKE.mActivity.setLikeMode(1, false);
                    return;
                case R.id.buttonStrobe /* 2131296438 */:
                    MainActivity_LIKE.mActivity.setLikeMode(2, false);
                    return;
                case R.id.buttonVoiceCtlBreathe /* 2131296439 */:
                    MainActivity_LIKE.mActivity.setLikeMode(0, true);
                    return;
                case R.id.buttonVoiceCtlFlash /* 2131296440 */:
                    MainActivity_LIKE.mActivity.setLikeMode(1, true);
                    return;
                case R.id.buttonVoiceCtlStrobe /* 2131296441 */:
                    MainActivity_LIKE.mActivity.setLikeMode(2, true);
                    return;
                case R.id.bv_sure /* 2131296449 */:
                    LedBleApplication.getApp().setCanConnect(true);
                    if (MainActivity_LIKE.mActivity != null && !MainActivity_LIKE.mActivity.isFinishing()) {
                        Toast.makeText(MainActivity_LIKE.mActivity, MainActivity_LIKE.getMainActivity().getResources().getString(R.string.connecting), 0).show();
                    }
                    if (LedBleApplication.getApp().getHashMapConnect().size() > 0) {
                        for (final String str : LedBleApplication.getApp().getHashMapConnect().keySet()) {
                            new Thread(new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.MyOnClickListener.4
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
                        new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.MyOnClickListener.5
                            @Override // java.lang.Runnable
                            public void run() {
                                LedBleApplication.getApp().getBleDevices().clear();
                                MainActivity_LIKE.this.listDevices.clear();
                                Iterator<BluetoothDataModel> it = LedBleApplication.getApp().getManmualBleDevices().iterator();
                                while (it.hasNext()) {
                                    BluetoothDataModel next = it.next();
                                    if (next.isSeleted()) {
                                        MainActivity_LIKE.this.listDevices.add(next.getDevice());
                                    }
                                }
                                LedBleApplication.getApp().getBleDevices().addAll(MainActivity_LIKE.this.listDevices);
                                ListenerManager.getInstance().sendBroadCast(Constant.ManualModeConnectDevice);
                            }
                        }, 2000L);
                        return;
                    }
                    LedBleApplication.getApp().getBleDevices().clear();
                    MainActivity_LIKE.this.listDevices.clear();
                    Iterator<BluetoothDataModel> it = LedBleApplication.getApp().getManmualBleDevices().iterator();
                    while (it.hasNext()) {
                        BluetoothDataModel next = it.next();
                        if (next.isSeleted()) {
                            MainActivity_LIKE.this.listDevices.add(next.getDevice());
                        }
                    }
                    LedBleApplication.getApp().getBleDevices().addAll(MainActivity_LIKE.this.listDevices);
                    ListenerManager.getInstance().sendBroadCast(Constant.ManualModeConnectDevice);
                    return;
                case R.id.change_under_pic_tv /* 2131296502 */:
                    MainActivity_LIKE.this.showPicturePicker();
                    return;
                case R.id.ivLeftMenu /* 2131296768 */:
                    MainActivity_LIKE.this.mDrawerLayout.openDrawer(MainActivity_LIKE.this.left_menu);
                    return;
                case R.id.ivRefresh /* 2131296775 */:
                    if (LedBleApplication.getApp().isAuto()) {
                        LedBleApplication.getApp().setCanConnect(true);
                    } else {
                        LedBleApplication.getApp().setCanConnect(false);
                    }
                    MainActivity_LIKE.this.refreshDevices(true);
                    MainActivity_LIKE.this.startAnimation(view);
                    return;
                case R.id.iv_all11 /* 2131296795 */:
                    MainActivity_LIKE.this.handover = 6666;
                    LedBleApplication.getApp().setAuto(false);
                    MainActivity_LIKE.this.rl_alldevices.setVisibility(0);
                    MainActivity_LIKE.this.iv_all22.setVisibility(0);
                    MainActivity_LIKE.this.devices_connect.setVisibility(8);
                    MainActivity_LIKE.this.iv_all11.setVisibility(8);
                    MainActivity_LIKE.this.linearLayoutBottom.setVisibility(8);
                    MainActivity_LIKE.this.clearList();
                    new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.MyOnClickListener.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (MainActivity_LIKE.this.dialogDisconnect != null) {
                                MainActivity_LIKE.this.dialogDisconnect.dismiss();
                                MainActivity_LIKE.this.dialogDisconnect = null;
                            }
                            LedBleApplication.getApp().setCanConnect(false);
                            MainActivity_LIKE.this.refreshDevices(true);
                        }
                    }, 2000L);
                    return;
                case R.id.iv_all22 /* 2131296796 */:
                    MainActivity_LIKE.this.handover = 6666;
                    LedBleApplication.getApp().setAuto(true);
                    MainActivity_LIKE.this.devices_connect.setVisibility(0);
                    MainActivity_LIKE.this.iv_all11.setVisibility(0);
                    MainActivity_LIKE.this.rl_alldevices.setVisibility(8);
                    MainActivity_LIKE.this.iv_all22.setVisibility(8);
                    MainActivity_LIKE.this.linearLayoutBottom.setVisibility(0);
                    MainActivity_LIKE.this.clearList();
                    new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.MyOnClickListener.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (MainActivity_LIKE.this.dialogDisconnect != null) {
                                MainActivity_LIKE.this.dialogDisconnect.dismiss();
                                MainActivity_LIKE.this.dialogDisconnect = null;
                            }
                            LedBleApplication.getApp().setCanConnect(true);
                            MainActivity_LIKE.this.refreshDevices(true);
                        }
                    }, 2000L);
                    return;
                case R.id.linearLayoutTopItem /* 2131296911 */:
                    MainActivity_LIKE.this.showActionSheet("");
                    return;
                case R.id.onOffButton /* 2131297111 */:
                    if (MainActivity_LIKE.this.isLightOpen) {
                        MainActivity_LIKE.this.onOffButton.setBackgroundResource(R.drawable.like_off);
                        MainActivity_LIKE.this.open();
                        MainActivity_LIKE.this.isLightOpen = false;
                        return;
                    }
                    MainActivity_LIKE.this.onOffButton.setBackgroundResource(R.drawable.like_on);
                    MainActivity_LIKE.this.close();
                    MainActivity_LIKE.this.isLightOpen = true;
                    return;
                case R.id.reset_tv /* 2131297262 */:
                    MainActivity_LIKE.this.imageView.setImageDrawable(MainActivity_LIKE.this.getResources().getDrawable(R.drawable.bg_all));
                    MainActivity_LIKE.this.getImagePath();
                    MainActivity_LIKE.this.saveImagePathToSharedPreferences("");
                    return;
                case R.id.tvModeBrightness0 /* 2131297738 */:
                    MainActivity_LIKE.mActivity.setBrightNessNoInterval(0);
                    return;
                case R.id.tvModeBrightness100 /* 2131297739 */:
                    MainActivity_LIKE.mActivity.setBrightNessNoInterval(100);
                    return;
                case R.id.tvModeBrightness20 /* 2131297740 */:
                    MainActivity_LIKE.mActivity.setBrightNessNoInterval(20);
                    return;
                case R.id.tvModeBrightness40 /* 2131297741 */:
                    MainActivity_LIKE.mActivity.setBrightNessNoInterval(40);
                    return;
                case R.id.tvModeBrightness60 /* 2131297742 */:
                    MainActivity_LIKE.mActivity.setBrightNessNoInterval(60);
                    return;
                case R.id.tvModeBrightness80 /* 2131297743 */:
                    MainActivity_LIKE.mActivity.setBrightNessNoInterval(80);
                    return;
                case R.id.tvMusic /* 2131297750 */:
                    if (ContextCompat.checkSelfPermission(MainActivity_LIKE.mActivity, "android.permission.RECORD_AUDIO") != 0) {
                        MainActivity_LIKE.mActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 200);
                        return;
                    } else {
                        MainActivity_LIKE.this.startActivity(new Intent(MainActivity_LIKE.mActivity, LikeMusicActivity.class));
                        return;
                    }
                case R.id.tvTimer /* 2131297810 */:
                    Intent intent = new Intent(MainActivity_LIKE.mActivity, LikeTimerActivity.class);
                    intent.putExtra("name", "1");
                    MainActivity_LIKE.this.startActivity(intent);
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
                new Thread(new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.9
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
                view2 = LayoutInflater.from(MainActivity_LIKE.this).inflate(R.layout.listlayout, (ViewGroup) null);
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

    private void showDisconnect() {
        MainActivity_LIKE mainActivity_LIKE = mActivity;
        if (mainActivity_LIKE == null || mainActivity_LIKE.isFinishing() || this.dialogDisconnect != null) {
            return;
        }
        Dialog dialog = new Dialog(mActivity, 16973840);
        this.dialogDisconnect = dialog;
        dialog.requestWindowFeature(1024);
        this.dialogDisconnect.setContentView(R.layout.dialogview_scan);
        ((TextView) this.dialogDisconnect.findViewById(R.id.dialodTv)).setText(getString(R.string.disconnect));
        ((ImageView) this.dialogDisconnect.findViewById(R.id.imageViewWait)).setVisibility(8);
        this.dialogDisconnect.show();
    }

    private void showCustomMessage() {
        MainActivity_LIKE mainActivity_LIKE = mActivity;
        if (mainActivity_LIKE == null || mainActivity_LIKE.isFinishing() || this.lDialog != null) {
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
            showToast(mActivity, getResources().getString(R.string.sent_success));
        }
    }

    private boolean isAndroid12() {
        return Build.VERSION.SDK_INT >= 31;
    }

    public void turnOnBluetooth() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), this.OPEN_BLE);
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
            MainActivity_LIKE mainActivity_LIKE = mActivity;
            if (mainActivity_LIKE == null || mainActivity_LIKE.isFinishing()) {
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
            niftyDialogBuilder.setButton1Click(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.11
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (niftyDialogBuilder.isShowing()) {
                        niftyDialogBuilder.dismiss();
                    }
                }
            }).setButton2Click(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.10
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (niftyDialogBuilder.isShowing()) {
                        niftyDialogBuilder.dismiss();
                    }
                    ActivityCompat.requestPermissions(MainActivity_LIKE.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, MainActivity_LIKE.REQUEST_CODE_PERMISSION_LOCATION);
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
            new Handler().postDelayed(new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.12
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_LIKE.this.stopLeScan(z);
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
            new AlertDialog.Builder(mActivity).setTitle(R.string.please_input).setIcon(17301659).setView(editText).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.14
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String obj = editText.getText().toString();
                    Iterator<Group> it = new GroupDeviceDao(MainActivity_LIKE.mActivity).getAllgroup().iterator();
                    while (it.hasNext()) {
                        if (it.next().getGroupName().equalsIgnoreCase(obj)) {
                            Tool.ToastShow(MainActivity_LIKE.mActivity, (int) R.string.groupname_cannot_same);
                            return;
                        }
                    }
                    if (StringUtils.isEmpty(obj)) {
                        return;
                    }
                    MainActivity_LIKE.this.addGroupByName(obj);
                }
            }).setNegativeButton(R.string.cancell_dialog, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.13
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
        slideSwitch.setSlideListener(new SlideSwitch.SlideListener() { // from class: com.home.activity.main.MainActivity_LIKE.15
            @Override // com.home.view.SlideSwitch.SlideListener
            public void open() {
                MainActivity_LIKE.this.changeStatus(str);
            }

            @Override // com.home.view.SlideSwitch.SlideListener
            public void close() {
                slideSwitch.setStateNoListener(true);
            }
        });
        groupView.getGroupView().setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (groupView.isTurnOn()) {
                    if (groupView.getConnect() > 0) {
                        return;
                    }
                    Tool.ToastShow(MainActivity_LIKE.mActivity, (int) R.string.edit_group_please);
                    MainActivity_LIKE.this.showActionSheet(str);
                    return;
                }
                MainActivity_LIKE.this.showActionSheet(str);
            }
        });
        groupView.getGroupView().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.17
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                MainActivity_LIKE.this.showDeleteDialog(str);
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
            Log.e(TAG, "size == : " + LedBleApplication.getApp().getHashMapConnect().size());
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
        slideSwitch.setSlideListener(new SlideSwitch.SlideListener() { // from class: com.home.activity.main.MainActivity_LIKE.18
            @Override // com.home.view.SlideSwitch.SlideListener
            public void open() {
                MainActivity_LIKE.this.changeStatus(str);
            }

            @Override // com.home.view.SlideSwitch.SlideListener
            public void close() {
                slideSwitch.setStateNoListener(true);
            }
        });
        groupView.getGroupView().setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (groupView.isTurnOn()) {
                    if (groupView.getConnect() > 0) {
                        return;
                    }
                    Tool.ToastShow(MainActivity_LIKE.mActivity, (int) R.string.edit_group_please);
                    MainActivity_LIKE.this.showActionSheet(str);
                    return;
                }
                MainActivity_LIKE.this.showActionSheet(str);
            }
        });
        this.linearGroups.addView(groupView.getGroupView());
        groupView.getGroupView().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.20
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                MainActivity_LIKE.this.showDeleteDialog(str);
                return true;
            }
        });
        if ("y".equalsIgnoreCase(str2) && z) {
            groupView.turnOn();
        } else {
            groupView.turnOff();
        }
        this.arrayListGroupViews.add(groupView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeleteDialog(final String str) {
        new AlertDialog.Builder(mActivity).setTitle(getResources().getString(R.string.tips)).setMessage(getResources().getString(R.string.delete_group, str)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.22
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                try {
                    GroupDeviceDao groupDeviceDao = new GroupDeviceDao(MainActivity_LIKE.mActivity);
                    groupDeviceDao.deleteGroup(str);
                    groupDeviceDao.delteByGroup(str);
                    MainActivity_LIKE.this.linearGroups.removeView(MainActivity_LIKE.this.linearGroups.findViewWithTag(str));
                    MainActivity_LIKE.this.map.remove(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(R.string.cancell_dialog, new DialogInterface.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.21
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
            ActionSheet.createBuilder(this, getFragmentManager()).setCancelItem(new ActionSheet.Item(R.color.white, R.color.white, 0, 0, R.color.colorPrimary, R.color.white, getResources().getString(R.string.text_cancel))).setmOtherItems(new ActionSheet.Item(R.color.white, R.color.white, R.drawable.tab_ct, R.drawable.tab_ct, R.color.colorPrimary, R.color.white, getResources().getString(R.string.control))).setGroupName(str).setCancelableOnTouchOutside(true).setListener(this).show();
            return;
        }
        ActionSheet.createBuilder(this, getFragmentManager()).setCancelItem(new ActionSheet.Item(R.color.white, R.color.white, 0, 0, R.color.colorPrimary, R.color.white, getResources().getString(R.string.text_cancel))).setmOtherItems(new ActionSheet.Item(R.color.white, R.color.white, R.drawable.tab_ct, R.drawable.tab_ct, R.color.colorPrimary, R.color.white, getResources().getString(R.string.control)), new ActionSheet.Item(R.color.white, R.color.white, R.drawable.tab_ct, R.drawable.tab_ct, R.color.colorPrimary, R.color.white, getResources().getString(R.string.add_device))).setGroupName(str).setCancelableOnTouchOutside(true).setListener(this).show();
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
            return;
        }
        switch (i) {
            case REQUEST_CODE_PERMISSION_LOCATION /* 223 */:
                if (iArr.length > 0 && iArr[0] == 0) {
                    refreshDevices(true);
                    return;
                } else {
                    Snackbar.make(findViewById(R.id.root), getResources().getString(R.string.scan_failed_with_missing_permissions), -2).setAction("settings", new View.OnClickListener() { // from class: com.home.activity.main.MainActivity_LIKE.23
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.addFlags(268435456);
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", MainActivity_LIKE.this.getPackageName(), null));
                            MainActivity_LIKE.this.startActivity(intent);
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

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        MainActivity_LIKE mainActivity_LIKE;
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 111) {
            if (intent.getStringExtra("group") != null) {
                String stringExtra = intent.getStringExtra("group");
                this.grop = stringExtra;
                try {
                    save2GroupByGroupName(stringExtra, LedBleApplication.getApp().getTempDevices());
                    ArrayList<GroupDevice> devicesByGroup = new GroupDeviceDao(mActivity).getDevicesByGroup(this.grop);
                    Iterator<GroupView> it = this.arrayListGroupViews.iterator();
                    while (it.hasNext()) {
                        GroupView next = it.next();
                        if (this.grop.equals(next.getGroupName())) {
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
                if (bitmap2 == null || (mainActivity_LIKE = mActivity) == null) {
                    return;
                }
                String realPathFromUri = getRealPathFromUri(mainActivity_LIKE, data);
                showImage(realPathFromUri);
                saveImagePathToSharedPreferences(realPathFromUri);
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public boolean open() {
        NetConnectBle.getInstanceByGroup(this.groupName).turnOn(sceneBean, false, false, false);
        return false;
    }

    public boolean close() {
        NetConnectBle.getInstanceByGroup(this.groupName).turnOff(sceneBean, false, false, false);
        return false;
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

    public void setRgb(int i, int i2, int i3) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setRgb(i, i2, i3, sceneBean, false, false, false);
            this.canSend = false;
            Runnable runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.24
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_LIKE.this.canSend = true;
                }
            };
            this.runnable = runnable;
            this.handler.postDelayed(runnable, 200L);
        }
    }

    public void setRegMode(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setRgbMode(i, sceneBean, false);
    }

    public void setDim(int i) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setDim(i, sceneBean);
            this.canSend = false;
            Runnable runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.25
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_LIKE.this.canSend = true;
                }
            };
            this.runnable = runnable;
            this.handler.postDelayed(runnable, 200L);
        }
    }

    public void setDimModel(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setDimModel(i);
    }

    public void setSPIModel(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setSPIModel(i, sceneBean);
    }

    public void setMusicSpeed(int i) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setMusicSpeed(i);
            this.canSend = false;
            Runnable runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.26
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_LIKE.this.canSend = true;
                }
            };
            this.runnable = runnable;
            this.handler.postDelayed(runnable, 200L);
        }
    }

    public void setMusicModel(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setMusicModel(i, sceneBean);
    }

    public void setSpeed(int i) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setSpeed(i, sceneBean, false, false);
            this.canSend = false;
            Runnable runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.27
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_LIKE.this.canSend = true;
                }
            };
            this.runnable = runnable;
            this.handler.postDelayed(runnable, 200L);
        }
    }

    public void setLikeSensitivity(int i) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setLikeSensitivity(i);
            this.canSend = false;
            Runnable runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.28
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_LIKE.this.canSend = true;
                }
            };
            this.runnable = runnable;
            this.handler.postDelayed(runnable, 150L);
        }
    }

    public void setLikeMode(int i, boolean z) {
        NetConnectBle.getInstanceByGroup(this.groupName).setLikeMode(i, z);
    }

    public void setLikeBrightness(int i, boolean z, boolean z2, boolean z3) {
        if (z3) {
            if (this.handler == null) {
                this.handler = new Handler();
            }
            if (this.canSend) {
                NetConnectBle.getInstanceByGroup(this.groupName).setLikeBrightness(i, sceneBean, z, z2);
                this.canSend = false;
                Runnable runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.29
                    @Override // java.lang.Runnable
                    public void run() {
                        MainActivity_LIKE.this.canSend = true;
                    }
                };
                this.runnable = runnable;
                this.handler.postDelayed(runnable, 300L);
                return;
            }
            return;
        }
        NetConnectBle.getInstanceByGroup(this.groupName).setLikeBrightness(i, sceneBean, z, z2);
    }

    public void setBrightNessNoInterval(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setLikeBrightness(i, sceneBean, false, false);
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

    public void setMusic(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setMusicBrightness(i, sceneBean);
    }

    public void setCT(int i, int i2) {
        NetConnectBle.getInstanceByGroup(this.groupName).setColorWarm(i, i2, sceneBean);
    }

    public void setDynamicModel(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setDynamicModel(i);
    }

    public void setSPIPause(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).pauseSPI(i, sceneBean);
    }

    public void setSmartBrightness(int i, int i2) {
        NetConnectBle.getInstanceByGroup(this.groupName).setSmartBrightness(i, i2, sceneBean);
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

    public void setSmartTimeNowSet() {
        Date date = new Date(System.currentTimeMillis());
        List asList = Arrays.asList(getResources().getStringArray(R.array.week));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("ss");
        String format = new SimpleDateFormat(ExifInterface.LONGITUDE_EAST).format(date);
        int i = 6;
        if (format.equals(asList.get(0))) {
            i = 1;
        } else if (format.equals(asList.get(1))) {
            i = 2;
        } else if (format.equals(asList.get(2))) {
            i = 3;
        } else if (format.equals(asList.get(3))) {
            i = 4;
        } else if (format.equals(asList.get(4))) {
            i = 5;
        } else if (!format.equals(asList.get(5))) {
            i = format.equals(asList.get(6)) ? 7 : 0;
        }
        int parseInt = Integer.parseInt(simpleDateFormat.format(date).trim());
        int parseInt2 = Integer.parseInt(simpleDateFormat2.format(date).trim());
        int parseInt3 = Integer.parseInt(simpleDateFormat3.format(date).trim());
        for (int i2 = 0; i2 < 2; i2++) {
            NetConnectBle.getInstanceByGroup(this.groupName).setSmartTimeSet(parseInt, parseInt2, parseInt3, i);
        }
    }

    public void SetCHN(int i, int i2, int i3, int i4, int i5, int i6) {
        NetConnectBle.getInstanceByGroup(this.groupName).SetCHN(i, i2, i3, i4, i5, i6);
    }

    public void setSunVcMode(int i) {
        NetConnectBle.getInstanceByGroup(this.groupName).setSunVcMode(i);
    }

    public void setSunSensitivity(int i) {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.canSend) {
            NetConnectBle.getInstanceByGroup(this.groupName).setSunSensitivity(i);
            this.canSend = false;
            Runnable runnable = new Runnable() { // from class: com.home.activity.main.MainActivity_LIKE.30
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity_LIKE.this.canSend = true;
                }
            };
            this.runnable = runnable;
            this.handler.postDelayed(runnable, 200L);
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
        super.onResume();
    }

    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        GPSPresenter gPSPresenter = this.gps_presenter;
        if (gPSPresenter != null) {
            gPSPresenter.onDestroy();
            this.gps_presenter = null;
        }
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
        mActivity = null;
    }
}
