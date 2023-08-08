package com.sahooz.library.countrypicker;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public abstract class PyAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {
    private static final String TAG = "PyAdapter";
    public static final int TYPE_LETTER = 0;
    public static final int TYPE_OTHER = 1;
    private char specialLetter;
    private WeakHashMap<View, VH> holders = new WeakHashMap<>();
    public final ArrayList<PyEntity> entityList = new ArrayList<>();
    public final HashSet<LetterEntity> letterSet = new HashSet<>();
    private OnItemClickListener listener = new OnItemClickListener() { // from class: com.sahooz.library.countrypicker.PyAdapter$$ExternalSyntheticLambda0
        @Override // com.sahooz.library.countrypicker.PyAdapter.OnItemClickListener
        public final void onItemClick(PyEntity pyEntity, int i) {
            PyAdapter.lambda$new$0(pyEntity, i);
        }
    };

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(PyEntity pyEntity, int i);
    }

    private boolean isLetter(char c) {
        return ('a' <= c && 'z' >= c) || ('A' <= c && 'Z' >= c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$0(PyEntity pyEntity, int i) {
    }

    public int getViewType(PyEntity pyEntity, int i) {
        return 1;
    }

    public void onBindHolder(VH vh, PyEntity pyEntity, int i) {
    }

    public void onBindLetterHolder(VH vh, LetterEntity letterEntity, int i) {
    }

    public abstract VH onCreateHolder(ViewGroup viewGroup, int i);

    public abstract VH onCreateLetterHolder(ViewGroup viewGroup, int i);

    public PyAdapter(List<? extends PyEntity> list, char c) {
        Objects.requireNonNull(list, "entities == null!");
        this.specialLetter = c;
        update(list);
    }

    public void update(List<? extends PyEntity> list) {
        Objects.requireNonNull(list, "entities == null!");
        this.entityList.clear();
        this.entityList.addAll(list);
        this.letterSet.clear();
        for (PyEntity pyEntity : list) {
            String pinyin = pyEntity.getPinyin();
            if (!TextUtils.isEmpty(pinyin)) {
                char charAt = pinyin.charAt(0);
                if (!isLetter(charAt)) {
                    charAt = this.specialLetter;
                }
                HashSet<LetterEntity> hashSet = this.letterSet;
                hashSet.add(new LetterEntity(charAt + ""));
            }
        }
        this.entityList.addAll(this.letterSet);
        Collections.sort(this.entityList, new Comparator() { // from class: com.sahooz.library.countrypicker.PyAdapter$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return PyAdapter.this.m65lambda$update$1$comsahoozlibrarycountrypickerPyAdapter((PyEntity) obj, (PyEntity) obj2);
            }
        });
        notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$update$1$com-sahooz-library-countrypicker-PyAdapter  reason: not valid java name */
    public /* synthetic */ int m65lambda$update$1$comsahoozlibrarycountrypickerPyAdapter(PyEntity pyEntity, PyEntity pyEntity2) {
        String lowerCase = pyEntity.getPinyin().toLowerCase();
        String lowerCase2 = pyEntity2.getPinyin().toLowerCase();
        char charAt = lowerCase.charAt(0);
        char charAt2 = lowerCase2.charAt(0);
        if (isLetter(charAt) && isLetter(charAt2)) {
            return lowerCase.compareTo(lowerCase2);
        }
        if (!isLetter(charAt) || isLetter(charAt2)) {
            if (isLetter(charAt) || !isLetter(charAt2)) {
                if (charAt == '#' && (pyEntity instanceof LetterEntity)) {
                    return -1;
                }
                if (charAt2 == '#' && (pyEntity2 instanceof LetterEntity)) {
                    return 1;
                }
                return lowerCase.compareTo(lowerCase2);
            }
            return 1;
        }
        return -1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(VH vh, int i) {
        PyEntity pyEntity = this.entityList.get(i);
        this.holders.put(vh.itemView, vh);
        vh.itemView.setOnClickListener(this);
        if (pyEntity instanceof LetterEntity) {
            onBindLetterHolder(vh, (LetterEntity) pyEntity, i);
        } else {
            onBindHolder(vh, pyEntity, i);
        }
    }

    public int getEntityPosition(PyEntity pyEntity) {
        return this.entityList.indexOf(pyEntity);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final VH onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return onCreateLetterHolder(viewGroup, i);
        }
        return onCreateHolder(viewGroup, i);
    }

    public int getLetterPosition(String str) {
        return this.entityList.indexOf(new LetterEntity(str));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        PyEntity pyEntity = this.entityList.get(i);
        if (pyEntity instanceof LetterEntity) {
            return 0;
        }
        return getViewType(pyEntity, i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.entityList.size();
    }

    public boolean isLetter(int i) {
        if (i < 0 || i >= this.entityList.size()) {
            return false;
        }
        return this.entityList.get(i) instanceof LetterEntity;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        VH vh = this.holders.get(view);
        if (vh == null) {
            Log.e(TAG, "Holder onClick event, but why holder == null?");
            return;
        }
        int adapterPosition = vh.getAdapterPosition();
        this.listener.onItemClick(this.entityList.get(adapterPosition), adapterPosition);
    }

    /* loaded from: classes.dex */
    public static final class LetterEntity implements PyEntity {
        public final String letter;

        public LetterEntity(String str) {
            this.letter = str;
        }

        @Override // com.sahooz.library.countrypicker.PyEntity
        public String getPinyin() {
            return this.letter.toLowerCase();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return this.letter.toLowerCase().equals(((LetterEntity) obj).letter.toLowerCase());
        }

        public int hashCode() {
            return this.letter.toLowerCase().hashCode();
        }
    }
}
