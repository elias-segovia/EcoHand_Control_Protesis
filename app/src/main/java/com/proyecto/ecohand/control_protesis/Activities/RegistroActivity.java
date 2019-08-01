package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.proyecto.ecohand.control_protesis.Models.Request.UsuarioRequest;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Services.UsuarioService;

import customfonts.MyTextView_SF_Pro_Display_Medium;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usuarioEditText;
    private EditText emailEditText;
    private EditText contrasenaEditText;
    private ProgressBar progressBar;
    private MyTextView_SF_Pro_Display_Medium registrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioEditText = findViewById(R.id.UsuarioID);
        emailEditText = findViewById(R.id.EmailID);
        contrasenaEditText = findViewById(R.id.ContraseñaID);
        progressBar = findViewById(R.id.progressBarID);
        registrarButton = findViewById(R.id.RegistrarID);

        registrarButton.setOnClickListener(this);

    }
//
//    public void Crear_Cuenta(View v) {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RegistrarID:
                UsuarioRequest usuarioRequest = new UsuarioRequest(usuarioEditText.getText().toString(),
                        emailEditText.getText().toString(), contrasenaEditText.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://ecohand-backend.azurewebsites.net/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                UsuarioService usuarioServices = retrofit.create(UsuarioService.class);
                Call <UsuarioRequest> call = usuarioServices.registrarUsuario(usuarioRequest);

                call.enqueue(new Callback<UsuarioRequest>(){
                    @Override
                    public void onResponse(Call <UsuarioRequest> call, Response<UsuarioRequest> response){
                        progressBar.setVisibility(View.GONE);
                        UsuarioRequest responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
                            Toast.makeText(RegistroActivity.this, String.format("Usuario creado correctamente!"),
                                    Toast.LENGTH_LONG).show();;
                        }else{
                            Toast.makeText(RegistroActivity.this, String.format("Response is %s", String.valueOf(response.code()))
                                    , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call <UsuarioRequest> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegistroActivity.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                    }
                });

                break;
        }
    }
}
