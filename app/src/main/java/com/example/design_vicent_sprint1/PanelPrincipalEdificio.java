package com.example.design_vicent_sprint1;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.design_vicent_sprint1.data.RepositorioPaneles;
import com.example.design_vicent_sprint1.model.Panel;
import com.example.design_vicent_sprint1.model.PanelAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PanelPrincipalEdificio extends Fragment {

    private RecyclerView recyclerView;
    private RepositorioPaneles repositorioPaneles;
    private String edificioSeleccionado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panel_principal_edificio, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPaneles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            edificioSeleccionado = getArguments().getString("edificioSeleccionado");
        }

        repositorioPaneles = new RepositorioPaneles();
        cargarPaneles();
        FloatingActionButton btn_emergente = view.findViewById(R.id.btn_emergente);
        btn_emergente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupAdd(v);
            }
        });
        return view;
    }

    private void mostrarPopupAdd(View view) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_add, null);
        PopupWindow popupWindowAdd = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        Button btnAdd = popupView.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v -> {

            //********************* PROCESO AÑADIR EDIFICIO

            popupWindowAdd.dismiss();
        });

        popupWindowAdd.setOutsideTouchable(true);
        popupWindowAdd.setFocusable(true);
        popupWindowAdd.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void cargarPaneles() {
        List<Panel> paneles = repositorioPaneles.getPanelesPorEdificio(edificioSeleccionado);
        PanelAdapter adapter = new PanelAdapter(paneles);
        recyclerView.setAdapter(adapter);
    }
}

