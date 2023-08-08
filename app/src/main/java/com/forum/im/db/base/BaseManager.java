package com.forum.im.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.forum.im.db.DaoMaster;
import com.forum.im.db.DaoSession;
import com.forum.im.db.interfaces.IDatabase;
import java.util.Collection;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

/* loaded from: classes.dex */
public abstract class BaseManager<M, K> implements IDatabase<M, K> {
    private static final String DEFAULT_DATABASE_NAME = "maxi.db";
    protected static DaoSession daoSession;
    private static DaoMaster.DevOpenHelper mHelper;

    public abstract AbstractDao<M, K> getAbstractDao();

    public static void initOpenHelper(Context context) {
        mHelper = getOpenHelper(context, DEFAULT_DATABASE_NAME);
        openWritableDb();
    }

    public static void initOpenHelper(Context context, String str) {
        mHelper = getOpenHelper(context, str);
        openWritableDb();
    }

    protected static void openReadableDb() throws SQLiteException {
        daoSession = new DaoMaster(getReadableDatabase()).newSession();
    }

    protected static void openWritableDb() throws SQLiteException {
        daoSession = new DaoMaster(getWritableDatabase()).newSession();
    }

    private static SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    private static DaoMaster.DevOpenHelper getOpenHelper(Context context, String str) {
        closeDbConnections();
        return new DaoMaster.DevOpenHelper(context, str, null);
    }

    public static void closeDbConnections() {
        DaoMaster.DevOpenHelper devOpenHelper = mHelper;
        if (devOpenHelper != null) {
            devOpenHelper.close();
            mHelper = null;
        }
        DaoSession daoSession2 = daoSession;
        if (daoSession2 != null) {
            daoSession2.clear();
            daoSession = null;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public void clearDaoSession() {
        DaoSession daoSession2 = daoSession;
        if (daoSession2 != null) {
            daoSession2.clear();
            daoSession = null;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean dropDatabase() {
        try {
            openWritableDb();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean insert(M m) {
        if (m == null) {
            return false;
        }
        try {
            openWritableDb();
            getAbstractDao().insert(m);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean insertOrReplace(M m) {
        if (m == null) {
            return false;
        }
        try {
            openWritableDb();
            getAbstractDao().insertOrReplace(m);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean delete(M m) {
        if (m == null) {
            return false;
        }
        try {
            openWritableDb();
            getAbstractDao().delete(m);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean deleteByKey(K k) {
        try {
            if (k.toString().isEmpty()) {
                return false;
            }
            openWritableDb();
            getAbstractDao().deleteByKey(k);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean deleteByKeyInTx(K... kArr) {
        try {
            openWritableDb();
            getAbstractDao().deleteByKeyInTx(kArr);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean deleteList(List<M> list) {
        if (list != null) {
            try {
                if (list.size() != 0) {
                    openWritableDb();
                    getAbstractDao().deleteInTx(list);
                    return true;
                }
            } catch (SQLiteException unused) {
            }
        }
        return false;
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean deleteAll() {
        try {
            openWritableDb();
            getAbstractDao().deleteAll();
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean update(M m) {
        if (m == null) {
            return false;
        }
        try {
            openWritableDb();
            getAbstractDao().update(m);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean updateInTx(M... mArr) {
        if (mArr == null) {
            return false;
        }
        try {
            openWritableDb();
            getAbstractDao().updateInTx(mArr);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean updateList(List<M> list) {
        if (list != null) {
            try {
                if (list.size() != 0) {
                    openWritableDb();
                    getAbstractDao().updateInTx(list);
                    return true;
                }
            } catch (SQLiteException unused) {
            }
        }
        return false;
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public M selectByPrimaryKey(K k) {
        try {
            openReadableDb();
            return getAbstractDao().load(k);
        } catch (SQLiteException unused) {
            return null;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public List<M> loadAll() {
        openReadableDb();
        return getAbstractDao().loadAll();
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public List<M> loadPages(int i, int i2) {
        openReadableDb();
        return getAbstractDao().queryBuilder().offset(i * i2).limit(i2).list();
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public long getPages(int i) {
        long count = getAbstractDao().queryBuilder().count();
        long j = i;
        long j2 = count / j;
        return (j2 <= 0 || count % j != 0) ? j2 : j2 - 1;
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean refresh(M m) {
        if (m == null) {
            return false;
        }
        try {
            openWritableDb();
            getAbstractDao().refresh(m);
            return true;
        } catch (SQLiteException unused) {
            return false;
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public void runInTx(Runnable runnable) {
        try {
            openWritableDb();
            daoSession.runInTx(runnable);
        } catch (SQLiteException unused) {
        }
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean insertList(List<M> list) {
        if (list != null) {
            try {
                if (list.size() != 0) {
                    openWritableDb();
                    getAbstractDao().insertInTx(list);
                    return true;
                }
            } catch (SQLiteException unused) {
            }
        }
        return false;
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public boolean insertOrReplaceList(List<M> list) {
        if (list != null) {
            try {
                if (list.size() != 0) {
                    openWritableDb();
                    getAbstractDao().insertOrReplaceInTx(list);
                    return true;
                }
            } catch (SQLiteException unused) {
            }
        }
        return false;
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public QueryBuilder<M> getQueryBuilder() {
        openReadableDb();
        return getAbstractDao().queryBuilder();
    }

    @Override // com.forum.im.db.interfaces.IDatabase
    public List<M> queryRaw(String str, String... strArr) {
        openReadableDb();
        return getAbstractDao().queryRaw(str, strArr);
    }

    public Query<M> queryRawCreate(String str, Object... objArr) {
        openReadableDb();
        return getAbstractDao().queryRawCreate(str, objArr);
    }

    public Query<M> queryRawCreateListArgs(String str, Collection<Object> collection) {
        openReadableDb();
        return getAbstractDao().queryRawCreateListArgs(str, collection);
    }
}
