package com.ubroker.arquimo.ubroker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import modelos.ModeloUsuario;
import utileria.Validaciones;
import utileria.VolleySingleton;

/**
 * Created by andresrodriguez on 07/06/17.
 */

public class RegistroCuenta extends AppCompatActivity {

    private Validaciones helpValidacion = new Validaciones();
    TextView tvTitulo, tvSubtitulo;
    URL urlFormateada;
    ProgressBar pbCargando;
    EditText etNombres, etApellidos, etCorreo, etContraseña, etValidarcontraseña;
    Button btnRegistrarCuenta;
    private ModeloUsuario usuario = null;


    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener firebaseListener;

    private FirebaseUser usuarioFirebase = null;

    private static final String TAG = "ACTY REGISTRO CUENTA | ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_cuenta);

        iniciarToolBar();


        tvSubtitulo = (TextView) findViewById(R.id.tvSubtitulo_RegistroCuenta);
        tvTitulo = (TextView) findViewById(R.id.tvTitulo_RegistroCuenta);
        pbCargando = (ProgressBar) findViewById(R.id.pbCargando_RegistroCuenta);


        etNombres = (EditText) findViewById(R.id.etNombres_RegistroCuenta);
        etApellidos = (EditText) findViewById(R.id.etApellidos_RegistroCuenta);
        etCorreo = (EditText) findViewById(R.id.etCorreo_RegistroCuenta);
        etContraseña = (EditText) findViewById(R.id.etContraseña_RegistroCuenta);
        etValidarcontraseña = (EditText) findViewById(R.id.etVerificarPass_RegistroCuenta);
        btnRegistrarCuenta = (Button) findViewById(R.id.btnRegistrar_RegistroCuenta);

        etNombres.requestFocus();


