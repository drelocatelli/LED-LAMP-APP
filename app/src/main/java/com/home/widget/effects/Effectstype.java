package com.home.widget.effects;

/* loaded from: classes.dex */
public enum Effectstype {
    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    
    private Class effectsClazz;

    Effectstype(Class cls) {
        this.effectsClazz = cls;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) this.effectsClazz.newInstance();
        } catch (Exception unused) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
