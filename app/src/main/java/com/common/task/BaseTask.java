package com.common.task;

import android.os.AsyncTask;
import com.common.net.NetResult;
import java.util.HashMap;

/* loaded from: classes.dex */
public class BaseTask extends AsyncTask<HashMap<String, String>, Void, NetResult> {
    NetCallBack mCallBack;

    public BaseTask(NetCallBack netCallBack) {
        this.mCallBack = netCallBack;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        NetCallBack netCallBack = this.mCallBack;
        if (netCallBack != null) {
            netCallBack.onPreCall();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public NetResult doInBackground(HashMap<String, String>... hashMapArr) {
        NetCallBack netCallBack = this.mCallBack;
        if (netCallBack != null) {
            if (hashMapArr == null) {
                return netCallBack.onDoInBack(null);
            }
            return netCallBack.onDoInBack(hashMapArr[0]);
        }
        return null;
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        super.onCancelled();
        NetCallBack netCallBack = this.mCallBack;
        if (netCallBack != null) {
            netCallBack.onCanCell();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(NetResult netResult) {
        super.onPostExecute((BaseTask) netResult);
        NetCallBack netCallBack = this.mCallBack;
        if (netCallBack != null) {
            netCallBack.onFinish(netResult);
        }
    }
}
