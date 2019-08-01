package com.proyecto.ecohand.control_protesis.Models.Request;

public class UsuarioRequest {

    private String username;
    private String email;
    private String contraseña;

    public UsuarioRequest(String username, String email, String contraseña) {
        this.username = username;
        this.email = email;
        this.contraseña = contraseña;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
