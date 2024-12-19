package com.example.design_vicent_sprint1.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.design_vicent_sprint1.model.Edificio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EdificiosFirestore implements EdificiosAsinc{

    private CollectionReference edificios;

    public EdificiosFirestore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        edificios = db.collection("edificios");
    }

    public void elemento(String id, final EdificiosAsinc.EscuchadorElemento escuchador){
        edificios.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Edificio edificio = task.getResult().toObject(Edificio.class);
                    escuchador.onRespuesta(edificio);
                } else {
                    Log.e("Firebase","Error al leer edificios", task.getException());
                    escuchador.onRespuesta(null);
                }
            }
        });
    }

    public void anyade(Edificio edificio) {
        edificios.document().set(edificio); //o .add()
    }

    public String nuevo() {
        return edificios.document().getId();
    }

    public void borrar(String id) {
        edificios.document(id).delete();
    }

    public void actualiza(String id, Edificio edificio) {
        edificios.document(id).set(edificio);
    }

    public void tamaño(final EscuchadorTamaño escuchador) {
        edificios.get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    escuchador.onRespuesta(task.getResult().size());
                } else {
                    Log.e("Firebase","Error en tamaño",task.getException());
                    escuchador.onRespuesta(-1);
                }
            }
        });
    }
}
