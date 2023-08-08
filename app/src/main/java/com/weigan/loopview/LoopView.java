package com.weigan.loopview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class LoopView extends View {
    private static final float DEFAULT_LINE_SPACE = 1.0f;
    private static final int DEFAULT_TEXT_SIZE = (int) (Resources.getSystem().getDisplayMetrics().density * 15.0f);
    private static final int DEFAULT_VISIBIE_ITEMS = 9;
    public static final int SCROLL_STATE_DRAGGING = 2;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SCROLLING = 3;
    public static final int SCROLL_STATE_SETTING = 1;
    int centerTextColor;
    int change;
    private Context context;
    int currentScrollState;
    int dividerColor;
    HashMap<Integer, IndexString> drawingStrings;
    int firstLineY;
    private GestureDetector flingGestureDetector;
    int halfCircumference;
    Handler handler;
    int initPosition;
    boolean isLoop;
    int itemTextHeight;
    List<IndexString> items;
    int itemsVisibleCount;
    int lastScrollState;
    float lineSpacingMultiplier;
    ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mFuture;
    private int mOffset;
    OnItemScrollListener mOnItemScrollListener;
    int measuredHeight;
    int measuredWidth;
    OnItemSelectedListener onItemSelectedListener;
    int outerTextColor;
    private int paddingLeft;
    private int paddingRight;
    private Paint paintCenterText;
    private Paint paintIndicator;
    private Paint paintOuterText;
    int preCurrentIndex;
    private float previousY;
    int radius;
    private float scaleX;
    int secondLineY;
    long startTime;
    private Rect tempRect;
    int textHeight;
    int textSize;
    int totalScrollY;
    private Typeface typeface;

    /* loaded from: classes.dex */
    public enum ACTION {
        CLICK,
        FLING,
        DRAG
    }

    public void setLineSpacingMultiplier(float f) {
        if (f > 1.0f) {
            this.lineSpacingMultiplier = f;
        }
    }

    public void setCenterTextColor(int i) {
        this.centerTextColor = i;
        Paint paint = this.paintCenterText;
        if (paint != null) {
            paint.setColor(i);
        }
    }

    public void setOuterTextColor(int i) {
        this.outerTextColor = i;
        Paint paint = this.paintOuterText;
        if (paint != null) {
            paint.setColor(i);
        }
    }

    public void setDividerColor(int i) {
        this.dividerColor = i;
        Paint paint = this.paintIndicator;
        if (paint != null) {
            paint.setColor(i);
        }
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public LoopView(Context context) {
        super(context);
        this.scaleX = 1.05f;
        this.lastScrollState = 0;
        this.currentScrollState = 1;
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mOffset = 0;
        this.startTime = 0L;
        this.tempRect = new Rect();
        this.typeface = Typeface.MONOSPACE;
        initLoopView(context, null);
    }

    public LoopView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.scaleX = 1.05f;
        this.lastScrollState = 0;
        this.currentScrollState = 1;
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mOffset = 0;
        this.startTime = 0L;
        this.tempRect = new Rect();
        this.typeface = Typeface.MONOSPACE;
        initLoopView(context, attributeSet);
    }

    public LoopView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.scaleX = 1.05f;
        this.lastScrollState = 0;
        this.currentScrollState = 1;
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mOffset = 0;
        this.startTime = 0L;
        this.tempRect = new Rect();
        this.typeface = Typeface.MONOSPACE;
        initLoopView(context, attributeSet);
    }

    private void initLoopView(Context context, AttributeSet attributeSet) {
        this.context = context;
        this.handler = new MessageHandler(this);
        GestureDetector gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        this.flingGestureDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LoopView);
        if (obtainStyledAttributes != null) {
            this.textSize = obtainStyledAttributes.getInteger(R.styleable.LoopView_awv_textsize, DEFAULT_TEXT_SIZE);
            this.textSize = (int) (Resources.getSystem().getDisplayMetrics().density * this.textSize);
            this.lineSpacingMultiplier = obtainStyledAttributes.getFloat(R.styleable.LoopView_awv_lineSpace, 1.0f);
            this.centerTextColor = obtainStyledAttributes.getInteger(R.styleable.LoopView_awv_centerTextColor, -13553359);
            this.outerTextColor = obtainStyledAttributes.getInteger(R.styleable.LoopView_awv_outerTextColor, -5263441);
            this.dividerColor = obtainStyledAttributes.getInteger(R.styleable.LoopView_awv_dividerTextColor, -3815995);
            int integer = obtainStyledAttributes.getInteger(R.styleable.LoopView_awv_itemsVisibleCount, 9);
            this.itemsVisibleCount = integer;
            if (integer % 2 == 0) {
                this.itemsVisibleCount = 9;
            }
            this.isLoop = obtainStyledAttributes.getBoolean(R.styleable.LoopView_awv_isLoop, true);
            obtainStyledAttributes.recycle();
        }
        this.drawingStrings = new HashMap<>();
        this.totalScrollY = 0;
        this.initPosition = -1;
    }

    public void setItemsVisibleCount(int i) {
        if (i % 2 == 0 || i == this.itemsVisibleCount) {
            return;
        }
        this.itemsVisibleCount = i;
        this.drawingStrings = new HashMap<>();
    }

    private void initPaintsIfPossible() {
        if (this.paintOuterText == null) {
            Paint paint = new Paint();
            this.paintOuterText = paint;
            paint.setColor(this.outerTextColor);
            this.paintOuterText.setAntiAlias(true);
            this.paintOuterText.setTypeface(this.typeface);
            this.paintOuterText.setTextSize(this.textSize);
        }
        if (this.paintCenterText == null) {
            Paint paint2 = new Paint();
            this.paintCenterText = paint2;
            paint2.setColor(this.centerTextColor);
            this.paintCenterText.setAntiAlias(true);
            this.paintCenterText.setTextScaleX(this.scaleX);
            this.paintCenterText.setTypeface(this.typeface);
            this.paintCenterText.setTextSize(this.textSize);
        }
        if (this.paintIndicator == null) {
            Paint paint3 = new Paint();
            this.paintIndicator = paint3;
            paint3.setColor(this.dividerColor);
            this.paintIndicator.setAntiAlias(true);
        }
    }

    private void remeasure() {
        List<IndexString> list = this.items;
        if (list == null || list.isEmpty()) {
            return;
        }
        this.measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        this.measuredHeight = measuredHeight;
        if (this.measuredWidth == 0 || measuredHeight == 0) {
            return;
        }
        this.paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        this.paddingRight = paddingRight;
        this.measuredWidth -= paddingRight;
        this.paintCenterText.getTextBounds("星期", 0, 2, this.tempRect);
        this.textHeight = this.tempRect.height();
        int i = this.measuredHeight;
        double d = i;
        Double.isNaN(d);
        int i2 = (int) ((d * 3.141592653589793d) / 2.0d);
        this.halfCircumference = i2;
        float f = this.lineSpacingMultiplier;
        int i3 = (int) (i2 / ((this.itemsVisibleCount - 1) * f));
        this.itemTextHeight = i3;
        this.radius = i / 2;
        this.firstLineY = (int) ((i - (i3 * f)) / 2.0f);
        this.secondLineY = (int) ((i + (f * i3)) / 2.0f);
        if (this.initPosition == -1) {
            if (this.isLoop) {
                this.initPosition = (this.items.size() + 1) / 2;
            } else {
                this.initPosition = 0;
            }
        }
        this.preCurrentIndex = this.initPosition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DRAG) {
            float f = this.lineSpacingMultiplier * this.itemTextHeight;
            int i = (int) (((this.totalScrollY % f) + f) % f);
            this.mOffset = i;
            if (i > f / 2.0f) {
                this.mOffset = (int) (f - i);
            } else {
                this.mOffset = -i;
            }
        }
        this.mFuture = this.mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, this.mOffset), 0L, 10L, TimeUnit.MILLISECONDS);
        changeScrollState(3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void scrollBy(float f) {
        cancelFuture();
        this.mFuture = this.mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, f), 0L, 10, TimeUnit.MILLISECONDS);
        changeScrollState(2);
    }

    public void cancelFuture() {
        ScheduledFuture<?> scheduledFuture = this.mFuture;
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            return;
        }
        this.mFuture.cancel(true);
        this.mFuture = null;
        changeScrollState(0);
    }

    private void printMethodStackTrace(String str) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder("printMethodStackTrace ");
        sb.append(str);
        sb.append(" ");
        for (int length = stackTrace.length - 1; length >= 4; length--) {
            StackTraceElement stackTraceElement = stackTrace[length];
            sb.append(String.format("%s(%d).%s", stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), stackTraceElement.getMethodName()));
            if (length > 4) {
                sb.append("-->");
            }
        }
        Log.i("printMethodStackTrace", sb.toString());
    }

    private void changeScrollState(int i) {
        if (i == this.currentScrollState || this.handler.hasMessages(MessageHandler.WHAT_SMOOTH_SCROLL_INERTIA)) {
            return;
        }
        this.lastScrollState = this.currentScrollState;
        this.currentScrollState = i;
    }

    public void setNotLoop() {
        this.isLoop = false;
    }

    public final void setTextSize(float f) {
        if (f > 0.0f) {
            int i = (int) (this.context.getResources().getDisplayMetrics().density * f);
            this.textSize = i;
            Paint paint = this.paintOuterText;
            if (paint != null) {
                paint.setTextSize(i);
            }
            Paint paint2 = this.paintCenterText;
            if (paint2 != null) {
                paint2.setTextSize(this.textSize);
            }
        }
    }

    public final void setInitPosition(int i) {
        if (i < 0) {
            this.initPosition = 0;
            return;
        }
        List<IndexString> list = this.items;
        if (list == null || list.size() <= i) {
            return;
        }
        this.initPosition = i;
    }

    public final void setListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public final void setOnItemScrollListener(OnItemScrollListener onItemScrollListener) {
        this.mOnItemScrollListener = onItemScrollListener;
    }

    public final void setItems(List<String> list) {
        this.items = convertData(list);
        remeasure();
        invalidate();
    }

    public List<IndexString> convertData(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(new IndexString(i, list.get(i)));
        }
        return arrayList;
    }

    public final int getSelectedItem() {
        return this.preCurrentIndex;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void onItemSelected() {
        if (this.onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    @Override // android.view.View
    public void setScaleX(float f) {
        this.scaleX = f;
    }

    public void setCurrentPosition(int i) {
        List<IndexString> list = this.items;
        if (list == null || list.isEmpty()) {
            return;
        }
        int size = this.items.size();
        if (i < 0 || i >= size || i == getSelectedItem()) {
            return;
        }
        this.initPosition = i;
        this.totalScrollY = 0;
        this.mOffset = 0;
        changeScrollState(1);
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        OnItemScrollListener onItemScrollListener;
        super.onDraw(canvas);
        List<IndexString> list = this.items;
        if (list == null || list.isEmpty()) {
            return;
        }
        int i = (int) (this.totalScrollY / (this.lineSpacingMultiplier * this.itemTextHeight));
        this.change = i;
        int size = this.initPosition + (i % this.items.size());
        this.preCurrentIndex = size;
        if (!this.isLoop) {
            if (size < 0) {
                this.preCurrentIndex = 0;
            }
            if (this.preCurrentIndex > this.items.size() - 1) {
                this.preCurrentIndex = this.items.size() - 1;
            }
        } else {
            if (size < 0) {
                this.preCurrentIndex = this.items.size() + this.preCurrentIndex;
            }
            if (this.preCurrentIndex > this.items.size() - 1) {
                this.preCurrentIndex -= this.items.size();
            }
        }
        int i2 = (int) (this.totalScrollY % (this.lineSpacingMultiplier * this.itemTextHeight));
        int i3 = 0;
        while (true) {
            int i4 = this.itemsVisibleCount;
            if (i3 >= i4) {
                break;
            }
            int i5 = this.preCurrentIndex - ((i4 / 2) - i3);
            if (this.isLoop) {
                while (i5 < 0) {
                    i5 += this.items.size();
                }
                while (i5 > this.items.size() - 1) {
                    i5 -= this.items.size();
                }
                this.drawingStrings.put(Integer.valueOf(i3), this.items.get(i5));
            } else if (i5 < 0) {
                this.drawingStrings.put(Integer.valueOf(i3), new IndexString());
            } else if (i5 > this.items.size() - 1) {
                this.drawingStrings.put(Integer.valueOf(i3), new IndexString());
            } else {
                this.drawingStrings.put(Integer.valueOf(i3), this.items.get(i5));
            }
            i3++;
        }
        float f = this.paddingLeft;
        int i6 = this.firstLineY;
        canvas.drawLine(f, i6, this.measuredWidth, i6, this.paintIndicator);
        float f2 = this.paddingLeft;
        int i7 = this.secondLineY;
        canvas.drawLine(f2, i7, this.measuredWidth, i7, this.paintIndicator);
        for (int i8 = 0; i8 < this.itemsVisibleCount; i8++) {
            canvas.save();
            float f3 = this.itemTextHeight * this.lineSpacingMultiplier;
            double d = (i8 * f3) - i2;
            Double.isNaN(d);
            double d2 = this.halfCircumference;
            Double.isNaN(d2);
            double d3 = (d * 3.141592653589793d) / d2;
            if (d3 >= 3.141592653589793d || d3 <= 0.0d) {
                canvas.restore();
            } else {
                double d4 = this.radius;
                double cos = Math.cos(d3);
                double d5 = this.radius;
                Double.isNaN(d5);
                Double.isNaN(d4);
                double d6 = d4 - (cos * d5);
                double sin = Math.sin(d3);
                double d7 = this.itemTextHeight;
                Double.isNaN(d7);
                int i9 = (int) (d6 - ((sin * d7) / 2.0d));
                canvas.translate(0.0f, i9);
                canvas.scale(1.0f, (float) Math.sin(d3));
                int i10 = this.firstLineY;
                if (i9 <= i10 && this.itemTextHeight + i9 >= i10) {
                    canvas.save();
                    canvas.clipRect(0, 0, this.measuredWidth, this.firstLineY - i9);
                    drawOuterText(canvas, i8);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, this.firstLineY - i9, this.measuredWidth, (int) f3);
                    drawCenterText(canvas, i8);
                    canvas.restore();
                } else {
                    int i11 = this.secondLineY;
                    if (i9 <= i11 && this.itemTextHeight + i9 >= i11) {
                        canvas.save();
                        canvas.clipRect(0, 0, this.measuredWidth, this.secondLineY - i9);
                        drawCenterText(canvas, i8);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(0, this.secondLineY - i9, this.measuredWidth, (int) f3);
                        drawOuterText(canvas, i8);
                        canvas.restore();
                    } else if (i9 >= i10 && this.itemTextHeight + i9 <= i11) {
                        canvas.clipRect(0, 0, this.measuredWidth, (int) f3);
                        drawCenterText(canvas, i8);
                    } else {
                        canvas.clipRect(0, 0, this.measuredWidth, (int) f3);
                        drawOuterText(canvas, i8);
                    }
                }
                canvas.restore();
            }
        }
        int i12 = this.currentScrollState;
        int i13 = this.lastScrollState;
        if (i12 != i13) {
            this.lastScrollState = i12;
            OnItemScrollListener onItemScrollListener2 = this.mOnItemScrollListener;
            if (onItemScrollListener2 != null) {
                onItemScrollListener2.onItemScrollStateChanged(this, getSelectedItem(), i13, this.currentScrollState, this.totalScrollY);
            }
        }
        int i14 = this.currentScrollState;
        if ((i14 == 2 || i14 == 3) && (onItemScrollListener = this.mOnItemScrollListener) != null) {
            onItemScrollListener.onItemScrolling(this, getSelectedItem(), this.currentScrollState, this.totalScrollY);
        }
    }

    private void drawOuterText(Canvas canvas, int i) {
        canvas.drawText(this.drawingStrings.get(Integer.valueOf(i)).string, getTextX(this.drawingStrings.get(Integer.valueOf(i)).string, this.paintOuterText, this.tempRect), getDrawingY(), this.paintOuterText);
    }

    private void drawCenterText(Canvas canvas, int i) {
        canvas.drawText(this.drawingStrings.get(Integer.valueOf(i)).string, getTextX(this.drawingStrings.get(Integer.valueOf(i)).string, this.paintOuterText, this.tempRect), getDrawingY(), this.paintCenterText);
    }

    private int getDrawingY() {
        int i = this.itemTextHeight;
        int i2 = this.textHeight;
        return i > i2 ? i - ((i - i2) / 2) : i;
    }

    private int getTextX(String str, Paint paint, Rect rect) {
        paint.getTextBounds(str, 0, str.length(), rect);
        int i = this.measuredWidth;
        int i2 = this.paddingLeft;
        return (((i - i2) - ((int) (rect.width() * this.scaleX))) / 2) + i2;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        initPaintsIfPossible();
        remeasure();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        double d;
        boolean onTouchEvent = this.flingGestureDetector.onTouchEvent(motionEvent);
        float f = this.lineSpacingMultiplier * this.itemTextHeight;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.startTime = System.currentTimeMillis();
            cancelFuture();
            this.previousY = motionEvent.getRawY();
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else if (action == 2) {
            this.previousY = motionEvent.getRawY();
            this.totalScrollY = (int) (this.totalScrollY + (this.previousY - motionEvent.getRawY()));
            if (!this.isLoop) {
                float f2 = (-this.initPosition) * f;
                float size = ((this.items.size() - 1) - this.initPosition) * f;
                int i = this.totalScrollY;
                if (i < f2) {
                    this.totalScrollY = (int) f2;
                } else if (i > size) {
                    this.totalScrollY = (int) size;
                }
            }
            changeScrollState(2);
        } else {
            if (!onTouchEvent) {
                float y = motionEvent.getY();
                int i2 = this.radius;
                double acos = Math.acos((i2 - y) / i2);
                double d2 = this.radius;
                Double.isNaN(d2);
                double d3 = acos * d2;
                double d4 = f / 2.0f;
                Double.isNaN(d4);
                double d5 = d3 + d4;
                Double.isNaN(f);
                this.mOffset = (int) (((((int) (d5 / d)) - (this.itemsVisibleCount / 2)) * f) - (((this.totalScrollY % f) + f) % f));
                if (System.currentTimeMillis() - this.startTime > 120) {
                    smoothScroll(ACTION.DRAG);
                } else {
                    smoothScroll(ACTION.CLICK);
                }
            }
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        invalidate();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class IndexString {
        private int index;
        private String string;

        public IndexString() {
            this.string = "";
        }

        public IndexString(int i, String str) {
            this.index = i;
            this.string = str;
        }
    }
}
