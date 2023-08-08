package com.home.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class MyGifView extends ImageView {
    public Thread animationThread;
    private ArrayList<Bitmap> bitmapArray;
    private int currentPosition;
    private int interval;
    public Context mContext;
    private Handler mHandler;
    Runnable mStatusChecker;
    public boolean stopRunnable;

    public MyGifView(Context context) {
        this(context, null);
    }

    public MyGifView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MyGifView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.interval = 1000;
        this.stopRunnable = false;
        this.mStatusChecker = new Runnable() { // from class: com.home.view.MyGifView.1
            @Override // java.lang.Runnable
            public void run() {
                if (MyGifView.this.stopRunnable) {
                    return;
                }
                MyGifView myGifView = MyGifView.this;
                myGifView.setImageBitmap((Bitmap) myGifView.bitmapArray.get(MyGifView.this.getNextImagePosition()));
                MyGifView.this.mHandler.postDelayed(MyGifView.this.mStatusChecker, MyGifView.this.interval);
            }
        };
        this.mContext = context;
        this.mHandler = new Handler();
        this.bitmapArray = new ArrayList<>();
    }

    public void setInterval(int i) {
        this.interval = i;
    }

    public void addImages(Bitmap... bitmapArr) {
        for (Bitmap bitmap : bitmapArr) {
            this.bitmapArray.add(bitmap);
        }
    }

    public void addImage(Bitmap bitmap) {
        this.bitmapArray.add(bitmap);
    }

    public void removeImages() {
        if (this.bitmapArray.size() > 0) {
            this.bitmapArray.clear();
        }
    }

    public ArrayList<Bitmap> getImages() {
        return this.bitmapArray;
    }

    public int getInterval() {
        return this.interval;
    }

    public void launchAnimation() {
        this.stopRunnable = false;
        if (this.bitmapArray.size() > 1) {
            this.mStatusChecker.run();
        } else if (this.bitmapArray.size() == 1) {
            setImageBitmap(this.bitmapArray.get(0));
        } else {
            setVisibility(8);
        }
    }

    public int getNextImagePosition() {
        int size = this.bitmapArray.size();
        int i = this.currentPosition;
        if (size > i + 1) {
            this.currentPosition = i + 1;
        } else {
            this.currentPosition = 0;
        }
        return this.currentPosition;
    }

    public void stopImage() {
        this.stopRunnable = true;
        this.mHandler.removeCallbacks(this.mStatusChecker);
        Iterator<Bitmap> it = this.bitmapArray.iterator();
        while (it.hasNext()) {
            it.next().recycle();
        }
    }
}
