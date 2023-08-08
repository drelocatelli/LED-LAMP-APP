package com.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import com.FirstActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.pictureselector.GlideEngine;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.login.LogInActivity;
import com.forum.login.PasswordUtil;
import com.forum.login.UserBean;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.ledlamp.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.squareup.picasso.Picasso;
import com.video.CircleImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class MyPersonalFragment extends LedBleFragment {
    private static final int Camea_OK = 161;
    private static final int Pic_OK = 160;
    private static final String TAG = "MyPersonalFragment";
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    Map<String, File> files;
    @BindView(R.id.ivUserPic)
    CircleImageView ivUserPic;
    LinearLayout ll_my;
    private PictureCropParameterStyle mCropParameterStyle;
    ExitCallBack mExitCall;
    private PictureParameterStyle mPictureParameterStyle;
    View mView;
    Map<String, String> params;
    Button regist;
    private List<LocalMedia> selectList = new ArrayList();
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvUserName)
    TextView tvUserName;

    /* loaded from: classes.dex */
    public interface ExitCallBack {
        void exitLog();
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mView = layoutInflater.inflate(R.layout.fragment_my_personal, viewGroup, false);
        this.params = new HashMap();
        this.files = new HashMap();
        this.ll_my = (LinearLayout) this.mView.findViewById(R.id.ll_my);
        this.regist = (Button) this.mView.findViewById(R.id.regist);
        getDefaultStyle();
        return this.mView;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
            this.ll_my.setVisibility(8);
            this.regist.setVisibility(0);
        } else {
            this.ll_my.setVisibility(0);
            this.regist.setVisibility(8);
        }
        if (!LedBleActivity.getBaseApp().getUserHeadImage().equals("")) {
            TextView textView = this.tvUserName;
            textView.setText(getResources().getString(R.string.register_account) + LedBleActivity.getBaseApp().getUserName());
            TextView textView2 = this.tvEmail;
            textView2.setText(getResources().getString(R.string.register_email) + LedBleActivity.getBaseApp().getCheckMode());
            Picasso.get().load(LedBleActivity.getBaseApp().getUserHeadImage()).placeholder(R.drawable.user_pic).into(this.ivUserPic);
            return;
        }
        TextView textView3 = this.tvUserName;
        textView3.setText(getResources().getString(R.string.register_account) + LedBleActivity.getBaseApp().getUserName());
        TextView textView4 = this.tvEmail;
        textView4.setText(getResources().getString(R.string.register_email) + LedBleActivity.getBaseApp().getCheckMode());
        this.ivUserPic.setImageResource(R.drawable.user_pic);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
            this.ll_my.setVisibility(8);
            this.regist.setVisibility(0);
        } else {
            this.ll_my.setVisibility(0);
            this.regist.setVisibility(8);
        }
        if (!LedBleActivity.getBaseApp().getUserHeadImage().equals("")) {
            TextView textView = this.tvUserName;
            textView.setText(getResources().getString(R.string.register_account) + LedBleActivity.getBaseApp().getUserName());
            TextView textView2 = this.tvEmail;
            textView2.setText(getResources().getString(R.string.register_email) + LedBleActivity.getBaseApp().getCheckMode());
            Picasso.get().load(LedBleActivity.getBaseApp().getUserHeadImage()).placeholder(R.drawable.user_pic).into(this.ivUserPic);
            return;
        }
        TextView textView3 = this.tvUserName;
        textView3.setText(getResources().getString(R.string.register_account) + LedBleActivity.getBaseApp().getUserName());
        TextView textView4 = this.tvEmail;
        textView4.setText(getResources().getString(R.string.register_email) + LedBleActivity.getBaseApp().getCheckMode());
        this.ivUserPic.setImageResource(R.drawable.user_pic);
    }

    public void setExitCallBack(ExitCallBack exitCallBack) {
        this.mExitCall = exitCallBack;
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
        final Intent intent = new Intent();
        this.btnUpdate.setOnClickListener(new View.OnClickListener() { // from class: com.me.MyPersonalFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                intent.setClass(MyPersonalFragment.this.activity, UpdatePasswordActivity.class);
                MyPersonalFragment.this.startActivity(intent);
            }
        });
        this.btnLogout.setOnClickListener(new View.OnClickListener() { // from class: com.me.MyPersonalFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LedBleActivity.getBaseApp().setUserBean(null);
                MyPersonalFragment.this.mExitCall.exitLog();
            }
        });
        this.regist.setOnClickListener(new View.OnClickListener() { // from class: com.me.MyPersonalFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MyPersonalFragment.this.startActivityForResult(new Intent(MyPersonalFragment.this.getContext(), LogInActivity.class), FirstActivity.RESULT444);
            }
        });
        this.ivUserPic.setOnClickListener(new View.OnClickListener() { // from class: com.me.MyPersonalFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
                    MyPersonalFragment.this.getUserImage();
                } else {
                    Toast.makeText(MyPersonalFragment.this.activity, (int) R.string.firstlogin, 0).show();
                }
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        Bitmap decodeFile;
        File file;
        super.onActivityResult(i, i2, intent);
        if (i != 188) {
            return;
        }
        this.selectList = PictureSelector.obtainMultipleResult(intent);
        for (int i3 = 0; i3 < this.selectList.size(); i3++) {
            this.params.clear();
            this.files.clear();
            if (SdkVersionUtils.checkedAndroid_Q()) {
                decodeFile = BitmapFactory.decodeFile(this.selectList.get(i3).getAndroidQToPath());
                file = new File(this.selectList.get(i3).getAndroidQToPath());
            } else {
                decodeFile = BitmapFactory.decodeFile(this.selectList.get(i3).getPath());
                file = new File(this.selectList.get(i3).getPath());
            }
            if (file.length() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED <= 1) {
                this.ivUserPic.setImageBitmap(decodeFile);
                this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                this.files.put(file.getName(), file);
                uploading();
            } else {
                Toast.makeText(this.activity, (int) R.string.picture, 0).show();
            }
        }
        for (LocalMedia localMedia : this.selectList) {
            Log.i(TAG, "压缩---->" + localMedia.getCompressPath());
            Log.i(TAG, "原图---->" + localMedia.getPath());
            Log.i(TAG, "裁剪---->" + localMedia.getCutPath());
            Log.i(TAG, "Android Q 特有Path---->" + localMedia.getAndroidQToPath());
        }
    }

    private void savePic(Bitmap bitmap, String str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (compress) {
                Log.e(TAG, "savePic: ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void uploading() {
        HttpUtil.getInstance().uploadFile(true, getActivity(), Constant.updateHeadImageByApp, this.params, this.files, new HttpUtil.HttpCallBack() { // from class: com.me.MyPersonalFragment.5
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e(MyPersonalFragment.TAG, "onSuccess: " + str);
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.me.MyPersonalFragment.5.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                LedBleActivity.getBaseApp().setUserBean(null);
                MyPersonalFragment.this.getHeadImage();
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Log.e(MyPersonalFragment.TAG, "onException: " + str);
                Toast.makeText(MyPersonalFragment.this.activity, (int) R.string.request_failed, 0).show();
            }
        });
    }

    public void getHeadImage() {
        this.params.clear();
        String string = LogInActivity.getString(getContext(), "account", "0000");
        String string2 = LogInActivity.getString(getContext(), "password", "0000");
        this.params.put("userName", string);
        this.params.put("password", new PasswordUtil().encrypt(string2));
        HttpUtil.getInstance().getSourceData(true, getActivity(), Constant.loginByApp, this.params, new HttpUtil.HttpCallBack() { // from class: com.me.MyPersonalFragment.6
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e("loginFragment", "onSuccess: " + str);
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<UserBean>>() { // from class: com.me.MyPersonalFragment.6.1
                }, new Feature[0]);
                if (responseBean != null) {
                    if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                        UserBean userBean = (UserBean) responseBean.getContent();
                        if (userBean == null || userBean.getToken() == null) {
                            return;
                        }
                        new LedBleApplication().setUserBean(userBean);
                        return;
                    }
                    Toast.makeText(MyPersonalFragment.this.getActivity(), responseBean.getReturnDesc(), 0).show();
                }
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Toast.makeText(MyPersonalFragment.this.getActivity(), MyPersonalFragment.this.getString(R.string.request_failed), 0).show();
            }
        });
    }

    public void getUserImage() {
        if (PermissionChecker.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE")) {
            PictureFileUtils.deleteCacheDirFile(getContext(), PictureMimeType.ofImage());
        } else {
            PermissionChecker.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        PictureSelector.create(getActivity()).openGallery(PictureMimeType.ofImage()).loadImageEngine(GlideEngine.createGlideEngine()).theme(2131821094).setPictureStyle(this.mPictureParameterStyle).setPictureCropStyle(this.mCropParameterStyle).maxSelectNum(1).minSelectNum(1).imageSpanCount(4).cameraFileName("").selectionMode(2).isSingleDirectReturn(false).previewImage(true).previewVideo(true).enablePreviewAudio(true).isCamera(true).isZoomAnim(true).enableCrop(true).compress(true).compressQuality(20).synOrAsy(true).isGif(false).openClickSound(false).selectionMedia(this.selectList).isDragFrame(false).cutOutQuality(90).minimumCompressSize(1).forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override // com.home.base.LedBleFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
    }

    private void getDefaultStyle() {
        PictureParameterStyle pictureParameterStyle = new PictureParameterStyle();
        this.mPictureParameterStyle = pictureParameterStyle;
        pictureParameterStyle.isChangeStatusBarFontColor = false;
        this.mPictureParameterStyle.isOpenCompletedNumStyle = false;
        this.mPictureParameterStyle.isOpenCheckNumStyle = false;
        this.mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        this.mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        this.mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        this.mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        this.mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        this.mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        this.mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getActivity(), R.color.picture_color_white);
        this.mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getActivity(), R.color.picture_color_white);
        this.mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        this.mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getActivity(), R.color.picture_color_fa);
        this.mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        this.mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getActivity(), R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getActivity(), R.color.picture_color_9b);
        this.mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getActivity(), R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getActivity(), R.color.picture_color_9b);
        this.mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getActivity(), R.color.picture_color_grey_3e);
        this.mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        this.mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        this.mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
        this.mCropParameterStyle = new PictureCropParameterStyle(ContextCompat.getColor(getActivity(), R.color.app_color_grey), ContextCompat.getColor(getActivity(), R.color.app_color_grey), Color.parseColor("#393a3e"), ContextCompat.getColor(getActivity(), R.color.app_color_white), this.mPictureParameterStyle.isChangeStatusBarFontColor);
    }
}
