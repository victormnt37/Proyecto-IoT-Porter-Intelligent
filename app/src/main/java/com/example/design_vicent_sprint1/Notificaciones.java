package com.example.design_vicent_sprint1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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

        //repositorioNotificaciones = new RepositorioNotificaciones();
        cargarNotificaciones();

        return view;
    }

    private void cargarNotificaciones() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificacionesRef = db.collection("edificios")
                .document(edificioSeleccionado)
                .collection("notificaciones");

        notificacionesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Notificacion> notificaciones = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String mensaje = doc.getString("mensaje");
                    String tipo = doc.getString("tipo");
                    String fechaString = doc.getString("fecha");

                    notificaciones.add(new Notificacion(mensaje, tipo, fechaString));
                }

                notificaciones.sort((n1, n2) -> {
                    if (n1.getFechaS() == null || n2.getFechaS() == null) {
                        return 0;
                    }
                    return n2.getFechaS().compareTo(n1.getFechaS());
                });

                if (!notificaciones.isEmpty()) {
                    NotificacionesAdapter adapter = new NotificacionesAdapter(notificaciones, new NotificacionesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Notificacion notificacion) {
                            String tipo = notificacion.getTipo();
                            switch (tipo) {
                                case "incendio":
                                case "emergencia":
                                case "robo":
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:911"));
                                    startActivity(intent);
                                    break;
                                case "anuncios":

                                    break;
                                case "administradores":

                                    break;
                                case "contactos":

                                    break;
                                case "Sensor":

                                    break;
                                default:

                                    break;
                            }
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("Notificaciones", "No hay notificaciones para mostrar.");
                }
            } else {
                Log.e("Notificaciones", "Error al cargar notificaciones: ", task.getException());
            }
        });
//        List<Notificacion> notificaciones = repositorioNotificaciones.getNotificacionesPorEdificio(edificioSeleccionado);
//
//        // Verificar si hay notificaciones antes de cargar el adaptador
//        if (notificaciones != null && !notificaciones.isEmpty()) {
//            NotificacionesAdapter adapter = new NotificacionesAdapter(notificaciones);
//            recyclerView.setAdapter(adapter);
//        } else {
//            // Puedes agregar lógica para mostrar un mensaje vacío si lo deseas
//            System.out.println("No hay notificaciones para este edificio.");
//        }

    }
}
