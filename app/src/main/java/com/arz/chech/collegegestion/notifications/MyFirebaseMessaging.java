package com.arz.chech.collegegestion.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.arz.chech.collegegestion.activities.ChatActivity;
import com.arz.chech.collegegestion.activities.MenuPrincipalActivity;
import com.arz.chech.collegegestion.activities.NuevaPublicacionActivity;
import com.arz.chech.collegegestion.entidades.Bandera;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    Boolean bandera;
    Boolean banderas;
    Boolean baaan;
    Class intt;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");


        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser", "none");

        String firebaseUser = Preferences.obtenerPreferenceString(MyFirebaseMessaging.this, Preferences.PREFERENCE_TOKEN);

        if (firebaseUser != null && sented.equals(firebaseUser)){
            if (!currentUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    sendOreoNotification(remoteMessage);
                } else {

                    sendNotification(remoteMessage);
                }
            }
        }


    }


    private void sendOreoNotification(RemoteMessage remoteMessage){
        //Class intt=ChatActivity.class;
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Boolean bandera = Boolean.valueOf(remoteMessage.getData().get("bandera"));

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        //int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        if (bandera){
            intt=ChatActivity.class;




            /*SharedPreferences ban = getApplication().getSharedPreferences(ChatActivity.prefGlobant,MODE_PRIVATE);
            SharedPreferences.Editor editor=ban.edit();
            editor.putBoolean(ChatActivity.prefbandera,bandera);
            editor.apply();

            SharedPreferences band = getApplication().getSharedPreferences(NuevaPublicacionActivity.prefGlobants,MODE_PRIVATE);
            SharedPreferences.Editor editors=band.edit();
            editors.putBoolean(NuevaPublicacionActivity.prefbanderas,banderas);
            editors.apply();*/

        }else if(!bandera){
            intt=MenuPrincipalActivity.class;
        }
        /*if(banderas && !bandera){
            intt=MenuPrincipalActivity.class;
            bandera=false;
            banderas=false;*/

            /*SharedPreferences ban = getApplication().getSharedPreferences(ChatActivity.prefGlobant,MODE_PRIVATE);
            SharedPreferences.Editor editor=ban.edit();
            editor.putBoolean(ChatActivity.prefbandera,bandera);
            editor.apply();

            SharedPreferences band = getApplication().getSharedPreferences(NuevaPublicacionActivity.prefGlobants,MODE_PRIVATE);
            SharedPreferences.Editor editors=band.edit();
            editors.putBoolean(NuevaPublicacionActivity.prefbanderas,banderas);
            editors.apply();*/

        //}







        Intent intent = new Intent(this,intt);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user);
        intent.putExtras(bundle);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon);

        int i = 0;
        /*if (j > 0){
            i = j;
        }*/
        Random r = new Random();
        int randomNoID = r.nextInt(1000+1);
        oreoNotification.getManager().notify(randomNoID, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        //Class intt=MenuPrincipalActivity.class;
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Boolean bandera = Boolean.valueOf(remoteMessage.getData().get("bandera"));


        RemoteMessage.Notification notification = remoteMessage.getNotification();

        //int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        if (bandera ){
            intt=ChatActivity.class;
            //bandera=false;


           /* SharedPreferences ban = getApplication().getSharedPreferences(ChatActivity.prefGlobant,MODE_PRIVATE);
            SharedPreferences.Editor editor=ban.edit();
            editor.putBoolean(ChatActivity.prefbandera,bandera);
            editor.apply();*/


        }else if(!bandera){
            intt=MenuPrincipalActivity.class;
        }
        /*if(banderas){
            intt=MenuPrincipalActivity.class;

            banderas=false;



            SharedPreferences band = getApplication().getSharedPreferences(NuevaPublicacionActivity.prefGlobants,MODE_PRIVATE);
            SharedPreferences.Editor editors=band.edit();
            editors.putBoolean(NuevaPublicacionActivity.prefbanderas,banderas);
            editors.apply();
        }*/
        System.out.println("ahora"+bandera);
        //System.out.println("ahoraaaa"+banderas);
        Intent intent = new Intent(this,intt);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user);
        intent.putExtras(bundle);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defaultSound)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        /*if (j > 0){
            i = j;
        }*/
        Random r = new Random();
        int randomNoID = r.nextInt(1000+1);
        noti.notify(randomNoID, builder.build());
    }


}