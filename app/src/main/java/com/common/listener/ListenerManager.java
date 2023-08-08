package com.common.listener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class ListenerManager {
    public static ListenerManager listenerManager;
    private List<IListener> iListenerList = new CopyOnWriteArrayList();

    public static ListenerManager getInstance() {
        if (listenerManager == null) {
            listenerManager = new ListenerManager();
        }
        return listenerManager;
    }

    public void registerListtener(IListener iListener) {
        this.iListenerList.add(iListener);
    }

    public void unRegisterListener(IListener iListener) {
        if (this.iListenerList.contains(iListener)) {
            this.iListenerList.remove(iListener);
        }
    }

    public void sendBroadCast(String str) {
        for (IListener iListener : this.iListenerList) {
            iListener.notifyAllActivity(str);
        }
    }
}
