package com.example.design_vicent_sprint1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.design_vicent_sprint1.data.RepositorioPaneles;
import com.example.design_vicent_sprint1.model.Panel;
import com.example.design_vicent_sprint1.model.PanelAdapter;
import com.example.design_vicent_sprint1.model.SensorData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

public class PanelPrincipalEdificio extends Fragment implements MqttCallback {

    private RecyclerView recyclerView;
    private RepositorioPaneles repositorioPaneles;
    private String edificioSeleccionado;
    private String rol;
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
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_alerta, null);
        PopupWindow popupWindowAdd = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        Button btnAdd = popupView.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v -> {

            //********************* PROCESO AÑADIR EDIFICIO

            popupWindowAdd.dismiss();
        });

        popupWindowAdd.setOutsideTouchable(true);
        popupWindowAdd.setFocusable(true);
        popupWindowAdd.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void mostrarPopupAdd(View view) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_add, null);
        PopupWindow popupWindowAdd = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

//        Button btnAdd = popupView.findViewById(R.id.btnAdd);
//
//        btnAdd.setOnClickListener(v -> {
//
//            //********************* PROCESO AÑADIR EDIFICIO
//
//            popupWindowAdd.dismiss();
//        });

        popupWindowAdd.setOutsideTouchable(true);
        popupWindowAdd.setFocusable(true);
        popupWindowAdd.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void cargarPaneles(SensorData datosSensor) {
        List<Panel> paneles = repositorioPaneles.getPanelesPorEdificio(edificioSeleccionado);

        if (adapter == null) {
            adapter = new PanelAdapter(paneles, edificioSeleccionado, datosSensor);
            recyclerView.setAdapter(adapter);
        } else {
            Log.i("Cargar paneles", datosSensor.toString());
            adapter.llenarDatosMQTT(datosSensor);
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
}

