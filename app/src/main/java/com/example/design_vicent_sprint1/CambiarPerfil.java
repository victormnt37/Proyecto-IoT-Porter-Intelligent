package com.example.design_vicent_sprint1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CambiarPerfil extends AppCompatActivity {

    private Button btnCambiarPerfil;
    private TextInputLayout tilNombre, tilCorreo, tilContra, tilConfirmarContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_perfil);

        EditText nombreUsuario = findViewById(R.id.nombreUsuario);
        EditText nuevoCorreo = findViewById(R.id.nuevoCorreo);
        EditText nuevaContra = findViewById(R.id.nuevaContra);
        EditText confirmContra = findViewById(R.id.confirmarContra);

        tilNombre = (TextInputLayout) findViewById(R.id.tlNombre);
        tilCorreo = (TextInputLayout) findViewById(R.id.tlCorreo);
        tilContra = (TextInputLayout) findViewById(R.id.tlContra);
        tilConfirmarContra = (TextInputLayout) findViewById(R.id.tlConfirmContra);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        if (usuario == null) {
            Intent i = new Intent(this, CustomLoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        nombreUsuario.setText(usuario.getDisplayName());
        nuevoCorreo.setText(usuario.getEmail());

        btnCambiarPerfil = findViewById(R.id.cambiarPerfil);
        btnCambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos(nombreUsuario.getText().toString(), nuevoCorreo.getText().toString(), nuevaContra.getText().toString(), confirmContra.getText().toString())) {
                    UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nombreUsuario.getText().toString())
                            .setPhotoUri(Uri.parse(String.valueOf(usuario.getPhotoUrl())))
                            .build();

                    if (!usuario.getDisplayName().equals(nombreUsuario.getText().toString())) {
                        usuario.updateProfile(perfil);
                        mensaje("Nombre de usuario actualizado");
                    }

                    if (!usuario.getEmail().equals(nuevoCorreo.getText().toString())) {
                        usuario.updateEmail(nuevoCorreo.getText().toString());
                        mensaje("Correo electrónico actualizado " + usuario.getEmail() + " -> " + nuevoCorreo.getText().toString());
                    }

                    usuario.updatePassword(nuevaContra.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mensaje("Contraseña actualizada");
                                        // Acción exitosa, redirigir a MainActivity
                                        Intent intent = new Intent(CambiarPerfil.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); // Finaliza la actividad actual para que no vuelva con el botón "Atrás"
                                    } else if (!task.isSuccessful()) {
                                        Log.e("VicentPorterIntelligent", "Acción incorrecta");
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean verificarCampos(String nombre, String correo, String contra, String confirmContra) {
        tilCorreo.setError(""); tilContra.setError("");
        if (correo.isEmpty()) {
            tilCorreo.setError("Introduce un correo");
        } else if (!correo.matches(".+@.+[.].+")) {
            tilCorreo.setError("Correo no válido");
        } else if (contra.isEmpty()) {
            tilContra.setError("Introduce una contraseña");
        } else if (contra.length()<6) {
            tilContra.setError("Ha de contener al menos 6 caracteres");
        } else if (!contra.matches(".*[0-9].*")) {
            tilContra.setError("Ha de contener un número");
        } else if (!contra.matches(".*[A-Z].*")) {
            tilContra.setError("Ha de contener una letra mayúscula");
        } else if (!contra.equals(confirmContra)){
            tilConfirmarContra.setError("Escribe de nuevo la contraseña");
        } else {
            return true;
        }
        return false;
    }

    private void mensaje(String mensaje) {
        Snackbar.make((ViewGroup) findViewById(R.id.contenedor), mensaje, Snackbar.LENGTH_LONG).show();
    }
}
