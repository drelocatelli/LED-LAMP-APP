package com.home.view.dmx02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.core.view.ViewCompat;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DoodleView extends SurfaceView implements SurfaceHolder.Callback {
    private BaseAction curAction;
    private int currentColor;
    private int currentSize;
    private ActionType mActionType;
    private List<BaseAction> mBaseActions;
    private Bitmap mBitmap;
    private Paint mPaint;
    private SurfaceHolder mSurfaceHolder;
    private OnGetBitmapListener onGetBitmapListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum ActionType {
        Point,
        Path,
        Line,
        Rect,
        Circle,
        FillEcRect,
        FilledCircle
    }

    /* loaded from: classes.dex */
    public interface OnGetBitmapListener {
        void onGetBitmap(Bitmap bitmap);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public DoodleView(Context context) {
        super(context);
        this.mSurfaceHolder = null;
        this.curAction = null;
        this.currentColor = ViewCompat.MEASURED_STATE_MASK;
        this.currentSize = 5;
        this.mActionType = ActionType.Path;
        init();
    }

    public DoodleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSurfaceHolder = null;
        this.curAction = null;
        this.currentColor = ViewCompat.MEASURED_STATE_MASK;
        this.currentSize = 5;
        this.mActionType = ActionType.Path;
        init();
    }

    public DoodleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSurfaceHolder = null;
        this.curAction = null;
        this.currentColor = ViewCompat.MEASURED_STATE_MASK;
        this.currentSize = 5;
        this.mActionType = ActionType.Path;
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        this.mSurfaceHolder = holder;
        holder.addCallback(this);
        setFocusable(true);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setStrokeWidth(this.currentSize);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas lockCanvas = this.mSurfaceHolder.lockCanvas();
        lockCanvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        this.mSurfaceHolder.unlockCanvasAndPost(lockCanvas);
        this.mBaseActions = new ArrayList();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 3) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (action == 0) {
            setCurAction(x, y);
            Canvas lockCanvas = this.mSurfaceHolder.lockCanvas();
            lockCanvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
            for (BaseAction baseAction : this.mBaseActions) {
                baseAction.draw(lockCanvas);
            }
            this.curAction.move(x, y);
            this.curAction.draw(lockCanvas);
            this.mSurfaceHolder.unlockCanvasAndPost(lockCanvas);
        } else if (action == 1) {
            this.mBaseActions.add(this.curAction);
            this.onGetBitmapListener.onGetBitmap(getCurrentBitmap());
            this.curAction = null;
        } else if (action == 2) {
            Canvas lockCanvas2 = this.mSurfaceHolder.lockCanvas();
            lockCanvas2.drawColor(ViewCompat.MEASURED_STATE_MASK);
            for (BaseAction baseAction2 : this.mBaseActions) {
                baseAction2.draw(lockCanvas2);
            }
            this.curAction.move(x, y);
            this.curAction.draw(lockCanvas2);
            this.mSurfaceHolder.unlockCanvasAndPost(lockCanvas2);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.home.view.dmx02.DoodleView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$home$view$dmx02$DoodleView$ActionType;

        static {
            int[] iArr = new int[ActionType.values().length];
            $SwitchMap$com$home$view$dmx02$DoodleView$ActionType = iArr;
            try {
                iArr[ActionType.Point.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$home$view$dmx02$DoodleView$ActionType[ActionType.Path.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$home$view$dmx02$DoodleView$ActionType[ActionType.Line.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$home$view$dmx02$DoodleView$ActionType[ActionType.Rect.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$home$view$dmx02$DoodleView$ActionType[ActionType.Circle.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$home$view$dmx02$DoodleView$ActionType[ActionType.FillEcRect.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$home$view$dmx02$DoodleView$ActionType[ActionType.FilledCircle.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    private void setCurAction(float f, float f2) {
        switch (AnonymousClass1.$SwitchMap$com$home$view$dmx02$DoodleView$ActionType[this.mActionType.ordinal()]) {
            case 1:
                this.curAction = new MyPoint(f, f2, this.currentColor);
                return;
            case 2:
                this.curAction = new MyPath(f, f2, this.currentSize, this.currentColor);
                return;
            case 3:
                this.curAction = new MyLine(f, f2, this.currentSize, this.currentColor);
                return;
            case 4:
                this.curAction = new MyRect(f, f2, this.currentSize, this.currentColor);
                return;
            case 5:
                this.curAction = new MyCircle(f, f2, this.currentSize, this.currentColor);
                return;
            case 6:
                this.curAction = new MyFillRect(f, f2, this.currentSize, this.currentColor);
                return;
            case 7:
                this.curAction = new MyFillCircle(f, f2, this.currentSize, this.currentColor);
                return;
            default:
                return;
        }
    }

    public void setColor(String str) {
        this.currentColor = Color.parseColor(str);
    }

    public void setSize(int i) {
        this.currentSize = i;
    }

    public void setType(ActionType actionType) {
        this.mActionType = actionType;
    }

    public Bitmap getBitmap() {
        this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        doDraw(new Canvas(this.mBitmap));
        return this.mBitmap;
    }

    public String saveBitmap(DoodleView doodleView) {
        String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/doodleview/" + System.currentTimeMillis() + PictureMimeType.PNG;
        if (!new File(str).exists()) {
            new File(str).getParentFile().mkdir();
        }
        savePicByPNG(doodleView.getBitmap(), str);
        return str;
    }

    public static void savePicByPNG(Bitmap bitmap, String str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void doDraw(Canvas canvas) {
        canvas.drawColor(0);
        for (BaseAction baseAction : this.mBaseActions) {
            baseAction.draw(canvas);
        }
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
    }

    public Bitmap getCurrentBitmap() {
        List<BaseAction> list = this.mBaseActions;
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<BaseAction> list2 = this.mBaseActions;
        this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mBitmap);
        canvas.drawColor(0);
        list2.get(list2.size() - 1).draw(canvas);
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
        return this.mBitmap;
    }

    public boolean back() {
        List<BaseAction> list = this.mBaseActions;
        if (list == null || list.size() <= 0) {
            return false;
        }
        List<BaseAction> list2 = this.mBaseActions;
        list2.remove(list2.size() - 1);
        Canvas lockCanvas = this.mSurfaceHolder.lockCanvas();
        lockCanvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        for (BaseAction baseAction : this.mBaseActions) {
            baseAction.draw(lockCanvas);
        }
        this.mSurfaceHolder.unlockCanvasAndPost(lockCanvas);
        return true;
    }

    public void reset() {
        List<BaseAction> list = this.mBaseActions;
        if (list == null || list.size() <= 0) {
            return;
        }
        this.mBaseActions.clear();
        Canvas lockCanvas = this.mSurfaceHolder.lockCanvas();
        lockCanvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        for (BaseAction baseAction : this.mBaseActions) {
            baseAction.draw(lockCanvas);
        }
        this.mSurfaceHolder.unlockCanvasAndPost(lockCanvas);
    }

    public void setOnGetBitmapListener(OnGetBitmapListener onGetBitmapListener) {
        this.onGetBitmapListener = onGetBitmapListener;
    }
}
