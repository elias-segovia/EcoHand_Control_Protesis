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

    String API_ROUTE_USERS = "Usuarios/Users";
    String API_ROUTE_REGISTRO = "Usuarios/User";
    String API_ROUTE_LOGIN = "Usuarios/Login";

    @GET(API_ROUTE_USERS)
    Call< List<UsuarioResponse> > get();

    @POST(API_ROUTE_REGISTRO)
    Call<IdResponse>  registrarUsuario(@Body UsuarioRequest userRequest);

    @POST(API_ROUTE_LOGIN)
    Call<IdResponse>  verificarUsuario(@Body UsuarioRequest userRequest);


}
