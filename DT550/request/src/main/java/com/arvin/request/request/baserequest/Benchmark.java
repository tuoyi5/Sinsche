package com.arvin.request.request.baserequest;
import android.util.Log;

/**
 * Created by arvin on 2017/8/23 0023.
 */
public class Benchmark {

    private long benchmarkStart = 0;
    private long benchmarkEnd = 0;

    public Benchmark() {
        restart();
    }

    static public long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public void restart() {
        benchmarkStart = getCurrentTimestamp();
    }

    public void report(final String tag) {
        Log.i(tag, String.valueOf(ts()));
    }

    public long ts() {
        benchmarkEnd = getCurrentTimestamp();
        return benchmarkEnd - benchmarkStart;
    }
}
