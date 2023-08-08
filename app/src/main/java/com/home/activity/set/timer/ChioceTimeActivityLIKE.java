package com.home.activity.set.timer;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.common.uitl.NumberHelper;
import com.home.activity.set.timer.sql.MyDataBase;
import com.home.activity.set.timer.sql.RecordMode;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.view.SelectMultiCheckGroup;
import com.home.view.wheel.OnWheelChangedListener;
import com.home.view.wheel.WheelModelAdapter;
import com.home.view.wheel.WheelView;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ChioceTimeActivityLIKE extends LedBleActivity implements View.OnClickListener {
    private SelectMultiCheckGroup checkGroupMulti;
    private SelectMultiCheckGroup checkGroupSingle;
    Map<Integer, String> hashMap = new HashMap();
    private int hour;
    String ledmode;
    String ledweek;
    WheelView listHour;
    WheelView listMinute;
    private int minute;
    ImageView no;
    RecordMode recordMode;
    TextView tvmode;
    TextView tvweek;
    WheelModelAdapter wheelAdapterH;
    WheelModelAdapter wheelAdapterM;
    ImageView yes;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_chioce_time_like);
        this.recordMode = (RecordMode) getIntent().getSerializableExtra("key");
        init();
    }

    public void init() {
        this.tvmode = (TextView) findViewById(R.id.modetv);
        this.tvweek = (TextView) findViewById(R.id.weektv);
        RecordMode recordMode = this.recordMode;
        if (recordMode != null) {
            this.tvmode.setText(recordMode.getMode());
            this.tvweek.setText(this.recordMode.getWeek());
        }
        this.yes = (ImageView) findViewById(R.id.yes);
        this.no = (ImageView) findViewById(R.id.no);
        this.yes.setOnClickListener(this);
        this.no.setOnClickListener(this);
        RecordMode recordMode2 = this.recordMode;
        if (recordMode2 != null) {
            this.hour = recordMode2.getHour();
            this.minute = this.recordMode.getMinute();
        } else {
            Time time = new Time();
            time.setToNow();
            this.hour = time.hour;
            this.minute = time.minute;
        }
        this.listHour = (WheelView) findViewById(R.id.listHour);
        this.listMinute = (WheelView) findViewById(R.id.listMinute);
        String[] strArr = new String[24];
        for (int i = 0; i < 24; i++) {
            strArr[i] = NumberHelper.LeftPad_Tow_Zero(i);
        }
        WheelModelAdapter wheelModelAdapter = new WheelModelAdapter(this, strArr);
        this.wheelAdapterH = wheelModelAdapter;
        this.listHour.setViewAdapter(wheelModelAdapter);
        this.listHour.setCurrentItem(this.hour);
        this.listHour.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.timer.ChioceTimeActivityLIKE.1
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i2, int i3) {
                ChioceTimeActivityLIKE.this.hour = i3;
            }
        });
        String[] strArr2 = new String[60];
        for (int i2 = 0; i2 < 60; i2++) {
            strArr2[i2] = NumberHelper.LeftPad_Tow_Zero(i2);
        }
        WheelModelAdapter wheelModelAdapter2 = new WheelModelAdapter(this, strArr2);
        this.wheelAdapterM = wheelModelAdapter2;
        this.listMinute.setViewAdapter(wheelModelAdapter2);
        this.listMinute.setCurrentItem(this.minute);
        this.listMinute.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.timer.ChioceTimeActivityLIKE.2
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                ChioceTimeActivityLIKE.this.minute = i4;
            }
        });
        SelectMultiCheckGroup selectMultiCheckGroup = (SelectMultiCheckGroup) findViewById(R.id.checkGroupSingle);
        this.checkGroupSingle = selectMultiCheckGroup;
        selectMultiCheckGroup.initData(likeModel());
        this.checkGroupSingle.setOnItemSelectedListener(new SelectMultiCheckGroup.OnItemSelectedListener() { // from class: com.home.activity.set.timer.ChioceTimeActivityLIKE.3
            @Override // com.home.view.SelectMultiCheckGroup.OnItemSelectedListener
            public void checked(View view, int i3, boolean z) {
                ChioceTimeActivityLIKE chioceTimeActivityLIKE = ChioceTimeActivityLIKE.this;
                chioceTimeActivityLIKE.ledmode = (String) chioceTimeActivityLIKE.likeModel().get(i3);
            }
        });
        SelectMultiCheckGroup selectMultiCheckGroup2 = (SelectMultiCheckGroup) findViewById(R.id.checkGroupMulti);
        this.checkGroupMulti = selectMultiCheckGroup2;
        selectMultiCheckGroup2.initData(getWeek());
        this.checkGroupMulti.setOnItemSelectedListener(new SelectMultiCheckGroup.OnItemSelectedListener() { // from class: com.home.activity.set.timer.ChioceTimeActivityLIKE.4
            @Override // com.home.view.SelectMultiCheckGroup.OnItemSelectedListener
            public void checked(View view, int i3, boolean z) {
                if (z) {
                    ChioceTimeActivityLIKE.this.hashMap.put(Integer.valueOf(i3), (String) ChioceTimeActivityLIKE.this.getWeek().get(i3));
                } else {
                    ChioceTimeActivityLIKE.this.hashMap.remove(Integer.valueOf(i3));
                }
                StringBuffer stringBuffer = new StringBuffer(100);
                for (Map.Entry<Integer, String> entry : ChioceTimeActivityLIKE.this.hashMap.entrySet()) {
                    String value = entry.getValue();
                    stringBuffer.append(((Object) value) + " ");
                }
                ChioceTimeActivityLIKE.this.ledweek = stringBuffer.toString();
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.no) {
            finish();
        } else if (id != R.id.yes) {
        } else {
            if (this.recordMode != null) {
                MyDataBase myDataBase = MyDataBase.getInstance(this);
                String sceneBean = LedBleApplication.getApp().getSceneBean();
                int i = this.hour;
                int i2 = this.minute;
                String str = this.ledmode;
                String str2 = this.ledweek;
                myDataBase.update(sceneBean, i, i2, str, str2, 1, "_id=?", new String[]{this.recordMode.getId() + ""});
                finish();
                return;
            }
            String str3 = this.ledmode;
            if (str3 != null && this.ledweek != null) {
                if (!str3.equals("") && !this.ledweek.equals("")) {
                    MyDataBase.getInstance(this).insert(LedBleApplication.getApp().getSceneBean(), this.hour, this.minute, this.ledmode, this.ledweek, 1);
                    finish();
                    return;
                }
                Toast.makeText(this, (int) R.string.enter, 1).show();
                return;
            }
            Toast.makeText(this, (int) R.string.enter, 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> likeModel() {
        ArrayList arrayList = new ArrayList();
        for (String str : getResources().getStringArray(R.array.modelike)) {
            arrayList.add(str);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> getWeek() {
        ArrayList arrayList = new ArrayList();
        for (String str : getResources().getStringArray(R.array.week)) {
            arrayList.add(str);
        }
        return arrayList;
    }
}
