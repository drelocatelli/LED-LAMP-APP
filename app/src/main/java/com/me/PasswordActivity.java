package com.me;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import com.common.listener.ReceiveDataListener;
import com.common.listener.ReceiveDataListenerManager;
import com.common.uitl.SharePersistent;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.ledlamp.R;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class PasswordActivity extends LedBleActivity implements View.OnClickListener, ReceiveDataListener {
    private static final String DIGISTS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String TAG = "RegisterActivity";
    private Button btnConfirm;
    private EditText clearEtCheckCode;
    private EditText clearEtCheckMode;
    private EditText clearEtPassWord;
    private DigitsKeyListener digitsKeyListener;
    private EditText etName;
    private ImageView ivBack;
    private ImageView releseCharacter;
    private TextView tvCheckcode;
    private TextView tvSpan;
    private TextView tvTitle;
    boolean sun = true;
    boolean mon = true;
    boolean tue = true;
    boolean wed = true;
    boolean thu = true;
    boolean fri = true;
    boolean sta = true;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        setContentView(R.layout.activity_password);
        ReceiveDataListenerManager.getInstance().registerListtener(this);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewAdd);
        this.releseCharacter = imageView;
        imageView.setVisibility(8);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        this.clearEtPassWord = (EditText) findViewById(R.id.clearEtPassWord);
        this.clearEtCheckMode = (EditText) findViewById(R.id.clearEtCheckMode);
        this.clearEtCheckCode = (EditText) findViewById(R.id.clearEtCheckCode);
        this.tvCheckcode = (TextView) findViewById(R.id.tvCheckcode);
        this.btnConfirm = (Button) findViewById(R.id.btnConfirm);
        this.etName = (EditText) findViewById(R.id.etName);
        DigitsKeyListener digitsKeyListener = new DigitsKeyListener() { // from class: com.me.PasswordActivity.1
            @Override // android.text.method.DigitsKeyListener, android.text.method.KeyListener
            public int getInputType() {
                return 1;
            }

            @Override // android.text.method.DigitsKeyListener, android.text.method.NumberKeyListener
            protected char[] getAcceptedChars() {
                return PasswordActivity.DIGISTS.toCharArray();
            }
        };
        this.digitsKeyListener = digitsKeyListener;
        this.etName.setKeyListener(digitsKeyListener);
        this.tvTitle.setText(getString(R.string.password));
        this.ivBack.setOnClickListener(this);
        this.tvCheckcode.setOnClickListener(this);
        this.btnConfirm.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.tvSpan);
        this.tvSpan = textView;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvSpan.setHighlightColor(getResources().getColor(17170445));
        String perference = SharePersistent.getPerference(this, Constant.PasswordSet);
        if (perference != null) {
            if (perference.length() > 0) {
                EditText editText = this.etName;
                editText.setText("" + perference);
                return;
            }
            return;
        }
        SharePersistent.savePerference(this, Constant.PasswordSet, "00000000");
    }

    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        ReceiveDataListenerManager.getInstance().unRegisterListener(this);
    }

    @Override // com.common.listener.ReceiveDataListener
    public void notifyReceiveData(String str, String str2) {
        if (!str.equalsIgnoreCase(Constant.PasswordSet) || str2 == null) {
            return;
        }
        if (str2.contains("1A") || str2.contains("1a")) {
            int parseInt = Integer.parseInt(str2.split(" ")[2], 16);
            LedBleApplication.getApp().setSafetyLevel(parseInt);
            if (parseInt == 1) {
                Toast.makeText(this, "" + getResources().getString(R.string.password_error), 0).show();
            } else if (parseInt != 2) {
            } else {
                Toast.makeText(this, "" + getResources().getString(R.string.The_password_cannot_be_the_ame_number_or_letter), 0).show();
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String obj = this.etName.getText().toString();
        int id = view.getId();
        if (id != R.id.btnConfirm) {
            if (id != R.id.ivBack) {
                return;
            }
            SharePersistent.savePerference(this, Constant.Activity, "");
            finish();
            return;
        }
        int i = 3;
        if (LedBleApplication.getApp().getSafetyLevel() != 3) {
            Toast.makeText(this, "" + getResources().getString(R.string.The_system_is_not_in_breathing_mode_yet), 0).show();
        } else if (TextUtils.isEmpty(obj)) {
            Toast.makeText(this, getString(R.string.input_not_null), 0).show();
        } else if (obj.length() != 8) {
            Toast.makeText(this, getString(R.string.input_length_error), 0).show();
        } else if (isOrderNumeric(obj)) {
            Toast.makeText(this, getString(R.string.The_password_cannot_be_the_ame_number_or_letter), 0).show();
        } else if (Pattern.compile("^[a-fA-F0-9]+$").matcher(obj).matches()) {
            Toast.makeText(this, getResources().getString(R.string.sent_success), 0).show();
            SharePersistent.savePerference(this, Constant.PasswordSet, obj);
            Arrays.asList(getResources().getStringArray(R.array.week));
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("ss");
            int parseInt = Integer.parseInt(simpleDateFormat.format(date).trim());
            int parseInt2 = Integer.parseInt(simpleDateFormat2.format(date).trim());
            Integer.parseInt(simpleDateFormat3.format(date).trim());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String valueOf = String.valueOf(calendar.get(7));
            if ("1".equals(valueOf)) {
                i = 7;
            } else if (ExifInterface.GPS_MEASUREMENT_2D.equals(valueOf)) {
                i = 1;
            } else if (ExifInterface.GPS_MEASUREMENT_3D.equals(valueOf)) {
                i = 2;
            } else if (!"4".equals(valueOf)) {
                if ("5".equals(valueOf)) {
                    i = 4;
                } else if ("6".equals(valueOf)) {
                    i = 5;
                } else {
                    i = "7".equals(valueOf) ? 6 : 0;
                }
            }
            int i2 = (i << 5) | parseInt;
            if (obj != null && obj.length() == 8) {
                NetConnectBle.getInstanceByGroup("").setPassword(Integer.parseInt(obj.substring(0, 2), 16), Integer.parseInt(obj.substring(2, 4), 16), Integer.parseInt(obj.substring(4, 6), 16), Integer.parseInt(obj.substring(6, 8), 16), i2, parseInt2);
            } else {
                NetConnectBle.getInstanceByGroup("").setPassword(0, 0, 0, 0, i2, parseInt2);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.input_format_error), 0).show();
        }
    }

    public static boolean isOrderNumeric(String str) {
        String upperCase = str.toUpperCase();
        String substring = upperCase.substring(0, 1);
        return substring.equals(upperCase.substring(1, 2)) && substring.equals(upperCase.substring(2, 3)) && substring.equals(upperCase.substring(3, 4)) && substring.equals(upperCase.substring(4, 5)) && substring.equals(upperCase.substring(5, 6)) && substring.equals(upperCase.substring(6, 7)) && substring.equals(upperCase.substring(7, 8));
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
