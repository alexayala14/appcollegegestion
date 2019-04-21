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
import com.arz.chech.collegegestion.activities.ChatActivity;
import com.arz.chech.collegegestion.entidades.Messages;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private Context mContext;
    private List<DatosUsuario> mUsers;
    private String theLastMessage;
    private Long theLastTime;
    private String firebaseUser;
    private static String dateFormat = "hh:mm a";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    public MessagesAdapter(Context context, List<DatosUsuario> mUsers){
        this.mUsers = mUsers;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_single_layout_chat, viewGroup, false);
        return new MessagesAdapter.ViewHolder(view);
    }

    public static String formatearHora(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final DatosUsuario datosUsuario = mUsers.get(i);
        viewHolder.username.setText(datosUsuario.getNombre() + " " + datosUsuario.getApellido());
        lastMessage(datosUsuario.getToken(), viewHolder.message, viewHolder.display_time);
        viewHolder.setImageView(datosUsuario.getImagenurl());
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", datosUsuario.getToken());
                intent.putExtra("user_name", datosUsuario.getNombre());
                intent.putExtra("user_apellido", datosUsuario.getApellido());
                intent.putExtra("urlimagen",datosUsuario.getImagenurl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TextView username;
        public TextView message;
        public TextView display_time;
        public CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            username = itemView.findViewById(R.id.user_single_name);
            message = itemView.findViewById(R.id.user_single_status);
            display_time = itemView.findViewById(R.id.user_single_time);
            imageView=mView.findViewById(R.id.user_single_image);
        }
        public void setImageView(String imagenurl){
            Glide
                    .with(mContext)
                    .load(imagenurl)
                    .fitCenter()
                    .error(R.drawable.default_avatar)
                    .into(imageView);
        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg, final TextView last_time){
        theLastMessage = "default";
        theLastTime = 0L;
        firebaseUser = Preferences.obtenerPreferenceString(mContext, Preferences.PREFERENCE_TOKEN);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages").child(firebaseUser).child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Messages chat = snapshot.getValue(Messages.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser) && chat.getFrom().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getFrom().equals(firebaseUser)) {
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
