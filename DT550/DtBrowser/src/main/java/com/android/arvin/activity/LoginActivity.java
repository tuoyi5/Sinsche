package com.android.arvin.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.arvin.DtPreference.DtSharePreference;
import com.android.arvin.R;
import com.android.arvin.Receiver.SMSReceiver;
import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.interfaces.PhoneNumberCallback;
import com.android.arvin.interfaces.UpdateDeviceLayouDataCallback;
import com.android.arvin.util.DtUtils;
import com.android.arvin.util.GAdapter;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by arvin on 2017/9/15 0015.
 */

public class LoginActivity extends DtMAppCompatActivity implements UpdateDeviceLayouDataCallback, PhoneNumberCallback {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private List<ClientInfoRspUserInfo> LoginList;
    private AlertDialog loginDialog;
    private AlertDialog numberDialog;
    private Context context = this;
    private EditText userNameEditText;
    private EditText passWordEditText;
    private CheckBox keepPasswordCheckBox;
    private CheckBox autoLoginCheckBox;
    private Button login_button;
    private EditText dialogSubNumberEditText;

    private boolean loginStart = false;
    private String phoneNumber;
    private boolean resetMessageSendStatus = false;
    private boolean registerSmsReceiverBoolean = false;
    private SMSReceiver smsReceiver;


