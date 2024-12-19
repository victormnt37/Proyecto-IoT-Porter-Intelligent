package com.example.design_vicent_sprint1.model;

public class Contacto {
    private Edificio edificio;
    private String nombre;
    private String telefono;

    public Contacto(Edificio edificio, String nombre, String telefono) {
        this.edificio = edificio;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Contacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }
}

