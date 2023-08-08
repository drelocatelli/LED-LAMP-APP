package org.apache.http.conn.ssl;

import java.net.Socket;
import java.util.Map;

@Deprecated
/* loaded from: classes.dex */
public interface PrivateKeyStrategy {
    String chooseAlias(Map<String, PrivateKeyDetails> map, Socket socket);
}
