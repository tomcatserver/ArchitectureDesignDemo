package com.example.common.views;


import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类。
 */
public class AppExecutors {
    private static final int SCHEDULED_CORE_POOL_SIZE = 16; //定时的任务核心线程数
    private static final int DISK_IO_CORE_POOL_SIZE = 1; //磁盘IO的核心线程数
    private static final int NETWORK_CORE_POOL_SIZE = 3; //网络IO的核心线程数

    private static final int DISK_IO_MAXI_MUM_POOL_SIZE = 1; //磁盘IO的最大线程数
    private static final int NETWORK_MAXI_MUM_POOL_SIZE = 6; //网络IO的最大线程数

    private static final int DISK_IO_KEEP_ALIVE_TIME = 0; //磁盘IO 线程池维护线程所允许的空闲时间
    private static final int NETWORK_KEEP_ALIVE_TIME = 1000; //网络IO的 线程池维护线程所允许的空闲时间

    private static final int DISK_QUEUE_CAPACITY = 1024; //磁盘线程缓冲队列
    private static final int NETWORK_QUEUE_CAPACITY = 6; //网络线程缓冲队列

    private static final String TAG = "AppExecutors";
    /**
     * 磁盘IO线程池
     **/
    private final ExecutorService mDiskIO;
    /**
     * 网络IO线程池
     **/
    private final ExecutorService mNetworkIO;
    /**
     * UI线程
     **/
    private final Executor mMainThread;
    /**
     * 定时任务线程池
     **/
    private final ScheduledExecutorService mScheduledExecutor;

    private volatile static AppExecutors sAppExecutors;

    public static AppExecutors getInstance() {
        if (sAppExecutors == null) {
            synchronized (AppExecutors.class) {
                if (sAppExecutors == null) {
                    sAppExecutors = new AppExecutors();
                }
            }
        }
        return sAppExecutors;
    }

    public AppExecutors(ExecutorService diskIO, ExecutorService networkIO, Executor mainThread, ScheduledExecutorService scheduledExecutor) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
        this.mScheduledExecutor = scheduledExecutor;
    }

    public AppExecutors() {
        this(diskIoExecutor(), networkExecutor(), new MainThreadExecutor(), scheduledThreadPoolExecutor());
    }

    /**
     * 定时(延时)任务线程池
     * 替代Timer,执行定时任务,延时任务
     */
    public ScheduledExecutorService scheduledExecutor() {
        YWLogUtil.e(TAG, "scheduledExecutor----");
        return mScheduledExecutor;
    }

    /**
     * 磁盘IO线程池（单线程）
     * 和磁盘操作有关的进行使用此线程(如读写数据库,读写文件)
     * 禁止延迟,避免等待
     * 此线程不用考虑同步问题
     */
    public ExecutorService diskIO() {
        YWLogUtil.e(TAG, "diskIO----");
        return mDiskIO;
    }

    /**
     * 网络IO线程池
     * 网络请求,异步任务等适用此线程
     * 不建议在这个线程 sleep 或者 wait
     */
    public ExecutorService networkIO() {
        YWLogUtil.e(TAG, "networkIO----");
        return mNetworkIO;
    }

    /**
     * UI线程
     * Android 的MainThread
     * UI线程不能做的事情这个都不能做
     */
    public Executor mainThread() {
        YWLogUtil.e(TAG, "mainThread----");
        return mMainThread;
    }

    private static ScheduledExecutorService scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(SCHEDULED_CORE_POOL_SIZE, r -> new Thread(r, "scheduled_executor_" + System.currentTimeMillis()), (r, executor) -> YWLogUtil.e(TAG, "rejectedExecution: scheduled executor queue overflow"));
    }

    private static ExecutorService diskIoExecutor() {
        return new ThreadPoolExecutor(DISK_IO_CORE_POOL_SIZE, DISK_IO_MAXI_MUM_POOL_SIZE, DISK_IO_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(DISK_QUEUE_CAPACITY), r -> new Thread(r, "disk_executor_" + System.currentTimeMillis()), (r, executor) -> YWLogUtil.e(TAG, "rejectedExecution: disk io executor queue overflow"));
    }

    private static ExecutorService networkExecutor() {
        return new ThreadPoolExecutor(NETWORK_CORE_POOL_SIZE, NETWORK_MAXI_MUM_POOL_SIZE, NETWORK_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(NETWORK_QUEUE_CAPACITY), r -> new Thread(r, "network_executor_" + System.currentTimeMillis()), (r, executor) -> YWLogUtil.e(TAG, "rejectedExecution: network executor queue overflow"));
    }


    private static class MainThreadExecutor implements Executor {
        private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mMainThreadHandler.post(command);
        }
    }
}
