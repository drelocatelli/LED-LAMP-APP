package com.home.http;

import android.app.Activity;
import android.util.Log;
import com.forum.login.DialogUtil;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import org.apache.http.HttpHost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

/* loaded from: classes.dex */
public class HttpUtil {
    private static HttpUtil httpUtil;
    private String result = "";
    private String message = "";

    /* loaded from: classes.dex */
    public interface HttpCallBack {
        void onException(String str);

        void onSuccess(String str);
    }

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            synchronized (HttpUtil.class) {
                if (httpUtil == null) {
                    httpUtil = new HttpUtil();
                }
            }
        }
        return httpUtil;
    }

    public void uploadFile(final boolean z, final Activity activity, final String str, final Map<String, String> map, final Map<String, File> map2, final HttpCallBack httpCallBack) {
        if (z) {
            DialogUtil.showProgress(activity);
        }
        Log.e("--", "params==" + map + "files" + map2);
        new Thread(new Runnable() { // from class: com.home.http.HttpUtil.1
            @Override // java.lang.Runnable
            public void run() {
                String uuid = UUID.randomUUID().toString();
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
                    httpURLConnection.setRequestProperty("connection", "keep-alive");
                    httpURLConnection.setRequestProperty("Charsert", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + uuid);
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry entry : map.entrySet()) {
                        sb.append("--");
                        sb.append(uuid);
                        sb.append("\r\n");
                        sb.append("Content-Disposition: form-data; name=\"" + ((String) entry.getKey()) + "\"\r\n");
                        sb.append("Content-Type: text/plain; charset=UTF-8\r\n");
                        sb.append("Content-Transfer-Encoding: 8bit\r\n");
                        sb.append("\r\n");
                        sb.append((String) entry.getValue());
                        sb.append("\r\n");
                    }
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.write(sb.toString().getBytes());
                    Map map3 = map2;
                    if (map3 != null) {
                        for (Map.Entry entry2 : map3.entrySet()) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("--");
                            sb2.append(uuid);
                            sb2.append("\r\n");
                            PrintStream printStream = System.out;
                            printStream.println("key:" + ((String) entry2.getKey()));
                            PrintStream printStream2 = System.out;
                            printStream2.println("name:" + ((File) entry2.getValue()).getName());
                            sb2.append("Content-Disposition: form-data; name=\"" + ((String) entry2.getKey()) + "\"; filename=\"" + ((File) entry2.getValue()).getName() + "\"\r\n");
                            sb2.append("Content-Type: application/octet-stream; charset=UTF-8\r\n");
                            sb2.append("\r\n");
                            dataOutputStream.write(sb2.toString().getBytes());
                            FileInputStream fileInputStream = new FileInputStream((File) entry2.getValue());
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = fileInputStream.read(bArr);
                                if (read != -1) {
                                    dataOutputStream.write(bArr, 0, read);
                                }
                            }
                            dataOutputStream.write("\r\n".getBytes());
                            fileInputStream.close();
                        }
                    }
                    dataOutputStream.write(("--" + uuid + "--\r\n").getBytes());
                    dataOutputStream.flush();
                    int responseCode = httpURLConnection.getResponseCode();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuilder sb3 = new StringBuilder();
                    if (responseCode == 200) {
                        while (true) {
                            int read2 = inputStream.read();
                            if (read2 == -1) {
                                break;
                            }
                            sb3.append((char) read2);
                        }
                        final String sb4 = sb3.toString();
                        if (httpCallBack != null && sb4 != null) {
                            activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (z) {
                                        DialogUtil.closeWithProgress();
                                    }
                                    httpCallBack.onSuccess(sb4);
                                }
                            });
                        }
                    }
                    dataOutputStream.close();
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    if (httpCallBack != null) {
                        HttpUtil httpUtil2 = HttpUtil.this;
                        httpUtil2.message = "ErrorMsg:" + e.getMessage();
                        activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (z) {
                                    DialogUtil.closeWithProgress();
                                }
                                httpCallBack.onException(HttpUtil.this.message);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void uploadMessage(final boolean z, final Activity activity, final String str, final Map<String, String> map, final HttpCallBack httpCallBack) {
        if (z) {
            DialogUtil.showProgress(activity);
        }
        new Thread(new Runnable() { // from class: com.home.http.HttpUtil.2
            @Override // java.lang.Runnable
            public void run() {
                String uuid = UUID.randomUUID().toString();
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
                    httpURLConnection.setRequestProperty("connection", "keep-alive");
                    httpURLConnection.setRequestProperty("Charsert", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type", URLEncodedUtils.CONTENT_TYPE);
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry entry : map.entrySet()) {
                        sb.append(((String) entry.getKey()) + "=");
                        sb.append(((String) entry.getValue()) + " ");
                    }
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.write(sb.toString().getBytes());
                    dataOutputStream.write(("--" + uuid + "--\r\n").getBytes());
                    dataOutputStream.flush();
                    int responseCode = httpURLConnection.getResponseCode();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuilder sb2 = new StringBuilder();
                    if (responseCode == 200) {
                        while (true) {
                            int read = inputStream.read();
                            if (read == -1) {
                                break;
                            }
                            sb2.append((char) read);
                        }
                        final String sb3 = sb2.toString();
                        if (httpCallBack != null && sb3 != null) {
                            activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (z) {
                                        DialogUtil.closeWithProgress();
                                    }
                                    httpCallBack.onSuccess(sb3);
                                }
                            });
                        }
                    }
                    dataOutputStream.close();
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    if (httpCallBack != null) {
                        HttpUtil httpUtil2 = HttpUtil.this;
                        httpUtil2.message = "ErrorMsg:" + e.getMessage();
                        activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (z) {
                                    DialogUtil.closeWithProgress();
                                }
                                httpCallBack.onException(HttpUtil.this.message);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void getSourceData(final boolean z, final Activity activity, final String str, final Map<String, String> map, final HttpCallBack httpCallBack) {
        if (z) {
            DialogUtil.showProgress(activity);
        }
        new Thread(new Runnable() { // from class: com.home.http.HttpUtil.3
            @Override // java.lang.Runnable
            public void run() {
                Exception e;
                String str2;
                BasicHttpParams basicHttpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
                HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                try {
                    KeyStore.getInstance(KeyStore.getDefaultType()).load(null, null);
                    schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
                HttpProtocolParams.setUseExpectContinue(defaultHttpClient.getParams(), false);
                HttpPost httpPost = new HttpPost(str);
                ArrayList arrayList = new ArrayList();
                for (String str3 : map.keySet()) {
                    arrayList.add(new BasicNameValuePair(str3, (String) map.get(str3)));
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                    str2 = (String) defaultHttpClient.execute(httpPost, new BasicResponseHandler());
                    try {
                        Log.v("HttpUtil", str2);
                    } catch (Exception e3) {
                        e = e3;
                        if (httpCallBack != null) {
                            HttpUtil.this.message = "ErrorMsg:" + e.getMessage();
                            Log.v("HttpUtil", HttpUtil.this.message);
                            activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.3.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (z) {
                                        DialogUtil.closeWithProgress();
                                    }
                                    httpCallBack.onException(HttpUtil.this.message);
                                }
                            });
                        }
                        defaultHttpClient.getConnectionManager().shutdown();
                        if (httpCallBack != null) {
                            return;
                        }
                        return;
                    }
                } catch (Exception e4) {
                    e = e4;
                    str2 = null;
                }
                defaultHttpClient.getConnectionManager().shutdown();
                if (httpCallBack != null || str2 == null) {
                    return;
                }
                HttpUtil.this.result = str2;
                activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.3.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (z) {
                            DialogUtil.closeWithProgress();
                        }
                        httpCallBack.onSuccess(HttpUtil.this.result);
                    }
                });
            }
        }).start();
    }

    public void getSourceData(final Activity activity, final String str, final Map<String, String> map, final HttpCallBack httpCallBack) {
        new Thread(new Runnable() { // from class: com.home.http.HttpUtil.4
            @Override // java.lang.Runnable
            public void run() {
                Exception e;
                String str2;
                BasicHttpParams basicHttpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
                HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                try {
                    KeyStore.getInstance(KeyStore.getDefaultType()).load(null, null);
                    schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
                HttpProtocolParams.setUseExpectContinue(defaultHttpClient.getParams(), false);
                HttpPost httpPost = new HttpPost(str);
                ArrayList arrayList = new ArrayList();
                for (String str3 : map.keySet()) {
                    arrayList.add(new BasicNameValuePair(str3, (String) map.get(str3)));
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                    str2 = (String) defaultHttpClient.execute(httpPost, new BasicResponseHandler());
                    try {
                        Log.v("HttpUtil", str2);
                    } catch (Exception e3) {
                        e = e3;
                        if (httpCallBack != null) {
                            HttpUtil.this.message = "ErrorMsg:" + e.getMessage();
                            Log.v("HttpUtil", HttpUtil.this.message);
                            activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    httpCallBack.onException(HttpUtil.this.message);
                                }
                            });
                        }
                        defaultHttpClient.getConnectionManager().shutdown();
                        if (httpCallBack != null) {
                            return;
                        }
                        return;
                    }
                } catch (Exception e4) {
                    e = e4;
                    str2 = null;
                }
                defaultHttpClient.getConnectionManager().shutdown();
                if (httpCallBack != null || str2 == null) {
                    return;
                }
                HttpUtil.this.result = str2;
                activity.runOnUiThread(new Runnable() { // from class: com.home.http.HttpUtil.4.2
                    @Override // java.lang.Runnable
                    public void run() {
                        httpCallBack.onSuccess(HttpUtil.this.result);
                    }
                });
            }
        }).start();
    }
}
