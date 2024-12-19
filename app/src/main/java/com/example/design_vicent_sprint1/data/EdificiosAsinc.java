package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Edificio;

public interface EdificiosAsinc {

    interface EscuchadorElemento{
        void onRespuesta(Edificio edificio);
    }

    interface EscuchadorTamaño{
        void onRespuesta(long tamaño);
    }

    void elemento(String id, EscuchadorElemento escuchador);

    void anyade(Edificio edificio);

    String nuevo();

    void borrar(String id);

    void actualiza(String id, Edificio edificio);

    void tamaño(EscuchadorTamaño escuchador);
}