    protected void onCreate(Bundle savedInstanceState) {
       // DtSharePreference.saveClientData(this, "", "");
       // DtSharePreference.saveServerData(this, "", "");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //  getPhoneNumber();
        initView();
        autoiLogin();
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop() {
        super.onStop();
        unregisterSmsReceiver();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterSmsReceiver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean startWorkThreadPrepare() {
        return getRemoteConnectionData();
    }


    public void initView() {
        userNameEditText = (EditText) findViewById(R.id.user_name_ecit);
        passWordEditText = (EditText) findViewById(R.id.passwork_name_ecit);

        keepPasswordCheckBox = (CheckBox) findViewById(R.id.remember_password_check);
        autoLoginCheckBox = (CheckBox) findViewById(R.id.auto_login_check);

        getKeepPasswordCheckBoxChecked();
        getAutoLoginCheckBoxChecked();

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
                    setKeepPasswordCheckBoxEnabled(false);
                    setAutoLoginCheckBoxEnabled(false);
                } else if (passWordEditText.getText().length() != 0) {
                    if (DtSharePreference.getKeepPassword(context)) {
                        setKeepPasswordCheckBoxEnabled(true);
                    }

                    if (DtSharePreference.getAutoLogin(context)) {
                        setAutoLoginCheckBoxEnabled(true);
                    }
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

                if (keepPasswordCheckBox.isChecked()) {
                    String userName = userNameEditText.getText().toString();
                    String password = passWordEditText.getText().toString();
                    DtSharePreference.saveLoginData(context, userName, password);
                    DtSharePreference.saveKeepPassword(context, keepPasswordCheckBox.isChecked() ? 1 : 0);
                }

                if (autoLoginCheckBox.isChecked() && keepPasswordCheckBox.isChecked()) {
                    DtSharePreference.saveAutoLogin(context, autoLoginCheckBox.isChecked() ? 1 : 0);
                }

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


    @Override
    public void numberCallback(String number) {
        Log.d(TAG, "numberCallback, PhoneNumber: " + number);
        if (resetMessageSendStatus) {
            resetMessageSendStatus = false;
        }
        if (numberDialog != null && numberDialog.isShowing() && dialogSubNumberEditText != null && !DtUtils.isNullOrEmpty(number)) {
            phoneNumber = number;
            dialogSubNumberEditText.setText(number);
        }
        unregisterSmsReceiver();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String str = bundle.getString("result");
                    if (str.length() > 0) {
                        String strs[] = str.split(";");
                        if (strs.length == 4) {
                            DtSharePreference.saveClientData(this, strs[2], strs[3]);
                            DtSharePreference.saveServerData(this, strs[0], strs[1]);
                            startWorkThread();
                            login();
                            Log.d(TAG, "扫描二维码的数据：：" + str + " : " + startWorkThreadPrepare());
                            return;
                        }
                    }
                    Toast.makeText(context, getString(R.string.qr_code_data_error), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void startLogin() {
        String userName = userNameEditText.getText().toString();
        String password = passWordEditText.getText().toString();

        for (ClientInfoRspUserInfo info : LoginList) {
            if (info.getStrPassword().equals(password) && info.getStrUername().equals(userName)) {
                loginDialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(context, MainActivity.class);
                startActivity(intent);
                this.finish();
                return;
            }
        }
        //账户密码验证失败
        closeDialog();
        Toast.makeText(context, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
    }


    public void login() {
        if (DtUtils.isNullOrEmpty(userNameEditText.getText().toString()) || DtUtils.isNullOrEmpty(passWordEditText.getText().toString())) {
            Toast.makeText(context, getString(R.string.pls_edit_username_and_pw), Toast.LENGTH_SHORT).show();
            return;
        }

        String number = DtSharePreference.getPhoneNum(this);
        if (!DtUtils.isNullOrEmpty(number)) {
            if (getRemoteConnectionData()) {
                showLoginDialog();
            } else {
                Toast.makeText(context, getString(R.string.binding_error), Toast.LENGTH_LONG).show();

                //Intent openCameraIntent = new Intent(this, CaptureActivity.class);
               Intent openCameraIntent = new Intent(this, DecoderActivity.class);

                startActivityForResult(openCameraIntent, SCANNIN_GREQUEST_CODE);
            }
        } else {
            if (!DtUtils.isSimCard(context)) {
                Toast.makeText(context, getString(R.string.phone_sim_error), Toast.LENGTH_SHORT).show();
                // return;
            }
            showNumberDialog();
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
            keepPasswordCheckBox.setChecked(false);
            autoLoginCheckBox.setChecked(false);
        }
    }

    private void setKeepPasswordCheckBoxChecked(boolean status) {
        DtSharePreference.saveKeepPassword(context, status ? 1 : 0);
        keepPasswordCheckBox.setChecked(status);
    }

    private boolean getRemoteConnectionData() {
        return DtSharePreference.getServerIP(context).length() > 0
                && DtSharePreference.getServerPort(context).length() > 0
                && DtSharePreference.getClientName(context).length() > 0
                && DtSharePreference.getClientSerial(context).length() > 0;
    }

    private String getPhoneNumber_notuse() {
        String number = DtSharePreference.getPhoneNum(this);
        if (DtUtils.isNullOrEmpty(number)) {
            number = DtUtils.getPhoneNumber(context);
            if (DtUtils.isNullOrEmpty(number)) {
                String operator = DtUtils.getSimOperator(context);
                if (operator != null) {
                    DtUtils.sendMessage(operator, context, registerSmsReceiver());
                    resetMessageSendStatus = true;
                }
            } else {
                DtSharePreference.savePhoneNum(context, number);
                phoneNumber = number;
            }
        } else {
            phoneNumber = number;
        }
        Log.d(TAG, "getPhoneNumber: " + phoneNumber);
        return phoneNumber;
    }

    private SMSReceiver registerSmsReceiver() {
        smsReceiver = new SMSReceiver();
        smsReceiver.setPhoneNumberCallback(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
        registerSmsReceiverBoolean = true;
        return smsReceiver;
    }

    private void unregisterSmsReceiver() {
        if (smsReceiver != null && registerSmsReceiverBoolean) {
            registerSmsReceiverBoolean = false;
            unregisterReceiver(smsReceiver);
        }
    }

    private void closeDialog() {
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
            loginDialog = null;
        }

        if (numberDialog != null && numberDialog.isShowing()) {
            numberDialog.dismiss();
            numberDialog = null;
        }
    }

    private void showLoginDialog() {
        closeDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.loging));
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginStart = false;
            }
        });

        builder.setCancelable(false);
        loginDialog = builder.create();
        loginDialog.show();

        if (LoginList != null) {
            startLogin();
        } else {
            loginStart = true;
        }
    }

    private void showNumberDialog() {
        closeDialog();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.number_dialog_sub_layout, (ViewGroup) findViewById(R.id.number_edit_layout));
        dialogSubNumberEditText = (EditText) layout.findViewById(R.id.number_edit);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.loging));
        builder.setView(layout);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetMessageSendStatus = false;
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetMessageSendStatus = false;
                String number = dialogSubNumberEditText.getText().toString();
                if (!DtUtils.isNullOrEmpty(number)) {
                    DtSharePreference.savePhoneNum(context, number);
                    login();
                }
            }
        });

        builder.setCancelable(false);
        numberDialog = builder.create();
        numberDialog.show();
    }


}
