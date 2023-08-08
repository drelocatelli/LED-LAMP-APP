package okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;
import okhttp3.Authenticator;

/* loaded from: classes.dex */
public interface Authenticator {
    public static final Authenticator NONE = new Authenticator() { // from class: okhttp3.Authenticator$$ExternalSyntheticLambda0
        @Override // okhttp3.Authenticator
        public final Request authenticate(Route route, Response response) {
            return Authenticator.CC.lambda$static$0(route, response);
        }
    };

    /* renamed from: okhttp3.Authenticator$-CC  reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
        static {
            Authenticator authenticator = Authenticator.NONE;
        }

        public static /* synthetic */ Request lambda$static$0(Route route, Response response) throws IOException {
            return null;
        }
    }

    @Nullable
    Request authenticate(@Nullable Route route, Response response) throws IOException;
}
