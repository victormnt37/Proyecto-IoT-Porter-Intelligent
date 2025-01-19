    package com.example.design_vicent_sprint1.model;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.design_vicent_sprint1.R;

    import java.util.List;

    public class EdificiosAdapter extends RecyclerView.Adapter<EdificiosAdapter.ViewHolder> {

        private List<Edificio> edificios;
        private OnItemClickListener listener;

        // Interfaz para manejar clics en elementos
        public interface OnItemClickListener {
            void onItemClick(Edificio edificio); // Clic en el item
            void onDeleteClick(int position, Edificio edificio);   // Clic en el botón de eliminar
        }

        public EdificiosAdapter(List<Edificio> edificios, OnItemClickListener listener) {
            this.edificios = edificios;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_edificios, parent, false); // Usamos el nuevo diseño personalizado
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Edificio edificio = edificios.get(position);

            // Asignar datos al item
            holder.nombreEdificio.setText(edificio.getNombre());
            holder.ciudadEdificio.setText(edificio.getCiudad());
            holder.calleEdificio.setText(edificio.getCalle());

            // Manejar clic en el item completo
            holder.itemView.setOnClickListener(v -> listener.onItemClick(edificio));

            // Manejar clic en el botón de eliminar
            holder.iconoEliminar.setOnClickListener(v -> listener.onDeleteClick(position, edificio));
        }

        @Override
        public int getItemCount() {
            return edificios.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView nombreEdificio, calleEdificio, ciudadEdificio;
            ImageView iconoEliminar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nombreEdificio = itemView.findViewById(R.id.nombreEdificio);
                ciudadEdificio = itemView.findViewById(R.id.ciudadEdificio);
                calleEdificio = itemView.findViewById(R.id.calleEdificio);
                iconoEliminar = itemView.findViewById(R.id.iconoEliminar);
            }
        }

        public void removeItem(int position) {
            edificios.remove(position);
            notifyItemRemoved(position);
        }


    }
