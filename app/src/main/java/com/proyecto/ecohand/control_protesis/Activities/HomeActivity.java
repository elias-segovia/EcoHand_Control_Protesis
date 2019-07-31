package com.proyecto.ecohand.control_protesis.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.ecohand.control_protesis.Models.Response.SecuenciaResponse;
import com.proyecto.ecohand.control_protesis.Services.SecuenciaService;
import com.proyecto.ecohand.control_protesis.Services.UsuarioService;
import com.proyecto.ecohand.control_protesis.R;
import com.proyecto.ecohand.control_protesis.Models.Response.UsuarioResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    ListView list;
    ArrayList<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("EcoHand - Control de Prótesis");
        setContentView(R.layout.activity_home);

        list = findViewById(R.id.list);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);

        list.setAdapter(arrayAdapter);

        getSecuencias(arrayAdapter);
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

    private void getSecuencias(final ArrayAdapter arrayAdapter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ecohand-backend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SecuenciaService secuenciaService = retrofit.create(SecuenciaService.class);
        Call<List<SecuenciaResponse>> call = secuenciaService.get();

        call.enqueue(new Callback<List<SecuenciaResponse>>() {
            @Override
            public void onResponse(Call<List<SecuenciaResponse>> call, Response<List<SecuenciaResponse>> response) {
//                for(Post post : response.body()) {
//                    titles.add(post.getTitle());
//                }
//                arrayAdapter.notifyDataSetChanged();
                for (SecuenciaResponse s : response.body()) {
                    titles.add(s.getNombre());
                    titles.add(s.getCodigoEjecutable());
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<SecuenciaResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
