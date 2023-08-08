package com.example.linechartlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class LineChartView extends AbstractChartView implements LineChartDataProvider {
    private static final String TAG = "LineChartView";
    public static int getWidth = 0;
    public static boolean isChange = true;
    public static boolean isGone = false;
    List<AxisValue> axisValues;
    protected LineChartData data;
    boolean dowm;
    private PathEffect effects;
    boolean five;
    boolean four;
    float getTimeX;
    float getViewSize;
    float getViewX;
    float getViewY;
    float getpointY;
    HashMap<Float, Float> hashMap;
    ArrayList<Integer> hourList;
    int isAlikee;
    Line lineB;
    Line lineC;
    Line lineG;
    String[] lineLabels;
    Line lineP;
    Line lineR;
    Line lineW;
    ReturnValueCallback mReturnValueCallback;
    SendCallBack mSendCallBack;
    ArrayList<Integer> minList;
    protected LineChartOnValueSelectListener onValueTouchListener;
    boolean one;
    Paint paint;
    Paint paintText;
    int paintValue;
    Path path;
    PointValue pointValue;
    int position;
    int showLineChart;
    ExecutorService singleThreadExecutor;
    boolean six;
    int tableOneorTwo;
    boolean three;
    boolean two;
    ArrayList valueList;
    List<PointValue> valuesB;
    List<PointValue> valuesC;
    List<PointValue> valuesG;
    List<PointValue> valuesP;
    List<PointValue> valuesR;
    List<PointValue> valuesW;
    float xx;
    float yy;

    /* loaded from: classes.dex */
    public interface ReturnValueCallback {
        void returnValue(int i);

        void update();
    }

    /* loaded from: classes.dex */
    public interface SendCallBack {
        void clear();

        void sendValue(int[] iArr);
    }

    public int returnY(float f, float f2, float f3, float f4, float f5) {
        float f6 = (f2 - f4) / (f - f3);
        int i = (int) ((((int) f6) * f5) + (f2 - (f * f6)));
        if (i >= 100) {
            return 100;
        }
        return i;
    }

    public LineChartView(Context context) {
        this(context, null, 0);
    }

    public LineChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LineChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.effects = new DashPathEffect(new float[]{20.0f, 10.0f}, 1.0f);
        this.paintText = new Paint();
        this.paint = new Paint();
        this.path = new Path();
        this.lineLabels = new String[]{"0000", "04", "08", "12", "16", "20", "0000"};
        this.axisValues = new ArrayList();
        this.paintValue = 0;
        this.xx = -1.0f;
        this.yy = 0.0f;
        this.isAlikee = 666;
        this.one = true;
        this.two = true;
        this.three = true;
        this.four = true;
        this.five = true;
        this.six = true;
        this.valueList = new ArrayList();
        this.valuesR = new ArrayList();
        this.valuesG = new ArrayList();
        this.valuesB = new ArrayList();
        this.valuesW = new ArrayList();
        this.valuesC = new ArrayList();
        this.valuesP = new ArrayList();
        this.lineR = new Line(this.valuesR);
        this.lineG = new Line(this.valuesG);
        this.lineB = new Line(this.valuesB);
        this.lineW = new Line(this.valuesW);
        this.lineC = new Line(this.valuesC);
        this.lineP = new Line(this.valuesP);
        this.onValueTouchListener = new DummyLineChartOnValueSelectListener();
        this.singleThreadExecutor = Executors.newFixedThreadPool(4);
        this.hashMap = new HashMap<>();
        this.hourList = new ArrayList<>();
        this.minList = new ArrayList<>();
        addLines();
        setChartRenderer(new LineChartRenderer(context, this, this));
        setLineChartData(LineChartData.generateDummyData());
    }

    @Override // com.example.linechartlibrary.LineChartDataProvider
    public LineChartData getLineChartData() {
        return this.data;
    }

    @Override // com.example.linechartlibrary.LineChartDataProvider
    public void setLineChartData(LineChartData lineChartData) {
        super.onChartDataChange();
    }

    @Override // com.example.linechartlibrary.Chart
    public ChartData getChartData() {
        return this.data;
    }

    @Override // com.example.linechartlibrary.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getLines().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
            return;
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public LineChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(LineChartOnValueSelectListener lineChartOnValueSelectListener) {
        if (lineChartOnValueSelectListener != null) {
            this.onValueTouchListener = lineChartOnValueSelectListener;
        }
    }

    public void setPaint(int i) {
        this.paintValue = i;
    }

    public int getPaint() {
        return this.paintValue;
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        getNowTime();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.example.linechartlibrary.AbstractChartView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.getViewSize = getRight() - getLeft();
        double bottom = getBottom() - getTop();
        Double.isNaN(bottom);
        this.getViewY = (float) (bottom / 100.0d);
        this.getViewX = this.getViewSize / 6.0f;
        this.mReturnValueCallback.update();
        Log.e(TAG, "onSizeChanged: " + this.getViewSize + "====" + this.getViewX);
    }

    public float getTimeX() {
        return this.getTimeX;
    }

    public void table(int i) {
        this.tableOneorTwo = i;
    }

    public void showLine(int i) {
        this.showLineChart = i;
    }

    public void addLines() {
        ValueShape valueShape = ValueShape.CIRCLE;
        this.lineR.setColor(SupportMenu.CATEGORY_MASK);
        this.lineR.setShape(valueShape);
        this.lineR.setPointRadius(3);
        this.lineR.setStrokeWidth(1);
        this.lineR.setHasLabelsOnlyForSelected(true);
        this.lineR.setHasLines(true);
        this.lineR.setHasPoints(true);
        this.lineG.setColor(-16711936);
        this.lineG.setShape(valueShape);
        this.lineG.setPointRadius(3);
        this.lineG.setStrokeWidth(1);
        this.lineG.setHasLabelsOnlyForSelected(true);
        this.lineG.setHasLines(true);
        this.lineG.setHasPoints(true);
        this.lineG.setHasGradientToTransparent(true);
        this.lineB.setColor(-16776961);
        this.lineB.setShape(valueShape);
        this.lineB.setPointRadius(3);
        this.lineB.setStrokeWidth(1);
        this.lineB.setHasLabelsOnlyForSelected(true);
        this.lineB.setHasLines(true);
        this.lineB.setHasPoints(true);
        this.lineB.setHasGradientToTransparent(true);
        this.lineW.setColor(-1);
        this.lineW.setShape(valueShape);
        this.lineW.setPointRadius(3);
        this.lineW.setStrokeWidth(1);
        this.lineW.setHasLabelsOnlyForSelected(true);
        this.lineW.setHasLines(true);
        this.lineW.setHasPoints(true);
        this.lineW.setHasGradientToTransparent(true);
        this.lineC.setColor(-16711681);
        this.lineC.setShape(valueShape);
        this.lineC.setPointRadius(3);
        this.lineC.setStrokeWidth(1);
        this.lineC.setHasLabelsOnlyForSelected(true);
        this.lineC.setHasLines(true);
        this.lineC.setHasPoints(true);
        this.lineC.setHasGradientToTransparent(true);
        this.lineP.setColor(-65281);
        this.lineP.setShape(valueShape);
        this.lineP.setPointRadius(3);
        this.lineP.setStrokeWidth(1);
        this.lineP.setHasLabelsOnlyForSelected(true);
        this.lineP.setHasLines(true);
        this.lineP.setHasPoints(true);
        this.lineP.setHasGradientToTransparent(true);
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.lineB);
        arrayList.add(this.lineG);
        arrayList.add(this.lineR);
        arrayList.add(this.lineC);
        arrayList.add(this.lineP);
        arrayList.add(this.lineW);
        this.data = new LineChartData(arrayList);
        for (int i = 0; i < 7; i++) {
            this.axisValues.add(new AxisValue(i).setLabel(this.lineLabels[i]));
        }
        Axis maxLabelChars = new Axis(this.axisValues).setMaxLabelChars(3);
        maxLabelChars.setTextColor(-1).setHasSeparationLineColor(-1).setHasTiltedLabels(true);
        this.data.setAxisXBottom(maxLabelChars);
        Axis maxLabelChars2 = new Axis().setHasLines(true).setHasSeparationLine(false).setMaxLabelChars(0);
        maxLabelChars2.setTextColor(-1);
        this.data.setAxisYLeft(maxLabelChars2);
        this.data.setBaseValue(Float.NEGATIVE_INFINITY);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.example.linechartlibrary.AbstractChartView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paintText.setColor(-1);
        this.paintText.setTextSize(30.0f);
        this.paintText.setStrokeWidth(0.6f);
        this.paintText.setStyle(Paint.Style.FILL);
        canvas.drawText(String.valueOf(((int) ((this.getTimeX * 24.0f) / this.getViewSize)) + ":" + ((int) (((this.getTimeX * 1440.0f) / this.getViewSize) % 60.0f))), this.getTimeX, 30.0f, this.paintText);
        this.paint.setColor(InputDeviceCompat.SOURCE_ANY);
        this.paint.setStrokeWidth(2.0f);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setPathEffect(this.effects);
        this.path.reset();
        this.path.moveTo(this.getTimeX, 0.0f);
        this.path.lineTo(this.getTimeX, (getBottom() - getTop()) - 50);
        canvas.drawPath(this.path, this.paint);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.e(TAG, "X = " + motionEvent.getX() + ",  Y = " + motionEvent.getY());
        if (isChange) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.yy = ((getBottom() - getTop()) - motionEvent.getY()) / this.getViewY;
                float x = motionEvent.getX() / this.getViewX;
                this.xx = x;
                if (x != 6.0f && x != 0.0f) {
                    this.getTimeX = motionEvent.getX();
                    int i = this.paintValue;
                    if (i == 1) {
                        this.isAlikee = 1;
                        actionDown(this.valuesR, this.xx, this.yy, motionEvent);
                    } else if (i == 2) {
                        this.isAlikee = 2;
                        actionDown(this.valuesG, this.xx, this.yy, motionEvent);
                    } else if (i == 3) {
                        this.isAlikee = 3;
                        actionDown(this.valuesB, this.xx, this.yy, motionEvent);
                    } else if (i == 4) {
                        this.isAlikee = 4;
                        actionDown(this.valuesW, this.xx, this.yy, motionEvent);
                    } else if (i == 5) {
                        this.isAlikee = 5;
                        actionDown(this.valuesC, this.xx, this.yy, motionEvent);
                    } else if (i == 6) {
                        this.isAlikee = 6;
                        actionDown(this.valuesP, this.xx, this.yy, motionEvent);
                    }
                    ReturnValueCallback returnValueCallback = this.mReturnValueCallback;
                    if (returnValueCallback != null) {
                        returnValueCallback.returnValue((int) this.yy);
                    }
                }
            } else if (action == 1) {
                float bottom = ((getBottom() - getTop()) - motionEvent.getY()) / this.getViewY;
                float x2 = motionEvent.getX() / this.getViewX;
                if (x2 != 6.0f && x2 != 0.0f) {
                    this.getTimeX = motionEvent.getX();
                    float f = bottom - this.yy;
                    double d = x2 - this.xx;
                    if (d > 0.2d || d < -0.2d || f > 5.0f || f < -5.0f) {
                        int i2 = this.paintValue;
                        if (i2 == 1) {
                            actionUp(this.valuesR, x2, this.getViewX, bottom);
                        } else if (i2 == 2) {
                            actionUp(this.valuesG, x2, this.getViewX, bottom);
                        } else if (i2 == 3) {
                            actionUp(this.valuesB, x2, this.getViewX, bottom);
                        } else if (i2 == 4) {
                            actionUp(this.valuesW, x2, this.getViewX, bottom);
                        } else if (i2 == 5) {
                            actionUp(this.valuesC, x2, this.getViewX, bottom);
                        } else if (i2 == 6) {
                            actionUp(this.valuesP, x2, this.getViewX, bottom);
                        }
                        ReturnValueCallback returnValueCallback2 = this.mReturnValueCallback;
                        if (returnValueCallback2 != null) {
                            returnValueCallback2.returnValue((int) bottom);
                        }
                    }
                    if (this.paintValue != 0) {
                        this.singleThreadExecutor.execute(new Thread() { // from class: com.example.linechartlibrary.LineChartView.1
                            @Override // java.lang.Thread, java.lang.Runnable
                            public void run() {
                                LineChartView.this.mSendCallBack.clear();
                                LineChartView lineChartView = LineChartView.this;
                                lineChartView.getPoint(lineChartView.showLineChart);
                            }
                        });
                    }
                }
            } else if (action == 2) {
                float bottom2 = ((getBottom() - getTop()) - motionEvent.getY()) / this.getViewY;
                float x3 = motionEvent.getX() / this.getViewX;
                if (x3 != 6.0f && x3 != 0.0f) {
                    this.getTimeX = motionEvent.getX();
                    float f2 = bottom2 - this.yy;
                    double d2 = x3 - this.xx;
                    if ((d2 > 0.2d || d2 < -0.2d || f2 > 2.0f || f2 < -2.0f) && this.dowm) {
                        int i3 = this.paintValue;
                        if (i3 == 1) {
                            actionMove(this.valuesR, x3, bottom2);
                        } else if (i3 == 2) {
                            actionMove(this.valuesG, x3, bottom2);
                        } else if (i3 == 3) {
                            actionMove(this.valuesB, x3, bottom2);
                        } else if (i3 == 4) {
                            actionMove(this.valuesW, x3, bottom2);
                        } else if (i3 == 5) {
                            actionMove(this.valuesC, x3, bottom2);
                        } else if (i3 == 6) {
                            actionMove(this.valuesP, x3, bottom2);
                        }
                        ReturnValueCallback returnValueCallback3 = this.mReturnValueCallback;
                        if (returnValueCallback3 != null) {
                            returnValueCallback3.returnValue((int) bottom2);
                        }
                    }
                }
            }
        }
        invalidate();
        return true;
    }

    public void setReturnValueCallback(ReturnValueCallback returnValueCallback) {
        this.mReturnValueCallback = returnValueCallback;
    }

    public void actionDown(List<PointValue> list, float f, float f2, MotionEvent motionEvent) {
        this.dowm = false;
        int i = 0;
        while (true) {
            if (i >= list.size() - 1) {
                i = -1;
                break;
            }
            float y = list.get(i).getY() - f2;
            double x = list.get(i).getX() - f;
            if (x < 0.5d && x > -0.5d && y < 10.0f && y > -10.0f) {
                this.dowm = true;
                break;
            } else {
                this.dowm = false;
                i++;
            }
        }
        if (this.dowm) {
            this.pointValue = list.get(i);
            this.getpointY = list.get(i).getY();
            this.position = i;
            return;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).getX() < f) {
                int i3 = i2 + 1;
                if (list.get(i3).getX() > f && getTimeSize(3) < 24) {
                    list.add(i3, new PointValue(f, f2));
                    this.position = i3;
                    this.pointValue = list.get(i3);
                    this.getpointY = list.get(this.position).getY();
                    this.dowm = true;
                    return;
                }
            }
        }
    }

    public void actionMove(List<PointValue> list, float f, float f2) {
        if (f >= list.get(this.position + 1).getX()) {
            Log.e(TAG, "移动 >=   position == " + this.position + ",  X = " + list.get(this.position).getX() + ",  moveX = " + f);
            if (f2 >= 100.0f) {
                int i = this.position;
                if (i == 0) {
                    this.pointValue.set(0.0f, 100.0f);
                    PointValue pointValue = list.get(list.size() - 1);
                    pointValue.set(pointValue.getX(), 100.0f);
                } else {
                    this.pointValue.set(list.get(i + 1).getX(), 100.0f);
                }
            } else if (f2 <= 0.0f) {
                int i2 = this.position;
                if (i2 == 0) {
                    this.pointValue.set(0.0f, 0.0f);
                    PointValue pointValue2 = list.get(list.size() - 1);
                    pointValue2.set(pointValue2.getX(), 0.0f);
                } else {
                    this.pointValue.set(list.get(i2 + 1).getX(), 0.0f);
                }
            } else {
                int i3 = this.position;
                if (i3 == 0) {
                    this.pointValue.set(0.0f, f2);
                    PointValue pointValue3 = list.get(list.size() - 1);
                    pointValue3.set(pointValue3.getX(), f2);
                } else {
                    this.pointValue.set(list.get(i3 + 1).getX(), f2);
                }
            }
        } else {
            int i4 = this.position;
            if (i4 < 1 || f > list.get(i4 - 1).getX()) {
                int i5 = this.position;
                if (i5 == 0 && f <= list.get(i5).getX()) {
                    PointValue pointValue4 = list.get(this.position);
                    PointValue pointValue5 = list.get(list.size() - 1);
                    if (f2 >= 100.0f) {
                        pointValue4.set(list.get(this.position).getX(), 100.0f);
                        pointValue5.set(pointValue5.getX(), 100.0f);
                    } else if (f2 <= 0.0f) {
                        pointValue4.set(list.get(this.position).getX(), 0.0f);
                        pointValue5.set(pointValue5.getX(), 0.0f);
                    } else {
                        pointValue4.set(0.0f, f2);
                        pointValue5.set(pointValue5.getX(), f2);
                    }
                } else if (f2 >= 100.0f) {
                    if (this.position == 0) {
                        this.pointValue.set(0.0f, 100.0f);
                        PointValue pointValue6 = list.get(list.size() - 1);
                        pointValue6.set(pointValue6.getX(), 100.0f);
                    } else {
                        this.pointValue.set(f, 100.0f);
                    }
                } else if (f2 <= 0.0f) {
                    if (this.position == 0) {
                        this.pointValue.set(0.0f, 0.0f);
                        PointValue pointValue7 = list.get(list.size() - 1);
                        pointValue7.set(pointValue7.getX(), 0.0f);
                    } else {
                        this.pointValue.set(f, 0.0f);
                    }
                } else if (this.position == 0) {
                    this.pointValue.set(0.0f, f2);
                    PointValue pointValue8 = list.get(list.size() - 1);
                    pointValue8.set(pointValue8.getX(), f2);
                } else {
                    this.pointValue.set(f, f2);
                }
            } else if (f2 >= 100.0f) {
                this.pointValue.set(list.get(this.position - 1).getX(), 100.0f);
            } else if (f2 <= 0.0f) {
                this.pointValue.set(list.get(this.position - 1).getX(), 0.0f);
            } else {
                this.pointValue.set(list.get(this.position - 1).getX(), f2);
            }
        }
        this.getpointY = this.pointValue.getY();
    }

    public void actionUp(List<PointValue> list, float f, float f2, float f3) {
        if (list.size() <= 0) {
            return;
        }
        int i = this.position;
        if (i >= 1 && f <= list.get(i - 1).getX()) {
            this.getTimeX = list.get(this.position - 1).getX() * f2;
            this.getpointY = list.get(this.position - 1).getY();
            list.remove(this.position);
        } else {
            int i2 = this.position;
            if (i2 == 0 && f <= list.get(i2).getX()) {
                this.getTimeX = list.get(this.position).getX() * f2;
                this.getpointY = list.get(this.position).getY();
            } else if (f >= list.get(this.position + 1).getX()) {
                this.getTimeX = list.get(this.position + 1).getX() * f2;
                this.getpointY = list.get(this.position + 1).getY();
                list.remove(this.position);
            } else if (f3 >= 100.0f) {
                this.pointValue.set(f, 100.0f);
            } else if (f3 <= 0.0f) {
                this.pointValue.set(f, 0.0f);
            } else if (this.position == 0) {
                this.pointValue.set(0.0f, f3);
                PointValue pointValue = list.get(list.size() - 1);
                pointValue.set(pointValue.getX(), f3);
            } else {
                this.pointValue.set(f, f3);
            }
        }
        float f4 = this.getTimeX;
        if (f4 <= 0.0f) {
            this.getTimeX = 0.0f;
            return;
        }
        float f5 = this.getViewSize;
        if (f4 >= f5) {
            this.getTimeX = f5;
        }
    }

    public void clear() {
        this.paintValue = 0;
        if (this.pointValue != null) {
            this.pointValue = null;
        }
        isChange = true;
        this.valuesR.clear();
        this.valuesG.clear();
        this.valuesB.clear();
        this.valuesW.clear();
        this.valuesC.clear();
        this.valuesP.clear();
        invalidate();
        isGone = false;
        this.one = true;
        this.two = true;
        this.three = true;
        this.four = true;
        this.five = true;
        this.six = true;
    }

    public float getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
        float f = this.getViewSize;
        this.getTimeX = (((Integer.parseInt(simpleDateFormat.format(date)) * 60) + Integer.parseInt(simpleDateFormat2.format(date))) * f) / 1440.0f;
        getWidth = (int) f;
        if (isGone && isChange) {
            invalidate();
        }
        Log.e(TAG, "getNowTime: ");
        return this.getTimeX;
    }

    public void setSize(int i) {
        if (isChange) {
            float f = this.getTimeX;
            if (f <= 0.0f || f >= this.getViewSize) {
                return;
            }
            int i2 = this.paintValue;
            if (i2 == 1) {
                if (this.one && this.valuesR.size() == 0) {
                    this.one = false;
                    this.valuesR.add(new PointValue(0.0f, 0.0f));
                    this.valuesR.add(new PointValue(6.0f, 0.0f));
                }
                addNewPoint(this.valuesR, i, this.paintValue);
            } else if (i2 == 2) {
                if (this.two && this.valuesG.size() == 0) {
                    this.two = false;
                    this.valuesG.add(new PointValue(0.0f, 0.0f));
                    this.valuesG.add(new PointValue(6.0f, 0.0f));
                }
                addNewPoint(this.valuesG, i, this.paintValue);
            } else if (i2 == 3) {
                if (this.three && this.valuesB.size() == 0) {
                    this.three = false;
                    this.valuesB.add(new PointValue(0.0f, 0.0f));
                    this.valuesB.add(new PointValue(6.0f, 0.0f));
                }
                addNewPoint(this.valuesB, i, this.paintValue);
            } else if (i2 == 4) {
                if (this.four && this.valuesW.size() == 0) {
                    this.four = false;
                    this.valuesW.add(new PointValue(0.0f, 0.0f));
                    this.valuesW.add(new PointValue(6.0f, 0.0f));
                }
                addNewPoint(this.valuesW, i, this.paintValue);
            } else if (i2 == 5) {
                if (this.five && this.valuesC.size() == 0) {
                    this.five = false;
                    this.valuesC.add(new PointValue(0.0f, 0.0f));
                    this.valuesC.add(new PointValue(6.0f, 0.0f));
                }
                addNewPoint(this.valuesC, i, this.paintValue);
            } else if (i2 == 6) {
                if (this.six && this.valuesP.size() == 0) {
                    this.six = false;
                    this.valuesP.add(new PointValue(0.0f, 0.0f));
                    this.valuesP.add(new PointValue(6.0f, 0.0f));
                }
                addNewPoint(this.valuesP, i, this.paintValue);
            }
            ReturnValueCallback returnValueCallback = this.mReturnValueCallback;
            if (returnValueCallback != null) {
                returnValueCallback.returnValue(i);
            }
            invalidate();
        }
    }

    public void addNewPoint(List<PointValue> list, int i, int i2) {
        boolean z;
        int i3 = 0;
        while (true) {
            if (i3 >= list.size()) {
                i3 = -1;
                z = false;
                break;
            } else if (list.get(i3).getX() == this.getTimeX / this.getViewX) {
                z = true;
                break;
            } else {
                i3++;
            }
        }
        if (z) {
            PointValue pointValue = list.get(i3);
            this.pointValue = pointValue;
            float f = i;
            this.pointValue.set(pointValue.getX(), f);
            this.getpointY = f;
            return;
        }
        for (int i4 = 0; i4 < list.size(); i4++) {
            float x = list.get(i4).getX();
            float f2 = this.getTimeX;
            float f3 = this.getViewX;
            if (x < f2 / f3) {
                int i5 = i4 + 1;
                if (f2 / f3 < list.get(i5).getX() && getTimeSize(3) < 24) {
                    this.mSendCallBack.clear();
                    this.isAlikee = 1;
                    this.pointValue = list.get(i5);
                    list.add(i5, new PointValue(this.getTimeX / this.getViewX, i));
                    getPoint(this.showLineChart);
                    return;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:123:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x04e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList start(float f, int i) {
        int i2;
        int i3;
        int i4;
        int returnY;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        this.valueList.clear();
        int i10 = 0;
        if (i == 1) {
            if (this.valuesW.size() > 1) {
                int i11 = 0;
                while (true) {
                    if (i11 >= this.valuesW.size() - 1) {
                        i9 = 0;
                        break;
                    }
                    if (f / this.getViewX >= this.valuesW.get(i11).getX()) {
                        int i12 = i11 + 1;
                        if (f / this.getViewX <= this.valuesW.get(i12).getX()) {
                            i9 = returnY(this.valuesW.get(i11).getX(), this.valuesW.get(i11).getY(), this.valuesW.get(i12).getX(), this.valuesW.get(i12).getY(), f / this.getViewX);
                            break;
                        }
                    }
                    i11++;
                }
                i10 = i9;
            }
            i5 = 0;
            i2 = 0;
            i3 = 0;
            i4 = 0;
        } else if (i == 2) {
            if (this.valuesB.size() > 1) {
                for (int i13 = 0; i13 < this.valuesB.size() - 1; i13++) {
                    if (f / this.getViewX >= this.valuesB.get(i13).getX()) {
                        int i14 = i13 + 1;
                        if (f / this.getViewX <= this.valuesB.get(i14).getX()) {
                            i7 = returnY(this.valuesB.get(i13).getX(), this.valuesB.get(i13).getY(), this.valuesB.get(i14).getX(), this.valuesB.get(i14).getY(), f / this.getViewX);
                            break;
                        }
                    }
                }
            }
            i7 = 0;
            if (this.valuesW.size() > 1) {
                int i15 = 0;
                while (true) {
                    if (i15 >= this.valuesW.size() - 1) {
                        i8 = 0;
                        break;
                    }
                    if (f / this.getViewX >= this.valuesW.get(i15).getX()) {
                        int i16 = i15 + 1;
                        if (f / this.getViewX <= this.valuesW.get(i16).getX()) {
                            i8 = returnY(this.valuesW.get(i15).getX(), this.valuesW.get(i15).getY(), this.valuesW.get(i16).getX(), this.valuesW.get(i16).getY(), f / this.getViewX);
                            break;
                        }
                    }
                    i15++;
                }
                i10 = i8;
            }
            i4 = i7;
            i5 = 0;
            i2 = 0;
            i3 = 0;
        } else {
            if (this.valuesR.size() > 1) {
                for (int i17 = 0; i17 < this.valuesR.size() - 1; i17++) {
                    if (f / this.getViewX >= this.valuesR.get(i17).getX()) {
                        int i18 = i17 + 1;
                        if (f / this.getViewX <= this.valuesR.get(i18).getX()) {
                            i2 = returnY(this.valuesR.get(i17).getX(), this.valuesR.get(i17).getY(), this.valuesR.get(i18).getX(), this.valuesR.get(i18).getY(), f / this.getViewX);
                            break;
                        }
                    }
                }
            }
            i2 = 0;
            if (this.valuesG.size() > 1) {
                for (int i19 = 0; i19 < this.valuesG.size() - 1; i19++) {
                    if (f / this.getViewX >= this.valuesG.get(i19).getX()) {
                        int i20 = i19 + 1;
                        if (f / this.getViewX <= this.valuesG.get(i20).getX()) {
                            i3 = returnY(this.valuesG.get(i19).getX(), this.valuesG.get(i19).getY(), this.valuesG.get(i20).getX(), this.valuesG.get(i20).getY(), f / this.getViewX);
                            break;
                        }
                    }
                }
            }
            i3 = 0;
            if (this.valuesB.size() > 1) {
                for (int i21 = 0; i21 < this.valuesB.size() - 1; i21++) {
                    if (f / this.getViewX >= this.valuesB.get(i21).getX()) {
                        int i22 = i21 + 1;
                        if (f / this.getViewX <= this.valuesB.get(i22).getX()) {
                            i4 = returnY(this.valuesB.get(i21).getX(), this.valuesB.get(i21).getY(), this.valuesB.get(i22).getX(), this.valuesB.get(i22).getY(), f / this.getViewX);
                            break;
                        }
                    }
                }
            }
            i4 = 0;
            if ((i == 4 || i == 5) && this.valuesW.size() > 1) {
                for (int i23 = 0; i23 < this.valuesW.size() - 1; i23++) {
                    if (f / this.getViewX >= this.valuesW.get(i23).getX()) {
                        int i24 = i23 + 1;
                        if (f / this.getViewX <= this.valuesW.get(i24).getX()) {
                            returnY = returnY(this.valuesW.get(i23).getX(), this.valuesW.get(i23).getY(), this.valuesW.get(i24).getX(), this.valuesW.get(i24).getY(), f / this.getViewX);
                            break;
                        }
                    }
                }
            }
            returnY = 0;
            if (i == 5) {
                if (this.valuesC.size() > 1) {
                    for (int i25 = 0; i25 < this.valuesC.size() - 1; i25++) {
                        if (f / this.getViewX >= this.valuesC.get(i25).getX()) {
                            int i26 = i25 + 1;
                            if (f / this.getViewX <= this.valuesC.get(i26).getX()) {
                                i6 = returnY(this.valuesC.get(i25).getX(), this.valuesC.get(i25).getY(), this.valuesC.get(i26).getX(), this.valuesC.get(i26).getY(), f / this.getViewX);
                                break;
                            }
                        }
                    }
                }
                i6 = 0;
                if (this.valuesP.size() > 1) {
                    for (int i27 = 0; i27 < this.valuesP.size() - 1; i27++) {
                        if (f / this.getViewX >= this.valuesP.get(i27).getX()) {
                            int i28 = i27 + 1;
                            if (f / this.getViewX <= this.valuesP.get(i28).getX()) {
                                i5 = returnY(this.valuesP.get(i27).getX(), this.valuesP.get(i27).getY(), this.valuesP.get(i28).getX(), this.valuesP.get(i28).getY(), f / this.getViewX);
                                i10 = returnY;
                                break;
                            }
                        }
                    }
                }
                i10 = returnY;
                i5 = 0;
                if (i != 1) {
                    this.valueList.add(0);
                    this.valueList.add(0);
                    this.valueList.add(0);
                    this.valueList.add(Integer.valueOf(i10));
                    this.valueList.add(0);
                    this.valueList.add(0);
                } else if (i == 2) {
                    this.valueList.add(0);
                    this.valueList.add(0);
                    this.valueList.add(Integer.valueOf(i4));
                    this.valueList.add(Integer.valueOf(i10));
                    this.valueList.add(0);
                    this.valueList.add(0);
                } else if (i == 3) {
                    this.valueList.add(Integer.valueOf(i2));
                    this.valueList.add(Integer.valueOf(i3));
                    this.valueList.add(Integer.valueOf(i4));
                    this.valueList.add(0);
                    this.valueList.add(0);
                    this.valueList.add(0);
                } else if (i == 4) {
                    this.valueList.add(Integer.valueOf(i2));
                    this.valueList.add(Integer.valueOf(i3));
                    this.valueList.add(Integer.valueOf(i4));
                    this.valueList.add(Integer.valueOf(i10));
                    this.valueList.add(0);
                    this.valueList.add(0);
                } else if (i == 5) {
                    this.valueList.add(Integer.valueOf(i2));
                    this.valueList.add(Integer.valueOf(i3));
                    this.valueList.add(Integer.valueOf(i4));
                    this.valueList.add(Integer.valueOf(i10));
                    this.valueList.add(Integer.valueOf(i6));
                    this.valueList.add(Integer.valueOf(i5));
                }
                this.getTimeX = f;
                invalidate();
                return this.valueList;
            }
            i10 = returnY;
            i5 = 0;
        }
        i6 = 0;
        if (i != 1) {
        }
        this.getTimeX = f;
        invalidate();
        return this.valueList;
    }

    public int getTimeSize(int i) {
        this.hashMap.clear();
        if (i == 1) {
            if (this.valuesW.size() > 1) {
                for (int i2 = 1; i2 < this.valuesW.size() - 1; i2++) {
                    if (!this.hashMap.containsKey(Float.valueOf(this.valuesW.get(i2).getX()))) {
                        this.hashMap.put(Float.valueOf(this.valuesW.get(i2).getX()), Float.valueOf(this.valuesW.get(i2).getY()));
                    }
                }
            }
        } else if (i == 2) {
            if (this.valuesB.size() > 1) {
                for (int i3 = 1; i3 < this.valuesB.size() - 1; i3++) {
                    if (!this.hashMap.containsKey(Float.valueOf(this.valuesB.get(i3).getX()))) {
                        this.hashMap.put(Float.valueOf(this.valuesB.get(i3).getX()), Float.valueOf(this.valuesB.get(i3).getY()));
                    }
                }
            }
            if (this.valuesW.size() > 1) {
                for (int i4 = 1; i4 < this.valuesW.size() - 1; i4++) {
                    if (!this.hashMap.containsKey(Float.valueOf(this.valuesW.get(i4).getX()))) {
                        this.hashMap.put(Float.valueOf(this.valuesW.get(i4).getX()), Float.valueOf(this.valuesW.get(i4).getY()));
                    }
                }
            }
        } else {
            if (this.valuesR.size() > 1) {
                for (int i5 = 1; i5 < this.valuesR.size() - 1; i5++) {
                    if (!this.hashMap.containsKey(Float.valueOf(this.valuesR.get(i5).getX()))) {
                        this.hashMap.put(Float.valueOf(this.valuesR.get(i5).getX()), Float.valueOf(this.valuesR.get(i5).getY()));
                    }
                }
            }
            if (this.valuesG.size() > 1) {
                for (int i6 = 1; i6 < this.valuesG.size() - 1; i6++) {
                    if (!this.hashMap.containsKey(Float.valueOf(this.valuesG.get(i6).getX()))) {
                        this.hashMap.put(Float.valueOf(this.valuesG.get(i6).getX()), Float.valueOf(this.valuesG.get(i6).getY()));
                    }
                }
            }
            if (this.valuesB.size() > 1) {
                for (int i7 = 1; i7 < this.valuesB.size() - 1; i7++) {
                    if (!this.hashMap.containsKey(Float.valueOf(this.valuesB.get(i7).getX()))) {
                        this.hashMap.put(Float.valueOf(this.valuesB.get(i7).getX()), Float.valueOf(this.valuesB.get(i7).getY()));
                    }
                }
            }
            if ((i == 3 || i == 4) && this.valuesW.size() > 1) {
                for (int i8 = 1; i8 < this.valuesW.size() - 1; i8++) {
                    if (!this.hashMap.containsKey(Float.valueOf(this.valuesW.get(i8).getX()))) {
                        this.hashMap.put(Float.valueOf(this.valuesW.get(i8).getX()), Float.valueOf(this.valuesW.get(i8).getY()));
                    }
                }
            }
            if (i == 5) {
                if (this.valuesC.size() > 1) {
                    for (int i9 = 1; i9 < this.valuesC.size() - 1; i9++) {
                        if (!this.hashMap.containsKey(Float.valueOf(this.valuesC.get(i9).getX()))) {
                            this.hashMap.put(Float.valueOf(this.valuesC.get(i9).getX()), Float.valueOf(this.valuesC.get(i9).getY()));
                        }
                    }
                }
                if (this.valuesP.size() > 1) {
                    for (int i10 = 1; i10 < this.valuesP.size() - 1; i10++) {
                        if (!this.hashMap.containsKey(Float.valueOf(this.valuesP.get(i10).getX()))) {
                            this.hashMap.put(Float.valueOf(this.valuesP.get(i10).getX()), Float.valueOf(this.valuesP.get(i10).getY()));
                        }
                    }
                }
            }
        }
        return this.hashMap.size() + 1;
    }

    public float getListOne(List<PointValue> list) {
        if (list.size() > 1) {
            return list.get(0).getY();
        }
        return 0.0f;
    }

    public void getPoint(int i) {
        this.minList.clear();
        this.hourList.clear();
        this.mSendCallBack.sendValue(new int[]{0, 0, (int) getListOne(this.valuesR), (int) getListOne(this.valuesG), (int) getListOne(this.valuesB), (int) getListOne(this.valuesW), (int) getListOne(this.valuesC), (int) getListOne(this.valuesP)});
        if (i == 1) {
            getRvalue(this.valuesW, 4);
        } else if (i == 2) {
            getRvalue(this.valuesB, 3);
            getRvalue(this.valuesW, 4);
        } else {
            getRvalue(this.valuesR, 1);
            getRvalue(this.valuesG, 2);
            getRvalue(this.valuesB, 3);
            if (i == 3 || i == 4) {
                getRvalue(this.valuesW, 4);
            }
            if (i == 5) {
                getRvalue(this.valuesC, 5);
                getRvalue(this.valuesP, 6);
            }
        }
    }

    public void getRvalue(List<PointValue> list, int i) {
        if (list.size() > 1) {
            for (int i2 = 1; i2 < list.size() - 1; i2++) {
                double x = list.get(i2).getX();
                float f = this.getViewSize;
                double d = f;
                Double.isNaN(d);
                Double.isNaN(x);
                float f2 = (float) (x * (d / 6.0d));
                int i3 = (int) ((24.0f * f2) / f);
                int i4 = (int) (((f2 * 1440.0f) / f) % 60.0f);
                if (i3 == 24) {
                    i3 = 23;
                    i4 = 59;
                }
                if (i3 == 0 && i4 == 0) {
                    i4 = 1;
                }
                if (this.hourList.contains(Integer.valueOf(i3)) && this.minList.contains(Integer.valueOf(i4))) {
                    Log.e(TAG, "getRvalue: ");
                } else {
                    this.hourList.add(Integer.valueOf(i3));
                    this.minList.add(Integer.valueOf(i4));
                    float x2 = list.get(i2).getX();
                    int y = (int) list.get(i2).getY();
                    if (i == 1) {
                        this.mSendCallBack.sendValue(new int[]{i3, i4, y, getOtherValuer(this.valuesG, x2), getOtherValuer(this.valuesB, x2), getOtherValuer(this.valuesW, x2), getOtherValuer(this.valuesC, x2), getOtherValuer(this.valuesP, x2)});
                    } else if (i == 2) {
                        this.mSendCallBack.sendValue(new int[]{i3, i4, getOtherValuer(this.valuesR, x2), y, getOtherValuer(this.valuesB, x2), getOtherValuer(this.valuesW, x2), getOtherValuer(this.valuesC, x2), getOtherValuer(this.valuesP, x2)});
                    } else if (i == 3) {
                        this.mSendCallBack.sendValue(new int[]{i3, i4, getOtherValuer(this.valuesR, x2), getOtherValuer(this.valuesG, x2), y, getOtherValuer(this.valuesW, x2), getOtherValuer(this.valuesC, x2), getOtherValuer(this.valuesP, x2)});
                    } else if (i == 4) {
                        this.mSendCallBack.sendValue(new int[]{i3, i4, getOtherValuer(this.valuesR, x2), getOtherValuer(this.valuesG, x2), getOtherValuer(this.valuesB, x2), y, getOtherValuer(this.valuesC, x2), getOtherValuer(this.valuesP, x2)});
                    } else if (i == 5) {
                        this.mSendCallBack.sendValue(new int[]{i3, i4, getOtherValuer(this.valuesR, x2), getOtherValuer(this.valuesG, x2), getOtherValuer(this.valuesB, x2), getOtherValuer(this.valuesW, x2), y, getOtherValuer(this.valuesP, x2)});
                    } else if (i == 6) {
                        this.mSendCallBack.sendValue(new int[]{i3, i4, getOtherValuer(this.valuesR, x2), getOtherValuer(this.valuesG, x2), getOtherValuer(this.valuesB, x2), getOtherValuer(this.valuesW, x2), getOtherValuer(this.valuesC, x2), y});
                    }
                }
            }
        }
    }

    public int getOtherValuer(List<PointValue> list, float f) {
        int i;
        if (list.size() > 1) {
            for (int i2 = 0; i2 < list.size() - 1; i2++) {
                if (f >= list.get(i2).getX()) {
                    int i3 = i2 + 1;
                    if (f <= list.get(i3).getX()) {
                        i = returnY(list.get(i2).getX(), list.get(i2).getY(), list.get(i3).getX(), list.get(i3).getY(), f);
                        break;
                    }
                }
            }
        }
        i = 0;
        if (i < 1) {
            return 0;
        }
        if (i > 99) {
            return 100;
        }
        return i;
    }

    public void setSendCallBack(SendCallBack sendCallBack) {
        this.mSendCallBack = sendCallBack;
    }

    public void updatePoint(int i) {
        isGone = true;
        if (i == 1) {
            this.valuesW.clear();
        } else if (i == 2) {
            this.valuesB.clear();
            this.valuesW.clear();
        } else {
            this.valuesR.clear();
            this.valuesG.clear();
            this.valuesB.clear();
            if (i == 4) {
                this.valuesW.clear();
            }
            if (i == 5) {
                this.valuesW.clear();
                this.valuesP.clear();
                this.valuesC.clear();
            }
        }
    }

    public void addPoint(int i) {
        if (i == 1) {
            this.valuesR.add(new PointValue(0.0f, 0.0f));
            this.valuesR.add(new PointValue(6.0f, 0.0f));
        } else if (i == 2) {
            this.valuesG.add(new PointValue(0.0f, 0.0f));
            this.valuesG.add(new PointValue(6.0f, 0.0f));
        } else if (i == 3) {
            this.valuesB.add(new PointValue(0.0f, 0.0f));
            this.valuesB.add(new PointValue(6.0f, 0.0f));
        } else if (i == 4) {
            this.valuesW.add(new PointValue(0.0f, 0.0f));
            this.valuesW.add(new PointValue(6.0f, 0.0f));
        } else if (i == 5) {
            this.valuesC.add(new PointValue(0.0f, 0.0f));
            this.valuesC.add(new PointValue(6.0f, 0.0f));
        } else if (i == 6) {
            this.valuesP.add(new PointValue(0.0f, 0.0f));
            this.valuesP.add(new PointValue(6.0f, 0.0f));
        }
    }

    public void setPoint(int i, int i2, int i3, int i4) {
        float f = this.getViewSize;
        float f2 = (((((i * 60) + i2) * f) / 1440.0f) * 6.0f) / f;
        if (i4 == 1) {
            updatePPP(this.valuesR, f2, i3);
            if (f2 == 0.0f) {
                updatePPP(this.valuesR, 6.0f, i3);
            }
        } else if (i4 == 2) {
            updatePPP(this.valuesG, f2, i3);
            if (f2 == 0.0f) {
                updatePPP(this.valuesG, 6.0f, i3);
            }
        } else if (i4 == 3) {
            updatePPP(this.valuesB, f2, i3);
            if (f2 == 0.0f) {
                updatePPP(this.valuesB, 6.0f, i3);
            }
        } else if (i4 == 4) {
            updatePPP(this.valuesW, f2, i3);
            if (f2 == 0.0f) {
                updatePPP(this.valuesW, 6.0f, i3);
            }
        } else if (i4 == 5) {
            updatePPP(this.valuesC, f2, i3);
            if (f2 == 0.0f) {
                updatePPP(this.valuesC, 6.0f, i3);
            }
        } else if (i4 == 6) {
            updatePPP(this.valuesP, f2, i3);
            if (f2 == 0.0f) {
                updatePPP(this.valuesP, 6.0f, i3);
            }
        }
        invalidate();
    }

    public void updatePPP(List<PointValue> list, float f, int i) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).getX() < f) {
                int i3 = i2 + 1;
                if (list.get(i3).getX() > f) {
                    list.add(i3, new PointValue(f, i));
                    Log.e(TAG, "updatePPP:AAAAAAAAA ");
                    return;
                }
            }
            if (list.get(i2).getX() == f) {
                list.get(i2).set(list.get(i2).getX(), i);
                Log.e(TAG, "updatePPPBBBBBBBBBBB: ");
                return;
            }
        }
    }
}
