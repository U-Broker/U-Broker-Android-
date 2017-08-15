package com.ubroker.arquimo.ubroker;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import modelos.ModeloCliente;
import modelos.ModeloProducto;
import utileria.Validaciones;
import utileria.VolleySingleton;

/**
 * Created by andresrodriguez on 29/05/17.
 */

public class NuevoCliente extends AppCompatActivity  {

    private EditText etNombres, etApellidos, etCorreo, etCelular, etVerificarCorreo;
    private Spinner spProducto;
    String url = "", parametros = "?";
    List<ModeloProducto> listaProductos = new ArrayList<ModeloProducto>();
    String TAG = "LOG ACTIVITY - NUEVO CLIENTE | ";
    int idProducto;
    ModeloCliente nuevoCliente;
    String nombres, apellidos, correo, celular, idFirebase, verificarCorreo;


//TEST
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_cliente);


        url = getResources().getString(R.string.url_consultar_productos);


        inicializarToolBar();

        etNombres = (EditText) findViewById(R.id.etNombre_NuevoCliente);
        etApellidos = (EditText) findViewById(R.id.etApellidos_NuevoCliente);
        etCorreo = (EditText) findViewById(R.id.etCorreo_NuevoCliente);
        etVerificarCorreo = (EditText) findViewById(R.id.etVerificarCorreo_NuevoCliente);
        etCelular = (EditText) findViewById(R.id.etCelular_NuevoCliente);
        spProducto = (Spinner) findViewById(R.id.spProductos);



        etNombres.requestFocus();


        volleyConsultarProductos(url);
        spProducto.setSelection(0);

    }

    public class ItemSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ModeloProducto producto = (ModeloProducto) parent.getSelectedItem();
            idProducto = producto.getId_producto();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            return;
        }
    }

    public void inicializarToolBar() {
        Toolbar miBarra;
        miBarra = (Toolbar) findViewById(R.id.tbBarra_NuevoCliente);
        miBarra.setTitle(getResources().getString(R.string.app_name) + " - Nuevo Cliente");
        setSupportActionBar(miBarra);
        miBarra.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAtras(v);
            }
        });
    }






    public void irAtras(View vista) {

        this.finish();
    }


    public void registrarCliente(View vista){
        Validaciones cadena = new Validaciones();

        nombres = etNombres.getText().toString();
        apellidos = etApellidos.getText().toString();
        celular = etCelular.getText().toString();
        correo = etCorreo.getText().toString();
        verificarCorreo = etVerificarCorreo.getText().toString();
        idFirebase =  FirebaseAuth.getInstance().getCurrentUser().getUid();



        if(cadena.validarVacio(nombres) || cadena.validarVacio(apellidos)
                || cadena.validarVacio(celular) || cadena.validarVacio(correo) || cadena.validarVacio(verificarCorreo)){
            Toast.makeText(getApplicationContext(), "Ningún campo debe ir vacio.", Toast.LENGTH_LONG).show();
            return;
        }

        if(!cadena.validarCorreo(correo)){
            Toast.makeText(getApplicationContext(), "La dirección de correo electrónico no es valida.", Toast.LENGTH_LONG).show();
            etCorreo.requestFocus();
            return;
        }

        if(!correo.equals(verificarCorreo)){
            Toast.makeText(getApplicationContext(), "Las direcciones de correo deben ser iguales.", Toast.LENGTH_LONG).show();
            return;
        }

        if(idProducto<1){
            Toast.makeText(getApplicationContext(), "Sebe seleccionar un producto.", Toast.LENGTH_LONG).show();
            spProducto.requestFocus();
            return;
        }


        url = getResources().getString(R.string.url_registro_nuevo_cliente);
        parametros += "nombres=" + nombres;
        parametros += "&apellidos=" + apellidos;
        parametros += "&celular=" + celular;
        parametros += "&correo=" + correo;
        parametros += "&id_referencia=" + idFirebase;
        parametros += "&id_producto=" + idProducto;

        url += parametros;




        volleyRegistroCliente(url);




    }






    // PETICIÓN WEB QUE VALIDA SI EL USUARIO TIENE UN REGISTRO COMPLETO EN LA PLATAFORMA.
    // SI ES VALIDA, TE DEJA EN LA PÁGINA DE INICIO.
    // SI NO ES VALIDA, TE MANDA AL FORMULARIO PARA EL REGISTRO DE DATOS.
    public void volleyConsultarProductos(String url) {
        String REQUEST_TAG = "Petición Consultar Productos | ";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                            if (verificarPeticion(status)) {
                                JSONArray productos = response.getJSONArray("productos");
                                listaProductos.add(new ModeloProducto(0,"SELECCIONE PRODUCTO"));
                                for (int i = 0; i < productos.length(); i++) {
                                    JSONObject jsonProducto;
                                    ModeloProducto producto;
                                    int id;
                                    String nombre;

                                    jsonProducto = productos.getJSONObject(i);
                                    id = Integer.parseInt(jsonProducto.getString("id_producto"));
                                    nombre = jsonProducto.getString("nombre");

                                    producto = new ModeloProducto(id, nombre);

                                    listaProductos.add(producto);

                                }




                                ArrayAdapter adaptador = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_spinner_item, listaProductos);

                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                                spProducto.setAdapter(adaptador);

                                spProducto.setOnItemSelectedListener(new ItemSelected());

                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR AL CARGAR PRODUCTOS", Toast.LENGTH_LONG).show();
                                NuevoCliente.this.finish();
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


    // PETICIÓN WEB QUE VALIDA SI EL USUARIO TIENE UN REGISTRO COMPLETO EN LA PLATAFORMA.
    // SI ES VALIDA, TE DEJA EN LA PÁGINA DE INICIO.
    // SI NO ES VALIDA, TE MANDA AL FORMULARIO PARA EL REGISTRO DE DATOS.
    public void volleyRegistroCliente(String url) {
        String REQUEST_TAG = "Petición Registro Cliente | ";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                            if (verificarPeticion(status)) {
                                Toast.makeText(getApplicationContext(), "CLIENTE REGISTRADO CON ÉXITO", Toast.LENGTH_LONG).show();
                                NuevoCliente.this.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR AL INTENTAR REGISTRAR NUEVO CLIENTE", Toast.LENGTH_LONG).show();
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



    public boolean verificarPeticion(String status) {
        boolean respuesta = true;
        if (status.toLowerCase().equals("error")) {
            respuesta = false;
        }

        return respuesta;
    }

}
