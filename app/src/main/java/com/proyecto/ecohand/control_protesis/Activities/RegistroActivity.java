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
import com.proyecto.ecohand.control_protesis.Models.Response.IdResponse;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Services.ApiService;
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

//        registrarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

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

                Call<IdResponse> call = ApiService.getUsuarioService().registrarUsuario(usuarioRequest);

                call.enqueue(new Callback<IdResponse>(){
                    @Override
                    public void onResponse(Call<IdResponse> call, Response<IdResponse> response){
                        progressBar.setVisibility(View.GONE);
                        IdResponse responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
                            Toast.makeText(RegistroActivity.this, String.format("Usuario creado correctamente! " + responseUser.getId()),
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(RegistroActivity.this, String.format("Response is %s", String.valueOf(response.code() + " - " + response.message()))
                                    , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<IdResponse> call, Throwable t) {
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
