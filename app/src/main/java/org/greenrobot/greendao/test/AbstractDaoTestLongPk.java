package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;

/* loaded from: classes.dex */
public abstract class AbstractDaoTestLongPk<D extends AbstractDao<T, Long>, T> extends AbstractDaoTestSinglePk<D, T, Long> {
    public AbstractDaoTestLongPk(Class<D> cls) {
        super(cls);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.greenrobot.greendao.test.AbstractDaoTestSinglePk
    public Long createRandomPk() {
        return Long.valueOf(this.random.nextLong());
    }

    public void testAssignPk() {
        if (this.daoAccess.isEntityUpdateable()) {
            T createEntity = createEntity(null);
            if (createEntity != null) {
                T createEntity2 = createEntity(null);
                this.dao.insert(createEntity);
                this.dao.insert(createEntity2);
                Long l = (Long) this.daoAccess.getKey(createEntity);
                assertNotNull(l);
                Long l2 = (Long) this.daoAccess.getKey(createEntity2);
                assertNotNull(l2);
                assertFalse(l.equals(l2));
                assertNotNull(this.dao.load(l));
                assertNotNull(this.dao.load(l2));
                return;
            }
            DaoLog.d("Skipping testAssignPk for " + this.daoClass + " (createEntity returned null for null key)");
            return;
        }
        DaoLog.d("Skipping testAssignPk for not updateable " + this.daoClass);
    }
}
