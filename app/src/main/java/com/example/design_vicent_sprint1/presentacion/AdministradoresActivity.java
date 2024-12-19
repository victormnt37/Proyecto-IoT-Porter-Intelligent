package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.model.Administrador;
import com.example.design_vicent_sprint1.model.AdministradoresAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdministradoresActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAdministradores;
    private AdministradoresAdapter administradoresAdapter;
    private String edificioSeleccionado;
    private ImageLoader lectorImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administradores);

        recyclerViewAdministradores = findViewById(R.id.recyclerViewAdministradores);
        edificioSeleccionado = getIntent().getStringExtra("edificio");

        // Configuración del RecyclerView
        recyclerViewAdministradores.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el lector de imágenes
        //lectorImagenes = new ImageLoader(Volley.newRequestQueue(this), new LruBitmapCache());

        // Crear una lista de ejemplo de administradores (puedes reemplazar esto con datos reales)
        List<Administrador> administradores = cargarAdministradores();

        // Configurar el adaptador
        administradoresAdapter = new AdministradoresAdapter(administradores, lectorImagenes);
        recyclerViewAdministradores.setAdapter(administradoresAdapter);
    }

    // Método para cargar datos de ejemplo de administradores
    private List<Administrador> cargarAdministradores() {
        List<Administrador> administradores = new ArrayList<>();
        administradores.add(new Administrador("admin1@example.com", "Administrador 1", "123456789"));
        administradores.add(new Administrador("admin2@example.com", "Administrador 2", "987654321"));
        administradores.add(new Administrador("admin3@example.com", "Administrador 3", "567890123"));
        return administradores;
    }
}
