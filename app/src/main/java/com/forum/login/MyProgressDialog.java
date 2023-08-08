package com.forum.login;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MyProgressDialog extends Dialog {
    String message;
    TextView messageTextView;
    String title;
    TextView titleTextView;

    public MyProgressDialog(Context context, String str, String str2) {
        super(context, 16973839);
        this.message = str2;
        this.title = str;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        super.onCreate(bundle);
        setContentView(R.layout.layout_progress_dialog);
        this.titleTextView = (TextView) findViewById(R.id.tvTitle);
        setTitle(this.title);
        this.messageTextView = (TextView) findViewById(R.id.tvMessage);
        setMessage(this.message);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
        this.messageTextView.setText(str);
    }

    public TextView getMessageTextView() {
        return this.messageTextView;
    }

    public void setMessageTextView(TextView textView) {
        this.messageTextView = textView;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
        if (TextUtils.isEmpty(str)) {
            this.titleTextView.setVisibility(8);
            return;
        }
        this.titleTextView.setVisibility(0);
        this.titleTextView.setText(str);
    }

    public TextView getTitleTextView() {
        return this.titleTextView;
    }

    public void setTitleTextView(TextView textView) {
        this.titleTextView = textView;
    }
}
