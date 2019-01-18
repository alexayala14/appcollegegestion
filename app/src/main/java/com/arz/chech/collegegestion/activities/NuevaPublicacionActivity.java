package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.UserDetails;
import com.arz.chech.collegegestion.fragments.APIService;
import com.arz.chech.collegegestion.notifications.Client;
import com.arz.chech.collegegestion.notifications.Data;
import com.arz.chech.collegegestion.notifications.MyResponse;
import com.arz.chech.collegegestion.notifications.Sender;
import com.arz.chech.collegegestion.notifications.Token;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;

public class NuevaPublicacionActivity extends AppCompatActivity {

    private Button btnPublicar;
    private Button btnCancelar;
    private EditText textPublicacion;
    private EditText textAsunto;
    private RadioGroup radioGroup;
    private int selected;
    private APIService apiService;
    private DatabaseReference mRootRef, mUsersDatabase;
    private String userName;
    private String userApellido;
    private String mCurrentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nueva_publicacion);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        // PARA NOTIFICACION
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        //Toast.makeText(NuevaPublicacionActivity.this, UserDetails.token, Toast.LENGTH_SHORT).show();
        userName = Preferences.obtenerPreferenceString(NuevaPublicacionActivity.this, Preferences.PREFERENCE_NOMBRE);
        userApellido = Preferences.obtenerPreferenceString(NuevaPublicacionActivity.this, Preferences.PREFERENCE_APELLIDO);
        mCurrentUserId = Preferences.obtenerPreferenceString(NuevaPublicacionActivity.this, Preferences.PREFERENCE_TOKEN);
        textAsunto = (EditText) findViewById(R.id.editTextAsunto);
        textPublicacion = (EditText) findViewById(R.id.editTextPublicacion);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnPublicar = (Button) findViewById(R.id.btnPublicar);
        
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonAlta:
                        selected = 1;
                        System.out.println("Es 1");
                        ocultarteclado();
                        break;
                    case R.id.radioButtonMedia:
                        selected = 2;
                        System.out.println("Es 2");
                        ocultarteclado();
                        break;
                    case R.id.radioButtonBaja:
                        selected = 3;
                        System.out.println("Es 3");
                        ocultarteclado();
                        break;
                        }
            }
        });
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (textAsunto.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Debe ingresar Asunto", Toast.LENGTH_SHORT).show();
                }else if (textPublicacion.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "La publicacion no puede estar vacia", Toast.LENGTH_SHORT).show();
                }else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(view.getContext(), "Seleccion la prioridad por favor", Toast.LENGTH_SHORT).show();
                }else {
                    final String asunto = textAsunto.getText().toString();
                    final String descripcion = textPublicacion.getText().toString();
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radioButtonAlta:
                            selected = 1;
                            System.out.println("ES rojo");
                            //textAsunto.setTextColor(Color.parseColor("#B71C1C"));
                            //textAsunto.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case R.id.radioButtonMedia:
                            selected = 2;
                            System.out.println("ES amarillo");
                            break;
                        case R.id.radioButtonBaja:
                            selected = 3;
                            System.out.println("ES azul");
                            break;
                    }
                    Toast.makeText(view.getContext(), "Publicación agregada..", Toast.LENGTH_SHORT).show();
                    DatabaseReference user_message_push = mRootRef.child("Publicaciones").child(mCurrentUserId).push();
                    String push_id = user_message_push.getKey();
                    mRootRef.child("Publicaciones").child(push_id).child("nombre").setValue(userName + " " + userApellido);
                    mRootRef.child("Publicaciones").child(push_id).child("asunto").setValue(asunto.trim());
                    mRootRef.child("Publicaciones").child(push_id).child("descripcion").setValue(descripcion.trim());
                    mRootRef.child("Publicaciones").child(push_id).child("prioridad").setValue(selected);
                    mRootRef.child("Publicaciones").child(push_id).child("estaEliminado").setValue(false);
                    mRootRef.child("Publicaciones").child(push_id).child("timestamp").setValue(ServerValue.TIMESTAMP);
                    mRootRef.child("Publicaciones").child(push_id).child("tokenUser").setValue(mCurrentUserId);
                    mRootRef.child("Publicaciones").child(push_id).child("tokenPubli").setValue(push_id);

                    mUsersDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                DatosUsuario datosUsuario = snapshot.getValue(DatosUsuario.class);
                                if (!datosUsuario.getToken().equals(mCurrentUserId)){
                                    sendNotification(datosUsuario.getToken(), asunto);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    onBackPressed();
                    finish();
                }
            }
        });

    }

    private void ocultarteclado(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    private void sendNotification(final String receiver, final String asunto){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(mCurrentUserId, R.mipmap.ic_launcher, "Asunto: " + asunto, "Nueva Publicacion!", receiver);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                            if (response.code() == 200){
                                if (response.body().success != 1){
                                    Toast.makeText(NuevaPublicacionActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
