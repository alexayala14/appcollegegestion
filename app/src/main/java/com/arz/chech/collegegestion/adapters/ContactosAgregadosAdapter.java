package com.arz.chech.collegegestion.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.ChatActivity;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactosAgregadosAdapter extends RecyclerView.Adapter<ContactosAgregadosAdapter.ViewHolder>{
    private ArrayList<DatosUsuario> datosUsuarioList;
    private Context mContext;
    private DatabaseReference mUsersDatabase;
    private String currentGroupName;

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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final DatosUsuario datosUsuario = datosUsuarioList.get(i);
        viewHolder.setName(datosUsuario.getNombre(), datosUsuario.getApellido());
        Glide
                .with(mContext)
                .load(datosUsuario.getImagenurl())
                .fitCenter()
                .error(R.drawable.default_avatar)
                .into(viewHolder.imageView);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");
        //currentGroupName = getIntent().getStringExtra("nombreGrupo");
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("el token de usuario es: "+);
                //mUsersDatabase.child("-LcmoPwdkACaK2ayeFbM").child("members").child("").removeValue();
                System.out.println("Miembro eliminado del grupo");

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
        public CircleImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            username = (TextView) mView.findViewById(R.id.user_single_name);
            imageView=itemView.findViewById(R.id.user_single_image);

        }

        public void setName(String name, String apellido){
            String nomape=name + " " + apellido;
            username.setText(nomape);
        }

    }
    public void enviarGrupo(String currentGroupName1){
        currentGroupName=currentGroupName1;
        System.out.println("el valor es: "+currentGroupName);
    }
}
