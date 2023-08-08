package com.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;
import com.common.net.NetResult;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class Segment extends View {
    private Drawable[] bgDrawable;
    private int index;
    private int normal_color;
    private OnSegMentClickListener onSegMentClickListener;
    private Paint paint;
    private int pic_height;
    private int pic_width;
    private int press_color;
    private int seg_text_size;
    private String[] tags;
    private String[] text;

    /* loaded from: classes.dex */
    public interface OnSegMentClickListener {
        void onSelect(int i, String str, String str2);
    }

    public Segment(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.index = 2;
        this.paint = null;
        this.pic_width = 0;
        this.pic_height = 1;
        this.bgDrawable = new Drawable[3];
        this.tags = new String[]{NetResult.CODE_OK, "1", ExifInterface.GPS_MEASUREMENT_2D};
        this.text = new String[]{"进港", "出港", "进出港"};
        this.seg_text_size = 20;
        this.normal_color = 8291982;
        this.press_color = ViewCompat.MEASURED_SIZE_MASK;
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.seg_text_size = (int) context.getResources().getDimension(R.dimen.label_text_size_third);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Segment);
        int dimension = (int) obtainStyledAttributes.getDimension(8, this.seg_text_size);
        this.seg_text_size = dimension;
        this.paint.setTextSize(dimension);
        this.text[0] = obtainStyledAttributes.getString(5);
        this.text[1] = obtainStyledAttributes.getString(6);
        this.text[2] = obtainStyledAttributes.getString(7);
        this.normal_color = obtainStyledAttributes.getColor(4, this.normal_color);
        this.press_color = obtainStyledAttributes.getColor(3, this.press_color);
        this.bgDrawable[0] = obtainStyledAttributes.getDrawable(0);
        this.bgDrawable[1] = obtainStyledAttributes.getDrawable(1);
        this.bgDrawable[2] = obtainStyledAttributes.getDrawable(2);
        this.pic_width = this.bgDrawable[2].getIntrinsicWidth();
        this.pic_height = this.bgDrawable[2].getIntrinsicHeight();
        obtainStyledAttributes.recycle();
        refresh();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(this.pic_width, this.pic_height);
    }

    public OnSegMentClickListener getOnSegMentClickListener() {
        return this.onSegMentClickListener;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnSegMentClickListener onSegMentClickListener;
        int action = motionEvent.getAction();
        int width = getWidth();
        float x = motionEvent.getX();
        int i = width / 3;
        int i2 = i * 2;
        if (action == 0) {
            float f = i;
            if (x <= f) {
                this.index = 0;
            }
            if (x > f && x < i2) {
                this.index = 1;
            }
            if (x > i2) {
                this.index = 2;
            }
            refresh();
            return true;
        }
        if (action == 1 && (onSegMentClickListener = this.onSegMentClickListener) != null) {
            int i3 = this.index;
            onSegMentClickListener.onSelect(i3, this.tags[i3], this.text[i3]);
        }
        refresh();
        return super.onTouchEvent(motionEvent);
    }

    public void setIndex(int i) {
        this.index = i;
        refresh();
    }

    private void refresh() {
        setBackgroundDrawable(this.bgDrawable[this.index]);
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Rect rect = new Rect();
        Paint paint = this.paint;
        String[] strArr = this.text;
        int i = 0;
        paint.getTextBounds(strArr[0], 0, strArr[0].length(), rect);
        int i2 = rect.bottom - rect.top;
        int i3 = ((height / 2) + (i2 / 2)) - (i2 / 5);
        while (i < this.text.length) {
            this.paint.setColor(i == this.index ? this.press_color : this.normal_color);
            int i4 = width / 3;
            canvas.drawText(this.text[i], (int) (((i4 / 2) - (this.paint.measureText(this.text[i]) / 2.0f)) + (i4 * i)), i3, this.paint);
            i++;
        }
    }

    public String[] getTags() {
        return this.tags;
    }

    public void setTags(String[] strArr) {
        this.tags = strArr;
    }

    public int getIndex() {
        return this.index;
    }

    public int getFontHeight(float f) {
        this.paint.setTextSize(f);
        Paint.FontMetrics fontMetrics = this.paint.getFontMetrics();
        return ((int) Math.ceil(fontMetrics.descent - fontMetrics.top)) + 2;
    }

    public void setOnSegMentClickListener(OnSegMentClickListener onSegMentClickListener) {
        this.onSegMentClickListener = onSegMentClickListener;
    }
}
