package com.home.activity.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.common.uitl.SharePersistent;
import com.home.base.LedBleActivity;
import com.home.bean.SceneBean;
import com.home.constant.CommonConstant;
import com.home.utils.BitmapTool;
import com.home.utils.ImageDispose;
import com.home.view.custom.RoundImageView;
import com.home.widget.effects.Effectstype;
import com.home.widget.effects.NiftyDialogBuilder;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SelectSceneActivity extends LedBleActivity implements View.OnClickListener {
    public static final int REQUST_CODE = 1;
    private LinearLayout containerLaout;
    private ArrayList<SceneBean> customBeans;
    private Effectstype effect;
    private ImageView ivRomance;
    private View lastView;
    private SceneBean mCustomBean;
    private TextView textViewNavLeft;
    private TextView textViewNavRight;
    private int lastSelectedItemResId = -1;
    public final String KEY_SCENE = "scene";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_selectscene);
        this.textViewNavLeft = (TextView) findViewById(R.id.textViewNavLeft);
        this.textViewNavRight = (TextView) findViewById(R.id.textViewNavRight);
        this.textViewNavLeft.setOnClickListener(this);
        this.textViewNavRight.setOnClickListener(this);
        findViewById(R.id.bedRoomLayout).setOnClickListener(this);
        findViewById(R.id.livingRoomLayout).setOnClickListener(this);
        findViewById(R.id.looLayout).setOnClickListener(this);
        findViewById(R.id.kitchenLayout).setOnClickListener(this);
        findViewById(R.id.diningLayout).setOnClickListener(this);
        findViewById(R.id.workLayout).setOnClickListener(this);
        findViewById(R.id.studyLayout).setOnClickListener(this);
        findViewById(R.id.recreationLayout).setOnClickListener(this);
        findViewById(R.id.layoutCustom).setOnClickListener(this);
    }

    private void initData() {
        ArrayList<SceneBean> arrayList = (ArrayList) SharePersistent.getObjectValue(this, CommonConstant.KEY_CUSTOM_SCENES);
        this.customBeans = arrayList;
        if (arrayList == null) {
            this.customBeans = new ArrayList<>();
        }
        if (this.customBeans.size() > 0) {
            Iterator<SceneBean> it = this.customBeans.iterator();
            while (it.hasNext()) {
                insertCustomScene(it.next());
            }
        }
    }

    private String getNameByResId(int i) {
        return getResources().getString(i);
    }

    private void insertCustomScene(final SceneBean sceneBean) {
        Bitmap Drawable2Bimap;
        this.containerLaout = (LinearLayout) findViewById(R.id.customLayout);
        View view = this.lastView;
        if (view != null) {
            view.setVisibility(0);
        }
        if (sceneBean != null) {
            findViewById(R.id.spView).setVisibility(0);
            View inflate = View.inflate(this, R.layout.item_custom_scene, null);
            final ImageView imageView = (ImageView) inflate.findViewById(R.id.ivRomance);
            imageView.setId((int) System.currentTimeMillis());
            RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.customRelLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.service.SelectSceneActivity.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (SelectSceneActivity.this.lastSelectedItemResId > 0) {
                        SelectSceneActivity selectSceneActivity = SelectSceneActivity.this;
                        selectSceneActivity.findViewById(selectSceneActivity.lastSelectedItemResId).setVisibility(4);
                        SelectSceneActivity.this.lastSelectedItemResId = -1;
                    }
                    if (SelectSceneActivity.this.ivRomance != null) {
                        SelectSceneActivity.this.ivRomance.setVisibility(4);
                        SelectSceneActivity.this.ivRomance = null;
                    }
                    SelectSceneActivity.this.mCustomBean = null;
                    imageView.setVisibility(0);
                    SelectSceneActivity.this.ivRomance = imageView;
                    SelectSceneActivity.this.mCustomBean = sceneBean;
                }
            });
            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.activity.service.SelectSceneActivity.2
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view2) {
                    SelectSceneActivity.this.deleteDialog(sceneBean);
                    return true;
                }
            });
            RoundImageView roundImageView = (RoundImageView) inflate.findViewById(R.id.ivHead);
            if (sceneBean.getImg() != null) {
                Drawable2Bimap = BitmapTool.getPicFromBytes(sceneBean.getImg(), null);
            } else {
                Drawable2Bimap = BitmapTool.Drawable2Bimap(this, R.drawable.bg_default_header);
            }
            roundImageView.setImageBitmap(Drawable2Bimap);
            TextView textView = (TextView) inflate.findViewById(R.id.customTextView);
            if (sceneBean.getName() != null) {
                textView.setText(sceneBean.getName());
            }
            View findViewById = inflate.findViewById(R.id.separator);
            findViewById.setVisibility(4);
            this.lastView = findViewById;
            this.containerLaout.addView(inflate, new ViewGroup.LayoutParams(-1, -2));
        }
    }

    private void mustDo() {
        ImageView imageView = this.ivRomance;
        if (imageView != null) {
            imageView.setVisibility(4);
            this.ivRomance = null;
        }
        this.mCustomBean = null;
        int i = this.lastSelectedItemResId;
        if (i > 0) {
            findViewById(i).setVisibility(4);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = getIntent();
        switch (id) {
            case R.id.bedRoomLayout /* 2131296350 */:
                mustDo();
                findViewById(R.id.ivBedroom).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivBedroom;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledble));
                return;
            case R.id.diningLayout /* 2131296569 */:
                mustDo();
                findViewById(R.id.ivDining).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivDining;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledspi));
                return;
            case R.id.kitchenLayout /* 2131296843 */:
                mustDo();
                findViewById(R.id.ivKtchen).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivKtchen;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledstrip));
                return;
            case R.id.livingRoomLayout /* 2131296937 */:
                mustDo();
                findViewById(R.id.ivLivingroom).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivLivingroom;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.leddmx));
                return;
            case R.id.ll_ledlike /* 2131297028 */:
                mustDo();
                findViewById(R.id.ivLedLike).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivLedLike;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledlike));
                return;
            case R.id.looLayout /* 2131297059 */:
                mustDo();
                findViewById(R.id.ivloo).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivloo;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledsmart));
                return;
            case R.id.recreationLayout /* 2131297231 */:
                mustDo();
                findViewById(R.id.ivRecreation).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivRecreation;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledwifi));
                return;
            case R.id.studyLayout /* 2131297526 */:
                mustDo();
                findViewById(R.id.ivStudy).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivStudy;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledwifi));
                return;
            case R.id.textViewNavLeft /* 2131297580 */:
                setResult(0, intent);
                finish();
                return;
            case R.id.textViewNavRight /* 2131297581 */:
                SceneBean sceneBean = this.mCustomBean;
                if (sceneBean != null) {
                    intent.putExtra("scene", sceneBean);
                }
                setResult(-1, intent);
                finish();
                return;
            case R.id.workLayout /* 2131298027 */:
                mustDo();
                findViewById(R.id.ivWork).setVisibility(0);
                this.lastSelectedItemResId = R.id.ivWork;
                ImageDispose.Bitmap2Bytes(BitmapTool.Drawable2Bimap(this, R.drawable.ledstage));
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (intent != null) {
        }
        super.onActivityResult(i, i2, intent);
    }

    private String getStringById(int i) {
        return getResources().getString(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteDialog(final SceneBean sceneBean) {
        final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
        this.effect = Effectstype.Shake;
        niftyDialogBuilder.withTitle(getStringById(R.string.text_warming)).withTitleColor("#FFFFFF").withDividerColor("#11000000").withMessage(getStringById(R.string.text_warm_content)).withMessageColor("#FFFFFF").withIcon(getResources().getDrawable(R.drawable.icon)).isCancelableOnTouchOutside(false).withDuration(700).withEffect(this.effect).withButton1Text(getStringById(R.string.text_ok)).withButton2Text(getStringById(R.string.text_cancel)).setButton1Click(new View.OnClickListener() { // from class: com.home.activity.service.SelectSceneActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectSceneActivity.this.deleteItem(sceneBean);
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).setButton2Click(new View.OnClickListener() { // from class: com.home.activity.service.SelectSceneActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteItem(SceneBean sceneBean) {
        if (this.customBeans.contains(sceneBean)) {
            this.customBeans.remove(sceneBean);
        }
        SharePersistent.setObjectValue(this, CommonConstant.KEY_CUSTOM_SCENES, this.customBeans);
        if (this.customBeans.size() > 0) {
            LinearLayout linearLayout = this.containerLaout;
            if (linearLayout != null) {
                linearLayout.removeAllViews();
            }
            Iterator<SceneBean> it = this.customBeans.iterator();
            while (it.hasNext()) {
                insertCustomScene(it.next());
            }
        }
    }
}
