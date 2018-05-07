package gustavogr.iotdevicecontrol;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NodeActivity extends AppCompatActivity {

    AndroidMqttClient mqttClient;

    TextView nomeTextView;
    TextView topicoTextView;
    TextView descricaoTextView;
    TextView dataTextView;
    TextView statusTextView;
    Switch switchRadioBtn;

    String nome;
    String descricao;
    String topicomqtt;
    String status;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        Intent intent = getIntent();
        if (intent.hasExtra(Node.NODE_NOME)) {
            nome = intent.getStringExtra(Node.NODE_NOME);
        }
        if (intent.hasExtra(Node.NODE_TOPICOMQTT)) {
            topicomqtt = intent.getStringExtra(Node.NODE_TOPICOMQTT);
        }
        if (intent.hasExtra(Node.NODE_DESCRICAO)) {
            descricao = intent.getStringExtra(Node.NODE_DESCRICAO);
        }
        if (intent.hasExtra(Node.NODE_STATUS)) {
            status = intent.getStringExtra(Node.NODE_STATUS);
        }
        if (intent.hasExtra(Node.NODE_DTATUALIZACAO)) {
            data = intent.getStringExtra(Node.NODE_DTATUALIZACAO);
        }

        refreshScreen();

        try {
            mqttClient = new AndroidMqttClient(this,
                    "192.168.100.56",
                    "1883",
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
        URL searchUrl = RestUtil.buildUrl("nodes/" + nome,null);
        new NodeActivity.NodeRefreshTask().execute(searchUrl);
    }

    public void refreshValues(Node node)
    {
        nome = node.getNome();
        topicomqtt = node.getTopicomqtt();
        descricao = node.getDescricao();
        data = node.getDtatualizacao();
        status = node.getStatus();
    }

    public void refreshScreen()
    {
        nomeTextView = (TextView) findViewById(R.id.node_nome);
        nomeTextView.setText(nome);
        topicoTextView = (TextView) findViewById(R.id.node_topicomqtt);
        topicoTextView.setText("Tópico: " + topicomqtt);
        descricaoTextView = (TextView) findViewById(R.id.node_descricao);
        descricaoTextView.setText("Descrição: " + descricao);
        dataTextView = (TextView) findViewById(R.id.node_dtatualizacao);
        dataTextView.setText("Última iteração em: " + data);

        statusTextView = (TextView) findViewById(R.id.node_status);
        switchRadioBtn = (Switch) findViewById(R.id.node_switchBtn);

        if(status.equals("1")){
            switchRadioBtn.setChecked(true);
            statusTextView.setText("Status: ON");
        } else {
            switchRadioBtn.setChecked(false);
            statusTextView.setText("Status: OFF");
        }
    }

    public void publish()
    {
        String status = null;
        if(switchRadioBtn.isChecked()) {
            status = switchRadioBtn.getTextOn().toString();
        } else {
            status = switchRadioBtn.getTextOff().toString();
        }
        try {
            mqttClient.publishMessage(status,0,topicomqtt);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void changeStatus(View view)
    {
        publish();
        URL createUrl = RestUtil.buildUrl("nodes/" + nome,null);
        new NodeActivity.NodeUpdateTask().execute(createUrl);
        refresh();
    }

    public void deleteNode()
    {
        URL createUrl = RestUtil.buildUrl("nodes/" + nome,null);
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

            if(switchRadioBtn.isChecked()) {
                status = switchRadioBtn.getTextOn().toString();
            } else {
                status = switchRadioBtn.getTextOff().toString();
            }
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String result = null;
            try {
                result = RestUtil.updateOnFirebase(createUrl,new Node(nome, descricao, topicomqtt, status, data));
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

            if(switchRadioBtn.isChecked()) {
                status = switchRadioBtn.getTextOn().toString();
            } else {
                status = switchRadioBtn.getTextOff().toString();
            }

            String result = null;
            try {
                result = RestUtil.deleteOnFirebase(createUrl,new Node(nome, descricao, topicomqtt, status, data));
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
            startChildActivityIntent.putExtra(Node.NODE_NOME, nome);
            startChildActivityIntent.putExtra(Node.NODE_DESCRICAO, descricao);
            startChildActivityIntent.putExtra(Node.NODE_TOPICOMQTT, topicomqtt);
            startChildActivityIntent.putExtra(Node.NODE_STATUS, status);
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
                .setMessage("Deseja excluir esse sensor?")
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
