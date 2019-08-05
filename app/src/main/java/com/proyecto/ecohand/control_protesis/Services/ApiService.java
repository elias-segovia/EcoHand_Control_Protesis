package com.proyecto.ecohand.control_protesis.Services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static Retrofit client = new Retrofit.Builder()
            .baseUrl("https://ecohand-backend.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static UsuarioService usuarioService = client.create(UsuarioService.class);
    private static SecuenciaService secuenciaService = client.create(SecuenciaService.class);

    public static UsuarioService getUsuarioService(){
        return usuarioService;
    }
    public static SecuenciaService getSecuenciaService() { return  secuenciaService; }
}
