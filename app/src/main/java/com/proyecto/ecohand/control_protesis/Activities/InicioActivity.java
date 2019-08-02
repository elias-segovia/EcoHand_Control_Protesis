package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.proyecto.ecohand.control_protesis.R;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("EcoHand - Control de Prótesis");
        setContentView(R.layout.activity_inicio);

        //SharedPreferences prefs=getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = prefs.edit();
        //editor.clear();
        //editor.commit();
        //finish();
    }

    public void Login(View v) {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentHome = new Intent(this, HomeActivity.class);

        try{
            SharedPreferences prefs=getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
            if (prefs.getBoolean("Registrado", false))
                startActivity(intentHome);
            else
                startActivity(intentLogin);
        }catch (Exception e){

        };

        startActivity(intentLogin);
    }

    public void Registrarse(View v) {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }
}
