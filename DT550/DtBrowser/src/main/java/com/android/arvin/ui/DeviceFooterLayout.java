package com.android.arvin.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.arvin.R;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class DeviceFooterLayout extends RelativeLayout {

    private static final String TAG = DeviceFooterLayout.class.getSimpleName();
    private Context context;
    private ImageView waterStatusImageView, moreImageView;
    private TextView waterStatusText, moreText;

    private View view;

    private OnClickCallBack onClickCallBack;
    private View.OnClickListener onClickListener;
    private boolean foldgState = true;

    public void setOnClickListener(View.OnClickListener o) {
        onClickListener = o;
    }

    public OnClickCallBack getOnClickCallBack() {
        return onClickCallBack;
    }

    public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

    public interface OnClickCallBack {
        public void onClickCallBack(boolean fold);
    }

    public DeviceFooterLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.device_footer_layout, null);
        addView(myView);
        waterStatusImageView = (ImageView) myView.findViewById(R.id.waterstatus_imageView);
        moreImageView = (ImageView) myView.findViewById(R.id.more_imageView);
        waterStatusText = (TextView) myView.findViewById(R.id.waterstatus_textView);
        moreText = (TextView) myView.findViewById(R.id.more_textView);
        view = (View) myView.findViewById(R.id.more_layout);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCallBack != null) {
                    if (foldgState) {
                        foldgState = false;
                    } else {
                        foldgState = true;
                    }
                    onClickCallBack.onClickCallBack(foldgState);
                }
            }
        });
        //view.setOnClickListener(onClickListener);
    }

    public void setDeviceFooterLayoutData(int waterId, int moreId, String waterString, String moreString) {
        setWaterStatusImageView(waterId);
        setMoreImageView(moreId);
        setWaterStatusText(waterString);
        setMoreText(moreString);
    }


    public void setWaterStatusImageView(int id) {
        this.waterStatusImageView.setImageResource(id);
    }

    public void setMoreImageView(int id) {
        this.moreImageView.setImageResource(id);
    }

    public void setWaterStatusText(String string) {
        this.waterStatusText.setText(string);
    }

    public void setMoreText(String string) {
        this.moreText.setText(string);
    }
}
