package com.proyecto.ecohand.control_protesis.Services;

import com.proyecto.ecohand.control_protesis.Models.Response.SecuenciaResponse;
import com.proyecto.ecohand.control_protesis.Models.Response.UsuarioResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SecuenciaService {

    String API_ROUTE = "Secuencias";

    @GET(API_ROUTE)
    Call<List<SecuenciaResponse>> get();
}
