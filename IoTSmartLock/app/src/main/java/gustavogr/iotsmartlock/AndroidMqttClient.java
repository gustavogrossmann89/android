package gustavogr.iotsmartlock;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class AndroidMqttClient {

    private static final String TAG = "AndroidMqttClient";
    private MqttAndroidClient mqttClient;

    private String brokerURL;
    private String brokerPort;
    Context context;

    AndroidMqttClient(Context context, String brokerURL, String brokerPort)
    {
        this.context = context;
        this.brokerURL = brokerURL;
        this.brokerPort = brokerPort;
        createMqttClient(new MqttCallbackHandler(this.context.getApplicationContext(), "IoT Smart Lock Callback"));
    }

    AndroidMqttClient(Context context, String brokerURL, String brokerPort, MqttCallback mqttCallback)
    {
        this.context = context;
        this.brokerURL = brokerURL;
        this.brokerPort = brokerPort;
        createMqttClient(mqttCallback);
    }

    public MqttAndroidClient createMqttClient(MqttCallback mqttCallback) {
        String clientId = MqttClient.generateClientId();
        this.mqttClient = new MqttAndroidClient(this.context.getApplicationContext(),
                  "tcp://"+this.brokerURL+":"+this.brokerPort,
                   clientId);
        this. mqttClient.setCallback(mqttCallback);
        return this.mqttClient;
    }

    public IMqttToken connect() throws MqttException
    {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setAutomaticReconnect(true);
        IMqttToken token = mqttClient.connect(options);
        token.setActionCallback(new ActionListener("MqttConnect"));
        return token;
    }

    public void disconnect() throws MqttException {
        IMqttToken mqttToken = mqttClient.disconnect();
        mqttToken.setActionCallback(new ActionListener("MqttDisconnect"));
    }

    public void publishMessage(String message, int qos,final String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = message.getBytes("UTF-8");
        MqttMessage encodedMessage = new MqttMessage(encodedPayload);
        mqttClient.publish(topic, encodedMessage);
    }

    public void subscribe(final String topic, int qos) throws MqttException {
        IMqttToken token = mqttClient.subscribe(topic,qos);
        token.setActionCallback(new ActionListener("MqttSubscribe"));
    }

    public void unSubscribe(final String topic) throws MqttException {
        IMqttToken token = mqttClient.unsubscribe(topic);
        token.setActionCallback(new ActionListener("MqttUnSubscribe"));
    }
}