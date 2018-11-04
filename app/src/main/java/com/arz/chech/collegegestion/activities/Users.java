package com.arz.chech.collegegestion.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;

import android.content.Intent;


import android.view.View;

import android.widget.AdapterView;

import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.TextView;

import com.android.volley.Request;

import com.android.volley.RequestQueue;

import com.android.volley.Response;

import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.UserDetails;

import org.json.JSONException;

import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Iterator;


public class Users extends AppCompatActivity {
    // Declarar variables

    ListView usersList;

    TextView noUsersText;

    ArrayList al = new ArrayList<>();

    int totalUsers = 0;

    ProgressDialog pd;

    // Variable del servicio Firebase

    String URL_FIREBASE = "https://appcollegegestion.firebaseio.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        // Obtener referencia de los objetos en las variables

        usersList = (ListView)findViewById(R.id.usersList);

        noUsersText = (TextView)findViewById(R.id.noUsersText);

        // Iniciar progressDialog

        pd = new ProgressDialog(Users.this);

        pd.setMessage("Cargando...");

        pd.show();

        // Variable para hacer referencia al archivo JSON del servicio Firebase

        String url = URL_FIREBASE + "/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Funcion para obtener los nombre de los usuarios Loggueados

                doOnSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);

        rQueue.add(request);
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 UserDetails.chatWith = (String) al.get(position);

                startActivity(new Intent(Users.this, Chat.class));
            }
        });
    }
    // Funcion para obtener Nombres usuarios

    public void doOnSuccess(String s){

        try {

            JSONObject obj = new JSONObject(s);

            // Obtiene los nombres de la devolucion del request JSON

            Iterator i = obj.keys();

            String key = "";

            // Añadir los nombres a la variable ArrayList

            while(i.hasNext()){

                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {

                    al.add(key);

                }

                totalUsers++;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        // Si la cantidad de usuarios es menor a 0 mostrara el mensaje de que no hay usuarios

        if(totalUsers <=1){

            // Mostrar TextView y Ocultar ListView

            noUsersText.setVisibility(View.VISIBLE);

            usersList.setVisibility(View.GONE);

        }

        else{

            // Ocultar TextView y Mostrar ListView

            noUsersText.setVisibility(View.GONE);

            usersList.setVisibility(View.VISIBLE);

            // Añadir usuarios al ListView

            usersList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, al));

        }

        // Finalizar el progressDialog

        pd.dismiss();

    }


}
