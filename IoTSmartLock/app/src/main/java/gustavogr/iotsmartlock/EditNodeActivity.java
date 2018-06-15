package gustavogr.iotsmartlock;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNodeActivity extends AppCompatActivity {

    private TextView nameTextView;
    private EditText descEditText;
    private EditText mqttidEditText;

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
        setContentView(R.layout.activity_edit_node);

        Intent intent = getIntent();
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

        nameTextView = (TextView) findViewById(R.id.name_text);
        nameTextView.setText(nome);
        mqttidEditText = (EditText) findViewById(R.id.mqttid_editText);
        mqttidEditText.setText(mqttid);
        descEditText = (EditText) findViewById(R.id.desc_editText);
        descEditText.setText(descricao);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setTitle("Editar Instalação");
        }
    }

    public void onClickEditNode(View view) {

        String description = descEditText.getText().toString();
        String mqttid = mqttidEditText.getText().toString();

        if (description.length() == 0) {
            Toast.makeText(this,"Descrição não pode ser vazia",Toast.LENGTH_LONG);
            return;
        } else if (mqttid.length() == 0) {
            Toast.makeText(this,"MQTT ID não pode ser vazio",Toast.LENGTH_LONG);
            return;
        }

        URL createUrl = RestUtil.buildUrl("nodes/" + nome,null);
        new NodeUpdateTask().execute(createUrl);

        finish();
    }

    public class NodeUpdateTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];

            String description = descEditText.getText().toString();
            String mqttid = mqttidEditText.getText().toString();
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String result = null;
            try {
                result = RestUtil.updateOnFirebase(createUrl,new Node(nome, description, mqttid, lockstatus, alarmstatus, installationstatus, data));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
