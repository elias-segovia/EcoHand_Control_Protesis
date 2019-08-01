package com.proyecto.ecohand.control_protesis.Services;

import com.proyecto.ecohand.control_protesis.Models.Request.UsuarioRequest;
import com.proyecto.ecohand.control_protesis.Models.Response.IdResponse;
import com.proyecto.ecohand.control_protesis.Models.Response.UsuarioResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UsuarioService {

    String API_ROUTE = "Usuarios";

    @GET(API_ROUTE)
    Call< List<UsuarioResponse> > get();

    @POST(API_ROUTE)
    Call<IdResponse>  registrarUsuario(@Body UsuarioRequest userRequest);
}
