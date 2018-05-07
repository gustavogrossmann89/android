package gustavogr.iotdevicecontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.net.URL;

public class NodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        Intent intent = getIntent();
        String nome = "";
        String descricao = "";
        String topicomqtt = "";
        String status = "";

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

        NodeInfoFragment nodeInfoFragment = (NodeInfoFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (nodeInfoFragment != null) {
            NodeInfoFragment newFragment = NodeInfoFragment.newInstance(nome,descricao,topicomqtt, status);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            NodeInfoFragment newFragment = NodeInfoFragment.newInstance(nome,descricao,topicomqtt, status);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void publish(View view)
    {
        URL createUrl = RestUtil.buildUrl("nodes",null);
        new NodeActivity.NodeCreateTask().execute(createUrl);
    }

    public class NodeCreateTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];

            Intent intent = getIntent();
            String nome = "";
            String descricao = "";
            String topicomqtt = "";
            String status = "";

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
            String result = null;
            try {
                result = RestUtil.saveOnFirebase(createUrl,new Node(nome, descricao, topicomqtt, status));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
