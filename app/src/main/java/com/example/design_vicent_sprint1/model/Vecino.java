package com.example.design_vicent_sprint1.model;

public class Vecino {
    private Edificio edificio;
    private String piso;
    private String puerta;
    private String correoElectronico;

    public Vecino(Edificio edificio, String piso, String puerta, String correoElectronico) {
        this.edificio = edificio;
        this.piso = piso;
        this.puerta = puerta;
        this.correoElectronico = correoElectronico;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }
}

