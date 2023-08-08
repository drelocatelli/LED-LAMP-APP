package com.home.activity.set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.common.adapter.ChipSelectAdapter;
import com.common.adapter.RGBSelectAdapter;
import com.common.uitl.SharePersistent;
import com.common.uitl.StringUtils;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.main.MainActivity_BLE;
import com.home.activity.main.MainActivity_DMX03;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.bean.AdapterBean;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ChipSelectActivity extends LedBleActivity {
    private ImageView backButton;
    private int bannerPix;
    private int bannerSort;
    private int bannerType = 4;
    private Button buttonChipselect;
    private ChipSelectAdapter chipAdapter;
    private Context mContext;
    private ListView mLeftLv;
    private Button pixNumber;
    private TextView pixTvResult;
    private RGBSelectAdapter rgbAdapter;
    private TextView rgbTvResult;
    private Button setPixBtn;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.black), true);
        this.mContext = this;
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_chip_select);
        ListView listView = (ListView) findViewById(R.id.lv_left);
        this.mLeftLv = listView;
        listView.setAdapter((ListAdapter) buildRGBModel());
        this.mLeftLv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.set.ChipSelectActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ChipSelectActivity.this.rgbAdapter.setIndex(i);
                ChipSelectActivity.this.rgbAdapter.notifyDataSetChanged();
                AdapterBean adapterBean = (AdapterBean) ChipSelectActivity.this.rgbAdapter.getItem(i);
                ChipSelectActivity.this.bannerSort = Integer.parseInt(adapterBean.getValue().replaceAll(" ", "").trim());
                Toast.makeText(ChipSelectActivity.this.getApplicationContext(), ChipSelectActivity.this.mContext.getResources().getString(R.string.current_rgb_format, adapterBean.getLabel()), 0).show();
                ChipSelectActivity.this.rgbTvResult.setText(adapterBean.getLabel());
                SharePersistent.savePerference(ChipSelectActivity.this.getApplicationContext(), LedBleApplication.getApp().getSceneBean() + "RGBDataBase-SORT-DMX-String", adapterBean.getLabel() + "," + i);
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.Back);
        this.backButton = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.ChipSelectActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ChipSelectActivity.this.finish();
            }
        });
        Button button = (Button) findViewById(R.id.button_chipselect_ok);
        this.buttonChipselect = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.ChipSelectActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (LedBleApplication.getApp().getSceneBean().contains("DMX03")) {
                    MainActivity_DMX03.getMainActivity().setConfigSPI(ChipSelectActivity.this.bannerType, (byte) (ChipSelectActivity.this.bannerPix >> 8), (byte) ChipSelectActivity.this.bannerPix, ChipSelectActivity.this.bannerSort);
                } else {
                    MainActivity_BLE.getMainActivity().setConfigSPI(ChipSelectActivity.this.bannerType, (byte) (ChipSelectActivity.this.bannerPix >> 8), (byte) ChipSelectActivity.this.bannerPix, ChipSelectActivity.this.bannerSort);
                }
                ChipSelectActivity.this.finish();
            }
        });
        Button button2 = (Button) findViewById(R.id.btn_pix_set);
        this.setPixBtn = button2;
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.ChipSelectActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ChipSelectActivity.this.setPixValue();
            }
        });
        Button button3 = (Button) findViewById(R.id.tv_pix_num);
        this.pixNumber = button3;
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.ChipSelectActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ChipSelectActivity.this.setPixValue();
            }
        });
        this.rgbTvResult = (TextView) findViewById(R.id.tv_rgb_result);
        this.pixTvResult = (TextView) findViewById(R.id.tv_pix_result);
        if (this.mLeftLv != null && this.rgbTvResult != null) {
            Context applicationContext = getApplicationContext();
            String perference = SharePersistent.getPerference(applicationContext, LedBleApplication.getApp().getSceneBean() + "RGBDataBase-SORT-DMX-String");
            if (perference == null || perference.length() <= 0) {
                this.bannerSort = 3;
                this.pixNumber.setText("200");
                this.pixTvResult.setText("200");
                this.rgbAdapter.setIndex(2);
                this.rgbAdapter.notifyDataSetChanged();
                this.mLeftLv.setSelection(2);
                this.rgbTvResult.setText("GRB");
            } else {
                String[] split = perference.split(",");
                this.rgbTvResult.setText(split[0]);
                this.bannerSort = Integer.parseInt(split[1].trim()) + 1;
                this.rgbAdapter.setIndex(Integer.parseInt(split[1].trim()));
                this.rgbAdapter.notifyDataSetChanged();
                this.mLeftLv.setSelection(Integer.parseInt(split[1].trim()));
            }
        }
        if (this.pixTvResult != null) {
            Context applicationContext2 = getApplicationContext();
            String perference2 = SharePersistent.getPerference(applicationContext2, LedBleApplication.getApp().getSceneBean() + "DMX-PIX-String");
            if (perference2 == null || perference2.length() <= 0) {
                this.bannerPix = 200;
                Context applicationContext3 = getApplicationContext();
                SharePersistent.savePerference(applicationContext3, LedBleApplication.getApp().getSceneBean() + "DMX-PIX-String", "200");
                return;
            }
            this.pixNumber.setText(perference2);
            this.pixTvResult.setText(perference2);
            this.bannerPix = Integer.parseInt(perference2);
        }
    }

    private RGBSelectAdapter buildRGBModel() {
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.rgb_model);
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            String[] split = str.split(",");
            arrayList.add(new AdapterBean(split[0], split[1]));
        }
        RGBSelectAdapter rGBSelectAdapter = new RGBSelectAdapter(this.mContext, arrayList);
        this.rgbAdapter = rGBSelectAdapter;
        return rGBSelectAdapter;
    }

    private ChipSelectAdapter buildChipModel() {
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.light_banner);
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            String[] split = str.split(",");
            arrayList.add(new AdapterBean(split[0], split[1]));
        }
        ChipSelectAdapter chipSelectAdapter = new ChipSelectAdapter(this.mContext, arrayList);
        this.chipAdapter = chipSelectAdapter;
        return chipSelectAdapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPixValue() {
        final EditText editText = new EditText(this.mContext);
        editText.setHint("0~1024");
        new AlertDialog.Builder(this.mContext).setTitle(R.string.btn_pix_number).setIcon(17301659).setView(editText).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.ChipSelectActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(ChipSelectActivity.this.mContext, (int) R.string.valuetip, 0).show();
                    return;
                }
                String obj = editText.getText().toString();
                if (!StringUtils.isNumeric(obj)) {
                    Toast.makeText(ChipSelectActivity.this.mContext, (int) R.string.key_in_numbers, 0).show();
                    return;
                }
                long parseLong = Long.parseLong(obj.trim());
                if (parseLong <= 0 || parseLong > Integer.parseInt(r3)) {
                    Toast.makeText(ChipSelectActivity.this, (int) R.string.please_enter_again, 0).show();
                    return;
                }
                String valueOf = String.valueOf(parseLong);
                if (StringUtils.isEmpty(obj)) {
                    return;
                }
                ChipSelectActivity.this.bannerPix = Integer.parseInt(valueOf.trim());
                ChipSelectActivity.this.pixNumber.setText(valueOf);
                ChipSelectActivity.this.pixTvResult.setText(valueOf);
                Context applicationContext = ChipSelectActivity.this.getApplicationContext();
                SharePersistent.savePerference(applicationContext, LedBleApplication.getApp().getSceneBean() + "DMX-PIX-String", valueOf);
            }
        }).setNegativeButton(R.string.cancell_dialog, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.ChipSelectActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
