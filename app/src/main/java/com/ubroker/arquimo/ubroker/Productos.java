package com.ubroker.arquimo.ubroker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import modelos.ModeloProducto;
import utileria.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresrodriguez on 13/06/17.
 */

public class Productos extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    List listaProductos = new ArrayList();
   // List<ModeloProducto> listaProductos = new ArrayList<ModeloProducto>();
    private String url = "";

//Test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos);

        inicializarToolBar();


        url = getResources().getString(R.string.url_consultar_productos);
        volleyConsultarProductos(url);


        //items.add(new ModeloProducto(R.drawable.credito_revolvente, "LINEA DE CRÉDITO REVOLVENTE", "Utiliza este crédito como capital de trabajo."));
        //items.add(new ModeloProducto(R.drawable.credito_factoraje, "LINEA DE FACTORAJE PARA PAGO DE PROVEEDORES", "Haz frente a los pagos que tengas con proveedores."));
        //items.add(new ModeloProducto(R.drawable.credito_linea_mixta, "CRÉDITO MIXTO", "Cubre necesidades tanto de capital de trabajo como pago de proveedores."));
        //items.add(new ModeloProducto(R.drawable.credito_construcredix, "CONSTRUCREDIX", "El crédito que necesitas para agilizar la venta de tu casa o propiedad."));
        //items.add(new ModeloProducto(R.drawable.credito_inmobicredix, "INMOBICREDIX", "Éste es un producto diseñado para otorgar liquidez a los proyectos inmobiliarios que necesiten capital."));



// Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador_Productos);
        recycler.setHasFixedSize(true);

// Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);



    }





    public void inicializarToolBar(){
        Toolbar miBarra;
        miBarra = (Toolbar)findViewById(R.id.tbBarra_Productos);
        miBarra.setTitle(getResources().getString(R.string.app_name) + " - Productos");
        setSupportActionBar(miBarra);

        miBarra.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAtras(v);
            }
        });

    }

    public void irAtras(View vista){
        this.finish();
    }


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
                                for (int i = 0; i < productos.length(); i++) {
                                    JSONObject jsonProducto;
                                    ModeloProducto producto;
                                    int id;
                                    String nombre, descripcion, rutaImagen, rutaVideo;

                                    jsonProducto = productos.getJSONObject(i);
                                    id = Integer.parseInt(jsonProducto.getString("id_producto"));
                                    nombre = jsonProducto.getString("nombre");
                                    descripcion = jsonProducto.getString("desc");
                                    rutaImagen = getResources().getString(R.string.url_ruta_img) +  jsonProducto.getString("img");
                                    rutaVideo = jsonProducto.getString("video");



                                    producto = new ModeloProducto(id, nombre, descripcion, rutaImagen, rutaVideo);

                                    listaProductos.add(producto);

                                }

// Crear un nuevo adaptador
                                adapter = new ProductosAdaptador(listaProductos);
                                recycler.setAdapter(adapter);







                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR AL CARGAR PRODUCTOS", Toast.LENGTH_LONG).show();
                                Productos.this.finish();
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
