package com.ccr.achenglibrary.photopicker.util;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.model.CCRImageFolderModel;
import com.ccr.achenglibrary.photopicker.util.CCRAsyncTask;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class CCRLoadPhotoTask extends CCRAsyncTask<Void, ArrayList<CCRImageFolderModel>> {
    private Context mContext;
    private boolean mTakePhotoEnabled;

    public CCRLoadPhotoTask(CCRAsyncTask.Callback<ArrayList<CCRImageFolderModel>> callback, Context context, boolean z) {
        super(callback);
        this.mContext = context.getApplicationContext();
        this.mTakePhotoEnabled = z;
    }

    private static boolean isNotImageFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        File file = new File(str);
        return !file.exists() || file.length() == 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public ArrayList<CCRImageFolderModel> doInBackground(Void... voidArr) {
        CCRImageFolderModel cCRImageFolderModel;
        int lastIndexOf;
        ArrayList<CCRImageFolderModel> arrayList = new ArrayList<>();
        CCRImageFolderModel cCRImageFolderModel2 = new CCRImageFolderModel(this.mTakePhotoEnabled);
        cCRImageFolderModel2.name = this.mContext.getString(R.string.bga_pp_all_image);
        arrayList.add(cCRImageFolderModel2);
        HashMap hashMap = new HashMap();
        Cursor cursor = null;
        try {
            try {
                Cursor query = this.mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data"}, "mime_type=? or mime_type=? or mime_type=?", new String[]{PictureMimeType.MIME_TYPE_IMAGE, "image/png", "image/jpg"}, "date_added DESC");
                if (query != null) {
                    try {
                        if (query.getCount() > 0) {
                            boolean z = true;
                            while (query.moveToNext()) {
                                String string = query.getString(query.getColumnIndex("_data"));
                                if (!isNotImageFile(string)) {
                                    if (z) {
                                        cCRImageFolderModel2.coverPath = string;
                                        z = false;
                                    }
                                    cCRImageFolderModel2.addLastImage(string);
                                    File parentFile = new File(string).getParentFile();
                                    String absolutePath = parentFile != null ? parentFile.getAbsolutePath() : null;
                                    if (TextUtils.isEmpty(absolutePath) && (lastIndexOf = string.lastIndexOf(File.separator)) != -1) {
                                        absolutePath = string.substring(0, lastIndexOf);
                                    }
                                    if (!TextUtils.isEmpty(absolutePath)) {
                                        if (hashMap.containsKey(absolutePath)) {
                                            cCRImageFolderModel = (CCRImageFolderModel) hashMap.get(absolutePath);
                                        } else {
                                            String substring = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);
                                            if (TextUtils.isEmpty(substring)) {
                                                substring = "/";
                                            }
                                            CCRImageFolderModel cCRImageFolderModel3 = new CCRImageFolderModel(substring, string);
                                            hashMap.put(absolutePath, cCRImageFolderModel3);
                                            cCRImageFolderModel = cCRImageFolderModel3;
                                        }
                                        cCRImageFolderModel.addLastImage(string);
                                    }
                                }
                            }
                            arrayList.addAll(hashMap.values());
                        }
                    } catch (Exception e) {
                        e = e;
                        cursor = query;
                        e.printStackTrace();
                        if (cursor != null) {
                            cursor.close();
                        }
                        return arrayList;
                    } catch (Throwable th) {
                        th = th;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
            } catch (Exception e2) {
                e = e2;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public CCRLoadPhotoTask perform() {
        if (Build.VERSION.SDK_INT >= 11) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {
            execute(new Void[0]);
        }
        return this;
    }
}
