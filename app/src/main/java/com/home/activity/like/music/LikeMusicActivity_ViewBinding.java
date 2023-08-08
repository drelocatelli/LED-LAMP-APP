package com.home.activity.like.music;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class LikeMusicActivity_ViewBinding implements Unbinder {
    private LikeMusicActivity target;

    public LikeMusicActivity_ViewBinding(LikeMusicActivity likeMusicActivity) {
        this(likeMusicActivity, likeMusicActivity.getWindow().getDecorView());
    }

    public LikeMusicActivity_ViewBinding(LikeMusicActivity likeMusicActivity, View view) {
        this.target = likeMusicActivity;
        likeMusicActivity.buttonCancell = (ImageView) Utils.findRequiredViewAsType(view, R.id.buttonCancell, "field 'buttonCancell'", ImageView.class);
        likeMusicActivity.imageViewPre = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPre, "field 'imageViewPre'", ImageView.class);
        likeMusicActivity.imageViewPlay = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlay, "field 'imageViewPlay'", ImageView.class);
        likeMusicActivity.imageViewNext = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewNext, "field 'imageViewNext'", ImageView.class);
        likeMusicActivity.buttonBreathe = (Button) Utils.findRequiredViewAsType(view, R.id.buttonBreathe, "field 'buttonBreathe'", Button.class);
        likeMusicActivity.buttonFlash = (Button) Utils.findRequiredViewAsType(view, R.id.buttonFlash, "field 'buttonFlash'", Button.class);
        likeMusicActivity.buttonStrobe = (Button) Utils.findRequiredViewAsType(view, R.id.buttonStrobe, "field 'buttonStrobe'", Button.class);
        likeMusicActivity.tvCurrentTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvCurrentTime, "field 'tvCurrentTime'", TextView.class);
        likeMusicActivity.tvTotalTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTotalTime, "field 'tvTotalTime'", TextView.class);
        likeMusicActivity.llBottom = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.llBottom, "field 'llBottom'", LinearLayout.class);
        likeMusicActivity.seekBarMusic = (SeekBar) Utils.findRequiredViewAsType(view, R.id.seekBarMusic, "field 'seekBarMusic'", SeekBar.class);
        likeMusicActivity.imageViewPlayType = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewPlayType, "field 'imageViewPlayType'", ImageView.class);
        likeMusicActivity.buttonMusicLib = (Button) Utils.findRequiredViewAsType(view, R.id.buttonMusicLib, "field 'buttonMusicLib'", Button.class);
        likeMusicActivity.listViewMuiscsList = (ListView) Utils.findRequiredViewAsType(view, R.id.listViewMuiscsList, "field 'listViewMuiscsList'", ListView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        LikeMusicActivity likeMusicActivity = this.target;
        if (likeMusicActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        likeMusicActivity.buttonCancell = null;
        likeMusicActivity.imageViewPre = null;
        likeMusicActivity.imageViewPlay = null;
        likeMusicActivity.imageViewNext = null;
        likeMusicActivity.buttonBreathe = null;
        likeMusicActivity.buttonFlash = null;
        likeMusicActivity.buttonStrobe = null;
        likeMusicActivity.tvCurrentTime = null;
        likeMusicActivity.tvTotalTime = null;
        likeMusicActivity.llBottom = null;
        likeMusicActivity.seekBarMusic = null;
        likeMusicActivity.imageViewPlayType = null;
        likeMusicActivity.buttonMusicLib = null;
        likeMusicActivity.listViewMuiscsList = null;
    }
}
