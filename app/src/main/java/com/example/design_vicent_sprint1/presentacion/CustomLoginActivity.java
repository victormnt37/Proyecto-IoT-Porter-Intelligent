package com.example.design_vicent_sprint1.presentacion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.design_vicent_sprint1.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/*
    DECIDIR SI CREAR ACTIVITY PARA USUARIOS SIN EDIFICIOS
*/

public class CustomLoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String correo = "";
    private String contraseña = "";
    private ViewGroup contenedor;
    private EditText etCorreo, etContraseña;
    private TextInputLayout tilCorreo, tilContraseña;
    private ProgressDialog dialogo;
    private ImageButton btnTwitter;
    private TextView recuperarContraseña;
    private static final int RC_GOOGLE_SIGN_IN = 123;
    GoogleSignInClient googleSignInClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);
        ActionBar appBar = getSupportActionBar();
        if (appBar != null) {
            appBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
        }
        etCorreo = (EditText) findViewById(R.id.correo);
        etContraseña = (EditText) findViewById(R.id.contraseña);
        tilCorreo = (TextInputLayout) findViewById(R.id.til_correo);
        tilContraseña = (TextInputLayout) findViewById(R.id.til_contraseña);
        contenedor = (ViewGroup) findViewById(R.id.contenedor);
        dialogo = new ProgressDialog(this);
        dialogo.setTitle("Verificando usuario");
        dialogo.setMessage("Por favor espere...");
        verificaSiUsuarioValidado();
        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        //Twitter
        btnTwitter = findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomLoginActivity.this, TwitterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // TODO: poner boton en activity_custom_login y descomentar cosas
        recuperarContraseña = findViewById(R.id.recuperarContraseña);
        recuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reestablecerContraseña(view);
            }
        });

    }

    private void verificaSiUsuarioValidado(ProgressDialog dialog) {

        if (auth.getCurrentUser() != null) {

            String cuenta_usuario = auth.getCurrentUser().getEmail();
           if(cuenta_usuario == null || cuenta_usuario.isEmpty()){
               cuenta_usuario = auth.getCurrentUser().getDisplayName(); ;
           }

           //comprobar si usuario autorizado (tiene edificios vinculados)
            DocumentReference usuario = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(cuenta_usuario);
            String cuenta = cuenta_usuario;
            usuario.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot userId = task.getResult();
                    if (userId.exists()) {
                        dialogo.dismiss();
                        Intent i = new Intent(this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("userId", cuenta);
                        startActivity(i);
                        finish();
                    } else {
                        dialogo.dismiss();
                        auth.signOut();
                        mensaje("No estás asociado a ningún edificio. Pide al administrador que permita acceso a tu cuenta.");
                    }
                } else {
                    Log.e("Firestore", "Error al obtener el documento", task.getException()); // task1, no task
                }
            });
        }
    }

    private void verificaSiUsuarioValidado() {

        if (auth.getCurrentUser() != null) {

            String cuenta_usuario = auth.getCurrentUser().getEmail();
            if(cuenta_usuario == null || cuenta_usuario.isEmpty()){
                cuenta_usuario = auth.getCurrentUser().getDisplayName(); ;
            }

            //combrobar si usuario autorizado (tiene edificios vinculados)
            DocumentReference usuario = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(cuenta_usuario);
            String cuenta = cuenta_usuario;
            usuario.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot userId = task.getResult();
                    if (userId.exists()) {
                        Intent i = new Intent(this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("userId", cuenta);
                        startActivity(i);
                        finish();
                    } else {
                        auth.signOut();
                        //TODO -> PASAR CURRENTUSER A NULL PARA PERMITIR ELEGIR CUENTA NUEVA
                        mensaje("No estás asociado a ningún edificio. Pide al administrador que permita acceso a tu cuenta.");
                    }
                } else {
                    Log.e("Firestore", "Error al obtener el documento", task.getException()); // task1, no task
                }
            });
        }
    }

    public void inicioSesiónCorreo(View v) {
        if (verificaCampos()) {
            dialogo.show();
            auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                verificaSiUsuarioValidado(dialogo);
                            } else {
                                dialogo.dismiss();
                                mensaje(task.getException().getLocalizedMessage());
                            }
                        }
                    });
        }
    }

    public void registroCorreo(View v) {
        if (verificaCampos()) {
            dialogo.show();
            auth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               verificaSiUsuarioValidado();
                            } else {
                                dialogo.dismiss();
                                mensaje(task.getException().getLocalizedMessage());
                            }
                        }
                    });
            }
    }

    private void mensaje(String mensaje) {
        Snackbar.make(contenedor, mensaje, Snackbar.LENGTH_LONG).show();
    }

    private boolean verificaCampos() {
        correo = etCorreo.getText().toString();
        contraseña = etContraseña.getText().toString();
        tilCorreo.setError(""); tilContraseña.setError("");
        if (correo.isEmpty()) {
            tilCorreo.setError("Introduce un correo");
        } else if (!correo.matches(".+@.+[.].+")) {
            tilCorreo.setError("Correo no válido");
        } else if (contraseña.isEmpty()) {
            tilContraseña.setError("Introduce una contraseña");
        } else if (contraseña.length()<6) {
            tilContraseña.setError("Ha de contener al menos 6 caracteres");
        } else if (!contraseña.matches(".*[0-9].*")) {
            tilContraseña.setError("Ha de contener un número");
        } else if (!contraseña.matches(".*[A-Z].*")) {
            tilContraseña.setError("Ha de contener una letra mayúscula");
        } else {
            return true;
        }
        return false;
    }

    //on click btn google en activity_custom_login
    public void autentificarGoogle(View v) {
        Intent i = googleSignInClient.getSignInIntent();
        startActivityForResult(i, RC_GOOGLE_SIGN_IN);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleAuth(account.getIdToken());
            } catch (ApiException e) {
                mensaje("Error de autentificación con Google");
            }
        }
    }
    private void googleAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verificaSiUsuarioValidado();

                        }else{
                            mensaje(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    public void reestablecerContraseña(View v) {
        correo = etCorreo.getText().toString();
        tilCorreo.setError("");
        if (correo.isEmpty()) {
            tilCorreo.setError("Introduce un correo");
        } else if (!correo.matches(".+@.+[.].+")) {
            tilCorreo.setError("Correo no válido");
        } else {
            dialogo.show();
            auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override public void onComplete(@NonNull Task<Void> task) {
                            dialogo.dismiss();
                            if (task.isSuccessful()) {
                                mensaje("Verifica tu correo para cambiar contraseña.");
                            } else {
                                mensaje("ERROR al mandar correo para cambiar contraseña");
                            }
                        }
                    });
        }
    }
}