package org.apache.http.conn.ssl;

import org.apache.http.util.Args;

/* loaded from: classes.dex */
final class SubjectName {
    static final int DNS = 2;
    static final int IP = 7;
    private final int type;
    private final String value;

    static SubjectName IP(String str) {
        return new SubjectName(str, 7);
    }

    static SubjectName DNS(String str) {
        return new SubjectName(str, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SubjectName(String str, int i) {
        this.value = (String) Args.notNull(str, "Value");
        this.type = Args.positive(i, "Type");
    }

    public int getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.value;
    }
}
