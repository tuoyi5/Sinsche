package com.android.arvin.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridLayout;

/**
 * Created by arvin on 2017/9/7 0007.
 */

public class DeviceGridLayout extends GridLayout {

    private static final String TAG = DeviceGridLayout.class.getSimpleName();
    CustomGridLayoutCallBack mCallBack;

    public static abstract class CustomGridLayoutCallBack{
        public void onSizeChange(int height,int width) {
        }
    }

    public void setCallBack(CustomGridLayoutCallBack mCallBack) {
        Log.d(TAG, "setCallBack");
        this.mCallBack = mCallBack;
    }

    public DeviceGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DeviceGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeviceGridLayout(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Handler updateHandler=new Handler();
        updateHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onSizeChange(h,w);
                }

            }
        });
    }
}
