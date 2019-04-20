package com.arz.chech.collegegestion.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.activities.ModifyPublicacionActivity;
import com.arz.chech.collegegestion.activities.ModifyUsersActivity;
import com.arz.chech.collegegestion.activities.NuevaPublicacionActivity;
import com.arz.chech.collegegestion.entidades.Publicacion;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.fragments.DetalleFragment;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdaptadorPublicaciones extends RecyclerView.Adapter<AdaptadorPublicaciones.ViewHolder>{

    ArrayList<Publicacion> listaPublicaciones;
    private Context mContext;
    FragmentManager fm;
    private DatabaseReference mPublicacionDatabase;
    private String imagenurl="";
    private String mCurrentUserId;
    private static String dateFormat = "hh:mm a";
    private static String añoFormat = "dd-MM-yy";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private static SimpleDateFormat simpleAñoFormat = new SimpleDateFormat(añoFormat);

    public AdaptadorPublicaciones(Context context, ArrayList<Publicacion>listaPublicaciones, FragmentManager fm){
        this.mContext = context;
        this.listaPublicaciones=listaPublicaciones;
        this.fm = fm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_publicaciones, viewGroup,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    public static String formatearHora(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatearAño(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleAñoFormat.format(calendar.getTime());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Publicacion publicacion = listaPublicaciones.get(position);
        if(publicacion.getPrioridad()==1){
            holder.mView.setBackgroundColor(Color.parseColor("#E57373"));
            holder.txt_descripcion.setText(publicacion.getAsunto());
            //holder.txt_descripcion.setTextColor(Color.parseColor("#B71C1C"));
            holder.txt_descripcion.setTextColor(Color.parseColor("#000000"));
            holder.txt_nombre.setText(publicacion.getNombre());
            holder.txt_nombre.setTextColor(Color.parseColor("#000000"));
            //holder.txt_nombre.setTextColor(Color.parseColor("#B71C1C"));
            long time = publicacion.getTimestamp();
            final String hora = formatearHora(time);
            final String año = formatearAño(time);
            holder.show_hora.setText(hora);
            holder.show_año.setText(año);

        }
        if(publicacion.getPrioridad()==2){
            holder.mView.setBackgroundColor(Color.parseColor("#FFF590"));
            holder.txt_descripcion.setText(publicacion.getAsunto());
            //holder.txt_descripcion.setTextColor(Color.parseColor("#FDD835"));
            holder.txt_descripcion.setTextColor(Color.parseColor("#000000"));
            holder.txt_nombre.setText(publicacion.getNombre());
            holder.txt_nombre.setTextColor(Color.parseColor("#000000"));
            //holder.txt_nombre.setTextColor(Color.parseColor("#FDD835"));
            long time = publicacion.getTimestamp();
            final String hora = formatearHora(time);
            final String año = formatearAño(time);
            holder.show_hora.setText(hora);
            holder.show_año.setText(año);
        }
        if(publicacion.getPrioridad()==3){
            holder.mView.setBackgroundColor(Color.parseColor("#9FA8DA"));
            holder.txt_descripcion.setText(publicacion.getAsunto());
            //holder.txt_descripcion.setTextColor(Color.parseColor("#1A237E"));
            holder.txt_descripcion.setTextColor(Color.parseColor("#000000"));
            holder.txt_nombre.setText(publicacion.getNombre());
            holder.txt_nombre.setTextColor(Color.parseColor("#000000"));
            //holder.txt_nombre.setTextColor(Color.parseColor("#1A237E"));
            long time = publicacion.getTimestamp();
            final String hora = formatearHora(time);
            final String año = formatearAño(time);
            holder.show_hora.setText(hora);
            holder.show_año.setText(año);
        }

        //holder.txt_nombre.setText(publicacion.getNombre());
        //holder.txt_descripcion.setText(publicacion.getAsunto());
        //holder.foto.setImageResource(R.drawable.hombre);

        System.out.println("La url en viewholdes es: "+imagenurl);
        cargarImagenesGlide(holder);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetalleFragment dialogFragment = new DetalleFragment ();
                Bundle b = new Bundle();
                b.putString("descripcion", listaPublicaciones.get(position).getAsunto()+"\n");
                b.putString ("detalle", listaPublicaciones.get(position).getDescripcion());
                dialogFragment.setArguments(b);
                dialogFragment.show(fm, "Sample Fragment");
            }
        });

        mCurrentUserId = Preferences.obtenerPreferenceString(mContext, Preferences.PREFERENCE_TOKEN);
        final int id_perfil = Preferences.obtenerPreferenceInt(mContext, Preferences.PREFERENCE_ESTADO_ID_PERFIL);
        mPublicacionDatabase = FirebaseDatabase.getInstance().getReference().child("Publicaciones");

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (id_perfil == 1){
                    mostrarOpciones(publicacion);
                }else {
                    if (publicacion.getTokenUser().equals(mCurrentUserId)){
                        mostrarOpciones(publicacion);
                    }
                }
                return true;
            }
        });
    }

    private void cargarImagenesGlide(ViewHolder holder) {
        Glide
                .with(mContext)
                .load(imagenurl)
                .fitCenter()
                .error(R.drawable.default_avatar)
                .into(holder.foto);
    }

    private void mostrarOpciones(final Publicacion publicacion){
        CharSequence options[] = new CharSequence[]{"Modificar", "Eliminar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Elija una opción");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Click Event for each item.
                if(i == 0){
                    Intent profileIntent = new Intent(mContext, ModifyPublicacionActivity.class);
                    profileIntent.putExtra("tokenPubli", publicacion.getTokenPubli());
                    profileIntent.putExtra("asunto", publicacion.getAsunto());
                    profileIntent.putExtra("descripcion", publicacion.getDescripcion());
                    profileIntent.putExtra("prioridad", publicacion.getPrioridad());
                    mContext.startActivity(profileIntent);
                }
                if(i == 1){
                    mPublicacionDatabase.child(publicacion.getTokenPubli()).child("estaEliminado").setValue(true);
                }
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView show_hora;
        public TextView show_año;
        TextView txt_nombre,txt_descripcion;
        ImageView foto;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            txt_nombre = itemView.findViewById(R.id.idNombre);
            txt_descripcion = itemView.findViewById(R.id.idInfo);
            foto = itemView.findViewById(R.id.idImagen);
            show_hora = itemView.findViewById(R.id.time_text_layout);
            show_año = itemView.findViewById(R.id.fecha_text_layout);
        }
    }
    public void enviarImagenurl(String imagnurl){
        imagenurl=imagnurl;
        System.out.println("La url en adapter es: "+imagenurl);



    }
}
