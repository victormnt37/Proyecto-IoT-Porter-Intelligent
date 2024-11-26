package com.example.design_vicent_sprint1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.design_vicent_sprint1.data.RepositorioPuertas;
import com.example.design_vicent_sprint1.model.Puerta;
import com.example.design_vicent_sprint1.model.PuertasAdapter;

import java.util.List;

public class Puertas extends Fragment {

    private RecyclerView recyclerView;
    private RepositorioPuertas repositorioPuertas;
    private int edificioSeleccionado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_puertas, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPuertas);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getInt("edificioSeleccionado");
        }

        repositorioPuertas = new RepositorioPuertas();
        cargarPuertas();
        return view;
    }

    private void cargarPuertas() {
        List<Puerta> puertas = repositorioPuertas.getPuertasPorEdificio(edificioSeleccionado);
        PuertasAdapter adapter = new PuertasAdapter(puertas);
        recyclerView.setAdapter(adapter);
    }


}

