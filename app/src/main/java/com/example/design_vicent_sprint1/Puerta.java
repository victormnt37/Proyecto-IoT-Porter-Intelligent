package com.example.design_vicent_sprint1;

public class Puerta {
    private Edificio edificio;
    private String nombre;
    private String codigoUnico;

    public Puerta(Edificio edificio, String nombre, String codigoUnico) {
        this.edificio = edificio;
        this.nombre = nombre;
        this.codigoUnico = codigoUnico;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }
}

