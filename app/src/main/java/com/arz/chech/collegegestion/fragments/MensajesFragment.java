package com.arz.chech.collegegestion.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.UserDetails;
import com.arz.chech.collegegestion.activities.Chat;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MensajesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MensajesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MensajesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View vista;
    ListView usersList;
    TextView noUsersText;
    ArrayList al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    // Variable del servicio Firebase
    DatabaseReference databaseReference;

    public MensajesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MensajesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MensajesFragment newInstance(String param1, String param2) {
        MensajesFragment fragment = new MensajesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_mensajes, container, false);
        // Obtener referencia de los objetos en las variables
        usersList = (ListView)vista.findViewById(R.id.usersList);
        noUsersText = (TextView)vista.findViewById(R.id.noUsersText);
        // Iniciar progressDialog
        pd = new ProgressDialog(getContext());
        pd.setMessage("Cargando...");
        pd.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    DatosUsuario datosUsuario = datasnapshot.getValue(DatosUsuario.class);
                    if (!datosUsuario.getRut().equals(UserDetails.username)) {
                        al.add(datosUsuario.toString());
                        totalUsers++;
                    }else {
                        UserDetails.nombre = datosUsuario.getNombre();
                        UserDetails.apellido = datosUsuario.getApellido();
                    }
                }
                if(totalUsers < 1){
                    // Mostrar TextView y Ocultar ListView
                    noUsersText.setVisibility(View.VISIBLE);
                    usersList.setVisibility(View.GONE);
                }
                else{
                    // Ocultar TextView y Mostrar ListView
                    noUsersText.setVisibility(View.GONE);
                    usersList.setVisibility(View.VISIBLE);
                    // Añadir usuarios al ListView
                    usersList.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, al));
                }
                // Finalizar el progressDialog
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = (String) al.get(position);

                startActivity(new Intent(getContext(), Chat.class));
            }
        });
        vaciar();
        usersList.setAdapter(null);
        return vista;
    }

    public void vaciar(){al.clear();}

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
