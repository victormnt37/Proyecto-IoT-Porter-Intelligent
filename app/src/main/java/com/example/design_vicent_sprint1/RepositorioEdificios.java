package com.example.design_vicent_sprint1;

import java.util.ArrayList;
import java.util.List;

public class RepositorioEdificios {
    private List<Edificio> edificios;

    public RepositorioEdificios() {
        edificios = new ArrayList<>();
        cargarEdificiosEjemplo();
    }

    public List<Edificio> getEdificios() {
        return edificios;
    }

    public Edificio getEdificioById(int id) {
        for (Edificio edificio : edificios) {
            if (edificio.getId() == id) {
                return edificio;
            }
        }
        return null;
    }

    private void cargarEdificiosEjemplo() {
        edificios.add(new Edificio(1, "Edificio A", "Calle 123", "Ciudad A"));
        edificios.add(new Edificio(2, "Edificio B", "Avenida 456", "Ciudad B"));
        edificios.add(new Edificio(3, "Edificio C", "Boulevard 789", "Ciudad C"));
        edificios.add(new Edificio(edificios.size()));
    }
}

