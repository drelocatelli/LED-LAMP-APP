package org.apache.http.client.protocol;

import java.net.URI;
import java.util.List;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Lookup;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

/* loaded from: classes.dex */
public class HttpClientContext extends HttpCoreContext {
    public static final String AUTHSCHEME_REGISTRY = "http.authscheme-registry";
    public static final String AUTH_CACHE = "http.auth.auth-cache";
    public static final String COOKIESPEC_REGISTRY = "http.cookiespec-registry";
    public static final String COOKIE_ORIGIN = "http.cookie-origin";
    public static final String COOKIE_SPEC = "http.cookie-spec";
    public static final String COOKIE_STORE = "http.cookie-store";
    public static final String CREDS_PROVIDER = "http.auth.credentials-provider";
    public static final String HTTP_ROUTE = "http.route";
    public static final String PROXY_AUTH_STATE = "http.auth.proxy-scope";
    public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
    public static final String REQUEST_CONFIG = "http.request-config";
    public static final String TARGET_AUTH_STATE = "http.auth.target-scope";
    public static final String USER_TOKEN = "http.user-token";

    public static HttpClientContext adapt(HttpContext httpContext) {
        if (httpContext instanceof HttpClientContext) {
            return (HttpClientContext) httpContext;
        }
        return new HttpClientContext(httpContext);
    }

    public static HttpClientContext create() {
        return new HttpClientContext(new BasicHttpContext());
    }

    public HttpClientContext(HttpContext httpContext) {
        super(httpContext);
    }

    public HttpClientContext() {
    }

    public RouteInfo getHttpRoute() {
        return (RouteInfo) getAttribute("http.route", HttpRoute.class);
    }

    public List<URI> getRedirectLocations() {
        return (List) getAttribute("http.protocol.redirect-locations", List.class);
    }

    public CookieStore getCookieStore() {
        return (CookieStore) getAttribute("http.cookie-store", CookieStore.class);
    }

    public void setCookieStore(CookieStore cookieStore) {
        setAttribute("http.cookie-store", cookieStore);
    }

    public CookieSpec getCookieSpec() {
        return (CookieSpec) getAttribute("http.cookie-spec", CookieSpec.class);
    }

    public CookieOrigin getCookieOrigin() {
        return (CookieOrigin) getAttribute("http.cookie-origin", CookieOrigin.class);
    }

    private <T> Lookup<T> getLookup(String str, Class<T> cls) {
        return (Lookup) getAttribute(str, Lookup.class);
    }

    public Lookup<CookieSpecProvider> getCookieSpecRegistry() {
        return getLookup("http.cookiespec-registry", CookieSpecProvider.class);
    }

    public void setCookieSpecRegistry(Lookup<CookieSpecProvider> lookup) {
        setAttribute("http.cookiespec-registry", lookup);
    }

    public Lookup<AuthSchemeProvider> getAuthSchemeRegistry() {
        return getLookup("http.authscheme-registry", AuthSchemeProvider.class);
    }

    public void setAuthSchemeRegistry(Lookup<AuthSchemeProvider> lookup) {
        setAttribute("http.authscheme-registry", lookup);
    }

    public CredentialsProvider getCredentialsProvider() {
        return (CredentialsProvider) getAttribute("http.auth.credentials-provider", CredentialsProvider.class);
    }

    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        setAttribute("http.auth.credentials-provider", credentialsProvider);
    }

    public AuthCache getAuthCache() {
        return (AuthCache) getAttribute("http.auth.auth-cache", AuthCache.class);
    }

    public void setAuthCache(AuthCache authCache) {
        setAttribute("http.auth.auth-cache", authCache);
    }

    public AuthState getTargetAuthState() {
        return (AuthState) getAttribute("http.auth.target-scope", AuthState.class);
    }

    public AuthState getProxyAuthState() {
        return (AuthState) getAttribute("http.auth.proxy-scope", AuthState.class);
    }

    public <T> T getUserToken(Class<T> cls) {
        return (T) getAttribute("http.user-token", cls);
    }

    public Object getUserToken() {
        return getAttribute("http.user-token");
    }

    public void setUserToken(Object obj) {
        setAttribute("http.user-token", obj);
    }

    public RequestConfig getRequestConfig() {
        RequestConfig requestConfig = (RequestConfig) getAttribute("http.request-config", RequestConfig.class);
        return requestConfig != null ? requestConfig : RequestConfig.DEFAULT;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        setAttribute("http.request-config", requestConfig);
    }
}
