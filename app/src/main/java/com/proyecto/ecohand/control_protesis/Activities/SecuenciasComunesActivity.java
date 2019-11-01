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

import customfonts.MyTextView_SF_Pro_Display_Medium;
import customfonts.TextViewSFProDisplayRegular;

public class SecuenciasComunesActivity extends Activity {

    private ListView listaSecuenciasBtn;
    private ListView listMenu;
    private ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
    private android.support.v7.widget.Toolbar toolbar;
    private boolean toolbarVisible = false;
    private ImageView comandoVoz;
    private MyTextView_SF_Pro_Display_Medium estado;
    private TextView comando, ReadBuffer, estadoBT;
    private String lastMessage;

    private static final String END = "\t\n";

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

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
        estado = findViewById(R.id.EstadoID);

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
                //comando.setText(secuencias.get(position).getNombre());

                if (BluetoothService.connectedThread != null) { //First check to make sure thread created
                    BluetoothService.connectedThread.write("QLOAD+" + secuencias.get(position).getCodigo() + END);
                    estado.setText(secuencias.get(position).getNombre());
                }
            }
        });

        BluetoothService.hd = new Handler() {


            public void handleMessage(android.os.Message msg) {
                try {
                    if (msg.what == MESSAGE_READ) {
                        String readMessage = null;
                        try {
                            readMessage = new String((byte[]) msg.obj, "UTF-8");
                            estado.setText(readMessage);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //ReadBuffer.setText(readMessage);

                        lastMessage = readMessage;
                    }

                    if (msg.what == CONNECTING_STATUS) {
                        if (msg.arg1 == 1) {
                            estado.setText("Conectado al Dispositivo: " + (String) (msg.obj));
                        } else {
                            estado.setText("Fallo la Conexión");
                        }
                    }
                } catch (Exception e) {
                    estado.setText(e.getMessage());
                }
            }
        };

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        intent = new Intent(SecuenciasComunesActivity.this, BluetoothActivity.class);
                        startActivity(intent);
                        break;
                    case 0:
                        intent = new Intent(SecuenciasComunesActivity.this, HomeActivity.class);
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
        arrayAdapter.addSecuencia(new Secuencia("Palma Cerrada", "D1B4D2B4D3B4D4B4D5B4"));
        arrayAdapter.addSecuencia(new Secuencia("Señalar", "D1B4D200D3B4D4B4D5B4"));
        arrayAdapter.addSecuencia(new Secuencia("Pinzar", "D1B4D2B4D300D400D500"));
        // agregar SOSTERNER la secuencia del FSR
        arrayAdapter.addSecuencia(new Secuencia("Piedra Papel o Tijera", "PP00"));

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

                    if (!respuesta.isEmpty() && respuesta.toUpperCase().indexOf("FINALIZAR") <= -1) {

                        boolean flag = true;
                        for (int i = 0; i < secuencias.size() && flag == true; i++) {
                            //if (respuesta.toUpperCase().indexOf(s.getNombre().toUpperCase()) > -1) {

                            flag = false;
                            if (BluetoothService.connectedThread != null) { //First check to make sure thread created

                                if (respuesta.toUpperCase().compareTo("PARAR") == 0)
                                    BluetoothService.connectedThread.write("STOP");
                                else if (respuesta.toUpperCase().compareTo("CONTINUAR") == 0)
                                    BluetoothService.connectedThread.write("CONT");
                                else if (respuesta.toUpperCase().compareTo(secuencias.get(i).getNombre().toUpperCase()) == 0)
                                    BluetoothService.connectedThread.write("QLOAD+" + secuencias.get(i).getCodigo() + END);
                                else
                                    flag = true;

                            }

                        }
                        estado.setText(respuesta);

                        ComandoVoz(getWindow().getDecorView().findViewById(android.R.id.content));
                    }

                }
                break;
            default:
                estado.setText("Esa Secuencia no existe!");
                break;
        }
    }

    public void right(View view) {
        Intent intent = new Intent(SecuenciasComunesActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void PreguntarEstado(View view) {

        if (BluetoothService.connectedThread != null) { //First check to make sure thread created
            BluetoothService.connectedThread.write("STATUS");

        }
    }
}
