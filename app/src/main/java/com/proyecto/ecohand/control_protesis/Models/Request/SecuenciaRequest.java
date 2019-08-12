package com.proyecto.ecohand.control_protesis.Models.Request;

public class SecuenciaRequest {

    private int usuarioID;

    public SecuenciaRequest(int usuarioID) {
        this.usuarioID = usuarioID;
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }
}
