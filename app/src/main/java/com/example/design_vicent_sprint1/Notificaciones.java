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

import java.util.List;

public class Notificaciones extends Fragment {

    private RecyclerView recyclerView;
    private RepositorioNotificaciones repositorioNotificaciones;
    private int edificioSeleccionado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificaciones, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getInt("edificioSeleccionado");
        }

        repositorioNotificaciones = new RepositorioNotificaciones();
        cargarNotificaciones();
        return view;
    }

    private void cargarNotificaciones() {
        List<Notificacion> notificaciones = repositorioNotificaciones.getNotificacionesPorEdificio(edificioSeleccionado);
        NotificacionesAdapter adapter = new NotificacionesAdapter(notificaciones);
        recyclerView.setAdapter(adapter);
    }
}
