package com.sahooz.library.countrypicker;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Country implements PyEntity {
    private static final String TAG = "Country";
    private static ArrayList<Country> countries = new ArrayList<>();
    public int code;
    public int flag;
    public String locale;
    public String name;
    public String pinyin;

    public Country(int i, String str, String str2, String str3, int i2) {
        this.code = i;
        this.name = str;
        this.flag = i2;
        this.locale = str3;
        this.pinyin = str2;
    }

    public String toString() {
        return "Country{code='" + this.code + "'flag='" + this.flag + "', name='" + this.name + "'}";
    }

    public static ArrayList<Country> getAll() {
        return new ArrayList<>(countries);
    }

    public static Country fromJson(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new Country(jSONObject.optInt("code"), jSONObject.optString("name"), jSONObject.optString("pinyin"), jSONObject.optString("locale"), jSONObject.optInt("flag"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson() {
        return "{\"name\":\"" + this.name + "\", \"code\":" + this.code + ", \"flag\":" + this.flag + ", \"pinyin\":" + this.pinyin + ",\"locale\":\"" + this.locale + "\"}";
    }

    public static void load(Context context, Language language) throws IOException, JSONException {
        int i;
        countries = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("code.json")));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            sb.append(readLine);
        }
        bufferedReader.close();
        JSONArray jSONArray = new JSONArray(sb.toString());
        String str = language.key;
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i2);
            String string = jSONObject.getString("locale");
            if (TextUtils.isEmpty(string)) {
                i = 0;
            } else {
                Resources resources = context.getResources();
                i = resources.getIdentifier("flag_" + string.toLowerCase(), "drawable", context.getPackageName());
            }
            String string2 = jSONObject.getString(str);
            countries.add(new Country(jSONObject.getInt("code"), string2, language == Language.ENGLISH ? string2 : jSONObject.getString("pinyin"), string, i));
        }
    }

    public static void destroy() {
        countries.clear();
    }

    public int hashCode() {
        return this.code;
    }

    @Override // com.sahooz.library.countrypicker.PyEntity
    public String getPinyin() {
        return this.pinyin;
    }
}
