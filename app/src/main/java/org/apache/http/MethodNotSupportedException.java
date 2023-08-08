package org.apache.http;

/* loaded from: classes.dex */
public class MethodNotSupportedException extends HttpException {
    private static final long serialVersionUID = 3365359036840171201L;

    public MethodNotSupportedException(String str) {
        super(str);
    }

    public MethodNotSupportedException(String str, Throwable th) {
        super(str, th);
    }
}
