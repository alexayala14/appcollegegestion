package com.example.chech.login;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorPublicaciones extends RecyclerView.Adapter<AdaptadorPublicaciones.ViewHolderPublicaciones> {
    ArrayList<Publicacion> listaPublicaciones;
    public AdaptadorPublicaciones(ArrayList<Publicacion>listaPublicaciones){
        this.listaPublicaciones=listaPublicaciones;
    }
    @NonNull
    @Override
    public ViewHolderPublicaciones onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_publicaciones,null,false);
        return new ViewHolderPublicaciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPublicaciones holder, int position) {
        holder.txt_nombre.setText(listaPublicaciones.get(position).getNombre());
        holder.txt_descripcion.setText(listaPublicaciones.get(position).getDescripcion());
        holder.foto.setImageResource(listaPublicaciones.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public class ViewHolderPublicaciones extends RecyclerView.ViewHolder {
        TextView txt_nombre,txt_descripcion;
        ImageView foto;
        public ViewHolderPublicaciones(@NonNull View itemView) {
            super(itemView);
            txt_nombre = itemView.findViewById(R.id.idNombre);
            txt_descripcion = itemView.findViewById(R.id.idInfo);
            foto = itemView.findViewById(R.id.idImagen);
        }
    }
}
