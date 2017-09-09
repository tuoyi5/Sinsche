package com.android.arvin.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.arvin.R;
import com.android.arvin.data.GObject;
import com.android.arvin.util.AnimationUtils;
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
    private View deviceLayout;

    private DeviceStatusLayout statusLayout;
    private DeviceGridLayout gridLayout;
    private DtContentView contentView;
    private DeviceFooterLayout footerLayout;

    private int contentViewItemInt = R.layout.content_view_item_layout;
    private int gridRowCount;
    private int gridColumnCount;
    private int itemhight;

    private UpdateUiCallback updateUiCallback;

    public int getGridRowCount() {
        return gridRowCount;
    }

    public void setGridRowCount(int gridRowCount) {
        this.gridRowCount = gridRowCount;
    }

    public int getGridColumnCount() {
        return gridColumnCount;
    }

    public void setGridColumnCount(int gridColumnCount) {
        this.gridColumnCount = gridColumnCount;
    }

    public UpdateUiCallback getUpdateUiCallback() {
        return updateUiCallback;
    }

    public void setUpdateUiCallback(UpdateUiCallback updateUiCallback) {
        this.updateUiCallback = updateUiCallback;
        contentView.setUpdateUiCallback(updateUiCallback);
    }

    public void setOnClickCallBack(DeviceFooterLayout.OnClickCallBack onClickCallBack){
        footerLayout.setOnClickCallBack(onClickCallBack);
    }

    public void  setContentViewCallback(DtContentView.ContentViewCallback callback){
        contentView.setContentViewCallback(callback);
    }

    public interface UpdateUiCallback {
        public void updateGridLayoutHight(int row);
    }



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
        //this.itemhight = StringUtils.dip2px(context, (int)context.getResources().getDimension(R.dimen.content_view_item_hight));
        this.itemhight = (int) context.getResources().getDimension(R.dimen.content_view_item_hight);
        RelativeLayout.LayoutParams layoutParams;
        LayoutInflater mInflater = LayoutInflater.from(context);
        deviceLayout = mInflater.inflate(R.layout.device_layout, null);
        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        deviceLayout.setLayoutParams(layoutParams);

        statusLayout = (DeviceStatusLayout) deviceLayout.findViewById(R.id.device_title);
        layoutParams = (RelativeLayout.LayoutParams) statusLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        statusLayout.setLayoutParams(layoutParams);

        contentView = (DtContentView) deviceLayout.findViewById(R.id.device_grid);
        layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = itemhight * gridRowCount/2;
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        contentView.setLayoutParams(layoutParams);

        footerLayout = (DeviceFooterLayout) deviceLayout.findViewById(R.id.device_footer);
        layoutParams = (RelativeLayout.LayoutParams) footerLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        footerLayout.setLayoutParams(layoutParams);
        addView(deviceLayout);
    }

    public void updateGridLayoutHight(boolean fold) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = itemhight * (fold ? gridRowCount/2 : gridRowCount);
        contentView.setLayoutParams(layoutParams);
        footerLayout.setMoreText(fold ? context.getString(R.string.view_more_project): context.getString(R.string.view_pack_up_project));
        footerLayout.setMoreImageView(fold ? R.drawable.more: R.drawable.puck_up);

    }

    public void setSubLayoutParameter(final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList){
        contentView.setSubLayoutParameter(contentViewItemInt, mapping, styleList);
    }

    public void setupGridLayout(Context context, boolean forceRefillContentGrid) {
        contentView.setupGridLayout(itemhight, gridRowCount, gridColumnCount, forceRefillContentGrid);
    }

    public void setAdapter(GAdapter gAdapter){
        contentView.setAdapter(gAdapter);
    }



}
