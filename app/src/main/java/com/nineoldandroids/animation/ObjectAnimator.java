package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class ObjectAnimator extends ValueAnimator {
    private static final boolean DBG = false;
    private static final Map<String, Property> PROXY_PROPERTIES;
    private Property mProperty;
    private String mPropertyName;
    private Object mTarget;

    static {
        HashMap hashMap = new HashMap();
        PROXY_PROPERTIES = hashMap;
        hashMap.put("alpha", PreHoneycombCompat.ALPHA);
        hashMap.put("pivotX", PreHoneycombCompat.PIVOT_X);
        hashMap.put("pivotY", PreHoneycombCompat.PIVOT_Y);
        hashMap.put("translationX", PreHoneycombCompat.TRANSLATION_X);
        hashMap.put("translationY", PreHoneycombCompat.TRANSLATION_Y);
        hashMap.put("rotation", PreHoneycombCompat.ROTATION);
        hashMap.put("rotationX", PreHoneycombCompat.ROTATION_X);
        hashMap.put("rotationY", PreHoneycombCompat.ROTATION_Y);
        hashMap.put("scaleX", PreHoneycombCompat.SCALE_X);
        hashMap.put("scaleY", PreHoneycombCompat.SCALE_Y);
        hashMap.put("scrollX", PreHoneycombCompat.SCROLL_X);
        hashMap.put("scrollY", PreHoneycombCompat.SCROLL_Y);
        hashMap.put("x", PreHoneycombCompat.X);
        hashMap.put("y", PreHoneycombCompat.Y);
    }

    public void setPropertyName(String str) {
        if (this.mValues != null) {
            PropertyValuesHolder propertyValuesHolder = this.mValues[0];
            String propertyName = propertyValuesHolder.getPropertyName();
            propertyValuesHolder.setPropertyName(str);
            this.mValuesMap.remove(propertyName);
            this.mValuesMap.put(str, propertyValuesHolder);
        }
        this.mPropertyName = str;
        this.mInitialized = false;
    }

    public void setProperty(Property property) {
        if (this.mValues != null) {
            PropertyValuesHolder propertyValuesHolder = this.mValues[0];
            String propertyName = propertyValuesHolder.getPropertyName();
            propertyValuesHolder.setProperty(property);
            this.mValuesMap.remove(propertyName);
            this.mValuesMap.put(this.mPropertyName, propertyValuesHolder);
        }
        if (this.mProperty != null) {
            this.mPropertyName = property.getName();
        }
        this.mProperty = property;
        this.mInitialized = false;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    public ObjectAnimator() {
    }

    private ObjectAnimator(Object obj, String str) {
        this.mTarget = obj;
        setPropertyName(str);
    }

    private <T> ObjectAnimator(T t, Property<T, ?> property) {
        this.mTarget = t;
        setProperty(property);
    }

    public static ObjectAnimator ofInt(Object obj, String str, int... iArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(obj, str);
        objectAnimator.setIntValues(iArr);
        return objectAnimator;
    }

    public static <T> ObjectAnimator ofInt(T t, Property<T, Integer> property, int... iArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(t, property);
        objectAnimator.setIntValues(iArr);
        return objectAnimator;
    }

    public static ObjectAnimator ofFloat(Object obj, String str, float... fArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(obj, str);
        objectAnimator.setFloatValues(fArr);
        return objectAnimator;
    }

    public static <T> ObjectAnimator ofFloat(T t, Property<T, Float> property, float... fArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(t, property);
        objectAnimator.setFloatValues(fArr);
        return objectAnimator;
    }

    public static ObjectAnimator ofObject(Object obj, String str, TypeEvaluator typeEvaluator, Object... objArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(obj, str);
        objectAnimator.setObjectValues(objArr);
        objectAnimator.setEvaluator(typeEvaluator);
        return objectAnimator;
    }

    public static <T, V> ObjectAnimator ofObject(T t, Property<T, V> property, TypeEvaluator<V> typeEvaluator, V... vArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(t, property);
        objectAnimator.setObjectValues(vArr);
        objectAnimator.setEvaluator(typeEvaluator);
        return objectAnimator;
    }

    public static ObjectAnimator ofPropertyValuesHolder(Object obj, PropertyValuesHolder... propertyValuesHolderArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.mTarget = obj;
        objectAnimator.setValues(propertyValuesHolderArr);
        return objectAnimator;
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public void setIntValues(int... iArr) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofInt(property, iArr));
                return;
            } else {
                setValues(PropertyValuesHolder.ofInt(this.mPropertyName, iArr));
                return;
            }
        }
        super.setIntValues(iArr);
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public void setFloatValues(float... fArr) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofFloat(property, fArr));
                return;
            } else {
                setValues(PropertyValuesHolder.ofFloat(this.mPropertyName, fArr));
                return;
            }
        }
        super.setFloatValues(fArr);
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public void setObjectValues(Object... objArr) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofObject(property, (TypeEvaluator) null, objArr));
                return;
            } else {
                setValues(PropertyValuesHolder.ofObject(this.mPropertyName, (TypeEvaluator) null, objArr));
                return;
            }
        }
        super.setObjectValues(objArr);
    }

    @Override // com.nineoldandroids.animation.ValueAnimator, com.nineoldandroids.animation.Animator
    public void start() {
        super.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nineoldandroids.animation.ValueAnimator
    public void initAnimation() {
        if (this.mInitialized) {
            return;
        }
        if (this.mProperty == null && AnimatorProxy.NEEDS_PROXY && (this.mTarget instanceof View)) {
            Map<String, Property> map = PROXY_PROPERTIES;
            if (map.containsKey(this.mPropertyName)) {
                setProperty(map.get(this.mPropertyName));
            }
        }
        int length = this.mValues.length;
        for (int i = 0; i < length; i++) {
            this.mValues[i].setupSetterAndGetter(this.mTarget);
        }
        super.initAnimation();
    }

    @Override // com.nineoldandroids.animation.ValueAnimator, com.nineoldandroids.animation.Animator
    public ObjectAnimator setDuration(long j) {
        super.setDuration(j);
        return this;
    }

    public Object getTarget() {
        return this.mTarget;
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setTarget(Object obj) {
        Object obj2 = this.mTarget;
        if (obj2 != obj) {
            this.mTarget = obj;
            if (obj2 == null || obj == null || obj2.getClass() != obj.getClass()) {
                this.mInitialized = false;
            }
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setupStartValues() {
        initAnimation();
        int length = this.mValues.length;
        for (int i = 0; i < length; i++) {
            this.mValues[i].setupStartValue(this.mTarget);
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setupEndValues() {
        initAnimation();
        int length = this.mValues.length;
        for (int i = 0; i < length; i++) {
            this.mValues[i].setupEndValue(this.mTarget);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nineoldandroids.animation.ValueAnimator
    public void animateValue(float f) {
        super.animateValue(f);
        int length = this.mValues.length;
        for (int i = 0; i < length; i++) {
            this.mValues[i].setAnimatedValue(this.mTarget);
        }
    }

    @Override // com.nineoldandroids.animation.ValueAnimator, com.nineoldandroids.animation.Animator
    /* renamed from: clone */
    public ObjectAnimator mo57clone() {
        return (ObjectAnimator) super.mo57clone();
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public String toString() {
        String str = "ObjectAnimator@" + Integer.toHexString(hashCode()) + ", target " + this.mTarget;
        if (this.mValues != null) {
            for (int i = 0; i < this.mValues.length; i++) {
                str = str + "\n    " + this.mValues[i].toString();
            }
        }
        return str;
    }
}
