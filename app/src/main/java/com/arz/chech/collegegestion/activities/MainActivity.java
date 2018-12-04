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
    private boolean sesion;
    private String nom;
    private String ape;
    private int log;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Preferences.obtenerPreferenceBoolean(this, Preferences.PREFERENCE_ESTADO_SESION)){
            if (Preferences.obtenerPreferenceInt(this, Preferences.PREFERENCE_ESTADO_ID_PERFIL)!= 1){
                UserDetails.username = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO);
                UserDetails.token = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
                Intent intent = new Intent(MainActivity.this, MenuPrincipalActivity.class);
                startActivity(intent);
                finish();
            }else{
                UserDetails.username = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO);
                UserDetails.token = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
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
                user = et_rut.getText().toString().trim();
                pass = et_password.getText().toString().trim();
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
                                    token = jsonResponse.getString("token");
                                    nom=jsonResponse.getString("nombre");
                                    ape=jsonResponse.getString("apellido");
                                    log = id_perfil;
                                    sesion = true;
                                    Preferences.savePreferenceBoolean(MainActivity.this, sesion, Preferences.PREFERENCE_ESTADO_SESION);
                                    Preferences.savePreferenceString(MainActivity.this, token, Preferences.PREFERENCE_TOKEN);
                                    Preferences.savePreferenceInt(MainActivity.this, log, Preferences.PREFERENCE_ESTADO_ID_PERFIL);
                                    Preferences.savePreferenceString(MainActivity.this, user, Preferences.PREFERENCE_USUARIO);
                                    Preferences.savePreferenceString(MainActivity.this, nom, Preferences.PREFERENCE_NOMBRE);
                                    Preferences.savePreferenceString(MainActivity.this, ape, Preferences.PREFERENCE_APELLIDO);
                                    if(id_perfil == 1){
                                        UserDetails.username = user;
                                        UserDetails.token = token;
                                        UserDetails.nombre=nom;
                                        UserDetails.apellido=ape;
                                        Intent intent = new Intent(MainActivity.this, Administrador.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        UserDetails.username = user;
                                        UserDetails.token = token;
                                        UserDetails.nombre=nom;
                                        UserDetails.apellido=ape;
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
}