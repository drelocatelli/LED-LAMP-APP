package com.home.constant;

import android.os.Environment;

/* loaded from: classes.dex */
public class Constant {
    public static final String APP_FIRST_OPEN = "app_first_open";
    public static final String AUTO_OR_MANUAL = "AUTO_OR_MANUAL";
    public static final String Activity = "Activity";
    public static final String AgreeAPPPolicy = "AgreeAPPPolicy";
    public static final String CHIP_TYPE = "CHIP_TYPE";
    public static final String CHIP_TYPE_KEY = "CHIP_TYPE_KEY";
    public static final String CUSTOM_DIY_APPKEY = "CUSTOM_DIY_APPKEY";
    public static final String CurrentQueryActivity = "CurrentQueryActivity";
    public static final String DMX02GraffitFragment = "DMX02GraffitFragment";
    public static final String DMX02ModeFragment = "DMX02ModeFragment";
    public static final String DMX02TextFragment = "DMX02TextFragment";
    public static final String IMAGE_VALUE = "IMAGE_VALUE";
    public static final String LIGHT_RGB_DIY = "LIGHT_RGB_DIY";
    public static final String MODLE_TYPE = "MODLE_TYPE";
    public static final String MODLE_VALUE = "MODLE_VALUE";
    public static final String ManualModeConnectDevice = "ManualModeConnectDevice";
    public static final String MaterialLibraryActivity = "MaterialLibraryActivity";
    public static final String NODATA_CODE = "000001";
    public static final String PHOTO_IMAGE_PATH;
    public static final String PIX_SPACERCODE_KEY = "PIX_SPACERCODE_KEY";
    public static final String PIX_SPACERCODE_TYPE = "PIX_SPACERCODE_TYPE";
    public static final String PIX_STARTCODE_KEY = "PIX_STARTCODE_KEY";
    public static final String PIX_STARTCODE_TYPE = "PIX_STARTCODE_TYPE";
    public static final String PasswordSet = "PasswordSet";
    public static final String PricavyPolicy = "PricavyPolicy";
    public static final String RefreshBluetooth = "RefreshBluetooth";
    public static final String SUCCESS_CODE = "000000";
    public static final String ServiceAgreement = "ServiceAgreement";
    public static final String ShowLeftMenu = "ShowLeftMenu";
    public static final String SmartTimeNowSet = "SmartTimeNowSet";
    public static final String StartLeScan = "StartLeScan";
    public static final String StopLeScan = "StopLeScan";
    public static final String TimingQueryActivity = "TimingQueryActivity";
    public static final String UPLOAD_PHOTO_PATH;
    public static final String USER_CHECK_MODE = "user_checkMode";
    public static final String USER_HEADIMAGE = "user_headImage";
    public static final String USER_INFO = "user_info";
    public static final String USER_NAME = "user_name";
    public static final String USER_TOKEN = "user_token";
    public static final String UpdateNewFindDevice = "UpdateNewFindDevice";
    public static final String allVideo = "http://120.78.240.163/xpy-server/video/queryAllVideo";
    public static final String browseVideo = "http://120.78.240.163/xpy-server/video/browseVideo";
    public static final String deleteAccount = "http://120.78.240.163/xpy-server/user/logoutByApp";
    public static final String deleteMessageByApp = "http://120.78.240.163/xpy-server/instantMessage/deleteMessageByApp";
    public static final String deleteShortVideoByApp = "http://120.78.240.163/xpy-server/shortVideo/deleteShortVideoByApp";
    public static final String devDomainPort = "http://192.168.0.139:8080/";
    public static final String domainPort = "http://120.78.240.163/";
    public static final String getPrdDomainPort3 = "http://192.168.0.190:8080/";
    public static final String getVerifyCodeByApp = "http://120.78.240.163/xpy-server/user/getVerifyCodeByApp";
    public static final String loginByApp = "http://120.78.240.163/xpy-server/user/loginByApp";
    public static final String prdDomainPort = "http://120.79.182.171/";
    public static final String prdDomainPort2 = "http://120.78.240.163/";
    public static final String publishCommentByApp = "http://120.78.240.163/xpy-server/comment/publishCommentByApp";
    public static final String publishReplyByApp = "http://120.78.240.163/xpy-server/comment/publishReplyByApp";
    public static final String publishShortVideoReply = "http://120.78.240.163/xpy-server/shortVideo/publishShortVideoReply";
    public static final String queryCommentByApp = "http://120.78.240.163/xpy-server/comment/queryCommentByApp";
    public static final String queryFileDownload = "http://120.78.240.163/xpy-server/file/queryFileDownload";
    public static final String queryInstantMsgByApp = "http://120.78.240.163/xpy-server/instantMessage/queryInstantMsgByApp";
    public static final String queryMaterial = "http://120.78.240.163/xpy-server/material/queryMaterialByApp";
    public static final String queryNewAdvert = "http://120.78.240.163/xpy-server/advert/queryNewAdvert";
    public static final String queryNewVideo = "http://120.78.240.163/xpy-server/video/queryNewVideo";
    public static final String queryReplyByApp = "http://120.78.240.163/xpy-server/comment/queryReplyByApp";
    public static final String queryShortVideoByApp = "http://120.78.240.163/xpy-server/shortVideo/queryShortVideoByApp";
    public static final String queryShortVideoReplyByApp = "http://120.78.240.163/xpy-server/shortVideo/queryShortVideoReplyByApp";
    public static final String registerByApp = "http://120.78.240.163/xpy-server/user/registerByApp";
    public static final String resetPswdByApp = "http://120.78.240.163/xpy-server/user/resetPswdByApp";
    public static final String sendMessageByApp = "http://120.78.240.163/xpy-server/instantMessage/sendMessageByApp";
    public static final String supportShortVideo = "http://120.78.240.163/xpy-server/shortVideo/supportShortVideo";
    public static final String updateHeadImageByApp = "http://120.78.240.163/xpy-server/user/updateHeadImageByApp";
    public static final String updatePswdByApp = "http://120.78.240.163/xpy-server/user/updatePswdByApp";
    public static final String uploadShortVideo = "http://120.78.240.163/xpy-server/shortVideo/uploadShortVideo";

    static {
        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/LED BLE/";
        PHOTO_IMAGE_PATH = str;
        UPLOAD_PHOTO_PATH = str + "upload/";
    }
}
