package com.video;

import java.util.List;

/* loaded from: classes.dex */
public class VideoReplyBean {
    private String author;
    private String content;
    private String headImage;
    private int id;
    private String imageVisitUrl;
    private List<VideoReplyBean> list;
    private String publishTime;
    private String replyId;
    private String replyType;
    private String target;
    private String videoVisitUrl;

    public void setVideoVisitUrl(String str) {
        this.videoVisitUrl = str;
    }

    public void setImageVisitUrl(String str) {
        this.imageVisitUrl = str;
    }

    public String getVideoVisitUrl() {
        return this.videoVisitUrl;
    }

    public void setAuthor(String str) {
        this.author = str;
    }

    public void setPublishTime(String str) {
        this.publishTime = str;
    }

    public String getPublishTime() {
        return this.publishTime;
    }

    public String getImageVisitUrl() {
        return this.imageVisitUrl;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }

    public List<VideoReplyBean> getList() {
        return this.list;
    }

    public String getContent() {
        return this.content;
    }

    public String getReplyId() {
        return this.replyId;
    }

    public String getReplyType() {
        return this.replyType;
    }

    public String getTarget() {
        return this.target;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setList(List<VideoReplyBean> list) {
        this.list = list;
    }

    public void setReplyId(String str) {
        this.replyId = str;
    }

    public void setReplyType(String str) {
        this.replyType = str;
    }

    public void setTarget(String str) {
        this.target = str;
    }

    public void setHeadImage(String str) {
        this.headImage = str;
    }

    public String getHeadImage() {
        return this.headImage;
    }
}
