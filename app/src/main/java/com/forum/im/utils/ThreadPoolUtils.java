package com.forum.im.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class ThreadPoolUtils {
    private static int CORE_POOL_SIZE;
    private static final int CPU_COUNT;
    private static int KEEP_ALIVE_TIME;
    private static int MAX_POOL_SIZE;
    private static ThreadFactory threadFactory;
    private static ThreadPoolExecutor threadPool;
    private static BlockingQueue workQueue;

    static {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        CPU_COUNT = availableProcessors;
        CORE_POOL_SIZE = availableProcessors + 1;
        MAX_POOL_SIZE = (availableProcessors * 2) + 1;
        KEEP_ALIVE_TIME = 10000;
        workQueue = new ArrayBlockingQueue(10);
        threadFactory = new ThreadFactory() { // from class: com.forum.im.utils.ThreadPoolUtils.1
            private final AtomicInteger integer = new AtomicInteger();

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "myThreadPool thread:" + this.integer.getAndIncrement());
            }
        };
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    }

    private ThreadPoolUtils() {
    }

    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public static void execute(FutureTask futureTask) {
        threadPool.execute(futureTask);
    }

    public static void cancel(FutureTask futureTask) {
        futureTask.cancel(true);
    }
}
