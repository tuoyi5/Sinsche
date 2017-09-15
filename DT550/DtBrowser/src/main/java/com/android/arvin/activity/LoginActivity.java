package com.android.arvin.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.arvin.DtPreference.DtSharePreference;
import com.android.arvin.Manager.DeviceManager;
import com.android.arvin.R;
import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.interfaces.UpdateDeviceLayouDataCallback;
import com.android.arvin.util.GAdapter;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;

import java.util.List;

/**
 * Created by arvin on 2017/9/15 0015.
 */

public class LoginActivity extends AppCompatActivity implements UpdateDeviceLayouDataCallback {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private DeviceManager deviceManager = null;
    private List<ClientInfoRspUserInfo> LoginList;
    private AlertDialog aDialog;
    private Context context = this;
    private EditText userNameEditText;
    private EditText passWordEditText;
    private CheckBox keepPasswordCheckBox;
    private CheckBox autoLoginCheckBox;
    private Button login_button;
    private boolean loginStart = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        autoiLogin();
    }

    public void onResume() {
        super.onResume();
        DeviceManager.instantiation(this, this);
    }

    public void initView() {
        userNameEditText = (EditText) findViewById(R.id.user_name_ecit);
        passWordEditText = (EditText) findViewById(R.id.passwork_name_ecit);

        keepPasswordCheckBox = (CheckBox) findViewById(R.id.remember_password_check);
        autoLoginCheckBox = (CheckBox) findViewById(R.id.auto_login_check);

        keepPasswordCheckBox.setChecked(DtSharePreference.getKeepPassword(context));
        autoLoginCheckBox.setChecked(DtSharePreference.getAutoLogin(context));
        if (DtSharePreference.getAutoLogin(context)) {
            keepPasswordCheckBox.setEnabled(false);
        } else {
            keepPasswordCheckBox.setEnabled(true);
        }

        if (keepPasswordCheckBox.isChecked()) {
            userNameEditText.setText(DtSharePreference.getLoginUserName(context));
            passWordEditText.setText(DtSharePreference.getLoginPassword(context));
        }


        keepPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    keepPasswordCheckBox.setChecked(isChecked);
                    DtSharePreference.saveKeepPassword(context, 1);
                } else {
                    DtSharePreference.saveAutoLogin(context, 0);
                    DtSharePreference.saveAutoLogin(context, 0);
                }
            }
        });

        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    keepPasswordCheckBox.setChecked(isChecked);
                    keepPasswordCheckBox.setEnabled(false);
                    DtSharePreference.saveKeepPassword(context, 1);
                    DtSharePreference.saveAutoLogin(context, 1);
                } else {
                    keepPasswordCheckBox.setEnabled(true);
                    DtSharePreference.saveAutoLogin(context, 0);
                }
            }
        });

        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void releaseLoginData(List<ClientInfoRspUserInfo> list) {
        LoginList = list;

        if (loginStart) {
            loginStart = false;
            startLogin();
        }
    }

    @Override
    public void releaseDeviceDataBack(DeviceData deviceData) {

    }

    @Override
    public void releaseDeviceHisDataBack(DeviceHistoryData historyData) {

    }

    @Override
    public void getGadpterBack(String deviceCode, GAdapter gAdapter) {

    }

    public void startLogin() {
        String userName = userNameEditText.getText().toString();
        String password = passWordEditText.getText().toString();

        for (ClientInfoRspUserInfo info : LoginList) {
            if (info.getStrPassword().equals(password) && info.getStrUername().equals(userName)) {
                if (DtSharePreference.getKeepPassword(context)) {
                    DtSharePreference.saveLoginData(context, userName, password);

                }
                aDialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(context, MainActivity.class);
                startActivity(intent);
            }
        }
    }


    public void login() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.loging));
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginStart = false;
            }
        });
        builder.setCancelable(false);
        aDialog = builder.create();
        aDialog.show();

        if (LoginList != null) {
            startLogin();
        } else {
            loginStart = true;
        }
    }

    public void autoiLogin() {
        if (DtSharePreference.getAutoLogin(context) && DtSharePreference.getKeepPassword(context)) {
            String userName = userNameEditText.getText().toString();
            String password = passWordEditText.getText().toString();
            login();
        }
    }
}
