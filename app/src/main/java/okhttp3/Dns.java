package okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import okhttp3.Dns;

/* loaded from: classes.dex */
public interface Dns {
    public static final Dns SYSTEM = new Dns() { // from class: okhttp3.Dns$$ExternalSyntheticLambda0
        @Override // okhttp3.Dns
        public final List lookup(String str) {
            return Dns.CC.lambda$static$0(str);
        }
    };

    List<InetAddress> lookup(String str) throws UnknownHostException;

    /* renamed from: okhttp3.Dns$-CC  reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
        static {
            Dns dns = Dns.SYSTEM;
        }

        public static /* synthetic */ List lambda$static$0(String str) throws UnknownHostException {
            if (str == null) {
                throw new UnknownHostException("hostname == null");
            }
            try {
                return Arrays.asList(InetAddress.getAllByName(str));
            } catch (NullPointerException e) {
                UnknownHostException unknownHostException = new UnknownHostException("Broken system behaviour for dns lookup of " + str);
                unknownHostException.initCause(e);
                throw unknownHostException;
            }
        }
    }
}
