package com.example.design_vicent_sprint1;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EdificiosActivity extends AppCompatActivity {

    private RepositorioEdificios repositorioEdificios;
    private RecyclerView recyclerViewEdificios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edificios);

        recyclerViewEdificios = findViewById(R.id.recyclerViewEdificios);

        repositorioEdificios = new RepositorioEdificios();
        cargarEdificios();
    }

    private void cargarEdificios() {
        List<Edificio> edificios = repositorioEdificios.getEdificios();
        edificios.remove(edificios.size()-1);
        //recyclerViewEdificios.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEdificios.setLayoutManager(new GridLayoutManager(this,2));
        EdificiosAdapter adapter = new EdificiosAdapter(edificios, this::onEdificioSeleccionado);
        recyclerViewEdificios.setAdapter(adapter);
    }

    private void onEdificioSeleccionado(Edificio edificio) {
        // Cuando un edificio es seleccionado, podrías lanzar otra actividad o hacer algo con el edificio seleccionado.
       /* Intent intent = new Intent(this, VecinosActivity.class); // Ejemplo: abrir VecinosActivity
        intent.putExtra("edificio", edificio.getId()); // Pasar el edificio seleccionado
        startActivity(intent);*/
    }
}
