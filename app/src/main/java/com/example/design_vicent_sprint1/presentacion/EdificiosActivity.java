package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioEdificios;
import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.EdificiosAdapter;

import java.util.List;

public class EdificiosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEdificios;
    private List<Edificio> listaEdificios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edificios);

        recyclerViewEdificios = findViewById(R.id.recyclerViewEdificios);

        RepositorioEdificios repositorioEdificios = new RepositorioEdificios();
        listaEdificios = repositorioEdificios.getEdificios(); // Obtener edificios

        configurarRecyclerView();
    }

    private void configurarRecyclerView() {
        recyclerViewEdificios.setLayoutManager(new GridLayoutManager(this, 1));

        EdificiosAdapter adapter = new EdificiosAdapter(listaEdificios, new EdificiosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Edificio edificio) {
                onEdificioSeleccionado(edificio); // Manejar selección
            }

            @Override
            public void onDeleteClick(int position) {
                // Eliminar el edificio de la lista
                listaEdificios.remove(position);
                recyclerViewEdificios.getAdapter().notifyItemRemoved(position);
            }
        });

        recyclerViewEdificios.setAdapter(adapter);
    }

    private void onEdificioSeleccionado(Edificio edificio) {
        // Aquí puedes manejar la acción al seleccionar un edificio.
        // Ejemplo: abrir otra actividad.
        /*Intent intent = new Intent(this, VecinosActivity.class);
        intent.putExtra("edificio", edificio.getId());
        startActivity(intent);*/
    }
}
