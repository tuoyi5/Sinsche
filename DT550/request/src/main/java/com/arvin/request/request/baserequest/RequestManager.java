package com.arvin.request.request.baserequest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by arvin on 2017/8/23 0023.
 */

public class RequestManager {

    static private final String TAG = RequestManager.class.getSimpleName();
    private boolean debugWakelock = true;
    private PowerManager.WakeLock wakeLock;
    private int wakeLockCounting = 0;
    private List<BaseRequest> requestList;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ExecutorService singleThreadPool;
    private ExecutorService multiThreadPool;

    public RequestManager() {
        initRequestList();
    }

    private void initRequestList() {
        requestList = Collections.synchronizedList(new ArrayList<BaseRequest>());
    }

    public void removeRequest(final BaseRequest request) {
        synchronized (requestList) {
            requestList.remove(request);
        }
    }

    public void addRequest(final BaseRequest request) {
        synchronized (requestList) {
            requestList.add(request);
        }
    }

    public void abortAllRequests() {
        synchronized (requestList) {
            for(BaseRequest request : requestList) {
                request.setAbort();
            }
        }
    }

    public int requestCount() {
        synchronized (requestList) {
            return requestList.size();
        }
    }

    public void acquireWakeLock(Context context) {
        synchronized (this) {
            try {
                if (wakeLock == null) {
                    PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
                }
                wakeLock.acquire();
                ++wakeLockCounting;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void releaseWakeLock() {
        synchronized (this) {
            try {
                if (wakeLock != null) {
                    if (wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                    if (--wakeLockCounting <= 0) {
                        wakeLock = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dumpWakelocks() {
        if (debugWakelock) {
            synchronized (this) {
                if (requestCount() <= 0 && (wakeLock != null || wakeLockCounting > 0)) {
                    Log.w(TAG, "wake lock not released. check wake lock." + wakeLock.toString() + " counting: " + wakeLockCounting);
                }
            }
        }
    }

    public ExecutorService getSingleThreadPool()   {
        if (singleThreadPool == null) {
            singleThreadPool = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setPriority(Thread.MAX_PRIORITY);
                    return t;
                }
            });
        }
        return singleThreadPool;
    }

    public final Runnable generateRunnable(final BaseRequest request) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    request.beforeExecute(RequestManager.this);
                    request.execute(request);
                } catch (Exception exception) {
                    Log.d(TAG, Log.getStackTraceString(exception));
                    request.setException(exception);
                } finally {
                    request.afterExecute(RequestManager.this);
                    dumpWakelocks();
                    removeRequest(request);
                }
            }
        };
        return runnable;
    }

    public Handler getLooperHandler() {
        return handler;
    }

    public boolean submitRequest(final BaseRequest request, final BaseCallback callback) {
        if (!beforeSubmitRequest(request.getContext(), request, callback)) {
            return false;
        }

        final Runnable runnable = generateRunnable(request);
        if (request.isRunInBackground()) {
            getSingleThreadPool().submit(runnable);
        } else {
            runnable.run();
        }
        return true;
    }
    private boolean beforeSubmitRequest(final Context context, final BaseRequest request, final BaseCallback callback) {
        if (request == null) {
            if (callback != null) {
                callback.done(null,  null);
            }
            return false;
        }
        request.setContext(context);
        request.setCallback(callback);
        if (request.isAbortPendingTasks()) {
            abortAllRequests();
        }
        addRequest(request);
        return true;
    }
}