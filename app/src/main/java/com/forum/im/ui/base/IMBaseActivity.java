package com.forum.im.ui.base;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import androidx.viewpager.widget.ViewPager;
import com.common.uitl.Constant;
import com.forum.im.adapter.DataAdapter;
import com.forum.im.adapter.ExpressionAdapter;
import com.forum.im.adapter.ExpressionPagerAdapter;
import com.forum.im.db.ChatDbManager;
import com.forum.im.db.ChatMessageBean;
import com.forum.im.utils.FileSaveUtil;
import com.forum.im.utils.ImageCheckoutUtil;
import com.forum.im.utils.KeyBoardUtils;
import com.forum.im.utils.PictureUtil;
import com.forum.im.utils.ScreenUtil;
import com.forum.im.utils.SmileUtils;
import com.forum.im.widget.AudioRecordButton;
import com.forum.im.widget.ChatBottomView;
import com.forum.im.widget.ExpandGridView;
import com.forum.im.widget.HeadIconSelectorView;
import com.forum.im.widget.MediaManager;
import com.forum.im.widget.pulltorefresh.PullToRefreshLayout;
import com.ledlamp.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public abstract class IMBaseActivity extends Activity implements View.OnClickListener {
    private static final int CAMERA_REQUEST_CODE = 16;
    private static final int IMAGE_SIZE = 102400;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 18;
    public static final int PULL_TO_REFRESH_DOWN = 273;
    public static final int RECERIVE_OK = 4369;
    public static final int REFRESH = 17;
    private static final int SDK_PERMISSION_REQUEST = 127;
    public static final int SEND_OK = 4368;
    public View activityRootView;
    private DataAdapter adapter;
    public Button btnSend;
    private String camPicPath;
    public ImageView emoji;
    public LinearLayout emoji_group;
    public ViewPager expressionViewpager;
    private ImageView imageViewAdd;
    private boolean isAndroidQ;
    public ImageView ivBack;
    private Uri mCameraUri;
    public ChatDbManager mChatDbManager;
    public File mCurrentPhotoFile;
    public EditText mEditTextContent;
    private Toast mToast;
    public ImageView mess_iv;
    public ListView mess_lv;
    private View.OnKeyListener onKeyListener;
    private String permissionInfo;
    public int position;
    public PullToRefreshLayout pullList;
    private List<String> reslist;
    public TextView send_emoji_icon;
    public ChatBottomView tbbv;
    public TextView tvTitle;
    public AudioRecordButton voiceBtn;
    public ImageView voiceIv;
    public boolean isDown = false;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private boolean CAN_RECORD_AUDIO = true;
    public int bottomStatusHeight = 0;
    public int listSlideHeight = 0;
    public String userName = "test";
    public String[] item = {"你好!", "我正忙着呢,等等", "有啥事吗？", "有时间聊聊吗", "再见！"};
    public List<ChatMessageBean> tblist = new ArrayList();
    public int page = 0;
    public int number = 10;
    public List<ChatMessageBean> pagelist = new ArrayList();
    public ArrayList<String> imageList = new ArrayList<>();
    public HashMap<Integer, Integer> imagePosition = new HashMap<>();

    protected void doRight() {
    }

    protected abstract void loadRecords();

    protected abstract void sendImage(Long l, String str, Boolean bool);

    protected abstract void sendMessage(Long l, Boolean bool, String str);

    protected abstract void sendVoice(float f, String str);

    public IMBaseActivity() {
        this.isAndroidQ = Build.VERSION.SDK_INT >= 29;
        this.onKeyListener = new View.OnKeyListener() { // from class: com.forum.im.ui.base.IMBaseActivity.13
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 66 && keyEvent.getAction() == 0) {
                    IMBaseActivity.this.sendMessage(null, true, "");
                    return true;
                }
                return false;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_chat);
        findView();
        initpop();
        init();
        getPersimmions();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        MediaManager.pause();
        MediaManager.release();
        cancelToast();
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void findView() {
        ImageView imageView = (ImageView) findViewById(R.id.ivBack);
        this.ivBack = imageView;
        imageView.setOnClickListener(this);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageViewAdd);
        this.imageViewAdd = imageView2;
        imageView2.setVisibility(8);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        Button button = (Button) findViewById(R.id.btnSend);
        this.btnSend = button;
        button.setOnClickListener(this);
        this.pullList = (PullToRefreshLayout) findViewById(R.id.content_lv);
        this.activityRootView = findViewById(R.id.layout_tongbao_rl);
        this.mEditTextContent = (EditText) findViewById(R.id.mess_et);
        this.mess_iv = (ImageView) findViewById(R.id.mess_iv);
        this.emoji = (ImageView) findViewById(R.id.emoji);
        this.voiceIv = (ImageView) findViewById(R.id.voice_iv);
        this.expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        this.voiceBtn = (AudioRecordButton) findViewById(R.id.voice_btn);
        this.emoji_group = (LinearLayout) findViewById(R.id.emoji_group);
        this.send_emoji_icon = (TextView) findViewById(R.id.send_emoji_icon);
        this.tbbv = (ChatBottomView) findViewById(R.id.other_lv);
        initActionBar();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void init() {
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(null).discCacheSize(52428800).discCacheFileCount(100).writeDebugLogs().build());
        this.mEditTextContent.setOnKeyListener(this.onKeyListener);
        this.mChatDbManager = new ChatDbManager();
        new PullToRefreshLayout.pulltorefreshNotifier() { // from class: com.forum.im.ui.base.IMBaseActivity.1
            @Override // com.forum.im.widget.pulltorefresh.PullToRefreshLayout.pulltorefreshNotifier
            public void onPull() {
            }
        };
        this.voiceIv.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (IMBaseActivity.this.voiceBtn.getVisibility() == 8) {
                    IMBaseActivity.this.emoji.setBackgroundResource(R.drawable.emoji);
                    IMBaseActivity.this.mess_iv.setBackgroundResource(R.drawable.tb_more);
                    IMBaseActivity.this.mEditTextContent.setVisibility(8);
                    IMBaseActivity.this.emoji_group.setVisibility(8);
                    IMBaseActivity.this.tbbv.setVisibility(8);
                    IMBaseActivity.this.mess_lv.setVisibility(8);
                    IMBaseActivity.this.voiceBtn.setVisibility(0);
                    IMBaseActivity iMBaseActivity = IMBaseActivity.this;
                    KeyBoardUtils.hideKeyBoard(iMBaseActivity, iMBaseActivity.mEditTextContent);
                    IMBaseActivity.this.voiceIv.setBackgroundResource(R.drawable.chatting_setmode_keyboard_btn_normal);
                    return;
                }
                IMBaseActivity.this.mEditTextContent.setVisibility(0);
                IMBaseActivity.this.voiceBtn.setVisibility(8);
                IMBaseActivity.this.voiceIv.setBackgroundResource(R.drawable.voice_btn_normal);
                IMBaseActivity iMBaseActivity2 = IMBaseActivity.this;
                KeyBoardUtils.showKeyBoard(iMBaseActivity2, iMBaseActivity2.mEditTextContent);
            }
        });
        this.mess_iv.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                IMBaseActivity.this.emoji_group.setVisibility(8);
                if (IMBaseActivity.this.tbbv.getVisibility() == 8 && IMBaseActivity.this.mess_lv.getVisibility() == 8) {
                    IMBaseActivity.this.mEditTextContent.setVisibility(0);
                    IMBaseActivity.this.mess_iv.setFocusable(true);
                    IMBaseActivity.this.voiceBtn.setVisibility(8);
                    IMBaseActivity.this.emoji.setBackgroundResource(R.drawable.emoji);
                    IMBaseActivity.this.voiceIv.setBackgroundResource(R.drawable.voice_btn_normal);
                    IMBaseActivity.this.tbbv.setVisibility(0);
                    IMBaseActivity iMBaseActivity = IMBaseActivity.this;
                    KeyBoardUtils.hideKeyBoard(iMBaseActivity, iMBaseActivity.mEditTextContent);
                    IMBaseActivity.this.mess_iv.setBackgroundResource(R.drawable.chatting_setmode_keyboard_btn_normal);
                    return;
                }
                IMBaseActivity.this.tbbv.setVisibility(8);
                IMBaseActivity iMBaseActivity2 = IMBaseActivity.this;
                KeyBoardUtils.showKeyBoard(iMBaseActivity2, iMBaseActivity2.mEditTextContent);
                IMBaseActivity.this.mess_iv.setBackgroundResource(R.drawable.tb_more);
                if (IMBaseActivity.this.mess_lv.getVisibility() != 8) {
                    IMBaseActivity.this.mess_lv.setVisibility(8);
                    IMBaseActivity iMBaseActivity3 = IMBaseActivity.this;
                    KeyBoardUtils.showKeyBoard(iMBaseActivity3, iMBaseActivity3.mEditTextContent);
                    IMBaseActivity.this.mess_iv.setBackgroundResource(R.drawable.tb_more);
                }
            }
        });
        this.send_emoji_icon.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                IMBaseActivity.this.sendMessage(null, true, "");
            }
        });
        this.tbbv.setOnHeadIconClickListener(new HeadIconSelectorView.OnHeadIconClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.5
            @Override // com.forum.im.widget.HeadIconSelectorView.OnHeadIconClickListener
            public void onClick(int i) {
                if (i == 1) {
                    if (IMBaseActivity.this.CAN_WRITE_EXTERNAL_STORAGE) {
                        IMBaseActivity.this.checkPermissionAndCamera();
                    } else {
                        Toast.makeText(IMBaseActivity.this, "权限未开通\n请到设置中开通相册权限", 0).show();
                    }
                } else if (i == 2) {
                    if (!IMBaseActivity.this.CAN_WRITE_EXTERNAL_STORAGE) {
                        Toast.makeText(IMBaseActivity.this, "权限未开通\n请到设置中开通相册权限", 0).show();
                    } else if (Environment.getExternalStorageState().equals("mounted")) {
                        Intent intent = new Intent();
                        if (Build.VERSION.SDK_INT < 19) {
                            intent.setAction("android.intent.action.GET_CONTENT");
                        } else {
                            intent.setAction("android.intent.action.OPEN_DOCUMENT");
                            intent.addCategory("android.intent.category.OPENABLE");
                            intent.putExtra("crop", "true");
                            intent.putExtra("scale", "true");
                            intent.putExtra("scaleUpIfNeeded", true);
                        }
                        intent.setType("image/*");
                        IMBaseActivity.this.startActivityForResult(intent, 2);
                    } else {
                        IMBaseActivity.this.showToast("没有SD卡");
                    }
                } else if (i == 3 && IMBaseActivity.this.mess_lv.getVisibility() == 8) {
                    IMBaseActivity.this.tbbv.setVisibility(8);
                    IMBaseActivity.this.emoji.setBackgroundResource(R.drawable.emoji);
                    IMBaseActivity.this.voiceIv.setBackgroundResource(R.drawable.voice_btn_normal);
                    IMBaseActivity.this.mess_lv.setVisibility(0);
                    IMBaseActivity iMBaseActivity = IMBaseActivity.this;
                    KeyBoardUtils.hideKeyBoard(iMBaseActivity, iMBaseActivity.mEditTextContent);
                    IMBaseActivity.this.mess_iv.setBackgroundResource(R.drawable.chatting_setmode_keyboard_btn_normal);
                }
            }
        });
        this.emoji.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                IMBaseActivity.this.mess_lv.setVisibility(8);
                IMBaseActivity.this.tbbv.setVisibility(8);
                if (IMBaseActivity.this.emoji_group.getVisibility() == 8) {
                    IMBaseActivity.this.mEditTextContent.setVisibility(0);
                    IMBaseActivity.this.voiceBtn.setVisibility(8);
                    IMBaseActivity.this.voiceIv.setBackgroundResource(R.drawable.voice_btn_normal);
                    IMBaseActivity.this.mess_iv.setBackgroundResource(R.drawable.tb_more);
                    IMBaseActivity.this.emoji_group.setVisibility(0);
                    IMBaseActivity.this.emoji.setBackgroundResource(R.drawable.chatting_setmode_keyboard_btn_normal);
                    IMBaseActivity iMBaseActivity = IMBaseActivity.this;
                    KeyBoardUtils.hideKeyBoard(iMBaseActivity, iMBaseActivity.mEditTextContent);
                    return;
                }
                IMBaseActivity.this.emoji_group.setVisibility(8);
                IMBaseActivity.this.emoji.setBackgroundResource(R.drawable.emoji);
                IMBaseActivity iMBaseActivity2 = IMBaseActivity.this;
                KeyBoardUtils.showKeyBoard(iMBaseActivity2, iMBaseActivity2.mEditTextContent);
            }
        });
        this.reslist = getExpressionRes(40);
        ArrayList arrayList = new ArrayList();
        View gridChildView = getGridChildView(1);
        View gridChildView2 = getGridChildView(2);
        arrayList.add(gridChildView);
        arrayList.add(gridChildView2);
        this.expressionViewpager.setAdapter(new ExpressionPagerAdapter(arrayList));
        this.mEditTextContent.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                IMBaseActivity.this.emoji_group.setVisibility(8);
                IMBaseActivity.this.tbbv.setVisibility(8);
                IMBaseActivity.this.mess_lv.setVisibility(8);
                IMBaseActivity.this.emoji.setBackgroundResource(R.drawable.emoji);
                IMBaseActivity.this.mess_iv.setBackgroundResource(R.drawable.tb_more);
                IMBaseActivity.this.voiceIv.setBackgroundResource(R.drawable.voice_btn_normal);
            }
        });
        this.mess_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.8
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                IMBaseActivity.this.mEditTextContent.setText(IMBaseActivity.this.item[i]);
                IMBaseActivity.this.sendMessage(null, false, "");
            }
        });
        this.bottomStatusHeight = ScreenUtil.getNavigationBarHeight(this);
        this.page = (int) this.mChatDbManager.getPages(this.number);
        loadRecords();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermissionAndCamera() {
        if (ContextCompat.checkSelfPermission(getApplication(), "android.permission.CAMERA") == 0) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 18);
        }
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("mime_type", PictureMimeType.MIME_TYPE_IMAGE);
        String format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE, format);
        Uri insert = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        this.mCameraUri = insert;
        intent.putExtra("output", insert);
        startActivityForResult(intent, 16);
    }

    private Uri createImageUri() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        }
        return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
    }

    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdir();
        }
        File file = new File(externalFilesDir, format);
        if ("mounted".equals(EnvironmentCompat.getStorageState(file))) {
            return file;
        }
        return null;
    }

    private void initActionBar() {
        if (getActionBar() == null) {
            return;
        }
        getActionBar().setCustomView(R.layout.layout_action_bar);
        getActionBar().setDisplayOptions(16);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.ivLeft) {
                    IMBaseActivity.this.doLeft();
                } else if (id == R.id.ivRight) {
                    IMBaseActivity.this.doRight();
                } else if (id != R.id.llRight) {
                } else {
                    IMBaseActivity.this.doRight();
                }
            }
        };
        getActionBar().getCustomView().findViewById(R.id.ivLeft).setOnClickListener(onClickListener);
        getActionBar().getCustomView().findViewById(R.id.ivRight).setOnClickListener(onClickListener);
        getActionBar().getCustomView().findViewById(R.id.llRight).setOnClickListener(onClickListener);
        ((TextView) getActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(getTitle().toString());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSend) {
            sendMessage(null, true, "");
        } else if (id != R.id.ivBack) {
        } else {
            finish();
        }
    }

    protected void getPersimmions() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> arrayList = new ArrayList<>();
            if (addPermission(arrayList, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                this.permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (addPermission(arrayList, "android.permission.RECORD_AUDIO")) {
                this.permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (arrayList.size() > 0) {
                requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    private boolean addPermission(ArrayList<String> arrayList, String str) {
        if (checkSelfPermission(str) == 0 || shouldShowRequestPermissionRationale(str)) {
            return true;
        }
        arrayList.add(str);
        return false;
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == SDK_PERMISSION_REQUEST) {
            HashMap hashMap = new HashMap();
            hashMap.put("android.permission.WRITE_EXTERNAL_STORAGE", 0);
            hashMap.put("android.permission.RECORD_AUDIO", 0);
            for (int i2 = 0; i2 < strArr.length; i2++) {
                hashMap.put(strArr[i2], Integer.valueOf(iArr[i2]));
            }
            if (((Integer) hashMap.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() != 0) {
                this.CAN_WRITE_EXTERNAL_STORAGE = false;
                Toast.makeText(this, "禁用图片权限将导致发送图片功能无法使用！", 0).show();
            }
            if (((Integer) hashMap.get("android.permission.RECORD_AUDIO")).intValue() != 0) {
                this.CAN_RECORD_AUDIO = false;
                Toast.makeText(this, "禁用录制音频权限将导致语音功能无法使用！", 0).show();
                return;
            }
            return;
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    protected void doLeft() {
        finish();
    }

    private void initpop() {
        this.mess_lv = (ListView) findViewById(R.id.mess_lv);
        DataAdapter dataAdapter = new DataAdapter(this, this.item);
        this.adapter = dataAdapter;
        this.mess_lv.setAdapter((ListAdapter) dataAdapter);
    }

    private void downLoad() {
        if (this.isDown) {
            return;
        }
        new Thread(new Runnable() { // from class: com.forum.im.ui.base.IMBaseActivity.10
            @Override // java.lang.Runnable
            public void run() {
                IMBaseActivity.this.loadRecords();
            }
        }).start();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        Uri uri;
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            this.tbbv.setVisibility(8);
            this.mess_iv.setBackgroundResource(R.drawable.tb_more);
            if (i == 2) {
                String path = FileSaveUtil.getPath(getApplicationContext(), intent.getData());
                File file = new File(path);
                this.mCurrentPhotoFile = file;
                if (file.exists()) {
                    if (ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLoacalBitmap(path)) > IMAGE_SIZE) {
                        sendImage(null, path, true);
                        return;
                    } else {
                        sendImage(null, path, true);
                        return;
                    }
                }
                showToast("The file does not exist!");
            } else if (i != 16) {
            } else {
                if (intent != null) {
                    uri = intent.getData();
                } else {
                    uri = this.mCameraUri;
                }
                String path2 = FileSaveUtil.getPath(getApplicationContext(), uri);
                File file2 = new File(path2);
                this.mCurrentPhotoFile = file2;
                if (file2.exists()) {
                    if (ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLoacalBitmap(path2)) > IMAGE_SIZE) {
                        sendImage(null, path2, true);
                        return;
                    } else {
                        sendImage(null, path2, true);
                        return;
                    }
                }
                showToast("The file does not exist!");
            }
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 4) {
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void reset() {
        this.emoji_group.setVisibility(8);
        this.tbbv.setVisibility(8);
        this.mess_lv.setVisibility(8);
        this.emoji.setBackgroundResource(R.drawable.emoji);
        this.mess_iv.setBackgroundResource(R.drawable.tb_more);
        this.voiceIv.setBackgroundResource(R.drawable.voice_btn_normal);
    }

    public void showToast(String str) {
        Toast toast = this.mToast;
        if (toast == null) {
            this.mToast = Toast.makeText(this, str, 0);
        } else {
            toast.setText(str);
            this.mToast.setDuration(0);
        }
        this.mToast.show();
    }

    public void cancelToast() {
        Toast toast = this.mToast;
        if (toast != null) {
            toast.cancel();
        }
    }

    private View getGridChildView(int i) {
        View inflate = View.inflate(this, R.layout.layout_expression_gridview, null);
        ExpandGridView expandGridView = (ExpandGridView) inflate.findViewById(R.id.gridview);
        ArrayList arrayList = new ArrayList();
        if (i == 1) {
            arrayList.addAll(this.reslist.subList(0, 20));
        } else if (i == 2) {
            List<String> list = this.reslist;
            arrayList.addAll(list.subList(20, list.size()));
        }
        arrayList.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, arrayList);
        expandGridView.setAdapter((ListAdapter) expressionAdapter);
        expandGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.forum.im.ui.base.IMBaseActivity.11
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                int selectionStart;
                String item = expressionAdapter.getItem(i2);
                try {
                    if (item != "delete_expression") {
                        Field field = Class.forName("com.maxi.chatdemo.utils.SmileUtils").getField(item);
                        String obj = IMBaseActivity.this.mEditTextContent.getText().toString();
                        int max = Math.max(IMBaseActivity.this.mEditTextContent.getSelectionStart(), 0);
                        StringBuilder sb = new StringBuilder(obj);
                        Spannable smiledText = SmileUtils.getSmiledText(IMBaseActivity.this, (String) field.get(null));
                        sb.insert(max, (CharSequence) smiledText);
                        IMBaseActivity.this.mEditTextContent.setText(sb.toString());
                        IMBaseActivity.this.mEditTextContent.setSelection(max + smiledText.length());
                    } else if (!TextUtils.isEmpty(IMBaseActivity.this.mEditTextContent.getText()) && (selectionStart = IMBaseActivity.this.mEditTextContent.getSelectionStart()) > 0) {
                        String substring = IMBaseActivity.this.mEditTextContent.getText().toString().substring(0, selectionStart);
                        int lastIndexOf = substring.lastIndexOf("[");
                        if (lastIndexOf != -1) {
                            if (SmileUtils.containsKey(substring.substring(lastIndexOf, selectionStart).toString())) {
                                IMBaseActivity.this.mEditTextContent.getEditableText().delete(lastIndexOf, selectionStart);
                            } else {
                                IMBaseActivity.this.mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                            }
                        } else {
                            IMBaseActivity.this.mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                        }
                    }
                } catch (Exception unused) {
                }
            }
        });
        return inflate;
    }

    public List<String> getExpressionRes(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 1; i2 <= i; i2++) {
            arrayList.add("f" + i2);
        }
        return arrayList;
    }

    private void showDialog(final String str) {
        new Thread(new Runnable() { // from class: com.forum.im.ui.base.IMBaseActivity.12
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String savePicPath = IMBaseActivity.this.getSavePicPath();
                    boolean saveBitmap = FileSaveUtil.saveBitmap(PictureUtil.reviewPicRotate(PictureUtil.compressSizeImage(str), savePicPath), savePicPath);
                    if (new File(savePicPath).exists() && saveBitmap) {
                        IMBaseActivity.this.sendImage(null, str, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSavePicPath() {
        String str = FileSaveUtil.SD_CARD_PATH + "image_data/";
        try {
            FileSaveUtil.createSDDirectory(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str + String.valueOf(System.currentTimeMillis() + PictureMimeType.PNG);
    }

    public ChatMessageBean getTbub(Long l, String str, int i, String str2, String str3, String str4, String str5, String str6, String str7, String str8, Float f, int i2) {
        ChatMessageBean chatMessageBean = new ChatMessageBean();
        chatMessageBean.setId(l);
        chatMessageBean.setUserName(str);
        chatMessageBean.setTime(str3);
        chatMessageBean.setType(i);
        chatMessageBean.setUserContent(str2);
        chatMessageBean.setImageIconUrl(str4);
        chatMessageBean.setImageUrl(str5);
        chatMessageBean.setUserVoicePath(str7);
        chatMessageBean.setUserVoiceUrl(str8);
        chatMessageBean.setUserVoiceTime(f.floatValue());
        chatMessageBean.setSendState(i2);
        chatMessageBean.setImageLocal(str6);
        this.mChatDbManager.insert(chatMessageBean);
        return chatMessageBean;
    }

    public static String returnTime() {
        return new SimpleDateFormat(Constant.STRING_DAY_FORMAT4).format(new Date());
    }
}
