package com.forum.im.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ledlamp.R;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
public class BubbleImageView extends ImageView {
    private static final int ERROR_INT = 0;
    private static final int OK_INT = 1;
    private Handler bitmapHandler;
    private Context context;
    private Bitmap iconBitmap;
    private int res;

    public BubbleImageView(Context context) {
        super(context);
        this.bitmapHandler = new Handler() { // from class: com.forum.im.widget.BubbleImageView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                Bitmap decodeResource = BitmapFactory.decodeResource(BubbleImageView.this.getResources(), BubbleImageView.this.res);
                BubbleImageView bubbleImageView = BubbleImageView.this;
                BubbleImageView.this.setImageBitmap(bubbleImageView.getRoundCornerImage(decodeResource, bubbleImageView.iconBitmap));
            }
        };
        this.context = context;
    }

    public BubbleImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.bitmapHandler = new Handler() { // from class: com.forum.im.widget.BubbleImageView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                Bitmap decodeResource = BitmapFactory.decodeResource(BubbleImageView.this.getResources(), BubbleImageView.this.res);
                BubbleImageView bubbleImageView = BubbleImageView.this;
                BubbleImageView.this.setImageBitmap(bubbleImageView.getRoundCornerImage(decodeResource, bubbleImageView.iconBitmap));
            }
        };
        this.context = context;
    }

    public BubbleImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.bitmapHandler = new Handler() { // from class: com.forum.im.widget.BubbleImageView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                Bitmap decodeResource = BitmapFactory.decodeResource(BubbleImageView.this.getResources(), BubbleImageView.this.res);
                BubbleImageView bubbleImageView = BubbleImageView.this;
                BubbleImageView.this.setImageBitmap(bubbleImageView.getRoundCornerImage(decodeResource, bubbleImageView.iconBitmap));
            }
        };
        this.context = context;
    }

    public void load(String str, int i, int i2) {
        setImageResource(i2);
        this.res = i;
        loadCover(this, str, getContext());
    }

    public static void loadCover(ImageView imageView, String str, Context context) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().frame(1000000L).centerCrop().fitCenter().override(HttpStatus.SC_BAD_REQUEST, HttpStatus.SC_BAD_REQUEST).placeholder(R.drawable.default_common).error(R.drawable.default_common)).load(str).into(imageView);
    }

    public void setLocalImageBitmap(Bitmap bitmap, int i) {
        setImageBitmap(getRoundCornerImage(BitmapFactory.decodeResource(getResources(), i), bitmap));
    }

    public Bitmap getRoundCornerImage(Bitmap bitmap, Bitmap bitmap2) {
        int i;
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int i2 = 100;
        if (height != 0) {
            double d = width;
            Double.isNaN(d);
            double d2 = height;
            Double.isNaN(d2);
            double d3 = (d * 1.0d) / d2;
            if (width >= height) {
                int bitmapWidth = getBitmapWidth();
                double d4 = bitmapWidth;
                Double.isNaN(d4);
                i2 = bitmapWidth;
                i = (int) (d4 / d3);
            } else {
                i = getBitmapHeight();
                double d5 = i;
                Double.isNaN(d5);
                i2 = (int) (d5 * d3);
            }
        } else {
            i = 100;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, i2, i);
        Rect rect2 = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        paint.setAntiAlias(true);
        new NinePatch(bitmap, bitmap.getNinePatchChunk(), null).draw(canvas, rect);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, rect2, rect, paint);
        return createBitmap;
    }

    public int getScreenWidth(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public int getScreenHeight(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getHeight();
    }

    public int getBitmapWidth() {
        return getScreenWidth(this.context) / 3;
    }

    public int getBitmapHeight() {
        return getScreenHeight(this.context) / 4;
    }
}
