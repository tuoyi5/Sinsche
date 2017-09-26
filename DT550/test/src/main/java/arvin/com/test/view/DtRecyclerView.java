package arvin.com.test.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import arvin.com.test.R;
import arvin.com.test.data.DeviceData;
import arvin.com.test.databinding.DtRecyclerViewLayoutBinding;
import arvin.com.test.utils.DtUtils;

/**
 * Created by tuoyi on 2017/9/25 0025.
 */

public class DtRecyclerView extends RelativeLayout {

    private static final String TAG = DtRecyclerView.class.getSimpleName();
    private static final int NUM_COLUMNS = 3;
    private DtRecyclerViewLayoutBinding binding;
    private Context context;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;

    private DeviceData deviceData;

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setData(DeviceData data) {
        this.deviceData = data;
        if (binding != null)
            binding.setData(data);
    }

    public DtRecyclerView(Context context) {
        super(context);
        initViews(context);

    }

    public DtRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(final Context context) {
        this.context = context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dt_recycler_view_layout, this, true);
        relativeLayout = binding.recyclerRelatveLayout;
        recyclerView = binding.contentRecyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.content_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);


        DtGridLayoutManager manager = new DtGridLayoutManager(context, NUM_COLUMNS);
        recyclerView.setLayoutManager(manager);

        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(context, new DtSpanData(NUM_COLUMNS, R.dimen.spacing, true));
        //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.spacing);
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

        private int spanCount;//列
        private int spacing; //边距
        private boolean includeEdge;

        public SpacesItemDecoration(Context context, DtSpanData dtSpanData) {
            this.spacing = context.getResources().getDimensionPixelSize(dtSpanData.getSpac());
            this.spanCount = dtSpanData.getSpanCount();
            this.includeEdge = dtSpanData.isIncludeEdge();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;
            Log.d(TAG, "");
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                Log.d(TAG, "");
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
        private int[] mMeasuredDimension = new int[2];

        public DtGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            Log.d(TAG, "initViews,onMeasure: ");

            int width = 0;
            int height = 0;
            double count = getItemCount();
            double span = getSpanCount();


            for (int i = 0; i < count; i++) {
                measureScrapChild(recycler, i,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);
                if (i % span == 0) {
                    height += mMeasuredDimension[1];
                }

                if (i / span == 0) {
                    width += mMeasuredDimension[0];
                }

            }
            setMeasuredDimension(width, height);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            layoutParams.height =(int)(1.1 * height + binding.bottomLine.getHeight());
            Log.d(TAG, "layoutParams.height: " + layoutParams.height);
            relativeLayout.setLayoutParams(layoutParams);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            if (position < getItemCount()) {
                try {
                    View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException
                    if (view != null) {
                        RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                                getPaddingLeft() + getPaddingRight(), p.width);
                        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                                getPaddingTop() + getPaddingBottom(), p.height);
                        view.measure(childWidthSpec, childHeightSpec);

                        measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                        measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                        recycler.recycleView(view);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private int findMinIndex(int[] array) {
            int index = 0;
            int min = array[0];
            for (int i = 0; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                    index = i;
                }
            }
            return index;
        }

        private int findMax(int[] array) {
            int max = array[0];
            for (int value : array) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }

    }
}
