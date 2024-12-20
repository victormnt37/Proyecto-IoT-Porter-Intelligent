package com.example.design_vicent_sprint1;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.design_vicent_sprint1.data.RepositorioNotificaciones;
import com.example.design_vicent_sprint1.model.Notificacion;
import com.example.design_vicent_sprint1.model.NotificacionesAdapter;
import com.example.design_vicent_sprint1.presentacion.AjustesNotificacionesActivity;
import com.example.design_vicent_sprint1.presentacion.CambiarPerfilActivity;
import com.example.design_vicent_sprint1.presentacion.MainActivity;

import java.util.List;

public class Notificaciones extends Fragment {

    private RecyclerView recyclerView;
    private RepositorioNotificaciones repositorioNotificaciones;
    private String edificioSeleccionado;
    private String userId;
    private ImageView btnAjustesNotificaciones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificaciones, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getString("edificioSeleccionado");
            userId = getArguments().getString("userId");
        }

        btnAjustesNotificaciones = view.findViewById(R.id.imageView);
        btnAjustesNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(getContext(), AjustesNotificacionesActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("edificio", edificioSeleccionado);
            i.putExtra("userId", userId);
            startActivity(i);
            }
        });

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
