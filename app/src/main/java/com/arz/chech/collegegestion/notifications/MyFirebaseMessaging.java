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
import android.widget.Switch;

import com.arz.chech.collegegestion.activities.ChatActivity;
import com.arz.chech.collegegestion.activities.ChatActivityGroup;
import com.arz.chech.collegegestion.activities.MenuPrincipalActivity;
import com.arz.chech.collegegestion.activities.NuevaPublicacionActivity;
import com.arz.chech.collegegestion.entidades.Bandera;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.fragments.GruposFragment;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private String bandera;
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
        //String nomGrupo = remoteMessage.getData().get("nombreGrupo");
        String nomGrupo = remoteMessage.getData().get("sented");
        //String datosUsuarios =remoteMessage.getData().get("datosUsuariosList");
        //Boolean bandera = Boolean.valueOf(remoteMessage.getData().get("bandera"));
        String bandera=remoteMessage.getData().get("bandera");
        String nombregrupo=remoteMessage.getData().get("nombregrupo");
        String participantes=remoteMessage.getData().get("participantes");




        RemoteMessage.Notification notification = remoteMessage.getNotification();


        assert bandera !=null;
        switch(bandera){
            case "1":
                intt=ChatActivity.class;

                break;
            case "2":
                intt=MenuPrincipalActivity.class;

                break;
            case "3":

                intt= ChatActivityGroup.class;

                break;

        }

        Intent intent = new Intent(this,intt);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user);
        bundle.putString("nombreGrupo",user);
        bundle.putString("nombreG",nombregrupo);
        bundle.putString("participantes",participantes);
        intent.putExtras(bundle);
        //intent.putExtra("datosUsuariosList", datosUsuarios);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);


        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon);

        int i = 0;

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
        //String nomGrupo = remoteMessage.getData().get("nombreGrupo");
        String nomGrupo = remoteMessage.getData().get("sented");
        //String datosUsuarios =remoteMessage.getData().get("datosUsuariosList");
        //Boolean bandera = Boolean.valueOf(remoteMessage.getData().get("bandera"));
        String bandera=remoteMessage.getData().get("bandera");
        String nombregrupo=remoteMessage.getData().get("nombregrupo");
        String participantes=remoteMessage.getData().get("participantes");


        RemoteMessage.Notification notification = remoteMessage.getNotification();



        assert bandera!=null;

        switch (bandera) {
                case "1":
                    intt = ChatActivity.class;
                    break;
                case "2":
                    intt = MenuPrincipalActivity.class;
                    break;
                case "3":
                    intt = ChatActivityGroup.class;
                    break;

            }


        Intent intent = new Intent(this,intt);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user);
        bundle.putString("nombreGrupo",user);
        //bundle.putString("nombreGrupo",nomGrupo);
        bundle.putString("nombreG",nombregrupo);
        bundle.putString("participantes",participantes);
        intent.putExtras(bundle);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);


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

        Random r = new Random();
        int randomNoID = r.nextInt(1000+1);
        noti.notify(randomNoID, builder.build());
    }


}