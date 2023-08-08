package org.apache.http;

import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public interface ConnectionReuseStrategy {
    boolean keepAlive(HttpResponse httpResponse, HttpContext httpContext);
}
