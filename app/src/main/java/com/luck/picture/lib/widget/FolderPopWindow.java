package com.luck.picture.lib.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.luck.picture.lib.R;
import com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.tools.AnimUtils;
import com.luck.picture.lib.tools.AttrsUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import java.util.List;

/* loaded from: classes.dex */
public class FolderPopWindow extends PopupWindow {
    private PictureAlbumDirectoryAdapter adapter;
    private int chooseMode;
    private PictureSelectionConfig config;
    private Context context;
    private Drawable drawableDown;
    private Drawable drawableUp;
    private boolean isDismiss = false;
    private ImageView ivArrowView;
    private int maxHeight;
    private RecyclerView recyclerView;
    private View rootView;
    private View window;

    public FolderPopWindow(Context context, PictureSelectionConfig pictureSelectionConfig) {
        this.context = context;
        this.config = pictureSelectionConfig;
        this.chooseMode = pictureSelectionConfig.chooseMode;
        View inflate = LayoutInflater.from(context).inflate(R.layout.picture_window_folder, (ViewGroup) null);
        this.window = inflate;
        setContentView(inflate);
        setWidth(-1);
        setHeight(-2);
        setAnimationStyle(R.style.PictureThemeWindowStyle);
        setFocusable(true);
        setOutsideTouchable(true);
        update();
        setBackgroundDrawable(new ColorDrawable(Color.argb(123, 0, 0, 0)));
        if (pictureSelectionConfig.style != null) {
            if (pictureSelectionConfig.style.pictureTitleUpResId != 0) {
                this.drawableUp = ContextCompat.getDrawable(context, pictureSelectionConfig.style.pictureTitleUpResId);
            }
            if (pictureSelectionConfig.style.pictureTitleDownResId != 0) {
                this.drawableDown = ContextCompat.getDrawable(context, pictureSelectionConfig.style.pictureTitleDownResId);
            }
        } else {
            if (pictureSelectionConfig.upResId != 0) {
                this.drawableUp = ContextCompat.getDrawable(context, pictureSelectionConfig.upResId);
            } else {
                this.drawableUp = AttrsUtils.getTypeValueDrawable(context, R.attr.picture_arrow_up_icon);
            }
            if (pictureSelectionConfig.downResId != 0) {
                this.drawableDown = ContextCompat.getDrawable(context, pictureSelectionConfig.downResId);
            } else {
                this.drawableDown = AttrsUtils.getTypeValueDrawable(context, R.attr.picture_arrow_down_icon);
            }
        }
        double screenHeight = ScreenUtils.getScreenHeight(context);
        Double.isNaN(screenHeight);
        this.maxHeight = (int) (screenHeight * 0.6d);
        initView();
    }

    public void initView() {
        this.rootView = this.window.findViewById(R.id.rootView);
        this.adapter = new PictureAlbumDirectoryAdapter(this.context, this.config);
        RecyclerView recyclerView = (RecyclerView) this.window.findViewById(R.id.folder_list);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        this.recyclerView.setAdapter(this.adapter);
        this.rootView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.widget.FolderPopWindow$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FolderPopWindow.this.m55lambda$initView$0$comluckpicturelibwidgetFolderPopWindow(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initView$0$com-luck-picture-lib-widget-FolderPopWindow  reason: not valid java name */
    public /* synthetic */ void m55lambda$initView$0$comluckpicturelibwidgetFolderPopWindow(View view) {
        dismiss();
    }

    public void bindFolder(List<LocalMediaFolder> list) {
        this.adapter.setChooseMode(this.chooseMode);
        this.adapter.bindFolderData(list);
        this.recyclerView.getLayoutParams().height = (list == null || list.size() <= 8) ? -2 : this.maxHeight;
    }

    public void setArrowImageView(ImageView imageView) {
        this.ivArrowView = imageView;
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view) {
        try {
            if (!this.config.isFallbackVersion) {
                if (Build.VERSION.SDK_INT == 24) {
                    int[] iArr = new int[2];
                    view.getLocationInWindow(iArr);
                    showAtLocation(view, 0, 0, iArr[1] + view.getHeight());
                } else {
                    super.showAsDropDown(view, 0, 0);
                }
            } else {
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);
                    setHeight(view.getResources().getDisplayMetrics().heightPixels - rect.bottom);
                }
                super.showAtLocation(view, 0, 0, view.getHeight() + ScreenUtils.getStatusBarHeight(this.context));
            }
            this.isDismiss = false;
            this.ivArrowView.setImageDrawable(this.drawableUp);
            AnimUtils.rotateArrow(this.ivArrowView, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(PictureAlbumDirectoryAdapter.OnItemClickListener onItemClickListener) {
        this.adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        if (this.isDismiss) {
            return;
        }
        this.ivArrowView.setImageDrawable(this.drawableDown);
        AnimUtils.rotateArrow(this.ivArrowView, false);
        this.isDismiss = true;
        if (Build.VERSION.SDK_INT > 16) {
            super.dismiss();
            this.isDismiss = false;
            return;
        }
        dismiss4Pop();
        this.isDismiss = false;
    }

    private void dismiss4Pop() {
        new Handler().post(new Runnable() { // from class: com.luck.picture.lib.widget.FolderPopWindow$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FolderPopWindow.this.m54lambda$dismiss4Pop$1$comluckpicturelibwidgetFolderPopWindow();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$dismiss4Pop$1$com-luck-picture-lib-widget-FolderPopWindow  reason: not valid java name */
    public /* synthetic */ void m54lambda$dismiss4Pop$1$comluckpicturelibwidgetFolderPopWindow() {
        super.dismiss();
    }

    public void notifyDataCheckedStatus(List<LocalMedia> list) {
        try {
            List<LocalMediaFolder> folderData = this.adapter.getFolderData();
            for (LocalMediaFolder localMediaFolder : folderData) {
                localMediaFolder.setCheckedNum(0);
            }
            if (list.size() > 0) {
                for (LocalMediaFolder localMediaFolder2 : folderData) {
                    int i = 0;
                    for (LocalMedia localMedia : localMediaFolder2.getImages()) {
                        String path = localMedia.getPath();
                        for (LocalMedia localMedia2 : list) {
                            if (path.equals(localMedia2.getPath())) {
                                i++;
                                localMediaFolder2.setCheckedNum(i);
                            }
                        }
                    }
                }
            }
            this.adapter.bindFolderData(folderData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
