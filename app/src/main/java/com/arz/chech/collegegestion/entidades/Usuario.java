package com.arz.chech.collegegestion.entidades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.fragments.PublicacionesFragment;

public class Usuario extends AppCompatActivity {
    TextView Text_nombre;
    PublicacionesFragment publicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Text_nombre = findViewById(R.id.Text_nombre);
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        Text_nombre.setText(nombre);

    }

}
