package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.FriendsAdapter;
import com.arz.chech.collegegestion.adapters.GrupoAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Grupo;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;
    private RecyclerView mFriendsList;
    private RecyclerView mGroupsList;
    private ArrayList<DatosUsuario> datosUsuarios;
    private ArrayList<Grupo> groups;
    private String mCurrent_user_id;
    private LinearLayoutManager mLinearLayout;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mGroupsDatabase;
    private FloatingActionButton grup;
    private DatabaseReference RootRef;
    private  String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        mLinearLayout = new LinearLayoutManager(this);
        mFriendsList = (RecyclerView) findViewById(R.id.act_friends_list);
        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(mLinearLayout);
        RootRef = FirebaseDatabase.getInstance().getReference();
        // TOOLBAR
        mChatToolbar = (Toolbar) findViewById(R.id.friends_app_bar);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.friends_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        grup = (FloatingActionButton) findViewById(R.id.my_grup);
        grup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Es un grupo nuevo",Toast.LENGTH_LONG).show();
                //RequestNewGroup();
                //Intent intent = new Intent(FriendsActivity.this, NuevoGrupoActivity.class);
                Intent intent = new Intent(FriendsActivity.this, MensajesGruposActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //
        datosUsuarios = new ArrayList<>();
        groups =new ArrayList<>();
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        mGroupsDatabase=FirebaseDatabase.getInstance().getReference().child("Groups");
        /*mGroupsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Grupo group = snapshot.getValue(Grupo.class);
                    if (snapshot.child("estaEliminado").exists()){
                        if (!mCurrent_user_id.equals(group.getToken())){
                            if (!group.isEstaEliminado()){
                                 if(mUsersDatabase.equals("d")) {
                                    datosUsuarios.add(usuario);
                                }
                                groups.add(group);
                            }
                        }
                    }else{
                        if (!mCurrent_user_id.equals(group.getToken())){
                            groups.add(group);
                        }
                    }

                }
                GrupoAdapter grupoAdapter = new GrupoAdapter(FriendsActivity.this, groups);
                mGroupsList.setAdapter(grupoAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosUsuarios.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                    if (snapshot.child("estaEliminado").exists()){
                        if (!mCurrent_user_id.equals(usuario.getToken())){
                            if (!usuario.isEstaEliminado()){
                                /*if(mUsersDatabase.equals("d")) {
                                    datosUsuarios.add(usuario);
                                }*/

                                if((String.valueOf(Preferences.obtenerPreferenceInt(FriendsActivity.this,Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("1"))){

                                    datosUsuarios.add(usuario);

                                }

                                if((String.valueOf(Preferences.obtenerPreferenceInt(FriendsActivity.this,Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("2"))){
                                    if (usuario.getPerfil().equals("1")||usuario.getPerfil().equals("2")||usuario.getPerfil().equals("3")) {
                                        datosUsuarios.add(usuario);
                                    }
                                }

                                if((String.valueOf(Preferences.obtenerPreferenceInt(FriendsActivity.this,Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("3"))){
                                    if (usuario.getPerfil().equals("2")||usuario.getPerfil().equals("3")) {
                                        datosUsuarios.add(usuario);
                                    }
                                }

                            }
                        }
                    }else{
                        if (!mCurrent_user_id.equals(usuario.getToken())){
                            datosUsuarios.add(usuario);
                        }
                    }

                }
                FriendsAdapter friendsAdapter = new FriendsAdapter(FriendsActivity.this, datosUsuarios);
                mFriendsList.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this,R.style.AlertDialog);
        builder.setTitle("Ingrese Nombre Del Grupo :");

        final EditText groupNameField = new EditText(FriendsActivity.this);
        groupNameField.setHint("Ejemplo : Amigos del club");

        builder.setView(groupNameField);



        builder.setPositiveButton("Nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(getApplicationContext(),"Por favor ingrese nombre del grupo.",Toast.LENGTH_LONG).show();

                }
                else{
                    createNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void createNewGroup(final String groupName) {
        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),groupName +" fue creado correctamente",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
