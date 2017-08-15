package com.ubroker.arquimo.ubroker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by andresrodriguez on 14/06/17.
 */

public class VerNotificaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificaciones);
        inicializarToolBar();
        String notificacion = "";

        if(getIntent().getExtras() != null){
            for(String llave : getIntent().getExtras().keySet()){
                notificacion += "\n" + getIntent().getExtras().getString(llave);
            }
        }

        Toast.makeText(getApplicationContext(),notificacion, Toast.LENGTH_LONG ).show();
    }


    public void inicializarToolBar(){
        Toolbar miBarra;
        miBarra = (Toolbar)findViewById(R.id.tbBarra_Notificaciones);
        setSupportActionBar(miBarra);
        miBarra.setTitle(getResources().getString(R.string.app_name) + " - Mis Notificaciones");
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
}
