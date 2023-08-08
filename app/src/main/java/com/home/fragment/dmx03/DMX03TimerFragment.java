package com.home.fragment.dmx03;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import com.common.net.NetResult;
import com.home.activity.main.MainActivity_DMX03;
import com.home.activity.set.timer.ChioceTimeActivity;
import com.home.activity.set.timer.DMX03ChioceTimeActivity;
import com.home.activity.set.timer.sql.MyDataBase;
import com.home.activity.set.timer.sql.RecordMode;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.view.SelectMultiCheckGroup;
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
public class DMX03TimerFragment extends LedBleFragment {
    private static final String TAG = "TimerFragment";
    public static List<RecordMode> listDate = new ArrayList();
    public static String[] modeble;
    private Date date;
    int day;
    int dayhour;
    int dayminute;
    private GridView gridView;
    List<String> list;
    private MainActivity_DMX03 mActivity;
    private View mContentView;
    int mod;
    private MyAdapter myAdapter;
    private MyDataBase myDataBaseDMX;
    int parameter;
    String[] week = {NetResult.CODE_OK, "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5", "6"};
    int[] dmx = {0, 254, 255, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, SubsamplingScaleImageView.ORIENTATION_180, 181, 182, 183, 184, 185, 186, 187, PictureConfig.CHOOSE_REQUEST, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, HttpStatus.SC_CREATED, HttpStatus.SC_ACCEPTED, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, HttpStatus.SC_NO_CONTENT, HttpStatus.SC_RESET_CONTENT, HttpStatus.SC_PARTIAL_CONTENT, HttpStatus.SC_MULTI_STATUS, 208, 209, 210};
    private int idx = 0;
    private int size = 0;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    static /* synthetic */ int access$208(DMX03TimerFragment dMX03TimerFragment) {
        int i = dMX03TimerFragment.size;
        dMX03TimerFragment.size = i + 1;
        return i;
    }

