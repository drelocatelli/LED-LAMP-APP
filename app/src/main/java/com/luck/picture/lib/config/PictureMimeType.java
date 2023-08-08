package com.luck.picture.lib.config;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.home.utils.Utils;
import com.luck.picture.lib.R;
import com.luck.picture.lib.tools.PictureFileUtils;
import java.io.File;
import org.apache.http.HttpHost;
import org.apache.http.message.TokenParser;

/* loaded from: classes.dex */
public final class PictureMimeType {
    public static final String JPEG = ".jpeg";
    private static final String MIME_TYPE_3GP = "video/3gp";
    public static final String MIME_TYPE_AUDIO = "audio/mpeg";
    private static final String MIME_TYPE_AVI = "video/avi";
    private static final String MIME_TYPE_BMP = "image/bmp";
    private static final String MIME_TYPE_GIF = "image/gif";
    public static final String MIME_TYPE_IMAGE = "image/jpeg";
    private static final String MIME_TYPE_JPEG = "image/jpeg";
    private static final String MIME_TYPE_MP4 = "video/mp4";
    private static final String MIME_TYPE_MPEG = "video/mpeg";
    private static final String MIME_TYPE_PNG = "image/png";
    public static final String MIME_TYPE_PREFIX_AUDIO = "audio";
    public static final String MIME_TYPE_PREFIX_IMAGE = "image";
    public static final String MIME_TYPE_PREFIX_VIDEO = "video";
    public static final String MIME_TYPE_VIDEO = "video/mp4";
    private static final String MIME_TYPE_WEBP = "image/webp";
    public static final String PNG = ".png";

    public static final String of3GP() {
        return MIME_TYPE_3GP;
    }

    public static final String ofAVI() {
        return MIME_TYPE_AVI;
    }

    public static final int ofAll() {
        return 0;
    }

    @Deprecated
    public static final int ofAudio() {
        return 3;
    }

    public static final String ofBMP() {
        return MIME_TYPE_BMP;
    }

    public static final String ofGIF() {
        return MIME_TYPE_GIF;
    }

    public static final int ofImage() {
        return 1;
    }

    public static final String ofJPEG() {
        return "image/jpeg";
    }

    public static final String ofMP4() {
        return "video/mp4";
    }

    public static final String ofMPEG() {
        return MIME_TYPE_MPEG;
    }

    public static final String ofPNG() {
        return MIME_TYPE_PNG;
    }

    public static final int ofVideo() {
        return 2;
    }

    public static final String ofWEBP() {
        return MIME_TYPE_WEBP;
    }

