package com.arz.chech.collegegestion.activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.MessagesAdapter;
import com.arz.chech.collegegestion.adapters.MessagesGroupAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Grupo;
import com.arz.chech.collegegestion.notifications.Token;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MensajesGruposActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessagesGroupAdapter messagesGroupAdapter;
    private List<DatosUsuario> mUsers;
    private String mCurrent_user_id;
    private DatabaseReference databaseReference;
    private List<String> usersList;
    private ArrayList<Grupo> groupList;
    private FloatingActionButton fab;
    private TextView noExistenMensajes;
    private DatabaseReference groupRef;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes_grupos);

        recyclerView = findViewById(R.id.conv_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        noExistenMensajes = (TextView) findViewById(R.id.no_exist_msj);


        groupList =new ArrayList<>();
        usersList = new ArrayList<>();
        mUsers = new ArrayList<>();
        messagesGroupAdapter = new MessagesGroupAdapter(MensajesGruposActivity.this,groupList);
        recyclerView.setAdapter(messagesGroupAdapter);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        //
        databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //groupList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                   final Grupo grupo = snapshot.getValue(Grupo.class);
                    assert grupo !=null;
                    groupId=grupo.getGroupId();
                    groupId = snapshot.getKey();
                    messagesGroupAdapter.enviarDatos(groupId);
                    usersList.add(snapshot.getKey());



                }
                chatList();

               //messagesGroupAdapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }



    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN)).setValue(token1);
    }


    private void chatList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Grupo grupo = snapshot.getValue(Grupo.class);
                    assert grupo !=null;

                    /*for(DatosUsuario user:grupo.getMembers()) {


                        if (Preferences.obtenerPreferenceString(MensajesGruposActivity.this, Preferences.PREFERENCE_TOKEN).equals(user.getToken())){
                            groupList.add(grupo);
                        }
                    }*/


                }
                if (groupList.isEmpty()){
                    noExistenMensajes.setVisibility(View.VISIBLE);
                }else {
                    noExistenMensajes.setVisibility(View.GONE);
                }
                messagesGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
