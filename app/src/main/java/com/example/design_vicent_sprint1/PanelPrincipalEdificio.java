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

public class PanelPrincipalEdificio extends Fragment {

    private RecyclerView recyclerView;
    private RepositorioPaneles repositorioPaneles;
    private int edificioSeleccionado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panel_principal_edificio, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPaneles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getInt("edificioSeleccionado");
        }

        repositorioPaneles = new RepositorioPaneles();
        cargarPaneles();
        return view;
    }

    private void cargarPaneles() {
        List<Panel> paneles = repositorioPaneles.getPanelesPorEdificio(edificioSeleccionado);
        PanelAdapter adapter = new PanelAdapter(paneles);
        recyclerView.setAdapter(adapter);
    }
}

