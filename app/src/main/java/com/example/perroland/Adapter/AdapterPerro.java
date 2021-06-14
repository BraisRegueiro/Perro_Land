package com.example.perroland.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perroland.Perro;
import com.example.perroland.R;
import com.example.perroland.Usuario;

import java.util.ArrayList;
import java.util.List;


public class AdapterPerro extends RecyclerView.Adapter<AdapterPerro.PerroHolder>  {

    private final Activity act;
    private List<Perro> items;
    private static final int layout = R.layout.item_detalleperro;

    public AdapterPerro(Activity act, List<Perro> items) {
        this.act = act;
        this.items= items;
    }

    @NonNull
    @Override
    public PerroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new PerroHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PerroHolder holder, int position) {
        final Perro item = items.get(position);

        String texto = item.getNombrePerro();
        holder.nombrePerro.setText(texto);
    }

    @Override
    public int getItemCount() { return items.size();}


    public class PerroHolder extends RecyclerView.ViewHolder {
        TextView nombrePerro;
        TextView razaPerro;
        TextView edadPerro;
        TextView nombreDueño;

        public PerroHolder(@NonNull View itemView) {
            super(itemView);
            nombrePerro = (TextView) itemView.findViewById(R.id.item_nombre_perro);
            razaPerro = (TextView) itemView.findViewById(R.id.item_raza_perro);
            edadPerro = (TextView) itemView.findViewById(R.id.item_edad_perro);
            nombreDueño = (TextView) itemView.findViewById(R.id.item_nombre_dueno);
        }
    }
}
