package arvin.com.test.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import arvin.com.test.R;
import arvin.com.test.adapter.DtBindAdapter;
import arvin.com.test.data.DtData;
import arvin.com.test.databinding.DtRecyclerViewLayoutBinding;
import arvin.com.test.utils.DtUtils;

/**
 * Created by tuoyi on 2017/9/25 0025.
 */

public class DtRecyclerView extends RelativeLayout {

    private static final int NUM_COLUMNS = 3;
    private DtRecyclerViewLayoutBinding binding;
    private Context context;
    private RecyclerView recyclerView;

    private DtData data;

    public DtData getData() {
        return data;
    }

    public void setData(DtData data) {
        this.data = data;
        if (binding != null)
            binding.setData(data);
    }


    public DtRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(final Context context) {
        this.context = context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dt_recycler_view_layout, this, true);
        recyclerView = binding.contentRecyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.content_recycler_view);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        DtGridLayoutManager manager = new DtGridLayoutManager(context, NUM_COLUMNS);
        recyclerView.setLayoutManager(manager);
      /* 第一种方法
        DtSpanData spanData = new DtSpanData(NUM_COLUMNS, spacingInPixels, true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spanData));*/
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.spacing);
        recyclerView.addItemDecoration(itemDecoration);
    }


    public class DtSpanData {
        int spanCount;
        int spac;
        boolean includeEdge;

        public DtSpanData(int spanCount,
                          int spac,
                          boolean includeEdge) {
            this.spanCount = spanCount;
            this.spac = spac;
            this.includeEdge = includeEdge;
        }

        public int getSpanCount() {
            return spanCount;
        }

        public void setSpanCount(int spanCount) {
            this.spanCount = spanCount;
        }

        public int getSpac() {
            return spac;
        }

        public void setSpac(int spac) {
            this.spac = spac;
        }

        public boolean isIncludeEdge() {
            return includeEdge;
        }

        public void setIncludeEdge(boolean includeEdge) {
            this.includeEdge = includeEdge;
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public SpacesItemDecoration(DtSpanData dtSpanData) {
            this.spacing = dtSpanData.getSpac();
            this.spanCount = dtSpanData.getSpanCount();
            this.includeEdge = dtSpanData.isIncludeEdge();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    public class DtGridLayoutManager extends GridLayoutManager {

        public DtGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            if (recycler != null && recycler.getScrapList().size() > 0) {
                View view = recycler.getViewForPosition(0);
                if (view != null) {
                    measureChild(view, widthSpec, heightSpec);
                    int measuredWidth = MeasureSpec.getSize(widthSpec);
                    int measuredHeight = view.getMeasuredHeight();
                    setMeasuredDimension(measuredWidth, measuredHeight);
                }
            }
        }
    }

}
