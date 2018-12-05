package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.FriendsAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;
    private RecyclerView mFriendsList;
    private ArrayList<DatosUsuario> datosUsuarios;
    private String mCurrent_user_id;
    private LinearLayoutManager mLinearLayout;
    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        mLinearLayout = new LinearLayoutManager(this);
        mFriendsList = (RecyclerView) findViewById(R.id.act_friends_list);
        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(mLinearLayout);

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

        //
        datosUsuarios = new ArrayList<>();
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);

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
                                datosUsuarios.add(usuario);
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
