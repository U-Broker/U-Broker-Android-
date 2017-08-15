package com.ubroker.arquimo.ubroker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.List;

import modelos.ModeloProducto;

/**
 * Created by andresrodriguez on 21/06/17.
 */

public class ProductosAdaptador extends  RecyclerView.Adapter<ProductosAdaptador.VistaProductos> {

    private List<ModeloProducto> items;
    private static String TAG = "PRODUCTOS ADAPTADOR | ";

    public static class VistaProductos extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView visitas;

        public VistaProductos(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            // nombre = (TextView) v.findViewById(R.id.nombre);
            visitas = (TextView) v.findViewById(R.id.descripcion);


        }
    }

    public ProductosAdaptador(List<ModeloProducto> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public VistaProductos onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.productos_cardview, viewGroup, false);
        return new VistaProductos(v);
    }


//test

    @Override
    public void onBindViewHolder(VistaProductos viewHolder, int i) {
        Log.d(TAG, items.get(i).getUrl_imagen());
        Ion.with(viewHolder.imagen).error(R.drawable.u_broker_logo_azul).load(items.get(i).getUrl_imagen());
        viewHolder.imagen.setTag(items.get(i).getUrl_video());
        //viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.visitas.setText(String.valueOf(items.get(i).getDescripcion()));
        viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = v.getTag().toString();
                Uri uri = Uri.parse(url);

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);

            }
        });
    }
}
