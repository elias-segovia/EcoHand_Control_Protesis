package com.proyecto.ecohand.control_protesis.Activities;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.ecohand.control_protesis.Models.Menu;
import com.proyecto.ecohand.control_protesis.Models.Response.SecuenciaResponse;
import com.proyecto.ecohand.control_protesis.Services.ApiService;
import com.proyecto.ecohand.control_protesis.Services.UsuarioService;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Models.Response.UsuarioResponse;

import java.util.ArrayList;
import java.util.List;

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

    private TextView comando;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listaSecuenciasBtn = findViewById(R.id.list);
        listMenu = findViewById(R.id.listMenuID);
        toolbar = findViewById(R.id.toolbarID);
        comandoVoz = findViewById(R.id.microsfonoID);
        comando = findViewById(R.id.txtComandoID);
        cargaSecuencias = findViewById(R.id.SubtituloID);
        spinner = findViewById(R.id.progressBarID);
        spinner.getIndeterminateDrawable().setColorFilter(Color.rgb(128, 139, 150), PorterDuff.Mode.SRC_IN);
        spinner.setVisibility(View.VISIBLE);

        Menu.SetMenu(this.getBaseContext());
        Menu.setActivity(this);
        listMenu.setAdapter(Menu.getAdapter());

        SecuenciaAdapter adapter = new SecuenciaAdapter(this, secuencias);

        listaSecuenciasBtn.setAdapter(adapter);
        getSecuencias(adapter);

        listaSecuenciasBtn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //            cambiar por los metodos de cada secuencia
                comando.setText(secuencias.get(position).getNombre());

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

    public void onBackPressed() {
        Intent intentInicio = new Intent(this, InicioActivity.class);
        startActivity(intentInicio);
    }

    private void getSecuencias(final SecuenciaAdapter arrayAdapter) {

        Call<List<SecuenciaResponse>> call = ApiService.getSecuenciaService().get();

        call.enqueue(new Callback<List<SecuenciaResponse>>() {
            @Override
            public void onResponse(Call<List<SecuenciaResponse>> call, Response<List<SecuenciaResponse>> response) {

                for (SecuenciaResponse s : response.body()) {
                    arrayAdapter.addSecuencia(new Secuencia(s.getNombre()));
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
        Intent intent = new Intent(HomeActivity.this, SecuenciasComunesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
