package com.arvin.request.request.baserequest;


/**
 * Created by arvin on 2017/8/23 0023.
 */

public abstract class BaseCallback {

    public void start(BaseRequest request) {
    }

    public void progress(BaseRequest request) {
    }

    public abstract void done(BaseRequest request, Exception e);

    public static class ProgressInfo {
        public long soFarBytes;
        public long totalBytes;
        public double progress;

        @Override
        public String toString() {
            return "soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",progress:" + progress + "%";
        }
    }

    public void progress(final BaseRequest request, final ProgressInfo info) {
    }

    public static void invokeProgress(final BaseCallback callback, final BaseRequest request, final ProgressInfo progressInfo) {
        if (callback != null) {
            callback.progress(request, progressInfo);
            callback.progress(request);
        }
    }

    public static void invoke(final BaseCallback callback, final BaseRequest request, final Exception e) {
        if (callback != null) {
            callback.done(request, e);
        }
    }

    public static void invokeStart(final BaseCallback callback, final BaseRequest request) {
        if (callback != null) {
            callback.start(request);
        }
    }

}