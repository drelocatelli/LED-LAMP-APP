package org.apache.http.io;

/* loaded from: classes.dex */
public interface HttpTransportMetrics {
    long getBytesTransferred();

    void reset();
}
