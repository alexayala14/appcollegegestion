package com.arz.chech.collegegestion.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactosAgregadosPerfilGrupoAdapter extends RecyclerView.Adapter<ContactosAgregadosPerfilGrupoAdapter.ViewHolder> {
    private ArrayList<DatosUsuario> datosUsuarioList;
    private Context mContext;
    private DatabaseReference mUsersDatabase;
    private String currentGroupName;
    private DatabaseReference mrefGrupo;
    private String imagenurl;
    private String nombreGrupo;
    private ArrayList<DatosUsuario> datosUsuarios1;

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
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext, PerfilGrupoActivity.class);
                intent.putExtra("user_id", datosUsuario.getToken());
                intent.putExtra("user_name", datosUsuario.getNombre());
                intent.putExtra("user_apellido", datosUsuario.getApellido());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();*/
                final CharSequence[] options={"Eliminar","Cancelar"};
                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setTitle("Elegir una Opcion: ");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if(options[seleccion]=="Eliminar"){
                            int i=0;
                            System.out.println("VA A ELIMINAR NOMBRE GRUPO: "+currentGroupName);
                            System.out.println("EL USUARIO ES: "+datosUsuario.getNombre());
                            //String indice=String.valueOf(i);
                            //mrefGrupo= FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("members").child(indice).child("estaEliminado");
                            //mrefGrupo.setValue(true);



                            try {
                                for(DatosUsuario user:datosUsuarios1){
                                    if(user.getToken().equals(datosUsuario.getToken())){
                                        String indice = String.valueOf(i);
                                        //System.out.println("VA A ELIMINAR NOMBRE GRUPO: "+currentGroupName);
                                       // mrefGrupo= FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("members").child(indice).child("estaEliminado");
                                       // mrefGrupo.setValue(true);
                                    }
                                    i++;
                                }
                            }catch (Exception e){

                            }



                        }else  if(options[seleccion]=="Cancelar"){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
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
            username.setText(name + " " + apellido);
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
