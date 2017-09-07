package com.android.arvin.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.arvin.R;

/**
 * Created by arvin on 2017/9/7 0007.
 */

public class DeviceStatusLayout extends LinearLayout {

    private Context context;
    private ImageView titleImageView, statusImageView;
    private TextView titleText, statusText;

    public DeviceStatusLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.device_status_layout, null);
        addView(myView);
        titleImageView = (ImageView) myView.findViewById(R.id.title_imageView);
        titleText = (TextView) myView.findViewById(R.id.title_textView);
        statusText = (TextView) myView.findViewById(R.id.status_textView);
        statusImageView = (ImageView) myView.findViewById(R.id.status_imageView);
    }

    public void setDeviceStatusLayoutData(int titleInt, int statusInt, String title, String status){
        setTitleImageView(titleInt);
        setStatusImageView(statusInt);
        setTitleText(title);
        setStatusText(status);
    }

    public void setTitleImageView(int id) {
        if (id > 0) {
            titleImageView.setImageResource(id);
        }
    }

    public void setStatusImageView(int id) {
        if (id > 0) {
            statusImageView.setImageResource(id);
        }
    }

    public void setTitleText(String string) {
        if (string != null) {
            titleText.setText(string);
        }
    }

    public void setStatusText(String string) {
        if (string != null) {
            statusText.setText(string);
        }
    }



}
