package com.android.arvin.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.arvin.R;
import com.android.arvin.util.GAdapter;
import com.android.arvin.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class DeviceLayout extends RelativeLayout {

    private static final String TAG = DeviceLayout.class.getSimpleName();
    private Context context;
    private DeviceStatusLayout statusLayout;
    private DeviceGridLayout gridLayout;
    private DtContentView contentView;
    private DeviceFooterLayout footerLayout;
    private int contentViewItemInt = R.layout.content_view_item_layout;

    private int gridRowCount;
    private int gridColumnCount;
    private int itemhight;

    public DeviceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public DeviceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }


    public DeviceLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        this.gridRowCount = context.getResources().getInteger(R.integer.gridLayout_row);
        this.gridColumnCount = context.getResources().getInteger(R.integer.gridLayout_column);
        this.itemhight = StringUtils.dip2px(context, context.getResources().getInteger(R.integer.content_view_item_hight));
        RelativeLayout.LayoutParams layoutParams;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.device_layout, null);
        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        myView.setLayoutParams(layoutParams);

        statusLayout = (DeviceStatusLayout) myView.findViewById(R.id.device_title);
        layoutParams = (RelativeLayout.LayoutParams) statusLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        statusLayout.setLayoutParams(layoutParams);

        contentView = (DtContentView) myView.findViewById(R.id.device_grid);
        layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        //layoutParams.width = StringUtils.dip2px(context,(int)context.getResources().getDimension(R.dimen.device_layout_width));
        layoutParams.height = itemhight * gridRowCount;
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        contentView.setLayoutParams(layoutParams);

        footerLayout = (DeviceFooterLayout) myView.findViewById(R.id.device_footer);
        layoutParams = (RelativeLayout.LayoutParams) footerLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        footerLayout.setLayoutParams(layoutParams);
        addView(myView);
    }

    public void setSubLayoutParameter(final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList){
        Log.d(TAG, "setSubLayoutParameter, resourceId: " + contentViewItemInt);
        contentView.setSubLayoutParameter(contentViewItemInt, mapping, styleList);
    }

    public void setupGridLayout(Context context, boolean forceRefillContentGrid) {
        contentView.setupGridLayout(itemhight, gridRowCount, gridColumnCount, forceRefillContentGrid);
    }

    public void setAdapter(GAdapter gAdapter){
        contentView.setAdapter(gAdapter);
    }


}
