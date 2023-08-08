package com.forum.im.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import com.forum.im.utils.FaceData;
import com.forum.im.utils.GifOpenHelper;
import com.forum.im.utils.ScreenUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class GifTextView extends EditText {
    private static final int DELAYED = 300;
    private Handler handler;
    private boolean isGif;
    private String myText;
    public TextRunnable rTextRunnable;
    private ArrayList<SpanInfo> spanInfoList;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SpanInfo {
        ArrayList<Bitmap> mapList = new ArrayList<>();
        int delay = 0;
        int currentFrameIndex = 0;
        int frameCount = 0;
        int end = 0;
        int start = 0;

        public SpanInfo() {
        }
    }

    public GifTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.spanInfoList = null;
        setFocusableInTouchMode(false);
    }

    public GifTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.spanInfoList = null;
        setFocusableInTouchMode(false);
    }

    public GifTextView(Context context) {
        super(context);
        this.spanInfoList = null;
        setFocusableInTouchMode(false);
    }

    private boolean parseText(String str) {
        this.myText = str;
        Matcher matcher = Pattern.compile("\\[[^\\]]+\\]").matcher(str);
        boolean z = false;
        while (matcher.find()) {
            Integer num = FaceData.gifFaceInfo.get(matcher.group());
            if (num != null) {
                if (this.isGif) {
                    parseGif(num.intValue(), matcher.start(), matcher.end());
                } else {
                    parseBmp(num.intValue(), matcher.start(), matcher.end());
                }
            }
            z = true;
        }
        return z;
    }

    private void parseBmp(int i, int i2, int i3) {
        Bitmap decodeResource = BitmapFactory.decodeResource(getContext().getResources(), i);
        new ImageSpan(getContext(), decodeResource);
        SpanInfo spanInfo = new SpanInfo();
        spanInfo.currentFrameIndex = 0;
        spanInfo.frameCount = 1;
        spanInfo.start = i2;
        spanInfo.end = i3;
        spanInfo.delay = 100;
        spanInfo.mapList.add(decodeResource);
        this.spanInfoList.add(spanInfo);
    }

    private void parseGif(int i, int i2, int i3) {
        GifOpenHelper gifOpenHelper = new GifOpenHelper();
        gifOpenHelper.read(getContext().getResources().openRawResource(i));
        SpanInfo spanInfo = new SpanInfo();
        spanInfo.currentFrameIndex = 0;
        spanInfo.frameCount = gifOpenHelper.getFrameCount();
        spanInfo.start = i2;
        spanInfo.end = i3;
        spanInfo.mapList.add(gifOpenHelper.getImage());
        for (int i4 = 1; i4 < gifOpenHelper.getFrameCount(); i4++) {
            spanInfo.mapList.add(gifOpenHelper.nextBitmap());
        }
        spanInfo.delay = gifOpenHelper.nextDelay();
        this.spanInfoList.add(spanInfo);
    }

    public void setSpanText(Handler handler, String str, boolean z) {
        this.handler = handler;
        this.isGif = z;
        this.spanInfoList = new ArrayList<>();
        if (parseText(str)) {
            if (parseMessage(this)) {
                startPost();
                return;
            }
            return;
        }
        setText(this.myText);
    }

    public boolean parseMessage(GifTextView gifTextView) {
        Bitmap createScaledBitmap;
        String str = gifTextView.myText;
        if (str != null && !str.equals("")) {
            SpannableString spannableString = new SpannableString("" + gifTextView.myText);
            int i = 0;
            for (int i2 = 0; i2 < gifTextView.spanInfoList.size(); i2++) {
                SpanInfo spanInfo = gifTextView.spanInfoList.get(i2);
                if (spanInfo.mapList.size() > 1) {
                    i++;
                }
                Bitmap bitmap = spanInfo.mapList.get(spanInfo.currentFrameIndex);
                spanInfo.currentFrameIndex = (spanInfo.currentFrameIndex + 1) % spanInfo.frameCount;
                int dip2px = ScreenUtil.dip2px(gifTextView.getContext(), 30.0f);
                if (i != 0) {
                    createScaledBitmap = Bitmap.createScaledBitmap(bitmap, dip2px, dip2px, true);
                } else {
                    createScaledBitmap = Bitmap.createScaledBitmap(bitmap, dip2px, dip2px, true);
                }
                ImageSpan imageSpan = new ImageSpan(gifTextView.getContext(), createScaledBitmap);
                if (spanInfo.end > spannableString.length()) {
                    break;
                }
                spannableString.setSpan(imageSpan, spanInfo.start, spanInfo.end, 33);
            }
            gifTextView.setText(spannableString);
            if (i != 0) {
                return true;
            }
        }
        return false;
    }

    public void startPost() {
        TextRunnable textRunnable = new TextRunnable(this);
        this.rTextRunnable = textRunnable;
        this.handler.post(textRunnable);
    }

    /* loaded from: classes.dex */
    public static final class TextRunnable implements Runnable {
        private final WeakReference<GifTextView> mWeakReference;

        public TextRunnable(GifTextView gifTextView) {
            this.mWeakReference = new WeakReference<>(gifTextView);
        }

        @Override // java.lang.Runnable
        public void run() {
            GifTextView gifTextView = this.mWeakReference.get();
            if (gifTextView == null || !gifTextView.parseMessage(gifTextView)) {
                return;
            }
            gifTextView.handler.postDelayed(this, 300L);
        }
    }
}
