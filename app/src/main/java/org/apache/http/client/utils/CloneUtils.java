package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class CloneUtils {
    public static <T> T cloneObject(T t) throws CloneNotSupportedException {
        if (t == null) {
            return null;
        }
        if (t instanceof Cloneable) {
            try {
                Class[] clsArr = null;
                try {
                    Object[] objArr = null;
                    return (T) t.getClass().getMethod("clone", null).invoke(t, null);
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessError(e.getMessage());
                } catch (InvocationTargetException e2) {
                    Throwable cause = e2.getCause();
                    if (cause instanceof CloneNotSupportedException) {
                        throw ((CloneNotSupportedException) cause);
                    }
                    throw new Error("Unexpected exception", cause);
                }
            } catch (NoSuchMethodException e3) {
                throw new NoSuchMethodError(e3.getMessage());
            }
        }
        throw new CloneNotSupportedException();
    }

    public static Object clone(Object obj) throws CloneNotSupportedException {
        return cloneObject(obj);
    }

    private CloneUtils() {
    }
}
