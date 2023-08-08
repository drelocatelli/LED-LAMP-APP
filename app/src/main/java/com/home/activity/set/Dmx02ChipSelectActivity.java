package com.home.activity.set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.common.net.NetResult;
import com.common.uitl.SharePersistent;
import com.common.uitl.StringUtils;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.main.MainActivity_DMX02;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.net.NetConnectBle;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;

/* loaded from: classes.dex */
public class Dmx02ChipSelectActivity extends LedBleActivity {
    private static final String TAG = "Dmx02ChipSelectActivity";
    private ImageView backButton;
    private int bannerSort;
    private Button btnSetPixLong;
    private Button btnSetPixWidth;
    private Button buttonChipselect;
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private View mContentView;
    private Context mContext;
    private int pixLong;
    private int pixWidth;
    private int wiring;
    private int selectedPosition = 0;
    private List<String> gifListName = new ArrayList();
    private int[] icon = {R.drawable.setup_wiring_1, R.drawable.setup_wiring_2, R.drawable.setup_wiring_3, R.drawable.setup_wiring_4, R.drawable.setup_wiring_5, R.drawable.setup_wiring_6, R.drawable.setup_wiring_7, R.drawable.setup_wiring_8, R.drawable.setup_wiring_9, R.drawable.setup_wiring_10, R.drawable.setup_wiring_11, R.drawable.setup_wiring_12, R.drawable.setup_wiring_13, R.drawable.setup_wiring_14, R.drawable.setup_wiring_15, R.drawable.setup_wiring_16};
    private int[] icon_check = {R.drawable.setup_wiring_1_check, R.drawable.setup_wiring_2_check, R.drawable.setup_wiring_3_check, R.drawable.setup_wiring_4_check, R.drawable.setup_wiring_5_check, R.drawable.setup_wiring_6_check, R.drawable.setup_wiring_7_check, R.drawable.setup_wiring_8_check, R.drawable.setup_wiring_9_check, R.drawable.setup_wiring_10_check, R.drawable.setup_wiring_11_check, R.drawable.setup_wiring_12_check, R.drawable.setup_wiring_13_check, R.drawable.setup_wiring_14_check, R.drawable.setup_wiring_15_check, R.drawable.setup_wiring_16_check};

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.black), true);
        this.mContext = this;
        initView();
    }

    public void initView() {
        String str;
        setContentView(R.layout.dmx02_activity_chip_select);
        this.mContentView = (LinearLayout) findViewById(R.id.ll_dmx02_chip_select);
        final String[] strArr = {"RGB", "RBG", "GRB", "GBR", "BRG", "BGR"};
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(Dmx02ChipSelectActivity.this, R.anim.layout_scale));
                TextView textView = (TextView) view;
                for (int i = 1; i <= strArr.length; i++) {
                    if (textView.getText().equals(strArr[i - 1])) {
                        Dmx02ChipSelectActivity.this.bannerSort = i;
                        textView.setTextColor(-1);
                        Context applicationContext = Dmx02ChipSelectActivity.this.getApplicationContext();
                        SharePersistent.savePerference(applicationContext, LedBleApplication.getApp().getSceneBean() + Dmx02ChipSelectActivity.TAG + "SORT", i);
                    } else {
                        View view2 = Dmx02ChipSelectActivity.this.mContentView;
                        ((TextView) view2.findViewWithTag(r3 + i)).setTextColor(-12237756);
                    }
                }
            }
        };
        for (int i = 1; i <= 6; i++) {
            TextView textView = (TextView) this.mContentView.findViewWithTag("viewSort" + i);
            textView.setOnClickListener(onClickListener);
            int i2 = SharePersistent.getInt(getApplicationContext(), LedBleApplication.getApp().getSceneBean() + TAG + "SORT");
            if (i2 == i) {
                textView.setTextColor(-1);
            } else if (i2 == 0 && i == 3) {
                textView.setTextColor(-1);
                SharePersistent.savePerference(getApplicationContext(), LedBleApplication.getApp().getSceneBean() + TAG + "SORT", 3);
            } else {
                textView.setTextColor(-12237756);
            }
        }
        for (int i3 = 1; i3 <= this.icon.length; i3++) {
            if (i3 <= 9) {
                this.gifListName.add(getString(R.string.Tab_Effect) + NetResult.CODE_OK + i3);
            } else {
                this.gifListName.add(getString(R.string.Tab_Effect) + "" + i3);
            }
        }
        this.gridView = (GridView) findViewById(R.id.gridView);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        this.imageAdapter = imageAdapter;
        this.gridView.setAdapter((ListAdapter) imageAdapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i4, long j) {
                Dmx02ChipSelectActivity.this.wiring = ((int) j) + 1;
                Log.e(Dmx02ChipSelectActivity.TAG, "wiring  :  " + Dmx02ChipSelectActivity.this.wiring);
                Dmx02ChipSelectActivity.this.imageAdapter.clearSelection(i4);
                Dmx02ChipSelectActivity.this.imageAdapter.notifyDataSetChanged();
                if (MainActivity_DMX02.getMainActivity() != null) {
                    SharePersistent.saveInt(Dmx02ChipSelectActivity.this.getApplicationContext(), "RGBDataBase-WIRING-DMX-String", Dmx02ChipSelectActivity.this.wiring);
                }
            }
        });
        if (MainActivity_DMX02.getMainActivity() != null) {
            this.wiring = SharePersistent.getInt(getApplicationContext(), "RGBDataBase-WIRING-DMX-String");
            Log.e(TAG, "wiring  :  " + this.wiring);
            int i4 = this.wiring;
            if (i4 > 0) {
                int i5 = i4 - 1;
                this.selectedPosition = i5;
                this.gridView.setSelection(i5);
                this.imageAdapter.notifyDataSetChanged();
            }
        }
        ImageView imageView = (ImageView) findViewById(R.id.Back);
        this.backButton = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02ChipSelectActivity.this.finish();
            }
        });
        Button button = (Button) findViewById(R.id.button_chipselect_ok);
        this.buttonChipselect = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MainActivity_DMX02.mActivity != null) {
                    NetConnectBle.getInstance().setConfigDmx02(Dmx02ChipSelectActivity.this.bannerSort, Dmx02ChipSelectActivity.this.wiring, (byte) (Dmx02ChipSelectActivity.this.pixLong >> 8), (byte) Dmx02ChipSelectActivity.this.pixLong, (byte) (Dmx02ChipSelectActivity.this.pixWidth >> 8), (byte) Dmx02ChipSelectActivity.this.pixWidth);
                }
                Dmx02ChipSelectActivity.this.finish();
            }
        });
        Button button2 = (Button) findViewById(R.id.btnSetPixLong);
        this.btnSetPixLong = button2;
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02ChipSelectActivity.this.setPixValue(view);
            }
        });
        Button button3 = (Button) findViewById(R.id.btnSetPixWidth);
        this.btnSetPixWidth = button3;
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Dmx02ChipSelectActivity.this.setPixValue(view);
            }
        });
        int i6 = SharePersistent.getInt(getApplicationContext(), LedBleApplication.getApp().getSceneBean() + TAG + "SORT");
        if (i6 == 0) {
            this.bannerSort = 3;
            this.btnSetPixWidth.setText("16");
            this.btnSetPixLong.setText("16");
            this.pixLong = 16;
            this.pixWidth = 16;
        } else {
            this.bannerSort = i6;
        }
        String str2 = null;
        if (MainActivity_DMX02.getMainActivity() != null) {
            str2 = SharePersistent.getPerference(getApplicationContext(), LedBleApplication.getApp().getSceneBean() + "btnSetPixLong");
            str = SharePersistent.getPerference(getApplicationContext(), LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth");
        } else {
            str = null;
        }
        if (str2 == null || str2.length() <= 0) {
            this.pixLong = 16;
            SharePersistent.savePerference(getApplicationContext(), LedBleApplication.getApp().getSceneBean() + "btnSetPixLong", "16");
        } else {
            this.pixLong = Integer.parseInt(str2);
            this.btnSetPixLong.setText(str2);
        }
        if (str == null || str.length() <= 0) {
            this.pixWidth = 16;
            SharePersistent.savePerference(getApplicationContext(), LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth", "16");
            return;
        }
        this.pixWidth = Integer.parseInt(str);
        this.btnSetPixWidth.setText(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPixValue(final View view) {
        final EditText editText = new EditText(this.mContext);
        editText.setHint("0~1024");
        new AlertDialog.Builder(this.mContext).setTitle(R.string.btn_pix_number).setIcon(17301659).setView(editText).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(Dmx02ChipSelectActivity.this.mContext, (int) R.string.valuetip, 0).show();
                    return;
                }
                String obj = editText.getText().toString();
                if (!StringUtils.isNumeric(obj)) {
                    Toast.makeText(Dmx02ChipSelectActivity.this.mContext, (int) R.string.key_in_numbers, 0).show();
                    return;
                }
                long parseLong = Long.parseLong(obj.trim());
                if (parseLong <= 0 || parseLong > PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                    Toast.makeText(Dmx02ChipSelectActivity.this, (int) R.string.please_enter_again, 0).show();
                    return;
                }
                String valueOf = String.valueOf(parseLong);
                if (StringUtils.isEmpty(obj)) {
                    return;
                }
                if (view.equals(Dmx02ChipSelectActivity.this.btnSetPixLong)) {
                    Dmx02ChipSelectActivity.this.btnSetPixLong.setText(valueOf);
                    Dmx02ChipSelectActivity.this.pixLong = Integer.parseInt(valueOf.trim());
                    Context applicationContext = Dmx02ChipSelectActivity.this.getApplicationContext();
                    SharePersistent.savePerference(applicationContext, LedBleApplication.getApp().getSceneBean() + "btnSetPixLong", valueOf);
                } else if (view.equals(Dmx02ChipSelectActivity.this.btnSetPixWidth)) {
                    Dmx02ChipSelectActivity.this.btnSetPixWidth.setText(valueOf);
                    Dmx02ChipSelectActivity.this.pixWidth = Integer.parseInt(valueOf.trim());
                    Context applicationContext2 = Dmx02ChipSelectActivity.this.getApplicationContext();
                    SharePersistent.savePerference(applicationContext2, LedBleApplication.getApp().getSceneBean() + "btnSetPixWidth", valueOf);
                }
            }
        }).setNegativeButton(R.string.cancell_dialog, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.Dmx02ChipSelectActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public ImageAdapter(Context context) {
            this.mContext = context;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return Dmx02ChipSelectActivity.this.icon.length;
        }

        public void clearSelection(int i) {
            Dmx02ChipSelectActivity.this.selectedPosition = i;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.dmx02_item_mode, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.itemImg = (GifImageView) view.findViewById(R.id.gifImv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if (Dmx02ChipSelectActivity.this.selectedPosition == i) {
                Glide.with(this.mContext).load(Integer.valueOf(Dmx02ChipSelectActivity.this.icon_check[i])).into(viewHolder.itemImg);
            } else {
                Glide.with(this.mContext).load(Integer.valueOf(Dmx02ChipSelectActivity.this.icon[i])).into(viewHolder.itemImg);
            }
            return view;
        }

        /* loaded from: classes.dex */
        class ViewHolder {
            GifImageView itemImg;
            LinearLayout llGifIVBoder;
            TextView name;

            ViewHolder() {
            }
        }
    }
}
