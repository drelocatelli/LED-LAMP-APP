package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

/* loaded from: classes.dex */
public class MainClientExec implements ClientExecChain {
    private final HttpAuthenticator authenticator;
    private final HttpClientConnectionManager connManager;
    private final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log;
    private final AuthenticationStrategy proxyAuthStrategy;
    private final HttpProcessor proxyHttpProcessor;
    private final HttpRequestExecutor requestExecutor;
    private final ConnectionReuseStrategy reuseStrategy;
    private final HttpRouteDirector routeDirector;
    private final AuthenticationStrategy targetAuthStrategy;
    private final UserTokenHandler userTokenHandler;

    public MainClientExec(HttpRequestExecutor httpRequestExecutor, HttpClientConnectionManager httpClientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpProcessor httpProcessor, AuthenticationStrategy authenticationStrategy, AuthenticationStrategy authenticationStrategy2, UserTokenHandler userTokenHandler) {
        this.log = LogFactory.getLog(getClass());
        Args.notNull(httpRequestExecutor, "HTTP request executor");
        Args.notNull(httpClientConnectionManager, "Client connection manager");
        Args.notNull(connectionReuseStrategy, "Connection reuse strategy");
        Args.notNull(connectionKeepAliveStrategy, "Connection keep alive strategy");
        Args.notNull(httpProcessor, "Proxy HTTP processor");
        Args.notNull(authenticationStrategy, "Target authentication strategy");
        Args.notNull(authenticationStrategy2, "Proxy authentication strategy");
        Args.notNull(userTokenHandler, "User token handler");
        this.authenticator = new HttpAuthenticator();
        this.routeDirector = new BasicRouteDirector();
        this.requestExecutor = httpRequestExecutor;
        this.connManager = httpClientConnectionManager;
        this.reuseStrategy = connectionReuseStrategy;
        this.keepAliveStrategy = connectionKeepAliveStrategy;
        this.proxyHttpProcessor = httpProcessor;
        this.targetAuthStrategy = authenticationStrategy;
        this.proxyAuthStrategy = authenticationStrategy2;
        this.userTokenHandler = userTokenHandler;
    }

