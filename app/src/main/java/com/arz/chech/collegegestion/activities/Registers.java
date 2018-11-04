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
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Registers extends AppCompatActivity {
    // Declarar variables
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;
    ProgressDialog pd;
    // URL del servicio firebase
    String URL_FIREBASE = "https://appcollegegestion.firebaseio.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registers);
        // Obtener referencia de los controles
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);
        // Aplicar el contecto a Firebase
        Firebase.setAndroidContext(this);
        // Direccionar al Login Activity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registers.this, Login.class));
            }
        });
        // Registrar usuario
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores de los EditText
                user = username.getText().toString();
                pass = password.getText().toString();
                // Validar si el User fue ingresado
                if(user.equals("")){
                    username.setError("No puede estar en blanco");
                } // Validar si la contraseña fue ingresada
                else if(pass.equals("")){
                    password.setError("No puede estar en blanco");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Solo alfabeto o números permitidos");
                }
                //else if(user.length()<5 5="" argando...="" caracteres="" de="" direccion="" egister.this="" el="" else="" request.method.get="" final="" if="" l="" largo="" los="" menos="" mostrar="" new="" pass.length="" password.seterror="" pd.setmessage="" pd.show="" pd="new" progressbar="" progressdialog="" que="" recibe="" request="new" response.listener="" string=""
                pd = new ProgressDialog(Registers.this);

                pd.setMessage("Cargando...");

                pd.show();

                // Variable para hacer referencia al archivo JSON del servicio Firebase

                String url = URL_FIREBASE + "/users.json";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        // Obtener referencia del archivo JSON Users
                        Firebase reference = new Firebase(URL_FIREBASE + "/users");
                        // Verificar response es null
                        if(s.equals("null")) {
                            // Mostrar mensaje de que el registro es Exitoso!
                            reference.child(user).child("password").setValue(pass);
                            Toast.makeText(Registers.this, "Registro exitoso!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject obj = new JSONObject(s);
                                // Validar si el usuario existe
                                if (!obj.has(user)) {
                                    // Mostrar mensaje de registro exitoso! Y direccionar al activity Login
                                    reference.child(user).child("password").setValue(pass);
                                    Toast.makeText(Registers.this, "Registro exitoso!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Registers.this, Login.class);
                                    startActivity(i);
                                } else {
                                    // Mostrar mensaje que el usuario ya existe!
                                    Toast.makeText(Registers.this, "Nombre de usuario ya existe!", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Terminar progreess dialog
                        pd.dismiss();
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError );
                        pd.dismiss();
                    }
                });
                // Procesar el request
                RequestQueue rQueue = Volley.newRequestQueue(Registers.this);
                rQueue.add(request);
            }

    });
    }
}
