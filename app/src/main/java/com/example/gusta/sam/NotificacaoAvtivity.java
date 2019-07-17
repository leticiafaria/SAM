package com.example.gusta.sam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;

import java.util.Locale;

public class NotificacaoAvtivity extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {


        String msg = "É Hora de tomar o remédio!!";
        String titulo = "SAM - Sistema de Administração de Medicamentos";

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(context);

        notificacao.setContentTitle(titulo);
        notificacao.setContentText(msg);
        notificacao.setPriority(Notification.PRIORITY_HIGH);

        notificacao.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_icone));
        notificacao.setSmallIcon(R.drawable.ic_alerta);

        Intent tarefaintent = new Intent(context, NotificacaoAvtivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, tarefaintent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificacao.setAutoCancel(true);
        notificacao.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = notificacao.build();
        n.vibrate = new long[]{150, 300, 150, 300};

        notificationManager.notify(9001, n);

        try {
            Uri audio = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, audio);
            toque.play();
        }catch (Exception e){

        }

    }
}
