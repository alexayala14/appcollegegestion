package com.arz.chech.collegegestion.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arz.chech.collegegestion.R;

public class NuevoGrupoActivity extends AppCompatActivity {
    private FloatingActionButton addpersona;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo);
        addpersona = (FloatingActionButton) findViewById(R.id.idAddIntegrante);
        btnCancel =(Button)findViewById(R.id.btncancelarr);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoGrupoActivity.this, FriendsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
