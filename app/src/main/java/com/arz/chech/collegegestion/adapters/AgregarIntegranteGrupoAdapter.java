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
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/*<ProgressBar
        android:layout_width="64dp"
        android:layout_height="64dp"
        android:id="@+id/idprogessbar"
        android:visibility="visible"/>*/

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarIntegranteGrupoAdapter extends RecyclerView.Adapter<AgregarIntegranteGrupoAdapter.ViewHolder> {

    private ArrayList<DatosUsuario> datosUsuarioList;
    private ArrayList<DatosUsuario> datosUsuarios1;
    private Context mContext;
    private DatabaseReference mrefGrupo;
    private String currentGroupName;
    private String imagenurl;
    private String nombreGrupo;

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

        viewHolder.setImageView(datosUsuario.getImagenurl());

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

                //Intent intent = new Intent(mContext, PerfilGrupoActivity.class);
                //intent.putExtra("user_id", datosUsuario.getToken());
                /*intent.putExtra("user_name", datosUsuario.getNombre());
                intent.putExtra("user_apellido", datosUsuario.getApellido());*/

                //((Activity)mContext).setResult(Activity.RESULT_OK,intent);
                datosUsuarios1.add(datosUsuario);
                for (int i =0;i<datosUsuarios1.size();i++){
                    int cont=0;
                    for (int j =0;j<datosUsuarios1.size()-1;j++){
                        if ((datosUsuarios1.get(i).getToken()).equals(datosUsuarios1.get(j).getToken())){
                            cont++;

                        }
                        if(cont==2){
                            cont--;
                            datosUsuarios1.remove(i);
                        }
                    }
                }

                mrefGrupo= FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("members");
                mrefGrupo.setValue(datosUsuarios1);
                Intent intent = new Intent(mContext, PerfilGrupoActivity.class);
                intent.putExtra("datosUsuariosList", datosUsuarios1);
                intent.putExtra("nombreGrupo",currentGroupName);
                intent.putExtra("nombreG", nombreGrupo);
                intent.putExtra("imagenurl",imagenurl);
                ((Activity)mContext).startActivity(intent);
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


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            username = (TextView) mView.findViewById(R.id.user_single_name);
            imageView=itemView.findViewById(R.id.user_single_image);

        }

        public void setName(String name, String apellido){
            username.setText(name + " " + apellido);
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

    public void enviarGrupo(String currentGroupName1,ArrayList<DatosUsuario> datosUsuarios,String nombreGrupo1,String imagenurl1){
        currentGroupName=currentGroupName1;
        datosUsuarios1=datosUsuarios;
        nombreGrupo=nombreGrupo1;
        imagenurl=imagenurl1;
        System.out.println("el valor es en adapterintegrante: "+currentGroupName);
    }


}
