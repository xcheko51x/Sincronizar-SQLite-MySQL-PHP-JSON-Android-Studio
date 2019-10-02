package com.xcheko51x.syncsqlitemysql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterServidor extends RecyclerView.Adapter<AdapterServidor.servidorViewHolder> {

    Context context;
    List<Venta> listaServidor;

    public AdapterServidor(Context context, List<Venta> listaServidor) {
        this.context = context;
        this.listaServidor = listaServidor;
    }

    @NonNull
    @Override
    public servidorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, null, false);
        return new AdapterServidor.servidorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull servidorViewHolder holder, int position) {
        holder.tvProducto.setText(listaServidor.get(position).getProducto());
        holder.tvPrecio.setText(listaServidor.get(position).getPrecio());
        holder.tvSync.setText(listaServidor.get(position).getSincronizado());
    }

    @Override
    public int getItemCount() {
        return listaServidor.size();
    }

    public class servidorViewHolder extends RecyclerView.ViewHolder {

        TextView tvProducto, tvPrecio, tvSync;

        public servidorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProducto = itemView.findViewById(R.id.tvProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvSync = itemView.findViewById(R.id.tvSync);
        }
    }
}
