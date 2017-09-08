package com.android.arvin.ui;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.arvin.data.GObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class ContentItemTestView extends LinearLayout{
    private static final String TAG = ContentItemView.class.getSimpleName();
    private GObject dataObject;
    private int layoutResourceId;
    private Map<String, Integer> dataLayoutMapping;
    private ArrayList<Integer> styleLayoutList;
    private double mWeight = 0.0;
    private ContentItemView.HyphenCallback hyphenCallback;

    private ContentItemTestView(LayoutInflater inflater, GObject data, int layoutResource, Map<String, Integer> mapping, ArrayList<Integer> styleLayoutList) {
        super(inflater.getContext());
        init(inflater, data, layoutResource, mapping, styleLayoutList);
    }


    private void init(LayoutInflater inflater, GObject data, int resId, Map<String, Integer> mapping, ArrayList<Integer> styleList) {
        dataObject = data;
        layoutResourceId = resId;
        dataLayoutMapping = mapping;
        styleLayoutList = styleList;
        removeAllViews();
        inflateView(inflater, layoutResourceId);
        if (dataObject != null) {
            dataObject.setCallback(new GObject.GObjectCallback() {
                @Override
                public void changed(String key, GObject object) {
                    ContentItemTestView.this.update();
                }
            });
            update();
        }

    }

    public GObject getData() {
        return dataObject;
    }

    public ArrayList<Integer> getStyleLayoutList() {
        return styleLayoutList;
    }

    private Map<String, Integer> getDataLayoutMapping() {
        if (dataLayoutMapping != null) {
            return dataLayoutMapping;
        }
        // return GAdapterUtil.getDefaultMenuMaping();
        return null;
    }

    public void setData(final GObject data) {
        dataObject = data;
        update();
    }

    public void update() {
        if (getChildCount() <= 0) {
            return;
        }
        View parent = getChildAt(0);

        if (getData().isDummyObject()) {
            parent.setVisibility(View.INVISIBLE);
            return;
        }
        parent.setVisibility(VISIBLE);
        updateStyleLayout(parent);
        updateByDataLayoutMapping(parent);
    }

    private void updateStyleLayout(View parent) {
        ArrayList<Integer> list = getStyleLayoutList();
        if (list == null) {
            return;
        }
        for (Integer viewId : list) {
            View view = parent.findViewById(viewId);
            Log.d(TAG, "updateStyleLayout, getData().isDummyObject(): " + getData().isDummyObject());
            //setVisibility(view, getData().isDummyObject() ? GONE : VISIBLE);
        }
    }

    private void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void updateByDataLayoutMapping(View parent) {
        Log.d(TAG, "updateByDataLayoutMapping");
        // mapping specifies relation between tag and view
        Map<String, Integer> mapping = getDataLayoutMapping();
        if (mapping == null) {
            return;
        }

        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            final String key = entry.getKey();
            int viewId = entry.getValue();
            View view = parent.findViewById(viewId);
            if (view == null) {
                continue;
            }

            Object value = getData().getObject(key);
            /**
             * Todo: If value ==null will hide the view which design to display the data.
             * some time will cause incorrect layout.so if layout need the view set to invincible
             * instead of gone,should store some empty value but not a null.
             */

            if (hyphenCallback != null && hyphenCallback.keyList.contains(key)) {
                view.setVisibility(VISIBLE);
                hyphenCallback.onUpdateHyphenTittleView(view, value);
                hyphenCallback.onUpdateHyphenTittleView(view, getData());
                continue;
            }

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            // TODO: list all possible tag, should use tag type instead of view type.
            view.setLayoutParams(layoutParams);
            if (view instanceof Button) {
                updateButtonText((Button) view, value);
            } else if (view instanceof ImageView) {
                updateImageView((ImageView) view, value);
            } else if (view instanceof TextView) {
                updateStandardTextView((TextView) view, value);
            }
        }
    }

    private void updateButtonText(Button button, Object value) {
        if (value == null) {
            return;
        }
        button.setVisibility(VISIBLE);
        int n = intValue(value);
        if (n == 0) {
            float f = floatValue(value);
            button.setText(String.valueOf(f));
        } else if (n > 0) {
            button.setText(String.valueOf(n));
        } else if (value instanceof String) {
            button.setText((String) value);
        }
    }

    private void updateImageView(ImageView imageView, Object value) {
        if (value == null) {
            imageView.setImageResource(android.R.color.transparent);
            return;
        }
        imageView.setVisibility(VISIBLE);
        int n = intValue(value);
        if (n > 0) {
            imageView.setImageResource(n);
        } else if (value instanceof Drawable) {
            imageView.setImageDrawable((Drawable) value);
        } else if (value instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) value;
            if (!bitmap.isRecycled()) {
                imageView.setImageBitmap(bitmap);
            }
        } else if (n == 0) {
            imageView.setImageResource(android.R.color.transparent);
        }
    }

    private void updateStandardTextView(TextView textView, Object value) {

        if (value == null) {
            textView.setVisibility(GONE);
            return;
        }
        textView.setVisibility(VISIBLE);
        if (value instanceof String) {
            Log.d(TAG, "updateStandardTextView, text: " + value);
            Log.d(TAG, "updateStandardTextView, width: " + textView.getWidth());
            Log.d(TAG, "updateStandardTextView, Heigth: " + textView.getHeight());
            textView.setText((String) value);
        } else if (value instanceof Integer) {
            textView.setText((Integer) value);
        } else if (value instanceof Typeface) {
            textView.setTypeface((Typeface) value);
        } else if (value instanceof SpannableString) {
            textView.setText((SpannableString) value, TextView.BufferType.SPANNABLE);
        }
    }

    private int intValue(Object object) {
        if (object == null) {
            return -1;
        }
        if (!(object instanceof Integer)) {
            return 0;
        }
        return (Integer) object;
    }

    private float floatValue(Object object) {
        if (object == null) {
            return -1;
        }
        if (!(object instanceof Float)) {
            return 0;
        }
        return (Float) object;
    }

    private void inflateView(LayoutInflater inflater, int resourceId) {
        if (resourceId <= 0) {
            return;
        }
        Log.d(TAG, "resourceId: " + resourceId);
        View view = inflater.inflate(resourceId, this, false);
        ((LinearLayout.LayoutParams) view.getLayoutParams()).gravity = Gravity.CENTER;
        addView(view);
        mWeight = ((LinearLayout.LayoutParams) view.getLayoutParams()).weight;
        Log.d(TAG, "inflateView,weight: " + mWeight );
    }


    public void setHyphenCallback(ContentItemView.HyphenCallback hyphenCallback) {
        this.hyphenCallback = hyphenCallback;
    }

    public abstract static class HyphenCallback {
        public ArrayList<String> keyList;

        protected HyphenCallback(ArrayList<String> keyList) {
            this.keyList = keyList;
        }

        public abstract void onUpdateHyphenTittleView(View view, Object value);
    }

    public static ContentItemTestView create(LayoutInflater inflater, GObject data, int layoutResource, Map<String, Integer> mapping, ArrayList<Integer> styleLayoutList) {
        int layoutOverride = layoutResource;
        Map<String, Integer> mappingOverride = mapping;
        return new ContentItemTestView(inflater, data, layoutOverride, mappingOverride, styleLayoutList);
    }


}