package com.android.arvin.util;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.graphics.Color;

import com.android.arvin.R;
import com.android.arvin.data.DeviceSubItemData;
import com.android.arvin.data.GObject;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class GAdapterUtil {

    public static GObject objectFromTestData(Context context, final DeviceSubItemData data) {
        GObject object = new GObject();

        if (!DtUtils.isNullOrEmpty(data.getSubItemDataCode())){
            object.putString(DeviceConfig.MEASURE_ITEM_CODE, data.getSubItemDataCode());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_CODE,"");
        }

        String format = context.getResources().getString(R.string.lack_of_liquid);
        if (data.isbWaterState()) {
            object.putString(DeviceConfig.MEASURE_ITEM_LIQUID_STATE,
                    String.format(format, context.getResources().getString(R.string.lack_of_liquid_yes)));
            object.putObject(DeviceConfig.MEASURE_ITEM_LIQUID_STATE_BG,
                    R.drawable.liquid_state_text_yes_bg);
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_LIQUID_STATE,
                    String.format(format, context.getResources().getString(R.string.lack_of_liquid_no)));
            object.putObject(DeviceConfig.MEASURE_ITEM_LIQUID_STATE_BG,
                    R.drawable.liquid_state_text_no_bg);
        }


        if (!DtUtils.isNullOrEmpty(data.getSubItemDataName())) {
            object.putString(DeviceConfig.MEASURE_ITEM_NAME, data.getSubItemDataName());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_NAME, "");
        }

        if (!DtUtils.isNullOrEmpty(data.getSubItemDataValue()) && !DtUtils.isNullOrEmpty(data.getSubItemDataUnit())) {
            object.putString(DeviceConfig.MEASURE_ITEM_VALUE, data.getSubItemDataValue() + data.getSubItemDataUnit());

        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_VALUE, "");
        }

        if (data.isbOverLevel()) {
            object.putObject(DeviceConfig.MEASURE_ITEM_VALUE_COLOR, Color.BLACK);
        } else {
            object.putObject(DeviceConfig.MEASURE_ITEM_VALUE_COLOR, Color.RED);
        }


        if (!DtUtils.isNullOrEmpty(data.getSubItemDataTestTime())) {
            object.putString(DeviceConfig.MEASURE_ITEM_TIME, data.getSubItemDataTestTime());
        } else {
            object.putString(DeviceConfig.MEASURE_ITEM_TIME, "");
        }

        return object;
    }


}
