package com.example.gustavo.exemploaula3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final static String TAG = "MainActivity";

    AndroidMqttClient mqttClient;

    TextView textView_result;
    TextView textView_result2;
    TextView textView_result3;
    TextView textView_result4;
    Switch switchRadioBtn;
    Switch switchRadioBtn2;
    EditText editTextPublishTopic;
    EditText editTextPublishMsg;
    EditText editTextSubscribeTopic;

    //TODO (1) Declarar gerenciador
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mSensorLight;
    private Sensor mSensorTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_result = (TextView) findViewById(R.id.textView_result);
        textView_result2 = (TextView) findViewById(R.id.textView_result2);
        textView_result3 = (TextView) findViewById(R.id.textView_result3);
        textView_result4 = (TextView) findViewById(R.id.textView_result4);
        switchRadioBtn = (Switch) findViewById(R.id.switch1);
        switchRadioBtn2 = (Switch) findViewById(R.id.switch2);
        //editTextPublishTopic = (EditText) findViewById(R.id.editText_publish_topic);
        //editTextPublishMsg = (EditText) findViewById(R.id.editText_publish_msg);
        //editTextSubscribeTopic = (EditText) findViewById(R.id.editText_subscribe_topic);

        try {
            mqttClient = new AndroidMqttClient(this,
                    "192.168.100.56",
                    "1883",
                    new MqttCallBackActivity(this,"MainActivityMqttCallback"));
            IMqttToken token = mqttClient.connect();

        } catch (MqttException e) {
            e.printStackTrace();
        }

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

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            // Success!
            mSensorTest = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        else {
            mSensorTest = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorTest,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    /**
     * @param view
     */
    public void publishCooler(View view)
    {
        String message = null;
        if(switchRadioBtn.isChecked()) {
            message = switchRadioBtn.getTextOn().toString();
        } else {
            message = switchRadioBtn.getTextOff().toString();
        }
        String topic = "00124B000F28C303/cool";
        try {
            mqttClient.publishMessage(message,0,topic);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param view
     */
    public void publishLeds(View view)
    {
        String message = null;
        if(switchRadioBtn2.isChecked()) {
            message = switchRadioBtn2.getTextOn().toString();
        } else {
            message = switchRadioBtn2.getTextOff().toString();
        }
        String topic = "00124B000F28C303/leds";
        try {
            mqttClient.publishMessage(message,0,topic);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param view
     */
    public void subscribe(View view)
    {
        String topic = "00124B000F28C303/cool";
        try {
            mqttClient.subscribe(topic,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        topic = "00124B000F28C303/leds";
        try {
            mqttClient.subscribe(topic,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param view
     */
    public void unsubscribe(View view)
    {
        String topic = "00124B000F28C303/cool";
        try {
            mqttClient.unSubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        topic = "00124B000F28C303/leds";
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
            textView_result2.setText(ACCELEROMETER);
        } else if(Sensor.TYPE_LIGHT == event.sensor.getType()) {
            String LIGHT = "Lux: " + event.values[0] + "\r\n";
            textView_result3.setText(LIGHT);
        } else if(Sensor.TYPE_AMBIENT_TEMPERATURE == event.sensor.getType()) {
            String TEMPERATURE = "Temp.: " + event.values[0] + "\r\n";
            textView_result4.setText(TEMPERATURE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

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
