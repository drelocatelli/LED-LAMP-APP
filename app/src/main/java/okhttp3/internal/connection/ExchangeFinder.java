package okhttp3.internal.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RouteSelector;
import okhttp3.internal.http.ExchangeCodec;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ExchangeFinder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Address address;
    private final Call call;
    private RealConnection connectingConnection;
    private final RealConnectionPool connectionPool;
    private final EventListener eventListener;
    private boolean hasStreamFailure;
    private Route nextRouteToTry;
    private RouteSelector.Selection routeSelection;
    private final RouteSelector routeSelector;
    private final Transmitter transmitter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ExchangeFinder(Transmitter transmitter, RealConnectionPool realConnectionPool, Address address, Call call, EventListener eventListener) {
        this.transmitter = transmitter;
        this.connectionPool = realConnectionPool;
        this.address = address;
        this.call = call;
        this.eventListener = eventListener;
        this.routeSelector = new RouteSelector(address, realConnectionPool.routeDatabase, call, eventListener);
    }

    public ExchangeCodec find(OkHttpClient okHttpClient, Interceptor.Chain chain, boolean z) {
        try {
            return findHealthyConnection(chain.connectTimeoutMillis(), chain.readTimeoutMillis(), chain.writeTimeoutMillis(), okHttpClient.pingIntervalMillis(), okHttpClient.retryOnConnectionFailure(), z).newCodec(okHttpClient, chain);
        } catch (IOException e) {
            trackFailure();
            throw new RouteException(e);
        } catch (RouteException e2) {
            trackFailure();
            throw e2;
        }
    }

    private RealConnection findHealthyConnection(int i, int i2, int i3, int i4, boolean z, boolean z2) throws IOException {
        while (true) {
            RealConnection findConnection = findConnection(i, i2, i3, i4, z);
            synchronized (this.connectionPool) {
                if (findConnection.successCount == 0 && !findConnection.isMultiplexed()) {
                    return findConnection;
                }
                if (findConnection.isHealthy(z2)) {
                    return findConnection;
                }
                findConnection.noNewExchanges();
            }
        }
    }

    private RealConnection findConnection(int i, int i2, int i3, int i4, boolean z) throws IOException {
        Socket socket;
        Socket releaseConnectionNoEvents;
        RealConnection realConnection;
        RealConnection realConnection2;
        Route route;
        boolean z2;
        boolean z3;
        List<Route> list;
        RouteSelector.Selection selection;
        synchronized (this.connectionPool) {
            if (this.transmitter.isCanceled()) {
                throw new IOException("Canceled");
            }
            this.hasStreamFailure = false;
            RealConnection realConnection3 = this.transmitter.connection;
            socket = null;
            releaseConnectionNoEvents = (this.transmitter.connection == null || !this.transmitter.connection.noNewExchanges) ? null : this.transmitter.releaseConnectionNoEvents();
            if (this.transmitter.connection != null) {
                realConnection2 = this.transmitter.connection;
                realConnection = null;
            } else {
                realConnection = realConnection3;
                realConnection2 = null;
            }
            if (realConnection2 == null) {
                if (this.connectionPool.transmitterAcquirePooledConnection(this.address, this.transmitter, null, false)) {
                    realConnection2 = this.transmitter.connection;
                    route = null;
                    z2 = true;
                } else {
                    route = this.nextRouteToTry;
                    if (route != null) {
                        this.nextRouteToTry = null;
                    } else if (retryCurrentRoute()) {
                        route = this.transmitter.connection.route();
                    }
                    z2 = false;
                }
            }
            route = null;
            z2 = false;
        }
        Util.closeQuietly(releaseConnectionNoEvents);
        if (realConnection != null) {
            this.eventListener.connectionReleased(this.call, realConnection);
        }
        if (z2) {
            this.eventListener.connectionAcquired(this.call, realConnection2);
        }
        if (realConnection2 != null) {
            return realConnection2;
        }
        if (route != null || ((selection = this.routeSelection) != null && selection.hasNext())) {
            z3 = false;
        } else {
            this.routeSelection = this.routeSelector.next();
            z3 = true;
        }
        synchronized (this.connectionPool) {
            if (this.transmitter.isCanceled()) {
                throw new IOException("Canceled");
            }
            if (z3) {
                list = this.routeSelection.getAll();
                if (this.connectionPool.transmitterAcquirePooledConnection(this.address, this.transmitter, list, false)) {
                    realConnection2 = this.transmitter.connection;
                    z2 = true;
                }
            } else {
                list = null;
            }
            if (!z2) {
                if (route == null) {
                    route = this.routeSelection.next();
                }
                realConnection2 = new RealConnection(this.connectionPool, route);
                this.connectingConnection = realConnection2;
            }
        }
        if (z2) {
            this.eventListener.connectionAcquired(this.call, realConnection2);
            return realConnection2;
        }
        realConnection2.connect(i, i2, i3, i4, z, this.call, this.eventListener);
        this.connectionPool.routeDatabase.connected(realConnection2.route());
        synchronized (this.connectionPool) {
            this.connectingConnection = null;
            if (this.connectionPool.transmitterAcquirePooledConnection(this.address, this.transmitter, list, true)) {
                realConnection2.noNewExchanges = true;
                socket = realConnection2.socket();
                realConnection2 = this.transmitter.connection;
                this.nextRouteToTry = route;
            } else {
                this.connectionPool.put(realConnection2);
                this.transmitter.acquireConnectionNoEvents(realConnection2);
            }
        }
        Util.closeQuietly(socket);
        this.eventListener.connectionAcquired(this.call, realConnection2);
        return realConnection2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RealConnection connectingConnection() {
        return this.connectingConnection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trackFailure() {
        synchronized (this.connectionPool) {
            this.hasStreamFailure = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasStreamFailure() {
        boolean z;
        synchronized (this.connectionPool) {
            z = this.hasStreamFailure;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasRouteToTry() {
        synchronized (this.connectionPool) {
            boolean z = true;
            if (this.nextRouteToTry != null) {
                return true;
            }
            if (retryCurrentRoute()) {
                this.nextRouteToTry = this.transmitter.connection.route();
                return true;
            }
            RouteSelector.Selection selection = this.routeSelection;
            if ((selection == null || !selection.hasNext()) && !this.routeSelector.hasNext()) {
                z = false;
            }
            return z;
        }
    }

    private boolean retryCurrentRoute() {
        return this.transmitter.connection != null && this.transmitter.connection.routeFailureCount == 0 && Util.sameConnection(this.transmitter.connection.route().address().url(), this.address.url());
    }
}
