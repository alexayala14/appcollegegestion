package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.FriendsAdapter;
import com.arz.chech.collegegestion.adapters.GrupoAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Grupo;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AgregarContactoActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;
    private RecyclerView mContactosList;
    private ArrayList<DatosUsuario> datosUsuarios;
    private String mCurrent_user_id;
    private LinearLayoutManager mLinearLayout;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference RootRef;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        mLinearLayout = new LinearLayoutManager(this);
        mContactosList = (RecyclerView) findViewById(R.id.act_contactos_list);
        mContactosList.setHasFixedSize(true);
        mContactosList.setLayoutManager(mLinearLayout);
        RootRef = FirebaseDatabase.getInstance().getReference();
        ////TOOL BAR
        mChatToolbar = (Toolbar) findViewById(R.id.friends_app_bar);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.friends_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        btnCancel =(Button)findViewById(R.id.btncancelarr);
        /*btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        datosUsuarios = new ArrayList<>();
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosUsuarios.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                    if (snapshot.child("estaEliminado").exists()) {
                        if (!mCurrent_user_id.equals(usuario.getToken())) {
                            if (!usuario.isEstaEliminado()) {

                                if ((String.valueOf(Preferences.obtenerPreferenceInt(AgregarContactoActivity.this, Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("1"))) {

                                    datosUsuarios.add(usuario);

                                }

                                if ((String.valueOf(Preferences.obtenerPreferenceInt(AgregarContactoActivity.this, Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("2"))) {
                                    if (usuario.getPerfil().equals("1") || usuario.getPerfil().equals("2") || usuario.getPerfil().equals("3")) {
                                        datosUsuarios.add(usuario);
                                    }
                                }

                                if ((String.valueOf(Preferences.obtenerPreferenceInt(AgregarContactoActivity.this, Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("3"))) {
                                    if (usuario.getPerfil().equals("2") || usuario.getPerfil().equals("3")) {
                                        datosUsuarios.add(usuario);
                                    }
                                }
                            }
                        }
                    } else {
                        if (!mCurrent_user_id.equals(usuario.getToken())) {
                            if ((String.valueOf(Preferences.obtenerPreferenceInt(AgregarContactoActivity.this, Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("1"))) {

                                datosUsuarios.add(usuario);

                            }

                            if ((String.valueOf(Preferences.obtenerPreferenceInt(AgregarContactoActivity.this, Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("2"))) {
                                if (usuario.getPerfil().equals("1") || usuario.getPerfil().equals("2") || usuario.getPerfil().equals("3")) {
                                    datosUsuarios.add(usuario);
                                }
                            }

                            if ((String.valueOf(Preferences.obtenerPreferenceInt(AgregarContactoActivity.this, Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("3"))) {
                                if (usuario.getPerfil().equals("2") || usuario.getPerfil().equals("3")) {
                                    datosUsuarios.add(usuario);
                                }
                            }
                        }
                    }

                }
                GrupoAdapter friendsAdapter = new GrupoAdapter(AgregarContactoActivity.this, datosUsuarios);
                mContactosList.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
