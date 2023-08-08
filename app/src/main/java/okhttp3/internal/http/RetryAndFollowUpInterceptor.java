package okhttp3.internal.http;

import androidx.appcompat.widget.ActivityChooserView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import javax.annotation.Nullable;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.Transmitter;
import okhttp3.internal.http2.ConnectionShutdownException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;

/* loaded from: classes.dex */
public final class RetryAndFollowUpInterceptor implements Interceptor {
    private static final int MAX_FOLLOW_UPS = 20;
    private final OkHttpClient client;

    public RetryAndFollowUpInterceptor(OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Exchange exchange;
        Request followUpRequest;
        Request request = chain.request();
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        Transmitter transmitter = realInterceptorChain.transmitter();
        Response response = null;
        int i = 0;
        while (true) {
            transmitter.prepareToConnect(request);
            if (transmitter.isCanceled()) {
                throw new IOException("Canceled");
            }
            try {
                try {
                    Response proceed = realInterceptorChain.proceed(request, transmitter, null);
                    if (response != null) {
                        proceed = proceed.newBuilder().priorResponse(response.newBuilder().body(null).build()).build();
                    }
                    response = proceed;
                    exchange = Internal.instance.exchange(response);
                    followUpRequest = followUpRequest(response, exchange != null ? exchange.connection().route() : null);
                } catch (IOException e) {
                    if (!recover(e, transmitter, !(e instanceof ConnectionShutdownException), request)) {
                        throw e;
                    }
                } catch (RouteException e2) {
                    if (!recover(e2.getLastConnectException(), transmitter, false, request)) {
                        throw e2.getFirstConnectException();
                    }
                }
                if (followUpRequest == null) {
                    if (exchange != null && exchange.isDuplex()) {
                        transmitter.timeoutEarlyExit();
                    }
                    return response;
                }
                RequestBody body = followUpRequest.body();
                if (body != null && body.isOneShot()) {
                    return response;
                }
                Util.closeQuietly(response.body());
                if (transmitter.hasExchange()) {
                    exchange.detachWithViolence();
                }
                i++;
                if (i > 20) {
                    throw new ProtocolException("Too many follow-up requests: " + i);
                }
                request = followUpRequest;
            } finally {
                transmitter.exchangeDoneDueToException();
            }
        }
    }

    private boolean recover(IOException iOException, Transmitter transmitter, boolean z, Request request) {
        if (this.client.retryOnConnectionFailure()) {
            return !(z && requestIsOneShot(iOException, request)) && isRecoverable(iOException, z) && transmitter.canRetry();
        }
        return false;
    }

    private boolean requestIsOneShot(IOException iOException, Request request) {
        RequestBody body = request.body();
        return (body != null && body.isOneShot()) || (iOException instanceof FileNotFoundException);
    }

    private boolean isRecoverable(IOException iOException, boolean z) {
        if (iOException instanceof ProtocolException) {
            return false;
        }
        return iOException instanceof InterruptedIOException ? (iOException instanceof SocketTimeoutException) && !z : (((iOException instanceof SSLHandshakeException) && (iOException.getCause() instanceof CertificateException)) || (iOException instanceof SSLPeerUnverifiedException)) ? false : true;
    }

    private Request followUpRequest(Response response, @Nullable Route route) throws IOException {
        String header;
        HttpUrl resolve;
        Proxy proxy;
        if (response == null) {
            throw new IllegalStateException();
        }
        int code = response.code();
        String method = response.request().method();
        if (code == 307 || code == 308) {
            if (!method.equals(HttpGet.METHOD_NAME) && !method.equals(HttpHead.METHOD_NAME)) {
                return null;
            }
        } else if (code != 401) {
            if (code == 503) {
                if ((response.priorResponse() == null || response.priorResponse().code() != 503) && retryAfter(response, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) == 0) {
                    return response.request();
                }
                return null;
            } else if (code == 407) {
                if (route != null) {
                    proxy = route.proxy();
                } else {
                    proxy = this.client.proxy();
                }
                if (proxy.type() != Proxy.Type.HTTP) {
                    throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                }
                return this.client.proxyAuthenticator().authenticate(route, response);
            } else if (code == 408) {
                if (this.client.retryOnConnectionFailure()) {
                    RequestBody body = response.request().body();
                    if (body == null || !body.isOneShot()) {
                        if ((response.priorResponse() == null || response.priorResponse().code() != 408) && retryAfter(response, 0) <= 0) {
                            return response.request();
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            } else {
                switch (code) {
                    case 300:
                    case HttpStatus.SC_MOVED_PERMANENTLY /* 301 */:
                    case HttpStatus.SC_MOVED_TEMPORARILY /* 302 */:
                    case HttpStatus.SC_SEE_OTHER /* 303 */:
                        break;
                    default:
                        return null;
                }
            }
        } else {
            return this.client.authenticator().authenticate(route, response);
        }
        if (!this.client.followRedirects() || (header = response.header(org.apache.http.HttpHeaders.LOCATION)) == null || (resolve = response.request().url().resolve(header)) == null) {
            return null;
        }
        if (resolve.scheme().equals(response.request().url().scheme()) || this.client.followSslRedirects()) {
            Request.Builder newBuilder = response.request().newBuilder();
            if (HttpMethod.permitsRequestBody(method)) {
                boolean redirectsWithBody = HttpMethod.redirectsWithBody(method);
                if (HttpMethod.redirectsToGet(method)) {
                    newBuilder.method(HttpGet.METHOD_NAME, null);
                } else {
                    newBuilder.method(method, redirectsWithBody ? response.request().body() : null);
                }
                if (!redirectsWithBody) {
                    newBuilder.removeHeader("Transfer-Encoding");
                    newBuilder.removeHeader("Content-Length");
                    newBuilder.removeHeader("Content-Type");
                }
            }
            if (!Util.sameConnection(response.request().url(), resolve)) {
                newBuilder.removeHeader("Authorization");
            }
            return newBuilder.url(resolve).build();
        }
        return null;
    }

    private int retryAfter(Response response, int i) {
        String header = response.header(org.apache.http.HttpHeaders.RETRY_AFTER);
        return header == null ? i : header.matches("\\d+") ? Integer.valueOf(header).intValue() : ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    }
}
