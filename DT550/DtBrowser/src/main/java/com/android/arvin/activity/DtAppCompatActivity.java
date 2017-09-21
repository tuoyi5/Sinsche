package com.android.arvin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.arvin.DtPreference.DtSharePreference;
import com.android.arvin.Manager.DeviceManager;
import com.android.arvin.interfaces.UpdateDeviceLayouDataCallback;

/**
 * Created by arvin on 2017/9/7 0007.
 */
public abstract class DtAppCompatActivity extends AppCompatActivity implements UpdateDeviceLayouDataCallback {

    final static String TAG = DtAppCompatActivity.class.getSimpleName();

    private boolean isQuit = false;
    public DeviceManager deviceManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void Resume() {
        super.onResume();

        deviceManager = DeviceManager.instantiation(this, DtSharePreference.getServerIP(this), Integer.parseInt(DtSharePreference.getServerPort(this)), DtSharePreference.getClientSerial(this), DtSharePreference.getClientName(this), this);

        if (deviceManager.getDt550RealDataRspDeviceList() != null) {
            deviceManager.requestFormCurrentlyData(deviceManager.getDt550RealDataRspDeviceList());
        }

        if (deviceManager.getClientName() != null) {
            deviceManager.requestSubTitle();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            deviceManager.stop();
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
