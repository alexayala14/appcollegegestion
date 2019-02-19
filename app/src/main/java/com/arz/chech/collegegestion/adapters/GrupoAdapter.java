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
import com.arz.chech.collegegestion.entidades.Grupo;

import java.util.ArrayList;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolder> {

    private ArrayList<Grupo> datosGrupoList;
    private Context mContext;

    public GrupoAdapter(Context mContext, ArrayList<Grupo> mUsers){
        this.mContext = mContext;
        this.datosGrupoList = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_single_layout_chat, viewGroup, false);
        return new GrupoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Grupo groups = datosGrupoList.get(i);
        viewHolder.setName(groups.getNombre());
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", groups.getToken());
                intent.putExtra("user_name", groups.getNombre());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return datosGrupoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            username = (TextView) mView.findViewById(R.id.user_single_name);
        }

        public void setName(String name){
            username.setText(name);
        }
    }

}
