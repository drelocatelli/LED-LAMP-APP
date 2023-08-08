package com.itheima.wheelpicker.widgets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.itheima.wheelpicker.WheelPicker;
import com.itheima.wheelpicker.model.City;
import com.itheima.wheelpicker.model.Province;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class WheelAreaPicker extends LinearLayout implements IWheelAreaPicker {
    private static final float ITEM_TEXT_SIZE = 18.0f;
    private static final int PROVINCE_INITIAL_INDEX = 0;
    private static final String SELECTED_ITEM_COLOR = "#353535";
    private AssetManager mAssetManager;
    private List<City> mCityList;
    private List<String> mCityName;
    private Context mContext;
    private LinearLayout.LayoutParams mLayoutParams;
    private List<Province> mProvinceList;
    private List<String> mProvinceName;
    private WheelPicker mWPArea;
    private WheelPicker mWPCity;
    private WheelPicker mWPProvince;

    public WheelAreaPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initLayoutParams();
        initView(context);
        this.mProvinceList = getJsonDataFromAssets(this.mAssetManager);
        obtainProvinceData();
        addListenerToWheelPicker();
    }

    private List<Province> getJsonDataFromAssets(AssetManager assetManager) {
        Exception e;
        List<Province> list;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(assetManager.open("RegionJsonData.dat"));
            list = (List) objectInputStream.readObject();
            try {
                objectInputStream.close();
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return list;
            }
        } catch (Exception e3) {
            e = e3;
            list = null;
        }
        return list;
    }

    private void initLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        this.mLayoutParams = layoutParams;
        layoutParams.setMargins(5, 5, 5, 5);
        this.mLayoutParams.width = 0;
    }

    private void initView(Context context) {
        setOrientation(0);
        this.mContext = context;
        this.mAssetManager = context.getAssets();
        this.mProvinceName = new ArrayList();
        this.mCityName = new ArrayList();
        this.mWPProvince = new WheelPicker(context);
        this.mWPCity = new WheelPicker(context);
        this.mWPArea = new WheelPicker(context);
        initWheelPicker(this.mWPProvince, 1.0f);
        initWheelPicker(this.mWPCity, 1.5f);
        initWheelPicker(this.mWPArea, 1.5f);
    }

    private void initWheelPicker(WheelPicker wheelPicker, float f) {
        this.mLayoutParams.weight = f;
        wheelPicker.setItemTextSize(dip2px(this.mContext, ITEM_TEXT_SIZE));
        wheelPicker.setSelectedItemTextColor(Color.parseColor(SELECTED_ITEM_COLOR));
        wheelPicker.setCurved(true);
        wheelPicker.setLayoutParams(this.mLayoutParams);
        addView(wheelPicker);
    }

    private void obtainProvinceData() {
        for (Province province : this.mProvinceList) {
            this.mProvinceName.add(province.getName());
        }
        this.mWPProvince.setData(this.mProvinceName);
        setCityAndAreaData(0);
    }

    private void addListenerToWheelPicker() {
        this.mWPProvince.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.itheima.wheelpicker.widgets.WheelAreaPicker.1
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                WheelAreaPicker wheelAreaPicker = WheelAreaPicker.this;
                wheelAreaPicker.mCityList = ((Province) wheelAreaPicker.mProvinceList.get(i)).getCity();
                WheelAreaPicker.this.setCityAndAreaData(i);
            }
        });
        this.mWPCity.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.itheima.wheelpicker.widgets.WheelAreaPicker.2
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                WheelAreaPicker.this.mWPArea.setData(((City) WheelAreaPicker.this.mCityList.get(i)).getArea());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCityAndAreaData(int i) {
        this.mCityList = this.mProvinceList.get(i).getCity();
        this.mCityName.clear();
        for (City city : this.mCityList) {
            this.mCityName.add(city.getName());
        }
        this.mWPCity.setData(this.mCityName);
        this.mWPCity.setSelectedItemPosition(0);
        this.mWPArea.setData(this.mCityList.get(0).getArea());
        this.mWPArea.setSelectedItemPosition(0);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelAreaPicker
    public String getProvince() {
        return this.mProvinceList.get(this.mWPProvince.getCurrentItemPosition()).getName();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelAreaPicker
    public String getCity() {
        return this.mCityList.get(this.mWPCity.getCurrentItemPosition()).getName();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelAreaPicker
    public String getArea() {
        return this.mCityList.get(this.mWPCity.getCurrentItemPosition()).getArea().get(this.mWPArea.getCurrentItemPosition());
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelAreaPicker
    public void hideArea() {
        removeViewAt(2);
    }

    private int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
