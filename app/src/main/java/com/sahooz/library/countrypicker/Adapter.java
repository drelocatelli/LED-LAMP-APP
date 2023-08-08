package com.sahooz.library.countrypicker;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Adapter extends RecyclerView.Adapter<VH> {
    private final Context context;
    private final LayoutInflater inflater;
    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private PickCountryCallback callback = null;
    private int itemHeight = -1;

    public Adapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setSelectedCountries(ArrayList<Country> arrayList) {
        this.selectedCountries = arrayList;
        notifyDataSetChanged();
    }

    public void setCallback(PickCountryCallback pickCountryCallback) {
        this.callback = pickCountryCallback;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VH(this.inflater.inflate(R.layout.item_country, viewGroup, false));
    }

    public void setItemHeight(float f) {
        this.itemHeight = (int) TypedValue.applyDimension(2, f, this.context.getResources().getDisplayMetrics());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(VH vh, int i) {
        final Country country = this.selectedCountries.get(i);
        vh.ivFlag.setImageResource(country.flag);
        vh.tvName.setText(country.name);
        TextView textView = vh.tvCode;
        textView.setText("+" + country.code);
        if (this.itemHeight != -1) {
            ViewGroup.LayoutParams layoutParams = vh.itemView.getLayoutParams();
            layoutParams.height = this.itemHeight;
            vh.itemView.setLayoutParams(layoutParams);
        }
        vh.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.sahooz.library.countrypicker.Adapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Adapter.this.m62x584411c1(country, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-sahooz-library-countrypicker-Adapter  reason: not valid java name */
    public /* synthetic */ void m62x584411c1(Country country, View view) {
        PickCountryCallback pickCountryCallback = this.callback;
        if (pickCountryCallback != null) {
            pickCountryCallback.onPick(country);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.selectedCountries.size();
    }
}
