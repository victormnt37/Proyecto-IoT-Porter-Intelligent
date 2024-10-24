package com.example.design_vicent_sprint1;

public class Edificio {
    private int id;
    private String nombre;
    private String calle;
    private String ciudad;

    public Edificio(int id, String nombre, String calle, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.calle = calle;
        this.ciudad = ciudad;
    }

    public Edificio(int id) {
        this.id = id;
        this.nombre = "add";
    }

    public int getId() {
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
}
