package br.edu.utfpr.ceiot.appceiot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat.Action;

/**
 * TODO (27) Exemplo de notificação
 * Esta classe implementa rotinas para envio de notificações.
 *
 * https://developer.android.com/training/notify-user/build-notification.html
 */
public class NotificationUtil {


    private static final String NOTIFICATION_CHANNEL_ID = "notification.channel";
    private static final int APPCEIOT_NOTIFICATION_CHANNEL_ID = 1199;
    private static final int ACTION_CLEAR_NOTIF = 1;

    /**
     *
     * @param context
     * @param msg
     */
    public static void notification(Context context, String msg) {
        //Recupera Serviço para notificação
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Cria um canal para envio de notificações.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        //Constroi a notificação
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Notification Example")
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Style Example"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))//Ao clicar vai para a atividade inicial
                .addAction(ignoreAction(context))//Opções
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(APPCEIOT_NOTIFICATION_CHANNEL_ID, notificationBuilder.build());
    }

    /*
        Recupera icone
     */
    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_launcher_background);
        return largeIcon;
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                APPCEIOT_NOTIFICATION_CHANNEL_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Action ignoreAction(Context context) {
        Intent ignoreIntent = new Intent(context, SendDataIntentService.class);
        ignoreIntent.setAction(NodeTasks.ACTION_DO_NOTHING);
        PendingIntent ignorePendingIntent = PendingIntent.getService(
                context,
                ACTION_CLEAR_NOTIF,
                ignoreIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Action ignoreAction = new NotificationCompat.Action(R.drawable.ic_launcher_background,
                "Ignore",
                ignorePendingIntent);
        return ignoreAction;
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
