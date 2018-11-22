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
import com.arz.chech.collegegestion.activities.UserDetails;
import com.arz.chech.collegegestion.activities.PublicacionRequest;

import org.json.JSONException;
import org.json.JSONObject;

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
        textAsunto=(EditText) vista.findViewById(R.id.editTextAsunto);
        textPublicacion =(EditText) vista.findViewById(R.id.editTextPublicacion);
        radioGroup=(RadioGroup) vista.findViewById(R.id.radioGroup);
        Button btnPublicar =(Button) vista.findViewById(R.id.btnPublicar);
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //MisPublicacionesFragment misPublicaciones=new MisPublicacionesFragment();
                //getActivity().getSupportFragmentManager().beginTransaction()
                //      .replace(R.id.container,misPublicaciones)
                //    .addToBackStack(null)
                //  .commit();
                if (textAsunto.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Debe ingresar nombre", Toast.LENGTH_SHORT).show();
                }else if (textPublicacion.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Debe ingresar apellido", Toast.LENGTH_SHORT).show();
                }else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(view.getContext(), "Debe ingresar rut", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(view.getContext(), "Publicación agregada..", Toast.LENGTH_SHORT).show();
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
                /////notificacion
                //setPendingIntent();
                createNotificationChannel();
                createNotification();
                }
            });
        return vista;
        }

    private void setPendingIntent(){
        Intent intent = new Intent(getActivity(),PublicacionesFragment.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(PublicacionesFragment.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent=stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name ="Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

    private void createNotification(){
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        builder.setContentTitle("Notificacion android");
        builder.setContentText("Cosasa quie van");
        builder.setColor(Color.CYAN);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        notificationManagerCompat.notify(NOTIFICACION_ID,builder.build());
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
    public void obtenerinfo(){
        Bundle b=getArguments();
         String descripcion=b.getString("descripcion", "DEFAULT_VALUE");
         String detalle=b.getString("detalle", "DEFAULT_VALUE");
        textAsunto.setText(descripcion);
        textPublicacion.setText(detalle);

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
