package com.arz.chech.collegegestion.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.Chat;
import com.arz.chech.collegegestion.activities.ChatActivity;
import com.arz.chech.collegegestion.activities.RegisterRequest;
import com.arz.chech.collegegestion.activities.Registrar;
import com.arz.chech.collegegestion.activities.RequestUsuarios;
import com.arz.chech.collegegestion.activities.UserDetails;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.fragments.MisPublicacionesFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView mFriendsList;
    private DatabaseReference mUsersDatabase;
    private String mCurrent_user_id;
    private View mMainView;
    private DatabaseReference mFriendsDatabase;
    ProgressDialog pd;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);
        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mCurrent_user_id = UserDetails.token;
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        pd = new ProgressDialog(getContext());
        pd.setMessage("Cargando...");
        pd.show();
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();
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
                        RequestQueue queue = Volley.newRequestQueue(getContext());
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

        FirebaseRecyclerAdapter<DatosUsuario, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<DatosUsuario, FriendsViewHolder>(
                DatosUsuario.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                mFriendsDatabase
        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, DatosUsuario friends, int i) {
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
                                    CharSequence options[] = new CharSequence[]{"Send message"};
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Select Options");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(i == 0){
                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                chatIntent.putExtra("user_id", list_user_id);
                                                chatIntent.putExtra("user_name", userName);
                                                startActivity(chatIntent);
                                            }
                                        }
                                    });
                                    builder.show();
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
