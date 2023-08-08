package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

/* loaded from: classes.dex */
public class UsernamePasswordCredentials implements Credentials, Serializable {
    private static final long serialVersionUID = 243343858802739403L;
    private final String password;
    private final BasicUserPrincipal principal;

    @Deprecated
    public UsernamePasswordCredentials(String str) {
        Args.notNull(str, "Username:password string");
        int indexOf = str.indexOf(58);
        if (indexOf >= 0) {
            this.principal = new BasicUserPrincipal(str.substring(0, indexOf));
            this.password = str.substring(indexOf + 1);
            return;
        }
        this.principal = new BasicUserPrincipal(str);
        this.password = null;
    }

    public UsernamePasswordCredentials(String str, String str2) {
        Args.notNull(str, "Username");
        this.principal = new BasicUserPrincipal(str);
        this.password = str2;
    }

    @Override // org.apache.http.auth.Credentials
    public Principal getUserPrincipal() {
        return this.principal;
    }

    public String getUserName() {
        return this.principal.getName();
    }

    @Override // org.apache.http.auth.Credentials
    public String getPassword() {
        return this.password;
    }

    public int hashCode() {
        return this.principal.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof UsernamePasswordCredentials) && LangUtils.equals(this.principal, ((UsernamePasswordCredentials) obj).principal);
    }

    public String toString() {
        return this.principal.toString();
    }
}
