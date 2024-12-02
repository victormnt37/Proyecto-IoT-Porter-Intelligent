package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.Puerta;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPuertas {

    private List<Puerta> puertas;

    public RepositorioPuertas() {
        RepositorioEdificios repositorioEdificios = new RepositorioEdificios();
        List<Edificio> edificios = repositorioEdificios.getEdificios();
        edificios.remove(edificios.size()-1);
        puertas = new ArrayList<>();

        puertas.add(new Puerta(edificios.get(0), "Puerta Principal", "P008"));
        puertas.add(new Puerta(edificios.get(0), "Puerta Terraza", "P009"));

        puertas.add(new Puerta(edificios.get(1), "Puerta Principal", "P001"));
        puertas.add(new Puerta(edificios.get(1), "Puerta del Garaje", "P002"));
        puertas.add(new Puerta(edificios.get(1), "Puerta de Emergencia", "P003"));

        puertas.add(new Puerta(edificios.get(2), "Puerta Principal", "P004"));
        puertas.add(new Puerta(edificios.get(2), "Puerta de Acceso al Sótano", "P005"));
        puertas.add(new Puerta(edificios.get(2), "Puerta del Garaje", "P006"));
        puertas.add(new Puerta(edificios.get(2), "Puerta de Emergencia", "P007"));
    }

    public List<Puerta> getPuertasPorEdificio(String id_edificio) {
        List<Puerta> resultado = new ArrayList<>();
        for (Puerta puerta : puertas) {
            if (puerta.getEdificio().getId().equals(id_edificio)) {
                resultado.add(puerta);
            }
        }
        return resultado;
    }
}

