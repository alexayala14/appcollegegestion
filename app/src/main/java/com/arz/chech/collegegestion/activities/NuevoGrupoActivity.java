package com.arz.chech.collegegestion.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    private int request_code = 2;
    public  Boolean bandera=true;
    private TextView textView;
    private FloatingActionButton creargrupo;
    private EditText editText;
    private HashSet<DatosUsuario>hashSet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo);

        addpersona = (FloatingActionButton) findViewById(R.id.idAddIntegrante);
        mLinearLayout = new LinearLayoutManager(this);
        agregadosList = (RecyclerView) findViewById(R.id.act_contactosagregados_list);
        agregadosList.setHasFixedSize(true);
        agregadosList.setLayoutManager(mLinearLayout);
        final ContactosAgregadosAdapter agregadosAdapter = new ContactosAgregadosAdapter(NuevoGrupoActivity.this, datosUsuarios);
        agregadosList.setAdapter(agregadosAdapter);
        textView =(TextView) findViewById(R.id.textView3);
        creargrupo =(FloatingActionButton)findViewById(R.id.crearGrupo);
        editText=(EditText)findViewById(R.id.editText);

        RootRef = FirebaseDatabase.getInstance().getReference();
        btnCancel =(Button)findViewById(R.id.btncancelarr);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(NuevoGrupoActivity.this, MenuPrincipalActivity.class);
                startActivity(intent);*/
               finish();
            }
        });
        addpersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NuevoGrupoActivity.this,AgregarContactoActivity.class);
                startActivityForResult(intent,request_code);

            }
        });



        datosUsuarios = new ArrayList<>();

        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        if (userid!=null){
            System.out.println(datosUsuarios.toString());
            for (DatosUsuario mi:datosUsuarios) {

                if (userid.equals(mi.getToken())) {

                    bandera = false;



                } else {

                    bandera = true;

                }
            }

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosUsuarios.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                   final DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                    if (snapshot.child("estaEliminado").exists()){
                        assert usuario!=null;
                        if (userid.equals(usuario.getToken())){
                            if (!usuario.isEstaEliminado()){
                                //if(mUsersDatabase.equals("d")) {
                                  //  datosUsuarios.add(usuario);
                                //}

                                //datosUsuarios.add(usuario);

                                if(bandera) {

                                    bandera=false;
                                    datosUsuarios.add(usuario);

                                }
                                else{

                                }

                            }
                        }
                    }/*else{
                        if (userid.equals(usuario.getToken())){
                            datosUsuarios.add(usuario);
                        }
                    }*/

                }



                agregadosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        String texto="Participantes: "+String.valueOf(datosUsuarios.size()+1);
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

        if (resultCode == RESULT_CANCELED) {
            // Si es así mostramos mensaje de cancelado por pantalla.
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT)
                    .show();
        } else {
            userid=data.getStringExtra("user_id");

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
        final ContactosAgregadosAdapter agregadosAdapter = new ContactosAgregadosAdapter(NuevoGrupoActivity.this, datosUsuarios);
        agregadosList.setAdapter(agregadosAdapter);
        RootRef = FirebaseDatabase.getInstance().getReference();
        addpersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoGrupoActivity.this,AgregarContactoActivity.class);
                startActivityForResult(intent,request_code);
                //onResume();

            }
        });


       // datosUsuarios = new ArrayList<>();

        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        if (userid!=null){

            System.out.println(datosUsuarios.toString());
            for (DatosUsuario mi:datosUsuarios) {

                if (userid.equals(mi.getToken())) {

                    bandera = false;


                } else {

                    bandera = true;


                }
            }



            mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                       final DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                       assert usuario!=null;
                        if (snapshot.child("estaEliminado").exists()){
                            if (userid.equals(usuario.getToken())){
                                if (!usuario.isEstaEliminado()){
                                    //if(mUsersDatabase.equals("d")) {
                                    //  datosUsuarios.add(usuario);
                                    //}
                                    if(bandera) {
                                        bandera=false;
                                        datosUsuarios.add(usuario);


                                    }else {

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


                    agregadosAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });}








        String texto="Participantes: "+String.valueOf(datosUsuarios.size()+1);
        textView.setText(texto);
        creargrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(NuevoGrupoActivity.this ,"Ingrese nombre de Grupo",Toast.LENGTH_LONG).show();
                }else {
                    String nombreGrupo=editText.getText().toString();
                    DatosUsuario miUsuario = new DatosUsuario();
                    miUsuario.setNombre(Preferences.obtenerPreferenceString(NuevoGrupoActivity.this,Preferences.PREFERENCE_NOMBRE));
                    miUsuario.setApellido(Preferences.obtenerPreferenceString(NuevoGrupoActivity.this,Preferences.PREFERENCE_APELLIDO));
                    miUsuario.setPerfil(String.valueOf(Preferences.obtenerPreferenceInt(NuevoGrupoActivity.this,Preferences.PREFERENCE_ESTADO_ID_PERFIL)));
                    miUsuario.setRut(Preferences.obtenerPreferenceString(NuevoGrupoActivity.this,Preferences.PREFERENCE_USUARIO));
                    miUsuario.setToken(Preferences.obtenerPreferenceString(NuevoGrupoActivity.this,Preferences.PREFERENCE_TOKEN));
                    datosUsuarios.add(miUsuario);
                    for (int i =0;i<datosUsuarios.size();i++){
                        int cont=0;
                        for (int j =0;j<datosUsuarios.size()-1;j++){
                            if ((datosUsuarios.get(i).getToken()).equals(datosUsuarios.get(j).getToken())){
                                cont++;

                            }
                            if(cont==2){
                                cont--;
                                datosUsuarios.remove(i);
                            }
                        }
                    }
                    DatabaseReference refGroup= RootRef.child("Groups").push();
                    refGroup.child("name").setValue(nombreGrupo);
                    refGroup.child("members").setValue(datosUsuarios);
                    refGroup.child("admin").setValue(mCurrent_user_id);
                    refGroup.child("groupId").setValue(refGroup.getKey());
                    Intent intent = new Intent(NuevoGrupoActivity.this, ChatActivityGroup.class);
                    intent.putExtra("datosUsuariosList", datosUsuarios);
                    intent.putExtra("nombreGrupo",refGroup.getKey());
                    startActivity(intent);

                    finish();



                }

            }
        });
    }

}
