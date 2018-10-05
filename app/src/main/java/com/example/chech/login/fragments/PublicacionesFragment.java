package com.example.chech.login.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chech.login.Publicacion;
import com.example.chech.login.R;
import com.example.chech.login.adapters.AdaptadorPublicaciones;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicacionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicacionesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ArrayList<Publicacion> listaPublicaciones;
    RecyclerView recyclerViewPublicaciones;

    public PublicacionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicacionesFragment newInstance(String param1, String param2) {
        PublicacionesFragment fragment = new PublicacionesFragment();
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
        View vista=inflater.inflate(R.layout.fragment_publicaciones, container, false);
        listaPublicaciones = new ArrayList<>();
        recyclerViewPublicaciones=vista.findViewById(R.id.Recyclerid);
        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarPublicaciones();
        AdaptadorPublicaciones adapter = new AdaptadorPublicaciones(listaPublicaciones);
        recyclerViewPublicaciones.setAdapter(adapter);


        return vista;
    }

    private void llenarPublicaciones(){
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio",R.drawable.hombre));
    }

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
