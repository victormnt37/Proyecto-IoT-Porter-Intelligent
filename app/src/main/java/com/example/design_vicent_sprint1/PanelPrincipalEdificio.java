package com.example.design_vicent_sprint1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.design_vicent_sprint1.data.RepositorioPaneles;
import com.example.design_vicent_sprint1.model.Anuncio;
import com.example.design_vicent_sprint1.model.Panel;
import com.example.design_vicent_sprint1.model.PanelAdapter;
import com.example.design_vicent_sprint1.model.SensorData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelPrincipalEdificio extends Fragment implements MqttCallback {

    private RecyclerView recyclerView;
    private RepositorioPaneles repositorioPaneles;
    private String edificioSeleccionado;
    private String rol;
    private String userId;
    private PanelAdapter adapter;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    // MQTT
    private static final String topic = "VicentPI/edificio1";
    private static final int qos = 1;
    private static final boolean retain = false;
    private static final String broker = "tcp://test.mosquitto.org:1883";
    private static final String clientId = "vecino";
    private MqttClient client;
    private SensorData datosSensor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panel_principal_edificio, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPaneles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getString("edificioSeleccionado");
            rol = getArguments().getString("rol");
            userId = getArguments().getString("userId");
        }

        try {
            client = new MqttClient(broker, clientId, new MemoryPersistence()); // Conexión con el bróker
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topic, "Desconectada!".getBytes(), qos, retain);
            client.connect(connOpts);
            client.setCallback(this); // Callback

            // Suscripción al tópico
            client.subscribe(topic, qos);
            Log.i("MQTT", "Suscripción al tópico: " + topic);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar con el bróker: " + e.getMessage());
        }

        repositorioPaneles = new RepositorioPaneles();
        datosSensor = new SensorData();
        cargarPaneles(datosSensor);

        FloatingActionButton btn_emergente = view.findViewById(R.id.btn_emergente);
        if(rol.equals("admin")){
            btn_emergente.setImageResource(R.drawable.icon_addfondo);
            btn_emergente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarPopupAdd(v);
                }
            });
        }
        if(rol.equals("vecino")){
            btn_emergente.setImageResource(R.drawable.icon_alerta);
            btn_emergente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarPopupAlerta(v);
                }
            });
        }
        return view;
    }

    private void mostrarPopupAlerta(View view) {
        Dialog popupViewAlert = new Dialog(getContext());
        popupViewAlert.setContentView(R.layout.popup_alerta);
        popupViewAlert.setCanceledOnTouchOutside(true);
//        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_alerta, null);
//        PopupWindow popupWindowAdd = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        Button btnAdd = popupViewAlert.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v -> {

            //********************* PROCESO AÑADIR EDIFICIO

            popupViewAlert.dismiss();
        });
        popupViewAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupViewAlert.show();

    }

    private void mostrarPopupAdd(View view) {
        Dialog popupViewAdd = new Dialog(getContext());
        popupViewAdd.setContentView(R.layout.popup_add);
        popupViewAdd.setCanceledOnTouchOutside(true);
//        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_add, null);
//        PopupWindow popupWindowAdd = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        Button btnVecino = popupViewAdd.findViewById(R.id.btnVecino); // Cambié el ID por 'btnVecino'
        btnVecino.setOnClickListener(v -> {
                // Mostrar el segundo pop-up
                mostrarPopupVecino();

                // Cerrar el primer pop-up
                popupViewAdd.dismiss();
        });

        Button btnAdmin = popupViewAdd.findViewById(R.id.btnAdmin);
        btnAdmin.setOnClickListener(v -> {
                mostrarPopupAdmin();
                popupViewAdd.dismiss();

        });

        Button btnAnuncios = popupViewAdd.findViewById(R.id.btnAnuncios);
        btnAnuncios.setOnClickListener(v -> {
                mostrarPopupAnuncio();
                popupViewAdd.dismiss();
        });

        Button btnContactos = popupViewAdd.findViewById(R.id.btnContactos);
        btnContactos.setOnClickListener(v -> {
                mostrarPopupContacto();
                popupViewAdd.dismiss();
        });
        // Mostrar el primer pop-up
        popupViewAdd.show();

        popupViewAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupViewAdd.show();
    }
    private void mostrarPopupVecino() {

        Dialog popupVecinos = new Dialog(getContext());
        popupVecinos.setContentView(R.layout.popup_anyadir_vecino);
        popupVecinos.setCanceledOnTouchOutside(true);

        // Hacer el fondo del segundo pop-up transparente
        popupVecinos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputLayout tilCorreo = popupVecinos.findViewById(R.id.tlCorreo);
        EditText correo = popupVecinos.findViewById(R.id.etCorreo);
        Spinner piso = popupVecinos.findViewById(R.id.spinnerPiso);
        Spinner puerta = popupVecinos.findViewById(R.id.spinnerPuerta);
        List<String> pisosList = new ArrayList<>();
        Map<String, List<String>> puertasMap = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("edificios").document(edificioSeleccionado).collection("pisos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String piso = document.getId();
                                pisosList.add(piso);

                                List<String> puertas = (List<String>) document.get("puertas");
                                if (puertas != null) {
                                    puertasMap.put(piso, puertas);
                                }
                            }
                            ArrayAdapter<String> pisosAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pisosList);
                            pisosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            piso.setAdapter(pisosAdapter);
                            piso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String pisoSeleccionado = pisosList.get(position);
                                    List<String> puertas = puertasMap.get(pisoSeleccionado);

                                    if (puertas != null) {
                                        ArrayAdapter<String> puertasAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, puertas);
                                        puertasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        puerta.setAdapter(puertasAdapter);
                                    } else {
                                        puerta.setAdapter(null); // Vaciar el spinner si no hay puertas
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // No hacer nada
                                }
                            });
                        } else {
                            Log.e("Firestore", "Error al cargar datos");
                        }
                    }
                });

        Button btnAdd = popupVecinos.findViewById(R.id.btnAddVecino3);
        btnAdd.setOnClickListener(view -> {
            String correo_i = correo.getText().toString();
            String puerta_i = puerta.getSelectedItem().toString();
            String piso_i = piso.getSelectedItem().toString();

            if(!verificarCorreo(correo_i)){
                tilCorreo.setError("Correo no válido");
            } else{
                DocumentReference usuarioRef = db.collection("usuarios").document(correo_i);

                usuarioRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        usuarioRef.set(new HashMap<>())
                                .addOnSuccessListener(aVoid -> {
                                    usuarioRef.collection("edificios").document(edificioSeleccionado)
                                            .set(new HashMap<String, Object>() {{
                                                put("rol", "vecino");
                                            }});
                                });

                    } else {
                        usuarioRef.collection("edificios").document(edificioSeleccionado)
                                .set(new HashMap<String, Object>() {{
                                    put("rol", "vecino");
                                }});

                    }
                    DocumentReference edificioRef = db.collection("edificios").document(edificioSeleccionado);

                    edificioRef.get().addOnSuccessListener(documentSnapshot2 -> {
                        edificioRef.collection("vecinos").document(correo_i)
                                .set(new HashMap<String, Object>() {{
                                    put("piso", piso_i);
                                    put("puerta", puerta_i);
                                }});
                    });
                    popupVecinos.dismiss();
                    Toast toast = Toast.makeText(getContext(), "Vecino añadido", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
            }
        });
        configurarTeclado(popupVecinos);
        // Mostrar el segundo pop-up
        popupVecinos.show();
    }
    private void mostrarPopupAdmin() {

        // Crear el segundo pop-up (Dialog)
        Dialog popupAdmin = new Dialog(getContext());
        popupAdmin.setContentView(R.layout.popup_admin);  // Asegúrate de que este layout existe
        popupAdmin.setCanceledOnTouchOutside(true);
        TextInputLayout tilNombre = popupAdmin.findViewById(R.id.tlNombre);
        TextInputLayout tilCorreo = popupAdmin.findViewById(R.id.tlCorreo);
        TextInputLayout tilTelefono = popupAdmin.findViewById(R.id.tlTelefono);

        EditText nombre = popupAdmin.findViewById(R.id.editText6);
        EditText correo = popupAdmin.findViewById(R.id.editText5);
        EditText telefono = popupAdmin.findViewById(R.id.editText7);

        // Hacer el fondo del segundo pop-up transparente
        popupAdmin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnAdd = popupAdmin.findViewById(R.id.btnAddVecino);
        btnAdd.setOnClickListener(view -> {
            String correo_i = correo.getText().toString();
            String nombre_i = nombre.getText().toString();
            String telefono_i = telefono.getText().toString();

            if(!verificarCorreo(correo_i)){
                tilCorreo.setError("Correo no válido");
            }else if(!verificarTelefono(telefono_i)){
                tilTelefono.setError("Telefono no válido");
            } else if (nombre_i.isEmpty()) {
                tilNombre.setError("Introduce un nombre");
            }else{
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference usuarioRef = db.collection("usuarios").document(correo_i);

                usuarioRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        usuarioRef.set(new HashMap<>())
                                .addOnSuccessListener(aVoid -> {
                                    usuarioRef.collection("edificios").document(edificioSeleccionado)
                                            .set(new HashMap<String, Object>() {{
                                                put("rol", "admin");
                                            }});
                                });

                    } else {
                        usuarioRef.collection("edificios").document(edificioSeleccionado)
                                .set(new HashMap<String, Object>() {{
                                    put("rol", "admin");
                                }});

                    }
                    DocumentReference edificioRef = db.collection("edificios").document(edificioSeleccionado);

                    edificioRef.get().addOnSuccessListener(documentSnapshot2 -> {
                        edificioRef.collection("administradores").document(correo_i)
                                .set(new HashMap<String, Object>() {{
                                    put("nombre", nombre_i);
                                    put("telefono", telefono_i);
                                }});
                    });
                    popupAdmin.dismiss();
                    Toast toast = Toast.makeText(getContext(), "Administrador añadido", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
            }
        });
        configurarTeclado(popupAdmin);
        // Mostrar el segundo pop-up
        popupAdmin.show();
    }
    private void mostrarPopupAnuncio() {

        // Crear el segundo pop-up (Dialog)
        Dialog popupAnuncios = new Dialog(getContext());
        popupAnuncios.setContentView(R.layout.popup_anuncio);  // Asegúrate de que este layout existe
        popupAnuncios.setCanceledOnTouchOutside(true);

        // Hacer el fondo del segundo pop-up transparente
        popupAnuncios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText asunto = popupAnuncios.findViewById(R.id.editText6);
        EditText texto = popupAnuncios.findViewById(R.id.editText5);
        TextInputLayout tilAsunto = popupAnuncios.findViewById(R.id.tlAsunto);
        TextInputLayout tilTexto = popupAnuncios.findViewById(R.id.tlTexto);

        Button btnAdd = popupAnuncios.findViewById(R.id.btnAddVecino);
        btnAdd.setOnClickListener(view -> {
            String asunto_i = asunto.getText().toString();
            String texto_i = texto.getText().toString();

            if(asunto_i.isEmpty()){
                tilAsunto.setError("Introduce un titulo");
            }else if(texto_i.isEmpty()){
                tilTexto.setError("Escribe una descripción");
            }else{
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("edificios").document(edificioSeleccionado) // Documento del edificio
                        .collection("administradores").document(userId) // Documento del administrador
                        .get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String autor = documentSnapshot.getString("nombre");
                                if (autor != null) {
                                    Anuncio anuncio = new Anuncio(asunto_i, texto_i, autor);
                                    String fecha = anuncio.getFecha();
                                    DocumentReference edificioRef = db.collection("edificios").document(edificioSeleccionado);
                                    edificioRef.get().addOnSuccessListener(documentSnapshot2 -> {
                                        edificioRef.collection("anuncios").document()
                                                .set(new HashMap<String, Object>() {{
                                                    put("asunto", asunto_i);
                                                    put("texto", texto_i);
                                                    put("fecha", fecha);
                                                    put("autor", autor);
                                                }});
                                        popupAnuncios.dismiss();
                                        Toast toast = Toast.makeText(getContext(), "Anuncio añadido", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    });
                                } else {
                                    Log.d("Firestore", "El campo 'nombre' no existe en el documento.");
                                }
                            } else {
                                Log.d("Firestore", "No se encontró el documento del administrador.");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Error al buscar el administrador: ", e);
                        });
            }
        });

        configurarTeclado(popupAnuncios);

        // Mostrar el segundo pop-up
        popupAnuncios.show();
    }
    private void mostrarPopupContacto() {

        // Crear el segundo pop-up (Dialog)
        Dialog popupContacto = new Dialog(getContext());
        popupContacto.setContentView(R.layout.popup_contacto);  // Asegúrate de que este layout existe
        popupContacto.setCanceledOnTouchOutside(true);

        // Hacer el fondo del segundo pop-up transparente
        popupContacto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputLayout tilNombre = popupContacto.findViewById(R.id.tlNombre);
        TextInputLayout tilTelefono = popupContacto.findViewById(R.id.tlTelefono);
        EditText nombre = popupContacto.findViewById(R.id.editText6);
        EditText telefono = popupContacto.findViewById(R.id.editText7);

        Button btnAdd = popupContacto.findViewById(R.id.btnAddVecino);
        btnAdd.setOnClickListener(view -> {
            String nombre_i = nombre.getText().toString();
            String telefono_i = telefono.getText().toString();

            if(!verificarTelefono(telefono_i)){
                tilTelefono.setError("Teléfono no válido");
            }else if (nombre_i.isEmpty()) {
                tilNombre.setError("Introduce un nombre");
            }else{
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference edificioRef = db.collection("edificios").document(edificioSeleccionado);
                edificioRef.get().addOnSuccessListener(documentSnapshot -> {
                    edificioRef.collection("contactos").document()
                            .set(new HashMap<String, Object>() {{
                                put("nombre", nombre_i);
                                put("telefono", telefono_i);
                            }});
                    popupContacto.dismiss();
                    Toast toast = Toast.makeText(getContext(), "Contacto añadido", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
            }
        });

        configurarTeclado(popupContacto);
        // Mostrar el segundo pop-up
        popupContacto.show();
    }

    private boolean verificarCorreo(String correo){
        if (correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return true;
        }
        return false;
    }

    private boolean verificarTelefono(String telefono) {
        if (telefono.matches("^\\d{9}$")) {
            return true;
        }
        return false;
    }

    private void cargarPaneles(SensorData datosSensor) {
        List<Panel> paneles = repositorioPaneles.getPanelesPorEdificio(edificioSeleccionado);

        if (adapter == null) {
            adapter = new PanelAdapter(paneles, edificioSeleccionado, datosSensor, getContext());
            recyclerView.setAdapter(adapter);
        } else {
            adapter.llenarDatosMQTT(datosSensor, recyclerView);
        }
    }

    // Funciones MQTT
    @Override
    public void connectionLost(Throwable cause) {
        Log.i("MQTT", "Conexión perdida: " + cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        Log.i("MQTT", "Mensaje recibido del tópico [" + topic + "]: " + payload);

        datosSensor.fromJson(payload);
        Log.i("MQTT", datosSensor.toString());

        // Ejecutar cargarPaneles en el hilo principal
        cargarPaneles(datosSensor);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i("MQTT", "Entrega completa!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                Log.i("MQTT", "Cliente desconectado");
            }
        } catch (MqttException e) {
            Log.e("MQTT", "Error al desconectar el cliente: " + e.getMessage());
        }
    }

    private void configurarTeclado(Dialog popupView){
        ConstraintLayout rootLayout = popupView.findViewById(R.id.contenedor); // Asegúrate de que este ID sea el correcto
        rootLayout.setOnTouchListener((v, event) -> {
            View currentFocus = popupView.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
            }
            return false; // Devuelve false para que otros eventos de toque se procesen normalmente
        });
    }
}

