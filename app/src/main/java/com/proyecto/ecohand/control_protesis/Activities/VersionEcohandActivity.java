package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.proyecto.ecohand.control_protesis.Models.Menu;
import com.proyecto.ecohand.control_protesis.R;

import customfonts.MyTextView_SF_Pro_Display_Semibold;

public class VersionEcohandActivity extends AppCompatActivity {

    private MyTextView_SF_Pro_Display_Semibold faceTextView;
    private ListView listMenu;
    private boolean toolbarVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_ecohand);

        listMenu = findViewById(R.id.listMenuID);
        faceTextView = findViewById(R.id.faceID);

        Menu.SetMenu(this.getBaseContext());
        Menu.setActivity(this);
        listMenu.setAdapter(Menu.getAdapter());

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        intent = new Intent(VersionEcohandActivity.this, BluetoothActivity.class);
                        startActivity(intent);
                        break;
                    case 0:
                        intent = new Intent(VersionEcohandActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(VersionEcohandActivity.this, VersionEcohandActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        openAlert();
                        break;
                }
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openURL(View view) {
        Uri uri = Uri.parse("https://www.instagram.com/ecohandok");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void Menu(View view) {
        if (!toolbarVisible) {
            listMenu.setVisibility(View.VISIBLE);
            toolbarVisible = true;
        } else {
            listMenu.setVisibility(View.INVISIBLE);
            toolbarVisible = false;
        }
    }

    public void openAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("¿Estás seguro que quieres cerrar sesión?");
        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(VersionEcohandActivity.this, InicioActivity.class);
                        SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("Registrado", false);
                        editor.commit();
                        finish();
                        Toast.makeText(VersionEcohandActivity.this, "Sesión cerrada!", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
