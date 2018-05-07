package gustavogr.iotdevicecontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNodeActivity extends AppCompatActivity {

    private TextView nameTextView;
    private EditText descEditText;
    private EditText topicomqttEditText;

    String nome;
    String descricao;
    String topicomqtt;
    String status;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_node);

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

        nameTextView = (TextView) findViewById(R.id.name_text);
        nameTextView.setText(nome);
        topicomqttEditText = (EditText) findViewById(R.id.topicomqtt_editText);
        topicomqttEditText.setText(topicomqtt);
        descEditText = (EditText) findViewById(R.id.desc_editText);
        descEditText.setText(descricao);
    }

    public void onClickEditNode(View view) {

        String description = descEditText.getText().toString();
        String topicomqtt = topicomqttEditText.getText().toString();

        if (description.length() == 0) {
            Toast.makeText(this,"Descrição não pode ser vazia",Toast.LENGTH_LONG);
            return;
        } else if (topicomqtt.length() == 0) {
            Toast.makeText(this,"Tópico MQTT não pode ser vazio",Toast.LENGTH_LONG);
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
            String topicomqtt = topicomqttEditText.getText().toString();
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String result = null;
            try {
                result = RestUtil.updateOnFirebase(createUrl,new Node(nome, description, topicomqtt, status, data));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
