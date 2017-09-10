package com.android.arvin.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.arvin.DataText.ContentViewItemData;
import com.android.arvin.DataText.DeviceTest;
import com.android.arvin.DataText.SubItemTest;
import com.android.arvin.R;
import com.android.arvin.util.GAdapter;
import com.android.arvin.util.GAdapterUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class DeviceLayout extends RelativeLayout {

    private static final String TAG = DeviceLayout.class.getSimpleName();
    private Context context;
    private View deviceLayout;

    private DeviceStatusLayout statusLayout;
    private DtContentView contentView;
    private DeviceFooterLayout footerLayout;

    private int contentViewItemInt = R.layout.content_view_item_layout;

    private ContentViewItemData itemData;
    private DeviceTest deviceTest;

    public ContentViewItemData getItemData() {
        return itemData;
    }

    public void setItemData(ContentViewItemData itemData) {
        this.itemData = itemData;
    }

    public DeviceTest getDeviceTest() {
        return deviceTest;
    }
    public void setDeviceTest(DeviceTest deviceTest) {
        this.deviceTest = deviceTest;
    }


    public void setOnClickCallBack(DeviceFooterLayout.OnClickCallBack onClickCallBack) {
        if (footerLayout != null)
            footerLayout.setOnClickCallBack(onClickCallBack);
    }

    public void setContentViewCallback(DtContentView.ContentViewCallback callback) {
        if (contentView != null)
            contentView.setContentViewCallback(callback);
    }

    public interface UpdateUiCallback {
        public void updateGridLayoutHight(int row);
    }

    public DeviceLayout(Context context, DeviceTest device) {
        super(context);
        this.context = context;
        this.deviceTest = device;

        if(deviceTest == null){
            Toast.makeText(context, context.getResources().getString(R.string.load_failed), Toast.LENGTH_LONG).show();
            return;
        }
        initData(device);
        initView(context);
    }

    private void initData(DeviceTest device){
        itemData = new ContentViewItemData();
        itemData.setGridDefaultShowRowCount(context.getResources().getInteger(R.integer.gridLayout_default_show_row));
        itemData.setGridColumnCount(context.getResources().getInteger(R.integer.gridLayout_column));
        itemData.setItemhight((int) context.getResources().getDimension(R.dimen.content_view_item_hight));

        int row;
        itemData.setItemSize(deviceTest.getSubItemTestList().size());
        if(itemData.getItemSize() > 0){
            row = (int) Math.ceil((double) itemData.getItemSize() / itemData.getGridColumnCount());
        } else {
            row = 0;
            Toast.makeText(context, context.getResources().getString(R.string.load_failed), Toast.LENGTH_LONG).show();
            return;
        }
        itemData.setGridRowCount(row);

    }

    private void initView(Context context) {
        this.context = context;
        RelativeLayout.LayoutParams layoutParams;
        deviceLayout = LayoutInflater.from(context).inflate(R.layout.device_layout, null);
        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        deviceLayout.setLayoutParams(layoutParams);

        statusLayout = (DeviceStatusLayout) deviceLayout.findViewById(R.id.device_title);
        layoutParams = (RelativeLayout.LayoutParams) statusLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        statusLayout.setLayoutParams(layoutParams);
        statusLayout.setDeviceLayout(this);

        contentView = (DtContentView) deviceLayout.findViewById(R.id.device_grid);
        layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = itemData.getItemhight() * itemData.getGridDefaultShowRowCount();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        contentView.setLayoutParams(layoutParams);
        contentView.setDeviceLayout(this);
        contentView.setContentViewItemData(itemData);

        footerLayout = (DeviceFooterLayout) deviceLayout.findViewById(R.id.device_footer);
        layoutParams = (RelativeLayout.LayoutParams) footerLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        footerLayout.setLayoutParams(layoutParams);
        footerLayout.setDeviceLayout(this);

        addView(deviceLayout);
    }

    public void updateGridLayoutHight(boolean fold) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = itemData.getItemhight() * (fold ? itemData.getGridDefaultShowRowCount() : itemData.getGridRowCount());
        contentView.setLayoutParams(layoutParams);
        footerLayout.setMoreText(fold ? context.getString(R.string.view_more_project) : context.getString(R.string.view_pack_up_project));
        footerLayout.setMoreImageView(fold ? R.drawable.more : R.drawable.puck_up);

    }

    public void setSubLayoutParameter(final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList) {
        if (contentView != null)
            contentView.setSubLayoutParameter(contentViewItemInt, mapping, styleList);
    }

  /*  public void setupGridLayout(Context context, boolean forceRefillContentGrid) {
        if (contentView != null)
            contentView.setupGridLayout(itemhight, gridRowCount, gridColumnCount, forceRefillContentGrid);
    }*/

    public void setAdapter() {
        List<SubItemTest> subItemTestList = deviceTest.getSubItemTestList();
        if(subItemTestList == null || subItemTestList.size() == 0){
            return;
        }

        GAdapter gAdapter = new GAdapter();
        for (SubItemTest subItemTest: subItemTestList) {
            gAdapter.addObject(GAdapterUtil.objectFromTestData(subItemTest));
        }

        contentView.setAdapter(gAdapter);
    }


}
