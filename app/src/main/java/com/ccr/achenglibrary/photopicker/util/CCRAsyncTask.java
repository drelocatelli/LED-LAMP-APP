package com.ccr.achenglibrary.photopicker.util;

import android.os.AsyncTask;

/* loaded from: classes.dex */
public abstract class CCRAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {
    private Callback<Result> mCallback;

    /* loaded from: classes.dex */
    public interface Callback<Result> {
        void onPostExecute(Result result);

        void onTaskCancelled();
    }

    public CCRAsyncTask(Callback<Result> callback) {
        this.mCallback = callback;
    }

    public void cancelTask() {
        if (getStatus() != AsyncTask.Status.FINISHED) {
            cancel(true);
        }
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        Callback<Result> callback = this.mCallback;
        if (callback != null) {
            callback.onPostExecute(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onCancelled() {
        super.onCancelled();
        Callback<Result> callback = this.mCallback;
        if (callback != null) {
            callback.onTaskCancelled();
        }
        this.mCallback = null;
    }
}
