package com.android.arvin.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.arvin.R;

/**
 * Created by arvin on 2017/9/7 0007.
 */
public abstract class DtAppCompatActivity extends AppCompatActivity {

    final static String TAG = DtAppCompatActivity.class.getSimpleName();

    private boolean isQuit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            quitApp(this);
            return true;
        }
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
