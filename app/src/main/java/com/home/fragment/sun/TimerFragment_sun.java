package com.home.fragment.sun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import butterknife.BindView;
import com.common.net.NetResult;
import com.home.activity.main.MainActivity_BLE;
import com.home.activity.set.timer.ChioceTimeActivity;
import com.home.activity.set.timer.sql.MyDataBase;
import com.home.activity.set.timer.sql.RecordMode;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.ledlamp.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class TimerFragment_sun extends LedBleFragment {
    private static final String TAG = "TimerFragment_sun";
    public static List<RecordMode> listDate_sun = new ArrayList();
    public static String[] modeble;
    @BindView(R.id.closeTime_sun)
    Button closeTime_sun;
    Date date;
    int day;
    int dayhour;
    int dayminute;
    @BindView(R.id.gv_sun)
    GridView gv_sun;
    private int k;
    List<String> list;
    private MainActivity_BLE mActivity;
    private MyAdapter myAdapter;
    MyDataBase myDataBaseSUN;
    int parameter;
    int[] sun = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    @BindView(R.id.sure_sun)
    Button sure_sun;
    String[] week;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_timer, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getData() {
        listDate_sun.clear();
        listDate_sun.addAll(this.myDataBaseSUN.query(LedBleApplication.getApp().getSceneBean()));
        Collections.sort(listDate_sun);
        this.myAdapter.notifyDataSetChanged();
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        this.mActivity = (MainActivity_BLE) getActivity();
        this.week = getResources().getStringArray(R.array.week);
        modeble = getResources().getStringArray(R.array.modelsun);
        this.list = Arrays.asList(this.week);
        this.myDataBaseSUN = MyDataBase.getInstance(this.mActivity);
        MyAdapter myAdapter = new MyAdapter();
        this.myAdapter = myAdapter;
        this.gv_sun.setAdapter((ListAdapter) myAdapter);
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        this.closeTime_sun.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.TimerFragment_sun.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(TimerFragment_sun.this.getActivity(), R.anim.layout_scale));
                TimerFragment_sun.this.mActivity.closeTime();
            }
        });
        this.gv_sun.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.fragment.sun.TimerFragment_sun.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(TimerFragment_sun.this.mActivity, ChioceTimeActivity.class);
                intent.putExtra("key", TimerFragment_sun.listDate_sun.get(i));
                TimerFragment_sun.this.startActivity(intent);
            }
        });
        this.gv_sun.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.home.fragment.sun.TimerFragment_sun.3
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long j) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimerFragment_sun.this.mActivity);
                builder.setTitle(R.string.tips);
                builder.setMessage(R.string.timer);
                builder.setPositiveButton(R.string.bind_end, new DialogInterface.OnClickListener() { // from class: com.home.fragment.sun.TimerFragment_sun.3.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        MyDataBase myDataBase = TimerFragment_sun.this.myDataBaseSUN;
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        myDataBase.delete(sceneBean, "_id", new String[]{TimerFragment_sun.listDate_sun.get(i).getId() + ""});
                        TimerFragment_sun.listDate_sun.remove(i);
                        TimerFragment_sun.this.myAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancell_dialog, (DialogInterface.OnClickListener) null).show();
                return true;
            }
        });
        this.sure_sun.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.TimerFragment_sun.4
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r3v40, types: [com.home.activity.main.MainActivity_BLE] */
            /* JADX WARN: Type inference failed for: r6v4 */
            /* JADX WARN: Type inference failed for: r6v5, types: [int] */
            /* JADX WARN: Type inference failed for: r6v6 */
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String str;
                view.startAnimation(AnimationUtils.loadAnimation(TimerFragment_sun.this.getActivity(), R.anim.layout_scale));
                int i = 0;
                for (int i2 = 0; i2 < TimerFragment_sun.listDate_sun.size(); i2++) {
                    if (TimerFragment_sun.listDate_sun.get(i2).getType() == 1) {
                        int i3 = i + 1;
                        RecordMode recordMode = TimerFragment_sun.listDate_sun.get(i2);
                        int i4 = TimerFragment_sun.this.sun[Integer.parseInt(TimerFragment_sun.listDate_sun.get(i2).getMode())];
                        if (TimerFragment_sun.listDate_sun.get(i2).getWeek() != null) {
                            StringBuffer stringBuffer = new StringBuffer(100);
                            String[] split = TimerFragment_sun.listDate_sun.get(i2).getWeek().split(" ");
                            for (int i5 = 0; i5 < split.length; i5++) {
                                stringBuffer.append(TimerFragment_sun.this.list.get(Integer.parseInt(split[i5])) + " ");
                            }
                            str = stringBuffer.toString();
                        } else {
                            str = "";
                        }
                        boolean contains = str.contains(TimerFragment_sun.this.list.get(0));
                        if (str.contains(TimerFragment_sun.this.list.get(1))) {
                            contains |= true;
                        }
                        if (str.contains(TimerFragment_sun.this.list.get(2))) {
                            contains |= true;
                        }
                        if (str.contains(TimerFragment_sun.this.list.get(3))) {
                            contains |= true;
                        }
                        if (str.contains(TimerFragment_sun.this.list.get(4))) {
                            contains |= true;
                        }
                        if (str.contains(TimerFragment_sun.this.list.get(5))) {
                            contains |= true;
                        }
                        TimerFragment_sun.this.mActivity.sendSun(i2, i4, str.contains(TimerFragment_sun.this.list.get(6)) ? contains | true : contains, recordMode.getHour(), recordMode.getMinute());
                        try {
                            Thread.sleep(150L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i = i3;
                    }
                }
                TimerFragment_sun.this.date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
                TimerFragment_sun timerFragment_sun = TimerFragment_sun.this;
                timerFragment_sun.dayhour = Integer.parseInt(simpleDateFormat.format(timerFragment_sun.date).trim());
                TimerFragment_sun timerFragment_sun2 = TimerFragment_sun.this;
                timerFragment_sun2.dayminute = Integer.parseInt(simpleDateFormat2.format(timerFragment_sun2.date).trim());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String valueOf = String.valueOf(calendar.get(7));
                if ("1".equals(valueOf)) {
                    TimerFragment_sun.this.day = 7;
                } else if (ExifInterface.GPS_MEASUREMENT_2D.equals(valueOf)) {
                    TimerFragment_sun.this.day = 1;
                } else if (ExifInterface.GPS_MEASUREMENT_3D.equals(valueOf)) {
                    TimerFragment_sun.this.day = 2;
                } else if ("4".equals(valueOf)) {
                    TimerFragment_sun.this.day = 3;
                } else if ("5".equals(valueOf)) {
                    TimerFragment_sun.this.day = 4;
                } else if ("6".equals(valueOf)) {
                    TimerFragment_sun.this.day = 5;
                } else if ("7".equals(valueOf)) {
                    TimerFragment_sun.this.day = 6;
                }
                TimerFragment_sun.this.mActivity.timeSun(i, TimerFragment_sun.this.day, TimerFragment_sun.this.dayhour, TimerFragment_sun.this.dayminute);
            }
        });
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
            return TimerFragment_sun.listDate_sun.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            final MyHolder myHolder;
            final RecordMode recordMode = TimerFragment_sun.listDate_sun.get(i);
            if (view == null) {
                myHolder = new MyHolder();
                view2 = LayoutInflater.from(TimerFragment_sun.this.mActivity).inflate(R.layout.gv, (ViewGroup) null);
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
                myHolder.tv_mode.setText(TimerFragment_sun.modeble[Integer.parseInt(recordMode.getMode())]);
            }
            if (recordMode.getWeek() != null) {
                StringBuffer stringBuffer = new StringBuffer(100);
                String[] split = recordMode.getWeek().split(" ");
                for (int i2 = 0; i2 < split.length; i2++) {
                    stringBuffer.append(TimerFragment_sun.this.list.get(Integer.parseInt(split[i2])) + " ");
                }
                myHolder.tv_week.setText(stringBuffer);
            }
            myHolder.imageView.setImageResource(recordMode.getType() == 0 ? R.drawable.butn_close : R.drawable.butn_open);
            myHolder.imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.TimerFragment_sun.MyAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (recordMode.getType() != 0) {
                        myHolder.imageView.setImageResource(R.drawable.butn_close);
                        MyDataBase myDataBase = MyDataBase.getInstance(TimerFragment_sun.this.mActivity);
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        int hour = recordMode.getHour();
                        int minute = recordMode.getMinute();
                        String mode = recordMode.getMode();
                        String week = recordMode.getWeek();
                        myDataBase.update(sceneBean, hour, minute, mode, week, 0, "_id=?", new String[]{recordMode.getId() + ""});
                        TimerFragment_sun.this.getData();
                        return;
                    }
                    myHolder.imageView.setImageResource(R.drawable.butn_open);
                    MyDataBase myDataBase2 = MyDataBase.getInstance(TimerFragment_sun.this.mActivity);
                    String sceneBean2 = LedBleApplication.getApp().getSceneBean();
                    int hour2 = recordMode.getHour();
                    int minute2 = recordMode.getMinute();
                    String mode2 = recordMode.getMode();
                    String week2 = recordMode.getWeek();
                    myDataBase2.update(sceneBean2, hour2, minute2, mode2, week2, 1, "_id=?", new String[]{recordMode.getId() + ""});
                    TimerFragment_sun.this.getData();
                }
            });
            return view2;
        }
    }
}
