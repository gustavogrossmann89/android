package appceiot.ceiot.com.br.appceiot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Para criar uma nova activity:
 *  1) Clicar com o botão direito nas pastas que contém código
 *  2) New => Activity => EmptyActivity
 *  3) Activity Name: NodeActivity
 *     Layout name: activity_node
 */
public class NodeActivity extends FragmentActivity {

    //TODO (2) Retirado os componentes de NodeActivity
//    TextView nomeTextView;
//    TextView localizacaoTextView;
//    TextView descricaoTextView;
//    Button goToMapsButton;
//    Button goToWebButton;
//    Button goToCameraButton;

    Button alternarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        //TODO (3) Retirado os componentes de NodeActivity
//        //Inicialização dos TextViews
//        nomeTextView = (TextView) findViewById(R.id.node_nome);
//        localizacaoTextView = (TextView) findViewById(R.id.node_localizacao);
//        descricaoTextView = (TextView) findViewById(R.id.node_descricao);
//
        //Recuperar informações enviadas

        Intent intent = getIntent();
        String nome = "";
        String descricao = "";
        String localizacao = "";

        if (intent.hasExtra(Node.NODE_NOME)) {
            nome = intent.getStringExtra(Node.NODE_NOME);
        }
        if (intent.hasExtra(Node.NODE_LOCALIZACAO)) {
            localizacao = intent.getStringExtra(Node.NODE_LOCALIZACAO);
        }
        if (intent.hasExtra(Node.NODE_DESCRICAO)) {
            descricao = intent.getStringExtra(Node.NODE_DESCRICAO);
        }
//
//        //Adição de outras funcionalidades
//        goToMapsButton = (Button) findViewById(R.id.button_go_to_maps);
//        goToMapsButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                goToMaps();
//            }
//        });
//        goToWebButton = (Button) findViewById(R.id.button_go_to_web);
//        goToWebButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                goToWeb();
//            }
//        });
//        goToCameraButton = (Button) findViewById(R.id.button_go_to_camera);
//        goToCameraButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                goToCamera();
//            }
//        });

        // Inicialização do fragmento
        NodeInfoFragment nodeInfoFragment = (NodeInfoFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (nodeInfoFragment != null) {
            NodeInfoFragment newFragment = NodeInfoFragment.newInstance(nome,descricao,localizacao);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            // Create fragment and give it an argument for the selected article
            NodeInfoFragment newFragment = NodeInfoFragment.newInstance(nome,descricao,localizacao);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        alternarButton = (Button) findViewById(R.id.button_node);
        alternarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OK();
            }
        });
    }

    //TODO (4) Retirado os componentes de NodeActivity
//    /**
//     * Implemtentação do método para navegar para o maps.
//     * Fonte: https://developers.google.com/maps/documentation/android-api/intents?hl=pt-br
//     */
//    public void goToMaps() {
//        //Cria URI
//        String addressString = localizacaoTextView.getText().toString();
//        Uri addressUri = Uri.parse("geo:0,0?q=" + addressString);
//
//        //Navega de fato para o maps
//        Intent intent = new Intent(Intent.ACTION_VIEW,addressUri);
//        intent.setPackage("com.google.android.apps.maps");
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
//
//    public void goToWeb() {
//        //Cria URI
//        String addressString = "https://www.google.com/";
//        Uri uri = Uri.parse(addressString);
//
//        //Direciona  para o navegador
//        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
//
//
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//
//    /**
//     * Redireciona para camera
//     * Para recuperar a imagem, verificar documentação.
//     * Fonte: https://developer.android.com/training/camera/photobasics.html#TaskCaptureIntent
//     */
//    public void goToCamera() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
//        }
//    }

    public void OK()
    {
        Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show();
        //TODO (12) Substituição do fragmet em tempo de execução.
        if (findViewById(R.id.fragment) != null) {
            TestFragment testFragment =  TestFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, testFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
