package com.ubroker.arquimo.ubroker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by andresrodriguez on 13/06/17.
 */

public class TokenNotificaciones extends FirebaseInstanceIdService {

    //public static final String TAG = "TokenN-LOG | ";
//TEST
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRecibido = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferencias = getSharedPreferences("TokenNotificaciones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("TOKEN_FCM", tokenRecibido);
        editor.commit();
        //Toast.makeText(getApplicationContext(), TAG + tokenRecibido, Toast.LENGTH_LONG ).show();
        //Log.d(TAG, "Este es mi Token: " + tokenRecibido);
        //sendRegistrationToServer(tokenRecibido);


    }
}
