package com.arz.chech.collegegestion.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.ChatActivityGroup;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Grupo;
import com.arz.chech.collegegestion.entidades.Messages;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessagesGroupAdapter extends RecyclerView.Adapter<MessagesGroupAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Grupo> groupList;
    private String theLastMessage;
    private Long theLastTime;
    private String firebaseUser;
    private static String dateFormat = "hh:mm a";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private  String groupId;
    private String nom;

    public MessagesGroupAdapter(Context context, ArrayList<Grupo> groupList){
        this.groupList = groupList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MessagesGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_single_layout_group, viewGroup, false);
        return new MessagesGroupAdapter.ViewHolder(view);
    }

    public static String formatearHora(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesGroupAdapter.ViewHolder viewHolder, int i) {
        final Grupo grupo = groupList.get(i);
        viewHolder.username.setText(grupo.getName());
        //ArrayList<DatosUsuario> us = new ArrayList<DatosUsuario>();
        for(DatosUsuario j:grupo.getMembers()){
            lastMessage(j.getToken(), viewHolder.message, viewHolder.display_time,groupList.get(i));



        }


        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivityGroup.class);
                ArrayList<DatosUsuario> mem = new ArrayList<DatosUsuario>();
                for(DatosUsuario i:grupo.getMembers()){
                    mem.add(i);


                }

                intent.putExtra("datosUsuariosList", mem);
                intent.putExtra("nombreGrupo",grupo.getGroupId());
                intent.putExtra("name", grupo.getName());



                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TextView username;
        public TextView message;
        public TextView display_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            username = itemView.findViewById(R.id.user_single_name);
            message = itemView.findViewById(R.id.user_single_status);
            display_time = itemView.findViewById(R.id.user_single_time);
        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg, final TextView last_time,final Grupo grupo){
        theLastMessage = "default";
        theLastTime = 0L;
        firebaseUser = Preferences.obtenerPreferenceString(mContext, Preferences.PREFERENCE_TOKEN);
        System.out.println("EL VALOR DE ID ES: "+userid);


       /* for(Messages k:grupo.getMessages()){

            System.out.println("EL VALOR DEL GRUPO ID ES: "+ k.getFrom());

        }*/

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups").child(grupo.getGroupId()).child("Messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    reference.getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1:dataSnapshot.getChildren()){

                                Messages chat = snapshot1.getValue(Messages.class);
                                //System.out.println("El mensaje esssssss: "+chat.getFrom());
                                if (firebaseUser != null && chat != null) {
                                    if (/*chat.getReceiver().equals(firebaseUser) && */chat.getFrom().equals(userid) ||
                                            /*chat.getReceiver().equals(userid) && */chat.getFrom().equals(firebaseUser)) {
                                        theLastMessage = chat.getMessage();
                                        theLastTime = chat.getTime();

                                    }
                                }


                            }

                            switch (theLastMessage){
                                case  "default":
                                    last_msg.setText("No hay mensajes");
                                    last_time.setText("");
                                    break;

                                default:
                                    last_msg.setText(theLastMessage);
                                    last_time.setText(formatearHora(theLastTime));
                                    break;
                            }

                            theLastMessage = "default";

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void enviarDatos(String groupIda) {
        groupId = groupIda;

    }
}
