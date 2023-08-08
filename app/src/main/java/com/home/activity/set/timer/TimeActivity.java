package com.home.activity.set.timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import com.common.net.NetResult;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.main.MainActivity_BLE;
import com.home.activity.main.MainActivity_DMX02;
import com.home.activity.set.timer.sql.MyDataBase;
import com.home.activity.set.timer.sql.RecordMode;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.constant.CommonConstant;
import com.home.service.BluetoothLeServiceSingle;
import com.ledlamp.R;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
public class TimeActivity extends LedBleActivity {
    private static final String TAG = "TimeActivity";
    public static List<RecordMode> listDate = new ArrayList();
    public static String[] modeble;
    Date date;
    int day;
    int dayhour;
    int dayminute;
    GridView gridView;
    List<String> list;
    private MainActivity_BLE mActivity;
    private MainActivity_DMX02 mActivity_dmx02;
    int mod;
    MyAdapter myAdapter;
    MyDataBase myDataBase;
    int parameter;
    String[] week;
    int[] modeOrder = {0, 255, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    int[] dmx = {0, 254, 255, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, SubsamplingScaleImageView.ORIENTATION_180, 181, 182, 183, 184, 185, 186, 187, PictureConfig.CHOOSE_REQUEST, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, HttpStatus.SC_CREATED, HttpStatus.SC_ACCEPTED, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, HttpStatus.SC_NO_CONTENT, HttpStatus.SC_RESET_CONTENT, HttpStatus.SC_PARTIAL_CONTENT, HttpStatus.SC_MULTI_STATUS, 208, 209, 210};
    int[] light = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, SubsamplingScaleImageView.ORIENTATION_180, 181, 182, 183, 184, 185, 186, 187, PictureConfig.CHOOSE_REQUEST, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, HttpStatus.SC_CREATED, HttpStatus.SC_ACCEPTED, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, HttpStatus.SC_NO_CONTENT, HttpStatus.SC_RESET_CONTENT, HttpStatus.SC_PARTIAL_CONTENT, HttpStatus.SC_MULTI_STATUS, 208, 209, 210, 211};
    private int idx = 0;
    private int size = 0;

    static /* synthetic */ int access$208(TimeActivity timeActivity) {
        int i = timeActivity.size;
        timeActivity.size = i + 1;
        return i;
    }

