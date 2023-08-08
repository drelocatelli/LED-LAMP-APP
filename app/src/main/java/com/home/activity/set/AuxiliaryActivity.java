package com.home.activity.set;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleActivity;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class AuxiliaryActivity extends LedBleActivity {
    private static Toast toast;
    private ImageView backButton;
    private Button button_KEY1;
    private Button button_KEY2;
    private Button button_KEY3;
    private Button button_KEY4;
    private Context mContext;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_auxiliary);
        ImageView imageView = (ImageView) findViewById(R.id.backcode);
        this.backButton = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.AuxiliaryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AuxiliaryActivity.this.finish();
            }
        });
        Button button = (Button) findViewById(R.id.button_KEY1);
        this.button_KEY1 = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.AuxiliaryActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity_BLE.getMainActivity().setAuxiliary(0, view);
                AuxiliaryActivity.shouToast(AuxiliaryActivity.this.getApplicationContext(), "KEY1");
            }
        });
        Button button2 = (Button) findViewById(R.id.button_KEY2);
        this.button_KEY2 = button2;
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.AuxiliaryActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity_BLE.getMainActivity().setAuxiliary(1, view);
                AuxiliaryActivity.shouToast(AuxiliaryActivity.this.getApplicationContext(), "KEY2");
            }
        });
        Button button3 = (Button) findViewById(R.id.button_KEY3);
        this.button_KEY3 = button3;
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.AuxiliaryActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity_BLE.getMainActivity().setAuxiliary(2, view);
                AuxiliaryActivity.shouToast(AuxiliaryActivity.this.getApplicationContext(), "KEY3");
            }
        });
        Button button4 = (Button) findViewById(R.id.button_KEY4);
        this.button_KEY4 = button4;
        button4.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.AuxiliaryActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity_BLE.getMainActivity().setAuxiliary(3, view);
                AuxiliaryActivity.shouToast(AuxiliaryActivity.this.getApplicationContext(), "KEY4");
            }
        });
    }

    public static void shouToast(Context context, String str) {
        Toast toast2 = toast;
        if (toast2 == null) {
            toast = Toast.makeText(context, str, 0);
        } else {
            toast2.setText(str);
        }
        toast.show();
    }
}
