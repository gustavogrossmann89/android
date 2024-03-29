package gustavogr.iotsmartlock.MQTT;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Classe para utilização do MQTT PAHO
 */
public class ActionListener implements IMqttActionListener {

    private static final String TAG = "ActionListener";
    private String name;

    public ActionListener(String name) {
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
