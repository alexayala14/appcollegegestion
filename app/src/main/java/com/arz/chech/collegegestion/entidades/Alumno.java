package com.arz.chech.collegegestion.entidades;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.fragments.PublicacionesFragment;

public class Alumno extends AppCompatActivity implements PublicacionesFragment.OnFragmentInteractionListener {
    PublicacionesFragment publicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);
        publicaciones = new PublicacionesFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragments,publicaciones).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
