package com.android.arvin.DataText;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class Test {

    String measure_item_liquid_state_text;
    String measure_item_name_text;
    String measure_item_value_text;
    String measure_item_time_text;

    public Test(String measure_item_liquid_state_text,
                String measure_item_name_text,
                String measure_item_value_text,
                String measure_item_time_text) {

        this.measure_item_liquid_state_text = measure_item_liquid_state_text;
        this.measure_item_name_text = measure_item_name_text;
        this.measure_item_value_text = measure_item_value_text;
        this.measure_item_time_text = measure_item_time_text;
    }

    public String getMeasure_item_liquid_state_text() {
        return measure_item_liquid_state_text;
    }

    public void setMeasure_item_liquid_state_text(String measure_item_liquid_state_text) {
        this.measure_item_liquid_state_text = measure_item_liquid_state_text;
    }

    public String getMeasure_item_name_text() {
        return measure_item_name_text;
    }

    public void setMeasure_item_name_text(String measure_item_name_text) {
        this.measure_item_name_text = measure_item_name_text;
    }

    public String getMeasure_item_value_text() {
        return measure_item_value_text;
    }

    public void setMeasure_item_value_text(String measure_item_value_text) {
        this.measure_item_value_text = measure_item_value_text;
    }

    public String getMeasure_item_time_text() {
        return measure_item_time_text;
    }

    public void setMeasure_item_time_text(String measure_item_time_text) {
        this.measure_item_time_text = measure_item_time_text;
    }
}
