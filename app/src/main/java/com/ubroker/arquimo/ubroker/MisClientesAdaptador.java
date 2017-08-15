package com.ubroker.arquimo.ubroker;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelos.ModeloCliente;

/**
 * Created by andresrodriguez on 20/06/17.
 */

public class MisClientesAdaptador extends ArrayAdapter {

//TEST
    private final Activity contexto;
   // private final String[] nombreCliente;
    //private final Integer[] imagenes;
    //private final String[] nombreEtapaProceso;
    //private final int[] porncetajeAvanceProceso;
    private final List<ModeloCliente> listaClientes ;


    public MisClientesAdaptador(Activity contexto, List clientes) {
        super(contexto, R.layout.fila_lista_mis_clientes, clientes);
        // TODO Auto-generated constructor stub

        this.contexto = contexto;
        this.listaClientes = clientes;

    }

    public View getView(int posicion, View view, ViewGroup parent) {

        LayoutInflater inflater = contexto.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.fila_lista_mis_clientes, null, true);

        TextView tvNombreCliente = (TextView) rowView.findViewById(R.id.tvNombreCliente_FilaMisClientes);
        TextView tvEtapaProceso = (TextView) rowView.findViewById(R.id.tvEtapaProceso_FilaMisClientes);
        ImageView ivImagen = (ImageView) rowView.findViewById(R.id.ivImagen_FilaMisClientes);
        ProgressBar pbBarraPogreso = (ProgressBar) rowView.findViewById(R.id.pbBarraProceso_FilaMisClientes);



        tvNombreCliente.setText(listaClientes.get(posicion).getNombres() + " " + listaClientes.get(posicion).getApellidos());
        tvEtapaProceso.setText(listaClientes.get(posicion).getNombreEtapa());
        ivImagen.setImageResource(listaClientes.get(posicion).getImagen());
        pbBarraPogreso.setProgress(listaClientes.get(posicion).getPorcentajeAvance());


        return rowView;
    }


}
