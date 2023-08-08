package com.luck.picture.lib.tools;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureMimeType;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class StringUtils {
    public static void tempTextFont(TextView textView, int i) {
        String string;
        String trim = textView.getText().toString().trim();
        if (i == PictureMimeType.ofAudio()) {
            string = textView.getContext().getString(R.string.picture_empty_audio_title);
        } else {
            string = textView.getContext().getString(R.string.picture_empty_title);
        }
        String str = string + trim;
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), string.length(), str.length(), 33);
        textView.setText(spannableString);
    }

    public static int stringToInt(String str) {
        if (Pattern.compile("^[-\\+]?[\\d]+$").matcher(str).matches()) {
            return Integer.valueOf(str).intValue();
        }
        return 0;
    }

    public static String getToastMsg(Context context, String str, int i) {
        return PictureMimeType.eqVideo(str) ? context.getString(R.string.picture_message_video_max_num, Integer.valueOf(i)) : PictureMimeType.eqAudio(str) ? context.getString(R.string.picture_message_audio_max_num, Integer.valueOf(i)) : context.getString(R.string.picture_message_max_num, Integer.valueOf(i));
    }
}
