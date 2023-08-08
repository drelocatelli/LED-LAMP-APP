package com.forum.bean;

import java.util.List;

/* loaded from: classes.dex */
public class ReplyBean {
    private String author;
    private String content;
    private String headImage;
    private int id;
    private String imageVisitUrl;
    private List<ReplyBean> list;
    private String publishTime;
    private String replyId;
    private String replyType;
    private String target;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getReplyId() {
        return this.replyId;
    }

    public void setReplyId(String str) {
        this.replyId = str;
    }

    public String getReplyType() {
        return this.replyType;
    }

    public void setReplyType(String str) {
        this.replyType = str;
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

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String str) {
        this.target = str;
    }

    public String getImageVisitUrl() {
        return this.imageVisitUrl;
    }

    public void setImageVisitUrl(String str) {
        this.imageVisitUrl = str;
    }

    public List<ReplyBean> getList() {
        return this.list;
    }

    public void setList(List<ReplyBean> list) {
        this.list = list;
    }

    public String getHeadImage() {
        return this.headImage;
    }

    public void setHeadImage(String str) {
        this.headImage = str;
    }
}