    static /* synthetic */ int access$308(DMX03TimerFragment dMX03TimerFragment) {
        int i = dMX03TimerFragment.idx;
        dMX03TimerFragment.idx = i + 1;
        return i;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dmx03_fragment_time, viewGroup, false);
        this.mContentView = inflate;
        return inflate;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        if (MyDataBase.getInstance(getContext()).query(LedBleApplication.getApp().getSceneBean()).size() == 0) {
            String[] strArr = {getString(R.string.Turn_On), getString(R.string.Turn_Off)};
            for (int i = 0; i < 2; i++) {
                MyDataBase.getInstance(getContext()).insert(LedBleApplication.getApp().getSceneBean(), 0, 0, strArr[i], null, 1);
            }
        }
        this.mActivity = (MainActivity_DMX03) getActivity();
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        initViews();
    }

    public void initViews() {
        MainActivity_DMX03.getMainActivity().getBtnTimerAdd().setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX03TimerFragment.listDate.size() < 16) {
                    DMX03TimerFragment.this.startActivity(new Intent(DMX03TimerFragment.this.getContext(), ChioceTimeActivity.class));
                    return;
                }
                Toast.makeText(DMX03TimerFragment.this.getContext(), (int) R.string.supported, 1).show();
            }
        });
        this.list = Arrays.asList(this.week);
        modeble = new String[2];
        if (MainActivity_DMX03.getSceneBean() != null) {
            this.mActivity = MainActivity_DMX03.getMainActivity();
            if (MainActivity_DMX03.getSceneBean().contains("LEDDMX-03-")) {
                for (int i = 0; i < 2; i++) {
                    modeble[i] = getResources().getStringArray(R.array.timer_model_dmx)[i];
                }
            }
        }
        init();
    }

    public void init() {
        this.myDataBaseDMX = MyDataBase.getInstance(getContext());
        this.myAdapter = new MyAdapter();
        GridView gridView = (GridView) this.mContentView.findViewById(R.id.gv);
        this.gridView = gridView;
        gridView.setAdapter((ListAdapter) this.myAdapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(DMX03TimerFragment.this.getContext(), ChioceTimeActivity.class);
                intent.putExtra("key", DMX03TimerFragment.listDate.get(i));
                DMX03TimerFragment.this.startActivity(intent);
            }
        });
        this.mContentView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
            }
        });
        this.mContentView.findViewById(R.id.closeTime).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(DMX03TimerFragment.this.getContext(), R.anim.layout_scale));
                if (DMX03TimerFragment.this.mActivity != null) {
                    DMX03TimerFragment.this.mActivity.closeTime();
                }
            }
        });
        this.mContentView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(DMX03TimerFragment.this.getContext(), R.anim.layout_scale));
                long currentTimeMillis = System.currentTimeMillis();
                DMX03TimerFragment.this.date = new Date(currentTimeMillis);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
                DMX03TimerFragment dMX03TimerFragment = DMX03TimerFragment.this;
                dMX03TimerFragment.dayhour = Integer.parseInt(simpleDateFormat.format(dMX03TimerFragment.date).trim());
                DMX03TimerFragment dMX03TimerFragment2 = DMX03TimerFragment.this;
                dMX03TimerFragment2.dayminute = Integer.parseInt(simpleDateFormat2.format(dMX03TimerFragment2.date).trim());
                DMX03TimerFragment dMX03TimerFragment3 = DMX03TimerFragment.this;
                dMX03TimerFragment3.day = Integer.parseInt(DMX03TimerFragment.getWeekOfDate(dMX03TimerFragment3.date));
                DMX03TimerFragment.this.size = 0;
                for (int i = 0; i < DMX03TimerFragment.listDate.size(); i++) {
                    if (DMX03TimerFragment.listDate.get(i).getType() == 1) {
                        DMX03TimerFragment.access$208(DMX03TimerFragment.this);
                    }
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (DMX03TimerFragment.this.idx >= DMX03TimerFragment.listDate.size()) {
                            DMX03TimerFragment.this.idx = 0;
                            if (DMX03TimerFragment.this.mActivity != null) {
                                DMX03TimerFragment.this.mActivity.endTime(DMX03TimerFragment.this.day, DMX03TimerFragment.this.size, DMX03TimerFragment.this.dayhour, DMX03TimerFragment.this.dayminute);
                            }
                            handler.removeCallbacks(this);
                            return;
                        }
                        if (DMX03TimerFragment.listDate.get(DMX03TimerFragment.this.idx).getType() == 1) {
                            DMX03TimerFragment.this.parameter = (DMX03TimerFragment.this.day << 4) | DMX03TimerFragment.this.idx;
                            DMX03TimerFragment.this.sendTime(DMX03TimerFragment.this.idx);
                        }
                        DMX03TimerFragment.access$308(DMX03TimerFragment.this);
                        handler.postDelayed(this, 300L);
                    }
                }, 300L);
            }
        });
        this.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.6
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long j) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DMX03TimerFragment.this.getContext());
                builder.setTitle(R.string.tips);
                builder.setMessage(R.string.timer);
                builder.setPositiveButton(R.string.bind_end, new DialogInterface.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.6.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        MyDataBase myDataBase = DMX03TimerFragment.this.myDataBaseDMX;
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        myDataBase.delete(sceneBean, "_id", new String[]{DMX03TimerFragment.listDate.get(i).getId() + ""});
                        DMX03TimerFragment.listDate.remove(i);
                        DMX03TimerFragment.this.myAdapter.notifyDataSetChanged();
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

    public void getDataDmx() {
        listDate.clear();
        listDate.addAll(this.myDataBaseDMX.query(LedBleApplication.getApp().getSceneBean()));
        this.myAdapter.notifyDataSetChanged();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getDataDmx();
    }

    /* loaded from: classes.dex */
    class MyHolder {
        ImageView imageView;
        RelativeLayout rlTime;
        SelectMultiCheckGroup selectMultiCheckGroup;
        TextView tvMode;
        TextView tvTimeValue;

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
            return DMX03TimerFragment.listDate.size();
        }

        @Override // android.widget.Adapter
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View view2;
            final MyHolder myHolder;
            if (view == null) {
                myHolder = new MyHolder();
                view2 = LayoutInflater.from(DMX03TimerFragment.this.getContext()).inflate(R.layout.dmx03_layout_timer, (ViewGroup) null);
                myHolder.tvMode = (TextView) view2.findViewById(R.id.tvMode);
                myHolder.imageView = (ImageView) view2.findViewById(R.id.image);
                myHolder.tvTimeValue = (TextView) view2.findViewById(R.id.tvTimeValue);
                myHolder.rlTime = (RelativeLayout) view2.findViewById(R.id.rlTime);
                myHolder.rlTime.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.MyAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        view3.startAnimation(AnimationUtils.loadAnimation(DMX03TimerFragment.this.getContext(), R.anim.layout_scale));
                        Intent intent = new Intent(DMX03TimerFragment.this.getContext(), DMX03ChioceTimeActivity.class);
                        intent.putExtra("key", DMX03TimerFragment.listDate.get(i));
                        DMX03TimerFragment.this.startActivity(intent);
                    }
                });
                myHolder.selectMultiCheckGroup = (SelectMultiCheckGroup) view2.findViewById(R.id.checkGroup);
                myHolder.selectMultiCheckGroup.initData(Arrays.asList(DMX03TimerFragment.this.getResources().getStringArray(R.array.week)));
                if (DMX03TimerFragment.listDate.get(i).getWeek() != null) {
                    String[] split = DMX03TimerFragment.listDate.get(i).getWeek().split(" ");
                    for (int i2 = 0; i2 < split.length; i2++) {
                        if (!split[i2].isEmpty()) {
                            myHolder.selectMultiCheckGroup.setSeleted(Integer.parseInt(split[i2]));
                        }
                    }
                }
                myHolder.selectMultiCheckGroup.setOnItemSelectedListener(new SelectMultiCheckGroup.OnItemSelectedListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.MyAdapter.2
                    @Override // com.home.view.SelectMultiCheckGroup.OnItemSelectedListener
                    public void checked(View view3, int i3, boolean z) {
                        List<Integer> selectedAll = myHolder.selectMultiCheckGroup.getSelectedAll();
                        Collections.sort(selectedAll);
                        StringBuffer stringBuffer = new StringBuffer();
                        String str = null;
                        for (int i4 = 0; i4 < selectedAll.size(); i4++) {
                            stringBuffer.append(selectedAll.get(i4));
                            stringBuffer.append(" ");
                            str = stringBuffer.toString();
                        }
                        MyDataBase.getInstance(DMX03TimerFragment.this.getContext()).update(LedBleApplication.getApp().getSceneBean(), DMX03TimerFragment.listDate.get(i).getHour(), DMX03TimerFragment.listDate.get(i).getMinute(), DMX03TimerFragment.listDate.get(i).getMode(), str, DMX03TimerFragment.listDate.get(i).getType(), "_id=?", new String[]{DMX03TimerFragment.listDate.get(i).getId() + ""});
                        DMX03TimerFragment.this.getDataDmx();
                    }
                });
                view2.setTag(myHolder);
            } else {
                view2 = view;
                myHolder = (MyHolder) view.getTag();
            }
            if (DMX03TimerFragment.listDate.get(i).getMinute() < 10 && DMX03TimerFragment.listDate.get(i).getHour() < 10) {
                TextView textView = myHolder.tvTimeValue;
                textView.setText(NetResult.CODE_OK + DMX03TimerFragment.listDate.get(i).getHour() + ":0" + DMX03TimerFragment.listDate.get(i).getMinute());
            } else if (DMX03TimerFragment.listDate.get(i).getHour() < 10) {
                TextView textView2 = myHolder.tvTimeValue;
                textView2.setText(NetResult.CODE_OK + DMX03TimerFragment.listDate.get(i).getHour() + ":" + DMX03TimerFragment.listDate.get(i).getMinute());
            } else if (DMX03TimerFragment.listDate.get(i).getMinute() < 10) {
                TextView textView3 = myHolder.tvTimeValue;
                textView3.setText(DMX03TimerFragment.listDate.get(i).getHour() + ":0" + DMX03TimerFragment.listDate.get(i).getMinute());
            } else {
                TextView textView4 = myHolder.tvTimeValue;
                textView4.setText(DMX03TimerFragment.listDate.get(i).getHour() + ":" + DMX03TimerFragment.listDate.get(i).getMinute());
            }
            if (DMX03TimerFragment.listDate.get(i).getId() == 1) {
                myHolder.tvMode.setText(DMX03TimerFragment.this.getString(R.string.Turn_On));
            } else {
                myHolder.tvMode.setText(DMX03TimerFragment.this.getString(R.string.Turn_Off));
            }
            myHolder.imageView.setImageResource(DMX03TimerFragment.listDate.get(i).getType() == 0 ? R.drawable.butn_close : R.drawable.butn_open);
            myHolder.imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03TimerFragment.MyAdapter.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (DMX03TimerFragment.listDate.get(i).getType() != 0) {
                        myHolder.imageView.setImageResource(R.drawable.butn_close);
                        MyDataBase myDataBase = MyDataBase.getInstance(DMX03TimerFragment.this.getContext());
                        String sceneBean = LedBleApplication.getApp().getSceneBean();
                        int hour = DMX03TimerFragment.listDate.get(i).getHour();
                        int minute = DMX03TimerFragment.listDate.get(i).getMinute();
                        String mode = DMX03TimerFragment.listDate.get(i).getMode();
                        String week = DMX03TimerFragment.listDate.get(i).getWeek();
                        myDataBase.update(sceneBean, hour, minute, mode, week, 0, "_id=?", new String[]{DMX03TimerFragment.listDate.get(i).getId() + ""});
                        DMX03TimerFragment.this.getDataDmx();
                        return;
                    }
                    myHolder.imageView.setImageResource(R.drawable.butn_open);
                    MyDataBase myDataBase2 = MyDataBase.getInstance(DMX03TimerFragment.this.getContext());
                    String sceneBean2 = LedBleApplication.getApp().getSceneBean();
                    int hour2 = DMX03TimerFragment.listDate.get(i).getHour();
                    int minute2 = DMX03TimerFragment.listDate.get(i).getMinute();
                    String mode2 = DMX03TimerFragment.listDate.get(i).getMode();
                    String week2 = DMX03TimerFragment.listDate.get(i).getWeek();
                    myDataBase2.update(sceneBean2, hour2, minute2, mode2, week2, 1, "_id=?", new String[]{DMX03TimerFragment.listDate.get(i).getId() + ""});
                    DMX03TimerFragment.this.getDataDmx();
                }
            });
            return view2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.home.activity.main.MainActivity_DMX03] */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1, types: [int] */
    /* JADX WARN: Type inference failed for: r4v25 */
    /* JADX WARN: Type inference failed for: r4v26 */
    public void sendTime(int i) {
        ?? r4;
        int hour = listDate.get(i).getHour();
        int minute = listDate.get(i).getMinute();
        String week = listDate.get(i).getWeek();
        int i2 = 0;
        if (week != null) {
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
            r4 = week.contains(this.list.get(6)) ? contains | true : contains;
        } else {
            r4 = 0;
        }
        while (true) {
            if (i2 > listDate.size() - 1) {
                break;
            } else if (listDate.get(i2).getMode().contains(modeble[(listDate.size() - 1) - i])) {
                this.mod = this.dmx[(listDate.size() - 1) - i];
                break;
            } else {
                i2++;
            }
        }
        ?? r1 = this.mActivity;
        if (r1 != 0) {
            r1.sendTime(this.parameter, this.mod, r4, hour, minute, this.dayhour, this.dayminute);
        }
    }
}
