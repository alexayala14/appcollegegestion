package com.arz.chech.collegegestion.entidades;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.activities.AllUsersRequest;
import com.arz.chech.collegegestion.activities.RequestUsuarios;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.LoginRequest;
import com.arz.chech.collegegestion.activities.MainActivity;
import com.arz.chech.collegegestion.activities.MenuPrincipalActivity;
import com.arz.chech.collegegestion.activities.Preferences;
import com.arz.chech.collegegestion.activities.RecyclerUsuarios;
import com.arz.chech.collegegestion.activities.Registrar;
import com.arz.chech.collegegestion.activities.UserDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.arz.chech.collegegestion.activities.UserDetails.token;

public class Administrador extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    Button btn_agregarUsuarios, btn_consultarUsuarios,btn_publicacionesymensajes, btn_cerrar_sesion;
    final String tokenAdmin = "-LRwhxacnIQ7KdFeajkd";
    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue;
    DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show();
        btn_cerrar_sesion = (Button) findViewById(R.id.cerrarSesion);
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.savePreferenceBoolean(Administrador.this, false, Preferences.PREFERENCE_ESTADO_SESION);
                Intent intent = new Intent(Administrador.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_agregarUsuarios = (Button) findViewById(R.id.btn_agregarUsuarios);
        btn_agregarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Administrador.this, Registrar.class);
                Administrador.this.startActivity(intent);
            }
        });

        btn_consultarUsuarios = (Button) findViewById(R.id.btn_consultarUsuarios);
        btn_consultarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Administrador.this, RecyclerUsuarios.class);
                Administrador.this.startActivity(intent);
            }
        });
        btn_publicacionesymensajes=(Button)findViewById(R.id.mensajesPublicaciones);
        btn_publicacionesymensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Administrador.this, MenuPrincipalActivity.class);
                Administrador.this.startActivity(intent);
            }
        });
        cargarWebService();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                        String key = datasnapshot.getKey();
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    JSONArray jsonRut = jsonResponse.getJSONArray("rut");
                                    JSONArray jsonNombre = jsonResponse.getJSONArray("nombre");
                                    JSONArray jsonApellido = jsonResponse.getJSONArray("apellido");
                                    JSONArray jsonToken = jsonResponse.getJSONArray("token");
                                    JSONArray jsonPerfil = jsonResponse.getJSONArray("id_perfil");
                                    for (int i = 0; i < jsonRut.length(); i++) {
                                        mUsersDatabase.child(jsonToken.get(i).toString()).child("apellido").setValue(jsonApellido.get(i).toString());
                                        mUsersDatabase.child(jsonToken.get(i).toString()).child("nombre").setValue(jsonNombre.get(i).toString());
                                        mUsersDatabase.child(jsonToken.get(i).toString()).child("rut").setValue(jsonRut.get(i).toString());
                                        mUsersDatabase.child(jsonToken.get(i).toString()).child("perfil").setValue(jsonPerfil.get(i).toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        AllUsersRequest usuariosRequest = new AllUsersRequest(key, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Administrador.this);
                        queue.add(usuariosRequest);
                    }
                }else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray jsonRut = jsonResponse.getJSONArray("rut");
                                JSONArray jsonNombre = jsonResponse.getJSONArray("nombre");
                                JSONArray jsonApellido = jsonResponse.getJSONArray("apellido");
                                JSONArray jsonToken = jsonResponse.getJSONArray("token");
                                JSONArray jsonPerfil = jsonResponse.getJSONArray("id_perfil");
                                for (int i = 0; i < jsonRut.length(); i++) {
                                    mUsersDatabase.child(jsonToken.get(i).toString()).child("apellido").setValue(jsonApellido.get(i).toString());
                                    mUsersDatabase.child(jsonToken.get(i).toString()).child("nombre").setValue(jsonNombre.get(i).toString());
                                    mUsersDatabase.child(jsonToken.get(i).toString()).child("rut").setValue(jsonRut.get(i).toString());
                                    mUsersDatabase.child(jsonToken.get(i).toString()).child("perfil").setValue(jsonPerfil.get(i).toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    AllUsersRequest usuariosRequest = new AllUsersRequest(tokenAdmin, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Administrador.this);
                    queue.add(usuariosRequest);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarWebService() {
        progress = new ProgressDialog(this);
        progress.setMessage("Consultando...");
        progress.show();
        String url = "http://miltonzambra.com/ModificarTokenAdmin.php?token='" + tokenAdmin + "'";
        requestQueue = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        progress.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        progress.dismiss();
    }
}
