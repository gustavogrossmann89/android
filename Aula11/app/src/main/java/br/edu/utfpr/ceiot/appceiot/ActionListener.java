package br.edu.utfpr.ceiot.appceiot;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * TODO (3) Criar classe  ActionListener que implementa IMqttActionListener
 * Esta classe é  utilizada por tratar mensagens de sucesso ou falha para a biblioteca Mqtt
 */
public class ActionListener implements IMqttActionListener {

    private static final String TAG = "ActionListener";
    private String name;

    public ActionListener(String name)
    {
        super();
        this.name = name;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.d(TAG, "onSuccess: "+name);
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.d(TAG, "onFailure: "+name);
    }
}
