package com.example.design_vicent_sprint1;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.design_vicent_sprint1.data.RepositorioPuertas;
import com.example.design_vicent_sprint1.model.Puerta;
import com.example.design_vicent_sprint1.model.PuertasAdapter;

import java.util.List;

public class Puertas extends Fragment {

    private RecyclerView recyclerView;
    private RepositorioPuertas repositorioPuertas;
    private String edificioSeleccionado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_puertas, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPuertas);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getString("edificioSeleccionado");
        }

        repositorioPuertas = new RepositorioPuertas();
        cargarPuertas();

        return view;
    }

    private void cargarPuertas() {
        List<Puerta> puertas = repositorioPuertas.getPuertasPorEdificio(edificioSeleccionado);

        PuertasAdapter adapter = new PuertasAdapter(puertas) {
            @Override
            public void onBindViewHolder(@NonNull PuertasAdapter.PuertaViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Puerta puerta = puertas.get(position);
                holder.itemView.setOnClickListener(v -> {
                    // Muestra el popup cuando se hace clic en un item
                    mostrarPopupPuertas(puerta);
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    // Método para mostrar el popup
    private void mostrarPopupPuertas(Puerta puerta) {
        Dialog popupDialog = new Dialog(requireContext());
        popupDialog.setContentView(R.layout.popup_puertas);
        popupDialog.setCanceledOnTouchOutside(true);

        // Personaliza el popup con datos de la puerta seleccionada
        //TextView tituloPopup = popupDialog.findViewById(R.id.textTituloPopup);
        //tituloPopup.setText("Opciones para: " + puerta.getNombre());

        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDialog.show();
    }
}
