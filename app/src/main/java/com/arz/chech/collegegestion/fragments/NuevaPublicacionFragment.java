package com.arz.chech.collegegestion.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.notifications.Client;
import com.arz.chech.collegegestion.notifications.Data;
import com.arz.chech.collegegestion.notifications.MyResponse;
import com.arz.chech.collegegestion.notifications.Sender;
import com.arz.chech.collegegestion.notifications.Token;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.arz.chech.collegegestion.entidades.UserDetails;
import com.arz.chech.collegegestion.request.PublicacionRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuevaPublicacionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NuevaPublicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuevaPublicacionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View vista;
    Button btnPublicar;
    Button btnCancelar;
    EditText textPublicacion;
    EditText textAsunto;
    RadioGroup radioGroup;
    int selected;
    private APIService apiService;


    private DatabaseReference mRootRef, reference;
    private String userid;
    private String userName;
    private String userApellido;
    private String mCurrentUserId;

    ////notificaciones
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID="NOTIFICACION";
    private final static int NOTIFICACION_ID=0014;


    public NuevaPublicacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NuevaPublicacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NuevaPublicacionFragment newInstance(String param1, String param2) {
        NuevaPublicacionFragment fragment = new NuevaPublicacionFragment();
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
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        vista=inflater.inflate(R.layout.fragment_nueva_publicacion, container, false);
        Button btnCancelar=(Button)vista.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction mispublicaciones = getFragmentManager().beginTransaction();
                mispublicaciones.replace(R.id.contenedor, new MisPublicacionesFragment());
                mispublicaciones.commit();
            }
        });
        userName = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_NOMBRE);
        userApellido = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_APELLIDO);
        mCurrentUserId = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_TOKEN);
        textAsunto=(EditText) vista.findViewById(R.id.editTextAsunto);
        textPublicacion =(EditText) vista.findViewById(R.id.editTextPublicacion);
        radioGroup=(RadioGroup) vista.findViewById(R.id.radioGroup);
        Button btnPublicar =(Button) vista.findViewById(R.id.btnPublicar);
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //MisPublicacionesFragment misPublicaciones=new MisPublicacionesFragment();
                //getActivity().getSupportFragmentManager().beginTransaction()
                //      .replace(R.id.container,misPublicaciones)
                //    .addToBackStack(null)
                //  .commit();
                if (textAsunto.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Debe ingresar Asunto", Toast.LENGTH_SHORT).show();
                }else if (textPublicacion.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "La publicacion no puede estar vacia", Toast.LENGTH_SHORT).show();
                }else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(view.getContext(), "Seleccion la prioridad por favor", Toast.LENGTH_SHORT).show();
                }else {
                    final String rut = UserDetails.username;
                    final String asunto = textAsunto.getText().toString();
                    final String descripcion = textPublicacion.getText().toString();
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radioButtonAlta:
                            selected = 1;
                            break;
                        case R.id.radioButtonMedia:
                            selected = 2;
                            break;
                        case R.id.radioButtonBaja:
                            selected = 3;
                            break;
                    }
                    final String id_prioridad = String.valueOf(selected);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success){
                                    //Toast.makeText(view.getContext(), "Publicación agregada..", Toast.LENGTH_SHORT).show();
                                    mRootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                DatosUsuario datosUsuario = snapshot.getValue(DatosUsuario.class);
                                                if (!datosUsuario.getToken().equals(mCurrentUserId)){
                                                    sendNotification(datosUsuario.getToken(), asunto);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    FragmentTransaction mispublicaciones = getFragmentManager().beginTransaction();
                                    mispublicaciones.replace(R.id.contenedor, new MisPublicacionesFragment());
                                    mispublicaciones.commit();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PublicacionRequest publicacionRequest = new PublicacionRequest(rut, id_prioridad, asunto, descripcion, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(view.getContext());
                    queue.add(publicacionRequest);
                }
                }
            });
        return vista;
        }

    private void sendNotification(final String receiver, final String asunto){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(mCurrentUserId, R.mipmap.ic_launcher, "Asunto: " + asunto, "Nueva Publicacion!", receiver);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                            if (response.code() == 200){
                                if (response.body().success != 1){
                                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
