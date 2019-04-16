package com.arz.chech.collegegestion.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.PerfilGrupoActivity;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarIntegranteGrupoAdapter extends RecyclerView.Adapter<AgregarIntegranteGrupoAdapter.ViewHolder> {

    private ArrayList<DatosUsuario> datosUsuarioList;
    private Context mContext;

    public AgregarIntegranteGrupoAdapter(Context mContext, ArrayList<DatosUsuario> mUsers){

        this.mContext = mContext;
        this.datosUsuarioList = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_single_layout_group_select, viewGroup, false);
        return new AgregarIntegranteGrupoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final DatosUsuario datosUsuario = datosUsuarioList.get(i);
        viewHolder.setName(datosUsuario.getNombre(), datosUsuario.getApellido());

        Glide
                .with(mContext)
                .load(datosUsuario.getImagenurl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        viewHolder.mProgressBar.setVisibility(View.GONE);
                        viewHolder.imageView.setVisibility(View.VISIBLE);
                        viewHolder.imageView.setImageResource(R.drawable.default_avatar);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.mProgressBar.setVisibility(View.GONE);
                        viewHolder.imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })

                .into(viewHolder.imageView);


        /*viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PerfilGrupoActivity.class);

                intent.putExtra("datosUsuariosList", mem);
                intent.putExtra("nombreGrupo",grupo.getGroupId());
                intent.putExtra("nombreG", grupo.getName());
                intent.putExtra("imagenurl",grupo.getImagenurl());
                mContext.startActivity(intent);
            }
        });*/

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = ((Activity)mContext).getIntent();
                intent.putExtra("user_id", datosUsuario.getToken());
                /*intent.putExtra("user_name", datosUsuario.getNombre());
                intent.putExtra("user_apellido", datosUsuario.getApellido());*/

                ((Activity)mContext).setResult(Activity.RESULT_OK,intent);
                //((Activity)mContext).setResult(50,intent);

                ((Activity)mContext).finish();
                //userid=datosUsuario.getToken();
                //((Activity)mContext).onBackPressed();
            }
        });

    }

    @Override
    public int getItemCount() {
        return  datosUsuarioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView username;
        public CircleImageView imageView;
        public ProgressBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            username = (TextView) mView.findViewById(R.id.user_single_name);
            imageView=itemView.findViewById(R.id.user_single_image);
            mProgressBar=itemView.findViewById(R.id.idprogessbar);
        }

        public void setName(String name, String apellido){
            username.setText(name + " " + apellido);
        }
    }


}
