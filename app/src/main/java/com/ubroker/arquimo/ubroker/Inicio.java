package com.ubroker.arquimo.ubroker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import modelos.ModeloUsuario;
import utileria.Validaciones;
import utileria.VolleySingleton;


/**
 * Created by andresrodriguez on 25/05/17.
 */

public class Inicio extends AppCompatActivity {

    private TextView bienvenida;
    private Validaciones helpValidacion = new Validaciones();
    private ModeloUsuario usuario = new ModeloUsuario();
    private FirebaseUser usuarioFirebase = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "ACTIVITY INICIO | ";
    //TEST
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        inicializarToolBar();

        bienvenida = (TextView) findViewById(R.id.txtBienvenida_Inicio);

        if(usuarioFirebase != null){


            if(!usuarioFirebase.isEmailVerified()){
                pantallaCuentaNoVerificada();
            }


            usuario.setIdFirebase(usuarioFirebase.getUid());

            String url = getResources().getString(R.string.url_verificar_datos);
            String parametros = "?id_firebase=" + usuario.getIdFirebase();


            url += parametros;

            volleyVerificarUsuario(url);

        }else{
            pantallaLogin();
        }
    }



    public void inicializarToolBar(){
        Toolbar miBarra;
        miBarra = (Toolbar) findViewById(R.id.tbBarra_Inicio);
        miBarra.setTitle( getResources().getString(R.string.app_name) + " - Inicio" );
        setSupportActionBar(miBarra);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inicio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notificaciones:
                irNotificaciones();
                return true;

            case R.id.action_salir:
                cerrarSesion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        pantallaLogin();
    }

    public void pantallaLogin(){
        Intent intento = new Intent(this, Login.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }

    public void pantallaCuentaNoVerificada(){
        Intent intento = new Intent(this, CuentaNoVerificada.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }

    public void irNotificaciones(){
        Intent intento = new Intent(this, VerNotificaciones.class);
        startActivity(intento);
    }








    public void irNuevoCliente(View view) {
        Intent intento = new Intent(this, NuevoCliente.class);
        startActivity(intento);
    }

    public void irMisClientes(View view) {
        Intent intento = new Intent(this, MisClientes.class);
        startActivity(intento);
    }

    public void irMiRed(View view) {
        Intent intento = new Intent(this, MiRed.class);
        startActivity(intento);
    }

    public void irProductos(View view) {
        Intent intento = new Intent(this, Productos.class);
        startActivity(intento);
    }










// PETICIÓN WEB QUE VALIDA SI EL USUARIO TIENE UN REGISTRO COMPLETO EN LA PLATAFORMA.
// SI ES VALIDA, TE DEJA EN LA PÁGINA DE INICIO.
// SI NO ES VALIDA, TE MANDA AL FORMULARIO PARA EL REGISTRO DE DATOS.
    public void volleyVerificarUsuario(String url) {
        String REQUEST_TAG = "com.ubroker.arquimo.ubroker";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status = "";
                        try {
                            status = response.getString("status");
                            if (helpValidacion.verificarRespuesta(status)) {
                                String saludo = (usuarioFirebase.getDisplayName() == null)? "¡Hola, Bienvenido!" : "¡Hola, " + usuarioFirebase.getDisplayName().split(" ")[0] + "!";
                                bienvenida.setText(saludo);
                            }else {
                              pantallaLogin();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR VERIFICAR USUARIO: "+ error.getMessage());
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }





}
