package pl.droidsonroids.gif;

/* loaded from: classes.dex */
public class GifOptions {
    boolean inIsOpaque;
    char inSampleSize;

    public GifOptions() {
        reset();
    }

    private void reset() {
        this.inSampleSize = (char) 1;
        this.inIsOpaque = false;
    }

    public void setInSampleSize(int i) {
        if (i < 1 || i > 65535) {
            this.inSampleSize = (char) 1;
        } else {
            this.inSampleSize = (char) i;
        }
    }

    public void setInIsOpaque(boolean z) {
        this.inIsOpaque = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFrom(GifOptions gifOptions) {
        if (gifOptions == null) {
            reset();
            return;
        }
        this.inIsOpaque = gifOptions.inIsOpaque;
        this.inSampleSize = gifOptions.inSampleSize;
    }
}
