package com.forum.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class CommentBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String author;
    private String content;
    private String headImage;
    private long id;
    private String imageVisitUrl;
    private String publishTime;
    private String title;

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(String str) {
        this.publishTime = str;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String str) {
        this.author = str;
    }

    public String getImageVisitUrl() {
        return this.imageVisitUrl;
    }

    public void setImageVisitUrl(String str) {
        this.imageVisitUrl = str;
    }

    public void setHeadImage(String str) {
        this.headImage = str;
    }

    public String getHeadImage() {
        return this.headImage;
    }
}
