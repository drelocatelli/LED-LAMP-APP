package com.home.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class RoundImageView extends ImageView {
    private int defaultColor;
    private int defaultHeight;
    private int defaultWidth;
    private int mBorderInsideColor;
    private int mBorderOutsideColor;
    private int mBorderThickness;
    private Context mContext;

    public RoundImageView(Context context) {
        super(context);
        this.mBorderThickness = 0;
        this.defaultColor = -1;
        this.mBorderOutsideColor = 0;
        this.mBorderInsideColor = 0;
        this.defaultWidth = 0;
        this.defaultHeight = 0;
        this.mContext = context;
    }

    public RoundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBorderThickness = 0;
        this.defaultColor = -1;
        this.mBorderOutsideColor = 0;
        this.mBorderInsideColor = 0;
        this.defaultWidth = 0;
        this.defaultHeight = 0;
        this.mContext = context;
        setCustomAttributes(attributeSet);
    }

    public RoundImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBorderThickness = 0;
        this.defaultColor = -1;
        this.mBorderOutsideColor = 0;
        this.mBorderInsideColor = 0;
        this.defaultWidth = 0;
        this.defaultHeight = 0;
        this.mContext = context;
        setCustomAttributes(attributeSet);
    }

    private void setCustomAttributes(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, R.styleable.roundedimageview);
        this.mBorderThickness = obtainStyledAttributes.getDimensionPixelSize(2, 0);
        this.mBorderOutsideColor = obtainStyledAttributes.getColor(1, this.defaultColor);
        this.mBorderInsideColor = obtainStyledAttributes.getColor(0, this.defaultColor);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        int i2;
        Drawable drawable = getDrawable();
        if (drawable == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }
        measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class) {
            return;
        }
        Bitmap copy = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        if (this.defaultWidth == 0) {
            this.defaultWidth = getWidth();
        }
        if (this.defaultHeight == 0) {
            this.defaultHeight = getHeight();
        }
        int i3 = this.mBorderInsideColor;
        int i4 = this.defaultColor;
        if (i3 != i4 && this.mBorderOutsideColor != i4) {
            int i5 = this.defaultWidth;
            int i6 = this.defaultHeight;
            if (i5 >= i6) {
                i5 = i6;
            }
            int i7 = this.mBorderThickness;
            i = (i5 / 2) - (i7 * 2);
            drawCircleBorder(canvas, (i7 / 2) + i, i3);
            int i8 = this.mBorderThickness;
            drawCircleBorder(canvas, i + i8 + (i8 / 2), this.mBorderOutsideColor);
        } else if (i3 != i4 && this.mBorderOutsideColor == i4) {
            int i9 = this.defaultWidth;
            int i10 = this.defaultHeight;
            if (i9 >= i10) {
                i9 = i10;
            }
            int i11 = this.mBorderThickness;
            i = (i9 / 2) - i11;
            drawCircleBorder(canvas, (i11 / 2) + i, i3);
        } else if (i3 == i4 && (i2 = this.mBorderOutsideColor) != i4) {
            int i12 = this.defaultWidth;
            int i13 = this.defaultHeight;
            if (i12 >= i13) {
                i12 = i13;
            }
            int i14 = this.mBorderThickness;
            i = (i12 / 2) - i14;
            drawCircleBorder(canvas, (i14 / 2) + i, i2);
        } else {
            int i15 = this.defaultWidth;
            int i16 = this.defaultHeight;
            if (i15 >= i16) {
                i15 = i16;
            }
            i = i15 / 2;
        }
        canvas.drawBitmap(getCroppedRoundBitmap(copy, i), (this.defaultWidth / 2) - i, (this.defaultHeight / 2) - i, (Paint) null);
    }

    public Bitmap getCroppedRoundBitmap(Bitmap bitmap, int i) {
        int i2 = i * 2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (height > width) {
            bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
        } else if (height < width) {
            bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
        }
        if (bitmap.getWidth() != i2 || bitmap.getHeight() != i2) {
            bitmap = Bitmap.createScaledBitmap(bitmap, i2, i2, true);
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    private void drawCircleBorder(Canvas canvas, int i, int i2) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(i2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(this.mBorderThickness);
        canvas.drawCircle(this.defaultWidth / 2, this.defaultHeight / 2, i, paint);
    }
}
