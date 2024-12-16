package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.design_vicent_sprint1.Cuenta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TwitterActivity extends CustomLoginActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

        // Localize to ESPAÑOLO
        provider.addCustomParameter("lang", "es");

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                        String username = authResult.getUser().getDisplayName();
                                        //combrobar si usuario autorizado (tiene edificios vinculados)
                                        DocumentReference usuario = FirebaseFirestore.getInstance()
                                                .collection("usuarios").document(username);
                                        usuario.get().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot userId = task1.getResult();
                                                if (userId.exists()) {
                                                   lanzarMainActivity(username);
                                                } else {
                                                    Log.e("Usuario no autorizado", "Pide al administrador que conceda permiso a tu cuenta.");
                                                }
                                            } else {
                                                Log.e("Firestore", "Error al obtener el documento", task1.getException()); // task1, no task
                                            }
                                        });
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TwitterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
        } else {
            firebaseAuth
                    .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                 String username = authResult.getUser().getDisplayName();
                                        //combrobar si usuario autorizado (tiene edificios vinculados)
                                        DocumentReference usuario = FirebaseFirestore.getInstance()
                                                .collection("usuarios").document(username);
                                        usuario.get().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot userId = task1.getResult();
                                                if (userId.exists()) {
                                                    lanzarMainActivity(username);
                                                } else {
                                                    Log.e("Usuario no autorizado", "Pide al administrador que conceda permiso a tu cuenta.");
                                                }
                                            } else {
                                                Log.e("Firestore", "Error al obtener el documento", task1.getException()); // task1, no task
                                            }
                                        });
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TwitterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
        }
    }

    private void lanzarVincularEdificioActivity(String username) {
        Intent i = new Intent(TwitterActivity.this, MainActivity.class);
        i.putExtra("userId", username);
        startActivity(i);
        Toast.makeText(TwitterActivity.this, "Login Succesfull", Toast.LENGTH_LONG).show();
        finish();
    }

    private void lanzarMainActivity(String username) {
        Intent i = new Intent(TwitterActivity.this, MainActivity.class);
        i.putExtra("userId", username);
        startActivity(i);
        Toast.makeText(TwitterActivity.this, "Login Succesfull", Toast.LENGTH_LONG).show();
        finish();
    }

}
