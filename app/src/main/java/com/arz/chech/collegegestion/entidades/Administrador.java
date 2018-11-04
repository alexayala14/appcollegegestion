package com.arz.chech.collegegestion.entidades;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.RecyclerUsuarios;
import com.arz.chech.collegegestion.activities.Registrar;

import java.util.ArrayList;

public class Administrador extends AppCompatActivity {
    private TextView Text_nombre;
    Button btn_agregarUsuarios, btn_consultarUsuarios;
    ArrayList<String> listaUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        Text_nombre = findViewById(R.id.Text_nombreAdmin);
        Text_nombre.setText("Bienvenido");
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
    }
}
