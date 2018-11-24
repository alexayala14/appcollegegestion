package com.arz.chech.collegegestion.adapters;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuariosHolder> implements View.OnClickListener{

    ArrayList<UsuarioList> listaUsuarios;

    private View.OnClickListener listener;
    public UsuariosAdapter(ArrayList<UsuarioList> listaUsuarios){
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarios_list, parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new UsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(UsuariosHolder holder, int position) {
        holder.txtNombre.setText(listaUsuarios.get(position).getNombre());
        holder.txtApellido.setText(listaUsuarios.get(position).getApellido());
        holder.txtRut.setText(listaUsuarios.get(position).getRut().toString());
        holder.txtPerfil.setText(listaUsuarios.get(position).getPerfil().toString());
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class UsuariosHolder extends RecyclerView.ViewHolder{
        TextView txtNombre, txtApellido, txtRut, txtPerfil;

        public UsuariosHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido = itemView.findViewById(R.id.txtApellido);
            txtRut = itemView.findViewById(R.id.txtRut);
            txtPerfil = itemView.findViewById(R.id.txtPerfil);
        }
    }
}
