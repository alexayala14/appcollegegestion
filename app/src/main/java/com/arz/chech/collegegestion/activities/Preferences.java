package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String STRING_PREFERENCES = "collegegestion.sharedpreferences";
    public static final String PREFERENCE_ESTADO_SESION = "collegegestion.sesion.activa";
    public static final String PREFERENCE_ESTADO_ID_PERFIL = "collegegestion.sesion.id_perfil";
    public static final String PREFERENCE_USUARIO = "collegegestion.sesion.usuario";
    public static final String PREFERENCE_TOKEN = "collegegestion.sesion.token";
    public static final String PREFERENCE_MENSAJES = "collegegestion.sesion.mensajes";

    public static void savePreferenceInt(Context c, int b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putInt(key, b).apply();
    }
    public static void savePreferenceBoolean(Context c, Boolean b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putBoolean(key, b).apply();
    }
    public static void savePreferenceString(Context c, String b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putString(key, b).apply();
    }

    public static boolean obtenerPreferenceBoolean(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static int obtenerPreferenceInt(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }

    public static String obtenerPreferenceString(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
