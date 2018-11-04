package com.arz.chech.collegegestion.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.adapters.UsuarioList;
import com.arz.chech.collegegestion.adapters.UsuariosAdapter;
import com.arz.chech.collegegestion.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerUsuarios extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    RecyclerView recyclerUsuarios;
    ArrayList<UsuarioList> listaUsuarios;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_usuarios);

        listaUsuarios = new ArrayList<>();
        recyclerUsuarios = (RecyclerView) findViewById(R.id.idRecycler);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerUsuarios.setHasFixedSize(true);

        request = Volley.newRequestQueue(this);

        cargarWebService();
    }

    private void cargarWebService() {
        progress = new ProgressDialog(this);
        progress.setMessage("Consultando...");
        progress.show();

        String url = "http://miltonzambra.com/ConsultarLista.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("Error: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        UsuarioList usuario = null;
        JSONArray json = response.optJSONArray("usuario");

        try {
            for (int i=0; i<json.length(); i++){
                usuario = new UsuarioList();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                usuario.setRut(jsonObject.optInt("rut"));
                usuario.setNombre(jsonObject.optString("nombre"));
                usuario.setApellido(jsonObject.optString("apellido"));
                usuario.setPerfil(jsonObject.optString("descripcion"));
                listaUsuarios.add(usuario);
            }
            progress.hide();
            UsuariosAdapter adapter = new UsuariosAdapter(listaUsuarios);
            recyclerUsuarios.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
