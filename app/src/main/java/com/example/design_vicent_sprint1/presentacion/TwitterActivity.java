package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.design_vicent_sprint1.Cuenta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

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
                                    if (authResult.getAdditionalUserInfo().isNewUser()) {

                                    }
                                    String cuenta_usuario = authResult.getUser().getDisplayName();
                                    Intent i = new Intent(TwitterActivity.this, MainActivity.class);
                                    i.putExtra("userId", cuenta_usuario);
                                    startActivity(i);
                                    Toast.makeText(TwitterActivity.this, "Login Succesfull", Toast.LENGTH_LONG).show();
                                    finish();
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
                                    if (authResult.getAdditionalUserInfo().isNewUser()) {

                                    }
                                    String cuenta_usuario = authResult.getUser().getDisplayName();
                                    Intent i = new Intent(TwitterActivity.this, MainActivity.class);
                                    i.putExtra("userId", cuenta_usuario);
                                    startActivity(i);
                                    Toast.makeText(TwitterActivity.this, "Login Succesfull", Toast.LENGTH_LONG).show();
                                    finish();
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
}
