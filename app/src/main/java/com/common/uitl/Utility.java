package com.common.uitl;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class Utility {
    public static void setListViewHeightBasedOnChildren(Context context, ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null || context == null) {
            return;
        }
        try {
            if (context.getResources() != null) {
                int count = (int) (adapter.getCount() * context.getResources().getDimension(R.dimen.dp_30));
                ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
                layoutParams.height = count + (listView.getDividerHeight() * (adapter.getCount() - 1));
                listView.setLayoutParams(layoutParams);
                Thread.sleep(0L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
