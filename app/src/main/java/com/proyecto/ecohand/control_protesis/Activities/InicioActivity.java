package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.proyecto.ecohand.control_protesis.R;

public class InicioActivity extends AppCompatActivity {

    private boolean registrado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

//        SharedPreferences prefs=getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.commit();
//        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    public void Login(View v) {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentHome = new Intent(this, HomeActivity.class);

        try {
            SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
            if (prefs.contains("Registrado"))
                if (prefs.getBoolean("Registrado", false))
                    registrado = true;
                else
                    registrado = false;
        } catch (Exception e) {

        }
        ;
        finish();
        if (registrado == true)
            startActivity(intentHome);
        else
            startActivity(intentLogin);
    }

    public void Registrarse(View v) {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }
}
