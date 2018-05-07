package br.edu.utfpr.ceiot.appceiot;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    AndroidMqttClient mqttClient;

    TextView textView_result;
    EditText editTextPublishTopic;
    EditText editTextPublishMsg;
    EditText editTextSubscribeTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_result = (TextView) findViewById(R.id.textView_result);
        editTextPublishTopic = (EditText) findViewById(R.id.editText_publish_topic);
        editTextPublishMsg = (EditText) findViewById(R.id.editText_publish_msg);
        editTextSubscribeTopic = (EditText) findViewById(R.id.editText_subscribe_topic);

        //TODO (6) Inicializa MQTT Client
        try {
            mqttClient = new AndroidMqttClient(this,
                                               "iot.eclipse.org",
                                               "1883",
                    new MqttCallBackActivity(this,"MainActivityMqttCallback"));
            IMqttToken token = mqttClient.connect();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO (7) Implementar publish
     * @param view
     */
    public void publish(View view)
    {
        String topic = editTextPublishTopic.getText().toString();
        String message = editTextPublishMsg.getText().toString();
        try {
            mqttClient.publishMessage(message,0,topic);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO (8) Implementar subscribe
     * @param view
     */
    public void subscribe(View view)
    {
        String topic = editTextSubscribeTopic.getText().toString();
        try {
            mqttClient.subscribe(topic,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO (9) Implementar unsubscribe
     * @param view
     */
    public void unsubscribe(View view)
    {
        String topic = editTextSubscribeTopic.getText().toString();
        try {
            mqttClient.unSubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * TODO (10) Estender MqttCallBackActivity para tratar mensagens recebidas na MainActivity
     */
    public class MqttCallBackActivity extends MqttCallbackHandler {
        public MqttCallBackActivity(Context context, String clientHandle) {
            super(context, clientHandle);
        }
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            super.messageArrived(topic,message);
            textView_result.setText(topic+":"+message);
        }
    }
}