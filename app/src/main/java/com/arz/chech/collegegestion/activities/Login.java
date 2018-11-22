package com.arz.chech.collegegestion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.arz.chech.collegegestion.R;


import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    // Declaramos las variables
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    // URL del servicio firebase
    String URL_FIREBASE = "https://appcollegegestion.firebaseio.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Obtenemos las referencias de los objetos
        registerUser = (TextView)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);
        // Boton para redirigir a la actividad Registrar
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registers.class));
            }
        });
        // Boton para acceder al MainActivity principal
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores de los controles
                user = username.getText().toString();
                pass = password.getText().toString();
                // Validar si el valor es nulo
                if(user.equals("")){
                    username.setError("No puede estar en blanco");
                } // Validar si el valor es nulo
                else if(pass.equals("")){
                    password.setError("No puede estar en blanco");
                }
                else{
                    // URL del servicio Firebase
                    String url = URL_FIREBASE+"/users.json";
                    // ProgressDialog que se mostrara en espera del servicio
                    final ProgressDialog pd = new ProgressDialog(Login.this);
                    pd.setMessage("Cargando...");
                    pd.show();
                    // Variable request del servicio
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Validar si el request es nullo
                            if(response.equals("null")){
                                Toast.makeText(Login.this, "Usuario no encontrado!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    // Validar si el usuario fue encontrado
                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "Usuario no encontrado!", Toast.LENGTH_LONG).show();
                                    } // Validar si la contraseña es valida
                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                        // Asignar los valores a variables globales
                                        UserDetails.username = user;
                                        // Redirigir al activity
                                        startActivity(new Intent(Login.this, MenuPrincipalActivity.class));
                                    }
                                    else {
                                        // La contraseña es incorrecta
                                        Toast.makeText(Login.this, "Contraseña incorrecta!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            // Finalizar el progressbar
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });
                    // Variables request utilizando la libreria Volley
                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }
            }
        });
    }
}