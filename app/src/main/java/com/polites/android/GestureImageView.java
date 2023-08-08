package com.polites.android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.home.utils.Utils;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class GestureImageView extends ImageView {
    private static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType = null;
    public static final String GLOBAL_NS = "http://schemas.android.com/apk/res/android";
    public static final String LOCAL_NS = "http://schemas.polites.com/android";
    private int alpha;
    private Animator animator;
    private float centerX;
    private float centerY;
    private ColorFilter colorFilter;
    private View.OnTouchListener customOnTouchListener;
    private int deviceOrientation;
    private int displayHeight;
    private int displayWidth;
    private final Semaphore drawLock;
    private Drawable drawable;
    private float fitScaleHorizontal;
    private float fitScaleVertical;
    private GestureImageViewListener gestureImageViewListener;
    private GestureImageViewTouchListener gestureImageViewTouchListener;
    private int hHeight;
    private int hWidth;
    private int imageOrientation;
    private boolean layout;
    private float maxScale;
    private float minScale;
    private View.OnClickListener onClickListener;
    private boolean recycle;
    private int resId;
    private float rotation;
    private float scale;
    private float scaleAdjust;
    private Float startX;
    private Float startY;
    private float startingScale;
    private boolean strict;
    private float x;
    private float y;

    static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType() {
        int[] iArr = $SWITCH_TABLE$android$widget$ImageView$ScaleType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ImageView.ScaleType.values().length];
        try {
            iArr2[ImageView.ScaleType.CENTER.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ImageView.ScaleType.CENTER_CROP.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ImageView.ScaleType.CENTER_INSIDE.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ImageView.ScaleType.FIT_CENTER.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ImageView.ScaleType.FIT_END.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ImageView.ScaleType.FIT_START.ordinal()] = 6;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[ImageView.ScaleType.FIT_XY.ordinal()] = 7;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[ImageView.ScaleType.MATRIX.ordinal()] = 8;
        } catch (NoSuchFieldError unused8) {
        }
        $SWITCH_TABLE$android$widget$ImageView$ScaleType = iArr2;
        return iArr2;
    }

    public GestureImageView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet);
    }

    public GestureImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.drawLock = new Semaphore(0);
        this.x = 0.0f;
        this.y = 0.0f;
        this.layout = false;
        this.scaleAdjust = 1.0f;
        this.startingScale = -1.0f;
        this.scale = 1.0f;
        this.maxScale = 5.0f;
        this.minScale = 0.75f;
        this.fitScaleHorizontal = 1.0f;
        this.fitScaleVertical = 1.0f;
        this.rotation = 0.0f;
        this.resId = -1;
        this.recycle = false;
        this.strict = false;
        this.alpha = 255;
        this.deviceOrientation = -1;
        String attributeValue = attributeSet.getAttributeValue(GLOBAL_NS, "scaleType");
        if (attributeValue == null || attributeValue.trim().length() == 0) {
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        String attributeValue2 = attributeSet.getAttributeValue(LOCAL_NS, "start-x");
        String attributeValue3 = attributeSet.getAttributeValue(LOCAL_NS, "start-y");
        if (attributeValue2 != null && attributeValue2.trim().length() > 0) {
            this.startX = Float.valueOf(Float.parseFloat(attributeValue2));
        }
        if (attributeValue3 != null && attributeValue3.trim().length() > 0) {
            this.startY = Float.valueOf(Float.parseFloat(attributeValue3));
        }
        setStartingScale(attributeSet.getAttributeFloatValue(LOCAL_NS, "start-scale", this.startingScale));
        setMinScale(attributeSet.getAttributeFloatValue(LOCAL_NS, "min-scale", this.minScale));
        setMaxScale(attributeSet.getAttributeFloatValue(LOCAL_NS, "max-scale", this.maxScale));
        setStrict(attributeSet.getAttributeBooleanValue(LOCAL_NS, "strict", this.strict));
        setRecycle(attributeSet.getAttributeBooleanValue(LOCAL_NS, "recycle", this.recycle));
        initImage();
    }

    public GestureImageView(Context context) {
        super(context);
        this.drawLock = new Semaphore(0);
        this.x = 0.0f;
        this.y = 0.0f;
        this.layout = false;
        this.scaleAdjust = 1.0f;
        this.startingScale = -1.0f;
        this.scale = 1.0f;
        this.maxScale = 5.0f;
        this.minScale = 0.75f;
        this.fitScaleHorizontal = 1.0f;
        this.fitScaleVertical = 1.0f;
        this.rotation = 0.0f;
        this.resId = -1;
        this.recycle = false;
        this.strict = false;
        this.alpha = 255;
        this.deviceOrientation = -1;
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        initImage();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.drawable != null) {
            if (getResources().getConfiguration().orientation == 2) {
                this.displayHeight = View.MeasureSpec.getSize(i2);
                if (getLayoutParams().width == -2) {
                    this.displayWidth = Math.round(this.displayHeight * (getImageWidth() / getImageHeight()));
                } else {
                    this.displayWidth = View.MeasureSpec.getSize(i);
                }
            } else {
                this.displayWidth = View.MeasureSpec.getSize(i);
                if (getLayoutParams().height == -2) {
                    this.displayHeight = Math.round(this.displayWidth * (getImageHeight() / getImageWidth()));
                } else {
                    this.displayHeight = View.MeasureSpec.getSize(i2);
                }
            }
        } else {
            this.displayHeight = View.MeasureSpec.getSize(i2);
            this.displayWidth = View.MeasureSpec.getSize(i);
        }
        setMeasuredDimension(this.displayWidth, this.displayHeight);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z || !this.layout) {
            setupCanvas(this.displayWidth, this.displayHeight, getResources().getConfiguration().orientation);
        }
    }

    protected void setupCanvas(int i, int i2, int i3) {
        if (this.deviceOrientation != i3) {
            this.layout = false;
            this.deviceOrientation = i3;
        }
        if (this.drawable == null || this.layout) {
            return;
        }
        int imageWidth = getImageWidth();
        int imageHeight = getImageHeight();
        this.hWidth = Math.round(imageWidth / 2.0f);
        this.hHeight = Math.round(imageHeight / 2.0f);
        int paddingLeft = i - (getPaddingLeft() + getPaddingRight());
        int paddingTop = i2 - (getPaddingTop() + getPaddingBottom());
        computeCropScale(imageWidth, imageHeight, paddingLeft, paddingTop);
        if (this.startingScale <= 0.0f) {
            computeStartingScale(imageWidth, imageHeight, paddingLeft, paddingTop);
        }
        this.scaleAdjust = this.startingScale;
        float f = paddingLeft / 2.0f;
        this.centerX = f;
        this.centerY = paddingTop / 2.0f;
        Float f2 = this.startX;
        if (f2 == null) {
            this.x = f;
        } else {
            this.x = f2.floatValue();
        }
        Float f3 = this.startY;
        if (f3 == null) {
            this.y = this.centerY;
        } else {
            this.y = f3.floatValue();
        }
        this.gestureImageViewTouchListener = new GestureImageViewTouchListener(this, paddingLeft, paddingTop);
        if (isLandscape()) {
            this.gestureImageViewTouchListener.setMinScale(this.minScale * this.fitScaleHorizontal);
        } else {
            this.gestureImageViewTouchListener.setMinScale(this.minScale * this.fitScaleVertical);
        }
        this.gestureImageViewTouchListener.setMaxScale(this.maxScale * this.startingScale);
        this.gestureImageViewTouchListener.setFitScaleHorizontal(this.fitScaleHorizontal);
        this.gestureImageViewTouchListener.setFitScaleVertical(this.fitScaleVertical);
        this.gestureImageViewTouchListener.setCanvasWidth(paddingLeft);
        this.gestureImageViewTouchListener.setCanvasHeight(paddingTop);
        this.gestureImageViewTouchListener.setOnClickListener(this.onClickListener);
        Drawable drawable = this.drawable;
        int i4 = this.hWidth;
        int i5 = this.hHeight;
        drawable.setBounds(-i4, -i5, i4, i5);
        super.setOnTouchListener(new View.OnTouchListener() { // from class: com.polites.android.GestureImageView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (GestureImageView.this.customOnTouchListener != null) {
                    GestureImageView.this.customOnTouchListener.onTouch(view, motionEvent);
                }
                return GestureImageView.this.gestureImageViewTouchListener.onTouch(view, motionEvent);
            }
        });
        this.layout = true;
    }

    protected void computeCropScale(int i, int i2, int i3, int i4) {
        this.fitScaleHorizontal = i3 / i;
        this.fitScaleVertical = i4 / i2;
    }

    protected void computeStartingScale(int i, int i2, int i3, int i4) {
        int i5 = $SWITCH_TABLE$android$widget$ImageView$ScaleType()[getScaleType().ordinal()];
        if (i5 == 1) {
            this.startingScale = 1.0f;
        } else if (i5 == 2) {
            this.startingScale = Math.max(i4 / i2, i3 / i);
        } else if (i5 != 3) {
        } else {
            if (i / i3 > i2 / i4) {
                this.startingScale = this.fitScaleHorizontal;
            } else {
                this.startingScale = this.fitScaleVertical;
            }
        }
    }

    protected boolean isRecycled() {
        Bitmap bitmap;
        Drawable drawable = this.drawable;
        if (drawable == null || !(drawable instanceof BitmapDrawable) || (bitmap = ((BitmapDrawable) drawable).getBitmap()) == null) {
            return false;
        }
        return bitmap.isRecycled();
    }

    protected void recycle() {
        Drawable drawable;
        Bitmap bitmap;
        if (!this.recycle || (drawable = this.drawable) == null || !(drawable instanceof BitmapDrawable) || (bitmap = ((BitmapDrawable) drawable).getBitmap()) == null) {
            return;
        }
        bitmap.recycle();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.layout) {
            if (this.drawable != null && !isRecycled()) {
                canvas.save();
                float f = this.scale * this.scaleAdjust;
                canvas.translate(this.x, this.y);
                float f2 = this.rotation;
                if (f2 != 0.0f) {
                    canvas.rotate(f2);
                }
                if (f != 1.0f) {
                    canvas.scale(f, f);
                }
                this.drawable.draw(canvas);
                canvas.restore();
            }
            if (this.drawLock.availablePermits() <= 0) {
                this.drawLock.release();
            }
        }
    }

    public boolean waitForDraw(long j) throws InterruptedException {
        return this.drawLock.tryAcquire(j, TimeUnit.MILLISECONDS);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        Animator animator = new Animator(this, "GestureImageViewAnimator");
        this.animator = animator;
        animator.start();
        int i = this.resId;
        if (i >= 0 && this.drawable == null) {
            setImageResource(i);
        }
        super.onAttachedToWindow();
    }

    public void animationStart(Animation animation) {
        Animator animator = this.animator;
        if (animator != null) {
            animator.play(animation);
        }
    }

    public void animationStop() {
        Animator animator = this.animator;
        if (animator != null) {
            animator.cancel();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        Animator animator = this.animator;
        if (animator != null) {
            animator.finish();
        }
        if (this.recycle && this.drawable != null && !isRecycled()) {
            recycle();
            this.drawable = null;
        }
        super.onDetachedFromWindow();
    }

    protected void initImage() {
        Drawable drawable = this.drawable;
        if (drawable != null) {
            drawable.setAlpha(this.alpha);
            this.drawable.setFilterBitmap(true);
            ColorFilter colorFilter = this.colorFilter;
            if (colorFilter != null) {
                this.drawable.setColorFilter(colorFilter);
            }
        }
        if (this.layout) {
            return;
        }
        requestLayout();
        redraw();
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        this.drawable = new BitmapDrawable(getResources(), bitmap);
        initImage();
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        this.drawable = drawable;
        initImage();
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        if (this.drawable != null) {
            recycle();
        }
        if (i >= 0) {
            this.resId = i;
            setImageDrawable(getContext().getResources().getDrawable(i));
        }
    }

    public int getScaledWidth() {
        return Math.round(getImageWidth() * getScale());
    }

    public int getScaledHeight() {
        return Math.round(getImageHeight() * getScale());
    }

    public int getImageWidth() {
        Drawable drawable = this.drawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return 0;
    }

    public int getImageHeight() {
        Drawable drawable = this.drawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return 0;
    }

    public void moveBy(float f, float f2) {
        this.x += f;
        this.y += f2;
    }

    public void setPosition(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void redraw() {
        postInvalidate();
    }

    public void setMinScale(float f) {
        this.minScale = f;
        GestureImageViewTouchListener gestureImageViewTouchListener = this.gestureImageViewTouchListener;
        if (gestureImageViewTouchListener != null) {
            gestureImageViewTouchListener.setMinScale(f * this.fitScaleHorizontal);
        }
    }

    public void setMaxScale(float f) {
        this.maxScale = f;
        GestureImageViewTouchListener gestureImageViewTouchListener = this.gestureImageViewTouchListener;
        if (gestureImageViewTouchListener != null) {
            gestureImageViewTouchListener.setMaxScale(f * this.startingScale);
        }
    }

    public void setScale(float f) {
        this.scaleAdjust = f;
    }

    public float getScale() {
        return this.scaleAdjust;
    }

    public float getImageX() {
        return this.x;
    }

    public float getImageY() {
        return this.y;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public void setStrict(boolean z) {
        this.strict = z;
    }

    public boolean isRecycle() {
        return this.recycle;
    }

    public void setRecycle(boolean z) {
        this.recycle = z;
    }

    public void reset() {
        this.x = this.centerX;
        this.y = this.centerY;
        this.scaleAdjust = this.startingScale;
        GestureImageViewTouchListener gestureImageViewTouchListener = this.gestureImageViewTouchListener;
        if (gestureImageViewTouchListener != null) {
            gestureImageViewTouchListener.reset();
        }
        redraw();
    }

    @Override // android.view.View
    public void setRotation(float f) {
        this.rotation = f;
    }

    public void setGestureImageViewListener(GestureImageViewListener gestureImageViewListener) {
        this.gestureImageViewListener = gestureImageViewListener;
    }

    public GestureImageViewListener getGestureImageViewListener() {
        return this.gestureImageViewListener;
    }

    @Override // android.widget.ImageView
    public Drawable getDrawable() {
        return this.drawable;
    }

    @Override // android.widget.ImageView
    public void setAlpha(int i) {
        this.alpha = i;
        Drawable drawable = this.drawable;
        if (drawable != null) {
            drawable.setAlpha(i);
        }
    }

    @Override // android.widget.ImageView
    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
        Drawable drawable = this.drawable;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        if (Utils.RESPONSE_CONTENT.equals(uri.getScheme())) {
            try {
                String[] strArr = {"orientation"};
                Cursor query = getContext().getContentResolver().query(uri, strArr, null, null, null);
                if (query != null && query.moveToFirst()) {
                    this.imageOrientation = query.getInt(query.getColumnIndex(strArr[0]));
                }
                InputStream openInputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream);
                if (this.imageOrientation != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(this.imageOrientation);
                    Bitmap createBitmap = Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
                    decodeStream.recycle();
                    setImageDrawable(new BitmapDrawable(getResources(), createBitmap));
                } else {
                    setImageDrawable(new BitmapDrawable(getResources(), decodeStream));
                }
                if (openInputStream != null) {
                    openInputStream.close();
                }
                if (query != null) {
                    query.close();
                }
            } catch (Exception e) {
                Log.w("GestureImageView", "Unable to open content: " + uri, e);
            }
        } else {
            setImageDrawable(Drawable.createFromPath(uri.toString()));
        }
        if (this.drawable == null) {
            Log.e("GestureImageView", "resolveUri failed on bad bitmap uri: " + uri);
        }
    }

    @Override // android.widget.ImageView
    public Matrix getImageMatrix() {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        return super.getImageMatrix();
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == ImageView.ScaleType.CENTER || scaleType == ImageView.ScaleType.CENTER_CROP || scaleType == ImageView.ScaleType.CENTER_INSIDE) {
            super.setScaleType(scaleType);
        } else if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    @Override // android.widget.ImageView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.invalidateDrawable(drawable);
    }

    @Override // android.widget.ImageView, android.view.View
    public int[] onCreateDrawableState(int i) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        return super.onCreateDrawableState(i);
    }

    @Override // android.widget.ImageView
    public void setAdjustViewBounds(boolean z) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.setAdjustViewBounds(z);
    }

    @Override // android.widget.ImageView
    public void setImageLevel(int i) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.setImageLevel(i);
    }

    @Override // android.widget.ImageView
    public void setImageMatrix(Matrix matrix) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    @Override // android.widget.ImageView
    public void setImageState(int[] iArr, boolean z) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void setSelected(boolean z) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.setSelected(z);
    }

    @Override // android.view.View
    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.customOnTouchListener = onTouchListener;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public boolean isLandscape() {
        return getImageWidth() >= getImageHeight();
    }

    public boolean isPortrait() {
        return getImageWidth() <= getImageHeight();
    }

    public void setStartingScale(float f) {
        this.startingScale = f;
    }

    public void setStartingPosition(float f, float f2) {
        this.startX = Float.valueOf(f);
        this.startY = Float.valueOf(f2);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        GestureImageViewTouchListener gestureImageViewTouchListener = this.gestureImageViewTouchListener;
        if (gestureImageViewTouchListener != null) {
            gestureImageViewTouchListener.setOnClickListener(onClickListener);
        }
    }

    public boolean isOrientationAligned() {
        int i = this.deviceOrientation;
        if (i == 2) {
            return isLandscape();
        }
        if (i == 1) {
            return isPortrait();
        }
        return true;
    }

    public int getDeviceOrientation() {
        return this.deviceOrientation;
    }
}
