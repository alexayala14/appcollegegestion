package com.arz.chech.collegegestion.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.entidades.Messages;
import com.arz.chech.collegegestion.preferences.Preferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ChatAdapterGroup extends RecyclerView.Adapter<ChatAdapterGroup.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private String nombreReceiver, apellidoReceiver;
    private List<Messages> mChat;
    private static String dateFormat = "hh:mm a";
    private static String añoFormat = "dd-MM-yy";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private static SimpleDateFormat simpleAñoFormat = new SimpleDateFormat(añoFormat);
    private Context mContext;
    private String mCurrent_user_id;

    public ChatAdapterGroup(Context context, List<Messages> mChat) {
        this.mContext = context;
        this.mChat = mChat;
    }

    @Override
    public ChatAdapterGroup.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapterGroup.ViewHolder(v);
        }else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapterGroup.ViewHolder(v);
        }
    }

    public static String formatearHora(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatearAño(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleAñoFormat.format(calendar.getTime());
    }


    @Override
    public void onBindViewHolder(final ChatAdapterGroup.ViewHolder viewHolder, int i) {
        final Messages chat = mChat.get(i);
        long time = chat.getTime();
        final String hora = formatearHora(time);
        final String año = formatearAño(time);
        viewHolder.show_message.setText(chat.getMessage());
        viewHolder.show_hora.setText(hora);
        viewHolder.show_año.setText(año);
        if (viewHolder.getItemViewType() == 1){
            viewHolder.show_name.setText("Tu");
        } else{
            viewHolder.show_name.setText(nombreReceiver + " " + apellidoReceiver);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_name;
        public TextView show_message;
        public TextView show_hora;
        public TextView show_año;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_name = itemView.findViewById(R.id.name_text_layout);
            show_message = itemView.findViewById(R.id.message_text_layout);
            show_hora = itemView.findViewById(R.id.time_text_layout);
            show_año = itemView.findViewById(R.id.fecha_text_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        mCurrent_user_id = Preferences.obtenerPreferenceString(mContext, Preferences.PREFERENCE_TOKEN);
        if (mChat.get(position).getFrom().equals(mCurrent_user_id)){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    public void enviarDatos(String nombre, String apellido) {
        nombreReceiver = nombre;
        apellidoReceiver = apellido;
    }
}
