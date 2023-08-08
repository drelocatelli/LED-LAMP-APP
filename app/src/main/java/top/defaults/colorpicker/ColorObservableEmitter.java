package top.defaults.colorpicker;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
class ColorObservableEmitter implements ColorObservable {
    private int color;
    private List<ColorObserver> observers = new ArrayList();

    @Override // top.defaults.colorpicker.ColorObservable
    public void subscribe(ColorObserver colorObserver) {
        if (colorObserver == null) {
            return;
        }
        this.observers.add(colorObserver);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public void unsubscribe(ColorObserver colorObserver) {
        if (colorObserver == null) {
            return;
        }
        this.observers.remove(colorObserver);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public int getColor() {
        return this.color;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onColor(int i, boolean z, boolean z2) {
        this.color = i;
        for (ColorObserver colorObserver : this.observers) {
            colorObserver.onColor(i, z, z2);
        }
    }
}
