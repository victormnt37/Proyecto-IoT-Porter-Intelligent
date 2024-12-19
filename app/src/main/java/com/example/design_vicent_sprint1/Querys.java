package com.example.design_vicent_sprint1;

import android.util.Log;

import com.example.design_vicent_sprint1.model.Contacto;
import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.Vecino;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Querys {

   /*private void obtenerEdificiosYRoles(String userId, OnCompleteListener<Map<String, String>> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference edificiosConPermiso = db.collection("usuarios").document(userId).collection("edificios");

        edificiosConPermiso.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Map<String, String> listaEdificiosYRoles = new HashMap<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    listaEdificiosYRoles.put(document.getId(), document.getString("rol"));
                }
                callback.onComplete(listaEdificiosYRoles);
            } else {
                Log.e("FirestoreError", "Error al leer edificios de usuario", task.getException());
            }
        });
    }

    private void obtenerAnunciosPorEdificio(String edificioId, OnCompleteListener<List<Anuncio>> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference anunciosRef = db.collection("edificios").document(edificioId).collection("anuncios");

        anunciosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    anuncios.add(doc.toObject(Anuncio.class));
                }
                callback.onComplete(anuncios);
            } else {
                Log.e("FirestoreError", "Error al obtener anuncios", task.getException());
            }
        });
    }




    private void obtenerContactosPorEdificio(String edificioId, OnCompleteListener<List<Contacto>> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contactosRef = db.collection("edificios").document(edificioId).collection("contactos");

        contactosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Contacto> contactos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    contactos.add(doc.toObject(Contacto.class));
                }
                callback.onComplete(contactos);
            } else {
                Log.e("FirestoreError", "Error al obtener contactos", task.getException());
            }
        });
    }

*/

}
