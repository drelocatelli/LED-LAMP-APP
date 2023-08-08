package com.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import com.ledlamp.R;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

/* loaded from: classes.dex */
public class DonwloadSaveImg {
    private static final String TAG = "PictureActivity";
    private static Context context;
    private static String filePath;
    private static Bitmap mBitmap;
    private static String mSaveMessage;
    private static Runnable saveFileRunnable = new Runnable() { // from class: com.forum.adapter.DonwloadSaveImg.1
        @Override // java.lang.Runnable
        public void run() {
            try {
                if (!TextUtils.isEmpty(DonwloadSaveImg.filePath)) {
                    InputStream openStream = new URL(DonwloadSaveImg.filePath).openStream();
                    Bitmap unused = DonwloadSaveImg.mBitmap = BitmapFactory.decodeStream(openStream);
                    openStream.close();
                }
                DonwloadSaveImg.saveFile(DonwloadSaveImg.mBitmap);
                String unused2 = DonwloadSaveImg.mSaveMessage = DonwloadSaveImg.context.getResources().getString(R.string.saveSuccessfully);
            } catch (IOException e) {
                String unused3 = DonwloadSaveImg.mSaveMessage = DonwloadSaveImg.context.getResources().getString(R.string.savefailed);
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            DonwloadSaveImg.messageHandler.sendMessage(DonwloadSaveImg.messageHandler.obtainMessage());
        }
    };
    private static Handler messageHandler = new Handler() { // from class: com.forum.adapter.DonwloadSaveImg.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Toast.makeText(DonwloadSaveImg.context, DonwloadSaveImg.mSaveMessage, 0).show();
        }
    };

    public static void donwloadImg(Context context2, String str) {
        context = context2;
        filePath = str;
        new Thread(saveFileRunnable).start();
    }

    public static void saveFile(Bitmap bitmap) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + (UUID.randomUUID().toString() + ".jpg"));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file2));
        context.sendBroadcast(intent);
    }
}
