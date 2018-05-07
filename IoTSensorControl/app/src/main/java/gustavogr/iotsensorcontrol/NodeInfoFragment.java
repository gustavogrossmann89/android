package gustavogr.iotsensorcontrol;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/*
 * TODO (0) Para criar um novo fragment
 *  1) Clicar com o botão direito nas pastas que contém código
 *  2) New => Fragment => EmptyActivity
 *  3) Fragment Name: NodeInfoFragment
 *     Fragment Layout name: fragment_node_info
 *
 *     Permitem a adição, a remoção e a substituição de fragmentos em uma atividade em tempo de
 *     execução para criar uma UI dinâmica.
 *
 *     Um Fragment representa o comportamento ou uma parte da interface do usuário em um Activity.
 *     É possível combinar vários fragmentos em uma única atividade para compilar uma IU de vários
 *     painéis e reutilizar um fragmento em diversas atividades. Um fragmento é como uma seção
 *     modular de uma atividade, que tem o próprio ciclo de vida, recebe os próprios eventos de
 *     entrada e que pode ser adicionado ou removido com a atividade em execução (uma espécie de
 *     "subatividade" que pode ser reutilizada em diferentes atividades).
 *
 *     Um fragmento deve sempre ser embutido em uma atividade e o ciclo de vida dele é diretamente
 *     impactado pelo ciclo de vida da atividade do host. Por exemplo, quando a atividade é
 *     pausada, todos os fragmentos também são e, quando a atividade é destruída, todos os fragmentos
 *     também são. No entanto, enquanto uma atividade estiver em execução (estiver no estado do ciclo
 *     de vida retomado), é possível processar cada fragmento independentemente, como adicionar ou
 *     removê-los.
 *
 *     Fonte:https://developer.android.com/guide/components/fragments.html?hl=pt-br
 *     https://developer.android.com/training/basics/fragments/fragment-ui.html?hl=pt-br
 */
public class NodeInfoFragment extends Fragment {

    // TODO (5) Cópia dos componentes de node Activity
    TextView nomeTextView;
    TextView localizacaoTextView;
    TextView descricaoTextView;
    Button goToMapsButton;
    Button goToWebButton;
    Button goToCameraButton;

    // TODO (6) Parametros
    String nome;
    String descricao;
    String localizacao;

    public NodeInfoFragment() {
        // Required empty public constructor
    }

    /**
     * TODO (7)
     * Factory utilizada paa criar uma nova instância deste fragment e inicializar os parâmetros.
     * @param nome
     * @param descricao
     * @param localizacao
     * @return
     */
    public static NodeInfoFragment newInstance(String nome, String descricao, String localizacao) {
        NodeInfoFragment fragment = new NodeInfoFragment();
        Bundle args = new Bundle();
        args.putString(Node.NODE_NOME, nome);
        args.putString(Node.NODE_DESCRICAO, descricao);
        args.putString(Node.NODE_LOCALIZACAO, localizacao);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //TODO (8) Recupera parâmetros
            nome = getArguments().getString(Node.NODE_NOME);
            descricao = getArguments().getString(Node.NODE_DESCRICAO);
            localizacao = getArguments().getString(Node.NODE_LOCALIZACAO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_node_info, container, false);
        updateValues(view,nome,descricao,localizacao);
        return view;
    }

    public void updateValues(View view, String nome, String descricao, String localizacao)
    {
        //TODO (9) Inicialização dos TextViews
        nomeTextView = (TextView) view.findViewById(R.id.node_nome);
        nomeTextView.setText(nome);
        localizacaoTextView = (TextView) view.findViewById(R.id.node_localizacao);
        localizacaoTextView.setText(localizacao);
        descricaoTextView = (TextView) view.findViewById(R.id.node_descricao);
        descricaoTextView.setText(descricao);

        //Adição de outras funcionalidades
        goToMapsButton = (Button) view.findViewById(R.id.button_go_to_maps);
        goToMapsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToMaps();
            }
        });
        goToWebButton = (Button) view.findViewById(R.id.button_go_to_web);
        goToWebButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToWeb();
            }
        });
        goToCameraButton = (Button) view.findViewById(R.id.button_go_to_camera);
        goToCameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToCamera();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Implemtentação do método para navegar para o maps.
     * Fonte: https://developers.google.com/maps/documentation/android-api/intents?hl=pt-br
     */
    public void goToMaps() {
        //Cria URI
        String addressString = localizacaoTextView.getText().toString();
        Uri addressUri = Uri.parse("geo:0,0?q=" + addressString);

        //Navega de fato para o maps
        Intent intent = new Intent(Intent.ACTION_VIEW,addressUri);
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void goToWeb() {
        //Cria URI
        String addressString = "https://www.google.com/";
        Uri uri = Uri.parse(addressString);

        //Direciona  para o navegador
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Redireciona para camera
     * Para recuperar a imagem, verificar documentação.
     * Fonte: https://developer.android.com/training/camera/photobasics.html#TaskCaptureIntent
     */
    public void goToCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
}