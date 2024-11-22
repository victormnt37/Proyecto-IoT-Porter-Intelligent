package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.Vecino;

import java.util.ArrayList;
import java.util.List;

public class RepositorioVecinos {
    private List<Vecino> vecinos;

    public RepositorioVecinos() {
        vecinos = new ArrayList<>();
        cargarVecinosEjemplo();
    }

    public List<Vecino> getVecinos() {
        return vecinos;
    }

    public List<Vecino> getVecinosPorEdificio(int id_edificio) {
        List<Vecino> resultado = new ArrayList<>();
        for (Vecino vecino : vecinos) {
            if (vecino.getEdificio().getId() == id_edificio) {
                resultado.add(vecino);
            }
        }
        return resultado;
    }

    private void cargarVecinosEjemplo() {
        RepositorioEdificios repositorioEdificios = new RepositorioEdificios();
        List<Edificio> edificios = repositorioEdificios.getEdificios();
        edificios.remove(edificios.size()-1); // edificio ADD

        vecinos.add(new Vecino(edificios.get(0), "1", "A", "vecino13@correo.com"));
        vecinos.add(new Vecino(edificios.get(0), "1", "B", "vecino14@correo.com"));
        vecinos.add(new Vecino(edificios.get(0), "2", "A", "vecino15@correo.com"));
        vecinos.add(new Vecino(edificios.get(0), "2", "B", "vecino16@correo.com"));
        vecinos.add(new Vecino(edificios.get(0), "3", "A", "vecino17@correo.com"));
        vecinos.add(new Vecino(edificios.get(0), "3", "B", "vecino18@correo.com"));


        vecinos.add(new Vecino(edificios.get(1), "1", "A", "vecino1@correo.com"));
        vecinos.add(new Vecino(edificios.get(1), "1", "B", "vecino2@correo.com"));
        vecinos.add(new Vecino(edificios.get(1), "2", "A", "vecino3@correo.com"));
        vecinos.add(new Vecino(edificios.get(1), "2", "B", "vecino4@correo.com"));
        vecinos.add(new Vecino(edificios.get(1), "3", "A", "vecino5@correo.com"));
        vecinos.add(new Vecino(edificios.get(1), "3", "B", "vecino6@correo.com"));
        vecinos.add(new Vecino(edificios.get(1), "4", "A", "vecino7@correo.com"));
        vecinos.add(new Vecino(edificios.get(1), "4", "B", "vecino8@correo.com"));

        vecinos.add(new Vecino(edificios.get(2), "1", "A", "vecino9@correo.com"));
        vecinos.add(new Vecino(edificios.get(2), "1", "B", "vecino10@correo.com"));
        vecinos.add(new Vecino(edificios.get(2), "2", "A", "vecino11@correo.com"));
        vecinos.add(new Vecino(edificios.get(2), "2", "B", "vecino12@correo.com"));
    }
}

