package com.arz.chech.collegegestion.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.entidades.Publicacion;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.UserDetails;
import com.arz.chech.collegegestion.adapters.AdaptadorPublicaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MisPublicacionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MisPublicacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisPublicacionesFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    Button btnPublicacion;
    View vista;

    //PRUEBA BD
    RecyclerView recyclerViewMisPublicaciones;
    ArrayList<Publicacion> listaPublicaciones;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public MisPublicacionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisPublicacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MisPublicacionesFragment newInstance(String param1, String param2) {
        MisPublicacionesFragment fragment = new MisPublicacionesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_mis_publicaciones, container, false);
        listaPublicaciones = new ArrayList<>();
        recyclerViewMisPublicaciones = vista.findViewById(R.id.RecycleridMis);
        recyclerViewMisPublicaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMisPublicaciones.setHasFixedSize(true);
        request = Volley.newRequestQueue(getContext());
        cargarWebService();
        btnPublicacion= vista.findViewById(R.id.idnuevaPublicacion);
        btnPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaciar();
                btnPublicacion.setVisibility(View.GONE);
                recyclerViewMisPublicaciones.setAdapter(null);
                FragmentTransaction nuevaPublicacionFragment = getFragmentManager().beginTransaction();
                nuevaPublicacionFragment.replace(R.id.contenedor,new NuevaPublicacionFragment());
                nuevaPublicacionFragment.commit();
            }
        });
        return vista;
    }

    private void cargarWebService() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();
        String userRut = UserDetails.username;
        String url = "http://miltonzambra.com/ConsultaPublicacionesPropias.php?rut=" + userRut;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void vaciar(){
        listaPublicaciones.clear();
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

    @Override
    public void onErrorResponse(VolleyError error) {
        //Toast.makeText(getContext(), "No hay publicaciones en la BD!", Toast.LENGTH_LONG).show();
        progress.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        Publicacion publicacion = null;
        JSONArray json = response.optJSONArray("publicaciones");
        try {
            for (int i=0; i<json.length(); i++){
                publicacion = new Publicacion();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                publicacion.setNombre(jsonObject.optString("nombre") + " " + jsonObject.optString("apellido"));
                publicacion.setAsunto(jsonObject.optString("asunto"));
                publicacion.setDescripcion(jsonObject.optString("descripcion"));
                publicacion.setFoto(R.drawable.hombre);
                listaPublicaciones.add(publicacion);
            }
            AdaptadorPublicaciones adapter = new AdaptadorPublicaciones(listaPublicaciones);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = getFragmentManager();
                    DetalleFragment dialogFragment = new DetalleFragment ();
                    Bundle b = new Bundle();
                    b.putString("descripcion",listaPublicaciones.get(recyclerViewMisPublicaciones.getChildAdapterPosition(view)).getAsunto()+"\n");
                    b.putString ("detalle", listaPublicaciones.get(recyclerViewMisPublicaciones.getChildAdapterPosition(view)).getDescripcion());
                    dialogFragment.setArguments(b);
                    dialogFragment.show(fm, "Sample Fragment");
                }
            });
            recyclerViewMisPublicaciones.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.dismiss();
    }

    /*@Override
    public void onFragmentInteraction(Uri uri) {

    }*/

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

    //ACTUALIZA EL FRAGMENT
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
