package com.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.common.uitl.DrawTool;
import com.common.view.DragGridView;
import com.home.bean.SceneBean;
import com.home.utils.BitmapTool;
import com.home.utils.ImageDispose;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.Collections;

/* loaded from: classes.dex */
public class SceneAdpter extends BaseAdapter implements DragGridView.DragGridBaseAdapter {
    private boolean isEdit;
    private Context mContext;
    public int mHidePosition = -1;
    private ArrayList<SceneBean> mSceneBeans;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    /* loaded from: classes.dex */
    public interface OnDeleteButtonClickListener {
        void onclick(SceneBean sceneBean);
    }

    /* loaded from: classes.dex */
    public static class ViewHolder {
        ImageButton ivDelete;
        ImageView ivImg;
        TextView tvName;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public SceneAdpter(Context context, ArrayList<SceneBean> arrayList) {
        this.mContext = context;
        this.mSceneBeans = arrayList;
    }

    public void refreshLists(ArrayList<SceneBean> arrayList) {
        this.mSceneBeans = arrayList;
        notifyDataSetChanged();
    }

    public void setEdited(boolean z) {
        this.isEdit = z;
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        ArrayList<SceneBean> arrayList = this.mSceneBeans;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        ArrayList<SceneBean> arrayList = this.mSceneBeans;
        if (arrayList != null) {
            return Integer.valueOf(arrayList.size());
        }
        return 0;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        Bitmap roundBitmap;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_scene, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.ivImg = (ImageView) view.findViewById(R.id.ivImg);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.ivDelete = (ImageButton) view.findViewById(R.id.ivDelete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ArrayList<SceneBean> arrayList = this.mSceneBeans;
        if (arrayList != null) {
            final SceneBean sceneBean = arrayList.get(i);
            if (this.isEdit) {
                Animation loadAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.shake);
                viewHolder.ivDelete.setVisibility(0);
                viewHolder.ivDelete.setAnimation(loadAnimation);
                viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() { // from class: com.home.adapter.SceneAdpter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        SceneAdpter.this.onDeleteButtonClickListener.onclick(sceneBean);
                    }
                });
            } else {
                viewHolder.ivDelete.setVisibility(8);
            }
            viewHolder.tvName.setText(sceneBean.getName());
            Bitmap picFromBytes = ImageDispose.getPicFromBytes(sceneBean.getImg(), null);
            if (sceneBean.getSceneType() == 1) {
                if (picFromBytes != null) {
                    roundBitmap = DrawTool.toRoundBitmap(picFromBytes);
                } else {
                    roundBitmap = DrawTool.toRoundBitmap(BitmapTool.Drawable2Bimap(this.mContext, R.drawable.bg_default_header));
                }
                picFromBytes = roundBitmap;
                int dimension = (int) this.mContext.getResources().getDimension(R.dimen.roundimage_size_small);
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(dimension, dimension);
                marginLayoutParams.setMargins(0, 0, 0, -5);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginLayoutParams);
                layoutParams.addRule(13);
                viewHolder.ivImg.setLayoutParams(layoutParams);
            }
            viewHolder.ivImg.setImageBitmap(picFromBytes);
            if (i == this.mHidePosition) {
                view.setVisibility(4);
            } else {
                view.setVisibility(0);
            }
        }
        return view;
    }

    @Override // com.common.view.DragGridView.DragGridBaseAdapter
    public void reorderItems(int i, int i2) {
        ArrayList<SceneBean> arrayList = this.mSceneBeans;
        if (arrayList == null || i < 0) {
            return;
        }
        SceneBean sceneBean = arrayList.get(i);
        if (i < i2) {
            while (i < i2) {
                int i3 = i + 1;
                Collections.swap(this.mSceneBeans, i, i3);
                i = i3;
            }
        } else if (i > i2) {
            while (i > i2) {
                Collections.swap(this.mSceneBeans, i, i - 1);
                i--;
            }
        }
        this.mSceneBeans.set(i2, sceneBean);
    }

    @Override // com.common.view.DragGridView.DragGridBaseAdapter
    public void setHideItem(int i) {
        this.mHidePosition = i;
        notifyDataSetChanged();
    }

    @Override // com.common.view.DragGridView.DragGridBaseAdapter
    public void deleteItem(int i) {
        ArrayList<SceneBean> arrayList = this.mSceneBeans;
        if (arrayList == null || i >= arrayList.size()) {
            return;
        }
        this.mSceneBeans.remove(i);
        notifyDataSetChanged();
    }

    public void setDeleteButtonClickListener(OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }
}
