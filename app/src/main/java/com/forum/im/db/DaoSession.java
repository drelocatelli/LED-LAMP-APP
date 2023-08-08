package com.forum.im.db;

import java.util.Map;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class DaoSession extends AbstractDaoSession {
    private final ChatMessageBeanDao chatMessageBeanDao;
    private final DaoConfig chatMessageBeanDaoConfig;

    public DaoSession(Database database, IdentityScopeType identityScopeType, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> map) {
        super(database);
        DaoConfig clone = map.get(ChatMessageBeanDao.class).clone();
        this.chatMessageBeanDaoConfig = clone;
        clone.initIdentityScope(identityScopeType);
        ChatMessageBeanDao chatMessageBeanDao = new ChatMessageBeanDao(clone, this);
        this.chatMessageBeanDao = chatMessageBeanDao;
        registerDao(ChatMessageBean.class, chatMessageBeanDao);
    }

    public void clear() {
        this.chatMessageBeanDaoConfig.clearIdentityScope();
    }

    public ChatMessageBeanDao getChatMessageBeanDao() {
        return this.chatMessageBeanDao;
    }
}
