package br.edu.utfpr.ceiot.appceiot;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import java.util.List;

/**
 * TODO (3) Implementar SensorEventListener
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private final static String TAG = "MainActivity";

    AndroidMqttClient mqttClient;

    TextView textView_result;
    TextView textView_result2;
    EditText editTextPublishTopic;
    EditText editTextPublishMsg;
    EditText editTextSubscribeTopic;

    //TODO (1) Declarar gerenciador
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mSensorLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_result = (TextView) findViewById(R.id.textView_result);
        textView_result2 = (TextView) findViewById(R.id.textView_result2);
        editTextPublishTopic = (EditText) findViewById(R.id.editText_publish_topic);
        editTextPublishMsg = (EditText) findViewById(R.id.editText_publish_msg);
        editTextSubscribeTopic = (EditText) findViewById(R.id.editText_subscribe_topic);

        //Inicializa MQTT Client
        try {
            mqttClient = new AndroidMqttClient(this,
                                               "iot.eclipse.org",
                                               "1883",
                    new MqttCallBackActivity(this,"MainActivityMqttCallback"));
            IMqttToken token = mqttClient.connect();

        } catch (MqttException e) {
            e.printStackTrace();
        }

        //TODO (2) Verificar a existencia de um sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

//        StringBuilder sensorList = new StringBuilder();
//        List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        for(Sensor sensor: list){
//            sensorList.append(sensor.toString()).append("\r\n");
//        }
//        textView_result.setText(sensorList);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            // Success!
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else {
            mSensor = null;
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            // Success!
            mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        else {
            mSensorLight = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorLight,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    /**
     * Implementar publish
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
     * Implementar subscribe
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
     * Implementar unsubscribe
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        //TODO (4) Recupera valor
        if(Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
            String ACCELEROMETER = "";
            ACCELEROMETER = ACCELEROMETER + "x axis: " + event.values[0] + "\r\n";
            ACCELEROMETER = ACCELEROMETER + "y axis: " + event.values[1] + "\r\n";
            ACCELEROMETER = ACCELEROMETER + "z axis: " + event.values[2] + "\r\n";
            textView_result.setText(ACCELEROMETER);
        } else if(Sensor.TYPE_LIGHT == event.sensor.getType()) {
            String LIGHT = "Lux: " + event.values[0] + "\r\n";
            textView_result2.setText(LIGHT);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }


    /**
     * Estender MqttCallBackActivity para tratar mensagens recebidas na MainActivity
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