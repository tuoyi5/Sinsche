
package com.android.arvin.ui.custom;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.arvin.R;
import com.android.arvin.util.DtUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private static final String TAG = MyMarkerView.class.getSimpleName();
    private TextView valueText, timeText;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        valueText = (TextView) findViewById(R.id.value_tv);
        timeText = (TextView) findViewById(R.id.time_tv);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        long t = (long) e.getX();
        String time = DtUtils.formatymdHmsTime(t) + " " + DtUtils.formatHmsTime(t);
        valueText.setText("" + e.getY());
        timeText.setText(time);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
