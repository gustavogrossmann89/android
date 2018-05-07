package br.edu.utfpr.ceiot.appceiot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * TODO (22) Criar classe Node Taks para implementação dos serviços
 */
public class NodeTasks {

    public static final String ACTION_SEND_NODE_TO_FIREBASE = "action.send.node.to.firebase";
    public static final String ACTION_DO_NOTHING = "action.do.nothing";
    public static final String ACTION_SEARCH_ON_FIREBASE = "action.search.on.firebase";

    public static void executeTask(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_SEND_NODE_TO_FIREBASE.equals(action)) {
            String name = "";
            String description = "";
            if (intent.hasExtra(NodeContract.Node.COLUMN_NAME)) {
                name = intent.getStringExtra(NodeContract.Node.COLUMN_NAME);
            }
            if (intent.hasExtra(NodeContract.Node.COLUMN_DESCRIPTION)) {
                description = intent.getStringExtra(NodeContract.Node.COLUMN_DESCRIPTION);
            }
            if (intent.hasExtra(NodeContract.Node.COLUMN_DESCRIPTION)) {
                description = intent.getStringExtra(NodeContract.Node.COLUMN_DESCRIPTION);
            }
            sendToFirebase(context,name,description);
        } else if (ACTION_DO_NOTHING.equals(action)) {
            NotificationUtil.clearAllNotifications(context);
        }
    }
    public static void executeTask(Context context, String action) {
         if (ACTION_SEARCH_ON_FIREBASE.equals(action)) {
            getFromFirebase(context);
        }
    }

    /**
     * Implementa a função para enviar o item para o FireBase
     */
    public static void sendToFirebase(Context context ,String name, String description)
    {
        //Monta URL
        URL createUrl = RestUtil.buildUrl("nodes",null);
        //Envia para a base
        String result = RestUtil.saveOnFirebase(createUrl,name,description);
        NotificationUtil.notification(context,result);
    }

    /**
     * TODO (29) Método para buscar nodes no firebase
     */
    public static void getFromFirebase(Context context)
    {
        try {
            URL searchUrl = RestUtil.buildUrl("nodes", null);
            String result = RestUtil.doGet(searchUrl);
            if(result != null && !result.isEmpty()) {
                context.getContentResolver().delete(NodeContract.Node.CONTENT_URI, null, null);

                //Recupera itens
                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = jsonObject.names();
                for (int i = 0; i <jsonArray.length(); i++) {
                    JSONObject obj = jsonObject.getJSONObject(jsonArray.getString(i));
                    String name = obj.getString("name");
                    String description = obj.getString("description");

                    // Inserir dados por meio do contentProvider
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NodeContract.Node.COLUMN_NAME, name);
                    contentValues.put(NodeContract.Node.COLUMN_DESCRIPTION, description);
                    Uri uri = context.getContentResolver().insert(NodeContract.Node.CONTENT_URI, contentValues);
                }
            }
            //Notifica
            NotificationUtil.notification(context,"Nodes updated");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
