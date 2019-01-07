package joslabs.kbssmsapp.SMSservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import joslabs.kbssmsapp.SmsClass;

public class SmsReceiver extends BroadcastReceiver {


    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefsms=context.getSharedPreferences("prefnumber",0);
        SharedPreferences prefkey=context.getSharedPreferences("prefkey",0);
        String number1=prefsms.getString("number1",null);
        String number2=prefsms.getString("number2",null);
        String pkey=prefkey.getString("key1",null);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String nowtime=formatter.format(date);
        Bundle data  = intent.getExtras();
       // String number="+254728844034";
        String number="MPESA";

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            Log.e( "Sender is: ",sender+"\n\n" );
            if (sender.equals(number1)||sender.equals(number2)){

                //You must check here if the sender is your provider and not another one with same text.

                String messageBody = smsMessage.getMessageBody();
                String thank= messageBody.substring(messageBody.indexOf("me")+6,messageBody.length());

                Pattern p = Pattern.compile("^[a-zA-Z]+([0-9]+).*");
                Matcher m = p.matcher(messageBody);
                Log.e( "Matcher 1: ",m+"\n\n" );
                Pattern q= Pattern.compile("\\d+");
                Matcher s = q.matcher(messageBody);
                s.find();
                Log.e( "Matcher 2: ",s.group()+"\n\n" );
                String message2=s.group();

                //Pass on the text to our listener.
               // mListener.messageReceived(messageBody);

                mListener.Smsclass(new SmsClass("Sender: "+sender,messageBody,nowtime));
                mListener.messageReceived(messageBody);
            }
            else
            {
                String messageBody = smsMessage.getMessageBody();
                if (messageBody.toUpperCase().contains(pkey.toUpperCase()))
                {
                    mListener.Smsclass(new SmsClass("Sender: "+sender,messageBody,nowtime));
                    mListener.messageReceived(messageBody);
                }

            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
