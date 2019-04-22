package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilUsuarioActivity extends AppCompatActivity {
    private Toolbar mChatToolbar;
    private CircleImageView imageViewPerfil;
    private TextView textViewNombre;
    private TextView textViewApellido;
    private FloatingActionButton floatingActionButtonImagenperfil;
    private FloatingActionButton floatingActionButton;
    private final int PICK_PHOTO1=3;
    private int request_code = 3;
    private Uri filepath;
    private StorageReference storageReference;
    private String mCurrent_user_id;
    private DatabaseReference RootRef;
    public ProgressBar mProgressBar;
    private String imagenurl;
    private DatabaseReference mUsersDatabase;
    private String nombre;
    private String apellido;
    private String APP_DIRECTORY="myPictureApp/";
    private String MEDIA_DIRECTORY=APP_DIRECTORY+"media";
    private String TEMPORAL_PICTURE_NAME="temporal.jpg";

    private final int PHOTO_CODE=100;
    private final int SELECT_PICTURE=200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // TOOLBAR
        mChatToolbar = (Toolbar) findViewById(R.id.main_app_bar_perfil);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.perfil_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        //

        imageViewPerfil=findViewById(R.id.idperfiluserimagen);
        textViewNombre=findViewById(R.id.idnombreuserperfil);
        textViewApellido=findViewById(R.id.idapellidouserperfil);
        //mProgressBar=findViewById(R.id.idprogessbar12);
        floatingActionButtonImagenperfil=findViewById(R.id.idbuttonagregarimagen1);
        mChatToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                Intent intent=new Intent(PerfilUsuarioActivity.this,MenuPrincipalActivity.class);
                startActivity(intent);
            }
        });

        storageReference= FirebaseStorage.getInstance().getReference();
        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        System.out.println(mCurrent_user_id);
        nombre="Nombre: "+Preferences.obtenerPreferenceString(this,Preferences.PREFERENCE_NOMBRE);
        System.out.println(nombre);

        apellido="Apellido: "+Preferences.obtenerPreferenceString(this,Preferences.PREFERENCE_APELLIDO);
        System.out.println(apellido);

        Toast.makeText(this, "el apellido es: "+apellido, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "el nombre es: "+nombre, Toast.LENGTH_SHORT).show();



        textViewNombre.setText(nombre);
        textViewApellido.setText(apellido);
        //imageViewPerfil=getIntent().getStringExtra("imagenurl");




        floatingActionButtonImagenperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options={/*"Tomar Foto",*/"Elegir de Galeria","Cancelar"};
                final AlertDialog.Builder builder=new AlertDialog.Builder(PerfilUsuarioActivity.this);
                builder.setTitle("Elegir una Opcion: ");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if(options[seleccion]=="Tomar Foto"){
                            //opencamara();
                            //guardarFotoenfirebase(filepath);
                        }else if(options[seleccion]=="Elegir de Galeria"){
                            abrirFotoGaleria();
                            //guardarFotoenfirebase(filepath);
                        }else  if(options[seleccion]=="Cancelar"){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


                //abrirFotoGaleria();
                //guardarFotoenfirebase(filepath);
               // cargarFotoGlide();


            }
        });

        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    final DatosUsuario datosUsuario=snapshot.getValue(DatosUsuario.class);
                    if(datosUsuario.getToken().equals(mCurrent_user_id)) {
                        imagenurl =  datosUsuario.getImagenurl();
                        System.out.println("LA IMAGEN ES: "+datosUsuario.getImagenurl());
                        cargarFotoGlide(imagenurl);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //cargarFotoGlide();


    }

    private void opencamara() {
        File file=new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        file.mkdir();

        String path=Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(newFile));
        startActivityForResult(intent,PHOTO_CODE);
    }

    private void decodeBitmap(String dir){
        Bitmap bitmap;
        bitmap= BitmapFactory.decodeFile(dir);
        imageViewPerfil.setImageBitmap(bitmap);
    }

    private void cargarFotoGlide(String imagenurl) {
        try {
            Glide
                    .with(this)
                    .load(imagenurl)
                    .error(R.drawable.default_avatar)

                    .fitCenter()
                    .into(imageViewPerfil);
        }catch (Exception e){}

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(PerfilUsuarioActivity.this,MenuPrincipalActivity.class);
        startActivity(intent);
    }

    private void abrirFotoGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),PICK_PHOTO1);


    }

    private void guardarFotoenfirebase(final Uri filepath){
        if(filepath!=null) {
            final StorageReference fotoref = storageReference.child("FotosPerfil").child(mCurrent_user_id).child(filepath.getLastPathSegment());
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
                        final Uri downloadlink=task.getResult();
                        //aca van los datos para cargar en la base de datos el link
                        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        RootRef.child(mCurrent_user_id).child("imagenurl").setValue(downloadlink.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PerfilUsuarioActivity.this, "Se cargo la imagen correctamente", Toast.LENGTH_SHORT).show();
                                Preferences.savePreferenceString(PerfilUsuarioActivity.this, downloadlink.toString(), Preferences.PREFERENCE_IMAGENURL);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("No se cargo correctamente");
                                Toast.makeText(PerfilUsuarioActivity.this, "No se cargo la imagen correctamente", Toast.LENGTH_SHORT).show();

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

            if(requestCode==PICK_PHOTO1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
                filepath = data.getData();

                /*try {
                    Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    //FileOutputStream fOut = new FileOutputStream(filepath.toString());
                    //bitmapImagen.compress(Bitmap.CompressFormat.JPEG, 80, fOut);

                    imageViewPerfil.setImageBitmap(bitmapImagen);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                try {
                    Glide
                        .with(this)
                        .load(filepath)
                        .error(R.drawable.default_avatar)
                        .fitCenter()
                        .into(imageViewPerfil);
                    guardarFotoenfirebase(filepath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(requestCode==PHOTO_CODE){
                String dir=Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;
                decodeBitmap(dir);
                //guardarFotoenfirebase(filepath);
            }


        }
    }
}
