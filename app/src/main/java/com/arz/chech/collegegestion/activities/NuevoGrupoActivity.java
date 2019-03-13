package com.arz.chech.collegegestion.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.ContactosAgregadosAdapter;
import com.arz.chech.collegegestion.adapters.FriendsAdapter;
import com.arz.chech.collegegestion.adapters.GrupoAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NuevoGrupoActivity extends AppCompatActivity {
    private FloatingActionButton addpersona;
    private Button btnCancel;
    private RecyclerView agregadosList;
    private ContactosAgregadosAdapter agregadosAdapter;
    private ArrayList<DatosUsuario> datosUsuarios;
    private String mCurrent_user_id;
    private DatabaseReference databaseReference;
    private DatabaseReference mUsersDatabase;
    private List<String> usersList;
    private LinearLayoutManager mLinearLayout;
    private DatabaseReference RootRef;
    private String userid;
    private String nom;
    private String ape;
    private int request_code = 1;
    public static Boolean bandera=true;
    private TextView textView;
    private FloatingActionButton creargrupo;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo);
        addpersona = (FloatingActionButton) findViewById(R.id.idAddIntegrante);
        mLinearLayout = new LinearLayoutManager(this);
        agregadosList = (RecyclerView) findViewById(R.id.act_contactosagregados_list);
        agregadosList.setHasFixedSize(true);
        agregadosList.setLayoutManager(mLinearLayout);
        textView =(TextView) findViewById(R.id.textView3);
        creargrupo =(FloatingActionButton)findViewById(R.id.crearGrupo);
        editText=(EditText)findViewById(R.id.editText);
        //Intent intent = getIntent();
        //userid=intent.getStringExtra("user_id");
        //userid = intent.getStringExtra("user_id");
        System.out.println(userid);
        /*nom=intent.getStringExtra("user_name");
        ape=intent.getStringExtra("user_apellido");
        System.out.println(userid);
        System.out.println(nom);
        System.out.println(ape);*/
        RootRef = FirebaseDatabase.getInstance().getReference();
        btnCancel =(Button)findViewById(R.id.btncancelarr);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoGrupoActivity.this, FriendsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addpersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoGrupoActivity.this,AgregarContactoActivity.class);
                startActivityForResult(intent,request_code);
                //onResume();

            }
        });



        datosUsuarios = new ArrayList<>();
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        if (userid!=null){
            for (DatosUsuario mi:datosUsuarios){

                if (userid.equals(mi.getToken())) {

                    bandera=false;

                }
                else {
                    //datosUsuarios.add(usuario);
                    bandera=true;
                }
            }
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosUsuarios.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                    if (snapshot.child("estaEliminado").exists()){
                        if (userid.equals(usuario.getToken())){
                            if (!usuario.isEstaEliminado()){
                                //if(mUsersDatabase.equals("d")) {
                                  //  datosUsuarios.add(usuario);
                                //}
                                datosUsuarios.add(usuario);
                                System.out.println("usuario agregado por create");
                                if(bandera) {

                                    bandera=false;
                                }
                                else{
                                    System.out.println("El usuario esta repetido por create");
                                }

                            }
                        }
                    }/*else{
                        if (userid.equals(usuario.getToken())){
                            datosUsuarios.add(usuario);
                        }
                    }*/

                }
                ContactosAgregadosAdapter agregadosAdapter = new ContactosAgregadosAdapter(NuevoGrupoActivity.this, datosUsuarios);
                agregadosList.setAdapter(agregadosAdapter);


                agregadosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        String texto="Participantes: "+String.valueOf(datosUsuarios.size());
        textView.setText(texto);

        creargrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(NuevoGrupoActivity.this ,"Ingrese nombre de Grupo",Toast.LENGTH_LONG).show();
                }else {
                    String nombreGrupo=editText.getText().toString();
                    RootRef.child("Groups").child(nombreGrupo).setValue("");
                    Intent intent = new Intent(NuevoGrupoActivity.this, ChatActivityGroup.class);
                    intent.putExtra("datosUsuariosList", datosUsuarios);
                    intent.putExtra("nombreGrupo",nombreGrupo);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == request_code) && (resultCode == RESULT_OK)){
            userid=data.getStringExtra("user_id");
            System.out.println("es el valor:"+userid);


        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        addpersona = (FloatingActionButton) findViewById(R.id.idAddIntegrante);
        mLinearLayout = new LinearLayoutManager(this);
        agregadosList = (RecyclerView) findViewById(R.id.act_contactosagregados_list);
        agregadosList.setHasFixedSize(true);
        agregadosList.setLayoutManager(mLinearLayout);
        RootRef = FirebaseDatabase.getInstance().getReference();
        addpersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoGrupoActivity.this,AgregarContactoActivity.class);
                startActivityForResult(intent,request_code);
                //onResume();

            }
        });

        for (DatosUsuario mi:datosUsuarios){

            if (userid.equals(mi.getToken())) {

                bandera=false;

            }
            else {
                //datosUsuarios.add(usuario);
                bandera=true;
            }
        }

        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        if (userid!=null){


            mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //datosUsuarios.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                        if (snapshot.child("estaEliminado").exists()){
                            if (userid.equals(usuario.getToken())){
                                if (!usuario.isEstaEliminado()){
                                    //if(mUsersDatabase.equals("d")) {
                                    //  datosUsuarios.add(usuario);
                                    //}
                                    if(bandera) {
                                        datosUsuarios.add(usuario);
                                        bandera=false;
                                        System.out.println("El usuario esta agregado por resume");
                                    }
                                    else{
                                        System.out.println("El usuario esta repetido por resume");
                                    }
                                }
                            }
                        }/*else{
                            if (userid.equals(usuario.getToken())){
                                datosUsuarios.add(usuario);
                                System.out.println("por dos");

                            }
                        }*/

                    }
                    ContactosAgregadosAdapter agregadosAdapter = new ContactosAgregadosAdapter(NuevoGrupoActivity.this, datosUsuarios);
                    agregadosList.setAdapter(agregadosAdapter);

                    agregadosAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });}
        String texto="Participantes: "+String.valueOf(datosUsuarios.size());
        textView.setText(texto);
        creargrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(NuevoGrupoActivity.this ,"Ingrese nombre de Grupo",Toast.LENGTH_LONG).show();
                }else {
                    String nombreGrupo=editText.getText().toString();
                    //RootRef.child("Groups").child(nombreGrupo).setValue("");
                    RootRef.child("Groups").child(nombreGrupo);
                    RootRef.child("Groups").child(nombreGrupo).child("members").setValue(datosUsuarios);
                    RootRef.child("Groups").child(nombreGrupo).child("admin").setValue(mCurrent_user_id);
                    Intent intent = new Intent(NuevoGrupoActivity.this, ChatActivityGroup.class);
                    intent.putExtra("datosUsuariosList", datosUsuarios);
                    intent.putExtra("nombreGrupo",nombreGrupo);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

}
