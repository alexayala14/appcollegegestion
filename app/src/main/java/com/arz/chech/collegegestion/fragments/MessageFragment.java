package com.arz.chech.collegegestion.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.FriendsActivity;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.arz.chech.collegegestion.adapters.MessagesAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.notifications.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment{

    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private List<DatosUsuario> mUsers;
    private String mCurrent_user_id;
    private DatabaseReference databaseReference;
    private List<String> usersList;
    private List<String> groupList;
    private FloatingActionButton fab;
    private TextView noExistenMensajes;
    private DatabaseReference groupRef;

    public MessageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.conv_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mCurrent_user_id = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_TOKEN);
        noExistenMensajes = (TextView) view.findViewById(R.id.no_exist_msj);

        // Floating Button
        fab = (FloatingActionButton) view.findViewById(R.id.my_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getContext(), FriendsActivity.class);
                startActivity(chatIntent);
            }
        });

        //
        groupList =new ArrayList<>();
        usersList = new ArrayList<>();
        mUsers = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(getContext(), mUsers);
        recyclerView.setAdapter(messagesAdapter);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        //
        databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(mCurrent_user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    usersList.add(snapshot.getKey());
                }
                chatList();
                //groupList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }



    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_TOKEN)).setValue(token1);
    }


    private void chatList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DatosUsuario user = snapshot.getValue(DatosUsuario.class);
                    if (snapshot.child("estaEliminado").exists()){
                        for (String usuario: usersList){
                            if (user.getToken().equals(usuario)){
                                if (!user.isEstaEliminado()){
                                    mUsers.add(user);
                                }
                            }
                        }
                    }else{
                        for (String usuario: usersList){
                            if (user.getToken().equals(usuario)){
                                mUsers.add(user);
                            }
                        }
                    }

                }
                if (mUsers.isEmpty()){
                    noExistenMensajes.setVisibility(View.VISIBLE);
                }else {
                    noExistenMensajes.setVisibility(View.GONE);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}