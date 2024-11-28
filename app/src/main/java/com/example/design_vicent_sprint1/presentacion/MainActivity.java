package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.design_vicent_sprint1.Cuenta;
import com.example.design_vicent_sprint1.data.Aplicacion;
import com.example.design_vicent_sprint1.data.EdificiosAsinc;
import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.data.EdificiosFirestore;
import com.example.design_vicent_sprint1.Notificaciones;
import com.example.design_vicent_sprint1.PanelPrincipalEdificio;
import com.example.design_vicent_sprint1.Puertas;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioEdificios;
import com.example.design_vicent_sprint1.model.EdificiosFirestoreAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private int[] iconos = new int[]{
            R.drawable.icon_casa,
            R.drawable.icon_llave,
            R.drawable.icon_notificaciones,
            R.drawable.icon_cuenta
    };

    public static EdificiosFirestoreAdapter adapter;
    private EdificiosAsinc edificios;
    private Button btnEdificios;
    private RepositorioEdificios repositorioEdificios;
    private Edificio edificioSeleccionado;
    private ImageButton btnMenu;
    private ViewPager2 contenedor_vista;
    private MiPagerAdapter pagerAdapter;
    private String userId;
    private Map<String,String> lista_edificios; // key -> edificio_id     value -> rol (vecino/admin)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        userId = extras.getString("userId");
        CollectionReference UsuariosRef = FirebaseFirestore.getInstance()
                .collection("usuarios").document(userId).collection("edificios");
        UsuariosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                lista_edificios = new HashMap<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    lista_edificios.put(document.getId(), document.getString("rol"));
                }
                /*String prueba = lista_edificios.toString();
                Log.d("Firestore", prueba + "prueba hecha");*/
                if(!lista_edificios.isEmpty()){
                    Map.Entry<String, String> primerEntry = lista_edificios.entrySet().iterator().next();
                    String id_edificio = primerEntry.getKey();
                    DocumentReference edificioRef = FirebaseFirestore.getInstance()
                            .collection("edificios").document(id_edificio);
                    edificioRef.get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            String nombreEdificio = task2.getResult().getString("nombre");
                            btnEdificios.setText(nombreEdificio);
                            Log.d("Firestore", "Nombre del primer edificio: " + nombreEdificio);
                        } else {
                            Log.e("Firestore", "Error o colección vacía", task.getException());
                        }
                    });
                }
            } else {
                Log.e("Firestore", "Error o colección vacía", task.getException());
            }
        });

        //Header
        Toolbar toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Boton Selector de Edificios
        btnEdificios = findViewById(R.id.edificio);
        adapter = ((Aplicacion) getApplicationContext()).adapter;
        edificios = ((Aplicacion) getApplicationContext()).edificios;
        repositorioEdificios = new RepositorioEdificios();

        //Edificio seleccionado por defecto


        edificioSeleccionado = repositorioEdificios.getEdificioById(1);


        //Contenedor de los layouts *** USAR SCROLLVIEW EN LAYOUT
        pagerAdapter = new MiPagerAdapter(this, edificioSeleccionado.getId());
        contenedor_vista = findViewById(R.id.vista);
        contenedor_vista.setAdapter(pagerAdapter);

        //Barra de herramientas
        TabLayout barra_herramientas = findViewById(R.id.barra_de_herramientas);
        new TabLayoutMediator(barra_herramientas, contenedor_vista, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setIcon(iconos[position]);
            }
        }).attach();

        btnEdificios.setOnClickListener(view -> mostrarPopupEdificios(view));

        btnMenu = findViewById(R.id.menu);
        btnMenu.setOnClickListener(view -> mostrarMenu(view));
        adapter.startListening();
    }

    private void mostrarPopupEdificios(View view) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_selector_edificios, null);
        PopupWindow popupWindow = new PopupWindow(popupView, 800, 600, true);

        RecyclerView recyclerView = popupView.findViewById(R.id.recyclerViewEdificios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*EdificiosAdapter adapter = new EdificiosAdapter(repositorioEdificios.getEdificios(), edificio -> {
            if (edificio.getNombre().equals("add")) {
                popupWindow.dismiss();
                mostrarPopupAddEdificio(view);
            } else {
                edificioSeleccionado = edificio;
                btnEdificios.setText(edificio.getNombre());
                popupWindow.dismiss();

                pagerAdapter = new MiPagerAdapter(this, edificioSeleccionado.getId());
                contenedor_vista.setAdapter(pagerAdapter);
                TabLayout barra_herramientas = findViewById(R.id.barra_de_herramientas);
                new TabLayoutMediator(barra_herramientas, contenedor_vista, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setIcon(iconos[position]);
                    }
                }).attach();

            }
        });*/

        recyclerView.setAdapter(adapter);
        popupWindow.showAsDropDown(view, 0, 0);
    }

    private void mostrarMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_tus_edificios) {
                lanzarActividad(EdificiosActivity.class);
                return true;
            } else if (item.getItemId() == R.id.menu_vecinos) {
                lanzarActividad(VecinosActivity.class);
                return true;
            } else if (item.getItemId() == R.id.menu_anuncios) {
                lanzarActividad(AnunciosActivity.class);
                return true;
            } else if (item.getItemId() == R.id.menu_contactos) {
                lanzarActividad(ContactosActivity.class);
                return true;
            } else {
                return false;
            }
        });

        popup.show();
    }

    private void lanzarActividad(Class<?> actividad) {
        Intent intent = new Intent(MainActivity.this, actividad);
        if (edificioSeleccionado != null) {
            intent.putExtra("edificio", edificioSeleccionado.getId());
        }
        startActivity(intent);
    }

    private void mostrarPopupAddEdificio(View view) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_add_edificio, null);
        PopupWindow popupWindowAdd = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        EditText idEdificio = popupView.findViewById(R.id.idEdificio);
        Button btnAdd = popupView.findViewById(R.id.btnAddEdificio);

        btnAdd.setOnClickListener(v -> {

            //********************* PROCESO AÑADIR EDIFICIO

            popupWindowAdd.dismiss();
        });

        popupWindowAdd.setOutsideTouchable(true);
        popupWindowAdd.setFocusable(true);
        popupWindowAdd.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public class MiPagerAdapter extends FragmentStateAdapter {
        private int edificioSeleccionadoId;

        public MiPagerAdapter(FragmentActivity activity, int edificioId) {
            super(activity);
            this.edificioSeleccionadoId = edificioId;
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        @Override
        @NonNull
        public Fragment createFragment(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new PanelPrincipalEdificio();
                    break;
                case 1:
                    fragment = new Puertas();
                    break;
                case 2:
                    fragment = new Notificaciones();
                    break;
                case 3:
                    fragment = new Cuenta();
                    break;
                default:
                    fragment = new PanelPrincipalEdificio();
            }

            Bundle args = new Bundle();
            args.putInt("edificioSeleccionado", edificioSeleccionadoId);
            fragment.setArguments(args);

            return fragment;
        }

        public void actualizarEdificioSeleccionado(int nuevoEdificioId) {
            this.edificioSeleccionadoId = nuevoEdificioId;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();// deja de escucha los cambios en la base de datos
    }
}



