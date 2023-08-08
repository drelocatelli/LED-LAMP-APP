package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthState;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

/* loaded from: classes.dex */
public class RedirectExec implements ClientExecChain {
    private final Log log = LogFactory.getLog(getClass());
    private final RedirectStrategy redirectStrategy;
    private final ClientExecChain requestExecutor;
    private final HttpRoutePlanner routePlanner;

    public RedirectExec(ClientExecChain clientExecChain, HttpRoutePlanner httpRoutePlanner, RedirectStrategy redirectStrategy) {
        Args.notNull(clientExecChain, "HTTP client request executor");
        Args.notNull(httpRoutePlanner, "HTTP route planner");
        Args.notNull(redirectStrategy, "HTTP redirect strategy");
        this.requestExecutor = clientExecChain;
        this.routePlanner = httpRoutePlanner;
        this.redirectStrategy = redirectStrategy;
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0119, code lost:
        return r4;
     */
    /* JADX WARN: Finally extract failed */
    @Override // org.apache.http.impl.execchain.ClientExecChain
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CloseableHttpResponse execute(HttpRoute httpRoute, HttpRequestWrapper httpRequestWrapper, HttpClientContext httpClientContext, HttpExecutionAware httpExecutionAware) throws IOException, HttpException {
        Args.notNull(httpRoute, "HTTP route");
        Args.notNull(httpRequestWrapper, "HTTP request");
        Args.notNull(httpClientContext, "HTTP context");
        List<URI> redirectLocations = httpClientContext.getRedirectLocations();
        if (redirectLocations != null) {
            redirectLocations.clear();
        }
        RequestConfig requestConfig = httpClientContext.getRequestConfig();
        int maxRedirects = requestConfig.getMaxRedirects() > 0 ? requestConfig.getMaxRedirects() : 50;
        int i = 0;
        HttpRequestWrapper httpRequestWrapper2 = httpRequestWrapper;
        while (true) {
            CloseableHttpResponse execute = this.requestExecutor.execute(httpRoute, httpRequestWrapper2, httpClientContext, httpExecutionAware);
            try {
                if (!requestConfig.isRedirectsEnabled() || !this.redirectStrategy.isRedirected(httpRequestWrapper2.getOriginal(), execute, httpClientContext)) {
                    break;
                } else if (i >= maxRedirects) {
                    throw new RedirectException("Maximum redirects (" + maxRedirects + ") exceeded");
                } else {
                    i++;
                    HttpUriRequest redirect = this.redirectStrategy.getRedirect(httpRequestWrapper2.getOriginal(), execute, httpClientContext);
                    if (!redirect.headerIterator().hasNext()) {
                        redirect.setHeaders(httpRequestWrapper.getOriginal().getAllHeaders());
                    }
                    httpRequestWrapper2 = HttpRequestWrapper.wrap(redirect);
                    if (httpRequestWrapper2 instanceof HttpEntityEnclosingRequest) {
                        RequestEntityProxy.enhance((HttpEntityEnclosingRequest) httpRequestWrapper2);
                    }
                    URI uri = httpRequestWrapper2.getURI();
                    HttpHost extractHost = URIUtils.extractHost(uri);
                    if (extractHost == null) {
                        throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
                    }
                    if (!httpRoute.getTargetHost().equals(extractHost)) {
                        AuthState targetAuthState = httpClientContext.getTargetAuthState();
                        if (targetAuthState != null) {
                            this.log.debug("Resetting target auth state");
                            targetAuthState.reset();
                        }
                        AuthState proxyAuthState = httpClientContext.getProxyAuthState();
                        if (proxyAuthState != null && proxyAuthState.isConnectionBased()) {
                            this.log.debug("Resetting proxy auth state");
                            proxyAuthState.reset();
                        }
                    }
                    httpRoute = this.routePlanner.determineRoute(extractHost, httpRequestWrapper2, httpClientContext);
                    if (this.log.isDebugEnabled()) {
                        Log log = this.log;
                        log.debug("Redirecting to '" + uri + "' via " + httpRoute);
                    }
                    EntityUtils.consume(execute.getEntity());
                    execute.close();
                }
            } catch (IOException e) {
                execute.close();
                throw e;
            } catch (RuntimeException e2) {
                execute.close();
                throw e2;
            } catch (HttpException e3) {
                try {
                    try {
                        EntityUtils.consume(execute.getEntity());
                    } catch (IOException e4) {
                        this.log.debug("I/O error while releasing connection", e4);
                        execute.close();
                        throw e3;
                    }
                    execute.close();
                    throw e3;
                } catch (Throwable th) {
                    execute.close();
                    throw th;
                }
            }
        }
    }
}
