package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.proyecto.ecohand.control_protesis.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Acceder(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}