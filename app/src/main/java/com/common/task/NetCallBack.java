package com.common.task;

import com.common.net.NetResult;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class NetCallBack {
    public void onCanCell() {
    }

    public abstract NetResult onDoInBack(HashMap<String, String> hashMap);

    public abstract void onFinish(NetResult netResult);

    public void onPreCall() {
    }
}
