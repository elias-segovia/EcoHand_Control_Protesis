package com.proyecto.ecohand.control_protesis.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.ecohand.control_protesis.Models.Menu;
import com.proyecto.ecohand.control_protesis.Models.Secuencia;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Services.BluetoothService;

import java.util.Set;

import customfonts.MyTextView_SF_Pro_Display_Medium;
import customfonts.MyTextView_SF_Pro_Display_Semibold;

public class BluetoothActivity extends AppCompatActivity {

    private ListView listMenu, dispositivos;
    private android.support.v7.widget.Toolbar toolbar;
    private boolean toolbarVisible = false;
    private MyTextView_SF_Pro_Display_Semibold titulo,infoText;
    private ListView listView;
    private MyTextView_SF_Pro_Display_Medium desconectarBT;

    // Debugging for LOGCAT
    private static final String TAG = "BluetoothActivity";

    private TextView textView1;

    // EXTRA string to send on to HomeActivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter mPairedDevicesArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        titulo = findViewById(R.id.tituloID);
        listMenu = findViewById(R.id.listMenuID);
        toolbar = findViewById(R.id.toolbarID);
        listView = findViewById(R.id.list);
        infoText = findViewById(R.id.infoText);
        desconectarBT = findViewById(R.id.DesconectarID);
        dispositivos = findViewById(R.id.navList);


        Menu.SetMenu(this.getBaseContext());
        Menu.setActivity(this);
        listMenu.setAdapter(Menu.getAdapter());

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        intent = new Intent(BluetoothActivity.this, BluetoothActivity.class);
                        startActivity(intent);
                        break;
                    case 0:
                        intent = new Intent(BluetoothActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(BluetoothActivity.this, VersionEcohandActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        openAlert();
                        break;
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        checkBTState();

        if (BluetoothService.connectedThread == null) {

            titulo.setText("Seleccione el Bluetooth de la Prótesis");
            listView.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.VISIBLE);

            textView1 = findViewById(R.id.connecting);
            textView1.setTextSize(40);
            textView1.setText(" ");

            // Initialize array adapter for paired devices
            mPairedDevicesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1);

            // Find and set up the ListView for paired devices
            ListView pairedListView = findViewById(R.id.list);
            pairedListView.setAdapter(mPairedDevicesArrayAdapter);
            pairedListView.setOnItemClickListener(mDeviceClickListener);

            // Get the local Bluetooth adapter
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();

            // Get a set of currently paired devices and append to 'pairedDevices'
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

            // Add previosuly paired devices to the array
            if (pairedDevices.size() > 0) {

                for (BluetoothDevice device : pairedDevices) {
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else {
                String noDevices = "Ningun dispositivo pudo ser emparejado";
                //mPairedDevicesArrayAdapter.add(noDevices);
            }
        }
        else{
            desconectarBT.setVisibility(View.VISIBLE);
            titulo.setText("El Dispositivo está conectado con la Prótesis!");
        }
    }

    public void Desconectar(View view){

        if(BluetoothService.connectedThread != null){
            BluetoothService.connectedThread.cancel();
            Intent i = new Intent(BluetoothActivity.this, BluetoothService.class);
            stopService(i);
            startActivity(getIntent());
        }

    }

    public void DesconectarBT(){

        if(BluetoothService.connectedThread != null){
            BluetoothService.connectedThread.cancel();
            BluetoothService.connectedThread = null;
            Intent i = new Intent(BluetoothActivity.this, BluetoothService.class);
            stopService(i);
            startActivity(getIntent());
        }

    }

    // Set up on-click listener for the list (nicked this - unsure)
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

            //textView1.setText("Conectando...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            // Make an intent to start next activity while taking an extra which is the MAC address.
            Intent i = new Intent(BluetoothActivity.this, BluetoothService.class);
            i.putExtra(EXTRA_DEVICE_ADDRESS, address);
            i.putExtra(EXTRA_DEVICE_NAME, name);

            try {
                startService(i);
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

        }
    };

    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter = BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if (mBtAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth Activado...");
            } else {
                //Prompt user to turn on Bluetooth
                DesconectarBT();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
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
                        Intent intent = new Intent(BluetoothActivity.this, InicioActivity.class);
                        SharedPreferences prefs = getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("Registrado", false);
                        editor.commit();
                        finish();
                        Toast.makeText(BluetoothActivity.this, "Sesión cerrada!", Toast.LENGTH_LONG).show();
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
