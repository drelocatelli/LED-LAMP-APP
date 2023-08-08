package org.apache.http.util;

/* loaded from: classes.dex */
public final class TextUtils {
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isBlank(CharSequence charSequence) {
        if (charSequence == null) {
            return true;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsBlanks(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            if (Character.isWhitespace(charSequence.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
