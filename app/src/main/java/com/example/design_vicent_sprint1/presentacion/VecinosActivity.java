package com.example.design_vicent_sprint1.presentacion;

import static android.content.Intent.getIntent;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioVecinos;
import com.example.design_vicent_sprint1.model.Vecino;
import com.example.design_vicent_sprint1.model.VecinosAdapter;

import java.util.ArrayList;
import java.util.List;

public class VecinosActivity extends AppCompatActivity {

    private RepositorioVecinos repositorioVecinos;
    private RecyclerView recyclerViewVecinos;
    private Spinner spinnerPisos;
    private String edificioSeleccionado;
    private VecinosAdapter adapter;
    private List<Vecino> vecinos; // Lista completa de vecinos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vecinos);

        recyclerViewVecinos = findViewById(R.id.recyclerViewVecinos);
        spinnerPisos = findViewById(R.id.spinnerPiso);
        edificioSeleccionado = getIntent().getStringExtra("edificio");

        repositorioVecinos = new RepositorioVecinos();

        cargarVecinos();
        configurarSpinner();
    }

    private void cargarVecinos() {
        vecinos = repositorioVecinos.getVecinosPorEdificio(edificioSeleccionado);
        recyclerViewVecinos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VecinosAdapter(vecinos);
        recyclerViewVecinos.setAdapter(adapter);
    }

    private void configurarSpinner() {
        // Obtener los pisos únicos del edificio seleccionado
        List<String> pisos = obtenerPisosUnicos(vecinos);

        // Configurar el adaptador del Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, pisos);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPisos.setAdapter(spinnerAdapter);

        // Configurar listener para detectar cambios en la selección
        spinnerPisos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pisoSeleccionado = (String) parent.getItemAtPosition(position);
                filtrarVecinosPorPiso(pisoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });
    }


    private List<String> obtenerPisosUnicos(List<Vecino> vecinos) {
        List<String> pisos = new ArrayList<>();
        for (Vecino vecino : vecinos) {
            if (!pisos.contains(vecino.getPiso())) {
                pisos.add(vecino.getPiso());
            }
        }
        return pisos;
    }

    private void filtrarVecinosPorPiso(String piso) {
        List<Vecino> vecinosFiltrados = new ArrayList<>();
        for (Vecino vecino : vecinos) {
            if (vecino.getPiso().equals(piso)) {
                vecinosFiltrados.add(vecino);
            }
        }
        adapter = new VecinosAdapter(vecinosFiltrados);
        recyclerViewVecinos.setAdapter(adapter);
    }


}
