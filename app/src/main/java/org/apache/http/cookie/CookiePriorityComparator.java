package org.apache.http.cookie;

import java.util.Comparator;
import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

/* loaded from: classes.dex */
public class CookiePriorityComparator implements Comparator<Cookie> {
    public static final CookiePriorityComparator INSTANCE = new CookiePriorityComparator();

    private int getPathLength(Cookie cookie) {
        String path = cookie.getPath();
        if (path != null) {
            return path.length();
        }
        return 1;
    }

    @Override // java.util.Comparator
    public int compare(Cookie cookie, Cookie cookie2) {
        int pathLength = getPathLength(cookie2) - getPathLength(cookie);
        if (pathLength == 0 && (cookie instanceof BasicClientCookie) && (cookie2 instanceof BasicClientCookie)) {
            Date creationDate = ((BasicClientCookie) cookie).getCreationDate();
            Date creationDate2 = ((BasicClientCookie) cookie2).getCreationDate();
            if (creationDate != null && creationDate2 != null) {
                return (int) (creationDate.getTime() - creationDate2.getTime());
            }
        }
        return pathLength;
    }
}
