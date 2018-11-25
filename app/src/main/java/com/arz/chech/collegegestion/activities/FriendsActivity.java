package com.arz.chech.collegegestion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.fragments.ChatsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;
    private RecyclerView mFriendsList;
    private DatabaseReference mUsersDatabase;
    private String mCurrent_user_id;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mConvDatabase;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayout;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        mChatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.friends_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        mFriendsList = (RecyclerView) findViewById(R.id.act_friends_list);
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.friends_swipe_layout);
        mLinearLayout = new LinearLayoutManager(this);
        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(mLinearLayout);
        pd = new ProgressDialog(this);
        pd.setMessage("Cargando...");
        pd.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map map = new HashMap();
                for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    String key = datasnapshot.getKey();
                    DatosUsuario datosUsuario = datasnapshot.getValue(DatosUsuario.class);
                    if (datosUsuario.getRut().equals(UserDetails.username)){
                        Response.Listener <String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    JSONArray jsonRut = jsonResponse.getJSONArray("rut");
                                    JSONArray jsonNombre = jsonResponse.getJSONArray("nombre");
                                    JSONArray jsonApellido = jsonResponse.getJSONArray("apellido");
                                    JSONArray jsonToken = jsonResponse.getJSONArray("token");
                                    JSONArray jsonPerfil = jsonResponse.getJSONArray("id_perfil");
                                    for(int i=0; i<jsonRut.length(); i++){
                                        mFriendsDatabase.child(jsonToken.get(i).toString()).child("apellido").setValue(jsonApellido.get(i).toString());
                                        mFriendsDatabase.child(jsonToken.get(i).toString()).child("nombre").setValue(jsonNombre.get(i).toString());
                                        mFriendsDatabase.child(jsonToken.get(i).toString()).child("rut").setValue(jsonRut.get(i).toString());
                                        mFriendsDatabase.child(jsonToken.get(i).toString()).child("perfil").setValue(jsonPerfil.get(i).toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pd.dismiss();

                            }
                        };
                        RequestUsuarios usuariosRequest = new RequestUsuarios(key, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
                        queue.add(usuariosRequest);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        // Finalizar el progressDialog
        pd.dismiss();

        FirebaseRecyclerAdapter<DatosUsuario, FriendsActivity.FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<DatosUsuario, FriendsActivity.FriendsViewHolder>(
                DatosUsuario.class,
                R.layout.users_single_layout,
                FriendsActivity.FriendsViewHolder.class,
                mFriendsDatabase
        ) {
            @Override
            protected void populateViewHolder(final FriendsActivity.FriendsViewHolder friendsViewHolder, final DatosUsuario friends, int i) {
                friendsViewHolder.setName(friends.getNombre(), friends.getApellido());
                final String list_user_id = getRef(i).getKey();

                if (list_user_id != null) {
                    mFriendsDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String userName = dataSnapshot.child("nombre").getValue().toString();
                            final String userApellido = dataSnapshot.child("apellido").getValue().toString();
                            friendsViewHolder.setName(userName, userApellido);
                            friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
                                    chatIntent.putExtra("user_id", list_user_id);
                                    chatIntent.putExtra("user_name", userName);
                                    chatIntent.putExtra("user_apellido", userApellido);
                                    startActivity(chatIntent);
                                    ChatsFragment.noExistMensajes.setVisibility(View.GONE);
                                    Preferences.savePreferenceBoolean(FriendsActivity.this, true, Preferences.PREFERENCE_MENSAJES);
                                    finish();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        };

        mFriendsList.setAdapter(friendsRecyclerViewAdapter);
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

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name, String apellido){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name + " " + apellido);
        }
    }
}
