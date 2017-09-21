package com.android.arvin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.arvin.DtPreference.DtSharePreference;
import com.android.arvin.Manager.DeviceManager;
import com.android.arvin.R;
import com.android.arvin.interfaces.UpdateDeviceLayouDataCallback;

/**
 * Created by arvin on 2017/9/7 0007.
 */
public abstract class DtAppCompatActivity extends AppCompatActivity implements UpdateDeviceLayouDataCallback {

    final static String TAG = DtAppCompatActivity.class.getSimpleName();

    private boolean isQuit = false;
    public DeviceManager deviceManager = null;

    protected final static int SCANNIN_GREQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();

        if (DtSharePreference.getServerIP(this).length() > 0 && DtSharePreference.getServerPort(this).length() > 0 && DtSharePreference.getClientName(this).length() > 0 && DtSharePreference.getClientSerial(this).length() > 0) {
            deviceManager = DeviceManager.instantiation(this, DtSharePreference.getServerIP(this), Integer.parseInt(DtSharePreference.getServerPort(this)), DtSharePreference.getClientSerial(this), DtSharePreference.getClientName(this), this);

            if (deviceManager.getDt550RealDataRspDeviceList() != null) {
                deviceManager.requestFormCurrentlyData(deviceManager.getDt550RealDataRspDeviceList());
            }

            if (deviceManager.getClientName() != null) {
                deviceManager.requestSubTitle();
            }
        } else {
            Toast.makeText(this, getString(R.string.binding_error), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(this, MipcaActivityCapture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
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
