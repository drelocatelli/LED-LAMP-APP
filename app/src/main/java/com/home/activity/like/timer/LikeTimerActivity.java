package com.home.activity.like.timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import com.common.net.NetResult;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.main.MainActivity_LIKE;
import com.home.activity.set.timer.ChioceTimeActivityLIKE;
import com.home.activity.set.timer.sql.MyDataBase;
import com.home.activity.set.timer.sql.RecordMode;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.ledlamp.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/* loaded from: classes.dex */
public class LikeTimerActivity extends LedBleActivity {
    private static final String TAG = "LikeTimerActivity";
    public static List<RecordMode> listDate = new ArrayList();
    public static String[] modeble;
    Date date;
    int day;
    int dayhour;
    int dayminute;
    GridView gridView;
    List<String> list;
    private MainActivity_LIKE mActivity;
    int mod;
    MyAdapter myAdapter;
    MyDataBase myDataBase;
    int parameter;
    String[] week;
    int[] modeOrder = {0, 1, 2, 3, 4, 5, 6, 7};
    private int idx = 0;
    private int size = 0;

    static /* synthetic */ int access$108(LikeTimerActivity likeTimerActivity) {
        int i = likeTimerActivity.size;
        likeTimerActivity.size = i + 1;
        return i;
    }

    static /* synthetic */ int access$208(LikeTimerActivity likeTimerActivity) {
        int i = likeTimerActivity.idx;
        likeTimerActivity.idx = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        super.onCreate(bundle);
        setContentView(R.layout.activity_timer_like);
        getIntent().getStringExtra("name");
        String[] stringArray = getResources().getStringArray(R.array.week);
        this.week = stringArray;
        this.list = Arrays.asList(stringArray);
        this.mActivity = MainActivity_LIKE.getMainActivity();
        modeble = getResources().getStringArray(R.array.modelike);
        init();
    }

