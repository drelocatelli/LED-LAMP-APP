package com.home.activity.set.smart;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import com.common.uitl.NumberHelper;
import com.common.uitl.SharePersistent;
import com.home.base.LedBleActivity;
import com.home.constant.CommonConstant;
import com.home.net.NetConnectBle;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class TimingQueryActivity extends LedBleActivity {
    private static final String TAG = "TimingQueryActivity";
    TextView bt_19;
    TextView bt_29;
    TextView bt_39;
    TextView bt_49;
    TextView bt_59;
    TextView bt_69;
    TextView bt_79;
    private Dialog lDialog;
    ListView list_item5;
    MyAdapterTime1 myAdapterTime1;
    Runnable runnable;
    private RelativeLayout viewBlue;
    private RelativeLayout viewCyan;
    private RelativeLayout viewGreen;
    private RelativeLayout viewPurple;
    private RelativeLayout viewRed;
    private RelativeLayout viewWhite;
    int show = 0;
    List<String[]> listDate = new ArrayList();
    List<String> listweek = new ArrayList();

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        setActive();
    }

    public void initView() {
        setContentView(R.layout.activity_timing_query);
        this.bt_19 = (TextView) findViewById(R.id.bt_19);
        this.bt_29 = (TextView) findViewById(R.id.bt_29);
        this.bt_39 = (TextView) findViewById(R.id.bt_39);
        this.bt_49 = (TextView) findViewById(R.id.bt_49);
        this.bt_59 = (TextView) findViewById(R.id.bt_59);
        this.bt_69 = (TextView) findViewById(R.id.bt_69);
        this.bt_79 = (TextView) findViewById(R.id.bt_79);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.activity.set.smart.TimingQueryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.buttonCancell) {
                    TimingQueryActivity.this.finish();
                } else if (id == R.id.tvQuery) {
                    TimingQueryActivity.this.showCustomMessage();
                    TimingQueryActivity.this.listweek.clear();
                    TimingQueryActivity.this.bt_19.setTextColor(TimingQueryActivity.this.getResources().getColor(R.color.black));
                    TimingQueryActivity.this.bt_19.setBackgroundColor(TimingQueryActivity.this.getResources().getColor(R.color.white));
                    TimingQueryActivity.this.bt_29.setTextColor(TimingQueryActivity.this.getResources().getColor(R.color.black));
                    TimingQueryActivity.this.bt_29.setBackgroundColor(TimingQueryActivity.this.getResources().getColor(R.color.white));
                    TimingQueryActivity.this.bt_39.setTextColor(TimingQueryActivity.this.getResources().getColor(R.color.black));
                    TimingQueryActivity.this.bt_39.setBackgroundColor(TimingQueryActivity.this.getResources().getColor(R.color.white));
                    TimingQueryActivity.this.bt_49.setTextColor(TimingQueryActivity.this.getResources().getColor(R.color.black));
                    TimingQueryActivity.this.bt_49.setBackgroundColor(TimingQueryActivity.this.getResources().getColor(R.color.white));
                    TimingQueryActivity.this.bt_59.setTextColor(TimingQueryActivity.this.getResources().getColor(R.color.black));
                    TimingQueryActivity.this.bt_59.setBackgroundColor(TimingQueryActivity.this.getResources().getColor(R.color.white));
                    TimingQueryActivity.this.bt_69.setTextColor(TimingQueryActivity.this.getResources().getColor(R.color.black));
                    TimingQueryActivity.this.bt_69.setBackgroundColor(TimingQueryActivity.this.getResources().getColor(R.color.white));
                    TimingQueryActivity.this.bt_79.setTextColor(TimingQueryActivity.this.getResources().getColor(R.color.black));
                    TimingQueryActivity.this.bt_79.setBackgroundColor(TimingQueryActivity.this.getResources().getColor(R.color.white));
                    TimingQueryActivity.this.listDate.clear();
                    TimingQueryActivity.this.myAdapterTime1.notifyDataSetChanged();
                    for (int i = 0; i < 4; i++) {
                        NetConnectBle.getInstance().setSmartCheck(1);
                    }
                    TimingQueryActivity.this.timingQuery();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() { // from class: com.home.activity.set.smart.TimingQueryActivity.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TimingQueryActivity.this.lDialog.dismiss();
                            TimingQueryActivity.this.lDialog = null;
                            handler.removeCallbacks(this);
                        }
                    }, 6000L);
                }
            }
        };
        this.viewRed = (RelativeLayout) findViewById(R.id.rl_Red);
        this.viewGreen = (RelativeLayout) findViewById(R.id.rl_Green);
        this.viewBlue = (RelativeLayout) findViewById(R.id.rl_Blue);
        this.viewWhite = (RelativeLayout) findViewById(R.id.rl_White);
        this.viewCyan = (RelativeLayout) findViewById(R.id.rl_Cyan);
        this.viewPurple = (RelativeLayout) findViewById(R.id.rl_Purple);
        findViewById(R.id.buttonCancell).setOnClickListener(onClickListener);
        findViewById(R.id.tvQuery).setOnClickListener(onClickListener);
        this.list_item5 = (ListView) findViewById(R.id.list_item5);
        MyAdapterTime1 myAdapterTime1 = new MyAdapterTime1();
        this.myAdapterTime1 = myAdapterTime1;
        this.list_item5.setAdapter((ListAdapter) myAdapterTime1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCustomMessage() {
        if (this.lDialog == null) {
            Dialog dialog = new Dialog(this, 16973840);
            this.lDialog = dialog;
            dialog.requestWindowFeature(1024);
            this.lDialog.setContentView(R.layout.query_dialogview);
            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setDuration(2000L);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setRepeatMode(1);
            rotateAnimation.setStartTime(-1L);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            ((ImageView) this.lDialog.findViewById(R.id.imageViewWait1)).startAnimation(rotateAnimation);
            this.lDialog.show();
        }
    }

    public void setActive() {
        String smartModeString = SharePersistent.getSmartModeString(getApplicationContext(), CommonConstant.SELECT_MODE_SMART_STRING);
        if (smartModeString.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST) || smartModeString.equalsIgnoreCase("BW")) {
            this.viewRed.setVisibility(8);
            this.viewGreen.setVisibility(8);
            this.viewBlue.setVisibility(8);
            this.viewCyan.setVisibility(8);
            this.viewPurple.setVisibility(8);
            if (smartModeString.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST)) {
                this.viewWhite.setVisibility(0);
            }
            if (smartModeString.equalsIgnoreCase("BW")) {
                this.viewBlue.setVisibility(0);
                this.viewWhite.setVisibility(0);
            }
        } else if (smartModeString.equalsIgnoreCase("RGB")) {
            this.viewWhite.setVisibility(8);
            this.viewCyan.setVisibility(8);
            this.viewPurple.setVisibility(8);
        } else if (smartModeString.equalsIgnoreCase("RGBW")) {
            this.viewCyan.setVisibility(8);
            this.viewPurple.setVisibility(8);
        } else {
            smartModeString.equalsIgnoreCase("RGBWCP");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void timingQuery() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() { // from class: com.home.activity.set.smart.TimingQueryActivity.2
            @Override // java.lang.Runnable
            public void run() {
                String perference = SharePersistent.getPerference(TimingQueryActivity.this.getApplicationContext(), "TimingQueryActivity");
                Log.e("TimingQueryActivity", "reveive data == " + perference);
                if (perference.length() <= 0) {
                    return;
                }
                try {
                    String[] split = perference.split("7B");
                    for (int i = 1; i < split.length; i++) {
                        String[] split2 = split[i].split("[ ]");
                        Log.e("TimingQueryActivity", "value == " + split2);
                        if (!split2.equals("")) {
                            TimingQueryActivity.this.listDate.add(split2);
                            TimingQueryActivity.this.myAdapterTime1.notifyDataSetChanged();
                        }
                    }
                    String[] split3 = split[0].split("[ ]");
                    for (int i2 = 0; i2 < split3.length; i2++) {
                        if (!split3[i2].equalsIgnoreCase("")) {
                            TimingQueryActivity.this.listweek.add(split3[i2]);
                        }
                    }
                    String valueOf = String.valueOf(Integer.valueOf(TimingQueryActivity.this.listweek.get(7), 16));
                    if (!valueOf.equals("")) {
                        TimingQueryActivity.this.getWeeks(valueOf);
                    }
                    SharePersistent.savePerference(TimingQueryActivity.this.getApplicationContext(), "TimingQueryActivity", "");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("======", "run:Exception");
                }
                handler.removeCallbacks(this);
            }
        };
        this.runnable = runnable;
        handler.postDelayed(runnable, 4000L);
    }

    public void getWeeks(String str) {
        int parseInt = Integer.parseInt(str.trim());
        if ((parseInt & 64) != 64) {
            this.bt_19.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if ((parseInt & 1) != 1) {
            this.bt_29.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if ((parseInt & 2) != 2) {
            this.bt_39.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if ((parseInt & 4) != 4) {
            this.bt_49.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if ((parseInt & 8) != 8) {
            this.bt_59.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if ((parseInt & 16) != 16) {
            this.bt_69.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if ((parseInt & 32) != 32) {
            this.bt_79.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    private void putDataback() {
        setResult(-1, new Intent());
        finish();
    }

    /* loaded from: classes.dex */
    class MyHolder {
        TextView tv_1;
        TextView tv_2;
        TextView tv_3;
        TextView tv_4;
        TextView tv_5;
        TextView tv_6;
        TextView tv_7;
        TextView tv_8;

        MyHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class MyAdapterTime1 extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        MyAdapterTime1() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return TimingQueryActivity.this.listDate.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            MyHolder myHolder;
            String[] strArr = TimingQueryActivity.this.listDate.get(i);
            if (view == null) {
                myHolder = new MyHolder();
                view2 = LayoutInflater.from(TimingQueryActivity.this).inflate(R.layout.item, (ViewGroup) null);
                myHolder.tv_1 = (TextView) view2.findViewById(R.id.tv1);
                myHolder.tv_2 = (TextView) view2.findViewById(R.id.tv2);
                myHolder.tv_3 = (TextView) view2.findViewById(R.id.tv3);
                myHolder.tv_4 = (TextView) view2.findViewById(R.id.tv4);
                myHolder.tv_5 = (TextView) view2.findViewById(R.id.tv5);
                myHolder.tv_6 = (TextView) view2.findViewById(R.id.tv6);
                myHolder.tv_7 = (TextView) view2.findViewById(R.id.tv7);
                myHolder.tv_8 = (TextView) view2.findViewById(R.id.tv8);
                view2.setTag(myHolder);
            } else {
                view2 = view;
                myHolder = (MyHolder) view.getTag();
            }
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (!strArr[i2].equalsIgnoreCase("")) {
                    arrayList.add(strArr[i2]);
                }
            }
            TextView textView = myHolder.tv_1;
            textView.setText((i + 1) + " ");
            TextView textView2 = myHolder.tv_2;
            textView2.setText(NumberHelper.LeftPad_Tow_Zero(Integer.valueOf((String) arrayList.get(0), 16).intValue()) + ":" + NumberHelper.LeftPad_Tow_Zero(Integer.valueOf((String) arrayList.get(1), 16).intValue()));
            String smartModeString = SharePersistent.getSmartModeString(TimingQueryActivity.this.getApplicationContext(), CommonConstant.SELECT_MODE_SMART_STRING);
            if (smartModeString.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST) || smartModeString.equalsIgnoreCase("BW")) {
                myHolder.tv_3.setVisibility(8);
                myHolder.tv_4.setVisibility(8);
                myHolder.tv_7.setVisibility(8);
                myHolder.tv_8.setVisibility(8);
                if (smartModeString.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST)) {
                    myHolder.tv_5.setVisibility(8);
                    myHolder.tv_6.setVisibility(0);
                    myHolder.tv_6.setText(String.valueOf(Integer.valueOf((String) arrayList.get(5), 16)));
                } else if (smartModeString.equalsIgnoreCase("BW")) {
                    myHolder.tv_5.setVisibility(0);
                    myHolder.tv_6.setVisibility(0);
                    myHolder.tv_5.setText(String.valueOf(Integer.valueOf((String) arrayList.get(4), 16)));
                    myHolder.tv_6.setText(String.valueOf(Integer.valueOf((String) arrayList.get(5), 16)));
                }
            } else {
                myHolder.tv_3.setVisibility(0);
                myHolder.tv_4.setVisibility(0);
                myHolder.tv_5.setVisibility(0);
                myHolder.tv_3.setText(String.valueOf(Integer.valueOf((String) arrayList.get(2), 16)));
                myHolder.tv_4.setText(String.valueOf(Integer.valueOf((String) arrayList.get(3), 16)));
                myHolder.tv_5.setText(String.valueOf(Integer.valueOf((String) arrayList.get(4), 16)));
                if (smartModeString.equalsIgnoreCase("RGB")) {
                    myHolder.tv_6.setVisibility(8);
                    myHolder.tv_7.setVisibility(8);
                    myHolder.tv_8.setVisibility(8);
                } else if (smartModeString.equalsIgnoreCase("RGBW")) {
                    myHolder.tv_6.setVisibility(0);
                    myHolder.tv_7.setVisibility(8);
                    myHolder.tv_8.setVisibility(8);
                    myHolder.tv_6.setText(NumberHelper.LeftPad_Tow_Zero(Integer.valueOf((String) arrayList.get(5), 16).intValue()));
                } else if (smartModeString.equalsIgnoreCase("RGBWCP")) {
                    myHolder.tv_6.setVisibility(0);
                    myHolder.tv_7.setVisibility(0);
                    myHolder.tv_8.setVisibility(0);
                    myHolder.tv_6.setText(NumberHelper.LeftPad_Tow_Zero(Integer.valueOf((String) arrayList.get(5), 16).intValue()));
                    myHolder.tv_7.setText(NumberHelper.LeftPad_Tow_Zero(Integer.valueOf((String) arrayList.get(6), 16).intValue()));
                    myHolder.tv_8.setText(NumberHelper.LeftPad_Tow_Zero(Integer.valueOf((String) arrayList.get(7), 16).intValue()));
                }
            }
            return view2;
        }
    }
}
