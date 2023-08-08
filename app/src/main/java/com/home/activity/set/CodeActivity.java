package com.home.activity.set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import com.common.adapter.ChipSelectAdapter;
import com.common.adapter.RGBSelectAdapter;
import com.common.net.NetResult;
import com.common.uitl.SharePersistent;
import com.common.uitl.StringUtils;
import com.home.base.LedBleActivity;
import com.home.bean.AdapterBean;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CodeActivity extends LedBleActivity {
    private ImageView backButton;
    private Button buttonChipselect;
    private ChipSelectAdapter chipAdapter;
    private int chipType;
    private Context mContext;
    private ListView mLeftLv;
    private RGBSelectAdapter rgbAdapter;
    private TextView rgbTvResult;
    private Button setSpacercodeBtn;
    private Button setStartcodeBtn;
    private SharedPreferences sp;
    private int spacerCode;
    private Button spacercodeNum;
    private TextView spacercodeTvResult;
    private int startCode;
    private Button startcodeNum;
    private TextView startcodeTvResult;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_code);
        ListView listView = (ListView) findViewById(R.id.lv_left);
        this.mLeftLv = listView;
        listView.setAdapter((ListAdapter) buildRGBModel());
        this.mLeftLv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.set.CodeActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                CodeActivity.this.rgbAdapter.setIndex(i);
                CodeActivity.this.rgbAdapter.notifyDataSetChanged();
                AdapterBean adapterBean = (AdapterBean) CodeActivity.this.rgbAdapter.getItem(i);
                CodeActivity.this.chipType = Integer.parseInt(adapterBean.getValue().trim());
                Toast.makeText(CodeActivity.this.getApplicationContext(), adapterBean.getLabel(), 0).show();
                CodeActivity.this.rgbTvResult.setText(adapterBean.getLabel());
                SharePersistent.saveConfigData(CodeActivity.this.mContext, Constant.CHIP_TYPE, Constant.CHIP_TYPE_KEY, adapterBean.getLabel(), i);
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.backcode);
        this.backButton = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.CodeActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CodeActivity.this.finish();
            }
        });
        Button button = (Button) findViewById(R.id.button_chipselect_ok);
        this.buttonChipselect = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.CodeActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CodeActivity.this.chipType <= 0) {
                    Toast.makeText(CodeActivity.this.mContext, (int) R.string.chiptypevaluetip, 0).show();
                } else if (CodeActivity.this.startCode < 0) {
                    Toast.makeText(CodeActivity.this.mContext, (int) R.string.startcodevaluetip, 0).show();
                } else if (CodeActivity.this.spacerCode <= 0) {
                    Toast.makeText(CodeActivity.this.mContext, (int) R.string.spacercodevaluetip, 0).show();
                } else {
                    NetConnectBle.getInstance().configCode(CodeActivity.this.chipType, CodeActivity.this.startCode, CodeActivity.this.spacerCode);
                    CodeActivity.this.finish();
                }
            }
        });
        Button button2 = (Button) findViewById(R.id.btn_startcode_set);
        this.setStartcodeBtn = button2;
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.CodeActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CodeActivity.this.setPixValue(0, Constant.PIX_STARTCODE_TYPE, Constant.PIX_STARTCODE_KEY);
            }
        });
        Button button3 = (Button) findViewById(R.id.startcodeNum);
        this.startcodeNum = button3;
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.CodeActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CodeActivity.this.setPixValue(0, Constant.PIX_STARTCODE_TYPE, Constant.PIX_STARTCODE_KEY);
            }
        });
        Button button4 = (Button) findViewById(R.id.btn_spacercode_set);
        this.setSpacercodeBtn = button4;
        button4.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.CodeActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CodeActivity.this.setPixValue(1, Constant.PIX_SPACERCODE_TYPE, Constant.PIX_SPACERCODE_KEY);
            }
        });
        Button button5 = (Button) findViewById(R.id.spacercodeNum);
        this.spacercodeNum = button5;
        button5.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.CodeActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CodeActivity.this.setPixValue(1, Constant.PIX_SPACERCODE_TYPE, Constant.PIX_SPACERCODE_KEY);
            }
        });
        this.rgbTvResult = (TextView) findViewById(R.id.tv_rgb_result);
        this.spacercodeTvResult = (TextView) findViewById(R.id.tv_spacercode_result);
        this.startcodeTvResult = (TextView) findViewById(R.id.tv_startcode_result);
        if (this.mLeftLv != null && this.rgbTvResult != null) {
            String configData = SharePersistent.getConfigData(this.mContext, Constant.CHIP_TYPE, Constant.CHIP_TYPE_KEY);
            if (configData == null || configData.length() <= 0) {
                this.chipType = 2;
                this.rgbTvResult.setText("UCS512C");
                this.rgbAdapter.setIndex(0);
                this.rgbAdapter.notifyDataSetChanged();
                this.mLeftLv.setSelection(0);
            } else {
                String[] split = configData.split(",");
                this.rgbTvResult.setText(split[0]);
                this.rgbAdapter.setIndex(Integer.parseInt(split[1].trim()));
                this.rgbAdapter.notifyDataSetChanged();
                this.mLeftLv.setSelection(Integer.parseInt(split[1].trim()));
                this.chipType = Integer.parseInt(split[1].trim());
            }
        }
        if (this.spacercodeTvResult != null) {
            String configData2 = SharePersistent.getConfigData(this.mContext, Constant.PIX_SPACERCODE_TYPE, Constant.PIX_SPACERCODE_KEY);
            if (configData2 == null || configData2.length() <= 0) {
                this.spacerCode = 3;
                this.spacercodeTvResult.setText(ExifInterface.GPS_MEASUREMENT_3D);
            } else {
                String[] split2 = configData2.split(",");
                this.spacercodeTvResult.setText(split2[0]);
                this.spacercodeNum.setText(split2[0]);
                this.spacerCode = Integer.parseInt(split2[0].trim());
            }
        }
        if (this.startcodeTvResult != null) {
            String configData3 = SharePersistent.getConfigData(this.mContext, Constant.PIX_STARTCODE_TYPE, Constant.PIX_STARTCODE_KEY);
            if (configData3 == null || configData3.length() <= 0) {
                this.startCode = 0;
                this.startcodeTvResult.setText(NetResult.CODE_OK);
                return;
            }
            String[] split3 = configData3.split(",");
            this.startcodeTvResult.setText(split3[0]);
            this.startcodeNum.setText(split3[0]);
            this.startCode = Integer.parseInt(split3[0].trim());
        }
    }

    private RGBSelectAdapter buildRGBModel() {
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.chip_model);
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
    public void setPixValue(final int i, String str, String str2) {
        final EditText editText = new EditText(this.mContext);
        editText.setHint("0~1024");
        new AlertDialog.Builder(this.mContext).setTitle(R.string.btn_pix_number).setIcon(17301659).setView(editText).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.CodeActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.dismiss();
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(CodeActivity.this.mContext, (int) R.string.valuetip, 0).show();
                    return;
                }
                String obj = editText.getText().toString();
                if (obj.length() <= 0 && i == 0) {
                    Toast.makeText(CodeActivity.this.mContext, (int) R.string.startcodevaluetip, 0).show();
                } else if (obj.length() <= 0 && 1 == i) {
                    Toast.makeText(CodeActivity.this.mContext, (int) R.string.spacercodevaluetip, 0).show();
                } else if (!StringUtils.isNumeric(obj)) {
                    Toast.makeText(CodeActivity.this.mContext, (int) R.string.key_in_numbers, 0).show();
                } else {
                    long parseLong = Long.parseLong(obj.trim());
                    if (parseLong <= 0 || parseLong >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                        Toast.makeText(CodeActivity.this, (int) R.string.please_enter_again, 0).show();
                        return;
                    }
                    String valueOf = String.valueOf(parseLong);
                    if (StringUtils.isEmpty(obj)) {
                        return;
                    }
                    if (1 == i) {
                        CodeActivity.this.spacercodeTvResult.setText(valueOf);
                        CodeActivity.this.spacercodeNum.setText(valueOf);
                        CodeActivity.this.spacerCode = Integer.parseInt(valueOf.trim());
                        SharePersistent.saveConfigData(CodeActivity.this.mContext, Constant.PIX_SPACERCODE_TYPE, Constant.PIX_SPACERCODE_KEY, valueOf, 0);
                        return;
                    }
                    CodeActivity.this.startcodeTvResult.setText(valueOf);
                    CodeActivity.this.startcodeNum.setText(valueOf);
                    CodeActivity.this.startCode = Integer.parseInt(valueOf.trim());
                    SharePersistent.saveConfigData(CodeActivity.this.mContext, Constant.PIX_STARTCODE_TYPE, Constant.PIX_STARTCODE_KEY, valueOf, 0);
                }
            }
        }).setNegativeButton(R.string.cancell_dialog, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.CodeActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
