package com.luck.picture.lib.anim;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class OptAnimationLoader {
    public static Animation loadAnimation(Context context, int i) throws Resources.NotFoundException {
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                xmlResourceParser = context.getResources().getAnimation(i);
                return createAnimationFromXml(context, xmlResourceParser);
            } catch (IOException e) {
                Resources.NotFoundException notFoundException = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(i));
                notFoundException.initCause(e);
                throw notFoundException;
            } catch (XmlPullParserException e2) {
                Resources.NotFoundException notFoundException2 = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(i));
                notFoundException2.initCause(e2);
                throw notFoundException2;
            }
        } finally {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }

    private static Animation createAnimationFromXml(Context context, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        return createAnimationFromXml(context, xmlPullParser, null, Xml.asAttributeSet(xmlPullParser));
    }

    private static Animation createAnimationFromXml(Context context, XmlPullParser xmlPullParser, AnimationSet animationSet, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int depth = xmlPullParser.getDepth();
        AnimationSet animationSet2 = null;
        while (true) {
            int next = xmlPullParser.next();
            if ((next != 3 || xmlPullParser.getDepth() > depth) && next != 1) {
                if (next == 2) {
                    String name = xmlPullParser.getName();
                    if (name.equals("set")) {
                        AnimationSet animationSet3 = new AnimationSet(context, attributeSet);
                        createAnimationFromXml(context, xmlPullParser, animationSet3, attributeSet);
                        animationSet2 = animationSet3;
                    } else if (name.equals("alpha")) {
                        animationSet2 = new AlphaAnimation(context, attributeSet);
                    } else if (name.equals("scale")) {
                        animationSet2 = new ScaleAnimation(context, attributeSet);
                    } else if (name.equals(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)) {
                        animationSet2 = new RotateAnimation(context, attributeSet);
                    } else if (name.equals("translate")) {
                        animationSet2 = new TranslateAnimation(context, attributeSet);
                    } else {
                        try {
                            animationSet2 = (Animation) Class.forName(name).getConstructor(Context.class, AttributeSet.class).newInstance(context, attributeSet);
                        } catch (Exception e) {
                            throw new RuntimeException("Unknown animation name: " + xmlPullParser.getName() + " error:" + e.getMessage());
                        }
                    }
                    if (animationSet != null) {
                        animationSet.addAnimation(animationSet2);
                    }
                }
            }
        }
        return animationSet2;
    }
}
