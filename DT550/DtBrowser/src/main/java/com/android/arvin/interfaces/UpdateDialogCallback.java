package com.android.arvin.interfaces;

import com.android.arvin.data.DeviceHistoryData;
import com.github.mikephil.charting.data.Entry;

import java.util.Collection;

/**
 * Created by arvin on 2017/9/15 0015.
 */

public interface UpdateDialogCallback {

    public void releaseEntrys(DeviceHistoryData hisData, Collection<Entry> collection);
}
