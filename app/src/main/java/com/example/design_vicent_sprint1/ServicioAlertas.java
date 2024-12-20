package com.example.design_vicent_sprint1;

import static android.content.Intent.getIntent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.design_vicent_sprint1.presentacion.CustomLoginActivity;
import com.example.design_vicent_sprint1.presentacion.MainActivity;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.Set;

public class ServicioAlertas extends Service {

    private FirebaseFirestore db;
    private NotificationManager notificationManager;
    private static final String CANAL_ID = "mi_canal";
    private static final int NOTIFICACION_ID = 1;
    private Set<String> alertasExistentes = new HashSet<>();
    private String idEdificioSeleccionado;

    @Override public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CANAL_ID,
                    "Canal de Alertas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de alertas");
            notificationManager.createNotificationChannel(channel);
        }

        /*notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
        notificacion.setContentIntent(intencionPendiente);

        notificationManager.notify(NOTIFICACION_ID, notificacion.build());*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranque) {
        idEdificioSeleccionado = intent.getStringExtra("edificio");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CANAL_ID)
                .setContentTitle("Bienvenido a tu edificio")
                .setContentText("")
                .setSmallIcon(R.drawable.icon_vicent)
                .setSilent(true);
        PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0,
                new Intent(this, CustomLoginActivity.class), PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(intencionPendiente);
        startForeground(NOTIFICACION_ID, builder.build());
        observarAlertas();
        return START_STICKY;
    }

    private void observarAlertas() {
        db.collection("edificios")
                .document(idEdificioSeleccionado)
                .collection("notificaciones")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(ServicioAlertas.this, "Error al escuchar alertas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            for (QueryDocumentSnapshot doc : snapshots) {
                                String idAlerta = doc.getId();
                                if (!alertasExistentes.contains(idAlerta)) {
                                    // Nueva alerta
                                    alertasExistentes.add(idAlerta);
                                    String mensaje = doc.getString("mensaje");
                                    generarNotificacion(mensaje != null ? mensaje : "Nueva alerta Vicent");
                                }
                            }
                        }
                    }
                });
    }

    private void generarNotificacion(String mensaje) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CANAL_ID)
                .setContentTitle("Nueva Alerta")
                .setContentText(mensaje)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(intencionPendiente);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override public void onDestroy() {
    }

    @Override public IBinder onBind(Intent intencion) {
        return null;
    }
}
