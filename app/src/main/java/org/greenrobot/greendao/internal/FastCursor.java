package org.greenrobot.greendao.internal;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

/* loaded from: classes.dex */
public final class FastCursor implements Cursor {
    private final int count;
    private int position;
    private final CursorWindow window;

    @Override // android.database.Cursor
    public Uri getNotificationUri() {
        return null;
    }

    public FastCursor(CursorWindow cursorWindow) {
        this.window = cursorWindow;
        this.count = cursorWindow.getNumRows();
    }

    @Override // android.database.Cursor
    public int getCount() {
        return this.window.getNumRows();
    }

    @Override // android.database.Cursor
    public int getPosition() {
        return this.position;
    }

    @Override // android.database.Cursor
    public boolean move(int i) {
        return moveToPosition(this.position + i);
    }

    @Override // android.database.Cursor
    public boolean moveToPosition(int i) {
        if (i < 0 || i >= this.count) {
            return false;
        }
        this.position = i;
        return true;
    }

    @Override // android.database.Cursor
    public boolean moveToFirst() {
        this.position = 0;
        return this.count > 0;
    }

    @Override // android.database.Cursor
    public boolean moveToLast() {
        int i = this.count;
        if (i > 0) {
            this.position = i - 1;
            return true;
        }
        return false;
    }

    @Override // android.database.Cursor
    public boolean moveToNext() {
        int i = this.position;
        if (i < this.count - 1) {
            this.position = i + 1;
            return true;
        }
        return false;
    }

    @Override // android.database.Cursor
    public boolean moveToPrevious() {
        int i = this.position;
        if (i > 0) {
            this.position = i - 1;
            return true;
        }
        return false;
    }

    @Override // android.database.Cursor
    public boolean isFirst() {
        return this.position == 0;
    }

    @Override // android.database.Cursor
    public boolean isLast() {
        return this.position == this.count - 1;
    }

    @Override // android.database.Cursor
    public boolean isBeforeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean isAfterLast() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getColumnIndex(String str) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getColumnIndexOrThrow(String str) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public String getColumnName(int i) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public String[] getColumnNames() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getColumnCount() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public byte[] getBlob(int i) {
        return this.window.getBlob(this.position, i);
    }

    @Override // android.database.Cursor
    public String getString(int i) {
        return this.window.getString(this.position, i);
    }

    @Override // android.database.Cursor
    public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public short getShort(int i) {
        return this.window.getShort(this.position, i);
    }

    @Override // android.database.Cursor
    public int getInt(int i) {
        return this.window.getInt(this.position, i);
    }

    @Override // android.database.Cursor
    public long getLong(int i) {
        return this.window.getLong(this.position, i);
    }

    @Override // android.database.Cursor
    public float getFloat(int i) {
        return this.window.getFloat(this.position, i);
    }

    @Override // android.database.Cursor
    public double getDouble(int i) {
        return this.window.getDouble(this.position, i);
    }

    @Override // android.database.Cursor
    public boolean isNull(int i) {
        return this.window.isNull(this.position, i);
    }

    @Override // android.database.Cursor
    public void deactivate() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean requery() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void registerContentObserver(ContentObserver contentObserver) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void unregisterContentObserver(ContentObserver contentObserver) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean getWantsAllOnMoveCalls() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public Bundle getExtras() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public Bundle respond(Bundle bundle) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getType(int i) {
        throw new UnsupportedOperationException();
    }
}
