package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.proyecto.ecohand.control_protesis.R;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    public void Crear_Cuenta(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
