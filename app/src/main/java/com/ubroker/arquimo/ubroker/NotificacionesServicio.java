package com.ubroker.arquimo.ubroker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by andresrodriguez on 13/06/17.
 */

public class NotificacionesServicio extends FirebaseMessagingService{



    @Override
    public void onMessageReceived(RemoteMessage mensajeRemoto){
        super.onMessageReceived(mensajeRemoto);



        if(mensajeRemoto.getNotification() != null){

            mostrarNotiicacion(mensajeRemoto.getNotification());
        }
    }




    //Notificaciones
    private void mostrarNotiicacion(RemoteMessage.Notification notificacion) {
        String tituloNotificacion = "";
        String cuerpoNotificacion = "";
        tituloNotificacion = notificacion.getTitle();
        cuerpoNotificacion = notificacion.getBody();

        Intent intento = new Intent(this, VerNotificaciones.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intentoPendiente = PendingIntent.getActivity(getApplicationContext(),0, intento, PendingIntent.FLAG_ONE_SHOT);

        Uri sonidoNotificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder constructorNotificaciones = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notificaciones)
                .setContentTitle(tituloNotificacion)
                .setContentText(cuerpoNotificacion)
                .setAutoCancel(true)
                .setSound(sonidoNotificacion)
                .setContentIntent(intentoPendiente);

        NotificationManager administradorNotificaciones = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        administradorNotificaciones.notify(0,constructorNotificaciones.build());

        
    }




}
