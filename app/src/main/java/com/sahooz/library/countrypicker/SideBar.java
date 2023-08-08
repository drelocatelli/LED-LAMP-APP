package com.sahooz.library.countrypicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes.dex */
public class SideBar extends View {
    private int cellHeight;
    private int cellWidth;
    private int currentIndex;
    public final ArrayList<String> indexes;
    private int letterColor;
    private int letterSize;
    private OnLetterChangeListener onLetterChangeListener;
    private Paint paint;
    private int selectColor;
    private float textHeight;

    /* loaded from: classes.dex */
    public interface OnLetterChangeListener {
        void onLetterChange(String str);

        void onReset();
    }

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SideBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        ArrayList<String> arrayList = new ArrayList<>();
        this.indexes = arrayList;
        this.currentIndex = -1;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SideBar, i, 0);
        this.letterColor = obtainStyledAttributes.getColor(R.styleable.SideBar_letterColor, ViewCompat.MEASURED_STATE_MASK);
        this.selectColor = obtainStyledAttributes.getColor(R.styleable.SideBar_selectColor, -16711681);
        this.letterSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SideBar_letterSize, 24);
        obtainStyledAttributes.recycle();
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = this.paint.getFontMetrics();
        this.textHeight = (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        arrayList.addAll(Arrays.asList(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "B", "C", "D", ExifInterface.LONGITUDE_EAST, "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", ExifInterface.LATITUDE_SOUTH, ExifInterface.GPS_DIRECTION_TRUE, "U", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, ExifInterface.LONGITUDE_WEST, "X", "Y", "Z"));
    }

    public void addIndex(String str, int i) {
        this.indexes.add(i, str);
        invalidate();
    }

    public void removeIndex(String str) {
        this.indexes.remove(str);
        invalidate();
    }

    public void setLetterSize(int i) {
        if (this.letterSize == i) {
            return;
        }
        this.letterSize = i;
        invalidate();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.cellWidth = getMeasuredWidth();
        this.cellHeight = getMeasuredHeight() / this.indexes.size();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setTextSize(this.letterSize);
        for (int i = 0; i < this.indexes.size(); i++) {
            String str = this.indexes.get(i);
            float measureText = (this.cellWidth - this.paint.measureText(str)) * 0.5f;
            int i2 = this.cellHeight;
            float f = ((i2 + this.textHeight) * 0.5f) + (i2 * i);
            if (i == this.currentIndex) {
                this.paint.setColor(this.selectColor);
            } else {
                this.paint.setColor(this.letterColor);
            }
            canvas.drawText(str, measureText, f, this.paint);
        }
    }

    public OnLetterChangeListener getOnLetterChangeListener() {
        return this.onLetterChangeListener;
    }

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        this.onLetterChangeListener = onLetterChangeListener;
    }

    public String getLetter(int i) {
        return (i < 0 || i >= this.indexes.size()) ? "" : this.indexes.get(i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x000a, code lost:
        if (r0 != 2) goto L6;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnLetterChangeListener onLetterChangeListener;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                this.currentIndex = -1;
                invalidate();
                OnLetterChangeListener onLetterChangeListener2 = this.onLetterChangeListener;
                if (onLetterChangeListener2 != null) {
                    onLetterChangeListener2.onReset();
                }
            }
            return true;
        }
        int y = ((int) motionEvent.getY()) / this.cellHeight;
        this.currentIndex = y;
        if (y >= 0 && y <= this.indexes.size() - 1 && (onLetterChangeListener = this.onLetterChangeListener) != null) {
            onLetterChangeListener.onLetterChange(this.indexes.get(this.currentIndex));
        }
        invalidate();
        return true;
    }
}
