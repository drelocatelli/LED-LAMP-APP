package com.forum.im.db;

import com.forum.im.db.base.BaseManager;
import org.greenrobot.greendao.AbstractDao;

/* loaded from: classes.dex */
public class ChatDbManager extends BaseManager<ChatMessageBean, Long> {
    @Override // com.forum.im.db.base.BaseManager
    public AbstractDao<ChatMessageBean, Long> getAbstractDao() {
        return daoSession.getChatMessageBeanDao();
    }
}
