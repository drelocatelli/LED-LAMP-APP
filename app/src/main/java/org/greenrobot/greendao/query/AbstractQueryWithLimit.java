package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.AbstractDao;

/* loaded from: classes.dex */
abstract class AbstractQueryWithLimit<T> extends AbstractQuery<T> {
    protected final int limitPosition;
    protected final int offsetPosition;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractQueryWithLimit(AbstractDao<T, ?> abstractDao, String str, String[] strArr, int i, int i2) {
        super(abstractDao, str, strArr);
        this.limitPosition = i;
        this.offsetPosition = i2;
    }

    @Override // org.greenrobot.greendao.query.AbstractQuery
    public AbstractQueryWithLimit<T> setParameter(int i, Object obj) {
        if (i >= 0 && (i == this.limitPosition || i == this.offsetPosition)) {
            throw new IllegalArgumentException("Illegal parameter index: " + i);
        }
        return (AbstractQueryWithLimit) super.setParameter(i, obj);
    }

    public AbstractQueryWithLimit<T> setParameter(int i, Date date) {
        return setParameter(i, (Object) (date != null ? Long.valueOf(date.getTime()) : null));
    }

    public AbstractQueryWithLimit<T> setParameter(int i, Boolean bool) {
        return setParameter(i, (Object) (bool != null ? Integer.valueOf(bool.booleanValue() ? 1 : 0) : null));
    }

    public void setLimit(int i) {
        checkThread();
        if (this.limitPosition == -1) {
            throw new IllegalStateException("Limit must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.limitPosition] = Integer.toString(i);
    }

    public void setOffset(int i) {
        checkThread();
        if (this.offsetPosition == -1) {
            throw new IllegalStateException("Offset must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.offsetPosition] = Integer.toString(i);
    }
}
