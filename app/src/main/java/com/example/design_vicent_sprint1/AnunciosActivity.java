package com.example.design_vicent_sprint1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnunciosActivity extends AppCompatActivity {

    private RepositorioAnuncios repositorioAnuncios;
    private RecyclerView recyclerViewAnuncios;
    private int edificioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        recyclerViewAnuncios = findViewById(R.id.recyclerViewAnuncios);
        edificioSeleccionado = getIntent().getIntExtra("edificio", 0);

        repositorioAnuncios = new RepositorioAnuncios();
        cargarAnuncios();
    }

    private void cargarAnuncios() {
        List<Anuncio> anuncios = repositorioAnuncios.getAnunciosPorEdificio(edificioSeleccionado);
        //recyclerViewAnuncios.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAnuncios.setLayoutManager(new GridLayoutManager(this,2));
        AnunciosAdapter adapter = new AnunciosAdapter(anuncios);
        recyclerViewAnuncios.setAdapter(adapter);
    }
}

