package com.luck.picture.lib.observable;

import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ImagesObservable {
    private static ImagesObservable sObserver;
    private List<LocalMedia> previewList;

    public static ImagesObservable getInstance() {
        if (sObserver == null) {
            synchronized (ImagesObservable.class) {
                if (sObserver == null) {
                    sObserver = new ImagesObservable();
                }
            }
        }
        return sObserver;
    }

    public void savePreviewMediaData(List<LocalMedia> list) {
        this.previewList = list;
    }

    public List<LocalMedia> readPreviewMediaData() {
        if (this.previewList == null) {
            this.previewList = new ArrayList();
        }
        return this.previewList;
    }

    public void clearPreviewMediaData() {
        List<LocalMedia> list = this.previewList;
        if (list != null) {
            list.clear();
        }
    }
}