    public void init() {
        this.myDataBase = MyDataBase.getInstance(this);
        this.myAdapter = new MyAdapter();
        GridView gridView = (GridView) findViewById(R.id.gv);
        this.gridView = gridView;
        gridView.setAdapter((ListAdapter) this.myAdapter);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LikeTimerActivity.this.finish();
            }
        });
        findViewById(R.id.closeTime).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(LikeTimerActivity.this, R.anim.layout_scale));
                LikeTimerActivity.this.mActivity.closeTime();
            }
        });
        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(LikeTimerActivity.this, R.anim.layout_scale));
                long currentTimeMillis = System.currentTimeMillis();
                LikeTimerActivity.this.date = new Date(currentTimeMillis);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
                LikeTimerActivity likeTimerActivity = LikeTimerActivity.this;
                likeTimerActivity.dayhour = Integer.parseInt(simpleDateFormat.format(likeTimerActivity.date).trim());
                LikeTimerActivity likeTimerActivity2 = LikeTimerActivity.this;
                likeTimerActivity2.dayminute = Integer.parseInt(simpleDateFormat2.format(likeTimerActivity2.date).trim());
                LikeTimerActivity likeTimerActivity3 = LikeTimerActivity.this;
                likeTimerActivity3.day = Integer.parseInt(LikeTimerActivity.getWeekOfDate(likeTimerActivity3.date));
                LikeTimerActivity.this.size = 0;
                for (int i = 0; i < LikeTimerActivity.listDate.size(); i++) {
                    if (LikeTimerActivity.listDate.get(i).getType() == 1) {
                        LikeTimerActivity.access$108(LikeTimerActivity.this);
                    }
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // from class: com.home.activity.like.timer.LikeTimerActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (LikeTimerActivity.this.idx >= LikeTimerActivity.listDate.size()) {
                            LikeTimerActivity.this.idx = 0;
                            LikeTimerActivity.this.mActivity.endTime(LikeTimerActivity.this.day, LikeTimerActivity.this.size, LikeTimerActivity.this.dayhour, LikeTimerActivity.this.dayminute);
                            handler.removeCallbacks(this);
                            return;
                        }
                        if (LikeTimerActivity.listDate.get(LikeTimerActivity.this.idx).getType() == 1) {
                            LikeTimerActivity.this.parameter = (LikeTimerActivity.this.day << 4) | LikeTimerActivity.this.idx;
                            LikeTimerActivity.this.sendTime(LikeTimerActivity.this.idx);
                        }
                        LikeTimerActivity.access$208(LikeTimerActivity.this);
                        handler.postDelayed(this, 300L);
                    }
                }, 300L);
            }
        });
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            }
        });
        this.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.5
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long j) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LikeTimerActivity.this);
                builder.setTitle(R.string.tips);
                builder.setMessage(R.string.timer);
                builder.setPositiveButton(R.string.bind_end, new DialogInterface.OnClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.5.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        MyDataBase myDataBase = LikeTimerActivity.this.myDataBase;
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        myDataBase.delete(sceneBean, "_id", new String[]{LikeTimerActivity.listDate.get(i).getId() + ""});
                        LikeTimerActivity.listDate.remove(i);
                        LikeTimerActivity.this.myAdapter.notifyDataSetChanged();
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

    public void addNullData() {
        listDate.clear();
        RecordMode recordMode = new RecordMode();
        recordMode.setHour(9999);
        recordMode.setMinute(20);
        listDate.add(recordMode);
        Collections.sort(listDate);
        this.myAdapter.notifyDataSetChanged();
    }

    public List<RecordMode> getData() {
        listDate.clear();
        listDate.addAll(this.myDataBase.query(LedBleApplication.getApp().getSceneBean()));
        RecordMode recordMode = new RecordMode();
        recordMode.setHour(9999);
        recordMode.setMinute(20);
        listDate.add(recordMode);
        Collections.sort(listDate);
        this.myAdapter.notifyDataSetChanged();
        return this.myDataBase.query(LedBleApplication.getApp().getSceneBean());
    }

    public void sendTime(int i) {
        int hour = listDate.get(i).getHour();
        int minute = listDate.get(i).getMinute();
        String week = listDate.get(i).getWeek();
        int i2 = 0;
        boolean contains = week.contains(this.list.get(0));
        if (week.contains(this.list.get(1))) {
            contains |= true;
        }
        if (week.contains(this.list.get(2))) {
            contains |= true;
        }
        if (week.contains(this.list.get(3))) {
            contains |= true;
        }
        if (week.contains(this.list.get(4))) {
            contains |= true;
        }
        if (week.contains(this.list.get(5))) {
            contains |= true;
        }
        if (week.contains(this.list.get(6))) {
            contains |= true;
        }
        boolean z = contains;
        while (true) {
            String[] strArr = modeble;
            if (i2 >= strArr.length) {
                break;
            } else if (strArr[i2].equals(listDate.get(i).getMode())) {
                this.mod = this.modeOrder[i2];
                break;
            } else {
                i2++;
            }
        }
        this.mActivity.sendTime(this.idx, this.mod, z ? 1 : 0, hour, minute, this.dayhour, this.dayminute);
        Log.e("---ble---", "parameter；" + this.parameter + "mod:" + this.mod + "time:" + (z ? 1 : 0) + "hour:" + hour + "minute:" + minute + "dayhour:" + this.dayhour + "dayminute:" + this.dayminute);
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (getData().size() > 0) {
            getData();
        } else {
            addNullData();
        }
    }

    /* loaded from: classes.dex */
    class MyHolder {
        ImageView imageView;
        RelativeLayout rlImageViewAdd;
        RelativeLayout rlTimerAll;
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
            return LikeTimerActivity.listDate.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            final MyHolder myHolder;
            final RecordMode recordMode = LikeTimerActivity.listDate.get(i);
            if (view == null) {
                myHolder = new MyHolder();
                view2 = LayoutInflater.from(LikeTimerActivity.this).inflate(R.layout.gv_like, (ViewGroup) null);
                myHolder.imageView = (ImageView) view2.findViewById(R.id.image);
                myHolder.tv_mode = (TextView) view2.findViewById(R.id.mtv);
                myHolder.tv_time = (TextView) view2.findViewById(R.id.ttv);
                myHolder.tv_week = (TextView) view2.findViewById(R.id.wtv);
                view2.setTag(myHolder);
            } else {
                view2 = view;
                myHolder = (MyHolder) view.getTag();
            }
            myHolder.tv_week.setText(recordMode.getWeek());
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
            myHolder.rlTimerAll = (RelativeLayout) view2.findViewById(R.id.rlTimerAll);
            myHolder.rlImageViewAdd = (RelativeLayout) view2.findViewById(R.id.rlImageViewAdd);
            if (recordMode.getHour() == 9999) {
                myHolder.rlTimerAll.setVisibility(4);
                myHolder.rlImageViewAdd.setVisibility(0);
                myHolder.rlImageViewAdd.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.MyAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        if (LikeTimerActivity.listDate.size() < 16) {
                            LikeTimerActivity.this.startActivity(new Intent(LikeTimerActivity.this, ChioceTimeActivityLIKE.class));
                            return;
                        }
                        Toast.makeText(LikeTimerActivity.this, (int) R.string.supported, 1).show();
                    }
                });
            } else {
                myHolder.rlTimerAll.setVisibility(0);
                myHolder.rlImageViewAdd.setVisibility(4);
            }
            myHolder.tv_mode.setText(recordMode.getMode());
            myHolder.imageView.setImageResource(recordMode.getType() == 0 ? R.drawable.butn_close : R.drawable.butn_open);
            myHolder.imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.like.timer.LikeTimerActivity.MyAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (recordMode.getType() != 0) {
                        myHolder.imageView.setImageResource(R.drawable.butn_close);
                        MyDataBase myDataBase = MyDataBase.getInstance(LikeTimerActivity.this);
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        int hour = recordMode.getHour();
                        int minute = recordMode.getMinute();
                        String mode = recordMode.getMode();
                        String week = recordMode.getWeek();
                        myDataBase.update(sceneBean, hour, minute, mode, week, 0, "_id=?", new String[]{recordMode.getId() + ""});
                        LikeTimerActivity.this.getData();
                        return;
                    }
                    myHolder.imageView.setImageResource(R.drawable.butn_open);
                    MyDataBase myDataBase2 = MyDataBase.getInstance(LikeTimerActivity.this);
                    String sceneBean2 = LedBleApplication.getApp().getSceneBean();
                    int hour2 = recordMode.getHour();
                    int minute2 = recordMode.getMinute();
                    String mode2 = recordMode.getMode();
                    String week2 = recordMode.getWeek();
                    myDataBase2.update(sceneBean2, hour2, minute2, mode2, week2, 1, "_id=?", new String[]{recordMode.getId() + ""});
                    LikeTimerActivity.this.getData();
                }
            });
            return view2;
        }
    }
}
