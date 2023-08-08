package com.luck.picture.lib.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class LocalMediaLoader implements Handler.Callback {
    private static final int AUDIO_DURATION = 500;
    private static final long FILE_SIZE_UNIT = 1048576;
    private static final int MSG_QUERY_MEDIA_SUCCESS = 0;
    private static final String NOT_GIF = "!='image/gif'";
    private static final String ORDER_BY = "_id DESC";
    private static final String SELECTION = "media_type=? AND _size>0";
    private static final String SELECTION_NOT_GIF = "media_type=? AND _size>0 AND mime_type!='image/gif'";
    private static final String SELECTION_SPECIFIED_FORMAT = "media_type=? AND _size>0 AND mime_type";
    private PictureSelectionConfig config;
    private LocalMediaLoadListener mCompleteListener;
    private Context mContext;
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String[] PROJECTION = {"_id", "_data", "mime_type", "width", "height", FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION, "_size", "bucket_display_name"};
    private static final String[] SELECTION_ALL_ARGS = {String.valueOf(1), String.valueOf(3)};
    private boolean isAndroidQ = SdkVersionUtils.checkedAndroid_Q();
    private Handler mHandler = new Handler(Looper.getMainLooper(), this);

    /* loaded from: classes.dex */
    public interface LocalMediaLoadListener {
        void loadComplete(List<LocalMediaFolder> list);
    }

    private static String getSelectionArgsForSingleMediaCondition() {
        return SELECTION;
    }

    private static String getSelectionArgsForSingleMediaCondition(String str) {
        return "media_type=? AND _size>0 AND " + str;
    }

    private static String getSelectionArgsForAllMediaCondition(String str, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("(media_type=?");
        sb.append(z ? "" : " AND mime_type!='image/gif'");
        sb.append(" OR ");
        sb.append("media_type=? AND ");
        sb.append(str);
        sb.append(") AND ");
        sb.append("_size");
        sb.append(">0");
        return sb.toString();
    }

    private static String[] getSelectionArgsForSingleMediaType(int i) {
        return new String[]{String.valueOf(i)};
    }

    public LocalMediaLoader(Context context, PictureSelectionConfig pictureSelectionConfig) {
        this.mContext = context.getApplicationContext();
        this.config = pictureSelectionConfig;
    }

    public void loadAllMedia() {
        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() { // from class: com.luck.picture.lib.model.LocalMediaLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LocalMediaLoader.this.m53xb53895a5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$loadAllMedia$0$com-luck-picture-lib-model-LocalMediaLoader  reason: not valid java name */
    public /* synthetic */ void m53xb53895a5() {
        String str;
        int[] localVideoSize;
        String string;
        Cursor query = this.mContext.getContentResolver().query(QUERY_URI, PROJECTION, getSelection(), getSelectionArgs(), ORDER_BY);
        if (query != null) {
            try {
                ArrayList arrayList = new ArrayList();
                LocalMediaFolder localMediaFolder = new LocalMediaFolder();
                ArrayList arrayList2 = new ArrayList();
                char c = 0;
                if (query.getCount() > 0) {
                    query.moveToFirst();
                    while (true) {
                        String[] strArr = PROJECTION;
                        String realPathAndroid_Q = this.isAndroidQ ? getRealPathAndroid_Q(query.getLong(query.getColumnIndexOrThrow(strArr[c]))) : query.getString(query.getColumnIndexOrThrow(strArr[1]));
                        String string2 = query.getString(query.getColumnIndexOrThrow(strArr[2]));
                        int i = query.getInt(query.getColumnIndexOrThrow(strArr[3]));
                        int i2 = query.getInt(query.getColumnIndexOrThrow(strArr[4]));
                        long j = query.getLong(query.getColumnIndexOrThrow(strArr[5]));
                        long j2 = query.getLong(query.getColumnIndexOrThrow(strArr[6]));
                        String string3 = query.getString(query.getColumnIndexOrThrow(strArr[7]));
                        if (this.config.filterFileSize <= 0 || j2 <= this.config.filterFileSize * 1048576) {
                            if (PictureMimeType.eqVideo(string2)) {
                                if (j == 0) {
                                    j = MediaUtils.extractDuration(this.mContext, this.isAndroidQ, realPathAndroid_Q);
                                }
                                if (i == 0 && i2 == 0) {
                                    if (this.isAndroidQ) {
                                        localVideoSize = MediaUtils.getLocalVideoSizeToAndroidQ(this.mContext, realPathAndroid_Q);
                                    } else {
                                        localVideoSize = MediaUtils.getLocalVideoSize(realPathAndroid_Q);
                                    }
                                    int i3 = localVideoSize[c];
                                    i2 = localVideoSize[1];
                                    i = i3;
                                }
                                if (this.config.videoMinSecond > 0) {
                                    str = realPathAndroid_Q;
                                    if (j < this.config.videoMinSecond) {
                                    }
                                } else {
                                    str = realPathAndroid_Q;
                                }
                                if (this.config.videoMaxSecond > 0) {
                                    if (j > this.config.videoMaxSecond) {
                                    }
                                }
                                if (j != 0) {
                                    if (j2 <= 0) {
                                    }
                                }
                            } else {
                                str = realPathAndroid_Q;
                            }
                            LocalMedia localMedia = new LocalMedia(str, j, this.config.chooseMode, string2, i, i2, j2);
                            LocalMediaFolder imageFolder = getImageFolder(str, string3, arrayList);
                            imageFolder.getImages().add(localMedia);
                            imageFolder.setImageNum(imageFolder.getImageNum() + 1);
                            arrayList2.add(localMedia);
                            localMediaFolder.setImageNum(localMediaFolder.getImageNum() + 1);
                        }
                        if (!query.moveToNext()) {
                            break;
                        }
                        c = 0;
                    }
                    if (arrayList2.size() > 0) {
                        sortFolder(arrayList);
                        arrayList.add(0, localMediaFolder);
                        localMediaFolder.setFirstImagePath(arrayList2.get(0).getPath());
                        if (this.config.chooseMode == PictureMimeType.ofAudio()) {
                            string = this.mContext.getString(R.string.picture_all_audio);
                        } else {
                            string = this.mContext.getString(R.string.picture_camera_roll);
                        }
                        localMediaFolder.setName(string);
                        localMediaFolder.setOfAllType(this.config.chooseMode);
                        localMediaFolder.setCameraFolder(true);
                        localMediaFolder.setImages(arrayList2);
                    }
                }
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(0, arrayList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getSelection() {
        int i = this.config.chooseMode;
        if (i != 0) {
            if (i == 1) {
                if (TextUtils.isEmpty(this.config.specifiedFormat)) {
                    return this.config.isGif ? SELECTION : SELECTION_NOT_GIF;
                }
                return "media_type=? AND _size>0 AND mime_type='" + this.config.specifiedFormat + "'";
            } else if (i == 2) {
                if (!TextUtils.isEmpty(this.config.specifiedFormat)) {
                    return "media_type=? AND _size>0 AND mime_type='" + this.config.specifiedFormat + "'";
                }
                return getSelectionArgsForSingleMediaCondition();
            } else if (i != 3) {
                return null;
            } else {
                if (!TextUtils.isEmpty(this.config.specifiedFormat)) {
                    return "media_type=? AND _size>0 AND mime_type='" + this.config.specifiedFormat + "'";
                }
                return getSelectionArgsForSingleMediaCondition(getDurationCondition(0L, 500L));
            }
        }
        return getSelectionArgsForAllMediaCondition(getDurationCondition(0L, 0L), this.config.isGif);
    }

    private String[] getSelectionArgs() {
        int i = this.config.chooseMode;
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        return null;
                    }
                    return getSelectionArgsForSingleMediaType(2);
                }
                return getSelectionArgsForSingleMediaType(3);
            }
            return getSelectionArgsForSingleMediaType(1);
        }
        return SELECTION_ALL_ARGS;
    }

    private void sortFolder(List<LocalMediaFolder> list) {
        Collections.sort(list, new Comparator() { // from class: com.luck.picture.lib.model.LocalMediaLoader$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LocalMediaLoader.lambda$sortFolder$1((LocalMediaFolder) obj, (LocalMediaFolder) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$sortFolder$1(LocalMediaFolder localMediaFolder, LocalMediaFolder localMediaFolder2) {
        int imageNum;
        int imageNum2;
        if (localMediaFolder.getImages() == null || localMediaFolder2.getImages() == null || (imageNum = localMediaFolder.getImageNum()) == (imageNum2 = localMediaFolder2.getImageNum())) {
            return 0;
        }
        return imageNum < imageNum2 ? 1 : -1;
    }

    private String getRealPathAndroid_Q(long j) {
        return QUERY_URI.buildUpon().appendPath(String.valueOf(j)).build().toString();
    }

    private LocalMediaFolder getImageFolder(String str, String str2, List<LocalMediaFolder> list) {
        for (LocalMediaFolder localMediaFolder : list) {
            if (localMediaFolder.getName().equals(str2)) {
                return localMediaFolder;
            }
        }
        LocalMediaFolder localMediaFolder2 = new LocalMediaFolder();
        localMediaFolder2.setName(str2);
        localMediaFolder2.setFirstImagePath(str);
        list.add(localMediaFolder2);
        return localMediaFolder2;
    }

    private String getDurationCondition(long j, long j2) {
        long j3 = this.config.videoMaxSecond == 0 ? Long.MAX_VALUE : this.config.videoMaxSecond;
        if (j != 0) {
            j3 = Math.min(j3, j);
        }
        Locale locale = Locale.CHINA;
        Object[] objArr = new Object[3];
        objArr[0] = Long.valueOf(Math.max(j2, this.config.videoMinSecond));
        objArr[1] = Math.max(j2, (long) this.config.videoMinSecond) == 0 ? "" : "=";
        objArr[2] = Long.valueOf(j3);
        return String.format(locale, "%d <%s duration and duration <= %d", objArr);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (this.mCompleteListener != null && message.what == 0) {
            this.mCompleteListener.loadComplete((List) message.obj);
        }
        return false;
    }

    public void setCompleteListener(LocalMediaLoadListener localMediaLoadListener) {
        this.mCompleteListener = localMediaLoadListener;
    }
}
