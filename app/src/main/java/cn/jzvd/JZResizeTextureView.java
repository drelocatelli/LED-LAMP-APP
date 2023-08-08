package cn.jzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

/* loaded from: classes.dex */
public class JZResizeTextureView extends TextureView {
    protected static final String TAG = "JZResizeTextureView";
    public int currentVideoHeight;
    public int currentVideoWidth;

    public JZResizeTextureView(Context context) {
        super(context);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public JZResizeTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public void setVideoSize(int i, int i2) {
        if (this.currentVideoWidth == i && this.currentVideoHeight == i2) {
            return;
        }
        this.currentVideoWidth = i;
        this.currentVideoHeight = i2;
        requestLayout();
    }

    @Override // android.view.View
    public void setRotation(float f) {
        if (f != getRotation()) {
            super.setRotation(f);
            requestLayout();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        Log.i(TAG, "onMeasure  [" + hashCode() + "] ");
        int rotation = (int) getRotation();
        int i7 = this.currentVideoWidth;
        int i8 = this.currentVideoHeight;
        int measuredHeight = ((View) getParent()).getMeasuredHeight();
        int measuredWidth = ((View) getParent()).getMeasuredWidth();
        if (measuredWidth != 0 && measuredHeight != 0 && i7 != 0 && i8 != 0 && JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE == 1) {
            if (rotation == 90 || rotation == 270) {
                measuredWidth = measuredHeight;
                measuredHeight = measuredWidth;
            }
            i8 = (i7 * measuredHeight) / measuredWidth;
        }
        if (rotation == 90 || rotation == 270) {
            i3 = i;
            i4 = i2;
        } else {
            i4 = i;
            i3 = i2;
        }
        int defaultSize = getDefaultSize(i7, i4);
        int defaultSize2 = getDefaultSize(i8, i3);
        if (i7 > 0 && i8 > 0) {
            int mode = View.MeasureSpec.getMode(i4);
            int size = View.MeasureSpec.getSize(i4);
            int mode2 = View.MeasureSpec.getMode(i3);
            int size2 = View.MeasureSpec.getSize(i3);
            Log.i(TAG, "widthMeasureSpec  [" + View.MeasureSpec.toString(i4) + "]");
            Log.i(TAG, "heightMeasureSpec [" + View.MeasureSpec.toString(i3) + "]");
            if (mode == 1073741824 && mode2 == 1073741824) {
                int i9 = i7 * size2;
                int i10 = size * i8;
                if (i9 < i10) {
                    defaultSize = i9 / i8;
                } else if (i9 > i10) {
                    i6 = i10 / i7;
                    defaultSize = size;
                    defaultSize2 = i6;
                } else {
                    defaultSize = size;
                }
                defaultSize2 = size2;
            } else if (mode == 1073741824) {
                i6 = (size * i8) / i7;
                if (mode2 == Integer.MIN_VALUE && i6 > size2) {
                    defaultSize = (size2 * i7) / i8;
                    defaultSize2 = size2;
                }
                defaultSize = size;
                defaultSize2 = i6;
            } else if (mode2 == 1073741824) {
                i5 = (size2 * i7) / i8;
                if (mode == Integer.MIN_VALUE && i5 > size) {
                    i6 = (size * i8) / i7;
                    defaultSize = size;
                    defaultSize2 = i6;
                }
                defaultSize = i5;
                defaultSize2 = size2;
            } else {
                if (mode2 != Integer.MIN_VALUE || i8 <= size2) {
                    i5 = i7;
                    size2 = i8;
                } else {
                    i5 = (size2 * i7) / i8;
                }
                if (mode == Integer.MIN_VALUE && i5 > size) {
                    i6 = (size * i8) / i7;
                    defaultSize = size;
                    defaultSize2 = i6;
                }
                defaultSize = i5;
                defaultSize2 = size2;
            }
        }
        if (measuredWidth != 0 && measuredHeight != 0 && i7 != 0 && i8 != 0) {
            if (JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE != 3) {
                if (JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE == 2) {
                    if (rotation == 90 || rotation == 270) {
                        int i11 = measuredWidth;
                        measuredWidth = measuredHeight;
                        measuredHeight = i11;
                    }
                    double d = i8;
                    double d2 = i7;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    double d3 = d / d2;
                    double d4 = measuredHeight;
                    double d5 = measuredWidth;
                    Double.isNaN(d4);
                    Double.isNaN(d5);
                    double d6 = d4 / d5;
                    if (d3 > d6) {
                        double d7 = defaultSize;
                        Double.isNaN(d5);
                        Double.isNaN(d7);
                        double d8 = d5 / d7;
                        double d9 = defaultSize2;
                        Double.isNaN(d9);
                        i8 = (int) (d8 * d9);
                        i7 = measuredWidth;
                    } else if (d3 < d6) {
                        double d10 = defaultSize2;
                        Double.isNaN(d4);
                        Double.isNaN(d10);
                        double d11 = d4 / d10;
                        double d12 = defaultSize;
                        Double.isNaN(d12);
                        i7 = (int) (d11 * d12);
                        i8 = measuredHeight;
                    }
                }
            }
            setMeasuredDimension(i7, i8);
        }
        i7 = defaultSize;
        i8 = defaultSize2;
        setMeasuredDimension(i7, i8);
    }
}
