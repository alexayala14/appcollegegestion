package com.arz.chech.collegegestion.activities;

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
import android.support.v7.app.AlertDialog;
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
import com.arz.chech.collegegestion.adapters.AgregarIntegranteGrupoAdapter;
import com.arz.chech.collegegestion.adapters.ContactosAgregadosAdapter;
import com.arz.chech.collegegestion.adapters.ContactosAgregadosPerfilGrupoAdapter;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Grupo;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class PerfilGrupoActivity extends AppCompatActivity {
    private ImageView imageView;
    private FloatingActionButton floatingActionButton;
    private final int PICK_PHOTO=1;
    private int request_code = 1;
    private Uri filepath;
    private StorageReference storageReference;
    private String mCurrent_user_id;
    private DatabaseReference RootRef;
    private DatabaseReference RootRef1;

    private String currentGroupName;
    private String nombreGrupo;
    private ArrayList<DatosUsuario> datosUsuarios;
    private ArrayList<DatosUsuario> datosUsuarios1;
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
        System.out.println("nombre del grupo token perfil "+currentGroupName);
        nombreGrupo=getIntent().getStringExtra("nombreG");
        datosUsuarios = getIntent().getParcelableArrayListExtra("datosUsuariosList");
        System.out.println("USUARIOS:"+datosUsuarios.toString());
        floatingActionButton =findViewById(R.id.idbuttonagregarimagen);
        textViewgrupo=findViewById(R.id.idnombregrupoperfil);


        textView =(TextView) findViewById(R.id.textView3);
        textViewgrupo.setText(nombreGrupo);
        imagenurl=getIntent().getStringExtra("imagenurl");



        cargarFotoGlide(imagenurl);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options={/*"Tomar Foto",*/"Elegir de Galeria","Cancelar"};
                final AlertDialog.Builder builder=new AlertDialog.Builder(PerfilGrupoActivity.this);
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
            }
        });



        addpersona = (FloatingActionButton) findViewById(R.id.idAddIntegrante);
        mLinearLayout = new LinearLayoutManager(this);
        agregadosList = (RecyclerView) findViewById(R.id.act_contactosagregados_list);
        agregadosList.setHasFixedSize(true);
        agregadosList.setLayoutManager(mLinearLayout);
        //datosUsuarios1=new ArrayList<DatosUsuario>();

        datosUsuarios1=datosUsuarios;
        //RootRef1 = FirebaseDatabase.getInstance().getReference();
        addpersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilGrupoActivity.this,AgregarIntegranteGrupoActivity.class);
                intent.putExtra("datosUsuariosList", datosUsuarios);
                intent.putExtra("nombreGrupo",currentGroupName);
                intent.putExtra("nombreG", nombreGrupo);
                intent.putExtra("imagenurl",imagenurl);
                //startActivityForResult(intent,request_code);
                startActivity(intent);
                //onResume();

            }
        });



        // datosUsuarios = new ArrayList<>();

        mCurrent_user_id = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_TOKEN);
        /*if (userid!=null){

            System.out.println(datosUsuarios.toString());
            for (DatosUsuario mi:datosUsuarios) {

                if (userid.equals(mi.getToken())) {

                    bandera = false;


                } else {

                    bandera = true;


                }
            }*/


            mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("members");
            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //final Grupo grupo = dataSnapshot.getValue(Grupo.class);
                    //datosUsuarios.clear();

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        final DatosUsuario usuario = snapshot.getValue(DatosUsuario.class);
                        assert usuario!=null;
                        try {
                            for(DatosUsuario user:datosUsuarios1){
                                if (snapshot.child("estaEliminado").exists()){
                                    if (user.getToken().equals(usuario.getToken())){
                                        if (!usuario.isEstaEliminado()){
                                            //if(mUsersDatabase.equals("d")) {
                                            //  datosUsuarios.add(usuario);
                                            //}
                                            datosUsuarios.add(usuario);

                                            System.out.println("ESTAS ADENTROOOOOOO"+usuario.getNombre());

                                        /*if(bandera) {
                                            bandera=false;
                                            datosUsuarios.add(usuario);
                                            assert grupo!=null;
                                            //agregadosAdapter.enviarGrupo(currentGroupName,datosUsuarios1,nombreGrupo,imagenurl);
                                            System.out.println("ESTOY ADENTROOOOOOOOOOOO");



                                        }else {

                                        }*/


                                        }
                                    }
                                }

                            }
                        }catch (Exception e){

                        }
                        try {
                            for (int i =0;i<datosUsuarios.size();i++){
                                int cont=0;
                                for (int j =0;j<datosUsuarios.size()-1;j++){
                                    if ((datosUsuarios.get(i).getToken()).equals(datosUsuarios.get(j).getToken())){
                                        cont++;

                                    }
                                    if(cont==2){
                                        cont--;
                                        datosUsuarios.remove(i);
                                    }
                                }
                            }

                        }catch (Exception e){

                        }


                    }
                    final ContactosAgregadosPerfilGrupoAdapter agregadosAdapter = new ContactosAgregadosPerfilGrupoAdapter(PerfilGrupoActivity.this, datosUsuarios1);
                    agregadosList.setAdapter(agregadosAdapter);
                    agregadosAdapter.enviarGrupo(currentGroupName,datosUsuarios1,nombreGrupo,imagenurl);
                    agregadosAdapter.notifyDataSetChanged();

                    agregadosList.scrollToPosition(agregadosAdapter.getItemCount() - 1);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });














        //String texto="Participantes: "+String.valueOf(datosUsuarios.size()+1);
        String texto="Participantes: "+String.valueOf(datosUsuarios.size());
        textView.setText(texto);


    }

    private void opencamara() {
        /*File file=new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        file.mkdir();

        String path=Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(newFile));
        startActivityForResult(intent,PHOTO_CODE);*/
    }

    private void decodeBitmap(String dir){
       /* Bitmap bitmap;
        bitmap= BitmapFactory.decodeFile(dir);
        imageViewPerfil.setImageBitmap(bitmap);*/
    }


    private void abrirFotoGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),PICK_PHOTO);


    }

    private void cargarFotoGlide(String imagenurl) {
        Glide
                .with(this)
                .load(imagenurl)
                .error(R.drawable.default_avatar)
                .fitCenter()
                .into(imageView);
    }

    private void guardarFotoenfirebase(final Uri filepath){
        if(filepath!=null) {
            final StorageReference fotoref = storageReference.child("FotosGrupo").child(currentGroupName).child(filepath.getLastPathSegment());
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
                                Toast.makeText(PerfilGrupoActivity.this, "Se cargo la imagen correctamente", Toast.LENGTH_SHORT).show();
                                System.out.println("Se cargo correctamente");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("No se cargo correctamente");
                                Toast.makeText(PerfilGrupoActivity.this, "No se cargo la imagen correctamente", Toast.LENGTH_SHORT).show();

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

            if(requestCode==PICK_PHOTO&& resultCode==RESULT_OK&&data!=null&&data.getData()!=null) {
                filepath = data.getData();

                try {
                    Glide
                            .with(this)
                            .load(filepath)
                            .error(R.drawable.default_avatar)
                            .fitCenter()
                            .into(imageView);
                    guardarFotoenfirebase(filepath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*try {
                    Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    //FileOutputStream fOut = new FileOutputStream(filepath.toString());
                    //bitmapImagen.compress(Bitmap.CompressFormat.JPEG, 80, fOut);

                    imageView.setImageBitmap(bitmapImagen);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
            else {
                userid=data.getStringExtra("user_id");

            }

        }
    }
}
