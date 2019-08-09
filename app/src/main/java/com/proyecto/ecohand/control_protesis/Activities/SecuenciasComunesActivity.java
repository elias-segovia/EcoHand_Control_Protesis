package com.proyecto.ecohand.control_protesis.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.ecohand.control_protesis.Adapters.SecuenciaAdapter;
import com.proyecto.ecohand.control_protesis.Models.Menu;
import com.proyecto.ecohand.control_protesis.Models.Secuencia;
import com.proyecto.ecohand.control_protesis.R;

import java.util.ArrayList;

import customfonts.TextViewSFProDisplayRegular;

public class SecuenciasComunesActivity extends Activity {

    private ListView listaSecuenciasBtn;
    private ListView listMenu;
    private ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
    private android.support.v7.widget.Toolbar toolbar;
    private boolean toolbarVisible = false;
    private ImageView comandoVoz;

    private TextView comando;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secuencias_comunes);

        listaSecuenciasBtn = findViewById(R.id.list);
        listMenu = findViewById(R.id.listMenuID);
        toolbar = findViewById(R.id.toolbarID);
        comandoVoz = findViewById(R.id.microsfonoID);
        comando = findViewById(R.id.txtComandoID);

        Menu.SetMenu(this.getBaseContext());
        Menu.setActivity(this);
        listMenu.setAdapter(Menu.getAdapter());


        SecuenciaAdapter adapter = new SecuenciaAdapter(this, secuencias);
        listaSecuenciasBtn.setAdapter(adapter);
        cargarSecuencias(adapter);

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(SecuenciasComunesActivity.this, BluetoothActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(SecuenciasComunesActivity.this, VozActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(SecuenciasComunesActivity.this, VersionEcohandActivity.class);
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
        Intent intentInicio = new Intent(this, HomeActivity.class);
        startActivity(intentInicio);
    }

    public void cargarSecuencias(final SecuenciaAdapter arrayAdapter) {
        arrayAdapter.addSecuencia(new Secuencia("Palma Abierta"));
        arrayAdapter.addSecuencia(new Secuencia("Palma Cerrada"));
        arrayAdapter.addSecuencia(new Secuencia("Sostener Objeto"));
        arrayAdapter.addSecuencia(new Secuencia("Pulsar Botón"));
        arrayAdapter.addSecuencia(new Secuencia("Hacer Click"));

        arrayAdapter.notifyDataSetChanged();
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
                        Intent intent = new Intent(SecuenciasComunesActivity.this, InicioActivity.class);
                        SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("Registrado", false);
                        editor.commit();
                        finish();
                        Toast.makeText(SecuenciasComunesActivity.this, "Sesión cerrada!", Toast.LENGTH_LONG).show();
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

    public void ComandoVoz(View view) {

        Intent intentActionRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    comando.setText(strSpeech2Text);
                }
                break;
            default:
                break;
        }
    }

    public void right(View view){
        Intent intent = new Intent(SecuenciasComunesActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
