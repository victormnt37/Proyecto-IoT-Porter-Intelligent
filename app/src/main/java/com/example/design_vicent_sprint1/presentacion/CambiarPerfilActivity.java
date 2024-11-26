package com.example.design_vicent_sprint1.presentacion;

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

import com.example.design_vicent_sprint1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CambiarPerfilActivity extends AppCompatActivity {

    private Button btnCambiarPerfil, btnCancelarCambiar;
    private TextInputLayout tilNombre, tilCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_perfil);

        EditText nombreUsuario = findViewById(R.id.nombreUsuario);
        EditText nuevoCorreo = findViewById(R.id.nuevoCorreo);

        tilNombre = (TextInputLayout) findViewById(R.id.tlNombre);
        tilCorreo = (TextInputLayout) findViewById(R.id.tlCorreo);

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

        btnCancelarCambiar = findViewById(R.id.cancelarCambiar);
        btnCancelarCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirigir a main
                Intent intent = new Intent(CambiarPerfilActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnCambiarPerfil = findViewById(R.id.cambiarPerfil);
        btnCambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean hayCambio = false;
                if (verificarCampos(nombreUsuario.getText().toString(), nuevoCorreo.getText().toString())) {
                    UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nombreUsuario.getText().toString())
                            .setPhotoUri(Uri.parse(String.valueOf(usuario.getPhotoUrl())))
                            .build();

                    if (!usuario.getDisplayName().equals(nombreUsuario.getText().toString())) {
                        usuario.updateProfile(perfil);
                        mensaje("Nombre de usuario actualizado");
                        hayCambio = true;
                    }

                    if (!usuario.getEmail().equals(nuevoCorreo.getText().toString())) {
                        usuario.updateEmail(nuevoCorreo.getText().toString());
                        mensaje("Correo electrónico actualizado " + usuario.getEmail() + " -> " + nuevoCorreo.getText().toString());
                        hayCambio = true;
                    }

                    if (hayCambio) {
                        // Acción exitosa, redirigir a MainActivity
                        Intent intent = new Intent(CambiarPerfilActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        mensaje("No has editado ningún campo");
                    }
                }
            }
        });
    }

    private boolean verificarCampos(String nombre, String correo) {
        tilCorreo.setError("");
        if (correo.isEmpty()) {
            tilCorreo.setError("Introduce un correo");
        } else if (!correo.matches(".+@.+[.].+")) {
            tilCorreo.setError("Correo no válido");
        } else {
            return true;
        }
        return false;
    }

    private void mensaje(String mensaje) {
        Snackbar.make((ViewGroup) findViewById(R.id.contenedor), mensaje, Snackbar.LENGTH_LONG).show();
    }
}
