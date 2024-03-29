package gustavogr.iotdevicecontrol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NodeAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NodeAdapter mAdapter;
    List<Node> nodeListRecycler = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNodes);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NodeAdapter(nodeListRecycler,this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddNodeActivity.class);
                startActivity(addTaskIntent);
            }
        });

        atualizar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_refresh) {
            atualizar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void atualizar()
    {
        Toast.makeText(this,"Atualizando lista de sensores", Toast.LENGTH_SHORT).show();
        URL searchUrl = RestUtil.buildUrl("nodes",null);
        new NodeSearchTask().execute(searchUrl);
    }

    public class NodeSearchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

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
                List<Node> listNodesRecycler = RestUtil.parseJSONArray(result);
                ((NodeAdapter)mAdapter).resetList(listNodesRecycler);

            } else {
                showErrorMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this,"ERRO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Node node;
        node = ((NodeAdapter)mAdapter).getItem(clickedItemIndex);

        Context context = MainActivity.this;
        Class destinationActivity = NodeActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra(Node.NODE_NOME, node.getNome());
        startChildActivityIntent.putExtra(Node.NODE_DESCRICAO, node.getDescricao());
        startChildActivityIntent.putExtra(Node.NODE_TOPICOMQTT, node.getTopicomqtt());
        startChildActivityIntent.putExtra(Node.NODE_STATUS, node.getStatus());

        startActivity(startChildActivityIntent);

    }
}
