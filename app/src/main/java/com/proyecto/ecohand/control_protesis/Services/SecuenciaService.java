package com.proyecto.ecohand.control_protesis.Services;

import com.proyecto.ecohand.control_protesis.Models.Request.SecuenciaRequest;
import com.proyecto.ecohand.control_protesis.Models.Request.UsuarioRequest;
import com.proyecto.ecohand.control_protesis.Models.Response.SecuenciaResponse;
import com.proyecto.ecohand.control_protesis.Models.Response.UsuarioResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SecuenciaService {

    String API_ROUTE_SECUENCIAS = "Secuencias/Get";
    String API_ROUTE_SECUENCIAS_USUARIO = "Secuencias/Secuencias";

    @GET(API_ROUTE_SECUENCIAS)
    Call<List<SecuenciaResponse>> get();

    @POST(API_ROUTE_SECUENCIAS_USUARIO)
    Call<List<SecuenciaResponse>> getSecuencias(@Body UsuarioRequest userRequest);
}
