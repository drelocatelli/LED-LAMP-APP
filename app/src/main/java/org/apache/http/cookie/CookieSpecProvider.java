package org.apache.http.cookie;

import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public interface CookieSpecProvider {
    CookieSpec create(HttpContext httpContext);
}
