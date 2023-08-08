package top.defaults.colorpicker;

/* loaded from: classes.dex */
public interface ColorObservable {
    int getColor();

    void subscribe(ColorObserver colorObserver);

    void unsubscribe(ColorObserver colorObserver);
}
