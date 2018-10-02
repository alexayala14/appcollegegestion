package com.example.chech.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Publicacion> listaPublicaciones;
    RecyclerView recyclerViewPublicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaPublicaciones = new ArrayList<>();
        recyclerViewPublicaciones =  findViewById(R.id.Recyclerid);
        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(this));

        llenarPublicaciones();
        AdaptadorPublicaciones adapter = new AdaptadorPublicaciones(listaPublicaciones);
        recyclerViewPublicaciones.setAdapter(adapter);

    }
    private void llenarPublicaciones(){
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio",R.drawable.hombre));
    }
}
