package com.example.design_vicent_sprint1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.design_vicent_sprint1.data.RepositorioNotificaciones;
import com.example.design_vicent_sprint1.model.Notificacion;
import com.example.design_vicent_sprint1.model.NotificacionesAdapter;

import java.util.List;

public class Notificaciones extends Fragment {

    private RecyclerView recyclerView;
    private RepositorioNotificaciones repositorioNotificaciones;
    private int edificioSeleccionado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificaciones, container, false);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener argumento del edificio seleccionado
        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getInt("edificioSeleccionado", 0);
        }

        // Cargar datos del repositorio
        repositorioNotificaciones = new RepositorioNotificaciones();
        cargarNotificaciones();

        return view;
    }

    private void cargarNotificaciones() {
        List<Notificacion> notificaciones = repositorioNotificaciones.getNotificacionesPorEdificio(edificioSeleccionado);

        // Verificar si hay notificaciones antes de cargar el adaptador
        if (notificaciones != null && !notificaciones.isEmpty()) {
            NotificacionesAdapter adapter = new NotificacionesAdapter(notificaciones);
            recyclerView.setAdapter(adapter);
        } else {
            // Puedes agregar lógica para mostrar un mensaje vacío si lo deseas
            System.out.println("No hay notificaciones para este edificio.");
        }
    }
}
