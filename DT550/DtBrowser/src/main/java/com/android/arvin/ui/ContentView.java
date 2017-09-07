package com.android.arvin.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.arvin.R;
import com.android.arvin.data.GObject;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by arvin on 2017/9/7 0007.
 */

public class ContentView extends RelativeLayout {

    private DtBaseGridLayout gridLayout;
    private LayoutInflater inflater = null;
    private boolean wrapContent = false;
    private ContentItemView.HyphenCallback hyphenCallback = null;
    private GObject dummyObject;

    private int itemViewResourceId;
    private HashMap<String, Integer> dataToViewMapping;
    private ArrayList<Integer> styleLayoutList;
    private RecyclerView.Adapter adapter;

    public void setHyphenTittleViewCallback(ContentItemView.HyphenCallback callback) {
        hyphenCallback = callback;
    }

    public static class ContentViewCallback {

        public void onItemSelected(ContentItemView view) {
        }

        public void onItemClick(ContentItemView view) {
        }

        public boolean onItemLongClick(ContentItemView view) {
            return false;
        }
    }

    public ContentView(Context context) {
        this(context, null);
    }

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public void setAdapter(RecyclerView.Adapter dataAdapter){
        adapter = dataAdapter;
        if (adapter == null) {
            return;
        }

        requestFocus();
    }



    public void setSubLayoutParameter(int resId, final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList) {
        itemViewResourceId = resId;
        dataToViewMapping = mapping;
        styleLayoutList = styleList;
    }

    private void initViews(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.content_view_layout, this, true);
        gridLayout = (DtBaseGridLayout) findViewById(R.id.grid_layout_content);


        inflater = LayoutInflater.from(context);
        gridLayout.setCallBack(new DtBaseGridLayout.CustomGridLayoutCallBack() {
            @Override
            public void onSizeChange(int height, int width) {
                super.onSizeChange(height, width);
                fillGridLayout(height, width);
            }
        });

    }

    private void fillGridLayout(int height, int width) {
        int rows = getGridRowCount();
        int cols = getGridColumnCount();
        gridLayout.removeAllViews();
        gridLayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        for (int i = gridLayout.getChildCount(); i < rows * cols; ++i) {
            int row = i / cols;
            int col = i % cols;
            GObject item = dummyObject;
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
        }
    }

    private void setGridLayoutParams(GridLayout.LayoutParams params, int width, int height) {
        if (width == 0 || height == 0) {
            params.width = getMeasuredWidth() / gridLayout.getColumnCount();
            int contentViewHeight = getMeasuredHeight();
            params.height = contentViewHeight / gridLayout.getRowCount();
        } else {
            params.width = width / gridLayout.getColumnCount();
            params.height = height / gridLayout.getRowCount();
        }
        if (wrapContent) {
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setGravity(Gravity.CENTER);
            params.bottomMargin = 0;
        }
    }

    public int getGridRowCount() {
        return gridLayout.getRowCount();
    }

    public int getGridColumnCount() {
        return gridLayout.getColumnCount();
    }
}
