package appceiot.ceiot.com.br.appceiot;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 */

public class MainActivity extends AppCompatActivity  implements NodeAdapter.ListItemClickListener{

    //Criação das váriaveis.
    private static final int NUMBER_OF_NODES = 20;
    List<String> nodeList = new ArrayList<>();
    //Substituir List View pelo RecyclerView
    //ListView listView;
    //ArrayAdapter<String> adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Node> nodeListRecycler = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicialização da lista de nós
        //for (int n = 0; n <= NUMBER_OF_NODES; n++) {
        //    nodeList.add("Node " + n);
        //}

        /*  ArrayAdapter
            Vinculação dos dados de nodeList ao ListView por meio de um ArrayAdapter.
            Fonte: https://developer.android.com/reference/android/widget/ArrayAdapter.html
            https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
         */
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nodeList);

        //Inicialização do listView
        //listView = (ListView) findViewById(R.id.list_view_example);
        //listView.setAdapter(adapter);

        /*
        Substituir a inicialização do List View
        pela inicializaçao do RecyclerView

        Recycler View:
        Componente que ficará na Activity/Fragment e irá apresentar a lista na tela do usuário,
        assim como outros campos (Button, Text View, etc).


        Fonte:
        https://developer.android.com/guide/topics/ui/layout/recyclerview.html
        https://developer.android.com/training/material/lists-cards.html?hl=pt-br
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        /*
        LayoutManager:
        É o gerenciador de layout. Nele é definido qual será a disposição do itens da lista.
        Neste caso, LinearLayout.
        */
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*
        Adapter:
        Classe responsável por associar a lista com o conteúdo à view correspondente.
        Cada objeto da lista será um item na view. O Adpter também é utilizado para definir se um item
        será exibido ou não.
        */
        mAdapter = new NodeAdapter(nodeListRecycler,this);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Cria as opções do menu na activity.
     * Para isso deve-se sobrescrever o método onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Cria o click do menu na activity
     * Para isso deve-se sobrescrever o método onOptionsItemSelected e fazer a tratativa em código.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            pesquisar();
            return true;
        } else if (itemThatWasClickedId == R.id.action_create) {
            //Nova ação para criar um Node
            criar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        Implementar rotina para efetuar uma pesquisa no firebase.
     */
    public void pesquisar()
    {
        Toast.makeText(this,"Pesquisa OK", Toast.LENGTH_SHORT).show();
        //Cria a url para fazer a requisição para o serviço
        URL searchUrl = RestUtil.buildUrl("nodes",null);
        new NodeSearchTask().execute(searchUrl);
    }

    /*
        AsyncTask
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
            //Converte o resultado JSON
            if (result != null && !result.equals("")) {
                //Substituir o preenchimento do ListView pelo RecyclerView.
//                List<Node> listNodes = RestUtil.parseJSONArray(result);
//                adapter.clear();
//                for (Node node:listNodes) {
//                    adapter.add(node.toString());
//                }
//                adapter.notifyDataSetChanged();

                List<Node> listNodesRecycler = RestUtil.parseJSONArray(result);
                ((NodeAdapter)mAdapter).resetList(listNodesRecycler);

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
     * Criação de rotina para salvar um Node
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


    /**
     * Alterar ação do click para abrir uma nova activity
     * Implementação do método que irá tratar o evento de onclick
     * @param clickedItemIndex
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Node node;
        node = ((NodeAdapter)mAdapter).getItem(clickedItemIndex);

        //Armazena a referência de um contexto em uma variável./
        Context context = MainActivity.this;

        //Seleciona a activity de destino
        Class destinationActivity = NodeActivity.class;

        /*
         * Here, we create the Intent that will start the Activity we specified above in
         * the destinationActivity variable. The constructor for an Intent also requires a
         * context, which we stored in the variable named "context".
         */
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra(Node.NODE_NOME, node.getNome());
        startChildActivityIntent.putExtra(Node.NODE_DESCRICAO, node.getDescricao());
        startChildActivityIntent.putExtra(Node.NODE_LOCALIZACAO, node.getLocalizacao());

        /*
         * Once the Intent has been created, we can use Activity's method, "startActivity"
         * to start the ChildActivity.
         */
        startActivity(startChildActivityIntent);

    }
}
