package gustavogr.iotsmartlock.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gustavogr.iotsmartlock.Model.ActionLog;
import gustavogr.iotsmartlock.R;
import gustavogr.iotsmartlock.Util.LogAdapter;
import gustavogr.iotsmartlock.Util.RestUtil;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Atividade que gera os relatórios referentes à uma instalação
 */
public class ReportActivity extends AppCompatActivity implements LogAdapter.ListItemClickListener{

    String nodeid;
    String nome;
    Integer reportId = 0;

    List<ActionLog> listLogRecycler = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LogAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        if (intent.hasExtra("nodeid")) {
            nodeid = intent.getStringExtra("nodeid");
        }
        if (intent.hasExtra("nome")) {
            nome = intent.getStringExtra("nome");
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setTitle(nome);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNodes);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LogAdapter(listLogRecycler, this);
        mRecyclerView.setAdapter(mAdapter);

        searchLogsByNodeId(nodeid);
    }

    public void displayReport(Integer reportId){

        if (reportId == R.id.action_report1 || reportId == R.id.action_report2 || reportId == R.id.action_report3) {
            ActionBar ab = getSupportActionBar();
            List<ActionLog> listShow = new ArrayList<>();

            if (reportId == R.id.action_report1) {
                if (ab != null) {
                    ab.setTitle("Detalhes das aberturas");
                }
                for (ActionLog log : listLogRecycler) {
                    if (log.getTopic().equals("open")) {
                        listShow.add(log);
                    }
                }
            } else if (reportId == R.id.action_report2) {
                if (ab != null) {
                    ab.setTitle("Disparos do alarme");
                }
                for (ActionLog log : listLogRecycler) {
                    if (log.getTopic().equals("alert")) {
                        listShow.add(log);
                    }
                }
            } else if (reportId == R.id.action_report3) {
                if (ab != null) {
                    ab.setTitle("Momentos esquecida aberta");
                }
                for (ActionLog log : listLogRecycler) {
                    if (log.getTopic().equals("leave")) {
                        listShow.add(log);
                    }
                }
            }
            ((LogAdapter) mAdapter).resetList(listShow);
        }
    }

    public void searchLogsByNodeId(String nodeid) {
       URL searchUrl = RestUtil.buildUrl("logs","orderBy=\"node\"&equalTo=\"" + nodeid + "\"");
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
                listLogRecycler = RestUtil.parseActionLogJSONArray(result);
                if(reportId == 0){
                    displayReport(R.id.action_report1);
                }
            } else {
                showErrorMessage();
            }
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this,"ERRO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        reportId = itemThatWasClickedId;
        displayReport(itemThatWasClickedId);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {}
}