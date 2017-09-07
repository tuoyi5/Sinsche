package com.android.arvin.ui;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.arvin.data.GObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by arvin on 2017/9/7 0007.
 */

public final class ContentItemView extends LinearLayout {

    private GObject dataObject;
    private int layoutResourceId;
    private Map<String, Integer> dataLayoutMapping;
    private ArrayList<Integer> styleLayoutList;
    private double mWeight = 0.0;
    private HyphenCallback hyphenCallback;

    private ContentItemView(LayoutInflater inflater, GObject data, int layoutResource, Map<String, Integer> mapping, ArrayList<Integer> styleLayoutList) {
        super(inflater.getContext());
        setBackgroundResource(getDefaultBackgroundResource());
        init(inflater, data, layoutResource, mapping, styleLayoutList);
    }

    public static int getDefaultBackgroundResource() {
        return -1;
    }

    private void init(LayoutInflater inflater, GObject data, int resId, Map<String, Integer> mapping, ArrayList<Integer> styleList) {
        dataObject = data;
        layoutResourceId = getLayoutResourceId(dataObject, resId);
        dataLayoutMapping = getLayoutMapping(dataObject, mapping);
        styleLayoutList = styleList;
        removeAllViews();
        inflateView(inflater, layoutResourceId);
        dataObject.setCallback(new GObject.GObjectCallback() {
            @Override
            public void changed(String key, GObject object) {
                ContentItemView.this.update();
            }
        });
        update();
    }

    public void update() {
        if (getChildCount() <= 0) {
            return;
        }
        View parent = getChildAt(0);
    }

    private void inflateView(LayoutInflater inflater, int resourceId) {
        if (resourceId <= 0) {
            return;
        }
        View view = inflater.inflate(resourceId, this, false);
        ((LayoutParams) view.getLayoutParams()).gravity = Gravity.CENTER;
        addView(view);
        mWeight = ((LayoutParams) view.getLayoutParams()).weight;
    }


    public void setHyphenCallback(HyphenCallback hyphenCallback) {
        this.hyphenCallback = hyphenCallback;
    }

    public abstract static class HyphenCallback {
        public ArrayList<String> keyList;

        protected HyphenCallback(ArrayList<String> keyList) {
            this.keyList = keyList;
        }

        public abstract void onUpdateHyphenTittleView(View view, Object value);
    }



    static private int getLayoutResourceId(GObject object, int fallback) {
        int layoutOverride = object.getInt("~~~~~~~", -1);
        if (layoutOverride > 0) {
            return layoutOverride;
        }
        return fallback;
    }

    static private Map<String, Integer> getLayoutMapping(GObject object, Map<String, Integer> fallback) {
        Object value = object.getObject("~~~~~~~~");
        if (value != null && value instanceof Map) {
            return (Map<String, Integer>) (value);
        }
        return fallback;
    }

    public static ContentItemView create(LayoutInflater inflater, GObject data, int layoutResource, Map<String, Integer> mapping, ArrayList<Integer> styleLayoutList) {
        int layoutOverride = getLayoutResourceId(data, layoutResource);
        Map<String, Integer> mappingOverride = getLayoutMapping(data, mapping);
        return new ContentItemView(inflater, data, layoutOverride, mappingOverride, styleLayoutList);
    }


}
