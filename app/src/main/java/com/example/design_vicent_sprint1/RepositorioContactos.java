package com.example.design_vicent_sprint1;

import java.util.ArrayList;
import java.util.List;

public class RepositorioContactos {
    private List<Contacto> contactos;

    public RepositorioContactos() {
        contactos = new ArrayList<>();
        cargarContactosEjemplo();
    }

    public List<Contacto> getContactosPorEdificio(int id_edificio) {
        List<Contacto> resultado = new ArrayList<>();
        for (Contacto contacto : contactos) {
            if (contacto.getEdificio().getId() == id_edificio) {
                resultado.add(contacto);
            }
        }
        return resultado;
    }

    private void cargarContactosEjemplo() {
        RepositorioEdificios repositorioEdificios = new RepositorioEdificios();
        List<Edificio> edificios = repositorioEdificios.getEdificios();
        edificios.remove(edificios.size()-1);

        contactos.add(new Contacto(edificios.get(0), "Conserje", "555-3456"));
        contactos.add(new Contacto(edificios.get(0), "Administrador", "555-7890"));
        contactos.add(new Contacto(edificios.get(0), "Mantenimiento", "555-6543"));
        contactos.add(new Contacto(edificios.get(0), "Seguridad", "555-5432"));
        contactos.add(new Contacto(edificios.get(0), "Servicios Generales", "555-8765"));
        contactos.add(new Contacto(edificios.get(0), "Limpieza", "555-2346"));
        contactos.add(new Contacto(edificios.get(0), "Jardinería", "555-7654"));
        contactos.add(new Contacto(edificios.get(0), "Administración de Copropiedad", "555-9875"));


        contactos.add(new Contacto(edificios.get(1), "Conserje", "555-1234"));
        contactos.add(new Contacto(edificios.get(1), "Administrador", "555-5678"));
        contactos.add(new Contacto(edificios.get(1), "Mantenimiento", "555-8765"));

        contactos.add(new Contacto(edificios.get(2), "Conserje", "555-2345"));
        contactos.add(new Contacto(edificios.get(2), "Administrador", "555-6789"));
        contactos.add(new Contacto(edificios.get(2), "Mantenimiento", "555-9876"));
        contactos.add(new Contacto(edificios.get(2), "Seguridad", "555-4321"));
    }
}

