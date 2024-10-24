package com.example.design_vicent_sprint1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactosActivity extends AppCompatActivity {

    private RepositorioContactos repositorioContactos;
    private RecyclerView recyclerViewContactos;
    private int edificioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        edificioSeleccionado = getIntent().getIntExtra("edificio", 0);

        repositorioContactos = new RepositorioContactos();
        cargarContactos();
    }

    private void cargarContactos() {
        List<Contacto> contactos = repositorioContactos.getContactosPorEdificio(edificioSeleccionado);
        recyclerViewContactos.setLayoutManager(new LinearLayoutManager(this));
        ContactosAdapter adapter = new ContactosAdapter(contactos);
        recyclerViewContactos.setAdapter(adapter);
    }
}

