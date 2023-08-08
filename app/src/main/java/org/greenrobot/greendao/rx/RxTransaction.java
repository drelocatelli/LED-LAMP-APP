package org.greenrobot.greendao.rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.AbstractDaoSession;
import rx.Observable;
import rx.Scheduler;

/* loaded from: classes.dex */
public class RxTransaction extends RxBase {
    private final AbstractDaoSession daoSession;

    @Override // org.greenrobot.greendao.rx.RxBase
    public /* bridge */ /* synthetic */ Scheduler getScheduler() {
        return super.getScheduler();
    }

    public RxTransaction(AbstractDaoSession abstractDaoSession) {
        this.daoSession = abstractDaoSession;
    }

    public RxTransaction(AbstractDaoSession abstractDaoSession, Scheduler scheduler) {
        super(scheduler);
        this.daoSession = abstractDaoSession;
    }

    public Observable<Void> run(final Runnable runnable) {
        return wrap(new Callable<Void>() { // from class: org.greenrobot.greendao.rx.RxTransaction.1
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                RxTransaction.this.daoSession.runInTx(runnable);
                return null;
            }
        });
    }

    public <T> Observable<T> call(final Callable<T> callable) {
        return wrap(new Callable<T>() { // from class: org.greenrobot.greendao.rx.RxTransaction.2
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                return (T) RxTransaction.this.daoSession.callInTx(callable);
            }
        });
    }

    public AbstractDaoSession getDaoSession() {
        return this.daoSession;
    }
}