    public MainClientExec(HttpRequestExecutor httpRequestExecutor, HttpClientConnectionManager httpClientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, AuthenticationStrategy authenticationStrategy, AuthenticationStrategy authenticationStrategy2, UserTokenHandler userTokenHandler) {
        this(httpRequestExecutor, httpClientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, new ImmutableHttpProcessor(new RequestTargetHost()), authenticationStrategy, authenticationStrategy2, userTokenHandler);
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x00eb, code lost:
        if (r28.isAborted() != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00f3, code lost:
        throw new org.apache.http.impl.execchain.RequestAbortedException(r5);
     */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0381  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x039c  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x03a5  */
    @Override // org.apache.http.impl.execchain.ClientExecChain
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CloseableHttpResponse execute(HttpRoute httpRoute, HttpRequestWrapper httpRequestWrapper, HttpClientContext httpClientContext, HttpExecutionAware httpExecutionAware) throws IOException, HttpException {
        HttpException httpException;
        ConnectionHolder connectionHolder;
        RuntimeException runtimeException;
        AuthState authState;
        IOException iOException;
        int i;
        ConnectionHolder connectionHolder2;
        HttpClientConnection httpClientConnection;
        Object obj;
        String str;
        HttpResponse httpResponse;
        Object obj2;
        AuthState authState2;
        HttpResponse httpResponse2;
        String str2;
        HttpResponse httpResponse3;
        HttpRoute httpRoute2 = httpRoute;
        HttpExecutionAware httpExecutionAware2 = httpExecutionAware;
        Args.notNull(httpRoute2, "HTTP route");
        Args.notNull(httpRequestWrapper, "HTTP request");
        Args.notNull(httpClientContext, "HTTP context");
        AuthState targetAuthState = httpClientContext.getTargetAuthState();
        if (targetAuthState == null) {
            targetAuthState = new AuthState();
            httpClientContext.setAttribute("http.auth.target-scope", targetAuthState);
        }
        AuthState authState3 = targetAuthState;
        AuthState proxyAuthState = httpClientContext.getProxyAuthState();
        if (proxyAuthState == null) {
            proxyAuthState = new AuthState();
            httpClientContext.setAttribute("http.auth.proxy-scope", proxyAuthState);
        }
        AuthState authState4 = proxyAuthState;
        if (httpRequestWrapper instanceof HttpEntityEnclosingRequest) {
            RequestEntityProxy.enhance((HttpEntityEnclosingRequest) httpRequestWrapper);
        }
        Object userToken = httpClientContext.getUserToken();
        ConnectionRequest requestConnection = this.connManager.requestConnection(httpRoute2, userToken);
        String str3 = "Request aborted";
        if (httpExecutionAware2 != null) {
            if (httpExecutionAware.isAborted()) {
                requestConnection.cancel();
                throw new RequestAbortedException("Request aborted");
            }
            httpExecutionAware2.setCancellable(requestConnection);
        }
        RequestConfig requestConfig = httpClientContext.getRequestConfig();
        try {
            int connectionRequestTimeout = requestConfig.getConnectionRequestTimeout();
            HttpClientConnection httpClientConnection2 = requestConnection.get(connectionRequestTimeout > 0 ? connectionRequestTimeout : 0L, TimeUnit.MILLISECONDS);
            httpClientContext.setAttribute("http.connection", httpClientConnection2);
            if (requestConfig.isStaleConnectionCheckEnabled() && httpClientConnection2.isOpen()) {
                this.log.debug("Stale connection check");
                if (httpClientConnection2.isStale()) {
                    this.log.debug("Stale connection detected");
                    httpClientConnection2.close();
                }
            }
            ConnectionHolder connectionHolder3 = new ConnectionHolder(this.log, this.connManager, httpClientConnection2);
            if (httpExecutionAware2 != null) {
                try {
                    try {
                        httpExecutionAware2.setCancellable(connectionHolder3);
                    } catch (IOException e) {
                        iOException = e;
                        connectionHolder = connectionHolder3;
                        authState = authState4;
                        connectionHolder.abortConnection();
                        if (authState.isConnectionBased()) {
                        }
                        if (authState3.isConnectionBased()) {
                        }
                        throw iOException;
                    } catch (RuntimeException e2) {
                        runtimeException = e2;
                        connectionHolder = connectionHolder3;
                        authState = authState4;
                        connectionHolder.abortConnection();
                        if (authState.isConnectionBased()) {
                        }
                        if (authState3.isConnectionBased()) {
                        }
                        throw runtimeException;
                    } catch (HttpException e3) {
                        httpException = e3;
                        connectionHolder = connectionHolder3;
                        connectionHolder.abortConnection();
                        throw httpException;
                    }
                } catch (Error e4) {
                    this.connManager.shutdown();
                    throw e4;
                } catch (ConnectionShutdownException e5) {
                    InterruptedIOException interruptedIOException = new InterruptedIOException("Connection has been shut down");
                    interruptedIOException.initCause(e5);
                    throw interruptedIOException;
                }
            }
            int i2 = 1;
            int i3 = 1;
            while (true) {
                if (i3 > i2 && !RequestEntityProxy.isRepeatable(httpRequestWrapper)) {
                    throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
                }
                try {
                    if (httpClientConnection2.isOpen()) {
                        i = i3;
                        connectionHolder2 = connectionHolder3;
                        httpClientConnection = httpClientConnection2;
                        obj = userToken;
                        authState = authState4;
                        str = str3;
                    } else {
                        Log log = this.log;
                        i = i3;
                        StringBuilder sb = new StringBuilder();
                        connectionHolder2 = connectionHolder3;
                        try {
                            try {
                                sb.append("Opening connection ");
                                sb.append(httpRoute2);
                                log.debug(sb.toString());
                                AuthState authState5 = authState4;
                                httpClientConnection = httpClientConnection2;
                                authState = authState4;
                                str = str3;
                                obj = userToken;
                                try {
                                    try {
                                        establishRoute(authState5, httpClientConnection2, httpRoute, httpRequestWrapper, httpClientContext);
                                    } catch (IOException e6) {
                                        iOException = e6;
                                        connectionHolder = connectionHolder2;
                                        connectionHolder.abortConnection();
                                        if (authState.isConnectionBased()) {
                                        }
                                        if (authState3.isConnectionBased()) {
                                        }
                                        throw iOException;
                                    } catch (RuntimeException e7) {
                                        runtimeException = e7;
                                        connectionHolder = connectionHolder2;
                                        connectionHolder.abortConnection();
                                        if (authState.isConnectionBased()) {
                                        }
                                        if (authState3.isConnectionBased()) {
                                        }
                                        throw runtimeException;
                                    }
                                } catch (TunnelRefusedException e8) {
                                    if (this.log.isDebugEnabled()) {
                                        this.log.debug(e8.getMessage());
                                    }
                                    httpResponse = e8.getResponse();
                                    connectionHolder = connectionHolder2;
                                    if (obj == null) {
                                        obj2 = this.userTokenHandler.getUserToken(httpClientContext);
                                        httpClientContext.setAttribute("http.user-token", obj2);
                                    } else {
                                        obj2 = obj;
                                    }
                                    if (obj2 != null) {
                                        connectionHolder.setState(obj2);
                                    }
                                    HttpEntity entity = httpResponse.getEntity();
                                    if (entity != null && entity.isStreaming()) {
                                        return new HttpResponseProxy(httpResponse, connectionHolder);
                                    }
                                    connectionHolder.releaseConnection();
                                    return new HttpResponseProxy(httpResponse, null);
                                }
                            } catch (IOException e9) {
                                iOException = e9;
                                authState = authState4;
                            } catch (RuntimeException e10) {
                                runtimeException = e10;
                                authState = authState4;
                            }
                        } catch (HttpException e11) {
                            httpException = e11;
                            connectionHolder = connectionHolder2;
                            connectionHolder.abortConnection();
                            throw httpException;
                        }
                    }
                    try {
                        try {
                            int socketTimeout = requestConfig.getSocketTimeout();
                            if (socketTimeout >= 0) {
                                httpClientConnection.setSocketTimeout(socketTimeout);
                            }
                            if (httpExecutionAware2 != null && httpExecutionAware.isAborted()) {
                                throw new RequestAbortedException(str);
                            }
                            if (this.log.isDebugEnabled()) {
                                this.log.debug("Executing request " + httpRequestWrapper.getRequestLine());
                            }
                            if (!httpRequestWrapper.containsHeader("Authorization")) {
                                if (this.log.isDebugEnabled()) {
                                    this.log.debug("Target auth state: " + authState3.getState());
                                }
                                this.authenticator.generateAuthResponse(httpRequestWrapper, authState3, httpClientContext);
                            }
                            if (httpRequestWrapper.containsHeader("Proxy-Authorization") || httpRoute.isTunnelled()) {
                                authState2 = authState;
                            } else {
                                if (this.log.isDebugEnabled()) {
                                    this.log.debug("Proxy auth state: " + authState.getState());
                                }
                                authState2 = authState;
                                try {
                                    this.authenticator.generateAuthResponse(httpRequestWrapper, authState2, httpClientContext);
                                } catch (IOException e12) {
                                    iOException = e12;
                                    authState = authState2;
                                    connectionHolder = connectionHolder2;
                                    connectionHolder.abortConnection();
                                    if (authState.isConnectionBased()) {
                                    }
                                    if (authState3.isConnectionBased()) {
                                    }
                                    throw iOException;
                                } catch (RuntimeException e13) {
                                    runtimeException = e13;
                                    authState = authState2;
                                    connectionHolder = connectionHolder2;
                                    connectionHolder.abortConnection();
                                    if (authState.isConnectionBased()) {
                                    }
                                    if (authState3.isConnectionBased()) {
                                    }
                                    throw runtimeException;
                                }
                            }
                            try {
                                HttpResponse execute = this.requestExecutor.execute(httpRequestWrapper, httpClientConnection, httpClientContext);
                                if (this.reuseStrategy.keepAlive(execute, httpClientContext)) {
                                    try {
                                        long keepAliveDuration = this.keepAliveStrategy.getKeepAliveDuration(execute, httpClientContext);
                                        if (this.log.isDebugEnabled()) {
                                            if (keepAliveDuration > 0) {
                                                str2 = "for " + keepAliveDuration + " " + TimeUnit.MILLISECONDS;
                                            } else {
                                                str2 = "indefinitely";
                                            }
                                            httpResponse2 = execute;
                                            this.log.debug("Connection can be kept alive " + str2);
                                        } else {
                                            httpResponse2 = execute;
                                        }
                                        connectionHolder = connectionHolder2;
                                        try {
                                            try {
                                                connectionHolder.setValidFor(keepAliveDuration, TimeUnit.MILLISECONDS);
                                                connectionHolder.markReusable();
                                            } catch (IOException e14) {
                                                e = e14;
                                                iOException = e;
                                                authState = authState2;
                                                connectionHolder.abortConnection();
                                                if (authState.isConnectionBased()) {
                                                }
                                                if (authState3.isConnectionBased()) {
                                                }
                                                throw iOException;
                                            } catch (RuntimeException e15) {
                                                e = e15;
                                                runtimeException = e;
                                                authState = authState2;
                                                connectionHolder.abortConnection();
                                                if (authState.isConnectionBased()) {
                                                }
                                                if (authState3.isConnectionBased()) {
                                                }
                                                throw runtimeException;
                                            }
                                        } catch (HttpException e16) {
                                            e = e16;
                                            httpException = e;
                                            connectionHolder.abortConnection();
                                            throw httpException;
                                        }
                                    } catch (IOException e17) {
                                        e = e17;
                                        connectionHolder = connectionHolder2;
                                    } catch (RuntimeException e18) {
                                        e = e18;
                                        connectionHolder = connectionHolder2;
                                    }
                                } else {
                                    httpResponse2 = execute;
                                    connectionHolder = connectionHolder2;
                                    try {
                                        connectionHolder.markNonReusable();
                                    } catch (IOException e19) {
                                        e = e19;
                                        authState = authState2;
                                        iOException = e;
                                        connectionHolder.abortConnection();
                                        if (authState.isConnectionBased()) {
                                            authState.reset();
                                        }
                                        if (authState3.isConnectionBased()) {
                                            authState3.reset();
                                        }
                                        throw iOException;
                                    } catch (RuntimeException e20) {
                                        e = e20;
                                        authState = authState2;
                                        runtimeException = e;
                                        connectionHolder.abortConnection();
                                        if (authState.isConnectionBased()) {
                                            authState.reset();
                                        }
                                        if (authState3.isConnectionBased()) {
                                            authState3.reset();
                                        }
                                        throw runtimeException;
                                    }
                                }
                                httpResponse3 = httpResponse2;
                                authState = authState2;
                            } catch (IOException e21) {
                                e = e21;
                                authState = authState2;
                                connectionHolder = connectionHolder2;
                                iOException = e;
                                connectionHolder.abortConnection();
                                if (authState.isConnectionBased()) {
                                }
                                if (authState3.isConnectionBased()) {
                                }
                                throw iOException;
                            } catch (RuntimeException e22) {
                                e = e22;
                                authState = authState2;
                                connectionHolder = connectionHolder2;
                                runtimeException = e;
                                connectionHolder.abortConnection();
                                if (authState.isConnectionBased()) {
                                }
                                if (authState3.isConnectionBased()) {
                                }
                                throw runtimeException;
                            }
                        } catch (IOException e23) {
                            e = e23;
                        } catch (RuntimeException e24) {
                            e = e24;
                        }
                        try {
                            if (!needAuthentication(authState3, authState2, httpRoute, httpResponse3, httpClientContext)) {
                                httpResponse = httpResponse3;
                                break;
                            }
                            HttpEntity entity2 = httpResponse3.getEntity();
                            if (connectionHolder.isReusable()) {
                                EntityUtils.consume(entity2);
                            } else {
                                httpClientConnection.close();
                                if (authState.getState() == AuthProtocolState.SUCCESS && authState.isConnectionBased()) {
                                    this.log.debug("Resetting proxy auth state");
                                    authState.reset();
                                }
                                if (authState3.getState() == AuthProtocolState.SUCCESS && authState3.isConnectionBased()) {
                                    this.log.debug("Resetting target auth state");
                                    authState3.reset();
                                }
                            }
                            HttpRequest original = httpRequestWrapper.getOriginal();
                            if (!original.containsHeader("Authorization")) {
                                httpRequestWrapper.removeHeaders("Authorization");
                            }
                            if (!original.containsHeader("Proxy-Authorization")) {
                                httpRequestWrapper.removeHeaders("Proxy-Authorization");
                            }
                            i3 = i + 1;
                            httpClientConnection2 = httpClientConnection;
                            connectionHolder3 = connectionHolder;
                            str3 = str;
                            authState4 = authState;
                            userToken = obj;
                            i2 = 1;
                            httpRoute2 = httpRoute;
                            httpExecutionAware2 = httpExecutionAware;
                        } catch (IOException e25) {
                            e = e25;
                            iOException = e;
                            connectionHolder.abortConnection();
                            if (authState.isConnectionBased()) {
                            }
                            if (authState3.isConnectionBased()) {
                            }
                            throw iOException;
                        } catch (RuntimeException e26) {
                            e = e26;
                            runtimeException = e;
                            connectionHolder.abortConnection();
                            if (authState.isConnectionBased()) {
                            }
                            if (authState3.isConnectionBased()) {
                            }
                            throw runtimeException;
                        }
                    } catch (HttpException e27) {
                        e = e27;
                        connectionHolder = connectionHolder2;
                    }
                } catch (IOException e28) {
                    e = e28;
                    connectionHolder = connectionHolder3;
                    authState = authState4;
                } catch (RuntimeException e29) {
                    e = e29;
                    connectionHolder = connectionHolder3;
                    authState = authState4;
                } catch (HttpException e30) {
                    e = e30;
                    connectionHolder = connectionHolder3;
                }
            }
        } catch (InterruptedException e31) {
            Thread.currentThread().interrupt();
            throw new RequestAbortedException("Request aborted", e31);
        } catch (ExecutionException e32) {
            ExecutionException executionException = e32;
            Throwable cause = executionException.getCause();
            if (cause != null) {
                executionException = cause;
            }
            throw new RequestAbortedException("Request execution failed", executionException);
        }
    }

    void establishRoute(AuthState authState, HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpRequest httpRequest, HttpClientContext httpClientContext) throws HttpException, IOException {
        int nextStep;
        int connectTimeout = httpClientContext.getRequestConfig().getConnectTimeout();
        RouteTracker routeTracker = new RouteTracker(httpRoute);
        do {
            HttpRoute route = routeTracker.toRoute();
            nextStep = this.routeDirector.nextStep(httpRoute, route);
            switch (nextStep) {
                case -1:
                    throw new HttpException("Unable to establish route: planned = " + httpRoute + "; current = " + route);
                case 0:
                    this.connManager.routeComplete(httpClientConnection, httpRoute, httpClientContext);
                    continue;
                case 1:
                    this.connManager.connect(httpClientConnection, httpRoute, connectTimeout > 0 ? connectTimeout : 0, httpClientContext);
                    routeTracker.connectTarget(httpRoute.isSecure());
                    continue;
                case 2:
                    this.connManager.connect(httpClientConnection, httpRoute, connectTimeout > 0 ? connectTimeout : 0, httpClientContext);
                    routeTracker.connectProxy(httpRoute.getProxyHost(), false);
                    continue;
                case 3:
                    boolean createTunnelToTarget = createTunnelToTarget(authState, httpClientConnection, httpRoute, httpRequest, httpClientContext);
                    this.log.debug("Tunnel to target created.");
                    routeTracker.tunnelTarget(createTunnelToTarget);
                    continue;
                case 4:
                    int hopCount = route.getHopCount() - 1;
                    boolean createTunnelToProxy = createTunnelToProxy(httpRoute, hopCount, httpClientContext);
                    this.log.debug("Tunnel to proxy created.");
                    routeTracker.tunnelProxy(httpRoute.getHopTarget(hopCount), createTunnelToProxy);
                    continue;
                case 5:
                    this.connManager.upgrade(httpClientConnection, httpRoute, httpClientContext);
                    routeTracker.layerProtocol(httpRoute.isSecure());
                    continue;
                default:
                    throw new IllegalStateException("Unknown step indicator " + nextStep + " from RouteDirector.");
            }
        } while (nextStep > 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x009a, code lost:
        if (r16.reuseStrategy.keepAlive(r7, r21) == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x009c, code lost:
        r16.log.debug("Connection kept alive");
        org.apache.http.util.EntityUtils.consume(r7.getEntity());
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00ab, code lost:
        r18.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean createTunnelToTarget(AuthState authState, HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpRequest httpRequest, HttpClientContext httpClientContext) throws HttpException, IOException {
        HttpResponse httpResponse;
        RequestConfig requestConfig = httpClientContext.getRequestConfig();
        int connectTimeout = requestConfig.getConnectTimeout();
        HttpHost targetHost = httpRoute.getTargetHost();
        HttpHost proxyHost = httpRoute.getProxyHost();
        BasicHttpRequest basicHttpRequest = new BasicHttpRequest("CONNECT", targetHost.toHostString(), httpRequest.getProtocolVersion());
        this.requestExecutor.preProcess(basicHttpRequest, this.proxyHttpProcessor, httpClientContext);
        while (true) {
            HttpResponse httpResponse2 = null;
            while (true) {
                if (httpResponse2 == null) {
                    if (!httpClientConnection.isOpen()) {
                        this.connManager.connect(httpClientConnection, httpRoute, connectTimeout > 0 ? connectTimeout : 0, httpClientContext);
                    }
                    basicHttpRequest.removeHeaders("Proxy-Authorization");
                    this.authenticator.generateAuthResponse(basicHttpRequest, authState, httpClientContext);
                    HttpResponse execute = this.requestExecutor.execute(basicHttpRequest, httpClientConnection, httpClientContext);
                    this.requestExecutor.postProcess(execute, this.proxyHttpProcessor, httpClientContext);
                    if (execute.getStatusLine().getStatusCode() < 200) {
                        throw new HttpException("Unexpected response to CONNECT request: " + execute.getStatusLine());
                    }
                    if (!requestConfig.isAuthenticationEnabled()) {
                        httpResponse = execute;
                    } else if (this.authenticator.isAuthenticationRequested(proxyHost, execute, this.proxyAuthStrategy, authState, httpClientContext) && this.authenticator.handleAuthChallenge(proxyHost, execute, this.proxyAuthStrategy, authState, httpClientContext)) {
                        break;
                    } else {
                        httpResponse = execute;
                    }
                    httpResponse2 = httpResponse;
                } else if (httpResponse2.getStatusLine().getStatusCode() > 299) {
                    HttpEntity entity = httpResponse2.getEntity();
                    if (entity != null) {
                        httpResponse2.setEntity(new BufferedHttpEntity(entity));
                    }
                    httpClientConnection.close();
                    throw new TunnelRefusedException("CONNECT refused by proxy: " + httpResponse2.getStatusLine(), httpResponse2);
                } else {
                    return false;
                }
            }
        }
    }

    private boolean createTunnelToProxy(HttpRoute httpRoute, int i, HttpClientContext httpClientContext) throws HttpException {
        throw new HttpException("Proxy chains are not supported.");
    }

    private boolean needAuthentication(AuthState authState, AuthState authState2, HttpRoute httpRoute, HttpResponse httpResponse, HttpClientContext httpClientContext) {
        if (httpClientContext.getRequestConfig().isAuthenticationEnabled()) {
            HttpHost targetHost = httpClientContext.getTargetHost();
            if (targetHost == null) {
                targetHost = httpRoute.getTargetHost();
            }
            if (targetHost.getPort() < 0) {
                targetHost = new HttpHost(targetHost.getHostName(), httpRoute.getTargetHost().getPort(), targetHost.getSchemeName());
            }
            boolean isAuthenticationRequested = this.authenticator.isAuthenticationRequested(targetHost, httpResponse, this.targetAuthStrategy, authState, httpClientContext);
            HttpHost proxyHost = httpRoute.getProxyHost();
            if (proxyHost == null) {
                proxyHost = httpRoute.getTargetHost();
            }
            boolean isAuthenticationRequested2 = this.authenticator.isAuthenticationRequested(proxyHost, httpResponse, this.proxyAuthStrategy, authState2, httpClientContext);
            if (isAuthenticationRequested) {
                return this.authenticator.handleAuthChallenge(targetHost, httpResponse, this.targetAuthStrategy, authState, httpClientContext);
            } else if (isAuthenticationRequested2) {
                return this.authenticator.handleAuthChallenge(proxyHost, httpResponse, this.proxyAuthStrategy, authState2, httpClientContext);
            } else {
                return false;
            }
        }
        return false;
    }
}