    @Deprecated
    public static int isPictureType(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1930021710:
                if (str.equals("audio/x-ms-wma")) {
                    c = 0;
                    break;
                }
                break;
            case -1664118616:
                if (str.equals("video/3gpp")) {
                    c = 1;
                    break;
                }
                break;
            case -1662382439:
                if (str.equals(MIME_TYPE_MPEG)) {
                    c = 2;
                    break;
                }
                break;
            case -1662095187:
                if (str.equals("video/webm")) {
                    c = 3;
                    break;
                }
                break;
            case -1606874997:
                if (str.equals("audio/amr-wb")) {
                    c = 4;
                    break;
                }
                break;
            case -1079884372:
                if (str.equals("video/x-msvideo")) {
                    c = 5;
                    break;
                }
                break;
            case -586683234:
                if (str.equals("audio/x-wav")) {
                    c = 6;
                    break;
                }
                break;
            case -107252314:
                if (str.equals("video/quicktime")) {
                    c = 7;
                    break;
                }
                break;
            case -48069494:
                if (str.equals("video/3gpp2")) {
                    c = '\b';
                    break;
                }
                break;
            case 5703450:
                if (str.equals("video/mp2ts")) {
                    c = '\t';
                    break;
                }
                break;
            case 187078282:
                if (str.equals("audio/aac")) {
                    c = '\n';
                    break;
                }
                break;
            case 187078669:
                if (str.equals("audio/amr")) {
                    c = 11;
                    break;
                }
                break;
            case 187090232:
                if (str.equals("audio/mp4")) {
                    c = '\f';
                    break;
                }
                break;
            case 187099443:
                if (str.equals("audio/wav")) {
                    c = TokenParser.CR;
                    break;
                }
                break;
            case 1331792072:
                if (str.equals(MIME_TYPE_3GP)) {
                    c = 14;
                    break;
                }
                break;
            case 1331836736:
                if (str.equals(MIME_TYPE_AVI)) {
                    c = 15;
                    break;
                }
                break;
            case 1331848029:
                if (str.equals("video/mp4")) {
                    c = 16;
                    break;
                }
                break;
            case 1338492737:
                if (str.equals("audio/quicktime")) {
                    c = 17;
                    break;
                }
                break;
            case 1503095341:
                if (str.equals("audio/3gpp")) {
                    c = 18;
                    break;
                }
                break;
            case 1504787571:
                if (str.equals("audio/lamr")) {
                    c = 19;
                    break;
                }
                break;
            case 1504831518:
                if (str.equals(MIME_TYPE_AUDIO)) {
                    c = 20;
                    break;
                }
                break;
            case 2039520277:
                if (str.equals("video/x-matroska")) {
                    c = 21;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 4:
            case 6:
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 17:
            case 18:
            case 19:
            case 20:
                return 3;
            case 1:
            case 2:
            case 3:
            case 5:
            case 7:
            case '\b':
            case '\t':
            case 14:
            case 15:
            case 16:
            case 21:
                return 2;
            default:
                return 1;
        }
    }

    public static boolean isGif(String str) {
        return str != null && (str.equals(MIME_TYPE_GIF) || str.equals("image/GIF"));
    }

    @Deprecated
    public static boolean isVideo(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1664118616:
                if (str.equals("video/3gpp")) {
                    c = 0;
                    break;
                }
                break;
            case -1662382439:
                if (str.equals(MIME_TYPE_MPEG)) {
                    c = 1;
                    break;
                }
                break;
            case -1662095187:
                if (str.equals("video/webm")) {
                    c = 2;
                    break;
                }
                break;
            case -1079884372:
                if (str.equals("video/x-msvideo")) {
                    c = 3;
                    break;
                }
                break;
            case -107252314:
                if (str.equals("video/quicktime")) {
                    c = 4;
                    break;
                }
                break;
            case -48069494:
                if (str.equals("video/3gpp2")) {
                    c = 5;
                    break;
                }
                break;
            case 5703450:
                if (str.equals("video/mp2ts")) {
                    c = 6;
                    break;
                }
                break;
            case 1331792072:
                if (str.equals(MIME_TYPE_3GP)) {
                    c = 7;
                    break;
                }
                break;
            case 1331836736:
                if (str.equals(MIME_TYPE_AVI)) {
                    c = '\b';
                    break;
                }
                break;
            case 1331848029:
                if (str.equals("video/mp4")) {
                    c = '\t';
                    break;
                }
                break;
            case 2039520277:
                if (str.equals("video/x-matroska")) {
                    c = '\n';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
                return true;
            default:
                return false;
        }
    }

    public static boolean eqVideo(String str) {
        return str != null && str.startsWith(MIME_TYPE_PREFIX_VIDEO);
    }

    public static boolean eqAudio(String str) {
        return str != null && str.startsWith(MIME_TYPE_PREFIX_AUDIO);
    }

    public static boolean eqImage(String str) {
        return str != null && str.startsWith(MIME_TYPE_PREFIX_IMAGE);
    }

    public static boolean isHttp(String str) {
        return !TextUtils.isEmpty(str) && (str.startsWith(HttpHost.DEFAULT_SCHEME_NAME) || str.startsWith("https"));
    }

