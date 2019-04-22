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
import com.arz.chech.collegegestion.activities.PerfilGrupoActivity;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactosAgregadosPerfilGrupoAdapter extends RecyclerView.Adapter<ContactosAgregadosPerfilGrupoAdapter.ViewHolder> {
    private ArrayList<DatosUsuario> datosUsuarioList;
    private Context mContext;
    private DatabaseReference mUsersDatabase;
    private String currentGroupName;

    public ContactosAgregadosPerfilGrupoAdapter(Context mContext, ArrayList<DatosUsuario> mUsers){
        this.mContext = mContext;
        this.datosUsuarioList = mUsers;
    }

    @NonNull
    @Override
    public ContactosAgregadosPerfilGrupoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_single_layout_grupos_perfil, viewGroup, false);
        return new ContactosAgregadosPerfilGrupoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactosAgregadosPerfilGrupoAdapter.ViewHolder viewHolder, int i) {
        final DatosUsuario datosUsuario = datosUsuarioList.get(i);
        viewHolder.setName(datosUsuario.getNombre(), datosUsuario.getApellido());
        /*viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PerfilGrupoActivity.class);
                intent.putExtra("user_id", datosUsuario.getToken());
                intent.putExtra("user_name", datosUsuario.getNombre());
                intent.putExtra("user_apellido", datosUsuario.getApellido());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });*/
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
            username.setText(name + " " + apellido);
        }

    }
    public void enviarGrupo(String currentGroupName1){
        currentGroupName=currentGroupName1;
        System.out.println("el valor es: "+currentGroupName);
    }
}