    static /* synthetic */ int access$308(TimeActivity timeActivity) {
        int i = timeActivity.idx;
        timeActivity.idx = i + 1;
        return i;
    }

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.black), true);
        setContentView(R.layout.activity_time);
        String stringExtra = getIntent().getStringExtra("name");
        String[] stringArray = getResources().getStringArray(R.array.week);
        this.week = stringArray;
        this.list = Arrays.asList(stringArray);
        if (stringExtra.equals("1")) {
            if (LedBleApplication.getApp().getSceneBean().contains("DMX02")) {
                this.mActivity_dmx02 = MainActivity_DMX02.getMainActivity();
                modeble = new String[2];
                String[] stringArray2 = getResources().getStringArray(R.array.timer_model_dmx);
                for (int i = 0; i < stringArray2.length; i++) {
                    if (i >= 0 && i <= 1) {
                        modeble[i] = stringArray2[i];
                    }
                }
            } else {
                this.mActivity = MainActivity_BLE.getMainActivity();
                if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDBLE)) {
                    modeble = getResources().getStringArray(R.array.modelble);
                } else if (MainActivity_BLE.getSceneBean().contains(CommonConstant.LEDDMX)) {
                    modeble = getResources().getStringArray(R.array.timer_model_dmx);
                } else if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDWiFi)) {
                    modeble = getResources().getStringArray(R.array.timer_model_dmx);
                }
            }
        }
        if (MainActivity_BLE.getSceneBean() != null) {
            this.mActivity = MainActivity_BLE.getMainActivity();
            if (MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSTAGE) || MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDLIGHT)) {
                modeble = getResources().getStringArray(R.array.light_mode);
            }
        }
        init();
    }

    public void init() {
        this.myDataBase = MyDataBase.getInstance(this);
        this.myAdapter = new MyAdapter();
        GridView gridView = (GridView) findViewById(R.id.gv);
        this.gridView = gridView;
        gridView.setAdapter((ListAdapter) this.myAdapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.set.timer.TimeActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(TimeActivity.this, ChioceTimeActivity.class);
                intent.putExtra("key", TimeActivity.listDate.get(i));
                TimeActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.ivAddTimer).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.timer.TimeActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (LedBleApplication.getApp().getSceneBean().contains(CommonConstant.LEDDMX) || LedBleApplication.getApp().getSceneBean().contains(CommonConstant.LEDBLE)) {
                    if (TimeActivity.listDate.size() < 16) {
                        TimeActivity.this.startActivity(new Intent(TimeActivity.this, ChioceTimeActivity.class));
                        return;
                    }
                    Toast.makeText(TimeActivity.this, (int) R.string.supported, 1).show();
                } else if (LedBleApplication.getApp().getSceneBean().contains(CommonConstant.LEDLIGHT) || LedBleApplication.getApp().getSceneBean().contains(CommonConstant.LEDSTAGE)) {
                    if (TimeActivity.listDate.size() < 10) {
                        TimeActivity.this.startActivity(new Intent(TimeActivity.this, ChioceTimeActivity.class));
                        return;
                    }
                    Toast.makeText(TimeActivity.this, (int) R.string.supportedlight, 1).show();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.timer.TimeActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TimeActivity.this.finish();
            }
        });
        findViewById(R.id.closeTime).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.timer.TimeActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(TimeActivity.this, R.anim.layout_scale));
                if (TimeActivity.this.mActivity != null) {
                    TimeActivity.this.mActivity.closeTime();
                } else if (TimeActivity.this.mActivity_dmx02 != null) {
                    TimeActivity.this.mActivity_dmx02.closeTime();
                }
            }
        });
        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.timer.TimeActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(TimeActivity.this, R.anim.layout_scale));
                if (LedBleApplication.getApp().getSceneBean().contains("LIGHT") || LedBleApplication.getApp().getSceneBean().contains("STAGE")) {
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis();
                TimeActivity.this.date = new Date(currentTimeMillis);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
                TimeActivity timeActivity = TimeActivity.this;
                timeActivity.dayhour = Integer.parseInt(simpleDateFormat.format(timeActivity.date).trim());
                TimeActivity timeActivity2 = TimeActivity.this;
                timeActivity2.dayminute = Integer.parseInt(simpleDateFormat2.format(timeActivity2.date).trim());
                TimeActivity timeActivity3 = TimeActivity.this;
                timeActivity3.day = Integer.parseInt(TimeActivity.getWeekOfDate(timeActivity3.date));
                TimeActivity.this.size = 0;
                for (int i = 0; i < TimeActivity.listDate.size(); i++) {
                    if (TimeActivity.listDate.get(i).getType() == 1) {
                        TimeActivity.access$208(TimeActivity.this);
                    }
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // from class: com.home.activity.set.timer.TimeActivity.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (TimeActivity.this.idx >= TimeActivity.listDate.size()) {
                            TimeActivity.this.idx = 0;
                            if (TimeActivity.this.mActivity != null) {
                                TimeActivity.this.mActivity.endTime(TimeActivity.this.day, TimeActivity.this.size, TimeActivity.this.dayhour, TimeActivity.this.dayminute);
                            } else if (TimeActivity.this.mActivity_dmx02 != null) {
                                TimeActivity.this.mActivity_dmx02.endTime(TimeActivity.this.day, TimeActivity.this.size, TimeActivity.this.dayhour, TimeActivity.this.dayminute);
                            }
                            handler.removeCallbacks(this);
                            return;
                        }
                        if (TimeActivity.listDate.get(TimeActivity.this.idx).getType() == 1) {
                            TimeActivity.this.parameter = (TimeActivity.this.day << 4) | TimeActivity.this.idx;
                            TimeActivity.this.sendTime(TimeActivity.this.idx);
                        }
                        TimeActivity.access$308(TimeActivity.this);
                        handler.postDelayed(this, 300L);
                    }
                }, 300L);
            }
        });
        this.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.home.activity.set.timer.TimeActivity.6
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long j) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeActivity.this);
                builder.setTitle(R.string.tips);
                builder.setMessage(R.string.timer);
                builder.setPositiveButton(R.string.bind_end, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.TimeActivity.6.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        MyDataBase myDataBase = TimeActivity.this.myDataBase;
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        myDataBase.delete(sceneBean, "_id", new String[]{TimeActivity.listDate.get(i).getId() + ""});
                        TimeActivity.listDate.remove(i);
                        TimeActivity.this.myAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancell_dialog, (DialogInterface.OnClickListener) null).show();
                return true;
            }
        });
    }

    public static String getWeekOfDate(Date date) {
        String[] strArr = {"7", "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(7) - 1;
        if (i < 0) {
            i = 0;
        }
        return strArr[i];
    }

    public void getData() {
        listDate.clear();
        listDate.addAll(this.myDataBase.query(LedBleApplication.getApp().getSceneBean()));
        Collections.sort(listDate);
        this.myAdapter.notifyDataSetChanged();
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        getData();
        if (LedBleApplication.getApp().getSceneBean().contains("LIGHT")) {
            findViewById(R.id.closeTime).setVisibility(8);
        } else if (LedBleApplication.getApp().getSceneBean().contains("STAGE")) {
            findViewById(R.id.closeTime).setVisibility(8);
        }
    }

    /* loaded from: classes.dex */
    class MyHolder {
        ImageView imageView;
        TextView tv_mode;
        TextView tv_time;
        TextView tv_week;

        MyHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class MyAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MyAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return TimeActivity.listDate.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            final MyHolder myHolder;
            final RecordMode recordMode = TimeActivity.listDate.get(i);
            if (view == null) {
                myHolder = new MyHolder();
                view2 = LayoutInflater.from(TimeActivity.this).inflate(R.layout.gv, (ViewGroup) null);
                myHolder.imageView = (ImageView) view2.findViewById(R.id.image);
                myHolder.tv_mode = (TextView) view2.findViewById(R.id.mtv);
                myHolder.tv_time = (TextView) view2.findViewById(R.id.ttv);
                myHolder.tv_week = (TextView) view2.findViewById(R.id.wtv);
                view2.setTag(myHolder);
            } else {
                view2 = view;
                myHolder = (MyHolder) view.getTag();
            }
            if (recordMode.getMinute() < 10 && recordMode.getHour() < 10) {
                TextView textView = myHolder.tv_time;
                textView.setText(NetResult.CODE_OK + recordMode.getHour() + ":0" + recordMode.getMinute());
            } else if (recordMode.getHour() < 10) {
                TextView textView2 = myHolder.tv_time;
                textView2.setText(NetResult.CODE_OK + recordMode.getHour() + ":" + recordMode.getMinute());
            } else if (recordMode.getMinute() < 10) {
                TextView textView3 = myHolder.tv_time;
                textView3.setText(recordMode.getHour() + ":0" + recordMode.getMinute());
            } else {
                TextView textView4 = myHolder.tv_time;
                textView4.setText(recordMode.getHour() + ":" + recordMode.getMinute());
            }
            if (recordMode.getMode() != null) {
                myHolder.tv_mode.setText(TimeActivity.modeble[Integer.parseInt(recordMode.getMode())]);
            }
            if (recordMode.getWeek() != null) {
                StringBuffer stringBuffer = new StringBuffer(100);
                String[] split = recordMode.getWeek().split(" ");
                for (int i2 = 0; i2 < split.length; i2++) {
                    stringBuffer.append(TimeActivity.this.list.get(Integer.parseInt(split[i2])) + " ");
                }
                myHolder.tv_week.setText(stringBuffer);
            }
            myHolder.imageView.setImageResource(recordMode.getType() == 0 ? R.drawable.butn_close : R.drawable.butn_open);
            myHolder.imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.timer.TimeActivity.MyAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (recordMode.getType() != 0) {
                        myHolder.imageView.setImageResource(R.drawable.butn_close);
                        MyDataBase myDataBase = MyDataBase.getInstance(TimeActivity.this);
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        int hour = recordMode.getHour();
                        int minute = recordMode.getMinute();
                        String mode = recordMode.getMode();
                        String week = recordMode.getWeek();
                        myDataBase.update(sceneBean, hour, minute, mode, week, 0, "_id=?", new String[]{recordMode.getId() + ""});
                        TimeActivity.this.getData();
                        return;
                    }
                    myHolder.imageView.setImageResource(R.drawable.butn_open);
                    MyDataBase myDataBase2 = MyDataBase.getInstance(TimeActivity.this);
                    String sceneBean2 = LedBleApplication.getApp().getSceneBean();
                    int hour2 = recordMode.getHour();
                    int minute2 = recordMode.getMinute();
                    String mode2 = recordMode.getMode();
                    String week2 = recordMode.getWeek();
                    myDataBase2.update(sceneBean2, hour2, minute2, mode2, week2, 1, "_id=?", new String[]{recordMode.getId() + ""});
                    TimeActivity.this.getData();
                }
            });
            return view2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.home.activity.main.MainActivity_BLE] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.home.activity.main.MainActivity_DMX02] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.home.activity.main.MainActivity_BLE] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.home.activity.main.MainActivity_DMX02] */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v33 */
    /* JADX WARN: Type inference failed for: r4v9, types: [int] */
    public void sendTime(int i) {
        String str;
        int hour = listDate.get(i).getHour();
        int minute = listDate.get(i).getMinute();
        if (LedBleApplication.getApp().getSceneBean().contains("BLE") || LedBleApplication.getApp().getSceneBean().contains(BluetoothLeServiceSingle.LEDDMX) || LedBleApplication.getApp().getSceneBean().contains("WiFi")) {
            if (listDate.get(i).getWeek() != null) {
                StringBuffer stringBuffer = new StringBuffer(100);
                String[] split = listDate.get(i).getWeek().split(" ");
                for (int i2 = 0; i2 < split.length; i2++) {
                    stringBuffer.append(this.list.get(Integer.parseInt(split[i2])) + " ");
                }
                str = stringBuffer.toString();
            } else {
                str = "";
            }
            boolean contains = str.contains(this.list.get(0));
            if (str.contains(this.list.get(1))) {
                contains |= true;
            }
            boolean z = contains;
            if (str.contains(this.list.get(2))) {
                z = (contains ? 1 : 0) | true;
            }
            boolean z2 = z;
            if (str.contains(this.list.get(3))) {
                z2 = (z ? 1 : 0) | true;
            }
            boolean z3 = z2;
            if (str.contains(this.list.get(4))) {
                z3 = (z2 ? 1 : 0) | true;
            }
            boolean z4 = z3;
            if (str.contains(this.list.get(5))) {
                z4 = (z3 ? 1 : 0) | true;
            }
            ?? r4 = z4;
            if (str.contains(this.list.get(6))) {
                r4 = (z4 ? 1 : 0) | true;
            }
            if (LedBleApplication.getApp().getSceneBean().contains("BLE")) {
                int i3 = this.modeOrder[Integer.parseInt(listDate.get(i).getMode())];
                this.mod = i3;
                ?? r1 = this.mActivity;
                if (r1 != 0) {
                    r1.sendTime(this.parameter, i3, r4, hour, minute, this.dayhour, this.dayminute);
                    return;
                }
                ?? r12 = this.mActivity_dmx02;
                if (r12 != 0) {
                    r12.sendTime(this.parameter, i3, r4, hour, minute, this.dayhour, this.dayminute);
                }
            } else if (LedBleApplication.getApp().getSceneBean().contains(BluetoothLeServiceSingle.LEDDMX) || LedBleApplication.getApp().getSceneBean().contains("WiFi")) {
                int i4 = this.dmx[Integer.parseInt(listDate.get(i).getMode())];
                this.mod = i4;
                ?? r13 = this.mActivity;
                if (r13 != 0) {
                    r13.sendTime(this.parameter, i4, r4, hour, minute, this.dayhour, this.dayminute);
                    return;
                }
                ?? r14 = this.mActivity_dmx02;
                if (r14 != 0) {
                    r14.sendTime(this.parameter, i4, r4, hour, minute, this.dayhour, this.dayminute);
                }
            }
        } else if (LedBleApplication.getApp().getSceneBean().contains("STAGE") || LedBleApplication.getApp().getSceneBean().contains("LIGHT")) {
            this.mod = this.light[Integer.parseInt(listDate.get(i).getMode())];
            int i5 = 0;
            for (int i6 = 0; i6 < listDate.size(); i6++) {
                if (listDate.get(i6).getType() == 1) {
                    i5++;
                }
            }
            MainActivity_BLE mainActivity_BLE = this.mActivity;
            if (mainActivity_BLE != null) {
                mainActivity_BLE.sendStageTime(i5, this.dayhour, this.dayminute, hour, minute, this.mod);
                return;
            }
            MainActivity_DMX02 mainActivity_DMX02 = this.mActivity_dmx02;
            if (mainActivity_DMX02 != null) {
                mainActivity_DMX02.sendStageTime(i5, this.dayhour, this.dayminute, hour, minute, this.mod);
            }
        }
    }
}
