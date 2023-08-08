package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.ietf.jgss.GSSCredential;

/* loaded from: classes.dex */
public class KerberosCredentials implements Credentials, Serializable {
    private static final long serialVersionUID = 487421613855550713L;
    private final GSSCredential gssCredential;

    @Override // org.apache.http.auth.Credentials
    public String getPassword() {
        return null;
    }

    @Override // org.apache.http.auth.Credentials
    public Principal getUserPrincipal() {
        return null;
    }

    public KerberosCredentials(GSSCredential gSSCredential) {
        this.gssCredential = gSSCredential;
    }

    public GSSCredential getGSSCredential() {
        return this.gssCredential;
    }
}
