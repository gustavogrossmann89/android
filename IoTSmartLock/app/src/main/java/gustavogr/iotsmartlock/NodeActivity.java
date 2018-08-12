package gustavogr.iotsmartlock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NodeActivity extends AppCompatActivity {

    private String mqttServerURL = "iotsmartlock.mooo.com";
    private String mqttServerPort = "1883";

    AndroidMqttClient mqttClient;

    //TextView idTextView;
    //TextView useridTextView;
    TextView nomeTextView;
    TextView descricaoTextView;
    TextView mqttidTextView;
    TextView dataTextView;
    TextView lockStatusTextView;
    TextView alarmStatusTextView;
    TextView installationStatusTextView;
    Switch switchRadioLockBtn;
    Switch switchRadioAlarmBtn;

    String id;
    String userid;
    String nome;
    String descricao;
    String mqttid;
    String lockstatus;
    String alarmstatus;
    String installationstatus;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        Intent intent = getIntent();
        if (intent.hasExtra(Node.NODE_ID)) {
            id = intent.getStringExtra(Node.NODE_ID);
        }
        if (intent.hasExtra(Node.NODE_USERID)) {
            userid = intent.getStringExtra(Node.NODE_USERID);
        }
        if (intent.hasExtra(Node.NODE_NOME)) {
            nome = intent.getStringExtra(Node.NODE_NOME);
        }
        if (intent.hasExtra(Node.NODE_MQTTID)) {
            mqttid = intent.getStringExtra(Node.NODE_MQTTID);
        }
        if (intent.hasExtra(Node.NODE_DESCRICAO)) {
            descricao = intent.getStringExtra(Node.NODE_DESCRICAO);
        }
        if (intent.hasExtra(Node.NODE_LOCKSTATUS)) {
            lockstatus = intent.getStringExtra(Node.NODE_LOCKSTATUS);
        }
        if (intent.hasExtra(Node.NODE_ALARMSTATUS)) {
            alarmstatus = intent.getStringExtra(Node.NODE_ALARMSTATUS);
        }
        if (intent.hasExtra(Node.NODE_INSTALLATIONSTATUS)) {
            installationstatus = intent.getStringExtra(Node.NODE_INSTALLATIONSTATUS);
        }
        if (intent.hasExtra(Node.NODE_DTATUALIZACAO)) {
            data = intent.getStringExtra(Node.NODE_DTATUALIZACAO);
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setTitle(nome);
        }

        refreshScreen();

        try {
            mqttClient = new AndroidMqttClient(this, mqttServerURL, mqttServerPort,
                    new MqttCallBackActivity(this,"NodeActivityMqttCallback"));
            IMqttToken token = mqttClient.connect();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh()
    {
        URL searchUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeRefreshTask().execute(searchUrl);
    }

    public void refreshValues(Node node)
    {
        id = node.getId();
        userid = node.getUserid();
        nome = node.getNome();
        mqttid = node.getMqttid();
        descricao = node.getDescricao();
        data = node.getDtatualizacao();
        lockstatus = node.getLockstatus();
        alarmstatus = node.getAlarmstatus();
        installationstatus = node.getInstallationstatus();
    }

    public void refreshScreen()
    {
        nomeTextView = (TextView) findViewById(R.id.node_nome);
        nomeTextView.setText(nome);
        mqttidTextView = (TextView) findViewById(R.id.node_mqttid);
        mqttidTextView.setText("MQTT ID: " + mqttid);
        descricaoTextView = (TextView) findViewById(R.id.node_descricao);
        descricaoTextView.setText("Descrição: " + descricao);
        dataTextView = (TextView) findViewById(R.id.node_dtatualizacao);
        dataTextView.setText("Última iteração em: " + data);

        lockStatusTextView = (TextView) findViewById(R.id.node_lock_status);
        alarmStatusTextView = (TextView) findViewById(R.id.node_alarm_status);
        installationStatusTextView = (TextView) findViewById(R.id.node_installation_status);

        switchRadioLockBtn = (Switch) findViewById(R.id.node_switchLockBtn);
        switchRadioAlarmBtn = (Switch) findViewById(R.id.node_switchAlarmBtn);

        if(lockstatus.equals("1")){
            switchRadioLockBtn.setChecked(true);
            lockStatusTextView.setText("Trava: ON");
        } else {
            switchRadioLockBtn.setChecked(false);
            lockStatusTextView.setText("Trava: OFF");
        }

        if(alarmstatus.equals("1")){
            switchRadioAlarmBtn.setChecked(true);
            alarmStatusTextView.setText("Alarme: ON");
        } else {
            switchRadioAlarmBtn.setChecked(false);
            alarmStatusTextView.setText("Alarme: OFF");
        }

        if(installationstatus.equals("1")){
            installationStatusTextView.setText("Instalação: FECHADA");
        } else {
            installationStatusTextView.setText("Instalação: ABERTA");
        }
    }

    public void publishLock()
    {
        String status = null;
        if(switchRadioLockBtn.isChecked()) {
            status = switchRadioLockBtn.getTextOn().toString();
        } else {
            status = switchRadioLockBtn.getTextOff().toString();
        }
        try {
            mqttClient.publishMessage(status,0,mqttid + "/lock");
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishAlarm()
    {
        String status = null;
        if(switchRadioAlarmBtn.isChecked()) {
            status = switchRadioAlarmBtn.getTextOn().toString();
        } else {
            status = switchRadioAlarmBtn.getTextOff().toString();
        }
        try {
            mqttClient.publishMessage(status,0,mqttid + "/alarm");
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void changeLockStatus(View view)
    {
        publishLock();
        URL createUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeUpdateTask().execute(createUrl);
        refresh();
    }

    public void changeAlarmStatus(View view)
    {
        publishAlarm();
        URL createUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeUpdateTask().execute(createUrl);
        refresh();
    }

    public void deleteNode()
    {
        URL createUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeDeleteTask().execute(createUrl);
        finish();
    }

    public class NodeRefreshTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String result = null;
            try {
                result = RestUtil.doGet(searchUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.equals("")) {
                Node node = RestUtil.parseJSON(result);
                refreshValues(node);
                refreshScreen();
            } else {
                showErrorMessage();
            }
        }
    }

    public class NodeUpdateTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];

            if(switchRadioLockBtn.isChecked()) {
                lockstatus = switchRadioLockBtn.getTextOn().toString();
            } else {
                lockstatus = switchRadioLockBtn.getTextOff().toString();
            }
            if(switchRadioAlarmBtn.isChecked()) {
                alarmstatus = switchRadioAlarmBtn.getTextOn().toString();
            } else {
                alarmstatus = switchRadioAlarmBtn.getTextOff().toString();
            }
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String result = null;
            try {
                result = RestUtil.updateOnFirebase(createUrl,new Node(id, userid, nome, descricao, mqttid, lockstatus, alarmstatus, installationstatus, data));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public class NodeDeleteTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];

            if(switchRadioLockBtn.isChecked()) {
                lockstatus = switchRadioLockBtn.getTextOn().toString();
            } else {
                lockstatus = switchRadioLockBtn.getTextOff().toString();
            }
            if(switchRadioAlarmBtn.isChecked()) {
                alarmstatus = switchRadioAlarmBtn.getTextOn().toString();
            } else {
                alarmstatus = switchRadioAlarmBtn.getTextOff().toString();
            }

            String result = null;
            try {
                result = RestUtil.deleteOnFirebase(createUrl,new Node(id, userid, nome, descricao, mqttid, lockstatus, alarmstatus, installationstatus, data));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this,"ERRO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_edit) {

            Context context = NodeActivity.this;
            Class destinationActivity = EditNodeActivity.class;
            Intent startChildActivityIntent = new Intent(context, destinationActivity);
            startChildActivityIntent.putExtra(Node.NODE_ID, id);
            startChildActivityIntent.putExtra(Node.NODE_USERID, userid);
            startChildActivityIntent.putExtra(Node.NODE_NOME, nome);
            startChildActivityIntent.putExtra(Node.NODE_DESCRICAO, descricao);
            startChildActivityIntent.putExtra(Node.NODE_MQTTID, mqttid);
            startChildActivityIntent.putExtra(Node.NODE_LOCKSTATUS, lockstatus);
            startChildActivityIntent.putExtra(Node.NODE_ALARMSTATUS, alarmstatus);
            startChildActivityIntent.putExtra(Node.NODE_INSTALLATIONSTATUS, installationstatus);
            startChildActivityIntent.putExtra(Node.NODE_DTATUALIZACAO, data);

            startActivity(startChildActivityIntent);
            return true;
        } else if (itemThatWasClickedId == R.id.action_delete) {
            AlertDialog diaBox = AskOption();
            diaBox.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Excluir")
                .setMessage("Deseja excluir essa instalação?")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteNode();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    public class MqttCallBackActivity extends MqttCallbackHandler {
        public MqttCallBackActivity(Context context, String clientHandle) {
            super(context, clientHandle);
        }
    }
}