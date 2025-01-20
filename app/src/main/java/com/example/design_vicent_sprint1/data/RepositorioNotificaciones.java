package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.Notificacion;

import java.util.ArrayList;
import java.util.List;

public class RepositorioNotificaciones {

    private List<Notificacion> notificaciones;

    public RepositorioNotificaciones() {
        RepositorioEdificios repositorioEdificios = new RepositorioEdificios();
        List<Edificio> edificios = repositorioEdificios.getEdificios();
        edificios.remove(edificios.size()-1);

        notificaciones = new ArrayList<>();

       /* notificaciones.add(new Notificacion(edificios.get(0), "Sensor de gas activado en el área de cocina común", "Sensor"));
        notificaciones.add(new Notificacion(edificios.get(0), "Anuncio: Reunión de propietarios este lunes a las 8 PM", "Anuncio"));
        notificaciones.add(new Notificacion(edificios.get(0), "Alerta de emergencia: fuga de agua detectada en el estacionamiento", "Alerta"));

        notificaciones.add(new Notificacion(edificios.get(1), "Notificación de prueba", "Alerta"));
        notificaciones.add(new Notificacion(edificios.get(1), "Movimiento detectado en el sótano", "Sensor"));
        notificaciones.add(new Notificacion(edificios.get(1), "Estado de puertas: puerta principal cerrada", "Sensor"));
        notificaciones.add(new Notificacion(edificios.get(1), "Alerta de emergencia: escape de gas en el piso 3", "Alerta"));
        notificaciones.add(new Notificacion(edificios.get(1), "Anuncio: Corte de agua programado para mañana", "Anuncio"));
        notificaciones.add(new Notificacion(edificios.get(1), "Acceso no autorizado a la azotea", "Sensor"));

        notificaciones.add(new Notificacion(edificios.get(2), "Alerta de emergencia: incendio en la sala de máquinas", "Alerta"));
        notificaciones.add(new Notificacion(edificios.get(2), "Sensor de humo activado en el piso 4", "Sensor"));
        notificaciones.add(new Notificacion(edificios.get(2), "Actividad reciente: acceso al gimnasio a las 10 PM", "Sensor"));
        notificaciones.add(new Notificacion(edificios.get(2), "Estado de puertas: puerta del garaje abierta", "Sensor"));
        notificaciones.add(new Notificacion(edificios.get(2), "Anuncio: Junta de vecinos programada para este viernes", "Anuncio"));
        notificaciones.add(new Notificacion(edificios.get(2), "Movimiento detectado en la terraza", "Sensor"));
        notificaciones.add(new Notificacion(edificios.get(2), "Alerta de robo: intento de acceso forzado en el piso 1", "Alerta"));*/
    }

    public List<Notificacion> getNotificacionesPorEdificio(String id_edificio) {
        List<Notificacion> resultado = new ArrayList<>();
        for (Notificacion notificacion : notificaciones) {
            if (notificacion.getEdificio().getId().equals(id_edificio)) {
                resultado.add(notificacion);
            }
        }
        return resultado;
    }
}

