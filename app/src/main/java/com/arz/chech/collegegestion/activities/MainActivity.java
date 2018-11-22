package com.arz.chech.collegegestion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.entidades.Administrador;
import com.arz.chech.collegegestion.entidades.Alumno;
import com.arz.chech.collegegestion.R;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText et_rut, et_password;
    private Button btn_log, btn_log_alumno;
    private String user;
    private String pass;
    private static final String STRING_PREFERENCES = "collegegestion.sharedpreferences";
    private static final String PREFERENCE_ESTADO_SESION = "collegegestion.sesion.activa";
    private static final String PREFERENCE_ESTADO_ID_PERFIL = "collegegestion.sesion.id_perfil";
    private static final String PREFERENCE_USUARIO = "collegegestion.sesion.usuario";
    private boolean sesion;
    private int log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (obtener_mantener_sesion()){
            if (obtener_id_sesion() != 1){
                UserDetails.username = obtener_usuario();
                Intent intent = new Intent(MainActivity.this, MenuPrincipalActivity.class);
                startActivity(intent);
                finish();
            }else{
                UserDetails.username = obtener_usuario();
                Intent intent = new Intent(MainActivity.this, Administrador.class);
                startActivity(intent);
                finish();
            }
        }
        et_rut = (EditText) findViewById(R.id.et_rut);
        et_password = (EditText) findViewById(R.id.et_pw);
        btn_log = (Button) findViewById(R.id.btn_iniciar);
        btn_log_alumno = (Button) findViewById(R.id.btn_iniciar_alumno);
        btn_log_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Alumno.class);
                MainActivity.this.startActivity(intent);
            }
        });
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores de los controles firebase
                user = et_rut.getText().toString();
                pass = et_password.getText().toString();
                /////
                if (et_rut.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Debe ingresar rut", Toast.LENGTH_SHORT).show();
                }else if (et_password.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Debe ingresar contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    // ProgressDialog que se mostrara en espera del servicio
                    final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Cargando...");
                    pd.show();
                    /////////
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (!success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Rut y/o Contraseña Incorrecto")
                                            .setNegativeButton("Aceptar", null)
                                            .create().show();
                                } else {
                                    int id_perfil = jsonResponse.getInt("id_perfil");
                                    log = id_perfil;
                                    sesion = true;
                                    guardar_mantener_sesion();
                                    if(id_perfil == 1){
                                        UserDetails.username = user;
                                        Intent intent = new Intent(MainActivity.this, Administrador.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        UserDetails.username = user;
                                        Intent intent = new Intent(MainActivity.this, MenuPrincipalActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pd.dismiss();
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(user, pass, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(loginRequest);
                }
            }
        });
    }

    private void guardar_mantener_sesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_SESION, sesion).apply();
        preferences.edit().putInt(PREFERENCE_ESTADO_ID_PERFIL, log).apply();
        preferences.edit().putString(PREFERENCE_USUARIO, user).apply();
    }

    private boolean obtener_mantener_sesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_SESION, false);
    }

    private int obtener_id_sesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getInt(PREFERENCE_ESTADO_ID_PERFIL, -1);
    }

    private String obtener_usuario(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getString(PREFERENCE_USUARIO, "");
    }

    public static void cambiar_mantener_sesion(Context c, Boolean b){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_SESION, b).apply();
    }
}