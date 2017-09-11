package com.arvin.request.request.baserequest;

/**
 * Created by arvin on 2017/8/23 0023.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;


public abstract class BaseRequest {
    public static final String RESPONSE_KEY_ERROR = "error";
    private int requestSequence;
    private volatile boolean abort = false;
    private volatile boolean abortPendingTasks = false;
    private boolean useWakeLock = true;
    private boolean runInBackground = true;
    private boolean saveToLocal = true;
    private BaseCallback callback;
    private Context context;
    private Benchmark benchmark;
    private Exception exception;
    //protected ResultCode resultCode;

    static private volatile int globalRequestSequence;
    static private boolean enableBenchmarkDebug = true;

    static public int generateRequestSequence() {
        globalRequestSequence += 1;
        return globalRequestSequence;
    }

    public BaseRequest() {
        requestSequence = generateRequestSequence();
        abort = false;
        abortPendingTasks = false;
        runInBackground = true;
    }

    public void setContext(final Context c) {
        context = c;
    }

    public void setCallback(final BaseCallback c) {
        callback = c;
    }

    public final BaseCallback getCallback() {
        return callback;
    }

    public int getRequestSequence() {
        return requestSequence;
    }

    public final Context getContext() {
        return context;
    }

    public void setAbort() {
        abort = true;
    }

    public boolean isAbort() {
        return abort;
    }

    public void setAbortPendingTasks() {
        abortPendingTasks = true;
    }

    public boolean isAbortPendingTasks() {
        return abortPendingTasks;
    }

    public void setUseWakeLock(boolean use) {
        useWakeLock = use;
    }

    public boolean isUseWakeLock() {
        return useWakeLock;
    }

    static public boolean isEnableBenchmarkDebug() {
        return enableBenchmarkDebug;
    }

    public void benchmarkStart() {
        if (!isEnableBenchmarkDebug()) {
            return;
        }
        benchmark = new Benchmark();
    }

    public long benchmarkEnd() {
        if (!isEnableBenchmarkDebug() || benchmark == null) {
            return 0;
        }
        return (benchmark.ts());
    }

    public void setRunInBackground(boolean b) {
        runInBackground = b;
    }

    public boolean isRunInBackground() {
        return runInBackground;
    }

    public boolean isSaveToLocal() {
        return saveToLocal;
    }

    public void setSaveToLocal(boolean save) {
        saveToLocal = save;
    }

    public void setException(final Exception e) {
        exception = e;
    }

    public Exception getException() {
        return exception;
    }

    public void beforeExecute(final RequestManager parent) {
        parent.acquireWakeLock(getContext());
        benchmarkStart();
        if (isAbort()) {
            return;
        }
        if (callback == null) {
            return;
        }
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callback.start(BaseRequest.this);
            }
        };
        if (isRunInBackground()) {
            parent.getLooperHandler().post(runnable);
        } else {
            runnable.run();
        }
    }

    public abstract void execute(final BaseRequest request) throws Exception;

    public void afterExecute(final RequestManager parent) {
        if (exception != null) {
            exception.printStackTrace();
        }
        benchmarkEnd();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.done(BaseRequest.this, getException());
                }
            }
        };
        if (isRunInBackground()) {
            parent.getLooperHandler().post(runnable);
        } else {
            runnable.run();
        }
        parent.releaseWakeLock();
    }

    public void dumpMessage(final String tag, final String message) {
            Log.d(tag, message);
    }

    public void dumpMessage(final String tag, Throwable throwable, JSONObject errorResponse) {
        if (throwable != null && throwable.getMessage() != null) {
            dumpMessage(tag, throwable.getMessage());
        }
        if (errorResponse != null) {
            dumpMessage(tag, errorResponse.toString());
        }
    }
}