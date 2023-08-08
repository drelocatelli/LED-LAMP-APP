package org.apache.http.impl.cookie;

import androidx.appcompat.widget.ActivityChooserView;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

/* loaded from: classes.dex */
public class DefaultCookieSpec implements CookieSpec {
    private final NetscapeDraftSpec netscapeDraft;
    private final RFC2109Spec obsoleteStrict;
    private final RFC2965Spec strict;

    @Override // org.apache.http.cookie.CookieSpec
    public Header getVersionHeader() {
        return null;
    }

    public String toString() {
        return CookieSpecs.DEFAULT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultCookieSpec(RFC2965Spec rFC2965Spec, RFC2109Spec rFC2109Spec, NetscapeDraftSpec netscapeDraftSpec) {
        this.strict = rFC2965Spec;
        this.obsoleteStrict = rFC2109Spec;
        this.netscapeDraft = netscapeDraftSpec;
    }

    public DefaultCookieSpec(String[] strArr, boolean z) {
        this.strict = new RFC2965Spec(z, new RFC2965VersionAttributeHandler(), new BasicPathHandler(), new RFC2965DomainAttributeHandler(), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler());
        this.obsoleteStrict = new RFC2109Spec(z, new RFC2109VersionHandler(), new BasicPathHandler(), new RFC2109DomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler());
        CommonCookieAttributeHandler[] commonCookieAttributeHandlerArr = new CommonCookieAttributeHandler[5];
        commonCookieAttributeHandlerArr[0] = new BasicDomainHandler();
        commonCookieAttributeHandlerArr[1] = new BasicPathHandler();
        commonCookieAttributeHandlerArr[2] = new BasicSecureHandler();
        commonCookieAttributeHandlerArr[3] = new BasicCommentHandler();
        commonCookieAttributeHandlerArr[4] = new BasicExpiresHandler(strArr != null ? (String[]) strArr.clone() : new String[]{"EEE, dd-MMM-yy HH:mm:ss z"});
        this.netscapeDraft = new NetscapeDraftSpec(commonCookieAttributeHandlerArr);
    }

    public DefaultCookieSpec() {
        this(null, false);
    }

    @Override // org.apache.http.cookie.CookieSpec
    public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
        CharArrayBuffer charArrayBuffer;
        ParserCursor parserCursor;
        Args.notNull(header, "Header");
        Args.notNull(cookieOrigin, "Cookie origin");
        HeaderElement[] elements = header.getElements();
        boolean z = false;
        boolean z2 = false;
        for (HeaderElement headerElement : elements) {
            if (headerElement.getParameterByName(ClientCookie.VERSION_ATTR) != null) {
                z2 = true;
            }
            if (headerElement.getParameterByName(ClientCookie.EXPIRES_ATTR) != null) {
                z = true;
            }
        }
        if (z || !z2) {
            NetscapeDraftHeaderParser netscapeDraftHeaderParser = NetscapeDraftHeaderParser.DEFAULT;
            if (header instanceof FormattedHeader) {
                FormattedHeader formattedHeader = (FormattedHeader) header;
                charArrayBuffer = formattedHeader.getBuffer();
                parserCursor = new ParserCursor(formattedHeader.getValuePos(), charArrayBuffer.length());
            } else {
                String value = header.getValue();
                if (value == null) {
                    throw new MalformedCookieException("Header value is null");
                }
                charArrayBuffer = new CharArrayBuffer(value.length());
                charArrayBuffer.append(value);
                parserCursor = new ParserCursor(0, charArrayBuffer.length());
            }
            return this.netscapeDraft.parse(new HeaderElement[]{netscapeDraftHeaderParser.parseHeader(charArrayBuffer, parserCursor)}, cookieOrigin);
        } else if (SM.SET_COOKIE2.equals(header.getName())) {
            return this.strict.parse(elements, cookieOrigin);
        } else {
            return this.obsoleteStrict.parse(elements, cookieOrigin);
        }
    }

    @Override // org.apache.http.cookie.CookieSpec
    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        Args.notNull(cookie, SM.COOKIE);
        Args.notNull(cookieOrigin, "Cookie origin");
        if (cookie.getVersion() > 0) {
            if (cookie instanceof SetCookie2) {
                this.strict.validate(cookie, cookieOrigin);
                return;
            } else {
                this.obsoleteStrict.validate(cookie, cookieOrigin);
                return;
            }
        }
        this.netscapeDraft.validate(cookie, cookieOrigin);
    }

    @Override // org.apache.http.cookie.CookieSpec
    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        Args.notNull(cookie, SM.COOKIE);
        Args.notNull(cookieOrigin, "Cookie origin");
        if (cookie.getVersion() > 0) {
            if (cookie instanceof SetCookie2) {
                return this.strict.match(cookie, cookieOrigin);
            }
            return this.obsoleteStrict.match(cookie, cookieOrigin);
        }
        return this.netscapeDraft.match(cookie, cookieOrigin);
    }

    @Override // org.apache.http.cookie.CookieSpec
    public List<Header> formatCookies(List<Cookie> list) {
        Args.notNull(list, "List of cookies");
        int i = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        boolean z = true;
        for (Cookie cookie : list) {
            if (!(cookie instanceof SetCookie2)) {
                z = false;
            }
            if (cookie.getVersion() < i) {
                i = cookie.getVersion();
            }
        }
        if (i > 0) {
            if (z) {
                return this.strict.formatCookies(list);
            }
            return this.obsoleteStrict.formatCookies(list);
        }
        return this.netscapeDraft.formatCookies(list);
    }

    @Override // org.apache.http.cookie.CookieSpec
    public int getVersion() {
        return this.strict.getVersion();
    }
}
