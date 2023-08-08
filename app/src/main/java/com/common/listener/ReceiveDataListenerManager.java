package com.common.listener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class ReceiveDataListenerManager {
    public static ReceiveDataListenerManager listenerManager;
    private List<ReceiveDataListener> iListenerList = new CopyOnWriteArrayList();

    public static ReceiveDataListenerManager getInstance() {
        if (listenerManager == null) {
            listenerManager = new ReceiveDataListenerManager();
        }
        return listenerManager;
    }

    public void registerListtener(ReceiveDataListener receiveDataListener) {
        this.iListenerList.add(receiveDataListener);
    }

    public void unRegisterListener(ReceiveDataListener receiveDataListener) {
        if (this.iListenerList.contains(receiveDataListener)) {
            this.iListenerList.remove(receiveDataListener);
        }
    }

    public void sendBroadCast(String str, String str2) {
        for (ReceiveDataListener receiveDataListener : this.iListenerList) {
            receiveDataListener.notifyReceiveData(str, str2);
        }
    }
}
