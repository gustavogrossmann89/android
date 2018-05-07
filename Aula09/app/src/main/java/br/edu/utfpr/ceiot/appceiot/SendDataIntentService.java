package br.edu.utfpr.ceiot.appceiot;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * TODO (21) Criar um serviço para enviar dados para o firebase.
 */
public class SendDataIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendDataIntentService() {
        super("SendDataIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NodeTasks.executeTask(this,intent);
    }
}
