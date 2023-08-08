package com.sahooz.library.countrypicker;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CountryPickerFragment extends DialogFragment {
    private PickCountryCallback callback;
    private ArrayList<Country> allCountries = new ArrayList<>();
    private ArrayList<Country> selectedCountries = new ArrayList<>();

    public static CountryPickerFragment newInstance(PickCountryCallback pickCountryCallback) {
        CountryPickerFragment countryPickerFragment = new CountryPickerFragment();
        countryPickerFragment.callback = pickCountryCallback;
        return countryPickerFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        Window window = onCreateDialog.getWindow();
        if (window != null) {
            window.requestFeature(1);
        }
        return onCreateDialog;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_country_picker, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_country);
        this.allCountries.clear();
        this.allCountries.addAll(Country.getAll());
        this.selectedCountries.clear();
        this.selectedCountries.addAll(this.allCountries);
        final Adapter adapter = new Adapter(getContext());
        adapter.setCallback(new PickCountryCallback() { // from class: com.sahooz.library.countrypicker.CountryPickerFragment$$ExternalSyntheticLambda0
            @Override // com.sahooz.library.countrypicker.PickCountryCallback
            public final void onPick(Country country) {
                CountryPickerFragment.this.m63xd89a6593(country);
            }
        });
        adapter.setSelectedCountries(this.selectedCountries);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ((EditText) inflate.findViewById(R.id.et_search)).addTextChangedListener(new TextWatcher() { // from class: com.sahooz.library.countrypicker.CountryPickerFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                String obj = editable.toString();
                CountryPickerFragment.this.selectedCountries.clear();
                Iterator it = CountryPickerFragment.this.allCountries.iterator();
                while (it.hasNext()) {
                    Country country = (Country) it.next();
                    if (country.name.toLowerCase().contains(obj.toLowerCase())) {
                        CountryPickerFragment.this.selectedCountries.add(country);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreateView$0$com-sahooz-library-countrypicker-CountryPickerFragment  reason: not valid java name */
    public /* synthetic */ void m63xd89a6593(Country country) {
        dismiss();
        PickCountryCallback pickCountryCallback = this.callback;
        if (pickCountryCallback != null) {
            pickCountryCallback.onPick(country);
        }
    }
}
