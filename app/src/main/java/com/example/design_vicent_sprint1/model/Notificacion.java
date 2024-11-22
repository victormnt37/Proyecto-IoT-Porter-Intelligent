package com.example.design_vicent_sprint1.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Notificacion {
    private Edificio edificio;
    private Calendar fecha;
    private String texto;
    private String tipo; // Alerta, Anuncio, Sensor

    public Notificacion(Edificio edificio, String texto, String tipo, Calendar fecha) {
        this.edificio = edificio;
        this.tipo = tipo;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Notificacion(Edificio edificio, String texto, String tipo) {
        this.edificio = edificio;
        this.tipo = tipo;
        this.texto = texto;
        this.fecha = Calendar.getInstance();
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public String getTexto() {
        return texto;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fecha.getTime());
    }
}

