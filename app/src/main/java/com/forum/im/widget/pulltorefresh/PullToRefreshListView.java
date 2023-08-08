package com.forum.im.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/* loaded from: classes.dex */
public class PullToRefreshListView extends ListView {
    boolean allowDragBottom;
    float downY;
    boolean needConsumeTouch;

    public PullToRefreshListView(Context context) {
        super(context);
        this.allowDragBottom = true;
        this.downY = 0.0f;
        this.needConsumeTouch = true;
        setDivider(null);
    }

    public PullToRefreshListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.allowDragBottom = true;
        this.downY = 0.0f;
        this.needConsumeTouch = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.downY = motionEvent.getRawY();
            this.needConsumeTouch = true;
            if (getMyScrollY() == 0) {
                this.allowDragBottom = true;
            } else {
                this.allowDragBottom = false;
            }
        } else if (motionEvent.getAction() == 2) {
            if (!this.needConsumeTouch) {
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            } else if (this.allowDragBottom && this.downY - motionEvent.getRawY() < -2.0f) {
                this.needConsumeTouch = false;
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        }
        getParent().requestDisallowInterceptTouchEvent(this.needConsumeTouch);
        return super.dispatchTouchEvent(motionEvent);
    }

    public int getMyScrollY() {
        View childAt = getChildAt(0);
        if (childAt == null) {
            return 0;
        }
        return (-childAt.getTop()) + (getFirstVisiblePosition() * childAt.getHeight());
    }
}
