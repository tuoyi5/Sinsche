package com.android.arvin.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.arvin.interfaces.PhoneNumberCallback;
import com.android.arvin.util.SMSCore;

/**
 * Created by tuoyi on 2017/9/22 0022.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = SMSReceiver.class.getSimpleName();
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    private String numberAddress = "10001";
    private PhoneNumberCallback phoneNumberCallback;

    public void setNumberAddress(String numberAddress) {
        this.numberAddress = numberAddress;
    }

    public void setPhoneNumberCallback(PhoneNumberCallback phoneNumberCallback) {
        this.phoneNumberCallback = phoneNumberCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onReceive: " + intent.getAction());
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] message = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();

            String address = "";
            for (int i = 0; i < pdus.length; i++) {
                message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                address = message[i].getDisplayOriginatingAddress();
                sb.append(address + "\n");
                sb.append(message[i].getDisplayMessageBody());
            }

            Log.d(TAG, "SMS: " + sb.toString());

            if (SMSCore.PhoneNumber == "" && address.equals(numberAddress)) {
                SMSCore.PhoneNumber = SMSCore.getPhoneNumberFromSMSText(sb.toString());

                if (phoneNumberCallback != null) {
                    phoneNumberCallback.numberCallback(SMSCore.PhoneNumber);
                }
            }
        }

    }

}
