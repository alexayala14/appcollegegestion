package com.arz.chech.collegegestion.services;


import android.app.Service;

import android.content.Intent;

import android.os.IBinder;
import android.support.annotation.Nullable;


public class ServiceMensajes extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
