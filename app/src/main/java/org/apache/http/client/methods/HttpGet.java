package org.apache.http.client.methods;

import java.net.URI;

/* loaded from: classes.dex */
public class HttpGet extends HttpRequestBase {
    public static final String METHOD_NAME = "GET";

    @Override // org.apache.http.client.methods.HttpRequestBase, org.apache.http.client.methods.HttpUriRequest
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpGet() {
    }

    public HttpGet(URI uri) {
        setURI(uri);
    }

    public HttpGet(String str) {
        setURI(URI.create(str));
    }
}
