package org.apache.http.pool;

/* loaded from: classes.dex */
public interface PoolEntryCallback<T, C> {
    void process(PoolEntry<T, C> poolEntry);
}
