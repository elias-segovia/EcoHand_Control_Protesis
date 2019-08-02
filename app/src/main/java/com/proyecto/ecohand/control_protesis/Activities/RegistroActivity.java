package com.proyecto.ecohand.control_protesis.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.proyecto.ecohand.control_protesis.Models.ErrorCodes;
import com.proyecto.ecohand.control_protesis.Models.Request.UsuarioRequest;
import com.proyecto.ecohand.control_protesis.Models.Response.ErrorResponse;
import com.proyecto.ecohand.control_protesis.Models.Response.IdResponse;
import com.proyecto.ecohand.control_protesis.Models.Response.UsuarioResponse;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Services.ApiService;
import com.proyecto.ecohand.control_protesis.Services.UsuarioService;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

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
    private MyTextView_SF_Pro_Display_Medium registrarButton;
    private Intent intent;
    private TextView Email_Error_TextView;
    private TextView Error_TextView;
    private TextView Usuario_Error_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioEditText = findViewById(R.id.UsuarioID);
        emailEditText = findViewById(R.id.EmailID);
        contrasenaEditText = findViewById(R.id.ContraseñaID);
        registrarButton = findViewById(R.id.RegistrarID);
        Email_Error_TextView = findViewById(R.id.Email_Error_TextViewID);
        Error_TextView = findViewById(R.id.Error_TextViewID);
        Usuario_Error_TextView = findViewById(R.id.Usuario_Error_TextViewID);

        registrarButton.setOnClickListener(this);

//        registrarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    public void volver(View v) {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        intent = new Intent(this, HomeActivity.class);

        if (usuarioEditText.getText().toString().isEmpty() ||
                emailEditText.getText().toString().isEmpty() || contrasenaEditText.getText().toString().isEmpty()) {
            Error_TextView.setText("Ningún campo puede estar vacío!");
            Error_TextView.setVisibility(View.VISIBLE);
            Email_Error_TextView.setVisibility(View.INVISIBLE);
            Usuario_Error_TextView.setVisibility(View.INVISIBLE);
        } else if (!validarEmail(emailEditText.getText().toString())) {
            Email_Error_TextView.setVisibility(View.VISIBLE);
            Error_TextView.setVisibility(View.INVISIBLE);
            Usuario_Error_TextView.setVisibility(View.INVISIBLE);
        } else {
            Email_Error_TextView.setVisibility(View.INVISIBLE);
            Error_TextView.setVisibility(View.INVISIBLE);
            switch (view.getId()) {
                case R.id.RegistrarID:
                    UsuarioRequest usuarioRequest = new UsuarioRequest(usuarioEditText.getText().toString(),
                            emailEditText.getText().toString(), contrasenaEditText.getText().toString());

                    Call<IdResponse> call = ApiService.getUsuarioService().registrarUsuario(usuarioRequest);

                    call.enqueue(new Callback<IdResponse>() {
                        @Override
                        public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {

                            IdResponse responseUser = response.body();
                            if (response.isSuccessful() && responseUser != null) {
                                Toast.makeText(RegistroActivity.this, String.format("Usuario creado correctamente!"),
                                        Toast.LENGTH_LONG).show();

                                SharedPreferences prefs=getSharedPreferences("PreferenciaUsuario", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("UserName",emailEditText.getText().toString());
                                editor.putString("Email",emailEditText.getText().toString());
                                editor.putBoolean("Registrado",true);
                                editor.commit();
                                finish();

                                startActivity(intent);
                            } else {

                                try {
                                    ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);

                                    switch (errorResponse.getMessage()) {
                                        case ErrorCodes.USUARIO_EXISTENTE:
                                            Usuario_Error_TextView.setVisibility(View.VISIBLE);
                                            break;
                                        default:
                                            Toast.makeText(RegistroActivity.this, String.format("La respuesta es %s - %s", response.code(), response.message())
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

                            Toast.makeText(RegistroActivity.this,
                                    "Error is " + t.getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    });

                    break;
            }
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
