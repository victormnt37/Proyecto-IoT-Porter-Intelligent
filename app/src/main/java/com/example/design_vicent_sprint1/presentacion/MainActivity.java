package com.example.design_vicent_sprint1.presentacion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;


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
import com.example.design_vicent_sprint1.data.Edificios;
import com.example.design_vicent_sprint1.data.EdificiosAsinc;
import com.example.design_vicent_sprint1.Notificaciones;
import com.example.design_vicent_sprint1.PanelPrincipalEdificio;
import com.example.design_vicent_sprint1.Puertas;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.model.EdificioMenuAdapter;
import com.example.design_vicent_sprint1.model.EdificiosFirestoreAdapter;
import com.example.design_vicent_sprint1.model.Edificio;
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

        //recuperar cuenta del usuario
        Bundle extras = getIntent().getExtras();
        userId = extras.getString("userId");

        lista_edificios = new Edificios();

        //Header
        Toolbar toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        btnEdificios = findViewById(R.id.edificio);//Boton selector edificio
        btnMenu = findViewById(R.id.menu);//Boton menu

        contenedor_vista = findViewById(R.id.vista);

        actualizarListaEdificiosRoles(() -> {
            if (!lista_edificios_y_roles.isEmpty()) {
                Map.Entry<String, String> primerEdificio = lista_edificios_y_roles.entrySet().iterator().next();
                //seleccionar un edificio por defecto
                id_edificioSeleccionado = primerEdificio.getKey();
                cargarDatosBotonEdificio(id_edificioSeleccionado);
                cargarPantalla(id_edificioSeleccionado);
            }
        });

        //adapter = ((Aplicacion) getApplicationContext()).adapter;
        //edificios = ((Aplicacion) getApplicationContext()).edificios;

        btnEdificios.setOnClickListener(view -> mostrarPopupEdificios(view));
        //adapter.startListening();
    }

    private void cargarDatosBotonEdificio(String edificioSeleccionado){

        DocumentReference datos_edificio_seleccionado = FirebaseFirestore.getInstance()
                .collection("edificios").document(edificioSeleccionado);

        datos_edificio_seleccionado.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {

                //rellenar el boton selector de edificio con edificio seleccionado por defecto
                String nombre_edificio_seleccionado = task.getResult().getString("nombre");
                String calle_edificio_seleccionado = task.getResult().getString("calle");
                String ciudad_edificio_seleccionado = task.getResult().getString("ciudad");

                String texto_boton = nombre_edificio_seleccionado.toUpperCase()+"\n"+
                        calle_edificio_seleccionado+"\n"+ciudad_edificio_seleccionado;

                btnEdificios.setText(texto_boton);

            } else {
                Log.e("Firestore", "Error o colección vacía", task.getException());
            }
        });
    }

    private void mostrarPopupEdificios(View view) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_selector_edificios, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        actualizarListaEdificiosRoles(() -> {
            Set<String> lista_id_edificios = lista_edificios_y_roles.keySet();

            CollectionReference edificios = FirebaseFirestore.getInstance().collection("edificios");
            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

            for (String id : lista_id_edificios) {
                Task<DocumentSnapshot> task = edificios.document(id).get();
                tasks.add(task);
                task.addOnCompleteListener(t -> {
                    if (t.isSuccessful() && t.getResult() != null) {
                        DocumentSnapshot documentSnapshot = t.getResult();
                        Edificio edificio = documentSnapshot.toObject(Edificio.class);
                        lista_edificios.cargarEdificio(edificio);
                    } else {
                        Log.e("FirestoreError", "Error al obtener el documento con ID: " + id, t.getException());
                    }
                });
            }

            Tasks.whenAllComplete(tasks).addOnCompleteListener(task -> {
                RecyclerView recyclerView = popupView.findViewById(R.id.recyclerViewEdificios);
                // Configurar LayoutManager para scroll horizontal
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                // Cambiar a EdificioMenuAdapter
                EdificioMenuAdapter adapter = new EdificioMenuAdapter(lista_edificios.getEdificios(), edificio -> {
                    if (edificio.getNombre().equals("add")) {
                        popupWindow.dismiss();
                        mostrarPopupAddEdificio(view);
                    } else {
                        id_edificioSeleccionado = edificio.getId();
                        String texto = edificio.getNombre().toUpperCase() + "\n" +
                                edificio.getCalle() + "\n" + edificio.getCiudad();
                        btnEdificios.setText(texto);
                        popupWindow.dismiss();
                        cargarPantalla(id_edificioSeleccionado);
                    }
                });
                recyclerView.setAdapter(adapter);
                popupWindow.showAsDropDown(view, 0, 0);
            });
        });

    }

    private void cargarPantalla(String id_edificioSeleccionado){
        pagerAdapter = new MiPagerAdapter(this, id_edificioSeleccionado);
        contenedor_vista.setAdapter(pagerAdapter);

        TabLayout barra_herramientas = findViewById(R.id.barra_de_herramientas);
        new TabLayoutMediator(barra_herramientas, contenedor_vista, (tab, position) -> {
            tab.setIcon(iconos[position]);
        }).attach();

        String rol = lista_edificios_y_roles.get(id_edificioSeleccionado);
        btnMenu.setOnClickListener(btnMenu -> mostrarMenu(btnMenu, rol));
    }

    private void mostrarMenu(View view, String rol) {
        PopupMenu popup = new PopupMenu(this, view);
        if(rol.equals("vecino")) {
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
        }
        if(rol.equals("admin")){
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
        }
        popup.show();
    }

    private void lanzarActividad(Class<?> actividad) {
        Intent intent = new Intent(MainActivity.this, actividad);
        if (id_edificioSeleccionado != null) {
            intent.putExtra("edificio", id_edificioSeleccionado);
            String rol = lista_edificios_y_roles.get(id_edificioSeleccionado);
            intent.putExtra("rol", rol);
        }
        startActivity(intent);
    }

    private void mostrarPopupAddEdificio(View view) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_add_edificio, null);
        // Get screen dimensions
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = (int) (size.x * 0.9); // 90% of screen width
        int height = WindowManager.LayoutParams.WRAP_CONTENT; //
        PopupWindow popupWindowAdd = new PopupWindow(popupView, width, height, true);

        EditText idEdificio = popupView.findViewById(R.id.idEdificio);
        Button btnAdd = popupView.findViewById(R.id.btnAddEdificio);
        TextView mensaje = popupView.findViewById(R.id.mensaje);

        btnAdd.setOnClickListener(v -> {
            String edificio_nuevo = idEdificio.getText().toString();

            //Vincularse a nuevo edificio con permiso
            DocumentReference edificio_por_vincular =  FirebaseFirestore.getInstance()
                    .collection("usuarios").document(userId).collection("edificios").document(edificio_nuevo);
            edificio_por_vincular.get().addOnCompleteListener(task -> {
                if(task.isSuccessful() && task.getResult().exists()){
                    String nombre_edificio_seleccionado = task.getResult().getString("nombre");
                    String calle_edificio_seleccionado = task.getResult().getString("calle");
                    String ciudad_edificio_seleccionado = task.getResult().getString("ciudad");
                    String texto_boton = nombre_edificio_seleccionado.toUpperCase()+"\n"+
                            calle_edificio_seleccionado+"\n"+ciudad_edificio_seleccionado;
                    btnEdificios.setText(texto_boton);
                    CollectionReference edificios_con_permiso = FirebaseFirestore.getInstance()
                            .collection("usuarios").document(userId).collection("edificios");
                    edificios_con_permiso.get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            lista_edificios_y_roles = new HashMap<>();
                            for (QueryDocumentSnapshot document : task2.getResult()) {
                                lista_edificios_y_roles.put(document.getId(), document.getString("rol"));
                            }
                            cargarPantalla(edificio_nuevo);
                        }});
                    //comprobar que esta el edificio introducido
                    //no esta -> mensaje
                    //esta -> recargar lista de edificios y roles, seleccionar nuevo edificio y cargar pantalla con sus datos
                    popupWindowAdd.dismiss();
                }else{
                   // mensaje.setText("El código no es valido. Comprueba que sea correcto y que tienes permiso para acceder.");
                    mensaje.setText(" "+ userId + "   " + edificio_nuevo);
                }
            });
        });
        popupWindowAdd.setOutsideTouchable(true);
        popupWindowAdd.setFocusable(true);
        popupWindowAdd.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void actualizarListaEdificiosRoles(Runnable callback){
        CollectionReference edificios_con_permiso = FirebaseFirestore.getInstance()
                .collection("usuarios").document(userId).collection("edificios");
        edificios_con_permiso.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                lista_edificios_y_roles = new HashMap<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    lista_edificios_y_roles.put(document.getId(), document.getString("rol"));
                }
                callback.run();
            }else {
                Log.e("Firestore", "Error al leer usuarios-userId-edificios o userId sin edificios");
            }
        });
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
            String rol = lista_edificios_y_roles.get(id_edificioSeleccionado);
            args.putString("rol", rol);
            fragment.setArguments(args);

            return fragment;
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();// deja de escucha los cambios en la base de datos
    }*/
}



