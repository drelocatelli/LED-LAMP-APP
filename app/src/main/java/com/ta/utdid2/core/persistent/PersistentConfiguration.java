package com.ta.utdid2.core.persistent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.ta.utdid2.android.utils.StringUtils;
import com.ta.utdid2.core.persistent.MySharedPreferences;
import java.io.File;
import java.util.Map;

/* loaded from: classes.dex */
public class PersistentConfiguration {
    private static final String KEY_TIMESTAMP = "t";
    private static final String KEY_TIMESTAMP2 = "t2";
    private boolean mCanRead;
    private boolean mCanWrite;
    private String mConfigName;
    private Context mContext;
    private String mFolderName;
    private boolean mIsLessMode;
    private boolean mIsSafety;
    private MySharedPreferences mMySP;
    private SharedPreferences mSp;
    private TransactionXMLFile mTxf;
    private SharedPreferences.Editor mEditor = null;
    private MySharedPreferences.MyEditor mMyEditor = null;

    /* JADX WARN: Removed duplicated region for block: B:101:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0154 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0170 A[Catch: Exception -> 0x017a, TRY_LEAVE, TryCatch #3 {Exception -> 0x017a, blocks: (B:79:0x016c, B:81:0x0170), top: B:94:0x016c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PersistentConfiguration(Context context, String str, String str2, boolean z, boolean z2) {
        long j;
        long j2;
        boolean z3;
        SharedPreferences sharedPreferences;
        MySharedPreferences mySharedPreferences;
        this.mCanRead = false;
        this.mCanWrite = false;
        this.mSp = null;
        this.mMySP = null;
        this.mTxf = null;
        this.mIsSafety = z;
        this.mIsLessMode = z2;
        this.mConfigName = str2;
        this.mFolderName = str;
        this.mContext = context;
        if (context != null) {
            SharedPreferences sharedPreferences2 = context.getSharedPreferences(str2, 0);
            this.mSp = sharedPreferences2;
            j = sharedPreferences2.getLong(KEY_TIMESTAMP, 0L);
        } else {
            j = 0;
        }
        String externalStorageState = Environment.getExternalStorageState();
        if (StringUtils.isEmpty(externalStorageState)) {
            this.mCanWrite = false;
            this.mCanRead = false;
        } else if (externalStorageState.equals("mounted")) {
            this.mCanWrite = true;
            this.mCanRead = true;
        } else if (externalStorageState.equals("mounted_ro")) {
            this.mCanRead = true;
            this.mCanWrite = false;
        } else {
            this.mCanWrite = false;
            this.mCanRead = false;
        }
        try {
            if ((this.mCanRead || this.mCanWrite) && context != null && !StringUtils.isEmpty(str)) {
                TransactionXMLFile transactionXMLFile = getTransactionXMLFile(str);
                this.mTxf = transactionXMLFile;
                if (transactionXMLFile != null) {
                    try {
                        MySharedPreferences mySharedPreferences2 = transactionXMLFile.getMySharedPreferences(str2, 0);
                        this.mMySP = mySharedPreferences2;
                        j2 = mySharedPreferences2.getLong(KEY_TIMESTAMP, 0L);
                    } catch (Exception unused) {
                    }
                    if (z2) {
                        long j3 = this.mSp.getLong(KEY_TIMESTAMP2, 0L);
                        try {
                            j2 = this.mMySP.getLong(KEY_TIMESTAMP2, 0L);
                            if (j3 < j2 && j3 > 0) {
                                copySPToMySP(this.mSp, this.mMySP);
                                this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                            } else if (j3 > j2 && j2 > 0) {
                                copyMySPToSP(this.mMySP, this.mSp);
                                this.mSp = context.getSharedPreferences(str2, 0);
                            } else if (j3 == 0 && j2 > 0) {
                                copyMySPToSP(this.mMySP, this.mSp);
                                this.mSp = context.getSharedPreferences(str2, 0);
                            } else if (j2 == 0 && j3 > 0) {
                                copySPToMySP(this.mSp, this.mMySP);
                                this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                            } else if (j3 == j2) {
                                copySPToMySP(this.mSp, this.mMySP);
                                this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                            }
                        } catch (Exception unused2) {
                        }
                        j = j3;
                    } else if (j > j2) {
                        copySPToMySP(this.mSp, this.mMySP);
                        this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                    } else if (j >= j2) {
                        if (j == j2) {
                            copySPToMySP(this.mSp, this.mMySP);
                            this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                        }
                        if (j == j2 || (j == 0 && j2 == 0)) {
                            long currentTimeMillis = System.currentTimeMillis();
                            z3 = this.mIsLessMode;
                            if (z3 || (z3 && j == 0 && j2 == 0)) {
                                sharedPreferences = this.mSp;
                                if (sharedPreferences != null) {
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putLong(KEY_TIMESTAMP2, currentTimeMillis);
                                    edit.commit();
                                }
                                mySharedPreferences = this.mMySP;
                                if (mySharedPreferences != null) {
                                    MySharedPreferences.MyEditor edit2 = mySharedPreferences.edit();
                                    edit2.putLong(KEY_TIMESTAMP2, currentTimeMillis);
                                    edit2.commit();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    } else {
                        copyMySPToSP(this.mMySP, this.mSp);
                        this.mSp = context.getSharedPreferences(str2, 0);
                    }
                    if (j == j2) {
                    }
                    long currentTimeMillis2 = System.currentTimeMillis();
                    z3 = this.mIsLessMode;
                    if (z3) {
                    }
                    sharedPreferences = this.mSp;
                    if (sharedPreferences != null) {
                    }
                    mySharedPreferences = this.mMySP;
                    if (mySharedPreferences != null) {
                    }
                }
            }
            mySharedPreferences = this.mMySP;
            if (mySharedPreferences != null) {
            }
        } catch (Exception unused3) {
            return;
        }
        j2 = 0;
        if (j == j2) {
        }
        long currentTimeMillis22 = System.currentTimeMillis();
        z3 = this.mIsLessMode;
        if (z3) {
        }
        sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
        }
    }

    private TransactionXMLFile getTransactionXMLFile(String str) {
        File rootFolder = getRootFolder(str);
        if (rootFolder != null) {
            TransactionXMLFile transactionXMLFile = new TransactionXMLFile(rootFolder.getAbsolutePath());
            this.mTxf = transactionXMLFile;
            return transactionXMLFile;
        }
        return null;
    }

    private File getRootFolder(String str) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            File file = new File(String.format("%s%s%s", externalStorageDirectory.getAbsolutePath(), File.separator, str));
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        return null;
    }

    private void copySPToMySP(SharedPreferences sharedPreferences, MySharedPreferences mySharedPreferences) {
        MySharedPreferences.MyEditor edit;
        if (sharedPreferences == null || mySharedPreferences == null || (edit = mySharedPreferences.edit()) == null) {
            return;
        }
        edit.clear();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                edit.putString(key, (String) value);
            } else if (value instanceof Integer) {
                edit.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                edit.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                edit.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Boolean) {
                edit.putBoolean(key, ((Boolean) value).booleanValue());
            }
        }
        edit.commit();
    }

    private void copyMySPToSP(MySharedPreferences mySharedPreferences, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit;
        if (mySharedPreferences == null || sharedPreferences == null || (edit = sharedPreferences.edit()) == null) {
            return;
        }
        edit.clear();
        for (Map.Entry<String, ?> entry : mySharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                edit.putString(key, (String) value);
            } else if (value instanceof Integer) {
                edit.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                edit.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                edit.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Boolean) {
                edit.putBoolean(key, ((Boolean) value).booleanValue());
            }
        }
        edit.commit();
    }

    private boolean checkSDCardXMLFile() {
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            boolean checkFile = mySharedPreferences.checkFile();
            if (!checkFile) {
                commit();
            }
            return checkFile;
        }
        return false;
    }

    private void initEditor() {
        MySharedPreferences mySharedPreferences;
        SharedPreferences sharedPreferences;
        if (this.mEditor == null && (sharedPreferences = this.mSp) != null) {
            this.mEditor = sharedPreferences.edit();
        }
        if (this.mCanWrite && this.mMyEditor == null && (mySharedPreferences = this.mMySP) != null) {
            this.mMyEditor = mySharedPreferences.edit();
        }
        checkSDCardXMLFile();
    }

    public void putInt(String str, int i) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putInt(str, i);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putInt(str, i);
        }
    }

    public void putLong(String str, long j) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putLong(str, j);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putLong(str, j);
        }
    }

    public void putBoolean(String str, boolean z) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putBoolean(str, z);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putBoolean(str, z);
        }
    }

    public void putFloat(String str, float f) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putFloat(str, f);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putFloat(str, f);
        }
    }

    public void putString(String str, String str2) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putString(str, str2);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putString(str, str2);
        }
    }

    public void remove(String str) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.remove(str);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.remove(str);
        }
    }

    public void reload() {
        Context context;
        if (this.mSp != null && (context = this.mContext) != null) {
            this.mSp = context.getSharedPreferences(this.mConfigName, 0);
        }
        String externalStorageState = Environment.getExternalStorageState();
        if (StringUtils.isEmpty(externalStorageState)) {
            return;
        }
        if (externalStorageState.equals("mounted") || (externalStorageState.equals("mounted_ro") && this.mMySP != null)) {
            try {
                TransactionXMLFile transactionXMLFile = this.mTxf;
                if (transactionXMLFile != null) {
                    this.mMySP = transactionXMLFile.getMySharedPreferences(this.mConfigName, 0);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void clear() {
        initEditor();
        long currentTimeMillis = System.currentTimeMillis();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.clear();
            this.mEditor.putLong(KEY_TIMESTAMP, currentTimeMillis);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.clear();
            this.mMyEditor.putLong(KEY_TIMESTAMP, currentTimeMillis);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean commit() {
        boolean z;
        String externalStorageState;
        Context context;
        long currentTimeMillis = System.currentTimeMillis();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            if (!this.mIsLessMode && this.mSp != null) {
                editor.putLong(KEY_TIMESTAMP, currentTimeMillis);
            }
            if (!this.mEditor.commit()) {
                z = false;
                if (this.mSp != null && (context = this.mContext) != null) {
                    this.mSp = context.getSharedPreferences(this.mConfigName, 0);
                }
                externalStorageState = Environment.getExternalStorageState();
                if (!StringUtils.isEmpty(externalStorageState)) {
                    if (externalStorageState.equals("mounted")) {
                        if (this.mMySP == null) {
                            TransactionXMLFile transactionXMLFile = getTransactionXMLFile(this.mFolderName);
                            if (transactionXMLFile != null) {
                                MySharedPreferences mySharedPreferences = transactionXMLFile.getMySharedPreferences(this.mConfigName, 0);
                                this.mMySP = mySharedPreferences;
                                if (!this.mIsLessMode) {
                                    copySPToMySP(this.mSp, mySharedPreferences);
                                } else {
                                    copyMySPToSP(mySharedPreferences, this.mSp);
                                }
                                this.mMyEditor = this.mMySP.edit();
                            }
                        } else {
                            MySharedPreferences.MyEditor myEditor = this.mMyEditor;
                            if (myEditor != null && !myEditor.commit()) {
                                z = false;
                            }
                        }
                    }
                    if (externalStorageState.equals("mounted") || (externalStorageState.equals("mounted_ro") && this.mMySP != null)) {
                        try {
                            TransactionXMLFile transactionXMLFile2 = this.mTxf;
                            if (transactionXMLFile2 != null) {
                                this.mMySP = transactionXMLFile2.getMySharedPreferences(this.mConfigName, 0);
                            }
                        } catch (Exception unused) {
                        }
                    }
                }
                return z;
            }
        }
        z = true;
        if (this.mSp != null) {
            this.mSp = context.getSharedPreferences(this.mConfigName, 0);
        }
        externalStorageState = Environment.getExternalStorageState();
        if (!StringUtils.isEmpty(externalStorageState)) {
        }
        return z;
    }

    public String getString(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            String string = sharedPreferences.getString(str, "");
            if (!StringUtils.isEmpty(string)) {
                return string;
            }
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        return mySharedPreferences != null ? mySharedPreferences.getString(str, "") : "";
    }

    public int getInt(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(str, 0);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getInt(str, 0);
        }
        return 0;
    }

    public long getLong(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(str, 0L);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getLong(str, 0L);
        }
        return 0L;
    }

    public float getFloat(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(str, 0.0f);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getFloat(str, 0.0f);
        }
        return 0.0f;
    }

    public boolean getBoolean(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(str, false);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getBoolean(str, false);
        }
        return false;
    }

    public Map<String, ?> getAll() {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getAll();
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getAll();
        }
        return null;
    }
}
