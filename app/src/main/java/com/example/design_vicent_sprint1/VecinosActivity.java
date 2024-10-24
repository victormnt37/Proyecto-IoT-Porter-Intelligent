package com.example.design_vicent_sprint1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VecinosActivity extends AppCompatActivity {

    private RepositorioVecinos repositorioVecinos;
    private RecyclerView recyclerViewVecinos;
    private int edificioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vecinos);

        recyclerViewVecinos = findViewById(R.id.recyclerViewVecinos);
        edificioSeleccionado = getIntent().getIntExtra("edificio", 0);

        repositorioVecinos = new RepositorioVecinos();

        cargarVecinos();
    }

    private void cargarVecinos() {
        List<Vecino> vecinos = repositorioVecinos.getVecinosPorEdificio(edificioSeleccionado);
        recyclerViewVecinos.setLayoutManager(new LinearLayoutManager(this));
        VecinosAdapter adapter = new VecinosAdapter(vecinos);
        recyclerViewVecinos.setAdapter(adapter);
    }
}
