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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.activities.MenuPrincipalActivity;
import com.arz.chech.collegegestion.activities.ModifyUsersActivity;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder> implements Response.ErrorListener, Response.Listener<JSONObject> {

    private ArrayList<DatosUsuario> datosUsuarioList;
    private Context mContext;
    private DatabaseReference mUsersDatabase;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public AllUsersAdapter(Context mContext, ArrayList<DatosUsuario> mUsers){
        this.mContext = mContext;
        this.datosUsuarioList = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_single_layout_allusers, viewGroup, false);
        return new AllUsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final DatosUsuario datosUsuario = datosUsuarioList.get(i);
        viewHolder.setName(datosUsuario.getNombre());
        viewHolder.setApellido(datosUsuario.getApellido());
        viewHolder.setRut(datosUsuario.getRut());
        viewHolder.setPerfil(datosUsuario.getPerfil());
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{"Modificar", "Eliminar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Elija una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Click Event for each item.
                        if(i == 0){
                            Intent profileIntent = new Intent(mContext, ModifyUsersActivity.class);
                            profileIntent.putExtra("nombre", datosUsuario.getNombre());
                            profileIntent.putExtra("apellido", datosUsuario.getApellido());
                            profileIntent.putExtra("rut", datosUsuario.getRut());
                            profileIntent.putExtra("perfil", datosUsuario.getPerfil());
                            profileIntent.putExtra("token", datosUsuario.getToken());
                            mContext.startActivity(profileIntent);
                            ((Activity)mContext).finish();
                        }
                        if(i == 1){
                            eliminarWebService(datosUsuario.getToken());
                            mUsersDatabase.child(datosUsuario.getToken()).child("estaEliminado").setValue(true);
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

    private void eliminarWebService(String token) {
        String url = "http://miltonzambra.com/EliminarUsuario.php?token='" + token + "'";
        requestQueue = Volley.newRequestQueue(mContext);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView nombre;
        public TextView apellido;
        public TextView perfil;
        public TextView rut;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            nombre = (TextView) mView.findViewById(R.id.txtNombre);
            apellido = (TextView) mView.findViewById(R.id.txtApellido);
            rut = (TextView) mView.findViewById(R.id.txtRut);
            perfil = (TextView) mView.findViewById(R.id.txtPerfil);
        }

        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.txtNombre);
            userNameView.setText(name);
        }

        public void setApellido(String apellido){
            TextView userApellidoView = (TextView) mView.findViewById(R.id.txtApellido);
            userApellidoView.setText(apellido);
        }

        public void setRut(String rut){
            TextView userRutView = (TextView) mView.findViewById(R.id.txtRut);
            userRutView.setText(rut);
        }

        public void setPerfil(String perfil){
            TextView userPerfilView = (TextView) mView.findViewById(R.id.txtPerfil);
            userPerfilView.setText(validarPerfil(perfil));
        }
    }

    private String validarPerfil(String perfil){
        String convert = "";
        if (perfil.equals("1")){
            convert = "Administrador";
        }else if (perfil.equals("2")){
            convert = "Directivos";
        }else if (perfil.equals("3")){
            convert = "Docentes";
        }else if (perfil.equals("4")){
            convert = "Asistentes";
        }else if (perfil.equals("5")){
            convert = "Padres";
        }
        return convert;
    }
}
