package com.home.activity.service.category;

import java.io.Serializable;
import java.util.List;

/* loaded from: classes.dex */
public class CategoryBean implements Serializable {
    private int code;
    private List<DataBean> data;

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> list) {
        this.data = list;
    }

    /* loaded from: classes.dex */
    public static class DataBean {
        private List<DataListBean> dataList;
        private String interfaceLink;
        private String moduleStyle;
        private String moduleTitle;
        private String moreIcon;
        private String moreLinkParam;
        private String moreLinkType;
        private String moreText;
        private String moreTextDisplay;
        private String moreUniversalNavigator;
        private String type;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getMoreIcon() {
            return this.moreIcon;
        }

        public void setMoreIcon(String str) {
            this.moreIcon = str;
        }

        public String getMoreText() {
            return this.moreText;
        }

        public void setMoreText(String str) {
            this.moreText = str;
        }

        public String getModuleStyle() {
            return this.moduleStyle;
        }

        public void setModuleStyle(String str) {
            this.moduleStyle = str;
        }

        public String getModuleTitle() {
            return this.moduleTitle;
        }

        public void setModuleTitle(String str) {
            this.moduleTitle = str;
        }

        public String getMoreLinkType() {
            return this.moreLinkType;
        }

        public void setMoreLinkType(String str) {
            this.moreLinkType = str;
        }

        public String getInterfaceLink() {
            return this.interfaceLink;
        }

        public void setInterfaceLink(String str) {
            this.interfaceLink = str;
        }

        public String getMoreLinkParam() {
            return this.moreLinkParam;
        }

        public void setMoreLinkParam(String str) {
            this.moreLinkParam = str;
        }

        public String getMoreTextDisplay() {
            return this.moreTextDisplay;
        }

        public void setMoreTextDisplay(String str) {
            this.moreTextDisplay = str;
        }

        public String getMoreUniversalNavigator() {
            return this.moreUniversalNavigator;
        }

        public void setMoreUniversalNavigator(String str) {
            this.moreUniversalNavigator = str;
        }

        public List<DataListBean> getDataList() {
            return this.dataList;
        }

        public void setDataList(List<DataListBean> list) {
            this.dataList = list;
        }

        /* loaded from: classes.dex */
        public static class DataListBean {
            private String appkey;
            private String desc;
            private String id;
            private String imgURL;
            private String linkParam;
            private String linkType;
            private String offline_time;
            private String online_time;
            private String title;
            private String type;
            private String universalNavigator;

            public DataListBean() {
            }

            public DataListBean(String str) {
                this.type = str;
            }

            public String getId() {
                return this.id;
            }

            public void setId(String str) {
                this.id = str;
            }

            public String getDesc() {
                return this.desc;
            }

            public void setDesc(String str) {
                this.desc = str;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String str) {
                this.type = str;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String str) {
                this.title = str;
            }

            public String getAppkey() {
                return this.appkey;
            }

            public void setAppkey(String str) {
                this.appkey = str;
            }

            public String getImgURL() {
                return this.imgURL;
            }

            public void setImgURL(String str) {
                this.imgURL = str;
            }

            public String getLinkType() {
                return this.linkType;
            }

            public void setLinkType(String str) {
                this.linkType = str;
            }

            public String getLinkParam() {
                return this.linkParam;
            }

            public void setLinkParam(String str) {
                this.linkParam = str;
            }

            public String getOnline_time() {
                return this.online_time;
            }

            public void setOnline_time(String str) {
                this.online_time = str;
            }

            public String getOffline_time() {
                return this.offline_time;
            }

            public void setOffline_time(String str) {
                this.offline_time = str;
            }

            public String getUniversalNavigator() {
                return this.universalNavigator;
            }

            public void setUniversalNavigator(String str) {
                this.universalNavigator = str;
            }
        }
    }
}
