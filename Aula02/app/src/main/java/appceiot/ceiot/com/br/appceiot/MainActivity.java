package appceiot.ceiot.com.br.appceiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*TODO: (1) Criação das váriaveis.
    Criar:
    Ums constante que contém a quantidade de nós que será criada
    Uma lista que irá conter a lista de nós
    Um ListView que será a referência da tela aqui na Activity
    */
    private static final int NUMBER_OF_NODES = 10;
    List<String> nodeList = new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: (2) Inicialização da lista de nós
        for (int n = 0; n <= NUMBER_OF_NODES; n++) {
            nodeList.add("Node " + n);
        }

        /*
            TODO: (3) ArrayAdapter
            Como vincular os dados de nodeList ao ListView?

            Deve-se criar um Adapter!

            A forma mais simples é usar o ArrayAdapter. Este adapter converte
            uma ArrayList de objetos em itens View carregados no contêiner ListView.

            O Adapter requer um layout, nesse exemplo utilizamos simple_list_item_1
            (Exemplo padrão Android)

            Fonte: https://developer.android.com/reference/android/widget/ArrayAdapter.html
            https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
         */
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nodeList);

        //TODO: (4) Inicialização do listView
        listView = (ListView) findViewById(R.id.list_view_example);
        listView.setAdapter(adapter);

    }
}
