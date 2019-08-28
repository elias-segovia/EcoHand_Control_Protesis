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
import android.os.Handler;
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
import com.proyecto.ecohand.control_protesis.Services.BluetoothService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import customfonts.TextViewSFProDisplayRegular;

public class SecuenciasComunesActivity extends Activity {

    private ListView listaSecuenciasBtn;
    private ListView listMenu;
    private ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
    private android.support.v7.widget.Toolbar toolbar;
    private boolean toolbarVisible = false;
    private ImageView comandoVoz;

    private static final String END = "\t\n";

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

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

        listaSecuenciasBtn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //            cambiar por los metodos de cada secuencia
                comando.setText(secuencias.get(position).getNombre());

                if (BluetoothService.connectedThread  != null) { //First check to make sure thread created
                    BluetoothService.connectedThread .write("LOAD+" + secuencias.get(position).getCodigo() + END);

                }
            }
        });

        BluetoothService.hd = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //ReadBuffer.setText(readMessage);
                    //lastMessage = readMessage;
                }

                //if (msg.what == CONNECTING_STATUS) {
                    //if (msg.arg1 == 1)
                        //estadoBT.setText("Conectado al Dispositivo: " + (String) (msg.obj));
                    //else
                        //estadoBT.setText("Fallo la Conexión");
                //}
            }
        };

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
        arrayAdapter.addSecuencia(new Secuencia("Palma Abierta", "D100D200D300D400D500"));
        arrayAdapter.addSecuencia(new Secuencia("Palma Cerrada","D15AD25AD35AD45AD55A"));
        arrayAdapter.addSecuencia(new Secuencia("Sostener Objeto","D15AD25AD35AD45AD55A"));
        arrayAdapter.addSecuencia(new Secuencia("Pulsar Botón","D100D25AD35AD45AD55A"));
        arrayAdapter.addSecuencia(new Secuencia("Hacer Click","D100D25AD35AD45AD55A"));

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
                    String respuesta = speech.get(0);

                    if (respuesta.toUpperCase().indexOf("PARAR") <= -1) {

                        for (Secuencia s : secuencias) {
                            if (respuesta.toUpperCase().indexOf(s.getNombre().toUpperCase()) > -1) {
                                if (BluetoothService.connectedThread  != null) { //First check to make sure thread created
                                    BluetoothService.connectedThread .write("LOAD+" + s.getCodigo() + END);

                                }
                            }
                        }

                        ComandoVoz(getWindow().getDecorView().findViewById(android.R.id.content));
                    }

                    comando.setText(respuesta);
                }
                break;
            default:
                comando.setText("Esa Secuencia no existe!");
                break;
        }
    }

    public void right(View view) {
        Intent intent = new Intent(SecuenciasComunesActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
