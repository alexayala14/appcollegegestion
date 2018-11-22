package com.arz.chech.collegegestion.adapters;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.activities.Chat;
import com.arz.chech.collegegestion.activities.Messages;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.content.ContextCompat.getSystemService;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    LinearLayout layout;
    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private static String dateFormat = "hh:mm a, dd-MM-yyyy";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list , parent, false);
        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView displayName;
        public TextView timeText;
        public CardView cardView;
        public LinearLayout linearLayout;

        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            timeText = (TextView) view.findViewById(R.id.time_text_layout);
            cardView = (CardView) view.findViewById(R.id.cvMensaje);
            linearLayout = (LinearLayout) view.findViewById(R.id.mensajeBG);
        }
    }

    public static String formatearHora(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        final Messages c = mMessageList.get(i);
        final String from_user = c.getFrom();
        final String message_type = c.getType();
        final long time = c.getTime();
        final String hora = formatearHora(time);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int tipo = 0;
                String name = dataSnapshot.child("nombre").getValue().toString();
                String apellido = dataSnapshot.child("apellido").getValue().toString();
                String rut = dataSnapshot.child("rut").getValue().toString();

                // SETEAR
                if(message_type.equals("text")) {
                    viewHolder.messageText.setText(c.getMessage());
                } else {
                    viewHolder.messageText.setVisibility(View.INVISIBLE);
                }
                viewHolder.timeText.setText(hora);
                if (UserDetails.username.equals(rut)){
                    viewHolder.displayName.setText("Tu");
                    tipo = 1;
                } else {
                    viewHolder.displayName.setText(name + " " + apellido);
                    tipo = 2;
                }

                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) viewHolder.cardView.getLayoutParams();
                FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) viewHolder.linearLayout.getLayoutParams();
                if (tipo == 1){
                    viewHolder.linearLayout.setBackgroundResource(R.drawable.text_in);
                    rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                    rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    fl.gravity = Gravity.RIGHT;
                }else{
                    viewHolder.linearLayout.setBackgroundResource(R.drawable.text_out);
                    rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                    rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    fl.gravity = Gravity.LEFT;
                }
                viewHolder.cardView.setLayoutParams(rl);
                viewHolder.linearLayout.setLayoutParams(fl);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

}
