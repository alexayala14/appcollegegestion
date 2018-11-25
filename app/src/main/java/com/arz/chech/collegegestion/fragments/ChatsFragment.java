package com.arz.chech.collegegestion.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.ChatActivity;
import com.arz.chech.collegegestion.activities.Conv;
import com.arz.chech.collegegestion.activities.FriendsActivity;
import com.arz.chech.collegegestion.activities.Preferences;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView mConvList;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;
    FloatingActionButton fab;
    public static TextView noExistMensajes;

    private String mCurrent_user_id;

    private View mMainView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ChatsFragment.OnFragmentInteractionListener mListener;

    public ChatsFragment() {
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChatsFragment.OnFragmentInteractionListener) {
            mListener = (ChatsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_chat, container, false);
        noExistMensajes = (TextView) mMainView.findViewById(R.id.no_exist_msj);
        mConvList = (RecyclerView) mMainView.findViewById(R.id.conv_list);

        mCurrent_user_id = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_TOKEN);
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);
        fab = (FloatingActionButton) mMainView.findViewById(R.id.my_fab);

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getContext(), FriendsActivity.class);
                startActivity(chatIntent);
            }
        });
        Query conversationQuery = mConvDatabase.orderByChild("timestamp");
        FirebaseRecyclerAdapter<Conv, ConvViewHolder> firebaseConvAdapter = new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(
                Conv.class,
                R.layout.users_single_layout,
                ConvViewHolder.class,
                conversationQuery
        ) {
            @Override
            protected void populateViewHolder(final ConvViewHolder convViewHolder, final Conv conv, int i) {
                final String list_user_id = getRef(i).getKey();
                Query lastMessageQuery = null;
                if (list_user_id != null) {
                    lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);
                }
                if (lastMessageQuery != null) {
                    lastMessageQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                            String data = dataSnapshot.child("message").getValue().toString();
                            convViewHolder.setMessage(data, conv.isSeen());
                            convViewHolder.setTime(conv.getTimestamp(), conv.isSeen());
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        }
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                //Toast.makeText(getContext(), mFriendsDatabase.child(list_user_id).toString(), Toast.LENGTH_LONG).show();
                if (list_user_id != null) {
                    mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String userName = dataSnapshot.child("nombre").getValue().toString();
                            final String userApellido = dataSnapshot.child("apellido").getValue().toString();
                            convViewHolder.setName(userName, userApellido);
                            convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                    chatIntent.putExtra("user_id", list_user_id);
                                    chatIntent.putExtra("user_name", userName);
                                    chatIntent.putExtra("user_apellido", userApellido);
                                    startActivity(chatIntent);
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
        if (Preferences.obtenerPreferenceBoolean(getContext(), Preferences.PREFERENCE_MENSAJES)){
            noExistMensajes.setVisibility(View.GONE);
        }else {
            noExistMensajes.setVisibility(View.VISIBLE);
        }
        mConvList.setAdapter(firebaseConvAdapter);
    }

    public static class ConvViewHolder extends RecyclerView.ViewHolder {
        View mView;
        private static String dateFormat = "hh:mm a";
        private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        public ConvViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setMessage(String message, boolean isSeen){
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(message);
            if(!isSeen){
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }
        }

        public String formatearHora(long milliSeconds){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return simpleDateFormat.format(calendar.getTime());
        }

        public void setTime(long time, boolean isSeen){
            TextView userTimeView = (TextView) mView.findViewById(R.id.user_single_time);
            String horaFormat = formatearHora(time);
            userTimeView.setText(horaFormat);
            if(!isSeen){
                userTimeView.setTypeface(userTimeView.getTypeface(), Typeface.BOLD_ITALIC);
            } else {
                userTimeView.setTypeface(userTimeView.getTypeface(), Typeface.NORMAL);
            }
        }

        public void setName(String name, String apellido){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name + " " + apellido);
        }
    }
}
