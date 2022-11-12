package com.techalgo.spendplus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class ReceiveSms extends BroadcastReceiver {
    String check;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle b = intent.getExtras();
            SmsMessage[] msg = null;
            if(b!=null){
                try{
                    Object[] pdus = (Object[])b.get("pdus");
                    msg = new SmsMessage[pdus.length];
                    for(int i=0;i<msg.length;i++){
                        msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        String msgBody = msg[i].getMessageBody();
                        if(msgContains(msgBody)){
                            //yahan notification wala feature daalna hai
                            //check if-else apply krna hai
                            Intent int2 = new Intent(context, MainActivity.class);
                            int2.putExtra("trxn",check);
                            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), int2, PendingIntent.FLAG_MUTABLE);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"My Notification");
                            builder.setSmallIcon(R.drawable.logo2)
                            .setColor(485)
                            .setWhen(Calendar.getInstance().getTimeInMillis())
                            .setContentTitle("Add Transaction")
                            .setAutoCancel(true)
                            .setContentIntent(pIntent)
                            .setContentText("Do you want to add the transaction ?")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msgBody))
                            .setPriority(NotificationCompat.PRIORITY_MIN);
                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                            managerCompat.notify(1, builder.build());
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean msgContains(String msgBody) {
        boolean res = false;
        msgBody = msgBody.toLowerCase();
        String[] msgs = msgBody.split(" ");
        List<String> arr = Arrays.asList(msgs);

        if(check_message(msgBody,".*\\bdebited\\b.*")){
             res = true;
            check = "debited";
        }
        if(check_message(msgBody,".*\\bcredited\\b.*")){
            check="credited";
            res=true;
        }
        if(check_message(msgBody,".*\\bpaid\\b.*")){
            check="debited";
            res=true;
        }
        if(check_message(msgBody,".*\\breceived\\b.*")){
            check="credited";
            res=true;
        }
        return res;
    }

    private boolean check_message(String s, String t) {
        return Pattern.compile(t).matcher(s).matches();
    }
}
