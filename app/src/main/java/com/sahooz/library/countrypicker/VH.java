package com.sahooz.library.countrypicker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
class VH extends RecyclerView.ViewHolder {
    ImageView ivFlag;
    TextView tvCode;
    TextView tvName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VH(View view) {
        super(view);
        this.ivFlag = (ImageView) view.findViewById(R.id.iv_flag);
        this.tvName = (TextView) view.findViewById(R.id.tv_name);
        this.tvCode = (TextView) view.findViewById(R.id.tv_code);
    }
}
