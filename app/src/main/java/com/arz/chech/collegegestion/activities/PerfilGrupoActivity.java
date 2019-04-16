package com.arz.chech.collegegestion.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.ContactosAgregadosAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PerfilGrupoActivity extends AppCompatActivity {
    private ImageView imageView;
    private FloatingActionButton floatingActionButton;
    private final int PICK_PHOTO=1;
    private int request_code = 2;
    private Uri filepath;
    private StorageReference storageReference;
    private String mCurrent_user_id;
    private DatabaseReference RootRef;
    private DatabaseReference RootRef1;

    private String currentGroupName;
    private String nombreGrupo;
    private ArrayList<DatosUsuario> datosUsuarios;
    private TextView textViewgrupo;
    public ProgressBar mProgressBar;
    private String imagenurl;
    private String userid;
    private RecyclerView agregadosList;
    private FloatingActionButton addpersona;
    private TextView textView;
    private LinearLayoutManager mLinearLayout;
    private DatabaseReference mUsersDatabase;
    public  Boolean bandera=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_grupo);
        storageReference= FirebaseStorage.getInstance().getReference();
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        imageView=findViewById(R.id.idperfilgroupimagen);
        currentGroupName = getIntent().getStringExtra("nombreGrupo");
        nombreGrupo=getIntent().getStringExtra("nombreG");
        datosUsuarios = getIntent().getParcelableArrayListExtra("datosUsuariosList");
        floatingActionButton =findViewById(R.id.idbuttonagregarimagen);
        textViewgrupo=findViewById(R.id.idnombregrupoperfil);
        mProgressBar=findViewById(R.id.idprogessbar);

        textView =(TextView) findViewById(R.id.textView3);
        textViewgrupo.setText(nombreGrupo);
        imagenurl=getIntent().getStringExtra("imagenurl");


        Glide
                .with(this)
                .load(imagenurl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.default_avatar);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })

                .into(imageView);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFotoGaleria();
                guardarFotoenfirebase(filepath);
            }
        });



        addpersona = (FloatingActionButton) findViewById(R.id.idAddIntegrante);
        mLinearLayout = new LinearLayoutManager(this);
        agregadosList = (RecyclerView) findViewById(R.id.act_contactosagregados_list);
        agregadosList.setHasFixedSize(true);
        agregadosList.setLayoutManager(mLinearLayout);
        final ContactosAgregadosAdapter agregadosAdapter = new ContactosAgregadosAdapter(PerfilGrupoActivity.this, datosUsuarios);
        agregadosList.setAdapter(agregadosAdapter);
        RootRef1 = FirebaseDatabase.getInstance().getReference();
        addpersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilGrupoActivity.this,AgregarIntegranteGrupoActivity.class);
                startActivityForResult(intent,request_code);
                //onResume();

            }
        });


        // datosUsuarios = new ArrayList<>();

        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        if (userid!=null){

            System.out.println(datosUsuarios.toString());
            for (DatosUsuario mi:datosUsuarios) {

                if (userid.equals(mi.getToken())) {

                    bandera = false;


                } else {

                    bandera = true;


                }
            }



            mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        final DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                        assert usuario!=null;
                        if (snapshot.child("estaEliminado").exists()){
                            if (userid.equals(usuario.getToken())){
                                if (!usuario.isEstaEliminado()){
                                    //if(mUsersDatabase.equals("d")) {
                                    //  datosUsuarios.add(usuario);
                                    //}
                                    if(bandera) {
                                        bandera=false;
                                        datosUsuarios.add(usuario);


                                    }else {

                                    }


                                }
                            }
                        }/*else{
                            if (userid.equals(usuario.getToken())){
                                datosUsuarios.add(usuario);
                                System.out.println("por dos");

                            }
                        }*/

                    }


                    agregadosAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });}








        String texto="Participantes: "+String.valueOf(datosUsuarios.size()+1);
        textView.setText(texto);


    }

    private void abrirFotoGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),PICK_PHOTO);


    }

    private void guardarFotoenfirebase(final Uri filepath){
        if(filepath!=null) {
            final StorageReference fotoref = storageReference.child("Fotos").child(currentGroupName).child(filepath.getLastPathSegment());
            fotoref.putFile(filepath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }

                    return fotoref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadlink=task.getResult();
                        //aca van los datos para cargar en la base de datos el link
                        RootRef = FirebaseDatabase.getInstance().getReference().child("Groups");
                        RootRef.child(currentGroupName).child("imagenurl").setValue(downloadlink.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                System.out.println("Se cargo correctamente");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("No se cargo correctamente");
                            }
                        });
                    }
                }
            });
        }


    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            // Si es así mostramos mensaje de cancelado por pantalla.
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT)
                    .show();
        } else {

            if(requestCode==1) {
                filepath = data.getData();

                try {
                    Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    //FileOutputStream fOut = new FileOutputStream(filepath.toString());
                    //bitmapImagen.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                    imageView.setImageBitmap(bitmapImagen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(requestCode==2){
                userid=data.getStringExtra("user_id");

            }

        }
    }
}
