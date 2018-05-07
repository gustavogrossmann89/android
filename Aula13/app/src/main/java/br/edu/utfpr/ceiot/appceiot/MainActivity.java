package br.edu.utfpr.ceiot.appceiot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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


    //TODO (2) Declaração dos componentes para trabalhar com localização
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_result = (TextView) findViewById(R.id.textView_result);
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

        //TODO (3) Inicializa LocationServices
        // Recupera a última localização conhecida
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        //TODO (11) Configurar serviço de localização
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO (4) Verifica permissões do usuário
        if (checkPermissions()) {
            getLastLocation();
            //TODO (12) Iniciar serviço de atualização de localização
            startLocationUpdates();
        }
    }

    /**
     * TODO (5) Retorna o status de permissão para localização
     * @return
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    //TODO (6) Recupera localização
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    textView_result.setText(location.toString());
                }
            }
        });
    }

    /**
     * //TODO (7) Criar callback da atualização da localização
     * Creates a callback for receiving location events.
     */
    public void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                StringBuilder locationStr = new StringBuilder();
                for (Location location : locationResult.getLocations()) {
                    locationStr.append(location.toString() + "\r\n");
                }
                textView_result.setText(locationStr.toString());
            }
        };
    }

    /**
     * TODO (8) Configura requests para localização
     */
    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }

    /**
     * TODO (9) Inicia configurações
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * TODO (10) Cadastra para escutar alterações na localização
     */
    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "addOnFailureListener");
                    }
                });
    }

    /**
     * TODO (13) Remover serviço de atualziação
     */
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
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