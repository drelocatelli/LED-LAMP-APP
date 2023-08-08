package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthState;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
/* loaded from: classes.dex */
public class RequestProxyAuthentication extends RequestAuthenticationBase {
    @Override // org.apache.http.HttpRequestInterceptor
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        Args.notNull(httpRequest, "HTTP request");
        Args.notNull(httpContext, "HTTP context");
        if (httpRequest.containsHeader("Proxy-Authorization")) {
            return;
        }
        HttpRoutedConnection httpRoutedConnection = (HttpRoutedConnection) httpContext.getAttribute("http.connection");
        if (httpRoutedConnection == null) {
            this.log.debug("HTTP connection not set in the context");
        } else if (httpRoutedConnection.getRoute().isTunnelled()) {
        } else {
            AuthState authState = (AuthState) httpContext.getAttribute("http.auth.proxy-scope");
            if (authState == null) {
                this.log.debug("Proxy auth state not set in the context");
                return;
            }
            if (this.log.isDebugEnabled()) {
                Log log = this.log;
                log.debug("Proxy auth state: " + authState.getState());
            }
            process(authState, httpRequest, httpContext);
        }
    }
}
