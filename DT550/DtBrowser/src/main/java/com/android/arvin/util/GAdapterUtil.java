package com.android.arvin.util;

import com.android.arvin.DataText.Test;
import com.android.arvin.data.GObject;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class GAdapterUtil {

    public static GObject objectFromTestData(final Test data) {
        GObject object = new GObject();

        if (!StringUtils.isNullOrEmpty(data.getMeasure_item_liquid_state_text())) {
            object.putString(DeviceConfig.MEASURE_ITEM_LIQUID_STATE, data.getMeasure_item_liquid_state_text());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_LIQUID_STATE, "");
        }

        if (!StringUtils.isNullOrEmpty(data.getMeasure_item_name_text())) {
            object.putString(DeviceConfig.MEASURE_ITEM_NAME, data.getMeasure_item_name_text());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_NAME, "");
        }

        if (!StringUtils.isNullOrEmpty(data.getMeasure_item_value_text())) {
            object.putString(DeviceConfig.MEASURE_ITEM_VALUE, data.getMeasure_item_value_text());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_VALUE, "");
        }


        if (!StringUtils.isNullOrEmpty(data.getMeasure_item_time_text())) {
            object.putString(DeviceConfig.MEASURE_ITEM_TIME, data.getMeasure_item_time_text());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_TIME, "");
        }




        return object;
    }


}
