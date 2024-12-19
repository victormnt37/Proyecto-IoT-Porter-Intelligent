package com.example.design_vicent_sprint1.model;

import android.net.Uri;

public class Vecino {
    private Edificio edificio;
    private String piso;
    private String puerta;
    private String correo;
    private Uri photoUrl; // URL de la imagen

    public Vecino(Edificio edificio, String piso, String puerta, String correoElectronico) {
        this.edificio = edificio;
        this.piso = piso;
        this.puerta = puerta;
        this.correo = correoElectronico;
    }

    public Vecino(String piso, String puerta, String correoElectronico) {
        this.piso = piso;
        this.puerta = puerta;
        this.correo = correoElectronico;
    }

    public Vecino() {
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public String getPiso() {
        return piso;
    }

    public String getPuerta() {
        return puerta;
    }

    public String getCorreo() {
        return correo;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}

