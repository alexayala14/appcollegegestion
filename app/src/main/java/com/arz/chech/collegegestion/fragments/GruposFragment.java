package com.arz.chech.collegegestion.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.FriendsActivity;
import com.arz.chech.collegegestion.activities.MensajesGruposActivity;
import com.arz.chech.collegegestion.activities.NuevoGrupoActivity;
import com.arz.chech.collegegestion.activities.PerfilGrupoActivity;
import com.arz.chech.collegegestion.adapters.MessagesGroupAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Grupo;
import com.arz.chech.collegegestion.notifications.Token;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GruposFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private MessagesGroupAdapter messagesGroupAdapter;
    private List<DatosUsuario> mUsers;
    private String mCurrent_user_id;
    private DatabaseReference databaseReference;
    private List<String> usersList;
    private ArrayList<Grupo> groupList;
    private FloatingActionButton fab;
    private TextView noExistenMensajes;
    private DatabaseReference groupRef;
    private String groupId;
    private FloatingActionButton grup;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GruposFragment() {
        // Required empty public constructor
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grupos, container, false);
        recyclerView = view.findViewById(R.id.conv_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mCurrent_user_id = Preferences.obtenerPreferenceString(requireContext(), Preferences.PREFERENCE_TOKEN);
        noExistenMensajes = (TextView) view.findViewById(R.id.no_exist_msj_grupo);

        grup = (FloatingActionButton) view.findViewById(R.id.my_grup);
        grup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Es un grupo nuevo",Toast.LENGTH_LONG).show();
                //RequestNewGroup();

                Intent intent = new Intent(getActivity(), NuevoGrupoActivity.class);


                startActivity(intent);



            }
        });

        groupList =new ArrayList<>();
        usersList = new ArrayList<>();
        mUsers = new ArrayList<>();
        messagesGroupAdapter = new MessagesGroupAdapter(getContext(),groupList);
        recyclerView.setAdapter(messagesGroupAdapter);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        //
        databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //groupList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Grupo grupo = snapshot.getValue(Grupo.class);
                    assert grupo !=null;
                    groupId=grupo.getGroupId();
                    groupId = snapshot.getKey();
                    messagesGroupAdapter.enviarDatos(groupId);
                    usersList.add(snapshot.getKey());



                }
                chatList();

                //messagesGroupAdapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
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


    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(Preferences.obtenerPreferenceString(requireContext(), Preferences.PREFERENCE_TOKEN)).setValue(token1);
    }


    private void chatList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                   final Grupo grupo = snapshot.getValue(Grupo.class);
                    assert grupo !=null;


                    try {
                        for(DatosUsuario user:grupo.getMembers()) {
                            assert user !=null;


                            if (Preferences.obtenerPreferenceString(requireContext(), Preferences.PREFERENCE_TOKEN).equals(user.getToken())){
                                groupList.add(grupo);
                            }
                        }
                    }catch (Exception e){

                    }





                }
                if (groupList.isEmpty()){
                    noExistenMensajes.setVisibility(View.VISIBLE);
                }else {
                    noExistenMensajes.setVisibility(View.GONE);
                }
                messagesGroupAdapter.notifyDataSetChanged();
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
