package com.common.uitl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ListUtiles {
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isAllEmpty(List list, List list2) {
        return isEmpty(list) && isEmpty(list2);
    }

    public static int getListSize(ArrayList arrayList) {
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int i = 0;
        for (int i2 = 0; i2 < adapter.getCount(); i2++) {
            View view = adapter.getView(i2, null, listView);
            view.measure(0, 0);
            i += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = i + (listView.getDividerHeight() * (adapter.getCount() - 1));
        layoutParams.height += 5;
        listView.setLayoutParams(layoutParams);
    }
}
