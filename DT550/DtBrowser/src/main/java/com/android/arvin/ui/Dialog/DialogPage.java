package com.android.arvin.ui.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.arvin.R;

/**
 * Created by arvin on 2017/9/9 0009.
 */

public class DialogPage extends RelativeLayout {

    private TextView ValueTitleText, ValueText;
    private ImageView valueImage;
    private View vSepLineView;


    public DialogPage(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.dialog_page_item, DialogPage.this);
        ValueTitleText = (TextView) findViewById(R.id.value_title_text);
        ValueText = (TextView) findViewById(R.id.value_text);
        valueImage = (ImageView) findViewById(R.id.value_image);
        vSepLineView = (View) findViewById(R.id.vertical_separation_line);
    }

    public void setValueTitleText(String string) {
        ValueTitleText.setText(string);
    }

    public void setValueText(String string) {
        ValueText.setText(string);
    }

    public void setValueImage(int id) {
        this.valueImage.setImageResource(id);
    }

    public void setValueTextShader(int color1, int color2, Shader.TileMode tileMode) {
        LinearGradient shader = new LinearGradient(0, 0, 0,20, color1, color2, tileMode);
        ValueText.getPaint().setShader(shader);
    }

    public void setValueTextColor(int color) {
        ValueText.setTextColor(color);
    }

    public void setSepLineViewVisibility(boolean v) {
        if (v) {
            vSepLineView.setVisibility(View.VISIBLE);
        } else {
            vSepLineView.setVisibility(View.GONE);
        }
    }

    class DialogPageData {
        String valueTitle;
        String value;
        int valueImage;
        int valueColor;

        public String getValueTitle() {
            return valueTitle;
        }

        public void setValueTitle(String valueTitle) {
            this.valueTitle = valueTitle;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getValueImage() {
            return valueImage;
        }

        public void setValueImage(int valueImage) {
            this.valueImage = valueImage;
        }

        public int getValueColor() {
            return valueColor;
        }

        public void setValueColor(int valueColor) {
            this.valueColor = valueColor;
        }
    }
}
