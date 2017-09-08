package com.android.arvin.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.arvin.R;

/**
 * Created by arvin on 2017/9/7 0007.
 */
public abstract class DtAppCompatActivity extends AppCompatActivity {

    final static String TAG = DtAppCompatActivity.class.getSimpleName();
    protected ActionBar actionBar;
    private boolean isCustomBackFunctionLayout = true;

    protected void initSupportActionBarWithCustomBackFunction() {
        initSupportActionBar(R.id.tool_bar, true);
    }

    protected void initSupportActionBar(int toolbarLayoutID, boolean customBackFunctionLayout) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarLayoutID);
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            isCustomBackFunctionLayout = customBackFunctionLayout;
            if (isCustomBackFunctionLayout) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayUseLogoEnabled(false);
            } else {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);

            }
        }
    }
}
