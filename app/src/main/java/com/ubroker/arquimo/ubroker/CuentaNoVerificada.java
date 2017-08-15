package com.ubroker.arquimo.ubroker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by andresrodriguez on 09/08/17.
 */

public class CuentaNoVerificada extends AppCompatActivity {
//TEST
    private Button btnSalir, btnEnviarEmail;
    private static final String TAG = "CUENTA NO VERIFICADA | ";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mListener;
    private FirebaseUser usuario = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_no_verificada);

        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getCurrentUser();

        if(usuario != null){
            usuario.reload();

        }

        btnSalir = (Button)findViewById(R.id.btnSalir_CuentaNoVerificada);
        btnEnviarEmail = (Button)findViewById(R.id.btnEnviarCorreo_CuentaNoVerificada);

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
                System.exit(0);
            }
        });

        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Correo de verificación enviado.");
                                    Toast.makeText(getApplicationContext(), "Correo de verificación enviado.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuario = firebaseAuth.getInstance().getCurrentUser();
                if (usuario != null) {
                    if(usuario.isEmailVerified()){
                        pantallaInicio();
                    }
                }
            }


        };

    }

    public void pantallaInicio(){
        Intent intento = new Intent(this, Inicio.class);
        startActivity(intento);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mListener);

    }
}
