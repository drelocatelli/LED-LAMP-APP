package com.forum.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ledlamp.R;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/* loaded from: classes.dex */
public class ScaleImageView implements EasyPermissions.PermissionCallbacks {
    private static final byte FILES = 1;
    private static final int RC_SETTINGS_SCREEN = 125;
    private static final byte URLS = 0;
    private static final int WRITE_EXTERNAL_STORAGE = 123;
    private Activity activity;
    private MyPagerAdapter adapter;
    private ImageView delete;
    private Dialog dialog;
    private ImageView download;
    private List<File> downloadFiles;
    private List<File> files;
    private TextView imageCount;
    private OnDeleteItemListener listener;
    private int selectedPosition;
    private int startPosition;
    private byte status;
    private List<String> urls;
    private ViewPager viewPager;
    private List<View> views;

    /* loaded from: classes.dex */
    public interface OnDeleteItemListener {
        void onDelete(int i);
    }

    @Override // androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public ScaleImageView(Context context) {
        this.activity = (Activity) context;
        init();
    }

    public void setUrls(List<String> list, int i) {
        List<String> list2 = this.urls;
        if (list2 == null) {
            this.urls = new ArrayList();
        } else {
            list2.clear();
        }
        this.urls.addAll(list);
        this.status = URLS;
        this.delete.setVisibility(8);
        List<File> list3 = this.downloadFiles;
        if (list3 == null) {
            this.downloadFiles = new ArrayList();
        } else {
            list3.clear();
        }
        int i2 = i + 1;
        this.startPosition = i;
        this.imageCount.setText(i2 + "/" + list.size());
    }

    public void setFiles(List<File> list, int i) {
        List<File> list2 = this.files;
        if (list2 == null) {
            this.files = new LinkedList();
        } else {
            list2.clear();
        }
        this.files.addAll(list);
        this.status = FILES;
        this.download.setVisibility(8);
        int i2 = i + 1;
        this.startPosition = i;
        this.imageCount.setText(i2 + "/" + list.size());
    }

    public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
        this.listener = onDeleteItemListener;
    }

    private void init() {
        RelativeLayout relativeLayout = (RelativeLayout) this.activity.getLayoutInflater().inflate(R.layout.dialog, (ViewGroup) null);
        this.delete = (ImageView) relativeLayout.findViewById(R.id.scale_image_delete);
        this.download = (ImageView) relativeLayout.findViewById(R.id.scale_image_save);
        this.imageCount = (TextView) relativeLayout.findViewById(R.id.scale_image_count);
        this.viewPager = (ViewPager) relativeLayout.findViewById(R.id.scale_image_view_pager);
        Dialog dialog = new Dialog(this.activity, R.style.Dialog_Fullscreen);
        this.dialog = dialog;
        dialog.setContentView(relativeLayout);
        ((ImageView) relativeLayout.findViewById(R.id.scale_image_close)).setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ScaleImageView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScaleImageView.this.dialog.dismiss();
            }
        });
        this.delete.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ScaleImageView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int size = ScaleImageView.this.views.size();
                ScaleImageView.this.files.remove(ScaleImageView.this.selectedPosition);
                if (ScaleImageView.this.listener != null) {
                    ScaleImageView.this.listener.onDelete(ScaleImageView.this.selectedPosition);
                }
                ScaleImageView.this.viewPager.removeView((View) ScaleImageView.this.views.remove(ScaleImageView.this.selectedPosition));
                if (ScaleImageView.this.selectedPosition != size) {
                    ScaleImageView.this.imageCount.setText((ScaleImageView.this.selectedPosition + 1) + "/" + ScaleImageView.this.views.size());
                }
                ScaleImageView.this.adapter.notifyDataSetChanged();
            }
        });
        this.download.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ScaleImageView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScaleImageView.this.writeTask();
            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.forum.adapter.ScaleImageView.4
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                ScaleImageView.this.selectedPosition = i;
                ScaleImageView.this.imageCount.setText((i + 1) + "/" + ScaleImageView.this.views.size());
            }
        });
    }

    @AfterPermissionGranted(123)
    public void writeTask() {
        if (EasyPermissions.hasPermissions(this.activity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            DonwloadSaveImg.donwloadImg(this.activity, this.urls.get(this.selectedPosition));
        } else {
            EasyPermissions.requestPermissions(this.activity, String.valueOf((int) R.string.write), 123, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    @Override // pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
    public void onPermissionsGranted(int i, List<String> list) {
        if (i != 123) {
            return;
        }
        DonwloadSaveImg.donwloadImg(this.activity, this.urls.get(this.selectedPosition));
    }

    @Override // pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
    public void onPermissionsDenied(int i, List<String> list) {
        new AppSettingsDialog.Builder(this.activity, String.valueOf((int) R.string.rationale_ask_again)).setTitle(String.valueOf((int) R.string.title_settings_dialog)).setPositiveButton(String.valueOf((int) R.string.setting)).setNegativeButton(String.valueOf((int) R.string.cancel), null).setRequestCode(RC_SETTINGS_SCREEN).build().show();
    }

    public void create() {
        this.dialog.show();
        this.views = new ArrayList();
        this.adapter = new MyPagerAdapter(this.views, this.dialog);
        if (this.status == 0) {
            for (String str : this.urls) {
                FrameLayout frameLayout = (FrameLayout) this.activity.getLayoutInflater().inflate(R.layout.view_scale_image, (ViewGroup) null);
                TouchImageView touchImageView = (TouchImageView) frameLayout.findViewById(R.id.scale_image_view);
                this.views.add(frameLayout);
                touchImageView.setMaxZoom(4.0f);
                loadCover(touchImageView, str, this.activity);
                touchImageView.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ScaleImageView.5
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ScaleImageView.this.dialog.dismiss();
                    }
                });
            }
            this.viewPager.setAdapter(this.adapter);
        }
        this.viewPager.setCurrentItem(this.startPosition);
    }

    public static void loadCover(ImageView imageView, String str, Context context) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().frame(1000000L).centerCrop().fitCenter().placeholder(R.drawable.default_common).error(R.drawable.default_common)).load(str).into(imageView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MyPagerAdapter extends PagerAdapter {
        private Dialog dialog;
        private List<View> views;

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getItemPosition(Object obj) {
            return -2;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        MyPagerAdapter(List<View> list, Dialog dialog) {
            this.views = list;
            this.dialog = dialog;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            viewGroup.addView(this.views.get(i));
            return this.views.get(i);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            if (i == 0 && this.views.size() == 0) {
                this.dialog.dismiss();
            } else if (i == this.views.size()) {
                viewGroup.removeView(this.views.get(i - 1));
            } else {
                viewGroup.removeView(this.views.get(i));
            }
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.views.size();
        }
    }
}
