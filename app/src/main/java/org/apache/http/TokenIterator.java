package org.apache.http;

import java.util.Iterator;

/* loaded from: classes.dex */
public interface TokenIterator extends Iterator<Object> {
    @Override // java.util.Iterator
    boolean hasNext();

    String nextToken();
}
