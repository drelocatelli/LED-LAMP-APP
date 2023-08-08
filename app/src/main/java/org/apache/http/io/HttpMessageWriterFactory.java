package org.apache.http.io;

import org.apache.http.HttpMessage;

/* loaded from: classes.dex */
public interface HttpMessageWriterFactory<T extends HttpMessage> {
    HttpMessageWriter<T> create(SessionOutputBuffer sessionOutputBuffer);
}
