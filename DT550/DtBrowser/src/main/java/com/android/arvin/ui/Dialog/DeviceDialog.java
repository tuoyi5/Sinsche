package com.android.arvin.ui.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.arvin.R;
import com.android.arvin.util.AnimationUtils;

/**
 * Created by tuoyi on 2017/9/9 0009.
 */

public class DeviceDialog extends DialogFragment {

    private TextView itemNameText;
    private ImageView dialogCloseImage;
    public static DeviceDialog instance() {
        Bundle args = new Bundle();
        DeviceDialog fragment = new DeviceDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_layout,container,false);
        AnimationUtils.slideToUp(view);
        initView(view);
        return view;
    }

    private void initView(View view){
        itemNameText = (TextView) view.findViewById(R.id.dialog_item_name);
        dialogCloseImage =  (ImageView) view.findViewById(R.id.dialog_close_image);
        dialogCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceDialog.this.dismiss();
            }
        });
    }

    private void setItemNameText(String string){
        itemNameText.setText(string);
    }
}

