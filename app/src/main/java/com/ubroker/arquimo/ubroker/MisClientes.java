package com.ubroker.arquimo.ubroker;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelos.ModeloCliente;
import modelos.ModeloProducto;
import utileria.VolleySingleton;

/**
 * Created by andresrodriguez on 12/06/17.
 */

public class MisClientes extends AppCompatActivity {


    //TEST
    ProgressBar barra;
    int progreso = 0;
    LinearLayout layoutSinClientes;
    private ListView lista;
    private List<ModeloCliente> listaClientes = new ArrayList();
    String url = "", idFirebase = "", parametros = "?";
    int[] porcentajeAvance;
    String[] etapasProceso;
    //TextView tvNombreCliente, tvEtapaProceso, tvPorcentajeAvance, tvComentarios;
    //ProgressBar pbBarraProgeso;


    Random semilla = new Random();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_clientes);


        //Llamada al método para iniciarlizar la Toolbar
        iniciarlizarToolBar();

        idFirebase = FirebaseAuth.getInstance().getCurrentUser().getUid();
        parametros += "id_firebase=" + idFirebase;
        url = getResources().getString(R.string.url_consultar_clientes);
        url += parametros;


        volleyConsultarClientes(url);

        Log.d("lista | ",url);


    }


    //Método para iniciarlizar Toolbar, creando un instancia de la clase "Toolbar",
    //asignandole el elemento "tbBarra_MisClientes" del layout y modificando el títuolo por defecto,
    //añadiendole un listener para que finalice la actividad al momento de apretar la flecha ejecutando el método "irAtras()"
    public void iniciarlizarToolBar() {
        Toolbar miBarra;
        miBarra = (Toolbar) findViewById(R.id.tbBarra_MisClientes);
        miBarra.setTitle( getResources().getString(R.string.app_name) + " - Mis Clientes" );
        setSupportActionBar(miBarra);
        miBarra.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAtras(v);
            }
        });
    }

    //Método que recibe un objeto View para ser ejecutado por un elemento de la interfaz de usuario,
    //finaliza la actividad actual, regresando a la actividad "Inicio"
    public void irAtras(View vista) {
        this.finish();
    }


    public void mostrarDescripcion(View vista) {


        AlertDialog.Builder alerta = new AlertDialog.Builder(this);


        alerta.setView(vista);

        alerta.setTitle("Descripción Oportunidad");
        alerta.setIcon(R.drawable.descripcion_mis_clientes);

        alerta.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface ventana, int whichButton) {
                //Your action here
                ventana.dismiss();
            }
        });

        alerta.show();

    }

    public void inicializarComponentesDialogo(View vista, ModeloCliente cliente) {
        TextView tvNombreCliente = (TextView) vista.findViewById(R.id.tvNombreCliente_Descripcion_Dialogo);
        TextView tvEtapaProceso = (TextView) vista.findViewById(R.id.tvEtapaProceso_Descripcion_Dialogo);
        TextView tvPorcentajeAvance = (TextView) vista.findViewById(R.id.tvPorcentajeAvance_Descripcion_Dialogo);
        TextView tvComentarios = (TextView) vista.findViewById(R.id.tvComentarios_Descripcion_Dialogo);
        ProgressBar pbBarraProgeso = (ProgressBar) vista.findViewById(R.id.pbBarraProgreso_Descripcion_Dialogo);


        int progreso = cliente.getPorcentajeAvance();
        String nombreCompleto = cliente.getNombres() + " " + cliente.getApellidos();
        tvNombreCliente.setText(nombreCompleto);
        tvEtapaProceso.setText(cliente.getNombreEtapa());
        pbBarraProgeso.setProgress(progreso);
        tvPorcentajeAvance.append("" + progreso);
        tvComentarios.append("\n" + cliente.getComentarios());
    }


    // PETICIÓN WEB QUE VALIDA SI EL USUARIO TIENE UN REGISTRO COMPLETO EN LA PLATAFORMA.
    // SI ES VALIDA, TE DEJA EN LA PÁGINA DE INICIO.
    // SI NO ES VALIDA, TE MANDA AL FORMULARIO PARA EL REGISTRO DE DATOS.
    public void volleyConsultarClientes(String url) {
        final String REQUEST_TAG = "Petición Consultar Clientes | ";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                            if (verificarPeticion(status)) {
                                JSONArray misClientes = response.getJSONArray("clientes");
                                for (int i = 0; i < misClientes.length(); i++) {
                                    JSONObject jsonCliente;
                                    ModeloCliente cliente;
                                    int idCliente, porcentajeAvance, imagen;
                                    String nombres, apellidos, celular, correo, idReferencia, comentarios, etapa, nombreProducto;

                                    Log.d(REQUEST_TAG, response.toString());

                                    jsonCliente = misClientes.getJSONObject(i);
                                    idCliente = Integer.parseInt(jsonCliente.getString("id_cliente"));
                                    nombreProducto = jsonCliente.getString("producto");
                                    porcentajeAvance = semilla.nextInt(100) + 1 ;
                                    imagen = R.drawable.clientes_mis_clientes;


                                    etapa = "[NOMBRE ETAPA DEL PROCESO]";

                                    nombres = jsonCliente.getString("nombres");
                                    celular = jsonCliente.getString("celular");
                                    correo = jsonCliente.getString("correo");
                                    idReferencia = jsonCliente.getString("referencia");
                                    comentarios = "[EN ESTA PARTE SE INCLUYEN LOS COMENTARIOS EN CASO DE QUE SE NECESITEN, ESTE CAMPO NO ESTÁ CONTEMPLADO, PERO PODRÍA INCLUIRSE]";
                                    apellidos = jsonCliente.getString("apellidos");


                                    cliente = new ModeloCliente(idCliente, imagen, nombreProducto, nombres, apellidos, celular, correo, idReferencia, etapa, porcentajeAvance, comentarios);

                                    listaClientes.add(cliente);

                                }


                                //Iniciar ListView
                                inicializarListView();


                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR AL CARGAR CLIENTES", Toast.LENGTH_LONG).show();
                                MisClientes.this.finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);

    }


    public void inicializarListView() {
        barra = (ProgressBar)findViewById(R.id.pbBarraProceso_MisClientes);
        lista = (ListView) findViewById(R.id.mi_lista);
        layoutSinClientes = (LinearLayout) findViewById(R.id.noClientes);
        barra.setVisibility(View.GONE);
        if(listaClientes.isEmpty()){

            lista.setVisibility(View.GONE);
            layoutSinClientes.setVisibility(View.VISIBLE);
        }else{
            layoutSinClientes.setVisibility(View.GONE);
            lista.setVisibility(View.VISIBLE);
            MisClientesAdaptador adapter = new MisClientesAdaptador(this, listaClientes);
            lista.setAdapter(adapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    LayoutInflater inflater = getLayoutInflater();
                    View vista = inflater.inflate(R.layout.descripcion_clientes_dialogo, null);


                    //Llamada al método para inicializar los componentes del dialogo.

                    inicializarComponentesDialogo(vista, listaClientes.get(position));


                    mostrarDescripcion(vista);
                }
            });
        }

    }

    public boolean verificarPeticion(String status) {
        boolean respuesta = true;
        if (status.toLowerCase().equals("error")) {
            respuesta = false;
        }

        return respuesta;
    }
}
