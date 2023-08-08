package com.forum.login;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.ledlamp.R;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
public class TimeCountUtil {
    Activity activity;
    TextView btGetCheckcode;
    int time;
    private Handler mHandler = new Handler() { // from class: com.forum.login.TimeCountUtil.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            TextView textView = TimeCountUtil.this.btGetCheckcode;
            textView.setText(message.arg1 + " " + TimeCountUtil.this.activity.getString(R.string.second));
            TimeCountUtil.this.btGetCheckcode.setClickable(false);
            TimeCountUtil.this.countTime();
        }
    };
    private Timer timer = new Timer();

    public TimeCountUtil(Activity activity, TextView textView, int i) {
        this.activity = activity;
        this.btGetCheckcode = textView;
        this.time = i;
    }

    public void countTime() {
        TextView textView = this.btGetCheckcode;
        textView.setText(this.time + " " + this.activity.getString(R.string.second));
        this.timer.schedule(new TimerTask() { // from class: com.forum.login.TimeCountUtil.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TimeCountUtil timeCountUtil = TimeCountUtil.this;
                timeCountUtil.time--;
                Message obtainMessage = TimeCountUtil.this.mHandler.obtainMessage();
                obtainMessage.arg1 = TimeCountUtil.this.time;
                TimeCountUtil.this.mHandler.sendMessage(obtainMessage);
            }
        }, 1000L);
        if (this.time == 0) {
            this.timer.cancel();
            this.btGetCheckcode.setText(this.activity.getString(R.string.get_checkcode));
            this.btGetCheckcode.setClickable(true);
        }
    }
}
