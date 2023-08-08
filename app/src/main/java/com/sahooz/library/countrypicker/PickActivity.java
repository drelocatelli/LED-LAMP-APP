package com.sahooz.library.countrypicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sahooz.library.countrypicker.PickActivity;
import com.sahooz.library.countrypicker.PyAdapter;
import com.sahooz.library.countrypicker.SideBar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class PickActivity extends AppCompatActivity {
    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private ArrayList<Country> allCountries = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_pick);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_pick);
        SideBar sideBar = (SideBar) findViewById(R.id.side);
        final TextView textView = (TextView) findViewById(R.id.tv_letter);
        this.allCountries.clear();
        this.allCountries.addAll(Country.getAll());
        this.selectedCountries.clear();
        this.selectedCountries.addAll(this.allCountries);
        final CAdapter cAdapter = new CAdapter(this.selectedCountries);
        recyclerView.setAdapter(cAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        ((EditText) findViewById(R.id.et_search)).addTextChangedListener(new TextWatcher() { // from class: com.sahooz.library.countrypicker.PickActivity.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                String obj = editable.toString();
                PickActivity.this.selectedCountries.clear();
                Iterator it = PickActivity.this.allCountries.iterator();
                while (it.hasNext()) {
                    Country country = (Country) it.next();
                    if (country.name.toLowerCase().contains(obj.toLowerCase())) {
                        PickActivity.this.selectedCountries.add(country);
                    }
                }
                cAdapter.update(PickActivity.this.selectedCountries);
            }
        });
        sideBar.addIndex("#", sideBar.indexes.size());
        sideBar.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() { // from class: com.sahooz.library.countrypicker.PickActivity.2
            @Override // com.sahooz.library.countrypicker.SideBar.OnLetterChangeListener
            public void onLetterChange(String str) {
                textView.setVisibility(0);
                textView.setText(str);
                int letterPosition = cAdapter.getLetterPosition(str);
                if (letterPosition != -1) {
                    linearLayoutManager.scrollToPositionWithOffset(letterPosition, 0);
                }
            }

            @Override // com.sahooz.library.countrypicker.SideBar.OnLetterChangeListener
            public void onReset() {
                textView.setVisibility(8);
            }
        });
        ((TextView) findViewById(R.id.tv_back)).setOnClickListener(new View.OnClickListener() { // from class: com.sahooz.library.countrypicker.PickActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PickActivity.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class CAdapter extends PyAdapter<RecyclerView.ViewHolder> {
        public CAdapter(List<? extends PyEntity> list) {
            super(list, '#');
        }

        @Override // com.sahooz.library.countrypicker.PyAdapter
        public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup viewGroup, int i) {
            return new LetterHolder(PickActivity.this.getLayoutInflater().inflate(R.layout.item_letter, viewGroup, false));
        }

        @Override // com.sahooz.library.countrypicker.PyAdapter
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup viewGroup, int i) {
            return new VH(PickActivity.this.getLayoutInflater().inflate(R.layout.item_country_large_padding, viewGroup, false));
        }

        @Override // com.sahooz.library.countrypicker.PyAdapter
        public void onBindHolder(RecyclerView.ViewHolder viewHolder, PyEntity pyEntity, int i) {
            VH vh = (VH) viewHolder;
            final Country country = (Country) pyEntity;
            vh.ivFlag.setImageResource(country.flag);
            vh.tvName.setText(country.name);
            TextView textView = vh.tvCode;
            textView.setText("+" + country.code);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.sahooz.library.countrypicker.PickActivity$CAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PickActivity.CAdapter.this.m64x2052e737(country, view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$onBindHolder$0$com-sahooz-library-countrypicker-PickActivity$CAdapter  reason: not valid java name */
        public /* synthetic */ void m64x2052e737(Country country, View view) {
            Intent intent = new Intent();
            intent.putExtra("country", country.toJson());
            PickActivity.this.setResult(-1, intent);
            PickActivity.this.finish();
        }

        @Override // com.sahooz.library.countrypicker.PyAdapter
        public void onBindLetterHolder(RecyclerView.ViewHolder viewHolder, PyAdapter.LetterEntity letterEntity, int i) {
            ((LetterHolder) viewHolder).textView.setText(letterEntity.letter.toUpperCase());
        }
    }
}
