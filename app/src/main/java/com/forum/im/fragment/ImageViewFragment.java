package com.forum.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.common.view.ActionSheetDialog;
import com.ledlamp.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.polites.android.GestureImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class ImageViewFragment extends Fragment {
    private GestureImageView imageGiv;
    private String imageUrl;
    private ProgressBar loadBar;
    private Bitmap mDownloadImgBitmap = null;
    final int REQUEST_WRITE = PointerIconCompat.TYPE_CONTEXT_MENU;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_images_view_item, viewGroup, false);
        init(inflate);
        loadImage(this.imageUrl);
        return inflate;
    }

    private void init(View view) {
        this.loadBar = (ProgressBar) view.findViewById(R.id.imageView_loading_pb);
        GestureImageView gestureImageView = (GestureImageView) view.findViewById(R.id.imageView_item_giv);
        this.imageGiv = gestureImageView;
        gestureImageView.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.fragment.ImageViewFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ImageViewFragment.this.showActionSheet("");
            }
        });
    }

    public void loadImage(String str) {
        if (str.startsWith("http://")) {
            Glide.with(this).load(str).into(this.imageGiv);
            this.loadBar.setVisibility(8);
            this.imageGiv.setVisibility(0);
        } else {
            this.imageGiv.setImageBitmap(BitmapFactory.decodeFile(str));
            this.loadBar.setVisibility(8);
            this.imageGiv.setVisibility(0);
        }
        ImageLoader.getInstance().loadImage(str, new SimpleImageLoadingListener() { // from class: com.forum.im.fragment.ImageViewFragment.2
            @Override // com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener, com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingFailed(String str2, View view, FailReason failReason) {
            }

            @Override // com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener, com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingStarted(String str2, View view) {
            }

            @Override // com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener, com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingComplete(String str2, View view, Bitmap bitmap) {
                ImageViewFragment.this.setDownloadImgBitmap(bitmap);
            }
        });
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    private Bitmap getDownloadImgBitmap() {
        return this.mDownloadImgBitmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDownloadImgBitmap(Bitmap bitmap) {
        this.mDownloadImgBitmap = bitmap;
    }

    public void showActionSheet(String str) {
        new ActionSheetDialog(getActivity()).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem(getString(R.string.save), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() { // from class: com.forum.im.fragment.ImageViewFragment.3
            @Override // com.common.view.ActionSheetDialog.OnSheetItemClickListener
            public void onClick(int i) {
                ImageViewFragment.this.checkPermission();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, PointerIconCompat.TYPE_CONTEXT_MENU);
                return;
            } else {
                saveImageToGallery(getContext(), getDownloadImgBitmap());
                return;
            }
        }
        saveImageToGallery(getContext(), getDownloadImgBitmap());
    }

    public void saveImageToGallery(Context context, Bitmap bitmap) {
        Log.d("ZoomImage", "saveImageToGallery:" + bitmap);
        String absolutePath = Environment.getExternalStorageState().equalsIgnoreCase("mounted") ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
        File file = new File(absolutePath + "/LED LAMP/");
        if (!file.exists()) {
            file.mkdir();
        }
        long currentTimeMillis = System.currentTimeMillis();
        File file2 = new File(file, currentTimeMillis + PictureMimeType.PNG);
        try {
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
        Toast.makeText(getContext(), getString(R.string.saved_to_local_album), 1).show();
    }
}