        mAuth = FirebaseAuth.getInstance();

//TEST
        firebaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuarioFirebase = firebaseAuth.getCurrentUser();
                if (usuarioFirebase != null) {
                    // User is signed in
                    usuario.setIdFirebase(usuarioFirebase.getUid());
                    usuario.setTokenFCM(FirebaseInstanceId.getInstance().getToken());
                    Log.d(TAG, "onAuthStateChanged: Inicio sesión:" + usuarioFirebase.getUid());
                    Log.d(TAG, "NOMBRES:" + usuario.getNombres());
                    Log.d(TAG, "APELLIDOS:" + usuario.getApellidos());
                    Log.d(TAG, "NOMBRE COMPLETO:" + usuario.getNombreCompleto());


                    String url = getResources().getString(R.string.url_registro_cuenta);
                    String parametros = "?id_firebase=" + usuario.getIdFirebase();
                    parametros += "&tokenFCM=" + usuario.getTokenFCM();

                    parametros = parametros.replace(" ", "%20");

                    url += parametros;

                    try {
                        urlFormateada = new URL(url);
                    } catch (MalformedURLException e) {
                        Log.e(TAG, "" + e.getMessage());
                    }

                    Log.d(TAG, urlFormateada.toString());

                    volleyRegistrarUsuario(urlFormateada.toString());





                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: Cerró Sesión");
                }
            }
        };


        //Acción que se ejecutará al momento de darle clic al botón "Registrar"
        btnRegistrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nombres = etNombres.getText().toString();
                String apellidos = etApellidos.getText().toString();
                String correo = etCorreo.getText().toString();
                String contraseña = etContraseña.getText().toString();
                String validarContraseña = etValidarcontraseña.getText().toString();

                if (helpValidacion.validarVacio(nombres) || helpValidacion.validarVacio(apellidos) ||
                        helpValidacion.validarVacio(correo) || helpValidacion.validarVacio(contraseña) ||
                        helpValidacion.validarVacio(validarContraseña)) {
                    Toast.makeText(getApplicationContext(), "Completa los campos. Ningún campo puede ir vacio.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!helpValidacion.validarCorreo(correo)) {
                    Toast.makeText(getApplicationContext(), "Ingresa una direcció de correo valida.", Toast.LENGTH_LONG).show();
                    return;
                }


                if (!contraseña.equals(validarContraseña)) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir.", Toast.LENGTH_LONG).show();
                    return;
                }

                usuario = new ModeloUsuario(
                        "",
                        "",
                        helpValidacion.CapitalizarCadena(nombres),
                        helpValidacion.CapitalizarCadena(apellidos),
                        correo,
                        contraseña
                );

                pbCargando.setVisibility(View.VISIBLE);
                tvSubtitulo.setVisibility(View.GONE);
                etNombres.setVisibility(View.GONE);
                etContraseña.setVisibility(View.GONE);
                etApellidos.setVisibility(View.GONE);
                etCorreo.setVisibility(View.GONE);
                etValidarcontraseña.setVisibility(View.GONE);
                btnRegistrarCuenta.setVisibility(View.GONE);


                crearUsuario(usuario);


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseListener != null) {
            mAuth.removeAuthStateListener(firebaseListener);
        }
    }


    public void pantallaLogin() {
        Intent intento = new Intent(this, Login.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }


    public void iniciarToolBar() {
        Toolbar miBarra;
        miBarra = (Toolbar) findViewById(R.id.tbBarra_RegistroCuenta);
        miBarra.setTitle(getResources().getString(R.string.app_name) + " - Registrar Cuenta");
        setSupportActionBar(miBarra);

        miBarra.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pantallaLogin();
            }
        });
    }


    public void crearUsuario(ModeloUsuario user) {
        mAuth.createUserWithEmailAndPassword(user.getCorreo(), user.getContraseña())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthException e) {
                                helpValidacion.validacionesFirebase(getApplicationContext(), e);
                            } catch (Exception e) {
                                Log.e(TAG, "ERROR: " + e.getMessage());
                            }
                        } else {
                            usuarioFirebase = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usuario.getNombreCompleto())
                                    .build();


                            usuarioFirebase.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "PERFIL DE USUARIO ACTUALIZADO.");
                                                usuarioFirebase.reload();

                                            }
                                        }
                                    });

                            usuarioFirebase.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Correo de verificación enviado.");
                                            }
                                        }
                                    });
                        }


                        pbCargando.setVisibility(View.GONE);
                        tvSubtitulo.setVisibility(View.VISIBLE);
                        etNombres.setVisibility(View.VISIBLE);
                        etContraseña.setVisibility(View.VISIBLE);
                        etApellidos.setVisibility(View.VISIBLE);
                        etCorreo.setVisibility(View.VISIBLE);
                        etValidarcontraseña.setVisibility(View.VISIBLE);
                        btnRegistrarCuenta.setVisibility(View.VISIBLE);

                    }
                });
    }

    public void volleyRegistrarDatosCuenta(String url) {
        String REQUEST_TAG = "com.ubroker.arquimo.ubroker";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status;
                        try {
                            status = response.getString("status");
                            if (helpValidacion.verificarRespuesta(status)) {
                                Toast.makeText(getApplicationContext(), "Se ha creado su cuenta correctamente, verifique su correo para poder iniciar sesión.", Toast.LENGTH_LONG).show();
                                pantallaLogin();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al momento de registar tus datos. Intentalo de nuevo por favor.", Toast.LENGTH_LONG).show();
                                pantallaLogin();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "" + e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR REGISTRAR DATOS CUENTA: " + error.getMessage());
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }


    public void volleyRegistrarUsuario(String url) {
        String REQUEST_TAG = "com.ubroker.arquimo.ubroker";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status;
                        try {
                            status = response.getString("status");
                            if (helpValidacion.verificarRespuesta(status)) {
                                String url = getResources().getString(R.string.url_registro);
                                String parametros = "?id_firebase=" + usuario.getIdFirebase();
                                parametros += "&nombres_usuario=" + usuario.getNombres();
                                parametros += "&apellidos_usuario=" + usuario.getApellidos();
                                parametros += "&correo_usuario=" + usuario.getCorreo();
                                parametros = parametros.replace(" ", "%20");
                                url += parametros;

                                urlFormateada = new URL(url);
                                Log.d(TAG, "" + urlFormateada);

                                volleyRegistrarDatosCuenta(urlFormateada.toString());
                            } else {
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al momento de registar tus datos. Intentalo de nuevo por favor.", Toast.LENGTH_LONG).show();
                                pantallaLogin();
                            }
                        } catch (JSONException | MalformedURLException e) {
                            Log.e(TAG, "" + e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR REGISTRAR USUARIO: " + error.getMessage());
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

}
