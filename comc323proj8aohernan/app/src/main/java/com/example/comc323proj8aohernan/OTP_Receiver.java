package com.example.comc323proj8aohernan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class OTP_Receiver extends BroadcastReceiver {

    private static EditText passwordEditText;
    private static TextView OTPText;
    private static TextView code;

    public void setPasswordEditText(EditText passwordEditText){
        OTP_Receiver.passwordEditText = passwordEditText;
    }

    public void setOTPTextView(TextView OTPText){
        OTP_Receiver.OTPText = OTPText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] message = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for(SmsMessage sms : message){
            String otp;
            String msg = sms.getMessageBody();
            if(msg.contains(":")) {
                otp = msg.split(": ")[1];
            }else{
                otp = msg.replaceAll("[^0-9]", "");
            }
            passwordEditText.setText(otp);
            OTPText.setText(msg);

        }
    }

}