    public static String fileToType(File file) {
        if (file != null) {
            String name = file.getName();
            if (name.endsWith(PictureFileUtils.POST_VIDEO) || name.endsWith(".avi") || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.endsWith(".mov")) {
                return "video/mp4";
            }
            if (name.endsWith(".PNG") || name.endsWith(PNG) || name.endsWith(JPEG) || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg") || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(PictureFileUtils.POSTFIX) || name.endsWith(".bmp")) {
                return "image/jpeg";
            }
            if (name.endsWith(PictureFileUtils.POST_AUDIO) || name.endsWith(".amr") || name.endsWith(".aac") || name.endsWith(".war") || name.endsWith(".flac") || name.endsWith(".lamr")) {
                return MIME_TYPE_AUDIO;
            }
        }
        return "image/jpeg";
    }

    @Deprecated
    public static boolean mimeToEqual(String str, String str2) {
        return isPictureType(str) == isPictureType(str2);
    }

    public static boolean isMimeTypeSame(String str, String str2) {
        return getMimeType(str) == getMimeType(str2);
    }

    public static String getImageMimeType(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return "image/jpeg";
            }
            String name = new File(str).getName();
            String substring = name.substring(name.lastIndexOf(".") + 1);
            return "image/" + substring;
        } catch (Exception e) {
            e.printStackTrace();
            return "image/jpeg";
        }
    }

    public static String getMimeType(Context context, Uri uri) {
        Cursor query;
        int columnIndexOrThrow;
        if (!Utils.RESPONSE_CONTENT.equals(uri.getScheme()) || (query = context.getApplicationContext().getContentResolver().query(uri, new String[]{"mime_type"}, null, null, null)) == null) {
            return "image/jpeg";
        }
        if (query.moveToFirst() && (columnIndexOrThrow = query.getColumnIndexOrThrow("mime_type")) > -1) {
            return query.getString(columnIndexOrThrow);
        }
        query.close();
        return "image/jpeg";
    }

    public static int getMimeType(String str) {
        if (TextUtils.isEmpty(str)) {
            return 1;
        }
        if (str.startsWith(MIME_TYPE_PREFIX_VIDEO)) {
            return 2;
        }
        return str.startsWith(MIME_TYPE_PREFIX_AUDIO) ? 3 : 1;
    }

    public static String getLastImgType(String str) {
        try {
            int lastIndexOf = str.lastIndexOf(".");
            if (lastIndexOf > 0) {
                String substring = str.substring(lastIndexOf);
                char c = 65535;
                switch (substring.hashCode()) {
                    case 1436279:
                        if (substring.equals(".BMP")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 1440950:
                        if (substring.equals(".GIF")) {
                            c = '\n';
                            break;
                        }
                        break;
                    case 1449755:
                        if (substring.equals(".PNG")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1468055:
                        if (substring.equals(".bmp")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 1472726:
                        if (substring.equals(".gif")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case 1475827:
                        if (substring.equals(".jpg")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1481531:
                        if (substring.equals(PNG)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 44765590:
                        if (substring.equals(PictureFileUtils.POSTFIX)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 45142218:
                        if (substring.equals(".WEBP")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 45750678:
                        if (substring.equals(JPEG)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 46127306:
                        if (substring.equals(".webp")) {
                            c = '\b';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case '\b':
                    case '\t':
                    case '\n':
                        return substring;
                }
            }
            return PNG;
        } catch (Exception e) {
            e.printStackTrace();
            return PNG;
        }
    }

    public static String getLastImgSuffix(String str) {
        try {
            int lastIndexOf = str.lastIndexOf("/") + 1;
            if (lastIndexOf > 0) {
                return "." + str.substring(lastIndexOf);
            }
            return PNG;
        } catch (Exception e) {
            e.printStackTrace();
            return PNG;
        }
    }

    public static String s(Context context, String str) {
        Context applicationContext = context.getApplicationContext();
        if (eqVideo(str)) {
            return applicationContext.getString(R.string.picture_video_error);
        }
        if (eqAudio(str)) {
            return applicationContext.getString(R.string.picture_audio_error);
        }
        return applicationContext.getString(R.string.picture_error);
    }
}
