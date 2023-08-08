package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class FutureRequestExecutionService implements Closeable {
    private final ExecutorService executorService;
    private final HttpClient httpclient;
    private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public FutureRequestExecutionService(HttpClient httpClient, ExecutorService executorService) {
        this.httpclient = httpClient;
        this.executorService = executorService;
    }

    public <T> HttpRequestFutureTask<T> execute(HttpUriRequest httpUriRequest, HttpContext httpContext, ResponseHandler<T> responseHandler) {
        return execute(httpUriRequest, httpContext, responseHandler, null);
    }

    public <T> HttpRequestFutureTask<T> execute(HttpUriRequest httpUriRequest, HttpContext httpContext, ResponseHandler<T> responseHandler, FutureCallback<T> futureCallback) {
        if (this.closed.get()) {
            throw new IllegalStateException("Close has been called on this httpclient instance.");
        }
        this.metrics.getScheduledConnections().incrementAndGet();
        HttpRequestFutureTask<T> httpRequestFutureTask = new HttpRequestFutureTask<>(httpUriRequest, new HttpRequestTaskCallable(this.httpclient, httpUriRequest, httpContext, responseHandler, futureCallback, this.metrics));
        this.executorService.execute(httpRequestFutureTask);
        return httpRequestFutureTask;
    }

    public FutureRequestExecutionMetrics metrics() {
        return this.metrics;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closed.set(true);
        this.executorService.shutdownNow();
        HttpClient httpClient = this.httpclient;
        if (httpClient instanceof Closeable) {
            ((Closeable) httpClient).close();
        }
    }
}
