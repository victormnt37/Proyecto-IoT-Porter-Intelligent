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
import com.example.design_vicent_sprint1.data.Edificios;
import com.example.design_vicent_sprint1.data.EdificiosAsinc;
import com.example.design_vicent_sprint1.Notificaciones;
import com.example.design_vicent_sprint1.PanelPrincipalEdificio;
import com.example.design_vicent_sprint1.Puertas;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioEdificios;
import com.example.design_vicent_sprint1.model.EdificiosFirestoreAdapter;
import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.SelectorEdificiosAdaptar;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
    private Edificios lista_edificios;
    private String id_edificioSeleccionado;
    private ImageButton btnMenu;
    private ViewPager2 contenedor_vista;
    private MiPagerAdapter pagerAdapter;
    private String userId;
    private Map<String,String> lista_edificios_y_roles; // key -> edificio_id     value -> rol (vecino/admin)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista_edificios = new Edificios();

        //Header
        Toolbar toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        btnEdificios = findViewById(R.id.edificio);//Boton selector edificio
        btnMenu = findViewById(R.id.menu);//Boton menu

        //recuperar el id de firebase del usuario
        Bundle extras = getIntent().getExtras();
        userId = extras.getString("userId");

        //buscar los edificios a los que esta vinculado el usuario
        CollectionReference edificios_del_usuario = FirebaseFirestore.getInstance()
                .collection("usuarios").document(userId).collection("edificios");
        edificios_del_usuario.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                lista_edificios_y_roles = new HashMap<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    lista_edificios_y_roles.put(document.getId(), document.getString("rol"));
                }
                /*String prueba = lista_edificios.toString();
                Log.d("Firestore", prueba + "prueba hecha");*/
                if(!lista_edificios_y_roles.isEmpty()){
                    Map.Entry<String, String> primerEdificio = lista_edificios_y_roles.entrySet().iterator().next();
                    //seleccionar un edificio por defecto
                    id_edificioSeleccionado = primerEdificio.getKey();
                    String rol = lista_edificios_y_roles.get(id_edificioSeleccionado);
                    if(rol.equals("vecino")){
                        btnMenu.setOnClickListener(view -> mostrarMenuVecino(view));
                    }
                    if(rol.equals("admin")){
                        btnMenu.setOnClickListener(view -> mostrarMenuAdmin(view));
                    }
                    //Contenedor de los layouts *** USAR SCROLLVIEW EN LAYOUT
                    pagerAdapter = new MiPagerAdapter(this, id_edificioSeleccionado);
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

                    DocumentReference datos_edificio_seleccionado = FirebaseFirestore.getInstance()
                            .collection("edificios").document(id_edificioSeleccionado);
                    datos_edificio_seleccionado.get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            //rellenar el boton selector de edificio con edificio seleccionado por defecto
                            String nombre_edificio_seleccionado = task2.getResult().getString("nombre");
                            String calle_edificio_seleccionado = task2.getResult().getString("calle");
                            String ciudad_edificio_seleccionado = task2.getResult().getString("ciudad");
                            String texto_boton = nombre_edificio_seleccionado.toUpperCase()+"\n"+
                                    calle_edificio_seleccionado+"\n"+ciudad_edificio_seleccionado;
                            btnEdificios.setText(texto_boton);
                            /*Log.d("Firestore", "Nombre del primer edificio: " + nombreEdificio);*/
                        } else {
                            Log.e("Firestore", "Error o colección vacía", task.getException());
                        }
                    });
                }
            } else {
                Log.e("Firestore", "Error o colección vacía", task.getException());
            }
        });

        adapter = ((Aplicacion) getApplicationContext()).adapter;
        edificios = ((Aplicacion) getApplicationContext()).edificios;

        btnEdificios.setOnClickListener(view -> mostrarPopupEdificios(view));
        adapter.startListening();
    }

    private void mostrarPopupEdificios(View view) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_selector_edificios, null);
        PopupWindow popupWindow = new PopupWindow(popupView, 800, 600, true);
        Set<String> lista_id_edificios = lista_edificios_y_roles.keySet();
        Log.d("Firestore", "ids" + lista_id_edificios);
        CollectionReference edificios = FirebaseFirestore.getInstance()
                .collection("edificios");
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String id : lista_id_edificios) {
            Task<DocumentSnapshot> task = edificios.document(id).get();
            tasks.add(task);
            task.addOnCompleteListener(t -> {
                if (t.isSuccessful() && t.getResult() != null) {
                    DocumentSnapshot documentSnapshot = t.getResult();
                    Edificio edificio = documentSnapshot.toObject(Edificio.class);
                    lista_edificios.cargarEdificio(edificio);
                }else {
                    Log.e("FirestoreError", "Error al obtener el documento con ID: " + id, t.getException());
                }
            });
        }
        Tasks.whenAllComplete(tasks).addOnCompleteListener(task -> {
            RecyclerView recyclerView = popupView.findViewById(R.id.recyclerViewEdificios);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            SelectorEdificiosAdaptar adapter = new SelectorEdificiosAdaptar(lista_edificios.getEdificios(), edificio -> {
                if (edificio.getNombre().equals("add")) {
                    popupWindow.dismiss();
                    mostrarPopupAddEdificio(view);
                } else {
                    id_edificioSeleccionado = edificio.getId();
                    String texto = edificio.getNombre().toUpperCase() + "\n" +
                            edificio.getCalle() + "\n" + edificio.getCiudad();
                    btnEdificios.setText(texto);
                    popupWindow.dismiss();
                    pagerAdapter = new MiPagerAdapter(this, id_edificioSeleccionado);
                    contenedor_vista.setAdapter(pagerAdapter);
                    TabLayout barra_herramientas = findViewById(R.id.barra_de_herramientas);
                    new TabLayoutMediator(barra_herramientas, contenedor_vista, new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            tab.setIcon(iconos[position]);
                        }
                    }).attach();
                    String rol = lista_edificios_y_roles.get(id_edificioSeleccionado);
                    if(rol.equals("vecino")){
                        btnMenu.setOnClickListener(btnMenu -> mostrarMenuVecino(btnMenu));
                    }
                    if(rol.equals("admin")){
                        btnMenu.setOnClickListener(btnMenu -> mostrarMenuAdmin(btnMenu));
                    }
                }
            });
            recyclerView.setAdapter(adapter);
            popupWindow.showAsDropDown(view, 0, 0);
        });
    }

    private void mostrarMenuVecino(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_vecino, popup.getMenu());
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

    //FALTA CREAR ACTIVIDAD ESPECIFICA SEGUN EL ROL
    private  void  mostrarMenuAdmin(View view){
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
            } else if (item.getItemId() == R.id.menu_administradores) {
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
        if (id_edificioSeleccionado != null) {
            intent.putExtra("edificio", id_edificioSeleccionado);
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
        private String edificioSeleccionadoId;

        public MiPagerAdapter(FragmentActivity activity, String edificioId) {
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
            args.putString("edificioSeleccionado", edificioSeleccionadoId);
            fragment.setArguments(args);

            return fragment;
        }

        public void actualizarEdificioSeleccionado(String nuevoEdificioId) {
            this.edificioSeleccionadoId = nuevoEdificioId;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();// deja de escucha los cambios en la base de datos
    }


}



