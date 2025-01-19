package com.example.design_vicent_sprint1.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Anuncio {
    private Edificio edificio;
    private String asunto;
    private String texto;
    private String autor;
    private Calendar fecha;
    private String id;

    public  Anuncio(){}

    public Anuncio(String asunto, String texto, String autor) {
        this.asunto = asunto;
        this.texto = texto;
        this.autor = autor;
        this.fecha = Calendar.getInstance();
    }

    public Anuncio(String id, String asunto, String texto, String autor) {
        this.id = id;
        this.asunto = asunto;
        this.texto = texto;
        this.autor = autor;
        this.fecha = Calendar.getInstance();
    }

    public Anuncio(String id,String asunto, String texto, String autor, String fecha) {
        this.id = id;
        this.edificio = edificio;
        this.asunto = asunto;
        this.texto = texto;
        this.autor = autor;
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formato.parse(fecha);
            this.fecha = Calendar.getInstance();
            this.fecha.setTime(date);
        }
        catch (ParseException e) {
            this.fecha = null;
        }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

