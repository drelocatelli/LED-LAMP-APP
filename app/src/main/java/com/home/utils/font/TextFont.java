package com.home.utils.font;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class TextFont {
    private List<byte[]> fontList;
    private int fontSize;
    private String fontType;
    private String text;

    public int getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(int i) {
        this.fontSize = i;
    }

    public String getFontType() {
        String str = this.fontType;
        return str == null ? "" : str;
    }

    public void setFontType(String str) {
        this.fontType = str;
    }

    public String getText() {
        String str = this.text;
        return str == null ? "" : str;
    }

    public void setText(String str) {
        this.text = str;
    }

    public List<byte[]> getFontList() {
        List<byte[]> list = this.fontList;
        return list == null ? new ArrayList() : list;
    }

    public void setFontList(List<byte[]> list) {
        this.fontList = list;
    }
}
