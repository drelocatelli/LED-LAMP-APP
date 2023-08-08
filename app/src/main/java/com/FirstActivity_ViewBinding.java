package com;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class FirstActivity_ViewBinding implements Unbinder {
    private FirstActivity target;
    private View view7f09056c;
    private View view7f0905a7;
    private View view7f0905c4;
    private View view7f0905dc;
    private View view7f090605;

    public FirstActivity_ViewBinding(FirstActivity firstActivity) {
        this(firstActivity, firstActivity.getWindow().getDecorView());
    }

    public FirstActivity_ViewBinding(final FirstActivity firstActivity, View view) {
        this.target = firstActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.tvOther, "method 'onClick'");
        this.view7f0905dc = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: com.FirstActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                firstActivity.onClick(view2);
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.tvConnect, "method 'onClick'");
        this.view7f0905a7 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: com.FirstActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                firstActivity.onClick(view2);
            }
        });
        View findRequiredView3 = Utils.findRequiredView(view, R.id.tvAdvisory, "method 'onClick'");
        this.view7f09056c = findRequiredView3;
        findRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: com.FirstActivity_ViewBinding.3
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                firstActivity.onClick(view2);
            }
        });
        View findRequiredView4 = Utils.findRequiredView(view, R.id.tvSuggest, "method 'onClick'");
        this.view7f090605 = findRequiredView4;
        findRequiredView4.setOnClickListener(new DebouncingOnClickListener() { // from class: com.FirstActivity_ViewBinding.4
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                firstActivity.onClick(view2);
            }
        });
        View findRequiredView5 = Utils.findRequiredView(view, R.id.tvHot, "method 'onClick'");
        this.view7f0905c4 = findRequiredView5;
        findRequiredView5.setOnClickListener(new DebouncingOnClickListener() { // from class: com.FirstActivity_ViewBinding.5
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                firstActivity.onClick(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        if (this.target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        this.view7f0905dc.setOnClickListener(null);
        this.view7f0905dc = null;
        this.view7f0905a7.setOnClickListener(null);
        this.view7f0905a7 = null;
        this.view7f09056c.setOnClickListener(null);
        this.view7f09056c = null;
        this.view7f090605.setOnClickListener(null);
        this.view7f090605 = null;
        this.view7f0905c4.setOnClickListener(null);
        this.view7f0905c4 = null;
    }
}
