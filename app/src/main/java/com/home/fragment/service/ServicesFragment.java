package com.home.fragment.service;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.FirstActivity;
import com.common.listener.IListener;
import com.common.listener.ListenerManager;
import com.common.listener.ReceiveDataListener;
import com.common.listener.ReceiveDataListenerManager;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.common.view.DragGridView;
import com.home.activity.main.MainActivity_BLE;
import com.home.activity.main.MainActivity_DMX02;
import com.home.activity.main.MainActivity_DMX03;
import com.home.activity.main.MainActivity_LIKE;
import com.home.activity.other.PrivacyPolicyActivity;
import com.home.activity.service.AddSceneActivity;
import com.home.adapter.BluetoothDataModel;
import com.home.adapter.SceneAdpter;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.bean.SceneBean;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.db.GroupDevice;
import com.home.service.BluetoothLeServiceSingle;
import com.home.service.MyServiceConenction;
import com.home.utils.BitmapTool;
import com.home.utils.ImageDispose;
import com.home.widget.effects.Effectstype;
import com.home.widget.effects.NiftyDialogBuilder;
import com.ledlamp.R;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ServicesFragment extends LedBleFragment implements View.OnClickListener, DragGridView.ClickCallBack, IListener {
    public static final int ADD_SCENE_REQUEST_CODE = 1;
    private static final String PrivacypolicyAgree = "PrivacypolicyAgree";
    private static final int REQUEST_BLUETOOTH_CONNECT = 225;
    private static final int REQUEST_BLUETOOTH_SCAN = 224;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 223;
    private static final String TAG = "ServicesFragment";
    private static FragmentActivity mActivity;
    private static BluetoothAdapter mBluetoothAdapter;
    public static String sceneBeanFragment;
    private static SharedPreferences sp;
    private static SharedPreferences spPrivacypolicy;
    private BluetoothLeScanner bleScanner;
    private BluetoothManager bluetoothManager;
    private Effectstype effect;
    private volatile HashMap<String, Boolean> hashMapLock;
    @BindView(R.id.ll_NoScene)
    LinearLayout ll_NoScene;
    private BluetoothLeServiceSingle mBluetoothLeService;
    private DragGridView mGridView;
    private MyServiceConenction myServiceConenction;
    private Handler refreshBLEHandler;
    private Runnable refreshBLERunnable;
    private Effectstype requestLocationEffect;
    private SceneAdpter sceneAdpter;
    @BindView(R.id.srlComment)
    SwipeRefreshLayout srlComment;
    private TextView textView;
    private Intent turnOnBluetoothIntent;
    private ArrayList<SceneBean> sceneBeans = new ArrayList<>();
    private ArrayList<String> serviceStrArr = new ArrayList<>();
    private boolean first = true;
    private int OPEN_BLE = FirstActivity.RESULT333;
    private Boolean hasOpenBle = true;
    private final int MSG_RELOAD_MAIN_THREAD = 1000;
    private final int MSG_START_CONNECT = 10000;
    private ScanCallback scanCallback = new ScanCallback() { // from class: com.home.fragment.service.ServicesFragment.12
        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            BluetoothDevice device = scanResult.getDevice();
            if (device.getName() == null || !device.getName().startsWith(BluetoothLeServiceSingle.NAME_START_LED) || LedBleApplication.getApp().getBleDevices().contains(device)) {
                return;
            }
            LedBleApplication.getApp().getBleDevices().add(device);
            ServicesFragment.this.updateNewFindDevice();
            if (LedBleApplication.getApp().isAuto()) {
                if (ServicesFragment.this.conectHandler != null) {
                    ServicesFragment.this.conectHandler.sendEmptyMessage(10000);
                    return;
                }
                return;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<BluetoothDataModel> it = LedBleApplication.getApp().getManmualBleDevices().iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getDevice());
            }
            if (!arrayList.contains(device)) {
                BluetoothDataModel bluetoothDataModel = new BluetoothDataModel();
                bluetoothDataModel.setDevice(device);
                LedBleApplication.getApp().getManmualBleDevices().add(bluetoothDataModel);
            }
            ServicesFragment.this.refreshApp(device);
        }
    };
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() { // from class: com.home.fragment.service.ServicesFragment.13
        @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            ServicesFragment.mActivity.runOnUiThread(new Runnable() { // from class: com.home.fragment.service.ServicesFragment.13.1
                @Override // java.lang.Runnable
                public void run() {
                    String name;
                    if (bluetoothDevice == null || LedBleApplication.getApp().getBleDevices().contains(bluetoothDevice) || bluetoothDevice.getName() == null || (name = bluetoothDevice.getName()) == null || !name.startsWith(BluetoothLeServiceSingle.NAME_START_LED)) {
                        return;
                    }
                    LedBleApplication.getApp().getBleDevices().add(bluetoothDevice);
                    ServicesFragment.this.updateNewFindDevice();
                    if (LedBleApplication.getApp().isAuto()) {
                        if (ServicesFragment.this.conectHandler != null) {
                            ServicesFragment.this.conectHandler.sendEmptyMessage(10000);
                            return;
                        }
                        return;
                    }
                    BluetoothDataModel bluetoothDataModel = new BluetoothDataModel();
                    bluetoothDataModel.setDevice(bluetoothDevice);
                    LedBleApplication.getApp().getManmualBleDevices().add(bluetoothDataModel);
                }
            });
        }
    };
    private boolean booleanCanStart = false;
    private Handler conectHandler = new Handler() { // from class: com.home.fragment.service.ServicesFragment.16
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 1000) {
                if (i == 10000 && !ServicesFragment.this.booleanCanStart) {
                    ServicesFragment.this.booleanCanStart = true;
                    Tool.delay(100L);
                    ServicesFragment.this.startConnectDevices();
                    return;
                }
                return;
            }
            if (ServicesFragment.this.sceneBeans.size() <= 0) {
                if (ServicesFragment.this.ll_NoScene != null) {
                    ServicesFragment.this.ll_NoScene.setVisibility(0);
                }
            } else if (ServicesFragment.this.ll_NoScene != null) {
                ServicesFragment.this.ll_NoScene.setVisibility(8);
            }
            if (ServicesFragment.this.sceneAdpter != null) {
                ServicesFragment.this.sceneAdpter.notifyDataSetChanged();
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.home.fragment.service.ServicesFragment.18
        public int DEFAULT_VALUE_BULUETOOTH = 1000;

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            intent.getStringExtra("name");
            String stringExtra = intent.getStringExtra(GroupDevice.ADDRESSNUM);
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", this.DEFAULT_VALUE_BULUETOOTH)) {
                    case 10:
                        ServicesFragment.this.hasOpenBle = false;
                        return;
                    case 11:
                        ServicesFragment.this.hasOpenBle = false;
                        return;
                    case 12:
                        ServicesFragment.this.hasOpenBle = true;
                        ServicesFragment.this.refreshBluetoothAdapter();
                        ServicesFragment.this.refreshDevices();
                        return;
                    case 13:
                        ServicesFragment.this.hasOpenBle = false;
                        ServicesFragment.this.clearList();
                        return;
                    default:
                        return;
                }
            } else if (BluetoothLeServiceSingle.ACTION_GATT_CONNECTED.equals(action)) {
                LedBleApplication.getApp().getHashMapConnect().put(stringExtra, true);
                ServicesFragment.this.updateNewFindDevice();
            } else if (BluetoothLeServiceSingle.ACTION_GATT_DISCONNECTED.equals(action)) {
                LedBleApplication.getApp().getHashMapConnect().put(stringExtra, false);
                LedBleApplication.getApp().getHashMapConnect().remove(stringExtra);
                LedBleApplication.getApp().getBleGattMap().remove(stringExtra);
                Iterator<BluetoothDevice> it = LedBleApplication.getApp().getBleDevices().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BluetoothDevice next = it.next();
                    if (next.getAddress().equals(stringExtra)) {
                        LedBleApplication.getApp().getBleDevices().remove(next);
                        break;
                    }
                }
                Iterator<BluetoothDataModel> it2 = LedBleApplication.getApp().getManmualBleDevices().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    BluetoothDataModel next2 = it2.next();
                    if (next2.getDevice().getAddress().equals(stringExtra)) {
                        LedBleApplication.getApp().getManmualBleDevices().remove(next2);
                        break;
                    }
                }
                ServicesFragment.this.updateNewFindDevice();
                ServicesFragment.this.hashMapLock.remove(stringExtra);
                if (LedBleApplication.getApp().isAuto() && ServicesFragment.this.refreshBLEHandler == null) {
                    ServicesFragment.this.refreshBLE();
                }
            } else if (BluetoothLeServiceSingle.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                if (ServicesFragment.this.refreshBLEHandler != null) {
                    ServicesFragment.this.refreshBLEHandler.removeCallbacks(ServicesFragment.this.refreshBLERunnable);
                    ServicesFragment.this.refreshBLEHandler = null;
                }
                BluetoothGatt bluetoothGatt = ServicesFragment.this.mBluetoothLeService.getBluetoothGatt();
                if (bluetoothGatt.getDevice().getName().contains("LEDDMX-02-")) {
                    bluetoothGatt.requestMtu(500);
                }
                LedBleApplication.getApp().getBleGattMap().put(stringExtra, bluetoothGatt);
                ServicesFragment.this.hashMapLock.put(stringExtra, true);
                ListenerManager.getInstance().sendBroadCast(Constant.SmartTimeNowSet);
            } else if (BluetoothLeServiceSingle.ACTION_DATA_AVAILABLE.equals(action)) {
                String stringExtra2 = intent.getStringExtra(BluetoothLeServiceSingle.EXTRA_DATA);
                if (context != null) {
                    String perference = SharePersistent.getPerference(context, Constant.Activity);
                    if (stringExtra2.contains("1A 05") || stringExtra2.contains("1a 05")) {
                        return;
                    }
                    if (perference.equalsIgnoreCase(Constant.CurrentQueryActivity)) {
                        String perference2 = SharePersistent.getPerference(context, Constant.CurrentQueryActivity);
                        if (perference2.length() > 0) {
                            SharePersistent.savePerference(context, Constant.CurrentQueryActivity, perference2 + "" + stringExtra2);
                            return;
                        }
                        SharePersistent.savePerference(context, Constant.CurrentQueryActivity, stringExtra2);
                    } else if (perference.equalsIgnoreCase(Constant.TimingQueryActivity)) {
                        String perference3 = SharePersistent.getPerference(context, Constant.TimingQueryActivity);
                        if (perference3.contains(stringExtra2)) {
                            return;
                        }
                        if (perference3.length() > 0) {
                            SharePersistent.savePerference(context, Constant.TimingQueryActivity, perference3 + " " + stringExtra2);
                            return;
                        }
                        SharePersistent.savePerference(context, Constant.TimingQueryActivity, stringExtra2);
                    } else if (perference.equalsIgnoreCase(Constant.MaterialLibraryActivity)) {
                        ReceiveDataListenerManager.getInstance().sendBroadCast(Constant.MaterialLibraryActivity, stringExtra2);
                    } else if (perference.equalsIgnoreCase(Constant.DMX02ModeFragment)) {
                        ReceiveDataListenerManager.getInstance().sendBroadCast(Constant.DMX02ModeFragment, stringExtra2);
                    } else if (perference.equalsIgnoreCase(Constant.DMX02TextFragment)) {
                        ReceiveDataListenerManager.getInstance().sendBroadCast(Constant.DMX02TextFragment, stringExtra2);
                    } else if (perference.equalsIgnoreCase(Constant.DMX02GraffitFragment)) {
                        ReceiveDataListenerManager.getInstance().sendBroadCast(Constant.DMX02GraffitFragment, stringExtra2);
                    }
                }
            }
        }
    };

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mActivity = getActivity();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_scene, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mActivity = getActivity();
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        initBLE();
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        SwipeRefreshLayout swipeRefreshLayout = this.srlComment;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        }
        this.mGridView = (DragGridView) getActivity().findViewById(R.id.gvScenes);
        this.textView = (TextView) getActivity().findViewById(R.id.textview);
        ArrayList<SceneBean> arrayList = (ArrayList) SharePersistent.getObjectValue(getContext(), CommonConstant.KEY_HOME_SCENES);
        this.sceneBeans = arrayList;
        if (arrayList == null) {
            this.sceneBeans = new ArrayList<>();
        }
        if (this.sceneBeans.size() <= 0) {
            this.ll_NoScene.setVisibility(0);
        } else {
            this.ll_NoScene.setVisibility(8);
        }
        SceneAdpter sceneAdpter = new SceneAdpter(getContext(), this.sceneBeans);
        this.sceneAdpter = sceneAdpter;
        this.mGridView.setAdapter((ListAdapter) sceneAdpter);
        this.mGridView.setSendCallBack(this);
        this.sceneAdpter.notifyDataSetChanged();
        this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.fragment.service.ServicesFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SceneBean sceneBean = (SceneBean) ServicesFragment.this.sceneBeans.get(i);
                if (sceneBean != null) {
                    ServicesFragment.this.goToMain(sceneBean.getSerice());
                    SharePersistent.setObjectValue(ServicesFragment.this.getContext(), CommonConstant.APP_MODE, sceneBean);
                    ServicesFragment.sceneBeanFragment = sceneBean.getName();
                }
            }
        });
        sceneBeanFragment = SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY);
        SharePersistent.getBoolean(getContext(), CommonConstant.GOTO_MAIN);
        String str = sceneBeanFragment;
        if (str != null) {
            goToMain(str);
        }
        ListenerManager.getInstance().registerListtener(this);
        setPrivacypolicy();
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
        this.srlComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.home.fragment.service.ServicesFragment.2
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                LedBleApplication.getApp().setAuto(false);
                ServicesFragment.this.reloadConnected();
                ServicesFragment.this.refreshDevices();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadConnected() {
        if (LedBleApplication.getApp().getBleGattMap().size() > 0) {
            for (final String str : LedBleApplication.getApp().getBleGattMap().keySet()) {
                new Thread(new Runnable() { // from class: com.home.fragment.service.ServicesFragment.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGatt bluetoothGatt = LedBleApplication.getApp().getBleGattMap().get(str);
                        LedBleApplication.getApp().getBleDevices().add(bluetoothGatt.getDevice());
                        ServicesFragment.this.refreshApp(bluetoothGatt.getDevice());
                    }
                }).start();
            }
        }
    }

    private String getConnectWifiSsid() {
        if (getContext() == null || getContext().getApplicationContext() == null) {
            return "";
        }
        WifiInfo connectionInfo = ((WifiManager) getContext().getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        Log.d("wifiInfo", connectionInfo.toString());
        Log.d("SSID", connectionInfo.getSSID());
        return connectionInfo.getSSID().replace("\"", "");
    }

    private boolean isAndroid12() {
        return Build.VERSION.SDK_INT >= 31;
    }

    public static BluetoothAdapter getBluetoothAdapter() {
        BluetoothAdapter bluetoothAdapter = mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            return bluetoothAdapter;
        }
        return null;
    }

    private void setPrivacypolicy() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.AgreeAPPPolicy, 0);
        spPrivacypolicy = sharedPreferences;
        if (Boolean.valueOf(sharedPreferences.getBoolean(PrivacypolicyAgree, false)).booleanValue()) {
            return;
        }
        showPrivacyolicyDialog();
    }

    private void showPrivacyolicyDialog() {
        if (getActivity().isFinishing()) {
            return;
        }
        final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(getContext(), R.style.dialog_user_agreement_and_privacy_policy);
        this.effect = Effectstype.SlideBottom;
        niftyDialogBuilder.setCancelable(false);
        niftyDialogBuilder.withTitle(getResources().getString(R.string.privacy_policy)).withTitleColor("#000000").withDividerColor("#11000000").isCancelableOnTouchOutside(false).withDuration(200).withEffect(this.effect).withButton1Text(getResources().getString(R.string.not_used)).withButton2Text(getResources().getString(R.string.agree));
        niftyDialogBuilder.setCustomView(R.layout.activity_spannable, getContext());
        TextView textView = (TextView) niftyDialogBuilder.getContentView().findViewById(R.id.span_builder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(getResources().getColor(17170445));
        textView.setText(getBuilder());
        niftyDialogBuilder.setButton1Click(new View.OnClickListener() { // from class: com.home.fragment.service.ServicesFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ServicesFragment.this.exitAPP();
                ServicesFragment.spPrivacypolicy.edit().putBoolean(ServicesFragment.PrivacypolicyAgree, false).commit();
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).setButton2Click(new View.OnClickListener() { // from class: com.home.fragment.service.ServicesFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ServicesFragment.spPrivacypolicy.edit().putBoolean(ServicesFragment.PrivacypolicyAgree, true).commit();
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitAPP() {
        for (ActivityManager.AppTask appTask : ((ActivityManager) getContext().getSystemService("activity")).getAppTasks()) {
            appTask.finishAndRemoveTask();
        }
        System.exit(0);
    }

    private SpannableStringBuilder getBuilder() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.privacy_policy_content));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.home.fragment.service.ServicesFragment.6
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(ServicesFragment.this.getContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.ServiceAgreement);
                ServicesFragment.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (getResources().getString(R.string.privacy_policy).contains("服务协议")) {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.privacy_policy_content1).length(), getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.privacy_policy_content1).length(), getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + 1, 34);
        }
        ClickableSpan clickableSpan2 = new ClickableSpan() { // from class: com.home.fragment.service.ServicesFragment.7
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                if (ServicesFragment.this.getResources().getString(R.string.privacy_policy).contains("服务协议")) {
                    Intent intent = new Intent(ServicesFragment.this.getContext(), PrivacyPolicyActivity.class);
                    intent.putExtra("scene", Constant.PricavyPolicy);
                    ServicesFragment.this.startActivity(intent);
                    return;
                }
                ServicesFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.toutiao.com/article/7239234156356534823/?log_from=42aa3ecbe5d768_1685515643598")));
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (getResources().getString(R.string.privacy_policy).contains("服务协议")) {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length(), getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length() + getResources().getString(R.string.privacy_policy_content4).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length() + 2, getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length() + getResources().getString(R.string.privacy_policy_content4).length() + 3, 34);
        }
        return spannableStringBuilder;
    }

    @Override // com.common.listener.IListener
    public void notifyAllActivity(String str) {
        String str2 = sceneBeanFragment;
        if (str2 == null || str2.equalsIgnoreCase(CommonConstant.LEDWiFi)) {
            return;
        }
        if (str.equalsIgnoreCase(Constant.StartLeScan)) {
            startLeScan();
        } else if (str.equalsIgnoreCase(Constant.StopLeScan)) {
            stopLeScan();
        } else if (str.equalsIgnoreCase(Constant.RefreshBluetooth)) {
            refreshDevices();
        } else if (str.equalsIgnoreCase(Constant.ManualModeConnectDevice)) {
            this.conectHandler.sendEmptyMessage(10000);
        }
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return getSp(context).getBoolean(str, z);
    }

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("SpUtil", 0);
        }
        return sp;
    }

    public void goToMain(String str) {
        if (str == null) {
            return;
        }
        LedBleApplication.getApp().setSceneBean(str.replaceAll("-", ""));
        SharePersistent.savePerference(getActivity(), Constant.CUSTOM_DIY_APPKEY, str);
        if (str.contains(CommonConstant.LEDBLE) || str.contains("LEDDMX-00-") || str.contains("LEDDMX-01-") || str.contains("LEDCAR-00-") || str.contains("LEDCAR-01-") || str.equalsIgnoreCase(CommonConstant.LEDSMART) || str.equalsIgnoreCase(CommonConstant.LEDSTAGE) || str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
            Intent intent = new Intent(getActivity(), MainActivity_BLE.class);
            intent.putExtra("scene", str);
            getActivity().startActivity(intent);
        } else if (str.equalsIgnoreCase("LEDDMX-02-")) {
            Intent intent2 = new Intent(getActivity(), MainActivity_DMX02.class);
            intent2.putExtra("scene", str);
            getActivity().startActivity(intent2);
        } else if (str.equalsIgnoreCase("LEDDMX-03-")) {
            Intent intent3 = new Intent(getActivity(), MainActivity_DMX03.class);
            intent3.putExtra("scene", str);
            getActivity().startActivity(intent3);
        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIGHT)) {
            Intent intent4 = new Intent(getActivity(), MainActivity_BLE.class);
            intent4.putExtra("scene", str);
            getActivity().startActivity(intent4);
        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
            Intent intent5 = new Intent(getActivity(), MainActivity_LIKE.class);
            intent5.putExtra("scene", str);
            getActivity().startActivity(intent5);
        } else {
            str.equalsIgnoreCase(CommonConstant.LEDWiFi);
        }
    }

    private void deleteItem(SceneBean sceneBean) {
        for (int i = 0; i < this.sceneBeans.size(); i++) {
            if (this.sceneBeans.get(i).getSerice().equalsIgnoreCase(sceneBean.getSerice())) {
                this.sceneBeans.remove(i);
            }
        }
        if (LedBleApplication.getApp().getBleDevices().size() > 0) {
            for (int i2 = 0; i2 < LedBleApplication.getApp().getBleDevices().size(); i2++) {
                BluetoothDevice bluetoothDevice = LedBleApplication.getApp().getBleDevices().get(i2);
                Log.e(TAG, "remove bluetoothDevice.getName() = " + bluetoothDevice.getName());
                Log.e(TAG, "remove sceneBean.getSerice() = " + sceneBean.getSerice());
                if (bluetoothDevice.getName().contains(sceneBean.getSerice())) {
                    LedBleApplication.getApp().getBleDevices().remove(bluetoothDevice);
                }
            }
        }
        Log.e(TAG, "LedBleApplication.getApp().getBleDevices().size = " + LedBleApplication.getApp().getBleDevices().size());
        SharePersistent.setObjectValue(getContext(), CommonConstant.KEY_HOME_SCENES, this.sceneBeans);
        if (this.sceneBeans.size() <= 0) {
            this.ll_NoScene.setVisibility(0);
        } else {
            this.ll_NoScene.setVisibility(8);
        }
        this.sceneAdpter.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.imageViewNavRight) {
            return;
        }
        startActivityForResult(new Intent(getContext(), AddSceneActivity.class), 1);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.common.view.DragGridView.ClickCallBack
    public void clickV() {
        Log.e(TAG, "clickV");
        this.srlComment.setEnabled(false);
        this.textView.setVisibility(0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int i = displayMetrics.heightPixels;
        this.textView.post(new Runnable() { // from class: com.home.fragment.service.ServicesFragment.8
            @Override // java.lang.Runnable
            public void run() {
                ServicesFragment.this.mGridView.setViewHeight(i - (ServicesFragment.this.textView.getHeight() * 2));
            }
        });
    }

    @Override // com.common.view.DragGridView.ClickCallBack
    public void clickD() {
        Log.e(TAG, "clickD");
    }

    @Override // com.common.view.DragGridView.ClickCallBack
    public void clickG(int i) {
        Log.e(TAG, "clickG， position=" + i);
        if (i >= 0) {
            deleteItem(this.sceneBeans.get(i));
        }
        this.textView.setVisibility(8);
    }

    @Override // com.common.view.DragGridView.ClickCallBack
    public void clickGone(int i, int i2) {
        Log.e(TAG, "clickGone， start=" + i + " , end = " + i2);
        this.srlComment.setEnabled(true);
        this.textView.setVisibility(8);
        if (this.sceneBeans.size() > 1) {
            SharePersistent.setObjectValue(mActivity, CommonConstant.KEY_HOME_SCENES, this.sceneBeans);
        }
    }

    private void initBLE() {
        this.hashMapLock = new HashMap<>();
        mActivity.registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (!mActivity.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(getContext(), (int) R.string.ble_not_supported, 0).show();
            Tool.exitApp();
        }
        BluetoothManager bluetoothManager = (BluetoothManager) mActivity.getSystemService("bluetooth");
        this.bluetoothManager = bluetoothManager;
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (Build.VERSION.SDK_INT >= 23) {
            this.bleScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (mBluetoothAdapter == null) {
            Tool.ToastShow(getActivity(), (int) R.string.ble_not_supported);
            Tool.exitApp();
        }
        MyServiceConenction myServiceConenction = new MyServiceConenction();
        this.myServiceConenction = myServiceConenction;
        myServiceConenction.setServiceConnectListener(new MyServiceConenction.ServiceConnectListener() { // from class: com.home.fragment.service.ServicesFragment.9
            @Override // com.home.service.MyServiceConenction.ServiceConnectListener
            public void onConnect(ComponentName componentName, IBinder iBinder, BluetoothLeServiceSingle bluetoothLeServiceSingle) {
                ServicesFragment.this.mBluetoothLeService = bluetoothLeServiceSingle;
                if (!ServicesFragment.this.mBluetoothLeService.initialize()) {
                    Log.e(LedBleApplication.tag, "Unable to initialize Bluetooth");
                } else {
                    Log.e(LedBleApplication.tag, "Initialize Bluetooth");
                }
            }

            @Override // com.home.service.MyServiceConenction.ServiceConnectListener
            public void onDisConnect(ComponentName componentName) {
                Log.e(ServicesFragment.TAG, "onDisConnect: " + componentName);
            }
        });
        getActivity().bindService(new Intent(getActivity(), BluetoothLeServiceSingle.class), this.myServiceConenction, 1);
    }

    protected void refreshBluetoothAdapter() {
        BluetoothManager bluetoothManager = (BluetoothManager) mActivity.getSystemService("bluetooth");
        this.bluetoothManager = bluetoothManager;
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (Build.VERSION.SDK_INT >= 23) {
            this.bleScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
    }

    protected void refreshDevices() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (isAndroid12()) {
                if (ContextCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH_SCAN") != 0 || ContextCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH_CONNECT") != 0) {
                    requestPermissions(new String[]{"android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT"}, REQUEST_BLUETOOTH_SCAN);
                } else if (getBluetoothAdapter() != null && !getBluetoothAdapter().isEnabled()) {
                    turnOnBluetooth();
                } else if (activity != null) {
                    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                        requestLocationPermission();
                    } else {
                        startLeScan();
                    }
                }
            } else if (ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") != 0) {
                requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_PERMISSION_LOCATION);
            } else if (getBluetoothAdapter() != null && !getBluetoothAdapter().isEnabled()) {
                turnOnBluetooth();
            } else if (activity != null) {
                if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    requestLocationPermission();
                } else {
                    startLeScan();
                }
            }
        }
    }

    protected void startLeScan() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (isAndroid12()) {
                if (ContextCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH_SCAN") != 0 || ContextCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH_CONNECT") != 0) {
                    requestPermissions(new String[]{"android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT"}, REQUEST_BLUETOOTH_SCAN);
                } else if (this.hasOpenBle.booleanValue() && getBluetoothAdapter().isEnabled()) {
                    if (Build.VERSION.SDK_INT < 23) {
                        BluetoothAdapter bluetoothAdapter = mBluetoothAdapter;
                        if (bluetoothAdapter != null) {
                            bluetoothAdapter.startLeScan(this.mLeScanCallback);
                        }
                    } else {
                        BluetoothLeScanner bluetoothLeScanner = this.bleScanner;
                        if (bluetoothLeScanner != null) {
                            bluetoothLeScanner.startScan(this.scanCallback);
                        }
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() { // from class: com.home.fragment.service.ServicesFragment.10
                        @Override // java.lang.Runnable
                        public void run() {
                            ServicesFragment.this.stopLeScan();
                            handler.removeCallbacks(this);
                        }
                    }, 6000L);
                }
            } else if (this.hasOpenBle.booleanValue() && getBluetoothAdapter().isEnabled()) {
                if (Build.VERSION.SDK_INT < 23) {
                    BluetoothAdapter bluetoothAdapter2 = mBluetoothAdapter;
                    if (bluetoothAdapter2 != null) {
                        bluetoothAdapter2.startLeScan(this.mLeScanCallback);
                    }
                } else {
                    BluetoothLeScanner bluetoothLeScanner2 = this.bleScanner;
                    if (bluetoothLeScanner2 != null) {
                        bluetoothLeScanner2.startScan(this.scanCallback);
                    }
                }
                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() { // from class: com.home.fragment.service.ServicesFragment.11
                    @Override // java.lang.Runnable
                    public void run() {
                        ServicesFragment.this.stopLeScan();
                        handler2.removeCallbacks(this);
                    }
                }, 6000L);
            }
        }
    }

    protected void stopLeScan() {
        ScanCallback scanCallback;
        BluetoothAdapter.LeScanCallback leScanCallback;
        SwipeRefreshLayout swipeRefreshLayout = this.srlComment;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (this.hasOpenBle.booleanValue()) {
            if (getBluetoothAdapter().isEnabled()) {
                if (Build.VERSION.SDK_INT < 23) {
                    if (this.bleScanner != null && (leScanCallback = this.mLeScanCallback) != null) {
                        mBluetoothAdapter.stopLeScan(leScanCallback);
                    }
                } else {
                    BluetoothLeScanner bluetoothLeScanner = this.bleScanner;
                    if (bluetoothLeScanner != null && (scanCallback = this.scanCallback) != null) {
                        bluetoothLeScanner.stopScan(scanCallback);
                    }
                }
            }
            if (LedBleApplication.getApp().getBleDevices().size() > 0) {
                int i = 0;
                while (true) {
                    if (i >= LedBleApplication.getApp().getBleDevices().size()) {
                        break;
                    } else if (!LedBleApplication.getApp().getBleGattMap().containsKey(LedBleApplication.getApp().getBleDevices().get(i).getAddress())) {
                        startConnectDevices();
                        break;
                    } else {
                        i++;
                    }
                }
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                if (getConnectWifiSsid().startsWith(BluetoothLeServiceSingle.LED_) || getConnectWifiSsid().startsWith(BluetoothLeServiceSingle.LED)) {
                    SceneBean sceneBean = new SceneBean("LED LIGHT", CommonConstant.LEDLIGHT, ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.ledlight)), 0);
                    ArrayList<SceneBean> arrayList = this.sceneBeans;
                    if (arrayList != null) {
                        if (!arrayList.contains(sceneBean)) {
                            this.sceneBeans.add(sceneBean);
                        }
                        if (this.sceneBeans.size() <= 0) {
                            this.ll_NoScene.setVisibility(0);
                        } else {
                            this.ll_NoScene.setVisibility(8);
                        }
                    }
                    this.sceneAdpter.notifyDataSetChanged();
                    SharePersistent.setObjectValue(activity, CommonConstant.KEY_HOME_SCENES, this.sceneBeans);
                }
            }
        }
    }

    public void refreshApp(BluetoothDevice bluetoothDevice) {
        this.serviceStrArr.clear();
        FragmentActivity activity = getActivity();
        if (this.sceneBeans == null || activity == null) {
            return;
        }
        int i = 0;
        if (bluetoothDevice.getName().startsWith(BluetoothLeServiceSingle.LED) || bluetoothDevice.getName().startsWith(BluetoothLeServiceSingle.LED_)) {
            SceneBean sceneBean = new SceneBean("LED BLE", CommonConstant.LEDBLE_01, ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.ledble01)), 0);
            for (int i2 = 0; i2 < this.sceneBeans.size(); i2++) {
                this.serviceStrArr.add(this.sceneBeans.get(i2).getSerice());
            }
            if (!this.serviceStrArr.contains(CommonConstant.LEDBLE_01)) {
                this.sceneBeans.add(sceneBean);
            }
            SceneBean sceneBean2 = new SceneBean("LED DMX", "LEDDMX-01-", ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.leddmx01)), 0);
            while (i < this.sceneBeans.size()) {
                this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                i++;
            }
            if (!this.serviceStrArr.contains("LEDDMX-01-")) {
                this.sceneBeans.add(sceneBean2);
            }
        } else if (bluetoothDevice.getName().startsWith(CommonConstant.LEDBLE)) {
            if (bluetoothDevice.getName().startsWith(CommonConstant.LEDBLE_00)) {
                SceneBean sceneBean3 = new SceneBean("LED BLE", CommonConstant.LEDBLE_00, ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.ledble00)), 0);
                while (i < this.sceneBeans.size()) {
                    this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                    i++;
                }
                if (!this.serviceStrArr.contains(CommonConstant.LEDBLE_00)) {
                    this.sceneBeans.add(sceneBean3);
                }
            } else {
                SceneBean sceneBean4 = new SceneBean("LED BLE", CommonConstant.LEDBLE_01, ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.ledble01)), 0);
                while (i < this.sceneBeans.size()) {
                    this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                    i++;
                }
                if (!this.serviceStrArr.contains(CommonConstant.LEDBLE_01)) {
                    this.sceneBeans.add(sceneBean4);
                }
            }
        } else if (bluetoothDevice.getName().contains(BluetoothLeServiceSingle.LEDDMX)) {
            if (bluetoothDevice.getName().contains("LEDDMX-00-")) {
                SceneBean sceneBean5 = new SceneBean("LED DMX", "LEDDMX-00-", ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.leddmx00)), 0);
                while (i < this.sceneBeans.size()) {
                    this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                    i++;
                }
                if (!this.serviceStrArr.contains("LEDDMX-00-")) {
                    this.sceneBeans.add(sceneBean5);
                }
            } else if (bluetoothDevice.getName().startsWith("LEDDMX-02-")) {
                if (activity != null) {
                    SceneBean sceneBean6 = new SceneBean("LED DMX", "LEDDMX-02-", ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.leddmx02)), 0);
                    while (i < this.sceneBeans.size()) {
                        this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                        i++;
                    }
                    if (!this.serviceStrArr.contains("LEDDMX-02-")) {
                        this.sceneBeans.add(sceneBean6);
                    }
                }
            } else if (!bluetoothDevice.getName().startsWith("LEDDMX-03-")) {
                SceneBean sceneBean7 = new SceneBean("LED DMX", "LEDDMX-01-", ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.leddmx01)), 0);
                while (i < this.sceneBeans.size()) {
                    this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                    i++;
                }
                if (!this.serviceStrArr.contains("LEDDMX-01-")) {
                    this.sceneBeans.add(sceneBean7);
                }
            } else if (activity != null) {
                SceneBean sceneBean8 = new SceneBean("LED DMX", "LEDDMX-03-", ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.leddmx03)), 0);
                while (i < this.sceneBeans.size()) {
                    this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                    i++;
                }
                if (!this.serviceStrArr.contains("LEDDMX-03-")) {
                    this.sceneBeans.add(sceneBean8);
                }
            }
        } else if (bluetoothDevice.getName().startsWith("LEDCAR-00-")) {
            SceneBean sceneBean9 = new SceneBean("LEDCAR00", "LEDCAR-00-", ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.ledcar00)), 0);
            while (i < this.sceneBeans.size()) {
                this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                i++;
            }
            if (!this.serviceStrArr.contains("LEDCAR-00-")) {
                this.sceneBeans.add(sceneBean9);
            }
        } else if (!bluetoothDevice.getName().startsWith("LEDCAR-01-")) {
            String[] strArr = {CommonConstant.LEDBLE, CommonConstant.LEDSMART, CommonConstant.LEDSTAGE, CommonConstant.LEDSUN, CommonConstant.LEDLIKE};
            String[] strArr2 = {CommonConstant.LEDBLE, CommonConstant.LEDSMART, CommonConstant.LEDSTAGE, CommonConstant.LEDSUN, CommonConstant.LEDLIKE};
            int[] iArr = {R.drawable.ledble, R.drawable.ledsmart, R.drawable.ledstage, R.drawable.ledsun, R.drawable.ledlike};
            String str = bluetoothDevice.getName().split("-")[0];
            for (int i3 = 0; i3 < 5; i3++) {
                if (str.equalsIgnoreCase(strArr[i3])) {
                    SceneBean sceneBean10 = new SceneBean(strArr2[i3], strArr[i3], ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, iArr[i3])), 0);
                    for (int i4 = 0; i4 < this.sceneBeans.size(); i4++) {
                        this.serviceStrArr.add(this.sceneBeans.get(i4).getSerice());
                    }
                    if (!this.serviceStrArr.contains(strArr[i3])) {
                        this.sceneBeans.add(sceneBean10);
                    }
                }
            }
        } else if (activity != null) {
            SceneBean sceneBean11 = new SceneBean("LEDCAR01", "LEDCAR-01-", ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(activity, R.drawable.ledcar01)), 0);
            while (i < this.sceneBeans.size()) {
                this.serviceStrArr.add(this.sceneBeans.get(i).getSerice());
                i++;
            }
            if (!this.serviceStrArr.contains("LEDCAR-01-")) {
                this.sceneBeans.add(sceneBean11);
            }
        }
        this.conectHandler.sendEmptyMessage(1000);
        SharePersistent.setObjectValue(activity, CommonConstant.KEY_HOME_SCENES, this.sceneBeans);
    }

    public void turnOnBluetooth() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (isAndroid12()) {
                if (ContextCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH_SCAN") != 0 || ContextCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH_CONNECT") != 0) {
                    requestPermissions(new String[]{"android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT"}, REQUEST_BLUETOOTH_SCAN);
                } else if (this.turnOnBluetoothIntent == null) {
                    Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                    this.turnOnBluetoothIntent = intent;
                    startActivityForResult(intent, this.OPEN_BLE);
                }
            } else if (ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") != 0) {
                requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_PERMISSION_LOCATION);
            } else if (this.turnOnBluetoothIntent == null) {
                Intent intent2 = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.turnOnBluetoothIntent = intent2;
                startActivityForResult(intent2, this.OPEN_BLE);
            }
        }
    }

    private void requestLocationPermission() {
        if (getActivity() != null) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.ACCESS_FINE_LOCATION")) {
                FragmentActivity fragmentActivity = mActivity;
                if (fragmentActivity == null || fragmentActivity.isFinishing() || this.requestLocationEffect != null) {
                    return;
                }
                final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(mActivity, R.style.dialog_user_agreement_and_privacy_policy);
                this.requestLocationEffect = Effectstype.SlideBottom;
                niftyDialogBuilder.setCancelable(false);
                niftyDialogBuilder.withTitle(getResources().getString(R.string.access_request)).withTitleColor("#000000").withDividerColor("#11000000").isCancelableOnTouchOutside(false).withDuration(200).withEffect(this.requestLocationEffect).withButton1Text(getResources().getString(R.string.cancel)).withButton2Text(getResources().getString(R.string.agree));
                niftyDialogBuilder.setCustomView(R.layout.activity_spannable, getActivity());
                TextView textView = (TextView) niftyDialogBuilder.getContentView().findViewById(R.id.span_builder);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setHighlightColor(getResources().getColor(17170445));
                textView.setText(getBuilder());
                niftyDialogBuilder.setButton1Click(new View.OnClickListener() { // from class: com.home.fragment.service.ServicesFragment.15
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (niftyDialogBuilder.isShowing()) {
                            niftyDialogBuilder.dismiss();
                        }
                    }
                }).setButton2Click(new View.OnClickListener() { // from class: com.home.fragment.service.ServicesFragment.14
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (niftyDialogBuilder.isShowing()) {
                            niftyDialogBuilder.dismiss();
                        }
                        ServicesFragment.this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, ServicesFragment.REQUEST_CODE_PERMISSION_LOCATION);
                    }
                }).show();
                return;
            }
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNewFindDevice() {
        ListenerManager.getInstance().sendBroadCast(Constant.UpdateNewFindDevice);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startConnectDevices() {
        PrintStream printStream = System.out;
        printStream.println("mBluetoothLeService:" + this.mBluetoothLeService);
        new Thread(new Runnable() { // from class: com.home.fragment.service.ServicesFragment.17
            @Override // java.lang.Runnable
            public void run() {
                while (ServicesFragment.this.mBluetoothLeService != null) {
                    try {
                        Iterator<BluetoothDevice> it = LedBleApplication.getApp().getBleDevices().iterator();
                        while (it.hasNext()) {
                            BluetoothDevice next = it.next();
                            String address = next.getAddress();
                            String name = next.getName();
                            if (!LedBleApplication.getApp().getBleGattMap().containsKey(address) && ServicesFragment.this.mBluetoothLeService != null) {
                                if (LedBleApplication.getApp().isCanConnect()) {
                                    ServicesFragment.this.mBluetoothLeService.connect(address, name);
                                    ServicesFragment.this.hashMapLock.put(address, false);
                                    Log.e(ServicesFragment.TAG, "connect ================= ");
                                }
                                do {
                                    Tool.delay(200L);
                                } while (!((Boolean) ServicesFragment.this.hashMapLock.get(address)).booleanValue());
                                Tool.delay(200L);
                            } else {
                                Tool.delay(200L);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Tool.delay(200L);
                }
            }
        }).start();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeServiceSingle.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeServiceSingle.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeServiceSingle.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeServiceSingle.ACTION_DATA_AVAILABLE);
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        return intentFilter;
    }

    public void clearList() {
        if (LedBleApplication.getApp().getBleGattMap().size() > 0) {
            for (final String str : LedBleApplication.getApp().getBleGattMap().keySet()) {
                new Thread(new Runnable() { // from class: com.home.fragment.service.ServicesFragment.19
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
        updateNewFindDevice();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshBLE() {
        this.refreshBLEHandler = new Handler();
        Runnable runnable = new Runnable() { // from class: com.home.fragment.service.ServicesFragment.20
            @Override // java.lang.Runnable
            public void run() {
                ServicesFragment.this.refreshDevices();
                if (ServicesFragment.this.refreshBLEHandler != null) {
                    ServicesFragment.this.refreshBLEHandler.postDelayed(ServicesFragment.this.refreshBLERunnable, 8000L);
                }
            }
        };
        this.refreshBLERunnable = runnable;
        this.refreshBLEHandler.postDelayed(runnable, 100L);
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        switch (i) {
            case REQUEST_CODE_PERMISSION_LOCATION /* 223 */:
                if (iArr.length <= 0 || iArr[0] != 0) {
                    return;
                }
                refreshDevices();
                return;
            case REQUEST_BLUETOOTH_SCAN /* 224 */:
                if (iArr.length <= 0 || iArr[0] != 0) {
                    return;
                }
                refreshDevices();
                return;
            case REQUEST_BLUETOOTH_CONNECT /* 225 */:
                if (iArr.length <= 0 || iArr[0] != 0) {
                    return;
                }
                refreshDevices();
                return;
            default:
                return;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        MyServiceConenction myServiceConenction;
        BroadcastReceiver broadcastReceiver;
        super.onDestroy();
        try {
            ListenerManager.getInstance().unRegisterListener(this);
            ReceiveDataListenerManager.getInstance().unRegisterListener((ReceiveDataListener) this);
            FragmentActivity fragmentActivity = mActivity;
            if (fragmentActivity != null && (broadcastReceiver = this.mGattUpdateReceiver) != null) {
                fragmentActivity.unregisterReceiver(broadcastReceiver);
            }
            FragmentActivity fragmentActivity2 = mActivity;
            if (fragmentActivity2 != null && (myServiceConenction = this.myServiceConenction) != null) {
                fragmentActivity2.unbindService(myServiceConenction);
            }
            ArrayList<SceneBean> arrayList = this.sceneBeans;
            if (arrayList == null || arrayList.size() <= 0) {
                SharePersistent.setObjectValue(getContext(), CommonConstant.APP_MODE, null);
            }
            Handler handler = this.refreshBLEHandler;
            if (handler != null) {
                handler.removeCallbacks(this.refreshBLERunnable);
                this.refreshBLEHandler = null;
            }
        } catch (Exception unused) {
        }
    }
}
