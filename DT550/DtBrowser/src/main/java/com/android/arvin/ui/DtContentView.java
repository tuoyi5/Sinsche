package com.android.arvin.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.android.arvin.DataText.ContentViewItemData;
import com.android.arvin.DataText.SubItemTest;
import com.android.arvin.R;
import com.android.arvin.data.GObject;
import com.android.arvin.util.GAdapter;
import com.android.arvin.util.GAdapterUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arvin on 2017/9/7 0s007.
 */

public class DtContentView extends RelativeLayout {

    private static final String TAG = DtContentView.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater = null;
    private DeviceGridLayout gridLayout;
    private DeviceLayout deviceLayout;
    private ContentViewItemData ItemData;

    private GAdapter adapter;
    private GObject dummyObject;

    private HashMap<String, Integer> dataToViewMapping;
    private ArrayList<Integer> styleLayoutList;

    private int itemViewResourceId;
    private int contentViewItemHight;
    private int contentViewItemWidth;

    private ContentViewCallback contentViewCallback;

    public void setDeviceLayout(DeviceLayout device){
        deviceLayout = device;
    }

    public ContentViewItemData getContentViewItemData() {
        return ItemData;
    }

    public void setContentViewItemData(ContentViewItemData contentViewItemData) {
        this.ItemData = contentViewItemData;
        updateGridLayout();
    }

    private GObject getDummyObject() {
        if (dummyObject == null) {
            dummyObject = new GObject().setDummyObject();
        }
        return dummyObject;
    }


    public void setContentViewCallback(ContentViewCallback callback) {
        contentViewCallback = callback;
    }


    public interface ContentViewCallback {
        public void uiLoading(DeviceLayout layout);

        public void beforeSetupData(ContentItemView view, GObject object);

        public void onItemClick(ContentItemView view);

        public boolean onItemLongClick(ContentItemView view);
    }

    private void notifyItemClick(ContentItemView view) {
        if (contentViewCallback != null) {
            contentViewCallback.onItemClick(view);
        }
    }

    private boolean notifyItemLongClick(ContentItemView view) {
        return contentViewCallback != null && contentViewCallback.onItemLongClick(view);
    }

    private void beforeSetupData(ContentItemView itemView, GObject data) {
        notifyBeforeSetupData(itemView, data);
    }

    private void notifyBeforeSetupData(ContentItemView itemView, GObject object) {
        if (contentViewCallback != null) {
            contentViewCallback.beforeSetupData(itemView, object);
        }
    }

    public DtContentView(Context context) {
        this(context, null);
    }

    public DtContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public DtContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }


    private void initViews(final Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.content_view_layout, this, true);
        gridLayout = (DeviceGridLayout) findViewById(R.id.grid_layout_content);

        inflater = LayoutInflater.from(context);
        gridLayout.setCallBack(new DeviceGridLayout.CustomGridLayoutCallBack() {
            @Override
            public void onSizeChange(int height, int width) {
                super.onSizeChange(height, width);
                fillGridLayout();
                if (contentViewCallback != null)
                    contentViewCallback.uiLoading(deviceLayout);
            }
        });

    }

    public void setSubLayoutParameter(int resId, final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList) {
        itemViewResourceId = resId;
        dataToViewMapping = mapping;
        styleLayoutList = styleList;
    }

    public int getGridRowCount() {
        return gridLayout.getRowCount();
    }

    public int getGridColumnCount() {
        return gridLayout.getColumnCount();
    }

    public void updateGridLayout() {
        if(gridLayout != null && ItemData != null){
            gridLayout.setColumnCount(ItemData.getGridColumnCount());
            gridLayout.setRowCount(ItemData.getGridRowCount());
        }
    }

    public void setAdapter(GAdapter dataAdapter) {
        adapter = dataAdapter;
        if (adapter == null) {
            return;
        }
        updateInfo();
    }


    private void fillGridLayout() {
        if (ItemData.getGridColumnCount() != 0)
            contentViewItemWidth = gridLayout.getMeasuredWidth() / ItemData.getGridColumnCount();

        contentViewItemHight = ItemData.getItemhight();


        int rows = getGridRowCount();
        int cols = getGridColumnCount();
        gridLayout.removeAllViews();
        gridLayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);

        for (int i = 0; i < ItemData.getItemSize(); ++i) {
            int row = i / cols;
            int col = i % cols;
            GObject item = getDummyObject();

            if (adapter != null) {
                item = adapter.get(i);
            } else {
                item = null;
            }

            ContentItemView itemView = ContentItemView.create(context, item, itemViewResourceId, dataToViewMapping, styleLayoutList);

            GridLayout.Spec rowSpec = GridLayout.spec(row);
            GridLayout.Spec columnSpec = GridLayout.spec(col);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.FILL);
            setGridLayoutParams(params);
            gridLayout.addView(itemView, i, params);
        }
    }

    private void setGridLayoutParams(GridLayout.LayoutParams params) {
        params.width = contentViewItemWidth;
        params.height = contentViewItemHight;
    }

    private void updateInfo() {
        ContentItemView itemView;
        int length = gridLayout.getColumnCount() * gridLayout.getRowCount();
        for (int i = 0; i < length; ++i) {
            GObject item = adapter.get(i);
            itemView = ((ContentItemView) gridLayout.getChildAt(i));

            if (item == null) {
                continue;
            }

            if (itemView == null) {
                continue;
            }
            setupItemView(itemView, item, true);
        }
    }

    private void setupItemView(ContentItemView itemView, GObject data, boolean showDivider) {
        beforeSetupData( itemView, data);
        itemView.setOnClickListener(mItemOnClickListener);
        itemView.setOnLongClickListener(mItemOnLongClickListener);
        //itemView.setBackgroundResource(R.drawable.bg);
        itemView.setData(data);
    }

    private OnClickListener mItemOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DtContentView.this.notifyItemClick((ContentItemView) v);
        }
    };

    private OnLongClickListener mItemOnLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            return DtContentView.this.notifyItemLongClick((ContentItemView) v);
        }
    };


}

