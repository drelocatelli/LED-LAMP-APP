package com.home.activity.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.common.listener.ReceiveDataListener;
import com.common.listener.ReceiveDataListenerManager;
import com.common.net.NetResult;
import com.common.uitl.SharePersistent;
import com.home.activity.main.MainActivity_DMX02;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.net.NetConnectBle;
import com.home.utils.Utils;
import com.home.view.custom.LevelProgressBar;
import com.ledlamp.R;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.droidsonroids.gif.GifImageView;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class MaterialLibraryActivity extends LedBleActivity implements ReceiveDataListener {
    private static final String TAG = "MaterialLibraryActivity";
    private static String materialType = "1";
    private Button buttonBack;
    private String cmdString;
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private int index;
    private ImageView ivExpression;
    private ImageView ivFestival;
    private ImageView ivOther;
    private ImageView ivPopular;
    JSONArray jsonArray;
    JSONObject jsonObject;
    private Handler overtimeHandler;
    private Runnable overtimeRunnable;
    private LevelProgressBar progressBar;
    private boolean receiveData;
    private RelativeLayout rlProgressView;
    private Handler sendCmdHandler;
    private Runnable sendCmdRunnable;
    private int timeoutCount;
    private TextView tvProgress;
    private TextView tvSend;
    private Map<String, String> params = new HashMap();
    private ArrayList<Bitmap> bitmapsOfGif = new ArrayList<>();
    private ArrayList<String> cmdStringList = new ArrayList<>();
    private List<String> contentList = new ArrayList();
    private List<String> imageVisitUrlList = new ArrayList();

    static /* synthetic */ int access$1308(MaterialLibraryActivity materialLibraryActivity) {
        int i = materialLibraryActivity.index;
        materialLibraryActivity.index = i + 1;
        return i;
    }

    static /* synthetic */ int access$2608(MaterialLibraryActivity materialLibraryActivity) {
        int i = materialLibraryActivity.timeoutCount;
        materialLibraryActivity.timeoutCount = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        materialType = "1";
        getURL();
    }

    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        ReceiveDataListenerManager.getInstance().unRegisterListener(this);
    }

    @Override // com.common.listener.ReceiveDataListener
    public void notifyReceiveData(String str, String str2) {
        if (!str.equalsIgnoreCase("MaterialLibraryActivity") || str2 == null) {
            return;
        }
        Log.i("MaterialLibraryActivity", "reveice data =" + str2 + ", index = " + this.index);
        this.timeoutCount = 0;
        this.receiveData = true;
        int i = this.index;
        if (i == -1) {
            if (str2.contains("5A") || str2.contains("5a")) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 100L);
            }
        } else if (i >= 0) {
            String[] split = str2.split(" ");
            int parseInt = Integer.parseInt(split[split.length - 1] + split[split.length - 2] + split[split.length - 3] + split[split.length - 4], 16);
            Log.i("MaterialLibraryActivity", "index =" + this.index + ", numberIndex = " + parseInt);
            if (this.index == parseInt) {
                this.sendCmdHandler.postDelayed(this.sendCmdRunnable, 100L);
            }
        }
    }

    public void initView() {
        setContentView(R.layout.dmx02_activity_materiallibrary);
        ReceiveDataListenerManager.getInstance().registerListtener(this);
        this.rlProgressView = (RelativeLayout) findViewById(R.id.rlProgressView);
        this.progressBar = (LevelProgressBar) findViewById(R.id.progressBar);
        this.tvProgress = (TextView) findViewById(R.id.tvProgress);
        Button button = (Button) findViewById(R.id.buttonBack);
        this.buttonBack = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.MaterialLibraryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MaterialLibraryActivity.this.finish();
            }
        });
        this.imageAdapter = new ImageAdapter(getApplicationContext());
        GridView gridView = (GridView) findViewById(R.id.gridView);
        this.gridView = gridView;
        gridView.setAdapter((ListAdapter) this.imageAdapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.other.MaterialLibraryActivity.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Log.e("MaterialLibraryActivity", "ModeDmxA1  :  " + i);
                MaterialLibraryActivity.this.imageAdapter.clearSelection(i);
                MaterialLibraryActivity.this.imageAdapter.notifyDataSetChanged();
                MaterialLibraryActivity.this.bitmapsOfGif.clear();
                Glide.with(MaterialLibraryActivity.this.getApplicationContext()).asGif().load((String) MaterialLibraryActivity.this.imageVisitUrlList.get(i)).into((RequestBuilder<GifDrawable>) new SimpleTarget<GifDrawable>() { // from class: com.home.activity.other.MaterialLibraryActivity.2.1
                    @Override // com.bumptech.glide.request.target.Target
                    public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                        onResourceReady((GifDrawable) obj, (Transition<? super GifDrawable>) transition);
                    }

                    public void onResourceReady(GifDrawable gifDrawable, Transition<? super GifDrawable> transition) {
                        try {
                            Drawable.ConstantState constantState = gifDrawable.getConstantState();
                            Field declaredField = constantState.getClass().getDeclaredField("frameLoader");
                            declaredField.setAccessible(true);
                            Object obj = declaredField.get(constantState);
                            Field declaredField2 = obj.getClass().getDeclaredField("gifDecoder");
                            declaredField2.setAccessible(true);
                            StandardGifDecoder standardGifDecoder = (StandardGifDecoder) declaredField2.get(obj);
                            for (int i2 = 0; i2 < standardGifDecoder.getFrameCount(); i2++) {
                                standardGifDecoder.advance();
                                MaterialLibraryActivity.this.bitmapsOfGif.add(MaterialLibraryActivity.scale(standardGifDecoder.getNextFrame(), MaterialLibraryActivity.this.getPixSetLong(), MaterialLibraryActivity.this.getPixSetLong()));
                                Log.e("MaterialLibraryActivity", "standardGifDecoder.getFrameCount() = " + standardGifDecoder.getFrameCount());
                            }
                            MaterialLibraryActivity.this.getCmdStringList(MaterialLibraryActivity.this.bitmapsOfGif);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.ivPopular);
        this.ivPopular = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.MaterialLibraryActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String unused = MaterialLibraryActivity.materialType = "1";
                MaterialLibraryActivity.this.ivExpression.setImageResource(R.drawable.dmx02_expression);
                MaterialLibraryActivity.this.ivFestival.setImageResource(R.drawable.dmx02_festival);
                MaterialLibraryActivity.this.ivOther.setImageResource(R.drawable.dmx02_other);
                MaterialLibraryActivity.this.ivPopular.setImageResource(R.drawable.dmx02_popular_check);
                MaterialLibraryActivity.this.getURL();
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.ivFestival);
        this.ivFestival = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.MaterialLibraryActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String unused = MaterialLibraryActivity.materialType = ExifInterface.GPS_MEASUREMENT_2D;
                MaterialLibraryActivity.this.ivExpression.setImageResource(R.drawable.dmx02_expression);
                MaterialLibraryActivity.this.ivFestival.setImageResource(R.drawable.dmx02_festival_check);
                MaterialLibraryActivity.this.ivOther.setImageResource(R.drawable.dmx02_other);
                MaterialLibraryActivity.this.ivPopular.setImageResource(R.drawable.dmx02_popular);
                MaterialLibraryActivity.this.getURL();
            }
        });
        ImageView imageView3 = (ImageView) findViewById(R.id.ivExpression);
        this.ivExpression = imageView3;
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.MaterialLibraryActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String unused = MaterialLibraryActivity.materialType = ExifInterface.GPS_MEASUREMENT_3D;
                MaterialLibraryActivity.this.ivExpression.setImageResource(R.drawable.dmx02_expression_check);
                MaterialLibraryActivity.this.ivFestival.setImageResource(R.drawable.dmx02_festival);
                MaterialLibraryActivity.this.ivOther.setImageResource(R.drawable.dmx02_other);
                MaterialLibraryActivity.this.ivPopular.setImageResource(R.drawable.dmx02_popular);
                MaterialLibraryActivity.this.getURL();
            }
        });
        ImageView imageView4 = (ImageView) findViewById(R.id.ivOther);
        this.ivOther = imageView4;
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.MaterialLibraryActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String unused = MaterialLibraryActivity.materialType = NetResult.CODE_OK;
                MaterialLibraryActivity.this.ivExpression.setImageResource(R.drawable.dmx02_expression);
                MaterialLibraryActivity.this.ivFestival.setImageResource(R.drawable.dmx02_festival);
                MaterialLibraryActivity.this.ivOther.setImageResource(R.drawable.dmx02_other_check);
                MaterialLibraryActivity.this.ivPopular.setImageResource(R.drawable.dmx02_popular);
                MaterialLibraryActivity.this.getURL();
            }
        });
        TextView textView = (TextView) findViewById(R.id.tvSend);
        this.tvSend = textView;
        textView.setOnClickListener(new AnonymousClass7());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.home.activity.other.MaterialLibraryActivity$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            view.startAnimation(AnimationUtils.loadAnimation(MaterialLibraryActivity.this.getBaseContext(), R.anim.layout_scale));
            SharePersistent.savePerference(MaterialLibraryActivity.this.getApplicationContext(), Constant.Activity, "MaterialLibraryActivity");
            if (MaterialLibraryActivity.this.cmdString == null) {
                return;
            }
            MaterialLibraryActivity.this.receiveData = false;
            MaterialLibraryActivity.this.index = -2;
            MaterialLibraryActivity.this.progressBar.setLevels(MaterialLibraryActivity.this.cmdStringList.size());
            MaterialLibraryActivity.this.rlProgressView.setVisibility(0);
            MaterialLibraryActivity.this.tvSend.setEnabled(false);
            MaterialLibraryActivity.this.index = -2;
            if (MaterialLibraryActivity.this.sendCmdHandler == null) {
                MaterialLibraryActivity.this.sendCmdHandler = new Handler();
            }
            if (MaterialLibraryActivity.this.sendCmdRunnable == null) {
                MaterialLibraryActivity.this.sendCmdRunnable = new Runnable() { // from class: com.home.activity.other.MaterialLibraryActivity.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MaterialLibraryActivity.access$1308(MaterialLibraryActivity.this);
                        MaterialLibraryActivity.this.receiveData = false;
                        if (MaterialLibraryActivity.this.index == MaterialLibraryActivity.this.cmdStringList.size()) {
                            MaterialLibraryActivity.this.progressBar.setCurrentLevel(0);
                            MaterialLibraryActivity.this.tvProgress.setText("0%");
                            MaterialLibraryActivity.this.rlProgressView.setVisibility(8);
                            MaterialLibraryActivity.this.tvSend.setEnabled(true);
                            MaterialLibraryActivity.this.sendCmdHandler.removeCallbacks(MaterialLibraryActivity.this.sendCmdRunnable);
                            MaterialLibraryActivity.this.overtimeHandler.removeCallbacks(MaterialLibraryActivity.this.overtimeRunnable);
                            MaterialLibraryActivity.this.index = -2;
                            return;
                        }
                        if (MaterialLibraryActivity.this.progressBar != null) {
                            MaterialLibraryActivity.this.progressBar.setCurrentLevel(MaterialLibraryActivity.this.index + 1);
                            MaterialLibraryActivity.this.progressBar.setAnimMaxTime(300);
                        }
                        if (MaterialLibraryActivity.this.cmdStringList.size() > 0 && MaterialLibraryActivity.this.tvProgress != null) {
                            MaterialLibraryActivity.this.tvProgress.setText((((MaterialLibraryActivity.this.index + 1) * 100) / MaterialLibraryActivity.this.cmdStringList.size()) + "%");
                        }
                        if (MaterialLibraryActivity.this.index == -1) {
                            int[] numb = MaterialLibraryActivity.this.getNumb(MaterialLibraryActivity.this.cmdString.length() / 2);
                            NetConnectBle.getInstance().sendDmx02DataToFFE1WithCallback(new int[]{90, 74, 1, 128, 12, 0, 1, 0, HttpStatus.SC_NO_CONTENT, 0, numb[0], numb[1], numb[2], numb[3], 0, 0, 0, 0});
                        } else if (MaterialLibraryActivity.this.index >= 0) {
                            NetConnectBle.getInstance().sendDmx02DataToFFE2WithCallback(MaterialLibraryActivity.HexStringToIntArray((MaterialLibraryActivity.this.to04Hex(MaterialLibraryActivity.this.index).substring(2) + MaterialLibraryActivity.this.to04Hex(MaterialLibraryActivity.this.index).substring(0, 2)) + "0000" + ((String) MaterialLibraryActivity.this.cmdStringList.get(MaterialLibraryActivity.this.index))));
                        }
                        if (MaterialLibraryActivity.this.overtimeHandler == null) {
                            MaterialLibraryActivity.this.overtimeHandler = new Handler();
                        }
                        if (MaterialLibraryActivity.this.overtimeRunnable == null) {
                            MaterialLibraryActivity.this.overtimeRunnable = new Runnable() { // from class: com.home.activity.other.MaterialLibraryActivity.7.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    MaterialLibraryActivity.access$2608(MaterialLibraryActivity.this);
                                    if (MaterialLibraryActivity.this.timeoutCount > 3) {
                                        if (MaterialLibraryActivity.this.rlProgressView != null && MaterialLibraryActivity.this.tvSend != null) {
                                            MaterialLibraryActivity.this.rlProgressView.setVisibility(8);
                                            MaterialLibraryActivity.this.tvSend.setEnabled(true);
                                        }
                                        MaterialLibraryActivity.this.sendCmdHandler.removeCallbacks(MaterialLibraryActivity.this.sendCmdRunnable);
                                        MaterialLibraryActivity.this.overtimeHandler.removeCallbacks(MaterialLibraryActivity.this.overtimeRunnable);
                                        MaterialLibraryActivity.this.index = -2;
                                        MaterialLibraryActivity.this.timeoutCount = 0;
                                    } else if (MaterialLibraryActivity.this.receiveData) {
                                    } else {
                                        MaterialLibraryActivity.this.index--;
                                        if (MaterialLibraryActivity.this.rlProgressView != null && MaterialLibraryActivity.this.tvSend != null) {
                                            MaterialLibraryActivity.this.rlProgressView.setVisibility(0);
                                        }
                                        MaterialLibraryActivity.this.sendCmdHandler.postDelayed(MaterialLibraryActivity.this.sendCmdRunnable, 70L);
                                    }
                                }
                            };
                        }
                        MaterialLibraryActivity.this.overtimeHandler.postDelayed(MaterialLibraryActivity.this.overtimeRunnable, 100L);
                    }
                };
            }
            MaterialLibraryActivity.this.sendCmdHandler.postDelayed(MaterialLibraryActivity.this.sendCmdRunnable, 10L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCmdStringList(ArrayList<Bitmap> arrayList) {
        String substring;
        this.cmdString = "";
        this.cmdStringList.clear();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("5a4a494d");
        stringBuffer.append("0100");
        stringBuffer.append("0000");
        stringBuffer.append("6c65642d30310000000000000000000000000000000000000000000000000000");
        stringBuffer.append("01");
        stringBuffer.append("01");
        stringBuffer.append("00");
        stringBuffer.append("00");
        StringBuilder sb = new StringBuilder();
        sb.append(to04Hex(getPixSetLong()).substring(2));
        sb.append(to04Hex(getPixSetLong()).substring(0, 2));
        stringBuffer.append(sb.toString());
        stringBuffer.append(to04Hex(getPixSetWidth()).substring(2) + to04Hex(getPixSetWidth()).substring(0, 2));
        stringBuffer.append("0000");
        stringBuffer.append("0000");
        stringBuffer.append("6400");
        stringBuffer.append(to02Hex(arrayList.size()));
        stringBuffer.append("00");
        stringBuffer.append("000000ff");
        stringBuffer.append("00000000");
        this.cmdString = stringBuffer.toString();
        for (int i = 0; i < arrayList.size(); i++) {
            Bitmap bitmap = arrayList.get(i);
            StringBuffer stringBuffer2 = new StringBuffer();
            for (int i2 = 0; i2 < bitmap.getHeight(); i2++) {
                for (int i3 = 0; i3 < bitmap.getWidth(); i3++) {
                    int pixel = bitmap.getPixel(i3, i2);
                    Color.alpha(pixel);
                    stringBuffer2.append(get565RgbHex(Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
                }
            }
            stringBuffer.append(stringBuffer2);
            this.cmdString = stringBuffer.toString();
        }
        int length = ((this.cmdString.length() / 2) + 199) / 200;
        for (int i4 = 0; i4 < length; i4++) {
            if (i4 == length - 1) {
                substring = this.cmdString.substring(i4 * HttpStatus.SC_BAD_REQUEST);
            } else {
                substring = this.cmdString.substring(i4 * HttpStatus.SC_BAD_REQUEST, (i4 + 1) * HttpStatus.SC_BAD_REQUEST);
            }
            this.cmdStringList.add(substring);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap scale(Bitmap bitmap, double d, double d2) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) d) / width, ((float) d2) / height);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    private String to02Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 2 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String to04Hex(int i) {
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 4 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString;
    }

    private String get565RgbHex(int i, int i2, int i3) {
        String binaryString = Integer.toBinaryString(i);
        int length = binaryString.length();
        for (int i4 = 0; i4 < 8 - length; i4++) {
            binaryString = NetResult.CODE_OK + binaryString;
        }
        String substring = binaryString.substring(0, 5);
        String binaryString2 = Integer.toBinaryString(i2);
        int length2 = binaryString2.length();
        for (int i5 = 0; i5 < 8 - length2; i5++) {
            binaryString2 = NetResult.CODE_OK + binaryString2;
        }
        String substring2 = binaryString2.substring(0, 6);
        String binaryString3 = Integer.toBinaryString(i3);
        int length3 = binaryString3.length();
        for (int i6 = 0; i6 < 8 - length3; i6++) {
            binaryString3 = NetResult.CODE_OK + binaryString3;
        }
        String hexString = Integer.toHexString(Integer.parseInt(substring + substring2 + binaryString3.substring(0, 5), 2));
        int length4 = hexString.length();
        for (int i7 = 0; i7 < 4 - length4; i7++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        return hexString.substring(2) + hexString.substring(0, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getNumb(int i) {
        int[] iArr = new int[4];
        String hexString = Integer.toHexString(i);
        int length = hexString.length();
        for (int i2 = 0; i2 < 8 - length; i2++) {
            hexString = NetResult.CODE_OK + hexString;
        }
        String substring = hexString.substring(4);
        String substring2 = hexString.substring(0, 4);
        iArr[0] = Integer.parseInt(substring.substring(2), 16);
        iArr[1] = Integer.parseInt(substring.substring(0, 2), 16);
        iArr[2] = Integer.parseInt(substring2.substring(2), 16);
        iArr[3] = Integer.parseInt(substring2.substring(0, 2), 16);
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] HexStringToIntArray(String str) {
        int length = str.length() / 2;
        String[] strArr = new String[length];
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int i3 = i2 + 2;
            strArr[i] = str.substring(i2, i3);
            iArr[i] = Integer.parseInt(str.substring(i2, i3), 16);
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPixSetLong() {
        String perference = SharePersistent.getPerference(getApplicationContext(), "DMX02-PIX-StringbtnSetPixLong");
        if (perference == null || perference.length() <= 0) {
            SharePersistent.savePerference(getApplicationContext(), "DMX02-PIX-StringbtnSetPixLong", "16");
            perference = "16";
        }
        return Integer.parseInt(perference);
    }

    private int getPixSetWidth() {
        String perference = SharePersistent.getPerference(getApplicationContext(), "DMX02-PIX-StringbtnSetPixWidth");
        if (perference == null || perference.length() <= 0) {
            SharePersistent.savePerference(getApplicationContext(), "DMX02-PIX-StringbtnSetPixWidth", "16");
            perference = "16";
        }
        return Integer.parseInt(perference);
    }

    public void getURL() {
        String str;
        this.contentList.clear();
        this.imageVisitUrlList.clear();
        this.imageAdapter.notifyDataSetChanged();
        this.params = new HashMap();
        if (getResources().getString(R.string.home).equalsIgnoreCase("首页")) {
            str = "cn";
        } else {
            str = getResources().getString(R.string.home).equalsIgnoreCase("Home") ? "en" : "dmx02_other";
        }
        this.params.put(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE, str);
        this.params.put("pageNum", String.valueOf(1));
        this.params.put("pageSize", "1000");
        this.params.put("type", materialType);
        HttpUtil.getInstance().getSourceData(false, this, Constant.queryMaterial, this.params, new HttpUtil.HttpCallBack() { // from class: com.home.activity.other.MaterialLibraryActivity.8
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str2) {
                try {
                    MaterialLibraryActivity.this.jsonObject = new JSONObject(str2);
                    MaterialLibraryActivity materialLibraryActivity = MaterialLibraryActivity.this;
                    materialLibraryActivity.jsonArray = materialLibraryActivity.jsonObject.getJSONObject(Utils.RESPONSE_CONTENT).getJSONArray("list");
                    int length = MaterialLibraryActivity.this.jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jSONObject = MaterialLibraryActivity.this.jsonArray.getJSONObject(i);
                        MaterialLibraryActivity.this.contentList.add(jSONObject.getString(Utils.RESPONSE_CONTENT));
                        MaterialLibraryActivity.this.imageVisitUrlList.add(jSONObject.getString("imageVisitUrl"));
                    }
                    MaterialLibraryActivity.this.imageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str2) {
                Toast.makeText(MaterialLibraryActivity.this.getApplicationContext(), MaterialLibraryActivity.this.getString(R.string.request_failed), 0).show();
            }
        });
    }

    public void updateRgbText(int[] iArr) {
        if (MainActivity_DMX02.getMainActivity() != null) {
            MainActivity_DMX02.getMainActivity().setRgb(iArr[0], iArr[1], iArr[2], false, false, false, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int selectedPosition = 0;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public ImageAdapter(Context context) {
            this.mContext = context;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return MaterialLibraryActivity.this.contentList.size();
        }

        public void clearSelection(int i) {
            this.selectedPosition = i;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.dmx02_animation_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.itemImg = (GifImageView) view.findViewById(R.id.gifImv);
                viewHolder.name = (TextView) view.findViewById(R.id.tv_name);
                TextView textView = viewHolder.name;
                textView.setTag("name" + (i + 1));
                viewHolder.llGifIVBoder = (LinearLayout) view.findViewById(R.id.llGifIVBoder);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if (MaterialLibraryActivity.this.imageVisitUrlList.size() > 0) {
                Glide.with(this.mContext).load((String) MaterialLibraryActivity.this.imageVisitUrlList.get(i)).into(viewHolder.itemImg);
            }
            if (this.selectedPosition == i) {
                viewHolder.name.setTextColor(MaterialLibraryActivity.this.getResources().getColor(R.color.dmx02_effect_color_check));
                viewHolder.llGifIVBoder.setVisibility(0);
            } else {
                viewHolder.name.setTextColor(MaterialLibraryActivity.this.getResources().getColor(R.color.dmx02_effect_color_normal));
                viewHolder.llGifIVBoder.setVisibility(8);
            }
            if (MaterialLibraryActivity.this.contentList.size() > 0) {
                TextView textView2 = viewHolder.name;
                textView2.setText("" + ((String) MaterialLibraryActivity.this.contentList.get(i)));
            }
            return view;
        }

        /* loaded from: classes.dex */
        class ViewHolder {
            GifImageView itemImg;
            LinearLayout llGifIVBoder;
            TextView name;

            ViewHolder() {
            }
        }
    }
}
