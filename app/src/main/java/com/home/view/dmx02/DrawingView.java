package com.home.view.dmx02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.home.view.dmx02.DoodleView;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class DrawingView extends View {
    private static final String TAG = "DrawingView";
    private static final float TOUCH_TOLERANCE = 4.0f;
    private Handler handler;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Canvas mCanvas;
    private boolean mDrawMode;
    private DrawPath mLastDrawPath;
    private Bitmap mOriginBitmap;
    private Paint mPaint;
    private int mPaintBarPenColor;
    private float mPaintBarPenSize;
    private Path mPath;
    private float mProportion;
    private float mX;
    private float mY;
    private Matrix matrix;
    private DoodleView.OnGetBitmapListener onGetBitmapListener;
    private LinkedList<DrawPath> savePath;

    /* loaded from: classes.dex */
    public interface OnGetBitmapListener {
        void onGetBitmap(Bitmap bitmap);
    }

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mProportion = 0.0f;
        init();
    }

    private void init() {
        Log.d(TAG, "init: ");
        this.mBitmapPaint = new Paint(5);
        this.mDrawMode = false;
        this.savePath = new LinkedList<>();
        this.matrix = new Matrix();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            if (bitmap.getHeight() > size2 && this.mBitmap.getHeight() > this.mBitmap.getWidth()) {
                size = (this.mBitmap.getWidth() * size2) / this.mBitmap.getHeight();
            } else if (this.mBitmap.getWidth() > size && this.mBitmap.getWidth() > this.mBitmap.getHeight()) {
                size2 = (this.mBitmap.getHeight() * size) / this.mBitmap.getWidth();
            } else {
                size2 = this.mBitmap.getHeight();
                size = this.mBitmap.getWidth();
            }
        }
        Log.d(TAG, "onMeasure: heightSize: " + size2 + " widthSize: " + size);
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mBitmap == null) {
            this.mBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(this.mBitmap);
        this.mCanvas = canvas;
        canvas.drawColor(0);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = canvas.getHeight() / this.mBitmap.getHeight();
        if (height < 1.0f) {
            this.mProportion = height;
            this.matrix.reset();
            this.matrix.postScale(height, height);
            this.matrix.postTranslate((canvas.getWidth() - (this.mBitmap.getWidth() * height)) / 2.0f, 0.0f);
            canvas.drawBitmap(this.mBitmap, this.matrix, this.mBitmapPaint);
            return;
        }
        this.mProportion = 0.0f;
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mBitmapPaint);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x;
        float y;
        if (this.mDrawMode) {
            if (this.mProportion != 0.0f) {
                x = motionEvent.getX() / this.mProportion;
                y = motionEvent.getY() / this.mProportion;
            } else {
                x = motionEvent.getX();
                y = motionEvent.getY();
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                if (this.mLastDrawPath != null) {
                    this.mPaint.setColor(this.mPaintBarPenColor);
                    this.mPaint.setStrokeWidth(this.mPaintBarPenSize);
                }
                Path path = new Path();
                this.mPath = path;
                path.reset();
                this.mPath.moveTo(x, y);
                this.mX = x;
                this.mY = y;
                this.mCanvas.drawPath(this.mPath, this.mPaint);
            } else if (action == 1) {
                this.mPath.lineTo(this.mX, this.mY);
                this.mCanvas.drawPath(this.mPath, this.mPaint);
                DrawPath drawPath = new DrawPath(this.mPath, this.mPaint.getColor(), this.mPaint.getStrokeWidth());
                this.mLastDrawPath = drawPath;
                this.savePath.add(drawPath);
                if (this.handler == null) {
                    this.handler = new Handler();
                }
                this.handler.postDelayed(new Runnable() { // from class: com.home.view.dmx02.DrawingView.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DrawingView.this.onGetBitmapListener.onGetBitmap(DrawingView.this.getImageBitmap());
                    }
                }, 3000L);
                this.mPath = null;
            } else if (action == 2) {
                float abs = Math.abs(x - this.mX);
                float abs2 = Math.abs(y - this.mY);
                if (abs >= TOUCH_TOLERANCE || abs2 >= TOUCH_TOLERANCE) {
                    Path path2 = this.mPath;
                    float f = this.mX;
                    float f2 = this.mY;
                    path2.quadTo(f, f2, (x + f) / 2.0f, (y + f2) / 2.0f);
                    this.mX = x;
                    this.mY = y;
                }
                this.mCanvas.drawPath(this.mPath, this.mPaint);
            }
            invalidate();
            return true;
        }
        return false;
    }

    public void initializePen() {
        this.mDrawMode = true;
        this.mPaint = null;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.mCanvas.drawColor(i);
        super.setBackgroundColor(i);
    }

    public void setPenSize(float f) {
        this.mPaintBarPenSize = f;
        this.mPaint.setStrokeWidth(f);
    }

    public float getPenSize() {
        return this.mPaint.getStrokeWidth();
    }

    public void setPenColor(int i) {
        this.mPaintBarPenColor = i;
        this.mPaint.setColor(i);
    }

    public int getPenColor() {
        return this.mPaint.getColor();
    }

    public Bitmap getImageBitmap() {
        return this.mBitmap;
    }

    public void loadImage(Bitmap bitmap) {
        Log.d(TAG, "loadImage: ");
        this.mOriginBitmap = bitmap;
        this.mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.mCanvas = new Canvas(this.mBitmap);
        invalidate();
    }

    public void undo() {
        Log.d(TAG, "undo: recall last path");
        LinkedList<DrawPath> linkedList = this.savePath;
        if (linkedList == null || linkedList.size() <= 0) {
            return;
        }
        this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.savePath.removeLast();
        Iterator<DrawPath> it = this.savePath.iterator();
        while (it.hasNext()) {
            DrawPath next = it.next();
            this.mPaint.setColor(next.getPaintColor());
            this.mPaint.setStrokeWidth(next.getPaintWidth());
            this.mCanvas.drawPath(next.path, this.mPaint);
        }
        invalidate();
    }

    public void reset() {
        LinkedList<DrawPath> linkedList = this.savePath;
        if (linkedList == null || linkedList.size() <= 0) {
            return;
        }
        this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.savePath.clear();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DrawPath {
        int paintColor;
        float paintWidth;
        Path path;

        DrawPath(Path path, int i, float f) {
            this.path = path;
            this.paintColor = i;
            this.paintWidth = f;
        }

        int getPaintColor() {
            return this.paintColor;
        }

        float getPaintWidth() {
            return this.paintWidth;
        }
    }

    public void setOnGetBitmapListener(DoodleView.OnGetBitmapListener onGetBitmapListener) {
        this.onGetBitmapListener = onGetBitmapListener;
    }
}
