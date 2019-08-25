package com.proyecto.ecohand.control_protesis.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.ecohand.control_protesis.Handlers.HandlerBT;
import com.proyecto.ecohand.control_protesis.Models.Menu;
import com.proyecto.ecohand.control_protesis.Models.Request.UsuarioRequest;
import com.proyecto.ecohand.control_protesis.Models.Response.SecuenciaResponse;
import com.proyecto.ecohand.control_protesis.Services.ApiService;
import com.proyecto.ecohand.control_protesis.Services.ConexionBT;
import com.proyecto.ecohand.control_protesis.Services.UsuarioService;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Models.Response.UsuarioResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.proyecto.ecohand.control_protesis.Adapters.SecuenciaAdapter;
import com.proyecto.ecohand.control_protesis.Models.Secuencia;

import customfonts.TextViewSFProDisplayRegular;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private ListView listaSecuenciasBtn;
    private ListView listMenu;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
    private android.support.v7.widget.Toolbar toolbar;
    private boolean toolbarVisible = false;
    private ImageView comandoVoz;
    private ProgressBar spinner;
    private TextViewSFProDisplayRegular cargaSecuencias;
    private String lastMessage;
    private boolean conectadoBT = false;

    private TextView comando, ReadBuffer, estadoBT;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    private BluetoothAdapter btAdapter = null;

    // EXTRA string to send on to HomeActivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";
    private static final String END = "\t\n";

    //private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread connectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket SocketBT = null; // bi-directional client-to-client data path

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listaSecuenciasBtn = findViewById(R.id.list);
        listMenu = findViewById(R.id.listMenuID);
        toolbar = findViewById(R.id.toolbarID);
        comandoVoz = findViewById(R.id.microsfonoID);
        comando = findViewById(R.id.txtComandoID);
        ReadBuffer = findViewById(R.id.readBufferID);
        estadoBT = findViewById(R.id.estadoBTID);
        cargaSecuencias = findViewById(R.id.SubtituloID);
        spinner = findViewById(R.id.progressBarID);
        spinner.getIndeterminateDrawable().setColorFilter(Color.rgb(128, 139, 150), PorterDuff.Mode.SRC_IN);
        spinner.setVisibility(View.VISIBLE);

        Menu.SetMenu(this.getBaseContext());
        Menu.setActivity(this);
        listMenu.setAdapter(Menu.getAdapter());

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        HandlerBT.handlerBT = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    ReadBuffer.setText(readMessage);
                    lastMessage = readMessage;
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1)
                        estadoBT.setText("Conectado al Dispositivo: " + (String) (msg.obj));
                    else
                        estadoBT.setText("Fallo la Conexión");
                }
            }
        };

        try {
            SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
            if (prefs.contains("ConexionBT"))
                if (prefs.getBoolean("ConexionBT", false))
                    conectadoBT = true;
                else
                    conectadoBT = false;
        } catch (Exception e) {

        }
        //finish();

//        if (conectadoBT == false)
        conectarBT(getIntent().getExtras().getString(EXTRA_DEVICE_ADDRESS), getIntent().getExtras().getString(EXTRA_DEVICE_NAME));

        SecuenciaAdapter adapter = new SecuenciaAdapter(this, secuencias);

        listaSecuenciasBtn.setAdapter(adapter);
        getSecuencias(adapter);

        listaSecuenciasBtn.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //            cambiar por los metodos de cada secuencia
                comando.setText(secuencias.get(position).getNombre());
                if(connectedThread != null) { //First check to make sure thread created
                    connectedThread.write("LOAD+" + secuencias.get(position).getCodigo() + END);

                }

            }
        });

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(HomeActivity.this, BluetoothActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(HomeActivity.this, VozActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(HomeActivity.this, VersionEcohandActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        openAlert();
                        break;
                }
            }
        });


    }

    private void conectarBT(final String address, final String name) {

        // Spawn a new thread to avoid blocking the GUI one
        new Thread() {
            public void run() {
                boolean fail = false;

                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                try {
                    SocketBT = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    SocketBT.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        SocketBT.close();
                        HandlerBT.handlerBT .obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        //insert code to deal with this
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
                if (fail == false) {
                    connectedThread = new ConnectedThread(SocketBT);
                    connectedThread.start();

                    HandlerBT.handlerBT .obtainMessage(CONNECTING_STATUS, 1, -1, name)
                            .sendToTarget();
                }
            }
        }.start();

        SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("ConexionBT", true);
        editor.commit();
        //finish();

    }

    public void onBackPressed() {
        Intent intentInicio = new Intent(this, InicioActivity.class);
        startActivity(intentInicio);
    }

    private void getSecuencias(final SecuenciaAdapter arrayAdapter) {
        SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
        String username = prefs.getString("UserName", "");
        UsuarioRequest usuarioRequest = new UsuarioRequest(username);
        Call<List<SecuenciaResponse>> call = ApiService.getSecuenciaService().getSecuencias(usuarioRequest);

        call.enqueue(new Callback<List<SecuenciaResponse>>() {
            @Override
            public void onResponse(Call<List<SecuenciaResponse>> call, Response<List<SecuenciaResponse>> response) {

                for (SecuenciaResponse s : response.body()) {
                    arrayAdapter.addSecuencia(new Secuencia(s.getNombre(), s.getCodigoEjecutable()));
//                    titles.add(s.getNombre());
                }

                arrayAdapter.notifyDataSetChanged();
                cargaSecuencias.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<SecuenciaResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
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
                        Intent intent = new Intent(HomeActivity.this, InicioActivity.class);
                        SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("Registrado", false);
                        editor.commit();
                        finish();
                        Toast.makeText(HomeActivity.this, "Sesión cerrada!", Toast.LENGTH_LONG).show();
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
                                // ejecutar metodo de la secuencia
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

    private void getUsuarios(final ArrayAdapter arrayAdapter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ecohand-backend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsuarioService usuarioServices = retrofit.create(UsuarioService.class);
        Call<List<UsuarioResponse>> call = usuarioServices.get();

        call.enqueue(new Callback<List<UsuarioResponse>>() {
            @Override
            public void onResponse(Call<List<UsuarioResponse>> call, Response<List<UsuarioResponse>> response) {

                for (UsuarioResponse u : response.body()) {
                    titles.add(u.getUsername());
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<UsuarioResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void left(View view) {
        Intent intent = new Intent(HomeActivity.this, SecuenciasComunesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void right(View view) {
        Intent intent = new Intent(HomeActivity.this, SecuenciaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    // Hilo para conectar el Bluetooth
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        HandlerBT.handlerBT .obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
