package arvin.com.test.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import arvin.com.test.adapter.DtBindAdapter;
import arvin.com.test.data.DtData;

/**
 * Created by tuoyi on 2017/9/25 0025.
 */

public class DtUtils {

//    @BindingAdapter("bind:image")
//    public static void loadImage(ImageView image, Drawable resId){
//        image.setImageDrawable(resId);
//    }

    @BindingAdapter("data")
    public static void setData(RecyclerView recyclerView, DtData data){
        recyclerView.setAdapter(new DtBindAdapter(recyclerView.getContext(), data));
    }
}
