package br.edu.utfpr.ceiot.appceiot;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * Criar classe para unificar métodos relacionados ao MQTT
 */
public class AndroidMqttClient {

    private static final String TAG = "AndroidMqttClient";
    private MqttAndroidClient mqttClient;

    private String brokerURL;
    private String brokerPort;
    Context context;

    /**
     * Contrutora que inicia serviço para um determinado broker.
     * @param context
     * @param brokerURL
     * @param brokerPort
     */
    AndroidMqttClient(Context context,String brokerURL, String brokerPort)
    {
        this.context = context;
        this.brokerURL = brokerURL;
        this.brokerPort = brokerPort;
        createMqttClient(new MqttCallbackHandler(this.context.getApplicationContext(), "AppCEIOT Callback"));
    }

    AndroidMqttClient(Context context, String brokerURL, String brokerPort, MqttCallback mqttCallback)
    {
        this.context = context;
        this.brokerURL = brokerURL;
        this.brokerPort = brokerPort;
        createMqttClient(mqttCallback);
    }

    /**
     * Inicializa client.
     * @return
     */
    public MqttAndroidClient createMqttClient(MqttCallback mqttCallback) {
        String clientId = MqttClient.generateClientId();
        this.mqttClient = new MqttAndroidClient(this.context.getApplicationContext(),
                  "tcp://"+this.brokerURL+":"+this.brokerPort,
                   clientId);
        this. mqttClient.setCallback(mqttCallback);
        return this.mqttClient;
    }

    /**
     * Realiza conexão
     * @return
     * @throws MqttException
     */
    public IMqttToken connect() throws MqttException
    {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setAutomaticReconnect(true);
        IMqttToken token = mqttClient.connect(options);
        token.setActionCallback(new ActionListener("MqttConnect"));
        return token;
    }

    /**
     * Implementa desconexão
     * @throws MqttException
     */
    public void disconnect() throws MqttException {
        IMqttToken mqttToken = mqttClient.disconnect();
        mqttToken.setActionCallback(new ActionListener("MqttDisconnect"));
    }

    /**
     * Publica uma mensagem no broker
     * @param message
     * @param qos
     * @param topic
     * @throws MqttException
     * @throws UnsupportedEncodingException
     */
    public void publishMessage(String message, int qos,final String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = message.getBytes("UTF-8");
        MqttMessage encodedMessage = new MqttMessage(encodedPayload);
        mqttClient.publish(topic, encodedMessage);
    }

    /**
     * Se inscreve para escutar um determinado tópico
     * @param topic
     * @param qos
     * @throws MqttException
     */
    public void subscribe(final String topic, int qos) throws MqttException {
        IMqttToken token = mqttClient.subscribe(topic,qos);
        token.setActionCallback(new ActionListener("MqttSubscribe"));
    }

    /**
     * Cancela inscrição em um determinado tópico
     * @param topic
     * @throws MqttException
     */
    public void unSubscribe(final String topic) throws MqttException {
        IMqttToken token = mqttClient.unsubscribe(topic);
        token.setActionCallback(new ActionListener("MqttUnSubscribe"));
    }

}
