package com.home.fragment.help;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.utils.Utils;
import com.ledlamp.R;
import com.video.VideoDetailActivity;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class HelpVideoFragment extends Fragment {
    private static final String TAG = "HelpVideoFragment";
    public static String[] appId;
    public static String[] con;
    public static String[] imageVisitUrl;
    public static HashMap<String, String> k_v;
    public static String[] videoId;
    public static String[] videoVisitUrl;
    GridViewWithHeaderAndFooter gv_video1;
    JSONArray jsonArray;
    JSONObject jsonObject;
    View mView;
    Map<String, Bitmap> map = new HashMap();
    MyVideoAdaptr myVideoAdaptr;
    public static List listUrl = new ArrayList();
    public static List coverImageUrl = new ArrayList();
    public static List<String> list = new ArrayList();

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video, viewGroup, false);
        this.mView = inflate;
        this.gv_video1 = (GridViewWithHeaderAndFooter) inflate.findViewById(R.id.gv_video1);
        MyVideoAdaptr myVideoAdaptr = new MyVideoAdaptr();
        this.myVideoAdaptr = myVideoAdaptr;
        this.gv_video1.setAdapter((ListAdapter) myVideoAdaptr);
        List<String> list2 = list;
        if (list2 != null) {
            list2.clear();
            listUrl.clear();
        }
        geturl();
        return this.mView;
    }

    public void geturl() {
        HttpUtil.getInstance().getSourceData(false, getActivity(), Constant.allVideo, new HashMap(), new HttpUtil.HttpCallBack() { // from class: com.home.fragment.help.HelpVideoFragment.1
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                try {
                    Log.e(HelpVideoFragment.TAG, "result = " + str);
                    HelpVideoFragment.this.jsonObject = new JSONObject(str);
                    HelpVideoFragment helpVideoFragment = HelpVideoFragment.this;
                    helpVideoFragment.jsonArray = helpVideoFragment.jsonObject.getJSONArray(Utils.RESPONSE_CONTENT);
                    int length = HelpVideoFragment.this.jsonArray.length();
                    HelpVideoFragment.appId = new String[length];
                    HelpVideoFragment.con = new String[length];
                    HelpVideoFragment.videoId = new String[length];
                    HelpVideoFragment.videoVisitUrl = new String[length];
                    HelpVideoFragment.imageVisitUrl = new String[length];
                    for (int i = 0; i < length; i++) {
                        JSONObject jSONObject = HelpVideoFragment.this.jsonArray.getJSONObject(i);
                        HelpVideoFragment.appId[i] = jSONObject.getString("appId");
                        HelpVideoFragment.con[i] = jSONObject.getString("describe");
                        HelpVideoFragment.videoId[i] = jSONObject.getString("videoId");
                        HelpVideoFragment.videoVisitUrl[i] = jSONObject.getString("videoVisitUrl");
                        HelpVideoFragment.imageVisitUrl[i] = jSONObject.getString("imageVisitUrl");
                        HelpVideoFragment.list.add(HelpVideoFragment.con[i]);
                        HelpVideoFragment.listUrl.add(HelpVideoFragment.videoVisitUrl[i]);
                        HelpVideoFragment.coverImageUrl.add(HelpVideoFragment.imageVisitUrl[i]);
                    }
                    HelpVideoFragment.this.myVideoAdaptr.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                if (HelpVideoFragment.this.getActivity() != null) {
                    Toast.makeText(HelpVideoFragment.this.getActivity(), HelpVideoFragment.this.getActivity().getResources().getString(R.string.request_failed), 0).show();
                }
            }
        });
    }

    /* loaded from: classes.dex */
    class TextHolder {
        ImageView app_video;
        TextView title_tv;

        TextHolder() {
        }
    }

    /* loaded from: classes.dex */
    class MyVideoAdaptr extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MyVideoAdaptr() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return HelpVideoFragment.list.size();
        }

        @Override // android.widget.Adapter
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View view2;
            TextHolder textHolder;
            if (view == null) {
                textHolder = new TextHolder();
                view2 = LayoutInflater.from(HelpVideoFragment.this.getActivity()).inflate(R.layout.appvideo, viewGroup, false);
                double width = viewGroup.getWidth();
                Double.isNaN(width);
                double width2 = viewGroup.getWidth();
                Double.isNaN(width2);
                view2.setLayoutParams(new AbsListView.LayoutParams((int) (width * 0.5d), (int) ((width2 * 0.5d) + 200.0d)));
                textHolder.title_tv = (TextView) view2.findViewById(R.id.title_tv);
                textHolder.app_video = (ImageView) view2.findViewById(R.id.app_video);
                view2.setTag(textHolder);
            } else {
                view2 = view;
                textHolder = (TextHolder) view.getTag();
            }
            textHolder.title_tv.setText(HelpVideoFragment.list.get(i));
            if (HelpVideoFragment.coverImageUrl.get(i).equals("")) {
                HelpVideoFragment.loadCover(textHolder.app_video, (String) HelpVideoFragment.listUrl.get(i), HelpVideoFragment.this.getContext());
            } else {
                HelpVideoFragment.loadCover(textHolder.app_video, (String) HelpVideoFragment.coverImageUrl.get(i), HelpVideoFragment.this.getContext());
            }
            textHolder.app_video.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.help.HelpVideoFragment.MyVideoAdaptr.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    Intent intent = new Intent(HelpVideoFragment.this.getActivity(), VideoDetailActivity.class);
                    intent.putExtra("videoPath", HelpVideoFragment.videoVisitUrl[i]);
                    HelpVideoFragment.this.startActivity(intent);
                }
            });
            return view2;
        }
    }

    public static void loadCover(ImageView imageView, String str, Context context) {
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().frame(1000000L).placeholder(R.drawable.default_common).error(R.drawable.default_common)).load(str).into(imageView);
    }

    public Bitmap getBitmap(String str) {
        FFmpegMediaMetadataRetriever fFmpegMediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
        try {
            try {
                fFmpegMediaMetadataRetriever.setDataSource(str);
                return fFmpegMediaMetadataRetriever.getFrameAtTime();
            } catch (Exception e) {
                e.printStackTrace();
                fFmpegMediaMetadataRetriever.release();
                return null;
            }
        } finally {
            fFmpegMediaMetadataRetriever.release();
        }
    }
}
