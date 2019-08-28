package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Services.BluetoothService;

public class InicioActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private static final String TAG = "InicioActivity";

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
        // salir de la app
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("BluetoothService", false);
        editor.commit();
        finish();
        System.exit(0);
    }

    public void Inicio(View v) {

        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentBluetooth = new Intent(this, BluetoothActivity.class);
        Intent intentHome = new Intent(this, HomeActivity.class);

        try {
            prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);

            if (!prefs.getBoolean("Registrado", false))
                startActivity(intentLogin);
            else if (BluetoothService.connectedThread == null)
                startActivity(intentBluetooth);
            else
                startActivity(intentHome);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
