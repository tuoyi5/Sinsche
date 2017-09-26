package arvin.com.test.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import arvin.com.test.adapter.DtBindAdapter;
import arvin.com.test.data.DeviceData;

/**
 * Created by tuoyi on 2017/9/25 0025.
 */

public class DtUtils {

//    @BindingAdapter("bind:image")
//    public static void loadImage(ImageView image, Drawable resId){
//        image.setImageDrawable(resId);
//    }

    @BindingAdapter("data")
    public static void setData(RecyclerView recyclerView, DeviceData data){
        recyclerView.setAdapter(new DtBindAdapter(recyclerView.getContext(), data));
    }

    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
