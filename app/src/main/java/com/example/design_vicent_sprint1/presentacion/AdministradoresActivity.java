package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioContactos;
import com.example.design_vicent_sprint1.model.Contacto;
import com.example.design_vicent_sprint1.model.ContactosAdapter;

import java.util.List;

public class AdministradoresActivity extends AppCompatActivity {


    private RecyclerView recyclerViewContactos;
    private String edificioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        edificioSeleccionado = getIntent().getStringExtra("edificio");



    }


}
