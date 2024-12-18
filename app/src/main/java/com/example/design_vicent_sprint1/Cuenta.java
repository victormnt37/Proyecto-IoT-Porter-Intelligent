package com.example.design_vicent_sprint1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.design_vicent_sprint1.data.RepositorioEdificios;
import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.presentacion.CambiarPerfilActivity;
import com.example.design_vicent_sprint1.presentacion.CustomLoginActivity;
import com.example.design_vicent_sprint1.presentacion.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Cuenta extends Fragment {

    TextView TextViewNombre;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cuenta, container, false);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        TextViewNombre = view.findViewById(R.id.nombre);
        assert usuario != null;
        TextViewNombre.setText(usuario.getDisplayName());

        /*
         *SOLO USUARIOS REGISTRADOS CON GOOGLE O EMAIL PUEDEN CAMBIAR SUS DATOS
            PARA EVITAR  PROBLEMAS DE AUTENTIFICACION CON USUARIOS DE TWITTER
         */

        if(usuario.getEmail()!=null && usuario.getEmail()!=""){
            TextView correo = view.findViewById(R.id.correo);
            correo.setText(usuario.getEmail());
            Button btnCambiarPerfil = view.findViewById(R.id.btnCambiar);
            btnCambiarPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* Intent i = new Intent(requireContext(), CambiarPerfilActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);*/
                    mostrarPopupCambiarDatos(view);
                }
            });
        }else{
            TextView correo = view.findViewById(R.id.correo);
            correo.setVisibility(View.GONE);
            Button btnCambiarPerfil = view.findViewById(R.id.btnCambiar);
            btnCambiarPerfil.setVisibility(View.GONE);
        }

