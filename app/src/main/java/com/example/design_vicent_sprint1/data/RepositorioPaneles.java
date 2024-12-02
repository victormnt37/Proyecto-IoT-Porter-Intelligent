package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.Panel;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPaneles {

    private List<Panel> paneles;

    public RepositorioPaneles() {
        RepositorioEdificios repositorioEdificios = new RepositorioEdificios();
        List<Edificio> edificios = repositorioEdificios.getEdificios();
        //edificios.remove(edificios.size()-1);
        paneles = new ArrayList<>();

        paneles.add(new Panel(edificios.get(0), "Actividad Reciente"));
        paneles.add(new Panel(edificios.get(0), "Tiempo"));
        paneles.add(new Panel(edificios.get(0), "Acceso"));
        paneles.add(new Panel(edificios.get(0), "Estado de Puertas"));
        paneles.add(new Panel(edificios.get(0), "Movimiento"));
        paneles.add(new Panel(edificios.get(0), "Ruido"));
        paneles.add(new Panel(edificios.get(0), "Luz"));
        paneles.add(new Panel(edificios.get(0), "Vibraciones"));
        paneles.add(new Panel(edificios.get(0), "Humo y Gas"));

        paneles.add(new Panel(edificios.get(1), "Actividad Reciente"));
        paneles.add(new Panel(edificios.get(1), "Tiempo"));
        paneles.add(new Panel(edificios.get(1), "Acceso"));
        paneles.add(new Panel(edificios.get(1), "Estado de Puertas"));
        paneles.add(new Panel(edificios.get(1), "Movimiento"));
        paneles.add(new Panel(edificios.get(1), "Ruido"));
        paneles.add(new Panel(edificios.get(1), "Luz"));
        paneles.add(new Panel(edificios.get(1), "Vibraciones"));
        paneles.add(new Panel(edificios.get(1), "Humo y Gas"));

        paneles.add(new Panel(edificios.get(2), "Actividad Reciente"));
        paneles.add(new Panel(edificios.get(2), "Tiempo"));
        paneles.add(new Panel(edificios.get(2), "Acceso"));
        paneles.add(new Panel(edificios.get(2), "Estado de Puertas"));
        paneles.add(new Panel(edificios.get(2), "Movimiento"));
        paneles.add(new Panel(edificios.get(2), "Ruido"));
        paneles.add(new Panel(edificios.get(2), "Luz"));
        paneles.add(new Panel(edificios.get(2), "Vibraciones"));
        paneles.add(new Panel(edificios.get(2), "Humo y Gas"));
    }

    public List<Panel> getPanelesPorEdificio(String id_edificio) {
        List<Panel> resultado = new ArrayList<>();
        for (Panel panel : paneles) {
            if (panel.getEdificio().getId().equals(id_edificio)) {
                resultado.add(panel);
            }
        }
        return resultado;
    }
}

