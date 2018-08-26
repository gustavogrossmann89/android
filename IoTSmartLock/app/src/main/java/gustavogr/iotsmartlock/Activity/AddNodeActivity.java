package gustavogr.iotsmartlock.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import gustavogr.iotsmartlock.Util.Helper;
import gustavogr.iotsmartlock.Model.Node;
import gustavogr.iotsmartlock.R;
import gustavogr.iotsmartlock.Util.RestUtil;

public class AddNodeActivity extends AppCompatActivity {

    private static final String TAG = AddNodeActivity.class.getSimpleName();

    private EditText nameEditText;
    private EditText descEditText;
    private EditText mqttidEditText;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_node);

        Intent intent = getIntent();
        if (intent.hasExtra("userid")) {
            userid = intent.getStringExtra("userid");
        }

        nameEditText = (EditText) findViewById(R.id.name_editText);
        descEditText = (EditText) findViewById(R.id.desc_editText);
        mqttidEditText = (EditText) findViewById(R.id.mqttid_editText);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setTitle("Nova Instalação");
        }
    }

    public void onClickAddNode(View view) {

        String name = nameEditText.getText().toString();
        String description = descEditText.getText().toString();
        String mqttid = mqttidEditText.getText().toString();

        if (name.length() == 0) {
            Helper.displayMessageToast(this, "Nome não pode ser vazio");
            return;
        } else if (description.length() == 0) {
            Helper.displayMessageToast(this, "Descrição não pode ser vazia");
            return;
        } else if (mqttid.length() == 0) {
            Helper.displayMessageToast(this, "MQTT ID não pode ser vazio");
            return;
        }

        URL createUrl = RestUtil.buildUrl("nodes/",null);
        new NodeCreateTask().execute(createUrl);

        finish();
    }

    public class NodeCreateTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];

            String name = nameEditText.getText().toString();
            String description = descEditText.getText().toString();
            String mqttid = mqttidEditText.getText().toString();
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String result = null;
            try {
                result = RestUtil.saveOnFirebase(createUrl,new Node("0", userid, name, description, mqttid, "0", "0", "0", data));
                if(result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    if(jsonResult.has("name") && jsonResult.get("name") != null && !jsonResult.get("name").equals("")){
                        createUrl = RestUtil.buildUrl("nodes/" + jsonResult.get("name").toString(), null);
                        RestUtil.updateOnFirebase(createUrl,new Node(jsonResult.get("name").toString(), userid, name, description, mqttid, "0", "0", "0", data));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
