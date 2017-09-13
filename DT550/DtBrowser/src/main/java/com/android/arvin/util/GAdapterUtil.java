package com.android.arvin.util;

import com.android.arvin.data.DeviceSubItemData;
import com.android.arvin.data.GObject;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class GAdapterUtil {

    public static GObject objectFromTestData(final DeviceSubItemData data) {
        GObject object = new GObject();

        if (!DtUtils.isNullOrEmpty(data.getSubItemDataWaterState())) {
            object.putString(DeviceConfig.MEASURE_ITEM_LIQUID_STATE, data.getSubItemDataWaterState());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_LIQUID_STATE, "");
        }

        if (!DtUtils.isNullOrEmpty(data.getSubItemDataName())) {
            object.putString(DeviceConfig.MEASURE_ITEM_NAME, data.getSubItemDataName());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_NAME, "");
        }

        if (!DtUtils.isNullOrEmpty(data.getSubItemData())) {
            object.putString(DeviceConfig.MEASURE_ITEM_VALUE, data.getSubItemData());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_VALUE, "");
        }


        if (!DtUtils.isNullOrEmpty(data.getSubItemDataTestTime())) {
            object.putString(DeviceConfig.MEASURE_ITEM_TIME, data.getSubItemDataTestTime());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_TIME, "");
        }

        if (data.getSubItemDataWaterTextBg() > 0 ){
            object.putString(DeviceConfig.MEASURE_ITEM_LIQUID_STATE_BG,
                    String.valueOf(data.getSubItemDataWaterTextBg()));
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_TIME, String.valueOf(-1));
        }




        return object;
    }


}
