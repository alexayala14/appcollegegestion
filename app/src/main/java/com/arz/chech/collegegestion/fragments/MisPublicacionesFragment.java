package com.arz.chech.collegegestion.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.NuevaPublicacionActivity;
import com.arz.chech.collegegestion.adapters.AdaptadorPublicaciones;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Publicacion;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MisPublicacionesFragment extends Fragment {

    private TextView noExistsMisPublicaciones;
    private DatabaseReference mRootRef;
    private Button btnPublicacion;
    private FragmentManager fm;

    //PRUEBA BD
    private RecyclerView recyclerViewMisPublicaciones;
    private ArrayList<Publicacion> listaPublicaciones;
    private AdaptadorPublicaciones adaptadorPublicaciones;
    private String mCurrentUserId;

    public MisPublicacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_publicaciones, container, false);
        recyclerViewMisPublicaciones = view.findViewById(R.id.RecycleridMis);
        recyclerViewMisPublicaciones.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewMisPublicaciones.setLayoutManager(linearLayoutManager);
        noExistsMisPublicaciones = (TextView) view.findViewById(R.id.no_exist_mis_publicaciones);
        mCurrentUserId = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_TOKEN);

        //
        fm = getFragmentManager();
        listaPublicaciones = new ArrayList<>();
        adaptadorPublicaciones = new AdaptadorPublicaciones(getContext(), listaPublicaciones, fm);
        recyclerViewMisPublicaciones.setAdapter(adaptadorPublicaciones);

        btnPublicacion = view.findViewById(R.id.idnuevaPublicacion);
        btnPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NuevaPublicacionActivity.class);
                startActivity(intent);
            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference().child("Publicaciones");
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPublicaciones.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Publicacion publicacion = snapshot.getValue(Publicacion.class);
                    if (!publicacion.isEstaEliminado()){
                        if (publicacion.getTokenUser() != null){
                            if (publicacion.getTokenUser().equals(mCurrentUserId)){
                                listaPublicaciones.add(publicacion);


                            }
                        }
                    }
                }
                if (listaPublicaciones.isEmpty()){
                    noExistsMisPublicaciones.setVisibility(View.VISIBLE);
                }else {
                    noExistsMisPublicaciones.setVisibility(View.GONE);
                }
                adaptadorPublicaciones.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DatosUsuario user = snapshot.getValue(DatosUsuario.class);

                    if(user.getToken().equals(mCurrentUserId)){
                        String urlImage=user.getImagenurl();
                        adaptadorPublicaciones.enviarImagenurl(urlImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
