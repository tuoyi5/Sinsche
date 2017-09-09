package com.android.arvin.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.android.arvin.DataText.Test;
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

    private HashMap<String, Integer> dataToViewMapping;
    private ArrayList<Integer> styleLayoutList;

    private GAdapter adapter;
    private GObject dummyObject;

    private int itemViewResourceId;
    private int contentViewItemHight;
    private int contentViewItemWidth;
    private int gridRowCount;
    private int gridColumnCount;
    private boolean isForceFocusInTouchMode = true;
    private boolean wrapContent = false;

    private ContentItemView.HyphenCallback hyphenCallback = null;
    private ContentViewCallback contentViewCallback;
    // private DeviceLayout.UpdateUiCallback updateUiCallback;


    public int getContentViewItemWidth() {
        return contentViewItemHight;
    }

    private GObject getDummyObject() {
        if (dummyObject == null) {
            dummyObject = new GObject().setDummyObject();
        }
        return dummyObject;
    }

    public void setHyphenTittleViewCallback(ContentItemView.HyphenCallback callback) {
        hyphenCallback = callback;
    }

    public void setContentViewCallback(ContentViewCallback callback) {
        contentViewCallback = callback;
    }

    public void setUpdateUiCallback(DeviceLayout.UpdateUiCallback callback) {
        // updateUiCallback = callback;
    }

    public interface ContentViewCallback {
        public void uiLoading();

        public void beforeSetupData(ContentItemView view, GObject object);

        public void onItemSelected(ContentItemView view);

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
                    contentViewCallback.uiLoading();
            }
        });

    }

    public void setSubLayoutParameter(int resId, final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList) {
        itemViewResourceId = resId;
        dataToViewMapping = mapping;
        styleLayoutList = styleList;
    }

    public void setupGridLayout(int contentViewItemHight, int gridRowCount, int gridColumnCount, boolean forceRefillContentGrid) {
        if (forceRefillContentGrid) {
            gridLayout.removeAllViews();
        }

        this.contentViewItemHight = contentViewItemHight;
        this.gridRowCount = gridRowCount;
        this.gridColumnCount = gridColumnCount;
        gridLayout.setColumnCount(gridColumnCount);
        gridLayout.setRowCount(gridRowCount);
        //fillGridLayout();

    }

    public int getGridRowCount() {
        return gridLayout.getRowCount();
    }

    public int getGridColumnCount() {
        return gridLayout.getColumnCount();
    }


    public void setAdapter(GAdapter dataAdapter) {
        adapter = dataAdapter;
        if (adapter == null) {
            return;
        }
        updateInfo();
    }


    private void fillGridLayout() {
        if (gridColumnCount != 0)
            contentViewItemWidth = gridLayout.getMeasuredWidth() / gridColumnCount;

        Log.d(TAG, "contentViewItemWidth: " + contentViewItemWidth);

        int rows = getGridRowCount();
        int cols = getGridColumnCount();
        gridLayout.removeAllViews();
        gridLayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        //gridLayout.getChildCount()
        // Log.d(TAG, "fillGridLayout Child" +
        //         ":" + gridLayout.getChildCount());
        for (int i = 0; i < rows * cols; ++i) {
            int row = i / cols;
            int col = i % cols;
//            Log.d(TAG, "fillGridLayout, rowSpec: " + row);
//            Log.d(TAG, "fillGridLayout, columnSpec: " + col);
            GObject item = getDummyObject();

            if (adapter != null) {
                item = adapter.get(i);
            } else {
                item = GAdapterUtil.objectFromTestData(
                        new Test(
                                String.format(context.getString(R.string.lack_of_liquid), context.getString(R.string.lack_of_liquid_yes)),
                                context.getString(R.string.nitrite_nitrogen),
                                String.format(context.getString(R.string.concentration_unit), 0.01),
                                "1986-08-07 15:00:00"
                        )
                );
            }

            ContentItemView itemView = ContentItemView.create(context, item, itemViewResourceId, dataToViewMapping, styleLayoutList);
            if (hyphenCallback != null) {
                itemView.setHyphenCallback(hyphenCallback);
            }
            GridLayout.Spec rowSpec = GridLayout.spec(row);
            GridLayout.Spec columnSpec = GridLayout.spec(col);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.FILL);
            setGridLayoutParams(params);
            gridLayout.addView(itemView, i, params);

            Log.d(TAG, "itemView.getWidth(): " + itemView.getWidth());
            Log.d(TAG, "itemView,getHeight(): " + itemView.getHeight());
        }
    }

    private void setGridLayoutParams(GridLayout.LayoutParams params) {
        params.width = contentViewItemWidth;
        params.height = contentViewItemHight;
    }

    private void updateInfo() {
        ContentItemView itemView;
        int length = gridLayout.getColumnCount() * gridLayout.getRowCount();
        Log.d(TAG, "updateInfo, length : " + length);
        Log.d(TAG, "updateInfo, adapter.size : " + adapter.getList().size());
        for (int i = 0; i < length; ++i) {
            GObject item = adapter.get(i);
            itemView = ((ContentItemView) gridLayout.getChildAt(i));

            if (item == null) {
                Log.d(TAG, "updateInfo, GObject = null : " + i);
                continue;
            }

            if (itemView == null) {
                Log.d(TAG, "itemView = null : " + i);
                continue;
            }
            setupItemView(itemView, item, true);
        }
    }

    private void setupItemView(ContentItemView itemView, GObject data, boolean showDivider) {
        // Log.d(TAG, "setupItemView, itemView,Width :" + itemView.getWidth());
        // Log.d(TAG, "setupItemView, itemView,Height :" + itemView.getHeight());
        beforeSetupData( itemView, data);
        itemView.setOnClickListener(mItemOnClickListener);
        itemView.setOnLongClickListener(mItemOnLongClickListener);
        itemView.setBackgroundResource(R.drawable.bg);
        itemView.setData(data);
    }

    private OnClickListener mItemOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + v.getId());
            if (isForceFocusInTouchMode) {
                if (gridLayout.getFocusedChild() != null) {
                    gridLayout.getFocusedChild().clearFocus();
                }
            }

            DtContentView.this.notifyItemClick((ContentItemView) v);
        }
    };

    private OnLongClickListener mItemOnLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (isForceFocusInTouchMode) {
                if (gridLayout.getFocusedChild() != null) {
                    gridLayout.getFocusedChild().clearFocus();
                }
            }
            return DtContentView.this.notifyItemLongClick((ContentItemView) v);
        }
    };
}

