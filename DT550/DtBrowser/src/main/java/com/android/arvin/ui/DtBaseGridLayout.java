package com.android.arvin.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.GridLayout;

/**
 * Created by arvin on 2017/9/7 0007.
 */

public class DtBaseGridLayout extends GridLayout {


    public static abstract class CustomGridLayoutCallBack{
        public void onSizeChange(int height,int width) {
        }
    }

    public void setCallBack(CustomGridLayoutCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    CustomGridLayoutCallBack mCallBack;


    public DtBaseGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DtBaseGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DtBaseGridLayout(Context context) {
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
