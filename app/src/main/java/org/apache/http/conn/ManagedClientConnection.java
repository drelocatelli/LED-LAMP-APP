package org.apache.http.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
/* loaded from: classes.dex */
public interface ManagedClientConnection extends HttpClientConnection, HttpRoutedConnection, ManagedHttpClientConnection, ConnectionReleaseTrigger {
    @Override // org.apache.http.conn.HttpRoutedConnection
    HttpRoute getRoute();

    @Override // org.apache.http.conn.HttpRoutedConnection, org.apache.http.conn.ManagedHttpClientConnection
    SSLSession getSSLSession();

    Object getState();

    boolean isMarkedReusable();

    @Override // org.apache.http.conn.HttpRoutedConnection
    boolean isSecure();

    void layerProtocol(HttpContext httpContext, HttpParams httpParams) throws IOException;

    void markReusable();

    void open(HttpRoute httpRoute, HttpContext httpContext, HttpParams httpParams) throws IOException;

    void setIdleDuration(long j, TimeUnit timeUnit);

    void setState(Object obj);

    void tunnelProxy(HttpHost httpHost, boolean z, HttpParams httpParams) throws IOException;

    void tunnelTarget(boolean z, HttpParams httpParams) throws IOException;

    void unmarkReusable();
}