//        TextView telefono = findViewById(R.id.telefono);
//        telefono.setText(usuario.getPhoneNumber());

        // Inicialización Volley
        RequestQueue colaPeticiones = Volley.newRequestQueue(requireContext());
        ImageLoader lectorImagenes = new ImageLoader(colaPeticiones,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache =
                            new LruCache<String, Bitmap>(10);
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                });

        // Foto de usuario
        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
            NetworkImageView foto = (NetworkImageView) view.findViewById(R.id.imagen);
            foto.setImageUrl(urlImagen.toString(), lectorImagenes);
        } else {
            NetworkImageView foto = (NetworkImageView) view.findViewById(R.id.imagen);
            foto.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // Inicializar el botón para cerrar sesión
        Button btnCerrarSesion = view.findViewById(R.id.btn_cerrar_sesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion(v);
            }
        });

        return view;
    }

    private boolean verificarCorreo(String correo){
        if (correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return true;
        }
        return false;
    }

    private void mostrarPopupCambiarDatos(View view) {
        Dialog popupView = new Dialog(getContext());
        popupView.setContentView(R.layout.activity_cambiar_perfil);
        popupView.setCanceledOnTouchOutside(false); // No permite cancelar al tocar fuera

        // Obtener las vistas del popup
        EditText nombreUsuario = popupView.findViewById(R.id.nombreUsuario);
        EditText nuevoCorreo = popupView.findViewById(R.id.nuevoCorreo);
        TextInputLayout tilNombre = popupView.findViewById(R.id.tlNombre);
        TextInputLayout tilCorreo = popupView.findViewById(R.id.tlCorreo);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        String nombre = usuario.getDisplayName();
        String correo = usuario.getEmail();
        nombreUsuario.setText(nombre);
        nuevoCorreo.setText(correo);

        Button btnCancelarCambiar = popupView.findViewById(R.id.cancelarCambiar);
        btnCancelarCambiar.setOnClickListener(v -> popupView.dismiss());

        Button btnCambiarPerfil = popupView.findViewById(R.id.cambiarPerfil);
        btnCambiarPerfil.setOnClickListener(v -> {
            String nuevo_correo = nuevoCorreo.getText().toString();
            String nuevo_nombre = nombreUsuario.getText().toString();
            if (!verificarCorreo(nuevo_correo)) {
                tilCorreo.setError("Correo no válido");
            } else if (correo.equals(nuevo_correo) && nombre.equals(nuevo_nombre)) {
                tilNombre.setError("No has hecho ningún cambio");
            } else if (nuevo_correo.isEmpty()) {
                tilCorreo.setError("Introduce un correo");
            } else if (nuevo_nombre.isEmpty()) {
                tilNombre.setError("Introduce un nombre");
            } else {
                UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nuevo_nombre)
                        .setPhotoUri(Uri.parse(String.valueOf(usuario.getPhotoUrl())))
                        .build();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference usuarioRef = db.collection("usuarios").document(correo);

                if (!nombre.equals(nuevo_nombre)) {
                    usuario.updateProfile(perfil);
                    popupView.dismiss();
                    Toast toast = Toast.makeText(getContext(), "Nombre de usuario actualizado", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                if (!correo.equals(nuevo_correo)) {
                    // Aquí irían los pasos mencionados en el comentario
                    popupView.dismiss();
                }
            }
        });

        // Ocultar teclado al tocar fuera de los campos de texto
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

        // Configurar el estilo del popup
        if (popupView.getWindow() != null) {
            popupView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupView.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        popupView.show();
    }





    /*private void mostrarPopupCambiarDatos(View view) {
        Dialog popupView = new Dialog(getContext());
        popupView.setContentView(R.layout.activity_cambiar_perfil);
        popupView.setCanceledOnTouchOutside(false); // No permite cancelar al tocar fuera


        //View popupView = LayoutInflater.from(getContext()).inflate(R.layout.activity_cambiar_perfil, null);
        // PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        EditText nombreUsuario = popupView.findViewById(R.id.nombreUsuario);
        EditText nuevoCorreo = popupView.findViewById(R.id.nuevoCorreo);
        TextInputLayout tilNombre = (TextInputLayout) popupView.findViewById(R.id.tlNombre);
        TextInputLayout tilCorreo = (TextInputLayout) popupView.findViewById(R.id.tlCorreo);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        String nombre = usuario.getDisplayName();
        String correo = usuario.getEmail();
        nombreUsuario.setText(nombre);
        nuevoCorreo.setText(correo);

        Button btnCancelarCambiar = popupView.findViewById(R.id.cancelarCambiar);
        btnCancelarCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupView.dismiss();
            }
        });

        Button btnCambiarPerfil = popupView.findViewById(R.id.cambiarPerfil);
        btnCambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevo_correo = nuevoCorreo.getText().toString();
                String nuevo_nombre = nombreUsuario.getText().toString();
                if(!verificarCorreo(nuevo_correo)){
                    tilCorreo.setError("Correo no válido");
                }
                else if(correo.equals(nuevo_correo) && nombre.equals(nuevo_nombre)){
                    // popupWindow.dismiss(); //¿MENSAJE SI NO HACE CAMBIOS?
                    tilNombre.setError("No has hecho ningún cambio");
                }
                else if(nuevo_correo.isEmpty()){
                    tilCorreo.setError("Introduce un correo");
                }
                else if(nuevo_nombre.isEmpty()){
                    tilNombre.setError("Introduce un nombre");
                }
                else {
                    UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nuevo_nombre)
                            .setPhotoUri(Uri.parse(String.valueOf(usuario.getPhotoUrl())))
                            .build();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference usuarioRef = db.collection("usuarios").document(correo);

                    if (!nombre.equals(nuevo_nombre)) {
                        usuario.updateProfile(perfil);
                        TextViewNombre.setText(nuevo_nombre);
                        popupView.dismiss();
                        Toast toast = Toast.makeText(getContext(),"Nombre de usuario actualizado",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    if (!correo.equals(nuevo_correo)) {
                       /* usuario.updateEmail(nuevo_correo);
                        /*

                        !!!!!!!!!!!!!!!!!!! SPRINT 4 !!!!!!!!!!!!!!!!!!!!

                        * PROCESO INCOMPLETO -> NO SE ACTUALIZAN DATOS EN FIRESTORE, LA CUENTA QUEDARIA SIN EDIFICIOS
                        * 1- CREAR NUEVO DOCUMENTO CON CORREO ACTUALIZADO
                        * 2- COPIAR EN ESE DOCUMENTO LA COLECCION EDIFICIOS DEL CORREO INICIAL
                        * 3- BORRAR MANUALMENTE LA COLLECCION EDIFICIOS DEL CORREO INICIAL
                        * 4- BORRAR DOCUMENTO CORREO INICIAL
                        * 5- PASAR A MAIN ACTIVITY LOS NUEVOS DATOS DEL USUARIO PARA RECARGAR SUS EDIFICIOS
                        *

                        popupView.dismiss();
                        Toast toast = Toast.makeText(getContext(),"Correo electrónico actualizado " + correo + " -> " + nuevo_correo,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        popupView.dismiss();
                    }

                }
            }
        });

        popupView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupView.show();
//        popupWindow.setFocusable(true); // Habilitar interacción con los elementos internos
//        popupWindow.setOutsideTouchable(false);
//
//        // Mostrar el PopupWindow
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }*/




    public void cerrarSesion(View view) {
        AuthUI.getInstance().signOut(requireContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(
                                requireContext (), CustomLoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        requireActivity().finish();
                    }
                });
    }
}
