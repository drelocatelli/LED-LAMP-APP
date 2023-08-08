package com.video;

import java.io.Serializable;

/* loaded from: classes.dex */
public class VideoBeanMode implements Serializable {
    private String author;
    private int commentNum;
    private boolean hasDelete;
    private boolean hasSupport;
    private String headImage;
    private String imageVisitUrl;
    private String publishTime;
    private int supportNum;
    private String title;
    private int videoId;
    private String videoVisitUrl;

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String str) {
        this.author = str;
    }

    public int getVideoId() {
        return this.videoId;
    }

    public void setVideoId(int i) {
        this.videoId = i;
    }

    public String getImageVisitUrl() {
        return this.imageVisitUrl;
    }

    public String getVideoVisitUrl() {
        return this.videoVisitUrl;
    }

    public void setImageVisitUrl(String str) {
        this.imageVisitUrl = str;
    }

    public void setVideoVisitUrl(String str) {
        this.videoVisitUrl = str;
    }

    public String getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(String str) {
        this.publishTime = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setHeadImage(String str) {
        this.headImage = str;
    }

    public String getHeadImage() {
        return this.headImage;
    }

    public int getCommentNum() {
        return this.commentNum;
    }

    public void setCommentNum(int i) {
        this.commentNum = i;
    }

    public int getSupportNum() {
        return this.supportNum;
    }

    public void setSupportNum(int i) {
        this.supportNum = i;
    }

    public boolean isHasDelete() {
        return this.hasDelete;
    }

    public void setHasDelete(boolean z) {
        this.hasDelete = z;
    }

    public boolean isHasSupport() {
        return this.hasSupport;
    }

    public void setHasSupport(boolean z) {
        this.hasSupport = z;
    }
}
