package com.arz.chech.collegegestion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.request.ModifyRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyUsersActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    EditText campoNombre, campoApellido, campoRut, campoContraseña;
    Spinner campoPerfil;
    Button btn_actualizar, btn_atras;
    String nombreIntent, apellidoIntent, rutIntent, perfilIntent, tokenIntent;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    Intent intent;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        campoNombre = (EditText) findViewById(R.id.modNombre);
        campoApellido = (EditText) findViewById(R.id.modApellido);
        campoRut = (EditText) findViewById(R.id.modRut);
        campoContraseña = (EditText) findViewById(R.id.modPassword);
        campoPerfil = (Spinner) findViewById(R.id.modPerfiles);
        btn_actualizar = (Button) findViewById(R.id.btn_actualizarUsuario);
        btn_atras = (Button) findViewById(R.id.btn_atras_mod);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        intent = getIntent();
        nombreIntent = intent.getStringExtra("nombre");
        apellidoIntent = intent.getStringExtra("apellido");
        rutIntent = intent.getStringExtra("rut");
        perfilIntent = intent.getStringExtra("perfil");
        tokenIntent = intent.getStringExtra("token");
        campoNombre.setText(nombreIntent);
        campoApellido.setText(apellidoIntent);
        campoRut.setText(rutIntent);
        campoPerfil.setSelection(Integer.parseInt(perfilIntent)-1);
        setearPassword(tokenIntent);
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoNombre.getText().toString().isEmpty()){
                    Toast.makeText(ModifyUsersActivity.this, "Debe ingresar nombre", Toast.LENGTH_SHORT).show();
                }else if (campoApellido.getText().toString().isEmpty()) {
                    Toast.makeText(ModifyUsersActivity.this, "Debe ingresar apellido", Toast.LENGTH_SHORT).show();
                }else if (campoRut.getText().toString().isEmpty()) {
                    Toast.makeText(ModifyUsersActivity.this, "Debe ingresar rut", Toast.LENGTH_SHORT).show();
                }else if (campoContraseña.getText().toString().isEmpty()) {
                    Toast.makeText(ModifyUsersActivity.this, "Debe ingresar contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    final String nombre = campoNombre.getText().toString().trim();
                    final String apellido = campoApellido.getText().toString().trim();
                    final String rut = campoRut.getText().toString().trim();
                    final String password = campoContraseña.getText().toString().trim();
                    final String perfil = String.valueOf(campoPerfil.getSelectedItemPosition()+1);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    pd = new ProgressDialog(ModifyUsersActivity.this);
                    pd.setMessage("Cargando...");
                    pd.show();
                    Response.Listener <String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    DatosUsuario datosUsuario = new DatosUsuario(nombre, apellido, rut, perfil, false, tokenIntent);
                                    databaseReference.child(tokenIntent).setValue(datosUsuario);
                                    Toast.makeText(ModifyUsersActivity.this, "Usuario modificado correctamente", Toast.LENGTH_SHORT).show();
                                    Intent volver = new Intent(ModifyUsersActivity.this, AllUsersActivity.class);
                                    startActivity(volver);
                                    finish();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUsersActivity.this);
                                    builder.setMessage("No se pudo modificar el usuario")
                                            .setNegativeButton("Aceptar", null)
                                            .create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pd.dismiss();

                        }
                    };
                    ModifyRequest modifyRequest = new ModifyRequest(nombre, apellido, rut, password, tokenIntent, perfil, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ModifyUsersActivity.this);
                    queue.add(modifyRequest);
                }
            }
        });
    }

    private void setearPassword(String token) {
        String url = "http://miltonzambra.com/SetPassword.php?token='" + token + "'";
        requestQueue = Volley.newRequestQueue(ModifyUsersActivity.this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private String validarPerfilFirebase(String intPerfil){
        switch (intPerfil) {
            case "1":
                intPerfil = "Administrador";
                break;
            case "2":
                intPerfil = "Directivos";
                break;
            case "3":
                intPerfil = "Docentes";
                break;
            case "4":
                intPerfil = "Asistentes";
                break;
            case "5":
                intPerfil = "Padres";
                break;
        }
        return intPerfil;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String password = response.getString("password");
            campoContraseña.setText(password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
