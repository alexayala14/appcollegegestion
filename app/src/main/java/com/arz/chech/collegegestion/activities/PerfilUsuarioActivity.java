package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilUsuarioActivity extends AppCompatActivity {
    private Toolbar mChatToolbar;
    private CircleImageView imageViewPerfil;
    private TextView textViewNombre;
    private TextView textViewApellido;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButtonImagenperfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // TOOLBAR
        mChatToolbar = (Toolbar) findViewById(R.id.main_app_bar_perfil);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.app_bar_layout_perfil, null);
        actionBar.setCustomView(action_bar_view);
        //

        imageViewPerfil=findViewById(R.id.idperfiluserimagen);
        textViewNombre=findViewById(R.id.idnombreuserperfil);
        textViewNombre=findViewById(R.id.idapellidouserperfil);
        progressBar=findViewById(R.id.idprogessbar12);
        floatingActionButtonImagenperfil=findViewById(R.id.idbuttonagregarimagen1);
        mChatToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
