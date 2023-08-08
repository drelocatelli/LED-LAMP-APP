package com.forum.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.forum.im.widget.HeadIconSelectorView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class ChatBottomView extends LinearLayout {
    public static final int FROM_CAMERA = 1;
    public static final int FROM_GALLERY = 2;
    public static final int FROM_PHRASE = 3;
    private View baseView;
    private LinearLayout cameraGroup;
    private LinearLayout imageGroup;
    private HeadIconSelectorView.OnHeadIconClickListener onHeadIconClickListener;
    private LinearLayout phraseGroup;

    public ChatBottomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        findView();
        init();
    }

    private void findView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_tongbaobottom, this);
        this.baseView = inflate;
        this.imageGroup = (LinearLayout) inflate.findViewById(R.id.image_bottom_group);
        this.cameraGroup = (LinearLayout) this.baseView.findViewById(R.id.camera_group);
        this.phraseGroup = (LinearLayout) this.baseView.findViewById(R.id.phrase_group);
    }

    private void init() {
        this.cameraGroup.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.widget.ChatBottomView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ChatBottomView.this.onHeadIconClickListener != null) {
                    ChatBottomView.this.onHeadIconClickListener.onClick(1);
                }
            }
        });
        this.imageGroup.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.widget.ChatBottomView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ChatBottomView.this.onHeadIconClickListener != null) {
                    ChatBottomView.this.onHeadIconClickListener.onClick(2);
                }
            }
        });
        this.phraseGroup.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.widget.ChatBottomView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ChatBottomView.this.onHeadIconClickListener != null) {
                    ChatBottomView.this.onHeadIconClickListener.onClick(3);
                }
            }
        });
    }

    public void setOnHeadIconClickListener(HeadIconSelectorView.OnHeadIconClickListener onHeadIconClickListener) {
        this.onHeadIconClickListener = onHeadIconClickListener;
    }
}
