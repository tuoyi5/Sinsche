package com.android.arvin.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.arvin.R;

/**
 * Created by arvin on 2017/9/19 0019.
 */

public class DtMAppCompatActivity extends DtAppCompatActivity {
    final static String TAG = DtMAppCompatActivity.class.getSimpleName();

    protected ActionBar actionBar;
    private boolean isCustomBackFunctionLayout = true;

    public Typeface mTfRegular;
    public Typeface mTfLight;

    private boolean isQuit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    public void quitApp(DtAppCompatActivity activity) {
        if (!isQuit) {
            Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            isQuit = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        isQuit = false;
                    }
                }
            }).start();

        } else {
            System.exit(0);
        }
    }
}
