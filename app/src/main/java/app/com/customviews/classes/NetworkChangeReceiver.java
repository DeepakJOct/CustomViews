package app.com.customviews.classes;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import app.com.customviews.fragments.BroadcastReceiverFragment;
import app.com.customviews.interfaces.DataListener;


public class NetworkChangeReceiver extends BroadcastReceiver {

    NetworkRequest networkRequest;
    final SmsManager sms = SmsManager.getDefault();
    private DataListener mDataListener;
    String data;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("API123", intent.getAction());
        try {
            if (isOnline(context)) {
                BroadcastReceiverFragment.dialog(true);
                Log.d("NetworkChangeReceiver", "Online. Connected to internet.");
            } else {
                BroadcastReceiverFragment.dialog(false);
                Log.d("NetworkChangeReceiver", "Offline. No internet.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        //To receive a message
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {

                SmsMessage currentMsg;
                Log.d("NetworkChangeReceiver", "Message Detected");
                if (Build.VERSION.SDK_INT >= 19) {
                    SmsMessage[] sms = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    for (int i = 0; i < sms.length; i++) {
                        currentMsg = sms[i];
                        String phoneNumber = currentMsg.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;

                        String msg = currentMsg.getDisplayMessageBody();
                        Log.d("SmsReceiver--->", "senderNum: " + senderNum + "; message: " + msg);

                        data = "senderNum: " + senderNum + "; message: " + msg;
                    }
                } else {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        currentMsg = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMsg.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;

                        String msg = currentMsg.getDisplayMessageBody();
                        Log.d("SmsReceiver--->", "senderNum: " + senderNum + "; message: " + msg);

                        data = "senderNum: " + senderNum + "; message: " + msg;
                    }
                }
                BroadcastReceiverFragment.getDataFromReceiver(data);
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }

    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setListener(DataListener dataListener) {
        mDataListener = dataListener;
    }
}
