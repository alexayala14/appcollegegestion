package com.arz.chech.collegegestion.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.AdaptadorPublicaciones;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Publicacion;
import com.arz.chech.collegegestion.notifications.Data;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PublicacionesFragment extends Fragment{

    private TextView noExistsPublicaciones;
    private DatabaseReference mRootRef, mConvDatabase;
    private Button btnPublicacion;
    private FragmentManager fm;

    //PRUEBA BD
    private RecyclerView recyclerViewPublicaciones;
    private ArrayList<Publicacion> listaPublicaciones;
    private ArrayList<DatosUsuario> listaUsuarios;
    private AdaptadorPublicaciones adaptadorPublicaciones;
    private String mCurrentUserId;

    public PublicacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicaciones, container, false);
        recyclerViewPublicaciones = view.findViewById(R.id.Recyclerid);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewPublicaciones.setHasFixedSize(true);
        recyclerViewPublicaciones.setLayoutManager(linearLayoutManager);
        noExistsPublicaciones = (TextView) view.findViewById(R.id.no_exist_publicaciones);
        mCurrentUserId = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_TOKEN);
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Publicaciones");

        //
        fm = getFragmentManager();
        listaPublicaciones = new ArrayList<>();
        listaUsuarios = new ArrayList<>();
        adaptadorPublicaciones = new AdaptadorPublicaciones(getContext(), listaPublicaciones, fm);
        recyclerViewPublicaciones.setAdapter(adaptadorPublicaciones);

        mRootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuarios.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DatosUsuario datosUsuario = snapshot.getValue(DatosUsuario.class);
                    if (!datosUsuario.isEstaEliminado()){
                        if((String.valueOf(Preferences.obtenerPreferenceInt(getContext(),Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("1"))){

                            listaUsuarios.add(datosUsuario);


                        }

                        if((String.valueOf(Preferences.obtenerPreferenceInt(getContext(),Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("2"))){
                            if (datosUsuario.getPerfil().equals("1")||datosUsuario.getPerfil().equals("2")||datosUsuario.getPerfil().equals("3")) {
                                listaUsuarios.add(datosUsuario);
                            }
                        }

                        if((String.valueOf(Preferences.obtenerPreferenceInt(getContext(),Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("3"))){
                            if (datosUsuario.getPerfil().equals("2")||datosUsuario.getPerfil().equals("3")) {
                                listaUsuarios.add(datosUsuario);
                            }
                        }

                        if((String.valueOf(Preferences.obtenerPreferenceInt(getContext(),Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("4"))){
                            if (datosUsuario.getPerfil().equals("2")) {
                                listaUsuarios.add(datosUsuario);
                            }
                        }

                        if((String.valueOf(Preferences.obtenerPreferenceInt(getContext(),Preferences.PREFERENCE_ESTADO_ID_PERFIL)).equals("5"))){
                            if (datosUsuario.getPerfil().equals("3")) {
                                listaUsuarios.add(datosUsuario);
                            }
                        }
                        /*if(datosUsuario.getToken().equals(datosUsuario.getToken())) {
                            String imagenurl = datosUsuario.getImagenurl();
                            adaptadorPublicaciones.enviarImagenurl(imagenurl);
                        }*/

                    }


                }
                publicacionesList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return view;
    }

    private void publicacionesList() {
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Publicaciones");
        mRootRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPublicaciones.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Publicacion publicacion = snapshot.getValue(Publicacion.class);
                    if (!publicacion.isEstaEliminado()){
                        if (publicacion.getTokenUser() != null){
                            for (DatosUsuario usuarios: listaUsuarios){
                                if (publicacion.getTokenUser().equals(usuarios.getToken())){

                                    listaPublicaciones.add(publicacion);

                                    adaptadorPublicaciones.enviarImagenurl(usuarios.getImagenurl());
                                    System.out.println("La url es: "+usuarios.getImagenurl());
                                }
                            }
                        }

                    }
                }
                if (listaPublicaciones.isEmpty()){
                    noExistsPublicaciones.setVisibility(View.VISIBLE);
                }else {
                    noExistsPublicaciones.setVisibility(View.GONE);
                }
                adaptadorPublicaciones.notifyDataSetChanged();
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
