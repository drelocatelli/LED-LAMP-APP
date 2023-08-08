package com.home.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.List;

/* loaded from: classes.dex */
public class ManageFragment {
    public static void showFragment(FragmentManager fragmentManager, List<Fragment> list, int i) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        for (int i2 = 0; i2 < list.size(); i2++) {
            Fragment fragment = list.get(i2);
            if (i2 == i) {
                beginTransaction.show(fragment);
            } else {
                beginTransaction.hide(fragment);
            }
        }
        beginTransaction.commit();
    }
}
