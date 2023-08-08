package com.common;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

/* loaded from: classes.dex */
public class BaseActivity extends FragmentActivity {
    public void initView() {
    }

    protected boolean isSupportSwipeBack() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        setRequestedOrientation(1);
        super.onCreate(bundle);
        setRequestedOrientation(1);
    }

    public String getInput(EditText editText) {
        return editText.getText().toString();
    }

    public TextView findTextViewById(int i) {
        return (TextView) findViewById(i);
    }

    public EditText findEditTextById(int i) {
        return (EditText) findViewById(i);
    }

    public Button findButtonById(int i) {
        return (Button) findViewById(i);
    }

    public ImageView findImageViewById(int i) {
        return (ImageView) findViewById(i);
    }

    public ImageButton findImageButtonById(int i) {
        return (ImageButton) findViewById(i);
    }

    public ListView findListViewById(int i) {
        return (ListView) findViewById(i);
    }

    public RelativeLayout findRelativeLayout(int i) {
        return (RelativeLayout) findViewById(i);
    }

    public LinearLayout findLinearLayout(int i) {
        return (LinearLayout) findViewById(i);
    }

    public AutoCompleteTextView findAutoCompleteTextById(int i) {
        return (AutoCompleteTextView) findViewById(i);
    }
}
