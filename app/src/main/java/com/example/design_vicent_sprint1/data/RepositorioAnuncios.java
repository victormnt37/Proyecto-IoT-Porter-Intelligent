package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Anuncio;
import com.example.design_vicent_sprint1.model.Edificio;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAnuncios {
    private List<Anuncio> anuncios;

    public RepositorioAnuncios() {
        anuncios = new ArrayList<>();
        cargarAnunciosEjemplo();
    }

    public List<Anuncio> getAnunciosPorEdificio(String id_edificio) {
        List<Anuncio> resultado = new ArrayList<>();
        for (Anuncio anuncio : anuncios) {
            if (anuncio.getEdificio().getId().equals(id_edificio)) {
                resultado.add(anuncio);
            }
        }
        return resultado;
    }

    private void cargarAnunciosEjemplo() {
        RepositorioEdificios repositorioEdificios = new RepositorioEdificios();
        List<Edificio> edificios = repositorioEdificios.getEdificios();
        edificios.remove(edificios.size()-1); // edificio ADD

        anuncios.add(new Anuncio(edificios.get(0), "Fumigación programada", "Este sábado se realizará una fumigación en las áreas comunes del edificio.", "Mantenimiento"));
        anuncios.add(new Anuncio(edificios.get(0), "Cambio de cerraduras", "Se procederá al cambio de cerraduras de la puerta principal por motivos de seguridad. Coordinar con la administración para las nuevas llaves.", "Administrador"));

        anuncios.add(new Anuncio(edificios.get(1), "Reunión de vecinos", "Se informa que el próximo martes tendremos una reunión a las 7 PM.", "Administrador"));
        anuncios.add(new Anuncio(edificios.get(1), "Corte de luz", "El edificio sufrirá un corte de luz este jueves por mantenimiento.", "Mantenimiento"));
        anuncios.add(new Anuncio(edificios.get(1), "Nueva portería", "Se ha inaugurado la nueva portería con horarios extendidos.", "Administrador"));

        anuncios.add(new Anuncio(edificios.get(2), "Mantenimiento del ascensor", "El ascensor estará fuera de servicio por mantenimiento el próximo miércoles.", "Mantenimiento"));
        anuncios.add(new Anuncio(edificios.get(2), "Reunión de copropietarios", "Se convoca a los copropietarios a una reunión para discutir el presupuesto anual el viernes a las 6 PM.", "Administrador"));
        anuncios.add(new Anuncio(edificios.get(2), "Reparación de la piscina", "La piscina estará cerrada por reparaciones durante los próximos 3 días.", "Mantenimiento"));
        anuncios.add(new Anuncio(edificios.get(2), "Nueva recolección de reciclaje", "A partir del lunes, se implementará un nuevo sistema de recolección de reciclaje.", "Administrador"));
        anuncios.add(new Anuncio(edificios.get(2), "Entrega de correspondencia", "Se recuerda a los vecinos que la correspondencia será entregada en la nueva portería a partir de esta semana.", "Administrador"));
    }
}

