package com.example.design_vicent_sprint1.model;


import android.net.Uri;

public class Administrador {

    // Atributos
    private String correo;
    private String nombre;
    private String telefono;
    private Uri photoUrl; // URL de la imagen

    // Constructor
    public Administrador(String correo, String nombre, String telefono) {
        this.correo = correo;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Administrador() {
    }

    // Getters y Setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public Uri getPhotoUrl() {
        return photoUrl;
    }



    // Método para mostrar la información del administrador
    @Override
    public String toString() {
        return "Administrador{" +
                "correo='" + correo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
