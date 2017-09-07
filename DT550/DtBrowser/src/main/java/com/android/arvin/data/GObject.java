package com.android.arvin.data;


import com.alibaba.fastjson.JSONObject;

public class GObject {

    private JSONObject backend = new JSONObject();
    transient private GObjectCallback callback;

    public static abstract class GObjectCallback {
        public void changed(final String key, GObject object) {
        }
    }

    public void setCallback(GObjectCallback cb) {
        callback = cb;
    }

    public void invokeCallback(final String key) {
        if (callback != null) {
            callback.changed(key, this);
        }
    }

    public GObject() {
        super();
    }

    public GObject(JSONObject obj) {
        backend = obj;
    }

    public boolean isDummyObject() {
        return (backend == null);
    }

    public boolean hasKey(final String key) {
        if (isDummyObject()){
            return false;
        }
        return backend.containsKey(key);
    }

    public String getString(final String key)  {
        if (isDummyObject()){
            return null;
        }
        if (backend.containsKey(key)) {
            return backend.getString(key);
        }
        return null;
    }

    public boolean putString(final String key, final String value) {
        if (isDummyObject()){
            return false;
        }
        backend.put(key, value);
        invokeCallback(key);
        return true;
    }

    public boolean putObject(final String key, Object value) {
        if (isDummyObject()){
            return false;
        }

        backend.put(key, value);
        invokeCallback(key);
        return true;
    }

    public boolean putNonNullObject(final String key, Object value) {
        if (isDummyObject()){
            return false;
        }

        if (value == null) {
            return false;
        }
        return putObject(key, value);
    }

    public Object getObject(final String key) {
        if (isDummyObject()){
            return null;
        }
        return backend.get(key);
    }



    public int getInt(final String key, final int defaultValue)  {
        if (isDummyObject()){
            return defaultValue;
        }
        if (backend.containsKey(key)) {
            return backend.getInteger(key);
        } else {
            return defaultValue;
        }
    }
}