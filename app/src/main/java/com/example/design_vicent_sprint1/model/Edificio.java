package com.example.design_vicent_sprint1.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Objects;

public class Edificio {
    @DocumentId
    private String id;
    private String nombre;
    private String calle;
    private String ciudad;

    public Edificio(String id, String nombre, String calle, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.calle = calle;
        this.ciudad = ciudad;
    }

    public Edificio(){}

    public Edificio(int btn_anyadir) {
        this.nombre = "add";
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCalle() {
        return calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edificio edificio = (Edificio) o;
        return Objects.equals(id, edificio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
