package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.yalantis.ucrop.R;
import com.yalantis.ucrop.callback.CropBoundsChangeListener;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;

/* loaded from: classes.dex */
public class UCropView extends FrameLayout {
    private final GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public UCropView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UCropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(context).inflate(R.layout.ucrop_view, (ViewGroup) this, true);
        GestureCropImageView gestureCropImageView = (GestureCropImageView) findViewById(R.id.image_view_crop);
        this.mGestureCropImageView = gestureCropImageView;
        OverlayView overlayView = (OverlayView) findViewById(R.id.view_overlay);
        this.mViewOverlay = overlayView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ucrop_UCropView);
        overlayView.processStyledAttributes(obtainStyledAttributes);
        gestureCropImageView.processStyledAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        gestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() { // from class: com.yalantis.ucrop.view.UCropView$$ExternalSyntheticLambda0
            @Override // com.yalantis.ucrop.callback.CropBoundsChangeListener
            public final void onCropAspectRatioChanged(float f) {
                UCropView.this.m73lambda$new$0$comyalantisucropviewUCropView(f);
            }
        });
        overlayView.setOverlayViewChangeListener(new OverlayViewChangeListener() { // from class: com.yalantis.ucrop.view.UCropView$$ExternalSyntheticLambda1
            @Override // com.yalantis.ucrop.callback.OverlayViewChangeListener
            public final void onCropRectUpdated(RectF rectF) {
                UCropView.this.m74lambda$new$1$comyalantisucropviewUCropView(rectF);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-yalantis-ucrop-view-UCropView  reason: not valid java name */
    public /* synthetic */ void m73lambda$new$0$comyalantisucropviewUCropView(float f) {
        this.mViewOverlay.setTargetAspectRatio(f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-yalantis-ucrop-view-UCropView  reason: not valid java name */
    public /* synthetic */ void m74lambda$new$1$comyalantisucropviewUCropView(RectF rectF) {
        this.mGestureCropImageView.setCropRect(rectF);
    }

    public GestureCropImageView getCropImageView() {
        return this.mGestureCropImageView;
    }

    public OverlayView getOverlayView() {
        return this.mViewOverlay;
    }
}
