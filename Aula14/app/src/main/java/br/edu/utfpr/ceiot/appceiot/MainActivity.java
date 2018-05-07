package br.edu.utfpr.ceiot.appceiot;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
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

    TextView textView_result;
    EditText editTextPublishTopic;
    EditText editTextPublishMsg;

    //TODO (6) Criação das entiddades que serão utilizadas para o COAP
    CaliforniumSimpleClient californiumSimpleClient;
    MainCoapHandler mainCoapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_result = (TextView) findViewById(R.id.textView_result);
        editTextPublishTopic = (EditText) findViewById(R.id.editText_publish_topic);
        editTextPublishMsg = (EditText) findViewById(R.id.editText_publish_msg);

        //TODO (7) Inicialização
        californiumSimpleClient = new CaliforniumSimpleClient();
        mainCoapHandler = new MainCoapHandler();

        startService(new Intent(this, CaliforniumCoapServer.class));
    }

    /**
     * TODO (8)
     * Método que irá implementar a ação do botão em tela para realizar um GET
     * @param view
     */
    public void getCoap(View view)
    {
        String topic = editTextPublishTopic.getText().toString();
        new CoapGetTask().execute(topic);
    }

    /**
     * TODO (10)
     * Método que irá implementar a ação do botão em tela para realizar um discovery
     * @param view
     */
    public void discovery(View view)
    {
        String topic = editTextPublishTopic.getText().toString();
        new CoapDiscoveryTask().execute(topic);
    }

    /**
     * TODO (12)
     * Método que irá implementar a ação do botão em tela para realizar um Observer
     * @param view
     */
    public void observe(View view)
    {
        String topic = editTextPublishTopic.getText().toString();
        new CoapObserveTask().execute(topic);
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, CaliforniumCoapServer.class));
        super.onDestroy();
    }

    /**
     * TODO (9)
     * Implementação para execução do GET
     */
    class CoapGetTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            textView_result.setText("");
        }

        protected String doInBackground(String... args) {
            return californiumSimpleClient.get(args[0]);
        }

        protected void onPostExecute(String response) {
            textView_result.setText(response);
        }
    }

    /**
     * TODO (11)
     * Implementação para execução do Discovery
     */
    class CoapDiscoveryTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            textView_result.setText("");
        }

        protected String doInBackground(String... args) {
            return californiumSimpleClient.discovery(args[0]);
        }

        protected void onPostExecute(String response) {
            textView_result.setText(response);
        }
    }

    /**
     * TODO (13)
     * Implementação para execução do Observer
     */
    class CoapObserveTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            textView_result.setText("");
        }

        protected String doInBackground(String... args) {
            return californiumSimpleClient.observe(args[0],mainCoapHandler);
        }

        protected void onPostExecute(String response) {
            textView_result.setText(response);
        }
    }

    /**
     * TODO (14)
     * Tratamento da resposta do observer.
     */
    class MainCoapHandler implements CoapHandler {

        @Override
        public void onLoad(CoapResponse response) {
            textView_result.setText(response.getResponseText());
        }

        @Override
        public void onError() {
            textView_result.setText("onError");
        }
    }
}