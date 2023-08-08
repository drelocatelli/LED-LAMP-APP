package com.forum.login;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.home.utils.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class ProgressBarCircular extends CustomView {
    static final String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    int arcD;
    int arcO;
    int backgroundColor;
    int cont;
    boolean firstAnimationOver;
    int innerCircleColor;
    int limite;
    float radius1;
    float radius2;
    float rotateAngle;

    public ProgressBarCircular(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.radius1 = 0.0f;
        this.radius2 = 0.0f;
        this.cont = 0;
        this.firstAnimationOver = false;
        this.arcD = 1;
        this.arcO = 0;
        this.rotateAngle = 0.0f;
        this.limite = 0;
        setAttributes(context, attributeSet);
    }

    protected void setAttributes(Context context, AttributeSet attributeSet) {
        setMinimumHeight(Utils.dpToPx(32.0f, getResources()));
        setMinimumWidth(Utils.dpToPx(32.0f, getResources()));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressBarCircular);
        this.innerCircleColor = obtainStyledAttributes.getInt(0, getResources().getColor(R.color.grayLight));
        int i = obtainStyledAttributes.getInt(1, getResources().getColor(R.color.colorPrimary));
        this.backgroundColor = i;
        setBackgroundColor(i);
        setMinimumHeight(Utils.dpToPx(3.0f, getResources()));
    }

    protected int makePressColor() {
        int i = this.backgroundColor;
        return Color.argb(128, (i >> 16) & 255, (i >> 8) & 255, (i >> 0) & 255);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSecondAnimation(canvas);
        invalidate();
    }

    private void drawFirstAnimation(Canvas canvas) {
        float f;
        if (this.radius1 < getWidth() / 2) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            this.radius1 = this.radius1 >= ((float) (getWidth() / 2)) ? getWidth() / 2.0f : this.radius1 + 1.0f;
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, this.radius1 - 1.0f, paint);
            return;
        }
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(makePressColor());
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getHeight() / 2) - 1, paint2);
        Paint paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setColor(this.innerCircleColor);
        if (this.cont >= 50) {
            this.radius2 = this.radius2 >= ((float) (getWidth() / 2)) ? getWidth() / 2.0f : this.radius2 + 1.0f;
        } else {
            if (this.radius2 >= (getWidth() / 2) - Utils.dpToPx(4.0f, getResources())) {
                f = (getWidth() / 2.0f) - Utils.dpToPx(4.0f, getResources());
            } else {
                f = this.radius2 + 1.0f;
            }
            this.radius2 = f;
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, this.radius2 - 1.0f, paint3);
        if (this.radius2 >= (getWidth() / 2) - Utils.dpToPx(4.0f, getResources())) {
            this.cont++;
        }
        if (this.radius2 >= getWidth() / 2) {
            this.firstAnimationOver = true;
        }
    }

    private void drawSecondAnimation(Canvas canvas) {
        int i = this.arcO;
        int i2 = this.limite;
        if (i == i2) {
            this.arcD += 6;
        }
        int i3 = this.arcD;
        if (i3 >= 290 || i > i2) {
            this.arcO = i + 6;
            this.arcD = i3 - 6;
        }
        int i4 = this.arcO;
        if (i4 > i2 + 290) {
            this.limite = i4;
            this.arcO = i4;
            this.arcD = 1;
        }
        float f = this.rotateAngle + 4.0f;
        this.rotateAngle = f;
        canvas.rotate(f, getWidth() / 2, getHeight() / 2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.backgroundColor);
        canvas.drawArc(new RectF(1.0f, 1.0f, getWidth() - 1, getHeight() - 1), this.arcO, this.arcD, true, paint);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(this.innerCircleColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, ((getWidth() / 2) - 1) - Utils.dpToPx(4.0f, getResources()), paint2);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(getResources().getColor(17170445));
        this.backgroundColor = i;
    }
}
