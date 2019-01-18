package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class ModifyPublicacionActivity extends AppCompatActivity {

    private Button btnActualizar;
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
        setContentView(R.layout.activity_modificar_publicacion);
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
        userName = Preferences.obtenerPreferenceString(ModifyPublicacionActivity.this, Preferences.PREFERENCE_NOMBRE);
        userApellido = Preferences.obtenerPreferenceString(ModifyPublicacionActivity.this, Preferences.PREFERENCE_APELLIDO);
        mCurrentUserId = Preferences.obtenerPreferenceString(ModifyPublicacionActivity.this, Preferences.PREFERENCE_TOKEN);
        textAsunto = (EditText) findViewById(R.id.editTextAsunto);
        textPublicacion = (EditText) findViewById(R.id.editTextPublicacion);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        Intent intent = getIntent();
        final String tokenPublicacion = intent.getStringExtra("tokenPubli");
        String asunto = intent.getStringExtra("asunto");
        String descripcion = intent.getStringExtra("descripcion");
        int prioridad = intent.getIntExtra("prioridad", 0) - 1;
        textAsunto.setText(asunto);
        textPublicacion.setText(descripcion);
        ((RadioButton)radioGroup.getChildAt(prioridad)).setChecked(true);
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
        btnActualizar.setOnClickListener(new View.OnClickListener() {
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
                            break;
                        case R.id.radioButtonMedia:
                            selected = 2;
                            break;
                        case R.id.radioButtonBaja:
                            selected = 3;
                            break;
                    }
                    Toast.makeText(view.getContext(), "Publicación modificada..", Toast.LENGTH_SHORT).show();
                    mRootRef.child("Publicaciones").child(tokenPublicacion).child("asunto").setValue(asunto.trim());
                    mRootRef.child("Publicaciones").child(tokenPublicacion).child("descripcion").setValue(descripcion.trim());
                    mRootRef.child("Publicaciones").child(tokenPublicacion).child("prioridad").setValue(selected);
                    mRootRef.child("Publicaciones").child(tokenPublicacion).child("timestamp").setValue(ServerValue.TIMESTAMP);
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

}
