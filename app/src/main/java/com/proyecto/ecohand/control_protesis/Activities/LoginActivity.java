package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.proyecto.ecohand.control_protesis.Models.ErrorCodes;
import com.proyecto.ecohand.control_protesis.Models.Request.UsuarioRequest;
import com.proyecto.ecohand.control_protesis.Models.Response.ErrorResponse;
import com.proyecto.ecohand.control_protesis.Models.Response.IdResponse;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Services.ApiService;

import java.io.IOException;

import customfonts.EditText__SF_Pro_Display_Medium;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usuarioEditText;
    private EditText passwordEditText;
    private EditText usuario_Error_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioEditText = findViewById(R.id.UsuarioID);
        passwordEditText = findViewById(R.id.PasswordID);
        usuario_Error_TextView = findViewById(R.id.Usuario_Error_TextViewID);

    }

    public void Acceder(View v) {
        final Intent intent = new Intent(this, HomeActivity.class);

        UsuarioRequest usuarioRequest = new UsuarioRequest(usuarioEditText.getText().toString(),
                "", passwordEditText.getText().toString());
        Call<IdResponse> call = ApiService.getUsuarioService().registrarUsuario(usuarioRequest);

        call.enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {

                IdResponse responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {
                    Toast.makeText(LoginActivity.this, String.format("Inicio Correcto!"),
                            Toast.LENGTH_LONG).show();

                    startActivity(intent);
                } else {

                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);

                        switch (errorResponse.getMessage()) {
                            case ErrorCodes.USUARIO_INEXISTENTE:
                                usuario_Error_TextView.setVisibility(View.VISIBLE);
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, String.format("La respuesta es %s - %s", response.code(), response.message())
                                        , Toast.LENGTH_LONG).show();
                                break;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {

                Toast.makeText(LoginActivity.this,
                        "Error is " + t.getMessage()
                        , Toast.LENGTH_LONG).show();
            }
        });


    }

    public void Registrarse(View v) {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }


}
