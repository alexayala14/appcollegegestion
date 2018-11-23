package com.arz.chech.collegegestion.entidades;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.MainActivity;
import com.arz.chech.collegegestion.activities.MenuPrincipalActivity;
import com.arz.chech.collegegestion.activities.Preferences;
import com.arz.chech.collegegestion.activities.RecyclerUsuarios;
import com.arz.chech.collegegestion.activities.Registrar;

import java.util.ArrayList;

public class Administrador extends AppCompatActivity {
    Button btn_agregarUsuarios, btn_consultarUsuarios,btn_publicacionesymensajes, btn_cerrar_sesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
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
    }
}
