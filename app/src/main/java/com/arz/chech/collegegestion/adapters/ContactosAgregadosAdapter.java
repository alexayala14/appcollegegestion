package com.arz.chech.collegegestion.adapters;


import android.app.Activity;
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
import com.arz.chech.collegegestion.entidades.DatosUsuario;

import java.util.ArrayList;

public class ContactosAgregadosAdapter extends RecyclerView.Adapter<ContactosAgregadosAdapter.ViewHolder>{
    private ArrayList<DatosUsuario> datosUsuarioList;
    private Context mContext;

    public ContactosAgregadosAdapter(Context mContext, ArrayList<DatosUsuario> mUsers){
        this.mContext = mContext;
        this.datosUsuarioList = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_single_layout_group_select, viewGroup, false);
        return new ContactosAgregadosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final DatosUsuario datosUsuario = datosUsuarioList.get(i);
        viewHolder.setName(datosUsuario.getNombre(), datosUsuario.getApellido());
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", datosUsuario.getToken());
                intent.putExtra("user_name", datosUsuario.getNombre());
                intent.putExtra("user_apellido", datosUsuario.getApellido());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();*/

                System.out.println("INGRESO AL BOTON");
            }
        });
    }

    @Override
    public int getItemCount() {
        return datosUsuarioList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            username = (TextView) mView.findViewById(R.id.user_single_name);
        }

        public void setName(String name, String apellido){
            String nomape=name + " " + apellido;
            username.setText(nomape);
        }
    }
}
