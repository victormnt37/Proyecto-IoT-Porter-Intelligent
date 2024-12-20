package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Edificio;

import java.util.ArrayList;
import java.util.List;

public class Edificios extends ArrayList{

    public List<Edificio> edificios;

    public Edificios() {
        edificios = new ArrayList<>();
        //edificios.add(new Edificio(0)); EDIFICIO ADD
        //NO HACE FALTA LA VINCULACION SE HACE AUTOMATICAMENTE
        //cargarEdificiosEjemplo();
    }

    public void cargarEdificio(Edificio edificio){
        if (!edificios.contains(edificio)) {
            edificios.add(edificio);
        }
    }

    public List<Edificio> getEdificios() {
        return edificios;
    }

    public Edificio getEdificioById(String id) {
        for (Edificio edificio : edificios) {
            if (edificio.getId().equals(id)) {
                return edificio;
            }
        }
        return null;
    }

    public void ordenarEdificios(String id){
        Edificio edificioSeleccionado = null;
        for (Edificio edificio : edificios) {
            if (edificio.getId().equals(id)) {
                edificioSeleccionado = edificio;
                break;
            }
        }
        if (edificioSeleccionado != null) {
            edificios.remove(edificioSeleccionado);
            edificios.add(0, edificioSeleccionado);
        }
    }
}
