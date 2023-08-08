package com.forum.im.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.style.ImageSpan;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class SmileUtils {
    public static final String f1 = "[:f1]";
    public static final String f10 = "[:f10]";
    public static final String f11 = "[:f11]";
    public static final String f12 = "[:f12]";
    public static final String f13 = "[:f13]";
    public static final String f14 = "[:f14]";
    public static final String f15 = "[:f15]";
    public static final String f16 = "[:f16]";
    public static final String f17 = "[:f17]";
    public static final String f18 = "[:f18]";
    public static final String f19 = "[:f19]";
    public static final String f2 = "[:f2]";
    public static final String f20 = "[:f20]";
    public static final String f21 = "[:f21]";
    public static final String f22 = "[:f22]";
    public static final String f23 = "[:f23]";
    public static final String f24 = "[:f24]";
    public static final String f25 = "[:f25]";
    public static final String f26 = "[:f26]";
    public static final String f27 = "[:f27]";
    public static final String f28 = "[:f28]";
    public static final String f29 = "[:f29]";
    public static final String f3 = "[:f3]";
    public static final String f30 = "[:f30]";
    public static final String f31 = "[:f31]";
    public static final String f32 = "[:f32]";
    public static final String f33 = "[:f33]";
    public static final String f34 = "[:f34]";
    public static final String f35 = "[:f35]";
    public static final String f36 = "[:f36]";
    public static final String f37 = "[:f37]";
    public static final String f38 = "[:f38]";
    public static final String f39 = "[:f39]";
    public static final String f4 = "[:f4]";
    public static final String f40 = "[:f40]";
    public static final String f5 = "[:f5]";
    public static final String f6 = "[:f6]";
    public static final String f7 = "[:f7]";
    public static final String f8 = "[:f8]";
    public static final String f9 = "[:f9]";
    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();
    private static final Map<Pattern, Integer> emoticons = new HashMap();

    private static void addPattern(Map<Pattern, Integer> map, String str, int i) {
        map.put(Pattern.compile(Pattern.quote(str)), Integer.valueOf(i));
    }

    public static boolean addSmiles(Context context, Spannable spannable) {
        ImageSpan[] imageSpanArr;
        boolean z;
        boolean z2 = false;
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                for (ImageSpan imageSpan : (ImageSpan[]) spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                    if (spannable.getSpanStart(imageSpan) < matcher.start() || spannable.getSpanEnd(imageSpan) > matcher.end()) {
                        z = false;
                        break;
                    }
                    spannable.removeSpan(imageSpan);
                }
                z = true;
                if (z) {
                    spannable.setSpan(new ImageSpan(context, entry.getValue().intValue()), matcher.start(), matcher.end(), 33);
                    z2 = true;
                }
            }
        }
        return z2;
    }

    public static Spannable getSmiledText(Context context, CharSequence charSequence) {
        Spannable newSpannable = spannableFactory.newSpannable(charSequence);
        addSmiles(context, newSpannable);
        return newSpannable;
    }

    public static boolean containsKey(String str) {
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            if (entry.getKey().matcher(str).find()) {
                return true;
            }
        }
        return false;
    }
}
