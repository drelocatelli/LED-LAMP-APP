package com.squareup.picasso;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DeferredRequestCreator implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
    Callback callback;
    private final RequestCreator creator;
    final WeakReference<ImageView> target;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeferredRequestCreator(RequestCreator requestCreator, ImageView imageView, Callback callback) {
        this.creator = requestCreator;
        this.target = new WeakReference<>(imageView);
        this.callback = callback;
        imageView.addOnAttachStateChangeListener(this);
        if (imageView.getWindowToken() != null) {
            onViewAttachedToWindow(imageView);
        }
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        view.getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        view.getViewTreeObserver().removeOnPreDrawListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        ImageView imageView = this.target.get();
        if (imageView == null) {
            return true;
        }
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            if (width > 0 && height > 0) {
                imageView.removeOnAttachStateChangeListener(this);
                viewTreeObserver.removeOnPreDrawListener(this);
                this.target.clear();
                this.creator.unfit().resize(width, height).into(imageView, this.callback);
            }
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancel() {
        this.creator.clearTag();
        this.callback = null;
        ImageView imageView = this.target.get();
        if (imageView == null) {
            return;
        }
        this.target.clear();
        imageView.removeOnAttachStateChangeListener(this);
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnPreDrawListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getTag() {
        return this.creator.getTag();
    }
}
