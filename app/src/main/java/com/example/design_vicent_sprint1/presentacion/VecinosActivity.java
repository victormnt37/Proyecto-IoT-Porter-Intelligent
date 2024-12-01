package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioVecinos;
import com.example.design_vicent_sprint1.model.Vecino;
import com.example.design_vicent_sprint1.model.VecinosAdapter;

import java.util.List;

public class VecinosActivity extends AppCompatActivity {

    private RepositorioVecinos repositorioVecinos;
    private RecyclerView recyclerViewVecinos;
    private String edificioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vecinos);

        recyclerViewVecinos = findViewById(R.id.recyclerViewVecinos);
        edificioSeleccionado = getIntent().getStringExtra("edificio");

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
