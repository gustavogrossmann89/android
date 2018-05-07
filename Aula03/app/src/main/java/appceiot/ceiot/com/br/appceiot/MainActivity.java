package appceiot.ceiot.com.br.appceiot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: (0) Criar um menu.
 * Para criar um menu:
 *  1) Clicar com o botão direito na pasta res
 *  2) New => Android Resource Folder
 *  3) Directory Name: menu
 *     Resource type: menu
 *  4) Clicar com o botão direito na pasta res/menu
 *  5) New => Menu Resource File
 *  6) File name: main
 *     Source Set: main
 *     Directory name: menu
 *
 *  Fonte: https://developer.android.com/guide/topics/ui/menus.html?hl=pt-br
 */
public class MainActivity extends AppCompatActivity {

    //Criação das váriaveis.
    private static final int NUMBER_OF_NODES = 20;
    List<String> nodeList = new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicialização da lista de nós
        for (int n = 0; n <= NUMBER_OF_NODES; n++) {
            nodeList.add("Node " + n);
        }

        /*  ArrayAdapter
            Vinculação dos dados de nodeList ao ListView por meio de um ArrayAdapter.
            Fonte: https://developer.android.com/reference/android/widget/ArrayAdapter.html
            https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
         */
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nodeList);

        //Inicialização do listView
        listView = (ListView) findViewById(R.id.list_view_example);
        listView.setAdapter(adapter);
    }

    /*
        TODO (2) Criar o menu na activity
        Para isso deve-se sobrescrever o método onCreateOptionsMenu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /*
        TODO (3) Criar o click do menu na activity
        Para isso deve-se sobrescrever o método onOptionsItemSelected e fazer a tratativa em código.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            pesquisar();
            return true;
        } else if (itemThatWasClickedId == R.id.action_create) {
            //TODO (11): Nova ação para criar um Node
            criar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        TODO: (4) Implementar a pesquisa.
     */
    public void pesquisar()
    {
        /*
            https://developer.android.com/guide/topics/ui/notifiers/toasts.html
         */
        Toast.makeText(this,"Pesquisa OK", Toast.LENGTH_SHORT).show();

        //TODO (6): Cria a url para fazer a requisição para o serviço
        URL searchUrl = RestUtil.buildUrl("nodes",null);
        new NodeSearchTask().execute(searchUrl);
    }

    /*
        TODO: (7) AsyncTask
        Permite criar Threads.
        Permite executar operações em segundo plano e publicar resultados na Activity/UI sem ter
        que manipular threads diretamente
        .
        Fonte:https://developer.android.com/reference/android/os/AsyncTask.html
     */
    public class NodeSearchTask extends AsyncTask<URL, Void, String> {

        // Sobreecrever o método onPreExecute
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //Método que realiza a tarefa principal
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

        /* Sobreecrever o método onPostExecute
         a tarefa */
        @Override
        protected void onPostExecute(String result) {
            //TODO (9) Converte o resultado JSON
            if (result != null && !result.equals("")) {
                List<Node> listNodes = RestUtil.parseJSONArray(result);
                adapter.clear();
                for (Node node:listNodes) {
                    adapter.add(node.toString());
                }
                adapter.notifyDataSetChanged();
            } else {
                //Apresenta uma mensagem de erro caso não apresente resultados
                showErrorMessage();
            }
        }

        /**
         * Chamado para atualizar informações durante a execução da atividade principal.
         * Executada quando após chamar o método publishProgress(Progress...)
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * Chamados após uma ação para cancelar a execução da thread.
         * @param s
         */
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void showErrorMessage()
    {
        Toast.makeText(this,"ERRO", Toast.LENGTH_SHORT).show();
    }

    /**
     * TODO (10): Criação de rotina para salvar um Node
     */
    private void criar()
    {
        Toast.makeText(this,"Criar", Toast.LENGTH_SHORT).show();

        //Cria a url para fazer a requisição para o serviço
        URL createUrl = RestUtil.buildUrl("nodes",null);
        new NodeCreateTask().execute(createUrl);
    }

    /**
     * Cria uma Thread para não travar a aplicação.
     */
    public class NodeCreateTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];
            String result = null;
            try {
                result = RestUtil.saveOnFirebase(createUrl,new Node("A","B","C"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            showCreateResult(result);
        }
    }

    /**
     * Método criado para apresentar o resultadado da requisição POST
     * @param value
     */
    private void showCreateResult(String value)
    {
        Toast.makeText(this,value, Toast.LENGTH_SHORT).show();
    }
}
