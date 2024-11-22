package com.example.design_vicent_sprint1.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Anuncio {
    private Edificio edificio;
    private String asunto;
    private String texto;
    private String autor;
    private Calendar fecha;

    public Anuncio(Edificio edificio, String asunto, String texto, String autor) {
        this.edificio = edificio;
        this.asunto = asunto;
        this.texto = texto;
        this.autor = autor;
        this.fecha = Calendar.getInstance();
    }

    public Anuncio(Edificio edificio, String asunto, String texto, String autor, Calendar fecha) {
        this.edificio = edificio;
        this.asunto = asunto;
        this.texto = texto;
        this.autor = autor;
        this.fecha = fecha;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getTexto() {
        return texto;
    }

    public String getAutor() {
        return autor;
    }

    public String getFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fecha.getTime());
    }
}

