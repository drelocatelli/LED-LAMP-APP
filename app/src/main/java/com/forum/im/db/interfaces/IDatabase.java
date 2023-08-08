package com.forum.im.db.interfaces;

import java.util.List;
import org.greenrobot.greendao.query.QueryBuilder;

/* loaded from: classes.dex */
public interface IDatabase<M, K> {
    void clearDaoSession();

    boolean delete(M m);

    boolean deleteAll();

    boolean deleteByKey(K k);

    boolean deleteByKeyInTx(K... kArr);

    boolean deleteList(List<M> list);

    boolean dropDatabase();

    long getPages(int i);

    QueryBuilder<M> getQueryBuilder();

    boolean insert(M m);

    boolean insertList(List<M> list);

    boolean insertOrReplace(M m);

    boolean insertOrReplaceList(List<M> list);

    List<M> loadAll();

    List<M> loadPages(int i, int i2);

    List<M> queryRaw(String str, String... strArr);

    boolean refresh(M m);

    void runInTx(Runnable runnable);

    M selectByPrimaryKey(K k);

    boolean update(M m);

    boolean updateInTx(M... mArr);

    boolean updateList(List<M> list);
}
