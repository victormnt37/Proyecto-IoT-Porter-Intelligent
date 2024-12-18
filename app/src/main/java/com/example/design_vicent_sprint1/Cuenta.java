package com.example.design_vicent_sprint1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Cuenta extends Fragment {

    private Button btnEdificios;
    private RepositorioEdificios repositorioEdificios;
    private Edificio edificioSeleccionado;
    private ImageButton btnMenu;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cuenta, container, false);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        TextView nombre = view.findViewById(R.id.nombre);
        assert usuario != null;
        nombre.setText(usuario.getDisplayName());

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
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.activity_cambiar_perfil, null);
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

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
                popupWindow.dismiss();
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

                    if (!nombre.equals(nuevo_nombre)) {
                        usuario.updateProfile(perfil);
                        popupWindow.dismiss();
                        Toast toast = Toast.makeText(getContext(),"Nombre de usuario actualizado",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    if (!correo.equals(nuevo_correo)) {
                        usuario.updateEmail(nuevo_correo);
                        popupWindow.dismiss();
                        Toast toast = Toast.makeText(getContext(),"Correo electrónico actualizado " + correo + " -> " + nuevo_correo,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
            }
        });

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

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
