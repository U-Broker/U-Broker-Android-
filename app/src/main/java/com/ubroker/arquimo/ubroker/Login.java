package com.ubroker.arquimo.ubroker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import modelos.ModeloUsuario;
import utileria.Validaciones;
import utileria.VolleySingleton;


public class Login extends AppCompatActivity {

    private Validaciones helpValidacion = new Validaciones();
    private CallbackManager callbackManager;
    private LoginButton botonLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressBar barra_cargando;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText etCorreo, etPassword;
    private Button btnIniciarSesion, btnRegistroCuenta;
    private static final String TAG = "ACTY LOGIN | ";
    private Boolean usuario_activo = false;
    private ModeloUsuario usuario = new ModeloUsuario();
    private FirebaseUser usuarioFirebase = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (AccessToken.getCurrentAccessToken() != null) {
            usuario_activo = usuarioFirebase.isEmailVerified();
            Log.d(TAG, "ENTRE - " + usuario_activo);

            if (usuario_activo) {
                pantallaIncio();
            } else {
                pantallaCuentaNoVerificada();
            }

        }


        etCorreo = (EditText) findViewById(R.id.etCorreo_Login);
        etPassword = (EditText) findViewById(R.id.etPassword_Login);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion_Login);
        btnRegistroCuenta = (Button) findViewById(R.id.btnRegistroCuenta_Login);
        barra_cargando = (ProgressBar) findViewById(R.id.pbCargando_FB);
        botonLogin = (LoginButton) findViewById(R.id.login_button);


        callbackManager = CallbackManager.Factory.create();
        botonLogin.setReadPermissions("email", "public_profile");

        botonLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken tokenFB = loginResult.getAccessToken();
                handleFacebookAccessToken(tokenFB);

            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), R.string.error_login + exception.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuarioFirebase = firebaseAuth.getInstance().getCurrentUser();
                if (usuarioFirebase != null) {
                    usuario.setIdFirebase(usuarioFirebase.getUid());
                    Log.d(TAG, "" + usuarioFirebase.getUid());
                    Log.d(TAG, "" + usuarioFirebase.getDisplayName());
                    String nombreAuxiliar[] = usuario.formatearNombre(usuarioFirebase.getDisplayName());

                    usuario.setNombres(helpValidacion.CapitalizarCadena(nombreAuxiliar[0]));
                    usuario.setApellidos(helpValidacion.CapitalizarCadena(nombreAuxiliar[1]));
                    usuario.setCorreo(usuarioFirebase.getEmail());

                    usuario.setTokenFCM(FirebaseInstanceId.getInstance().getToken());
                    usuario_activo = usuarioFirebase.isEmailVerified();

                    Log.d(TAG, "" + usuario_activo);

                    String url = getResources().getString(R.string.url_login);
                    String parametros = "?id_firebase=" + usuario.getIdFirebase();
                    parametros += "&tokenFCM=" + usuario.getTokenFCM();
                    url += parametros;
                    volleyVerificarUsuario(url);
                }


            }


        };


        btnRegistroCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pantallaRegistroCuenta();
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken accessToken) {
        barra_cargando.setVisibility(View.VISIBLE);
        botonLogin.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        etCorreo.setVisibility(View.GONE);
        btnIniciarSesion.setVisibility(View.GONE);
        btnRegistroCuenta.setVisibility(View.GONE);

        AuthCredential credencial = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, task.getException().getMessage());
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_SHORT).show();
                } else {
                    usuario_activo = firebaseAuth.getCurrentUser().isEmailVerified();
                    if (!usuario_activo) {
                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Correo de verificación enviado FACEBOOK.");
                                            FirebaseAuth.getInstance().signOut();
                                            LoginManager.getInstance().logOut();
                                        }
                                    }
                                });
                    }

                }


                barra_cargando.setVisibility(View.GONE);
                botonLogin.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                etCorreo.setVisibility(View.VISIBLE);
                btnIniciarSesion.setVisibility(View.VISIBLE);
                btnRegistroCuenta.setVisibility(View.VISIBLE);

            }
        });
    }

    public void pantallaIncio() {
        Intent intento = new Intent(this, Inicio.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }

    public void pantallaCuentaNoVerificada() {
        Intent intento = new Intent(this, CuentaNoVerificada.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }


    public void pantallaRegistroCuenta() {
        Intent intento = new Intent(this, RegistroCuenta.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intento);
    }


    public void iniciarSesionCorreo(View vista) {



        String correo = etCorreo.getText().toString();
        String password = etPassword.getText().toString();


        if (helpValidacion.validarVacio(correo)) {
            Toast.makeText(getApplicationContext(), "El campo de correo no puede ir vacio.", Toast.LENGTH_LONG).show();
            etCorreo.requestFocus();
            return;
        }

        if (helpValidacion.validarVacio(password)) {
            Toast.makeText(getApplicationContext(), "Los campo de contraseña no pueden ir vacio.", Toast.LENGTH_LONG).show();
            etPassword.requestFocus();
            return;
        }


        usuario.setCorreo(correo);
        usuario.setContraseña(password);

        barra_cargando.setVisibility(View.VISIBLE);
        botonLogin.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        etCorreo.setVisibility(View.GONE);
        btnRegistroCuenta.setVisibility(View.GONE);
        btnIniciarSesion.setVisibility(View.GONE);

        firebaseAuth.signInWithEmailAndPassword(usuario.getCorreo(), usuario.getContraseña())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            try{
                                throw task.getException();
                            } catch(FirebaseAuthException e){
                                helpValidacion.validacionesFirebase(getApplicationContext(),e);
                            } catch(Exception e ){
                                Log.e(TAG, "ERROR: " + e.getMessage());
                            }
                        }

                        barra_cargando.setVisibility(View.GONE);
                        botonLogin.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                        etCorreo.setVisibility(View.VISIBLE);
                        btnIniciarSesion.setVisibility(View.VISIBLE);
                        btnRegistroCuenta.setVisibility(View.VISIBLE);
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);

    }


    public void volleyVerificarUsuario(String url) {
        String REQUEST_TAG = "com.ubroker.arquimo.ubroker";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status;
                        JSONObject datos;
                        try {
                            status = response.getString("status");

                            if (helpValidacion.verificarRespuesta(status)) {
                                if (usuario_activo) {
                                    pantallaIncio();
                                } else {
                                    pantallaCuentaNoVerificada();
                                }

                            } else {
                                datos = response.getJSONObject("data");
                                String url = getResources().getString(R.string.url_registro_cuenta);
                                String parametros = "?id_firebase=" + usuario.getIdFirebase();
                                parametros += "&tokenFCM=" + usuario.getTokenFCM();

                                url += parametros;
                                volleyRegistroUsuario(url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage().toString());
                //Toast.makeText(getApplicationContext(), "ERRORRRRRR" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    public void volleyRegistroUsuario(String url) {
        String REQUEST_TAG = "com.ubroker.arquimo.ubroker";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status = "";
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

                                Log.d(TAG, "" + url);
                                volleyRegistrarDatosUsuario(url);
                            } else {
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al intentar registrar id.", Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                //Toast.makeText(getApplicationContext(), "ERROR 2 " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }




    public void volleyRegistrarDatosUsuario(String url) {
        String REQUEST_TAG = "com.ubroker.arquimo.ubroker";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status = "";
                        try {
                            status = response.getString("status");
                            if (helpValidacion.verificarRespuesta(status)) {
                                if (usuario_activo) {
                                    pantallaIncio();
                                } else {
                                    pantallaCuentaNoVerificada();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al momento de registar tus datos. Intentalo de nuevo por favor.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "" + e.getMessage());
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


}
