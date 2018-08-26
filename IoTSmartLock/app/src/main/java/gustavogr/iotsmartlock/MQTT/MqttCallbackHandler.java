package gustavogr.iotsmartlock.MQTT;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Classe para utilização do MQTT PAHO
 */
public class MqttCallbackHandler implements MqttCallbackExtended {

    private static final String TAG = "MqttCallbackHandler";
    private Context context;
    private String clientHandle;

    public MqttCallbackHandler(Context context, String clientHandle) {
        super();
        this.context = context;
        this.clientHandle = clientHandle;
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        Log.d(TAG, "connectComplete: "+clientHandle);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(TAG, "connectionLost: "+clientHandle);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "messageArrived: "+clientHandle);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete: " + clientHandle);
    }
}