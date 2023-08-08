package com.forum.im.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class ChatMessageBeanDao extends AbstractDao<ChatMessageBean, Long> {
    public static final String TABLENAME = "CHAT_MESSAGE_BEAN";

    /* loaded from: classes.dex */
    public static class Properties {
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property UserId = new Property(1, String.class, "UserId", false, "UserId");
        public static final Property UserName = new Property(2, String.class, "UserName", false, "UserName");
        public static final Property UserHeadIcon = new Property(3, String.class, "UserHeadIcon", false, "UserHeadIcon");
        public static final Property UserContent = new Property(4, String.class, "UserContent", false, "UserContent");
        public static final Property Time = new Property(5, String.class, "time", false, "time");
        public static final Property Type = new Property(6, Integer.TYPE, "type", false, "type");
        public static final Property Messagetype = new Property(7, Integer.TYPE, "messagetype", false, "messagetype");
        public static final Property UserVoiceTime = new Property(8, Float.TYPE, "UserVoiceTime", false, "UserVoiceTime");
        public static final Property UserVoicePath = new Property(9, String.class, "UserVoicePath", false, "UserVoicePath");
        public static final Property UserVoiceUrl = new Property(10, String.class, "UserVoiceUrl", false, "UserVoiceUrl");
        public static final Property SendState = new Property(11, Integer.TYPE, "sendState", false, "sendState");
        public static final Property ImageUrl = new Property(12, String.class, "imageUrl", false, "imageUrl");
        public static final Property ImageIconUrl = new Property(13, String.class, "imageIconUrl", false, "imageIconUrl");
        public static final Property ImageLocal = new Property(14, String.class, "imageLocal", false, "imageLocal");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    public final boolean isEntityUpdateable() {
        return true;
    }

    public ChatMessageBeanDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public ChatMessageBeanDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(Database database, boolean z) {
        String str = z ? "IF NOT EXISTS " : "";
        database.execSQL("CREATE TABLE " + str + "\"CHAT_MESSAGE_BEAN\" (\"_id\" INTEGER PRIMARY KEY ,\"UserId\" TEXT,\"UserName\" TEXT,\"UserHeadIcon\" TEXT,\"UserContent\" TEXT,\"time\" TEXT,\"type\" INTEGER NOT NULL ,\"messagetype\" INTEGER NOT NULL ,\"UserVoiceTime\" REAL NOT NULL ,\"UserVoicePath\" TEXT,\"UserVoiceUrl\" TEXT,\"sendState\" INTEGER NOT NULL ,\"imageUrl\" TEXT,\"imageIconUrl\" TEXT,\"imageLocal\" TEXT);");
    }

    public static void dropTable(Database database, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(z ? "IF EXISTS " : "");
        sb.append("\"CHAT_MESSAGE_BEAN\"");
        database.execSQL(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    public final void bindValues(DatabaseStatement databaseStatement, ChatMessageBean chatMessageBean) {
        databaseStatement.clearBindings();
        Long id = chatMessageBean.getId();
        if (id != null) {
            databaseStatement.bindLong(1, id.longValue());
        }
        String userId = chatMessageBean.getUserId();
        if (userId != null) {
            databaseStatement.bindString(2, userId);
        }
        String userName = chatMessageBean.getUserName();
        if (userName != null) {
            databaseStatement.bindString(3, userName);
        }
        String userHeadIcon = chatMessageBean.getUserHeadIcon();
        if (userHeadIcon != null) {
            databaseStatement.bindString(4, userHeadIcon);
        }
        String userContent = chatMessageBean.getUserContent();
        if (userContent != null) {
            databaseStatement.bindString(5, userContent);
        }
        String time = chatMessageBean.getTime();
        if (time != null) {
            databaseStatement.bindString(6, time);
        }
        databaseStatement.bindLong(7, chatMessageBean.getType());
        databaseStatement.bindLong(8, chatMessageBean.getMessagetype());
        databaseStatement.bindDouble(9, chatMessageBean.getUserVoiceTime());
        String userVoicePath = chatMessageBean.getUserVoicePath();
        if (userVoicePath != null) {
            databaseStatement.bindString(10, userVoicePath);
        }
        String userVoiceUrl = chatMessageBean.getUserVoiceUrl();
        if (userVoiceUrl != null) {
            databaseStatement.bindString(11, userVoiceUrl);
        }
        databaseStatement.bindLong(12, chatMessageBean.getSendState());
        String imageUrl = chatMessageBean.getImageUrl();
        if (imageUrl != null) {
            databaseStatement.bindString(13, imageUrl);
        }
        String imageIconUrl = chatMessageBean.getImageIconUrl();
        if (imageIconUrl != null) {
            databaseStatement.bindString(14, imageIconUrl);
        }
        String imageLocal = chatMessageBean.getImageLocal();
        if (imageLocal != null) {
            databaseStatement.bindString(15, imageLocal);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    public final void bindValues(SQLiteStatement sQLiteStatement, ChatMessageBean chatMessageBean) {
        sQLiteStatement.clearBindings();
        Long id = chatMessageBean.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String userId = chatMessageBean.getUserId();
        if (userId != null) {
            sQLiteStatement.bindString(2, userId);
        }
        String userName = chatMessageBean.getUserName();
        if (userName != null) {
            sQLiteStatement.bindString(3, userName);
        }
        String userHeadIcon = chatMessageBean.getUserHeadIcon();
        if (userHeadIcon != null) {
            sQLiteStatement.bindString(4, userHeadIcon);
        }
        String userContent = chatMessageBean.getUserContent();
        if (userContent != null) {
            sQLiteStatement.bindString(5, userContent);
        }
        String time = chatMessageBean.getTime();
        if (time != null) {
            sQLiteStatement.bindString(6, time);
        }
        sQLiteStatement.bindLong(7, chatMessageBean.getType());
        sQLiteStatement.bindLong(8, chatMessageBean.getMessagetype());
        sQLiteStatement.bindDouble(9, chatMessageBean.getUserVoiceTime());
        String userVoicePath = chatMessageBean.getUserVoicePath();
        if (userVoicePath != null) {
            sQLiteStatement.bindString(10, userVoicePath);
        }
        String userVoiceUrl = chatMessageBean.getUserVoiceUrl();
        if (userVoiceUrl != null) {
            sQLiteStatement.bindString(11, userVoiceUrl);
        }
        sQLiteStatement.bindLong(12, chatMessageBean.getSendState());
        String imageUrl = chatMessageBean.getImageUrl();
        if (imageUrl != null) {
            sQLiteStatement.bindString(13, imageUrl);
        }
        String imageIconUrl = chatMessageBean.getImageIconUrl();
        if (imageIconUrl != null) {
            sQLiteStatement.bindString(14, imageIconUrl);
        }
        String imageLocal = chatMessageBean.getImageLocal();
        if (imageLocal != null) {
            sQLiteStatement.bindString(15, imageLocal);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.greenrobot.greendao.AbstractDao
    public Long readKey(Cursor cursor, int i) {
        int i2 = i + 0;
        if (cursor.isNull(i2)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(i2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.greenrobot.greendao.AbstractDao
    public ChatMessageBean readEntity(Cursor cursor, int i) {
        int i2 = i + 0;
        int i3 = i + 1;
        int i4 = i + 2;
        int i5 = i + 3;
        int i6 = i + 4;
        int i7 = i + 5;
        int i8 = i + 9;
        int i9 = i + 10;
        int i10 = i + 12;
        int i11 = i + 13;
        int i12 = i + 14;
        return new ChatMessageBean(cursor.isNull(i2) ? null : Long.valueOf(cursor.getLong(i2)), cursor.isNull(i3) ? null : cursor.getString(i3), cursor.isNull(i4) ? null : cursor.getString(i4), cursor.isNull(i5) ? null : cursor.getString(i5), cursor.isNull(i6) ? null : cursor.getString(i6), cursor.isNull(i7) ? null : cursor.getString(i7), cursor.getInt(i + 6), cursor.getInt(i + 7), cursor.getFloat(i + 8), cursor.isNull(i8) ? null : cursor.getString(i8), cursor.isNull(i9) ? null : cursor.getString(i9), cursor.getInt(i + 11), cursor.isNull(i10) ? null : cursor.getString(i10), cursor.isNull(i11) ? null : cursor.getString(i11), cursor.isNull(i12) ? null : cursor.getString(i12));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public void readEntity(Cursor cursor, ChatMessageBean chatMessageBean, int i) {
        int i2 = i + 0;
        chatMessageBean.setId(cursor.isNull(i2) ? null : Long.valueOf(cursor.getLong(i2)));
        int i3 = i + 1;
        chatMessageBean.setUserId(cursor.isNull(i3) ? null : cursor.getString(i3));
        int i4 = i + 2;
        chatMessageBean.setUserName(cursor.isNull(i4) ? null : cursor.getString(i4));
        int i5 = i + 3;
        chatMessageBean.setUserHeadIcon(cursor.isNull(i5) ? null : cursor.getString(i5));
        int i6 = i + 4;
        chatMessageBean.setUserContent(cursor.isNull(i6) ? null : cursor.getString(i6));
        int i7 = i + 5;
        chatMessageBean.setTime(cursor.isNull(i7) ? null : cursor.getString(i7));
        chatMessageBean.setType(cursor.getInt(i + 6));
        chatMessageBean.setMessagetype(cursor.getInt(i + 7));
        chatMessageBean.setUserVoiceTime(cursor.getFloat(i + 8));
        int i8 = i + 9;
        chatMessageBean.setUserVoicePath(cursor.isNull(i8) ? null : cursor.getString(i8));
        int i9 = i + 10;
        chatMessageBean.setUserVoiceUrl(cursor.isNull(i9) ? null : cursor.getString(i9));
        chatMessageBean.setSendState(cursor.getInt(i + 11));
        int i10 = i + 12;
        chatMessageBean.setImageUrl(cursor.isNull(i10) ? null : cursor.getString(i10));
        int i11 = i + 13;
        chatMessageBean.setImageIconUrl(cursor.isNull(i11) ? null : cursor.getString(i11));
        int i12 = i + 14;
        chatMessageBean.setImageLocal(cursor.isNull(i12) ? null : cursor.getString(i12));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    public final Long updateKeyAfterInsert(ChatMessageBean chatMessageBean, long j) {
        chatMessageBean.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public Long getKey(ChatMessageBean chatMessageBean) {
        if (chatMessageBean != null) {
            return chatMessageBean.getId();
        }
        return null;
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public boolean hasKey(ChatMessageBean chatMessageBean) {
        return chatMessageBean.getId() != null;
    }
}
