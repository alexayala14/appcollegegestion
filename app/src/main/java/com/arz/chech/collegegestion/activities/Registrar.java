package com.arz.chech.collegegestion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.arz.chech.collegegestion.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.entidades.Administrador;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {
    EditText campoNombre, campoApellido, campoRut, campoContraseña;
    Spinner campoPerfil;
    Button btn_registrar;
    String user, pass;
    ProgressDialog pd;
    // URL del servicio firebase
    String URL_FIREBASE = "https://appcollegegestion.firebaseio.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        campoNombre = (EditText) findViewById(R.id.EditT_Nombre);
        campoApellido = (EditText) findViewById(R.id.EditT_Apellido);
        campoRut = (EditText) findViewById(R.id.EditT_Rut);
        campoContraseña = (EditText) findViewById(R.id.EditT_Password);
        campoPerfil = (Spinner) findViewById(R.id.comboPerfiles);
        btn_registrar = (Button) findViewById(R.id.btn_registrarUsuario);
        // Aplicar el contecto a Firebase
        Firebase.setAndroidContext(this);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = campoRut.getText().toString();
                pass = campoContraseña.getText().toString();
                if (campoNombre.getText().toString().isEmpty()){
                    Toast.makeText(Registrar.this, "Debe ingresar nombre", Toast.LENGTH_SHORT).show();
                }else if (campoApellido.getText().toString().isEmpty()) {
                    Toast.makeText(Registrar.this, "Debe ingresar apellido", Toast.LENGTH_SHORT).show();
                }else if (campoRut.getText().toString().isEmpty()) {
                    Toast.makeText(Registrar.this, "Debe ingresar rut", Toast.LENGTH_SHORT).show();
                }else if (campoContraseña.getText().toString().isEmpty()) {
                    Toast.makeText(Registrar.this, "Debe ingresar contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    final String nombre = campoNombre.getText().toString();
                    final String apellido = campoApellido.getText().toString();
                    final String rut = campoRut.getText().toString();
                    final String password = campoContraseña.getText().toString();
                    final String perfil = String.valueOf(campoPerfil.getSelectedItemPosition()+1);
                    pd = new ProgressDialog(Registrar.this);

                    pd.setMessage("Cargando...");

                    pd.show();
                    String url = URL_FIREBASE + "/users.json";
                    Response.Listener < String > responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Obtener referencia del archivo JSON Users
                                    Firebase reference = new Firebase(URL_FIREBASE + "/users");
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            Toast.makeText(Registrar.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                                            reference.child(user).child("password").setValue(pass);
                                            Intent intent = new Intent(Registrar.this, Administrador.class);
                                            Registrar.this.startActivity(intent);
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Registrar.this);
                                            builder.setMessage("Ya existe el usuario en la Base de Datos")
                                                    .setNegativeButton("Reintentar", null)
                                                    .create().show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    pd.dismiss();

                                }

                            };
                    RegisterRequest registerRequest = new RegisterRequest(nombre, apellido, rut, password, perfil, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Registrar.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}
