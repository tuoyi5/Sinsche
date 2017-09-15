package com.android.arvin.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.android.arvin.interfaces.TouchCallback;

/**
 * Created by arvin on 2017/9/15 0015.
 */

public class DtScrollView extends ScrollView {

    private static final String TAG = DtScrollView.class.getSimpleName();

    private int lastY;
    private int scrollY;

    private TouchCallback touchCallback;

    public DtScrollView(Context context) {
        super(context);
    }

    public DtScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setTouchCallback(TouchCallback touchCallback) {
        this.touchCallback = touchCallback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                scrollY = getScrollY();
                break;
            case MotionEvent.ACTION_UP:
                int deltY = (int) (ev.getY() - lastY);
                if (scrollY == 0 && deltY > 500) {
                    if (touchCallback != null) {
                        touchCallback.toDropDownBack();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        View view = (View) getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        int top = view.getTop();
        top -= (0 + getScrollY());
        if (d == 0) {

        } else if (top == 0) {

        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
}