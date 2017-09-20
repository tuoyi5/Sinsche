package com.android.arvin.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.arvin.DtPreference.DtSharePreference;
import com.android.arvin.Manager.DeviceManager;
import com.android.arvin.R;
import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.interfaces.UpdateDeviceLayouDataCallback;
import com.android.arvin.util.GAdapter;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by arvin on 2017/9/15 0015.
 */

public class LoginActivity extends DtMAppCompatActivity implements UpdateDeviceLayouDataCallback {

    private static final String TAG = LoginActivity.class.getSimpleName();

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
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }


    public void initView() {
        userNameEditText = (EditText) findViewById(R.id.user_name_ecit);
        passWordEditText = (EditText) findViewById(R.id.passwork_name_ecit);

        keepPasswordCheckBox = (CheckBox) findViewById(R.id.remember_password_check);
        autoLoginCheckBox = (CheckBox) findViewById(R.id.auto_login_check);

        getKeepPasswordCheckBoxChecked();
        getAutoLoginCheckBoxChecked();
        //getKeepPasswordCheckBoxEnabled();

        if (keepPasswordCheckBox.isChecked()
                && DtSharePreference.getLoginUserName(context).length() != 0
                && DtSharePreference.getLoginPassword(context).length() != 0) {
            userNameEditText.setText(DtSharePreference.getLoginUserName(context));
            passWordEditText.setText(DtSharePreference.getLoginPassword(context));
        } else if (DtSharePreference.getLoginUserName(context).length() == 0 || DtSharePreference.getLoginPassword(context).length() == 0) {
            setKeepPasswordCheckBoxChecked(false);
            userNameEditText.setText(getString(R.string.administratir));
            passWordEditText.setText(getString(R.string.password));
            if (userNameEditText.isFocusable()) {
                userNameEditText.setSelection(userNameEditText.getText().length());
            }

        }


        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLenght = s.toString().length();
                if (textLenght == 0) {
                    userNameEditText.setHint(R.string.please_enter_user_name);
                    setKeepPasswordCheckBoxEnabled(false);
                    setAutoLoginCheckBoxEnabled(false);
                } else if (passWordEditText.getText().length() != 0) {
                    setKeepPasswordCheckBoxEnabled(true);
                    setAutoLoginCheckBoxEnabled(true);
                }
            }
        });

        passWordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLenght = s.toString().length();
                if (textLenght == 0) {
                    passWordEditText.setHint(R.string.please_enter_password);
                    setKeepPasswordCheckBoxEnabled(false);
                    setAutoLoginCheckBoxEnabled(false);
                } else if (userNameEditText.getText().length() != 0) {
                    setKeepPasswordCheckBoxEnabled(true);
                    setAutoLoginCheckBoxEnabled(true);
                }
            }
        });


        keepPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    keepPasswordCheckBox.setChecked(isChecked);
                }
            }
        });

        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    keepPasswordCheckBox.setChecked(isChecked);
                    setKeepPasswordCheckBoxEnabled(false);
                } else {
                    DtSharePreference.saveAutoLogin(context, 0);
                    setKeepPasswordCheckBoxEnabled(true);
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
    public void releaseClientName(String name) {

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
    public void releaseDeviceDataBack(Map<String, DeviceData> deviceDataMap) {

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
                if (keepPasswordCheckBox.isChecked()) {
                    DtSharePreference.saveLoginData(context, userName, password);
                    DtSharePreference.saveKeepPassword(context, keepPasswordCheckBox.isChecked() ? 1 : 0);
                }

                if (autoLoginCheckBox.isChecked() && keepPasswordCheckBox.isChecked()) {
                    DtSharePreference.saveAutoLogin(context, autoLoginCheckBox.isChecked() ? 1 : 0);
                }
                aDialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(context, MainActivity.class);
                startActivity(intent);
                this.finish();
                return;
            }
        }

        if (aDialog != null) {
            aDialog.dismiss();
            aDialog = null;
        }

        Toast.makeText(context, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();

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
            login();
        }
    }

    private void setKeepPasswordCheckBoxEnabled(boolean status) {
        if (!status || autoLoginCheckBox.isChecked()) {
            keepPasswordCheckBox.setEnabled(false);
        } else {
            keepPasswordCheckBox.setEnabled(true);
        }
    }

    private void setAutoLoginCheckBoxEnabled(boolean status) {
        autoLoginCheckBox.setEnabled(status);
    }

    private void getKeepPasswordCheckBoxChecked() {
        if (DtSharePreference.getKeepPassword(context)) {
            keepPasswordCheckBox.setChecked(true);
        } else {
            keepPasswordCheckBox.setChecked(false);
        }
    }

    private void getAutoLoginCheckBoxChecked() {
        if (DtSharePreference.getAutoLogin(context)) {
            autoLoginCheckBox.setChecked(true);
            keepPasswordCheckBox.setChecked(true);
            DtSharePreference.saveKeepPassword(context, 1);
        } else {
            autoLoginCheckBox.setChecked(false);
        }
    }

    private void setKeepPasswordCheckBoxChecked(boolean status) {
        DtSharePreference.saveKeepPassword(context, status ? 1 : 0);
        keepPasswordCheckBox.setChecked(status);
    }
}
