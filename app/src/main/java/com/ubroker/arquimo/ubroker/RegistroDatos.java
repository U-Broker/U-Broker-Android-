package com.ubroker.arquimo.ubroker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import utileria.Validaciones;
import utileria.VolleySingleton;

/**
 * Created by andresrodriguez on 03/07/17.
 */

public class RegistroDatos extends AppCompatActivity {

//TEST
    URL urlFormateada;

    EditText etNombres, etApellidos, etCorreo, etContraseña, etValidarcontraseña;
    Button btnRegistrarCuenta;
    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    private static String TAG = "ACTV REGISTRO DATOS |";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_datos);




        etNombres = (EditText)findViewById(R.id.etNombres_RegistroDatos);
        etApellidos = (EditText)findViewById(R.id.etApellidos_RegistroDatos);
        etCorreo = (EditText)findViewById(R.id.etCorreo_RegistroDatos);
        etContraseña = (EditText) findViewById(R.id.etContraseña_RegistroDatos);
        etValidarcontraseña = (EditText) findViewById(R.id.etVerificarPass_RegistroDatos);
        btnRegistrarCuenta = (Button) findViewById(R.id.btnRegistrar_RegistroDatos);





        if(usuario != null){
            String datos = "DATOS DEL USUARIO\n";
            datos += "| Nombre: " + usuario.getDisplayName();
            datos += "| Correo: " + usuario.getEmail();
            datos += "| UID: " + usuario.getUid();
            datos += "| URI Foto: " + usuario.getPhotoUrl();




            Log.d(TAG, datos);
            String[] nombreCompleto = usuario.getDisplayName().split(" ");
            String nombre = "";
            String apellido = "";
            switch(nombreCompleto.length){
                case 2:
                    nombre = nombreCompleto[0];
                    apellido = nombreCompleto[1];
                    break;
                case 3:
                    nombre = nombreCompleto[0];
                    apellido = nombreCompleto[1] + " " + nombreCompleto[2];
                    break;
                case 4:
                    nombre = nombreCompleto[0] + " " + nombreCompleto[1];
                    apellido = nombreCompleto[2] + " " + nombreCompleto[3];
                    break;
                default:
                    for(int i = 0 ; i < nombreCompleto.length ; i++){
                        if(nombreCompleto.length-i <= 2){
                            apellido += nombreCompleto[i];
                        }else{
                            nombre += nombreCompleto[i];
                        }
                    }
                    break;

            }


            String correoE = usuario.getEmail();
            etNombres.setText(nombre);
            etApellidos.setText(apellido);
            etCorreo.setText(correoE);

        }



        //Acción que se ejecutará al momento de darle clic al botón "Registrar"
        btnRegistrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validaciones cadena = new Validaciones();
                final String nombres = etNombres.getText().toString(),
                        apellidos = etApellidos.getText().toString(),
                        correo = etCorreo.getText().toString(),
                        contraseña = etContraseña.getText().toString(),
                        validarContraseña = etValidarcontraseña.getText().toString();

                if(cadena.validarVacio(nombres) || cadena.validarVacio(apellidos) ||
                        cadena.validarVacio(correo) || cadena.validarVacio(contraseña) || cadena.validarVacio(validarContraseña)){
                    Toast.makeText(getApplicationContext(), "Completa los campos. Ningún campo puede ir vacio.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!cadena.validarCorreo(correo)){
                    Toast.makeText(getApplicationContext(), "Ingresa una dirección de correo valida.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!cadena.validarTamañoContraseña(contraseña)){
                    Toast.makeText(getApplicationContext(), "La contraseña debe ser igual o mayor a 6 caracteres.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!contraseña.equals(validarContraseña)){
                    Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir.", Toast.LENGTH_LONG).show();
                    return;
                }
                String nombreCompleto = nombres + " " + apellidos;
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nombreCompleto)
                        .build();

                usuario.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String url = getResources().getString(R.string.url_registro);
                            String parametros = "?id_firebase=" + usuario.getUid();
                            parametros += "&nombres_usuario=" + nombres;
                            parametros += "&apellidos_usuario=" + apellidos;
                            parametros += "&correo_usuario=" + correo;
                            parametros = parametros.replace(" ", "%20");
                            url += parametros;

                            try {
                                urlFormateada = new URL(url);
                            } catch (MalformedURLException e) {
                                Log.e(TAG,e.getMessage());
                            }
                            Log.d(TAG,urlFormateada.toString());



                            volleyRegistrarDatosCuenta(urlFormateada.toString());
                        }
                    }
                });


            }
        });



    }

    public void pantallaLogin(){
        Intent intento = new Intent(this, Login.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }

    public void pantallaInicio(){
        Intent intento = new Intent(this, Inicio.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }





    public boolean verificarRespuesta(String status) {
        boolean respuesta = true;
        if (status.toLowerCase().equals("error")) {
            respuesta = false;
        }

        return respuesta;
    }

    public void volleyRegistrarDatosCuenta(String url) {
        String REQUEST_TAG = "com.ubroker.arquimo.ubroker";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status = "";
                        try {
                            status = response.getString("status");
                            if (verificarRespuesta(status)) {
                                pantallaInicio();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al momento de registar tus datos. Intentalo de nuevo por favor.", Toast.LENGTH_LONG).show();
                                pantallaLogin();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.getMessage());

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

}
