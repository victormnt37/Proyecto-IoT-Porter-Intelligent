package com.example.design_vicent_sprint1;

import java.util.List;

public class Panel {
    private Edificio edificio;
    private String tipo;
    // Actividad Reciente, Tiempo, Acceso, Estado de Puertas, Movimiento, Ruido, Luz, Vibraciones, Humo y Gas

    //private List<Sensor> sensores;

    public Panel(Edificio edificio, String tipo/*, List<Sensor> sensores*/) {
        this.edificio = edificio;
        this.tipo = tipo;
        //this.sensores = sensores;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public String getTipo() {
        return tipo;
    }
}
