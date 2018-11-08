package com.arz.chech.collegegestion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.arz.chech.collegegestion.UserDetails;
import com.arz.chech.collegegestion.entidades.Administrador;
import com.arz.chech.collegegestion.entidades.Alumno;
import com.arz.chech.collegegestion.R;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText et_rut, et_password;
    private Button btn_log, btn_log_alumno;
    //firebase
    String user, pass,nom;
    // URL del servicio firebase
    String URL_FIREBASE = "https://appcollegegestion.firebaseio.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    final String rut = et_rut.getText().toString();
                    final String password = et_password.getText().toString();
                    // URL del servicio Firebase
                    String url = URL_FIREBASE+"/users.json";
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
                                            .setNegativeButton("Volver", null)
                                            .create().show();
                                } else {
                                    String nombre = jsonResponse.getString("nombre");
                                    int id_perfil = jsonResponse.getInt("id_perfil");
                                    if(id_perfil == 1){
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        Intent intent = new Intent(MainActivity.this, Administrador.class);
                                        MainActivity.this.startActivity(intent);
                                    }else{
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        Intent intent = new Intent(MainActivity.this, MenuPrincipalActivity.class);
                                        intent.putExtra("nombre", nombre);
                                        MainActivity.this.startActivity(intent);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pd.dismiss();
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(rut, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(loginRequest);
                }
            }
        });

    }

}