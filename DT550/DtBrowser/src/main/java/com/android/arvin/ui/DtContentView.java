package com.android.arvin.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private DeviceGridLayout gridLayout;
    private LayoutInflater inflater = null;
    private boolean wrapContent = false;
    private ContentItemView.HyphenCallback hyphenCallback = null;
    private ContentViewCallback callback;
    private int itemViewResourceId;
    private HashMap<String, Integer> dataToViewMapping;
    private ArrayList<Integer> styleLayoutList;
    private GAdapter adapter;
    private boolean isForceFocusInTouchMode = true;
    private GObject dummyObject;

    private int contentViewItemHight;
    private int contentViewItemWidth;

    public int getContentViewItemWidth(){
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

    public static class ContentViewCallback {

        public void beforeSetupData(ContentItemView view, GObject object){

        }
        public void onItemSelected(ContentItemView view) {
        }

        public void onItemClick(ContentItemView view) {
        }

        public boolean onItemLongClick(ContentItemView view) {
            return false;
        }
    }

    private void notifyItemClick(ContentItemView view) {
        if (callback != null) {
            callback.onItemClick(view);
        }
    }

    private boolean notifyItemLongClick(ContentItemView view) {
        return callback != null && callback.onItemLongClick(view);
    }

    private void beforeSetupData(ContentItemView itemView, GObject data) {
        notifyBeforeSetupData(itemView, data);
    }

    private void notifyBeforeSetupData(ContentItemView itemView, GObject object) {
        if (callback != null) {
            callback.beforeSetupData(itemView, object);
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
                ViewGroup.LayoutParams layoutParams = gridLayout.getLayoutParams();
                layoutParams.height = gridLayout.getRowCount() * contentViewItemHight/2;
                gridLayout.setLayoutParams(layoutParams);

                //gridLayout.setRowCount(gridLayout.getRowCount()/2);
                int gridLayoutSize = gridLayout.getChildCount();
                for( int i =  gridLayoutSize / 2; i < gridLayoutSize; i++) {
                    View view =  gridLayout.getChildAt(i);
                    if(view != null)
                        view.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setSubLayoutParameter(int resId, final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList) {
        itemViewResourceId = resId;
        dataToViewMapping = mapping;
        styleLayoutList = styleList;
    }

    public void setupGridLayout(int contentViewItemHight, int gridRowCount, int gridColumnCount,  boolean forceRefillContentGrid) {
        if (forceRefillContentGrid) {
            gridLayout.removeAllViews();
        }

        this.contentViewItemHight = contentViewItemHight;
        gridLayout.setColumnCount(gridColumnCount);
        gridLayout.setRowCount(gridRowCount);
        fillGridLayout();
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


    private void fillGridLayout(int height, int width) {
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

            item = GAdapterUtil.objectFromTestData(
                    new Test(
                            String.format(context.getString(R.string.lack_of_liquid), context.getString(R.string.lack_of_liquid_yes)),
                            context.getString(R.string.nitrite_nitrogen),
                            String.format(context.getString(R.string.concentration_unit), 0.01),
                            "1986-08-07 15:00:00"
                    )
            );
            ContentItemView itemView = ContentItemView.create(inflater, item, itemViewResourceId, dataToViewMapping, styleLayoutList);
            if (hyphenCallback != null) {
                itemView.setHyphenCallback(hyphenCallback);
            }
            GridLayout.Spec rowSpec = GridLayout.spec(row);
            GridLayout.Spec columnSpec = GridLayout.spec(col);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.FILL);
            setGridLayoutParams(params, width, height);
            gridLayout.addView(itemView, i, params);
            Log.d(TAG, "itemView.getWidth(): " + itemView.getWidth());
            Log.d(TAG, "itemView,getHeight(): " + itemView.getHeight());
        }
    }

    private void fillGridLayout() {
        if (gridLayout.getColumnCount() == 0) {
            gridLayout.setColumnCount(2);
        }
        contentViewItemWidth = gridLayout.getMeasuredWidth() / gridLayout.getColumnCount();
        fillGridLayout(gridLayout.getMeasuredWidth(), gridLayout.getMeasuredHeight());
    }

    private void setGridLayoutParams(GridLayout.LayoutParams params, int width, int height) {
        params.width = contentViewItemWidth;
        params.height = contentViewItemHight;
   /*     if (width == 0 || height == 0) {
            params.width = getMeasuredWidth() / gridLayout.getColumnCount();
            int contentViewHeight = getMeasuredHeight();
            params.height = contentViewHeight / gridLayout.getRowCount();
        } else {
            //contentViewItemWidth = width / gridLayout.getColumnCount();
            params.width = contentViewItemWidth;
            //params.height = height / gridLayout.getRowCount();
            params.height = contentViewItemHight;

        }
        if (wrapContent) {
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setGravity(Gravity.CENTER);
            params.bottomMargin = 0;
        }*/
        Log.d(TAG, "updateStandardTextView, width: " + params.width);
        Log.d(TAG, "updateStandardTextView, Heigth: " + params.height );

    }

    private void updateInfo(){
        ContentItemView itemView;
        int length = gridLayout.getColumnCount() * gridLayout.getRowCount();
        Log.d(TAG, "updateInfo, length : " + length);
        Log.d(TAG, "updateInfo, adapter.size : " + adapter.getList().size());
        for (int i = 0; i < length; ++i) {
            GObject item = adapter.get(i);
            itemView = ((ContentItemView) gridLayout.getChildAt(i));
            if(item == null){
                Log.d(TAG, "updateInfo, GObject = null : " + i);
                continue;
            }
            setupItemView(itemView, item, true);
        }
    }

    private void setupItemView(ContentItemView itemView, GObject data, boolean showDivider) {
        beforeSetupData(itemView, data);
        if (data.isDummyObject()) {
            itemView.setFocusable(false);
            itemView.setOnTouchListener(null);
            itemView.setOnClickListener(null);
            itemView.setOnLongClickListener(null);
            itemView.setOnFocusChangeListener(null);
            //itemView.setBackgroundResource(0);
        } else {
            //itemView.setFocusable(true);
            itemView.setOnClickListener(mItemOnClickListener);
            itemView.setOnLongClickListener(mItemOnLongClickListener);
            itemView.setBackgroundResource(R.drawable.bg);
        }
        itemView.setData(data);
    }

    private OnClickListener mItemOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
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

